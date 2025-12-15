package com.syukaworld.syukainvest.video.repository;

import com.syukaworld.syukainvest.asset.domain.AssetInfo;
import com.syukaworld.syukainvest.video.domain.Video;
import com.syukaworld.syukainvest.video.domain.VideoMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoMentionRepository extends JpaRepository<VideoMention, Long> {

    // 특정 영상의 언급 목록 조회
    List<VideoMention> findByVideo(Video video);

    // 특정 자산이 언급된 기록 조회
    List<VideoMention> findByAssetInfo(AssetInfo assetInfo);
}
