package com.ailk.bi.leader.struct;

import com.ailk.bi.common.event.JBTableBase;

public class LeaderKpiInfoStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String day_id = "";// 分析日期

	public String month_id = "";// 分析月份

	public String msu_id = "";// 指标编码

	public String msu_name = "";// 指标名称

	public String unit = "";// 单位

	public int digit = 0;// 精度

	public String parent_id = ""; // 上级指标

	public String msu_desc = "";

	public String msu_type = "";

	public String msu_code = "";

	public String status = "";

	public String sequence = "";

	public String day_same_limit = "";

	public String day_last_limit = "";

	public String day_agg_last_limit = "";

	public String day_agg_same_limit = "";

	public String month_same_limit = "";

	public String month_last_limit = "";

	public String month_agg_last_limit = "";

	public String month_agg_same_limit = "";

	public String msu_belong = "";

	public String msu_knd = "";

	public String msu_cycle = "";

	public String cur_value = "";// 本期值

	public String last_value = "";// 上期值

	public String add_value = "";// 净增 （本期值-上期值）

	public String last_per = "";// 环比

	public String same_value = "";// 同期值

	public String same_add_value = "";// 同期净增 （本期值-上期值）

	public String same_per = "";// 同比

	public String agg_value = "";// 累计值

	public String last_agg_value = "";// 累计值

	public String same_agg_value = "";// 同期累计值

	public String agg_last_per = "";// 累计环比

	public String agg_same_per = "";// 累计同期比

	public String getDay_id() {
		return day_id;
	}

	public void setDay_id(String day_id) {
		this.day_id = day_id;
	}

	public String getMonth_id() {
		return month_id;
	}

	public void setMonth_id(String month_id) {
		this.month_id = month_id;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getMsu_desc() {
		return msu_desc;
	}

	public void setMsu_desc(String msu_desc) {
		this.msu_desc = msu_desc;
	}

	public String getMsu_type() {
		return msu_type;
	}

	public void setMsu_type(String msu_type) {
		this.msu_type = msu_type;
	}

	public String getMsu_code() {
		return msu_code;
	}

	public void setMsu_code(String msu_code) {
		this.msu_code = msu_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getDay_same_limit() {
		return day_same_limit;
	}

	public void setDay_same_limit(String day_same_limit) {
		this.day_same_limit = day_same_limit;
	}

	public String getDay_last_limit() {
		return day_last_limit;
	}

	public void setDay_last_limit(String day_last_limit) {
		this.day_last_limit = day_last_limit;
	}

	public String getDay_agg_last_limit() {
		return day_agg_last_limit;
	}

	public void setDay_agg_last_limit(String day_agg_last_limit) {
		this.day_agg_last_limit = day_agg_last_limit;
	}

	public String getDay_agg_same_limit() {
		return day_agg_same_limit;
	}

	public void setDay_agg_same_limit(String day_agg_same_limit) {
		this.day_agg_same_limit = day_agg_same_limit;
	}

	public String getMonth_same_limit() {
		return month_same_limit;
	}

	public void setMonth_same_limit(String month_same_limit) {
		this.month_same_limit = month_same_limit;
	}

	public String getMonth_last_limit() {
		return month_last_limit;
	}

	public void setMonth_last_limit(String month_last_limit) {
		this.month_last_limit = month_last_limit;
	}

	public String getMonth_agg_last_limit() {
		return month_agg_last_limit;
	}

	public void setMonth_agg_last_limit(String month_agg_last_limit) {
		this.month_agg_last_limit = month_agg_last_limit;
	}

	public String getMonth_agg_same_limit() {
		return month_agg_same_limit;
	}

	public void setMonth_agg_same_limit(String month_agg_same_limit) {
		this.month_agg_same_limit = month_agg_same_limit;
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

	public String getMsu_cycle() {
		return msu_cycle;
	}

	public void setMsu_cycle(String msu_cycle) {
		this.msu_cycle = msu_cycle;
	}

	public String getCur_value() {
		return cur_value;
	}

	public void setCur_value(String cur_value) {
		this.cur_value = cur_value;
	}

	public String getLast_value() {
		return last_value;
	}

	public void setLast_value(String last_value) {
		this.last_value = last_value;
	}

	public String getAdd_value() {
		return add_value;
	}

	public void setAdd_value(String add_value) {
		this.add_value = add_value;
	}

	public String getLast_per() {
		return last_per;
	}

	public void setLast_per(String last_per) {
		this.last_per = last_per;
	}

	public String getSame_value() {
		return same_value;
	}

	public void setSame_value(String same_value) {
		this.same_value = same_value;
	}

	public String getSame_add_value() {
		return same_add_value;
	}

	public void setSame_add_value(String same_add_value) {
		this.same_add_value = same_add_value;
	}

	public String getSame_per() {
		return same_per;
	}

	public void setSame_per(String same_per) {
		this.same_per = same_per;
	}

	public String getAgg_value() {
		return agg_value;
	}

	public void setAgg_value(String agg_value) {
		this.agg_value = agg_value;
	}

	public String getLast_agg_value() {
		return last_agg_value;
	}

	public void setLast_agg_value(String last_agg_value) {
		this.last_agg_value = last_agg_value;
	}

	public String getSame_agg_value() {
		return same_agg_value;
	}

	public void setSame_agg_value(String same_agg_value) {
		this.same_agg_value = same_agg_value;
	}

	public String getAgg_last_per() {
		return agg_last_per;
	}

	public void setAgg_last_per(String agg_last_per) {
		this.agg_last_per = agg_last_per;
	}

	public String getAgg_same_per() {
		return agg_same_per;
	}

	public void setAgg_same_per(String agg_same_per) {
		this.agg_same_per = agg_same_per;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
