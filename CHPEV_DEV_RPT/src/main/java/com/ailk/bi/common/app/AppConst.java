/*
 * @(#)AppConst.java
 *
 */
package com.ailk.bi.common.app;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 系统底层程序相关的常量定义
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
public class AppConst {
	// 系统配置信息
	/** Html中空格常量 */
	public final static String HTMLBLANK = "&nbsp;";

	/** 普通空格常量 */
	public final static String BLANK = "";

	/** string为空常量 */
	public final static String STRINGNULL = "null";

	public final static String TOKEN_STRING = "tag.HIDDEN_TOKEN_STRING";

	public final static String SYSTEMERRMSG = "SYSTEMERRORMSG";

	public final static String JSPURL = "JSPURL";

	public final static String LOGINUSERINFO = "LOGINUSERINO";

	/**
	 * 格式数据时的分隔符号，默认为逗号
	 */
	public final static String FMTSPLITCHAR = ",";

	/**
	 * 默认保留两位小数
	 */
	public final static int FRACTIONNUM = 2;

	public final static int FRACTIONZERONUM = 0;

	/**
	 * 当提供的字符串格式化位数为此值时，对于98.0形式返回98
	 */
	public final static int NATUREFMT = -1;

	/**
	 * oracle 数据库类型
	 */
	public final static String ORACLE = "ORA";

	/**
	 * db2 数据库类型
	 */
	public final static String DB2 = "DB2";

	/**
	 * sybase 数据库类型
	 */
	public final static String SYBASE = "SYB";

	/**
	 * 日期字符串分隔字符
	 */
	public final static String DATESPLITCHAR = ",";

	/**
	 * 默认的日期格式
	 */
	public final static String DATEFMT = "yyyyMMdd";

	/**
	 * 默认的年月格式
	 */
	public final static String MONTHFTM = "yyyyMM";

	/**
	 * 默认的年的格式
	 */
	public final static String YEARFMT = "yyyy";

	/**
	 * 前端系统星期的显示定义
	 */
	public final static String[] WEEKDAYS = { "", "星期日", "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六" };

	public final static int UNKNOWN = 0;

	public final static int SUNDAY = 1;

	public final static int MONDAY = 2;

	public final static int TUESDAY = 3;

	public final static int WENSDAY = 4;

	public final static int THURSDAY = 5;

	public final static int FRIDAY = 6;

	public final static int SATURDAY = 7;

	/**
	 * 所有非正常返回的结果用此变量表示，默认为－1
	 */
	public final static int ERRORRESULT = -1;

	/**
	 * 所有非正常返回的结果用此变量表示，默认为0 // jcm专用,需要讨论
	 */
	public final static double RESULT = 0;

	/**
	 * 百分比默认输出值 add by jcm
	 */
	public final static String RatePer = "0%";

	/**
	 * 百分比默认输出值 add by jcm
	 */
	public final static String RatePerNot = "0";

	public static final int MAX_FETCH_SIZE = 100;
	
	//总部
	public static final String Province_ZB="09";
	
	//所有地市
	public static final String City_ALL="000";
	
	//所有区县
	public static final String County_ALL="00000";
	
	//预演计算数据 0：现网数据；1：模板导数据
	public static final String IS_Import="1";
	
	public static final String IS_NoImport="0";
	
	//导入模板类型 01：用户数量表；02：用户比例表；03：产品数量表；04：产品比例表
	public static final String USER_TEMP="01";
	
	public static final String USER_BI_TEMP="02";
	
	public static final String PRODUCT_TEMP="03";
	
	public static final String PRODUCT_BI_TEMP="04";
	
    public static final String USER_NUMBER = "发展用户总数";
	
	public static final String USER_CHANL_NUMBER = "发展用户";
	
	public static final String USER_NUMBER_BI = "用户数量占比(%)";
	
    public static final String PRODUCT_NUMBER = "销售产品总数";
	
	public static final String PRODUCT_CHANL_NUMBER = "销售产品数量";
	
	public static final String PRODUCT_NUMBER_BI = "销售产品数量占比(%)";
	
	//计算方式 1:乘以固定金额 2:乘以百分比 3:固定金额
	public static final String CAL_METHOD_JIN = "1";
	
	public static final String CAL_METHOD_BI = "2";
	
	public static final String CAL_METHOD_GU = "3";
}