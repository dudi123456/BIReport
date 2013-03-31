package com.ailk.bi.base.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class SubjectCommTabCol extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6002094790352549309L;

	// HERE IS FROM DATABASE
	/**
	 * 表格标识
	 */
	public String table_id = ""; // VARCHAR->String

	/**
	 * 列标识
	 */
	public String col_id = ""; // NUMBER->String

	/**
	 * 列名称
	 */
	public String col_name = "";// VARCHAR->String

	/**
	 * 列的全描述名称
	 */
	public String col_desc = "";// VARCHAR->String

	/**
	 * 列显示顺序
	 */
	public String col_sequence = ""; // NUMBER->String

	/**
	 * 是否是指标
	 */
	public String is_measure = "";

	/**
	 * 维度值是否作为展开条件
	 */
	public String dim_aswhere = "";

	/**
	 * 列是否默认显示,配合用户自定义指标使用
	 */
	public String default_display = "";

	/**
	 * 维度作为列横向展示
	 */
	public String dim_ascol = "";

	/**
	 * 是否可扩展
	 */
	public String is_expand_col = "";

	/**
	 * 默认是否展开，一个表格只应有一个列为Y
	 */
	public String defalut_drilled = "";

	/**
	 * 默认展开层次
	 */
	public String init_level = "";

	/**
	 * 维度的编码字段
	 */
	public String code_field = ""; // NUMBER->String

	/**
	 * 初始字段
	 */
	public String init_code_field = "";
	/**
	 * 维度的描述字段
	 */
	public String desc_field = ""; // NUMBER->String

	/**
	 * 维度的描述字段是否作为title间接显示
	 */
	public String desc_astitle = "";

	/**
	 * 是否是比率指标
	 */
	public String is_ratio = "";

	/**
	 * 数据类型
	 */
	public String data_type = ""; // NUMBER->String

	/**
	 * 小数位数
	 */
	public String digit_length = ""; // NUMBER->String

	/**
	 * 是否计算占比
	 */
	public String has_comratio = "";

	/**
	 * 数据具有链接
	 */
	public String has_link = "";

	/**
	 * 链接URL
	 */
	public String link_url = "";

	/**
	 * 链接目标，如弹出等
	 */
	public String link_target = "";

	/**
	 * 是否有同比箭头
	 */
	public String has_last = "";

	/**
	 * 同比是否显示
	 */
	public String last_display = "";

	/**
	 * 比率上升时的箭头颜色
	 */
	public String rise_arrow_color = "";

	/**
	 * 是否有同比链接
	 */
	public String has_last_link = "";

	/**
	 * 同比链接URL
	 */
	public String last_url = "";

	/**
	 * 同比链接目标
	 */
	public String last_target = "";

	/**
	 * 是否有环比
	 */
	public String has_loop = "";

	/**
	 * 环比是否显示
	 */
	public String loop_display = "";

	/**
	 * 是否有环比链接
	 */
	public String has_loop_link = "";

	/**
	 * 环比链接URL
	 */
	public String loop_url = "";

	/**
	 * 环比链接目标
	 */
	public String loop_target = "";

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
	 * 是否有效
	 */
	public String status = "";

	/**
	 * 特殊合计行样式，会增加到这个所在行
	 */
	public String row_css_class = "";

	/**
	 * 表头对齐方式
	 */
	public String title_align = "";

	// HERE IS USER DEFINE 这一行不要删除！

	/**
	 * 当前的层次
	 */
	public String level = "";

	/**
	 * 是否能够钻取
	 */
	public boolean canDrill = false;

	/**
	 * 在表格中实际列序号
	 */
	public int index = -1;

	/**
	 * 显示的先后顺序
	 */
	public int display_order = -1;

	/**
	 * 维度的具体值
	 */
	public Map values = null;

	/**
	 * 是否合并行
	 */
	public boolean rowSpaned = false;

	/**
	 * 列的表头的HTML代码
	 */
	public String colHeadHTML1 = "";

	/**
	 * 列的表头的占比HTML代码
	 */
	public String colHeadHTML2 = "";

	/**
	 * 列的表头的同比HTML代码
	 */
	public String colHeadHTML3 = "";

	/**
	 * 列的表头的环比HTML代码
	 */
	public String colHeadHTML4 = "";

	/**
	 * 维度层次
	 */
	public List levels = null;

	/**
	 * 合计行是否有效
	 */
	public String total_displayed = "";

	/**
	 * 指标跨行数
	 * 
	 */
	public String row_span = "";

	/**
	 * 在表头中位置，因为只能向上跨行，因此需要替换
	 * 
	 */
	public String head_place = "";

	/**
	 * 默认指标是否排序
	 */
	public String default_sort = "";

	/**
	 * 排序顺序
	 */
	public String sort_order = "";

	/**
	 * 是否有排名
	 */
	public String has_rank = "";

	/**
	 * 排名方式，非连续排名RANK，连续排名DENSE_RANK
	 */
	public String rank_mode = "";

	/**
	 * 排名顺序，是按升序还是降序
	 */
	public String rank_order = "";

	/**
	 * 排名是否有波动
	 */
	public String rank_varity = "";

	/**
	 * 为计算排名波动所需的上期字段名，需要事先准备好
	 */
	public String rank_last = "";

	/**
	 * 链接是否应有权限
	 * 
	 */
	public String link_limit_right = "";

	/**
	 * 此列的均值是否显示
	 */
	public String avg_display = "";

	/**
	 * 正负值是否处理
	 */
	public String pos_neg_process = "";

	/**
	 * 正值如何处理，0表示上升箭头，1表示下降箭头
	 */
	public String pos_process = "";

	/**
	 * 小数处理方式 0:截取，1：四舍五入，2：向上取整
	 */
	public String digit_process = "";

	/**
	 * 是否需要截取处理
	 */
	public String need_substr = "";

	/**
	 * 截取汉字位数
	 */
	public String substr_num = "";
	/**
	 * 存储所有的各层次的描述是否作为title显示
	 */
	public Map descAsTitle = new HashMap();

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof SubjectCommTabCol))
			return false;
		SubjectCommTabCol tmpObj = (SubjectCommTabCol) obj;
		return this.table_id.equals(tmpObj.table_id) && this.col_id.equals(tmpObj.col_id);
	}

	public int hashCode() {
		return this.table_id.hashCode() + this.col_id.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 报表标识
		sb.append(" table_id = ").append(this.table_id).append("\n");

		sb.append(" col_id = ").append(this.col_id).append("\n");

		sb.append(" col_name = ").append(this.col_name).append("\n");

		sb.append(" col_desc = ").append(this.col_desc).append("\n");

		sb.append(" col_sequence = ").append(this.col_sequence).append("\n");

		sb.append(" is_measure = ").append(this.is_measure).append("\n");

		sb.append(" dim_aswhere = ").append(this.dim_aswhere).append("\n");

		sb.append(" default_display = ").append(this.default_display).append("\n");

		sb.append(" dim_ascol = ").append(this.dim_ascol).append("\n");

		sb.append(" is_expand_col = ").append(this.is_expand_col).append("\n");

		sb.append(" defalut_drilled = ").append(this.defalut_drilled).append("\n");

		sb.append(" init_level = ").append(this.init_level).append("\n");

		sb.append(" code_field = ").append(this.code_field).append("\n");

		sb.append(" desc_field = ").append(this.desc_field).append("\n");

		sb.append(" is_ratio = ").append(this.is_ratio).append("\n");

		sb.append(" data_type = ").append(this.data_type).append("\n");

		sb.append(" digit_length = ").append(this.digit_length).append("\n");

		sb.append(" has_comratio = ").append(this.has_comratio).append("\n");

		sb.append(" has_link = ").append(this.has_link).append("\n");

		sb.append(" link_url = ").append(this.link_url).append("\n");

		sb.append(" link_target = ").append(this.link_target).append("\n");

		sb.append(" has_last = ").append(this.has_last).append("\n");

		sb.append(" last_display = ").append(this.last_display).append("\n");

		sb.append(" rise_arrow_color = ").append(this.rise_arrow_color).append("\n");

		sb.append(" has_last_link = ").append(this.has_last_link).append("\n");

		sb.append(" last_url = ").append(this.last_url).append("\n");

		sb.append(" last_target = ").append(this.last_target).append("\n");

		sb.append(" has_loop = ").append(this.has_loop).append("\n");

		sb.append(" loop_display =").append(this.loop_display).append("\n");

		sb.append(" has_loop_link = ").append(this.has_loop_link).append("\n");

		sb.append(" loop_url = ").append(this.loop_url).append("\n");

		sb.append(" loop_target = ").append(this.loop_target).append("\n");

		sb.append(" is_colclk = ").append(this.is_colclk).append("\n");

		sb.append(" col_chart_ids = ").append(this.col_chart_ids).append("\n");

		sb.append(" is_cellclk = ").append(this.is_cellclk).append("\n");

		sb.append(" cell_chart_ids = ").append(this.cell_chart_ids).append("\n");

		sb.append(" status = ").append(this.status).append("\n");
		return sb.toString();
	}

	public Object clone() {
		SubjectCommTabCol tabCol = new SubjectCommTabCol();
		tabCol.table_id = this.table_id;

		tabCol.col_id = this.col_id;

		tabCol.col_name = this.col_name;

		tabCol.col_desc = this.col_desc;

		tabCol.col_sequence = this.col_sequence;

		tabCol.is_measure = this.is_measure;

		tabCol.dim_aswhere = this.dim_aswhere;

		tabCol.default_display = this.default_display;

		tabCol.dim_ascol = this.dim_ascol;

		tabCol.is_expand_col = this.is_expand_col;

		tabCol.defalut_drilled = this.defalut_drilled;

		tabCol.init_level = this.init_level;

		tabCol.code_field = this.code_field;

		tabCol.desc_field = this.desc_field;

		tabCol.is_ratio = this.is_ratio;

		tabCol.data_type = this.data_type;

		tabCol.digit_length = this.digit_length;

		tabCol.has_comratio = this.has_comratio;

		tabCol.has_link = this.has_link;

		tabCol.link_url = this.link_url;

		tabCol.link_target = this.link_target;

		tabCol.has_last = this.has_last;

		tabCol.last_display = this.last_display;

		tabCol.rise_arrow_color = this.rise_arrow_color;

		tabCol.has_last_link = this.has_last_link;

		tabCol.last_url = this.last_url;

		tabCol.last_target = this.last_target;

		tabCol.has_loop = this.has_loop;

		tabCol.loop_display = this.loop_display;

		tabCol.has_loop_link = this.has_loop_link;

		tabCol.loop_url = this.loop_url;

		tabCol.loop_target = this.loop_target;

		tabCol.is_colclk = this.is_colclk;

		tabCol.col_chart_ids = this.col_chart_ids;

		tabCol.is_cellclk = this.is_cellclk;

		tabCol.cell_chart_ids = this.cell_chart_ids;

		tabCol.status = this.status;

		tabCol.level = this.level;

		tabCol.canDrill = this.canDrill;

		tabCol.index = this.index;

		tabCol.values = null;

		tabCol.rowSpaned = this.rowSpaned;

		// 由于列表是对象，因此不克隆了
		tabCol.levels = null;
		return tabCol;
	}

}
