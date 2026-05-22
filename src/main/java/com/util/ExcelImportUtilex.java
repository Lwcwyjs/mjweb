package com.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ExcelImportUtilex {
	// 已有的成员变量（保持不变）
	private final File excelFile;
	private Workbook workbook;
	private Sheet sheet;
	private int headerRowNum = 0;
	private Map<String, Integer> headerMap;
	private int totalDataRows; // 总数据行数（新增，用于校验）
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Pattern phonePattern = Pattern.compile("^1[3-9]\\d{9}$"); // 手机号正则

	// 构造方法（保持不变）
	public ExcelImportUtilex(String filePath) throws IOException, InvalidFormatException {
		this.excelFile = new File(filePath);
		this.workbook = WorkbookFactory.create(excelFile);
		this.sheet = workbook.getSheetAt(0);
		this.headerMap = loadHeaderMap();
		this.totalDataRows = getTotalDataRows(); // 初始化总数据行数
	}

	// ------------------------------
	// 新增工具方法：统一处理单元格值，兼容所有类型
	// ------------------------------
	/**
	 * 自动识别单元格类型，转换为字符串返回
	 * 支持：数字、字符串、日期、布尔值、公式等所有Excel单元格类型
	 */
	/**
	 * 自动识别单元格类型（兼容POI 3.x版本），转换为字符串返回
	 * 支持：数字、字符串、日期、布尔值、公式等所有Excel单元格类型
	 */
	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return ""; // 单元格为空，返回空字符串
		}

		// 旧版本POI：getCellType()返回int类型，用常量判断
		int cellType = cell.getCellType();

		// 处理公式单元格：先获取公式计算结果的类型
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		switch (cellType) {
			case Cell.CELL_TYPE_STRING:
				// 字符串类型：直接去空格返回
				return cell.getStringCellValue().trim();
			case Cell.CELL_TYPE_NUMERIC:
				// 数字类型：判断是否为日期，否则按数字处理（避免科学计数法）
				if (DateUtil.isCellDateFormatted(cell)) {
					// 日期类型：转换为统一格式（yyyy-MM-dd HH:mm:ss）
					return sdfDateTime.format(cell.getDateCellValue());
				} else {
					// 普通数字：转换为字符串（避免123.0变成123，或123456789变成1.23456789E8）
					double numericValue = cell.getNumericCellValue();
					if (numericValue == Math.floor(numericValue)) {
						// 整数：直接转long再转字符串
						return String.valueOf((long) numericValue);
					} else {
						// 小数：保留原始精度（可根据需求调整，如保留2位小数）
						return String.valueOf(numericValue);
					}
				}
			case Cell.CELL_TYPE_BOOLEAN:
				// 布尔值：转换为"true"/"false"字符串
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_BLANK:
				// 空单元格：返回空字符串
				return "";
			case Cell.CELL_TYPE_ERROR:
				// 错误值：返回空字符串
				return "";
			default:
				// 其他类型：返回空字符串
				return "";
		}
	}

	// ------------------------------
	// 修改readBatchData方法：使用新的单元格处理工具
	// ------------------------------
	public <T> List<T> readBatchData(int currentBatch, int batchSize, Class<T> clazz) throws Exception {
		List<T> dataList = new ArrayList<>();
		if (headerMap.isEmpty()) {
			return dataList;
		}

		// 计算当前批次的行范围（数据行从表头行下一行开始）
		int dataStartRow = headerRowNum + 1; // 数据起始行（表头下一行）
		int totalDataRows = getTotalDataRows(); // 总数据行数
		int startRow = dataStartRow + (currentBatch - 1) * batchSize;
		int endRow = Math.min(startRow + batchSize - 1, dataStartRow + totalDataRows - 1);

		// 获取实体类所有带@ExcelColumn注解的字段
		List<Field> annotationFields = getAnnotationFields(clazz);

		// 遍历当前批次的行，逐行转换为实体类
		for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue; // 跳过空行
			}

			T obj = clazz.getDeclaredConstructor().newInstance(); // 实例化实体类

			// 遍历注解字段，按表头名称匹配列索引，赋值给实体类
			for (Field field : annotationFields) {
				ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
				String headerName = excelColumn.name(); // 注解指定的Excel表头名称
				boolean required = excelColumn.required(); // 是否必填

				// 获取该表头对应的列索引
				Integer colIndex = headerMap.get(headerName);
				if (colIndex == null) {
					if (required) {
						throw new RuntimeException("Excel缺少必填表头：" + headerName);
					}
					continue; // 非必填表头缺失，跳过该字段
				}

				// 获取单元格数据：使用新工具方法，兼容所有类型
				Cell cell = row.getCell(colIndex);
				String cellValue = getCellValueAsString(cell); // 关键修改：替换原来的强制读取

				// 反射赋值给实体类字段（如果需要空值处理，可在这里添加）
				field.setAccessible(true);
				ExcelReflectUtil.setFieldValue(obj, field, cellValue); // 复用类型转换工具
			}

			dataList.add(obj);
		}
		return dataList;
	}

	// ------------------------------
	// 其他原有方法（保持不变）
	// ------------------------------
	public void exportInvalidData(List<Map<String, String>> invalidData, String filePath) throws IOException {
		// 你的原有代码，无需修改
		if (invalidData.isEmpty()) {
			return;
		}

		Workbook errorWorkbook = new XSSFWorkbook();
		Sheet errorSheet = errorWorkbook.createSheet("错误数据");
		int rowIndex = 0;

		Row headerRow = errorSheet.createRow(rowIndex++);
		List<String> originalHeaders = new ArrayList<>(headerMap.keySet());
		for (int i = 0; i < originalHeaders.size(); i++) {
			headerRow.createCell(i).setCellValue(originalHeaders.get(i));
		}
		headerRow.createCell(originalHeaders.size()).setCellValue("错误信息");

		for (Map<String, String> errorRecord : invalidData) {
			Row dataRow = errorSheet.createRow(rowIndex++);
			String userRowNum = errorRecord.get("行号");
			int excelRowNum = Integer.parseInt(userRowNum) - 1;
			Row originalRow = sheet.getRow(excelRowNum);

			for (int i = 0; i < originalHeaders.size(); i++) {
				String headerName = originalHeaders.get(i);
				Integer colIndex = headerMap.get(headerName);
				Cell originalCell = originalRow.getCell(colIndex);
				// 这里也建议替换为新的工具方法，避免同样的错误
				String cellValue = getCellValueAsString(originalCell);
				dataRow.createCell(i).setCellValue(cellValue);
			}

			StringBuilder errorMsg = new StringBuilder();
			for (Map.Entry<String, String> entry : errorRecord.entrySet()) {
				if (!"行号".equals(entry.getKey())) {
					errorMsg.append(entry.getKey()).append("：").append(entry.getValue()).append("；");
				}
			}
			dataRow.createCell(originalHeaders.size()).setCellValue(errorMsg.toString());
		}

		for (int i = 0; i <= originalHeaders.size(); i++) {
			errorSheet.autoSizeColumn(i);
		}

		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			errorWorkbook.write(fos);
		} finally {
			errorWorkbook.close();
		}
	}

	private boolean validateCphm(String cphm) {
		return cphm.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{5}$");
	}

	private boolean validateDate(String dateStr) {
		try {
			if (dateStr.contains(":")) {
				sdfDateTime.parse(dateStr);
			} else {
				sdf.parse(dateStr);
			}
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	private Map<String, Integer> loadHeaderMap() {
		Map<String, Integer> map = new HashMap<>();
		Row headerRow = sheet.getRow(headerRowNum);
		if (headerRow == null) {
			throw new RuntimeException("Excel无表头行");
		}

		for (int col = 0; col <= headerRow.getLastCellNum(); col++) {
			Cell cell = headerRow.getCell(col);
			if (cell != null) {
				String headerName = cell.getStringCellValue().trim();
				if (!headerName.isEmpty()) {
					map.put(headerName, col);
				}
			}
		}
		return map;
	}

	private <T> List<Field> getAnnotationFields(Class<T> clazz) {
		List<Field> fieldList = new ArrayList<>();
		Field[] allFields = clazz.getDeclaredFields();
		for (Field field : allFields) {
			if (field.isAnnotationPresent(ExcelColumn.class)) {
				fieldList.add(field);
			}
		}
		return fieldList;
	}

	public int getTotalDataRows() {
		return sheet.getLastRowNum() - headerRowNum;
	}

	public void close() throws IOException {
		if (workbook != null) {
			workbook.close();
		}
	}
}