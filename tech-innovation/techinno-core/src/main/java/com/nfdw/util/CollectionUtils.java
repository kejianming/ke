package com.nfdw.util;

import java.util.Collection;

/**
 * 
 * @Description: 集合工具类
 * @Author Ivan Lee
 * @Date 2018年8月9日
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
	
	public static boolean isEmptyCollection(Collection collection) {
		
		if (null == collection || collection.size() < 1) {
			return true;
		}
		return false;
	}
	
	
}
