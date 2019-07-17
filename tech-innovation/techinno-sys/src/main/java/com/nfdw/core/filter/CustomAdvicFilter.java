package com.nfdw.core.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.nfdw.service.MenuService;
import com.nfdw.service.SysUserService;

/**

 * 自定义拦截器 暂时不用
 */
public class CustomAdvicFilter extends FormAuthenticationFilter {

  @Autowired
  private SysUserService userService;

  @Autowired
  private MenuService menuService;


  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
      return true;
  }
}
