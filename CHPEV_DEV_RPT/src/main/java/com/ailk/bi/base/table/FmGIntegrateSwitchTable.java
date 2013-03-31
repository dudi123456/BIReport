package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGIntegrateSwitchTable extends JBTableBase {

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
	 * 静态指标考核权重
	 */
	public String static_weight = "";

	/**
	 * 动态指标考核权重
	 */
	public String act_weight = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof FmGIntegrateSwitchTable))
			return false;
		FmGIntegrateSwitchTable tmpObj = (FmGIntegrateSwitchTable) obj;
		return this.gather_mon.equals(tmpObj.gather_mon)
				&& this.city_lvl.equals(tmpObj.city_lvl);
	}

	public int hashCode() {
		return this.gather_mon.hashCode() + this.city_lvl.hashCode()
				+ super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("gather_mon = ").append(this.gather_mon);

		sb.append("city_lvl = ").append(this.city_lvl);

		sb.append("city_lvl_desc = ").append(this.city_lvl_desc);

		sb.append("static_weight = ").append(this.static_weight);

		sb.append("act_weight = ").append(this.act_weight);
		return sb.toString();
	}

}
