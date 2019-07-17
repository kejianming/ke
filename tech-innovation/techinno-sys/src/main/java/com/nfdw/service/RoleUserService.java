package com.nfdw.service;

import java.util.List;

import com.nfdw.base.service.BaseService;
import com.nfdw.entity.SysRoleUser;

/**

 */
public interface RoleUserService  extends BaseService<SysRoleUser,String> {

  int deleteByPrimaryKey(SysRoleUser sysRoleUser);

  int insert(SysRoleUser sysRoleUser);

  int selectCountByCondition(SysRoleUser sysRoleUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

   String findRoleIdByUserId(String userId);
}
