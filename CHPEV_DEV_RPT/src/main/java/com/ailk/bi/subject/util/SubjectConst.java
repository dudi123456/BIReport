package com.ailk.bi.subject.util;

/**
 * @author XDOU 专题通用表格模版常量
 */
public class SubjectConst {
	/**
	 * MSU的字段分隔符
	 */
	public final static String MSU_SUB_SELECT_SPLIT = "~";
	/**
	 * 维度目标链接处理的JS
	 */
	public final static String DIM_LINK_JS = "linkProcess";

	/**
	 * 要被替换的列样式
	 */
	public final static String TD_CLASS_REPLACE = "::tdClass::";

	/**
	 * 要被替换的行样式
	 */
	public final static String TR_CLASS_REPLACE = "::trClass::";

	/**
	 * 字体最小占10个象素
	 */
	public final static int MIN_FONT_PX = 8;

	/**
	 * 行点击处理的JS
	 */
	public final static String ROW_CLICK_JS = "changeTRBgColor('::row_id::');";
	/**
	 * 点击表格图形联动
	 */
	public final static String CHART_CHANGE_JS = "subjectchartchange('::CHART_IDS::','::dim_state::','::CHART_IDS::',true);";

	public final static String TABLE_CHANGE_JS = "subjecttablechange('::CHART_IDS::','::dim_state::','::CHART_IDS::',true);";

	public final static String TABLE_ROW_ROWID = "::row_id::";

	/**
	 * AJAX请求JAVASCRIPT行数名字
	 */
	public final static String AJAX_REQUEST_JS_FUNCTION = "loadNewContent('::LINK::')";

	/**
	 * 指标提示信息
	 */
	public final static String MSU_TIP = "::MSU_TIP::";

	/**
	 * 是否第一次访问表格
	 */
	public final static String REQ_FIRST_ACCESS = "first";

	/**
	 * 矩阵过滤时传进来的操作符号，传值
	 */
	public final static char FILTER_OPERATOR_VALUE = 'G';

	/**
	 * 矩阵过滤时传进来的操作符号，传比率
	 */
	public final static char FILTER_OPERATOR_RATION = 'D';

	/**
	 * 矩阵过滤时传进来的操作符号，传占比
	 */
	public final static char FILTER_OPERATOR_COMPOSTION = 'C';

	/**
	 * 只显示占比符号
	 */
	public final static String COMPOSTION = "C";

	/**
	 * AJAX请求模式的参数，区分传统模式
	 */
	public final static String REQ_AJAX_REQUEST = "ajax_request";

	/**
	 * 查询条件直接替换
	 */
	public final static String PUB_INFO_QUERY_TYPE_REPLACE = "2";

	/**
	 * 统一条件表的表格资源类型
	 */
	public final static String PUB_INFO_RES_TABLE_TYPE = "2";

	/**
	 * 过滤中列对象索引标识
	 */
	public final static String REQ_FILTER_INDEXS = "filter_indexs";

	/**
	 * 过滤中的层次水平
	 */
	public final static String REQ_FILTER_LEVEL = "filter_level";

	/**
	 * 过滤的值
	 */
	public final static String REQ_FILTER_VALUES = "filter_values";

	/**
	 * 过滤时是否是最大值过滤
	 */
	public final static String REQ_FILTER_IS_MAX = "filter_is_max";

	/**
	 * 过滤时是否是最小值过滤
	 */
	public final static String REQ_FILTER_IS_MIN = "filter_is_min";

	/**
	 * 请求对象中偶数行的样式参数
	 */
	public final static String REQ_STYLE_EVEN_TR_CLASS = "even_tr_class";

	/**
	 * 请求对象中奇数行的样式参数
	 */
	public final static String REQ_STYLE_ODD_TR_CLASS = "odd_tr_class";

	/**
	 * 请求对象中表头的行样式参数
	 */
	public final static String REQ_STYLE_HEAD_TR_CLASS = "head_tr_class";

	/**
	 * 请求对象中偶数行单元格样式参数
	 */
	public final static String REQ_STYLE_EVEN_TD_CLASS = "even_td_class";

	/**
	 * 请求对象中奇数行单元格样式参数
	 */
	public final static String REQ_STYLE_ODD_TD_CLASS = "odd_td_class";

	/**
	 * 请求对象中表头的单元格样式参数
	 */
	public final static String REQ_STYLE_HEAD_TD_CLASS = "head_td_class";

	/**
	 * 请求对象中行的其余属性参数
	 */
	public final static String REQ_STYLE_TR_REST = "tr_rest";

	/**
	 * 请求对象中链接样式参数
	 */
	public final static String REQ_STYLE_HREF_CLASS = "href_class";

	/**
	 * 请求对象中图形路径参数
	 */
	public final static String REQ_IMG_PATH = "img_path";

	/**
	 * 请求对象中收缩图形参数
	 */
	public final static String REQ_COLLAPSE_IMG = "collapse_gif";

	/**
	 * 请求对象中扩展图形参数
	 */
	public final static String REQ_EXPAND_IMG = "expand_gif";

