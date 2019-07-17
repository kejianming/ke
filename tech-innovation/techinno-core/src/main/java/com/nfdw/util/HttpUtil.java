package com.nfdw.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <B>描述: </B> http工具类
 * @author yongkang.li
 * @time 2018年2月2日 上午10:19:41
 */
public class HttpUtil {

	private static final String HTTP_CLIENT_OPT = "";
	
	private HttpUtil(){
	}
	/**
	 * post text/xml
	 */
	public static String post(String url, String postData,
			Map<String, String> map, String charSet, int connectTimeOut,
			int readTimeOut) throws Exception {
		return post(url, postData, map, charSet, connectTimeOut, readTimeOut, "text/xml");
	}
	/**
	 * post application/json
	 */
	public static String postByJson(String url, String postData,
			Map<String, String> map, String charSet, int connectTimeOut,
			int readTimeOut) throws Exception {
		return post(url, postData, map, charSet, connectTimeOut, readTimeOut, "application/json");
	}
	/**
	 * post application/xml
	 */
	public static String postByXml(String url, String postData,
			Map<String, String> headerMap, String charSet, int connectTimeOut,
			int readTimeOut) throws Exception {
		return post(url, postData, headerMap, charSet, connectTimeOut, readTimeOut, "application/xml");
	}
	
	/**
	 * 功能： post请求
	 * @param url
	 * @param postData
	 * @param map
	 * @param charSet
	 * @param connectTimeOut
	 * @param readTimeOut
	 * @param contentType 请求的类型
	 * @return
	 * @throws Exception 
	 * String
	 */
	public static String post(String url, String postData,
			Map<String, String> map, String charSet, int connectTimeOut,
			int readTimeOut, String contentType) throws Exception {
		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient
				.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(connectTimeOut);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(readTimeOut);
		
		//另外设置http client的重试次数，默认是3次；当前是禁用掉
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		httpClient.setParams(httpClientParams);
		
		PostMethod httpPost = new PostMethod(url);
		if ("Y".equals(HTTP_CLIENT_OPT)) {
			httpPost.setRequestHeader("Connection", "close");
		}
		String result = null;
		try {
			if (StringUtils.isNotBlank(postData)) {
				RequestEntity entity = new StringRequestEntity(postData,contentType, charSet);
				httpPost.setRequestEntity(entity);
			}
			packagePostHttps(map, httpPost);
			httpClient.executeMethod(httpPost);
			
			InputStream inputStream = httpPost.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputStream, charSet));
			StringBuilder strBuidler = new StringBuilder();
			String str = "";
			
			while ((str = br.readLine()) != null) {
				strBuidler.append(str);
			}
			result = strBuidler.toString();
		} catch (ConnectException e) {
			throw new ConnectException("发送数据至" + url + "时出现[ConnectException]异常:"+ e.getMessage());
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException("发送数据至" + url
					+ "时出现[SocketTimeoutException]异常:" + e.getMessage());
		} catch (Exception e) {
			throw new Exception("发送数据至" + url + "时出现异常:" + e.getMessage());
		} finally {
			httpPost.releaseConnection();
		}
		return result;
	}
	
	
	//组装post数据
	private static void packagePostHttps(Map<String, String> map,
			PostMethod httpPost) {
		if (map != null) {
			Set<Map.Entry<String, String>> set = map.entrySet();
			NameValuePair[] pairs = new NameValuePair[set.size()];
			int i = 0; 
			int sikpCount=0;//统计set里面空值的个数
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry<String, String> entry =  it.next();
				if (entry.getValue() == null){
					sikpCount++;
					continue;
				}
				NameValuePair nvp = new NameValuePair(entry.getKey(),
						entry.getValue());
				pairs[i] = nvp;
				i++;
			}
			//去除空值
			NameValuePair[] pairs2 = new NameValuePair[set.size()-sikpCount];
			System.arraycopy(pairs, 0, pairs2, 0, pairs2.length);
			httpPost.setQueryString(pairs2);//
		}
	}
	
	

	/**
	 * Get 请求
	 * @param url
	 * @param charSet
	 * @param map
	 * @param connectTimeOut
	 * @param readTimeOut
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, String charSet,Map<String,String> map, int connectTimeOut, int readTimeOut) throws Exception {
		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(connectTimeOut);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(readTimeOut); 
		GetMethod httpGet = new GetMethod(url);
		String result = null;
		BufferedReader br = null;
		if(map!=null){
		for (String key : map.keySet())
		{
			httpGet.addRequestHeader(key, map.get(key));
		}}
		try {
			StringBuffer stringBuffer = new StringBuffer();
			httpClient.executeMethod(httpGet);
			InputStream in = httpGet.getResponseBodyAsStream();			
			br = new BufferedReader(new InputStreamReader(in, charSet));
			String line = "";
			while ((line = br.readLine()) != null) {
				stringBuffer.append(line);
			}
			result = stringBuffer.toString();
		} catch (ConnectException e) {
			throw new ConnectException("发送数据至" + url + "时出现[ConnectException]异常:"
					+ e.getMessage());
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException("发送数据至" + url
					+ "时出现[SocketTimeoutException]异常:" + e.getMessage());
		} catch (Exception e) {
			throw new Exception("发送数据至" + url + "时出现异常:" + e.getMessage(),e);
		} finally {
			httpGet.releaseConnection();
		}
		return result;
	}
	public static String get(String url, String charSet, int connectTimeOut, int readTimeOut) throws Exception {
		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(connectTimeOut);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(readTimeOut); 
		GetMethod httpGet = new GetMethod(url);
		String result = null;
		BufferedReader br = null;
		try {
			StringBuffer stringBuffer = new StringBuffer();
			httpClient.executeMethod(httpGet);
			InputStream in = httpGet.getResponseBodyAsStream();			
			br = new BufferedReader(new InputStreamReader(in, charSet));
			String line = "";
			while ((line = br.readLine()) != null) {
				stringBuffer.append(line);
			}
			result = stringBuffer.toString();
		} catch (ConnectException e) {
			throw new ConnectException("发送数据至" + url + "时出现[ConnectException]异常:"
					+ e.getMessage());
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException("发送数据至" + url
					+ "时出现[SocketTimeoutException]异常:" + e.getMessage());
		} catch (Exception e) {
			throw new Exception("发送数据至" + url + "时出现异常:" + e.getMessage(),e);
		} finally {
			httpGet.releaseConnection();
		}
		return result;
	}
}
