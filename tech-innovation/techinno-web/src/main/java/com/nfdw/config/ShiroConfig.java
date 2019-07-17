package com.nfdw.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.nfdw.core.filter.PermissionFilter;
import com.nfdw.core.filter.VerfityCodeFilter;
import com.nfdw.core.shiro.LoginRealm;
import com.nfdw.core.shiro.RetryLimitCredentialsMatcher;

/**
 *spring shiro
 */
@Configuration
public class ShiroConfig {

  @Bean
  public RetryLimitCredentialsMatcher getRetryLimitCredentialsMatcher(){
//    RetryLimitCredentialsMatcher rm = new RetryLimitCredentialsMatcher(getCacheManager(),2);
    RetryLimitCredentialsMatcher rm = new RetryLimitCredentialsMatcher(getCacheManager());
    rm.setHashAlgorithmName("md5");
    rm.setHashIterations(4);
    return rm;

  }
  @Bean(name = "loginRealm")
  public LoginRealm getLoginRealm(){
    LoginRealm realm= new LoginRealm();
    realm.setCredentialsMatcher(getRetryLimitCredentialsMatcher());
    return realm;
  }

  @Bean
  public EhCacheManager getCacheManager(){
    EhCacheManager ehCacheManager=new EhCacheManager();
    ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache.xml");
    return ehCacheManager;
  }

  @Bean
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean(name="securityManager")
  public SecurityManager getSecurityManager(@Qualifier("loginRealm") LoginRealm loginRealm){
    DefaultWebSecurityManager dwm=new DefaultWebSecurityManager();
    dwm.setRealm(loginRealm);
    dwm.setCacheManager(getCacheManager());
    dwm.setSessionManager(defaultWebSessionManager());
    return dwm;
  }

  @Bean
  public PermissionFilter getPermissionFilter(){
    PermissionFilter pf=new PermissionFilter();
    return pf;
  }

  @Bean
  public VerfityCodeFilter getVerfityCodeFilter(){
    VerfityCodeFilter vf= new VerfityCodeFilter();
    vf.setFailureKeyAttribute("shiroLoginFailure");
    vf.setJcaptchaParam("code");
    vf.setVerfitiCode(true);
    return vf;
  }

  @Bean(name = "shiroFilter")
  public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager){
    ShiroFilterFactoryBean sfb = new ShiroFilterFactoryBean();
    sfb.setSecurityManager(securityManager);
    sfb.setLoginUrl("/login");
    sfb.setUnauthorizedUrl("/goLogin");
    Map<String, Filter> filters=new HashMap<>();
    filters.put("per",getPermissionFilter());
    filters.put("verCode",getVerfityCodeFilter());
    sfb.setFilters(filters);
    Map<String, String> filterMap = new LinkedHashMap<>();
    filterMap.put("/login","verCode,anon");
    //filterMap.put("/login","anon");
    filterMap.put("/getCode","anon");
    filterMap.put("/applogin","anon");
    filterMap.put("/equipment/checkDetectionReport","anon");
    filterMap.put("/card/checkMonthReport","anon");
    filterMap.put("/blog/**","anon");
    filterMap.put("/logout","logout");
    filterMap.put("/plugin/**","anon");
    filterMap.put("/user/**","per");
    filterMap.put("/**","authc");
    sfb.setFilterChainDefinitionMap(filterMap);
    return sfb;
  }

  @Bean
  public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    advisorAutoProxyCreator.setProxyTargetClass(true);
    return advisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
    AuthorizationAttributeSourceAdvisor as=new AuthorizationAttributeSourceAdvisor();
    as.setSecurityManager(securityManager);
    return as;
  }

  @Bean
  public DefaultWebSessionManager defaultWebSessionManager() {
    DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
    defaultWebSessionManager.setSessionIdCookieEnabled(true);
    
    // session失效时间 1h
    defaultWebSessionManager.setGlobalSessionTimeout(1000*60*60);
    defaultWebSessionManager.setDeleteInvalidSessions(true);
    defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
    defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
    return defaultWebSessionManager;
  }
/*
  @Bean
  public FilterRegistrationBean delegatingFilterProxy(){
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
    proxy.setTargetFilterLifecycle(true);
    proxy.setTargetBeanName("shiroFilter");

    filterRegistrationBean.setFilter(proxy);
    return filterRegistrationBean;
  }*/


}
