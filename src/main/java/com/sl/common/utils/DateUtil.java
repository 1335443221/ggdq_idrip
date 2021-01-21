package com.sl.common.utils;


import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service

public class DateUtil {
	// ==格式到年==
	/**
	 * 日期格式，年份，例如：2004   2008年
	 */
	public static  String DATE_FORMAT_YYYY = "yyyy";
	public static  String DATE_FORMAT_YYYY年 = "yyyy年";

	// ==格式到年月 ==
	/**
	 * 日期格式，年份和月份，例如：200707  2020-10   2020-10月
	 */
	public   String DATE_FORMAT_YYYYMM = "yyyyMM";
	public static  String DATE_FORMAT_YYYY_MM = "yyyy-MM";
	public static  String DATE_FORMAT_YYYY_MM月 = "yyyy-MM月";


	// ==格式到年月日==
	/**
	 * 日期格式，年月日，例如：050630，080808
	 */
	public   String DATE_FORMAT_YYMMDD = "yyMMdd";

	/**
	 * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
	 */
	public   String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

	/**
	 * 日期格式，年月日，例如：20050630，20080808
	 */
	public   String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	/**
	 * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
	 */
	public static  String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public   String DATE_FORMAT_00 = " 00:00:00";
	public   String DATE_FORMAT_DDHHMISS = "HH:mm:ss";

	/**
	 * 日期格式，年月日，例如：2016.10.05
	 */
	public  String DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd";

	/**
	 * 日期格式，年月日，例如：2016年10月05日
	 */
	public  String DATE_TIME_FORMAT_YYYY年MM月DD日 = "yyyy年MM月dd日";


	// ==格式到年月日 时分 ==

	/**
	 * 日期格式，年月日时分，例如：200506301210，200808081210
	 */
	public  String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

	/**
	 * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
	 */
	public  String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

	/**
	 * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
	 */
	public  String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";


	// ==格式到年月日 时分秒==
	/**
	 * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
	 */
	public  String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

	/**
	 * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
	 * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
	 */
	public  static String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";



	// ==格式到年月日 时分秒 毫秒==
	/**
	 * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
	 */
	public  String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";

	/**
	 * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
	 * 例如：2005-05-10 23：20：00 :000，2008-08-08 20:08:08:000
	 */
	public  String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS = "dd-MM-yyyy HH:mm:ss:SSS";


	// ==特殊格式==
	/**
	 * 日期格式，月日时分，例如：10-05 12:00
	 */
	public  String DATE_FORMAT_MM_DDHHMI = "MM-dd HH:mm";

	// ==格式到年月日 时分秒==
	/**
	 * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
	 */
	public  String DATE_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";


	/* ************工具方法***************   */

