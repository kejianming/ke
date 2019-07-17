package com.nfdw.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 获取上下文
 */
public class ContextUtil {

  public static HttpServletRequest getServletRequest(){
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    return request;
  }

  public static HttpSession getSession(){
    return getServletRequest().getSession();
  }

}
