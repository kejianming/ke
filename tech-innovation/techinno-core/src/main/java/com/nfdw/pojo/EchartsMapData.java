package com.nfdw.pojo;

import java.io.Serializable;

/**
 * 
 * @Description: echarts地图的基础数据格式
 * @author Ivan Lee
 * @time 2018-12-12
 */
public class EchartsMapData implements Serializable {
	
	private String name;
	private String value;
	private Integer setValueto;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getSetValueto() {
		return setValueto;
	}
	public void setSetValueto(Integer setValueto) {
		this.setValueto = setValueto;
	}
	
}
