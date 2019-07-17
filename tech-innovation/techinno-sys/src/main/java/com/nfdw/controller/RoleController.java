package com.nfdw.controller;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.controller.BaseController;
import com.nfdw.common.ResultCodes;
import com.nfdw.core.annotation.Log;
import com.nfdw.core.annotation.Log.LOG_TYPE;
import com.nfdw.entity.SysRole;
import com.nfdw.entity.SysRoleMenu;
import com.nfdw.entity.SysRoleUser;
import com.nfdw.exception.MyException;
import com.nfdw.service.MenuService;
import com.nfdw.service.RoleMenuService;
import com.nfdw.service.RoleService;
import com.nfdw.service.RoleUserService;
import com.nfdw.util.BeanUtil;
import com.nfdw.util.JsonUtil;
import com.nfdw.util.ReType;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 角色业务
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping(value = "showRole")
    @RequiresPermissions(value = "role:show")
    public String showRole(Model model) {
        return "/system/role/roleList";
    }

    @ApiOperation(value = "/showRoleList", httpMethod = "GET", notes = "展示角色")
    @GetMapping(value = "showRoleList")
    @ResponseBody
    @RequiresPermissions("role:show")
    public ReType showRoleList(SysRole role, Model model, String page, String limit) {
        return roleService.show(role, Integer.valueOf(page), Integer.valueOf(limit));
    }

    @ApiOperation(value = "/showaLLRoleList", httpMethod = "GET", notes = "展示角色")
    @GetMapping(value = "showaLLRoleList")
    @ResponseBody
    @RequiresPermissions("role:show")
    public String showRoleList(SysRole role, Model model) {
        return roleService.showAll(role);
    }


    @GetMapping(value = "showAddRole")
    public String goAddRole(Model model) {
        JSONArray jsonArray = menuService.getTreeUtil(null);
        String s = jsonArray.toString();
        model.addAttribute("menus", jsonArray.toJSONString());
        return "/system/role/add-role";
    }

    @ApiOperation(value = "/addRole", httpMethod = "POST", notes = "添加角色")
    @Log(desc = "角色管理:添加角色")
    @PostMapping(value = "addRole")
    @ResponseBody
    public void addRole(SysRole sysRole, String[] menus,HttpServletResponse response) {
        if (StringUtils.isEmpty(sysRole.getRoleName())) {
            JsonUtil.error("角色名称不能为空");
        }
        JsonUtil j = new JsonUtil();
        String msg = "修改成功";
        try {
            roleService.insertSelective(sysRole);
            //操作role-menu data
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());

            if (menus != null)
                for (String menu : menus) {
                    sysRoleMenu.setMenuId(menu);
                    roleMenuService.insert(sysRoleMenu);
                }
            writeJson(response, true, null, "添加成功");
        } catch (MyException e) {
            msg = "修改失败";
            j.setFlag(false);
            e.printStackTrace();
            writeJson(response, false, null, "添加失败！");
        }


    }

    @GetMapping(value = "updateRole")
    public String updateRole(String id, Model model, boolean detail) {
        if (StringUtils.isNotEmpty(id)) {
            SysRole role = roleService.selectByPrimaryKey(id);
            model.addAttribute("role", role);
            JSONArray jsonArray = menuService.getTreeUtil(id);
            model.addAttribute("menus", jsonArray.toJSONString());
        }
        model.addAttribute("detail", detail);
        return "system/role/update-role";
    }

    @ApiOperation(value = "/updateRole", httpMethod = "POST", notes = "更新角色")
    @Log(desc = "角色管理:更新角色")
    @PostMapping(value = "updateRole")
    @ResponseBody
    public void updateUser(SysRole role, String[] menus,HttpServletResponse response) {
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.setFlag(false);
        if (role == null) {
            jsonUtil.setMsg("获取数据失败");
            writeJson(response,jsonUtil);
//            return jsonUtil;
        }
        try {
            SysRole oldRole = roleService.selectByPrimaryKey(role.getId());
            BeanUtil.copyNotNullBean(role, oldRole);
            roleService.updateByPrimaryKeySelective(oldRole);

            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(role.getId());
            List<SysRoleMenu> menuList = roleMenuService.selectByCondition(sysRoleMenu);
            for (SysRoleMenu sysRoleMenu1 : menuList) {
                roleMenuService.deleteByPrimaryKey(sysRoleMenu1);
            }
            if (menus != null)
                for (String menu : menus) {
                    sysRoleMenu.setMenuId(menu);
                    roleMenuService.insert(sysRoleMenu);
                }
            jsonUtil.setFlag(true);
            jsonUtil.setMsg("修改成功");
        } catch (MyException e) {
            jsonUtil.setMsg("修改失败");
            e.printStackTrace();
        }
        writeJson(response,jsonUtil);
//        return jsonUtil;
    }

    @ApiOperation(value = "/del", httpMethod = "POST", notes = "删除角色")
    @Log(desc = "角色管理:删除角色", type = LOG_TYPE.DEL)
    @PostMapping(value = "del")
    @ResponseBody
    @RequiresPermissions("role:del")
    public JsonUtil del(String id) {
        if (StringUtils.isEmpty(id)) {
            return JsonUtil.error("获取数据失败");
        }
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setRoleId(id);
        SysRoleMenu sysRoleMenu =new SysRoleMenu();
        sysRoleMenu.setRoleId(id);
        JsonUtil j = new JsonUtil();
        try {
            int count = roleUserService.selectCountByCondition(sysRoleUser);
            if (count > 0) {
                return JsonUtil.error("已分配给用户，删除失败");
            }
            count = roleMenuService.selectCountByCondition(sysRoleMenu);
            if (count > 0) {
                return JsonUtil.error("已关联菜单，删除失败");
            }
            roleService.deleteByPrimaryKey(id);
            j.setMsg("删除成功");
        } catch (MyException e) {
            j.setMsg("删除失败");
            j.setFlag(false);
            e.printStackTrace();
        }
        return j;
    }

}
