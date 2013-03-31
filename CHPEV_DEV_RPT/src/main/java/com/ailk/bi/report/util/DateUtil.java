package com.ailk.bi.report.util;

import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.String;
import java.lang.StringBuffer;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Title: 日期处理类
 * </p>
 * <p/>
 * <p>
 * Description: 主要完成获取系统日期、格式转换、时间比较等功能
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p/>
 * <p>
 * Company: ailk
 * </p>
 * 
 * @author 
 * @version 1.0
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class DateUtil {
	public static long HOUR = 0x36ee80L;
	public static long DAY = 0x5265c00L;
	public static String dateFormat = "yyyy.MM.dd HH:mm:ss";

	/**
	 * 构造器
	 */

	public DateUtil() {
	}

	/**
	 * 获取系统日期 格式：2006-01-17或者2006/01/17
	 * 
	 * @param forFormat
	 *            String 日期格式符合（只能是"-","/"）
	 * @return String 系统日期
	 */
	public static String getSystemDate(String forFormat) {
		Calendar cal = Calendar.getInstance();
		String date = null;
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		date = year + forFormat + month + forFormat + day;
		return date;
	}

	/**
	 * 获得系统的当前时间 格式：2006-01-17 12:58:58
	 * 
	 * @return String 当前时间
	 */
	public static String getSystemTime() {
		String curr_time = null;
		GregorianCalendar gc = new GregorianCalendar();
		int getYear = gc.get(Calendar.YEAR);
		int getMonth = gc.get(Calendar.MONTH);
		int getDay = gc.get(Calendar.DAY_OF_MONTH);
		int getHour = gc.get(Calendar.HOUR_OF_DAY);
		int getMinute = gc.get(Calendar.MINUTE);
		int getSecond = gc.get(Calendar.SECOND);
		curr_time = getYear + "-" + (getMonth + 1) + "-" + getDay + " "
				+ getHour + ":" + getMinute + ":" + getSecond;
		return curr_time;
	}

	/**
	 * 获得系统的当前日期 格式：20060117
	 * 
	 * @return String 当前时间
	 */
	public static String getSystemTimeForpc() {
		String curr_time = null;
		GregorianCalendar gc = new GregorianCalendar();
		int getYear = gc.get(Calendar.YEAR);
		int getMonth = gc.get(Calendar.MONTH);
		int getDay = gc.get(Calendar.DAY_OF_MONTH);
		int getHour = gc.get(Calendar.HOUR_OF_DAY);
		int getMinute = gc.get(Calendar.MINUTE);
		int getSecond = gc.get(Calendar.SECOND);
		String day = "" + getDay;
		getMonth = getMonth + 1;
		String mon = "" + getMonth;
		if (getMonth < 10) {
			mon = "0" + getMonth;
		}
		if (getDay < 10) {
			day = "0" + getDay;
		}
		curr_time = getYear + "" + mon + "" + day + "";
		return curr_time;
	}

	/**
	 * 取得格式化后的当前系统时间
	 * 
	 * @param strPattern
	 *            String strPattern 格式化的样式，详细信息参照SimpleDateFormat类的说明
	 * @return String 格式化后的当前系统时间
	 */
	public static String getFormatTime(String strPattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(strPattern);
		return formatter.format(new Date());

	}

	/**
	 * 将指定格式的String的时间对象(YYYY-MM-DD hh:mm)转换为Timestamp对象
	 * 
	 * @param strDate
	 *            String 指定格式的String的时间对象(yyyy-mm-dd)
	 * @return Timestamp 格式化后的Timestamp对象
	 */
	public static Timestamp TimeString2Timestamp(String strDate) {
		return Timestamp.valueOf(strDate + " 00:00:00.000000000");
	}

	/**
	 * 将指定格式的String的时间对象(YYYY-MM-DD HH:mm:ss)转换为Timestamp对象
	 * 
	 * @param strDate
	 *            String 指定格式的String的时间对象(YYYY-MM-DD HH:mm:ss)
	 * @return Timestamp 格式化后的Timestamp对象
	 */
	public static Timestamp TimeString2Timestamp2(String strDate) {
		return Timestamp.valueOf(strDate + ".000000000");
	}

	/**
	 * 将Timestamp转换为指定格式的String对象
	 * 
	 * @param ts
	 *            Timestamp 预转换的Timestamp对象
	 * @param strPattern
	 *            String 格式化的样式，详细信息参照SimpleDateFormat类的说明
	 * @return String 格式化后的String对象
	 */
	public static String Timestamp2String(Timestamp ts, String strPattern) {
		if (ts == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(strPattern);
		return formatter.format(ts);
	}

	/**
	 * 计算两个时间的间隔
	 * 
	 * @param forBeginDate
	 *            String 开始日期
	 * @param forEndDate
	 *            String 结束日期
	 * @return long 时间间隔
	 */
	public static long getInterval(String forBeginDate, String forEndDate) {
		// 参数：开始日期，结束日期
		// 返回：两个日期之间的间隔天数
		long i = Timestamp.valueOf(forBeginDate + " 00:00:00").getTime();
		long j = Timestamp.valueOf(forEndDate + " 00:00:00").getTime();
		// 用两天相差的秒数除以每天的毫秒数86400000，可得到间隔天数
		return (j - i) / 86400000;
	}

	/**
	 * 取得年
	 * 
	 * @param forDate
	 *            String 日期,格式如：2006-01-18或者2006/01/18
	 * @return int 年
	 */
	public static int getYear(String forDate) {
		return Integer.parseInt(forDate.substring(0, 4));
	}

	/**
	 * 取得月
	 * 
	 * @param forDate
	 *            String 日期,格式如：2006-01-18或者2006/01/18
	 * @return int 月份
	 */
	public static int getMonth(String forDate) {
		return Integer.parseInt(forDate.substring(5, 7));
	}

	/**
	 * 取得天
	 * 
	 * @param forDate
	 *            String 日期,格式如：2006-01-18或者2006/01/18
	 * @return int 天
	 */
	public static int getDay(String forDate) {
		return Integer.parseInt(forDate.substring(8, 10));
	}

	/**
	 * 日期比较大小(到天)
	 * 
	 * @param forStrDate1
	 *            String 第一个参数日期
	 * @param forStrDate2
	 *            String 第二个参数日期
	 * @return int 0 第一个参数日期较大. 1 第一个参数日期较小. 2 两个日期相等
	 */
	public static int compDate(String forStrDate1, String forStrDate2) {
		int intYear1 = getYear(forStrDate1);
		int intYear2 = getYear(forStrDate2);
		int intMonth1 = getMonth(forStrDate1);
		int intMonth2 = getMonth(forStrDate2);
		int intDay1 = getDay(forStrDate1);
		int intDay2 = getDay(forStrDate2);
		if (intYear1 > intYear2) {
			return 0;
		} else if (intYear1 < intYear2) {
			return 1;
		} else if (intYear1 == intYear2) {
			if (intMonth1 > intMonth2) {
				return 0;
			} else if (intMonth1 < intMonth2) {
				return 1;
			} else if (intMonth1 == intMonth2) {
				if (intDay1 > intDay2) {
					return 0;
				} else if (intDay1 < intDay2) {
					return 1;
				} else {
					return 2;
				}
			}
		}
		return -1;
	}

	/**
	 * 求@today的第二天
	 * 
	 * @param forToday
	 *            Date 基准日期
	 * @return Date 第二天
	 */
	public static java.util.Date tomorrow(java.util.Date forToday) {
		long start;
		start = forToday.getTime() + DAY;
		return new java.util.Date(start);
	}

	/**
	 * 求前一天的日期
	 * 
	 * @param forToday
	 *            Date 当天
	 * @return Date 前一天
	 */
	public static java.util.Date yesterday(java.util.Date forToday) {
		long start;
		start = forToday.getTime() - DAY;
		return new java.util.Date(start);
	}

	/**
	 * 计算日期偏移
	 * 
	 * @param forDate
	 *            Date 基准日期
	 * @param forDays
	 *            long 偏移天数；（>0 向后偏 ） （<0向前偏）
	 * @return Date 偏移日期
	 */
	public static java.util.Date relativeDate(java.util.Date forDate,
			long forDays) {
		if (forDate == null) {
			return null;
		}
		if (forDays == 0) {
			return forDate;
		}
		java.util.Date dd = (java.util.Date) forDate.clone();
		if (forDays > 0) {
			for (long i = 1; i <= forDays; i++) {
				dd = tomorrow(dd);
			}
			return dd;
		}
		forDays = (-1) * forDays;
		for (long i = 1; i <= forDays; i++) {
			dd = yesterday(dd);
		}
		return dd;
	}

	/**
	 * 检验date参数是否符合YYYY-MM-DD格式
	 * 
	 * @param forDate
	 *            String 日期
	 * @return boolean true/false符合格式/不符合格式
	 */
	public static boolean chechDate(String forDate) {
		if (forDate.length() != 10) {
			return false;
		}
		if (!forDate.substring(4, 5).equals("-")) {
			return false;
		}
		if (!forDate.substring(7, 8).equals("-")) {
			return false;
		}
		return true;
	}

	/**
	 * 日期移动运算函数
	 * 
	 * @param forDate
	 *            String 起始时间
	 * @param forMoveDays
	 *            int 天数
	 * @return String 偏移时间
	 * @throws Exception
	 *             日期处理异常
	 */
	@SuppressWarnings("static-access")
	public static String moveDay(String forDate, int forMoveDays)
			throws Exception {
		if (!chechDate(forDate)) {
			throw new Exception("日期格式不符合YYYY-MM-DD格式");
		}
		Calendar cal = toCalendar(forDate);
		cal.add(Calendar.DATE, forMoveDays);
		DateUtil DateUtil = new DateUtil();
		return DateUtil.dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 月份移动运算函数
	 * 
	 * @param forDate
	 *            String 起始时间
	 * @param forMoveMonths
	 *            int 月数量
	 * @return String 偏移时间
	 * @throws Exception
	 *             日期处理异常
	 */
	@SuppressWarnings("static-access")
	public static String moveMonth(String forDate, int forMoveMonths)
			throws Exception {
		if (!chechDate(forDate)) {
			throw new Exception("日期格式不符合YYYY-MM-DD格式");
		}
		Calendar cal = toCalendar(forDate);
		cal.add(Calendar.MONTH, forMoveMonths);
		DateUtil DateUtil = new DateUtil();
		return DateUtil.dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 年份移动运算函数
	 * 
	 * @param forDate
	 *            String 日期
	 * @param forMoveYears
	 *            int 年数
	 * @return String 偏移日期
	 * @throws Exception
	 *             日期处理异常
	 */
	@SuppressWarnings("static-access")
	public static String moveYear(String forDate, int forMoveYears) {
		forDate = forDate.substring(0, 10);
		if (!chechDate(forDate)) {
			throw new IllegalArgumentException("日期格式不符合YYYY-MM-DD格式");
		}
		Calendar cal = toCalendar(forDate);
		cal.add(Calendar.YEAR, forMoveYears);
		DateUtil DateUtil = new DateUtil();
		return DateUtil.dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 将字符形式的日期转换成为日历
	 * 
	 * @param forDate
	 *            String 日期
	 * @return Calendar 日历
	 */
	public static Calendar toCalendar(String forDate) {
		Calendar cal = Calendar.getInstance();
		int year = getYear(forDate);
		int month = getMonth(forDate);
		int day = getDay(forDate);
		cal.set(year, month - 1, day);
		return cal;
	}

	/**
	 * 将日期转换为字符串"yyyy.MM.dd HH:mm:ss"
	 * 
	 * @param forDate
	 *            Date 日期
	 * @return String
	 */
	public static String dateToString(java.util.Date forDate) {
		if (forDate == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		try {
			return formatter.format(forDate);
		} catch (Exception e) {
			throw new IllegalArgumentException("错误的日期:" + forDate + " " + e);
		}
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            Timestamp 时间
	 * @param format
	 *            String
	 * @return String
	 */

	public static String dateToString(Timestamp date, String format) {
		return dateToString(((Date) (date)), format);
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String
	 * @return String
	 */
	public static String dateToString(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("日期转换成字符串失败:".concat(String
					.valueOf(String.valueOf(e))));
		}
	}

	/**
	 * 计算当前日期是星期几
	 * 
	 * @param forDate
	 *            String 日期
	 * @return int 星期的哪一天
	 */
	public static int getDayOfWeek(String forDate) {
		Calendar cal = toCalendar(forDate);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 输出中文日期：传入2003-05-01返回2003年5月1日
	 * 
	 * @param forDate
	 *            String 英文日期
	 * @return String 中文日期表达
	 */
	public static String dateToChineseExpr(String forDate) {
		if (forDate == null || forDate.length() == 0) {
			return "";
		}
		if (forDate.length() < 10) {
			throw new IllegalArgumentException("日期非法：" + forDate);
		}
		String year = forDate.substring(0, 4);
		int mm, dd;
		try {
			mm = (new Integer("1" + forDate.substring(5, 7))).intValue() - 100;
			dd = (new Integer("1" + forDate.substring(8, 10))).intValue() - 100;
		} catch (Exception e) {
			throw new IllegalArgumentException("[日期非法]" + forDate + ":" + e);
		}
		StringBuffer out = new StringBuffer(year);
		out.append("年").append(mm);
		out.append("月").append(dd);
		out.append("日");
		return out.toString();
	}

	/**
	 * 输出中文日期：传入2/6/2006 0:0:0返回2006年2月6日
	 * 
	 * @param forDate
	 *            String 要转换的日期
	 * @param format
	 *            String 只能是1或者2，1返回2006-2-6,2返回2006年2月6日
	 * @return String 转换后的日期
	 */
	public static String dateToChineseFormat(String forDate, int format) {
		// 保存结果
		String result = "";
		// 如果传入参数为空，则返回空串
		if (forDate == null || forDate.length() == 0) {
			return "&nbsp;";
		}
		// 去掉“ 0:0:0”
		forDate = forDate.substring(0, forDate.length() - 6);
		// 如果日期
		if (forDate.length() < 8 || forDate.length() > 10) {
			throw new IllegalArgumentException("日期非法：" + forDate);
		}
		String year = "";
		String month = "";
		String day = "";
		int i = 0;
		// 第一个“/”字符的位置
		int firstset = 0;
		// 第二个“/”字符的位置
		int towset = 0;
		// 取出"/"的位置
		for (int len = 0; len < forDate.length(); len++) {
			if (forDate.charAt(len) == '/') {
				i = i + 1;
				if (i == 1) {
					firstset = len;
				}
				if (i == 2) {
					towset = len;
				}
			}
		}
		// 获取年
		year = forDate.substring(towset + 1, forDate.length());
		// 获取月
		month = forDate.substring(0, firstset);
		// 获取日
		day = forDate.substring(firstset + 1, towset);
		// 组成新的时间字符串
		if (format == 1) {
			result = year + "-" + month + "-" + day;
		}
		if (format == 2) {
			result = year + "年" + month + "月" + day + "日";
		}
		return result;
	}

	/**
	 * 取当前月份的上月的最后一天
	 * 
	 * @param date
	 *            String 当前日期
	 * @return String 当前日期的上月的最后一天
	 */
	public static String getLastMonthEndDay(String date) {
		String newDate = "";
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 10));
		String newDay = "";
		String newMonth, newYear;
		if (month == 1) {
			year = year - 1;
			month = 12;
		} else {
			month = month - 1;
		}
		switch (month) {
		case 1:
			newDay = "31";
			break;
		case 2:
			if (year % 4 == 0) {
				newDay = "29";
			} else {
				newDay = "28";
			}
			break;
		case 3:
			newDay = "31";
			break;
		case 4:
			newDay = "30";
			break;
		case 5:
			newDay = "31";
			break;
		case 6:
			newDay = "30";
			break;
		case 7:
			newDay = "31";
			break;
		case 8:
			newDay = "30";
			break;
		case 9:
			newDay = "30";
			break;
		case 10:
			newDay = "31";
			break;
		case 11:
			newDay = "30";
			break;
		case 12:
			newDay = "31";
			break;
		default:
			break;
		}
		if (String.valueOf(month).length() == 1) {
			newMonth = "0" + String.valueOf(month);
		} else {
			newMonth = String.valueOf(month);
		}
		newYear = String.valueOf(year);
		newDate = newYear + "-" + newMonth + "-" + newDay;
		return newDate;
	}

	/**
	 * 取当前日期的月份的第一天
	 * 
	 * @param forDate
	 *            String 当前日期
	 * @return String 取当前日期的月份的第一天
	 */
	public static String getFirstDay(String forDate) {
		String year = String.valueOf(getYear(forDate));
		int m = getMonth(forDate);
		String month = m < 10 ? ("0" + m) : ("" + m);
		return year + "-" + month + "-01";
	}

	/**
	 * 取当前月份的最后一天
	 * 
	 * @param forDate
	 *            String 当前日期
	 * @return String 取当前月份的最后一天
	 * @throws Exception
	 */
	public static String getLastDay(String forDate) throws Exception {
		String newDate = getNextDay(forDate);
		newDate = moveDay(newDate, -1);
		return newDate;
	}

	/**
	 * 取当前月份的下月的第一天
	 * 
	 * @param forDate
	 *            String 当前日期
	 * @return String 取当前月份的下月的第一天
	 */
	public static String getNextDay(String forDate) {
		int year = getYear(forDate);
		int month = getMonth(forDate);
		String newDay = "01";
		String newMonth, newYear;
		if (month == 12) {
			year = year + 1;
			month = 1;
		} else {
			month = month + 1;
		}
		if (String.valueOf(month).length() == 1) {
			newMonth = "0" + String.valueOf(month);
		} else {
			newMonth = String.valueOf(month);
		}
		newYear = String.valueOf(year);
		return newYear + "-" + newMonth + "-" + newDay;
	}

	/**
	 * 将日期格式化为相应的时间格式
	 * 
	 * @param forDate
	 *            Date 要转换的日期
	 * @param forType
	 *            int 要转换的类型 0 "yyyy年MM月dd日 HH时mm分ss秒" 1 "yyyy年MM月dd日" 2
	 *            "yyyy-MM-dd HH:mm:ss" 3 "yyyy-MM-dd" 4 "yyyy/MM/dd" 5
	 *            "MM-dd HH:mm"
	 * @return String 转换后的日期
	 */
	public static String formatDate(Date forDate, int forType) {
		SimpleDateFormat formatter = new SimpleDateFormat("");
		String strDate = "";
		switch (forType) {
		case 0:
			formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			break;
		case 1:
			formatter = new SimpleDateFormat("yyyy年MM月dd日");
			break;
		case 2:
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 3:
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 4:
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			break;
		case 5:
			formatter = new SimpleDateFormat("MM-dd HH:mm");
			break;
		default:
			break;
		}
		return formatter.format(forDate);
	}

	/**
	 * 中文格式日期
	 * 
	 * @param d
	 *            Date 日期
	 * @return String 格式化后日期
	 */
	public static String getChineseDate(Date d) {
		if (d == null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",
				new DateFormatSymbols());
		String dtrDate = df.format(d);
		return dtrDate.substring(0, 4) + "\u5E74"
				+ Integer.parseInt(dtrDate.substring(4, 6)) + "\u6708"
				+ Integer.parseInt(dtrDate.substring(6, 8)) + "\u65E5";
	}

	/**
	 * 当前日期
	 * 
	 * @return String
	 */
	public static String getCurrentDate_String() {
		Calendar cal = Calendar.getInstance();
		String currentDate = null;
		String currentYear = (new Integer(cal.get(1))).toString();
		String currentMonth = null;
		String currentDay = null;
		if (cal.get(2) < 9) {
			currentMonth = "0" + (new Integer(cal.get(2) + 1)).toString();
		} else {
			currentMonth = (new Integer(cal.get(2) + 1)).toString();
		}
		if (cal.get(5) < 10) {
			currentDay = "0" + (new Integer(cal.get(5))).toString();
		} else {
			currentDay = (new Integer(cal.get(5))).toString();
		}
		currentDate = currentYear + currentMonth + currentDay;
		return currentDate;
	}

	/**
	 * 取当前日期
	 * 
	 * @param strFormat
	 *            String 日期格式
	 * @return String
	 */
	public static String getCurrentDate_String(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		return getDate(d, strFormat);
	}

	/**
	 * 返回两个年月之间间隔的月数
	 * 
	 * @param dealMonth
	 *            String 大的日期
	 * @param alterMonth
	 *            String 小的日期
	 * @return int 相差月份
	 */
	public static int calBetweenTwoMonth(String dealMonth, String alterMonth) {
		int length = 0;
		if (dealMonth.length() != 6 || alterMonth.length() != 6) {
			length = -1;
		} else {
			int dealInt = Integer.parseInt(dealMonth);
			int alterInt = Integer.parseInt(alterMonth);
			if (dealInt < alterInt) {
				length = -2;
			} else {
				int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
				int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
				int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
				int alterMonthInt = Integer
						.parseInt(alterMonth.substring(4, 6));
				length = (dealYearInt - alterYearInt) * 12
						+ (dealMonthInt - alterMonthInt);
			}
		}
		return length;
	}

	/**
	 * 返回年 格式：YYYY
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int convertDateToYear(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy",
				new DateFormatSymbols());
		return Integer.parseInt(df.format(date));
	}

	/**
	 * 返回年月 格式：YYYYMM
	 * 
	 * @param d
	 *            Date
	 * @return String
	 */
	public static String convertDateToYearMonth(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM",
				new DateFormatSymbols());
		return df.format(d);
	}

	/**
	 * 返回年月日 格式：YYYYMMDD
	 * 
	 * @param d
	 *            Date
	 * @return String
	 */
	public static String convertDateToYearMonthDay(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",
				new DateFormatSymbols());
		return df.format(d);
	}

	/**
	 * 计算日期间隔的天数
	 * 
	 * @param beginDate
	 *            Date 开始日期
	 * @param endDate
	 *            Date 结束日期
	 * @return int 间隔的天数
	 */
	public static int daysBetweenDates(Date beginDate, Date endDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(beginDate);
		caln.setTime(endDate);
		int oday = calo.get(6);
		int nyear = caln.get(1);
		for (int oyear = calo.get(1); nyear > oyear;) {
			calo.set(2, 11);
			calo.set(5, 31);
			days += calo.get(6);
			oyear++;
			calo.set(1, oyear);
		}
		int nday = caln.get(6);
		days = (days + nday) - oday;
		return days;
	}

	/**
	 * 计算间隔天数后的日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param intBetween
	 *            int 间隔天数
	 * @return Date 间隔天数后的日期
	 */
	public static Date getDateBetween(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, intBetween);
		return calo.getTime();
	}

	/**
	 * 计算指定天数后的日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param intBetween
	 *            int 指定天数
	 * @param strFromat
	 *            String 日期格式
	 * @return String 指定天数后的日期
	 */
	public static String getDateBetween_String(Date date, int intBetween,
			String strFromat) {
		Date dateOld = getDateBetween(date, intBetween);
		return getDate(dateOld, strFromat);
	}

	/**
	 * 年月值增1
	 * 
	 * @param yearMonth
	 *            String
	 * @return String
	 */
	public static String increaseYearMonth(String yearMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		if (++month <= 12 && month >= 10) {
			return yearMonth.substring(0, 4) + (new Integer(month)).toString();
		}
		if (month < 10) {
			return yearMonth.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		}
		return (new Integer(year + 1)).toString() + "0"
				+ (new Integer(month - 12)).toString();
	}

	/**
	 * 年月值增加指定的值
	 * 
	 * @param yearMonth
	 *            String 初始年月
	 * @param addMonth
	 *            int 指定的值
	 * @return String
	 */
	public static String increaseYearMonth(String yearMonth, int addMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		month += addMonth;
		year += month / 12;
		month %= 12;
		if (month <= 12 && month >= 10) {
			return year + (new Integer(month)).toString();
		}
		return year + "0" + (new Integer(month)).toString();
	}

	/**
	 * 年月值减1
	 * 
	 * @param yearMonth
	 *            String
	 * @return String
	 */
	public static String descreaseYearMonth(String yearMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		if (--month >= 10) {
			return yearMonth.substring(0, 4) + (new Integer(month)).toString();
		}
		if (month > 0 && month < 10) {
			return yearMonth.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		}

		return (new Integer(year - 1)).toString()
				+ (new Integer(month + 12)).toString();
	}

	/**
	 * 字符转化成时间
	 * 
	 * @param strDate
	 *            String 时间字符串
	 * @param oracleFormat
	 *            String 格式
	 * @return Date
	 */
	public static Date stringToDate(String strDate, String oracleFormat) {
		if (strDate == null) {
			return null;
		}
		Hashtable h = new Hashtable();
		String javaFormat = "";
		String s = oracleFormat.toLowerCase();
		if (s.indexOf("yyyy") != -1) {
			h.put(new Integer(s.indexOf("yyyy")), "yyyy");
		} else if (s.indexOf("yy") != -1) {
			h.put(new Integer(s.indexOf("yy")), "yy");
		}
		if (s.indexOf("mm") != -1) {
			h.put(new Integer(s.indexOf("mm")), "MM");
		}
		if (s.indexOf("dd") != -1) {
			h.put(new Integer(s.indexOf("dd")), "dd");
		}
		if (s.indexOf("hh24") != -1) {
			h.put(new Integer(s.indexOf("hh24")), "HH");
		}
		if (s.indexOf("mi") != -1) {
			h.put(new Integer(s.indexOf("mi")), "mm");
		}
		if (s.indexOf("ss") != -1) {
			h.put(new Integer(s.indexOf("ss")), "ss");
		}
		for (int intStart = 0; s.indexOf("-", intStart) != -1; intStart++) {
			intStart = s.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; s.indexOf("/", intStart) != -1; intStart++) {
			intStart = s.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}
		for (int intStart = 0; s.indexOf(" ", intStart) != -1; intStart++) {
			intStart = s.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}
		for (int intStart = 0; s.indexOf(":", intStart) != -1; intStart++) {
			intStart = s.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}
		if (s.indexOf("\u5E74") != -1) {
			h.put(new Integer(s.indexOf("\u5E74")), "\u5E74");
		}
		if (s.indexOf("\u6708") != -1) {
			h.put(new Integer(s.indexOf("\u6708")), "\u6708");
		}
		if (s.indexOf("\u65E5") != -1) {
			h.put(new Integer(s.indexOf("\u65E5")), "\u65E5");
		}
		if (s.indexOf("\u65F6") != -1) {
			h.put(new Integer(s.indexOf("\u65F6")), "\u65F6");
		}
		if (s.indexOf("\u5206") != -1) {
			h.put(new Integer(s.indexOf("\u5206")), "\u5206");
		}
		if (s.indexOf("\u79D2") != -1) {
			h.put(new Integer(s.indexOf("\u79D2")), "\u79D2");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat);
		Date myDate = null;
		try {
			myDate = df.parse(strDate);
		} catch (Exception e) {
			return null;
		}
		return myDate;
	}

	/**
	 * 日期转化成字符串
	 * 
	 * @param d
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String
	 */
	public static String dateToString1(Date d, String format) {
		if (d == null) {
			return "";
		}
		Hashtable h = new Hashtable();
		String javaFormat = "";
		String s = format.toLowerCase();
		if (s.indexOf("yyyy") != -1) {
			h.put(new Integer(s.indexOf("yyyy")), "yyyy");
		} else if (s.indexOf("yy") != -1) {
			h.put(new Integer(s.indexOf("yy")), "yy");
		}
		if (s.indexOf("mm") != -1) {
			h.put(new Integer(s.indexOf("mm")), "MM");
		}
		if (s.indexOf("dd") != -1) {
			h.put(new Integer(s.indexOf("dd")), "dd");
		}
		if (s.indexOf("hh24") != -1) {
			h.put(new Integer(s.indexOf("hh24")), "HH");
		}
		if (s.indexOf("mi") != -1) {
			h.put(new Integer(s.indexOf("mi")), "mm");
		}
		if (s.indexOf("ss") != -1) {
			h.put(new Integer(s.indexOf("ss")), "ss");
		}
		for (int intStart = 0; s.indexOf("-", intStart) != -1; intStart++) {
			intStart = s.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; s.indexOf("/", intStart) != -1; intStart++) {
			intStart = s.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}
		for (int intStart = 0; s.indexOf(" ", intStart) != -1; intStart++) {
			intStart = s.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}
		for (int intStart = 0; s.indexOf(":", intStart) != -1; intStart++) {
			intStart = s.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}
		if (s.indexOf("\u5E74") != -1) {
			h.put(new Integer(s.indexOf("\u5E74")), "\u5E74");
		}
		if (s.indexOf("\u6708") != -1) {
			h.put(new Integer(s.indexOf("\u6708")), "\u6708");
		}
		if (s.indexOf("\u65E5") != -1) {
			h.put(new Integer(s.indexOf("\u65E5")), "\u65E5");
		}
		if (s.indexOf("\u65F6") != -1) {
			h.put(new Integer(s.indexOf("\u65F6")), "\u65F6");
		}
		if (s.indexOf("\u5206") != -1) {
			h.put(new Integer(s.indexOf("\u5206")), "\u5206");
		}
		if (s.indexOf("\u79D2") != -1) {
			h.put(new Integer(s.indexOf("\u79D2")), "\u79D2");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat,
				new DateFormatSymbols());
		return df.format(d);
	}

	/**
	 * 格式化日期
	 * 
	 * @param d
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
	public static String getDate(Date d, String format) {
		if (d == null) {
			return "";
		}
		Hashtable h = new Hashtable();
		String javaFormat = "";
		String s = format.toLowerCase();
		if (s.indexOf("yyyy") != -1) {
			h.put(new Integer(s.indexOf("yyyy")), "yyyy");
		} else if (s.indexOf("yy") != -1) {
			h.put(new Integer(s.indexOf("yy")), "yy");
		}
		if (s.indexOf("mm") != -1) {
			h.put(new Integer(s.indexOf("mm")), "MM");
		}
		if (s.indexOf("dd") != -1) {
			h.put(new Integer(s.indexOf("dd")), "dd");
		}
		if (s.indexOf("hh24") != -1) {
			h.put(new Integer(s.indexOf("hh24")), "HH");
		}
		if (s.indexOf("mi") != -1) {
			h.put(new Integer(s.indexOf("mi")), "mm");
		}
		if (s.indexOf("ss") != -1) {
			h.put(new Integer(s.indexOf("ss")), "ss");
		}
		for (int intStart = 0; s.indexOf("-", intStart) != -1; intStart++) {
			intStart = s.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; s.indexOf("/", intStart) != -1; intStart++) {
			intStart = s.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}
		for (int intStart = 0; s.indexOf(" ", intStart) != -1; intStart++) {
			intStart = s.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}
		for (int intStart = 0; s.indexOf(":", intStart) != -1; intStart++) {
			intStart = s.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}
		if (s.indexOf("\u5E74") != -1) {
			h.put(new Integer(s.indexOf("\u5E74")), "\u5E74");
		}
		if (s.indexOf("\u6708") != -1) {
			h.put(new Integer(s.indexOf("\u6708")), "\u6708");
		}
		if (s.indexOf("\u65E5") != -1) {
			h.put(new Integer(s.indexOf("\u65E5")), "\u65E5");
		}
		if (s.indexOf("\u65F6") != -1) {
			h.put(new Integer(s.indexOf("\u65F6")), "\u65F6");
		}
		if (s.indexOf("\u5206") != -1) {
			h.put(new Integer(s.indexOf("\u5206")), "\u5206");
		}
		if (s.indexOf("\u79D2") != -1) {
			h.put(new Integer(s.indexOf("\u79D2")), "\u79D2");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat,
				new DateFormatSymbols());
		return df.format(d);
	}

	/**
	 * 比较日期1是否大于等于日期2
	 * 
	 * @param s1
	 *            日期1
	 * @param s2
	 *            日期2
	 * @return
	 */
	public static boolean yearMonthGreatEqual(String s1, String s2) {
		String temp1 = s1.substring(0, 4);
		String temp2 = s2.substring(0, 4);
		String temp3 = s1.substring(4, 6);
		String temp4 = s2.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2)) {
			return true;
		}
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2)) {
			return Integer.parseInt(temp3) >= Integer.parseInt(temp4);
		}
		return false;
	}

	/**
	 * 比较日期1是否大于日期2
	 * 
	 * @param s1
	 *            日期1
	 * @param s2
	 *            日期2
	 * @return
	 */
	public static boolean yearMonthGreater(String s1, String s2) {
		String temp1 = s1.substring(0, 4);
		String temp2 = s2.substring(0, 4);
		String temp3 = s1.substring(4, 6);
		String temp4 = s2.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2)) {
			return true;
		}
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2)) {
			return Integer.parseInt(temp3) > Integer.parseInt(temp4);
		}
		return false;
	}

	/**
	 * 获得oracle格式的日期字符串
	 * 
	 * @param d
	 *            日期
	 * @return oracle格式的日期字符串
	 */
	public static String getOracleFormatDateStr(Date d) {
		return getDate(d, "YYYY-MM-DD HH24:MI:SS");
	}

	/**
	 * 增加月
	 * 
	 * @param strDate
	 *            初始年月
	 * @param intDiff
	 *            增加的数量
	 * @return
	 */
	public static String getMonthBetween(String strDate, int intDiff) {
		try {
			int intYear = Integer.parseInt(strDate.substring(0, 4));
			int intMonth = Integer.parseInt(strDate.substring(4, 6));
			String strDay = "";
			if (strDate.length() > 6) {
				strDay = strDate.substring(6, strDate.length());
			}
			for (intMonth += intDiff; intMonth <= 0; intMonth += 12) {
				intYear--;
			}
			for (; intMonth > 12; intMonth -= 12) {
				intYear++;
			}
			if (intMonth < 10) {
				return Integer.toString(intYear) + "0"
						+ Integer.toString(intMonth) + strDay;
			}
			return Integer.toString(intYear) + Integer.toString(intMonth)
					+ strDay;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 计算两个年月之间的相差月数
	 * 
	 * @param strDateBegin
	 * @param strDateEnd
	 * @return
	 */
	public static String getMonthBetween(String strDateBegin, String strDateEnd) {
		try {
			String strOut;
			if (strDateBegin.equals("") || strDateEnd.equals("")
					|| strDateBegin.length() != 6 || strDateEnd.length() != 6) {
				strOut = "";
			} else {
				int intMonthBegin = Integer.parseInt(strDateBegin.substring(0,
						4))
						* 12
						+ Integer.parseInt(strDateBegin.substring(4, 6));
				int intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4))
						* 12 + Integer.parseInt(strDateEnd.substring(4, 6));
				strOut = String.valueOf(intMonthBegin - intMonthEnd);
			}
			return strOut;
		} catch (Exception e) {
			return "0";
		}
	}

	/*
	 * 将“yyyymmdd”格式的日期转化成“yyyy-mm-dd”格式
	 * 
	 * @param strDate
	 * 
	 * @return
	 */

	public static String getStrHaveAcross(String strDate) {
		try {
			return strDate.substring(0, 4) + "-" + strDate.substring(4, 6)
					+ "-" + strDate.substring(6, 8);
		} catch (Exception e) {
			return strDate;
		}
	}

	/**
	 * 计算当前日期的下个月的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfNextMonth() {
		String strToday = getCurrentDate_String();
		return increaseYearMonth(strToday.substring(0, 6)) + "01";
	}

}
