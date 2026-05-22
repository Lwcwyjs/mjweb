package com.util;

import org.apache.poi.ss.usermodel.IndexedColors;

public class ExcelCell {
	/**
	 * 左
	 */
	static public final int LEFT = 0;
	/**
	 * 居中
	 */
	static public final int CENTRE = 1;
	/**
	 * 右
	 */
	static public final int RIGHT = 2;
	// 行，列
	private int row, col;
	// 合并行数，合并列数
	private int mergeRows, mergeCols;
	// 单元格文字
	private Object value;
	// 单元格字体名称
	private String fontName;
	// 单元格字体大小
	private int fontSize;
	//对齐方式
	private int alignment;

	private IndexedColors cellBackColor;

	public ExcelCell(int row, int col, Object value) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.fontName = "Arial";
		this.fontSize = 10;
		this.alignment = 0;
	}


	public ExcelCell(int row, int col, Object value,
			int mergeRows, int mergeCols) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.mergeCols = mergeCols;
		this.mergeRows = mergeRows;
	}

	public ExcelCell(int row, int col, Object value, int border) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.fontName = "Arial";
		this.fontSize = 10;
	}

	public ExcelCell(int row, int col, Object value, String fontName,
			int fontSize, int alignment) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.alignment = alignment;
	}

	public ExcelCell(int row, int col, Object value, String fontName,
			int fontSize,  int alignment, int mergeRows,
			int mergeCols) {
		super();
		this.row = row;
		this.col = col;
		this.mergeRows = mergeRows;
		this.mergeCols = mergeCols;
		this.value = value;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.alignment = alignment;
	}
	public ExcelCell(int row, int col, Object value, IndexedColors cellBackColor) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.cellBackColor = cellBackColor;
	}

	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getMergeRows() {
		return mergeRows;
	}
	public void setMergeRows(int mergeRows) {
		this.mergeRows = mergeRows;
	}
	public int getMergeCols() {
		return mergeCols;
	}
	public void setMergeCols(int mergeCols) {
		this.mergeCols = mergeCols;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getAlignment() {
		return alignment;
	}
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}


	public IndexedColors getCellBackColor() {
		return cellBackColor;
	}


	public void setCellBackColor(IndexedColors cellBackColor) {
		this.cellBackColor = cellBackColor;
	}

}
