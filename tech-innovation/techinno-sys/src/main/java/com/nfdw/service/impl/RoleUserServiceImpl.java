package com.nfdw.service.impl;

import com.nfdw.base.BaseMapper;
import com.nfdw.base.service.impl.BaseServiceImpl;
import com.nfdw.entity.SysRoleUser;
import com.nfdw.mapper.SysRoleUserMapper;
import com.nfdw.service.RoleUserService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl<SysRoleUser,String> implements
    RoleUserService {

  @Autowired
  private SysRoleUserMapper sysRoleUserMapper;

  @Override
  public BaseMapper<SysRoleUser, String> getMappser() {
    return sysRoleUserMapper;
  }

  @Override
  public int deleteByPrimaryKey(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.deleteByPrimaryKey(sysRoleUser);
  }

  @Override
  public int selectCountByCondition(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.selectCountByCondition(sysRoleUser);
  }

  @Override
  public List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.selectByCondition(sysRoleUser);
  }

  @Override
  public String findRoleIdByUserId(String userId) {
    SysRoleUser sysRoleUser = sysRoleUserMapper.selectByPrimaryKey(userId);
   return sysRoleUser.getRoleId();
  }

}
