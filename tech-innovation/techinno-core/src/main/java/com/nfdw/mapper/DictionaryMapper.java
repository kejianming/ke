package com.nfdw.mapper;

import java.util.List;

import com.nfdw.entity.Dictionary;
import com.nfdw.entity.PageData;


public interface DictionaryMapper {
	public List<Dictionary> selectDictionary(Dictionary dictionary);

	public boolean addDictionary(Dictionary dictionary);

	public boolean editDictionary(Dictionary dictionary);

	public boolean delDictionary(int id);

	public List<Dictionary> selectDictionaryPage(Dictionary dictionary);

	public int getPage(Dictionary dictionary);
	
	public List<PageData> selectItemsByGroup(PageData group);

	public List<PageData> selectDictionaryView(PageData pd);

	public void editDictionaryInfo(PageData pd);

	public void addDictionaryInfo(PageData pd);

	public List<PageData> findDictionaryList(PageData pd);
	List<PageData> findGroup(PageData pd) throws  Exception;

}
