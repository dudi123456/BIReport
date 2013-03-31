package com.ailk.bi.olap.util;

public class RptOlapConsts {
	/**
	 * 是否有新内容
	 */
	public final static String REQ_HAS_NEW_CONTENT = "has_new_content";
	/**
	 * 清除扩展
	 */
	public final static String REQ_CLEAR_EXPAND_SESS = "clear_expand";
	/**
	 * 对于超常维度值，每行的字符数
	 */
	public final static int DIM_VALUE_ONE_LINE_BYTE = 15;

	/**
	 * 区分G/C两网权限时的大类业务字段
	 */
	public final static String SVC_KND_LVL_FLD = "SVC_KND_LVL";

	/**
	 * 时间为月数据
	 */
	public final static String DATA_TABLE_TIME_MONTH = "M";

	/**
	 * 时间为日数据
	 */
	public final static String DATA_TABLE_TIME_DAY = "D";

	/**
	 * 数据表名
	 */
	public final static String DATA_TABLE_TIME_TABLE_NAME_FLD = "TABLE_NAME";

	/**
	 * 数据表最近时间周期
	 */
	public final static String DATA_TABLE_TIME_PERIOD_FLD = "TIME_TYPE";

	/**
	 * 数据表最近时间字段
	 */
	public final static String DATA_TABLE_TIME_FLD = "GATHER_TIME";

	/**
	 * 数据表最近时间视图
	 */
	public final static String DATA_TABLE_TIME_VIEW = "V_T_ETL_MAX_DATA_TIME";

	/**
	 * 用户控制数据级别为行区
	 */
	public final static String DATA_RIGHT_LEVEL_SECTOR = "5";

	/**
	 * 用户控制数据级别为片区
	 */
	public final static String DATA_RIGHT_LEVEL_DISTRICT = "4";

	/**
	 * 用户控制数据级别为区县
	 */
	public final static String DATA_RIGHT_LEVEL_COUNTY = "3";

	/**
	 * 用户控制数据级别为地市
	 */
	public final static String DATA_RIGHT_LEVEL_CITY = "2";

	/**
	 * 用户控制数据级别为省级
	 */
	public final static String DATA_RIGHT_LEVEL_PROVINCE = "1";

	/**
	 * 用户数据权限为所属区域控制，单区域
	 */
	public final static String DATA_RIGHT_BELONG_AREA = "0";

	/**
	 * 用户数据权限为控制区域,多区域
	 */
	public final static String DATA_RIGHT_CTL_AREA = "1";

	/**
	 * 图形请求变量中的分组维度
	 */
	public final static String REQ_CHART_HIDDEN_DIMGROUP = "hidden_dim_group";

	/**
	 * 图形请求变量中的分组维度层次
	 */
	public final static String REQ_CHART_HIDDEN_DIMGROUPLEVEL = "hidden_dim_group_level";

	/**
	 * 图形请求变量中的分组值是排出还是包括
	 */
	public final static String REQ_CHART_HIDDEN_DIMGROUPINCLUDE = "hidden_dim_group_include";

	/**
	 * 图形请求变量中的分组维度值是否全部
	 */
	public final static String REQ_CHART_HIDDEN_DIMGROUPALL = "hidden_dim_group_all_values";

	/**
	 * 图形请求变量中的分组维度的具体值
	 */
	public final static String REQ_CHART_HIDDEN_DIMGROUPVALUE = "hidden_dim_group_value";

	/**
	 * 图形请求变量中的条件维度
	 */
	public final static String REQ_CHART_HIDDEN_DIMWHERE = "hidden_dim_where";

	/**
	 * 图形请求变量中的条件维度层次前缀
	 */
	public final static String REQ_CHART_HIDDEN_DIMWHERELEVEL = "hidden_dim_where_level_";

	/**
	 * 图形请求变量中的条件维度是否包括或排除
	 */
	public final static String REQ_CHART_HIDDEN_DIMWHEREINCLUDE = "hidden_dim_where_include_";

