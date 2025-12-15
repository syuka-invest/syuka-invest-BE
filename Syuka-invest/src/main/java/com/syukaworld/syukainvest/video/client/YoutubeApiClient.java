package com.syukaworld.syukainvest.video.client;

import java.util.List;

public interface YoutubeApiClient {
    // 영상 검색 (키워드 기반)
    // TODO: Define return DTOs
    // List<VideoDto> searchVideos(String keyword);
    
    // 특정 영상 상세 정보 조회
    // VideoDetailDto getVideoDetail(String videoId);
    
    // 채널의 최신 영상 조회
    // List<VideoDto> getChannelLatestVideos(String channelId);
    
    // 영상의 자막(Captions) 조회 (별도 라이브러리나 API 필요 가능성 있음)
    String getVideoScript(String videoId);
}
