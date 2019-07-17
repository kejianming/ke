package com.nfdw.base.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.nfdw.common.Constants;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.PageData;
import com.nfdw.entity.SysUser;
import com.nfdw.pojo.ResuleBean;
import com.nfdw.util.DateUtils;
import com.nfdw.util.JSONUtils;
import com.nfdw.util.ServiceUtil;
import com.nfdw.util.StringUtils;
import com.nfdw.util.WebUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**

 */
@Slf4j
public abstract class BaseController<T> {

    private PageData pd =null;
	public static final String RSP_CODE = "RSPCODE";
	public static final String RSP_MSG = "RSPMSG";
	
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        if (isAjaxRequest(request)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("code", "403");
            map.put("message", "无权限");
            return JSON.toJSONString(map);
        } else {
            String message = "权限不足";
            try {
                message = URLEncoder.encode(message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("BaseController：" + e.getMessage());
                e.printStackTrace();
            }
            return "redirect:/error/403?message=" + message;
        }
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");
    }
    /**
     * 得到PageData
     */
    public PageData getPageData(){
        String urseid = getCurrentUserId();
        String regionid  =getDepartmentId();
        String username =getUserName();
        pd = new PageData(this.getRequest());
        pd.put(Constants.LASTUDATE, DateUtils.getCurDate());
        pd.put(Constants.DayOfDate,DateUtils.getDayOfDate());
        pd.put(Constants.LOGIN_RECORD_ID,urseid);
        pd.put(Constants.LOGIN_REGION_ID,regionid);

        if (!"".equals(regionid) && !"0".equals(regionid)) {
            // 不是市级登录的用户，则默认只查该用户所在区的数据
            pd.put(Constants.LOGIN_USER_REGION_ID,regionid);
        }

        pd.put(Constants.USRE_NAME,username);
        return pd;
    }
    
    /**
     * 得到PageData(分页查询使用)
     */
    public PageData getPageData(String page, String limit){

        this.getPageData();

        if (StringUtils.isNotBlank(page) && StringUtils.isNotBlank(limit)) {
        	pd.put("page", page);
        	pd.put("limit", limit);
        }
    	
        return pd;
    }
    
    /**
     * 得到分页列表的信息
     */
    public Page getPage(){
        return new Page();
    }
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    protected String generateJsonPageData(Page<PageData> data) throws JSONException {
        JSONArray jsonArray =new JSONArray();
        String json = jsonArray.put(data).get(0).toString();
        return json;
    }
    protected String generateJson(PageData data) throws JSONException {
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        jsonArray.add(data);
        String json = jsonArray.toString();
        return json;
    }
    protected String generateJsonlist(List<PageData> data) throws JSONException {
        JSONArray jsonArray =new JSONArray();
        String json = jsonArray.put(data).get(0).toString();
        return json;
    }
    public String getCurrentUserId(){
        PageData pd= new PageData();
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CurrentUser user = ServiceUtil.getCurrentUser();
        if(user != null){
            return user.getId();
        }
        return null;
    }
    public String getDepartmentId(){
        PageData pd= new PageData();
        CurrentUser user = ServiceUtil.getCurrentUser();
        if(user != null){
            return user.getGzarea();
        }
        return null;
    }
    public String getUserName(){
        PageData pd= new PageData();
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CurrentUser user = ServiceUtil.getCurrentUser();
        if(user != null){
            return user.getUsername();
        }
        return null;
    }
    public Session getSession(){
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = null;
        if(currentSubject != null){
            session = currentSubject.getSession();
        }
        return session;
    }
    public Object getFromSession(Object key){
        Session session = getSession();
        Object value = null;
        if(session != null){
            value = session.getAttribute(key);
        }
        return value;
    }
    public <T> void writeJson(HttpServletResponse response, List<T> beans) {
        writePlainText(response, JSONUtils.listToJsonString(beans, ""));
    }

    public void writeJson(HttpServletResponse response,Object obj) {
        writePlainText(response,JSONUtils.objectToDateFormatJson(obj));
    }

    /**
     * 带状态码的返回结果
     * @param response
     * @param data
     * @param resultCode
     * @param message
     */
    public void writeJson(HttpServletResponse response,Object data, String resultCode, String message) {
        ResuleBean resuleBean = new ResuleBean();
        resuleBean.setCode(resultCode);
        resuleBean.setMessage(message);
        resuleBean.setData(data);
        writePlainText(response, JSONUtils.objectToDateFormatJson(resuleBean));
    }

    /**
     * 功能：输出文本流到页面
     *
     * @param text
     *            <code>String</code>
     */
    protected void writePlainText(HttpServletResponse response, String text) {
        WebUtils.writeText(response, text);
    }
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(String viewName){
		return new ModelAndView(viewName);
	}
}
