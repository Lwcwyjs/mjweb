package com.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DecodeHelper {
	
	protected static Logger logger=LoggerFactory.getLogger(DecodeHelper.class);

	/**
	 * 将xml文档的节点的值解码 为iso-8859-1
	 * 
	 * @param xPath
	 *            xPath路径
	 * @param xmlEncode
	 *            编码的xml文档
	 * @return
	 */
	public static XmlHelper decodeText(String xPath, XmlHelper xmlEncode) {
		//XmlHelper xmlDecode = new XmlHelper();
		String nodeName;
		String nodeValue;
		List<Element> list = xmlEncode.selectNodes(xPath);
		Iterator<Element> iter = list.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Iterator<?> iterator = element.elementIterator();
			while (iterator.hasNext()) {
				Element elementChild = (Element) iterator.next();
				nodeName = elementChild.getName();
				nodeValue = elementChild.getText();
				try {
					nodeValue=URLDecoder.decode(nodeValue,"UTF-8");
					elementChild.setText(nodeValue);
					//xmlEncode.updateNodeValue(xPath, nodeName, nodeValue);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		//xmlDecode = xmlEncode;
		return xmlEncode;
	}

	/**
	 * 将xml文档节点的值编码UTF-8
	 * 
	 * @param xPath
	 *            xPath路径
	 * @param xmlDecode
	 *            未编码的xml文档
	 * @return
	 */
	public static XmlHelper encodeText(String xPath, XmlHelper xmlDecode) {
		//logger.debug("xml Data:" + xmlDecode.getXml());
		String nodeName;
		String nodeValue;
		List<Element> list = xmlDecode.selectNodes(xPath);
		Iterator<Element> iter = list.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Iterator<?> iterator = element.elementIterator();
			while (iterator.hasNext()) {
				Element elementChild = (Element) iterator.next();
				nodeName = elementChild.getName();
				nodeValue = elementChild.getText();
				try {
					nodeValue=URLEncoder.encode(nodeValue,"UTF-8");
					elementChild.setText(nodeValue);
					//xmlDecode.updateNodeValue(xPath, nodeName, nodeValue);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		//xmlEncode = xmlDecode;
		return xmlDecode;
	}
	
	public static String decodeText(String sSrc)
	{
		String sDes=sSrc;
		try {
			return URLDecoder.decode(sDes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			sDes="解码异常:"+e.getMessage();
			logger.error("字符串解码异常:"+sDes+" "+e.getMessage());
			return "解码异常:"+e.getMessage();
		}
	}
	
	public static String encodeText(String sSrc)
	{
		String sDes=sSrc;
		try {
			return URLEncoder.encode(sDes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			sDes="编码异常:"+e.getMessage();
			logger.error("字符串编码异常:"+sDes+" "+e.getMessage());
			return "编码异常:"+e.getMessage();
		}		
	}
}
