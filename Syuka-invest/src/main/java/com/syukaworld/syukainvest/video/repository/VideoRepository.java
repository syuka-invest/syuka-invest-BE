package com.syukaworld.syukainvest.video.repository;

import com.syukaworld.syukainvest.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    // Youtube Video ID로 조회
    Optional<Video> findByYoutubeVideoId(String youtubeVideoId);

    // 게시일 기준 내림차순 정렬 조회
    List<Video> findAllByOrderByPublishedAtDesc();
    
    // 기간 내 영상 조회
    List<Video> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end);
}
