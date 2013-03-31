package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UiCommonTabledictTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1016028160363236833L;

	// HERE IS FROM DATABASE
	public String table_id = ""; // NUMBER->String

	public String col_id = ""; // NUMBER->String

	public String col_name = ""; // VARCHAR->String

	public String col_sequence = ""; // NUMBER->String

	public String code_field = ""; // VARCHAR->String

	public String desc_field = ""; // VARCHAR->String

	public String is_display = ""; // CHAR->String

	public String data_type = ""; // NUMBER->String

	public String digit_length = ""; // NUMBER->String

	public String ismeasure = ""; // CHAR->String

	public String isexpandcol = ""; // CHAR->String

	public String default_drilled = ""; // CHAR->String

	public String init_level = ""; // NUMBER->String

	public String colsrch_where = ""; // VARCHAR->String

	public String has_link = ""; // CHAR->String

	public String link_url = ""; // VARCHAR->String

	public String link_target = ""; // VARCHAR->String

	public String has_last = ""; // CHAR->String

	public String last_field = ""; // VARCHAR->String

	public String last_display = ""; // CHAR->String

	public String rise_arrow_color = ""; // CHAR->String

	public String has_last_link = ""; // CHAR->String

	public String last_url = ""; // VARCHAR->String

	public String last_target = ""; // VARCHAR->String

	public String has_loop = ""; // CHAR->String

	public String loop_field = ""; // VARCHAR->String

	public String loop_display = ""; // CHAR->String

	public String has_loop_link = ""; // CHAR->String

	public String loop_url = ""; // VARCHAR->String

	public String loop_target = ""; // VARCHAR->String

	public String has_comratio = ""; // CHAR->String

	public String is_col_click_chart_change = ""; // CHAR->String

	public String col_rlt_chart_id = ""; // VARCHAR->String

	public String is_cell_click_chart_change = ""; // CHAR->String

	public String cell_rlt_chart_id = ""; // VARCHAR->String

	public String status = ""; // CHAR->String

	public String col_desc = ""; // VARCHAR->String

	public String is_ratio = ""; // CHAR->String

	public String blk_num = ""; // NUMBER->String

	public String is_sumdisplay = ""; // CHAR->String

	public String code_field_clone = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！
	// HERE IS FROM DATABASE
	/**
	 * 是否由其他列计算
	 */
	public String is_calByOther = "";

	/**
	 * 是否是指标
	 */
	public String is_measure = "";

	/**
	 * 是否可扩展
	 */
	public String isExpandCol = "";

	/**
	 * 默认是否展开，一个表格只应有一个列为Y
	 */
	public String defalut_drilled = "";

	/**
	 * 是否单击列标题图形联动
	 */
	public String is_colclk = "";

	/**
	 * 联动图形标识，多个以逗号分隔
	 */
	public String col_chart_ids = "";

	/**
	 * 是否单击单元格图形联动
	 */
	public String is_cellclk = "";

	/**
	 * 联动图形标识，多个以逗号分隔
	 */
	public String cell_chart_ids = "";
	/**
	 * 对齐列而使用的空格数
	 */
	public String blank_num = "";

	/**
	 * 当前的层次
	 */
	public String level = "";
	/**
	 * 从没有钻取过
	 */
	public boolean neverDrilled = false;

	/**
	 * 是否能够钻取
	 */
	public boolean canDrill = false;

	/**
	 * 在表格中实际列序号
	 */
	public int index = -1;

	/**
	 * 维度的具体值
	 */
	public List value = null;

	/**
	 * 计算公式对象
	 */
	public UiCommonMsucalTable calObj = null;

	/**
	 * 计算因子
	 */
	public List msuFactors = null;

	/**
	 * 列标题联动报表Target
	 */
	public String col_rlt_chart_target = "";

	public Object clone() {

		UiCommonTabledictTable result = (UiCommonTabledictTable) super.clone();
		if (null != value) {
			result.value = new ArrayList();
			result.value.addAll(value);
		}
		return result;
	}

}
