package com.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExportUtil {
	private  Sheet sheet;
	private Workbook book;
	private final ExcelType type;
	private CellStyle titleStyle;
	private CellStyle left;
	private CellStyle right;
	private CellStyle defaultType;
	private Map<String,CellStyle> colorStyle = new HashMap<String, CellStyle>();

	public ExcelExportUtil(ExcelType type) {
		this.type = type;
		if(type.equals(ExcelType.XLS)){
			book = new HSSFWorkbook();
			sheet = book.createSheet("任务模版");
		}else if(type.equals(ExcelType.XLSX)){
			book = new XSSFWorkbook();
			sheet = book.createSheet("任务模版");
		}
	}
	public ExcelExportUtil(ExcelType type,String sheetName) {
		this.type = type;
		if(type.equals(ExcelType.XLS)){
			book = new HSSFWorkbook();
			sheet = book.createSheet(sheetName);
		}else if(type.equals(ExcelType.XLSX)){
			book = new XSSFWorkbook();
			sheet = book.createSheet(sheetName);
		}
//		sheet.setDisplayGridlines(false);
//		sheet.setPrintGridlines(false);
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
	}

	public ExcelExportUtil(ExcelType type,int num) {
		this.type = type;
		if(type.equals(ExcelType.XLS)){
			book = new HSSFWorkbook();
		}else if(type.equals(ExcelType.XLSX)){
			book = new XSSFWorkbook();
		}
	}

	public Sheet createSheet(String sheetName){
		Sheet sheet = book.createSheet(sheetName);
		 return sheet;
	}

	public Workbook export(Set<Integer> rowSet,List<ExcelCell> list){
		//创建一个sheet
		Map<Integer,Long> cellWith = new HashMap<Integer, Long>();
		if(list != null && list.size() > 0 && rowSet != null && rowSet.size() > 0){
			for(Integer cnum : rowSet){
				Row row = sheet.createRow(cnum);
				row.setHeight((short) (18 * 20));
				for(ExcelCell c : list){
					if(c.getRow() != cnum.intValue())
						continue;
					Cell cell = row.createCell(c.getCol());
					String val = c.getValue() == null ? "" :String.valueOf(c.getValue());
					cell.setCellValue(val);
					if(c.getAlignment() == ExcelCell.CENTRE){
						cell.setCellStyle(getTitleStyle());
					}else if(c.getAlignment() == ExcelCell.LEFT){
						cell.setCellStyle(getLeftStyle());
					}else if(c.getAlignment() == ExcelCell.RIGHT){
						cell.setCellStyle(getRightStyle());
					}
					if(c.getCellBackColor() != null){
						String  key = c.getAlignment() + "_" + c.getCellBackColor().getIndex();
						CellStyle style = colorStyle.get(key);
						if(style == null){
							style = book.createCellStyle();
							style.setFillForegroundColor(c.getCellBackColor().getIndex());
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							colorStyle.put(key, style);
						}
						cell.setCellStyle(style);
					}
//							else{
//								cell.setCellStyle(getDefaultType());
//							}
					if(c.getMergeRows() > 0 || c.getMergeCols() > 0){
						CellRangeAddress cellRangeAddress =  new CellRangeAddress(c.getRow(), c.getRow() + c.getMergeRows(), c.getCol(), c.getCol() + c.getMergeCols() - 1);
						try {
							setBorder(cellRangeAddress, sheet, book);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sheet.addMergedRegion(cellRangeAddress);
					}
					if(c.getMergeCols() <= 1){
						int len = val.length();
						if(!isContainChinese(val)){
							if(len % 2 == 0)
								len = len / 2 + 1;
							else
								len = len / 2 + 2;
						}
						Long existWith = cellWith.get(c.getCol());
						if(existWith != null){
							if(existWith.longValue() < len){
								cellWith.put(c.getCol(), Long.valueOf(len));
							}
						}else{
							cellWith.put(c.getCol(), Long.valueOf(len));
						}
					}
				}

			}

			int rowNum = sheet.getLastRowNum();
			int cellNum = 0;
			int curRow = 0;
			Row row = null;
			for(int i = 0;i < rowNum;i++){
				row = sheet.getRow(i);
				if(row == null)
					continue;
				if(cellNum > row.getPhysicalNumberOfCells()){
					curRow = i;
				}
			}
			row = sheet.getRow(curRow);
			if(row != null){
//						int rowCount = row.getPhysicalNumberOfCells();
//						for(int i = 0;i < rowCount;i++){
//							sheet.autoSizeColumn(i);
//						}
				for(Integer i : cellWith.keySet()){
					int strLen = cellWith.get(i).intValue();
					if(strLen <= 0)
						strLen = 1;
					//strLen = strLen + 1;
					sheet.setColumnWidth(i, strLen * 512);
				}
			}
		}
		list.clear();
		colorStyle.clear();
		colorStyle = null;
		list = null;
		return book;
	}


	/**
	 * 生成xls格式
	 * @param list
	 */
	public  Workbook  export(List<ExcelCell> list){
		//创建一个sheet
		Map<Integer,Long> cellWith = new HashMap<Integer, Long>();
		if(list != null && list.size() > 0){
			for(ExcelCell cnum : list){
				Row row = sheet.createRow(cnum.getRow());
				row.setHeight((short) (18 * 20));
				for(ExcelCell c : list){
					if(c.getRow() != cnum.getRow())
						continue;
					Cell cell = row.createCell(c.getCol());
					String val = c.getValue() == null ? "" :String.valueOf(c.getValue());
					cell.setCellValue(val);
					if(c.getAlignment() == ExcelCell.CENTRE){
						cell.setCellStyle(getTitleStyle());
					}else if(c.getAlignment() == ExcelCell.LEFT){
						cell.setCellStyle(getLeftStyle());
					}else if(c.getAlignment() == ExcelCell.RIGHT){
						cell.setCellStyle(getRightStyle());
					}
					if(c.getCellBackColor() != null){
						String  key = c.getAlignment() + "_" + c.getCellBackColor().getIndex();
						CellStyle style = colorStyle.get(key);
						if(style == null){
							style = book.createCellStyle();
							style.setFillForegroundColor(c.getCellBackColor().getIndex());
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							colorStyle.put(key, style);
						}
						cell.setCellStyle(style);
					}
//					else{
//						cell.setCellStyle(getDefaultType());
//					}
					if(c.getMergeRows() > 0 || c.getMergeCols() > 0){
						CellRangeAddress cellRangeAddress =  new CellRangeAddress(c.getRow(), c.getRow() + c.getMergeRows(), c.getCol(), c.getCol() + c.getMergeCols() - 1);
						try {
							setBorder(cellRangeAddress, sheet, book);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sheet.addMergedRegion(cellRangeAddress);
					}
					if(c.getMergeCols() <= 1){
						int len = val.length();
						if(!isContainChinese(val)){
							if(len % 2 == 0)
								len = len / 2 + 1;
							else
								len = len / 2 + 2;
						}
						Long existWith = cellWith.get(c.getCol());
						if(existWith != null){
							if(existWith.longValue() < len){
								cellWith.put(c.getCol(), Long.valueOf(len));
							}
						}else{
							cellWith.put(c.getCol(), Long.valueOf(len));
						}
					}
				}

			}

			int rowNum = sheet.getLastRowNum();
			int cellNum = 0;
			int curRow = 0;
			Row row = null;
			for(int i = 0;i < rowNum;i++){
				row = sheet.getRow(i);
				if(row == null)
					continue;
				if(cellNum > row.getPhysicalNumberOfCells()){
					curRow = i;
				}
			}
			row = sheet.getRow(curRow);
			if(row != null){
//				int rowCount = row.getPhysicalNumberOfCells();
//				for(int i = 0;i < rowCount;i++){
//					sheet.autoSizeColumn(i);
//				}
				for(Integer i : cellWith.keySet()){
					int strLen = cellWith.get(i).intValue();
					if(strLen <= 0)
						strLen = 1;
					//strLen = strLen + 1;
					sheet.setColumnWidth(i, strLen * 512);
				}
			}
		}
		list.clear();
		colorStyle.clear();
		colorStyle = null;
		list = null;
		return book;
	}

	public  void  export(List<ExcelCell> list,Sheet sheet){
		this.sheet = sheet;
		//创建一个sheet
		Map<Integer,Long> cellWith = new HashMap<Integer, Long>();
		if(list != null && list.size() > 0){
			for(ExcelCell cnum : list){
				Row row = sheet.createRow(cnum.getRow());
				row.setHeight((short) (18 * 20));
				for(ExcelCell c : list){
					if(c.getRow() != cnum.getRow())
						continue;
					Cell cell = row.createCell(c.getCol());
					String val = c.getValue() == null ? "" :String.valueOf(c.getValue());
					cell.setCellValue(val);
					if(c.getAlignment() == ExcelCell.CENTRE){
						cell.setCellStyle(getTitleStyle());
					}else if(c.getAlignment() == ExcelCell.LEFT){
						cell.setCellStyle(getLeftStyle());
					}else if(c.getAlignment() == ExcelCell.RIGHT){
						cell.setCellStyle(getRightStyle());
					}
					if(c.getCellBackColor() != null){
						String  key = c.getAlignment() + "_" + c.getCellBackColor().getIndex();
						CellStyle style = colorStyle.get(key);
						if(style == null){
							style = book.createCellStyle();
							style.setFillForegroundColor(c.getCellBackColor().getIndex());
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							colorStyle.put(key, style);
						}
						cell.setCellStyle(style);
					}
//					else{
//						cell.setCellStyle(getDefaultType());
//					}
					if(c.getMergeRows() > 0 || c.getMergeCols() > 0){
						CellRangeAddress cellRangeAddress =  new CellRangeAddress(c.getRow(), c.getRow() + c.getMergeRows(), c.getCol(), c.getCol() + c.getMergeCols() - 1);
						try {
							setBorder(cellRangeAddress, sheet, book);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sheet.addMergedRegion(cellRangeAddress);
					}
					if(c.getMergeCols() <= 1){
						int len = val.length();
						if(!isContainChinese(val)){
							if(len % 2 == 0)
								len = len / 2 + 1;
							else
								len = len / 2 + 2;
						}
						Long existWith = cellWith.get(c.getCol());
						if(existWith != null){
							if(existWith.longValue() < len){
								cellWith.put(c.getCol(), Long.valueOf(len));
							}
						}else{
							cellWith.put(c.getCol(), Long.valueOf(len));
						}
					}
				}
			}

			int rowNum = sheet.getLastRowNum();
			int cellNum = 0;
			int curRow = 0;
			Row row = null;
			for(int i = 0;i < rowNum;i++){
				row = sheet.getRow(i);
				if(row == null)
					continue;
				if(cellNum > row.getPhysicalNumberOfCells()){
					curRow = i;
				}
			}
			row = sheet.getRow(curRow);
			if(row != null){
//				int rowCount = row.getPhysicalNumberOfCells();
//				for(int i = 0;i < rowCount;i++){
//					sheet.autoSizeColumn(i);
//				}
				for(Integer i : cellWith.keySet()){
					int strLen = cellWith.get(i).intValue();
					if(strLen <= 0)
						strLen = 1;
					//strLen = strLen + 1;
					sheet.setColumnWidth(i, strLen * 512);
				}
			}
		}
		if(list != null){
			list.clear();
			list = null;
		}
	}

	 public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
		 return m.find();
	 }

	public static void main(String[] args) throws IOException {
		ExcelExportUtil util = new ExcelExportUtil(ExcelType.XLS);
		List<ExcelCell> list = new ArrayList<ExcelCell>();

		IndexedColors[] colors = IndexedColors.values();
		int row = 0;
		int col = 0;
		for(int i = 0;i < colors.length;i++){
			ExcelCell cell = new ExcelCell(row, col, colors[i]);
			cell.setCellBackColor(colors[i]);
			list.add(cell);
			col++;
			if(col == 6){
				row++;
				col = 0;
			}
		}
		Workbook book = util.export(list);
		book.write(new FileOutputStream("D:/text.xls"));
	}

	public CellStyle getDefaultType(){
		if(defaultType == null){
			defaultType =  book.createCellStyle();
			defaultType.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
			defaultType.setAlignment(HorizontalAlignment.CENTER);// 水平
			defaultType.setBorderBottom(BorderStyle.THIN); //下边框
			defaultType.setBorderLeft(BorderStyle.THIN);//左边框
			defaultType.setBorderTop(BorderStyle.THIN);//上边框
			defaultType.setBorderRight(BorderStyle.THIN);//右边框
		}
		return defaultType;
	}

//	public CellStyle getErrorType(){
//		if(errorType == null){
//			errorType =  book.createCellStyle();
//			errorType.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
//			errorType.setAlignment(CellStyle.ALIGN_RIGHT);// 水平
//			errorType.setBorderBottom(CellStyle.BORDER_THIN); //下边框
//			errorType.setBorderLeft(CellStyle.BORDER_THIN);//左边框
//			errorType.setBorderTop(CellStyle.BORDER_THIN);//上边框
//			errorType.setBorderRight(CellStyle.BORDER_THIN);//右边框
//			Font redFont = book.createFont();
//			//颜色
//			redFont.setColor(Font.COLOR_RED);
//			errorType.setFont(redFont);
//		}
//		return defaultType;
//	}

	public CellStyle getTitleStyle(){
		if(titleStyle == null){
			titleStyle = book.createCellStyle();
			Font font = book.createFont();
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
			titleStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
			font.setColor(IndexedColors.BLACK.getIndex());
			font.setFontHeightInPoints((short)14);
			titleStyle.setFont(font);
			titleStyle.setBorderBottom(BorderStyle.THIN); //下边框
			titleStyle.setBorderLeft(BorderStyle.THIN);//左边框
			titleStyle.setBorderTop(BorderStyle.THIN);//上边框
			titleStyle.setBorderRight(BorderStyle.THIN);//右边框
		}
		return titleStyle;
	}

	public CellStyle getLeftStyle(){
		if(left == null){
			left = book.createCellStyle();
			left.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
			left.setAlignment(HorizontalAlignment.CENTER);// 水平
			left.setBorderBottom(BorderStyle.THIN); //下边框
			left.setBorderLeft(BorderStyle.THIN);//左边框
			left.setBorderTop(BorderStyle.THIN);//上边框
			left.setBorderRight(BorderStyle.THIN);//右边框
		}
		return left;
	}

	public CellStyle getRightStyle(){
		if(right == null){
			right = book.createCellStyle();
			right.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
			right.setAlignment(HorizontalAlignment.CENTER);// 水平
			right.setBorderBottom(BorderStyle.THIN); //下边框
			right.setBorderLeft(BorderStyle.THIN);//左边框
			right.setBorderTop(BorderStyle.THIN);//上边框
			right.setBorderRight(BorderStyle.THIN);//右边框
		}
		return right;
	}


	public void setBorder(CellRangeAddress cellRangeAddress, Sheet sheet,
            Workbook wb) throws Exception {
//        RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, wb);
//        RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, wb);
//        RegionUtil.setBorderRight(1, cellRangeAddress, sheet, wb);
//        RegionUtil.setBorderTop(1, cellRangeAddress, sheet, wb);

}

	/**
	 * 设置标题
	 * @param row
	 * @param
	 * @param
	 */
	public void createTitle(int row,String title,int mergeRows,int mergeCells){
			Row r = sheet.createRow(row);
			r.setHeight((short) (20 * 20));
			Font font = book.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
			CellStyle style = book.createCellStyle();
			style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
			style.setAlignment(HorizontalAlignment.CENTER);// 水平
//			gu((short)1);
//			style.setBorderLeft((short)1);
//			style.setBorderTop((short)1);
//			style.setBorderRight((short)1);
			style.setFont(font);
			if(type.equals(ExcelType.XLS))
				style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
			if(type.equals(ExcelType.XLSX))
				style.setFillBackgroundColor(new XSSFColor(Color.GRAY).getIndexed());
			Cell cell = r.createCell(0);
			cell.setCellValue(title);
			cell.setCellStyle(style);
			if(mergeCells > 0 || mergeRows > 0){
				if(mergeCells <= 0)
					mergeCells = 1;
				if(mergeRows < 0)
					mergeRows = 0;
				sheet.addMergedRegion(new CellRangeAddress(row, row + mergeRows,0, mergeCells - 1));
			}
	}

	public Sheet getSheet(){
		return sheet;
	}
	public Workbook getWorkbook(){
		return book;
	}
}
