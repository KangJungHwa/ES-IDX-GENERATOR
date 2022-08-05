package com.dd.elk.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticSearchClientConfig {
    @Value("${elasticsearch.host}")
    String host;

    @Value("${elasticsearch.port}")
    int port;

    @Bean("client")
    public  RestHighLevelClient create() {
       return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http")));
    }

}
