package dev.mfataka.esnzlin.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 11:26
 */
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final var caffeineCacheManager = new CaffeineCacheManager("userProperties");
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .initialCapacity(100)
                .expireAfterAccess(Duration.ofHours(2))
                .recordStats()
        );
        return caffeineCacheManager;
    }
}
