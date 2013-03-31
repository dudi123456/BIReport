package com.ailk.bi.upload.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.upload.dao.impl.UploadDao;
import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;
import com.ailk.bi.upload.domain.UiMsuUpReportMetaInfoTable;
import com.ailk.bi.upload.facade.UploadFacade;
import com.ailk.bi.upload.util.UploadHelper;
import com.ailk.bi.upload.util.UploadUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class UploadAjaxHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		//
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
			ex1.printStackTrace();
		}
		String row = CommTool.getParameterGB(request, "row");
		if (row == null) {
			row = "0";
		}
		String col = CommTool.getParameterGB(request, "col");
		if (col == null) {
			col = "0";
		}
		String report_id = CommTool.getParameterGB(request, "report_id");
		if (report_id == null) {
			report_id = "";
		}
		String record_code = CommTool.getParameterGB(request, "record_code");
		if (record_code == null) {
			record_code = "";
		}
		String value = CommTool.getParameterGB(request, "value");
		if (value == null) {
			value = "";
		}
		String date = CommTool.getParameterGB(request, "date");
		if (date == null) {
			date = "";
		}
		//

		//
		UploadFacade facade = new UploadFacade(new UploadDao());
		// 报表配置信息
		UiMsuUpReportConfigTable reportInfo = facade.getReportInfo(report_id);
		if (reportInfo == null) {
			reportInfo = new UiMsuUpReportConfigTable();
		}
		// 取出行列元数据
		UiMsuUpReportMetaInfoTable[] metaInfo = facade.getReportMetaInfo(
				report_id, record_code);
		if (metaInfo == null) {
			metaInfo = new UiMsuUpReportMetaInfoTable[0];
		}
		int dimCount = 0;
		for (int i = 0; i < metaInfo.length; i++) {
			if (metaInfo[i].getFiled_type().equals("1")) {
				dimCount++;
			}
		}
		//
		// 从结构中提取字段列
		HashMap fieldMap = UploadHelper.getFiledMap(metaInfo, "1");
		String colStr = fieldMap.get(record_code).toString();
		String[] colArr = colStr.split(",");
		//
		String[][] list = UploadHelper.getReportDetailList(
				reportInfo.getReport_type(), report_id, date, record_code,
				colStr);

		// 取出辅助原始值
		int row_index = Integer.parseInt(row);
		int col_index = Integer.parseInt(col);

		String old_value = list[row_index][col_index];
		String whereStr = " ";
		// 日期
		if (date != null && !"".equals(date)) {
			if (reportInfo.getReport_type().equals("D")) {
				whereStr += " AND DAY_ID ='" + date + "'";
			} else if (reportInfo.getReport_type().equals("M")) {
				whereStr += " AND MONTH_ID ='" + date + "'";
			} else {
				whereStr += " AND DAY_ID ='" + date + "'";
			}
		}
		// 报表ID
		if (report_id != null && !"".equals(report_id)) {
			whereStr += " AND RPT_ID='" + report_id + "'";
		}
		// 报表记录
		if (record_code != null && !"".equals(record_code)) {
			whereStr += " AND RECORD_CODE='" + record_code + "'";
		}
		// 纬度条件
		for (int i = 0; i < col_index && i < dimCount; i++) {
			whereStr += " AND " + colArr[i] + " = '" + list[row_index][i] + "'";
		}
		// 变更值
		String tableName = "";
		if (reportInfo.getReport_type().equals("D")) {
			tableName = "ui_msu_up_info_day_data";
		} else if (reportInfo.getReport_type().equals("M")) {
			tableName = "ui_msu_up_info_month_data";
		} else {
			tableName = "ui_msu_up_info_day_data";
		}
		// update
		String sql = " update " + tableName + " set " + colArr[col_index]
				+ " = " + value + " where 1=1 " + whereStr;
		// log
		InfoOperTable infoOper = CommonFacade
				.getLoginUser(request.getSession());
		if (infoOper == null) {
			infoOper = new InfoOperTable();
		}

		String logSQL = "insert into  UI_MSU_UP_DATA_LOG (oper_no , oper_ip , oper_date , oper_type , report_id , report_type, record_code ,oper_field , oper_condition,old_value , new_value)";
		logSQL += "values('" + infoOper.oper_no + "','" + infoOper.login_ip
				+ "',sysdate,'2','" + report_id + "','"
				+ reportInfo.getReport_type() + "','" + record_code + "','"
				+ colArr[col_index] + "','"
				+ StringB.replace(whereStr, "'", "''") + "','" + old_value
				+ "','" + value + "')";
		System.out.println("sql========" + sql);
		System.out.println("logSQL========" + logSQL);
		String sqlArr[] = new String[2];
		sqlArr[0] = sql;
		sqlArr[1] = logSQL;
		int i = UploadUtil.queryArrayFacade(sqlArr);

		// 记录日志

		// 返回

		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			PrintWriter pw = response.getWriter();
			pw.write(i + ":" + value);

			setNvlNextScreen(request);
		} catch (IOException ex) {

		}

	}

}
