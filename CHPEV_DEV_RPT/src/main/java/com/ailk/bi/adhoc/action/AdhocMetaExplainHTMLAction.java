package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocMetaExplainHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String opType = StringB.NulltoBlank(request.getParameter("opType"))
				.toLowerCase();
		HttpSession session = request.getSession(true);
		if (opType == null || "".equals(opType)) {
			opType = "qry";
		}

		String strReturn = "";
		if (opType.equals("qry")) {// 指标解释查询
			session.setAttribute("VIEW_TREE_LIST",
					adhocMetaExplainQry(request, response, session));
			request.setAttribute("init", "true");
			strReturn = "qryMetaExplain";
		}

		setNextScreen(request, strReturn + ".screen");

	}

	private String[][] adhocMetaExplainQry(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String metaName = StringB.NulltoBlank(request.getParameter("metaName"));
		String adhoc_id = StringB.NulltoBlank(request.getParameter("adhoc_id"));

		String sql = "Select * from UI_ADHOC_META_EXPLAIN a where flag=1 and adhoc_id='"
				+ adhoc_id + "'";

		if (metaName.length() > 0) {
			sql += " and a.meta_name like '%" + metaName + "%'";

		}

		sql += " order by a.sort_num,a.meta_name";
		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = metaName;
		qryStruct.dim2 = adhoc_id;
		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

}
