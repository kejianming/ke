package com.nfdw.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nfdw.entity.PageData;
import com.nfdw.util.ObjectUtils;

/**
 * 
 * @Description: 分页配置拦截器(除了LoginRealm.java中引用的service切不了，其他service均可)
 * @Author Ivan Lee
 * @Date 2019年1月7日
 */
@Aspect
@Component
public class PageHelperAspect {
	
	@Pointcut("execution(* com.nfdw.service..*Service.*PageList*(..))||execution(* com.nfdw.*.service..*Service.*PageList*(..))")
	public void pageListPointcut() {
	}
	
	@Around("pageListPointcut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		
		Page<Object> pageObject = null;
		PageData pd = null;
		
		if (null != args && args.length != 0) {
			
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (arg instanceof PageData) {
					pd = (PageData) arg;
					int pageNum = pd.containsKey("page") ? ObjectUtils.toInt(pd.get("page")) : 1;
					int pageSize = pd.containsKey("limit") ? ObjectUtils.toInt(pd.get("limit")) : 10;
					pageObject = PageHelper.startPage(pageNum, pageSize);
					break;
				}
			}
			
		}
		
		Object result = joinPoint.proceed();
		
		if (null != pageObject && null != pd) {
			pd.put("total", pageObject.getTotal());
		}
		
		return result;
		

	}
	
}
