package com.commons.publicTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PublicTool {
	
	public static void main(String[] args) {
		SimpleDateFormat shortDateFormater = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		try {
			Date dateLine = shortDateFormater.parse("2030-11-30 00:00:00");
			dateLine = getDateAfterNDay(dateLine,1);
			System.out.println(dateLine.before(new Date()));
//			System.out.println(getDaybetwTWafterDay(new Date(),dateLine));
			System.out.println(getDaybetwTWafterDay1(new Date(),dateLine));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取两日期相差天数  afterDay - beforDay
	 * @author songdh
	 * @since 2018-08-28
	 * @param  afterDay,beforDay
	 * @see 获取两日期相差天数  afterDay - beforDay
	 * 
	 */
	public static int getDaybetwTWafterDay1(Date beforDay,Date afterDay){
	       if (null == beforDay || null == afterDay) {

	           return -1;

	       }
	       
	       long intervalMilli = afterDay.getTime() - beforDay.getTime();

	       return (int) (intervalMilli / (24 * 60 * 60 * 1000));
		
	}

	/**
	 * @description 获取thisDay日期后N天的时间
	 * @author songdh
	 * @since 2018-08-28
	 * 
	 */
	public static Date getDateAfterNDay(Date thisDay,Integer N){
		int dayLong = 0;
		Calendar ca = Calendar.getInstance();
		ca.setTime(thisDay);
		ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
	    return ca.getTime();
		
	}
	
	/**
	 * @description 获取thisTime时间后N分钟的时间
	 * @author liwq
	 * @since 2018-07-25
	 * 
	 */
	public static Date getTimeAfterNMinute(Date thisTime,Integer N){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		thisTime = new Date();
		java.util.Calendar Cal=java.util.Calendar.getInstance();   
		Cal.setTime(thisTime);   
		Cal.add(java.util.Calendar.MINUTE,N); 
		//System.out.println("date:"+sdf.format(Cal.getTime()));   
	    return Cal.getTime();
		
	}
	
	
}
