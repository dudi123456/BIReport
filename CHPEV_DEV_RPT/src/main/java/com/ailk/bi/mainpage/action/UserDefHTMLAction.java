package com.ailk.bi.mainpage.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.UiMainTableCfg;
import com.ailk.bi.base.table.UiMainTableName;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.mainpage.common.MeasureTableOpt;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserDefHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 首页个性定制
	 * 
	 * @param request
	 * @param response
	 * @throws HTMLActionException
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 变量申明区
		HttpSession session = request.getSession();
		// 操作员ID
		String oper_no = CommonFacade.getLoginId(session);
		// 获取操作类型值
		String opType = request.getParameter("opType");
		if (opType == null || "".equals(opType))
			opType = "list";

		if ("save".equals(opType)) {
			// 获取系统全部定义
			UiMainTableName[] table = null;
			try {
				table = MeasureTableOpt.getDayViewDef();
			} catch (AppException e) {
				e.printStackTrace();
			}
			for (int i = 0; table != null && i < table.length; i++) {
				// 获取指定ID的表定义
				UiMainTableCfg[] tableDef = null;
				try {
					tableDef = MeasureTableOpt.getTableDef(table[i].table_id);
				} catch (AppException ex) {
					ex.printStackTrace();
				}

				String[] strSql = null;
				List tmpSQL = new ArrayList();
				String table_id = table[i].table_id;
				for (int k = 0; tableDef != null && k < tableDef.length; k++) {
					String item_id = tableDef[k].item_id;
					String axis_type = tableDef[k].axis_type;
					String name = table_id + "|" + item_id + "|" + axis_type;
					String value = request.getParameter(name);
					String[] arrValue = new String[5];
					arrValue[0] = oper_no;
					arrValue[1] = table_id;
					arrValue[2] = item_id;
					arrValue[3] = axis_type;
					arrValue[4] = "1";

					try {
						if ("on".equals(value)) {
							String insert = insertSql(arrValue);
							tmpSQL.add(insert);
						}
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "提取SQL语句失败！");
					}
				}
				// 先删除
				try {
					String delSql = delSql(oper_no, table_id);
					// System.out.println("delSql=" + delSql);
					WebDBUtil.execUpdate(delSql);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "操作失败！");
				}
				// 加入新的
				strSql = (String[]) tmpSQL.toArray(new String[tmpSQL.size()]);
				// for (int m = 0; strSql != null && m < strSql.length; m++) {
				// System.out.println("strSql=" + strSql[m]);
				// }
				try {
					WebDBUtil.execTransUpdate(strSql);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "更新操作失败！");
				}
			}
		}
		setNextScreen(request, "MainPageDef.screen");
	}

	private static String insertSql(String[] arrValue) throws AppException {
		return SQLGenator.genSQL("I4130", arrValue);
	}

	private static String delSql(String user_id, String table_id)
			throws AppException {
		return SQLGenator.genSQL("D4131", user_id, table_id);
	}
}
