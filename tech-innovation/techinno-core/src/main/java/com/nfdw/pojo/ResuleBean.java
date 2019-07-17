package com.nfdw.pojo;

import java.io.Serializable;

/**
 * 
 * @Description: 
 * @author Ivan Lee
 * @time 2018-9-14
 */
public class ResuleBean<T> implements Serializable {
	
	/**
	 * 返回结果码,如果为正常返回,则为0000,否则就是错误码
	 */
	private String code;
	/**
	 * 返回结果信息,如果为正常返回,则为成功,否则就是提示语
	 */
	private String message;
	/**
	 * 返回业务结果,如果为正常返回,则为业务类,否则为空
	 */
	private T data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