	/**
	 * 图形请求变量中的条件维度是否全部值
	 */
	public final static String REQ_CHART_HIDDEN_DIMWHEREALL = "hidden_dim_where_all_values_";

	/**
	 * 图形请求变量中的条件维度具体值
	 */
	public final static String REQ_CHART_HIDDEN_DIMWHEREVALUE = "hidden_dim_where_value_";

	/**
	 * 图形请求变量中的指标组
	 */
	public final static String REQ_CHART_HIDDEN_MSUGROUP = "hidden_msu_group";

	/**
	 * 图形分析时的操作
	 */
	public final static String CHART_DIG_ACTION = "digOlapChart.rptdo";

	/**
	 * 获取图形分析的维度值操作
	 */
	public final static String CHART_DIM_VALUES_LOAD = "loadOlapChartDim.rptdo";

	/**
	 * 图形默认设置的指标关键字
	 */
	public final static String CHART_SETTING_MSU = "3";

	/**
	 * 图形默认设置的条件关键字
	 */
	public final static String CHART_SETTING_WHERE = "2";

	/**
	 * 图形默认设置的观察维度关键字
	 */
	public final static String CHART_SETTING_GROUP = "1";

	/**
	 * 时间关联的伪表名
	 */
	public final static String SQL_TIME_JOIN_FLD = "JOIN_TIME";

	/**
	 * 组装SQL中的各期值类型表示
	 */
	public final static String SQL_PERIOD_TYPE = "PERIOD_TYPE";

	/**
	 * 组装SQL中标识本期值的类型
	 */
	public final static String SQL_THIS_PERIOD = "1";

	/**
	 * 组装SQL中表示月同比值的类型
	 */
	public final static String SQL_SAME_PERIOD = "2";

	/**
	 * 组装SQL中表示月周比值的类型
	 */
	public final static String SQL_SAME_WEEK_PERIOD = "3";

	/**
	 * 组装SQL中表示环比值的类型
	 */
	public final static String SQL_LAST_PERIOD = "4";

	/**
	 * 保存用户自定义报表动作
	 */
	public final static String CUSTOM_REPORT_SAVE_ACTION = "saveOlapCusRpt.rptdo";

	/**
	 * 自定义报表展开方式为钻取
	 */
	public final static String CUSTOM_DISPLAY_DIG = "1";

	/**
	 * 自定义报表展现方式为折叠展开
	 */
	public final static String CUSTOM_DISPLAY_EXPAND = "2";

	/**
	 * 用户自定义报表的序列下一值
	 */
	public final static String CUSTOM_REPORT_SEQ_NEXT = "SEQ_RPT_USER_OLAP.nextval";

	/**
	 * 预警下降绿箭头
	 */
	public final static String ALERT_ARROW_DOWN_GREEN_IMG = "<img src=\"../images/down_green.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升绿箭头
	 */
	public final static String ALERT_ARROW_RISE_GREEN_IMG = "<img src=\"../images/rise_green.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警下降红箭头
	 */
	public final static String ALERT_ARROW_DOWN_RED_IMG = "<img src=\"../images/down_red.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升红箭头
	 */
	public final static String ALERT_ARROW_RISE_RED_IMG = "<img src=\"../images/rise_red.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升红箭头
	 */
	public final static String ALERT_ARROW_BLANK_IMG = "<img src=\"../images/blank.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警底色绿色
	 */
	public final static String ALERT_BG_GREEN_CLASS = "tabbg-green";

	/**
	 * 预警底色红色
	 */
	public final static String ALERT_BG_RED_CLASS = "tabbg-red";

	/**
	 * 预警模式-字体颜色
	 */
	public final static String ALERT_MODE_FONTCOLOR = "3";

	/**
	 * 预警模式箭头
	 */
	public final static String ALERT_MODE_ARROW = "2";

