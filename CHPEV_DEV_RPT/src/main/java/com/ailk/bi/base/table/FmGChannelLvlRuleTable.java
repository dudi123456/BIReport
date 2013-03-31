package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGChannelLvlRuleTable extends JBTableBase {

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
	 * 代理商级别
	 */
	public String channel_lvl = "";

	/**
	 * 代理商级别描述
	 */
	public String channel_lvl_desc = "";

	/**
	 * 阈值1
	 */
	public String val1 = "";

	/**
	 * 阈值2
	 */
	public String val2 = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof FmGChannelLvlRuleTable))
			return false;
		FmGChannelLvlRuleTable tmpObj = (FmGChannelLvlRuleTable) obj;
		return this.gather_mon.equals(tmpObj.gather_mon)
				&& this.city_lvl.equals(tmpObj.city_lvl)
				&& this.channel_lvl.equals(tmpObj.channel_lvl);
	}

	public int hashCode() {
		return this.gather_mon.hashCode() + this.city_lvl.hashCode()
				+ this.channel_lvl.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("gather_mon = ").append(this.gather_mon);

		sb.append("city_lvl = ").append(this.city_lvl);

		sb.append("city_lvl_desc = ").append(this.city_lvl_desc);

		sb.append("channel_lvl = ").append(this.channel_lvl);

		sb.append("channel_lvl_desc = ").append(this.channel_lvl_desc);
		sb.append("val1 = ").append(this.val1);
		sb.append("val2 = ").append(this.val2);
		return sb.toString();
	}

}
