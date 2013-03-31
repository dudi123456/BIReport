package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class MIDStUnionCustViewTable extends JBTableBase {
	// HERE IS FROM DATABASE

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 标识
	public String gather_mon = ""; // INTEGER->String 数据时间

	public String user_id = ""; // INTEGER->String 用户标识

	public String cust_id = ""; // INTEGER->String 客户标识

	// 客户属性（背景属性）
	public String cust_name = ""; // VARCHAR->String 客户姓名

	public String home_city_id = ""; // VARCHAR->String 归属城市

	public String home_city_desc = ""; // VARCHAR->String 归属城市名字

	public String home_county_id = ""; // VARCHAR->String 归属县区

	public String home_county_desc = "";// VARCHAR->String 归属县区名字

	public String gender_id = ""; // VARCHAR->String 客户性别

	public String birth_day = ""; // INTEGER->String 出生日期

	public String cert_id = ""; // VARCHAR->String 证件号码

	public String homeaddr = ""; // VARCHAR->String 居住地址

	public String inc_lvl = ""; // VARCHAR->String 收入级别

	public String company_name = ""; // VARCHAR->String 公司名称

	public String marrige = ""; // VARCHAR->String 婚姻状况

	public String occp_knd = ""; // VARCHAR->String 职业类型

	// 客户属性（联系属性）
	public String commaddr = ""; // VARCHAR->String 通信地址

	public String commzip = ""; // VARCHAR->String 通信邮编

	public String contact_name = ""; // VARCHAR->String 联系人姓名

	public String contact_telno = ""; // VARCHAR->String 联系人电话

	// 客户属性（计算属性）
	public String acct_num = ""; // INTEGER->String 帐户数目

	public String phone_num = ""; // INTEGER->String 电话数目

	public String acct_id = ""; // VARCHAR->String 主帐户标识

	public String pay_knd = ""; // VARCHAR->String 主帐户缴费类型

	// 用户属性（基本属性）
	public String svc_knd = ""; // VARCHAR->String 业务类型

	public String svc_knd_desc = ""; // VARCHAR->String 业务类型名称

	public String variety_knd = ""; // VARCHAR->String 品牌类型

	public String variety_knd_desc = "";// VARCHAR->String 品牌类型名称

	public String svc_id = ""; // VARCHAR->String 业务标识

	public String user_knd = ""; // VARCHAR->String 用户类型

	public String user_knd_desc = ""; // VARCHAR->String 用户类型名称

	public String mac_knd = ""; // VARCHAR->String 机器类型

	public String mac_knd_desc = ""; // VARCHAR->String 机器名称

	public String group_id = ""; // VARCHAR->String 客户群代码

	public String group_id_desc = ""; // VARCHAR->String 客户群名称

	public String vip_knd = ""; // VARCHAR->String 大客户类型

	public String vip_knd_desc = ""; // VARCHAR->String 大客户类型描述

	public String pay_mnr = ""; // VARCHAR->String 付费方式编码

	// 用户属性（发展属性）
	public String innet_mnr = ""; // VARCHAR->String 入网方式

	public String innet_way = ""; // VARCHAR->String 入网渠道

	public String innet_way_desc = ""; // VARCHAR->String 入网渠道名称

	public String sale_code = ""; // VARCHAR->String 销售策略

	public String sale_code_desc = ""; // VARCHAR->String 销售策略名称

	public String innet_day = ""; // INTEGER->String 入网时间

	public String acpt_perid = ""; // VARCHAR->String 操作员ID

	public String user_dnnr = ""; // VARCHAR->String 用户套参

	public String user_dnnr_desc = ""; // VARCHAR->String 用户套参名称

	public String off_net_day = ""; // INTEGER->String 离网时间

	// 用户属性（状态属性）
	public String user_sta = ""; // VARCHAR->String 用户状态

	public String user_sta_desc = ""; // VARCHAR->String 用户状态名称

	public String onl_dura = ""; // INTEGER->String 在网时长

	public String stop_dura = ""; // INTEGER->String 停机时长

	public String owed_tag = ""; // VARCHAR->String 是否为欠费用户

	// 业务使用
	public String mon_call_times = ""; // BIGINT->String 本月通话次数

	public String yar_call_times = ""; // BIGINT->String 本年累计通话次数

	public String acc_call_times = ""; // BIGINT->String 累计通话次数

	public String mon_call_dura = ""; // BIGINT->String 本月通话时长（分钟）

	public String yar_call_dura = ""; // BIGINT->String 本年累计通话时长（分钟）

	public String acc_call_dura = ""; // BIGINT->String 累计通话时长（分钟）

	public String mon_base_fee = ""; // DECIMAL->String 本月基本费（元）

	public String mon_toll_fee = ""; // DECIMAL->String 本月长途费（元）

	public String mon_roam_fee = ""; // DECIMAL->String 本月漫游费（元）

	public String mon_avg_times = ""; // INTEGER->String 月均通话次数

	public String mon_avg_dura = ""; // DECIMAL->String 月均通话时长（分钟）

	public String first_lac_id = ""; // VARCHAR->String 本月最多通话基站号

	public String first_lac_times = ""; // INTEGER->String 本月最多基站通话次数

	public String first_lac_dura = ""; // INTEGER->String 本月最多基站通话时长

	public String seco_lac_id = ""; // VARCHAR->String 本月次多基站ID

	public String seco_lac_times = ""; // INTEGER->String 本月次多基站通话次数

	public String seco_lac_dura = ""; // INTEGER->String 本月次多基站通话时长

	public String first_busy_seg = ""; // VARCHAR->String 本月呼叫最集中时段

	public String first_busy_times = "";// INTEGER->String 本月最集中时段通话次数

	public String first_busy_dura = ""; // INTEGER->String 本月最集中时段通话时长

	public String seco_busy_seg = ""; // VARCHAR->String 本月次呼叫集中时段

	public String seco_busy_times = ""; // INTEGER->String 本月次集中时段通话次数

	public String seco_busy_dura = ""; // INTEGER->String 本月次集中时段通话时长

	public String first_opp_id = ""; // VARCHAR->String 通话最多运营商编号

	public String first_opp_desc = ""; // VARCHAR->String 通话最多运营商名称

	public String first_opp_times = ""; // INTEGER->String 通话最多的运营商通话次数（本月）

	public String first_opp_dura = ""; // INTEGER->String 通话最多的运营商通话时长（本月，分钟）

	public String first_opp_svcid = ""; // VARCHAR->String 本月通话次数最多的电话号码

	public String first_opp_vsctimes = "";// INTEGER->String 本月通话次数最多的电话号码通话次数

	public String first_opp_vscdura = "";// INTEGER->String 本月通话次数最多的电话号码通话时长

	public String mon_org0_times = ""; // INTEGER->String 本月主叫次数

	public String mon_org0_dura = ""; // INTEGER->String 本月主叫时长（分钟）

	public String mon_toll_times = ""; // INTEGER->String 本月长途次数

	public String mon_toll_dura = ""; // INTEGER->String 长途话单通话时长（分钟）

	public String mon_ip_charge_dura = "";// INTEGER->String 本月IP长途计费时长

	public String mon_193_charge_dura = "";// INTEGER->String 本月193长途计费时长

	public String mon_pnum = ""; // INTEGER->String 本月通话电话号码数

	public String mon_org0_pnum = ""; // INTEGER->String 本月主叫电话号码数目

	public String mon_org1_pnum = ""; // INTEGER->String 本月被叫电话号码数目

	public String mon_mou = ""; // DECIMAL->String 本月MOU（分钟）

	public String yar_mou = ""; // DECIMAL->String 本年平均MOU（分钟）

	// 帐务属性（应收）
	public String mon_fav_fee = ""; // DECIMAL->String 本月应收总费用

	public String fav_lvl_num = ""; // INTEGER->String 本月应收费用分档代码

	public String com_lvl_num = ""; // INTEGER->String 本月应收通话费分档代码

	public String mon_derate_fee = ""; // DECIMAL->String 本月减免费用

	public String yar_fav_fee = ""; // DECIMAL->String 本年应收总费用

	public String acc_fav_fee = ""; // DECIMAL->String 所有累计应收费用

	public String yar_arpu = ""; // DECIMAL->String 本年平均ARPU

	public String top_fav_month = ""; // INTEGER->String 费用最高月份

	public String top_fav = ""; // DECIMAL->String 费用最高月份费用

	public String bot_fav_month = ""; // INTEGER->String 费用最低月份

	public String bot_fav = ""; // DECIMAL->String 费用最低月份收入

	public String fav_rent_fee = ""; // DECIMAL->String 本月月租费

	public String fav_base_fee = ""; // DECIMAL->String 本月基本费

	public String fav_roam_fee = ""; // DECIMAL->String 本月漫游费

	public String fav_inla_fee = ""; // DECIMAL->String 本月国内长途费用

	public String fav_intr_fee = ""; // DECIMAL->String 本月国际长途费用

	public String fav_gat_fee = ""; // DECIMAL->String 本月港澳台长途费用

	public String fav_ip_fee = ""; // DECIMAL->String 本月IP长途费

	public String fav_193_fee = ""; // DECIMAL->String 本月193长途费

	public String fav_1x_fee = ""; // DECIMAL->String 本月CDMA1X费用

	public String fav_add_fee = ""; // DECIMAL->String 本月增值业务费用

	public String fav_oth_fee = ""; // DECIMAL->String 本月其它费用

	// 帐务属性（实收）
	public String mon_get_fee = ""; // DECIMAL->String 本月实收总费用

	public String mon_get_last_fee = "";// DECIMAL->String 本月实收上月费用

	public String mon_get_year_fee = "";// DECIMAL->String 本月实收本年费用

	public String yar_get_fee = ""; // DECIMAL->String 本年实收总费用

	public String acc_get_fee = ""; // DECIMAL->String 所有累计实收费用

	public String get_rent_fee = ""; // DECIMAL->String 本月实收月租费

	public String get_base_fee = ""; // DECIMAL->String 本月实收基本费

	public String get_roam_fee = ""; // DECIMAL->String 本月实收漫游费

	public String get_inla_fee = ""; // DECIMAL->String 本月实收国内长途费用

	public String get_intr_fee = ""; // DECIMAL->String 本月实收国际长途费用

	public String get_gat_fee = ""; // DECIMAL->String 本月实收港澳台长途费用

	public String get_1x_fee = ""; // DECIMAL->String 本月实收CDMA1X费用

	public String get_add_fee = ""; // DECIMAL->String 本月实收增值业务费用

	public String get_late_fee = ""; // DECIMAL->String 本月实收滞纳金费用

	public String get_oth_fee = ""; // DECIMAL->String 本月实收其它费用

	public String get_accmon_num = ""; // INTEGER->String 本月实收的帐期数目

	public String acc_get_times = ""; // INTEGER->String 累计缴费次数

	// 帐务属性（欠费）
	public String bot_owe_mon = ""; // INTEGER->String 最近欠费帐期

	public String top_owe_mon = ""; // INTEGER->String 最小欠费帐期

	public String owe_month_num = ""; // INTEGER->String 发生欠费的月份数目

	public String owe_len = ""; // INTEGER->String 欠费时长

	public String mon_owe_fee = ""; // DECIMAL->String 本期欠费金额

	public String acc_owe_fee = ""; // DECIMAL->String 累计欠费金额

	public String acc_lvl_num = ""; // DECIMAL->String 累计欠费金额分档

	public String yar_owe_fee = ""; // DECIMAL->String 本年欠费金额

	public String top_owe_month = ""; // INTEGER->String 欠费费用最高月份

	public String top_owe = ""; // DECIMAL->String 欠费费用最高月份费用

	public String bot_owe_month = ""; // INTEGER->String 欠费费用最低月份

	public String bot_owe = ""; // DECIMAL->String 欠费费用最低月份收入

	public String owe_rent_fee = ""; // DECIMAL->String 累计欠月租费

	public String owe_base_fee = ""; // DECIMAL->String 累计欠基本费

	public String owe_roam_fee = ""; // DECIMAL->String 累计欠漫游费

	public String owe_inla_fee = ""; // DECIMAL->String 累计欠国内长途费用

	public String owe_intr_fee = ""; // DECIMAL->String 累计欠国际长途费用

	public String owe_gat_fee = ""; // DECIMAL->String 累计欠港澳台长途费用

	public String owe_1x_fee = ""; // DECIMAL->String 累计欠CDMA1X费用

	public String owe_add_fee = ""; // DECIMAL->String 累计欠增值业务费用

	public String owe_oth_fee = ""; // DECIMAL->String 累计欠其它费用

	public String left_pre_pay = ""; // DECIMAL->String 用户剩余预存款

	public String owe_type = ""; // VARCHAR->String 欠费用户类型

	public String owe_reas_type = ""; // VARCHAR->String 欠费原因类型

	// 异动属性
	public String mon_chg_sta_times = "";// INTEGER->String 本月状态变更次数

	public String acc_chg_sta_times = "";// INTEGER->String 累计状态变更次数

	public String mon_chg_dnn_times = "";// INTEGER->String 本月套参变更次数

	public String acc_chg_dnn_times = "";// INTEGER->String 累计套参变更次数

	public String acc_stop_times = ""; // INTEGER->String 累计停机次数

	public String acc_open_times = ""; // INTEGER->String 累计开机次数

	public String last_chg_sta_day = "";// INTEGER->String 最近状态变更时间

	public String last_chg_dnnr_day = "";// INTEGER->String 最近套参变更时间

	public String first_call_day = ""; // INTEGER->String 首次通话时间

	// 服务属性
	public String call_1860_times = "";// INTEGER->String 本月呼叫1860次数

	public String call_1001_times = "";// INTEGER->String 本月呼叫1001次数

	public String call_1000_times = "";// INTEGER->String 本月呼叫1000次数

	public String ask_times = ""; // INTEGER->String 本月咨询次数

	public String accuse_times = ""; // INTEGER->String 本月投诉次数

	// 流失属性
	public String mon_leave_rate = ""; // DECIMAL->String 流失概率

	public String mon_cheat_rate = ""; // DECIMAL->String 欺诈概率

	public String mon_tran_times = ""; // INTEGER->String 本月呼转次数

	public String mon_tran_dura = ""; // INTEGER->String 本月呼转时长（分钟）

	public String mon_tran_base_fee = "";// DECIMAL->String 本月呼转基本费（分钟）

	// HERE IS USER DEFINE 这一行不要删除！
	public String other_fee = "";

	public String age_lvl1_code = "";

	public String age_lvl1_desc = "";

	public String is_innet = "";

	public String on_dura_lvl1 = "";

	public String on_dura_lvl1_desc = "";

	public String stop_dura_lvl1 = "";

	public String stop_dura_lvl1_desc = "";

	public String call_times_lvl1 = "";

	public String call_times_lvl1_desc = "";

	public String call_dura_lvl1 = "";

	public String call_dura_lvl1_desc = "";

	public String mon_call_fee = "";

	public String mon_sms_times = "";

	public String mon_sms_fee = "";

	public String mon_sms_comfee = "";

	public String mon_sms_infofee = "";

	public String mon_1x_times = "";

	public String mon_1x_famount = "";

	public String mon_1x_fee = "";

	public String mon_fav_flag = "";

	public String fav_fee_lvl1 = "";

	public String fav_fee_lvl1_desc = "";

	public String com_fee_lvl1 = "";

	public String com_fee_lvl1_desc = "";

	public String mon_get_02_fee = "";

	public String mon_get_03_fee = "";

	public String mon_get_06_fee = "";

	public String mon_get_12_fee = "";

	public String mon_get_99_fee = "";

	public String owe_len_lvl1 = "";

	public String owe_len_lvl1_desc = "";

	public String acc_lvl1_code = "";

	public String acc_lvl1_desc = "";

	public String owe_02_fee = "";

	public String owe_03_fee = "";

	public String owe_04_fee = "";

	public String owe_05_fee = "";

	public String owe_06_fee = "";

	public String owe_12_fee = "";

	public String owe_18_fee = "";

	public String owe_24_fee = "";

	public String owe_36_fee = "";

	public String owe_99_fee = "";

	public String chr_field_01 = "";

	public String chr_field_02 = "";

	public String dec_field_01 = "";

	public String dec_field_02 = "";

	public String dec_field_03 = "";
}
