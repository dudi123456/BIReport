package com.ailk.bi.adhoc.util;

public class AdhocConstant {
	public final static String ADHOC_USER_BASE_INFO = "ADHOC_USER_BASE_INFO";
	public final static String ADHOC_USER_CONSUME_INFO = "ADHOC_USER_CONSUME_INFO";
	public final static String ADHOC_USER_CALL_INFO = "ADHOC_USER_CALL_INFO";
	public final static String ADHOC_USER_CONTRIBUTE_INFO = "ADHOC_USER_CONTRIBUTE_INFO";
	public final static String ADHOC_USER_SCORE_INFO = "ADHOC_USER_SCORE_INFO";
	/**
	 * 即席查询功能业务应用
	 */
	public final static String ADHOC_ROOT = "adhoc_root";

	/**
	 * 默认功能业务应用
	 */
	public final static String ADHOC_ROOT_DEFAULT_VALUE = "Y001";

	/**
	 * 条件名称
	 */
	public final static String ADHOC_CONDITION_NAME = "CON_NAME";

	/**
	 * 纬度名称
	 */
	public final static String ADHOC_DIM_NAME = "WD_NAME";

	/**
	 * 度量名称
	 */
	public final static String ADHOC_MSU_NAME = "DL_NAME";

	public final static String ADHOC_FAV_FLAG = "ADHOC_FAV_FLAG";

	/**
	 * 度量名称
	 */
	public final static String ADHOC_DIM_TABLE_TAG = "tableWD";

	/**
	 * 指标表格标记
	 */
	public final static String ADHOC_MSU_TABLE_TAG = "tableDL";

	/**
	 * 清除标记
	 */
	public final static String ADHOC_CLEAR_SESSION_TAG = "clear_tag";

	/**
	 * 即席查询条件处理会话标识
	 */
	public final static String ADHOC_CONDITION_WEBKEYS = "ADHOC_CONDITION_WEBKEYS";

	/**
	 * 即席查询默认条件
	 */
	public final static String ADHOC_DEFAULT_CONDITION_LIST = "ADHOC_DEFAULT_CONDITION_LIST";

	/**
	 * 即席查询已选择条件
	 */
	public final static String ADHOC_CONDITION_SELECTED = "ADHOC_CONDITION_SELECTED";

	public final static String ADHOC_CONDITION_SCRIPT_FUN = "ADHOC_CONDITION_SCRIPT_FUN";

	/**
	 * 即席查询纬度处理会话标识
	 */
	public final static String ADHOC_DIM_WEBKEYS = "ADHOC_DIM_WEBKEYS";

	/**
	 * 即席查询指标处理会话标识
	 */
	public final static String ADHOC_MSU_WEBKEYS = "ADHOC_MSU_WEBKEYS";

	/**
	 * 即席查询指标属性表格会话标识
	 */
	public final static String ADHOC_MSU_TABLE_WEBKEYS = "group_msu_tag";

	/**
	 * 即席查询属性表格会话标识
	 */
	public final static String ADHOC_TABLE_WEBKEYS = "group_tag";

	/**
	 * 即席查询功能会话标识
	 */
	public final static String ADHOC_ROOT_WEBKEYS = "ADHOC_ROOT_WEBKEYS";

	/**
	 * 收藏夹定义表
	 */
	public final static String ADHOC_FAVORITE_DEF_TABLE = "UI_ADHOC_FAVORITE_DEF";

	/**
	 * 收藏夹定义表会话标识
	 * 
	 */
	public final static String ADHOC_FAVORITE_DEF_TABLE_WEBKEYS = "ADHOC_FAVORITE_DEF_TABLE_WEBKEYS";

	/**
	 * 刷新标志
	 */
	public final static String ADHOC_RELOAD_TREE_KEY = "reloadtree";

	/**
	 * 收藏夹标识
	 */
	public final static String ADHOC_FAVORITE_ID = "favorite_id";

	/**
	 * ************************************条件类型定义*******************************
	 * *************
	 */
	public final static String ADHOC_CONDITION_TYPE_1 = "1"; // 简单文本框

	public final static String ADHOC_CONDITION_TYPE_2 = "2"; // 起始文本框，结束文本框

	public final static String ADHOC_CONDITION_TYPE_3 = "3"; // 下拉列表框（指定SQLID）

	public final static String ADHOC_CONDITION_TYPE_4 = "4"; // 简单单选纽（指定SQLID）

	public final static String ADHOC_CONDITION_TYPE_5 = "5"; // 多选button 弹出窗口

	public final static String ADHOC_CONDITION_TYPE_6 = "6"; // 单选button 暂时不提供

