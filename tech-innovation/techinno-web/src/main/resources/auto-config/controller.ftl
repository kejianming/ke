package com.nfdw.controller;

import com.github.pagehelper.PageHelper;
import com.nfdw.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import ${tableClass.fullClassName};
import ${tableClass.packageName?replace("entity","mapper")}.${tableClass.shortClassName}Mapper;
import com.nfdw.util.ReType;
import java.util.List;
import com.nfdw.util.JsonUtil;
@Controller
@RequestMapping(value = "/${tableClass.lowerCaseName}")
public class ${tableClass.shortClassName}Controller extends BaseController {

    @Autowired
    ${tableClass.shortClassName}Mapper ${tableClass.lowerCaseName}Mapper;

    @GetMapping(value = "show${tableClass.shortClassName}ListModal")
    public String show${tableClass.shortClassName}ListModal(Model model) {
    return "/${tableClass.lowerCaseName}/${tableClass.lowerCaseName}List";
    }

    @GetMapping(value = "${tableClass.lowerCaseName}List")
    @ResponseBody
    public ReType ${tableClass.lowerCaseName}List(int page, int limit,${tableClass.shortClassName} ${tableClass.lowerCaseName}){
    PageHelper.startPage(page, limit);
    Example example = getExample(${tableClass.lowerCaseName});

    ${tableClass.lowerCaseName}Mapper.selectByExample(example);
    List<${tableClass.shortClassName}> list = ${tableClass.lowerCaseName}Mapper.selectByExample(example);
    int count = ${tableClass.lowerCaseName}Mapper.selectCountByExample(example);
    return new ReType(count,list);
    }

    @GetMapping(value = "show${tableClass.shortClassName}Modal")
    @ResponseBody
    public String add(${tableClass.shortClassName} ${tableClass.lowerCaseName}){
    ${tableClass.lowerCaseName}Mapper.insertSelective(${tableClass.lowerCaseName});
    return "success";
    }

    @GetMapping(value = "showAddModal")
    public String showAddModal(){
    return "/${tableClass.lowerCaseName}/addModal";
    }


    @RequestMapping(value = "deleteAll")
    @ResponseBody
    public JsonUtil deleteAll(String ids){
    JsonUtil j = new JsonUtil();
    try{
    ${tableClass.lowerCaseName}Mapper.deleteByIds(ids);
    j.setMsg("success");
    }catch (Exception e){
    j.setMsg("false");
    e.printStackTrace();
    }
    return j;
    }

    @GetMapping(value = "showUpdateModal")
    public String showUpdateModal(){
    return "/${tableClass.lowerCaseName}/updateModal";
    }

    @GetMapping(value = "update")
    @ResponseBody
    public String update(${tableClass.shortClassName} ${tableClass.lowerCaseName}){
    ${tableClass.lowerCaseName}Mapper.updateByPrimaryKeySelective(${tableClass.lowerCaseName});
    return "success";
    }

}
