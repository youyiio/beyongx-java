package com.beyongx.framework.config;

import com.beyongx.framework.shiro.ShiroFilter;
import com.beyongx.framework.shiro.ShiroRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ShiroConfig.class);

    //从配置文件里面读取是否需要启动登录认证的开关，默认true
    //@Value("${jwt.auth}")
    private boolean auth = true;

    //jwt验证例外列表
    private String[] jwtActionExcludes = {
        "/api/sign/login", //登录
        "/api/sign/register", //注册
        "/api/sms/sendcode", //发送验证码
        "/api/sms/login" //短信登录
    };

    //配置拦截器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义过滤器链
        Map<String, javax.servlet.Filter> filters = new HashMap<>();
        //指定拦截器处理
        filters.put("jwt", new ShiroFilter());
        shiroFilterFactoryBean.setFilters(filters);

        //设置拦截范围
        Map<String, String> filterMap = new LinkedHashMap<>();
        //设置jwt不拦截的路径
        for (String action : jwtActionExcludes) {
            filterMap.put(action, "anon");
        }

        //拦截所有接口请求，做权限判断
        //启用认证
        String openAuth = auth ? "jwt" : "anon";
        filterMap.put("/**", openAuth);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        logger.info("Shiro拦截器工厂类注入成功");

        return shiroFilterFactoryBean;
    }

    // SecurityManager 安全管理器；Shiro的核心
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean("lifecycleBeanPostProcessor")
    //管理shiro生命周期
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //Shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
