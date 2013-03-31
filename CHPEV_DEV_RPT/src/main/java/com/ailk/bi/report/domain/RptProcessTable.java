package com.ailk.bi.report.domain;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.common.event.JBTableBase;

public class RptProcessTable extends JBTableBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String p_id = ""; // VARCHAR->String

	public String p_flag_name = ""; // VARCHAR->String

	public String p_note = ""; // VARCHAR->String

	public String create_time = ""; // VARCHAR->String

	public String status = ""; // VARCHAR->String

	public String status_desc = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String rpt_id = ""; // VARCHAR->String

	public String rpt_name = ""; // VARCHAR->String

	public String cycle = ""; // VARCHAR->String

	public String cycle_desc = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String parent_name = ""; // VARCHAR->String

	/**
	 * 生成查询定义对象
	 *
	 * @param svces
	 * @return
	 */
	public static RptProcessTable genProcessFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptProcessTable processTable = new RptProcessTable();
		int m = 0;
		processTable.p_id = svces[m++];
		processTable.p_flag_name = svces[m++];
		processTable.p_note = svces[m++];
		processTable.create_time = svces[m++];
		processTable.status = svces[m++].trim();
		if (TableConsts.YES.equals(processTable.status))
			processTable.status_desc = "有效";
		else if (TableConsts.NO.equals(processTable.status))
			processTable.status_desc = "无效";
		return processTable;
	}

	/**
	 * 生成查询定义对象
	 *
	 * @param svces
	 * @return
	 */
	public static RptProcessTable genReportProcessFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptProcessTable processTable = new RptProcessTable();
		int m = 0;
		processTable.rpt_id = svces[m++];
		processTable.rpt_name = svces[m++];
		processTable.cycle = svces[m++];
		if (SysConsts.STAT_PERIOD_YEAR.equals(processTable.cycle)) {
			processTable.cycle_desc = "年报";
		} else if (SysConsts.STAT_PERIOD_HALFYEAR.equals(processTable.cycle)) {
			processTable.cycle_desc = "半年报";
		} else if (SysConsts.STAT_PERIOD_QUARTER.equals(processTable.cycle)) {
			processTable.cycle_desc = "季报";
		} else if (SysConsts.STAT_PERIOD_MONTH.equals(processTable.cycle)) {
			processTable.cycle_desc = "月报";
		} else if (SysConsts.STAT_PERIOD_TENDAYS.equals(processTable.cycle)) {
			processTable.cycle_desc = "旬报";
		} else if (SysConsts.STAT_PERIOD_DAY.equals(processTable.cycle)) {
			processTable.cycle_desc = "日报";
		}
		processTable.parent_id = svces[m++];
		String tmpName = svces[m++] + "-";
		if ("报表中心-".equals(tmpName)) {
			tmpName = "";
			tmpName += svces[m++] + "-";
			tmpName += svces[m++];
		} else {
			tmpName += svces[m++];
			m++;
		}
		processTable.parent_name = tmpName;
		processTable.p_id = svces[m++];
		processTable.p_flag_name = svces[m++];
		return processTable;
	}
}
