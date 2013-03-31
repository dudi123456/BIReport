package com.ailk.bi.report.util;

/**
 * @author Administrator
 *
 */
public class ReportConsts {
	/** ******************************** 字段值含义定义* */

	/**
	 * 固定报表的默认数据表 UI_RPT_INFO_DATA
	 */
	public final static String RPT_DEFAUTL_DATATABLE = "UI_RPT_INFO_DATA";

	/**
	 * 固定报表的默认数据表的日期字段名 RPT_DATE
	 */
	public final static String RPT_DEFAULT_DATADATE = "RPT_DATE";

	/**
	 * 固定报表的默认数据表的排序字段名 DATA_ID
	 */
	public final static String RPT_SEQUENCE_CODE = "DATA_ID";

	/**
	 * 默认指标体系报表 100
	 */
	public final static String RPTM_DEFAULT_SHOW = "100";

	/**
	 * 默认指标体系报表 101
	 */
	public final static String RPTM_EXPAND_SHOW = "101";

	/**
	 * 默认指标体系报表 102
	 */
	public final static String RPTM_EXPAND_SHOW1 = "102";

	/**
	 * 默认指标体系报表 103
	 */
	public final static String RPTM_EXPAND_SHOW2 = "103";

	/**
	 * 不带合计行的固定报表 200
	 */
	public final static String RPT_NULL_SUMROW = "200";

	/**
	 * 带合计行的固定报表 201
	 */
	public final static String RPT_HAS_SUMROW = "201";

	/**
	 * 专题模式报表
	 */
	public final static String RPT_SUBJECT_SHOW = "209";

	/**
	 * 合计行编码特定值 9999999999
	 */
	public final static String RPT_SUMROW_ID = "9999999999";

	/**
	 * 合计行描述特定值 '合计'
	 */
	public final static String RPT_SUMROW_DESC = "'合计'";

	/**
	 * 小计行编码特定值 99999999
	 */
	public final static String RPT_SUBSUMROW_ID = "99999999";

	/**
	 * 小计行描述特定值 '小计'
	 */
	public final static String RPT_SUBSUMROW_DESC = "'小计'";

	/**
	 * 报表合并列特定值
	 */
	public final static String RPT_EXPAND_VALUE = "-99999999";

	/**
	 * 报表状态-待认证 W
	 */
	public final static String RPT_STATUS_WAIT = "W";

	/**
	 * 常量 0
	 */
	public final static String ZERO = "0";

	/**
	 * 常量 1
	 */
	public final static String FIRST = "1";

	/**
	 * 常量 2
	 */
	public final static String SECOND = "2";

	/**
	 * 常量 3
	 */
	public final static String THIRD = "3";

	/**
	 * 字段数据类型-标识布尔型变量是 Y
	 */
	public final static String YES = "Y";

	/**
	 * 字段数据类型-标识布尔型变量否 N
	 */
	public final static String NO = "N";

	/**
	 * 字段数据类型-数值型 1
	 */
	public final static String DATA_TYPE_NUMBER = "1";

	/**
	 * 字段数据类型-字符串型 2
	 */
	public final static String DATA_TYPE_STRING = "2";

	/**
	 * 数据方向-向上 u
	 */
	public final static String DIRECTION_UP = "u";

	/**
	 * 数据方向-向下 d
	 */
	public final static String DIRECTION_DOWN = "d";

	/**
	 * 显示序号列 1
	 */
	public final static String DIS_SEQUENCE_COL = "1";

	/**
	 * 显示日期列 2
	 */
	public final static String DIS_DATE_COL = "2";

	/**
	 * 显示序号和日期列 3
	 */
	public final static String DIS_ALL_COL = "3";

	/**
	 * 资源条件-统一资源表定义 0
	 */
	public final static String CONDITION_PUB = "0";

	/**
	 * 资源条件-图形 1
	 */
	public final static String CONDITION_CHART = "1";

	/**
	 * 资源条件-表格 2
	 */
	public final static String CONDITION_TABLE = "2";

