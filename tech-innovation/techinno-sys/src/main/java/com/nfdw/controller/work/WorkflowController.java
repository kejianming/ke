package com.nfdw.controller.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.nfdw.base.controller.BaseController;
import com.nfdw.common.ResultCodes;
import com.nfdw.entity.PageData;
import com.nfdw.pojo.Activiti;
import com.nfdw.pojo.EchartsBasicBean;
import com.nfdw.pojo.EchartsMapBean;
import com.nfdw.service.work.WorkFlowService;
import com.nfdw.util.JsonUtil;
import com.nfdw.util.ReType;
import com.nfdw.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 首页管理
 */
//@Api(value="user")
@Slf4j
@Controller
@RequestMapping(value = "/workflow")
public class WorkflowController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);
    //private static final Logger
	@Resource
    private WorkFlowService  workFlowService;
	
	@RequestMapping(value = "/showShenqingList")
    public String showShenqing(Activiti activiti,Model model) {
		 PageData pd = this.getPageData();
        return "/workflow/shenqingList";
    }
	@RequestMapping(value = "/showCardSq")
    public String showHome(Activiti activiti,Model model) {
		PageData pd = this.getPageData();
		try {
			if (!"".equals(pd.getString("id"))) {
				
				PageData activitiDo=workFlowService.queryCardFlowData(pd);
				
				if (activitiDo != null) {
					pd.put("processId", activitiDo.getString("processId"));
				}
				
				model.addAttribute("activitiDo", activitiDo);
			}
			activiti=workFlowService.goActiviti(activiti,pd,"ksq");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("启动流程实例异常！", e);
		}
		model.addAttribute("activiti", activiti);
        return "/workflow/sqshView";
    }
	@GetMapping(value = "/findCardOrderList")
	@ResponseBody
    public ReType findCardOrderList(HttpServletResponse response,HttpServletRequest re) throws Exception{
		 PageData pd=this.getPageData();
		 
	     Page<PageData> users=null;
		 users = (Page<PageData>) workFlowService.findCardOrderPageList(pd);
		 return new ReType((long)pd.get("total"), users);
	}
	@RequestMapping(value = "/getCardOrderInfo")
    private void getCardOrderInfo(HttpServletResponse response) {
		 PageData pd=this.getPageData();
		 String magemap="sesson";
		 try {
			 if ("next".equals(pd.getString("type"))) {
				 workFlowService.nextWork(pd);
			 }
			 if ("return".equals(pd.getString("type"))) {
				 workFlowService.returnWork(pd);
			 }
			 if ("close".equals(pd.getString("type"))) {
				 workFlowService.closeWork(pd);
			 }
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			 magemap="errer";
			log.error("转下一步发生异常！", e);
		 }
		  writeJson(response, magemap);
	}
	@RequestMapping(value = "/addCardWorkInfo")
    private void addCardWorkInfo(HttpServletResponse response) {
		 PageData pd=this.getPageData();
		 String magemap="sesson";
		 try {
				workFlowService.addCardProcessInfo(pd);
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			 magemap="errer";
			log.error("保存发生异常！", e);
		 }
		  writeJson(response, magemap);
	}
}
