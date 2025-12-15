package com.syukaworld.syukainvest.video.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class YoutubeApiClientImpl implements YoutubeApiClient {

    @Value("${api.youtube.key}")
    private String apiKey;
    
    private final RestClient restClient = RestClient.create();
    private static final String YOUTUBE_API_BASE_URL = "https://www.googleapis.com/youtube/v3";

    @Override
    public String getVideoScript(String videoId) {
        // Implementation for retrieving captions/transcripts.
        // YouTube Data API does not directly provide captions in a simple way for arbitrary videos without OAuth usually.
        // For MVP, might need to rely on unofficial libraries or just metadata first.
        return "";
    }
}
