package com.ailk.bi.system.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UICodeListHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		// 程序判断区
		String opType = request.getParameter("opType");

		// session.setAttribute("viewdata", viewParamMemory(request,response));
		if (session.getAttribute("viewdata") != null) {
			session.removeAttribute("viewdata");
		}

		if (opType == null || "".equals(opType)) {
			opType = "qryinfo";
		}
		String strRetn = "ParamMemo.screen";

		if ("qryinfo".equals(opType)) {
			session.setAttribute("BULLETIN_BOARD_LIST",
					qryCodeListInfo(request, response));

		} else if ("viewdata".equals(opType)) {
			session.setAttribute("viewdata", viewParamMemory(request, response));
			strRetn = "ParamMemoViewData.screen";
		} else if ("refreshmem".equals(opType)) {
			refreshParamMemory(request, response);
			session.setAttribute("BULLETIN_BOARD_LIST",
					qryCodeListInfo(request, response));
			request.setAttribute("info", "刷新成功");
		}

		setNextScreen(request, strRetn);
		// super.doTrans(request, response);
	}

	private void refreshParamMemory(HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		String type = StringB.NulltoBlank(request.getParameter("type"));
		// context.setAttribute(arg0, arg1)
		String typeCode = StringB.NulltoBlank(request.getParameter("typeCode"));

		if (codeMap != null) {
			// HashMap map =
			// (HashMap)codeMap.get(typeCode.trim().toUpperCase());
			if (type.equals("1")) {
				codeMap.remove(typeCode.trim().toUpperCase());
				String sql = "select * from ui_code_list where TYPE_CODE='"
						+ typeCode + "'";
				String[][] res = null;
				HashMap mapTmp = new HashMap();
				try {
					res = WebDBUtil.execQryArray(sql, "");
				} catch (AppException e) {

					e.printStackTrace();
				}
				if (res != null && res.length > 0) {
					String sqlDim = "select " + res[0][5] + " from "
							+ res[0][6] + " where " + res[0][7] + " "
							+ res[0][10];
					try {
						// System.out.println(sqlDim);
						String[][] arr = WebDBUtil.execQryArray(sqlDim, "");
						for (int j = 0; arr != null && j < arr.length; j++) {
							mapTmp.put(arr[j][0], arr[j][1]);
						}
					} catch (AppException e) {

						e.printStackTrace();
					}
				}
				codeMap.put(typeCode.trim().toUpperCase(), mapTmp);
				context.setAttribute(WebConstKeys.ATTR_C_CODE_LIST, codeMap);
			} else if (type.equals("2")) {
				String sql = "select * from ui_code_list where TYPE_CODE='"
						+ typeCode + "'";
				String[][] res = null;
				codeMap.remove(typeCode.trim().toUpperCase());
				HashMap mapTmp = new HashMap();
				try {
					res = WebDBUtil.execQryArray(sql, "");
				} catch (AppException e) {

					e.printStackTrace();
				}
				if (res != null && res.length > 0) {
					for (int j = 0; res != null && j < res.length; j++) {
						mapTmp.put(res[j][3], res[j][4]);
					}

				}
				codeMap.put(typeCode.trim().toUpperCase(), mapTmp);
				context.setAttribute(WebConstKeys.ATTR_C_CODE_LIST, codeMap);
			}
		}
		// String code

	}

	private String[][] viewParamMemory(HttpServletRequest request,
			HttpServletResponse response) {
		String typeCode = StringB.NulltoBlank(request.getParameter("typeCode"));
		@SuppressWarnings("unused")
		String type = StringB.NulltoBlank(request.getParameter("type"));

		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		String obj[][] = null;
		if (codeMap != null) {
			HashMap map = (HashMap) codeMap.get(typeCode.trim().toUpperCase());
			if (map != null) {
				// if (type.equals("1")){
				obj = new String[map.keySet().size()][2];

				Iterator iter = map.entrySet().iterator();
				int ii = 0;
				while (iter.hasNext()) {
					Entry entry = (Entry) iter.next();
					String key = (String) entry.getKey();
					String val = (String) entry.getValue();
					obj[ii][0] = key;
					obj[ii][1] = val;
					ii++;
				}
				// }

			}

		}
		return obj;
	}

	private String[][] qryCodeListInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String qryCode = StringB.NulltoBlank(request.getParameter("qryCode"));
		String qryName = StringB.NulltoBlank(request.getParameter("qryName"));

		// String sql = "select * from ui_code_list where 1=1";
		String sql = "SELECT distinct type_code,type_name,rule_type,'','',code_filed,code_datasource,code_condition,1,status,code_order"
				+ " from ui_code_list where 1=1";

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryCode;
		qryStruct.dim2 = qryName;

		if (qryStruct.dim1.length() > 0) {
			sql += " and TYPE_CODE Like '%" + qryCode + "%'";
		}

		if (qryStruct.dim2.length() > 0) {
			sql += " and TYPE_NAME Like '%" + qryName + "%'";
		}

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				qryStruct);

		try {
			String res[][] = WebDBUtil.execQryArray(sql, "");
			return res;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

}
