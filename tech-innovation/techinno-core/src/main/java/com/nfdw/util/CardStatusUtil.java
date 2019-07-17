package com.nfdw.util;

/**
 * 
 * @Description: 卡状态工具类
 * @author Yang
 * @time 2019-07-03
 */
public class CardStatusUtil {

	/**
	 * 是换卡（001）
	 */
	public final static int CARD_STATUS_CCHANGE = 1 << 0;
	/**
	 * 是销卡（010）
	 */
	public final static int CARD_STATUS_OVER = 1 << 1;
	

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
