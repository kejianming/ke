package com.nfdw.base.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.nfdw.base.service.DictionaryService;
import com.nfdw.common.ResultCodes;
import com.nfdw.entity.Dictionary;
import com.nfdw.entity.PageData;
import com.nfdw.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {


    private int pageNum=0;
    private int pageSize=10;

    @Resource
    private DictionaryService dictionaryService;

    @GetMapping(value = "/findDictionaryList")
    public void findDictionaryList(HttpServletResponse response) throws Exception{
        PageData pd=this.getPageData();
        Page<PageData> dictionarys = (Page<PageData>) dictionaryService.findDictionaryList(pd);
        for (int i = 0; i < dictionarys.size(); i++) {
            PageData pds = dictionarys.get(i);
            pds.put("createtime", DateUtils.date3Str((java.util.Date) pds.get("createtime")));
            pds.put("pageNum", dictionarys.getPageNum());
            pds.put("count", dictionarys.getTotal());
            pds.put("pageSize", dictionarys.getPageSize());
            pds.put("totalPage", dictionarys.getPages());
        }
        String json = generateJsonPageData(dictionarys);
        writePlainText(response, json);
    }
    
	@PostMapping(value = "/selectDictionary", produces = "text/String;charset=UTF-8")
	@ResponseBody
	public String selectDictionary(Dictionary dictionary) {
		List<Dictionary> listD = dictionaryService.selectDictionary(dictionary);
		String json = null;
		json = JSON.toJSONString(listD);
		return json;
	}



}
