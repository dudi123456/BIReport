package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocCheckDao;
//import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.service.impl.AdhocCheckFacade;
//import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocCheckStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
//import com.ailk.bi.base.table.DChannelTable;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
//import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocMultiCheckSelectHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 得到会话
		HttpSession session = request.getSession();

		session.removeAttribute(WebKeys.ATTR_HiCodeSelectId);
		session.removeAttribute(WebKeys.ATTR_HiPageRows);
		//
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "1";
		}

		// 默认条件编码
		String obj_type = CommTool.getParameterGB(request, "con_id");
		if (obj_type == null || "".equals(obj_type)) {
			obj_type = "";
		}
		session.removeAttribute(WebKeys.ATTR_AdhocCheckStruct_ObjType);
		session.setAttribute(WebKeys.ATTR_AdhocCheckStruct_ObjType, obj_type);
		//
		if ("1".equals(operType)) {
			// 增加发展人部门控制
			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			setValueInSession(request, obj_type, ctlStruct);
			this.setNextScreen(request, "AdhocMultiCheckProc.screen");

		} else if ("2".equals(operType)) {
			String pageRows = request.getParameter("page_rows");
			if (pageRows == null) {
				pageRows = "10";
			}

			// 增加发展人部门控制
			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			setValueInSession(request, obj_type, ctlStruct);
			setNextScreen(request, "AdhocMultiCheckDisp.screen");
		}

	}

	/**
	 * 
	 * @param session
	 * @param channel_type
	 * @param sub_channel_type
	 */
	public static void setValueInSession(HttpServletRequest request,
			String con_id, UserCtlRegionStruct ctlStruct) {

		AdhocCheckStruct checkObj[] = null;

		// 服务接口
		AdhocCheckFacade facade = new AdhocCheckFacade(new AdhocCheckDao());
		// 基本SQL
		String[][] multiCheck = facade.getCheckSQL(con_id);
		String securityRule = StringB.NulltoBlank(multiCheck[0][4]);// 是否需要权限控制

		String sql = multiCheck[0][1];

		String orderStr = "";
		String sqlStr = "";
		// logcommon.debug("sql===========" + sql);

		// 处理order by
		if (sql.toUpperCase().indexOf("ORDER BY") > 0) {
			sqlStr = sql.substring(0, sql.toUpperCase().indexOf("ORDER BY"));
			orderStr = sql.substring(sql.toUpperCase().indexOf("ORDER BY"));
			// logcommon.debug("sql=====order======" + sqlStr);
			// logcommon.debug("orderStr=order==========" + orderStr);

		} else {
			sqlStr = sql;
		}

		if (sqlStr.toUpperCase().indexOf(" WHERE ") < 0) {
			sqlStr += " where 1=1 ";
		}
		String where = "";
		// 条件规则
		// OBJ_TYPE , APP_TYPE , QRY_CODE , CON_CODE , CON_NAME , DATA_TYPE
		// ,CON_TAG
		String[][] con_rule = facade.getCheckRule(con_id);
		for (int i = 0; con_rule != null && i < con_rule.length; i++) {
			String qry_code = con_rule[i][2];
			String data_type = con_rule[i][5];
			String con_code = con_rule[i][3];
			String con_tag = con_rule[i][6];
			// 1:外部条件(request传入)
			String tmpValue = request.getParameter(qry_code);
			where += CommConditionUtil.genWherePart(tmpValue, data_type,
					con_code, con_tag);
			//

		}
		where = "";
		String conn = request.getParameter("addConResult");
		if (conn != null && !"".equals(conn)) {
			where += " and " + conn;
		}

		sqlStr += where;

		// 权限控制规则
		// 业务类型
		// 数据部门
		// 用户所在组等权限控制规则，目前就只有数据部门。
		if (securityRule.equals("1")) {
			// 需要权限控制
			int iLevel = 0;
			if (ctlStruct.ctl_lvl.length() > 0) {
				iLevel = Integer.parseInt(ctlStruct.ctl_lvl);
			}
			if (iLevel > 1) {
				if (ctlStruct.ctl_sql.trim().length() > 0) {
					sqlStr += " and dept_id in (select dept_id from ("
							+ ctlStruct.ctl_sql + "))";

				}
			}
		}
		/*
		 * if (ctlStruct != null && ctlStruct.ctl_county_str != null&&
		 * !"".equals(ctlStruct.ctl_county_str)) { sqlStr += " and depart_id = "
		 * + ctlStruct.ctl_county_str_add; }
		 */
		sqlStr += " " + orderStr;

		//
		try {
			System.out.println("sql===========" + sqlStr);
			String svces[][] = WebDBUtil.execQryArray(sqlStr, "");
			if (svces != null && svces.length > 0) {
				checkObj = new AdhocCheckStruct[svces.length];
				for (int i = 0; i < svces.length; i++) {
					checkObj[i] = new AdhocCheckStruct();
					checkObj[i].setKey(svces[i][0]);
					checkObj[i].setDesc(svces[i][1]);
				}
			}

		} catch (AppException ex1) {
			ex1.printStackTrace();
		}
		// 判断后存入session
		request.getSession().removeAttribute(WebKeys.ATTR_AdhocCheckStruct);
		request.getSession().setAttribute(WebKeys.ATTR_AdhocCheckStruct,
				checkObj);

		request.getSession()
				.removeAttribute(WebKeys.ATTR_AdhocCheckStruct_Rule);
		request.getSession().setAttribute(WebKeys.ATTR_AdhocCheckStruct_Rule,
				con_rule);

	}

}
