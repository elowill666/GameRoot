package tw.eeit175groupone.finalproject.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import tw.eeit175groupone.finalproject.domain.OrdersBean;

public class DatetimeConverter {
	public static String toString(Date datetime, String format) {
		String result = "";
		try {
			if (datetime != null) {
				result = new SimpleDateFormat(format).format(datetime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date parse(String datetime, String format) {
		Date result = new Date();
		try {
			result = new SimpleDateFormat(format).parse(datetime);
		} catch (Exception e) {
			result = new Date();
			e.printStackTrace();
		}
		return result;
	}

	// 綠界金流需要的時間格式
	public static String convertForECPay(Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 日期拿到年份
	 * @param date
	 * @return Integer--only Year
	 */
	public static Integer getOnlyYear(java.util.Date date) {
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	
		return Integer.parseInt(sdf.format(date));
	}

	public static Integer getOnlyMonth(java.util.Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM");

		return Integer.parseInt(sdf.format(date));
	}

	public static Integer getOnlyDay(java.util.Date date) {

		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());

		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 這裡是字串轉字串
	 * @param requestTime--String
	 * @return String
	 */
	public static String parseToLocalTime(String requestTime){
		Instant instant=Instant.parse(requestTime);
		ZoneId zoneInTaipei=ZoneId.of("Asia/Taipei");
		ZonedDateTime taipeiTime=ZonedDateTime.ofInstant(instant, zoneInTaipei);
		DateTimeFormatter dTF=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return taipeiTime.format(dTF);
	}

	/**
	 * 這裡是java.util.date轉java.util.date
	 * 
	 * @param requestTime--java.util.date
	 * @return java.util.date
	 */
//	public static String parseToLocalTimeForDate(String requestTime){
//		Instant instant=Instant.parse(requestTime);
//		ZoneId zoneInTaipei=ZoneId.of("Asia/Taipei");
//		ZonedDateTime taipeiTime=ZonedDateTime.ofInstant(instant, zoneInTaipei);
//		DateTimeFormatter dTF=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//		return taipeiTime.format(dTF);
//	}
	
	
	public static Integer getDayOfMonth(String YearAndMonth) {
		DateTimeFormatter dtF=DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth parse=YearMonth.parse(YearAndMonth,dtF);
		return parse.lengthOfMonth();
	}

	/**
	 * 將date格式轉成"yyyy/MM/dd"
	 * @param date--Date
	 * @return Date
	 */
	public static Date dateParsetoDate(Date date) {
		Date result=new Date();
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			String formattedDateTime=sdf.format(date);
			result=sdf.parse(formattedDateTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
}
