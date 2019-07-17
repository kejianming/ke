package com.nfdw.config;

import java.util.Arrays;
import java.util.Properties;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**

 * 事务不能忘。。。
 */
@Configuration
public class TransactionalConfig {

  private static final String  PROPAGATION_REQUIRED="PROPAGATION_REQUIRED,-Throwable";
  private static final String  PROPAGATION_REQUIRED_READ="PROPAGATION_REQUIRED,-Throwable,readOnly";
  private static final String[]  REQUIRED_RULE_TRANSACTION={"insert*","add*","update*","del*","create*","save*","upload*"};
//  private static final String[]  REQUIRED_RULE_TRANSACTION={"insert*","update*","del*","create*"};
  private static final String[]  READ_RULE_TRANSACTION={"select*","get*","count*","find*"};

  /**
   *aop
   * @param platformTransactionManager 自动注入 无需手动
   * @return
   */
  @Bean(name="transactionInterceptor")
  public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
    TransactionInterceptor interceptor = new TransactionInterceptor();
    Properties properties = new Properties();
    for(String s:REQUIRED_RULE_TRANSACTION){
      properties.setProperty(s, PROPAGATION_REQUIRED);
    }
    for(String s:READ_RULE_TRANSACTION){
      properties.setProperty(s, PROPAGATION_REQUIRED_READ);
    }
    interceptor.setTransactionManager(platformTransactionManager);
    interceptor.setTransactionAttributes(properties);
    return interceptor;
  }
  @Bean
    public BeanNameAutoProxyCreator getBeanNameAutoProxyCreator(){
    BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
//    proxyCreator.setIncludePatterns(Arrays.asList("execution(public * com.nfdw.service..*Service.*(..))||execution(public *  com.nfdw.*.service..*Service.*(..))"));
      proxyCreator.setBeanNames("*ServiceImpl");
//      proxyCreator.setBeanNames("*ServiceImpl","*Controller");
      proxyCreator.setInterceptorNames("transactionInterceptor");
      proxyCreator.setOrder(2);
      return proxyCreator;
  }


}
