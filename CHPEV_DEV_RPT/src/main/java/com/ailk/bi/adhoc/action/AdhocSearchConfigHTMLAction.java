package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
//import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
//import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocSearchConfigHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4310787422832351647L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();

		String operType = CommTool.getParameterGB(request, "opType");
		if (operType == null || "".equals(operType)) {
			operType = "open";
		}

		if ("open".equals(operType)) {// 打开搜索窗口
			String adhocId = StringB.NulltoBlank(request
					.getParameter("adhocId"));

			ReportQryStruct struct = new ReportQryStruct();
			struct.dim1 = "0";
			struct.dim2 = "";
			struct.dim3 = adhocId;

			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);
			session.setAttribute("VIEW_TREE_LIST", null);
			setNextScreen(request, "AdhocSearchOpen.screen");

		} else if ("qryinfo".equals(operType)) {
			// 查询条件，维度，指标中的符合条件信息
			request.setAttribute("init", "true");
			session.setAttribute("VIEW_TREE_LIST",
					qryConfigInfo(request, session));
			setNextScreen(request, "AdhocSearchOpen.screen");

		} else if ("openUserList".equals(operType)) {// 打开清单搜索窗口
			String adhocId = StringB.NulltoBlank(request
					.getParameter("adhocId"));

			ReportQryStruct struct = new ReportQryStruct();
			struct.dim1 = "0";
			struct.dim2 = "";
			struct.dim3 = adhocId;

			session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);
			session.setAttribute("VIEW_TREE_LIST", null);
			setNextScreen(request, "AdhocSearchOpenUserList.screen");

		} else if ("qryListInfo".equals(operType)) {
			// 查询清单符合条件记录
			request.setAttribute("init", "true");
			session.setAttribute("VIEW_TREE_LIST",
					qryUserListConfigInfo(request, session));
			setNextScreen(request, "AdhocSearchOpenUserList.screen");

		}

	}

	private String[][] qryUserListConfigInfo(HttpServletRequest request,
			HttpSession session) {
		String searchString = StringB.NulltoBlank(request
				.getParameter("txtQryName"));
		String adhocId = StringB.NulltoBlank(request.getParameter("adhocId"));

		ReportQryStruct struct = new ReportQryStruct();
		struct.dim1 = "0";
		struct.dim2 = searchString;
		struct.dim3 = adhocId;

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);

		String roleId = CommonFacade.getUserAdhocRole(session);

		String sql = "SELECT MSU_FIELD,MSU_NAME "
				+ "FROM UI_ADHOC_USER_LIST A,UI_RULE_ADHOC_ROLE B WHERE A.ADHOC_ID = '"
				+ adhocId
				+ "' AND OPER_NO = '-1' AND FAV_ID='-1' "
				+ "AND STATUS = '1' AND A.ADHOC_ID=B.ADHOC_ID AND A.MSU_FIELD=B.RES_ID AND B.ROLE_ID='"
				+ roleId + "' AND B.RES_TYPE='4' and MSU_NAME like '%"
				+ searchString + "%' ORDER BY SEQUENCE";

		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private String[][] qryConfigInfo(HttpServletRequest request,
			HttpSession session) {

		String searchRange = StringB.NulltoBlank(request
				.getParameter("searchRange"));
		String searchString = StringB.NulltoBlank(request
				.getParameter("txtQryName"));
		String adhocId = StringB.NulltoBlank(request.getParameter("adhocId"));

		ReportQryStruct struct = new ReportQryStruct();
		struct.dim1 = searchRange;
		struct.dim2 = searchString;
		struct.dim3 = adhocId;

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);

		String roleId = CommonFacade.getUserAdhocRole(session);

		int iRange = Integer.parseInt(searchRange);

		String sqlCon = "";
		String sqlDim = "";
		String sqlMsu = "";
		String sql = "";
		switch (iRange) {
		case 0:
			sqlCon = " select 'con' || B.CON_ID , B.CON_NAME,'条件','CON_NAME'  from ui_adhoc_rule_group_prop A, ui_adhoc_condition_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND "
					+ "A.META_TYPE = '1' AND A.META_CODE = B.CON_ID AND B.STATUS='1' and b.con_id in( "
					+ "select res_id from UI_RULE_ADHOC_ROLE where res_type='1' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and b.con_name like '%" + searchString + "%'";

			sqlDim = " select 'wd'  || B.Dim_Id , B.Dim_Name,'维度','WD_NAME'  from "
					+ "ui_adhoc_rule_group_prop A , ui_adhoc_dim_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND A.META_TYPE = '2' AND A.META_CODE =B.Dim_Id and b.STATUS='1'  and b.Dim_Id in( "
					+ "select res_id from UI_RULE_ADHOC_ROLE where res_type='2' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and B.Dim_Name like '%" + searchString + "%'";

			sqlMsu = "  select 'msu' || B.Msu_Id , B.Msu_Name,'指标','DL_NAME' from ui_adhoc_rule_group_prop A , ui_adhoc_msu_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND A.META_TYPE = '3' AND A.META_CODE =B.MSU_ID and b.STATUS='1' and b.Msu_Id "
					+ "in(select res_id from UI_RULE_ADHOC_ROLE where res_type='3' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and B.Msu_Name like '%" + searchString + "%'";

			sql = sqlCon + " union " + sqlDim + " union " + sqlMsu;
			break;
		case 1:
			// 条件
			sql = " select 'con' || B.CON_ID , B.CON_NAME,'条件','CON_NAME'  from ui_adhoc_rule_group_prop A, ui_adhoc_condition_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND "
					+ "A.META_TYPE = '1' AND A.META_CODE = B.CON_ID AND B.STATUS='1' and b.con_id in( "
					+ "select res_id from UI_RULE_ADHOC_ROLE where res_type='1' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and b.con_name like '%" + searchString + "%'";
			break;
		case 2:
			// 维度
			sql = " select 'wd'  || B.Dim_Id , B.Dim_Name,'维度','WD_NAME'  from "
					+ "ui_adhoc_rule_group_prop A , ui_adhoc_dim_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND A.META_TYPE = '2' AND A.META_CODE =B.Dim_Id and b.STATUS='1'  and b.Dim_Id in( "
					+ "select res_id from UI_RULE_ADHOC_ROLE where res_type='2' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and B.Dim_Name like '%" + searchString + "%'";
			break;
		case 3:
			// 指标

			sql = "  select 'msu' || B.Msu_Id , B.Msu_Name,'指标','DL_NAME'  from ui_adhoc_rule_group_prop A , ui_adhoc_msu_meta B WHERE "
					+ "A.GROUP_ID IN(select distinct A.group_id from ui_adhoc_rule_group_prop A) AND A.META_TYPE = '3' AND A.META_CODE =B.MSU_ID and b.STATUS='1' and b.Msu_Id "
					+ "in(select res_id from UI_RULE_ADHOC_ROLE where res_type='3' and adhoc_id='"
					+ adhocId
					+ "' and role_id='"
					+ roleId
					+ "')"
					+ " and B.Msu_Name like '%" + searchString + "%'";
			break;

		}

		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

}
