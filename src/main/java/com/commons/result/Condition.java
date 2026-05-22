package com.commons.result;

import java.io.Serializable;

public class Condition implements Serializable {
	private String columnname;// 字段名
	private String columntype="string";// 字段类型
	private String columnvalue;// 字段值
	private String operator="=";// 运算符
	private String logic="and";// 逻辑
	private String liketype="LR";// like通配符位置

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumntype() {
		return columntype;
	}

	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}

	public String getColumnvalue() {
		return columnvalue;
	}

	public void setColumnvalue(String columnvalue) {
		this.columnvalue = columnvalue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getLiketype() {
		return liketype;
	}

	public void setLiketype(String liketype) {
		this.liketype = liketype;
	}

}
