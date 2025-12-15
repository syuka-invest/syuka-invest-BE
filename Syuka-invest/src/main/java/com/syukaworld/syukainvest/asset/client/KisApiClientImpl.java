package com.syukaworld.syukainvest.asset.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KisApiClientImpl implements KisApiClient {

    @Value("${api.kis.appkey}")
    private String appKey;

    @Value("${api.kis.appsecret}")
    private String appSecret;

    @Value("${api.kis.base-url}")
    private String baseUrl;

    private final RestClient restClient = RestClient.create();

    // 토큰 관리 로직 필요
}
