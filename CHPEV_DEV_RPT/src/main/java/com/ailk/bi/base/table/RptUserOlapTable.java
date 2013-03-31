package com.ailk.bi.base.table;

import com.ailk.bi.common.annotation.PriKey;
import com.ailk.bi.common.annotation.TableCol;
import com.ailk.bi.common.annotation.TableName;
import com.ailk.bi.common.event.JBTableBase;

@TableName("UI_RPT_INFO_USER_OLAP")
public class RptUserOlapTable extends JBTableBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 自定义报表标识
	@TableCol("CUSTOM_RPTID")
	public String custom_rptid = ""; // NUMBER->String

	// 自定义报表名称
	@TableCol("CUSTOM_RPTNAME")
	public String custom_rptname = ""; // VARCHAR->String

	// 用户标识
	@TableCol("USER_ID")
	@PriKey
	public String user_id = ""; // VARCHAR->String

	// 分析型报表标识
	@TableCol("REPORT_ID")
	@PriKey
	public String report_id = ""; // CHAR->String

	// 显示方式
	@TableCol("DISPLAY_MODE")
	public String display_mode = ""; // NUMBER->String

	// 是否有效
	@TableCol("IS_VALID")
	public String is_valid = ""; // CHAR->String

	/**
	 * 所关联的报表
	 */
	public PubInfoResourceTable report = null;

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptUserOlapTable) {
			RptUserOlapTable tmpObj = (RptUserOlapTable) obj;
			equal = this.custom_rptid.equals(tmpObj.custom_rptid);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 自定义报表标识
		sb.append(" report_id = ").append(this.custom_rptid).append("\n");

		// 自定义报表名称
		sb.append(" custom_rptname = ").append(this.custom_rptname)
				.append("\n");

		// 用户标识
		sb.append(" user_id = ").append(this.user_id).append("\n");

		// 分析型报表标识
		sb.append(" report_id = ").append(this.report_id).append("\n");

		// 显示方式
		sb.append(" display_mode = ").append(this.display_mode).append("\n");

		// 是否有效
		sb.append(" is_valid = ").append(this.is_valid).append("\n");

		return sb.toString();
	}

}
