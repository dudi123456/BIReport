package com.ailk.bi.syspar.util;

public class ParamConstant {
	/**
	 * 查询数据结果
	 */
	public final static String PARAM_SQL_VALUE = "PARAM_SQL_VALUE";
	/**
	 * 查询条件结果
	 */
	public final static String PARAM_CONDITION_RESULT = "addConResult";
	/**
	 * 参数节点
	 */
	public final static String PARAM_CONDITION_NODE_ID = "t_node_id";
	/**
	 * 
	 */
	public final static String PARAM_TABLE_NAME = "t_table_name";
	/**
	 * 参数元素据表
	 */
	public final static String PARAM_CONF_META_TABLE = "PARAM_CONF_IBAS_TABLE";
	/**
	 * 参数表
	 */
	public final static String PARAM_CONF_INFO_TABLE = "PARAM_CONF_IBAS_TREE_TABLE";
	/**
	 * 清空回话标志
	 */
	public final static String PARAM_CLEAR_FLAG = "clearFlag";

	/*************************************** 显示类型 *************************************/
	/**
	 * 文本框
	 */
	public final static String COLUMN_SHOW_TYPE_1 = "1";// 文本框
	/**
	 * 日期
	 */
	public final static String COLUMN_SHOW_TYPE_2 = "2";// 日期
	/**
	 * 月份
	 */
	public final static String COLUMN_SHOW_TYPE_3 = "3";// 月份
	/**
	 * 自定义SQL
	 */
	public final static String COLUMN_SHOW_TYPE_4 = "4";// 自定义SQL
	/**
	 * 自定义标识
	 */
	public final static String COLUMN_SHOW_TYPE_5 = "5";// 自定义标识
	/**
	 * textarea
	 */
	public final static String COLUMN_SHOW_TYPE_6 = "6";// textarea

	/**
	 * 自定义标识
	 */
	public final static String COLUMN_SHOW_TYPE_7 = "7";// 内存参数

	/**
	 * 弹出单选按钮
	 */
	public final static String COLUMN_SHOW_TYPE_8 = "8";// 弹出单选按钮

	/********************************************** 数据类型 ******************************/
	/**
	 * 日期
	 */
	public final static String COLUMN_DATA_TYPE_D = "D";// 日期

	/**
	 * 文本
	 */
	public final static String COLUMN_DATA_TYPE_V = "V";// 文本

	/**
	 * 数值
	 */
	public final static String COLUMN_DATA_TYPE_N = "N";// 数值

	/**
	 * LONG类型
	 */
	public final static String COLUMN_DATA_TYPE_L = "L";// LONG类型

	/**
	 * 只读
	 */
	public final static String COLUMN_DATA_TYPE_R = "R";// 只读文本

	/******************************** 默认应用 *************************************/

	public final static String DEFAULT_APP_NODE_ID = "1011";

	/******************************** 总帐数据应用 *************************************/

	public final static String ZZ_APP_LOGIN_USER = "APP1001";// 登录用户标识

	public final static String ZZ_APP_BEGIN_DATE = "APP1002";// 生效日期

	public final static String ZZ_APP_END_DATE = "APP1003";// 失效日期

	public final static String ZZ_APP_SEQ_ID = "APP1004";// 失效日期

}