	public final static String ADHOC_CONDITION_TYPE_7 = "7"; // 日

	public final static String ADHOC_CONDITION_TYPE_8 = "8"; // 月

	public final static String ADHOC_CONDITION_TYPE_9 = "9"; // 日期时间断

	public final static String ADHOC_CONDITION_TYPE_10 = "10"; // 月份时间段

	public final static String ADHOC_CONDITION_TYPE_11 = "11"; // 下拉列表框（自定义SQL）

	public final static String ADHOC_CONDITION_TYPE_12 = "12"; // 下拉列表框（内存参数）

	public final static String ADHOC_CONDITION_TYPE_13 = "13"; // 简单单选纽（自定义SQL）

	public final static String ADHOC_CONDITION_TYPE_14 = "14"; // 简单单选纽（内存参数）

	public final static String ADHOC_CONDITION_TYPE_15 = "15"; // 多选Check

	/**
	 * 文本区域TEXTAREA
	 */

	public final static String ADHOC_CONDITION_TYPE_16 = "16"; // 文本区域,TEXTAREA

	/**
	 * 文件格式上传
	 */

	public final static String ADHOC_CONDITION_TYPE_17 = "17"; // 文件file

	public final static String ADHOC_CONDITION_TYPE_18 = "18"; // IS NULL

	public final static String ADHOC_CONDITION_TYPE_19 = "19"; // IS NOT NULL

	public final static String ADHOC_CONDITION_TYPE_20 = "20"; // 不等于的情况

	public final static String ADHOC_CONDITION_TAG_1 = "1"; // 数值型条件

	public final static String ADHOC_CONDITION_TAG_2 = "2"; // 字符型条件

	public final static String ADHOC_CONDITION_TAG_3 = "3"; // 字符型条件(模糊查询)

	/**
	 * *********************************多选定义KEY*********************************
	 * **************
	 */
	// 已经多选值
	public final static String ADHOC_MULTI_SELECT_SESSION = "ADHOC_MULTI_SELECT_SESSION";
	// 当前选定条件分析会话
	public final static String ADHOC_MULTI_SELECT_HOC_ID_SESSION = "ADHOC_MULTI_SELECT_HOC_ID_SESSION";
	// 当前选定条件分析会话
	public final static String ADHOC_MULTI_SELECT_CONDITION_ID_SESSION = "ADHOC_MULTI_SELECT_CONDITION_ID_SESSION";
	// 当前选定条件分析会话
	public final static String ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION = "ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION";
	// 已选条件查询出的数据库值
	public final static String ADHOC_MULTI_SELECT_VAlUE_SESSION = "ADHOC_MULTI_SELECT_VAlUE_SESSION";
	// 已选条件名称
	public final static String ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION = "ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION";

	/**
	 * 过滤条件生成器应用类型部门标识
	 */
	public final static String ADHOC_MULTI_SELECT_APP_TYPE_DEPT = "DEPT";
	/**
	 * 过滤条件生成器应用类型渠道标识
	 */
	public final static String ADHOC_MULTI_SELECT_APP_TYPE_CHANNEL = "CHANNEL";
	/**
	 * 过滤条件生成器应用类型套餐标识
	 */
	public final static String ADHOC_MULTI_SELECT_APP_TYPE_DNNR = "DNNR";
	/**
	 * 过滤条件生成器应用类型业务类型标识
	 */
	public final static String ADHOC_MULTI_SELECT_APP_TYPE_SVC = "SVC";

	/**
	 * *********************************界面查询条件固定参数定义****************************
	 * *******************
	 */
	// 业务类型
	public final static String ADHOC_MULTI_SELECT_QRY_SVC_KND = "svc_knd";
	// 区域或部门
	public final static String ADHOC_MULTI_SELECT_QRY_AREA_ID = "area_id";
	// 条件
	public final static String ADHOC_MULTI_SELECT_QRY_CONDITION_ID = "con_id";
	// 即席查询业务
	public final static String ADHOC_MULTI_SELECT_QRY_HOC_ID = "adhoc_id";
	// 条件名称
	public final static String ADHOC_MULTI_SELECT_QRY_CONDITION_NAME = "con_name";
	// 查询取值名称
	public final static String ADHOC_MULTI_SELECT_QRY_CONDITION_QRY_NAME = "qry_name";
	// 下级区域或部门
	public final static String ADHOC_MULTI_SELECT_QRY_SEC_AREA_ID = "sec_area_id";

	public final static String ADHOC_MULTI_SELECT_QRY_SWEARCH_WORD = "search_word";

	// 分隔符
	public final static String ADHOC_SPLIT_CHAR = "$$$";

