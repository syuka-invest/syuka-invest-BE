# Backend Development Plan (백엔드 개발 계획)

## 1. 개요 (Overview)
슈카 인베스트(Syuka-Invest) 백엔드는 유튜브 영상 데이터를 수집/분석하고 주가 정보를 매핑하여 프론트엔드에 제공하는 핵심 로직을 담당합니다. Spring Boot 기반으로 안정적인 데이터 파이프라인과 REST API를 구축합니다.

## 2. 개발 단계 (Phases)

### Phase 1: 도메인 및 DB 스키마 설계 <!-- id: p1 -->
핵심 엔티티 정의 및 JPA/NoSQL 설계.
- [ ] **엔티티 설계 (Entities)**
    - `Video` (영상): id, title, publishedAt, thumbnail 등
    - `Transcript` (자막): id, video_id, text_content (MongoDB)
    - `Mention` (언급): video_id, asset_id, start_time, end_time, summary
    - `AssetInfo` (자산 메타): id(code), name, type(STOCK, BOND, INDEX, RATE), currency
    - `AssetPrice` (자산 시세): asset_id, date, open, high, low, close, value (MongoDB Time-Series)
- [ ] **유연한 자산 구조 설계**
    - 주식 뿐만 아니라 금리, 환율, 지수까지 포괄할 수 있는 추상화된 모델링 (`Asset` 인터페이스 활용)
- [ ] **ERD 작성 및 검토**
- [ ] **JPA Repository 생성**

### Phase 2: 외부 API 연동 모듈 (Infrastructure) <!-- id: p2 -->
외부 서비스와의 통신을 담당하는 Client 구현.
- [ ] **YouTube API Client**
    - 채널 최신 영상 리스트 조회 기능
    - 영상 자막(Caption) 다운로드 기능
- [ ] **LLM Client (OpenAI)**
    - 자막 텍스트 -> JSON 파싱 프롬프트 구현
    - 키워드(기업명, 경제지표) 추출 로직 고도화
- [ ] **Market Data Provider (Strategy Pattern)**
    - `StockDataProvider` (KIS API): 국내 주식
    - `MacroDataProvider` (FRED or Yahoo Finance): 금리, 환율, 원자재 등
    - 자산 타입(Type)에 따라 적절한 Provider가 선택되도록 구현

### Phase 3: 비즈니스 로직 구현 (Core Service) <!-- id: p3 -->
데이터를 가공하고 저장하는 핵심 서비스 계층.
- [ ] **Collector Service**
    - 스케줄러(Batch)를 통한 주기적 신규 영상 수집
- [ ] **Analysis Service**
    - LLM 응답을 파싱하여 `Mention` 데이터로 변환/저장
- [ ] **Asset Data Service (On-Demand Loading)**
    - **Lazy Loading 전략**: 모든 종목을 미리 저장하지 않음.
    - 영상 분석에서 추출된 '자산'이나 사용자가 조회한 '자산'에 대해서만 DB 존재 여부 확인.
    - DB에 없거나 데이터가 오래된 경우에만 외부 API(KIS 등)를 호출하여 업데이트(Upsert).
    - 시계열 데이터(Price/Rate)는 **MongoDB Time-Series Collection**에 저장하여 성능 최적화.

### Phase 4: API 엔드포인트 개발 (Controller) <!-- id: p4 -->
프론트엔드와 통신할 REST API.
- [ ] **VideoController**
    - `GET /api/v1/videos`: 분석된 영상 목록
    - `GET /api/v1/videos/{id}`: 영상 상세 및 핀(Pin) 리스트
- [ ] **AssetController**
    - `GET /api/v1/assets/{id}/chart`: 자산 유형(주식/금리)에 맞는 차트 데이터 반환
    - `GET /api/v1/assets/{id}/mentions`: 해당 자산이 언급된 영상 리스트

### Phase 5: 테스트 및 배포 (Test & Deploy) <!-- id: p5 -->
- [ ] **단위 테스트 (JUnit)**
- [ ] **통합 테스트**
- [ ] **CI/CD 파이프라인 구축**

## 3. 기술 스택 상세 (Tech Stack Details)
- **Framework**: Spring Boot 3.3.x (Java 17/24)
- **Persistence**: 
    - **MySQL (JPA)**: Video, Mention, AssetInfo (관계형 데이터)
    - **MongoDB**: AssetPrice (시계열 데이터), Transcript (대용량 텍스트)
- **Data Strategy**: Polyglot Persistence (RDB + NoSQL)
- **Build Tool**: Gradle
- **Testing**: JUnit5, Mockito
