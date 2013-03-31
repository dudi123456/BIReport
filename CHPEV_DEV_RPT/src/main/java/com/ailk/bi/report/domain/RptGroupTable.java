package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptGroupTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String group_id = ""; // VARCHAR->String

	public String group_name = ""; // VARCHAR->String

	public String rpt_id = ""; // VARCHAR->String

	public static RptGroupTable genReportGroupFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");

		RptGroupTable group = new RptGroupTable();
		int m = 0;
		group.rpt_id = svces[m++];
		group.group_id = svces[m++];
		group.group_name = svces[m++];

		return group;
	}
}
