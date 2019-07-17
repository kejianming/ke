package com.nfdw.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

public class WebUtils {

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String x_requested_with = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(x_requested_with)) {
			return true;
		}
		return false;
	}

	/**
	 * 功能：输出文本流到页面
	 * 
	 * @param text
	 *            <code>String</code>
	 */
	public static void writeText(ServletResponse response, String text) {
		try {
			response.setContentType("text/plain;charset=UTF-8");
			Writer writer = response.getWriter();
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	public static String getLocalHttpURL() {
		HttpServletRequest request = getRequest();
		String contextPath = request.getContextPath().equals("/") ? ""
				: request.getContextPath();
		String url = "http://" + request.getServerName();
		int port = request.getServerPort();
		if (port != 80) {
			url = url + ":" + port + contextPath;
		} else {
			url = url + contextPath;
		}
		return url;
	}
	
}
