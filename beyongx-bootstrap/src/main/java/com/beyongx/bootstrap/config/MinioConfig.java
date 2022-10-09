package com.beyongx.bootstrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.Data;

@Configuration  
@ConfigurationProperties(prefix = "minio")  
@Data 
public class MinioConfig {
    
    private String accessKey;  
  
    private String secretKey;  
  
    private String endpoint;  
  
    private String bucketName;  
  
    @Bean  
    public MinioClient minioClient() {  
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }  
}
