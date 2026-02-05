package com.imi.mcsrapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient mcsrWebClient() {
        return WebClient.builder()
                .baseUrl("https://mcsrranked.com/api")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
