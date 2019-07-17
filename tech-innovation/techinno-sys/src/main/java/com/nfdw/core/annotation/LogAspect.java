package com.nfdw.core.annotation;

import com.alibaba.fastjson.JSON;
import com.nfdw.core.shiro.ShiroUtil;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.SysLog;
import com.nfdw.mapper.SysLogMapper;
import com.nfdw.util.IpUtil;

import org.apache.shiro.UnavailableSecurityManagerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**

 * 为增删改添加监控
 */
@Aspect
@Component
@Order(1)
public class LogAspect {

    @Autowired
    private SysLogMapper logMapper;

    @Pointcut("@annotation(com.nfdw.core.annotation.Log)")
    private void pointcut() {

    }

    @Around(value = "pointcut() && @annotation(myLog)")
    public Object around(ProceedingJoinPoint point, Log myLog) throws Throwable {

        System.out.println("++++执行了around方法++++");

        String requestUrl = myLog.desc();

        //拦截的类名
        Class clazz = point.getTarget().getClass();
        //拦截的方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        System.out.println("执行了 类:" + clazz + " 方法:" + method + " 自定义请求地址:" + requestUrl);

        return point.proceed(); //执行程序

    }

    /**
     * 正常执行则保持日志
     * @param jp
     */
    @AfterReturning("pointcut()")
    public void insertLogSuccess(JoinPoint jp) {
        addLog(jp, getDesc(jp));
    }

    private void addLog(JoinPoint jp, String text) {
        Log.LOG_TYPE type = getType(jp);
        SysLog log = new SysLog();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //一些系统监控
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IpUtil.getIp(request);
            log.setIp(ip);
        }
        log.setCreateTime(new Date());
        log.setType(type.toString());
        log.setText(text);

        Object[] obj = jp.getArgs();
        StringBuffer buffer = new StringBuffer();
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                buffer.append("[参数" + (i + 1) + ":");
                Object o = obj[i];
                if(o instanceof Model){
                    continue;
                }
                String parameter=null;
                try {
                    parameter=JSON.toJSONString(o);
                }catch (Exception e){
                    continue;
                }
                buffer.append(parameter);
                buffer.append("]");
            }
        }
        log.setParam(buffer.toString());
        try {
            CurrentUser currentUsr = ShiroUtil.getCurrentUse();
            if (currentUsr !=null) {
            	 log.setUserName(currentUsr.getUsername());
            }
        } catch (UnavailableSecurityManagerException e) {

        }
        logMapper.insert(log);
    }

    private String getDesc(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).desc();
    }

    private Log.LOG_TYPE getType(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).type();
    }
}

