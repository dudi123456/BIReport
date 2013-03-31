package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGActSwitchTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6543075988298165781L;

	/**
	 * 分析月份
	 */
	public String gather_mon = "";

	/**
	 * 地市等级
	 */
	public String city_lvl = "";

	/**
	 * 地市等级描述
	 */
	public String city_lvl_desc = "";

	/**
	 * 考核期
	 */
	public String check_time = "";

	/**
	 * 考核期描述
	 */
	public String check_time_desc = "";

	/**
	 * 发展计算
	 */
	public String unum_desc = "";

	/**
	 * 收益计算
	 */
	public String income_desc = "";

	/**
	 * 发展分位点1
	 */
	public String unum_dot1 = "";

	/**
	 * 发展分位点2
	 */
	public String unum_dot2 = "";

	/**
	 * 收益分位点1
	 */
	public String income_dot1 = "";

	/**
	 * 收益分位点2
	 */
	public String income_dot2 = "";

	/**
	 * 分值步进
	 */
	public String operator = "";

	/**
	 * 发展对应字段
	 */
	public String new_field_name = "";

	/**
	 * 发展对应字段描述
	 */
	public String new_field_desc = "";
	/**
	 * 收益对应字段
	 */
	public String income_field_name = "";

	/**
	 * 收益对应字段描述
	 */
	public String income_field_desc = "";
	/**
	 * 是否有效
	 */
	public String use_tag = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof FmGActSwitchTable))
			return false;
		FmGActSwitchTable tmpObj = (FmGActSwitchTable) obj;
		return this.gather_mon.equals(tmpObj.gather_mon)
				&& this.city_lvl.equals(tmpObj.city_lvl)
				&& this.check_time.equals(tmpObj.check_time);
	}

	public int hashCode() {
		return this.gather_mon.hashCode() + this.city_lvl.hashCode()
				+ this.check_time.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("gather_mon = ").append(this.gather_mon);

		sb.append("city_lvl = ").append(this.city_lvl);

		sb.append("city_lvl_desc = ").append(this.city_lvl_desc);

		sb.append("check_time = ").append(this.check_time);

		sb.append("check_time_desc = ").append(this.check_time_desc);

		sb.append("unum_desc = ").append(this.unum_desc);

		sb.append("income_desc = ").append(this.income_desc);

		sb.append("unum_dot1 = ").append(this.unum_dot1);

		sb.append("unum_dot2 = ").append(this.unum_dot2);

		sb.append("income_dot1 = ").append(this.income_dot1);

		sb.append("income_dot2 = ").append(this.income_dot2);

		sb.append("operator = ").append(this.operator);

		sb.append("new_field_name = ").append(this.new_field_name);

		sb.append("income_field_name = ").append(this.income_field_name);

		sb.append("use_tag = ").append(this.use_tag);
		return sb.toString();
	}

}
