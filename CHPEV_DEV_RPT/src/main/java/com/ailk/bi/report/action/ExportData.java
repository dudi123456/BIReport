package com.ailk.bi.report.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.CodeParamUtil;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;

public class ExportData extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取用户session
		HttpSession session = request.getSession();

		String oper_type = (String) session.getAttribute("oper_type_export");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "report";
		}

		if ("report".equals(oper_type)) {
			// 定义报表操作接口
			ILReportService rptService = new LReportServiceImpl();
			// 定义HTML输出接口
			// ILTableService tableService = new LTableServiceImpl();
			// 报表基本信息
			RptResourceTable rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE);
			// 报表查询条件对象
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
			// 获取固定报表列对象
			@SuppressWarnings("rawtypes")
			List listRptCol = null;
			try {
				listRptCol = rptService.getReportCol(rptTable.rpt_id,
						qryStruct.expandcol);
			} catch (AppException e) {
				System.out.println("listRptCol error!!!");
			}
			if (listRptCol == null || listRptCol.size() == 0) {

			}
			// 报表条件记录表
			PubInfoConditionTable[] cdnTables = null;
			try {
				cdnTables = CommConditionUtil.genCondition(rptTable.rpt_id,
						ReportConsts.CONDITION_PUB);
			} catch (AppException e2) {
				e2.printStackTrace();
			}

			// 记录日志
			// CommTool.setLogAndObjInfo(session,CLogType.ACCESS,reportID,CObjType.report,"99000099");
			// ************************************************
			// String filePath =
			// GetSystemConfig.getBIBMConfig().getExtParam("excel_file");
			String fileName = "export.csv";

			// ***** Output strOut to Response ******
			response.reset(); // Reset the response
			response.setContentType("application/octet-stream;charset=GB2312");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");

			PrintWriter out;

			// Exporting vector to csv file

			String strTitle = "";
			if (ReportConsts.DIS_SEQUENCE_COL.equals(rptTable.row_flag)) {
				// 显示序号
				if (strTitle.length() > 0) {
					strTitle += ",";
				}
				strTitle += "序号";
			}
			if (ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
				// 显示日期
				if (strTitle.length() > 0) {
					strTitle += ",";
				}
				strTitle += "日期";
			}
			if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
				// 显示序号
				if (strTitle.length() > 0) {
					strTitle += ",";
				}
				strTitle += "序号";

				// 显示日期
				if (strTitle.length() > 0) {
					strTitle += ",";
				}
				strTitle += "日期";
			}
			for (int i = 0; listRptCol != null && i < listRptCol.size(); i++) {
				RptColDictTable dict = (RptColDictTable) listRptCol.get(i);
				if (!ReportConsts.YES.equals(dict.default_display)) {
					continue;
				}
				if (strTitle.length() > 0) {
					strTitle += ",";
				}
				strTitle += dict.field_title;
			}
			strTitle += "\n";
			System.out.println("strTitle=" + strTitle);
			try {
				out = response.getWriter();
				out.write(strTitle);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (int m = 0; m < 100000000; m++) {
				String[][] arrData = null;
				int start = m;
				int end = m + 50000;
				String strSql = com.ailk.bi.report.dao.impl.LReportDataDao
						.genDataSql(rptTable, listRptCol, qryStruct, cdnTables);
				strSql = "SELECT * FROM (SELECT rownum AS r, t.* FROM ("
						+ strSql + ") t) where r>" + start + " and r<=" + end;
				System.out.println("strSql=" + strSql);
				try {
					arrData = WebDBUtil.execQryArray(strSql, "");
				} catch (AppException e1) {
					e1.printStackTrace();
				}
				if (arrData == null) {
					break;
				}
				System.out.println("50000 rows out");

				for (int i = 0; arrData != null && i < arrData.length; i++) {
					String strBody = "";
					int iNum = 2;
					if (StringTool
							.checkEmptyString(rptTable.data_sequence_code)) {
						iNum = 1;
					}
					// 是否为合计行
					boolean sumRow = false;
					if (ReportConsts.RPT_SUMROW_ID.equals(arrData[i][0])) {
						sumRow = true;
					}
					// 是否为小计行
					boolean subSumRow = false;
					if (ReportConsts.RPT_SUBSUMROW_ID.equals(arrData[i][0])) {
						subSumRow = true;
					}
					if (sumRow || subSumRow) {
						if (ReportConsts.DIS_SEQUENCE_COL
								.equals(rptTable.row_flag)) {
							strBody += "合计";
						}
						if (ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
							strBody += "合计";
						}
						if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
							strBody += "合计";
							strBody += ",";
						}
					} else {
						if (ReportConsts.DIS_SEQUENCE_COL
								.equals(rptTable.row_flag)) {
							// 显示序号
							if (strBody.length() > 0) {
								strBody += ",";
							}
							strBody += Integer.toString(m + i + 1);
						}
						if (ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
							// 显示日期
							if (strBody.length() > 0) {
								strBody += ",";
							}
							String vValue = arrData[i][iNum++];
							if (vValue == null || "".equals(vValue)) {
								vValue = " ";
							}
							strBody += vValue;
						}
						if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
							// 显示序号
							if (strBody.length() > 0) {
								strBody += ",";
							}
							strBody += Integer.toString(m + i + 1);

							// 显示日期
							if (strBody.length() > 0) {
								strBody += ",";
							}
							strBody += arrData[i][iNum++];
						}
					}

					for (int k = 0; k < listRptCol.size(); k++) {
						RptColDictTable dict = (RptColDictTable) listRptCol
								.get(k);

						if (!ReportConsts.YES.equals(dict.default_display)) {
							continue;
						}

						String tmpValue = "";
						if (ReportConsts.DATA_TYPE_STRING
								.equals(dict.data_type)) {
							if (!StringTool
									.checkEmptyString(dict.field_dim_code)) {
								iNum++;
							}
							tmpValue = arrData[i][iNum++];
						} else {
							tmpValue = arrData[i][iNum++];
						}

						if (strBody.length() > 0) {
							strBody += ",";
						}
						strBody += tmpValue;

					}
					strBody += "\n";
					// System.out.println("strBody=" + strBody);

					try {
						out = response.getWriter();
						out.write(strBody);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				m = m + 50000;
				m = m - 1;
			}
			// ***************************************
		} else if ("datareport".equals(oper_type)) {
			String rpt_id = request.getParameter("rpt_id");
			if (!StringTool.checkEmptyString(rpt_id)) {
				String dataSql = (String) session
						.getAttribute("datareport_sql_" + rpt_id);
				String condition = (String) session
						.getAttribute("datareport_condition_" + rpt_id);
				@SuppressWarnings("unused")
				String desc = (String) session.getAttribute("datareport_desc_"
						+ rpt_id);
				String totalnum = session.getAttribute(
						"datareport_totalnum_" + rpt_id).toString();
				String strSql = "SELECT t.sigma_name,t.Sql_Define FROM UI_SIGMA_EXPORT_INFO t WHERE sigma_id='"
						+ rpt_id + "'";
				String titleDesc = "总记录数：" + totalnum;
				try {
					String[][] dataValue = WebDBUtil.execQryArray(strSql);
					for (int i = 0; dataValue != null && i < dataValue.length; i++) {
						String tmpValueSql = dataValue[i][1] + condition;
						System.out.println("tmpValueSql=" + tmpValueSql);
						String tmpValue = WebDBUtil.getSingleValue(tmpValueSql);
						titleDesc += ";" + dataValue[i][0] + "：" + tmpValue;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				String fileName = "csv_export.csv";
				System.out.println("fileName=" + fileName);
				// ***** Output strOut to Response ******
				response.reset(); // Reset the response
				response.setContentType("application/octet-stream;charset=gbk");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + fileName + "\"");

				PrintWriter out;
				// 输出标题
				try {
					out = response.getWriter();
					out.write(titleDesc + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 输入内容
				for (int m = 0; m < 100000000; m++) {
					String[][] arrData = null;
					int start = m;
					int end = m + 40000;
					strSql = "SELECT * FROM (SELECT rownum AS r, t.* FROM ("
							+ dataSql + ") t) where r>" + start + " and r<="
							+ end;
					System.out.println("strSql=" + strSql);
					try {
						arrData = WebDBUtil.execQryArray(strSql, "");
					} catch (AppException e1) {
						e1.printStackTrace();
					}
					if (arrData == null) {
						break;
					}
					System.out.println(end + " rows out");

					for (int i = 0; arrData != null && i < arrData.length; i++) {
						String strBody = "";
						for (int k = 1; k < arrData[i].length; k++) {
							if (strBody.length() > 0) {
								strBody += ",";
							}
							strBody += arrData[i][k];
						}
						strBody += "\n";
						// System.out.println("strBody=" + strBody);

						try {
							out = response.getWriter();
							out.write(strBody);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					m = m + 40000;
					m = m - 1;
				}
			}
		} else {
			// 其他输出
			UiAdhocUserListTable[] defineInfo = (UiAdhocUserListTable[]) session
					.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO);

			String fileName = "userlist_export.csv";
			// ***** Output strOut to Response ******
			response.reset(); // Reset the response
			response.setContentType("application/octet-stream;charset=GB2312");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");

			PrintWriter out;
			// 输出标题
			String sqlDesc = (String) session
					.getAttribute("AdhocExportUserListDesc");
			try {
				out = response.getWriter();
				out.write(sqlDesc + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 输入内容
			for (int m = 0; m < 100000000; m++) {
				String[][] arrData = null;
				int start = m;
				int end = m + 20000;
				String strSql = (String) session
						.getAttribute("AdhocExportUserListSQL");
				strSql = "SELECT * FROM (SELECT rownum AS r, t.* FROM ("
						+ strSql + ") t) where r>" + start + " and r<=" + end;
				System.out.println("strSql=" + strSql);
				try {
					arrData = WebDBUtil.execQryArray(strSql, "");
				} catch (AppException e1) {
					e1.printStackTrace();
				}
				if (arrData == null) {
					break;
				}
				System.out.println(end + " rows out");

				for (int i = 0; arrData != null && i < arrData.length; i++) {
					String strBody = "";
					for (int k = 1; k < defineInfo.length + 1; k++) {
						UiAdhocUserListTable tmpInfo = defineInfo[k - 1];
						if (strBody.length() > 0) {
							strBody += ",";
						}
						// strBody += arrData[i][k];
						strBody += StringB.replace(CodeParamUtil
								.codeListParamFetcher(request, tmpInfo
										.getMap_code().toUpperCase(),
										arrData[i][k]), ",", "，");
					}
					strBody += "\n";
					// System.out.println("strBody=" + strBody);

					try {
						out = response.getWriter();
						out.write(strBody);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				m = m + 20000;
				m = m - 1;
			}
		}
	}
}