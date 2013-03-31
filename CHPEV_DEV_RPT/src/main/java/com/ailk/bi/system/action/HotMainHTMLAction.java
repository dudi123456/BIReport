package com.ailk.bi.system.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.*;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes" })
public class HotMainHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6328597759056544605L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		//
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}
		System.out.println("==================oper_type================"
				+ operType);
		if ("0".equals(operType)) {// 登陆访问热点点击
			// 查询界面信息
			LsbiQryStruct qry = new LsbiQryStruct();
			// 取页面用户信息
			try {
				AppWebUtil.getHtmlObject(request, "qry", qry);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "用户受理信息有误！");
			}

			if (qry.begin_day == null || "".equals(qry.begin_day)) {
				qry.begin_day = DateUtil.getDiffDay(-15, DateUtil.getNowDate());
			}
			if (qry.end_day == null || "".equals(qry.end_day)) {
				qry.end_day = DateUtil.getDiffDay(0, DateUtil.getNowDate());
			}

			// 查询条件
			String whereStr = "";
			String whereStr_A = "";
			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}
			//
			if (qry.oper_no != null && !"".equals(qry.oper_no)) {
				whereStr += " and log_oper_no ='" + qry.oper_no + "'";
				whereStr_A += " and log_oper_no ='" + qry.oper_no + "'";
			}

			// 条件查询
			UiSysLogTable logs[] = getLogFixFilter("Q1007", whereStr);
			// 累计查询
			UiSysLogTable log_A[] = getLogFixFilter("Q1008", whereStr_A);
			if (logs != null && logs.length > 0) {
				UiSysLogTable logseq[] = getLogFixSeq("Q1014", whereStr,
						logs[0].obj_id);
				session.removeAttribute(WebKeys.ATTR_UiSysLogTable_PIC_LIST);
				session.setAttribute(WebKeys.ATTR_UiSysLogTable_PIC_LIST,
						logseq);
			}
			//
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct, qry);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_A);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_A, logs);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_B);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_B, log_A);
			setNextScreen(request, "HotMain.screen");

		} else if ("1".equals(operType)) {// 选定特定报表类型分析该报表类型下的热点
			// 报表访问列表和报表的访问趋势图形
			String begin_day = CommTool.getParameterGB(request, "begin_day");
			String end_day = CommTool.getParameterGB(request, "end_day");
			String hot_type = CommTool.getParameterGB(request, "hot_type");
			String oper_no = CommTool.getParameterGB(request, "oper_no");

			if (begin_day == null || "".equals(begin_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[开始日期]缺失，请联系系统管理员！");
			}

			if (end_day == null || "".equals(end_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[结束日期]缺失，请联系系统管理员！");
			}

			if (hot_type == null || "".equals(hot_type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[热点类型]缺失，请联系系统管理员！");
			}

			// 传递特殊的界面参数
			LsbiQryStruct qry = new LsbiQryStruct();
			qry.begin_day = begin_day;
			qry.end_day = end_day;
			qry.hot_type = hot_type;
			qry.oper_no = oper_no;

			//
			String whereStr = "";

			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}
			if (qry.oper_no != null && !"".equals(qry.oper_no)) {
				whereStr += " and a.log_oper_no ='" + qry.oper_no + "'";
			}

			UiSysLogTable logs[] = null;
			UiSysLogTable logseq[] = null;
			// 热点日志列表
			logs = getLogFixFilter_T("Q1009", whereStr, hot_type);
			// 热点趋势
			String whereStrB = whereStr + " and a.obj_id = '" + logs[0].obj_id
					+ "'";
			logseq = getLogFixFilterSeq("Q1010", whereStrB);
			//

			//
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct_POP);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct_POP, qry);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_LIST);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_LIST, logs);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC, logseq);
			setNextScreen(request, "HotPop.screen");

		} else if ("2".equals(operType)) {
			String obj_id = CommTool.getParameterGB(request, "obj_id");
			if (obj_id == null || "".equals(obj_id)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[热点标识]缺失，请联系系统管理员！");
			}
			LsbiQryStruct qry = (LsbiQryStruct) session
					.getAttribute(WebKeys.ATTR_LsbiQryStruct_POP);

			String whereStr = "";

			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}

			if (qry.oper_no != null && !"".equals(qry.oper_no)) {
				whereStr += " and a.log_oper_no ='" + qry.oper_no + "'";
			}

			whereStr += "and a.obj_id = '" + obj_id + "'";
			//
			UiSysLogTable logseq[] = null;
			logseq = getLogFixFilterSeq("Q1010", whereStr);

			//
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC, logseq);
			setNextScreen(request, "PicPOP.screen");
		} else if ("3".equals(operType)) {// 热点类型趋势

			String hot_type = CommTool.getParameterGB(request, "hot_type");
			if (hot_type == null || "".equals(hot_type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[热点类型]缺失，请联系系统管理员！");
			}

			LsbiQryStruct qry = (LsbiQryStruct) session
					.getAttribute(WebKeys.ATTR_LsbiQryStruct);

			String whereStr = "";

			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}

			UiSysLogTable logseq[] = null;
			logseq = getLogFixSeq("Q1014", whereStr, hot_type);

			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_PIC_LIST);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_PIC_LIST, logseq);
			setNextScreen(request, "PicList.screen");

		} else if ("4".equals(operType)) {// ok
			// 报表访问列表和报表的访问趋势图形
			String begin_day = CommTool.getParameterGB(request,
					"pop__begin_day");
			String end_day = CommTool.getParameterGB(request, "pop__end_day");
			String hot_type = CommTool.getParameterGB(request, "pop__hot_type");
			String oper_no = CommTool.getParameterGB(request, "pop__oper_no");
			System.out.println("begin_day=====" + begin_day);
			System.out.println("end_day=====" + end_day);
			System.out.println("hot_type=====" + hot_type);
			System.out.println("oper_no=====" + oper_no);

			if (begin_day == null || "".equals(begin_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[开始日期]缺失，请联系系统管理员！");
			}

			if (end_day == null || "".equals(end_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[结束日期]缺失，请联系系统管理员！");
			}

			if (hot_type == null || "".equals(hot_type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[热点类型]缺失，请联系系统管理员！");
			}

			// 传递特殊的界面参数
			LsbiQryStruct qry = new LsbiQryStruct();
			qry.begin_day = begin_day;
			qry.end_day = end_day;
			qry.hot_type = hot_type;
			qry.oper_no = oper_no;

			//
			String whereStr = "";

			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}
			if (qry.oper_no != null && !"".equals(qry.oper_no)) {
				whereStr += " and a.log_oper_no ='" + qry.oper_no + "'";
			}

			UiSysLogTable logs[] = null;
			UiSysLogTable logseq[] = null;
			// 热点日志列表
			logs = getLogFixFilter_T("Q1009", whereStr, hot_type);
			// 热点趋势
			String whereStrB = whereStr + " and a.obj_id = '" + logs[0].obj_id
					+ "'";
			logseq = getLogFixFilterSeq("Q1010", whereStrB);
			//

			//
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct_POP);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct_POP, qry);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_LIST);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_LIST, logs);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_PIC, logseq);
			setNextScreen(request, "HotPop.screen");

		} else if ("5".equals(operType)) {

			// 取得条件值
			String begin_day = CommTool.getParameterGB(request, "begin_day");
			String end_day = CommTool.getParameterGB(request, "end_day");
			String hot_type = CommTool.getParameterGB(request, "hot_type");

			if (begin_day == null || "".equals(begin_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[开始日期]缺失，请联系系统管理员！");
			}

			if (end_day == null || "".equals(end_day)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[结束日期]缺失，请联系系统管理员！");
			}

			if (hot_type == null || "".equals(hot_type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"界面传递参数[热点类型]缺失，请联系系统管理员！");
			}

			// 查询结构
			LsbiQryStruct qry = new LsbiQryStruct();
			qry.begin_day = begin_day;
			qry.end_day = end_day;
			// 默认显示页面调用对象信息
			qry.hot_type = hot_type;
			// 条件
			String whereStr = "";

			if (qry.begin_day != null && !"".equals(qry.begin_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') >="
						+ qry.begin_day;
			}
			if (qry.end_day != null && !"".equals(qry.end_day)) {
				whereStr += " and to_char(a.log_oper_time,'YYYYMMDD') <="
						+ qry.end_day;
			}

			// 取值
			UiSysLogTable[] logs = getSinglePer(whereStr, qry.hot_type);
			// top 10
			UiSysLogTable[] logTop = getSinglePerTop(whereStr, qry.hot_type);
			// bottom 10
			UiSysLogTable[] logBop = getSinglePerBop(whereStr, qry.hot_type);

			// session
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct_POP);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct_POP, qry);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single, logs);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single_Top);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single_Top,
					logTop);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single_Bop);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_POP_Single_Bop,
					logBop);

			setNextScreen(request, "PicPOPSingle.screen");
		}

	}

	/**
	 * 
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getLogFixFilter(String ID, String whereStr) {
		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL(ID, whereStr);
			System.out.println(ID + "=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_index = String.valueOf(i + 1);
				logs[i].obj_id = ((Vector) v.get(i)).get(0).toString();
				logs[i].obj_name = ((Vector) v.get(i)).get(1).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(2).toString();
			}
		}
		return logs;

	}

	/**
	 * 
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getLogFixSeq(String ID, String whereStr, String type) {
		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL(ID, whereStr, type);
			System.out.println(ID + "=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_oper_time = ((Vector) v.get(i)).get(0).toString();
				logs[i].obj_id = ((Vector) v.get(i)).get(1).toString();
				logs[i].obj_name = ((Vector) v.get(i)).get(2).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(3).toString();
			}
		}
		return logs;

	}

	/**
	 * 
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getLogFixFilter_T(String ID, String whereStr,
			String hot_type) {
		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL(ID, whereStr, hot_type);
			System.out.println(ID + "=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_index = String.valueOf(i + 1);
				logs[i].obj_id = ((Vector) v.get(i)).get(0).toString();
				logs[i].obj_name = ((Vector) v.get(i)).get(1).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(2).toString();
			}
		}
		return logs;

	}

	/**
	 * 
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getLogFixFilterSeq(String ID, String whereStr) {
		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL(ID, whereStr);
			System.out.println(ID + "=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_oper_time = ((Vector) v.get(i)).get(0).toString();
				logs[i].obj_id = ((Vector) v.get(i)).get(1).toString();
				logs[i].obj_name = ((Vector) v.get(i)).get(2).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(3).toString();
			}
		}
		return logs;

	}

	/**
	 * 取得单功能点用户访问战比明晰
	 * 
	 * @param ID
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getSinglePer(String whereStr, String hot_type) {

		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL("Q1011", whereStr, hot_type);
			System.out.println("Q1011=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_oper_no = ((Vector) v.get(i)).get(0).toString();
				logs[i].log_oper_name = ((Vector) v.get(i)).get(1).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(2).toString();
			}
		}
		return logs;

	}

	/**
	 * 取得单功能点用户访问战比明晰
	 * 
	 * @param ID
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getSinglePerTop(String whereStr, String hot_type) {

		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL("Q1012", whereStr, hot_type);
			System.out.println("Q1012=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_oper_no = ((Vector) v.get(i)).get(0).toString();
				logs[i].log_oper_name = ((Vector) v.get(i)).get(1).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(2).toString();
			}
		}
		return logs;

	}

	/**
	 * 取得单功能点用户访问战比明晰
	 * 
	 * @param ID
	 * @param whereStr
	 * @return
	 */
	public UiSysLogTable[] getSinglePerBop(String whereStr, String hot_type) {

		UiSysLogTable[] logs = null;
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL("Q1013", whereStr, hot_type);
			System.out.println("Q1013=========" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (v != null && v.size() > 0) {
			logs = new UiSysLogTable[v.size()];
			for (int i = 0; i < v.size(); i++) {
				logs[i] = new UiSysLogTable();
				logs[i].log_oper_no = ((Vector) v.get(i)).get(0).toString();
				logs[i].log_oper_name = ((Vector) v.get(i)).get(1).toString();
				logs[i].log_num = ((Vector) v.get(i)).get(2).toString();
			}
		}
		return logs;

	}

}
