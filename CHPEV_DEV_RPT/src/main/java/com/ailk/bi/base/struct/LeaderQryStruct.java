package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class LeaderQryStruct extends JBTableBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String gather_day = ""; // 统计日

	public String date_s = "";// 数据开始日期

	public String date_e = "";// 数据结束日期

	public String gather_mon = ""; // 统计月

	public String begin_mon = ""; // 统计月

	public String end_mon = ""; // 统计月

	public String begin_day = ""; // 起始日期

	public String end_day = ""; // 起始日期

	public String cust_group_id = ""; // 客户分群

	public String cust_group_name = ""; // 客户分群

	public String group_type = ""; // 客户分群类型

	public String msu_id = ""; // 客户分群类型

	public String msu_name = ""; // 客户分群类型

	public String area_id = ""; // 地域编码

	public String area_name = ""; // 地域名称

	public String svc_id = ""; // 服务类型编码

	public String svc_name = ""; // 服务类型名称

	public String data_source = ""; // 客户群 指标数据源

	public String data_cycle = ""; // 指标分析周期

	public String city_id = ""; // 指标分析周期

	public String city_name = ""; //

	public String msu_type = ""; // 指标类型

	public String msu_belong = ""; // 指标归属

	public String msu_knd = ""; // 指标特征

	public String msu_desc = ""; // 指标描述

	public String getGather_day() {
		return gather_day;
	}

	public void setGather_day(String gather_day) {
		this.gather_day = gather_day;
	}

	public String getDate_s() {
		return date_s;
	}

	public void setDate_s(String date_s) {
		this.date_s = date_s;
	}

	public String getDate_e() {
		return date_e;
	}

	public void setDate_e(String date_e) {
		this.date_e = date_e;
	}

	public String getGather_mon() {
		return gather_mon;
	}

	public void setGather_mon(String gather_mon) {
		this.gather_mon = gather_mon;
	}

	public String getBegin_mon() {
		return begin_mon;
	}

	public void setBegin_mon(String begin_mon) {
		this.begin_mon = begin_mon;
	}

	public String getEnd_mon() {
		return end_mon;
	}

	public void setEnd_mon(String end_mon) {
		this.end_mon = end_mon;
	}

	public String getBegin_day() {
		return begin_day;
	}

	public void setBegin_day(String begin_day) {
		this.begin_day = begin_day;
	}

	public String getEnd_day() {
		return end_day;
	}

	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}

	public String getCust_group_id() {
		return cust_group_id;
	}

	public void setCust_group_id(String cust_group_id) {
		this.cust_group_id = cust_group_id;
	}

	public String getCust_group_name() {
		return cust_group_name;
	}

	public void setCust_group_name(String cust_group_name) {
		this.cust_group_name = cust_group_name;
	}

	public String getGroup_type() {
		return group_type;
	}

	public void setGroup_type(String group_type) {
		this.group_type = group_type;
	}

	public String getMsu_id() {
		return msu_id;
	}

	public void setMsu_id(String msu_id) {
		this.msu_id = msu_id;
	}

	public String getMsu_name() {
		return msu_name;
	}

	public void setMsu_name(String msu_name) {
		this.msu_name = msu_name;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getSvc_id() {
		return svc_id;
	}

	public void setSvc_id(String svc_id) {
		this.svc_id = svc_id;
	}

	public String getSvc_name() {
		return svc_name;
	}

	public void setSvc_name(String svc_name) {
		this.svc_name = svc_name;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	public String getData_cycle() {
		return data_cycle;
	}

	public void setData_cycle(String data_cycle) {
		this.data_cycle = data_cycle;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getMsu_belong() {
		return msu_belong;
	}

	public void setMsu_belong(String msu_belong) {
		this.msu_belong = msu_belong;
	}

	public String getMsu_knd() {
		return msu_knd;
	}

	public void setMsu_knd(String msu_knd) {
		this.msu_knd = msu_knd;
	}

	public String getMsu_type() {
		return msu_type;
	}

	public void setMsu_type(String msu_type) {
		this.msu_type = msu_type;
	}

	public String getMsu_desc() {
		return msu_desc;
	}

	public void setMsu_desc(String msuDesc) {
		msu_desc = msuDesc;
	}
}
