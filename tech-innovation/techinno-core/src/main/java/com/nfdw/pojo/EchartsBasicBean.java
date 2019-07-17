package com.nfdw.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @Description: echarts图表的基础类
 * @author Ivan Lee
 * @time 2018-12-12
 */
public class EchartsBasicBean implements Serializable {
	
	/**
	 * 标题名称
	 */
	private String titleName;
	
	/**
	 * 单位名称
	 */
	private String unitName;
	
	/**
	 * x轴数据
	 */
	private List xaxisList;
	
	/**
	 * y轴数据
	 */
	private List yaxisList;

	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public List getXaxisList() {
		return xaxisList;
	}
	public void setXaxisList(List xaxisList) {
		this.xaxisList = xaxisList;
	}
	public List getYaxisList() {
		return yaxisList;
	}
	public void setYaxisList(List yaxisList) {
		this.yaxisList = yaxisList;
	}
	
}
