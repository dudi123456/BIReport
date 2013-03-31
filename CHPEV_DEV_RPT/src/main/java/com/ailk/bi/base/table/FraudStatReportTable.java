package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FraudStatReportTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3045486919110462742L;

	// HERE IS FROM DATABASE
	public String fraud_warn_sn = ""; // NUMBER->String

	public String fraud_item_code = ""; // VARCHAR->String

	public String fraud_evt_tele_type = ""; // VARCHAR->String

	public String fraud_user_type = ""; // VARCHAR->String

	public String fraud_emerg_level = ""; // VARCHAR->String

	public String fraud_local_net = ""; // VARCHAR->String

	public String fraud_evt_call_date = ""; // VARCHAR->String

	public String fraud_evt_answer_date = ""; // VARCHAR->String

	public String fraud_evt_call_duration = ""; // NUMBER->String

	public String fraud_evt_callout_device = ""; // VARCHAR->String

	public String fraud_evt_callin_device = ""; // VARCHAR->String

	public String fraud_evt_call_country = ""; // VARCHAR->String

	public String fraud_evt_record_type = ""; // VARCHAR->String

	public String fraud_charge_prepay = ""; // VARCHAR->String

	public String fraud_charge = ""; // VARCHAR->String

	public String fraud_comments = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String cat_id = "";
	public String cat_type = "";
	public String cat_num = "";

}
