package com.ailk.bi.report.domain;

import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.common.event.JBTableBase;

public class RptProcessHisTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String rpt_id = ""; // VARCHAR->String

	public String rpt_date = ""; // NUMBER->String

	public String region_id = ""; // NUMBER->String

	public String p_time = ""; // NUMBER->String

	public String p_id = ""; // NUMBER->String

	public String p_decision = ""; // NUMBER->String\

	public String p_decision_desc = ""; // NUMBER->String

	public String p_step_flag = ""; // NUMBER->String

	public String p_step_next = ""; // VARCHAR->String

	public String p_user_id = ""; // VARCHAR->String

	public String p_user_name = ""; // VARCHAR->String

	public String p_his_note = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	/**
	 * 生成列定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptProcessHisTable genReportProcessHisFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptProcessHisTable hisTable = new RptProcessHisTable();
		int m = 0;
		hisTable.p_time = svces[m++];
		hisTable.p_id = svces[m++];
		hisTable.p_decision = svces[m++].trim();
		if (TableConsts.YES.equals(hisTable.p_decision))
			hisTable.p_decision_desc = "审核通过";
		else if (TableConsts.RETURN.equals(hisTable.p_decision))
			hisTable.p_decision_desc = "回退请求";
		else if (TableConsts.NO.equals(hisTable.p_decision))
			hisTable.p_decision_desc = "审核回退";
		else if (TableConsts.FORWARD.equals(hisTable.p_decision))
			hisTable.p_decision_desc = "审核前行";
		hisTable.p_step_flag = svces[m++].trim();
		hisTable.p_step_next = svces[m++].trim();
		hisTable.p_user_id = svces[m++];
		hisTable.p_user_name = svces[m++];
		hisTable.p_his_note = svces[m++];
		return hisTable;
	}

}
