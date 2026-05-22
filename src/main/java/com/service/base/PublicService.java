package com.service.base;

import com.commons.utils.PageInfo;

import java.util.List;
import java.util.Map;

public interface PublicService {

	void insert(String sql);

	void insert(String sTableName, String fieldOut, String keyField, Object bean);

	void insert(String sTableName, String fieldOut, String keyField, String encryptField, Object bean);
	
	void update(String sql);

	void update(String sTableName, String fieldOut, String keyField, Object bean);

	void delete(String sql);

	void delete(String sTableName, String fieldOut, String keyField, Object bean);

	Object selectObject(String sql, Class clz);

	Object selectObject(String sTableName, String fieldOut, String keyField, Object bean);

	<T> T selectObj(String sTable, String fieldOut, String keyField, Object bean);

	<T> T selectObj(String sql, Class<T> clz);

	List<Object> selectObjects(String sql, Class clz);

	List<Object> selectObjects(String sTableName, String fieldOut, String keyField, Object bean);

	<T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, Object bean);

	void selectPageObjects(PageInfo pageInfo, String sTableName, String fieldOut, String keyField, Object bean);

	void selectPageObjects(PageInfo pageInfo, String sTableName, String fieldOut, String keyField, String strWhere,
			Object bean);
    void selectPageObjects(PageInfo pageInfo,String sTableName,Object beanClass);
	void selectPageObjects(PageInfo pageInfo, String sTableName, String field, String strWhere, String strOrder);
	void selectPageObjectsNew(PageInfo pageInfo, String sTableName, String field, String strWhere);

	List<Map<Object, Object>> selectObjects(String sTableName, String fields, String strWhere, String strOrder);

	<T> List<T> selectObjs(String sql, Class<T> clz);

	Integer selectCount(String sql);

	Integer selectCount(String sTableName, String fieldOut, String keyField, Object bean);

	Integer selectSum(String sql);

	Integer selectSum(String sTableName, String fieldOut, String keyField, String sumField, Object bean);

	Object selectObject(String sTableName, String fieldOut, String keyField, String orderString, Object bean);

	List<Object> selectObjects(String sTableName, String fieldOut, String keyField, String orderString, Object bean);

	<T> T selectObj(String sTable, String fieldOut, String keyField, String orderString, Object bean);

	<T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, String orderString, Object bean);

	void update(String sTableName, String fieldOut, String keyField, String initWhere, Object bean);

	void update(String sTableName, String fieldOut, String keyField, String encryptField, String initWhere, Object bean);
	
	void delete(String sTableName, String fieldOut, String keyField, String initWhere, Object bean);

	Object selectObject(String sTableName, String fieldOut, String keyField, String orderString, String initWhere,
			Object bean);

	List<Object> selectObjects(String sTableName, String fieldOut, String keyField, String orderString,
			String initWhere, Object bean);

	<T> T selectObj(String sTable, String fieldOut, String keyField, String orderString, String initWhere, Object bean);

	<T> T selectObj(String sTable, String fieldOut, String keyField,String encryptField, String orderString, String initWhere, Object bean);
	
	<T> List<T> selectObjs(String sTable, String fieldOut, String keyField, String encryptField, String orderString,
			String initWhere, Object bean);
	<T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, String orderString, String initWhere,
			Object bean);

	Integer selectSum(String sTableName, String fieldOut, String keyField, String sumField, String initWhere,
			Object bean);

	Integer selectCount(String sTableName, String fieldOut, String keyField, String initWhere, Object bean);
	
    String getWhereOrgan(String organName,String organValue);


    /**
     * 接口操作日志 插入记录<BR/>
     * @author JiaoSiYuan<BR/>
     * @param interface_Condition 接口服务条件
     * @param interface_Result 接口服务结果 1:成功    0:失败
     * @param requester 请求方名称
     * @param terminal_Id 请求方标识，如IP
     * @return
     */
    void insertInterLog(String interface_Condition,String interface_Result,String requester,String terminal_Id);
     /**
     * 操作业务日志插入记录
     * @author JiaoSiYuan<BR/>
     * @param operateCondition 操作条件
     * @param operateType 操作类型 &nbsp;&nbsp;&nbsp;0:登录 1:查询2:新增 3:修改 4删除<BR/>
     * @param operateResult 操作返回值 &nbsp;&nbsp;&nbsp; 1:成功 0:失败
     * @param operatemodule 模块名称
     * @return
     */
    void insertLogInfo(String operateCondition,String operateType,String operateResult,String operatemodule);
    /**
    * 操作业务日志插入记录
    * @author JiaoSiYuan<BR/>
    * @param operateCondition 操作条件
    * @param operateType 操作类型 &nbsp;&nbsp;&nbsp;0:登录 1:查询2:新增 3:修改 4删除<BR/>
    * @param operateResult 操作返回值 &nbsp;&nbsp;&nbsp; 1:成功 0:失败
    * @param userid 用户ID
    * @param username 用户名
    * @param organ 机构ID
    * @param operatemodule 模块名称
    * @return
    */
   void insertLogInfo(String operateCondition,String operateType,String operateResult,Long userid,String username,String organ,String operatemodule);


    public void insertInvokeLog(String jkid ,String interface_Condition, String interface_Result, String requester,
			String terminal_Id,String lsh,String zpzl);
    
}
