package com.commons.result;

import com.commons.utils.XmlHelper;

public class XmlResult {
	private int code;
	
	private String message;
	
	private Object obj;
	
	private XmlHelper xmlHelper;

	public XmlResult()
	{
		this.xmlHelper=new XmlHelper();
		xmlHelper.addRoot("root");
		xmlHelper.addNode("/root","head");
		xmlHelper.addNode("/root/head","code");
		xmlHelper.addNode("/root/head","message");	
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		xmlHelper.updateNodeValue("/root/head","code",String.valueOf(code));
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		xmlHelper.updateNodeValue("/root/head","message",message);
		this.message = message;
	}
	
	public void setCodeMessage(int iCode,String sMessage)
	{
		this.code=iCode;
		this.message=sMessage;
		xmlHelper.updateNodeValue("/root/head","code",String.valueOf(iCode));
		xmlHelper.updateNodeValue("/root/head","message",sMessage);
	}
		
	@Override
	public String toString(){
		return String.format("<?xml version=\"1.0\" encoding=\"GBK\"?><root><head><code>%d</code><message>%s</message></head></root>",code,message);
	}
	
	public String getXml()
	{
		return xmlHelper.getXml();
	}
	
}
