package com.syukaworld.syukainvest.asset.domain;

import com.syukaworld.syukainvest.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "asset_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class AssetInfo extends BaseEntity {

    @Id
    @Column(name = "asset_code", nullable = false)
    private String assetCode; // 종목 코드 등 고유 식별자 (예: 005930, US10Y)

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType assetType;

    private String currency; // KRW, USD

    @Builder
    public AssetInfo(String assetCode, String name, AssetType assetType, String currency) {
        this.assetCode = assetCode;
        this.name = name;
        this.assetType = assetType;
        this.currency = currency;
    }
}
