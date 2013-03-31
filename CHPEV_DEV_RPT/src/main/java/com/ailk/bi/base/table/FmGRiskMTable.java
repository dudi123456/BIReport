package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class FmGRiskMTable extends JBTableBase implements Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8986118757363220553L;

	// HERE IS FROM DATABASE
	public String gather_mon = ""; // NUMBER->String

	public String svc_lvl = ""; // VARCHAR->String

	public String svc_lvl_desc = ""; // VARCHAR->String

	public String city_id = ""; // VARCHAR->String

	public String city_desc = ""; // VARCHAR->String

	public String county_id = ""; // VARCHAR->String

	public String county_desc = ""; // VARCHAR->String

	public String channel_type = ""; // VARCHAR->String

	public String channeltype_desc = ""; // VARCHAR->String

	public String channel_id = ""; // VARCHAR->String

	public String channel_desc = ""; // VARCHAR->String

	public String onl_perc = ""; // NUMBER->String

	public String total_unum = ""; // NUMBER->String

	public String new_unum = ""; // NUMBER->String

	public String last1_new_unum = ""; // NUMBER->String

	public String onl_unum = ""; // NUMBER->String

	public String off_unum = ""; // NUMBER->String

	public String mon3_add_unum = ""; // NUMBER->String

	public String dev_perc = ""; // NUMBER->String

	public String dev_minus = ""; // NUMBER->String

	public String aver_dev_perc = ""; // NUMBER->String

	public String last1_dev_perc = ""; // NUMBER->String

	public String last1_dev_minus = ""; // NUMBER->String

	public String last1_aver_dev_perc = ""; // NUMBER->String

	public String last2_dev_perc = ""; // NUMBER->String

	public String last2_dev_minus = ""; // NUMBER->String

	public String last2_aver_dev_perc = ""; // NUMBER->String

	public String mon3_new_unum = ""; // NUMBER->String

	public String mon3_off_unum = ""; // NUMBER->String

	public String mon3_off_perc = ""; // NUMBER->String

	public String mon3_off_minus = ""; // NUMBER->String

	public String aver_mon3_off_minus = ""; // NUMBER->String

	public String bill_unum = ""; // NUMBER->String

	public String fav_fee_tach = ""; // NUMBER->String

	public String fav_fee = ""; // NUMBER->String

	public String last1_fav_fee = ""; // NUMBER->String

	public String arpu_tach = ""; // NUMBER->String

	public String arpu = ""; // NUMBER->String

	public String last1_arpu = ""; // NUMBER->String

	public String arrfee_perc = ""; // NUMBER->String

	public String arrfee_minus = ""; // NUMBER->String

	public String aver_arrfee_perc = ""; // NUMBER->String

	public String arr_fee = ""; // NUMBER->String

	public String stop_perc = ""; // NUMBER->String

	public String stop_tach = ""; // NUMBER->String

	public String stop_unum = ""; // NUMBER->String

	public String over3_stop_perc = ""; // NUMBER->String

	public String mon3_low_perc = ""; // NUMBER->String

	public String mon3_dlowunum = ""; // NUMBER->String

	public String low_unum = ""; // NUMBER->String

	public String low_onl_perc = ""; // NUMBER->String

	public String new_low_perc = ""; // NUMBER->String

	public String mon3_zero_perc = ""; // NUMBER->String

	public String mon3_dzunum = ""; // NUMBER->String

	public String zero_unum = ""; // NUMBER->String

	public String zero_onl_perc = ""; // NUMBER->String

	public String new_zero_perc = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	/*
	 * 01 发展突增 02 发展突降 03 低存留比 04 高离网率 05 高停机率 06 高欠费率 07 出帐收入降低 08 高低通话占比 09
	 * 高零通话占比 10 ARPU降低 11 发展连续下降
	 */
	public String dev_up = "";

	public String dev_down = "";

	public String low_onl_rate = "";

	public String high_off_rate = "";

	public String high_stop_rate = "";

	public String high_owe_rate = "";

	public String low_fav_fee = "";

	public String low_unum_rate = "";

	public String zero_unum_rate = "";

	public String arpu_perc = "";

	public String cx_down = "";

	public String total = "";

	public String type = "";

	public String risk_knd = "";

	public String riskknd_desc = "";

	public String risk_num = "";

	public int compareTo(Object o) {

		return 0;
	}

	public String lose_perc = "";

}