	/**
	 * 资源条件-整体合并替换 3
	 */
	public final static String CONDITION_PART = "3";

	/**
	 * 条件类型-文本框 T
	 */
	public final static String FILTER_TYPE_TEXT = "T";

	/**
	 * 条件类型-下拉列表框 L
	 */
	public final static String FILTER_TYPE_LIST = "L";

	/**
	 * 条件类型-下拉列表框合并 L
	 */
	public final static String FILTER_TYPE_MLIST = "M";

	/**
	 * 条件类型-单选按钮 R
	 */
	public final static String FILTER_TYPE_RADIO = "R";

	/**
	 * 条件类型-多选按钮 C
	 */
	public final static String FILTER_TYPE_CHECKBOX = "C";

	/**
	 * 条件类型-脚本 S
	 */
	public final static String FILTER_TYPE_SCRIPT = "S";

	/**
	 * 条件数据-来源于SQL语句提取 S
	 */
	public final static String FILTER_DATA_SQL = "S";

	/**
	 * 条件数据-来源于数据表 D
	 */
	public final static String FILTER_DATA_TABLE = "D";

	/**
	 * 条件数据-来源于字符串定义 M
	 */
	public final static String FILTER_DATA_MAP = "M";

	/**
	 * 条件数据-来源于字符串定义 N
	 */
	public final static String FILTER_DATA_NONE = "N";

	/**
	 * 零显示零 0
	 */
	public final static String ZERO_TO_ZERO = "0";

	/**
	 * 零显示空串 1
	 */
	public final static String ZERO_TO_BLANK = "1";

	/**
	 * null值显示为空串 1
	 */
	public final static String NULL_TO_BLANK = "1";

	/**
	 * null值显示为零 2
	 */
	public final static String NULL_TO_ZERO = "2";

	/**
	 * 链接的方式_self不弹出
	 */
	public final static String TARGET_SELF = "_self";

	/**
	 * 链接的方式_blank弹出
	 */
	public final static String TARGET_BLANK = "_blank";

	/**
	 * 显示位置上
	 */
	public final static String CHART_POS_TOP = "T";

	/**
	 * 显示位置下
	 */
	public final static String CHART_POS_BOTTOM = "B";

	/**
	 * 显示位置右
	 */
	public final static String CHART_POS_RIGHT = "R";

	/**
	 * 显示位置左
	 */
	public final static String CHART_POS_LEFT = "L";

	/** ******************************** 字段值含义定义结束* */

	/** ******************************** 报表审核定义* */
	/**
	 * 回退标志 R
	 */
	public final static String RETURN = "R";

	/**
	 * 前行标志 F
	 */
	public final static String FORWARD = "F";

	/**
	 * 步骤 1
	 */
	public final static String STEP_FIRST = "1";

	/**
	 * 终审步骤 E
	 */
	public final static String STEP_FINAL = "E";

	/** ******************************** 报表审核定义结束* */

	/** ******************************** 预警字段定义* */
	/**
	 * 预警和本期值比较 0
	 */
	public final static String ALERT_COMPARE_TO_THIS_PERIOD = "0";

	/**
	 * 预警和同比上期值比较 1
	 */
	public final static String ALERT_COMPARE_TO_SAME_PERIOD = "1";

	/**
	 * 预警和环比上期值比较 2
	 */
	public final static String ALERT_COMPARE_TO_LAST_PERIOD = "2";

	/**
	 * 单元格底色预警 1
	 */
	public final static String ALERT_MODE_TDBGCOLOR = "1";

	/**
	 * 箭头预警 2
	 */
	public final static String ALERT_MODE_ARROW = "2";

	/**
	 * 文字颜色预警 3
	 */
	public final static String ALERT_MODE_FONTCOLOR = "3";

	/**
	 * 预警颜色绿色 G
	 */
	public final static String ALERT_COLOR_GREEN = "G";

	/**
	 * 预警颜色红色 R
	 */
	public final static String ALERT_COLOR_RED = "R";

	/** ******************************** 预警字段定义结束* */

