package com.beyongx.bootstrap;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(scanBasePackages = {"com.beyongx", "cn.airsafety"}, exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
// @MapperScan已经使用了DataSourceConfig设置
// @MapperScan(basePackages={"com.beyongx.framework.mapper", "cn.airsafety.sms.mapper"}, nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@ComponentScan(basePackages={"com.beyongx.bootstrap", "com.beyongx.common", "com.beyongx.framework", "cn.airsafety.sms"}, nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
