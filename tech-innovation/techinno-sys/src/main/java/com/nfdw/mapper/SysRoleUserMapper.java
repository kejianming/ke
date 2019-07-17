package com.nfdw.mapper;

import java.util.List;

import com.nfdw.base.BaseMapper;
import com.nfdw.entity.SysRoleUser;

public interface SysRoleUserMapper extends BaseMapper<SysRoleUser,String> {

    List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

    int selectCountByCondition(SysRoleUser sysRoleUser);
}