	/**
	 * 预警模式－单元格底色
	 */
	public final static String ALERT_MODE_TDBGCOLOR = "1";

	/**
	 * 预警红色颜色
	 */
	public final static String ALERT_RED_COLOR = "R";

	/**
	 * 预警绿色颜色
	 */
	public final static String ALERT_GREEN_COLOR = "G";

	/**
	 * 预警和同比进行比较
	 */
	public final static String ALERT_COMPARE_TO_SAME = "1";

	/**
	 * 预警和环比进行比较
	 */
	public final static String ALERT_COMPARE_TO_LAST = "2";

	/**
	 * 展开图片定义
	 */
	public final static String IMG_EXPAND = "<img "
			+ "src=\"../images/switchs_icon_closed.gif\" "
			+ "width=\"10\" height=\"10\" border=\"0\">";

	/**
	 * 折叠图片定义
	 */
	public final static String IMG_COLLAPSE = "<img "
			+ "src=\"../images/switchs_icon_opened.gif\" "
			+ "width=\"10\" height=\"10\" border=\"0\">";

	public final static String IMG_CANNOT_CAL = "<img "
			+ "src=\"../images/no_result.gif\" "
			+ " width=\"20\" border=\"0\">";
	/**
	 * 按本期值排序
	 */
	public final static String SORT_THIS_PERIOD = "0";

	/**
	 * 按列对象的周比率排序
	 */
	public final static String SORT_THIS_WEEK_RATIO = "1";

	/**
	 * 按列对象的比率进行排序
	 */
	public final static String SORT_THIS_RATIO = "2";

	/**
	 * 按列对象的累计进行排序
	 */
	public final static String SORT_THIS_SUM = "3";

	/**
	 * 用户定制的层层钻取模式
	 */
	public final static String RESET_MODE_DIG = "dig";

	/**
	 * 用户定制的折叠展开模式
	 */
	public final static String RESET_MODE_EXPAND = "expand";

	/**
	 * 折叠展开模式、单维度多层次模式
	 */
	public final static String EXPAND_SIGLE_DIM = "single";

	/**
	 * 折叠展开模式、多维度单层次模式
	 */
	public final static String EXPAND_MULTI_DIM = "multi";

	/**
	 * 折叠、展开各层次的值
	 */
	public final static String REQ_EXPAND_VALUE = "exp_value_";

	/**
	 * 请求中折叠展开的层次
	 */
	public final static String REQ_EXPAND_LEVEL = "expand_level";

	/**
	 * 请求中用户定制的模式
	 */
	public final static String REQ_RESET_MODE = "reset_mode";

	/**
	 * 用户定制中折叠展开模式、单维度多层次模式或多维度单层次模式
	 */
	public final static String REQ_EXPAND_MODE = "expand_mode";

	/**
	 * 请求中的报表标识
	 */
	public final static String REQ_REPORT_ID = "report_id";

	/**
	 * 请求中结束日期
	 */
	public final static String REQ_END_DATE = "end_date";

	/**
	 * 请求中开始日期
	 */
	public final static String REQ_START_DATE = "start_date";

	/**
	 * 请求中排序方向
	 */
	public final static String REQ_SORT_ORDER = "sort_order";

	/**
	 * 请求中是否排序本期值
	 */
	public final static String REQ_SORT_THIS = "sort_this";

	/**
	 * 请求中排序列
	 */
	public final static String REQ_SORT = "sort";

	/**
	 * 请求中的当前功能
	 */
	public final static String REQ_OLAP_FUNC = "cur_func";

	/**
	 * 是否收缩展开的维度
	 */
	public final static String REQ_COLLAPSE_EXPAND = "collapse_expand";

	/**
	 * 维度或指标钻取的JS函数
	 */
	public final static String DIG_DIM_MSU_JS_FUNCTION = "loadNewContent";

	/**
	 * 请求中指标是否可以钻取
	 */
	public final static String REQ_MSU_CAN_DIG_PREFIX = "can_dig_m_";

