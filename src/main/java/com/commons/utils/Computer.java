package com.commons.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {

	private static String macAddressStr = null;
	private static String computerName = System.getenv().get("COMPUTERNAME");

	private static final String[] windowsCommand = { "ipconfig", "/all" };
	private static final String[] linuxCommand = { "/sbin/ifconfig", "-a" };
	private static final Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
			Pattern.CASE_INSENSITIVE);

	/*
	 * 取计算机名称
	 */
	public static String getComputerName() {
		if (computerName == null || computerName.equals("")) {
			computerName = System.getenv().get("COMPUTERNAME");
		}

		return computerName;

	}

	public static String getMachineCode(){
		if (macAddressStr == null || macAddressStr.equals("")){
			macAddressStr=getMacAddress();
		}
		
		return DigestUtils.md5Hex(macAddressStr+"@tanzhonghe365.COM").toUpperCase();
	}
	
	public static String getLicenceCode(String organ){
		if (macAddressStr == null || macAddressStr.equals("")){
			macAddressStr=getMacAddress();
		}
		
		return DigestUtils.md5Hex(organ+getMachineCode()+"@300572").toUpperCase();
	}
	/*
	 * 取MAC地址
	 */
	public static String getMacAddress() {
		if (macAddressStr == null || macAddressStr.equals("")) {
			StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
			try {
				List<String> macList = getMacAddressList();
				for (Iterator<String> iter = macList.iterator(); iter.hasNext();) {
					String amac = iter.next();
					if (!amac.equals("0000000000E0")) {
						sb.append(amac);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			macAddressStr = sb.toString();

		}

		return macAddressStr;
	}

	private final static List<String> getMacAddressList() throws IOException {
		final ArrayList<String> macAddressList = new ArrayList<String>();
		final String os = System.getProperty("os.name");
		final String command[];

		if (os.startsWith("Windows")) {
			command = windowsCommand;
		} else if (os.startsWith("Linux")) {
			command = linuxCommand;
		} else {
			throw new IOException("Unknow operating system:" + os);
		}

		final Process process = Runtime.getRuntime().exec(command);

		BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		for (String line = null; (line = bufReader.readLine()) != null;) {
			Matcher matcher = macPattern.matcher(line);
			if (matcher.matches()) {
				macAddressList.add(matcher.group(1));
				// macAddressList.add(matcher.group(1).replaceAll("[-:]",
				// ""));//去掉MAC中的“-”
			}
		}

		process.destroy();
		bufReader.close();
		return macAddressList;
	}

}
