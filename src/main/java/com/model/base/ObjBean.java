package com.model.base;

public class ObjBean {
	
	private String fieldName;
	
	private String fieldType;
	
	private String fieldValue;
	
	@Override
	public String toString() {
		return "ObjBean [fieldName=" + fieldName + ", fieldType=" + fieldType + ", fieldValue=" + fieldValue + "]";
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	

}
