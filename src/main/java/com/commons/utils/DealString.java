package com.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class DealString { 

	/************************************
	 * 方法说明: 删除字符串尾部的0 参数： @param str 返回： String
	 ***********************************/
	public static String removeTail0(String str) {
		// 如果字符串尾部不为0，返回字符串
		if (str.length() != 0) {
			if (!str.substring(str.length() - 1).equals("0")) {
				return str;
			} else {
				// 否则将字符串尾部删除一位再进行递归
				return removeTail0(str.substring(0, str.length() - 1));
			}
		}
		return "";
	}

	/**
	 * 字符串格式化成固定长度，左补零
	 * 
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String lpad(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}

	/**
	 * 字符串格式化成固定长度，右补零
	 * 
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String rpad(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append(str).append("0");// 右补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}

	/**
	 * @see 取得当前日期（格式为：yyyy-MM-dd）
	 * @return String
	 */
	public static String GetDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/**
	 * @see 取得当前日期（格式为：yyyy-MM-dd）
	 * @return String
	 */
	public static String GetDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = date != null ? sdf.format(date) : "";
		return sDate;
	}

	/**
	 * 获取日期，并去掉横杠
	 * 
	 * @return
	 */
	public static String GetDateNoneBreak() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(new Date());
		sDate = sDate.replaceAll("-", "");
		return sDate;
	}

	/**
	 * @see 取得当前时间（格式为：yyy-MM-dd HH:mm:ss）
	 * @return String
	 */
	public static String GetDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/**
	 * @see 取得当前时间（格式为：yyy-MM-dd HH:mm:ss）
	 * @return String
	 */
	public static String GetDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = date != null ? sdf.format(date) : "";
		return sDate;
	}


	/**
	 * 获取当前的时间，不带日期
	 * 
	 * @return
	 */
	public static String GetTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/** 把null转化为"" */
	public static String toString(String str) {
		if (str == null)
			str = "";
		if (str.equals("null"))
			str = "";
		str = str.trim();
		return str;
	}

	/** 把null转化为"" */
	public static String toString(Object str) {
		String ret = "";
		if (str != null) {
			ret = str.toString();
		}
		if (ret.equals("null")) {
			ret = "";
		}
		ret = ret.trim();
		return ret;
	}

	public static Calendar parseDateTime(String baseDate) {
		Calendar cal = null;
		cal = new GregorianCalendar();
		int yy = Integer.parseInt(baseDate.substring(0, 4));
		int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
		int dd = Integer.parseInt(baseDate.substring(8, 10));
		int hh = 0;
		int mi = 0;
		int ss = 0;
		if (baseDate.length() > 12) {
			hh = Integer.parseInt(baseDate.substring(11, 13));
			mi = Integer.parseInt(baseDate.substring(14, 16));
			ss = Integer.parseInt(baseDate.substring(17, 19));
		}
		cal.set(yy, mm, dd, hh, mi, ss);
		return cal;
	}

	/**
	 * 计算两个日期的差
	 * 
	 * @param strDateBegin
	 *            开始日期
	 * @param strDateEnd
	 *            结束日期
	 * @param iType
	 *            类型
	 * @return
	 */
	public static int DateDiff(String strDateBegin, String strDateEnd, int iType) {
		Calendar calBegin = parseDateTime(strDateBegin);
		Calendar calEnd = parseDateTime(strDateEnd);
		long lBegin = calBegin.getTimeInMillis();
		long lEnd = calEnd.getTimeInMillis();
		int ss = (int) ((lEnd - lBegin) / 1000L);
		int min = ss / 60;
		int hour = min / 60;
		int day = hour / 24;
		if (iType == 0)
			return hour;
		if (iType == 1)
			return min;
		if (iType == 2)
			return day;
		else
			return -1;
	}

	/**
	 * @param strDate
	 * @param iCount
	 * @param iType
	 * @return
	 */
	public static String DateAdd(String strDate, int iCount, int iType) {
		Calendar Cal = parseDateTime(strDate);
		int pType = 0;
		if (iType == 0) // 年份
		{
			pType = 1;
		} else if (iType == 1) // 月份
		{
			pType = 2;
		} else if (iType == 2) // 日期
		{
			pType = 5;
		} else if (iType == 3) // 小时
		{
			pType = 10;
		} else if (iType == 4) // 分钟
		{
			pType = 12;
		} else if (iType == 5) // 秒
		{
			pType = 13;
		}
		Cal.add(pType, iCount);
		SimpleDateFormat sdf = null;
		if (iType <= 2)
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(Cal.getTime());
		return sDate;
	}

	/**
	 * 判断字符串的格式
	 * @param string
	 * @return
	 */
	public static String getType(String string) {
		if (isNumber(string))
			return "Number";
		else if (isJson(string))
			return "Json";
		else if (isXML(string))
			return "xml";
		else
			return "String";
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断是否是json结构
	 */
	public static boolean isJson(String value) {
		try {
			JSONObject.parse(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是xml结构
	 */
	public static boolean isXML(String value) {
		try {
			DocumentHelper.parseText(value);
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}

}
