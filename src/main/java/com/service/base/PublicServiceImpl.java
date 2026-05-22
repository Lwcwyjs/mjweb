package com.service.base;

import com.commons.annotation.DataSourceChange;
import com.commons.shiro.ShiroUser;
import com.commons.sql.SQLBean;
import com.commons.utils.DealString;
import com.commons.utils.PageInfo;
import com.commons.utils.ReflectGetValue;
import com.commons.utils.StringUtils;
import com.mapper.base.DataItemMapper;
import com.model.sysmanage.SysUser;
import com.service.sysmanage.SysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
//import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;


@Service
public class PublicServiceImpl implements PublicService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat shortDateFormater = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat longDateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
    DataItemMapper dItemMapper;

	@Autowired
	private HttpServletRequest request;

	@Override
	public void insert(String sql) {
		// logger.debug("执行插入语句:" + sql);
		dItemMapper.insert(sql);
	}

	@Override
	public void insert(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateInsertSQL(bean);
		// logger.debug("生成的插入sql语句:" + sql);
		dItemMapper.insert(sql);
	}

	@Override
	public void insert(String sTableName, String fieldOut, String keyField, String encryptField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, encryptField, "");
		String sql = sqlBean.generateInsertSQL(bean);
		// logger.debug("生成的插入sql语句:" + sql);
		dItemMapper.insert(sql);
	}

	@Override
	public void update(String sql) {
		// logger.debug("执行更新语句:" + sql);
		dItemMapper.update(sql);
	}

	@Override
	public void update(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateUpdateSQL(bean);
		// logger.debug("生成的更新sql语句:" + sql);
		dItemMapper.update(sql);
	}

	@Override
	public void update(String sTableName, String fieldOut, String keyField, String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateUpdateSQL(bean);
		// logger.debug("生成的更新sql语句:" + sql);
		dItemMapper.update(sql);
	}

	@Override
	public void update(String sTableName, String fieldOut, String keyField, String encryptField, String initWhere,
			Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, encryptField, initWhere);
		String sql = sqlBean.generateUpdateSQL(bean);
		// logger.debug("生成的更新sql语句:" + sql);
		dItemMapper.update(sql);
	}

	@Override
	public void delete(String sql) {
		// logger.debug("执行删除语句:" + sql);
		dItemMapper.delete(sql);
	}

	@Override
	public void delete(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateDeleteSQL(bean);
		// logger.debug("生成的删除sql语句:" + sql);
		dItemMapper.delete(sql);
	}

	@Override
	public void delete(String sTableName, String fieldOut, String keyField, String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateDeleteSQL(bean);
		// logger.debug("生成的删除sql语句:" + sql);
		dItemMapper.delete(sql);
	}

	@Override
	public Object selectObject(String sql, Class clz) {
		// Map<Object, Object> map = dItemMapper.selectObject(sql);
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		Map<Object, Object> map = null;
		Object obj = null;
		try {
			if (maps.size() > 0) {
				obj = clz.newInstance();
				map = maps.get(0);
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
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		return selectObject(sql, bean.getClass());
	}

	@Override
	public Object selectObject(String sTableName, String fieldOut, String keyField, String orderString, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return selectObject(sql, bean.getClass());
	}

	@Override
	public Object selectObject(String sTableName, String fieldOut, String keyField, String orderString,
			String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return selectObject(sql, bean.getClass());
	}

	@Override
	public List<Object> selectObjects(String sql, Class clz) {
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		List<Object> objects = new ArrayList<Object>();
		try {
			for (Map<Object, Object> map : maps) {
				Object obj = clz.newInstance();
				ReflectGetValue.setFieldValue(map, obj);
				objects.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	@Override
	public List<Object> selectObjects(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		return selectObjects(sql, bean.getClass());
	}

	@Override
	public List<Object> selectObjects(String sTableName, String fieldOut, String keyField, String orderString,
			Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return selectObjects(sql, bean.getClass());
	}

	@Override
	public List<Object> selectObjects(String sTableName, String fieldOut, String keyField, String orderString,
			String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return selectObjects(sql, bean.getClass());
	}

	@Override
	public void selectPageObjects(PageInfo pageInfo, String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectPageSQL(bean, pageInfo);
		String sqlCount = sqlBean.generateSelectCountSQL(bean);

		pageInfo.setRows(selectObjects(sql, bean.getClass()));
		pageInfo.setTotal(selectCount(sqlCount));
	}



	@Override
	public void selectPageObjects(PageInfo pageInfo, String sTableName, String fieldOut, String keyField,
								  String strWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectPageSQL(bean, pageInfo, strWhere);
		String sqlCount = sqlBean.generateSelectCountSQL(pageInfo, strWhere);

		pageInfo.setRows(selectObjects(sql, bean.getClass()));
		pageInfo.setTotal(selectCount(sqlCount));
	}

	@Override
	public void selectPageObjects(PageInfo pageInfo, String sTableName, Object beanClass) {
		SQLBean sqlBean = new SQLBean(sTableName,"","");
		pageInfo.setSort("cjsj");
		pageInfo.setOrder("  ");
		String sql = sqlBean.generateSelectPageSQL(beanClass, pageInfo);
		String sqlCount = sqlBean.generateSelectCountSQL(pageInfo);

		pageInfo.setRows(selectObjects(sql, beanClass.getClass()));
		pageInfo.setTotal(selectCount(sqlCount));
	}

	@Override
	@DataSourceChange(slave = true)
	public void selectPageObjects(PageInfo pageInfo, String sTableName, String field, String strWhere,
			String strOrder) {
		field = field.equals("") ? "*" : field;
		if (!strOrder.equalsIgnoreCase("")) {
			strOrder = " order by " + strOrder;
		}
		SQLBean sqlBean = new SQLBean(sTableName, "", "");
		String sql = sqlBean.generateSelectPageSQL(pageInfo, field, strWhere, strOrder);
		String sqlCount = sqlBean.generateSelectCountSQL(pageInfo, strWhere);
		logger.info(sql);
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		// 处理Oracle大写字段转小写
		maps = rebuildData(field, maps);

		pageInfo.setRows(maps);
		pageInfo.setTotal(selectCount(sqlCount));
	}

	@Override
	public void selectPageObjectsNew(PageInfo pageInfo, String sTableName, String field, String strWhere) {
		field = field.equals("") ? "*" : field;
		SQLBean sqlBean = new SQLBean(sTableName, "", "");
		// 开始拼接出SQL语句
		strWhere = StringUtils.isBlank(strWhere) ? "" : strWhere;
		String selectSQL = "";
		{
			selectSQL = "SELECT * " + "FROM (SELECT tt.*, ROW_NUMBER() over ( ORDER BY " + pageInfo.getSort() + " " + pageInfo.getOrder()+") AS rowno " + "FROM (SELECT t.* " + "FROM  ( select "
					+ field + " from " + sTableName;
			selectSQL += " WHERE 1=1 " + strWhere + " ) t ";
			selectSQL = selectSQL + ") tt "
					+ "WHERE rowno <=" + pageInfo.getTo() + ") table_alias " + "WHERE table_alias.rowno >="
					+ pageInfo.getFrom() + " ";
		}

		String sql = selectSQL;
		String sqlCount = sqlBean.generateSelectCountSQL(pageInfo, strWhere);

		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		// 处理Oracle大写字段转小写
		maps = rebuildData(field, maps);

		pageInfo.setRows(maps);
		pageInfo.setTotal(selectCount(sqlCount));
	}

	@Override
	public List<Map<Object, Object>> selectObjects(String sTableName, String fields, String strWhere, String strOrder) {
		SQLBean sqlBean = new SQLBean(sTableName, "", "");
		String sql = sqlBean.generateSelectPageSQL(fields, strWhere, strOrder);

		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		// 处理Oracle大写字段转小写
		maps = rebuildData(fields, maps);

		return maps;
	}

	@Override
	public <T> T selectObj(String sql, Class<T> clz) {
		// Map<Object, Object> map= dItemMapper.selectObject(sql);
		List<Map<Object, Object>> maps = dItemMapper.selectMultiObject(sql);
		Map<Object, Object> map = maps.size() > 0 ? maps.get(0) : null;
		T t = null;
		try {
			if (map != null) {
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
		List<T> t = new ArrayList<T>();
		try {
			for (Map<Object, Object> map : maps) {
				Object obj = clz.newInstance();
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
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectCountSQL(bean);
		return dItemMapper.selectCount(sql);
	}

	@Override
	public Integer selectCount(String sTableName, String fieldOut, String keyField, String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectCountSQL(bean);
		return dItemMapper.selectCount(sql);
	}

	@Override
	public <T> T selectObj(String sTable, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		return (T) selectObj(sql, bean.getClass());
	}

	@Override
	public <T> T selectObj(String sTable, String fieldOut, String keyField, String orderString, Object bean) {
		SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (T) selectObj(sql, bean.getClass());
	}

	@Override
	public <T> T selectObj(String sTable, String fieldOut, String keyField, String orderString, String initWhere,
			Object bean) {
		SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (T) selectObj(sql, bean.getClass());
	}

	@Override
	public <T> T selectObj(String sTable, String fieldOut, String keyField, String encryptField, String orderString,
			String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField, encryptField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		System.out.println(sql);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (T) selectObj(sql, bean.getClass());
	}

	@Override
	public <T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		return (List<T>) selectObjs(sql, bean.getClass());
	}

	@Override
	public <T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, String orderString,
			Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (List<T>) selectObjs(sql, bean.getClass());
	}

	@Override
	public <T> List<T> selectObjs(String sTableName, String fieldOut, String keyField, String orderString,
			String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (List<T>) selectObjs(sql, bean.getClass());
	}

	@Override
	public Integer selectSum(String sql) {
		return dItemMapper.selectSum(sql);
	}

	@Override
	public Integer selectSum(String sTableName, String fieldOut, String keyField, String sumField, Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField);
		String sql = sqlBean.generateSelectSumSQL(bean, sumField);
		return dItemMapper.selectSum(sql);
	}

	@Override
	public Integer selectSum(String sTableName, String fieldOut, String keyField, String sumField, String initWhere,
			Object bean) {
		SQLBean sqlBean = new SQLBean(sTableName, fieldOut, keyField, initWhere);
		String sql = sqlBean.generateSelectSumSQL(bean, sumField);
		return dItemMapper.selectSum(sql);
	}

	public String getWhereOrgan(String organName, String organValue) {
		String sqlWhere = "";
		if (organValue != null && !organValue.equals("")) {
			if (organValue.length() >= 6) {
				if (organValue.substring(2).equals("000000")) // 省级单位
				{
					sqlWhere = "1=1";
				} else if (organValue.substring(4).equals("0000")) // 市级
				{
					sqlWhere = organName + " like '" + organValue.substring(0, 4) + "%'";
				} else if (organValue.substring(6).equals("00")) // 区县
				{
					sqlWhere = organName + " like '" + organValue.substring(0, 6) + "%'";
				} else {
					sqlWhere = organName + " = '" + organValue + "'";
				}
			} else {
				sqlWhere = organName + " = '" + organValue + "'";
			}
		} else {
			sqlWhere = organName + " = '" + organValue + "'";
		}
		return sqlWhere;
	}

	/**
	 * 处理Oracle大写字段转小写
	 * 
	 * @param fileds
	 *            字段内容
	 * @param maps
	 *            结果数据
	 * @return
	 */
	private List<Map<Object, Object>> rebuildData(String fileds, List<Map<Object, Object>> maps) {
		List<Map<Object, Object>> rets = new ArrayList<>();
		fileds = DealString.toString(fileds);
		// 默认内容直接转小写
		if (fileds == "" || fileds == "*") {
			for (Map<Object, Object> map : maps) {
				Map<Object, Object> ret = new HashMap<>();
				for (Object o : map.keySet()) {
					ret.put(o.toString().toLowerCase(), map.get(o));
				}
				rets.add(ret);
			}
		} else {
			String[] strs = fileds.split(",");
			// 规定内容
			Map<Object, Object> s = new HashMap<>();
			for (String str : strs) {
				str = str.trim();
				if (str.indexOf(" ") < 0) {
					if (str.indexOf(".") < 0) {
						s.put(str, "");
					} else {
						s.put(str.substring(str.indexOf(".") + 1), "");
					}
				} else {
					s.put(str.substring(str.indexOf(" ") + 1).trim(), "");
				}
			}
			// 规定内容转小写
			for (Map<Object, Object> map : maps) {
				Map<Object, Object> ret = new HashMap<>();
				for (Object o : map.keySet()) {
					if (s.containsKey(o)) {
						ret.put(o, map.get(o));
					} else {
						ret.put(o.toString().toLowerCase(), map.get(o));
					}
				}
				rets.add(ret);
			}
		}
		return rets;
	}

	@Autowired
	private SysUserService sysUserService;


	@Override
	public void insertInterLog(String interface_Condition, String interface_Result, String requester,
			String terminal_Id) {
		// TODO Auto-generated method stub
		String interface_Time = longDateFormater.format(new Date());
		// 数据库单引号为特殊字符,进行特殊字符替换，防止插入单引号
		interface_Condition = interface_Condition.replaceAll("'", "''");
		String jyw = DigestUtils.md5Hex(interface_Time + interface_Result);
		insert("insert into SYS_INTER_LOG(INTERFACE_TIME,REQUESTER,TERMINAL_ID,INTERFACE_CONDITION,INTERFACE_RESULT,JYW) values ('"
				+ interface_Time + "','" + requester + "','" + terminal_Id
				+ "','" + interface_Condition + "','" + interface_Result + "','"
				+ jyw + "')");
	}

	@Override
	public void insertInvokeLog(String jkid, String interface_Condition, String interface_Result, String requester,
			String terminal_Id,String lsh,String zpzl) {
		// TODO Auto-generated method stub
		String interface_Time = longDateFormater.format(new Date());
		// 数据库单引号为特殊字符,进行特殊字符替换，防止插入单引号
		interface_Condition = interface_Condition.replaceAll("'", "''");
		String jyw = DigestUtils.md5Hex(interface_Time + interface_Result);
		insert("insert into SYS_INVOKING_LOG(JKID,OPERATE_TIME,USER_NAME,TERMINAL_ID,OPERATE_CONTENT,OPERATE_RESULT,JYW,LSH,ZPZL) values ('"
				+ jkid + "','" + interface_Time + "','" + requester + "','"
				+ terminal_Id + "','" + interface_Condition + "','"
				+ interface_Result + "','" + jyw + "','" + lsh + "','" + zpzl + "')");
	}

	@Override
	public void insertLogInfo(String operateCondition, String operateType, String operateResult,String operatemodule) {
		// TODO Auto-generated method stub
		String insertType = "0";
		if (operateType.contains("查询")) {
			insertType = "1";
		} else if (operateType.contains("新增")) {
			insertType = "2";
		} else if (operateType.contains("新建")) {
			insertType = "2";
		} else if (operateType.contains("添加")) {
			insertType = "2";
		} else if (operateType.contains("插入")) {
			insertType = "2";
		} else if (operateType.contains("修改")) {
			insertType = "3";
		} else if (operateType.contains("编辑")) {
			insertType = "3";
		} else if (operateType.contains("删除")) {
			insertType = "4";
		} else if (operateType.contains("登陆")) {
			insertType = "0";
		} else if (operateType.contains("登录")) {
			insertType = "0";
		} else {
			insertType = operateType;
		}
		// log_info log = new log_info();
		Subject subject = ThreadContext.getSubject();
		ShiroUser user = (ShiroUser) subject.getPrincipal();
		SysUser currentUser = sysUserService.findUserById(user.id);
		String operateTime = longDateFormater.format(new Date());
		operateCondition = operateCondition.replaceAll("'", "''");
		String jyw = DigestUtils.md5Hex(currentUser.getId() + insertType + operateTime + operateResult);
		insert("insert into AI_SYS_LOG_INFO(USER_ID,ORGANIZATION,USER_NAME,TERMINAL_ID,OPERATE_TYPE,OPERATE_TIME,OPERATE_CONDITION,OPERATE_RESULT,TERMINAL_TYPE,JYW,OPERATE_MODULE)"
				+ " values ('" + currentUser.getId() + "','" + currentUser.getOrgan()
				+ "','" + currentUser.getName() + "','"
				+ getRemoteHost(request) + "','" + insertType + "','" + operateTime + "','"
				+ operateCondition + "','" + operateResult + "','1','" + jyw  + "','"+operatemodule+"')");
	}

	@Override
	public void insertLogInfo(String operateCondition, String operateType, String operateResult, Long userid,
			String username, String organ,String operatemodule) {
		// TODO Auto-generated method stub
		String insertType = "0";
		if (operateType.contains("查询")) {
			insertType = "1";
		} else if (operateType.contains("新增")) {
			insertType = "2";
		} else if (operateType.contains("添加")) {
			insertType = "2";
		} else if (operateType.contains("插入")) {
			insertType = "2";
		} else if (operateType.contains("修改")) {
			insertType = "3";
		} else if (operateType.contains("编辑")) {
			insertType = "3";
		} else if (operateType.contains("删除")) {
			insertType = "4";
		} else if (operateType.contains("登陆")) {
			insertType = "0";
		} else if (operateType.contains("登录")) {
			insertType = "0";
		} else {
			insertType = operateType;
		}
		String terminal_id="127.0.0.1";
		try
		{
			terminal_id=getRemoteHost(request);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		if (organ.equals(""))
		{
			organ="000000";
		}
		// log_info log = new log_info();
		Subject subject = ThreadContext.getSubject();
		String operateTime = longDateFormater.format(new Date());
		operateCondition = operateCondition.replaceAll("'", "''");
		String jyw = DigestUtils.md5Hex(userid + insertType + operateTime + operateResult);
		insert("insert into AI_SYS_LOG_INFO(USER_ID,ORGANIZATION,USER_NAME,TERMINAL_ID,OPERATE_TYPE,OPERATE_TIME,OPERATE_CONDITION,OPERATE_RESULT,TERMINAL_TYPE,JYW,OPERATE_MODULE)"
				+ " values ('" + userid + "','" + organ + "','" + username
				+ "','" + terminal_id + "','" + insertType + "','"
				+ operateTime + "','" + operateCondition + "','" + operateResult + "','1','"
				+ jyw + "','"+operatemodule+"')");
	}


	public String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	@Override
	public <T> List<T> selectObjs(String sTable, String fieldOut, String keyField, String encryptField,
			String orderString, String initWhere, Object bean) {
		SQLBean sqlBean = new SQLBean(sTable, fieldOut, keyField, encryptField, initWhere);
		String sql = sqlBean.generateSelectSQL(bean);
		if (!orderString.equalsIgnoreCase("")) {
			sql += " order by " + orderString;
		}
		return (List<T>) selectObjs(sql, bean.getClass());
	}
}
