package com.ailk.bi.base.table;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class MeasureTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7892065796435723686L;

	// HERE IS FROM DATABASE

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 指标名称
	public String msu_name = ""; // VARCHAR->String

	// 指标用途
	public String msu_target = ""; // NUMBER->String

	// 是否是计算型指标
	public String is_calmsu = ""; // CHAR->String

	// 是否是时点指标
	public String is_timemsu = ""; // CHAR->String

	// 是否衍生指标
	public String is_deri = ""; // VARCHAR->String

	// 指标类型
	public String msu_type = ""; // NUMBER->String

	// 父指标标识
	public String parent_id = ""; // VARCHAR->String

	// 指标统计规则
	public String msu_rule = ""; // NUMBER->String

	// 统计规则解释
	public String rule_desc = ""; // VARCHAR->String

	// 指标说明
	public String msu_desc = ""; // VARCHAR->String

	// 指标单位
	public String msu_unit = ""; // VARCHAR->String

	// 指标小数位数
	public String pricesion = "";// NUMBER->String

	// 是否逗号分隔显示
	public String comma_splitted = "";// VARCHAR->String

	// 是否显示％标记
	public String ratio_displayed = "";// VARCHAR->String

	// NULL值处理
	public String nvl_proc = "";// VARCHAR->String

	// 零值处理
	public String zero_proc = "";// VARCHAR->String

	// 指标字段名称，或AS后的伪名称
	public String msu_fld = "";// VARCHAR->String

	// 组装SQL所用字段，如SUM()
	public String real_fld = "";// VARCHAR->String

	// 字段类型
	public String fld_type = "";// NUMBER->String

	// 是否有效
	public String is_valid = "";// VARCHAR->String

	// 生效日期
	public String val_date = "";// DATE->String

	// 失效日期
	public String exp_date = "";// DATE->String

	// 保留字段
	public String rsv_fld1 = "";// VARCHAR->String

	// 保留字段
	public String rsv_fld2 = "";// VARCHAR->String

	// 保留字段
	public String rsv_fld3 = "";// VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	// 类型对象
	public MsuTypeTable msu_typeObj = null;

	// 生成衍生指标的维度定义列表，值为 MsuDeriDimsTable对象，索引为 hlvl_id
	public List msu_deri_dims = null;

	// 该指标的数据源表列表，MsuDataTable为对象
	public List msu_data_tabs = null;

	// 所有的孩子列表
	public List children = null;

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof MeasureTable) {
			MeasureTable tmpObj = (MeasureTable) obj;
			equal = this.msu_id.equals(tmpObj.msu_id);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 指标标识
		sb.append(" msu_id = ").append(this.msu_id).append("\n");

		// 指标名称
		sb.append(" msu_name = ").append(this.msu_name).append("\n");

		// 指标用途
		sb.append(" msu_target = ").append(this.msu_target).append("\n");

		// 是否计算型指标
		sb.append(" is_calmsu = ").append(this.is_calmsu).append("\n");

		// 是否时点指标
		sb.append(" is_timemsu = ").append(this.is_timemsu).append("\n");

		// 是否衍生指标
		sb.append(" is_deri = ").append(this.is_deri).append("\n");

		// 指标类型
		sb.append(" msu_type = ").append(this.msu_type).append("\n");

		// 父指标标识
		sb.append(" parent_id = ").append(this.parent_id).append("\n");

		// 指标统计规则
		sb.append(" msu_rule = ").append(this.msu_rule).append("\n");

		// 统计规则解释
		sb.append(" rule_desc = ").append(this.rule_desc).append("\n");

		// 指标说明
		sb.append(" msu_desc = ").append(this.msu_desc).append("\n");

		// 指标单位
		sb.append(" msu_unit = ").append(this.msu_unit).append("\n");

		// 指标小数位数
		sb.append(" pricesion = ").append(this.pricesion).append("\n");

		// 是否逗号分隔显示
		sb.append(" comma_splitted = ").append(this.comma_splitted)
				.append("\n");

		// 是否显示％标记
		sb.append(" ratio_displayed = ").append(this.ratio_displayed)
				.append("\n");

		// NULL值处理
		sb.append(" nvl_proc = ").append(this.nvl_proc).append("\n");

		// 零值处理
		sb.append(" zero_proc = ").append(this.zero_proc).append("\n");

		// 指标字段名称，或AS后的伪名称
		sb.append(" msu_fld = ").append(this.msu_fld).append("\n");

		// 组装SQL所用字段，如SUM()
		sb.append(" real_fld = ").append(this.real_fld).append("\n");

		// 字段类型
		sb.append(" fld_type = ").append(this.fld_type).append("\n");

		// 是否有效
		sb.append(" is_valid = ").append(this.is_valid).append("\n");

		// 生效日期
		sb.append(" val_date = ").append(this.val_date).append("\n");

		// 失效日期
		sb.append(" exp_date = ").append(this.exp_date).append("\n");

		// 保留字段
		sb.append(" rsv_fld1 = ").append(this.rsv_fld1).append("\n");

		// 保留字段
		sb.append(" rsv_fld2 = ").append(this.rsv_fld2).append("\n");

		// 保留字段
		sb.append(" rsv_fld3 = ").append(this.rsv_fld3).append("\n");
		return sb.toString();
	}

}
