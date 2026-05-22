package com.service.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.sql.SQLBean;
import com.commons.utils.PageInfo;
import com.commons.utils.ReflectGetValue;
import com.mapper.base.DataItemMapper;

@Service
public class DataItemServiceImpl implements DataItemService {
	
	private final Logger logger=LoggerFactory.getLogger(getClass());

	@Autowired
	DataItemMapper dItemMapper;

	@Override
	public void insert(String sql) {
		dItemMapper.insert(sql);
	}
	
	@Override
	public void insert(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateInsertSQL(bean);
		dItemMapper.insert(sql);
	}

	@Override
	public void update(String sql) {
		dItemMapper.update(sql);
	}
	
	@Override
	public void update(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateUpdateSQL(bean);
		dItemMapper.update(sql);
	}
	
	@Override
	public void delete(String sql) {
		dItemMapper.delete(sql);
	}
	
	@Override
	public void delete(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateDeleteSQL(bean);
		dItemMapper.delete(sql);
	}


	@Override
	public Object selectObject(String sql, Class clz) {
		Map<Object, Object> map = dItemMapper.selectObject(sql);
		Object obj = null;
		try {
			if (map!=null) {
				obj = clz.newInstance();
				ReflectGetValue.setFieldValue(map, obj);
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Override
	public Object selectObject(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateSelectSQL(bean);
		return selectObject(sql,bean.getClass());
	}

	@Override
	public List<Object> selectObjects(String sql, Class clz) {
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		List<Object> objects=new ArrayList<Object>();
        try {
			for (Map<Object, Object> map : maps) {
				Object obj=clz.newInstance();
				ReflectGetValue.setFieldValue(map, obj);
				objects.add(obj);
			}
		} catch (Exception e) {
			
		}		
		return objects;
	}

	public List<Object> selectObjects(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateSelectSQL(bean);
		return selectObjects(sql,bean.getClass());
	}
	
	@Override
	public void selectPageObjects(PageInfo pageInfo,String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateSelectPageSQL(bean, pageInfo);
		String sqlCount=sqlBean.generateSelectCountSQL(pageInfo);
		
		pageInfo.setRows(selectObjects(sql,bean.getClass()));
		pageInfo.setTotal(selectCount(sqlCount));
	}
	
	@Override
	public void selectPageObjects(PageInfo pageInfo,String sTableName, String fieldOut, String keyField,String strWhere, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateSelectPageSQL(bean, pageInfo,strWhere);
		String sqlCount=sqlBean.generateSelectCountSQL(pageInfo,strWhere);
		
		pageInfo.setRows(selectObjects(sql,bean.getClass()));
		pageInfo.setTotal(selectCount(sqlCount));
	}
	
	@Override
	public <T> T selectObj(String sql, Class<T> clz) {
		Map<Object, Object> map = dItemMapper.selectObject(sql);
		T t=null;
		try {
			if (map!=null) {
				t = clz.newInstance();
				ReflectGetValue.setFieldValue(map, t);
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public <T> List<T> selectObjs(String sql, Class<T> clz) {
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		List<T> t=new ArrayList<T>();
        try {
			for (Map<Object, Object> map : maps) {
				Object obj=clz.newInstance();
				ReflectGetValue.setFieldValue(map, obj);
				t.add((T) obj);
			}
		} catch (Exception e) {
			
		}		
		return t;
	}

	@Override
	public Integer selectCount(String sql) {
		return dItemMapper.selectCount(sql);
	}
	
	@Override
	public Integer selectCount(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean=new SQLBean(sTableName, fieldOut, keyField);
		String sql=sqlBean.generateSelectCountSQL(bean);
		return dItemMapper.selectCount(sql);
	}

}
