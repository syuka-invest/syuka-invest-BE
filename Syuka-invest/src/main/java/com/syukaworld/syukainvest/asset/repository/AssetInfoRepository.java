package com.syukaworld.syukainvest.asset.repository;

import com.syukaworld.syukainvest.asset.domain.AssetInfo;
import com.syukaworld.syukainvest.asset.domain.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetInfoRepository extends JpaRepository<AssetInfo, String> {
    
    // 자산 타입으로 목록 조회
    List<AssetInfo> findAllByAssetType(AssetType assetType);
    
    // 이름으로 검색 (포함 검색)
    List<AssetInfo> findByNameContaining(String name);
}