	/**
	 * 请求对象中比率下降绿色图象参数
	 */
	public final static String REQ_RATIO_DOWN_GREEN_IMG = "ratio_down_green_gif";

	/**
	 * 请求对象中比率下降红色图象参数
	 */
	public final static String REQ_RATIO_DOWN_RED_IMG = "ratio_down_red_gif";

	/**
	 * 请求对象中比率上升绿色图象参数
	 */
	public final static String REQ_RATIO_RISE_GREEN_IMG = "ratio_rise_green_gif";

	/**
	 * 请求对象中比率上升红色图象参数
	 */
	public final static String REQ_RATIO_RISE_RED_IMG = "ratio_rise_red_gif";

	/**
	 * 请求对象中比率不变图象参数
	 */
	public final static String REQ_RATIO_ZERO_IMG = "ratio_zero_gif";

	/**
	 * 请求对象中表格的高度参数
	 */
	public final static String REQ_TABLE_HEIGHT = "table_height";

	/**
	 * 分页情况下每页的行数
	 */
	public final static int ROWS_PER_PAGE = 20;

	/**
	 * 跨行替换字符串
	 */
	public final static String ROW_SPAN = "::rowspan::";

	/**
	 * 最小缩进空格数
	 */
	public final static String INDENT_SPACE = "&nbsp;&nbsp;";

	/**
	 * 行结构标识层次水平前缀
	 */
	public final static String ROWID_EXP_LEVEL_PREFIX = "level_";

	/**
	 * 行结构标识维度前缀
	 */
	public final static String ROWID_DIM_PREFIX = "dim_";

	/**
	 * 同比字符串
	 */
	public final static String RATIO_TYPE_LAST = "last";

	/**
	 * 环比字符串
	 */
	public final static String RATIO_TYPE_LOOP = "loop";

	/**
	 * 请求中指标字段参数
	 */
	public final static String REQ_MSU_FLD = "msu_fld";

	/**
	 * 请求中指标名称参数
	 */
	public final static String REQ_MSU_NAME = "msu_name";

	/**
	 * 链接中维度状态替换字符串
	 */
	public final static String LINK_DIM_STATE = "::dim_state::";

	/**
	 * 提示中维度名称替换字符串
	 */
	public final static String TIP_DIM_NAME = "::DIM_NAME::";

	/**
	 * 表格行结构的左侧行标识替换字符串
	 */
	public final static String TABLE_ROW_LEFT_ROWID = "::L_ROW_ID::";

	/**
	 * 表格行结构的右侧行标识替换字符串
	 */
	public final static String TABLE_ROW_RIGHT_ROWID = "::R_ROW_ID::";

	/**
	 * 请求对象中表格功能参数
	 */
	public final static String REQ_TABLE_FUNC = "table_func";

	/**
	 * 请求对象中维度扩展行标识参数
	 */
	public final static String REQ_DIM_EXPAND_ROWID = "expand_rowid";

	/**
	 * 请求对象中维度收缩行标识参数
	 */
	public final static String REQ_DIM_COLLAPSE_ROWID = "collapse_rowid";

	/**
	 * 请求对象中排序索引
	 */
	public final static String REQ_SORT_INDEX = "sort_index";

	/**
	 * 请求对象中排序数据类型参数
	 */
	public final static String REQ_SORT_DATA_TYPE = "sort_data_type";

	/**
	 * 请求对象中排序顺序参数
	 */
	public final static String REQ_SORT_ORDER = "sort_order";

	/**
	 * 请求对象中维度水平前缀参数
	 */
	public final static String REQ_DIM_LEVEL_PREFIX = "dim_level_";

	/**
	 * 请求对象中指标水平前缀参数
	 */
	public final static String REQ_MSU_LEVEL_PREFIX = "msu_level_";

	/**
	 * 请求对象中维度值前缀参数
	 */
	public final static String REQ_DIM_VALUE_PREFIX = "dim_value_";

	/**
	 * 请求对象中表格标识参数
	 */
	public final static String REQ_TABLE_ID = "table_id";

	/**
	 * 请求对象中表格标识参数
	 */
	public final static String REQ_TABLE_PAGESIZE = "page_size";

	/**
	 * 请求对象中表格后台处理参数
	 */
	public static String TABLE_ACTION_DOT_DO = "SubjectCommTable.rptdo";

	/**
	 * 链接中维度参数替换字符串
	 */
	public final static String LINK_DIM_URL = "::DIM_URL::";

	/**
	 * 指标替换字符串
	 */
	public final static String MSU_REPLACE = "::MSU::";

	/**
	 * 比率临时替换字符串
	 */
	public final static String RATIO_TMP_FLD = "::RATIO_FLD::";

	/**
	 * NULL处理字符串
	 */
	public final static String NULL_PORCESS = "COALESCE(::NULL::,0)";

	/**
	 * 表格的最初功能
	 */
	public final static String TABLE_FUNC_INIT = "1";

	/**
	 * 表格的全部扩展功能
	 */
	public final static String TABLE_FUNC_EXPAND_ALL = "2";

