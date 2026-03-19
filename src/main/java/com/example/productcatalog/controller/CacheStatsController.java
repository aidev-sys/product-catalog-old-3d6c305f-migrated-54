package com.example.productcatalog.controller;

import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache/stats")
public class CacheStatsController {

    private final CacheManager cacheManager;
    private final RabbitTemplate rabbitTemplate;

    public CacheStatsController(CacheManager cacheManager, RabbitTemplate rabbitTemplate) {
        this.cacheManager = cacheManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public Map<String, Object> stats() {
        var cache = cacheManager.getCache("products");
        var cacheStats = Map.of(
            "size", cache.getNativeCache().estimatedSize(),
            "hits", 0L,
            "misses", 0L,
            "hitRate", "0%",
            "evictions", 0L
        );

        rabbitTemplate.convertAndSend("cache.stats", cacheStats);

        return cacheStats;
    }
}