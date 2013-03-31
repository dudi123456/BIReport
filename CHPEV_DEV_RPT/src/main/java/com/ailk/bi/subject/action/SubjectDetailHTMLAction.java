package com.ailk.bi.subject.action;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.csvreader.CsvWriter;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "serial" })
public class SubjectDetailHTMLAction extends HTMLActionSupport {

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		String table_id = request.getParameter("table_id");
		ReportQryStruct struct = new ReportQryStruct();

		if (table_id.equalsIgnoreCase("opp_warn_002_1")) {
			// 客服预警

			String op_time = request.getParameter("op_time");
			String msuFld = request.getParameter("msu_fld");
			// System.out.println(op_time + ":" + msuFld);
			struct.dim1 = op_time;
			struct.dim2 = msuFld;
			struct.dim3 = table_id;

			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);

			String sqlSelect = "SELECT ACC_NBR , " + msuFld + " field2 "
					+ " FROM FUI_OPP_ALERT_BUSI_USE_M A WHERE op_time="
					+ op_time + " AND  CARRIER_ID=10 group by ACC_NBR "
					+ "having " + msuFld + ">0";

			String sql = " Select * from(" + sqlSelect + ") where rownum<=2000";

			try {
				CsvWriter csvwr = new CsvWriter(response.getOutputStream(),
						',', Charset.forName("gb2312"));
				String[] writeRcd = new String[2];
				writeRcd[0] = "电话号码";
				writeRcd[1] = "次数";
				csvwr.writeRecord(writeRcd);

				double recordCnt = 0;
				String sqlCnt = "select count(*) from (" + sqlSelect + ")";

				String sqlQry = "select ACC_NBR,field2 from (select JFTOT.*,ROWNUM RN from ("
						+ sqlSelect
						+ ") JFTOT  Where rownum <= ?) where RN > ?";
				try {
					String[][] res = WebDBUtil.execQryArray(sqlCnt, "");
					if (res != null && res.length > 0) {
						recordCnt = Double.parseDouble(res[0][0]);
					}
				} catch (AppException e) {

					e.printStackTrace();
				}
				int perPageCnt = 10000;
				double pageCnt = Math.floor((recordCnt + perPageCnt - 1)
						/ perPageCnt);
				for (int i = 0; i < pageCnt; i++) {
					int dblTmp = (i + 1) * perPageCnt;
					String strWhere[] = new String[] { dblTmp + "|1",
							i * perPageCnt + "|1" };

					try {
						String arr[][] = WebDBUtil.execQryArray(sqlQry,
								strWhere, "");

						if (arr != null && arr.length > 0) {
							for (int j = 0; j < arr.length; j++) {
								String[] value = arr[j];
								csvwr.writeRecord(value);
							}
						}
					} catch (AppException e) {

						e.printStackTrace();
					}

				}

				csvwr.close();

			} catch (IOException e) {

				e.printStackTrace();
			}

			request.setAttribute("init", "true");
			session.setAttribute("VIEW_TREE_LIST", qryUserPhoneNumber(sql));
			setNextScreen(request, "SubjectUserNumDtl.screen");

		}
		// super.doTrans(request, response);
		// setNvlNextScreen(request);
	}

	private String[][] qryUserPhoneNumber(String sql) {
		try {
			// System.out.println(sql);
			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

}
