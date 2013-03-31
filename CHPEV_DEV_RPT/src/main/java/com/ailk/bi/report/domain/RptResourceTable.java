package com.ailk.bi.report.domain;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.JBTableBase;
import com.ailk.bi.report.util.ReportConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptResourceTable extends JBTableBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE

	public String res_id = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String parent_name = ""; // VARCHAR->String

	public String url = ""; // NUMBER->String

	public String rpt_id = ""; // VARCHAR->String，同rpt_id

	public String local_res_code = ""; // VARCHAR->String

	public String code = ""; // VARCHAR->String

	public String name = ""; // VARCHAR->String

	public String cycle = ""; // NUMBER->String

	public String cycle_desc = ""; // NUMBER->String

	public String title_note = ""; // NUMBER->String

	public String filldept = ""; // VARCHAR->String

	public String fillperson = ""; // VARCHAR->String

	public String makedept = ""; // VARCHAR->String

	public String makeperson = ""; // VARCHAR->String

	public String needdept = ""; // VARCHAR->String

	public String needperson = ""; // VARCHAR->String

	public String needreason = ""; // VARCHAR->String

	public String inputnote = ""; // VARCHAR->String

	public String rpt_type = ""; // NUMBER->String

	public String start_date = ""; // NUMBER->String

	public String divcity_flag = ""; // NUMBER->String

	public String row_flag = ""; // NUMBER->String

	public String pagecount = ""; // NUMBER->String

	public String ishead = ""; // NUMBER->String

	public String isleft = ""; // NUMBER->String

	public String sms_flag = ""; // NUMBER->String

	public String startcol = ""; // NUMBER->String

	public String processflag = ""; // NUMBER->String

	public String dispenseflag = ""; // NUMBER->String

	public String metaflag = ""; // NUMBER->String

	public String privateflag = ""; // NUMBER->String

	public String d_user_id = ""; // NUMBER->String

	public String data_table = ""; // VARCHAR->String

	public String data_where = ""; // VARCHAR->String

	public String data_order = ""; // VARCHAR->String

	public String data_sequence = ""; // VARCHAR->String

	public String data_date = ""; // VARCHAR->String

	public String rpt_export_rule = "";// 报表导出方式

	public String status = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	public boolean displaydate = false;// 是否显示日期字段

	public String data_where_sql = "";// 查询条件

	public String data_sequence_code = "";// 数据排序字段

	public String preview_data = "";// 是否预览报表

	public String print_code = "";// 报表打印码

	public String print_time = "";// 报表打印时间

	public String print_userid = "";// 打印用户id

	public String print_username = "";// 打印用户名称

	public String rpt_operate = "";// 报表操作类型

	public String r_role_id = "";// 报表发布的角色id

	public String r_role_type = "";// 报表发布的角色类型

	public RptGroupTable[] rpt_group = null;// 报表批量导出的归属分组

	public String rp_status = ""; // NUMBER->String

	public String rp_engine = ""; // NUMBER->String

	public String rp_bottom = "";

	/**
	 * 生成报表对象
	 *
	 * @param svces
	 * @param arrGroup
	 * @return
	 */
	public static RptResourceTable genReportFromArray(String[] svces,
			String[][] arrGroup) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptResourceTable report = new RptResourceTable();
		int m = 0;
		report.rpt_id = svces[m++];
		report.res_id = report.rpt_id;
		report.parent_id = svces[m++];
		report.parent_name = svces[m++];
		report.url = svces[m++];

		report.local_res_code = svces[m++];
		report.code = svces[m++];
		report.name = svces[m++];
		report.cycle = svces[m++];
		if (SysConsts.STAT_PERIOD_YEAR.equals(report.cycle)) {
			report.cycle_desc = "年";
		} else if (SysConsts.STAT_PERIOD_HALFYEAR.equals(report.cycle)) {
			report.cycle_desc = "半年";
		} else if (SysConsts.STAT_PERIOD_QUARTER.equals(report.cycle)) {
			report.cycle_desc = "季报";
		} else if (SysConsts.STAT_PERIOD_MONTH.equals(report.cycle)) {
			report.cycle_desc = "月报";
		} else if (SysConsts.STAT_PERIOD_TENDAYS.equals(report.cycle)) {
			report.cycle_desc = "旬报";
		} else if (SysConsts.STAT_PERIOD_DAY.equals(report.cycle)) {
			report.cycle_desc = "日报";
		}

		report.title_note = svces[m++];
		report.filldept = svces[m++];
		report.fillperson = svces[m++];
		report.makedept = svces[m++];
		report.makeperson = svces[m++];
		report.needdept = svces[m++];
		report.needperson = svces[m++];
		report.needreason = svces[m++];
		report.inputnote = svces[m++];

		report.rpt_type = svces[m++];
		report.start_date = svces[m++];
		report.divcity_flag = svces[m++];
		report.row_flag = svces[m++];
		report.pagecount = svces[m++];
		report.ishead = svces[m++].trim();
		report.isleft = svces[m++].trim();
		report.sms_flag = svces[m++].trim();
		report.startcol = svces[m++];

		report.processflag = svces[m++].trim();
		report.dispenseflag = svces[m++].trim();
		report.metaflag = svces[m++].trim();
		report.privateflag = svces[m++].trim();
		report.d_user_id = svces[m++];

		report.data_table = svces[m++];
		report.data_where = svces[m++];
		report.data_order = svces[m++];
		report.data_sequence = svces[m++];
		report.data_date = svces[m++];
		report.rpt_export_rule = svces[m++];
		report.status = svces[m++].trim();
		report.rp_status = svces[m++];
		report.rp_engine = svces[m++];

		// 报表排序字段代码

		if (StringTool.checkEmptyString(report.data_sequence)) {
			report.data_sequence_code = "";// ReportConsts.RPT_SEQUENCE_CODE;
		} else {
			report.data_sequence_code = report.data_sequence;
		}

		// 应用于报表发布
		int iExtend = m++;
		if (iExtend < svces.length) {
			report.r_role_id = svces[m++];
			report.r_role_type = svces[m++];
		}

		// once more
		report.row_flag = getRowFlag(report);
		report.displaydate = getdisplayDate(report);

		// 报表导出分组
		RptGroupTable[] _group = null;
		if (arrGroup != null) {
			List listGroup = new ArrayList();
			for (int i = 0; i < arrGroup.length; i++) {
				if (report.rpt_id.equals(arrGroup[i][0])) {
					RptGroupTable _tmp = RptGroupTable
							.genReportGroupFromArray(arrGroup[i]);
					listGroup.add(_tmp);
				}
			}
			if (listGroup != null && listGroup.size() > 0) {
				_group = (RptGroupTable[]) listGroup
						.toArray(new RptGroupTable[listGroup.size()]);
			}
		}
		report.rpt_group = _group;

		return report;
	}

	/**
	 * 生成报表对象
	 *
	 * @param svces
	 *            一维数组
	 * @return
	 */
	public static RptResourceTable genReportPrintFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptResourceTable report = new RptResourceTable();
		int m = 0;
		report.print_code = svces[m++];
		report.print_time = svces[m++];
		report.rpt_id = svces[m++];
		report.res_id = report.rpt_id;
		report.name = svces[m++];
		report.url = svces[m++];
		report.print_userid = svces[m++];
		report.print_username = svces[m++];

		return report;
	}

	/**
	 * 是否需要显示日期字段
	 *
	 * @param rptTable
	 * @return
	 */
	private static boolean getdisplayDate(RptResourceTable rptTable) {
		boolean isTrue = false;

		String row_flag = getRowFlag(rptTable);
		if (ReportConsts.DIS_DATE_COL.equals(row_flag))
			isTrue = true;
		if (ReportConsts.DIS_ALL_COL.equals(row_flag))
			isTrue = true;

		return isTrue;
	}

	/**
	 * 获取特殊显示标志,1显示序号；2显示日期；3显示序号和日期
	 *
	 * @param divcity_flag
	 * @param isleft
	 * @param row_flag
	 * @return
	 */
	private static String getRowFlag(RptResourceTable rptTable) {
		if ("100".equals(rptTable.rpt_type) || "101".equals(rptTable.rpt_type)) {
			return rptTable.row_flag;
		}
		String ret = "0";
		if (StringB.toInt(rptTable.divcity_flag, 0) > 0) {
			if (ReportConsts.NO.equals(rptTable.isleft)) {
				// 特殊显示方式，加入扩展显示列
				ret = rptTable.row_flag;
			}
		}
		return ret;
	}

}
