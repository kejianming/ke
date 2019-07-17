package com.nfdw.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.ObjectUtils;

import com.nfdw.base.service.DictionaryService;
import com.nfdw.common.Dict;
import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.PageData;

import org.apache.shiro.session.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceUtil {


    public static CurrentUser getCurrentUser(){
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = null;
        CurrentUser userData = null;
        if(currentSubject != null){
            session = currentSubject.getSession();
            if(session != null){
                userData = (CurrentUser)session.getAttribute("curentUser");
            }
        }
        return userData;
    }
    
     public static Map<String, List<PageData>> dictGroupMap = new HashMap<String, List<PageData>>();
    
    public static List<PageData> getDictsByGroup(String group) throws Exception {
        if (!dictGroupMap.containsKey(group)) {
            initDictByGroup(group);
        }
        return dictGroupMap.get(group);
    }
    
    public static void deleDictsByGroup() throws Exception {
        dictGroupMap.clear();
    }
    
    public static PageData getDict(String group, String itemCode)
            throws Exception {
        if (!dictGroupMap.containsKey(group)) {
            return null;
           // initDictByGroup(group);
        }
        List<PageData> dictGroup = dictGroupMap.get(group);
        PageData rs =null;
        if (!ObjectUtils.isEmpty(dictGroup)) {
            for (PageData pd : dictGroup) {
                String dictItemCode = (String) pd.get(Dict.ITEMCODE);
                if (itemCode != null && itemCode.equals(dictItemCode)) {
                    rs = pd;
                    break;
                }
            }
        }
        return rs;
    }
    
    public static String getDictValue(String group, String itemCode)
            throws Exception {
       PageData pd= getDict(group, itemCode);
        if (pd!= null) {
            return (String) pd.get("dvalue");
        }
        return null;
    }
    
    private static void initDictByGroup(String group) throws Exception {
        DictionaryService dict = (DictionaryService) SpringUtil.getBean("dictionaryServiceImpl");
        //DictionaryService dict = ApplicationContextUtil.getBean("dictionaryService");
        PageData pd = new PageData();
        pd.put(Dict.GROUP, group);
        List<PageData> dicts = (List<PageData>) dict.selectItemsByGroup(pd);
        if (!ObjectUtils.isEmpty(dicts)) {
            dictGroupMap.put(group, dicts);
        }
    }
    
    public static void refreshDictByGroup(String group) throws Exception {
        initDictByGroup(group);
    }
    
    /**
     * @Desc 根据Group值及ItemValue返回匹配的code值
     * @param group
     * @return
     * @throws Exception
     */
    public static String getValueArrayByGroup(String group, String itemValue)
            throws Exception {
        String[] contentArr = null;
        String resultCode = "";
        if (!dictGroupMap.containsKey(group)) {
            initDictByGroup(group);
        }
        List<PageData> dictLists = dictGroupMap.get(group);
        if ((null != dictLists) && (dictLists.size() > 0)) {
            contentArr = new String[dictLists.size()];
            for (int i = 0; i < dictLists.size(); i++) {
                PageData pg = dictLists.get(i);
                String itemVal = String.valueOf(pg.get(Dict.ITEMVALUE));
                if (StringUtils.equals(itemValue, itemVal)) {
                    resultCode = String.valueOf(pg.get(Dict.ITEMCODE));
                }
            }
        }
        return resultCode;
    }
//    public static User getCurrentUser(){
//  		Subject currentSubject = SecurityUtils.getSubject();
//  		Session session = null;
//  		User userData = null;
//  		if(currentSubject != null){
//  			session = currentSubject.getSession();
//  			if(session != null){
//  				userData = (User)session.getAttribute("user");
//  			}
//  		}
//  		return userData;
//  	}
}
