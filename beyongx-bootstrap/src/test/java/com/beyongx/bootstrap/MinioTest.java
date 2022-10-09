package com.beyongx.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.beyongx.bootstrap.config.MinioConfig;
import com.beyongx.framework.service.IOssService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioTest {
    
    @Autowired
    private IOssService ossService;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;

    @Test
    public void upload() throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException {
        File file = new File("G:\\桌面电子书\\math-deep.pdf");
        InputStream inputStream = new FileInputStream(file);
        
        System.out.println(minioConfig.getEndpoint() + " " + minioConfig.getBucketName());
        minioClient.putObject(PutObjectArgs.builder()  
                .bucket(minioConfig.getBucketName())
                .object(file.getName())  
                .stream(inputStream, file.length(), -1)  
                //.contentType(file.getContentType())  
                .build());
    }
}
