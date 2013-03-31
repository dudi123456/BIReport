package com.ailk.bi.adhoc.domain;

import java.util.List;

@SuppressWarnings({ "rawtypes" })
public class UiAdhocUserContribute {
	/**
	 * 开帐应收
	 */
	public String f_fav_fee = "";
	/**
	 * 优惠
	 */
	public String f_pref_fav_fee = "";
	/**
	 * 优惠比率
	 */
	public String f_pref_fav_fee_ratio = "";
	/**
	 * MOU
	 */
	public String v_base_fee_dura = "";
	/**
	 * ARPM
	 */
	public String arpm = "";
	/**
	 * 近三月ARPU
	 */
	public String thr_avg_arpu = "";
	/**
	 * 近三月MOU
	 */
	public String thr_avg_mou = "";
	/**
	 * 近三月ARPM
	 */
	public String thr_avg_arpm = "";
	/**
	 * 近六个月出帐收入
	 */
	public List sixMonFavFee = null;
	/**
	 * 近六个月1X
	 */
	public List sixMonIX = null;
	/**
	 * 累计欠费
	 */
	public String a_aggarr_fee = "";
	/**
	 * 本期欠费
	 */
	public String a_arr_fee = "";
	/**
	 * 本年欠费
	 */
	public String a_yarr_fee = "";
	/**
	 * 最早欠费帐期
	 */
	public String a_first_arr_mon = "";
	/**
	 * 最近欠费帐期
	 */
	public String a_last_arr_mon = "";
	/**
	 * 欠费帐期数
	 */
	public String a_arr_acct_mon_num = "";
	/**
	 * 本月实收
	 */
	public String r_real_fee = "";
	/**
	 * 实收本金
	 */
	public String r_real_cost = "";
	/**
	 * 实收上期欠费
	 */
	public String r_last_mon_fee = "";
	/**
	 * 实收本年欠费
	 */
	public String r_this_year_fee = "";
}
