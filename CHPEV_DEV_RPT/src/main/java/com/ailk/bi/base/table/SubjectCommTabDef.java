package com.ailk.bi.base.table;

import java.util.List;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class SubjectCommTabDef extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 127866136319493155L;

	// HERE IS FROM DATABASE
	/**
	 * 表格标识
	 */
	public String table_id = ""; // NUMBER->String

	/**
	 * 表格名称
	 */
	public String table_name = "";// VARCHAR->String

	/**
	 * 表格说明
	 */
	public String table_desc = ""; // VARCHAR->String

	/**
	 * 数据表
	 */
	public String data_table = ""; // VARCHAR->String

	/**
	 * 数据表查询条件
	 */
	public String data_where = ""; // VARCHAR->String

	/**
	 * 是否带有折叠、展开功能
	 */
	public String has_expand = ""; // CHAR->String

	/**
	 * 是否带有全展开功能
	 */
	public String has_expandall = "";// CHAR->String

	/**
	 * 当扩展维度在条件中到某一级别时，是否扔掉以前的
	 */
	public String throw_old = "";// CHAR->String

	/**
	 * 是否带有分页功能
	 */
	public String has_paging = ""; // CHAR->String

	/**
	 * 时间字段
	 */
	public String time_field = "";// VARCHAR->String

	/**
	 * 字段类型
	 */
	public String field_type = "";// NUMBER->String

	/**
	 * 数据粒度,4月数据，6日数据粒度
	 */
	public String time_level = "";// NUMBER->String

	/**
	 * 单击行图形变化
	 */
	public String row_clicked_chartchange = "";// CHAR->String

	public String row_clicked_tablechange = "";// CHAR->String

	/**
	 * 要变化的图形标识，英文逗号分隔
	 */
	public String rlt_chart_ids = "";// VARCHAR->String
	public String rlt_table_ids = "";// VARCHAR->String

	/**
	 * 是否具有自定义表头
	 */
	public String has_head = "";// CHAR->String

	/**
	 * 是否显示合计行
	 */
	public String sum_display = "";// CHAR->String

	/**
	 * 维度作为列横向展示
	 */
	public String dim_ascol = "";// CHAR->String

	/**
	 * 用户自定义指标
	 */
	public String custom_msu = ""; // CHAR->String

	/**
	 * 表格是否有排序
	 */
	public String has_sort = "";

	/**
	 * 分页时每页行数
	 */
	public String page_rows = "";

	/**
	 * 表格是否纵转横
	 */
	public String row_head_swap = "";

	/**
	 * 权限对应的列定义的标识
	 */
	public String right_col_id = "";

	/**
	 * 全新级别
	 */
	public String right_lvl = "";

	/**
	 * 权限对应的取值
	 */
	public int rightValueIndex = -1;

	/**
	 * 是否有均值
	 * 
	 */
	public String has_avg = "";

	/**
	 * 均值位置
	 */
	public String avg_pos = "";

	/**
	 * 纵转横时具体要显示为百分比的列序号
	 */
	public String head_swap_ratio_index = "";

	/**
	 * 纵转横时具体要显示为百分比小数位数
	 */
	public String head_swap_ratio_digit = "";

	/**
	 * 指定纵转横时各列的小数位数，如果不指定，按照指标定义格式化
	 */
	public String head_swap_col_digit = "";

	/**
	 * 合计行的位置
	 */
	public String sum_pos = "";

	/**
	 * 条件是否放到数据源里面
	 * 
	 */
	public String condition_in = "";

	/**
	 * 是否有分组功能
	 */
	public String no_groupby = "";

	/**
	 * 是否有排序
	 */
	public String no_orderby = "";
	// HERE IS USER DEFINE 这一行不要删除！
	/**
	 * 表格的表头
	 */
	public SubjectCommTabHead tabHead = null;

	/**
	 * 表格的列定义
	 */
	public List tableCols = null;

	/**
	 * 表格类型
	 */
	public String table_type = "";

	/**
	 * 唯独扩展为列前的列定义列表
	 */
	public List preTableCols = null;

	/**
	 * 查询出的总行数，默认为-1，以便判断是否是第一次访问
	 */
	public int totalRows = -1;

	/**
	 * 查询语句缓存
	 * 
	 */
	public String querySQL = "";

	/**
	 * 用户权限
	 */
	public UserCtlRegionStruct userRight = null;

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof SubjectCommTabDef))
			return false;
		SubjectCommTabDef tmpObj = (SubjectCommTabDef) obj;
		return this.table_id.equals(tmpObj.table_id);
	}

	public int hashCode() {
		return this.table_id.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 报表标识
		sb.append(" table_id = ").append(this.table_id).append("\n");

		sb.append(" table_name = ").append(this.table_name).append("\n");

		sb.append(" table_desc = ").append(this.table_desc).append("\n");

		sb.append(" data_table = ").append(this.data_table).append("\n");

		sb.append(" data_where = ").append(this.data_where).append("\n");

		sb.append(" has_expand = ").append(this.has_expand).append("\n");

		sb.append(" has_paging = ").append(this.has_paging).append("\n");

		sb.append(" time_field = ").append(this.time_field).append("\n");

		sb.append(" field_type = ").append(this.field_type).append("\n");

		sb.append(" time_level = ").append(this.time_level).append("\n");

		sb.append(" row_clicked_chartchange = ").append(this.row_clicked_chartchange).append("\n");

		sb.append(" rlt_chart_ids = ").append(this.rlt_chart_ids).append("\n");

		// add by zhxiaojing
		sb.append(" row_clicked_tablechange = ").append(this.row_clicked_tablechange).append("\n");

		sb.append(" rlt_table_ids = ").append(this.rlt_table_ids).append("\n");

		// end add

		sb.append(" has_head = ").append(this.has_head).append("\n");

		sb.append(" sum_display = ").append(this.sum_display).append("\n");

		sb.append(" dim_ascol = ").append(this.dim_ascol).append("\n");

		sb.append(" custom_msu = ").append(this.custom_msu).append("\n");
		return sb.toString();
	}
}
