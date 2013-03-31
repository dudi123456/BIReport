package com.ailk.bi.base.table;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class DimensionTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5514461266288868149L;

	// HERE IS FROM DATABASE

	// 维度标识
	public String dim_id = ""; // NUMBER->String

	// 维度名称
	public String dim_name = ""; // VARCHAR->String

	// 维度简单描述
	public String dim_desc = ""; // VARCHAR->String

	// 维度类型
	public String dim_type = ""; // NUMBER->String

	// 维度所在的维表
	public String dim_table = ""; // VARCHAR->String

	// 维度的编码字段名称
	public String code_idfld = ""; // VARCHAR->String

	// 维度编码字段类型 1 数值型，2 字符型
	public String idfld_type = ""; // NUMBER->String

	// 维度描述字段
	public String code_descfld = ""; // VARCHAR->String

	// 维度单位
	public String dim_unit = ""; // VARCHAR->String

	// 是否部门维度
	public String is_deptdim = ""; // NUMBER->String

	// 是否能到用户级
	public String to_userlvl = "";

	// 是否有效
	public String is_valid = "";

	// 生效日期
	public String val_date = "";

	// 失效日期
	public String exp_date = "";

	// 保留字段
	public String rsv_fld1 = "";

	// 保留字段
	public String rsv_fld2 = "";

	// 保留字段
	public String rsv_fld3 = "";

	// HERE IS USER DEFINE 这一行不要删除！

	// 类型对象
	public DimTypeTable type_obj = null;

	// 维度的层次对象列表，列表index为层次对象的层次标识
	public List dim_levels = null;

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof DimensionTable) {
			DimensionTable tmpObj = (DimensionTable) obj;
			equal = this.dim_id.equals(tmpObj.dim_id);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer toString = new StringBuffer("");
		toString.append("dim_id=").append(this.dim_id).append("\n");

		// 维度名称
		toString.append("dim_name").append(this.dim_name).append("\n");

		// 维度简单描述
		toString.append("dim_desc").append(this.dim_desc).append("\n");

		// 维度类型
		toString.append("dim_type").append(this.dim_type).append("\n");

		// 维度所在的维表
		toString.append("dim_table").append(this.dim_table).append("\n");

		// 维度的编码字段名称
		toString.append("code_idfld").append(this.code_idfld).append("\n");

		// 维度编码字段类型 1 数值型，2 字符型
		toString.append("idfld_type").append(this.idfld_type).append("\n");

		// 维度描述字段
		toString.append("code_descfld").append(this.code_descfld).append("\n");

		// 维度单位
		toString.append("dim_unit").append(this.dim_unit).append("\n");

		// 是否部门维度
		toString.append("is_deptdim").append(this.is_deptdim).append("\n");

		// 是否能到用户级
		toString.append("to_userlvl").append(this.to_userlvl).append("\n");

		// 是否有效
		toString.append("is_valid").append(this.is_valid).append("\n");

		// 生效日期
		toString.append("val_date").append(this.val_date).append("\n");

		// 失效日期
		toString.append("exp_date").append(this.exp_date).append("\n");

		// 保留字段
		toString.append("rsv_fld1 ").append(this.rsv_fld1).append("\n");

		// 保留字段
		toString.append("rsv_fld2").append(this.rsv_fld2).append("\n");

		// 保留字段
		toString.append("rsv_fld3").append(this.rsv_fld3).append("\n");
		return toString.toString();
	}

}