	/**
	 * 请求中指标的的层次前缀
	 */
	public final static String REQ_MSU_LEVEL_PREFIX = "level_m_";

	/**
	 * 请求中维度是否可以钻取
	 */
	public final static String REQ_DIM_CAN_DIG_PREFIX = "can_dig_d_";

	/**
	 * 请求中维度值的前面部分
	 */
	public final static String REQ_DIM_VALUE_PREFIX = "dim_";

	/**
	 * 请求维度的前面部分
	 */
	public final static String REQ_DIM_LEVEL_PREFIX = "level_d_";

	/**
	 * 维度收到最初状态
	 */
	public final static String REQ_COLLAPSE_DIM_INIT = "collapse_dim_init";

	/**
	 * 收缩的维度索引
	 */
	public final static String REQ_COLLAPSE_DIM = "collapse_dim";

	/**
	 * 指标收缩到最初状态
	 */
	public final static String REQ_COLLAPSE_MSU_INIT = "collapse_msu_init";

	/**
	 * 收缩的指标索引
	 */
	public final static String REQ_COLLAPSE_MSU = "collapse_msu";

	/**
	 * 收缩的指标标识
	 */
	public final static String REQ_COLLAPSE_MSU_ID = "collapse_msu_id";

	/**
	 * 钻取的指标
	 */
	public final static String REQ_CLICK_MSU = "click_msu";

	/**
	 * 钻取的指标标识
	 */
	public final static String REQ_CLICK_MSU_ID = "click_msu_id";

	/**
	 * 钻取的维度
	 */
	public final static String REQ_CLICK_DIM = "click_dim";

	/**
	 * 计算日指标同比时即要计算周同比、还要计算月同比
	 */
	public final static String SAME_RATIO_BOTH_PERIOD = "0";

	/**
	 * 计算日指标同比时只计算周同比
	 */
	public final static String SAME_RATIO_WEEK_PERIOD = "1";

	/**
	 * 计算日指标同比时只计算月同比
	 */
	public final static String SAME_RATIO_MONTH_PERIOD = "2";

	/**
	 * AJAX 表格钻取动作
	 */
	public final static String OLAP_CLEAR_SESSION = "clear_sess";

	/**
	 * AJAX 表格钻取动作
	 */
	public final static String OLAP_DIG_ACTION = "digTableOlap.rptdo";

	/**
	 * 字体最小象素
	 */
	public final static int MIN_FONT_PX = 8;

	/**
	 * 链接的样式
	 */
	public final static String HREF_LINK_CLASS = "bule";

	/**
	 * 表头行样式
	 */
	public final static String TABLE_HEAD_TR_CLASS = "";

	/**
	 * 表头单元格样式
	 */
	public final static String TABLE_HEAD_TD_CLASS = "tab-title3";

	/**
	 * 表头单元格样式
	 */
	public final static String TABLE_HEAD_TD_CLASS2 = "tab-title2";

	/**
	 * 表体行样式
	 */
	public final static String TABLE_BODY_TR_CLASS = "table-white-bg";

	/**
	 * 表体行样式
	 */
	public final static String TABLE_BODY_TR_CLASS2 = "table-white-bg";
	//public final static String TABLE_BODY_TR_CLASS2 = "tab-td2";

	/**
	 * 表体单元格样式
	 */
	public final static String TABLE_BODY_TD_CLASS = "tab-td2";

	/**
	 * 合计行样式
	 */
	public final static String TABLE_SUM_TR_CLASS = "table-total";
	//public final static String TABLE_SUM_TR_CLASS = "allaccount";

	/**
	 * 对齐表格用的表格高度
	 */
	public final static String ALIGN_TABLE_HEIGHT = "300";

	/**
	 * 列按升序排序后的图片
	 */
	public final static String SORT_ASC_IMG = "menu_up.gif";

	/**
	 * 列按降序排序显示的图片
	 */
	public final static String SORT_DESC_IMG = "menu_down.gif";

