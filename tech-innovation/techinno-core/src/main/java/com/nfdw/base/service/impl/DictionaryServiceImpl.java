package com.nfdw.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nfdw.base.dao.impl.BaseService;
import com.nfdw.base.service.DictionaryService;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.Dictionary;
import com.nfdw.entity.PageData;
import com.nfdw.mapper.DictionaryMapper;
import com.nfdw.util.DateUtils;
import com.nfdw.util.ServiceUtil;
import com.nfdw.util.StringUtils;
import com.nfdw.util.Tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DictionaryServiceImpl extends BaseService implements DictionaryService {


	/**
	 * 所属区域类型
	 */
	private static final String REGION_TYPE = "region_type";
	/**
	 * 市级区域代码
	 */
	private static final String REGION_CITY_LEVEL = "0";
	
    @Autowired
    private DictionaryMapper dictionaryMapper;
    
    @Override
    public List<Dictionary> selectDictionary(Dictionary dictionary) {
        
    	if (null != dictionary && REGION_TYPE.equals(dictionary.getDgroup())) {
    		// 将筛选条件设为当前用户所属的区域，以便在页面的下拉框只显示当前用户所属区域
    		
    		CurrentUser user = ServiceUtil.getCurrentUser();
            if(user != null && !REGION_CITY_LEVEL.equals(user.getGzarea())) {
            	// 控制市级可以显示所有区域的下来菜单，区局只可以显示当前区域的下拉菜单
            	dictionary.setCode(user.getGzarea());
            }
            
    	}
    	
        return dictionaryMapper.selectDictionary(dictionary);
    }
    
    @Override
    public boolean addDictionary(Dictionary dictionary) {
        // TODO Auto-generated method stub
        return dictionaryMapper.addDictionary(dictionary);
    }
    
    @Override
    public boolean editDictionary(Dictionary dictionary) {
        // TODO Auto-generated method stub
        return dictionaryMapper.editDictionary(dictionary);
    }
    
    @Override
    public boolean delDictionary(int id) {
        // TODO Auto-generated method stub
        return dictionaryMapper.delDictionary(id);
    }
    
    @Override
    public List<Dictionary> selectDictionaryPage(Dictionary dictionary) {
        // TODO Auto-generated method stub
        return dictionaryMapper.selectDictionaryPage(dictionary);
    }
    
    @Override
    public int getPage(Dictionary dictionary) {
        // TODO Auto-generated method stub
        return dictionaryMapper.getPage(dictionary);
    }
    
    @Override
    public List<PageData> selectItemsByGroup(PageData group) {
        // TODO Auto-generated method stub
        List<PageData> dictlist = dictionaryMapper.selectItemsByGroup(group);
        return dictlist;
    }

	@Override
	public List<PageData> selectDictionaryView(PageData pd) {
		// TODO Auto-generated method stub
		List<PageData> pds= dictionaryMapper.selectDictionaryView(pd);
		return pds;
	}

	@Override
	public void editDictionaryInfo(PageData pd) {
		// TODO Auto-generated method stub
		dictionaryMapper.editDictionaryInfo(pd);
	}

	@Override
	public void addDictionaryInfo(PageData pd) {
		// TODO Auto-generated method stub
		dictionaryMapper.addDictionaryInfo(pd);
	}

	@Override
	public List<PageData> findDictionaryList(PageData pd) {
		// TODO Auto-generated method stub
		String pageNum ="1";
		if (Tools.notEmpty(pd.getString("pageNum"))) {
			pageNum =  pd.getString("pageNum");
		}
		Integer pagenums =Integer.valueOf(pageNum);
		int pageSize = 10;
		if (Tools.notEmpty(pd.getString("pageSize"))) {
			pageSize =  Integer.parseInt(pd.getString("pageSize"));
		}
		PageHelper.startPage(pagenums, pageSize,true);
		List<PageData> list= dictionaryMapper.findDictionaryList(pd);
		
		return list;
	}

	@Override
	public Page<PageData> findSystemDictionaryList(PageData pd) {
	Page<PageData> SystemDictionaryList =null;
		try {
			String page = "1";
			if (Tools.notEmpty(pd.getString("page"))) {
				page = pd.getString("page");
			}
			Integer pages = Integer.valueOf(page);
			int limit = 5;
			if (Tools.notEmpty(pd.getString("limit"))) {
				limit = Integer.parseInt(pd.getString("limit"));
			}
			PageHelper.startPage(pages, limit, true);
			SystemDictionaryList=(Page<PageData>)dao.findForList("com.nfdw.mapper.DictionaryMapper.findSystemDictionaryList", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SystemDictionaryList;

	}

	@Override
    public void deleteDictionary(PageData pd, String ids) throws Exception {
		List<String> idList = new ArrayList<>();
		String[] splitList = ids.split(",");
		for (int i = 0; i < splitList.length; i++) {
			idList.add(splitList[i]);
		}
		dao.delete("com.nfdw.mapper.DictionaryMapper.deleteDictionary", idList);
	}

	@Override
    public void saveOrUpdateDictionary(PageData pd) throws Exception {
		String curDate = DateUtils.getCurDate();
		pd.put("updatetime", curDate);
		if (StringUtils.isBlank((String.valueOf(pd.get("id"))))) {
			dao.save("com.nfdw.mapper.DictionaryMapper.saveDictionary", pd);
		} else {
			dao.save("com.nfdw.mapper.DictionaryMapper.updateDictionary", pd);
		}

	}

	@Override
	public List<PageData> findGroup(PageData pd) throws Exception {

    	return  dictionaryMapper.findGroup(pd);
	}
}
