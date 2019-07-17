package com.nfdw.entity;

import java.util.Date;


public class Permission {
	private Integer id;
	private Integer pid;
	private String name;
	private Integer sort;
	private String shiropermission;
	private String isused;
	private String url;
	private String creater;
	private Date createtime;
	private String updater;
	private Date updatetime;
	
	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Permission(Integer id, Integer pid, String name, Integer sort,
			String shiropermission, String isused, String url, String creater,
			Date createtime, String updater, Date updatetime) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.sort = sort;
		this.shiropermission = shiropermission;
		this.isused = isused;
		this.url = url;
		this.creater = creater;
		this.createtime = createtime;
		this.updater = updater;
		this.updatetime = updatetime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getShiropermission() {
		return shiropermission;
	}
	public void setShiropermission(String shiropermission) {
		this.shiropermission = shiropermission;
	}
	public String getIsused() {
		return isused;
	}
	public void setIsused(String isused) {
		this.isused = isused;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
}
