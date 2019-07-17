package com.nfdw.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.BaseMapper;
import com.nfdw.base.service.impl.BaseServiceImpl;
import com.nfdw.entity.SysMenu;
import com.nfdw.entity.SysRoleMenu;
import com.nfdw.mapper.SysMenuMapper;
import com.nfdw.mapper.SysRoleMenuMapper;
import com.nfdw.service.MenuService;
import com.nfdw.util.TreeUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 */
@Service
@Configuration
public class MenuServiceImpl extends BaseServiceImpl<SysMenu, String> implements MenuService{

    @Autowired
    private SysMenuMapper menuDao;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Value("${server.servlet.context-path}")
    private String defaultUrl;


    @Override
    public BaseMapper<SysMenu, String> getMappser() {
        return menuDao;
    }

    @Override
    public List<SysMenu> getMenuNotSuper() {
        return menuDao.getMenuNotSuper();
    }

    @Override
    public int insert(SysMenu menu) {
        return menuDao.insert(menu);
    }


    @Override
    public List<SysMenu> getMenuChildren(String id) {
        return menuDao.getMenuChildren(id);
    }

    public SysMenu child(SysMenu sysMenu, List<SysMenu> sysMenus, Integer pNum, Integer num) {
        List<SysMenu> childSysMenu = sysMenus.stream().filter(s ->
                s.getPId().equals(sysMenu.getId())).collect(Collectors.toList());
        sysMenus.removeAll(childSysMenu);
        SysMenu m;
        for (SysMenu menu : childSysMenu) {
            ++num;
            m = child(menu, sysMenus, pNum, num);
            sysMenu.addChild(menu);
        }
        return sysMenu;
    }

    @Override
    public JSONArray getMenuJsonList() {
        List<SysMenu> sysMenus = selectAll();
        List<SysMenu> supers = sysMenus.stream().filter(sysMenu ->
                StringUtils.isEmpty(sysMenu.getPId()))
                .collect(Collectors.toList());
        sysMenus.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysMenu::getOrderNum));
        JSONArray jsonArr = new JSONArray();
        for (SysMenu sysMenu : supers) {
            SysMenu child = child(sysMenu, sysMenus, 0, 0);
            jsonArr.add(child);
        }


        return jsonArr;
    }

    @Override
    public JSONArray getMenuJsonByUser(List<SysMenu> menuList) {
        JSONArray jsonArr = new JSONArray();
        for(SysMenu sysMenu:menuList){
            if(sysMenu.getOrderNum()==null){
                sysMenu.setOrderNum(0);
            }
        }

      //  System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(menuList, new Comparator<SysMenu>() {
            @Override
            public int compare(SysMenu o1, SysMenu o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });

        //遍历添加索引
        int pNum = 1000;
        int index=0;
        for (SysMenu menu : menuList) {
            if (StringUtils.isEmpty(menu.getPId())) {
                SysMenu sysMenu = getChilds(menu, pNum, 0, menuList);
               jsonArr.add(index++,sysMenu);

                pNum += 1000;
            }
        }

        return jsonArr;
    }



    public SysMenu getChilds(SysMenu menu, int pNum, int num, List<SysMenu> menuList) {
        for (SysMenu menus : menuList) {
            if (menu.getId().equals(menus.getPId()) && menus.getMenuType() == 0) {
                ++num;
                SysMenu m = getChilds(menus, pNum, num, menuList);
                m.setNum(pNum + num);
                menu.addChild(m);
            }
        }
        return menu;

    }

    @Override
    public List<SysMenu> getMenuChildrenAll(String id) {
        return menuDao.getMenuChildrenAll(id);
    }


    @Override
    public JSONArray getTreeUtil(String roleId) {
        TreeUtil treeUtil = null;
        List<SysMenu> sysMenus = selectAll();
        List<SysMenu> supers = sysMenus.stream().filter(sysMenu ->
                StringUtils.isEmpty(sysMenu.getPId()))
                .collect(Collectors.toList());
        sysMenus.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysMenu::getOrderNum));
        JSONArray jsonArr = new JSONArray();
        for (SysMenu sysMenu : supers) {
            treeUtil = getChildByTree(sysMenu, sysMenus, 0, null, roleId);
            jsonArr.add(treeUtil);
        }
        return jsonArr;

    }

    @Override
    public List<SysMenu> getUserMenu(String id) {

        return menuDao.getUserMenu(id, defaultUrl);
    }

    public TreeUtil getChildByTree(SysMenu sysMenu, List<SysMenu> sysMenus, int layer, String pId, String roleId) {
        layer++;
        List<SysMenu> childSysMenu = sysMenus.stream().filter(s ->
                s.getPId().equals(sysMenu.getId())).collect(Collectors.toList());
        sysMenus.removeAll(childSysMenu);
        TreeUtil treeUtil = new TreeUtil();
        treeUtil.setId(sysMenu.getId());
        treeUtil.setName(sysMenu.getName());
        treeUtil.setLayer(layer);
        treeUtil.setPId(pId);
        /**判断是否存在*/
        if (!StringUtils.isEmpty(roleId)) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(sysMenu.getId());
            sysRoleMenu.setRoleId(roleId);
            int count = roleMenuMapper.selectCountByCondition(sysRoleMenu);
            if (count > 0)
                treeUtil.setChecked(true);
        }
        for (SysMenu menu : childSysMenu) {
            TreeUtil m = getChildByTree(menu, sysMenus, layer, menu.getId(), roleId);
            treeUtil.getChildren().add(m);
        }
        return treeUtil;
    }
}
