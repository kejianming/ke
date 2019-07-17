package com.nfdw.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.service.BaseService;
import com.nfdw.entity.SysDepartment;

public interface DepartmentService extends BaseService<SysDepartment,String> {


	List<SysDepartment> showMenuJsonList();

//    JSONArray getTreeUtil(String departmentId);
     @Override
     int insertSelective(SysDepartment record);

     int checkDepartment(String departmentname);
}
