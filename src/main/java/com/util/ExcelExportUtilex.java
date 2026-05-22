package com.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExportUtilex {
	private final Workbook workbook;
	private final Sheet sheet;
	private final String filePath; // 仅记录文件路径，不提前创建输出流
	private int currentRow = 0;
	private CellStyle borderStyle;

	public ExcelExportUtilex(ExcelType type, String filePath) {
		if (type == ExcelType.XLS) {
			this.workbook = new HSSFWorkbook();
		} else {
			throw new IllegalArgumentException("暂不支持的Excel类型");
		}
		this.sheet = workbook.createSheet("数据导出");
		this.filePath = filePath; // 延迟创建输出流
		this.borderStyle = createBorderStyle();
	}

	private CellStyle createBorderStyle() {
		CellStyle style = workbook.createCellStyle();
		short borderColor = IndexedColors.BLACK.getIndex();

		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(borderColor);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(borderColor);
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(borderColor);
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(borderColor);

		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		return style;
	}

	public void writeHeader(List<ExcelCell> headerCells) {
		Row row = sheet.createRow(currentRow);
		for (ExcelCell cell : headerCells) {
			Cell excelCell = row.createCell(cell.getCol());
			excelCell.setCellValue(cell.getValue().toString());
			excelCell.setCellStyle(borderStyle);
		}
		currentRow++;
	}

	public void writePage(List<ExcelCell> dataCells) {
		if (dataCells.isEmpty()) return;

		for (ExcelCell cell : dataCells) {
			int rowNum = currentRow + cell.getRow();
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
				row.setHeightInPoints(20);
			}

			Cell excelCell = row.createCell(cell.getCol());
			String value = cell.getValue() != null ? cell.getValue().toString() : "";
			excelCell.setCellValue(value);
			excelCell.setCellStyle(borderStyle);
		}

		int maxLocalRow = dataCells.stream().mapToInt(ExcelCell::getRow).max().orElse(0);
		currentRow += (maxLocalRow + 1);
	}

	// 调整列宽（内存中操作）
	private void adjustColumnWidths() {
		int lastCol = sheet.getRow(0).getLastCellNum() - 1;
		for (int col = 0; col <= lastCol; col++) {
			sheet.autoSizeColumn(col, true); // 包含所有行计算

			// 限制超宽列（如照片URL）
			int maxWidth = 30 * 256; // 30字符上限
			int currentWidth = sheet.getColumnWidth(col);
			if (currentWidth > maxWidth) {
				sheet.setColumnWidth(col, maxWidth);
			}

			// 打印最终计算的列宽（验证用）
			System.out.println("列" + col + "最终宽度：" + sheet.getColumnWidth(col));
		}
	}

	// 最后一次性写入文件（核心修复）
	public void finish() throws IOException {
		adjustColumnWidths(); // 先调整列宽（内存中）

		// 使用 ByteArrayOutputStream 作为中间缓冲，避免分页写入冲突
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 FileOutputStream fos = new FileOutputStream(filePath)) {

			workbook.write(bos); // 先写入内存缓冲
			bos.writeTo(fos); // 再一次性写入磁盘文件
			fos.flush();
		} finally {
			workbook.close();
		}
	}

	public enum ExcelType {
		XLS
	}
}