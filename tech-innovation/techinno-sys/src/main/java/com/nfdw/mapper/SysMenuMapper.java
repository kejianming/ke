package com.nfdw.mapper;

import org.apache.ibatis.annotations.Param;

import com.nfdw.base.BaseMapper;
import com.nfdw.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu,String> {

    /**获取元节点*/
    List<SysMenu> getMenuNotSuper();

    /**
     * 获取子节点
     * @return
     */
    List<SysMenu> getMenuChildren(String id);

    List<SysMenu> getMenuChildrenAll(String id);

    /**
     * 根据用户获取所有菜单
     * @param id
     * @return
     */
    //,@Param("defaultUrl") String defaultUrl
    List<SysMenu> getUserMenu(@Param("id") String id,@Param("defaultUrl") String defaultUrl);

    List<SysMenu> findLeftMenu(String ids);


}