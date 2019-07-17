package com.nfdw.mapper;

import org.apache.ibatis.annotations.Param;

import com.nfdw.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserMapper extends com.nfdw.base.BaseMapper<SysUser,String> {

    SysUser login(@Param("username") String username);

    int count();

    int add(SysUser user);

    int delById(String id);

    int checkUser(String username);

    /**
     * 更新密码
     * @param user
     * @return
     */
    int rePass(SysUser user);

    List<SysUser> getUserByRoleId(Map map);
    int countUserByRoleId(Map map);

    List<SysUser> findBydepartmentId(Integer departmentName);
    
    List<SysUser> queryUserByRoleIdAndDepartmentId(@Param("roleId") String roleId, @Param("departmentId") Integer departmentId);
}