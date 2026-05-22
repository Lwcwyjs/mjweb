package com.commons.utils;

import org.dom4j.Element;


public class XmlWriteResult {
	
	private XmlHelper xmlHelper;
	
	private String code;
	
	private String message;
	
	public XmlWriteResult()
	{
		this.xmlHelper=new XmlHelper();
		xmlHelper.addRoot("root");
		xmlHelper.addNode("/root","head");
		xmlHelper.addNode("/root/head","code");
		xmlHelper.addNode("/root/head","message");	
	}
		


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
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
	
	public void setCodeMessage(String sCode,String sMessage)
	{
		this.code=sCode;
		this.message=sMessage;
		xmlHelper.updateNodeValue("/root/head","code",String.valueOf(sCode));
		xmlHelper.updateNodeValue("/root/head","message",sMessage);		
	}

	
	public void addRecord(Element element)
	{
		if (element!=null){
		   xmlHelper.addElement("/root/body",element);		
		}
	}
	public void addRecord(String xPath,Element element)
	{
		if (element!=null){
		   xmlHelper.addElement(xPath,element);		
		}
	}
	public void addNode(String xPath,String nodeName)
	{
		xmlHelper.addNode(xPath,"vehcrpara");
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
		if (xmlHelper.getElement("/root","vehcrpara")!=null){
			xmlHelper=DecodeHelper.decodeText("/root/vehcrpara",xmlHelper);
		}
		if (xmlHelper.getElement("/root/body","vehcrpara")!=null){
			xmlHelper=DecodeHelper.decodeText("/root/body/vehcrpara",xmlHelper);
		}		
		return xmlHelper;
	}
	
	public XmlHelper encodeText(){
		xmlHelper=DecodeHelper.encodeText("/root/head",xmlHelper);
		//if (xmlHelper.getElement("/root","vehcrpara")!=null){
			xmlHelper=DecodeHelper.encodeText("/root/vehcrpara",xmlHelper);
		//}
		if (xmlHelper.getElement("/root","body")!=null){
			xmlHelper=DecodeHelper.encodeText("/root/body/vehcrpara",xmlHelper);
		}
		
		return xmlHelper;		
	}
	

}
