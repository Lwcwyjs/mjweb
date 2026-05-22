package com.service.base;

import com.commons.sql.SQLBean;
import com.commons.utils.ReflectGetValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MidPublicServiceImpl implements MidPublicService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setSlaveDataSource(@Qualifier("slave-dataSource") DataSource slaveDataSource) {
        this.jdbcTemplate = new JdbcTemplate(slaveDataSource);
    }

    @Override
    public void insert(String sql) {
        jdbcTemplate.update(sql);
    }

    @Override
    public void insert(String sTableName, String fieldOut, String keyField, Object bean) {
        SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
        jdbcTemplate.update(sqlBean.generateInsertSQL(bean));
    }

    @Override
    public void update(String sql) {
        jdbcTemplate.update(sql);
    }

    @Override
    public void update(String sTableName, String fieldOut, String keyField, Object bean) {
        SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
        jdbcTemplate.update(sqlBean.generateUpdateSQL(bean));
    }

    @Override
    public void delete(String sql) {
        jdbcTemplate.update(sql);
    }

    @Override
    public void delete(String sTableName, String fieldOut, String keyField, Object bean) {
        SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
        jdbcTemplate.update(sqlBean.generateDeleteSQL(bean));
    }

    @Override
    public <T> T selectObj(String sql, Class<T> clz) {
        List<Map<Object, Object>> maps = queryForMaps(sql);
        Map<Object, Object> map = maps.size() > 0 ? maps.get(0) : null;
        T t = null;
        try {
            if (map != null) {
                t = clz.newInstance();
                ReflectGetValue.setFieldValue(map, t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public <T> T selectObj(String sTable, String fieldOut, String keyField, Object bean) {
        SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField);
        String sql = sqlBean.generateSelectSQL(bean);
        return selectObj(sql, (Class<T>) bean.getClass());
    }

    @Override
    public <T> List<T> selectObjs(String sql, Class<T> clz) {
        List<Map<Object, Object>> maps = queryForMaps(sql);
        List<T> list = new ArrayList<T>();
        try {
            for (Map<Object, Object> map : maps) {
                T obj = clz.newInstance();
                ReflectGetValue.setFieldValue(map, obj);
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public <T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, Object bean) {
        SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
        String sql = sqlBean.generateSelectSQL(bean);
        return selectObjs(sql, (Class<T>) bean.getClass());
    }

    @Override
    public Integer selectCount(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Integer selectSum(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private List<Map<Object, Object>> queryForMaps(String sql) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<Map<Object, Object>> result = new ArrayList<Map<Object, Object>>();
        for (Map<String, Object> row : rows) {
            Map<Object, Object> converted = new LinkedHashMap<Object, Object>();
            converted.putAll(row);
            result.add(converted);
        }
        return result;
    }
}
