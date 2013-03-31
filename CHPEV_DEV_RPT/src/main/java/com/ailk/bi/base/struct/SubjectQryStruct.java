package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class SubjectQryStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2316364738304958282L;

	/**
	 * 统计日期
	 */
	public String gather_day = "";

	/**
	 * 起始日期
	 */
	public String begin_day = "";

	/**
	 * 结束日期
	 */
	public String end_day = "";

	/**
	 * 统计月份
	 */
	public String gather_mon = "";

	/**
	 * 开始月份
	 */
	public String begin_mon = "";

	/**
	 * 结束月份
	 */
	public String end_mon = "";

	/**
	 * 指标字段
	 */
	public String msu_fld = "";

	/**
	 * 指标字段名称
	 */
	public String msu_name = "";

	/**
	 * 指标精度
	 */
	public String digit_len = "";

	/**
	 * 业务大类
	 */
	public String svc_knd_lvl = "";

	/**
	 * 业务大类描述
	 */
	public String svckndlvl_desc = "";

	/**
	 * 业务类型
	 */
	public String svc_knd = "";

	/**
	 * 业务类型描述
	 */
	public String svcknd_desc = "";

	/**
	 * 地市代码
	 */
	public String city_id = "";

	/**
	 * 地市描述
	 */
	public String city_desc = "";

	/**
	 * 区县代码
	 */
	public String county_id = "";

	/**
	 * 区县描述
	 */
	public String county_desc = "";

	/**
	 * 品牌编码
	 */
	public String brand_code = "";

	/**
	 * 品牌描述
	 */
	public String brandcode_desc = "";

	/**
	 * 品牌
	 */
	public String brand = "";

	/**
	 * 品牌描述
	 */
	public String brand_desc = "";

	/**
	 * 品牌等级
	 */
	public String brand_lvl = "";

	/**
	 * 排行标记
	 */
	public String rank_flag = "";

	/**
	 * 付费方式
	 */
	public String pay_type = "";

	/**
	 * 付费方式
	 */
	public String paytype_desc = "";

	/**
	 * 子品牌编码
	 */
	public String sub_brand_knd = "";

	/**
	 * 子品牌描述
	 */
	public String subbrandknd_desc = "";

	/**
	 * 产品编码
	 */
	public String product = "";

	/**
	 * 产品名称
	 */
	public String product_name = "";

	/**
	 * 用户分析定制的产品where串，AND 格式,字段不带A.
	 */
	public String locked_products = "";

	/**
	 * 用户分析定制的产品where串，AND 格式,字段带A.
	 */
	public String locked_products_with_a = "";

	/**
	 * 区分多产品还是单产品分析 取值：取值 ProductCustomUtil.PACK_TYPE_MULTI 0 多产品分析
	 * ProductCustomUtil.PACK_TYPE_SINGLE 1 单产品分析
	 */
	public String pack_type = "";

	/**
	 * 在网用户数环比
	 */
	public String onl_unum_bit = "";

	/**
	 * 新增用户数环比
	 */
	public String net_unum_bit = "";

	/**
	 * 出帐用户数环比
	 */
	public String bill_unum_bit = "";

	/**
	 * 出帐金额环比
	 */
	public String fav_fee_bit = "";

	/**
	 * ARPU环比
	 */
	public String arpu_bit = "";

	/**
	 * 通话用户数环比
	 */
	public String call_unum_bit = "";

	/**
	 * 欠费金额环比
	 */
	public String arrfee_unum_bit = "";

	/**
	 * 低通话用户数环比
	 */
	public String low_call_unum_bit = "";

	/**
	 * 离网用户数环比
	 */
	public String off_unum_bit = "";

	/**
	 * 发展用户数环比
	 */
	public String new_unum_bit = "";

	/**
	 * 主被叫
	 */
	public String call_type = "";

	/**
	 * 漫游类型
	 */
	public String roam_type = "";

	/**
	 * 是否本地
	 */
	public String if_local = "";

	/**
	 * 异动产品
	 */
	public String product_a = "";

	/**
	 * 协议即将到期
	 */
	public String protocol_end = "";

	/**
	 * 即将完成消费
	 */
	public String consume_end = "";

	/**
	 * ARPU分档
	 */
	public String arpu_lvl = "";

	/**
	 * 本地通话费分档
	 */
	public String loc_fee_lvl = "";

	/**
	 * 长途通话费分档
	 */
	public String toll_fee_lvl = "";

	/**
	 * 漫游通话费分档
	 */
	public String roam_fee_lvl = "";

	/**
	 * 短信费分档
	 */
	public String ptp_sms_fee_lvl = "";

	/**
	 * 掌宽流量分档
	 */
	public String hand_net_flux_lvl = "";

	/**
	 * x轴分界1
	 */
	public String splitx_one = "";

	/**
	 * x轴分界2
	 */
	public String splitx_two = "";

	/**
	 * x轴分界3
	 */
	public String splitx_three = "";

	/**
	 * x轴分界4
	 */
	public String splitx_four = "";

	/**
	 * y轴分界1
	 */
	public String splity_one = "";

	/**
	 * y轴分界2
	 */
	public String splity_two = "";

	/**
	 * y轴分界3
	 */
	public String splity_three = "";

	/**
	 * y轴分界4
	 */
	public String splity_four = "";

	/**
	 * 产品专题权限控制,AND 格式,字段不带A.
	 */
	public String product_right = "";

	/**
	 * 产品专题权限控制,AND 格式,字段带A.
	 */
	public String product_right_with_a = "";

	/**
	 * 产品目录
	 */

	/**
	 * 业务类行
	 */
	public String svc_lvl = "";

	/**
	 * 业务类行描述
	 */
	public String svc_lvl_desc = "";

	public String channel_lvl = "";

	public String channel_code = "";

	public String channel_desc = "";

	public String sub_channel_type = "";

	public String subchanneltype_desc = "";

	public String channel_type = "";

	public String channeltype_desc = "";

	public String risk_knd = "";

	/**
	 * 新老用户标志
	 */
	public String new_old_tag = "";

	/**
	 * 产品类型
	 */
	public String product_type = "";

	public String producttype_desc = "";

	// 本年累计发展用户数
	public String agg_dev_unum_a = "";

	public String agg_dev_unum_b = "";

	// 本年累计离网用户数
	public String agg_off_unum_a = "";

	public String agg_off_unum_b = "";

	// 本期离网发展比
	public String off_new_perc_a = "";

	public String off_new_perc_b = "";

	// 发展增长率
	public String dev_perc_a = "";

	public String dev_perc_b = "";

	// 欠费率
	public String owe_perc_a = "";

	public String owe_perc_b = "";

	// 出帐收入环比
	public String fav_perc_a = "";

	public String fav_perc_b = "";

	// 佣金占比
	public String commision_perc_a = "";

	public String commision_perc_b = "";

	public String havingstr = "";

	/**
	 * 零贡献列表
	 */
	public String zero_list = "";

	public String zerolist_desc = "";

	public String zerolist_table = "";

	/**
	 * 客户专题
	 */
	public String cust_lvl = ""; // 客户价值级别

	public String custlvl_desc = ""; // 客户价值级别描述

	// 风险条件
	public String risk_where = "";

	// 渠道名称
	public String channel_name = "";

	// 渠道名称
	public String channel_where = "";

	// 渠道数量
	public String channel_num = "";

	/**
	 * 风险指数
	 */

	public String risk_knd_01 = "";

	public String risk_knd_02 = "";

	public String risk_knd_03 = "";

	public String risk_knd_04 = "";

	public String risk_knd_05 = "";

	public String risk_knd_06 = "";

	public String risk_knd_07 = "";

	public String risk_knd_08 = "";

	public String risk_knd_09 = "";

	public String risk_knd_10 = "";

	public String risk_knd_11 = "";

	public String risk_knd_12 = "";

	public String risk_knd_13 = "";

	public String risk_knd_14 = "";

	public String risk_knd_15 = "";

	public String risk_knd_16 = "";

	public String risk_knd_17 = "";

	public String risk_knd_18 = "";

	public String risk_knd_19 = "";

	public String risk_knd_20 = "";

	// 分群类型id
	public String group_id = "";

	// 分群类型描述
	public String group_desc = "";

	/**
	 * 主集团编号
	 */
	public String corp_no = "";

	/**
	 * 行业类型
	 */
	public String industry_type = "";

	public String industrytype_desc = "";

	// 客户筛选id
	public String custom_id = "";

	// 客户筛选描述
	public String custom_desc = "";

	// 收藏客户级别列表-客户价值得分
	public String seg_code_score = "";

	// 收藏客户级别列表-客户生命周期
	public String seg_code_life = "";

	// 收藏客户级别列表-语音消费成色
	public String seg_code_quty = "";

	// 收藏客户级别列表-增值消费成色
	public String seg_code_add = "";

	// 收藏客户级别列表-年龄分档
	public String seg_code_age = "";

	// 收藏客户级别列表-性别
	public String seg_code_sex = "";

	// 收藏客户级别列表-入网时长分档标识
	public String seg_code_onldura = "";

	// 当前定制方案对应table_id
	public String display_table_id = "";

	public String user_no = "";
}
