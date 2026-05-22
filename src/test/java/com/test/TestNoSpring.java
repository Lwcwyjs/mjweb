package com.test;

import static org.junit.Assert.*;

import com.commons.utils.DealString;
import org.junit.Test;

public class TestNoSpring {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDateDiff(){
		String dtStart="2016-11-11 12:30:45";
		String dtEnd="2016-11-11 13:45:20";
		
		System.out.println(DealString.DateDiff(dtStart, dtEnd,1));
	}
	
	@Test
	public void testSystem(){
		String osName = System.getProperty("os.name");
//		System.out.println(osName);
		if (osName.matches("^(?i)Windows.*$")){
			System.out.println("windows");
		} else{
			System.out.println("linux");
		}
		
	}

}
