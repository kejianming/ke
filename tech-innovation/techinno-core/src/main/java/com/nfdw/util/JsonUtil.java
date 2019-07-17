package com.nfdw.util;


import com.alibaba.fastjson.JSONObject;

/**

 * ajax 回执
 */
public class JsonUtil {

  //默认成功
  private boolean flag=true;
  private String msg;
  private JSONObject josnObj;
  private Object data;

  public boolean isFlag() {
    return flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public JSONObject getJosnObj() {
    return josnObj;
  }

  public void setJosnObj(JSONObject josnObj) {
    this.josnObj = josnObj;
  }
  
  

  	public Object getData() {
  		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

public JsonUtil() {
  }

  public JsonUtil(boolean flag, String msg) {
    this.flag = flag;
    this.msg = msg;
  }

  /**restful 返回*/
  public static JsonUtil error(String msg){
    return new JsonUtil(false,msg);
  }
  public  static JsonUtil sucess(String msg){
    return new JsonUtil(true,msg);
  }
}
