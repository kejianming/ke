package com.nfdw.service;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.service.BaseService;
import com.nfdw.entity.SysMenu;

import java.util.List;

/**

 */
public interface MenuService extends BaseService<SysMenu,String> {

  List<SysMenu> getMenuNotSuper();

  @Override
  int insert(SysMenu menu);


  List<SysMenu> getMenuChildren(String id);

  public JSONArray getMenuJsonList();

  List<SysMenu> getMenuChildrenAll(String id);

  public JSONArray getTreeUtil(String roleId);

  List<SysMenu> getUserMenu(String id);

  public JSONArray getMenuJsonByUser(List<SysMenu> menuList);



}
