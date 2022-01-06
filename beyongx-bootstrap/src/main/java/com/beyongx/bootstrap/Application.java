package com.beyongx.bootstrap;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(scanBasePackages = {"com.beyongx"}, exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
// @MapperScan 前往beyongx-bootstrap模块下config/DataSourceConfig.java文件进行配置修改
@ComponentScan(basePackages={"com.beyongx"}, nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
