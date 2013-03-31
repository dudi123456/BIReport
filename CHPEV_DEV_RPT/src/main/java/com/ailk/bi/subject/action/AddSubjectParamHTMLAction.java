package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * 
 * @author zhxiaojing
 * @desc 增值参数维护
 * 
 * 
 */
public class AddSubjectParamHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();
		String opType = StringB.NulltoBlank(request.getParameter("optype"));
		if (opType.equalsIgnoreCase("adv_param_config")) {
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "adv_param_config";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_adv_param_config(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "adv_param_config.screen");

		} else if (opType.equalsIgnoreCase("adv_param_lvl")) {

		} else if (opType.equalsIgnoreCase("add_param_config")) {
			add_adv_param_config(request, session);
			request.setAttribute("CTL_INFO", "新增成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "adv_param_config";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_adv_param_config(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "adv_param_config.screen");
		} else if (opType.equalsIgnoreCase("update_adv_param_config")) {
			update_adv_param_config(request, session);
			request.setAttribute("CTL_INFO", "修改成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "adv_param_config";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_adv_param_config(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "adv_param_config.screen");
		} else if (opType.equalsIgnoreCase("del_adv_param_config")) {
			del_adv_param_config(request, session);
			request.setAttribute("CTL_INFO", "删除成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "adv_param_config";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_adv_param_config(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "adv_param_config.screen");
		} else if (opType.equalsIgnoreCase("viewDtl")) {

			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "viewDtl";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_d_adv_param_lvl(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "d_adv_param_lvl.screen");
		} else if (opType.equalsIgnoreCase("add_param_lvl")) {
			add_adv_param_lvl(request, session);
			request.setAttribute("CTL_INFO", "新增成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "viewDtl";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_d_adv_param_lvl(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "d_adv_param_lvl.screen");
		} else if (opType.equalsIgnoreCase("edt_param_lvl")) {
			edt_adv_param_lvl(request, session);
			request.setAttribute("CTL_INFO", "修改成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "viewDtl";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_d_adv_param_lvl(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "d_adv_param_lvl.screen");
		} else if (opType.equalsIgnoreCase("del_param_lvl")) {
			del_adv_param_lvl(request, session);
			request.setAttribute("CTL_INFO", "删除成功");
			ReportQryStruct qryStruct = new ReportQryStruct();
			qryStruct.optype = "viewDtl";
			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
			session.setAttribute("VIEW_TREE_LIST",
					qry_d_adv_param_lvl(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "d_adv_param_lvl.screen");
		}

	}

	private String[][] qry_d_adv_param_lvl(HttpServletRequest request,
			HttpSession session) {
		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));

		String sql = "select * from d_adv_param_lvl where  param_id='"
				+ paramId + "' order by lvl_id";
		String[][] result = null;
		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;

	}

	private void del_adv_param_config(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));

		String sql = "delete from d_adv_param_config Where param_id='"
				+ paramId + "'";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void del_adv_param_lvl(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));
		String lvl_id = StringB.NulltoBlank(request.getParameter("lvl_id"));

		String sql = "delete from d_adv_param_lvl Where param_id='" + paramId
				+ "' and lvl_id='" + lvl_id + "'";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void update_adv_param_config(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));
		String paramName = StringB.NulltoBlank(request
				.getParameter("param_name"));
		String paramDesc = StringB.NulltoBlank(request
				.getParameter("param_desc"));

		String sql = "update d_adv_param_config set PARAM_NAME='" + paramName
				+ "',PARAM_DESC='" + paramDesc + "' Where param_id='" + paramId
				+ "'";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void add_adv_param_lvl(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));
		String lvlId = StringB.NulltoBlank(request.getParameter("lvl_id"));
		String lvlname = StringB.NulltoBlank(request.getParameter("lvl_name"));
		String startVal = StringB
				.NulltoBlank(request.getParameter("start_val"));
		String endVal = StringB.NulltoBlank(request.getParameter("end_val"));
		String remk = StringB.NulltoBlank(request.getParameter("remk"));

		String sql = "insert into d_adv_param_lvl(PARAM_TYPE,PARAM_ID,LVL_ID,LVL_NAME,START_VAL,END_VAL,REMARK) VALUES('1','"
				+ paramId
				+ "','"
				+ lvlId
				+ "','"
				+ lvlname
				+ "','"
				+ startVal
				+ "','" + endVal + "','" + remk + "')";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void edt_adv_param_lvl(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));
		String lvlId = StringB.NulltoBlank(request.getParameter("lvl_id"));
		String lvlname = StringB.NulltoBlank(request.getParameter("lvl_name"));
		String startVal = StringB
				.NulltoBlank(request.getParameter("start_val"));
		String endVal = StringB.NulltoBlank(request.getParameter("end_val"));
		String remk = StringB.NulltoBlank(request.getParameter("remk"));

		String sql = "update d_adv_param_lvl set LVL_ID='" + lvlId
				+ "',LVL_NAME='" + lvlname + "',START_VAL='" + startVal
				+ "',END_VAL='" + endVal + "',REMARK='" + remk
				+ "' where param_id='" + paramId + "' and lvl_id='" + lvlId
				+ "'";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void add_adv_param_config(HttpServletRequest request,
			HttpSession session) {

		String paramId = StringB.NulltoBlank(request.getParameter("param_id"));
		String paramName = StringB.NulltoBlank(request
				.getParameter("param_name"));
		String paramDesc = StringB.NulltoBlank(request
				.getParameter("param_desc"));

		String sql = "insert into d_adv_param_config(PARAM_TYPE,PARAM_ID,PARAM_NAME,PARAM_DESC) VALUES('1','"
				+ paramId + "','" + paramName + "','" + paramDesc + "')";
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private String[][] qry_adv_param_config(HttpServletRequest request,
			HttpSession session) {
		String sql = "select * from d_adv_param_config order by param_id ";
		String[][] result = null;
		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;

	}

}
