package com.commons.result;

import org.dom4j.Element;

import com.commons.utils.DecodeHelper;
import com.commons.utils.XmlHelper;

public class XmlQueryResult {
	
	private XmlHelper xmlHelper;
	
	private int code;
	
	private String message;
	
	private String rowNum;
	
	public XmlQueryResult()
	{
		this.xmlHelper=new XmlHelper();
		xmlHelper.addRoot("root");
		xmlHelper.addNode("/root","head");
		xmlHelper.addNode("/root/head","code");
		xmlHelper.addNode("/root/head","message");	
		xmlHelper.addNode("/root/head","rownum");
		xmlHelper.addNode("/root","vehcrpara");
	}
	public XmlQueryResult(XmlHelper xmlHelper)
	{
		this.xmlHelper=xmlHelper;
	}
	
	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
		xmlHelper.updateNodeValue("/root/head","rownum",rowNum);		
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		xmlHelper.updateNodeValue("/root/head","code",String.valueOf(code));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		xmlHelper.updateNodeValue("/root/head","message",message);
	}
	
	public void setCodeMessage(Integer sCode,String sMessage,String sRowNum,String svehcrpara)
	{
		this.code=sCode;
		this.message=sMessage;
		this.rowNum=sRowNum;
		xmlHelper.updateNodeValue("/root/head","code",String.valueOf(sCode));
		xmlHelper.updateNodeValue("/root/head","message",sMessage);
		xmlHelper.updateNodeValue("/root/head","rownum",sRowNum);
		xmlHelper.updateNodeValue("/root","vehcrpara",svehcrpara);
	}
	
	public void addRecord(Element element)
	{
		if (element!=null){
		   xmlHelper.addElement("/root/body",element);		
		}
	}
	
	public void addNode(String xPath,String nodeName)
	{
		xmlHelper.addNode(xPath,nodeName);
	}
	
	public void deleteNode(String xPath,String nodeName){
		xmlHelper.deleteNode(xPath, nodeName);
	}
	
	public void addNodeValue(String xPath,String nodeName,String value)
	{
		xmlHelper.addNodeValue(xPath, nodeName, value);
	}

	public String getXml()
	{
		return xmlHelper.getXml();
	}
	
	public XmlHelper decodeText(){
		xmlHelper=DecodeHelper.decodeText("/root/head",xmlHelper);
		if (xmlHelper.getElement("/root","vehispara")!=null){
			xmlHelper=DecodeHelper.decodeText("/root/vehispara",xmlHelper);
		}
		if (xmlHelper.getElement("/root/body","vehispara")!=null){
			xmlHelper=DecodeHelper.decodeText("/root/body/vehispara",xmlHelper);
		}		
		return xmlHelper;
	}
	
	public XmlHelper encodeText(){
		xmlHelper=DecodeHelper.encodeText("/root/head",xmlHelper);
		//if (xmlHelper.getElement("/root","vehispara")!=null){
			xmlHelper=DecodeHelper.encodeText("/root/vehispara",xmlHelper);
		//}
		if (xmlHelper.getElement("/root","body")!=null){
			xmlHelper=DecodeHelper.encodeText("/root/body/vehispara",xmlHelper);
		}
		
		return xmlHelper;		
	}
	public void addRecord(String xPath,Element element)
	{
		if (element!=null){
		   xmlHelper.addElement(xPath,element);		
		}
	}

}
