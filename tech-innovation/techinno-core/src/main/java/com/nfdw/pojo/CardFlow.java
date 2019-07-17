package com.nfdw.pojo;

import java.io.Serializable;

public class CardFlow implements Serializable {
	private String pkgStartTime;
	private String pkgEndTime;
	private String totalFlow;
	private String usedFlow;
	private String remainFlow;
	private String status;
	private String packageCode;
	
	public String getPkgStartTime() {
		return pkgStartTime;
	}
	public void setPkgStartTime(String pkgStartTime) {
		this.pkgStartTime = pkgStartTime;
	}
	public String getPkgEndTime() {
		return pkgEndTime;
	}
	public void setPkgEndTime(String pkgEndTime) {
		this.pkgEndTime = pkgEndTime;
	}
	public String getTotalFlow() {
		return totalFlow;
	}
	public void setTotalFlow(String totalFlow) {
		this.totalFlow = totalFlow;
	}
	public String getUsedFlow() {
		return usedFlow;
	}
	public void setUsedFlow(String usedFlow) {
		this.usedFlow = usedFlow;
	}
	public String getRemainFlow() {
		return remainFlow;
	}
	public void setRemainFlow(String remainFlow) {
		this.remainFlow = remainFlow;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	
}
