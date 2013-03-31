package com.ailk.bi.base.util;

/**
 * 专题，系统监控通用模块的常量定义
 * 
 */
public class ConstantKeys {

	/**
	 * boolean 值的真
	 */
	public static String BOOLEAN_TRUE = "T";

	/**
	 * boolean 值的假
	 */
	public static String BOOLEAN_FALSE = "F";

	/**
	 * 时间对应的月维表名称，由于所有时间维度在一张维表中 因此在这里定义
	 */
	public static String TIME_DIM_MONTH_TABLE = "BI_RW.D_MONTH";

	/**
	 * 时间对应的日维表名称，由于所有时间维度在一张维表中 因此在这里定义
	 */
	public static String TIME_DIM_DAY_TABLE = "BI_RW.D_DATE";

	/**
	 * 表示日期天的字段，20060101表示2006年1月1日
	 */
	public static String TIME_DIM_DAY_FLD = "DAY_ID";

	/**
	 * 表示日期天的描述字段
	 */
	public static String TIME_DIM_DAY_DESC_FLD = "DAY_DESC";

	/**
	 * 同比日字段
	 */
	public static String TIME_DIM_SAME_DAY_FLD = "SDAY_ID";

	/**
	 * 环比日字段
	 */
	public static String TIME_DIM_LAST_DAY_FLD = "LDAY_ID";

	/**
	 * 同比月字段
	 */
	public static String TIME_DIM_SAME_MONTH_FLD = "SMONTH_ID";

	/**
	 * 环比月字段
	 */
	public static String TIME_DIM_LAST_MONTH_FLD = "LMONTH_ID";

	/**
	 * 表示日期月份的字段，格式200601表示2006年2月份
	 */
	public static String TIME_DIM_MONTH_FLD = "MONTH_ID";

	/**
	 * 表示0
	 */
	public static String ZERO = "0";

	/**
	 * 列按升序排序
	 */
	public static String SORT_ASC = "1";

	/**
	 * 列按降序排序
	 */
	public static String SORT_DESC = "2";

	/**
	 * 行唯一标识中，各字段分隔符
	 */
	public final static String MIN_SPLIT = ",";

	/**
	 * 行唯一标识中，维度间分隔符
	 */
	public final static String MAX_SPLIT = ",_,";

	/**
	 * 是否中的是
	 */
	public final static String YES = "Y";

	/**
	 * 是否中的否
	 */
	public final static String NO = "N";

	/**
	 * 字段的数据类型为数值型
	 */
	public final static String DATA_TYPE_NUMBER = "1";

	/**
	 * 字段的数据类型为字符型
	 */
	public final static String DATA_TYPE_STRING = "0";

	/**
	 * 数据存储的日期粒度为日级
	 */
	public final static String DATA_DAY_LEVEL = "6";

	/**
	 * 数据存储的日期粒度为月级
	 */
	public final static String DATA_MONTH_LEVEL = "4";

	/**
	 * 比率上升时红色标识
	 */
	public final static String RATIO_RISE_RED = "R";

	/**
	 * 比率上升时绿色标识
	 */
	public final static String RATIO_RISE_GREEN = "G";

	/**
	 * 多个联动图形只显示其中之一
	 */
	public final static String CHART_SELECT_ONE = "SELECT";

	/*
	 * [地域]
	 */
	public final static String REGION = "REGION";

	/*
	 * [品牌] BRAND
	 */
	public final static String BRAND = "BRAND";
	/*
	 * [产品] PRODUCT
	 */
	public final static String PRODUCT = "PRODUCT";

	/*
	 * [入网渠道] CHANNEL
	 */
	public final static String CHANNEL = "CHANNEL";

	/*
	 * [销售模式] SALE
	 */
	public final static String SALE = "SALE";

	/*
	 * [手机机型] MAC_TYPE
	 */
	public final static String MAC_TYPE = "MAC_TYPE";

	/*
	 * [用户级别] VIP_LEVEL
	 */
	public final static String VIP_LEVEL = "VIP_LEVEL";

	/*
	 * [在网时长] ONL_DURA
	 */
	public final static String ONL_DURA = "ONL_DURA";

	/*
	 * [基站] BASE_STATION
	 */
	public final static String BASE_STATION = "BASE_STATION";

	/*
	 * [呼转] CALL
	 */
	public final static String CALL = "CALL";

	public final static String TAG_PARENT = "0";

	/******************************** 数据表格定义常量 *******************************************/
	/**
	 * 渠道专题风险分析数据表
	 */
	public final static String CHANNEL_RISK_TABLE = "FM_G_RISK_M";

	/**
	 * 渠道专题综合信息数据表
	 */
	public final static String CHANNEL_INTEGRATE_TABLE = "FM_G_INTEGRATE_INFO_M";

	/******************************** 业务数据定义常量 *******************************************/
	/**
	 * 渠道专题业务等级默认纬度值
	 */
	public final static String CHANNEL_SVC_LVL_VALUE = "0";
	/**
	 * 渠道专题业务等级默认纬度描述
	 */
	public final static String CHANNEL_SVC_LVL_DESC = "全网";
	/**
	 * 渠道专题业务等级C网默认纬度值
	 */
	public final static String CHANNEL_SVC_LVL_C_DESC = "CDMA业务";
	/**
	 * 渠道专题业务等级G网默认纬度值
	 */
	public final static String CHANNEL_SVC_LVL_G_DESC = "GSM业务";

}
