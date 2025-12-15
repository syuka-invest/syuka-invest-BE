package com.syukaworld.syukainvest.asset.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.time.Instant;

@Getter
@Document(collection = "asset_price")
// MongoDB Time Series Collection 설정 (MongoDB 5.0+)
// timeField: 시간 필드명, metaField: 그룹핑할 메타데이터 필드(assetCode), granularity: 데이터 빈도
@TimeSeries(timeField = "timestamp", metaField = "assetCode", granularity = Granularity.HOURS)
@CompoundIndexes({
    @CompoundIndex(name = "asset_time_idx", def = "{'assetCode': 1, 'timestamp': -1}")
})
public class AssetPrice {

    @Id
    private String id; // Mongo 자동 생성 ID

    @Field("metadata") 
    private String assetCode; // TimeSeries의 metaField

    private Instant timestamp; // TimeSeries의 timeField (UTC)

    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;

    @Builder
    public AssetPrice(String assetCode, Instant timestamp, Double open, Double high, Double low, Double close, Long volume) {
        this.assetCode = assetCode;
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
