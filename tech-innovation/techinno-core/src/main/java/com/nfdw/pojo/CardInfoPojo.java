package com.nfdw.pojo;

import java.io.Serializable;
import java.util.List;

public class CardInfoPojo implements Serializable {
	private String snno;
	private String iccid;
	private String cardno;
	private String status;
	private String cardstatus;
	private String used;
	private String activedate;
	private String totalflow;
	private String cardtype;
	private String currentprdcode;
	private String nextprdcode;
	private List<CardFlow> flowlist;
	private String dateexpire;
	private String imei;
	private String cardversion;
	private String ctdDataUsage;
	private int businessstatus;
	
	
	public int getBusinessstatus() {
		return businessstatus;
	}
	public void setBusinessstatus(int businessstatus) {
		this.businessstatus = businessstatus;
	}
	public String getCtdDataUsage() {
		return ctdDataUsage;
	}
	public void setCtdDataUsage(String ctdDataUsage) {
		this.ctdDataUsage = ctdDataUsage;
	}
	public String getIccid() {
		return iccid;
	}
	public String getSnno() {
		return snno;
	}
	public void setSnno(String snno) {
		this.snno = snno;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCardstatus() {
		return cardstatus;
	}
	public void setCardstatus(String cardstatus) {
		this.cardstatus = cardstatus;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public String getActivedate() {
		return activedate;
	}
	public void setActivedate(String activedate) {
		this.activedate = activedate;
	}
	public String getTotalflow() {
		return totalflow;
	}
	public void setTotalflow(String totalflow) {
		this.totalflow = totalflow;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getCurrentprdcode() {
		return currentprdcode;
	}
	public void setCurrentprdcode(String currentprdcode) {
		this.currentprdcode = currentprdcode;
	}
	public String getNextprdcode() {
		return nextprdcode;
	}
	public void setNextprdcode(String nextprdcode) {
		this.nextprdcode = nextprdcode;
	}
	public List<CardFlow> getFlowlist() {
		return flowlist;
	}
	public void setFlowlist(List<CardFlow> flowlist) {
		this.flowlist = flowlist;
	}
	public String getDateexpire() {
		return dateexpire;
	}
	public void setDateexpire(String dateexpire) {
		this.dateexpire = dateexpire;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getCardversion() {
		return cardversion;
	}
	public void setCardversion(String cardversion) {
		this.cardversion = cardversion;
	}
	
}
