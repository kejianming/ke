package com.nfdw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nfdw.util.SpringUtil;

import tk.mybatis.spring.annotation.MapperScan;

/**

 */

@EnableWebMvc
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@ComponentScan({"com.nfdw", "org.activiti"})
@MapperScan(basePackages = {"com.nfdw.mapper", "com.nfdw.*.mapper"})
@EnableAsync
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        new SpringUtil().setApplicationContext(applicationContext);
//    String[] names = applicationContext.getBeanDefinitionNames();
        //1.8 forEach循环
//    Arrays.asList(names).forEach(System.out::println);
        System.out.println("Server start success");
    }


}
