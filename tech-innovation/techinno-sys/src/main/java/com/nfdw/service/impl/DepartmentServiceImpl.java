package com.nfdw.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.BaseMapper;
import com.nfdw.base.service.impl.BaseServiceImpl;
import com.nfdw.entity.SysDepartment;
import com.nfdw.mapper.SysDepartmentMapper;
import com.nfdw.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<SysDepartment, String> implements DepartmentService {

    @Autowired
    private SysDepartmentMapper departmentDao;


    @Override
    public BaseMapper<SysDepartment, String> getMappser() {
        return departmentDao;
    }

    @Override
    public int insert(SysDepartment department) {
        return departmentDao.insert(department);
    }


    @Override
    public List<SysDepartment> showMenuJsonList() {
        List<SysDepartment> sysDepartments = departmentDao.getMenuJsonList();
        return sysDepartments;
    }

    @Override
    public int checkDepartment(String departmentname) {
        return  departmentDao.checkDepartment(departmentname);
    }

//    @Override
//    public JSONArray getTreeUtil(String departmentId) {
//        TreeUtil treeUtil = null;
//        List<SysDepartment> sysMenus = selectAll();
//        List<SysDepartment> supers = sysMenus.stream().filter(sysMenu ->
//                StringUtils.isEmpty(sysMenu.getPId()))
//                .collect(Collectors.toList());
//        sysMenus.removeAll(supers);
//        supers.sort(Comparator.comparingInt(SysMenu::getOrderNum));
//        JSONArray jsonArr = new JSONArray();
//        for (SysMenu sysMenu : supers) {
//            treeUtil = getChildByTree(sysMenu, sysMenus, 0, null, roleId);
//            jsonArr.add(treeUtil);
//        }
//        return jsonArr;
//    }
}
