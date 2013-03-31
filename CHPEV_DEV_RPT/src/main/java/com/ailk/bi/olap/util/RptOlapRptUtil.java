package com.ailk.bi.olap.util;

import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.UiRptInfoHeadTable;

public class RptOlapRptUtil {
	/**
	 * 生成报表对象
	 * 
	 * @param svces
	 *            一维数组
	 * @return
	 */
	public static PubInfoResourceTable genReportFromArray(String[] svces) {
		PubInfoResourceTable report = null;
		if (null == svces)
			throw new IllegalArgumentException("组装报表对象的数组为空");
		report = new PubInfoResourceTable();
		report.rpt_id = svces[0];
		report.res_id = svces[0];
		report.parent_id = svces[1];
		report.isresource = svces[2];
		report.ishot = svces[3];
		report.hot_type = svces[4];
		report.belong = svces[5];
		report.type = svces[6];
		report.code = svces[7];
		report.name = svces[8];
		report.cycle = svces[9];
		report.url = svces[10];
		report.filldept = svces[11];
		report.fillperson = svces[12];
		report.makedept = svces[13];
		report.makeperson = svces[14];
		report.needdept = svces[15];
		report.needperson = svces[16];
		report.needreason = svces[17];
		report.inputnote = svces[18];
		report.divcity_flag = svces[19];
		report.row_flag = svces[20];
		report.ishead = svces[21];
		report.isleft = svces[22];
		report.startcol = svces[23];
		report.processtype = svces[24];
		report.processflag = svces[25];
		report.dispenseflag = svces[26];
		report.start_date = svces[27];
		report.data_table = svces[28];
		report.data_where = svces[29];
		report.filter_zerorow = svces[30];
		if (null == report.filter_zerorow || "".equals(report.filter_zerorow)
				|| !RptOlapConsts.NO.equalsIgnoreCase(report.filter_zerorow)) {
			report.filter_zerorow = RptOlapConsts.YES;
		}
		report.data_order = svces[31];
		report.sequence = svces[32];
		report.status = svces[33];
		String head = svces[34];
		if (null != head && !"".equals(head)) {
			UiRptInfoHeadTable rptHead = new UiRptInfoHeadTable();
			rptHead.rpt_id = report.rpt_id;
			rptHead.rpt_header = head;
			report.head = rptHead;
		}
		return report;
	}
}
