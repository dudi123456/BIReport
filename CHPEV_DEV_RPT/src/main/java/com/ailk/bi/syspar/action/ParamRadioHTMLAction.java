package com.ailk.bi.syspar.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class ParamRadioHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();
		// 获取用户信息
		String oper_no = CommonFacade.getLoginId(session);

		// 操作类型
		String opt_type = StringB.NulltoBlank(request.getParameter("opt_type"));
		opt_type.replaceAll("\"", "'");
		if (opt_type == null || "".equals(opt_type)) {
			opt_type = "main";
		}
		// 返回
		String strReturn = "";

		// 条件对象
		ReportQryStruct qryStruct = new ReportQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException e) {

			e.printStackTrace();
		}

		if ("radioCheck".equals(opt_type) || "radioCheckQry".equals(opt_type)) {
			String node_id = request.getParameter("node_id");
			String en_name = request.getParameter("en_name");
			String qry_id = request.getParameter("qry_id");
			String qry_name = request.getParameter("qry_name");
			String[][] struct = qryItemList(qryStruct, node_id, en_name,
					qry_id, qry_name);
			session.setAttribute("listRadioStruct", struct);
			request.setAttribute("qry_radio_id",
					request.getParameter("qry_radio_id"));
			request.setAttribute("node_id", node_id);
			request.setAttribute("en_name", en_name);

			// 返回
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			if ("radioCheckQry".equals(opt_type)) {
				strReturn = "radioCheckDisp";
			} else {
				strReturn = "radioCheckProc";
			}
		}

		strReturn += ".screen";

		setNextScreen(request, strReturn);
	}

	/**
	 * 查询列表信息
	 * 
	 * @param qryStruct
	 * @return
	 * @throws HTMLActionException
	 */
	public static String[][] qryItemList(ReportQryStruct qryStruct,
			String node_id, String en_name, String qry_id, String qry_name)
			throws HTMLActionException {
		if (StringTool.checkEmptyString(node_id)
				|| StringTool.checkEmptyString(en_name)) {
			return null;
		}
		String sql = "SELECT COLUMN_SHOW_RULE,COLUMN_SHOW_RULENAME,COLUMN_ID,COLUMN_NAME FROM UI_PARAM_META_RADIO_CONFIG where 1=1";
		sql += " and PARAM_ID like '%" + node_id + "%'";
		sql += " and COLUMN_EN_NAME like '%" + en_name + "%'";

		String[][] arrList = null;
		try {
			// System.out.println("qryItemList=" + sql);
			arrList = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (arrList == null || arrList.length <= 0) {
			return null;
		}

		// 存在定义
		sql = arrList[0][0];
		if (!StringTool.checkEmptyString(qry_id)) {
			sql += " and " + arrList[0][2] + " like '%" + qry_id + "%'";
		}
		if (!StringTool.checkEmptyString(qry_name)) {
			sql += " and " + arrList[0][3] + " like '%" + qry_name + "%'";
		}
		sql += " ORDER BY " + arrList[0][2];

		String[][] value = null;
		try {
			// System.out.println("qryItemList=" + sql);
			value = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * 查询描述信息
	 * 
	 * @param qryStruct
	 * @return
	 * @throws HTMLActionException
	 */
	public static String qryItemName(String node_id, String en_name,
			String qry_id) throws HTMLActionException {
		if (StringTool.checkEmptyString(node_id)
				|| StringTool.checkEmptyString(en_name)) {
			return "";
		}
		String sql = "SELECT COLUMN_SHOW_RULE,COLUMN_SHOW_RULENAME,COLUMN_ID,COLUMN_NAME FROM UI_PARAM_META_RADIO_CONFIG where 1=1";
		sql += " and PARAM_ID like '%" + node_id + "%'";
		sql += " and COLUMN_EN_NAME like '%" + en_name + "%'";

		String[][] arrList = null;
		try {
			// System.out.println("qryItemList=" + sql);
			arrList = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (arrList == null || arrList.length <= 0) {
			return null;
		}

		// 存在定义
		sql = arrList[0][1];
		if (!StringTool.checkEmptyString(qry_id)) {
			sql += " and " + arrList[0][2] + " = '" + qry_id + "'";
		}

		try {
			// System.out.println("qryItemList=" + sql);
			arrList = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		String value = "";
		if (arrList != null && arrList.length > 0) {
			value = arrList[0][0];
		}
		return value;
	}

}
