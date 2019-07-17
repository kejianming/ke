package com.nfdw.service.work;

import java.util.List;

import com.nfdw.entity.PageData;
import com.nfdw.pojo.Activiti;
import com.nfdw.pojo.EchartsBasicBean;
import com.nfdw.pojo.EchartsMapBean;

public interface WorkFlowService {

	Activiti goActiviti(Activiti activiti, PageData pd, String key) throws Exception;

	List<PageData> findCardOrderPageList(PageData pd) throws Exception;

	void nextWork(PageData pd) throws Exception;

	PageData addCardProcessInfo(PageData pd) throws Exception;

	void returnWork(PageData pd) throws Exception;

	PageData queryCardFlowData(PageData pd) throws Exception;

	void closeWork(PageData pd) throws Exception;
}
