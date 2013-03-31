package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmGStaticSwitchTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8443086090598989300L;

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
	 * 指标描述
	 */
	public String id = "";

	/**
	 * 指标说明
	 */
	public String pref_alarm_desc = "";

	/**
	 * 权重
	 */
	public String weight = "";

	/**
	 * 是否启用
	 */
	public String use_tag = "";

	/**
	 * 对应源字段
	 */
	public String id_cal = "";

	/**
	 * 数据表
	 */
	public String table_name = "";
	/**
	 * 
	 */
	public String val_name = "";
	/**
	 * 对应得分字段
	 */
	public String score_name = "";

	/**
	 * 对应得分字段
	 */
	public String base_score = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof FmGStaticSwitchTable))
			return false;
		FmGStaticSwitchTable tmpObj = (FmGStaticSwitchTable) obj;
		return this.gather_mon.equals(tmpObj.gather_mon)
				&& this.city_lvl.equals(tmpObj.city_lvl)
				&& this.id.equals(tmpObj.id);
	}

	public int hashCode() {
		return this.gather_mon.hashCode() + this.city_lvl.hashCode()
				+ this.id.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(" gather_mon = ").append(this.gather_mon);
		sb.append(" city_lvl = ").append(this.city_lvl);

		sb.append(" city_lvl_desc = ").append(this.city_lvl_desc);

		sb.append(" id = ").append(this.id);

		sb.append(" pref_alarm_desc = ").append(this.pref_alarm_desc);

		sb.append(" weight = ").append(this.weight);

		sb.append(" use_tag = ").append(this.use_tag);

		sb.append(" table_name = ").append(this.table_name);

		sb.append(" score_name = ").append(this.score_name);

		sb.append(" base_score = ").append(this.base_score);
		return sb.toString();
	}

}