	/**
	 * 列没有进行排序时的图片
	 */
	public final static String SORT_INIT_IMG = "menu_init.gif";

	/**
	 * 上一层次
	 */
	public final static int LEVEL_PRE = 0;

	/**
	 * 下一层次
	 */
	public final static int LEVEL_NEXT = 1;

	/**
	 * 维度虚表表名前缀
	 */
	public final static String DIM_VIR_TAB_PREFIX = "D";

	/**
	 * 组装查询语句中的临时伪表名
	 */
	public final static String TMP_VIR_TAB_NAME = "M";

	/**
	 * 组装查询语句的本期伪表名
	 */
	public final static String THIS_VIR_TAB_NAME = "A";

	/**
	 * 组装查询语句的时间伪表名
	 */
	public final static String TIME_VIR_TAB_NAME = "B";

	/**
	 * 组装查询语句的本期伪表名
	 */
	public final static String THIS_VIR_TMP_TAB_NAME = "A0";

	/**
	 * 同比周统计周期虚临时表名
	 */
	public final static String SAME_VIR_TMP_TAB_NAME_WEEK = "W0";

	/**
	 * 同比周统计周期虚表名
	 */
	public final static String SAME_VIR_TAB_NAME_WEEK = "W";

	/**
	 * 分析型报表的数据显示功能
	 */
	public final static String OLAP_FUN_DATA = "1";

	/**
	 * 分析型报表的占比功能
	 */
	public final static String OLAP_FUN_PERCENT = "2";

	/**
	 * 分析型报表的同比功能
	 */
	public final static String OLAP_FUN_SAME = "3";

	/**
	 * 分析型报表的环比功能
	 */
	public final static String OLAP_FUN_LAST = "4";

	/**
	 * 分析型报表的趋势图形功能
	 */
	public final static String OLAP_FUN_LINE = "5";

	/**
	 * 分析型报表的对比图形功能
	 */
	public final static String OLAP_FUN_BAR = "6";

	/**
	 * 分析型报表的占比图形功能
	 */
	public final static String OLAP_FUN_PIE = "7";

	/**
	 * 列对象的索引默认标识
	 */
	public final static int NO_INDEX = -1;

	/**
	 * 指标为比率
	 */
	public final static String UNIT_PECENT = "%";

	/**
	 * 表示0的字符串
	 */
	public final static String ZERO_STR = "0";

	/**
	 * 没有展开时的层次标识
	 */
	public final static String NO_DIGGED_LEVEL = "-1";

	/**
	 * 报表统计周期，月报
	 */
	public final static String RPT_STATIC_MONTH_PERIOD = "4";

	/**
	 * 报表统计周期，日报
	 */
	public final static String RPT_STATIC_DAY_PERIOD = "6";

	/**
	 * 处理NULL值，以便正确排序,其中::NULL::将被替换掉
	 */
	public final static String NVL_PROC = "COALESCE(::NULL::,0)";

	/**
	 * MSU的字段分隔符
	 */
	public final static String MSU_SUB_SELECT_SPLIT="~";

	/**
	 * 没有排序
	 */
	public final static String NO_SORT = "NOSORT";

	/**
	 * 升序
	 */
	public final static String SORT_ASC = "ASC";

	/**
	 * 降序
	 */
	public final static String SORT_DESC = "DESC";

	/**
	 * 布尔常量true
	 */
	public final static String TRUE = "true";

	/**
	 * 布尔常量false
	 */
	public final static String FALSE = "false";

	/**
	 * 数据库和多维分析中表示布尔型变量是的字符串
	 */
	public final static String YES = "Y";

	/**
	 * 数据库和多维分析中标识布尔型变量否的字符串
	 */
	public final static String NO = "N";

	/**
	 * 多维分析中按年分析时的显示值
	 */
	public final static String BY_YEAR = "年度";

	/**
	 * 多维分析中按季度分析时的显示值
	 */
	public final static String BY_QUARTER = "季度";

