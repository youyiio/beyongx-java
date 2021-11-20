package com.beyongx.bootstrap.config;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {
    
    @Value("${spring.redis.expire}")
    private int expire;

    public interface CacheManagerNames {
        String REDIS_CACHE_MANAGER = "redisCacheManager";
        String EHCACHE_CACHE_MANAGER = "ehCacheManager";
    }

    @Bean(name = CacheManagerNames.REDIS_CACHE_MANAGER)
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory factory) {    

        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expire))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(createJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
 
        return new RedisCacheManager(cacheWriter, config);
    }

    // @Bean(name = CacheManagerNames.EHCACHE_CACHE_MANAGER)
    // public EhCacheCacheManager ehCacheManager() {
    //     Resource resource = this.cacheProperties.getEhcache().getConfig();
    //     resource = this.cacheProperties.resolveConfigLocation(resource);
    //     EhCacheCacheManager ehCacheManager = new EhCacheCacheManager(
    //             EhCacheManagerUtils.buildCacheManager(resource)
    //     );
    //     ehCacheManager.afterPropertiesSet();
    //     return ehCacheManager;
    // }

    private RedisSerializer<Object> createJackson2JsonRedisSerializer() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //设置value的序列化器
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }
}
