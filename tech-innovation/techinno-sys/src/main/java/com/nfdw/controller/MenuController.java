package com.nfdw.controller;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.controller.BaseController;
import com.nfdw.core.annotation.Log;
import com.nfdw.core.annotation.Log.LOG_TYPE;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.SysMenu;
import com.nfdw.entity.SysRoleMenu;
import com.nfdw.entity.SysRoleUser;
import com.nfdw.exception.MyException;
import com.nfdw.service.MenuService;
import com.nfdw.service.RoleMenuService;
import com.nfdw.service.RoleUserService;
import com.nfdw.util.BeanUtil;
import com.nfdw.util.CommonUtil;
import com.nfdw.util.JsonUtil;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 菜单
 */
@RequestMapping("/menu")
@Controller
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleUserService roleUserService;

    /**
     * 展示tree
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "/showMenu", httpMethod = "GET", notes = "展示菜单")
    @GetMapping(value = "showMenu")
    @RequiresPermissions("menu:show")
    public String showMenu(Model model) {
        JSONArray ja = menuService.getMenuJsonList();
        model.addAttribute("menus", ja.toJSONString());
        return "/system/menu/menuList";
    }

    @GetMapping(value = "showAddMenu")
    public String addMenu(Model model) {
        JSONArray ja = menuService.getMenuJsonList();
        model.addAttribute("menus", ja.toJSONString());
        return "/system/menu/add-menu";
    }

    @Log(desc = "菜单管理:添加菜单", type = LOG_TYPE.UPDATE)
    @ApiOperation(value = "/addMenu", httpMethod = "POST", notes = "添加菜单")
    @PostMapping(value = "addMenu")
    @ResponseBody
    public JsonUtil addMenu(SysMenu sysMenu, Model model) {
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.setFlag(false);
        if (sysMenu == null) {
            jsonUtil.setMsg("获取数据失败");
            return jsonUtil;
        }

        if (StringUtils.isEmpty(sysMenu.getPId())) {
            sysMenu.setPId(null);
        }
        if (StringUtils.isEmpty(sysMenu.getUrl())) {
            sysMenu.setUrl(null);
        }
        if (StringUtils.isEmpty(sysMenu.getPermission())) {
            sysMenu.setPermission(null);
        }
        //将前台三级菜单统一转为类型0
        //库表0为类型 1为操作
        try {
            if (sysMenu.getMenuType() == 0) {
                sysMenu.setMenuType((byte) 1);
            } else {
                sysMenu.setMenuType((byte) 0);
            }

            menuService.insertSelective(sysMenu);
            //获取ID，加入权限菜单表
            String menuId = sysMenu.getId();
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);

            SysRoleUser sysRoleUser = new SysRoleUser();
            CurrentUser user = CommonUtil.getUser();
            String userId = user.getId();
            String roleId = roleUserService.findRoleIdByUserId(userId);
            sysRoleMenu.setRoleId(roleId);
            roleMenuService.insertRoleAndMenu(sysRoleMenu);
            jsonUtil.setMsg("添加成功");
            jsonUtil.setFlag(true);
        } catch (MyException e) {
            e.printStackTrace();
            jsonUtil.setMsg("添加失败");
        }
        return jsonUtil;
    }

    @GetMapping(value = "showUpdateMenu")
    public String showUpdateMenu(Model model, String id) {
        SysMenu sysMenu = menuService.selectByPrimaryKey(id);
        JSONArray ja = menuService.getMenuJsonList();
        model.addAttribute("menus", ja.toJSONString());
        model.addAttribute("sysMenu", sysMenu);
        if (null != sysMenu.getPId()) {
            SysMenu pSysMenu = menuService.selectByPrimaryKey(sysMenu.getPId());
            model.addAttribute("pName", pSysMenu.getName());
        }
        return "/system/menu/update-menu";
    }


    @Log(desc = "菜单管理:更新菜单", type = LOG_TYPE.ADD)
    @PostMapping(value = "updateMenu")
    @ResponseBody
    public JsonUtil updateMenu(SysMenu sysMenu) {
        SysMenu oldMenu = menuService.selectByPrimaryKey(sysMenu.getId());
        BeanUtil.copyNotNullBean(sysMenu, oldMenu);
        menuService.updateByPrimaryKeySelective(oldMenu);
        return JsonUtil.sucess("保存成功");
    }

    @Log(desc = "菜单管理:删除菜单", type = LOG_TYPE.DEL)
    @PostMapping("/menu-del")
    @ResponseBody
    public JsonUtil del(String id) {
        JsonUtil json = new JsonUtil();
        json.setFlag(false);
        if (StringUtils.isEmpty(id)) {
            json.setMsg("获取数据失败,请刷新重试!");
            return json;
        }
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setMenuId(id);
        int count = roleMenuService.selectCount(sysRoleMenu);
        //存在角色绑定不能删除
        if (count > 0) {
            json.setMsg("本菜单存在绑定角色,请先解除绑定!");
            return json;
        }
        //存在下级菜单 不能解除
        SysMenu sysMenu = new SysMenu();
        sysMenu.setPId(id);
        if (menuService.selectCount(sysMenu) > 0) {
            json.setMsg("存在子菜单,请先删除子菜单!");
            return json;
        }
        menuService.deleteByPrimaryKey(id);
        json.setMsg("删除成功");
        json.setFlag(true);
        return json;

    }

    @GetMapping(value = "ceshi")
    public String showTopMenu(Model model, String id) {

        return "/system/menu/ceshi2";
    }


}
