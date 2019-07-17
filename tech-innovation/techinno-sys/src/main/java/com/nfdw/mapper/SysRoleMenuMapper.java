package com.nfdw.mapper;

import java.util.List;

import com.nfdw.base.BaseMapper;
import com.nfdw.entity.SysRoleMenu;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu, String> {
    List<SysRoleMenu> selectByCondition(SysRoleMenu sysRoleMenu);

    int selectCountByCondition(SysRoleMenu sysRoleMenu);
}