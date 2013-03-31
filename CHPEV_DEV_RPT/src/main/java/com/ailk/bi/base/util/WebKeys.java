package com.ailk.bi.base.util;

import java.io.Serializable;

/**
 * 存放session属性名字的常量定义 命名规则 ATTR_表名(s) 如果为复数(数组或vector)则后面要加s
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class WebKeys implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public WebKeys() {
	}

	// 查询结构
	public static final String ATTR_LsbiQryStruct = "ATTR_LsbiQryStruct";

	public static final String ATTR_MonitorQryStruct = "ATTR_MonitorQryStruct";

	/** 系统资源标识 * */
	public static final String ATTR_PAGEOBJID = "ATTR_PAGEOBJID";

	public static final String ATTR_ANYFLAG = "ATTR_ANYFLAG";

	public static final String ATTR_INNET_WAY_QRY = "ATTR_INNET_WAY_QRY";

	/** 如果一下的模块调用以上的某个相同的部分，必须在该段描述中加上一个说明 * */
	/** 系统菜单开始 * */
	public static final String ATTR_EXPLAME = "ATTR_EXPLAME";

	public static final String ATTR_PARENTSCREEN = "ATTR_PARENTSCREEN";

	/** 系统菜单结束 * */

	/** 系统公用开始 * */
	public static final String ATTR_FAV_TABLES = "ATTR_FAV_TABLES";

	public static final String ATTR_NEWS_LISTS = "ATTR_NEWS_LISTS";

	public static final String ATTR_NEWS_INFO = "ATTR_NEWS_INFO";

	public static final String ATTR_MAINPAGE_AREA_ID = "ATTR_MAINPAGE_AREA_ID";

	public static final String ATTR_MAINPAGE_CTRL_LVL = "ATTR_MAINPAGE_CTRL_LVL";

	/** 系统公用结束 * */

	/** 系统报表中心开始 * */
	public static final String ATTR_REPORT_TITLE_HTML = "ATTR_REPORT_TITLE_HTML";

	public static final String ATTR_REPORT_BODY_HTML = "ATTR_REPORT_BODY_HTML";

	public static final String ATTR_REPORT_PROCESS_HTML = "ATTR_REPORT_PROCESS_HTML";

	public static final String ATTR_REPORT_QRYSTRUCT = "ATTR_REPORT_QRYSTRUCT";

	public static final String ATTR_REPORT_TABLE = "ATTR_REPORT_TABLE";

	public static final String ATTR_REPORT_TABLE_DEFINE = "ATTR_REPORT_TABLE_DEFINE";

	public static final String ATTR_REPORT_TABLES = "ATTR_REPORT_TABLES";

	public static final String ATTR_REPORT_COLS = "ATTR_REPORT_COLS";

	public static final String ATTR_REPORT_COLS_DEFINE = "ATTR_REPORT_COLS_DEFINE";

	public static final String ATTR_REPORT_FILTERS = "ATTR_REPORT_FILTERS";

	public static final String ATTR_REPORT_FILTERS_DEFINE = "ATTR_REPORT_FILTERS_DEFINE";

	public static final String ATTR_REPORT_FILTERS_VALUEALL = "ATTR_REPORT_FILTERS_VALUEALL";

	public static final String ATTR_REPORT_CONDITION_TABLES = "ATTR_REPORT_CONDITION_TABLES";

	public static final String ATTR_REPORT_STEP = "ATTR_REPORT_STEP";

	public static final String ATTR_REPORT_SMS = "ATTR_REPORT_SMS";

	public static final String ATTR_REPORT_PROCESS = "ATTR_REPORT_PROCESS";

	public static final String ATTR_REPORT_PROCESSES = "ATTR_REPORT_PROCESSES";

	public static final String ATTR_REPORT_PROCESS_STEP = "ATTR_REPORT_PROCESS_STEP";

	public static final String ATTR_REPORT_PROCESS_RELT = "ATTR_REPORT_PROCESS_RELT";

	public static final String ATTR_REPORT_HEAD_CONTENT = "ATTR_REPORT_HEAD_CONTENT";

	public static final String ATTR_REPORT_CUSTOM_MSU_TOKEN = "ATTR_REPORT_CUSTOM_MSU_TOKEN";

	public static final String ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD = "ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD";

	public static final String ATTR_REPORT_RESOURCE_DEFINE = "ATTR_REPORT_RESOURCE_DEFINE";

	public static final String ATTR_REPORT_BATCH_EXPORT = "ATTR_REPORT_BATCH_EXPORT";

	public static final String ATTR_REPORT_BATCH_IMPORT = "ATTR_REPORT_BATCH_IMPORT";

	public static final String ATTR_REPORT_DISPENSE_ROLE_ID = "ATTR_REPORT_DISPENSE_ROLE_ID";

	public static final String ATTR_REPORT_DISPENSE_ROLE_NAME = "ATTR_REPORT_DISPENSE_ROLE_NAME";

	public static final String ATTR_REPORT_CHARTS = "ATTR_REPORT_CHARTS";

	public static final String ATTR_REPORT_CHART_FUN = "ATTR_REPORT_CHART_FUN";

	/** 系统报表中心结束 * */

	/** 系统分析性报表开始 * */
	public static final String ATTR_OLAP_REPORT_ID = "ATTR_OLAP_REPORT_ID";

	public static final String ATTR_OLAP_REPORT_OBJ = "ATTR_OLAP_REPORT_OBJ";

	public static final String ATTR_OLAP_TABLE_PRE = "ATTR_OLAP_TABLE_PRE";

	public static final String ATTR_OLAP_TABLE_PRE_BODY = "ATTR_OLAP_TABLE_PRE_BODY";

	public static final String ATTR_OLAP_TABLE_DOMAINS_OBJ = "ATTR_OLAP_TABLE_DOMAINS_OBJ";

	public static final String ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ = "ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ";

	public static final String ATTR_OLAP_CHART_DOMAINS_OBJ = "ATTR_OLAP_CHART_DOMAINS_OBJ";

	public static final String ATTR_OLAP_CUR_FUNC_OBJ = "ATTR_OLAP_CUR_FUNC_OBJ";

	public static final String ATTR_OLAP_DATE_OBJ = "ATTR_OLAP_DATE_OBJ";

	public static final String ATTR_OLAP_RPT_DIMS = "ATTR_OLAP_RPT_DIMS";

	public static final String ATTR_OLAP_RPT_MSUS = "ATTR_OLAP_RPT_MSUS";

	public static final String ATTR_OLAP_RPT_PRE_MSUS = "ATTR_OLAP_RPT_PRE_MSUS";

	public static final String ATTR_OLAP_CHART_DIM = "ATTR_OLAP_CHART_DIM";

	public static final String ATTR_OLAP_CHART_DIM_VALUES = "ATTR_OLAP_CHART_DIM_VALUES";

	public static final String ATTR_OLAP_CHART_DIMS_VALUES = "ATTR_OLAP_CHART_DIMS_VALUES";

	public static final String ATTR_OLAP_CHART_DIM_JS_OBJECT = "ATTR_OLAP_CHART_DIM_JS_OBJECT";

	public static final String ATTR_OLAP_FIR_URL = "ATTR_OLAP_FIR_URL";

	public static final String ATTR_OLAP_TABLE_CONTENT = "ATTR_OLAP_TABLE_CONTENT";

	public static final String ATTR_OLAP_ROW_CONTENT = "ATTR_OLAP_ROW_CONTENT";

	public static final String ATTR_OLAP_ROW_EXP_CONTENT = "ATTR_OLAP_ROW_EXP_CONTENT";

	public static final String ATTR_OLAP_TABLE_HTML = "ATTR_OLAP_TABLE_HTML";

	public static final String ATTR_OLAP_TABLE_EXPORT = "ATTR_OLAP_TABLE_EXPORT";

	public static final String ATTR_OLAP_USER_REPORTS = "ATTR_OLAP_USER_REPORTS";

	/** 系统分析性报表结束 * */

	/** 系统即席查询报表开始 * */
	public static final String ATTR_MainPage6 = "MainPage6"; // 今日视点

	// public static final String ATTR_anyFlag = "ATTR_anyFlag";

	public static final String ATTR_Innet_way_Qry = "ATTR_Innet_way_Qry";

	public static final String ATTR_CustViewQryStruct = "ATTR_CustViewQryStruct";

	public static final String ATTR_CustViewQryList = "ATTR_CustViewQryList";

	public static final String ATTR_CustViewQryData = "ATTR_CustViewQryData";

	public static final String ATTR_CustViewQryArr = "ATTR_CustViewQryArr";

	public static final String ATTR_Unionview_Condition = "front.UnionCondition";

	public static final String ATTR_Unionview_DuLiangCont = "front.UnionDL";

	public static final String ATTR_Unionview_WeiDuContent = "front.UnionWD";

	public static final String ATTR_Unionview_StructContent = "front.UnionviewTable";

	public static final String ATTR_Unionview_Condition_value = "front.UnionConditionValue";

	public static final String ATTR_Unionview_conStr = "front.UnionView_conStr";

	public static final String ATTR_Unionview_DuLiang = "front.UnionDL_select";

	public static final String ATTR_Unionview_WeiDu = "front.UnionWD_select";

	public static final String ATTR_Unionview_sqlListAry = "front.sqlListAry";

	public static final String ATTR_Unionview_headStr = "front.Union_headStr";

	public static final String ATTR_Unionview_sqlStd = "front.Union_sqlStd";

	public static final String ATTR_Unionview_sqlGrp = "front.Union_sqlGrp";

	public static final String ATTR_Unionview_sqlOdr = "front.Union_sqlOdr";

	public static final String ATTR_Unionview_AllconStr = "front.UnionView_AllconStr";

	public static final String ATTR_Unionview_dispPage = "front.UnionView_dispPage";

	public static final String ATTR_Unionview_secPage = "front.UnionView_secPage";

	public static final String ATTR_Unionview_mulitselect = "front.mulitselect";

	public static final String ATTR_Unionview_favorite = "front.favorite";

	public static final String ATTR_Unionview_favoriteid = "front.favoriteid";

	public static final String ATTR_Unionview_favoriteDisName = "front.favoriteDisName";

	public static final String ATTR_Unionview_sectype = "front.sectype";

	public static final String ATTR_Unionview_favoritename = "front.favoritename";

	public static final String ATTR_Unionview_SelectName = "ATTR_Unionview_SelectName";

	public static final String ATTR_Unionview_MulitSelectId = "ATTR_Unionview_MulitSelectId";

	public static final String ATTR_CustViewMultiValues = "ATTR_CustViewMultiValues";

	public static final String ATTR_CustViewSelectID = "ATTR_CustViewSelectID";

	public static final String ATTR_SPLIT_CHAR = "$$$";

	public static final String ATTR_HiCodeSelectId = "HiCodeSelectId_dnner";

	public static final String ATTR_HiPageRows = "ATTR_HiPageRows";

	public static final String ATTR_UserListDef = "ATTR_UserListDef";

	public static final String ATTR_UserListData = "ATTR_UserListData";

	public static final String ATTR_HiCurRow = "ATTR_HiCurRow";

	public static final String ATTR_DLUnitInfo = "ATTR_DLUnitInfo";

	public static final String ATTR_Default_User_Lvl = "ATTR_Default_User_Lvl";

	/** 系统即席查询报表结束 * */

	/** 统一客户视图查询开始 * */
	public static final String ATTR_Union_CustView = "ATTR_Union_CustView";

	public static final String ATTR_Union_CustDetail = "ATTR_Union_CustDetail";

	public static final String ATTR_Union_cf = "ATTR_Union_Cf";

	public static final String ATTR_Union_Measure_desc = "ATTR_Union_Measure_desc";

	public static final String ATTR_Union_ProductChange = "ATTR_Union_ProductChange";

	public static final String ATTR_Union_StatueChange = "ATTR_Union_StatueChange";

	public static final String ATTR_Union_CallDetail = "ATTR_Union_CallDetail";

	/** 统一客户视图查询结束 * */

	/** 即时搜索查询开始* */
	public static final String ATTR_SearchInfoTable = "ATTR_SearchInfoTable";

	/** 即时搜索查询结束* */

	/** 系统KPI报表开始 * */
	public static final String ATTR_KpiDayMeasureList = "ATTR_KpiDayMeasureList";

	public static final String ATTR_KpiDayMeasureDetail = "ATTR_KpiDayMeasureDetail";

	public static final String ATTR_KpiDateForChart = "ATTR_KpiDateForChart";

	public static final String ATTR_KpiMeasureDetail = "ATTR_KpiMeasureDetail";

	public static final String ATTR_KpiCharacter = "ATTR_KpiCharacter";

	public static final String ATTR_KpiTaskList = "ATTR_KpiTaskList";

	public static final String ATTR_KpiTaskSum = "ATTR_KpiTaskSum";

	/** 系统KPI报表结束 * */

	/** 系统专题分析开始 * */
	/** 系统专题分析结束 * */

	/** 系统管理分析开始 * */

	/** 系统管理之ETL监控开始 * */
	public static final String ATTR_TSysEtlLogTable = "ATTR_TSysEtlLogTable"; // ETL

	public static final String ATTR_TSysEtlLogTable_A = "ATTR_TSysEtlLogTable_A"; // ETL

	public static final String ATTR_TSysEtlLogTable_B = "ATTR_TSysEtlLogTable_B"; // ETL

	/** 系统管理之ETL监控结束 * */

	/** 系统管理之日志监控开始 * */
	public static final String ATTR_UiSysLogTable = "ATTR_UiSysLogTable"; // LOG

	public static final String ATTR_UiSysLogTable_A = "ATTR_UiSysLogTable_A"; // LOG

	public static final String ATTR_UiSysLogTable_B = "ATTR_UiSysLogTable_B"; // LOG

	public static final String ATTR_InfoOperTable = "ATTR_InfoOperTable"; // LOG

	public static final String ATTR_InfoOperTable_A = "ATTR_InfoOperTable_A"; // LOG

	public static final String ATTR_InfoOperTable_B = "ATTR_InfoOperTable_B"; // LOG

	public static final String ATTR_UiSysInfoBackTable = "ATTR_UiSysInfoBackTable"; // BACK

	public static final String ATTR_UiSysInfoBackTable_B = "ATTR_UiSysInfoBackTable_B";// BACK

	public static final String ATTR_UiSysInfoRebackTable = "ATTR_UiSysInfoRebackTable";// REBACK

	public static final String ATTR_LsbiQryStruct_POP = "ATTR_LsbiQryStruct_POP";

	public static final String ATTR_UiSysLogTable_POP_LIST = "ATTR_UiSysLogTable_POP_LIST";

	public static final String ATTR_UiSysLogTable_POP_PIC = "ATTR_UiSysLogTable_POP_PIC";

	public static final String ATTR_UiSysLogTable_POP_Single = "ATTR_UiSysLogTable_POP_Single";

	public static final String ATTR_UiSysLogTable_POP_Single_Top = "ATTR_UiSysLogTable_POP_Single_Top";

	public static final String ATTR_UiSysLogTable_POP_Single_Bop = "ATTR_UiSysLogTable_POP_Single_Bop";

	public static final String ATTR_UiSysLogTable_PIC_LIST = "ATTR_UiSysLogTable_PIC_LIST";

	public static final String ATTR_UploadFileList = "ATTR_UploadFileList"; // upload

	// files

	/** 系统管理之日志监控开始 * */

	public static final String ATTR_ParamMonitorStruct = "ATTR_ParamMonitorStruct"; // 系统参数监控

	/** 系统管理分析结束 * */
	// ---通用表格部分开始---
	public static final String ATTR_SUBJECT_QUERY_STRUCT = "ATTR_SUBJECT_QUERY_STRUCT";

	public static final String ATTR_SUBJECT_TABLE_UTIL = "ATTR_SUBJECT_TABLE_UTIL";

	public static final String ATTR_SUBJECT_TABLE_EXPORT = "ATTR_SUBJECT_TABLE_EXPORT";

	// ---通用表格部分结束---

	/** 系统专题通用分析开始 * */
	public static final String ATTR_SUBJECT_COMMON_TABLE_OBJ = "ATTR_SUBJECT_COMMON_TABLE_OBJ";

	public static final String ATTR_SUBJECT_COMMON_FUNC_OBJ = "ATTR_SUBJECT_COMMON_FUNC_OBJ";

	public static final String ATTR_SUBJECT_COMMON_BODY_LIST = "ATTR_SUBJECT_COMMON_BODY_LIST";

	public static final String ATTR_SUBJECT_COMMON_BODY_SVCES = "ATTR_SUBJECT_COMMON_BODY_SVCES";

	public static final String ATTR_SUBJECT_COMMON_HTML = "ATTR_SUBJECT_COMMON_HTML";

	public static final String ATTR_SUBJECT_COMMON_EXPORT = "ATTR_SUBJECT_COMMON_EXPORT";

	/** 领导看板分析开始 * */
	public static final String ATTR_ailk_QRY_STRUCT = "ATTR_ailk_QRY_STRUCT";

	public static final String ATTR_LEADER_KPI_INFO_STRUCT = "ATTR_LEADER_KPI_INFO_STRUCT";

	public static final String ATTR_LEADER_KPI_INFO_STRUCT_FIRST = "ATTR_LEADER_KPI_INFO_STRUCT_FIRST";

	/** 领导看板分析结束 * */

	public static final String ATTR_SUBJECT_MONITOR_PORTAL_VIEW_DATA = "ATTR_SUBJECT_MONITOR_PORTAL_VIEW_DATA";

	public static final String ATTR_SUBJECT_PRODUCT_PLAN_INFO = "ATTR_SUBJECT_PRODUCT_PLAN_INFO";

	public static final String ATTR_SUBJECT_PRODUCT_PLAN_MUTI_INFO = "ATTR_SUBJECT_PRODUCT_PLAN_MUTI_INFO";

	// 屏幕可用宽度
	public static final String Screenx = "Screenx";

	// 屏幕可用高度
	public static final String Screeny = "Screeny";

	public static final String ATTR_AdhocCheckStruct = "ATTR_AdhocCheckStruct";

	public static final String ATTR_AdhocCheckStruct_ObjType = "ATTR_AdhocCheckStruct_ObjType";

	public static final String ATTR_AdhocCheckStruct_Rule = "ATTR_AdhocCheckStruct_RULE";

	public static final String ATTR_REPORT_MEASURE_RULE_DIM = "ATTR_REPORT_MEASURE_RULE_DIM";

	public static final String ATTR_GROUP_TEMPLATE_LIST = "ATTR_GROUP_TEMPLATE_LIST";

	/***************************************************************************************/
	/******************** 数据视窗（北京电信现场应用20100120） ********************************/
	/***************************************************************************************/
	public static final String ATTR_UI_SYS_INFO_DATA_STORE = "UI_SYS_INFO_DATA_STORE";

	public static final String ATTR_UPLAOD_WIN_STORE_DATE = "UPLAOD_WIN_STORE_DATE";

	public static final String ATTR_UPLAOD_WIN_STORE_HTML = "ATTR_UPLAOD_WIN_STORE_HTML";

	/***************************************************************************************/
	/******************** 竞争对手专题（北京电信现场应用20100520） ****************************/
	/***************************************************************************************/

	public static final String ATTR_OPP_SUBJECT_PARAM_LVL_lIST = "ATTR_OPP_SUBJECT_PARAM_LVL_lIST";

	public static final String ATTR_OPP_SUBJECT_STABLE_lIST = "ATTR_OPP_SUBJECT_STABLE_lIST";

	public static final String ATTR_OPP_SUBJECT_lIST_PARAM = "ATTR_OPP_SUBJECT_STABLE_lIST_PARAM";

	public static final String ATTR_OPP_SUBJECT_lIST_SQL = "ATTR_OPP_SUBJECT_lIST_SQL";

	public static final String ATTR_OPP_SUBJECT_lIST_TABLE_TITLE = "ATTR_OPP_SUBJECT_lIST_TABLE_TITLE";

	public static final String ATTR_OPP_SUBJECT_VALUE_lIST = "ATTR_OPP_SUBJECT_VALUE_lIST";

	public static final String ATTR_OPP_SUBJECT_FCF_lIST = "ATTR_OPP_SUBJECT_FCF_lIST";

	public static final String ATTR_OPP_SUBJECT_CF_lIST = "ATTR_OPP_SUBJECT_CF_lIST";

	//评分指标及权重
	public static final String ATTR_ST_AREA_SEGMENT_REFLECT = "ATTR_ST_AREA_SEGMENT_REFLECT";

}