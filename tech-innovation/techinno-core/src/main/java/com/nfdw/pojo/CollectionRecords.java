package com.nfdw.pojo;

import java.io.Serializable;

public class CollectionRecords implements Serializable {
	
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String collect_time;
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getCollect_person() {
		return collect_person;
	}
	public void setCollect_person(String collect_person) {
		this.collect_person = collect_person;
	}
	public String getCollect_state() {
		return collect_state;
	}
	public void setCollect_state(String collect_state) {
		this.collect_state = collect_state;
	}
	public String getSjcjzdh() {
		return sjcjzdh;
	}
	public void setSjcjzdh(String sjcjzdh) {
		this.sjcjzdh = sjcjzdh;
	}
	private String iccid;
	private String collect_person;
	private String collect_state;
	private String sjcjzdh;

}
