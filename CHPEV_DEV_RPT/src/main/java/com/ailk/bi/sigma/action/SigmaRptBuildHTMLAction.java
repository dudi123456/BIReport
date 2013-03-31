package com.ailk.bi.sigma.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.util.AdhocBuildUserDetail;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.sigma.SigmaGridConstant;
import com.ailk.bi.sigma.SigmaGridReportCondBean;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SigmaRptBuildHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		String sigmaId = request.getParameter("sigmaId");

		String flag = qrySigmaRptBuildInfo(request, session);

		request.setAttribute("flag", flag);
		request.setAttribute("sigmaId", sigmaId);

		if (flag.equals("1")) {
			setNextScreen(request, "SigmaReportBuildStatus.screen");
		} else {
			setNextScreen(request, "SigmaReportBuildShow.screen");
		}

	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return -1:失败;0:插入成功,1:已有记录存在
	 */
	private String qrySigmaRptBuildInfo(HttpServletRequest request,
			HttpSession session) {

		int sigmaId = Integer
				.parseInt((String) request.getParameter("sigmaId"));

		SigmaGridReportCondBean condBean = (SigmaGridReportCondBean) session
				.getAttribute(SigmaGridConstant.GRID_CONDITION_SESSION
						+ sigmaId);

		// String sql =
		// "select t.sigma_id,b.flag from ui_sigma_define_base t,ui_adhoc_buildxls_task b "
		// +
		// "where t.sigma_id=b.sigma_id and t.sigma_id=" + sigmaId;
		String sql = "SELECT b.id,b.xls_name,b.xls_desc,to_char(b.add_date,'YYYY-MM-DD hh24:mi:ss'),b.flag,b.file_name,b.oper_no,b.bak_fld_01,b.bak_fld_02,b.RUNMACHINE,to_char(b.RUN_DATE,'YYYY-MM-DD hh24:mi:ss'),to_char(b.FINISH_DATE,'YYYY-MM-DD hh24:mi:ss'),"
				+ "b.RECORDCNT,b.bak_fld_03 from ui_adhoc_buildxls_task b , ui_sigma_define_base t where 1=1 and t.sigma_id=b.sigma_id and t.sigma_id="
				+ sigmaId;

		if (condBean != null) {
			sql += " and b.SIGMA_PARAM='" + condBean.getSigmaConValue() + "'";
		} else {
			condBean = new SigmaGridReportCondBean();
		}

		try {

			String res[][] = WebDBUtil.execQryArray(sql);
			if (res != null && res.length > 0) {
				request.setAttribute("init", "true");
				session.setAttribute("VIEW_TREE_LIST", res);

				// setNextScreen(request, "SubjectUserXlsTaskStatus.screen");

				return "1";
			} else {
				// 没有记录，插入记录到ui_adhoc_buildxls_task中
				String sqlSigma = "select t.sigma_name,t.sql_define,t.sql_condition,t.sql_orderby,t.sql_count from ui_sigma_define_base t"
						+ " where t.sigma_id=" + sigmaId;

				String resSigma[][] = WebDBUtil.execQryArray(sqlSigma);

				if (resSigma != null && resSigma.length > 0) {

					String strSelect = resSigma[0][1] + " " + resSigma[0][2]
							+ " " + condBean.getSigmaConSql() + " "
							+ resSigma[0][3];
					String sqlCnt = " Select count(*) from (" + strSelect + ")";

					String[][] resCnt = WebDBUtil.execQryArray(sqlCnt);
					if (Integer.parseInt(resCnt[0][0]) == 0) {
						return "2";
					}

					String typeId = SigmaGridConstant.GRID_ADHOC_DETAIL;// adhocId

					String sqlCol = "select COL_ENG_NAME,COL_GBK_NAME from ui_sigma_column_define t where  is_show=1 and sigma_id="
							+ sigmaId + " order by sort_num";

					String[][] resCol = WebDBUtil.execQryArray(sqlCol, "");

					List listField = new ArrayList();// 字段信息

					if (resCol != null && resCol.length > 0) {
						for (int i = 0; i < resCol.length; i++) {
							String fld = StringB.NulltoBlank(resCol[i][0]);
							String fldGBK = StringB.NulltoBlank(resCol[i][1]);

							listField.add(fld + "|" + fldGBK + "||||");
						}
					}

					AdhocBuildUserDetail expDetail = new AdhocBuildUserDetail(
							request, sqlCnt, strSelect, listField,
							resSigma[0][0], typeId, sigmaId,
							condBean.getSigmaConValue());
					expDetail.doSaveSigmaExpDetailTask();

					return "0";

				}

			}
		} catch (AppException e) {

			e.printStackTrace();
			return "-1";
		}

		return "-1";

	}

}
