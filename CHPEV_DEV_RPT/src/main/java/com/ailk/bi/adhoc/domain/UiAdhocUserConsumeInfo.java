package com.ailk.bi.adhoc.domain;

import java.util.List;

@SuppressWarnings({ "rawtypes" })
public class UiAdhocUserConsumeInfo {

	/**
	 * 本月通话次数
	 */
	public String v_call_times = "";
	/**
	 * 本月日均次数
	 */
	public String v_d_avg_call_times = "";
	/**
	 * 本月通话时长
	 */
	public String v_call_dura = "";
	/**
	 * 本月日均通话时长
	 */
	public String v_d_avg_dura = "";

	/**
	 * 通话习惯是本地还是漫游为主
	 */
	public String local_roam = "";
	/**
	 * 通话习惯是忙时还是闲时为主
	 */
	public String busy_spare = "";
	/**
	 * 本月通话基站数
	 */
	public String v_lac_num = "";
	/**
	 * 本月通话次数最多基站
	 */
	public String v_fst_lac_desc = "";
	/**
	 * 本基站通话次数
	 */
	public String v_fst_lac_times = "";
	/**
	 * 本基站通话时长
	 */
	public String v_fst_lac_dura = "";
	/**
	 * 本月通话次数最多时段
	 */
	public String v_fst_busy_seg = "";
	/**
	 * 本时段通话次数
	 */
	public String v_fst_busy_times = "";
	/**
	 * 本时段通话时长
	 */
	public String v_fst_busy_dura = "";
	/**
	 * 本月通话次数最多运营商
	 */
	public String v_fstcarrier_desc = "";
	/**
	 * 与该运营商通话次数
	 */
	public String v_fstcarrier_times = "";
	/**
	 * 与该运营商通话时长
	 */
	public String v_fstcarrier_dura = "";
	/**
	 * 互转次数
	 */
	public String v_cf_times = "";
	/**
	 * 占通话次数比率
	 */
	public String cf_times_ratio = "";
	/**
	 * 呼转时长
	 */
	public String v_cf_dura = "";
	/**
	 * 本月呼转最多号码
	 */
	public String v_max_cf_svc_id = "";
	/**
	 * 呼转该号码次数
	 */
	public String v_max_cf_times = "";
	/**
	 * 呼转该号码时长
	 */
	public String v_max_cf_dura = "";
	/**
	 * 本月网内呼转次数
	 */
	public String v_i_cf_times = "";
	/**
	 * 本月网内呼转时长
	 */
	public String v_i_cf_dura = "";
	/**
	 * 本月网间呼转次数
	 */
	public String v_o_cf_times = "";
	/**
	 * 本月网间呼转时长
	 */
	public String v_o_cf_dura = "";
	/**
	 * 用户互转明细
	 */
	public List userCFDetails = null;
	/**
	 * 短信条数
	 */
	public String s_sms_num = "";
	/**
	 * 其中点对点短信占比
	 */
	public String ptp_sms_ratio = "";
	/**
	 * 1X总流量
	 */
	public String s_ix_bytes = "";
	/**
	 * 近六个月增值业务构成
	 */
	public List valueAdds = null;
	/**
	 * 近六个月1X业务
	 */
	public List userIXs = null;
}
