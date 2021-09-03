package com.modyo.pokedex.infrastructure.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                                     @Value("${pokeapi.timeout.connect:5000}") Integer connectTimeout,
                                     @Value("${pokepai.timeout.connection-request:5000}") Integer connectionRequestTimeout,
                                     @Value("${pokeapi.timeout.socket:5000}") Integer socketTimeout) {
        return restTemplateBuilder
                .requestFactory(() -> getRequestFactory(connectTimeout, connectionRequestTimeout, socketTimeout))
                .build();
    }

    private ClientHttpRequestFactory getRequestFactory(Integer connectTimeout, Integer requestTimeout,
                                                       Integer socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
