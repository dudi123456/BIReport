package com.ailk.bi.base.table;

import com.ailk.bi.common.annotation.TableCol;
import com.ailk.bi.common.annotation.TableName;
import com.ailk.bi.common.event.JBTableBase;

@TableName("UI_RPT_META_USER_OLAP_DIM")
public class RptOlapUserDimTable extends JBTableBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 自定义分析型报表标识
	@TableCol("CUSTOM_RPTID")
	public String custom_rptid = ""; // NUMBER->String

	// 维度标识
	@TableCol("DIM_ID")
	public String dim_id = ""; // VARCHAR->String

	// 显示顺序
	@TableCol("DISPLAY_ORDER")
	public String display_order = ""; // VARCHAR->String

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapUserDimTable) {
			RptOlapUserDimTable tmpObj = (RptOlapUserDimTable) obj;
			equal = this.custom_rptid.equals(tmpObj.custom_rptid)
					&& this.dim_id.equals(tmpObj.dim_id);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 自定义分析型报表标识
		sb.append(" custom_rptid = ").append(this.custom_rptid).append("\n");

		// 维度标识
		sb.append(" dim_id = ").append(this.dim_id).append("\n");

		// 显示顺序
		sb.append(" display_order = ").append(this.display_order).append("\n");

		return sb.toString();
	}

}
