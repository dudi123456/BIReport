package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptProcessStateTable extends JBTableBase {

	/**
	 * 报表审核审核状态信息
	 */
	private static final long serialVersionUID = 1L;

	public String rpt_date = ""; // VARCHAR->String

	public String p_id = ""; // VARCHAR->String

	public String p_step_length = ""; // VARCHAR->String

	public String now_decision = ""; // VARCHAR->String

	public String now_step = ""; // VARCHAR->String

	public String now_step_name = ""; // VARCHAR->String

	public String next_step = ""; // VARCHAR->String

	public String next_step_name = ""; // VARCHAR->String

}
