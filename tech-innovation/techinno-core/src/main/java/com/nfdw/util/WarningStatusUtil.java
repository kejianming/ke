package com.nfdw.util;

/**
 * 
 * @Description: 设备告警状态工具类
 * @author Ivan Lee
 * @time 2018-12-8
 */
public class WarningStatusUtil {

	/**
	 * cpu过高，超过50%为高
	 */
	public final static int WARNING_STATUS_CPU = 1 << 0;
	/**
	 * 内存占用高，超过80%为高
	 */
	public final static int WARNING_STATUS_MEM = 1 << 1;
	/**
	 * 温度过高，超过45为高
	 */
	public final static int WARNING_STATUS_TEMPERATURE = 1 << 2;
	/**
	 * 信噪比过低，小于9为过低
	 */
	public final static int WARNING_STATUS_SNR = 1 << 3;
	/**
	 * 信号强弱度较低，小于-100为信号弱
	 */
	public final static int WARNING_STATUS_SIGNAL = 1 << 4;
	/**
	 * 离线告警
	 */
	public final static int WARNING_Offline_Alarm = 1 << 5;

	/**
	 * 使用二进制的“与”运算（同为1则1），判断是否具有某种状态
	 * @param states:原有状态
	 * @param value:要判断状态
	 * @return
	 */
	public static boolean hasState(int states, int value) {
		return (states & value) != 0;
	}

	/**
	 * 使用二进制的“或”运算（有1则1），添加一种状态
	 * @param states:原有状态
	 * @param value:要判断状态
	 * @return
	 */
	public static int addState(int states, int value) {
		if (hasState(states, value)) {
			return states;
		}
		return states | value;
	}

	/**
	 * 使用二进制的“异或”运算（相同为0，不同为1），移除一种状态
	 * @param states:原有状态
	 * @param value:要判断状态
	 * @return
	 */
	public static int removeState(int states, int value) {
		if (!hasState(states, value)) {
			return states;
		}
		return states ^ value;
	}
	
	
	
}
