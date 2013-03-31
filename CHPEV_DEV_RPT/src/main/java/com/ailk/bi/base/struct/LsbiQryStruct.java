package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class LsbiQryStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8531989683818103949L;

	public String gather_day = ""; // 统计日

	public String gather_mon = ""; // 统计月

	public String begin_mon = ""; // 统计月

	public String end_mon = ""; // 统计月

	public String begin_day = ""; // 起始日期

	public String end_day = ""; // 起始日期

	public String oper_freq = ""; // 操作频次

	public String result = ""; // 操作结果

	public String sess_layer = ""; // 操作层次名称

	public String oper_name = ""; // 操作层次名称

	public String oper_no = ""; // 操作层次名称

	public String obj_id = ""; // 资源标识

	public String obj_name = ""; // 资源名称

	public String back_id = ""; // 反馈标识

	public String reback_id = ""; // 回复标识

	public String back_status = ""; // 反馈标识

	public String hot_type = ""; // 热点类型

	public String cycle = ""; // 统计周期

	public String status = ""; // 状态标记

	// 报表中心
	public String visible_data = "N";// 报表数据是否可见

	public String region_id_r = "";// 报表选择数据区域

	public String date_s = "";// 数据开始日期

	public String date_e = "";// 数据结束日期

	public String divcity_flag = "";// 是否分区域查看

	public String row_flag_r = "";// 指标横向还是竖向展示

	public String dim0 = "";// 条件1

	public String dim1 = "";// 条件2

	public String dim2 = "";// 条件3

	public String dim3 = "";// 条件4

	public String dim4 = "";// 条件5

	public String dim5 = "";// 条件6

	public String dim6 = "";// 条件7

	public String dim7 = "";// 条件8

	public String dim8 = "";// 条件9

	public String dim9 = "";// 条件10

	public String dim10 = "";// 条件11

	public String dim11 = "";// 条件12

	public String dim12 = "";// 条件13

	public String dim13 = "";// 条件14

	public String dim14 = "";// 条件15

	public String sec_area_id = "";// 片区

	public String channel_id = "";// 条件10

	public String order_code = "";

	public String order = "";

	// 报表中心结束

	// 运营监控增加
	public String city_id = "";// 行区

	public String county_id = "";// 分公司

	public String area_id = "";// 分部

	public String oper_type = "";// 操作类型

	public String region_id = "";// 区域ID

	public String region_name = "";// 区域名称

	public String region_level = "";// 区域等级

	public String svc_level = "";// 抽象业务等级

	public String svc_knd = ""; // 业务类型

	public String svcknd_desc = ""; // 业务类型

	public String vprepay_knd = ""; // 业务类型描述

	public String vprepayknd_desc = ""; // 智能网类型描述

	public String svc_knd_lvl = ""; // 业务大类

	public String svckndlvl_desc = ""; // 业务大类描述

	public String svc_name = "";// 抽象业务名称

	public String user_id = ""; // 用户编号

	public String data_rights = ""; // 数据权限

	public String call_type = "";// 话务类型

}