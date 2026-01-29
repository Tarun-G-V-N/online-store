package com.onlinestore.buy.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ManualChromaConfig {

    @Bean
    public ChromaApi chromaApi(
            @Value("${spring.ai.vectorstore.chroma.base-url}") String baseUrl,
            RestClient.Builder builder,
            ObjectMapper mapper) {

        return new ChromaApi(baseUrl, builder, mapper);
    }
}

