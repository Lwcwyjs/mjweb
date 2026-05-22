package com.commons.annotation.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.annotation.DV;
import com.commons.utils.RegexType;
import com.commons.utils.RegexUtils;
import com.commons.utils.StringUtils;

/**
 * 注解解析
 * 
 * @author Goofy
 */
public class ValidateService {

	private final Logger logger = LoggerFactory.getLogger(ValidateService.class);
	private static DV dv;

	public ValidateService() {
		super();
	}

	// 解析的入口
	public static List<String> valid(Object object) {
		List<String> validateResults = new ArrayList<String>();
		String validateResult = "";
		// object不为空的情况下做属性验证，为空的情况下返回验证结果对象为空
		if (object != null) {
			// 获取object的类型
			Class<? extends Object> clazz = object.getClass();
			// 获取该类型声明的成员
			Field[] fields = clazz.getDeclaredFields();
			// 遍历属性
			for (Field field : fields) {
				// 对于private私有化的成员变量，通过setAccessible来修改器访问权限
				field.setAccessible(true);
				validateResult = validate(field, object);
				// 重新设置会私有权限
				field.setAccessible(false);
				if (!validateResult.equalsIgnoreCase("")) {
					validateResults.add(validateResult);
				}
			}

			// 获取父类申明的成员
			Field[] fieldsSuper = clazz.getSuperclass().getDeclaredFields();
			for (Field fieldSuper : fieldsSuper) {
				// 对于private私有化的成员变量，通过setAccessible来修改器访问权限
				fieldSuper.setAccessible(true);
				validateResult = validate(fieldSuper, object);
				// 重新设置会私有权限
				fieldSuper.setAccessible(false);
				if (!validateResult.equalsIgnoreCase("")) {
					validateResults.add(validateResult);
				}
			}
		} else {
			validateResults.add("验证对象为空");
		}
		return validateResults;
	}

	public static String validate(Field field, Object object) {

		String description;
		Object value;
		String ret = "";
		try {
			// 获取对象的成员的注解信息
			dv = field.getAnnotation(DV.class);
			value = field.get(object);

			if (dv == null)
				return ret;

			description = dv.description().equals("") ? field.getName() : dv.description();

			/************* 注解解析工作开始 ******************/
			if (!dv.nullable()) {
				if (value == null || StringUtils.isBlank(value.toString())) {
					ret = ret.equalsIgnoreCase("") ? description + "不能为空" : ret + "," + description + "不能为空";
				}
			}

			if (value != null) {
				if (RegexUtils.strLength(value.toString()) > dv.maxLength() && dv.maxLength() != 0) {
					ret = ret.equalsIgnoreCase("") ? description + "长度不能超过" + dv.maxLength()
							: ret + "," + description + "长度不能超过" + dv.maxLength();
				}

				if (RegexUtils.strLength(value.toString()) < dv.minLength() && dv.minLength() != 0) {
					ret = ret.equalsIgnoreCase("") ? description + "长度不能小于" + dv.minLength()
							: ret + "," + description + "长度不能小于" + dv.minLength();
				}

				if (dv.regexType() != RegexType.NONE) {
					switch (dv.regexType()) {
					case NONE:
						break;
					case SPECIALCHAR:
						if (RegexUtils.hasSpecialChar(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "不能含有特殊字符"
									: ret + "," + description + "不能含有特殊字符";
						}
						break;
					case CHINESE:
						if (RegexUtils.isChinese2(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "不能含有中文字符"
									: ret + "," + description + "不能含有中文字符";
						}
						break;
					case EMAIL:
						if (!RegexUtils.isEmail(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "地址格式不正确"
									: ret + "," + description + "地址格式不正确";
						}
						break;
					case IP:
						if (!RegexUtils.isIp(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "地址格式不正确"
									: ret + "," + description + "地址格式不正确";
						}
						break;
					case NUMBER:
						if (!RegexUtils.isNumber(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "不是数字" : ret + "," + description + "不是数字";
						}
						break;
					case PHONENUMBER:
						if (!RegexUtils.isPhoneNumber(value.toString())) {
							ret = ret.equalsIgnoreCase("") ? description + "不是有效的电话号码"
									: ret + "," + description + "不是有效的电话号码";
						}
						break;
					default:
						break;
					}
				}
				if (!dv.regexExpression().equals("")) {
					if (!value.toString().matches(dv.regexExpression())) {
						ret = ret.equalsIgnoreCase("") ? description + "格式不正确" : ret + "," + description + "格式不正确";
					}
				}
			}
			/************* 注解解析工作结束 ******************/
		} catch (Exception e) {
			ret = e.getMessage();

		}
		return ret;
	}

	
}
