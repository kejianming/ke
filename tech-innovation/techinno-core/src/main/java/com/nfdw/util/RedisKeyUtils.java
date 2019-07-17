package com.nfdw.util;

/**
 * 
 * @Description: redis键值工具类。key的封装规则统一为：前缀+模块+功能+自定义字符串（若字符串拼接次数较多，建议使用StringBuilder）
 * @author Ivan Lee
 * @time 2018-10-25
 * 
 */
public class RedisKeyUtils {
	
	/**
	 * web项目生成的redis所有key必须以此字符串作为前缀
	 */
	private static String KEY_PREFIX = "web_";
	
	/**
	 * 资费管理传redis的key
	 */
	public static final String CARD_RATEPLAN ="web_Card_Rateplan";
	/**
	 * 大屏模块接口
	 */
	private static String SCREEN_INTERFACE = "screen_interface_";
	/**
	 * 设备告警设置
	 */
    private static String B_CT_WARININGCONFIG="web_CT_Warning_Config";
    /**
     * 设备性能设置
     */
    private static String B_CT_PERFORMANCE_THRESHOLDCONFIG="web_CT_Performance_Threshold_Config";
    
	/**
	 * 
	 * @param startDate
	 * @return
	 */
	
	/**
	 * 获取大屏接口数据的key
	 * @param startDate
	 * @return
	 */
	public static String getScreenInterfaceDataKey(String startDate) {
		return KEY_PREFIX + SCREEN_INTERFACE + startDate;
	}
	
	/**
	 * 获取在线接口数的key
	 * @param startDate
	 * @return
	 */
	public static String getOnlineInterfaceDataKey(String startDate) {
		return KEY_PREFIX + SCREEN_INTERFACE + startDate;
	}
	
	public static String getB_Ct_WariningConfig(){
		return B_CT_WARININGCONFIG;
	}
	
	public static String getB_CT_PERFORMANCE_THRESHOLDCONFIG(){
		return B_CT_PERFORMANCE_THRESHOLDCONFIG;
	}
}
