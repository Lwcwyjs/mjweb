package com.commons.sql;

import com.commons.utils.JaxbDateTimeAdapter;
import com.commons.utils.PageInfo;
import com.commons.utils.StringUtils;
import com.model.base.ObjBean;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SQLBean {

	private final Logger logger = LoggerFactory.getLogger(SQLBean.class);

	static final String STANDARM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	static final String DATE_FORMAT = "yyyy-MM-dd";

	private List<ObjBean> objBeans;

	private List<ObjBean> objKeyBeans;

	private List<ObjBean> objSelBeans;

	private String tableName;

	private String fieldsOut;

	private String fieldsKey;

	private String fieldsEncrypt;

	private String initWhere;

	private DateFormat format;

	private DateFormat formatDate;

	private boolean bReflect;

	public SQLBean() {

	}

	public SQLBean(String sTableName, String fieldOut, String keyField) {
		this.tableName = sTableName.toUpperCase();
		this.fieldsOut = fieldOut.toUpperCase();
		this.fieldsKey = keyField.toUpperCase();
		this.fieldsEncrypt = "";
		this.initWhere = "";
		this.objBeans = new ArrayList<ObjBean>();
		this.objKeyBeans = new ArrayList<ObjBean>();
		format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
		formatDate = new SimpleDateFormat(DATE_FORMAT);

		this.bReflect = false;
	}

	public SQLBean(String sTableName, String fieldOut, String keyField, String whereString) {
		this.tableName = sTableName.toUpperCase();
		this.fieldsOut = fieldOut.toUpperCase();
		this.fieldsKey = keyField.toUpperCase();
		this.fieldsEncrypt = "";
		this.initWhere = whereString.toUpperCase();
		this.objBeans = new ArrayList<ObjBean>();
		this.objKeyBeans = new ArrayList<ObjBean>();
		format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
		formatDate = new SimpleDateFormat(DATE_FORMAT);
		this.bReflect = false;
	}

	public SQLBean(String sTableName, String fieldOut, String keyField, String encryptField, String whereString) {
		this.tableName = sTableName.toUpperCase();
		this.fieldsOut = fieldOut.toUpperCase();
		this.fieldsKey = keyField.toUpperCase();
		this.fieldsEncrypt = encryptField.toUpperCase();
		this.initWhere = whereString.toUpperCase();
		this.objBeans = new ArrayList<ObjBean>();
		this.objKeyBeans = new ArrayList<ObjBean>();
		format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
		formatDate = new SimpleDateFormat(DATE_FORMAT);
		this.bReflect = false;
	}

	public SQLBean(String sTableName, String fieldOut, String keyField, boolean breReflect) {
		this.tableName = sTableName.toUpperCase();
		this.fieldsOut = fieldOut.toUpperCase();
		this.fieldsKey = keyField.toUpperCase();
		this.fieldsEncrypt = "";
		this.initWhere = "";
		this.objBeans = new ArrayList<ObjBean>();
		this.objKeyBeans = new ArrayList<ObjBean>();
		format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
		formatDate = new SimpleDateFormat(DATE_FORMAT);
		this.bReflect = breReflect;
	}

	public boolean reflectBean(Object bean) {
		boolean bRet = false;
		try {
			// if (bReflect)
			// {
			this.objBeans = new ArrayList<ObjBean>();
			this.objSelBeans = new ArrayList<ObjBean>();
			Class clz = bean.getClass();
			Class clzSuper = clz.getSuperclass();
			Field[] fields = clz.getDeclaredFields();
			Field[] fieldsSuper = clzSuper.getDeclaredFields();
			String[] fOut = fieldsOut.split(",");
			Object val = "";
			for (Field field : fields) {
				field.setAccessible(true);
				val = field.get(bean);

				if (val != null && !ArrayUtils.contains(fOut, field.getName().toUpperCase())
						&& !val.toString().equalsIgnoreCase("null")) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName().toUpperCase());
					objBean.setFieldType(field.getType().getName().replaceAll("java.lang.", "")
							.replaceAll("java.util.", "").replaceAll("java.math.", ""));
					if (!objBean.getFieldType().equalsIgnoreCase("Date")) {
						objBean.setFieldValue(val.toString());
					} else {
						if (field.getAnnotation(XmlJavaTypeAdapter.class).value() == JaxbDateTimeAdapter.class) {
							objBean.setFieldValue(format.format((Date) val));
						} else {
							objBean.setFieldValue(formatDate.format((Date) val));
						}
					}
					// System.err.println(objBean);
					// logger.debug(objBean.toString());
					objBeans.add(objBean);
				}
				if (!ArrayUtils.contains(fOut, field.getName().toUpperCase())) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName().toUpperCase());
					objBean.setFieldType(field.getType().getName().replaceAll("java.lang.", "")
							.replaceAll("java.util.", "").replaceAll("java.math.", ""));

					objSelBeans.add(objBean);
				}
			}
			for (Field field : fieldsSuper) {
				field.setAccessible(true);
				val = field.get(bean);
				if (val != null && !ArrayUtils.contains(fOut, field.getName().toUpperCase())
						&& !val.toString().equalsIgnoreCase("null")) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName().toUpperCase());
					objBean.setFieldType(field.getType().getName().replaceAll("java.lang.", "")
							.replaceAll("java.util.", "").replaceAll("java.math.", ""));
					if (!objBean.getFieldType().equalsIgnoreCase("Date")) {
						objBean.setFieldValue(val.toString());
					} else {
						if (field.getAnnotation(XmlJavaTypeAdapter.class).value() == JaxbDateTimeAdapter.class) {
							objBean.setFieldValue(format.format((Date) val));
						} else {
							objBean.setFieldValue(formatDate.format((Date) val));
						}
					}
					// System.err.println(objBean);
					// logger.debug(objBean.toString());
					objBeans.add(objBean);
				}

				if (!ArrayUtils.contains(fOut, field.getName().toUpperCase())) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName().toUpperCase());
					objBean.setFieldType(field.getType().getName().replaceAll("java.lang.", "")
							.replaceAll("java.util.", "").replaceAll("java.math.", ""));

					objSelBeans.add(objBean);
				}
			}
			// }
			// logger.debug("反射处理java bean成功");
			bRet = true;
		} catch (Exception e) {
			logger.error("反射处理java bean异常:" + e.getMessage());
			bRet = false;
		}
		return bRet;
	}

	public boolean reflectKeyBean(Object bean) {
		boolean bRet = false;
		try {
			// if (bReflect)
			// {
			this.objBeans = new ArrayList<ObjBean>();
			Class clz = bean.getClass();
			Class clzSuper = clz.getSuperclass();
			Field[] fields = clz.getDeclaredFields();
			Field[] fieldsSuper = clzSuper.getDeclaredFields();
			String[] keys = fieldsKey.split(",");
			Object val = "";
			String fieldType = "";
			for (Field field : fields) {
				field.setAccessible(true);
				val = field.get(bean);
				fieldType = field.getType().getName().replaceAll("java.lang.", "").replaceAll("java.util.", "")
						.replaceAll("java.math.", "");
				if (val != null && ArrayUtils.contains(keys, field.getName().toUpperCase())) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName());
					objBean.setFieldType(fieldType);
					if (!fieldType.equalsIgnoreCase("Date")) {
						objBean.setFieldValue(val.toString());
					} else {
						if (field.getAnnotation(XmlJavaTypeAdapter.class).value() == JaxbDateTimeAdapter.class) {
							objBean.setFieldValue(format.format((Date) val));
						} else {
							objBean.setFieldValue(formatDate.format((Date) val));
						}
					}
					objKeyBeans.add(objBean);
				}
			}
			for (Field field : fieldsSuper) {
				field.setAccessible(true);
				val = field.get(bean);
				fieldType = field.getType().getName().replaceAll("java.lang.", "").replaceAll("java.util.", "")
						.replaceAll("java.math.", "");
				if (val != null && ArrayUtils.contains(keys, field.getName().toUpperCase())) {
					ObjBean objBean = new ObjBean();
					objBean.setFieldName(field.getName());
					objBean.setFieldType(fieldType);
					if (!fieldType.equalsIgnoreCase("Date")) {
						objBean.setFieldValue(val.toString());
					} else {
						if (field.getAnnotation(XmlJavaTypeAdapter.class).value() == JaxbDateTimeAdapter.class) {
							objBean.setFieldValue(format.format((Date) val));
						} else {
							objBean.setFieldValue(formatDate.format((Date) val));
						}
					}
					objKeyBeans.add(objBean);
				}
			}
			// }
			// logger.debug("反射处理java bean成功");
			bRet = true;
		} catch (Exception e) {
			logger.error("反射处理java bean异常:" + e.getMessage());
			bRet = false;
		}
		return bRet;
	}

	public String getInsertFields(Object bean) {
		String sInsertFields = "";
		if (checkReflect(bean)) {
			for (ObjBean objBean : objBeans) {
				sInsertFields = sInsertFields.equals("") ? objBean.getFieldName()
						: sInsertFields + "," + objBean.getFieldName();
			}
			if (!sInsertFields.equals("")) {
				sInsertFields = "(" + sInsertFields + ")";
			}
		}
		// logger.debug("InsertFields:" + sInsertFields);
		return sInsertFields;
	}

	public String getInsertValues(Object bean) {
		checkReflect(bean);
		String sInsertValues = "";
		if (checkReflect(bean)) {
			for (ObjBean objBean : objBeans) {
				String fieldtype = objBean.getFieldType();
				if (objBean.getFieldType().equalsIgnoreCase("String")) {
					String[] fEncrypt = fieldsEncrypt.split(",");
					if (ArrayUtils.contains(fEncrypt, objBean.getFieldName())) {
						sInsertValues = sInsertValues.equals("")
								? "'" + objBean.getFieldValue() + "'"
								: sInsertValues + ",'" + objBean.getFieldValue()  + "'";
					} else {
						sInsertValues = sInsertValues.equals("") ? "'" + objBean.getFieldValue() + "'"
								: sInsertValues + ",'" + objBean.getFieldValue() + "'";
					}
				}
				if (objBean.getFieldType().equalsIgnoreCase("Long")
						|| objBean.getFieldType().equalsIgnoreCase("Integer")
						|| objBean.getFieldType().equalsIgnoreCase("Double")
						|| objBean.getFieldType().equalsIgnoreCase("Short")
						|| objBean.getFieldType().equalsIgnoreCase("Float")) {
					sInsertValues = sInsertValues.equals("") ? objBean.getFieldValue()
							: sInsertValues + "," + objBean.getFieldValue();
				}
				if (objBean.getFieldType().equalsIgnoreCase("Date")) {
					// Date date = format.parse(objBean.getFieldValue());
					if (objBean.getFieldValue().length() > 10) {
						sInsertValues = sInsertValues.equals("")
								? "'" + objBean.getFieldValue() + "'"
								: sInsertValues + ",'" + objBean.getFieldValue()
										+ "'";
					} else {
						sInsertValues = sInsertValues.equals("")
								? "'" + objBean.getFieldValue() + "'"
								: sInsertValues + ",'" + objBean.getFieldValue() + "'";
					}
				}
			}
			if (!sInsertValues.equals("")) {
				sInsertValues = "(" + sInsertValues + ")";
			}
		}
		// logger.debug("InsertValues:" + sInsertValues);
		return sInsertValues;
	}

	public String getSQLWhere(Object bean) {
		String sqlWhere = "";
		String[] keyFields = fieldsKey.split(",");
		if (reflectKeyBean(bean)) {
			for (ObjBean objBean : objKeyBeans) {
				if (ArrayUtils.contains(keyFields, objBean.getFieldName().toUpperCase())) {
					if (objBean.getFieldType().equalsIgnoreCase("String")) {
						if (!objBean.getFieldValue().equals("")) {
							String[] encryptFields = fieldsEncrypt.split(",");
							if (ArrayUtils.contains(encryptFields, objBean.getFieldName().toUpperCase())) {
								sqlWhere = sqlWhere.equals("")
										? objBean.getFieldName()+ "='"
												+ objBean.getFieldValue() + "'"
										: sqlWhere + " AND " + objBean.getFieldName()+ "='" + objBean.getFieldValue() + "'";
							} else {
								sqlWhere = sqlWhere.equals("")
										? objBean.getFieldName() + "='" + objBean.getFieldValue() + "'"
										: sqlWhere + " AND " + objBean.getFieldName() + "='" + objBean.getFieldValue()
												+ "'";
							}
						} else {
							sqlWhere = sqlWhere.equals("") ? objBean.getFieldName() + " is null "
									: sqlWhere + " AND " + objBean.getFieldName() + " is null ";
						}
					}
					if (objBean.getFieldType().equalsIgnoreCase("Long")
							|| objBean.getFieldType().equalsIgnoreCase("Integer")
							|| objBean.getFieldType().equalsIgnoreCase("Double")
							|| objBean.getFieldType().equalsIgnoreCase("Float")) {
						if (!objBean.getFieldValue().equals("")) {
							sqlWhere = sqlWhere.equals("") ? objBean.getFieldName() + "=" + objBean.getFieldValue()
									: sqlWhere + " AND " + objBean.getFieldName() + "=" + objBean.getFieldValue();
						} else {
							sqlWhere = sqlWhere.equals("") ? objBean.getFieldName() + " is null "
									: sqlWhere + " AND " + objBean.getFieldName() + " is null ";
						}
					}
					if (objBean.getFieldType().equalsIgnoreCase("Date")) {
						if (objBean.getFieldValue().length() > 10) {
							sqlWhere = sqlWhere.equals("")
									? objBean.getFieldName() + "='" + objBean.getFieldValue()
											+ "'"
									: sqlWhere + " AND " + objBean.getFieldName() + "='"
											+ objBean.getFieldValue() + "'";
						} else {
							if (!objBean.getFieldValue().equals("")) {
								sqlWhere = sqlWhere.equals("")
										? objBean.getFieldName() + "='" + objBean.getFieldValue()
												+ "'"
										: sqlWhere + " AND " + objBean.getFieldName() + "='"
												+ objBean.getFieldValue() + "'";
							} else {
								sqlWhere = sqlWhere.equals("") ? objBean.getFieldName() + " is null "
										: sqlWhere + " AND " + objBean.getFieldName() + " is null ";
							}
						}
					}
				}
			}
		}
		if (!sqlWhere.equals("")) {
			sqlWhere = " WHERE " + sqlWhere;
		}

		if (!initWhere.equalsIgnoreCase("")) {
			sqlWhere = sqlWhere.equalsIgnoreCase("") ? " WHERE " + initWhere : sqlWhere + " AND " + initWhere;
		}

		// logger.debug("sqlWhere:" + sqlWhere);
		return sqlWhere;
	}

	public String getSQLWhereByConditions(Map<String, Object> conditions) {
		String sqlWhere = "";
		if (conditions != null) {
			for (String key : conditions.keySet()) {
				sqlWhere += sqlWhere.equals("") ? key + "='" + conditions.get(key).toString() + "'"
						: " AND " + key + "='" + conditions.get(key).toString() + "'";
			}
		}

		if (!sqlWhere.equals("")) {
			sqlWhere = " WHERE " + sqlWhere;
		}

		if (!initWhere.equalsIgnoreCase("")) {
			sqlWhere = sqlWhere.equalsIgnoreCase("") ? " WHERE " + initWhere : sqlWhere + " AND " + initWhere;
		}

		// logger.debug("sqlWhere:" + sqlWhere);
		return sqlWhere;
	}

	private boolean checkReflect(Object bean) {
		boolean bReflect = true;
		if (!bReflect) {
			if (objBeans.size() == 0) {
				bReflect = reflectBean(bean);
			}
		} else {
			bReflect = reflectBean(bean);
		}
		return bReflect;
	}

	public String generateDeleteSQL(Object bean) {
		String deleteSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		if (!sqlWhere.equals("")) {
			deleteSQL = "DELETE FROM " + tableName + " " + sqlWhere;
		}
		return deleteSQL;
	}

	public String getSelectFields(Object bean) {
		String sInsertFields = "";
		if (checkReflect(bean)) {
			for (ObjBean objBean : objSelBeans) {
				String[] encryptFields = fieldsEncrypt.split(",");
				if (ArrayUtils.contains(encryptFields, objBean.getFieldName().toUpperCase())) {
					sInsertFields = sInsertFields.equals("")
							?  objBean.getFieldName()
							: sInsertFields + "," + objBean.getFieldName();
				} else {
					sInsertFields = sInsertFields.equals("") ? objBean.getFieldName()
							: sInsertFields + "," + objBean.getFieldName();
				}
			}
			if (sInsertFields.equals("")) {
				sInsertFields = "*";
			}
		}
		// logger.debug("InsertFields:" + sInsertFields);
		return sInsertFields;
	}

	public String generateSelectSQL(Object bean) {
		String selectSQL = "";
		String selectFields = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		if (StringUtils.isBlank(fieldsEncrypt) && StringUtils.isBlank(fieldsOut)) {
			selectFields = "*";
		} else {
			selectFields = getSelectFields(bean);
		}

		selectSQL = "SELECT " + selectFields + " FROM " + tableName;
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		return selectSQL;
	}

	public String generateSelectPageSQL(PageInfo pageInfo, String filed, String strWhere, String strOrder) {
		String selectSQL = "";
		strWhere = StringUtils.isBlank(strWhere) ? "" : strWhere;
		String sqlOrder = StringUtils.isBlank(strOrder) ? "" : strOrder;
		if (sqlOrder.equals("")) {
			sqlOrder = " ORDER BY " + pageInfo.getSort() + " " + pageInfo.getOrder();
		}
		// add by wangwj 2017-06-08 过滤字段前缀
		// String[] keys = filed.split(",");
		// String content = "";
		// for (String key : keys) {
		// String subKey = key.substring(key.indexOf(".") + 1, key.length());
		//// subKey = subKey.substring(subKey.indexOf(" ") +
		// 1).trim();//当用“,”分隔后，最后一个字段像这样“name ”，会导致语法错误，使得最后一个字符是“,”
		// subKey = subKey.trim();
		//// if (subKey.indexOf("(") < 0 && subKey.indexOf(")") < 0) {
		// //当有使用函数的时候，该字段不会被使用拼接到content上
		// if (content.equals("")) {
		// content = subKey;
		// } else {
		// content = content + "," + subKey;
		// }
		//// }
		// }
		// selectSQL = "SELECT " + content + " FROM (SELECT " + filed + ", ROWNUM AS
		// rowno " + "FROM " + tableName + " ";

		selectSQL = "SELECT * FROM (select b.*,ROW_NUMBER() OVER ("+sqlOrder + ") as rowno from(SELECT " + filed  + " FROM " + tableName + " ";
		if (!strWhere.equals("")) {
			selectSQL += " WHERE 1=1 " + strWhere;
		}
		selectSQL = selectSQL + ") b)c " + "WHERE rowno >=" + pageInfo.getFrom() + " and rowno <="
				+ pageInfo.getTo() + " ";

		return selectSQL;
	}

	public String generateSelectPageSQL(String filed, String strWhere, String strOrder) {
		String selectSQL = "";
		strWhere = StringUtils.isBlank(strWhere) ? "" : strWhere;
		String sqlOrder = StringUtils.isBlank(strOrder) ? "" : strOrder;
		if (!sqlOrder.equals("")) {
			sqlOrder = " ORDER BY " + sqlOrder;
		}

		selectSQL = "SELECT " + filed + " " + "FROM " + tableName + " ";
		if (!strWhere.equals("")) {
			selectSQL += " WHERE 1=1 " + strWhere;
		}
		selectSQL = selectSQL + " " + sqlOrder;

		return selectSQL;
	}

	public String generateSelectPageSQL(Object bean, PageInfo pageInfo) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		Map<String, Object> condition = pageInfo.getCondition();
		selectSQL = "SELECT * " + "FROM (SELECT tt.*, ROW_NUMBER() over ( ORDER BY " + pageInfo.getSort() + " " + pageInfo.getOrder()+") AS rowno " + "FROM (SELECT t.* " + "FROM " + tableName
				+ " t ";
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		selectSQL = selectSQL + ") tt "
				+ "WHERE rowno() <=" + pageInfo.getTo() + ") table_alias " + "WHERE table_alias.rowno >="
				+ pageInfo.getFrom() + " ";
		return selectSQL;
	}

	public String generateSelectPageSQL(Object bean, PageInfo pageInfo, String strWhere) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		Map<String, Object> condition = pageInfo.getCondition();
		selectSQL = "SELECT * " + "FROM (SELECT tt.*, ROW_NUMBER() over ( ORDER BY " + pageInfo.getSort() + " " + pageInfo.getOrder()+") AS rowno " + "FROM (SELECT t.* " + "FROM " + tableName
				+ " t ";
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}

		selectSQL += strWhere;

		selectSQL = selectSQL +  ") tt "
				+ ") table_alias " + "WHERE table_alias.rowno >="
				+ pageInfo.getFrom() + " and table_alias.rowno <=" + pageInfo.getTo();
		return selectSQL;
	}

	public String generateSelectCountSQL(PageInfo pageInfo) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhereByConditions(pageInfo.getCondition());
		selectSQL = "SELECT COUNT(*) FROM " + tableName;
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		return selectSQL;
	}

	public String generateSelectCountSQL(PageInfo pageInfo, String strWhere) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhereByConditions(pageInfo.getCondition());
		selectSQL = "SELECT COUNT(*) FROM " + tableName;
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		selectSQL += strWhere;
		return selectSQL;
	}

	public String generateSelectCountSQL(Object bean) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		selectSQL = "SELECT COUNT(*) FROM " + tableName;
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		return selectSQL;
	}

	public String generateSelectSumSQL(Object bean, String sumField) {
		String selectSQL = "";
		String sqlWhere = "";
		sqlWhere = getSQLWhere(bean);
		selectSQL = "SELECT nvl(SUM(" + sumField + "),0) FROM " + tableName;
		if (!sqlWhere.equals("")) {
			selectSQL += sqlWhere;
		} else {
			selectSQL += " WHERE 1=1 ";
		}
		return selectSQL;
	}

	public String generateUpdateSQL(Object bean) {
		String updateSQL = "";
		String sqlWhere = "";
		if (checkReflect(bean)) {
			for (ObjBean objBean : objBeans) {
				if (objBean.getFieldType().equalsIgnoreCase("String")) {
					if (!objBean.getFieldName().toUpperCase().equals("ROWID"))// 排除掉rowid的修改
					{
						String[] fEncrypt = fieldsEncrypt.split(",");
						if (ArrayUtils.contains(fEncrypt, objBean.getFieldName())) {
							updateSQL = updateSQL.equals("")
									? objBean.getFieldName() + "='" + objBean.getFieldValue() + "'"
									: updateSQL + "," + objBean.getFieldName() + "='"
											+ objBean.getFieldValue() + "'";
						} else {
							updateSQL = updateSQL.equals("")
									? objBean.getFieldName() + "='" + objBean.getFieldValue() + "'"
									: updateSQL + "," + objBean.getFieldName() + "='" + objBean.getFieldValue() + "'";
						}
					}
				}
				if (objBean.getFieldType().equalsIgnoreCase("Long")
						|| objBean.getFieldType().equalsIgnoreCase("Integer")
						|| objBean.getFieldType().equalsIgnoreCase("Double")
						|| objBean.getFieldType().equalsIgnoreCase("Float")) {
					updateSQL = updateSQL.equals("") ? objBean.getFieldName() + "=" + objBean.getFieldValue()
							: updateSQL + "," + objBean.getFieldName() + "=" + objBean.getFieldValue();
				}
				if (objBean.getFieldType().equalsIgnoreCase("Date")) {
					// Date date = format.parse(objBean.getFieldValue());
					if (objBean.getFieldValue().length() > 10) {
						updateSQL = updateSQL.equals("")
								? objBean.getFieldName() + "='" + objBean.getFieldValue()
										+ "'"
								: updateSQL + "," + objBean.getFieldName() + "='" + objBean.getFieldValue()
										+ "'";
					} else {
						updateSQL = updateSQL.equals("")
								? objBean.getFieldName() + "='" + objBean.getFieldValue() + "'"
								: updateSQL + "," + objBean.getFieldName() + "='" + objBean.getFieldValue()
										+ "'";
					}
				}

			}
		}
		sqlWhere = getSQLWhere(bean);
		if (!updateSQL.equals("")) {
			updateSQL = "UPDATE " + tableName + " SET " + updateSQL + " " + sqlWhere;
		}
		// logger.debug("updateSQL:" + updateSQL);
		return updateSQL;
	}

	public String generateInsertSQL(Object bean) {
		String insertSQL = "";
		String insertFiels = "";
		String insertValues = "";
		if (checkReflect(bean)) {
			insertFiels = getInsertFields(bean);
			insertValues = getInsertValues(bean);
			if (!insertFiels.equals("") && !insertValues.equals("")) {
				insertSQL = "INSERT INTO " + tableName + " " + insertFiels + " VALUES " + insertValues;
			}
		}
		return insertSQL;
	}

}
