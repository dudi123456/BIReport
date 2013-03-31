package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptProcessStepTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String rpt_date = ""; // VARCHAR->String

	public String p_id = ""; // VARCHAR->String

	public String p_step_flag = ""; // VARCHAR->String

	public String p_step_flag_name = ""; // VARCHAR->String

	public String r_role_id = ""; // VARCHAR->String

	public String r_role_type = ""; // NUMBER->String

	public String p_step_visible_data = ""; // NUMBER->String

	public String p_step_note = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String role_code = ""; // VARCHAR->String

	public String role_name = ""; // VARCHAR->String

	public String comments = ""; // VARCHAR->String

	/**
	 * 从角色定义中生成查询定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptProcessStepTable getRoleDefineFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptProcessStepTable roleTable = new RptProcessStepTable();
		int m = 0;
		roleTable.role_code = svces[m++];
		roleTable.role_name = svces[m++];
		roleTable.comments = svces[m++];
		roleTable.p_step_flag_name = roleTable.comments;
		return roleTable;
	}

	/**
	 * 审核流程生成查询定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptProcessStepTable getStepDefineFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptProcessStepTable stepTable = new RptProcessStepTable();
		int m = 0;
		stepTable.rpt_date = svces[m++];
		stepTable.p_id = svces[m++];
		stepTable.p_step_flag = svces[m++];
		stepTable.p_step_flag_name = svces[m++];
		stepTable.r_role_id = svces[m++];
		stepTable.r_role_type = svces[m++];
		stepTable.p_step_visible_data = svces[m++].trim();
		stepTable.p_step_note = svces[m++];
		return stepTable;
	}
}
