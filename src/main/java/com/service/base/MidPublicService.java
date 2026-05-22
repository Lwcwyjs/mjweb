package com.service.base;

import java.util.List;

public interface MidPublicService {

    void insert(String sql);

    void insert(String sTableName, String fieldOut, String keyField, Object bean);

    void update(String sql);

    void update(String sTableName, String fieldOut, String keyField, Object bean);

    void delete(String sql);

    void delete(String sTableName, String fieldOut, String keyField, Object bean);

    <T> T selectObj(String sql, Class<T> clz);

    <T> T selectObj(String sTable, String fieldOut, String keyField, Object bean);

    <T> List<T> selectObjs(String sql, Class<T> clz);

    <T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, Object bean);

    Integer selectCount(String sql);

    Integer selectSum(String sql);
}
