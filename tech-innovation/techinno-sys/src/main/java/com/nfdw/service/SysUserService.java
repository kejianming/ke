package com.nfdw.service;


import java.util.List;

import com.nfdw.base.service.BaseService;
import com.nfdw.entity.PageData;
import com.nfdw.entity.SysRoleUser;
import com.nfdw.entity.SysUser;
import com.nfdw.util.Checkbox;
import com.nfdw.util.JsonUtil;

/**

 */
public interface SysUserService extends BaseService<SysUser,String> {

  SysUser login(String username);


  SysUser selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  @Override
  List<SysUser> selectListByPage(SysUser sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(SysUser user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  @Override
  int updateByPrimaryKey(SysUser sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(SysUser user);


  List<SysUser> getUserByRoleId(String roleId,int page,int limit);

  int countUserByRoleId(String roleId,int page,int limit);

    List<SysUser> findBydepartmentId(Integer departmentId);

  boolean ifNetUser(PageData pd);
}
