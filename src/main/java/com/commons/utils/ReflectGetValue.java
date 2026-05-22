package com.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectGetValue {

	private static Logger logger = LoggerFactory.getLogger(ReflectGetValue.class);

	static final String STANDARM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * 取出bean 属性和值
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<Object, Object> getFileValue(Object obj) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Class<?> cls = obj.getClass();
		Method methods[] = cls.getDeclaredMethods();
		Field fields[] = cls.getDeclaredFields();

		for (Field field : fields) {

			String fldtype = field.getType().getSimpleName();
			String getMetName = pareGetName(field.getName());
			String result = "";
			if (!checkMethod(methods, getMetName)) {
				continue;
			}
			Method method = cls.getMethod(getMetName, null);
			Object object = method.invoke(obj, new Object[] {});
			if (null != object) {
				if (fldtype.equals("Date")) {
					if (field.getAnnotation(XmlJavaTypeAdapter.class).value()==JaxbDateTimeAdapter.class)
					{
					    result = fmlDate((Date) object,"L");
					} else{
						result=fmlDate((Date) object,"S");						
					}
				}
				result = String.valueOf(object);
			}
			map.put(field.getName(), result);
		}

		return map;
	}

	/**
	 * 设置bean 属性值
	 * 
	 * @param map
	 * @param bean
	 * @throws Exception
	 */
	public static void setFieldValue(Map<Object, Object> map, Object bean) throws Exception {
		Class<?> cls = bean.getClass();
		Method methods[] = cls.getDeclaredMethods();
		Field fields[] = cls.getDeclaredFields();

		//获取当前对象的属性和值
		//logger.debug("开始处理当前类属性和值...");
		for (Field field : fields) {
			String fldtype = field.getType().getSimpleName();
			String fldSetName = field.getName();
			String setMethod = pareSetName(fldSetName);
			if (!checkMethod(methods, setMethod)) {
				continue;
			}
			Object value = map.get(fldSetName);
			if(value==null)
			{
				value = map.get(fldSetName.toUpperCase());
			}
			if (null != value) {
				//logger.debug("值不为空:"+value+",字段类型:"+fldtype+",方法:"+setMethod+",字段名:"+fldSetName);
				//System.out.println(value.toString());
				Method method = cls.getMethod(setMethod, field.getType());
				//System.out.println(method.getName());
				if ("String".equals(fldtype)) {
					method.invoke(bean, (String) value.toString());
				} else if ("Double".equals(fldtype)) {
					method.invoke(bean, Double.parseDouble(value.toString()));
				} else if ("Long".equalsIgnoreCase(fldtype)) {
					method.invoke(bean, Long.parseLong(value.toString()));
				} else if ("Float".equalsIgnoreCase(fldtype)) {
					method.invoke(bean, Float.parseFloat(value.toString()));
				} else if ("int".equals(fldtype) || "Integer".equalsIgnoreCase(fldtype)) {
					int val = Integer.valueOf((String) value.toString());
					method.invoke(bean, val);
				} else if ("Date".equalsIgnoreCase(fldtype)) {
					if (value!=null)
					{
					if (field.getAnnotation(XmlJavaTypeAdapter.class).value()==JaxbDateTimeAdapter.class)
					{
						SimpleDateFormat sdf=new SimpleDateFormat(STANDARM_DATE_FORMAT);
						method.invoke(bean,sdf.parse(sdf.format(value)));
					} else{
						SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
						method.invoke(bean,sdf.parse(sdf.format(value)));
					}
					}
				}
			}
		}
		//logger.debug("处理当前类属性和值结束。");
		//获取父类属性的值
		Class clzSuper=cls.getSuperclass();
		Method methodsSuper[] = clzSuper.getDeclaredMethods();
		Field fieldsSuper[] = clzSuper.getDeclaredFields();
		//logger.debug("开始处理父类的属性和值...");
		for (Field field : fieldsSuper) {
			String fldtype = field.getType().getSimpleName();
			String fldSetName = field.getName();
			String setMethod = pareSetName(fldSetName);
			if (!checkMethod(methodsSuper, setMethod)) {
				continue;
			}
			Object value = map.get(fldSetName);
			if(value==null)
			{
				value = map.get(fldSetName.toUpperCase());
			}
			if (null != value) {
				//logger.debug("值不为空:"+value+",字段类型:"+fldtype+",方法:"+setMethod+",字段名:"+fldSetName);
				//System.out.println(value.toString());
				Method method = clzSuper.getMethod(setMethod, field.getType());
				//System.out.println(method.getName());
				if ("String".equals(fldtype)) {
					method.invoke(bean, (String) value.toString());
				} else if ("Double".equals(fldtype)) {
					method.invoke(bean, Double.parseDouble(value.toString()));
				} else if ("Long".equalsIgnoreCase(fldtype)) {
					method.invoke(bean, Long.parseLong(value.toString()));
				} else if ("Float".equalsIgnoreCase(fldtype)) {
					method.invoke(bean, Float.parseFloat(value.toString()));
				} else if ("int".equals(fldtype) || "Integer".equalsIgnoreCase(fldtype)) {
					int val = Integer.valueOf((String) value.toString());
					method.invoke(bean, val);
				} else if ("Date".equalsIgnoreCase(fldtype)) {
					//method.invoke(bean, value);
					if (value!=null)
					{
					if (field.getAnnotation(XmlJavaTypeAdapter.class).value()==JaxbDateTimeAdapter.class)
					{
						SimpleDateFormat sdf=new SimpleDateFormat(STANDARM_DATE_FORMAT);
						method.invoke(bean,sdf.parse(sdf.format(value)));
					} else{
						SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
						method.invoke(bean,sdf.parse(sdf.format(value)));
					}
					}

				}
			}
		}
		//logger.debug("处理父类属性和值结束。");
	}

	/**
	 * 拼接某属性get 方法
	 * 
	 * @param fldname
	 * @return
	 */
	public static String pareGetName(String fldname) {
		if (null == fldname || "".equals(fldname)) {
			return null;
		}
		String pro = "get" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
		return pro;
	}

	/**
	 * 拼接某属性set 方法
	 * 
	 * @param fldname
	 * @return
	 */
	public static String pareSetName(String fldname) {
		if (null == fldname || "".equals(fldname)) {
			return null;
		}
		String pro = "set" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
		return pro;
	}

	/**
	 * 判断该方法是否存在
	 * 
	 * @param methods
	 * @param met
	 * @return
	 */
	public static boolean checkMethod(Method methods[], String met) {
		if (null != methods) {
			for (Method method : methods) {
				if (met.equals(method.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 把date 类转换成string
	 * 
	 * @param date
	 * @return
	 */
	public static String fmlDate(Date date,String formatType) {
		if (null != date) {
			SimpleDateFormat sdf=null;
			if (formatType.equals("L"))
			{
			    sdf = new SimpleDateFormat(STANDARM_DATE_FORMAT);
			} else
			{
			    sdf = new SimpleDateFormat(DATE_FORMAT);				
			}
			return sdf.format(date);
		}
		return null;
	}

	public static Date fmlDate(String date,String formatType) {
		if (null != date) {
			SimpleDateFormat sdf=null;
			if (formatType.equals("L"))
			{
			    sdf = new SimpleDateFormat(STANDARM_DATE_FORMAT);
			} else
			{
			    sdf = new SimpleDateFormat(DATE_FORMAT);				
			}
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}


}