	/**
	 * 表格的行扩展
	 */
	public final static String TABLE_FUNC_ROW_EXPAND = "3";

	/**
	 * 表格的行收缩
	 */
	public final static String TABLE_FUNC_ROW_COLLAPSE = "4";

	/**
	 * 表格的排序功能
	 */
	public final static String TABLE_FUNC_SORT = "5";

	/**
	 * 数据表表的伪表名
	 */
	public final static String DATA_TABLE_VIR_NAME = "A";

	/**
	 * 同比数据表名
	 */
	public final static String DATA_TABLE_LAST_VIR_NAME = "B";

	/**
	 * 环比数据表名
	 */
	public final static String DATA_TABLE_LOOP_VIR_NAME = "C";

	/**
	 * 数据表伪表名具有句点
	 */
	public final static String DATA_TABLE_BASE_VIR_NAME = "A0";

	/**
	 * 同比基本数据表名
	 */
	public final static String DATA_TABLE_BASE_LAST_VIR_NAME = "A1";

	/**
	 * 环比基本数据表名
	 */
	public final static String DATA_TABLE_BASE_LOOP_VIR_NAME = "A2";

	/**
	 * 数据表临时表名
	 */
	public final static String DATA_TABLE_TMP_VIR_NAME = "T";

	/**
	 * 数据表临时表名
	 */
	public final static String DATA_TABLE_MID_VIR_NAME = "M";

	/**
	 * boolean 值的真
	 */
	public final static String BOOLEAN_TRUE = "T";

	/**
	 * boolean 值的假
	 */
	public final static String BOOLEAN_FALSE = "F";

	/**
	 * 时间对应的月维表名称，由于所有时间维度在一张维表中 因此在这里定义
	 */
	public final static String TIME_DIM_MONTH_TABLE = "D_MONTH";

	/**
	 * 时间对应的日维表名称，由于所有时间维度在一张维表中 因此在这里定义
	 */
	public final static String TIME_DIM_DAY_TABLE = "D_DATE";

	/**
	 * 表示日期天的字段，20060101表示2006年1月1日
	 */
	public final static String TIME_DIM_DAY_FLD = "GATHER_DAY";

	/**
	 * 表示日期天的描述字段
	 */
	public final static String TIME_DIM_DAY_DESC_FLD = "DAY_DESC";

	/**
	 * 同比日字段
	 */
	public final static String TIME_DIM_SAME_DAY_FLD = "SAME_GATHER_DAY";

	/**
	 * 环比日字段
	 */
	public final static String TIME_DIM_LAST_DAY_FLD = "LAST_GATHER_DAY";

	/**
	 * 同比月字段
	 */
	public final static String TIME_DIM_SAME_MONTH_FLD = "SAME_GATHER_MON";

	/**
	 * 环比月字段
	 */
	public final static String TIME_DIM_LAST_MONTH_FLD = "LAST_GATHER_MON";

	/**
	 * 表示日期月份的字段，格式200601表示2006年2月份
	 */
	public final static String TIME_DIM_MONTH_FLD = "GATHER_MON";

	/**
	 * 表示0
	 */
	public final static String ZERO = "0";

	/**
	 * 列按升序排序
	 */
	public final static String SORT_ASC = "1";

	/**
	 * 列按降序排序
	 */
	public final static String SORT_DESC = "2";

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
	 * 排名模式，为非连续
	 */
	public final static String RANK_MODE_DISCON = "R";

	/**
	 * 排名函数，非连续式
	 */
	public final static String RANK_FUNC_DISCON = "RANK";

	/**
	 * 排名函数，连续式
	 */
	public final static String RANK_FUNC_CON = "DENSE_RANK";

	/**
	 * 专题数据表格占位符
	 */
	public final static String SUBJECT_TABLE_DATASOURCE = "::SOURCE_TABLE::";

	/**
	 * 表头key
	 */
	public final static String HEAD_KEYS_LEFT = "HEAD_KEYS_LEFT";
	/**
	 * 表头key
	 */
	public final static String HEAD_KEYS_LEFT_EXPORT = "HEAD_KEYS_LEFT_EXPORT";

	/**
	 * 表头key
	 */
	public final static String HEAD_KEYS_RIGHT = "HEAD_KEYS_RIGHT";
	/**
	 * 表头key
	 */
	public final static String HEAD_KEYS_RIGHT_EXPORT = "HEAD_KEYS_RIGHT_EXPORT";

	/**
	 * 均值在第一行
	 */
	public final static String AVG_ROW_POS_FIRST = "0";

	/**
	 * 表示正值时上升箭头
	 */
	public final static String POS_PROCESS_RISE = "0";

	/**
	 * 替换掉负号，而在导出中保留
	 */
	public final static String POS_NEG_REPLACE = "::NEG::";

	/**
	 * 小数处理截取
	 */
	public final static String DIGIT_PPROCESS_TRUNC = "0";

	/**
	 * 小数处理四舍五入
	 */
	public final static String DIGIT_PROCESS_ROUND = "1";

	/**
	 * 小数处理向上取整
	 */
	public final static String DIGIT_PROCESS_FLOOR = "2";
}
