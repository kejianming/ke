package com.nfdw.controller;


import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.controller.BaseController;
import com.nfdw.core.annotation.Log;
import com.nfdw.core.annotation.Log.LOG_TYPE;
import com.nfdw.entity.SysDepartment;
import com.nfdw.entity.SysUser;
import com.nfdw.exception.MyException;
import com.nfdw.service.DepartmentService;
import com.nfdw.service.SysUserService;
import com.nfdw.util.BeanUtil;
import com.nfdw.util.JsonUtil;
import com.nfdw.util.ReType;

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

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

@RequestMapping("/department")
@Controller
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SysUserService userService;

    /**
     * 2019/1/3
     * 区域显示列表
     */

    @GetMapping("/showmenu")
    public String showMenu(Model model) {
        
        return "/system/department/departmentList";
    }

    @ApiOperation(value = "/showDepartmentList", httpMethod = "GET", notes = "展示角色")
    @GetMapping(value = "showDepartmentList")
    @ResponseBody
    @RequiresPermissions("department:show")
    public ReType showRoleList(SysDepartment department, Model model, String page, String limit) {
        ReType show = departmentService.show(department, Integer.valueOf(page), Integer.valueOf(limit));
        return show;
    }

    @GetMapping(value = "showAddDepartment")
    public String goAddRole(Model model) {
//        JSONArray jsonArray =departmentService.getTreeUtil(null);
//        String s = jsonArray.toString();
//        model.addAttribute("menus", jsonArray.toJSONString());
        return "/system/department/add-department";
    }


    @ApiOperation(value = "/addDepartment", httpMethod = "POST", notes = "添加部门")
    @Log(desc = "区域管理:添加部门",type = LOG_TYPE.ADD)
    @PostMapping(value = "addDepartment")
    @ResponseBody
    public JsonUtil addDepartment(SysDepartment sysDepartment) {
        if (StringUtils.isEmpty(sysDepartment.getDepartmentName())) {
            JsonUtil.error("部门名称不能为空");
        }
        JsonUtil j = new JsonUtil();
        try {
        	sysDepartment.setCreateTime(new Date());
            departmentService.insertSelective(sysDepartment);
            //操作role-menu data
            j.setMsg("保存成功");

        } catch (MyException e) {
            j.setMsg("保存失败");
            j.setFlag(false);
            e.printStackTrace();
        }
        return j;
    }


    /**
     * 2019/1/9
     * 部门删除，联合查询用户表
     */
    @PostMapping(value = "del")
    @ResponseBody
    @Log(desc = "区域管理:删除部门", type = LOG_TYPE.DEL)
    public JsonUtil del(Integer id) {
        if (id == null) {
            return JsonUtil.error("获取数据失败");
        }
        JsonUtil j = new JsonUtil();
        try {

            SysDepartment department = departmentService.selectByPrimaryKey(id);
            Integer departmentId = department.getId();
            List<SysUser> sysUserList = userService.findBydepartmentId(departmentId);
            if (!sysUserList.isEmpty()) {
                return JsonUtil.error("已分配给用户，删除失败");
            }
            departmentService.deleteByPrimaryKey(id);
            j.setMsg("删除成功");
        } catch (MyException e) {
            j.setMsg("删除失败");
            j.setFlag(false);
            e.printStackTrace();
        }
        return j;
    }

    @GetMapping(value = "updateDepartment")
    public String updateRole(Integer id, Model model, boolean detail) {
        if (StringUtils.isNotEmpty(String.valueOf(id))) {
            SysDepartment department = departmentService.selectByPrimaryKey(id);
            model.addAttribute("department", department);
//            JSONArray jsonArray = menuService.getTreeUtil(id);
//            model.addAttribute("menus", jsonArray.toJSONString());
        }
        model.addAttribute("detail", detail);
        return "system/department/update-department";
    }


    @ApiOperation(value = "/updateDepartment", httpMethod = "POST", notes = "更新用户")
    @Log(desc = "区域管理:更新部门", type = LOG_TYPE.UPDATE)
    @PostMapping(value = "updateDepartment")
    @ResponseBody
    public JsonUtil updateDepartment(SysDepartment department, String role[]) {
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.setFlag(false);
        if (department == null) {
            jsonUtil.setMsg("获取数据失败");
            return jsonUtil;
        }
        try {
            SysDepartment oldDepartment = departmentService.selectByPrimaryKey(department.getId());
            BeanUtil.copyNotNullBean(department, oldDepartment);
            departmentService.updateByPrimaryKeySelective(oldDepartment);
            jsonUtil.setFlag(true);
            jsonUtil.setMsg("修改成功");
        } catch (MyException e) {
            e.printStackTrace();
        }
        return jsonUtil;
    }

    /**
     * 验证部门是否存在
     */
    @GetMapping(value = "checkDepartment")
    @ResponseBody
    public JsonUtil checkDepartment(String departmentname, HttpServletRequest req) {
        JsonUtil j = new JsonUtil();
        j.setFlag(Boolean.FALSE);
        if (StringUtils.isEmpty(departmentname)) {
            j.setMsg("获取数据失败");
            return j;
        }
        int result = departmentService.checkDepartment(departmentname);
        if (result > 0) {
            j.setMsg("部门已存在");
            return j;
        }
        j.setFlag(true);
        return j;

    }
    
    /**
     * 获取部门列表
     * @param department
     * @param role
     * @return
     */
    @ApiOperation(value = "/showDepartmentAllList", httpMethod = "GET", notes = "获取部门列表")
    @GetMapping(value = "showDepartmentAllList")
    @ResponseBody
    public JsonUtil showDepartmentAllList() {
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.setFlag(false);
        
        try {
        	List<SysDepartment> depts = departmentService.showMenuJsonList();
            
            jsonUtil.setFlag(true);
            jsonUtil.setMsg("查询部门列表成功");
            jsonUtil.setData(depts);
        } catch (MyException e) {
            e.printStackTrace();
        }
        
        return jsonUtil;
    }

}
