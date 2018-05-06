package com.boot.shiro.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean // 注入ShiroFilterFactoryBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        /*
         1. 定义shrioFactoryBean
         2. 设置SecurityManager
         3. 配置拦截器
         4. 返回ShiroFactoryBean
         */
        // 1. 定义shrioFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 2.设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 3. 配置拦截器, 因为LinkedHashMap是有序的，shiro会根据添加的顺序进行拦截
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        // 配置退出,过滤器是由shiro实现的
        filterChainMap.put("/logout", "logout");
        filterChainMap.put("/403", "anon");
        //允许favicon.ico可以匿名访问（anon）
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/js/**", "anon"); // 可匿名访问到js文件
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/img/**", "anon");
        // authc所有的url都必须认证通过
        filterChainMap.put("/**", "authc");
        // 设置登录的URL
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 设置成功后跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 设置一个未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        // 4. 返回ShiroFactoryBean
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(MyShiroRealm myShiroRealm, RedisCacheManager cacheManager, DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(myShiroRealm);
        defaultSecurityManager.setCacheManager(cacheManager);
        defaultSecurityManager.setSessionManager(sessionManager);
        return defaultSecurityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        // 定义密码加密方式，设置加密器
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return myShiroRealm;
    }

    // 加密器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 指定加密算法
        hashedCredentialsMatcher.setHashIterations(2); // 指定 迭代次数
        return hashedCredentialsMatcher;
    }

    // 开启shiro aop注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return attributeSourceAdvisor;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        redisManager.setExpire(1800);// 配置缓存过期时间 单位：秒
        redisManager.setTimeout(2000);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setGlobalSessionTimeout(1800000); // 默认也是30分钟
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public SimpleMappingExceptionResolver resolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
        resolver.setExceptionMappings(properties);
        return resolver;
    }
}
