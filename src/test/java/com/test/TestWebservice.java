//package com.anche.ajxm.test;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import com.utils.commons.DecodeHelper;
//import com.utils.commons.JaxbUtil;
//import com.utils.commons.XmlHelper;
//
//public class TestWebservice {
//
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpload(){
//		XmlHelper xmlHelper=new XmlHelper();
//		xmlHelper.addRoot("root");
//		xmlHelper.addNode("/root","QueryCondition");
//		xmlHelper.addNodeValue("/root/QueryCondition","jyjgbh","010288376112112");
//		xmlHelper.addNodeValue("/root/QueryCondition","jylb","01");
//		xmlHelper.addNodeValue("/root/QueryCondition","hphm","粤B12345");
//		xmlHelper.addNodeValue("/root/QueryCondition","syr","测试单位");
//		xmlHelper.addNodeValue("/root/QueryCondition","cpxh","桑塔纳");
//		xmlHelper.addNodeValue("/root/QueryCondition","fdjh","3322321121212");
//		xmlHelper.addNodeValue("/root/QueryCondition","clsbdh","DDFEWEW34342121");
//		xmlHelper.addNodeValue("/root/QueryCondition","csys","01");
//		xmlHelper.addNodeValue("/root/QueryCondition","ccrq","2016-08-01");
//		xmlHelper.addNodeValue("/root/QueryCondition","dlzd","01");
//		xmlHelper.addNodeValue("/root/QueryCondition","xzqhbm","01");
//
//		xmlHelper=DecodeHelper.encodeText("/root/QueryCondition",xmlHelper);
//		System.out.println(xmlHelper.getXml());
//
//	}
//
//}
