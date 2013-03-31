package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGRiskSwitchMTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -764486994896234632L;

	// HERE IS FROM DATABASE
	public String gather_mon = ""; // NUMBER->String

	public String user_id = ""; // NUMBER->String

	public String svc_lvl = ""; // VARCHAR->String

	public String svc_lvl_desc = ""; // VARCHAR->String

	public String risk_knd = ""; // VARCHAR->String

	public String risk_knd_desc = ""; // VARCHAR->String

	public String pref_alarm = ""; // VARCHAR->String

	public String pref_alarm_desc = ""; // VARCHAR->String

	public String operator = ""; // VARCHAR->String

	public String alarm_knd = ""; // VARCHAR->String

	public String alarm_knd_desc = ""; // VARCHAR->String

	public String value = ""; // NUMBER->String

	public String use_tag = ""; // VARCHAR->String

	public String remark = ""; // VARCHAR->String

	public String change_date = ""; // DATE->String

	public String change_person_id = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
