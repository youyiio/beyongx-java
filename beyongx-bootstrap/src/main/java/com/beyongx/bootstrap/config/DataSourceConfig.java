package com.beyongx.bootstrap.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.beyongx.framework.mapper", "com.beyongx.*.mapper"}, sqlSessionFactoryRef = "beyongxSqlSessionFactory", nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class DataSourceConfig {
    // 将这个对象放入Spring容器中
    @Bean(name = "beyongxDataSource")
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.druid.db0")
    public DataSource createDateSource() {
        //return DataSourceBuilder.create().build(); //使用spring的默认连接池
        return DruidDataSourceBuilder.create().build(); //使用了Druid的连接池
    }

    @Bean(name = "beyongxSqlSessionFactory")
    // @Qualifier表示查找Spring容器中名字为commonDataSource的对象
    public SqlSessionFactory createSqlSessionFactory(@Qualifier("beyongxDataSource") DataSource datasource)
            throws Exception {
        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean(); //使用了mybatis
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean(); //使用了mybatis-plus，非常重要！！！
        bean.setDataSource(datasource);
        // 设置mybatis的xml所在位置
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));

        // 指明实体扫描(多个package用逗号或者分号分隔)
        bean.setTypeAliasesPackage("com.beyongx.framework,com.beyongx.system,cn.airsafety.sms");
        // 导入mybatis配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        // 配置打印sql语句
        configuration.setLogImpl(StdOutImpl.class);
        bean.setConfiguration(configuration);
        // 添加分页功能
        bean.setPlugins(new Interceptor[]{mybatisPlusInterceptor()});
        // 导入全局配置
        //bean.setGlobalConfig(globalConfiguration());

        return bean.getObject();
    }

    @Bean("beyongxSqlSessionTemplate")
    public SqlSessionTemplate createSqlSessionTemplate(@Qualifier("beyongxSqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    @Bean(name = "beyongxTransactionManager")
    //@Primary
    public DataSourceTransactionManager createTransactionManager(@Qualifier("beyongxDataSource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    //分页插件
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    public GlobalConfiguration globalConfiguration() {
//
//    }
    /**
     * Mapper接口所在包名，Spring会自动查找其下的Mapper
     *
     * @return mapperScannerConfigurer
     */

//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setBasePackage("**.mapper");
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//
//        return mapperScannerConfigurer;
//    }


}
