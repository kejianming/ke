package com.nfdw.controller;

import com.github.pagehelper.Page;
import com.nfdw.base.controller.BaseController;
import com.nfdw.base.service.DictionaryService;
import com.nfdw.core.annotation.Log;
import com.nfdw.entity.PageData;
import com.nfdw.util.JsonUtil;
import com.nfdw.util.ReType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequestMapping("/systemDictionary")
@Controller
public class DitigalDictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping(value = "/getDictionary")
    public String getDictionary(){
        return "/system/dictionary/dictionaryList";
    }

    @GetMapping("/showSystemDictionaryList")
    @ResponseBody
    public ReType finddictionaryList(Model model, String page, String limit){
        PageData pd=this.getPageData(page,limit);

        Page<PageData> tPage = null;
        try {
            tPage= dictionaryService.findSystemDictionaryList(pd);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReType(tPage.getTotal(), tPage);
    }

    @GetMapping("/showAddDictionary")
    public String showAddDictionary(){
        return "/system/dictionary/add-dictionary";
    }

    @PostMapping(value = "/del", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Log(desc = "字典管理:删除字典", type = Log.LOG_TYPE.DEL)
    public JsonUtil deleteDictionary(HttpServletRequest request, HttpServletResponse response){
        JsonUtil j = new JsonUtil();
        String msg = "删除成功";
        try {

            PageData pd=this.getPageData();
            String ids =(String) pd.get("ids[]");
            if(ids==null||ids==""){
                ids =(String) pd.get("ids");
            }
            dictionaryService.deleteDictionary(pd, ids);
            //writeJson(response, null, ResultCodes.successCode, "删除成功！");
        } catch (Exception e) {
            msg = "删除失败";
            j.setFlag(false);
            //log.error("删除设备入网检测单异常！", e);
            //writeJson(response, null, ResultCodes.exceptionResult, "服务异常，请稍后再试！");
        }


        j.setMsg(msg);
        return j;
    }
    @GetMapping(value = "updateDictionary")
    public String updateDictionary(String id, Model model, boolean detail) throws Exception {
        PageData pd=this.getPageData();
        if (StringUtils.isNotEmpty(String.valueOf(id))) {
            Page<PageData> systemDictionaryList = dictionaryService.findSystemDictionaryList(pd);
            model.addAttribute("systemDictionaryList", systemDictionaryList.get(0));
        }
        return "/system/dictionary/update-dictionary";
    }

    @PostMapping(value = "/saveOrUpdateDictionary")
    @ResponseBody
    @Log(desc = "字典管理:新增/修改字典", type = Log.LOG_TYPE.UPDATE)
    public JsonUtil saveOrUpdateDictionary(String id, HttpServletResponse response){

        PageData pd=this.getPageData();
        pd.put("id",id);
        JsonUtil j = new JsonUtil();
        String msg = "修改成功";

        try {
            dictionaryService.saveOrUpdateDictionary(pd);
            j.setFlag(true);
        } catch (Exception e) {
            msg = "修改失败";
            j.setFlag(false);
            e.printStackTrace();

        }
        j.setMsg(msg);
        return j;
    }


}
