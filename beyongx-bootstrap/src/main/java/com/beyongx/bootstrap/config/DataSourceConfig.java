package com.beyongx.bootstrap.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.beyongx.**.mapper"}, sqlSessionFactoryRef = "beyongxSqlSessionFactory", nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class DataSourceConfig {

    @Value("${mybatis-plus.mapper-locations}")
    private String mapperLocations;
    @Value("${mybatis-plus.type-aliases-package}")
    private String typeAliasesPackage;
    @Value("${mybatis-plus.configuration.map-underscore-to-camel-case}")
    private Boolean mapUnderscoreToCamelCase;
    @Value("${mybatis-plus.configuration.call-setters-on-nulls}")
    private Boolean callSettersOnNulls;

    // ?????????????????????Spring?????????
    @Bean(name = "beyongxDataSource")
    // ??????application.properties??????????????????????????????????????????
    // prefix?????????????????????
    @ConfigurationProperties(prefix = "spring.datasource.druid.db0")
    public DataSource createDateSource() {
        //return DataSourceBuilder.create().build(); //??????spring??????????????????
        return DruidDataSourceBuilder.create().build(); //?????????Druid????????????
    }

    @Bean(name = "beyongxSqlSessionFactory")
    // @Qualifier????????????Spring??????????????????commonDataSource?????????
    public SqlSessionFactory createSqlSessionFactory(@Qualifier("beyongxDataSource") DataSource datasource)
            throws Exception {
        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean(); //?????????mybatis
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean(); //?????????mybatis-plus????????????????????????
        bean.setDataSource(datasource);
        // ??????mybatis???xml????????????
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));

        // ??????????????????(??????package???????????????????????????)
        bean.setTypeAliasesPackage(typeAliasesPackage);
        // ??????mybatis??????
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        configuration.setCallSettersOnNulls(callSettersOnNulls);
        configuration.setCacheEnabled(false);
        // ????????????sql??????
        configuration.setLogImpl(StdOutImpl.class);
        configuration.setObjectWrapperFactory(new com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory());
        bean.setConfiguration(configuration);
        // ??????????????????
        bean.setPlugins(new Interceptor[]{mybatisPlusInterceptor()});
        // ??????????????????
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

    //????????????
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    //mybatis-plus map???????????????map-underscore-to-camel-case
    // @Bean
    // public ConfigurationCustomizer configurationCustomizer() {
    //     return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    // }
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override        
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
            }
        };
    }

    // @Bean
    // public Configuration globalConfiguration() {
    //    return new org.apache.ibatis.session.Configuration();
    // }


}
