package com.netflix.api.githubapicache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheService {

    CacheManager cacheManager;
    Map<String, Cache<?>> caches;

    @Value("${cache.refreshDelay:3600000}")
    private long refreshDelay;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.caches = new HashMap<>();
        System.out.println("Creating cache for the first time");
        refreshCache();
    }

    @Scheduled(fixedDelayString = "${cache.refreshDelay}")
    public void refreshCache() {
        System.out.println("Inititate Cache refresh");
        Map<String, Cache<?>> newCaches = cacheManager.refreshCache();
        caches.putAll(newCaches);
        System.out.println("Process completed");
    }

    public List<?> getCachedData(String cacheKey) {
        return caches.get(cacheKey).getCache();
    }
}