	/** ******************************** 样式定义* */
	/**
	 * 报表标题样式
	 */
	public final static String STYLE_TITLE_OLD = "class=\"tab-boldtitle\"";
	public final static String STYLE_TITLE = "class=\"bigtitle\"";

	/**
	 * 报表tr样式
	 */
	public final static String STYLE_TR_HEAD = "class=\"tl\"";

	/**
	 * 报表表头td样式
	 */
	public final static String STYLE_TD_HEAD = "";

	/**
	 * 报表白色背景tr样式
	 */
	public final static String STYLE_TR_WHITE = "class=\"bgwl\"";

	/**
	 * 报表带背景图tr样式
	 */
	public final static String STYLE_TR_JPG = "class=\"jg\"";

	/**
	 * 报表td样式
	 */
	public final static String STYLE_TD = "";

	/**
	 * 预警底色绿色
	 */
	public final static String STYLE_ALERT_BG_GREEN = "class=\"tabbg-green\"";

	/**
	 * 预警底色红色
	 */
	public final static String STYLE_ALERT_BG_RED = "class=\"tabbg-red\"";

	/**
	 * 预警下降绿箭头
	 */
	public final static String IMG_ALERT_ARROW_DOWN_GREEN = "<img src=\"../images/down_green.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升绿箭头
	 */
	public final static String IMG_ALERT_ARROW_RISE_GREEN = "<img src=\"../images/rise_green.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警下降红箭头
	 */
	public final static String IMG_ALERT_ARROW_DOWN_RED = "<img src=\"../images/down_red.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升红箭头
	 */
	public final static String IMG_ALERT_ARROW_RISE_RED = "<img src=\"../images/rise_red.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/**
	 * 预警上升红箭头
	 */
	public final static String IMG_ALERT_ARROW_BLANK = "<img src=\"../images/blank.gif\" "
			+ "width=\"9\" alt=\"::ALT::\" border=\"0\">";

	/** ******************************** 样式定义结束* */
	/**
	 * 对比对象 dim5
	 */
	public final static String IS_PERIOD_OBJECT = "dim5";
	/**
	 * 日环比 1
	 */
	public final static String DATE_SAME_PERIOD = "1";

	/**
	 * 月环比 2
	 */
	public final static String MONTH_SAME_PERIOD = "2";

	/**
	 * 日同比 3
	 */
	public final static String DATE_LAST_PERIOD = "3";

	/**
	 * 月同比 4
	 */
	public final static String MONTH_LASE_PERIOD = "4";

	/**
	 * 日环比-增幅 4
	 */
	public final static String DATE_SAME_PERIOD_PERC = "4";

	/**
	 * 月环比-增幅 8
	 */
	public final static String MONTH_SAME_PERIOD_PERC = "8";

	/**
	 * 日同比-增幅 12
	 */
	public final static String DATE_LAST_PERIOD_PERC = "12";

	/**
	 * 月同比-增幅 16
	 */
	public final static String MONTH_LASE_PERIOD_PERC = "16";

	/**
	 * 字段是否包含 特殊运算操作
	 *
	 * @param field_code
	 * @return
	 */
	public static boolean hasSpcCalculator(String field_code) {
		if (field_code == null || "".equals(field_code.trim())) {
			return false;
		}
		field_code = field_code.toUpperCase();

		boolean isTrue = false;
		if (field_code.indexOf("SUM(") >= 0 || field_code.indexOf("COUNT(") >= 0
				|| field_code.indexOf("MAX(") >= 0 || field_code.indexOf("MIN(") >= 0) {
			isTrue = true;
		}
		return isTrue;
	}

	/**
	 * 字段是否包含 + - * / 操作
	 *
	 * @param field_code
	 * @return
	 */
	public static boolean hasCalculator(String field_code) {
		if (field_code == null || "".equals(field_code.trim())) {
			return false;
		}

		boolean isTrue = false;
		if (field_code.indexOf("+") >= 0 || field_code.indexOf("-") >= 0
				|| field_code.indexOf("*") >= 0 || field_code.indexOf("/") >= 0) {
			isTrue = true;
		}
		return isTrue;
	}

}
