package com.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.sql.Types.BOOLEAN;
import static java.sql.Types.NUMERIC;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType.FORMULA;
import static org.aspectj.apache.bcel.classfile.annotation.ElementValue.STRING;

/**
 * Excel行数据与Java实体类反射映射工具类（无需额外依赖，纯JDK实现）
 */
public class ExcelReflectUtil {

	// 日期格式化（支持Excel中常见的日期格式）
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将Excel指定行范围的数据转换为实体类列表
	 * @param sheet Excel工作表（默认第一个sheet）
	 * @param startRow 开始行号（从0开始，包含）
	 * @param endRow 结束行号（从0开始，包含）
	 * @param clazz 目标实体类字节码
	 * @param <T> 实体类泛型
	 * @return 实体类列表
	 */
	public static <T> List<T> convertRowsToBean(Sheet sheet, int startRow, int endRow, Class<T> clazz) throws Exception {
		List<T> resultList = new ArrayList<>();
		if (sheet == null || startRow > endRow) {
			return resultList;
		}

		// 获取实体类所有字段（包括父类字段）
		Field[] fields = clazz.getDeclaredFields();

		// 遍历Excel行，逐行转换为实体类
		for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue; // 跳过空行
			}

			// 实例化实体类
			T obj = clazz.getDeclaredConstructor().newInstance();

			// 遍历实体类字段，与Excel单元格数据映射（字段顺序需与Excel列顺序一致）
			for (int colNum = 0; colNum < fields.length; colNum++) {
				Field field = fields[colNum];
				field.setAccessible(true); // 允许访问私有字段

				// 获取Excel单元格数据
				Cell cell = row.getCell(colNum);
				Object cellValue = getCellValue(cell);

				// 将单元格数据设置到实体类字段
				setFieldValue(obj, field, cellValue);
			}

			resultList.add(obj);
		}

		return resultList;
	}

	/**
	 * 获取Excel单元格的真实值（自动处理不同单元格类型）
	 */
	static Object getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
			case STRING: // 字符串类型
				return cell.getStringCellValue().trim(); // 去除首尾空格
			case NUMERIC: // 数字类型（包含日期、整数、小数）
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
					// 日期格式单元格
					return cell.getDateCellValue();
				} else {
					// 数字格式（避免长数字自动转为科学计数法）
					double numericValue = cell.getNumericCellValue();
					if (numericValue == Math.floor(numericValue)) {
						return (long) numericValue; // 整数转为Long
					} else {
						return numericValue; // 小数保留Double
					}
				}
			case BOOLEAN: // 布尔类型
				return cell.getBooleanCellValue();
			case FORMULA: // 公式类型（获取计算结果）
				return getCellValue(cell); // 递归获取公式结果
			default:
				return null;
		}
	}

	/**
	 * 将单元格值设置到实体类字段（自动类型转换）
	 */
	static void setFieldValue(Object obj, Field field, Object cellValue) throws IllegalAccessException, ParseException {
		if (cellValue == null) {
			return; // 空值不设置
		}

		Class<?> fieldType = field.getType();

		// 类型匹配与转换
		if (fieldType == String.class) {
			// 所有类型转为字符串（日期格式化为yyyy-MM-dd HH:mm:ss）
			if (cellValue instanceof Date) {
				field.set(obj, DATETIME_FORMAT.format((Date) cellValue));
			} else {
				field.set(obj, cellValue.toString());
			}
		} else if (fieldType == Integer.class || fieldType == int.class) {
			field.set(obj, Integer.valueOf(cellValue.toString()));
		} else if (fieldType == Long.class || fieldType == long.class) {
			field.set(obj, Long.valueOf(cellValue.toString()));
		} else if (fieldType == Double.class || fieldType == double.class) {
			field.set(obj, Double.valueOf(cellValue.toString()));
		} else if (fieldType == Float.class || fieldType == float.class) {
			field.set(obj, Float.valueOf(cellValue.toString()));
		} else if (fieldType == Boolean.class || fieldType == boolean.class) {
			field.set(obj, Boolean.valueOf(cellValue.toString()));
		} else if (fieldType == Date.class) {
			// 字符串日期转为Date类型（支持两种常见格式）
			if (cellValue instanceof String) {
				String dateStr = (String) cellValue;
				if (dateStr.contains(":")) {
					field.set(obj, DATETIME_FORMAT.parse(dateStr));
				} else {
					field.set(obj, DATE_FORMAT.parse(dateStr));
				}
			} else if (cellValue instanceof Date) {
				field.set(obj, cellValue);
			}
		}
	}
}