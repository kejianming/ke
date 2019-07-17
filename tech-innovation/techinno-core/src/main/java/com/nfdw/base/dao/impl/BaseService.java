package com.nfdw.base.dao.impl;

import com.github.pagehelper.Page;
import com.nfdw.base.dao.DAO;
import com.nfdw.entity.PageData;

import javax.annotation.Resource;

public abstract class BaseService {

	@Resource(name="daoSupport")
	protected DAO dao;	
	
	protected synchronized Page<PageData> setPageDatalist(Page<PageData> list) throws Exception{
		for (int i = 0; i < list.size(); i++) {
			PageData pds = list.get(i);
			pds.put("pageNum", list.getPageNum());
			pds.put("count", list.getTotal());
			pds.put("pageSize", list.getPageSize());
			pds.put("totalPage", list.getPages());
		}
		return list;
	}
}
