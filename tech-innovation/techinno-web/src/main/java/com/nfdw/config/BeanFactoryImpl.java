package com.nfdw.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

/**

 */
@Configuration
public class BeanFactoryImpl implements BeanFactoryAware {


  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    System.out.println("BeanFactoryAware------->"+beanFactory);
  }
}
