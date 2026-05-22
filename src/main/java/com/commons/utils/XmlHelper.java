package com.commons.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

public class XmlHelper {

	private Document xmlDoc;

	public XmlHelper() {
		this.xmlDoc = DocumentHelper.createDocument();
	}

	/**
	 * 将字符串读入到xmlDoc对象
	 * 
	 * @param xmlText
	 *            xml格式的字符串
	 */
	public boolean loadFromStr(String xmlText) {
		try {
			this.xmlDoc = DocumentHelper.parseText(xmlText);
			return true;
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("将字符串:" + xmlText + "转为xmlDoc对象异常:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 读取xml文件到xmlDoc对象
	 * 
	 * @param fileName
	 *            文件路径及文件名
	 */
	public boolean loadFromFile(String fileName) {
		SAXReader saxReader = new SAXReader();
		try {
			this.xmlDoc = saxReader.read(new File(fileName));
			System.out.println(xmlDoc.asXML());
			return true;
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("将文件:" + fileName + "读取到xmlDoc对象异常:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 添加根节点
	 * 
	 * @param rootName
	 *            根节点名称
	 * @return
	 */
	public boolean addRoot(String rootName) {
		try {
			this.xmlDoc.addElement(rootName);
			return true;
		} catch (Exception e) {
			System.out.println("添加根节点异常:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 为根节点添加注释
	 * 
	 * @param xPathName
	 *            xpath 路径
	 * @param comment
	 *            注释信息
	 * @return
	 */
	public boolean addComment(String xPathName, String comment) {
		boolean ret = false;
		try {
			List<Element> list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				element.addComment(comment);
				ret = true;
			} else {
				ret = false;
			}
		} catch (Exception e) {
			System.out.println("为节点添加注释异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 为节点添加注释
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @param comment
	 *            注释
	 * @return
	 */
	public boolean addComment(String xPathName, String nodeName, String comment) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				if (element.getText().equals(nodeName)) {
					element.addComment(comment);
				}
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("为节点添加注释异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 添加空节点
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @return
	 */
	public boolean addNode(String xPathName, String nodeName) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				element.addElement(nodeName);
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("添加节点异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	public boolean addElement(String xPathName,Element elementNode) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				//element.addElement(elementNode);
				element.add(elementNode);
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("添加节点异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	public boolean updateNode(String xPathName, String oldNodeName,String newNodeName) {
		boolean ret = false;
		try {			
			Element oldElement=xmlDoc.getRootElement().element(oldNodeName);
			if (oldElement!=null)
			{
			   oldElement.setName(newNodeName);
			}
			ret = true;
		} catch (Exception e) {
			System.out.println("添加节点异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	public Element getRoot(){
		Element element=null;
		try {
			element=xmlDoc.getRootElement();
		} catch (Exception e) {
		}
		return element;
	}
	
	public String getRootxml(){
		Element element=null;
		String sxml="";
		try {
			element=xmlDoc.getRootElement();
			sxml=element.asXML();
		} catch (Exception e) {
		}
		return sxml;
	}
	public Element getElement(String xPathName,String nodeName)
	{
		Element element=null;
		try {
			element=xmlDoc.getRootElement().element(nodeName);
		} catch (Exception e) {
		}
		return element;		
	}

	
	/**
	 * 删除xml文档中的节点
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @return
	 */
	public boolean deleteNode(String xPathName, String nodeName) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {

				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator(nodeName);

				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					element.remove(elementChild);
				}
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("删除节点异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 添加带值的节点
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @param value
	 *            节点值
	 * @return
	 */
	public boolean addNodeValue(String xPathName, String nodeName, String value) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator<Element> iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Element elementChild = element.addElement(nodeName);
				elementChild.setText(value);
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("添加节点异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	public boolean addAttribute(String xPathName, String nodeName, String attrName, String value) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator<Element> iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator(nodeName);
				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					elementChild.addAttribute(attrName, value);
				}
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("添加节点的属性异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 更新节点的值
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @param value
	 *            更新的值
	 * @return
	 */
	public boolean updateNodeValue(String xPathName, String nodeName, String value) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator(nodeName);

				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					elementChild.setText(value);
				}
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("更新节点值异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 更新属性的值
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @param attrName
	 *            属性名称
	 * @param value
	 *            更改的值
	 * @return
	 */
	public boolean updateAttribute(String xPathName, String nodeName, String attrName, String value) {
		boolean ret = false;
		try {
			List list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator(nodeName);
				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					elementChild.addAttribute(attrName, value);
				}
				ret = true;
			}
		} catch (Exception e) {
			System.out.println("更新节点值异常:" + e.getMessage());
			ret = false;
		}
		return ret;
	}

	/**
	 * 获取xml节点的值
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @return
	 */
	public String getInnerText(String xPathName, String nodeName) {
		String ret = "";
		try {
			//System.out.println(xPathName);
			List list = this.xmlDoc.selectNodes(xPathName);
			// System.out.println("list.size():"+list.size());
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				// System.out.println(element.asXML());
				Iterator iterator = element.elementIterator(nodeName);

				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					//System.out.println(elementChild.asXML()+elementChild.nodeCount());
					if (elementChild.nodeCount() >1) {
						// System.out.println("mixed");
						ret = elementChild.asXML();
					} else {
						// System.out.println("not mixed");
					    ret = elementChild.getText();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("获取节点值异常:" + e.getMessage());
		}
		return ret;
	}
	//获取节点数据，如需解码则进行解码
	public String getInnerTextCode(String xPathName, String nodeName, boolean decoded)
    {
		String ret = "";String decval = "";
		try {
			// System.out.println(xPathName);
			List<Element> list = this.xmlDoc.selectNodes(xPathName);
			// System.out.println("list.size():"+list.size());
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				// System.out.println(element.asXML());
				Iterator iterator = element.elementIterator(nodeName);

				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					if (elementChild.nodeCount() > 1) {
						ret = elementChild.asXML();
					} else {
						ret = elementChild.getText();
					}
					if (decoded)
					{
						
						String val = ret.replaceAll("%(?![0-9a-fA-F]{2})", "%25");  
						val = URLDecoder.decode(val, "UTF-8");  
						decval=val;
						if (!decval.equals(""))
						{
							ret = decval;
						}
						
					}
				}
			}
		} catch (Exception e) {
			System.out.println("获取节点值异常:" + e.getMessage());
		}
		return ret;
    }
	/**
	 * 获取属性值
	 * 
	 * @param xPathName
	 *            xpath路径
	 * @param nodeName
	 *            节点名称
	 * @param attrName
	 *            属性名称
	 * @return
	 */
	public String getAttribute(String xPathName, String nodeName, String attrName) {
		String ret = "";
		try {
			List<Element> list = this.xmlDoc.selectNodes(xPathName);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator(nodeName);

				if (iterator.hasNext()) {
					Element elementChild = (Element) iterator.next();
					ret = elementChild.attributeValue(attrName);
				}
			}
		} catch (Exception e) {
			System.out.println("获取节点属性值异常:" + e.getMessage());
		}
		return ret;
	}
	

	// 通过父节点名，节点名，属性获取节点数据
	public String getElementTextByAttr(String path, String parentname, String idindex, String nodeName,
			boolean decoded) {
		String val = "";
		try {
			List list = this.xmlDoc.selectNodes(path);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				String elename = element.getName();
				String id = element.attributeValue("id");
				if ((element.getName().equals(parentname))
						&& (element.attributeValue("id").equals(idindex.toString()))) {
					Iterator iterator = element.elementIterator(nodeName);

					if (iterator.hasNext()) {
						Element elementChild = (Element) iterator.next();
						val = elementChild.getText();
						if (decoded)
						{
							val = val.replaceAll("%(?![0-9a-fA-F]{2})", "%25");  
							val = URLDecoder.decode(val, "UTF-8");  
						    if (!val.equals(""))
						    {
						    	return val;
						    }
						    else
						    {
						    	return elementChild.getText();
						    }
						}
					}
					break;
				}
				val = "";
			}
		} catch (Exception e) {
			System.out.println("获取节点值异常:" + e.getMessage());
			return "";
		}
		return "";
	}

	/**
	 * 获取节点
	 * 
	 * @param xPath
	 * @return
	 */
	public List<Element> selectNodes(String xPath) {
		List<Element> list = this.xmlDoc.selectNodes(xPath);
		return list;
	}

	/**
	 * 获取xml字符串 可以根据options.properties中配置的格式类型输出， 分为pretty和compact两种格式
	 * 
	 * @return
	 */
	public String getXml() {
		// 格式化输出格式
		boolean pretty = true;
		OutputFormat format = null;
		if (pretty) {
			format = OutputFormat.createPrettyPrint();
		} else {
			format = OutputFormat.createCompactFormat();
		}
		StringWriter writer = new StringWriter();
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		// 将document写入到输出流
		try {
			xmlWriter.write(this.xmlDoc);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("格式化输出xml异常:" + e.getMessage());
		}
		return writer.toString();
	}
	/**
	 * 获取xml字符串 可以根据options.properties中配置的格式类型输出， 分为pretty和compact两种格式
	 * 
	 * @return
	 */
	public String removeNullNode(String xPathName) {
		// 格式化输出格式
		boolean pretty = true;
		List<Element> list = this.xmlDoc.selectNodes(xPathName);
		
		Element wdata = this.xmlDoc.getRootElement();
		
		wdata=this.getNodes(wdata,xPathName);
		 
		OutputFormat format = null;
		if (pretty) {
			format = OutputFormat.createPrettyPrint();
		} else {
			format = OutputFormat.createCompactFormat();
		}
		StringWriter writer = new StringWriter();
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		try {
			
			xmlWriter.write(wdata);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("格式化输出xml异常:" + e.getMessage());
		}
		System.out.println("处理完一级空节点格式化输出xml:" + writer.toString());
		return writer.toString();
	}
	
	//删除当前xml文档一级节点所有为空的节点
	public Element getNodes(Element wdata,String saveNode){

		    //递归遍历当前节点所有的子节点  
		    List<Element> listElement=wdata.elements();//所有一级子节点的list  
		    for(Element e:listElement){//遍历所有一级子节点  
		    	 String name=e.getName();//属性名称  
			     String value=e.getStringValue();//属性的值 
 
				 if(e.getTextTrim()==null||e.getTextTrim().equals("")){
					 wdata.remove(e);
				  }
				
//		        this.getNodes(e,saveNode);//递归  
		    } 
		    return wdata;
	}
}
