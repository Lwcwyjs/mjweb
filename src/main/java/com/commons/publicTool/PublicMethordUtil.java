package com.commons.publicTool;

import com.model.configmange.SysOptions;
import com.model.sysmanage.SysOrganization;
import com.model.webservice.CyToGa;
import com.service.configmanage.SysOptionsService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PublicMethordUtil {

	private static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	

	public String decodeUTF8(String xml_str){
		String str="";
		try{
			str=URLDecoder.decode(xml_str, "utf-8");
			return str;
		}
		catch(Exception ex){
			ex.printStackTrace();
			//str=ex.toString();
		}
		return str;
	}
	public final static String md5(String s) {  
	    char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
	            'a', 'b', 'c', 'd', 'e', 'f' };  
	    try {  
	        byte[] strTemp = s.getBytes("UTF-8");  
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");  
	        mdTemp.update(strTemp);  
	        byte[] md = mdTemp.digest();  
	        int j = md.length;  
	        char str[] = new char[j * 2];  
	        int k = 0;  
	        for (int i = 0; i < j; i++) {  
	            byte byte0 = md[i];  
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
	            str[k++] = hexDigits[byte0 & 0xf];  
	        }  
	        return new String(str);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return null;  
	    }  
	}
public static String convertToXml(Object obj, String encoding) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    } 
	
	public static void main(String[] args) {
		System.out.println(md5("admin系统管理员1101011990030751970"));
	}
	
	
	public static String checkMmStrong(String mm){
		String strong = "";
		int i = mm.matches(".*\\d+.*") ? 1 : 0;
		int j = mm.matches(".*[a-z]+.*") ? 1 : 0;
		int q = mm.matches(".*[A-Z]+.*") ? 1 : 0;
		int k = mm.matches(
				".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*") ? 1 : 0;
		int l = mm.length();
		if (i == 0) {
//			this.returnStr = "密码不包含数字";
			strong = "密码不包含数字";
		}
		if (j == 0) {
//			this.returnStr = "密码不包小写字母";
			strong = "密码不包小写字母";
		}
		if (k == 0) {
//			this.returnStr = "密码不包含特殊字符";
			strong = "密码不包含特殊字符";
		}
		if (q == 0) {
//			this.returnStr = "密码不包小写字母";
			strong = "密码不包大写字母";
		}
		if (l < 8) {
//			this.returnStr = "密码长度小于8";
			strong = "密码长度小于8";
		}
		
		return strong ;
	}

	public static String getOpStatus(String cslb,String csdm, HashMap<String, Object> bdmap) {
		List<SysOptions> sysOptionsList = (List<SysOptions>) bdmap.get(cslb);
		String status="0";
		for (SysOptions sysOptions : sysOptionsList) {
			if (sysOptions.getOption_des().equals(csdm)) {
				if (sysOptions.getStatus() != null) {
					status= sysOptions.getStatus();
				}
			}
		}
		return status;
	}
	public static CyToGa getCyToGa(HashMap<String, Object> bdmap) {
		CyToGa cyToGa=new CyToGa();
		List<SysOptions> sysOptionsList = (List<SysOptions>) bdmap.get("rwcs");
		for (SysOptions sysOptions : sysOptionsList) {
			String value = sysOptions.getOption_value();
			String code = sysOptions.getOption_params();
			switch (value) {
				case "jkxlh":
					cyToGa.setJkxlh(code!=null?code:"");
					break;
				case "yhbz":
					cyToGa.setYhbz(code!=null?code:"");
					break;
				case "dwmc":
					cyToGa.setDwmc(code!=null?code:"");
					break;
				case "dwjgdm":
					cyToGa.setDwjgdm(code!=null?code:"");
					break;
				case "yhxm":
					cyToGa.setYhxm(code!=null?code:"");
					break;
				case "zdbs":
					cyToGa.setZdbs(code!=null?code:"");
					break;
				case "url":
					cyToGa.setUrl(code!=null?code:"");
					break;
				case "cjsqbh":
					cyToGa.setCjsqbh(code!=null?code:"");
					break;
				case "dls":
					cyToGa.setDls(code!=null?code:"");
					break;
				case "querytype":
					cyToGa.setQuerytype(code!=null?code:"");
					break;
				case "sfzhm":
					cyToGa.setSfzhm(code!=null?code:"");
					break;
				case "syzps":
					cyToGa.setSyzps(code!=null?code:"");
					break;
				default:
					break;
			}
		}
		cyToGa.setXtlb("01");
		cyToGa.setGxsj(dateFormater.format(new Date()));
		return cyToGa;
	}
	public static HashMap<String, String> getZpcs(String zpzl, HashMap<String, Object> bdmap) {
		List<SysOptions> sysOptionsList = (List<SysOptions>) bdmap.get("zpzl");
		HashMap<String, String> csmap = new HashMap<String, String>();
		csmap.put("status", "0");
		csmap.put("zpzl", zpzl);
		for (SysOptions sysOptions : sysOptionsList) {
			if (sysOptions.getOption_params().equals(zpzl)) {
				csmap.put("zpzl", sysOptions.getOption_value());
				if (sysOptions.getStatus() != null) {
					csmap.put("status", sysOptions.getStatus());
				}
			}
		}
		return csmap;
	}
	public static  String getOrganZt(String organ, HashMap<String, Object> bdmap) {
		List<SysOrganization> sysOrganizationList = (List<SysOrganization>) bdmap.get("organ");
		HashMap<String, String> csmap = new HashMap<String, String>();
		String status="0";
		for (SysOrganization sysOrganization : sysOrganizationList) {
			if (sysOrganization.getOrgan().equals(organ)) {
				if (sysOrganization.getStatus() != null) {
					status=(sysOrganization.getStatus());
				}
			}
		}
		return status;
	}
	public static CyToGa setPeremeters(CyToGa cyToGa, SysOptionsService sysOptionsService) {
		// TODO Auto-generated method stub
		// 获取查验统一平台配置
		List<SysOptions> optionList = new ArrayList<SysOptions>();
		optionList = sysOptionsService.findOptionList("任务配置参数");
		if (optionList.size() > 0) {
			for (SysOptions sysOptions : optionList) {
				String value = sysOptions.getOption_des();
				String code = sysOptions.getOption_value();
				switch (value) {
				case "jkxlh":
					cyToGa.setJkxlh(code!=null?code:"");
					break;
				case "yhbz":
					cyToGa.setYhbz(code!=null?code:"");
					break;
				case "dwmc":
					cyToGa.setDwmc(code!=null?code:"");
					break;
				case "dwjgdm":
					cyToGa.setDwjgdm(code!=null?code:"");
					break;
				case "yhxm":
					cyToGa.setYhxm(code!=null?code:"");
					break;
				case "zdbs":
					cyToGa.setZdbs(code!=null?code:"");
					break;
				case "url":
					cyToGa.setUrl(code!=null?code:"");
					break;
				case "cjsqbh":
					cyToGa.setCjsqbh(code!=null?code:"");
					break;
				case "dls":
					cyToGa.setDls(code!=null?code:"");
					break;
				case "querytype":
					cyToGa.setQuerytype(code!=null?code:"");
					break;
				case "sfzhm":
					cyToGa.setSfzhm(code!=null?code:"");
					break;
				case "syzps":
					cyToGa.setSyzps(code!=null?code:"");
					break;
				default:
					break;
				}
			}
		}
		cyToGa.setXtlb("01");
		cyToGa.setGxsj(dateFormater.format(new Date()));
		return cyToGa;
	}

}
