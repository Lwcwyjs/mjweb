package com.test;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import com.commons.utils.StringUtils;

public class TestStr {

	public static void main(String[] args) {		
		ProtectionDomain pd = StringUtils.class.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();  
		System.out.println(cs.getLocation());  
	}



}
