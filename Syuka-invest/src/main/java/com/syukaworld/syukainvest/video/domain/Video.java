package com.syukaworld.syukainvest.video.domain;

import com.syukaworld.syukainvest.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "video")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Video extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String youtubeVideoId; // YouTube 영상 고유 ID

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    private String thumbnailUrl;
    
    private Long viewCount;

    @Builder
    public Video(String youtubeVideoId, String title, LocalDateTime publishedAt, String thumbnailUrl, String description) {
        this.youtubeVideoId = youtubeVideoId;
        this.title = title;
        this.publishedAt = publishedAt;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }
}
