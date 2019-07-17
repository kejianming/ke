package com.nfdw.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @Description: echarts地图的基础类
 * @author Ivan Lee
 * @time 2018-12-12
 */
public class EchartsMapBean implements Serializable {
	
	/**
	 * 地图数据
	 */
	private List<EchartsMapData> mapDataList;

	public List<EchartsMapData> getMapDataList() {
		return mapDataList;
	}
	public void setMapDataList(List<EchartsMapData> mapDataList) {
		this.mapDataList = mapDataList;
	}
	
}
