package com.nfdw.service.impl.work;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.activiti.engine.form.FormProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.nfdw.activiti.util.ProcessUtils;
import com.nfdw.base.dao.impl.BaseService;
import com.nfdw.common.Constants;
import com.nfdw.core.shiro.ShiroUtil;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.PageData;
import com.nfdw.entity.SysDepartment;
import com.nfdw.entity.SysUser;
import com.nfdw.mapper.SysDepartmentMapper;
import com.nfdw.pojo.Activiti;
import com.nfdw.pojo.EchartsBasicBean;
import com.nfdw.pojo.EchartsMapBean;
import com.nfdw.pojo.EchartsMapData;
import com.nfdw.service.work.WorkFlowService;
import com.nfdw.util.CollectionUtils;
import com.nfdw.util.CreateSerialNo;
import com.nfdw.util.DateUtils;
import com.nfdw.util.ServiceUtil;
import com.nfdw.util.StringUtils;

@Service
@Transactional
public class WorkFlowServiceImpl extends BaseService implements WorkFlowService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowServiceImpl.class);
	 @Autowired
	 ProcessUtils processUtils;
	 
	 @Autowired
	 SysDepartmentMapper sysDepartmentMapper;
	@Override
	public Activiti goActiviti(Activiti activiti,PageData pd, String key) throws Exception {
		// TODO Auto-generated method stub
		CurrentUser sysUser = ShiroUtil.getCurrentUse();
		 activiti.setCbrId(sysUser.getId());
		if (StringUtils.isNotEmpty(pd.getString("processId"))) {
			
			processUtils.init(pd.getString("processId"));
			activiti.setProcessId(pd.getString("processId"));
			
		} else {
			processUtils.init(key, pd.getString(Constants.LOGIN_RECORD_ID));
			activiti.setProcessId(processUtils.getProcessInstance().getId());
		}
		SysDepartment sysDepartment = sysDepartmentMapper.selectByPrimaryKey(sysUser.getDepartmentId());
		Map<String, FormProperty> formPropertyMap = processUtils.getTaskFormData(activiti, sysDepartment.getId());
		activiti.setFormPropertyMap(formPropertyMap);
		return activiti;
	}
	@Override
	public void nextWork(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isNotEmpty(pd.getString("processId"))) {
			processUtils.init(pd.getString("processId"));
		}
		this.addCardProcessInfo(pd);
		processUtils.nextProcess(pd.getString("fzrId"));
	}
	@Override
	public void closeWork(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isNotEmpty(pd.getString("processId"))) {
			processUtils.init(pd.getString("processId"));
		}
		processUtils.close();
	}
	@Override
	public void returnWork(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isNotEmpty(pd.getString("processId"))) {
			processUtils.init(pd.getString("processId"));
		}
		Activiti activiti=new Activiti();
		activiti.setFzrId(pd.getString("fzrId"));
		activiti.setReturnActiviti(pd.getString("returnActiviti"));
	    processUtils.Withdraw(null, activiti);
	}
	@Override
	public List<PageData> findCardOrderPageList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		List<PageData> users= (List<PageData>) dao.findForList("WorkFlowMapper.queryCardFlowList", pd);
		 for (int i = 0; i < users.size(); i++) {
				PageData pds = users.get(i);
				if (StringUtils.isNotEmpty(pds.get("status")+"")) {
					pds.put("statuss", ServiceUtil.getDictValue("lc_type", pds.get("status")+""));
				}
			}
		return users;
	}
	@Override
	public PageData queryCardFlowData(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData users= (PageData) dao.findForObject("WorkFlowMapper.queryCardFlowData", pd);
		return users;
	}
	
	@Override
	public PageData addCardProcessInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		pd.put("code",CreateSerialNo.getNum());
		pd.put("requireman", pd.getString(Constants.USRE_NAME));
		pd.put("requiredate", pd.getString(Constants.LASTUDATE));
		pd.put("nowAssignId", pd.getString("fzrId"));
		dao.save("WorkFlowMapper.merge", pd);
		return pd;
	}
}
