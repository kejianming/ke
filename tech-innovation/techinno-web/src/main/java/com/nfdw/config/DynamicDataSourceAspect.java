package com.nfdw.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.nfdw.TargetDataSource;
import com.nfdw.holder.DataSourceContextHolder;

import java.lang.reflect.Method;

/**
 * 编写AOP切面，实现切换逻辑：
 * @author
 * @date 
 */
@Aspect
@Order(2)
@Component
public class DynamicDataSourceAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
	
	@Pointcut("execution(* com.nfdw.service..*Service.*(..))||execution(* com.nfdw.*.service..*Service.*(..))")
	public void dataSourcePointCut() {
		System.out.println("dataSourcePointCut service");
	}
	
    @Before("dataSourcePointCut()")
    public void before(JoinPoint point){

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();

        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);

            // 判断是否存在@TargetDataSource注解
            if (null != method && method.isAnnotationPresent(TargetDataSource.class)) {
            	TargetDataSource annotation = method.getAnnotation(TargetDataSource.class);
                // 取出注解中的数据源名
            	DataSourceContextHolder.setDB(annotation.value().getName());
            	LOGGER.info("》》》》》》》 current thread " + Thread.currentThread().getName() + " add 【 " + annotation.value().getName() + " 】 to ThreadLocal");
            } else {
            	LOGGER.info("》》》》》》》 use default datasource");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After("dataSourcePointCut()")
    public void afterSwitchDS(JoinPoint point){

        DataSourceContextHolder.clearDB();

    }
}