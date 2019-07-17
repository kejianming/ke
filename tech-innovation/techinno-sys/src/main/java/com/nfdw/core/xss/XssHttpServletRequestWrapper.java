package com.nfdw.core.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;

import com.nfdw.util.StringUtils;

/**
 * 
 * @Description: 跨站请求防范，防止XSS攻击
 * @Author Ivan Lee
 * @Date 2019年3月19日
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

//	@Override
//	public String getHeader(String name) {
//		String value = super.getHeader(name);
//		return HtmlUtils.htmlEscape(value);
//	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		
        if (!StringUtils.isBlank(value)) {
            value = HtmlUtils.htmlEscape(value);
        }
		
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		
		String[] values = super.getParameterValues(name);
		
		if (values != null) {
			
			for (int i = 0; i < values.length; i++) {
				values[i] = HtmlUtils.htmlEscape(values[i]);
			}
			
		}
		
		return values;
	}

}
