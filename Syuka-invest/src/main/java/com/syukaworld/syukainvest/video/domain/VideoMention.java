package com.syukaworld.syukainvest.video.domain;

import com.syukaworld.syukainvest.asset.domain.AssetInfo;
import com.syukaworld.syukainvest.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "video_mention")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class VideoMention extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_code", nullable = false)
    private AssetInfo assetInfo;

    // 영상 내 언급 시작 시간 (초 단위)
    private Integer startTimeSeconds;
    
    // 언급 종료 시간 (초 단위, 선택)
    private Integer endTimeSeconds;

    // 언급 맥락 요약
    @Column(columnDefinition = "TEXT")
    private String contextSummary;

    // 감성 분석 (POSITIVE, NEGATIVE, NEUTRAL) - 필요 시 Enum으로 변경 가능
    private String sentiment;

    @Builder
    public VideoMention(Video video, AssetInfo assetInfo, Integer startTimeSeconds, Integer endTimeSeconds, String contextSummary, String sentiment) {
        this.video = video;
        this.assetInfo = assetInfo;
        this.startTimeSeconds = startTimeSeconds;
        this.endTimeSeconds = endTimeSeconds;
        this.contextSummary = contextSummary;
        this.sentiment = sentiment;
    }
}