	/** ************************************************************************ */

	public final static String ADHOC_VIEW_STRUCT = "AdhocViewStruct";

	public final static String ADHOC_VIEW_QUERY_STRUCT = "AdhocViewQryStruct";

	public final static String ADHOC_VIEW_SUM = "ADHOC_VIEW_SUM";

	public final static String ADHOC_VIEW_LIST = "ADHOC_VIEW_LIST";

	public final static String ADHOC_VIEW_REPORT = "ADHOC_VIEW_REPORT";

	public final static String ADHOC_VIEW_REPORT_HELPER_ARRAY = "ADHOC_VIEW_REPORT_HELPER_ARRAY";

	public final static String ADHOC_VIEW_REPORT_HELPER_LIST = "ADHOC_VIEW_REPORT_HELPER_LIST";

	public final static String ADHOC_VIEW_DIGIT_UNIT = "ADHOC_VIEW_DIGIT_UNIT";

	// 汇总类型 count
	public final static String CAL_RULE_TYPE_C = "C";
	// 汇总类型 sum
	public final static String CAL_RULE_TYPE_S = "S";

	/**
	 * ********************************视图SESSION
	 * KEY**************************************
	 */

	public final static String ADHOC_VIEW_CONDITION_SESSION_KEY = "ADHOC_VIEW_CONDITION_SESSION_KEY";

	public final static String ADHOC_VIEW_DIM_SESSION_KEY = "ADHOC_VIEW_DIM_SESSION_KEY";

	public final static String ADHOC_VIEW_DIM_EXPAND_SESSION_KEY = "ADHOC_VIEW_DIM_EXPAND_SESSION_KEY";

	public final static String ADHOC_VIEW_MSU_SESSION_KEY = "ADHOC_VIEW_MSU_SESSION_KEY";

	/**
	 * ********************************checkbox
	 * 的名称字段**************************************
	 */
	public final static String ADHOC_CONDITION_CHECK_NAME = "con_check";// 条件名称

	public final static String ADHOC_DIM_CHECK_NAME = "dim_check";// 纬度名称

	public final static String ADHOC_MSU_CHECK_NAME = "msu_check";// 指标名称

	/**
	 * ********************************排序方式*************************************
	 * *
	 */
	/**
	 * 升序
	 */
	public final static String SORT_ASC = "1";

	/**
	 * 降序
	 */
	public final static String SORT_DESC = "2";

	/**
	 * 排序类型（1,数值；2文本）
	 */
	public final static String ADHOC_VIEW_LIST_SORT_TYPE = "sort_type";
	/**
	 * 二维数组排序索引
	 */
	public final static String ADHOC_VIEW_LIST_SORT_INDEX = "sort_idx";

	/**
	 * 排序方式1,asc;2,desc;
	 */
	public final static String ADHOC_VIEW_LIST_ORDER_FLAG = "order_flag";

	/**
	 * 默认纬度值
	 */
	public final static String ADHOC_DIM_DEFAULT_VALUE = "ADHOC_DIM_DEFAULT_VALUE";

	/**
	 * ********************************定制纬度值************************************
	 * **
	 */
	public final static String ADHOC_DIM_FIXED_VALUE = "ADHOC_DIM_FIXED_VALUE";

	public final static String ADHOC_DIM_FIXED_ADHOC_ID = "ADHOC_DIM_FIXED_ADHOC_ID";

	public final static String ADHOC_DIM_FIXED_RELATION_FIELD = "ADHOC_DIM_FIXED_RELATION_FIELD";

	public final static String ADHOC_DIM_FIXED_OPER_NO = "ADHOC_DIM_FIXED_OPER_NO";

	/**
	 * ********************************用户清单*************************************
	 * *
	 */

	public final static String ADHOC_USER_LIST_DEFINE_INFO = "USER_INFO_DEFINE";

	public final static String ADHOC_USER_LIST_VALUE = "USER_INFO_VALUE";
	public final static String ADHOC_QRY_SQL = "ADHOC_QRY_SQL";
	public final static String ADHOC_RECORD_CNT = "ADHOC_RECORD_CNT";

	public final static String ADHOC_EXPORT_ROWCNT = "ADHOC_EXPORT_ROWCNT";

	public final static int ADHOC_USER_LIST_ROW_CONSTRAINT = 10000;

	public final static String ADHOC_USER_DETAIL_FIELD = "ADHOC_USER_DETAIL_FIELD";

	public final static double CONST_EXCEL_RECORDS_LIMITS = 200000;
	public final static double CONST_EXCEL_RUN_47_LIMITS = 70000;

}
