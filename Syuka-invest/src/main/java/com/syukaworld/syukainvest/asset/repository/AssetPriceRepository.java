package com.syukaworld.syukainvest.asset.repository;

import com.syukaworld.syukainvest.asset.domain.AssetPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AssetPriceRepository extends MongoRepository<AssetPrice, String> {
    
    // 특정 종목의 특정 기간 시세 조회
    List<AssetPrice> findByAssetCodeAndTimestampBetween(String assetCode, Instant start, Instant end);

    // 특정 종목의 최신 시세 조회
    List<AssetPrice> findByAssetCodeOrderByTimestampDesc(String assetCode);
}
