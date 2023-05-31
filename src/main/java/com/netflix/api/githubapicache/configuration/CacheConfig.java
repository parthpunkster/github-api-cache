package com.netflix.api.githubapicache.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.api.githubapicache.cache.CacheManager;
import com.netflix.api.githubapicache.cache.CacheService;
import com.netflix.api.githubapicache.github.GitHubApi;
import com.netflix.api.githubapicache.github.GitHubApiImpl;
import com.netflix.api.githubapicache.service.CustomViewService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class CacheConfig {

    @Value("${GITHUB_API_TOKEN:}")
    private String githubApiToken;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper  objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GitHubApi gitHubApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new GitHubApiImpl(restTemplate, githubApiToken, objectMapper);
    }

    @Bean
    public CacheManager cacheManager(GitHubApi gitHubApi, ObjectMapper objectMapper) {
        return new CacheManager(gitHubApi, objectMapper);
    }
}

