package com.test;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import com.commons.utils.DealString;
import com.commons.utils.XmlHelper;
import org.junit.Test;

public class TestJaxb {

	@Test
	public void test() throws ParseException {
		//int i=6;
		//System.out.println(i%2);		
		NumberFormat format=NumberFormat.getNumberInstance();
		System.out.println(format.parse("111133.987"));
	}
	
	@Test
	public void testPath() throws IOException
	{
//		// 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
//        File f = new File(this.getClass().getResource("/").getPath());
//        System.out.println(f);
//
//        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
//        File f2 = new File(this.getClass().getResource("").getPath());
//        System.out.println(f2);
//
//        // 第二种：获取项目路径    D:\git\daotie\daotie
//        File directory = new File("");// 参数为空
//        String courseFile = directory.getCanonicalPath();
//        System.out.println(courseFile);
//
//
//        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
//        URL xmlpath = this.getClass().getClassLoader().getResource("");
//        System.out.println(xmlpath);
//
//
//        // 第四种： D:\git\daotie\daotie
//        System.out.println(System.getProperty("user.dir"));
//        /*
//         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
//         * 获取当前工程路径
//         */
//
//        // 第五种：  获取所有的类路径 包括jar包的路径
//        System.out.println(System.getProperty("java.class.path"));
		String filepath="";
		String pssj= DealString.GetDateTime();
		filepath=filepath+pssj.substring(0,4)+"\\"+pssj.substring(5,7)+"\\"+pssj.substring(8,10);
		System.out.println(filepath);

	}
	
	@Test
	public void testNode()
	{
		XmlHelper xmlHelper=new XmlHelper();
		xmlHelper.addRoot("root");
		xmlHelper.addNode("/root","QueryCondition");
		xmlHelper.addNodeValue("/root/QueryCondition","cphm","粤B123456");
		xmlHelper.addNodeValue("/root/QueryCondition","hpzl","大型汽车");
		System.out.println(xmlHelper.getXml());
		xmlHelper.updateNode("/root","QueryCondition","vehispara");
		System.out.println(xmlHelper.getXml());		
	}
	
	@Test
	public void testXmlHelper()
	{
		//System.out.println(aDeal.getClassType("H1"));
		XmlHelper xmlHelper=new XmlHelper();
		//xmlHelper.addRoot("root");
		System.out.println(xmlHelper.getXml());		
	}
	

}
