package com.nfdw.base.service;

import com.github.pagehelper.Page;
import com.nfdw.entity.Dictionary;
import com.nfdw.entity.PageData;

import java.util.List;


public interface DictionaryService {
	public List<Dictionary> selectDictionary(Dictionary dictionary);

	public boolean addDictionary(Dictionary dictionary);

	public boolean editDictionary(Dictionary dictionary);

	public boolean delDictionary(int id);

	public List<Dictionary> selectDictionaryPage(Dictionary dictionary);

	public int getPage(Dictionary dictionary);
	
	public  List<PageData> selectItemsByGroup(PageData group);

	public List<PageData> selectDictionaryView(PageData pd);

	public void editDictionaryInfo(PageData pd);

	public void addDictionaryInfo(PageData pd);

	public List<PageData> findDictionaryList(PageData pd);

	Page<PageData> findSystemDictionaryList(PageData pd);

	void deleteDictionary(PageData pd, String ids) throws Exception;

	void saveOrUpdateDictionary(PageData pd) throws Exception;
	List<PageData> findGroup(PageData pd) throws  Exception;
}