	/**
	 * 获取某日期的年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 获取某日期的月份
	 * @param date
	 * @return
	 */
	public int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取某日期的日数
	 * @param date
	 * @return
	 */
	public int getDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}

	/**
	 * 获取某日期的小时数
	 * @param date
	 * @return
	 */
	public int getHour(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	//**************格式化********************//

	/**
	 * 格式化Date时间
	 * @param time Date类型时间
	 * @param timeFromat String类型格式
	 * @return 格式化后的字符串
	 */
	public static String parseDateToStr(Date time, String timeFromat){
		DateFormat dateFormat=new SimpleDateFormat(timeFromat);
		return dateFormat.format(time);
	}

	/**
	 * 格式化Timestamp时间
	 * @param timestamp Timestamp类型时间
	 * @param timeFromat
	 * @return 格式化后的字符串
	 */
	public  String parseTimestampToStr(long timestamp,String timeFromat){
		SimpleDateFormat df = new SimpleDateFormat(timeFromat);
		return df.format(timestamp);
	}


	/**
	 * 格式化String时间
	 * @param time String类型时间
	 * @param timeFromat String类型格式
	 * @return 格式化后的Date日期
	 */
	public static Date parseStrToDate(String time, String timeFromat) {
		if (time == null || time.equals("")) {
			return null;
		}
		Date date=null;
		try{
			DateFormat dateFormat=new SimpleDateFormat(timeFromat);
			date=dateFormat.parse(time);
		}catch(Exception e){
		}
		return date;
	}

	/**
	 * 格式化String to String
	 * @param time
	 * @param timeFromat
	 * @return
	 */
	public static String parseStrToStr(String time, String timeFromat) {
		return parseDateToStr(parseStrToDate(time,timeFromat),timeFromat);
	}



	//==========解析两个日期之前 所有 年/月/日======================================
	/**
	 * 解析两个日期之间的所有月份
	 * @param beginDateStr 开始日期，至少精确到yyyy-MM
	 * @param endDateStr 结束日期，至少精确到yyyy-MM
	 * @param timeFromat 指定要解析的时间格式
	 * @return yyyy-MM日期集合
	 */
	public static List<String> getMonthListOfDate(String beginDateStr, String endDateStr,String timeFromat) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		// 返回的月份列表
		String sRet = "";
		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;
		GregorianCalendar beginGC = null;
		GregorianCalendar endGC = null;
		List<String> list = new ArrayList<String>();
		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = new GregorianCalendar();
			beginGC.setTime(beginDate);

			endGC = new GregorianCalendar();
			endGC.setTime(endDate);

			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
				sRet = beginGC.get(Calendar.YEAR) + "-"
						+ (beginGC.get(Calendar.MONTH) + 1);
				Date sRetDate=parseStrToDate(sRet,"yyyy-MM");
				sRet=parseDateToStr(sRetDate,timeFromat);
				list.add(sRet);
				// 以月为单位，增加时间
				beginGC.add(Calendar.MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 解析两个日期段之间的所有年份
	 * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
	 * @param endDateStr 结束日期  ，至少精确到yyyy-MM-dd
	 * @return yyyy-MM-dd日期集合
	 */
	public static List<String> getYearListOfDate(String beginDateStr, String endDateStr,String timeFromat) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy");

		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;

		Calendar beginGC = null;
		Calendar endGC = null;
		List<String> list = new ArrayList<String>();

		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = Calendar.getInstance();
			beginGC.setTime(beginDate);

			endGC = Calendar.getInstance();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);

			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

				list.add(sdf.format(beginGC.getTime()));
				// 以日为单位，增加时间
				beginGC.add(Calendar.YEAR, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 解析两个日期段之间的所有日期 相隔多久
	 * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
	 * @param endDateStr 结束日期  ，至少精确到yyyy-MM-dd
	 * @return yyyy-MM-dd日期集合
	 */
	public  static List<String> getDayListOfDate(String beginDateStr, String endDateStr,int year,int month,int day,int hour,int minute,int second,int millisecond,String timeFromat) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;

		Calendar beginGC = null;
		Calendar endGC = null;
		List<String> list = new ArrayList<String>();

		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = Calendar.getInstance();
			beginGC.setTime(beginDate);

			endGC = Calendar.getInstance();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);

			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

				list.add(sdf.format(beginGC.getTime()));
				beginGC.add(Calendar.YEAR, year);//加减年数
				beginGC.add(Calendar.MONTH, month);//加减月数
				beginGC.add(Calendar.DATE, day);//加减天数
				beginGC.add(Calendar.HOUR,hour);//加减小时数
				beginGC.add(Calendar.MINUTE, minute);//加减分钟数
				beginGC.add(Calendar.SECOND, second);//加减秒
				beginGC.add(Calendar.MILLISECOND, millisecond);//加减毫秒数
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	//**************用的比较多的方法********************//

	/**
	 * 获得指定时间加减参数后的日期(不计算则输入0)
	 * @param date 指定日期
	 * @param year 年数，可正可负
	 * @param month 月数，可正可负
	 * @param day 天数，可正可负
	 * @param hour 小时数，可正可负
	 * @param minute 分钟数，可正可负
	 * @param second 秒数，可正可负
	 * @param millisecond 毫秒数，可正可负
	 * @return 计算后的日期
	 */
	public static Date addDate(Date date,int year,int month,int day,int hour,int minute,int second,int millisecond){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);//加减年数
		c.add(Calendar.MONTH, month);//加减月数
		c.add(Calendar.DATE, day);//加减天数
		c.add(Calendar.HOUR,hour);//加减小时数
		c.add(Calendar.MINUTE, minute);//加减分钟数
		c.add(Calendar.SECOND, second);//加减秒
		c.add(Calendar.MILLISECOND, millisecond);//加减毫秒数
		return c.getTime();
	}



	/**
	 * 获取某月的天数
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}


	/**
	 * 获取当下年份指定前后数量的年份集合
	 * @param before 当下年份前年数
	 * @param behind 当下年份后年数
	 * @return 集合
	 */
	public  List<Integer> getYearListOfYears(int before,int behind) {
		if (before<0 || behind<0) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		Calendar c = null;
		c = Calendar.getInstance();
		c.setTime(new Date());
		int currYear = Calendar.getInstance().get(Calendar.YEAR);

		int startYear = currYear - before;
		int endYear = currYear + behind;
		for (int i = startYear; i < endYear; i++) {
			list.add(Integer.valueOf(i));
		}
		return list;
	}

	/**
	 * 获取当前日期是一年中第几周
	 * @param date
	 * @return
	 */
	public  Integer getWeekthOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}


	/**
	 * 获取某一年各星期的始终时间
	 * 实例：getWeekList(2016)，第52周(从2016-12-26至2017-01-01)
	 * @param
	 * @return
	 */
	public  List<HashMap<String,String>> getWeekTimeOfYear(int year) {
		List<HashMap<String,String>> list = new ArrayList<>();
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		int count = getWeekthOfYear(c.getTime());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dayOfWeekStart = "";
		String dayOfWeekEnd = "";
		for (int i = 1; i <= count; i++) {
			HashMap<String,String> map=new LinkedHashMap<>();
			dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
			dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
			map.put("begin_time",dayOfWeekStart);
			map.put("end_time",dayOfWeekEnd);
			list.add(map);
		}
		return list;

	}

	/**
	 * 获取某一年的总周数
	 * @param year
	 * @return
	 */
	public  Integer getWeekCountOfYear(int year){
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		int count = getWeekthOfYear(c.getTime());
		return count;
	}

	/**
	 * 获取指定日期所在周的第一天
	 * @param date
	 * @return
	 */
	public  Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 获取指定日期所在周的最后一天
	 * @param date
	 * @return
	 */
	public  Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 获取某年某周的第一天
	 * @param year 目标年份
	 * @param week 目标周数
	 * @return
	 */
	public  Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 获取某年某周的最后一天
	 * @param year 目标年份
	 * @param week 目标周数
	 * @return
	 */
	public  Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 获取某年某月的第一天
	 * @param year 目标年份
	 * @param month 目标月份
	 * @return
	 */
	public  String getFirstDayOfMonth(int year,int month){
		month = month-1;
		Calendar   c   =   Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);

		int day = c.getActualMinimum(c.DAY_OF_MONTH);

		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	/**
	 * 获取某年某月的最后一天
	 * @param year 目标年份
	 * @param month 目标月份
	 * @return
	 */
	public  String getLastDayOfMonth(int year,int month){
		month = month-1;
		Calendar   c   =   Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		int day = c.getActualMaximum(c.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}


	/**
	 * 获取某个日期为星期几
	 * @param date
	 * @return String "星期*"
	 */
	public  String getDayWeekOfDate1(Date date) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获得指定日期的星期几数
	 * @param date
	 * @return int
	 */
	public  int getDayWeekOfDate2(Date date){
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		return aCalendar.get(Calendar.DAY_OF_WEEK);
	}


	/**
	 * 将指定日期的时分秒格式为零
	 * @param date
	 * @return
	 */
	public  Date formatHhMmSsOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}



	/**
	 * 获得两个日期的时间戳之差
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public  Long getDistanceTimestamp(Date startDate,Date endDate){
		long daysBetween=(endDate.getTime()-startDate.getTime()+1000000)/(3600*24*1000);
		return daysBetween;
	}


	public  long[] getDistanceTime(String str1, String str2) {
		DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff ;
			if(time1<time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = {day, hour, min, sec};
		return times;
	}

	/**
	 * 两个时间之间相差距离多少天
	 * @param one 时间参数 1：
	 * @param two 时间参数 2：
	 * @return 相差天数
	 */

	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 * @param date
	 * @return
	 */
	public  Date getDayEndTime(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	/**
	 * 获取指定格式时间字符串
	 * @param
	 * @return
	 */
	public static String getFormateDateStr(Calendar cla,String formate) {
		SimpleDateFormat sj = new SimpleDateFormat(formate);
		return  sj.format(cla.getTime());
	}


	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime,String timeFromat) {
		/*Date nowTime=null;
		Date startTime=null;
		Date endTime=null;
		try{
			DateFormat dateFormat=new SimpleDateFormat(timeFromat);
			nowTime=dateFormat.parse(nowDay);
			startTime=dateFormat.parse(startDay);
			endTime=dateFormat.parse(endDay);
		}catch(Exception e){

		}*/

		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取过去N天之前(之后)的时间
	 * @param time String类型时间
	 * @param timeFormat  String类型格式
	 * @return 格式化后的Date日期
	 */
	public  String getSomeDayOfTime(String time, String timeFormat,int n) {
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat(timeFormat).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获取n天之前(之后)
		c.setTime(date);
		c.add(Calendar.DATE,n);
		String day=new SimpleDateFormat(timeFormat).format(c.getTime());
		return day;
	}

	/**
	 * 获取前后N月的第一天
	 * @param
	 * @param timeFormat  String类型格式
	 * @return 格式化后的Date日期
	 */
	public  String getMonthFirstDay(String timeFormat,int n) {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		Calendar cal_1=Calendar.getInstance();//获取当前日期
		cal_1.add(Calendar.MONTH, n);
		cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		String firstDay = format.format(cal_1.getTime());
		return firstDay;
	}


	/**
	 * 获取前月的最后一天
	 * @param timeFormat  String类型格式
	 * @return 格式化后的Date日期
	 */
	public  String getLastMonthLastDay(String timeFormat,int n) {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取当前月的最后一天
	 * @return 格式化后的Date日期
	 */
	public  String getNewMonthLastDay(String timeFormat) {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		return last;
	}

	//获取两个日期之间的日期集合
	public  List<String> getDays(String startTime, String endTime) {
		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}


	/**
	 * 解析两个日期之间的所有月份
	 * @param beginDateStr 开始日期，至少精确到yyyy-MM
	 * @param endDateStr 结束日期，至少精确到yyyy-MM
	 * @return yyyy-MM日期集合
	 */
	public static List<String> getMonthList(String beginDateStr, String endDateStr) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		// 返回的月份列表
		String sRet = "";

		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;

		GregorianCalendar beginGC = null;
		GregorianCalendar endGC = null;
		List<String> list = new ArrayList<String>();

		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = new GregorianCalendar();
			beginGC.setTime(beginDate);

			endGC = new GregorianCalendar();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
				list.add(sdf.format(beginGC.getTime()));
				// 以月为单位，增加时间
				beginGC.add(Calendar.MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析两个日期段之间的所有日期
	 * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
	 * @param endDateStr 结束日期  ，至少精确到yyyy-MM-dd
	 * @return yyyy-MM-dd日期集合
	 */
	public static List<String> getDayList(String beginDateStr, String endDateStr) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;

		Calendar beginGC = null;
		Calendar endGC = null;
		List<String> list = new ArrayList<String>();

		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = Calendar.getInstance();
			beginGC.setTime(beginDate);

			endGC = Calendar.getInstance();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

				list.add(sdf.format(beginGC.getTime()));
				// 以日为单位，增加时间
				beginGC.add(Calendar.DAY_OF_MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	    * 判断时间是不是今天
	    * @param date
	    * @return 是返回true，不是返回false
	    */
	public static boolean isNow(String day) {
		//当前时间
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		//获取今天的日期
		String nowDay = sf.format(now);
		return day.equals(nowDay);
	}

	/**
	 * 给时间加上几个小时
	 * @param day 当前时间 格式：yyyy-MM-dd HH:mm:ss
	 * @param hour 需要加的时间
	 * @return
	 */
	public static String addDateMinut(String day, int hour){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}


	/**
	 * 获取一段时间内的所有周
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	public static Map<String,String> getAllWeeks(String dateStart, String dateEnd) {
		Map<String,String> natureWeekMap = new LinkedHashMap<>();
		DateUtil dateUtil=new DateUtil();
		Date startDate= DateUtil.parseStrToDate(dateStart,"yyyy-MM-dd");
		Date endDate= DateUtil.parseStrToDate(dateEnd,"yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		int week = cal.get(Calendar.DAY_OF_WEEK)-1;
		if (week!=1){
			startDate= DateUtil.addDate(startDate,0,0,1-week,0,0,0,0);
		}
		cal.setTime(endDate);
		week = cal.get(Calendar.DAY_OF_WEEK);
		if (week!=7){
			endDate= DateUtil.addDate(endDate,0,0,7-week,0,0,0,0);
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(startDate);
			int count=1;
			for (; ; ) {
				int dayOfWeeek = new Integer(calendar.get(Calendar.DAY_OF_WEEK));
				//周一(周日-周一...周五-周六依次用1-7表示)
				if (dayOfWeeek == 2) {  //证明是周一
					String dateNum = dateFormat.format(calendar.getTime());
					Date numDate= DateUtil.parseStrToDate(dateNum,"yyyy-MM-dd");

					if (numDate.compareTo(endDate)<=0) {

						String day = dateFormat2.format(calendar.getTime());
						String natureWeek = day;
						calendar.add(Calendar.DATE, 6);
						day = dateFormat2.format(calendar.getTime());
						natureWeek=natureWeek+"-"+day;
						calendar.add(Calendar.DATE, 1);

						int weekTh=dateUtil.getWeekthOfYear(numDate)+1;
						int yserTh=dateUtil.getYear(numDate);

						natureWeekMap.put(yserTh+""+weekTh,natureWeek);
						count++;
					} else {
						break;
					}
				} else {
					calendar.add(Calendar.DATE, 1);
				}
			}
		} catch (Exception e) {
			System.out.print("获取时间段内的自然周异常,"+e.toString());
		}
		return natureWeekMap;
	}

	/**
	 * 获取二十四小时之前的时间
	 * @param time String类型时间
	 * @param timeFromat  String类型格式
	 * @return 格式化后的Date日期
	 */
	public  String getBeforeDayOfTime(String time, String timeFromat) {
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat(timeFromat).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-1);
		String dayAfter=new SimpleDateFormat(timeFromat).format(c.getTime());
		return dayAfter;
	}

	/**
	 * 获取两个日期直接差的天数
	 * @return
	 */
	public static long getDayOfTwoDate(Date startDate, Date endDate){
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		return (endTime - startTime)/86400000;
	}

	/**
	 * 获取当天零点零分零秒的日期字符串
	 * @return
	 */
	public String getTodayZeroStr(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		return parseDateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当天时间字符串
	 * @return
	 */
	public String getTodayStr(){
		return parseDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 时间戳转成时间字符串
	 * 例如： "2019-10-09 13:38:16" 转为 1570599496000
	 *
	 * @param dateStr "2019-10-09 13:38:16"
	 * @param format 传空默认："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public long str2TimeStamp(String dateStr, String format) {
		if(dateStr == null || dateStr.length() == 0) {
			return 0L;
		}

		if(format == null || format.length() == 0) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date praseDate = dateFormat.parse(dateStr);
			long timeStamp = praseDate.getTime();
			return timeStamp;
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return 0L;
	}


	/**
	 * 时间字符串转时间
	 *
	 * @param dateStr "2019-10-09 13:38:16"
	 * @param format 传空默认："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public Date str2Date(String dateStr, String format) {
		if(dateStr == null || dateStr.length() == 0) {
			return new Date();
		}

		if(format == null || format.length() == 0) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return new Date();
	}

	/**
	 * 获取当年年份
	 * @return
	 */
	public Integer getTodayYear(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 获取昨天年月日
	 * @return String
	 * */
	public String getYestoday(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}

	public static void main(String[] args) {
		DateUtil dateUtil=new DateUtil();
		System.out.println(getAllWeeks("2020-08-10","2020-09-10"));

		//	System.out.println(getNatureWeeks("2020-08-04","2020-09-04"));
	}
}
