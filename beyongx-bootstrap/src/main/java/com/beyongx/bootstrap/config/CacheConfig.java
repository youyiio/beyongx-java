package com.beyongx.bootstrap.config;

import java.lang.reflect.Method;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
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
public class CacheConfig extends CachingConfigurerSupport {
    
    @Value("${spring.redis.expire}")
    private int expire;

    public interface CacheManagerNames {
        String REDIS_CACHE_MANAGER = "redisCacheManager";
        String EHCACHE_CACHE_MANAGER = "ehCacheManager";
    }

    @Autowired
    private RedisConnectionFactory factory;

    @Bean(name = CacheManagerNames.REDIS_CACHE_MANAGER)
    @Primary
    @Override
    public CacheManager cacheManager() {    

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

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
                @Override
                public Object generate(Object target, Method method, Object... params) {
                    StringBuilder sb = new StringBuilder();
                    //sb.append(target.getClass().getName());
                    sb.append(method.getName());
                    sb.append("-");
                    if (params.length > 0) {
                        // 参数值
                        for (Object object : params) {
                            if (isBaseType(object.getClass())) {
                                sb.append(object);
                            } else {
                                sb.append(object.hashCode());
                            }
                            sb.append("_");
                        }

                        sb.substring(0, sb.length() - 2);
                    } else {
                        sb.append("void");
                    }

                    return sb.toString();
                }

                /**
                * 判断object是否为基本类型
                * @param object
                * @return
                */
                public boolean isBaseType(Object object) {
                    Class className = object.getClass();
                    if (className.equals(java.lang.Integer.class) ||
                        className.equals(java.lang.Byte.class) ||
                        className.equals(java.lang.Long.class) ||
                        className.equals(java.lang.Double.class) ||
                        className.equals(java.lang.Float.class) ||
                        className.equals(java.lang.Character.class) ||
                        className.equals(java.lang.Short.class) ||
                        className.equals(java.lang.Boolean.class)) {
                        return true;
                    }
                    return false;
                }
        };
    }

    private RedisSerializer<Object> createJackson2JsonRedisSerializer() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        
        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //设置value的序列化器
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }
}
