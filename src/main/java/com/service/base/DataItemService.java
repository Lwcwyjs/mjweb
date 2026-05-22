package com.service.base;

import java.util.List;

import com.commons.utils.PageInfo;

public interface DataItemService {
	
	void insert(String sql);
	
	void insert(String sTableName, String fieldOut, String keyField, Object bean);
	
	void update(String sql);
	
	void update(String sTableName, String fieldOut, String keyField, Object bean);
	
	void delete(String sql);
	
	void delete(String sTableName, String fieldOut, String keyField, Object bean);
	
	Object selectObject(String sql,Class clz);
	
	Object selectObject(String sTableName, String fieldOut, String keyField, Object bean);
	
	<T> T selectObj(String sql,Class<T> clz);
	
	List<Object> selectObjects(String sql,Class clz);
	
	List<Object> selectObjects(String sTableName, String fieldOut, String keyField, Object bean);
	
	void selectPageObjects(PageInfo pageInfo,String sTableName, String fieldOut, String keyField, Object bean);
	
	void selectPageObjects(PageInfo pageInfo,String sTableName, String fieldOut, String keyField, String strWhere, Object bean);
	
	<T> List<T> selectObjs(String sql,Class<T> clz);
	
	Integer selectCount(String sql);
	
	Integer selectCount(String sTableName, String fieldOut, String keyField, Object bean);

}
