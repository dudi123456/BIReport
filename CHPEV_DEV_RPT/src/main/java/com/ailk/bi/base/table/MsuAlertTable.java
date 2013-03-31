package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class MsuAlertTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2048877926381851359L;

	// HERE IS FROM DATABASE

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 预警用途
	public String alert_target = ""; // NUMBER->String

	// 数据统计周期
	public String stat_period = ""; // NUMBER->String

	// 有效开始日期
	public String start_date = ""; // NUMBER->String

	// 有效结束日期
	public String end_date = ""; // NUMBER->String

	// 维度的编码字段名称
	public String dept_id = ""; // VARCHAR->String

	// 和本期 0 同比上期 1 环比上期 2 比较
	public String compare_to = ""; // NUMBER->String

	// 是否是比率值
	public String ratio_value = ""; // VARCHAR->String

	// 预警值上限
	public String high_value = ""; // NUMBER->String

	// 预警值下限
	public String low_value = ""; // NUMBER->String

	// 预警模式 单元格底色 箭头
	public String alert_mode = "";// NUMBER->String

	// 超出上限颜色
	public String rise_color = "";// VARCHAR->String

	// 低于下限颜色
	public String down_color = "";// VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof MsuAlertTable) {
			MsuAlertTable tmpObj = (MsuAlertTable) obj;
			equal = (this.msu_id.equals(tmpObj.msu_id)
					&& this.alert_target.equals(tmpObj.alert_target)
					&& this.start_date.equals(tmpObj.start_date)
					&& this.end_date.equals(tmpObj.end_date) && this.dept_id
					.equals(tmpObj.dept_id));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("msu_id=").append(this.msu_id).append("\n");
		sb.append("alert_target=").append(this.alert_target).append("\n");
		sb.append("start_date=").append(this.start_date).append("\n");
		sb.append("end_date=").append(this.end_date).append("\n");
		sb.append("dept_id=").append(this.dept_id).append("\n");
		sb.append("compare_to=").append(this.compare_to).append("\n");
		sb.append("high_value=").append(this.high_value).append("\n");
		sb.append("low_value=").append(this.low_value).append("\n");
		sb.append("alert_mode=").append(this.alert_mode).append("\n");
		sb.append("rise_color=").append(this.rise_color).append("\n");
		sb.append("down_color=").append(this.down_color).append("\n");
		return super.toString();
	}
}
