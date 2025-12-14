# Backend Infrastructure & Environment Plan

## 1. 데이터베이스 운영 전략 (Database Strategy)

비용 효율성과 데이터 안정성을 모두 확보하기 위해, 운영(Production)과 개발(Development) 환경의 DB를 논리적으로 분리하여 운영합니다.

| 서비스 | 운영 환경 (Production) | 개발 환경 (Development) | 비고 |
| :--- | :--- | :--- | :--- |
| **MySQL** | **AWS RDS** (t3.micro) <br> Schema: `syuka_prod` | **AWS RDS** (동일 인스턴스) <br> Schema: `syuka_dev` | 하나의 인스턴스에서 스키마로 격리하여 비용 절감 |
| **MongoDB** | **MongoDB Atlas** (Cluster) <br> Database: `syuka_prod` | **MongoDB Atlas** (동일 클러스터) <br> Database: `syuka_dev` | 하나의 클러스터에서 DB명으로 격리 |
| **Redis** | **AWS ElastiCache** (t3.micro) | **Local Docker** (EC2 내부) | 개발환경은 비용 절감을 위해 Docker 컨테이너 사용 권장 |

> **Note**: Redis의 경우 `ElastiCache` 프리티어(t3.micro 1대)를 사용한다면 운영 서버 전용으로 할당하고, 개발 서버는 EC2 내부에 Docker로 띄우는 것이 안전하고 경제적입니다.

---

## 2. Spring Boot Profile 전략

Spring Boot의 Profile 기능을 사용하여 환경별 설정을 완벽하게 분리합니다.

### 2.1 Profile 구성
*   `local`: 로컬 컴퓨터 개발용 (DB도 로컬 Docker 사용)
*   `dev`: 개발 서버용 (AWS RDS `syuka_dev` 연결)
*   `prod`: 운영 서버용 (AWS RDS `syuka_prod` 연결)

### 2.2 설정 파일 구조
```text
src/main/resources/
├── application.yml          # 공통 설정 (기본값)
├── application-local.yml    # 로컬 개발용 (localhost DB)
├── application-dev.yml      # 개발 서버용 (AWS Dev DB)
└── application-prod.yml     # 운영 서버용 (AWS Prod DB)
```

### 2.3 실행 방법
*   **로컬**: IntelliJ 등에서 `Active profiles: local` 설정 또는 `./gradlew bootRun --args='--spring.profiles.active=local'`
*   **서버**: `java -jar -Dspring.profiles.active=prod app.jar`

---

## 3. 환경 변수 (.env) 및 Docker 계획

보안이 필요한 민감 정보(DB 비밀번호, API 키)는 코드에 포함하지 않고 환경 변수로 주입합니다.

### 3.1 관리할 환경 변수 목록 (예시)
| 변수명 | 설명 |
| :--- | :--- |
| `MYSQL_URL` | MySQL 접속 URL (스키마 포함) |
| `MYSQL_USER` / `PASSWORD` | MySQL 계정 정보 |
| `MONGO_URI` | MongoDB 접속 Connection String |
| `REDIS_HOST` / `PORT` | Redis 접속 정보 |
| `WISDOM_API_KEY` | 외부 API 키 등 |

### 3.2 서버별 .env 파일 관리
각 서버(EC2)의 홈 디렉토리나 배포 폴더에 `.env` 파일을 **직접 생성**하여 관리합니다. 이 파일은 Git에 **절대 커밋하지 않습니다.**

**EC2 인스턴스 내부 예시 (`/home/ubuntu/syuka-backend/.env`):**
```env
SPRING_PROFILES_ACTIVE=prod
MYSQL_URL=jdbc:mysql://syuka-db.xxx.ap-northeast-2.rds.amazonaws.com:3306/syuka_prod
MYSQL_USER=admin
MYSQL_PASSWORD=very_secret_password
MONGO_URI=mongodb+srv://...
REDIS_HOST=syuka-cache.xxx.cache.amazonaws.com
```

### 3.3 Docker 실행 계획
`docker-compose`나 `Dockerfile`에서 이 `.env` 파일을 로드하도록 설정합니다.

**Prod용 실행 커맨드 (예시):**
```bash
# .env 파일을 읽어서 컨테이너에 환경변수로 주입
docker run -d \
  --env-file .env \
  -p 8080:8080 \
  --name syuka-backend \
  syuka-backend:latest
```

**docker-compose.yml (서버 배포용):**
```yaml
version: '3.8'
services:
  backend:
    image: ghcr.io/kimjeongyeon/syuka-backend:latest
    env_file:
      - .env  # 같은 경로의 .env 파일을 자동으로 로드
    ports:
      - "8080:8080"
```

## 4. Spring Boot Profile 구성 방식 비교

프로필 설정을 하나의 `application.yml` 파일 안에서 `---` (문서 구분자)를 이용해 나누는 방식(Multi-Document)과, 파일 자체를 물리적으로 분리하는 방식(Multi-File)의 장단점을 비교합니다.

| 항목 | **단일 파일 방식** (Multi-Document) | **파일 분리 방식** (Multi-File) **(추천)** |
| :--- | :--- | :--- |
| **구성** | `application.yml` 하나에 `---`로 구분하여 `local`, `dev`, `prod` 기술 | `application.yml`, `application-local.yml` 등으로 파일 분리 |
| **가독성** | 파일이 길어지면 어떤 프로필 설정인지 파악하기 어려움 | 파일별로 목적이 명확하여 가독성이 좋음 |
| **보안** | `prod` 설정이 `application.yml`에 포함되어 노출 위험 높음 | `.gitignore`로 민감한 파일(예: `application-secret.yml`) 배제 가능 |
| **협업** | 동시에 여러 환경 설정 수정 시 git 충돌(conflict) 자주 발생 | 서로 다른 파일 수정 시 충돌 가능성 낮음 |
| **결론** | 설정이 매우 간단한 토이 프로젝트에 적합 | **실무 및 중대형 프로젝트에 적합**하며 본 프로젝트에 권장됨 |

### 4.1 이번 프로젝트의 선택
이번 프로젝트는 **보안**(운영 설정 분리)과 **가독성**을 위해 **파일 분리 방식**을 채택했습니다.
- `application.yml`: 모든 환경 공통 설정
- `application-local.yml`: 로컬 개발용 인메모리/도커 DB 설정
- `application-prod.yml`: 실제 운영 DB 연결 정보 (환경변수로 주입)
