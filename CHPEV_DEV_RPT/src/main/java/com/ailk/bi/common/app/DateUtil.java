package com.ailk.bi.common.app;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 系统使用时间转化的类型
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "unused" })
public class DateUtil {

	// /////////////////////////////////////////////////
	/**
	 * 返回两个日期相差的天数，如果任何一个为空，返回为空
	 *
	 * @param date1
	 *            指定的被比较日期
	 * @param date2
	 *            指定的比较日期
	 * @return 返回的一个正的数值字符串
	 */
	public static String getDateSpans(Date date1, Date date2) {
		String diff = null;
		if (null != date1 && null != date2) {
			Calendar cal1 = Calendar.getInstance(Locale.CHINA);
			Calendar cal2 = Calendar.getInstance(Locale.CHINA);
			cal1.setTime(date1);
			cal2.setTime(date2);
			Calendar tmpCal1 = Calendar.getInstance(Locale.CHINA);
			tmpCal1.clear();
			tmpCal1.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
			tmpCal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
			tmpCal1.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
			Calendar tmpCal2 = Calendar.getInstance(Locale.CHINA);
			tmpCal2.clear();
			tmpCal2.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
			tmpCal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
			tmpCal2.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));
			diff = ""
					+ Math.abs(tmpCal1.getTimeInMillis()
							- tmpCal2.getTimeInMillis())
					/ (1000 * 60 * 60 * 24);
		}
		return diff;
	}

	/**
	 * 返回两个日期相差的天数，如果任何一个为空，返回为空
	 *
	 * @param dateStr1
	 *            指定的被比较日期
	 * @param dateStr2
	 *            指定的比较日期
	 * @param dateFmt
	 *            日期分析格式，默认为 yyyyMMdd
	 * @return 返回的一个正的数值字符串
	 */
	public static String getDateSpans(String dateStr1, String dateStr2,
			String dateFmt) {
		String diff = null;
		String tmpFmt = dateFmt;
		if (null == tmpFmt)
			tmpFmt = AppConst.DATEFMT;
		if (null != dateStr1 && null != dateStr2
				&& dateStr1.length() >= tmpFmt.length()
				&& dateStr1.length() >= tmpFmt.length()) {
			try {

				String tmpStr1 = dateStr1.substring(0, tmpFmt.length());
				String tmpStr2 = dateStr2.substring(0, tmpFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
				Date date1 = sdf.parse(tmpStr1);
				Date date2 = sdf.parse(tmpStr2);
				diff = DateUtil.getDateSpans(date1, date2);
			} catch (ParseException pe) {
				diff = null;
			}
		}
		return diff;
	}

	/**
	 * 返回两个日期相差的天数，如果任何一个为空，返回为空
	 *
	 * @param dateStr1
	 *            指定的被比较日期
	 * @param date2
	 *            指定的比较日期
	 * @param dateFmt
	 *            日期分析格式，默认为 yyyyMMdd
	 * @return 返回的一个正的数值字符串
	 */
	public static String getDateSpans(String dateStr1, Date date2,
			String dateFmt) {
		String diff = null;
		String tmpFmt = dateFmt;
		if (null == tmpFmt)
			tmpFmt = AppConst.DATEFMT;
		if (null != dateStr1 && null != date2
				&& dateStr1.length() >= tmpFmt.length()) {
			try {
				String tmpStr = dateStr1.substring(0, tmpFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
				Date date1 = sdf.parse(tmpStr);
				diff = DateUtil.getDateSpans(date1, date2);
			} catch (ParseException pe) {
				diff = null;
			}
		}
		return diff;
	}

	// ///////////////////////////////////////////////////////////////////
	/**
	 * 判断指定日期是否为闰年
	 *
	 * @param date
	 *            指定的日期，null时返回 false
	 * @return 是否为闰年
	 */
	public static boolean isLeap(Date date) {
		boolean isLeap = false;
		if (null != date) {
			Calendar cal = Calendar.getInstance(Locale.CHINA);
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
				isLeap = true;
		}
		return isLeap;
	}

	/**
	 * 判断指定的日期是否是闰年
	 *
	 * @param dateStr
	 *            指定的日期字符串，如果为 null,返回 false
	 * @param dateFmt
	 *            日期分析格式，默认 yyyy
	 * @return 是否为闰年
	 */
	public static boolean isLeap(String dateStr, String dateFmt) {
		boolean isLeap = false;
		String tmpFmt = dateFmt;
		if (null == tmpFmt)
			tmpFmt = AppConst.YEARFMT;
		if (null != dateStr && dateStr.length() >= tmpFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, tmpFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
				Date date = sdf.parse(tmpStr);
				isLeap = DateUtil.isLeap(date);
			} catch (ParseException pe) {

			}
		}
		return isLeap;
	}

	// ////////////////////////////////////////////////////////////
	/**
	 * 取出指定日期所在工作周，指定几周范围内，所有指定工作日的日期数组 如取出 20051201 所在工作周内，5周内所有星期三的日期数组
	 *
	 * @param date
	 *            指定的日期
	 * @param diffWeeks
	 *            工作周的范围，正值向后，负值向前
	 * @param baseWeekDay
	 *            基准的工作日,取值 1－7，1 表示星期日
	 * @return 返回指定的几周内基准工作日的日期数组，日期格式 yyyyMMdd
	 */
	public static String[] getDiffWeek(Date date, int diffWeeks, int baseWeekDay) {
		String[] ret = null;
		String tmpFmt = AppConst.DATEFMT;

		SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
		Date tmpDate = date;
		if (null == tmpDate)
			tmpDate = new Date();
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		// 设置到指定日期
		cal.setTime(tmpDate);
		// 设置到指定的基准工作日
		cal.set(Calendar.DAY_OF_WEEK, baseWeekDay);
		int weeks = Math.abs(diffWeeks) + 1;
		if (diffWeeks < 0)
			cal.add(Calendar.DATE, diffWeeks * 7);
		ret = new String[weeks];
		sdf = new SimpleDateFormat(AppConst.DATEFMT);
		for (int i = 0; i < weeks; i++) {
			ret[i] = sdf.format(cal.getTime());
			cal.add(Calendar.DATE, 7);
		}

		return ret;
	}

	/**
	 * 取出指定日期所在工作周，指定几周范围内，所有指定工作日的日期数组 如取出 20051201 所在工作周内，5周内所有星期三的日期数组
	 *
	 * @param dateStr
	 *            指定的日期
	 * @param dateFmt
	 *            指定的日期分析格式
	 * @param diffWeeks
	 *            工作周的范围，正值向后，负值向前
	 * @param baseWeekDay
	 *            基准的工作日,取值 1－7，1 表示星期日
	 * @return 返回指定的几周内基准工作日的日期数组，日期格式 yyyyMMdd
	 */
	public static String[] getDiffWeek(String dateStr, String dateFmt,
			int diffWeeks, int baseWeekDay) {
		String[] ret = null;
		String tmpFmt = dateFmt;
		if (null == dateFmt)
			tmpFmt = AppConst.DATEFMT;
		if (null != dateStr && dateStr.length() >= tmpFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, tmpFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
				Date date = null;
				if (null == dateStr)
					date = new Date();
				else
					date = sdf.parse(tmpStr);
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				// 设置到指定日期
				cal.setTime(date);
				// 设置到指定的基准工作日
				cal.set(Calendar.DAY_OF_WEEK, baseWeekDay);
				int weeks = Math.abs(diffWeeks) + 1;
				if (diffWeeks < 0)
					cal.add(Calendar.DATE, diffWeeks * 7);
				ret = new String[weeks];
				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				for (int i = 0; i < weeks; i++) {
					ret[i] = sdf.format(cal.getTime());
					cal.add(Calendar.DATE, 7);
				}
			} catch (ParseException pe) {
				ret = null;
			}
		}
		return ret;
	}

	/**
	 * 取出指定日期所在工作周，指定几周范围内，所有指定工作日的日期数组 如取出 20051201 所在工作周内，5周内所有星期三的日期数组
	 *
	 * @param dateStr
	 *            指定的日期
	 * @param diffWeeks
	 *            工作周的范围，正值向后，负值向前
	 * @param baseWeekDay
	 *            基准的工作日,取值 1－7，1 表示星期日
	 * @return 返回指定的几周内基准工作日的日期数组,日期格式 yyyyMMdd
	 */
	public static String[] getDiffWeek(String dateStr, int diffWeeks,
			int baseWeekDay) {
		return getDiffWeek(dateStr, null, diffWeeks, baseWeekDay);
	}

	// ///////////////////////////////////////
	/**
	 * 获取指定的SQL语句所获取的有数据的最大年份值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @return 返回的日期值，默认格式 yyyy
	 */
	public static String getDataYear(String sql) {
		return getDataYear(sql, null);
	}

	/**
	 * 获取指定的SQL语句所获取的有数据的最大年份值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @param dateFmt
	 *            查询结果的日期格式，默认 yyyy
	 * @return 返回的日期值，默认格式 yyyy
	 */
	public static String getDataYear(String sql, String dateFmt) {
		String ret = null;
		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			String tmpFmt = dateFmt;
			if (null == tmpFmt)
				tmpFmt = AppConst.YEARFMT;
			if (null != svces) {
				String tmpStr = svces[0][0];
				if (null != tmpStr && !"".equals(tmpStr)
						&& tmpStr.length() >= tmpFmt.length()) {
					// 这里进行一下 format
					tmpStr = tmpStr.substring(0, tmpFmt.length());
					SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
					Date tmpDate = sdf.parse(tmpStr);
					sdf = new SimpleDateFormat(AppConst.YEARFMT);
					ret = sdf.format(tmpDate);
				}
			}
		} catch (AppException ae) {
			// 这里不再向上抛出异常
		} catch (ParseException pe) {
			// 这里不再向上抛出异常
		}
		return ret;
	}

	// //////////////////////////////////////////////////
	/**
	 * 获取指定的SQL语句所获取的有数据的最大月份值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @return 返回的日期值，默认格式 yyyyMM
	 */
	public static String getDataMonth(String sql) {
		return getDataMonth(sql, null);
	}

	/**
	 * 获取指定的SQL语句所获取的有数据的最大月份值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @param dateFmt
	 *            查询结果的日期格式，默认 yyyyMM
	 * @return 返回的日期值，默认格式 yyyyMM
	 */
	public static String getDataMonth(String sql, String dateFmt) {
		String ret = null;
		try {
			System.out.println("sql==" + sql);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			String tmpFmt = dateFmt;
			if (null == tmpFmt)
				tmpFmt = AppConst.MONTHFTM;
			if (null != svces) {
				String tmpStr = svces[0][0];
				if (null != tmpStr && !"".equals(tmpStr)
						&& tmpStr.length() >= tmpFmt.length()) {
					// 这里进行一下 format
					tmpStr = tmpStr.substring(0, tmpFmt.length());
					SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
					Date tmpDate = sdf.parse(tmpStr);
					sdf = new SimpleDateFormat(AppConst.MONTHFTM);
					ret = sdf.format(tmpDate);
				}
			}
		} catch (AppException ae) {
			// 这里不再向上抛出异常
		} catch (ParseException pe) {
			// 这里不再向上抛出异常
		}
		return ret;
	}

	// /////////////////////////////////////////////

	/**
	 * 获取指定的SQL语句所获取的有数据的最大日期值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @return 返回的日期值，默认格式 yyyyMMdd
	 */
	public static String getDataDay(String sql) {
		return getDataDay(sql, null);
	}

	/**
	 * 获取指定的SQL语句所获取的有数据的最大日期值
	 *
	 * @param sql
	 *            指定的查询SQL语句
	 * @param dateFmt
	 *            查询结果的日期格式，默认 yyyyMMdd
	 * @return 返回的日期值，默认格式 yyyyMMdd
	 */
	public static String getDataDay(String sql, String dateFmt) {
		String ret = null;
		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			String tmpFmt = dateFmt;
			if (null == tmpFmt)
				tmpFmt = AppConst.DATEFMT;
			if (null != svces) {
				String tmpStr = svces[0][0];
				if (null != tmpStr && !"".equals(tmpStr)
						&& tmpStr.length() >= tmpFmt.length()) {
					// 这里进行一下 format
					tmpStr = tmpStr.substring(0, tmpFmt.length());
					SimpleDateFormat sdf = new SimpleDateFormat(tmpFmt);
					Date tmpDate = sdf.parse(tmpStr);
					sdf = new SimpleDateFormat(AppConst.DATEFMT);
					ret = sdf.format(tmpDate);
				}
			}
		} catch (AppException ae) {
			// 这里不再向上抛出异常
		} catch (ParseException pe) {
			// 这里不再向上抛出异常
		}
		return ret;
	}

	// ////////////////////////
	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getWeekSpansStr(int diffDays, Date date,
			boolean preToPro) {
		String ret = null;
		Date inDate = null;

		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffDays < 0)
			startCal.add(Calendar.DATE, diffDays);

		int arrLen = Math.abs(diffDays) + 1;
		ret = "";
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret = ret
						+ AppConst.WEEKDAYS[startCal.get(Calendar.DAY_OF_WEEK)]
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.DATE, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret = ret
						+ AppConst.WEEKDAYS[startCal.get(Calendar.DAY_OF_WEEK)]
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.DATE, 1);
			}
		}
		if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
			ret = ret.substring(0, ret.lastIndexOf(AppConst.DATESPLITCHAR));

		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMDdd，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMMDdd
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getWeekSpansStr(int diffDays, String dateStr,
			String dateFmt, boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		if (null != dateFmt)
			dtFmt = dateFmt;
		Date inDate = null;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());

				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffDays < 0)
					startCal.add(Calendar.DATE, diffDays);

				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				int arrLen = Math.abs(diffDays) + 1;
				ret = "";
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret = ret
								+ AppConst.WEEKDAYS[startCal
										.get(Calendar.DAY_OF_WEEK)]
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.DATE, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret = ret
								+ AppConst.WEEKDAYS[startCal
										.get(Calendar.DAY_OF_WEEK)]
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.DATE, 1);
					}
				}
				if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
					ret = ret.substring(0,
							ret.lastIndexOf(AppConst.DATESPLITCHAR));

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的年份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getWeekSpansStr(int diffDays, String dateStr,
			boolean preToPro) {
		return DateUtil.getWeekSpansStr(diffDays, dateStr, null, preToPro);
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的年数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为com.asiabi.common.app.AppConst.WEEKDAYS 定义
	 */
	public static String[] getWeekSpans(int diffDays, Date date,
			boolean preToPro) {
		String[] ret = null;
		Date inDate = null;

		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffDays < 0)
			startCal.add(Calendar.DATE, diffDays);

		int arrLen = Math.abs(diffDays) + 1;
		ret = new String[arrLen];
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret[i] = AppConst.WEEKDAYS[startCal.get(Calendar.DAY_OF_WEEK)];
				startCal.add(Calendar.DATE, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret[i] = AppConst.WEEKDAYS[startCal.get(Calendar.DAY_OF_WEEK)];
				startCal.add(Calendar.DATE, 1);
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的年份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMMdd
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为com.asiabi.common.app.AppConst.WEEKDAYS 定义
	 */
	public static String[] getWeekSpans(int diffDays, String dateStr,
			String dateFmt, boolean preToPro) {
		String[] ret = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffDays < 0)
					startCal.add(Calendar.DATE, diffDays);

				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				int arrLen = Math.abs(diffDays) + 1;
				ret = new String[arrLen];
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret[i] = AppConst.WEEKDAYS[startCal
								.get(Calendar.DAY_OF_WEEK)];
						startCal.add(Calendar.DATE, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret[i] = AppConst.WEEKDAYS[startCal
								.get(Calendar.DAY_OF_WEEK)];
						startCal.add(Calendar.DATE, 1);
					}
				}

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的年数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为com.asiabi.common.app.AppConst.WEEKDAYS 定义
	 */
	public static String[] getWeekSpans(int diffDays, String dateStr,
			boolean preToPro) {
		return DateUtil.getWeekSpans(diffDays, dateStr, null, preToPro);

	}

	// ////////////////////////////////////////////////////////////////
	/**
	 * 返回指定日期内，指定年份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyy 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getYearSpansStr(int diffYears, Date date,
			boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.YEARFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;

		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffYears < 0)
			startCal.add(Calendar.YEAR, diffYears);

		sdf = new SimpleDateFormat(AppConst.YEARFMT);
		int arrLen = Math.abs(diffYears) + 1;
		ret = "";
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret = sdf.format(startCal.getTime());
				startCal.add(Calendar.YEAR, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret = ret + sdf.format(startCal.getTime())
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.YEAR, 1);
			}
		}
		if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
			ret = ret.substring(0, ret.lastIndexOf(AppConst.DATESPLITCHAR));

		return ret;
	}

	/**
	 * 返回指定日期内，指定年份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyy，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyy
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyy 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getYearSpansStr(int diffYears, String dateStr,
			String dateFmt, boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.YEARFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffYears < 0)
					startCal.add(Calendar.YEAR, diffYears);

				sdf = new SimpleDateFormat(AppConst.YEARFMT);
				int arrLen = Math.abs(diffYears) + 1;
				ret = "";
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.YEAR, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.YEAR, 1);
					}
				}
				if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
					ret = ret.substring(0,
							ret.lastIndexOf(AppConst.DATESPLITCHAR));

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定年份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的年份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyy，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyy 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getYearSpansStr(int diffYears, String dateStr,
			boolean preToPro) {
		return DateUtil.getYearSpansStr(diffYears, dateStr, null, preToPro);
	}

	/**
	 * 返回指定日期内，指定年份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的年数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyy 格式
	 */
	public static String[] getYearSpans(int diffYears, Date date,
			boolean preToPro) {
		String[] ret = null;
		Date inDate = null;

		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffYears < 0)
			startCal.add(Calendar.YEAR, diffYears);

		SimpleDateFormat sdf = new SimpleDateFormat(AppConst.YEARFMT);
		int arrLen = Math.abs(diffYears) + 1;
		ret = new String[arrLen];
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.YEAR, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.YEAR, 1);
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定年数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的年份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyy，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyy
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyy 格式
	 */
	public static String[] getYearSpans(int diffYears, String dateStr,
			String dateFmt, boolean preToPro) {
		String[] ret = null;
		String dtFmt = AppConst.YEARFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffYears < 0)
					startCal.add(Calendar.YEAR, diffYears);

				sdf = new SimpleDateFormat(AppConst.YEARFMT);
				int arrLen = Math.abs(diffYears) + 1;
				ret = new String[arrLen];
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.YEAR, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.YEAR, 1);
					}
				}

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定年份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffYears
	 *            指定范围的年数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyy，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyy 格式
	 */
	public static String[] getYearSpans(int diffYears, String dateStr,
			boolean preToPro) {
		return DateUtil.getYearSpans(diffYears, dateStr, null, preToPro);

	}

	// ////////////////////////////////////////////////////////////////

	/**
	 * 返回指定日期内，指定月份数的所有日期字符串，并用分隔符分隔，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMM 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getMonthSpansStr(int diffMonths, Date date,
			boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.MONTHFTM;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;

		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffMonths < 0)
			startCal.add(Calendar.MONTH, diffMonths);

		sdf = new SimpleDateFormat(AppConst.MONTHFTM);
		int arrLen = Math.abs(diffMonths) + 1;
		ret = "";
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret = ret + sdf.format(startCal.getTime())
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.MONTH, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret = ret + sdf.format(startCal.getTime())
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.MONTH, 1);
			}
		}
		if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
			ret = ret.substring(0, ret.lastIndexOf(AppConst.DATESPLITCHAR));

		return ret;
	}

	/**
	 * 返回指定日期内，指定月份数的所有日期字符串，并用分隔符分隔，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMM，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMM
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMM 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getMonthSpansStr(int diffMonths, String dateStr,
			String dateFmt, boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.MONTHFTM;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffMonths < 0)
					startCal.add(Calendar.MONTH, diffMonths);

				sdf = new SimpleDateFormat(AppConst.MONTHFTM);
				int arrLen = Math.abs(diffMonths) + 1;
				ret = "";
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.MONTH, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.MONTH, 1);
					}
				}
				if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
					ret = ret.substring(0,
							ret.lastIndexOf(AppConst.DATESPLITCHAR));

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定月份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMM，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMM 格式，日期串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getMonthSpansStr(int diffMonths, String dateStr,
			boolean preToPro) {
		return DateUtil.getMonthSpansStr(diffMonths, dateStr, null, preToPro);
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMM 格式
	 */
	public static String[] getMonthSpans(int diffMonths, Date date,
			boolean preToPro) {
		String[] ret = null;
		Date inDate = null;

		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffMonths < 0)
			startCal.add(Calendar.MONTH, diffMonths);

		SimpleDateFormat sdf = new SimpleDateFormat(AppConst.MONTHFTM);
		int arrLen = Math.abs(diffMonths) + 1;
		ret = new String[arrLen];
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.MONTH, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.MONTH, 1);
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定月份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMM，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMM
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMM 格式
	 */
	public static String[] getMonthSpans(int diffMonths, String dateStr,
			String dateFmt, boolean preToPro) {
		String[] ret = null;
		String dtFmt = AppConst.MONTHFTM;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffMonths < 0)
					startCal.add(Calendar.MONTH, diffMonths);

				sdf = new SimpleDateFormat(AppConst.MONTHFTM);
				int arrLen = Math.abs(diffMonths) + 1;
				ret = new String[arrLen];
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.MONTH, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.MONTH, 1);
					}
				}

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定月份数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffMonths
	 *            指定范围的月份数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMM，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMM 格式
	 */
	public static String[] getMonthSpans(int diffMonths, String dateStr,
			boolean preToPro) {
		return DateUtil.getMonthSpans(diffMonths, dateStr, null, preToPro);

	}

	// ///////////////////////////////////////////////////////////////////////
	/**
	 * 返回指定日期内，指定天数的所有日期字符串，并按指定的分隔符号分隔，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMMdd 格式，日期子串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getDaySpansStr(int diffDays, Date date,
			boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;

		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffDays < 0)
			startCal.add(Calendar.DATE, diffDays);

		sdf = new SimpleDateFormat(AppConst.DATEFMT);
		int arrLen = Math.abs(diffDays) + 1;
		ret = "";
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret = ret + sdf.format(startCal.getTime())
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.DATE, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret = ret + sdf.format(startCal.getTime())
						+ AppConst.DATESPLITCHAR;
				startCal.add(Calendar.DATE, 1);
			}
		}
		if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
			ret = ret.substring(0, ret.lastIndexOf(AppConst.DATESPLITCHAR));

		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期字符串，字符串用分隔符分隔，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMMdd
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMMdd 格式，日期子串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getDaySpansStr(int diffDays, String dateStr,
			String dateFmt, boolean preToPro) {
		String ret = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffDays < 0)
					startCal.add(Calendar.DATE, diffDays);

				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				int arrLen = Math.abs(diffDays) + 1;
				ret = "";
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						;
						startCal.add(Calendar.DATE, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret = ret + sdf.format(startCal.getTime())
								+ AppConst.DATESPLITCHAR;
						startCal.add(Calendar.DATE, 1);
					}
				}
				if (ret.indexOf(AppConst.DATESPLITCHAR) >= 0)
					ret = ret.substring(0,
							ret.lastIndexOf(AppConst.DATESPLITCHAR));

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期字符串，字符串以分隔符分隔，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串，每个字符串为 yyyyMMdd 格式，日期子串用
	 *         com.asiabi.common.app.AppConst.DATESPLITCHAR(默认为逗号) 分隔,
	 */
	public static String getDaySpansStr(int diffDays, String dateStr,
			boolean preToPro) {
		return DateUtil.getDaySpansStr(diffDays, dateStr, null, preToPro);
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param date
	 *            指定的日期，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMMdd 格式
	 */
	public static String[] getDaySpans(int diffDays, Date date, boolean preToPro) {
		String[] ret = null;
		Date inDate = null;

		if (null == date)
			inDate = new Date();
		else
			inDate = date;

		// 开始偏移
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(inDate);
		// 如果是向前的范围，将起始设置偏移后的
		if (diffDays < 0)
			startCal.add(Calendar.DATE, diffDays);

		SimpleDateFormat sdf = new SimpleDateFormat(AppConst.DATEFMT);
		int arrLen = Math.abs(diffDays) + 1;
		ret = new String[arrLen];
		if (preToPro) {
			for (int i = 0; i < arrLen; i++) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.DATE, 1);
			}
		} else {
			for (int i = arrLen - 1; i >= 0; i--) {
				ret[i] = sdf.format(startCal.getTime());
				startCal.add(Calendar.DATE, 1);
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param dateFmt
	 *            指定的格式，默认为 yyyyMMdd
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMMdd 格式
	 */
	public static String[] getDaySpans(int diffDays, String dateStr,
			String dateFmt, boolean preToPro) {
		String[] ret = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);

				// 开始偏移
				Calendar startCal = Calendar.getInstance(Locale.CHINA);
				startCal.setTime(inDate);
				// 如果是向前的范围，将起始设置偏移后的
				if (diffDays < 0)
					startCal.add(Calendar.DATE, diffDays);

				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				int arrLen = Math.abs(diffDays) + 1;
				ret = new String[arrLen];
				if (preToPro) {
					for (int i = 0; i < arrLen; i++) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.DATE, 1);
					}
				} else {
					for (int i = arrLen - 1; i >= 0; i--) {
						ret[i] = sdf.format(startCal.getTime());
						startCal.add(Calendar.DATE, 1);
					}
				}

			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 返回指定日期内，指定天数的所有日期数组，并按指定顺序显示
	 *
	 * @param diffDays
	 *            指定范围的天数,正值为向后的范围，负值为向前的范围
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd，如果为 null,取系统当前日期
	 * @param preToPro
	 *            是否按由小日期到大日期格式存进数组
	 * @return 返回的包含指定日期的字符串数组，每个字符串为 yyyyMMdd 格式
	 */
	public static String[] getDaySpans(int diffDays, String dateStr,
			boolean preToPro) {
		return DateUtil.getDaySpans(diffDays, dateStr, null, preToPro);

	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @return 返回整型的星期的数值，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static int getWorkDay() {
		int ret = AppConst.ERRORRESULT;
		Date date = null;

		date = new Date();
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(date);
		ret = cal.get(Calendar.DAY_OF_WEEK);
		return ret;
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @return 返回星期的字符串，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static String getWorkDayStr() {
		return AppConst.WEEKDAYS[DateUtil.getWorkDay()];
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param date
	 *            输入的日期，如果为 null，取当前系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为 null，默认 yyyyMMdd
	 * @return 返回整型的星期的数值，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static int getWorkDay(int diffDays, Date date) {
		int ret = AppConst.ERRORRESULT;

		Date tmpDate = null;

		if (null == date)
			tmpDate = new Date();
		else
			tmpDate = date;
		// 开始偏移
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(tmpDate);
		cal.add(Calendar.DATE, diffDays);
		ret = cal.get(Calendar.DAY_OF_WEEK);
		return ret;
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param date
	 *            输入的日期，如果为 null，取当前系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为 null，默认 yyyyMMdd
	 * @return 返回星期的字符串，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static String getWorkDayStr(int diffDays, Date date) {
		return AppConst.WEEKDAYS[DateUtil.getWorkDay(diffDays, date)];
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param dateStr
	 *            输入的日期字符串，如果为 null，取当前系统日期
	 * @return 返回整型的星期的数值，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static int getWorkDay(int diffDays, String dateStr) {
		return DateUtil.getWorkDay(diffDays, dateStr, null);
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param dateStr
	 *            输入的日期字符串，如果为 null，取当前系统日期
	 * @return 返回星期的字符串，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static String getWorkDayStr(int diffDays, String dateStr) {
		return AppConst.WEEKDAYS[DateUtil.getWorkDay(diffDays, dateStr, null)];
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param dateStr
	 *            输入的日期字符串，如果为 null，取当前系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为 null，默认 yyyyMMdd
	 * @return 返回整型的星期的数值，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static int getWorkDay(int diffDays, String dateStr, String dateFmt) {
		int ret = AppConst.ERRORRESULT;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);
				// 开始偏移
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				cal.setTime(inDate);
				cal.add(Calendar.DATE, diffDays);
				ret = cal.get(Calendar.DAY_OF_WEEK);
			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return ret;
	}

	/**
	 * 以指定的日期为基准，获取偏移指定天数的星期表示
	 *
	 * @param diffDays
	 *            指定偏移的天数，正值为向后偏移，负值向前偏移
	 * @param dateStr
	 *            输入的日期字符串，如果为 null，取当前系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为 null，默认 yyyyMMdd
	 * @return 返回星期的字符串，具体对照请参考 com.asiabi.common.app.AppConst 中定义
	 */
	public static String getWorkDayStr(int diffDays, String dateStr,
			String dateFmt) {
		return AppConst.WEEKDAYS[getWorkDay(diffDays, dateStr, dateFmt)];
	}

	/**
	 * 以指定某年为基准年，得到前后n年的年串
	 *
	 * @param diffYears
	 *            指定前移或后移的年数,正值向后移，负值向前移
	 * @param dateStr
	 *            指定的年，默认格式 yyyy 如果指定 null,使用系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为null，默认使用 yyyy
	 * @return 偏移后的日期，格式 yyyy
	 */
	public static String getDiffYear(int diffYears, String dateStr,
			String dateFmt) {
		String retDate = null;
		String dtFmt = AppConst.YEARFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);
				// 开始偏移
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				cal.setTime(inDate);
				cal.add(Calendar.YEAR, diffYears);

				sdf = new SimpleDateFormat(AppConst.YEARFMT);
				retDate = sdf.format(cal.getTime());
			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return retDate;
	}

	/**
	 * 以指定某年为基准年，得到前后n年的年串
	 *
	 * @param diffYears
	 *            指定前移或后移的年份数,正值向后移，负值向前移
	 * @param dateStr
	 *            指定的年月，默认格式 yyyy 如果指定 null,使用系统日期
	 * @return 偏移后的日期，格式 yyyy
	 */
	public static String getDiffYear(int diffYears, String dateStr) {
		return DateUtil.getDiffYear(diffYears, dateStr, null);

	}

	/**
	 * 以指定某年为基准年，得到前后n年的年串
	 *
	 * @param diffYears
	 *            指定前移或后移的年份数,正值向后移，负值向前移
	 * @param date
	 *            指定的日期,使用系统日期
	 * @return 偏移后的日期，格式 yyyyMM
	 */
	public static String getDiffYear(int diffYears, Date date) {
		String dtFmt = AppConst.YEARFMT;
		if (null == date)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		String dateStr = sdf.format(date);
		return DateUtil.getDiffYear(diffYears, dateStr, null);
	}

	/**
	 * 以系统某月为基准月，得到前后n月的年月串
	 *
	 * @param diffMonths
	 *            指定前移或后移的月份数,正值向后移，负值向前移
	 * @param dateStr
	 *            指定的年月，默认格式 yyyyMM 如果指定 null,使用系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为null，默认使用 yyyyMM
	 * @return 偏移后的日期，格式 yyyyMM
	 */
	public static String getDiffMonth(int diffMonths, String dateStr,
			String dateFmt) {
		String retDate = null;
		String dtFmt = AppConst.MONTHFTM;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);
				// 开始偏移
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				cal.setTime(inDate);
				cal.add(Calendar.MONTH, diffMonths);

				sdf = new SimpleDateFormat(AppConst.MONTHFTM);
				retDate = sdf.format(cal.getTime());
			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return retDate;
	}

	/**
	 * 以系统某月为基准月，得到前后n月的年月串
	 *
	 * @param diffMonths
	 *            指定前移或后移的月份数,正值向后移，负值向前移
	 * @param dateStr
	 *            指定的年月，默认格式 yyyyMM 如果指定 null,使用系统日期
	 * @return 偏移后的日期，格式 yyyyMM
	 */
	public static String getDiffMonth(int diffMonths, String dateStr) {
		return DateUtil.getDiffMonth(diffMonths, dateStr, null);

	}

	/**
	 * 以系统某月为基准月，得到前后n月的年月串
	 *
	 * @param diffMonths
	 *            指定前移或后移的月份数,正值向后移，负值向前移
	 * @param date
	 *            指定的日期,使用系统日期
	 * @return 偏移后的日期，格式 yyyyMM
	 */
	public static String getDiffMonth(int diffMonths, Date date) {
		String dtFmt = AppConst.MONTHFTM;
		if (null == date)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		String dateStr = sdf.format(date);
		return DateUtil.getDiffMonth(diffMonths, dateStr, null);
	}

	/**
	 * 以系统某日为基准日，得到前后n日的日期
	 *
	 * @param diffDays
	 *            指定前移或后移的天数,正值向后移的天数，负值向前移的天数
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd 如果指定 null,使用系统日期
	 * @param dateFmt
	 *            指定的日期格式，如果为null，默认使用 yyyyMMdd
	 * @return 偏移后的日期，格式 yyyyMMdd
	 */
	public static String getDiffDay(int diffDays, String dateStr, String dateFmt) {
		String retDate = null;
		String dtFmt = AppConst.DATEFMT;// 这里没有设置成类变量，防止其他方法设置。
		Date inDate = null;
		if (null != dateFmt)
			dtFmt = dateFmt;
		if (null != dateStr && dateStr.length() >= dtFmt.length()) {
			try {
				String tmpStr = dateStr.substring(0, dtFmt.length());
				SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
				if (null == dateStr)
					inDate = new Date();
				else
					inDate = sdf.parse(tmpStr);
				// 开始偏移
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				cal.setTime(inDate);
				cal.add(Calendar.DATE, diffDays);

				sdf = new SimpleDateFormat(AppConst.DATEFMT);
				retDate = sdf.format(cal.getTime());
			} catch (ParseException pe) {
				// 不作处理，也不再抛出新的异常，直接返回 null
			}
		}
		return retDate;
	}

	/**
	 * 以系统某日为基准日，得到前后n日的日期
	 *
	 * @param diffDays
	 *            指定前移或后移的天数,正值向后移的天数，负值向前移的天数
	 * @param dateStr
	 *            指定的日期，默认格式 yyyyMMdd 如果指定 null,使用系统日期
	 * @return 偏移后的日期，格式 yyyyMMdd
	 */
	public static String getDiffDay(int diffDays, String dateStr) {
		return DateUtil.getDiffDay(diffDays, dateStr, null);
	}

	/**
	 * 以系统某日为基准日，得到前后n日的日期
	 *
	 * @param diffDays
	 *            指定前移或后移的天数,正值向后移的天数，负值向前移的天数
	 * @param date
	 *            指定的日期，如果指定 null,使用系统日期
	 * @return 偏移后的日期，格式 yyyyMMdd
	 */
	public static String getDiffDay(int diffDays, Date date) {
		String dtFmt = AppConst.DATEFMT;
		if (null == date)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		String dateStr = sdf.format(date);
		return DateUtil.getDiffDay(diffDays, dateStr, null);
	}

	public static String getDiffDay(int diffDays, Date date, String dtFmt) {

		if (null == date)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		String dateStr = sdf.format(date);
		return DateUtil.getDiffDay(diffDays, dateStr, dtFmt);
	}

	public static String getDiffMonth(int diffDays, Date date, String dtFmt) {

		if (null == date)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dtFmt);
		String dateStr = sdf.format(date);
		return DateUtil.getDiffMonth(diffDays, dateStr, dtFmt);
	}

	/**
	 * 取得系统当前时间,类型为Timestamp
	 *
	 * @return Timestamp
	 */
	public static Timestamp getNowTimeStamp() {
		java.util.Date date = new java.util.Date();
		Timestamp numTime = new Timestamp(date.getTime());
		return numTime;
	}

	/**
	 * 取得系统的当前时间,类型为java.sql.Date
	 *
	 * @return java.sql.Date
	 */
	public static java.sql.Date getNowDate() {
		java.util.Date date = new java.util.Date();
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 将Timestamp类型的日期格式化为指定格式的的字符串
	 *
	 * @param date
	 *            指定的日期，如果为NULL,返回NULL
	 * @param dateFmt
	 *            日期格式，如果为NULL,按默认格式格式化yyyyMMdd
	 * @return
	 */
	public static String TimeStampToString(Timestamp date, String dateFmt) {
		String strTemp = null;
		if (date != null) {
			String dtFmt = dateFmt;
			if (null == dtFmt)
				dtFmt = AppConst.DATEFMT;
			SimpleDateFormat formatter = new SimpleDateFormat(dtFmt);
			strTemp = formatter.format(date);
		}
		return strTemp;
	}

	/**
	 * 将Timestamp类型的日期格式化为默认yyyyMMdd格式的的字符串
	 *
	 * @param date
	 *            指定的日期，如果为NULL,返回NULL
	 * @return
	 */
	public static String TimeStampToString(Timestamp date) {
		return TimeStampToString(date, null);
	}

	/**
	 * date类型的日期格式化为指定格式的的字符串
	 *
	 * @param date
	 *            指定的日期，如果为NULL,返回NULL
	 * @param dateFmt
	 *            日期格式，如果为NULL,按默认格式格式化yyyyMMdd
	 * @return
	 */
	public static String dateToString(java.util.Date date, String dateFmt) {
		String strTemp = null;
		if (date != null) {
			String dtFmt = dateFmt;
			if (null == dtFmt)
				dtFmt = AppConst.DATEFMT;
			SimpleDateFormat formatter = new SimpleDateFormat(dtFmt);
			strTemp = formatter.format(date);
		}
		return strTemp;
	}

	/**
	 * date类型的日期格式化为默认yyyyMMdd格式的的字符串
	 *
	 * @param date
	 *            指定的日期，如果为NULL,返回NULL
	 * @return
	 */
	public static String dateToString(java.sql.Date date) {
		return dateToString(date, null);
	}

	/**
	 * 将字符串按指定格式转换为 TimeStamp
	 *
	 * @param strDate
	 *            日期字符串，如果为NULL,返回 NULL
	 * @param dateFmt
	 *            指定的分析格式，如果为NULL， 按默认方式 yyyyMMdd
	 * @return
	 */
	public static Timestamp stringToTimestamp(String strDate, String dateFmt) {
		if (strDate != null && !strDate.equals("")) {
			try {
				String dtFmt = dateFmt;
				if (null == dtFmt)
					dtFmt = AppConst.DATEFMT;
				SimpleDateFormat formatter = new SimpleDateFormat(dtFmt);
				java.util.Date d = formatter.parse(strDate);
				Timestamp numTime = new Timestamp(d.getTime());
				return numTime;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * String转化为java.util.date类型，
	 *
	 * @param strDate
	 * @return
	 */
	public static java.util.Date stringToDate(String strDate) {

		return stringToDate(strDate, null);

	}

	/**
	 * String转化为java.util.date类型，根据指定的格式
	 *
	 * @param strDate
	 *            日期字符串
	 * @param strFormat
	 *            日期格式，如： yyyyMMdd HHmmss 或 yyyyMMdd hhmmss
	 * @return
	 */
	public static java.util.Date stringToDate(String strDate, String strFormat) {
		if (strDate != null && !strDate.equals("")) {
			try {
				String dtFmt = strFormat;
				if (null == dtFmt)
					dtFmt = AppConst.DATEFMT;
				SimpleDateFormat formatter = new SimpleDateFormat(dtFmt);
				java.util.Date d = formatter.parse(strDate);
				return d;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 取得当前系统的年份数值
	 *
	 * @return
	 */
	public static int getSystemYear() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(DateUtil.getNowDate());
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 取得系统月份
	 *
	 * @return
	 */
	public static int getSystemMonth() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(DateUtil.getNowDate());
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取得系统当前月具体天数
	 *
	 * @return
	 */
	public static int getSystemDay() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(DateUtil.getNowDate());
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回指定年月的实际天数
	 *
	 * @param strYear
	 * @param strMonth
	 * @return
	 */
	public static int getCurMonthDayNumber(String strYear, String strMonth) {
		int ret = AppConst.ERRORRESULT;
		try {
			if (Integer.parseInt(strMonth) > 0
					&& Integer.parseInt(strMonth) <= 12) {
				Calendar cal = Calendar.getInstance(Locale.CHINA);
				cal.clear();
				cal.set(new Integer(strYear).intValue(),
						new Integer(strMonth).intValue() - 1, 1);
				ret = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
		} catch (NumberFormatException nfe) {

		}
		return ret;
	}

	public static String getMonthLastDay(String dateStr) {

		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(dateStr);
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return dateToString(calendar.getTime(), "yyyyMMdd");

		} catch (ParseException e) {

			e.printStackTrace();
			return null;
		}

	}

	public static String[] getThisSeasonTime(String yearMon) {
		String retn[] = new String[2];

		String strYear = yearMon.substring(0, 4);
		String strMonth = yearMon.substring(4);

		int month = Integer.parseInt(strMonth);

		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		retn[0] = strYear + season;

		retn[1] = strYear + "年第" + season + "季度";
		return retn;
	}

	public static String getDayDesc(String date) {
		String retn = "";

		String strYear = date.substring(0, 4);
		String strMonth = date.substring(4,6);
		String strDay = date.substring(6);


		retn = strYear + "年" + strMonth + "月"+strDay +"日";

		return retn;
	}

	public static String[] getYearAndMonthDesc(String yearMon) {
		String retn[] = new String[3];

		String strYear = yearMon.substring(0, 4);
		String strMonth = yearMon.substring(4);
		int month = Integer.parseInt(strMonth);

		retn[0] = strYear;
		retn[1] = strYear + "年";

		retn[2] = strYear + "年" + month + "月";

		return retn;
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.getDiffDay(-2, "20060105", "yyyyMMdd"));
		System.out.println(DateUtil.getDiffDay(-2, "20060105"));
		System.out.println(DateUtil.getDiffDay(-2, "2006-01-05"));
		System.out.println(DateUtil.getDiffMonth(-2, "200601", "yyyyMM"));
		System.out.println(DateUtil.getDiffMonth(-2, "200512"));
		System.out.println(DateUtil.getDiffMonth(-2, "2006-01-05"));
		System.out.println(DateUtil.getDiffYear(-2, "2006", "yyyy"));
		System.out.println(DateUtil.getDiffYear(-2, "2005"));
		System.out.println(DateUtil.getDiffYear(-2, "2006-01-05"));
		System.out.println(DateUtil.getWorkDay(-2, "20060109", "yyyyMMdd"));
		System.out.println(DateUtil.getWorkDay());
		String[] tmpStr = DateUtil.getDaySpans(-2, "20060109", "yyyyMMdd",
				false);
		for (int i = 0; i < tmpStr.length; i++)
			System.out.println(tmpStr[i]);
		System.out.println("----"
				+ DateUtil.getDaySpansStr(-2, "20060109", "yyyyMMdd", true));
		tmpStr = DateUtil.getMonthSpans(-2, "200601", "yyyyMM", false);
		for (int i = 0; i < tmpStr.length; i++)
			System.out.println(tmpStr[i]);
		System.out.println("----"
				+ DateUtil.getMonthSpansStr(-2, "200601", "yyyyMM", true));
		tmpStr = DateUtil.getYearSpans(-2, "2006", "yyyy", false);
		for (int i = 0; i < tmpStr.length; i++)
			System.out.println(tmpStr[i]);
		System.out.println("----"
				+ DateUtil.getYearSpansStr(-2, "2006", "yyyy", true));

		tmpStr = DateUtil.getWeekSpans(-2, "20060110", "yyyyMMdd", false);
		for (int i = 0; i < tmpStr.length; i++)
			System.out.println(tmpStr[i]);
		System.out.println("----"
				+ DateUtil.getWeekSpansStr(-2, "20060110", "yyyyMMdd", true));

		tmpStr = DateUtil.getDiffWeek("20060110", "yyyyMMdd", 3, 3);
		for (int i = 0; i < tmpStr.length; i++)
			System.out.println(tmpStr[i]);

		System.out.println(DateUtil.isLeap("2006", null));
		System.out.println(DateUtil.getDateSpans("20051213", "20051201",
				"yyyyMMdd"));
		System.out.println(DateUtil.getMonthLastDay("20040201"));
		System.out.println(DateUtil.getMonthLastDay("20050201"));
		System.out.println(DateUtil.getMonthLastDay("20060201"));
		System.out.println(DateUtil.getMonthLastDay("20070201"));
		System.out.println(DateUtil.getMonthLastDay("20080201"));
		System.out.println(DateUtil.getNowDate());
	}
}