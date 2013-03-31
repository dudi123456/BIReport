package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGIntegrateInfoMTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8736497512277586232L;

	// HERE IS FROM DATABASE
	public String gather_mon = ""; // NUMBER->String

	public String svc_knd_lvl = ""; // VARCHAR->String

	public String svckndlvl_desc = ""; // VARCHAR->String

	public String svc_knd = ""; // VARCHAR->String

	public String svcknd_desc = ""; // VARCHAR->String

	public String city_id = ""; // VARCHAR->String

	public String city_desc = ""; // VARCHAR->String

	public String county_id = ""; // VARCHAR->String

	public String county_desc = ""; // VARCHAR->String

	public String brand_code = ""; // VARCHAR->String

	public String brandcode_desc = ""; // VARCHAR->String

	public String sub_brand_knd = ""; // VARCHAR->String

	public String subbrandknd_desc = ""; // VARCHAR->String

	public String product_type = ""; // VARCHAR->String

	public String producttype_desc = ""; // VARCHAR->String

	public String product = ""; // VARCHAR->String

	public String product_desc = ""; // VARCHAR->String

	public String channel_type = ""; // VARCHAR->String

	public String channeltype_desc = ""; // VARCHAR->String

	public String channel_id = ""; // VARCHAR->String

	public String channel_desc = ""; // VARCHAR->String

	public String channel_addr = ""; // VARCHAR->String

	public String new_unum = ""; // NUMBER->String

	public String off_unum = ""; // NUMBER->String

	public String fresh_off_unum = ""; // NUMBER->String

	public String old_off_unum = ""; // NUMBER->String

	public String onl_unum = ""; // NUMBER->String

	public String stop_unum = ""; // NUMBER->String

	public String call_unum = ""; // NUMBER->String

	public String active_unum = ""; // NUMBER->String

	public String sms_unum = ""; // NUMBER->String

	public String ix_unum = ""; // NUMBER->String

	public String bill_unum = ""; // NUMBER->String

	public String fresh_bill_unum = ""; // NUMBER->String

	public String old_bill_unum = ""; // NUMBER->String

	public String new_add_fav_fee = ""; // NUMBER->String

	public String fav_fee = ""; // NUMBER->String

	public String fresh_fav_fee = ""; // NUMBER->String

	public String old_fav_fee = ""; // NUMBER->String

	public String rent_fee = ""; // NUMBER->String

	public String call_fee = ""; // NUMBER->String

	public String loc_base_fee = ""; // NUMBER->String

	public String loc_toll_fee = ""; // NUMBER->String

	public String roam_fee = ""; // NUMBER->String

	public String roam_base_fee = ""; // NUMBER->String

	public String roam_toll_fee = ""; // NUMBER->String

	public String sms_fee = ""; // NUMBER->String

	public String ix_fee = ""; // NUMBER->String

	public String oth_fav_fee = ""; // NUMBER->String

	public String commision_fee = ""; // NUMBER->String

	public String income_fee = ""; // NUMBER->String

	public String arr_fee = ""; // NUMBER->String

	public String fresh_arr_fee = ""; // NUMBER->String

	public String old_arr_fee = ""; // NUMBER->String

	public String zero_unum = ""; // NUMBER->String

	public String low_unum = ""; // NUMBER->String

	public String real_fee = ""; // NUMBER->String

	public String fresh_real_fee = ""; // NUMBER->String

	public String old_real_fee = ""; // NUMBER->String

	public String settle_fee = ""; // NUMBER->String

	public String fresh_settle_fee = ""; // NUMBER->String

	public String old_settle_fee = ""; // NUMBER->String

	public String add_unum = ""; // NUMBER->String

	public String add_bill_unum = ""; // NUMBER->String

	public String add_fav_fee = ""; // NUMBER->String

	public String new_bill_unum = ""; // NUMBER->String

	public String new_fav_fee = ""; // NUMBER->String

	public String dzero_unum = ""; // NUMBER->String

	public String dlow_unum = ""; // NUMBER->String

	public String new_add_bill_unum = ""; // NUMBER->String

	public String last1_onl_unum = ""; // NUMBER->String

	public String last1_new_unum = ""; // NUMBER->String

	public String last1_off_unum = ""; // NUMBER->String

	public String last1_stop_unum = ""; // NUMBER->String

	public String last1_bill_unum = ""; // NUMBER->String

	public String last1_fav_fee = ""; // NUMBER->String

	public String last1_zunum = ""; // NUMBER->String

	public String last1_low_unum = ""; // NUMBER->String

	public String last2_new_unum = ""; // NUMBER->String

	public String last2_off_unum = ""; // NUMBER->String

	public String last2_stop_unum = ""; // NUMBER->String

	public String last2_zunum = ""; // NUMBER->String

	public String last2_low_unum = ""; // NUMBER->String

	public String mon3_new_unum = ""; // NUMBER->String

	public String mon3_new_bill_unum = ""; // NUMBER->String

	public String mon3_new_fav_fee = ""; // NUMBER->String

	public String mon3_fav_fee = ""; // NUMBER->String

	public String mon3_off_unum = ""; // NUMBER->String

	public String mon3_onl_unum = ""; // NUMBER->String

	public String mon3_doff_unum = ""; // NUMBER->String

	public String mon3_dzunum = ""; // NUMBER->String

	public String mon3_dlowunum = ""; // NUMBER->String

	public String over3_stop_unum = ""; // NUMBER->String

	public String mon6_new_unum = ""; // NUMBER->String

	public String mon6_fav_fee = ""; // NUMBER->String

	public String mon6_new_fav_fee = ""; // NUMBER->String

	public String mon12_new_unum = ""; // NUMBER->String

	public String mon12_fav_fee = ""; // NUMBER->String

	public String mon12_new_fav_fee = ""; // NUMBER->String

	public String y_new_unum = ""; // NUMBER->String

	public String y_off_unum = ""; // NUMBER->String

	public String y_fresh_off_unum = ""; // NUMBER->String

	public String y_old_off_unum = ""; // NUMBER->String

	public String y_fav_fee = ""; // NUMBER->String

	public String fresh_y_fav_fee = ""; // NUMBER->String

	public String old_y_fav_fee = ""; // NUMBER->String

	public String yarr_fee = ""; // NUMBER->String

	public String fresh_yarr_fee = ""; // NUMBER->String

	public String old_yarr_fee = ""; // NUMBER->String

	public String total_unum = ""; // NUMBER->String

	public String total_off_unum = ""; // NUMBER->String

	public String fresh_total_off_unum = ""; // NUMBER->String

	public String old_total_off_unum = ""; // NUMBER->String

	public String agg_arr_fee = ""; // NUMBER->String

	public String fresh_agg_arr_fee = ""; // NUMBER->String

	public String old_agg_arr_fee = ""; // NUMBER->String

	public String sub_channel_type = ""; // VARCHAR->String

	public String subchanneltype_desc = ""; // VARCHAR->String

	public String cs_fix_fee = ""; // NUMBER->String

	public String cs_perc_fee = ""; // NUMBER->String

	public String cs_timepay_fee = ""; // NUMBER->String

	public String cs_agent_fee = ""; // NUMBER->String

	public String cs_bounty_fee = ""; // NUMBER->String

	public String cs_cut_fee = ""; // NUMBER->String

	public String cs_oth_fee = ""; // NUMBER->String

	public String y_commision_fee = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public String off_new_per = "";
	public String arpu = "";

	public String commision_per = "";

}
