package com.nfdw;

/**
 * 
 * @Description: 多数据源枚举类
 * @Author Ivan Lee
 * @Date 2019年4月16日
 */
public enum DataSourceType {
	
	/*
	 * 第一数据源
	 */
	Primary("primary"),
	
	/*
	 * 第二数据源
	 */
	Secondary("secondary");
	
	private String name;

	DataSourceType(String name) {
        this.name = name;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
