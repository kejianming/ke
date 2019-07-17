package com.nfdw.util;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @Description: Object类型转换工具
 * @Author Ivan Lee
 * @Date 2019年1月7日
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
	
	/**
     * 转为整型
     * 
     * @param obj
     *            输入对象
     * @return int整型，如果obj不是整型默认返回0
     */
    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }
    /**
     * 转为整型
     * 
     * @param obj
     *            输入对象
     * @param defaultValue
     *            默认值
     * @return int整型，如果obj不是整型默认返回默认值
     */
    public static int toInt(Object obj, int defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return NumberUtils.toInt(obj.toString(), defaultValue);
    }
	
    /**
     * Return whether the given array is empty: that is, <code>null</code> or of
     * zero length.
     * 
     * @param array
     *            the array to check
     * @return whether the given array is empty
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }
    
    public static boolean isEmpty(List list) {
        return (list == null || list.size() == 0);
    }
}
