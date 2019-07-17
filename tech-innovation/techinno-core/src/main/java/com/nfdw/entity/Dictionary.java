package com.nfdw.entity;



public class Dictionary {
	private int id;
	private String dgroup;
	private String code;
	private String dvalue;
	private String name;
	private String describe;
	private int sort;
	private String isused;
	private int pageNum;
	private int pageSize;
	private int totalPage;
	private int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Dictionary() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Dictionary(int id, String dgroup, String code, String dvalue,
                      String name, String describe, int sort, String isused) {
		super();
		this.id = id;
		this.dgroup = dgroup;
		this.code = code;
		this.dvalue = dvalue;
		this.name = name;
		this.describe = describe;
		this.sort = sort;
		this.isused = isused;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDgroup() {
		return dgroup;
	}
	public void setDgroup(String dgroup) {
		this.dgroup = dgroup;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDvalue() {
		return dvalue;
	}
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIsused() {
		return isused;
	}
	public void setIsused(String isused) {
		this.isused = isused;
	}


}
