package com.nfdw.common;

import java.util.HashMap;
import java.util.Map;


public class Constants {
	
	public final static String LOGIN_RECORD_ID = "login_record_id";// 登录时记录的ID
	public final static String LOGIN_REGION_ID = "login_region_id";// 登录时区域ID
	public final static String LOGIN_USER_REGION_ID = "regionId";// 登录用户时区域ID
	public final static String USRE_NAME = "user_name";// 登录时区域ID
	public static final String LOGIN_USER = "login_user";// 登录时记录的ID
	public static final String LOGIN_TOLL_LINE = "login_toll_line";// 登录时记录的ID
	public static final String LOGIN_UUID = "login_uuid";// 登录时记录的ID
	public static final String LOGIN_SHIFT = "login_shift";
	public static final String LOGIN_QRY_USER = "login_qry_usr";
	public static final String ISNEED_LOGIN_RECORD = "isNeed_login_record";
	public static final String LOGIN_TICKET_END = "login_ticket_end";
	public static final String LOGIN_TICKET_STAR = "login_ticket_star";
	public static Map<String, Object> CACHEMAP = new HashMap<String, Object>();
	public static final String SHIRO_USER= "shiroUser";
	public static final String TB_CLIENT_DATA= "tb_client_data";
	public static final String TB_SHIFT_DATA="tb_shift_data";
	public static final String LASTUDATE = "LASTUDATE"; // 获取当前时间
	public static final String DayOfDate = "DayOfDate"; //获取当前日期
	public static final String DICT_CARTYPE= "sta_dict_carTpe";
	
	/** 读数据超时时间(单位毫秒) */
	public static final int READ_TIMEOUT = 30000;
	/** 连接超时时间(单位毫秒) */
	public static final int CONNECT_TIMEOUT = 30000;
	
	/** 读数据超时时间(单位毫秒)-短 */
	public static final int READ_TIMEOUT_FIVE_SECOND = 5000;
	/** 连接超时时间(单位毫秒)-短 */
	public static final int CONNECT_TIMEOUT_FIVE_SECOND = 5000;
	
	
	/** 字符集：UTF-8 */
	public static final String CHARSET_UTF8 = "UTF-8";
}
