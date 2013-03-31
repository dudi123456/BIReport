package com.ailk.bi.base.util;

public final class TableConsts {

	/**
	 * 用户自定义指标的月分类的虚拟标识
	 */
	public final static String CUSTOM_MSU_VIR_MONTH_ID = "MONTH_TYPE";

	/**
	 * 用户自定义指标的日分类的虚拟标识
	 */
	public final static String CUSTOM_MSU_VIR_DAY_ID = "DAY_TYPE";

	/**
	 * 用户自定义指标的序列名称
	 */
	public final static String CUSTOM_MSU_SEQUENCE = "SEQ_RPT_DERI_MSU";

	/**
	 * 指标体系中第一级指标的父指标标识
	 */
	public final static String MSU_FIRST_LEVEL_PARENT_ID = "0";

	/**
	 * 字段数据类型为数值型
	 */
	public final static String DATA_TYPE_NUMER = "1";

	/**
	 * 字段数据类型为字符串型
	 */
	public final static String DATA_TYPE_STRING = "2";

	/**
	 * 数据库是否字段的是
	 */
	public final static String YES = "Y";

	/**
	 * 数据库是否字段的否
	 */
	public final static String NO = "N";

	/**
	 * 数据通用
	 */
	public final static String DATA_USED_FOR_GENERAL = "0";

	/**
	 * 数据用于报表中心
	 */
	public final static String DATA_USED_FOR_RPT = "10";

	/**
	 * 数据用于报表定制
	 */
	public final static String DATA_USED_FOR_RPT_CUSTOM = "15";

	/**
	 * 数据用于主题分析
	 */
	public final static String DATA_USED_FOR_OLAP = "20";

	/**
	 * 数据用于KPI
	 */
	public final static String DATA_USED_FOR_KPI = "30";

	/**
	 * 数据用于上传总部
	 */
	public final static String DATA_USED_FOR_UPLOAD = "40";

	/**
	 * 数据用于即席查询
	 */
	public final static String DATA_USED_FOR_CUSSEARCH = "50";

	/**
	 * 数据用于即时搜索
	 */
	public final static String DATA_USED_FOR_SEARCH = "60";

	/**
	 * 数据用于专题
	 */
	public final static String DATA_USED_FOR_SUBJECT = "70";

	/**
	 * 数据为NULL时显示为0
	 */
	public final static String NULL_DISPLAY_ZERO = "0";

	/**
	 * 数据为NULL时显示空白
	 */
	public final static String NULL_DISPLAY_BLANK = "1";

	/**
	 * 数据为0时显示0
	 */
	public final static String ZERO_DISPLAY_ZERO = "0";

	/**
	 * 数据为0时显示空白
	 */
	public final static String ZERO_DISPLAY_BLANK = "1";

	/**
	 * 数据统计周期年
	 */
	public final static String STAT_PERIOD_YEAR = "1";

	/**
	 * 数据统计周期半年
	 */
	public final static String STAT_PERIOD_HALFYEAR = "2";

	/**
	 * 数据统计周期季度
	 */
	public final static String STAT_PERIOD_QUARTER = "3";

	/**
	 * 数据统计周期月
	 */
	public final static String STAT_PERIOD_MONTH = "4";

	/**
	 * 数据统计周期旬
	 */
	public final static String STAT_PERIOD_TENDAYS = "5";

	/**
	 * 数据统计周期日
	 */
	public final static String STAT_PERIOD_DAY = "6";

	/**
	 * 预警和本期值比较
	 */
	public final static String ALERT_COMPARE_TO_THIS_PERIOD = "0";

	/**
	 * 预警和同比上期值比较
	 */
	public final static String ALERT_COMPARE_TO_SAME_PERIOD = "1";

	/**
	 * 预警和环比上期值比较
	 */
	public final static String ALERT_COMPARE_TO_LAST_PERIOD = "2";

	/**
	 * 单元格底色预警
	 */
	public final static String ALERT_CELL_BGCOLOR = "0";

	/**
	 * 箭头预警
	 */
	public final static String ALERT_ARROW = "1";

	/**
	 * 预警颜色绿色
	 */
	public final static String ALERT_COLOR_GREEN = "G";

	/**
	 * 预警颜色红色f
	 */
	public final static String ALERT_COLOR_RED = "R";

	/* renhui */
	/**
	 * 报表tr样式
	 */
	public final static String RPT_TR_STYLE = "class=\"table-white-bg\"";

	/**
	 * 报表表头td样式
	 */
	public final static String RPT_HEADTD_STYLE = "class=\"tab-title\"";

	/**
	 * 报表td样式
	 */
	public final static String RPT_TD_STYLE = "";

	/**
	 * 报表时间段
	 */
	public final static String RPT_DATE_PART = "99";

	/**
	 * 有数据的最近日期
	 */
	public final static String RPT_DATE_LAST = "-99";

	/**
	 * 回退标志
	 */
	public final static String RETURN = "R";

	/**
	 * 前行标志
	 */
	public final static String FORWARD = "F";

	/**
	 * 步骤1
	 */
	public final static String FRIST_STEP = "1";

	/**
	 * 终审步骤
	 */
	public final static String FINAL_STEP = "E";

	/**
	 * 同一资源条件
	 */
	public final static String CONDITION_PUB = "0";

	/**
	 * 图形资源条件
	 */
	public final static String CONDITION_CHART = "1";

	/**
	 * 表格资源条件
	 */
	public final static String CONDITION_TABLE = "2";

	/**
	 * 整体合并替换条件
	 */
	public final static String CONDITION_PART = "3";

	/**
	 * 整体合并替换条件4
	 */
	public final static String CONDITION_PART_FOUR = "4";

	/**
	 * 整体合并替换条件5
	 */
	public final static String CONDITION_PART_FIVE = "5";
	/* renhui end */
}