	/**
	 * 多维分析中按月分析时的显示值
	 */
	public final static String BY_MONTH = "月份";

	/**
	 * 多维分析中按旬分析时的显示值
	 */
	public final static String BY_TENDAYS = "旬";

	/**
	 * 多维分析中按周分析时的显示值
	 */
	public final static String BY_WEEK = "周";

	/**
	 * 多维分析中按日分析时的显示值
	 */
	public final static String BY_DAY = "日期";

	/**
	 * 分析型对数据的处理，进行累加,数据库函数，使用时会替换掉::sum
	 */
	public final static String CMD_SUM = "SUM(::SUM::)";

	/**
	 * 月同比日字段
	 */
	public final static String TIME_DIM_SAME_DAY_FLD = "SAME_GATHER_DAY";

	/**
	 * 周同比日字段
	 */
	public final static String TIME_DIM_SAME_WEEK_DAY_FLD = "PRE_WEEK_GATHER_DAY";

	/**
	 * 环比日字段
	 */
	public final static String TIME_DIM_LAST_DAY_FLD = "LAST_GATHER_DAY";

	/**
	 * 同比月字段 modify by jcm 把SAME_GATHER_MONTH 值改为 SAME_GATHER_MON
	 */
	public final static String TIME_DIM_SAME_MONTH_FLD = "SAME_GATHER_MON";

	/**
	 * 环比月字段 modify by jcm 把LAST_GATHER_MONTH 值改为 LAST_GATHER_MON
	 */
	public final static String TIME_DIM_LAST_MONTH_FLD = "LAST_GATHER_MON";

	/**
	 * 表示日期的字段，这个星期是全年的标识，20060101表示2006第一月第一日
	 */
	public final static String TIME_DIM_DAY_FLD = "GATHER_DAY";

	/**
	 * 表示日期的描述字段
	 */
	public final static String TIME_DIM_DAY_DESC_FLD = "DAY_DESC";

	/**
	 * 表示日期星期的字段，这个星期是全年的标识，200601表示2006第一周
	 */
	public final static String TIME_DIM_WEEK_FLD = "WEEK_ID";

	/**
	 * 表示日期星期的描述字段
	 */
	public final static String TIME_DIM_WEEK_DESC_FLD = "WEEK_DESC";

	/**
	 * 表示日期旬的字段，格式如20060101，表示2006年1月份上旬
	 */
	public final static String TIME_DIM_TENDAYS_FLD = "TENGATHER_DAY";

	/**
	 * 表示日期旬的描述字段
	 */
	public final static String TIME_DIM_TENDAYS_DESC_FLD = "TENDAY_DESC";

	/**
	 * 表示日期月份的字段，格式200601表示2006年2月份
	 */
	public final static String TIME_DIM_MONTH_FLD = "MONTH_ID";

	/**
	 * 表示日期月份的描述字段
	 */
	public final static String TIME_DIM_MONTH_DESC_FLD = "MONTH_DESC";

	/**
	 * 表示日期季度的字段，格式200601表示2006年第一季度
	 */
	public final static String TIME_DIM_QUARTER_FLD = "QUARTER_ID";

	/**
	 * 表示日期季度的描述字段
	 */
	public final static String TIME_DIM_QUARTER_DESC_FLD = "QUARTER_DESC";

	/**
	 * 表示日期年的字段，格式2006表示2006年
	 */
	public final static String TIME_DIM_YEAR_FLD = "YEAR_ID";

	/**
	 * 表示日期年的描述字段，
	 */
	public final static String TIME_DIM_YEAR_DESC_FLD = "YEAR_DESC";

	/**
	 * 按年统计时层次定义为1
	 */
	public final static String DIM_TIME_YEAR_LEVEL = "1";

	/**
	 * 按季度统计时层次定义为2
	 */
	public final static String DIM_TIME_QUARTER_LEVEL = "2";

	/**
	 * 按月统计时层次定义为3
	 */
	public final static String DIM_TIME_MONTH_LEVEL = "3";

	/**
	 * 按旬统计时层次定义为4
	 */
	public final static String DIM_TIME_TENDAYS_LEVEL = "4";

	/**
	 * 按周统计时层次定义为5
	 */
	public final static String DIM_TIME_WEEK_LEVEL = "5";

	/**
	 * 按日统计时层次定义为6
	 */
	public final static String DIM_TIME_DAY_LEVEL = "6";

	/**
	 * 字段的数值类型
	 */
	public final static String FLD_NUMBER_TYPE = "1";

	/**
	 * 字段为字符串类型
	 */
	public final static String FLD_STRING_TYPE = "2";

	/**
	 * 维度不能进行下一层钻取
	 */
	public final static String NO_NEXT_LEVEL = "99";

	/**
	 * 纵向占比分析类型
	 */
	public final static String RATIO_COMP = "0";

	/**
	 * 同比分析类型
	 */
	public final static String RATIO_SAME = "1";

	/**
	 * 环比分析类型
	 */
	public final static String RATIO_LAST = "2";

	/**
	 * 平面饼状图形
	 */
	public final static String PIE_CHART_FLAT = "1";

	/**
	 * FLASH饼状图形
	 */
	public final static String PIE_CHART_FLAT_FLASH = "Pie3D";

	/**
	 * 生成立体饼状图形
	 */
	public final static String PIE_CHART_3D = "2";

	/**
	 * 生成指环状饼图形
	 */
	public final static String PIE_CHART_RING = "3";

	/**
	 * 生成平面组合饼状图形
	 */
	public final static String PIE_CHART_STACKED_FLAT = "4";

	/**
	 * 生成立体组合饼状图形
	 */
	public final static String PIE_CHART_STACKED_3D = "5";

	/**
	 * 生成平面簇状柱形图
	 */
	public final static String BAR_CHART_FLAT = "6";

	/**
	 * 生成FLASH平面簇状柱形图
	 */
	public final static String BAR_CHART_FLAT_FALSH = "MSColumn2D";

	/**
	 * 生成立体簇状柱形图
	 */
	public final static String BAR_CHART_3D = "7";

	/**
	 * 生成平面堆积柱形图
	 */
	public final static String BAR_CHART_STACKED_FLAT = "8";

	/**
	 * 生成立体堆积柱形图
	 */
	public final static String BAR_CHART_STACKED_3D = "9";

	/**
	 * 生成平面面积图
	 */
	public final static String AREA_CHART_FLAT = "10";

	/**
	 * 生成堆积面积图
	 */
	public final static String AREA_CHART_STACKED_FLAT = "11";

	/**
	 * 生成平面折线图
	 */
	public final static String LINE_CHART_FLAT = "12";

	/**
	 * 生成FLASH平面折线图
	 */
	public final static String LINE_CHART_FLAT_FLASH = "MSLine";

	/**
	 * 生成立体折线图
	 */
	public final static String LINE_CHART_3D = "13";

	/**
	 * 生成雷达图
	 */
	public final static String SPIDER_CHART = "14";

	/**
	 * 生成极面图
	 */
	public final static String POLAR_CHART = "15";

	/**
	 * 生成瀑布图
	 */
	public final static String WATER_FALL_CHART = "16";

	/**
	 * 生成气泡图
	 */
	public final static String BUBBLE_CHART = "17";

	/**
	 * 生成平面簇状折线图
	 */
	public final static String BAR_LINE_CHART_FALT = "18";

	/**
	 * 生成立体簇状折线图
	 */
	public final static String BAR_LINE_CHART_3D = "19";

	/**
	 * 生成平面堆积折线图
	 */
	public final static String BAR_STACKED_LINE_CHART_FlAT = "20";

	/**
	 * 生成立体堆积折线图
	 */
	public final static String BAR_STACKED_LINE_CHART_3D = "21";

}
