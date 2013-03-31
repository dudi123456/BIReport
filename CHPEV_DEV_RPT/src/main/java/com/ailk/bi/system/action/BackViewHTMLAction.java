package com.ailk.bi.system.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.UiSysInfoBackTable;
import com.ailk.bi.base.table.UiSysInfoRebackTable;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.*;

@SuppressWarnings({ "rawtypes" })
public class BackViewHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3583492342986992918L;

	@SuppressWarnings({ "unchecked" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}
		System.out.println("oper_type=====================" + operType);
		// 当前反馈对象
		String obj = CommTool.getBackObjFromSession(session);
		if (obj == null) {
			obj = "0";
		}

		//
		UiSysInfoBackTable infoBacks[] = null;
		LsbiQryStruct qryStruct = new LsbiQryStruct();

		if ("0".equals(operType)) {
			infoBacks = getBackInfo(obj);
			session.removeAttribute(WebKeys.ATTR_UiSysInfoBackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoBackTable, infoBacks);
			setNextScreen(request, "BackView.screen");

		} else if ("1".equals(operType)) {

			UiSysInfoBackTable infoBack = new UiSysInfoBackTable();

			try {
				AppWebUtil.getHtmlObject(request, "back", infoBack);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "系统反馈页面信息有误！");
			}
			//
			System.out.println("infoBack====================="
					+ infoBack.back_info);
			//
			String loginID = CommonFacade.getLoginId(session);
			String loginName = CommonFacade.getLoginName(session);
			String obj_name = CommTool.getObjName(session, obj);

			//
			String sql = "";
			try {
				if ("".equals(infoBack.back_icon)) {
					sql = SQLGenator.genSQL("I1022", obj, obj_name, loginID,
							loginName, infoBack.back_info);
				} else {
					sql = SQLGenator.genSQL("I1021", obj, obj_name, loginID,
							loginName, infoBack.back_info, infoBack.back_icon);
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}

			try {
				int i = WebDBUtil.execUpdate(sql);
				if (i < 0) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "反馈信息失败！");
				} else {
					throw new HTMLActionException(session,
							HTMLActionException.SUCCESS_PAGE, "反馈信息成功！",
							"BackView.do?oper_type=0");

				}
			} catch (AppException ex1) {
				ex1.printStackTrace();
			}
		} else if ("2".equals(operType)) {
			String back_id = request.getParameter("back_id");
			if (back_id == null) {
				back_id = "";
			}

			String obj_id = request.getParameter("obj_id");
			if (obj_id == null) {
				obj_id = "";
			}
			UiSysInfoRebackTable[] rebacks = getReBackInfo(back_id);
			session.removeAttribute(WebKeys.ATTR_UiSysInfoRebackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoRebackTable, rebacks);
			setNextScreen(request, "REBACKLIST.screen");
		} else if ("3".equals(operType)) {
			String back_id = request.getParameter("back_id");
			if (back_id == null) {
				back_id = "";
			}

			int i = delBackInfo(session, back_id);
			if (i < 0) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "删除反馈信息失败!");
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "删除反馈信息成功!",
						"BackView.do?oper_type=7");
			}

		} else if ("4".equals(operType)) {
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "提取反馈管理信息有误!");
			}
			//
			if ("".equals(qryStruct.begin_day)) {
				qryStruct.begin_day = DateUtil.getDiffDay(-7,
						DateUtil.getNowDate());

			}
			if ("".equals(qryStruct.end_day)) {
				qryStruct.end_day = DateUtil.getDiffDay(0,
						DateUtil.getNowDate());
			}
			String whereStr = getWhereStrByStruct(qryStruct);
			//
			infoBacks = getBackList(whereStr);
			//
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct, qryStruct);
			session.removeAttribute(WebKeys.ATTR_UiSysInfoBackTable_B);
			session.setAttribute(WebKeys.ATTR_UiSysInfoBackTable_B, infoBacks);
			setNextScreen(request, "BackMain.screen");

		} else if ("5".equals(operType)) {

			String back_id = request.getParameter("back_id");
			if (back_id == null) {
				back_id = "";
			}
			UiSysInfoRebackTable[] rebacks = getReBackInfo(back_id);
			UiSysInfoBackTable back = getBackInfoByBackId(back_id);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysInfoRebackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoRebackTable, rebacks);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysInfoBackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoBackTable, back);

			setNextScreen(request, "REBACKEDIT.screen");
		} else if ("6".equals(operType)) {
			// 信息
			String back_id = CommTool.getParameterGB(request, "back_id");
			if (back_id == null) {
				back_id = "";
			}
			String obj_id = CommTool.getParameterGB(request, "obj_id");
			if (obj_id == null) {
				obj_id = "";
			}
			String obj_name = CommTool.getParameterGB(request, "obj_name");
			if (obj_name == null) {
				obj_name = "";
			}
			// 回复
			String reback_info = CommTool.getParameterGB(request,
					"reback__reback_info");
			if (reback_info == null) {
				reback_info = "";
			}
			//
			String oper_no = CommonFacade.getLoginId(session);
			String oper_name = CommonFacade.getLoginName(session);

			// sql
			try {
				Vector v = new Vector();
				String sqla = SQLGenator.genSQL("I1026", back_id, obj_id,
						obj_name, oper_no, oper_name, reback_info);
				System.out.println("I1026=============" + sqla);
				v.add(sqla);
				String sqlb = SQLGenator.genSQL("C1027", "200", back_id);
				System.out.println("C1027=============" + sqlb);
				v.add(sqlb);
				String sqlArr[] = new String[v.size()];
				for (int i = 0; i < v.size(); i++) {
					sqlArr[i] = v.get(i).toString();
				}
				int i = WebDBUtil.execTransUpdate(sqlArr);
				String url = "BackView.do?oper_type=5&back_id=" + back_id;
				if (i > 0) {
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.SUCCESS_PAGE, "回复信息添加成功！", url);
				} else {
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.ERROR_PAGE, "回复信息添加失败！");
				}

			} catch (AppException ex) {
				ex.printStackTrace();
			}
		} else if ("7".equals(operType)) {
			qryStruct = (LsbiQryStruct) session
					.getAttribute(WebKeys.ATTR_LsbiQryStruct);

			String whereStr = getWhereStrByStruct(qryStruct);
			//
			infoBacks = getBackList(whereStr);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysInfoBackTable_B);
			session.setAttribute(WebKeys.ATTR_UiSysInfoBackTable_B, infoBacks);
			setNextScreen(request, "BackMain.screen");
		} else if ("8".equals(operType)) {
			String back_id = request.getParameter("back_id");
			if (back_id == null) {
				back_id = "";
			}
			UiSysInfoRebackTable[] rebacks = getReBackInfo(back_id);
			UiSysInfoBackTable back = getBackInfoByBackId(back_id);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysInfoRebackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoRebackTable, rebacks);
			//
			session.removeAttribute(WebKeys.ATTR_UiSysInfoBackTable);
			session.setAttribute(WebKeys.ATTR_UiSysInfoBackTable, back);

			setNextScreen(request, "EDITBACK.screen");
		} else if ("9".equals(operType)) {
			UiSysInfoBackTable info_back = new UiSysInfoBackTable();
			try {
				AppWebUtil.getHtmlObject(request, "back", info_back);
			} catch (AppException ex) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "提取反馈信息有误!");
			}
			//
			int i = UpdateBackInfo(session, info_back.back_status,
					info_back.back_info, info_back.back_id);
			//
			if (i < 0) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.ERROR_PAGE, "反馈信息编辑失败！");
			} else {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.SUCCESS_PAGE, "反馈信息编辑成功！",
						"BackView.do?oper_type=8&back_id=" + info_back.back_id);

			}
		}
	}

	/**
	 * 提取当前对象的反馈信息历史列表
	 * 
	 * @param obj_id
	 * @return
	 */
	public static UiSysInfoBackTable[] getBackInfo(String obj_id) {
		UiSysInfoBackTable[] backs = null;
		try {
			String sql = SQLGenator.genSQL("Q1020", obj_id);
			System.out.println("Q1020=========" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				backs = new UiSysInfoBackTable[v.size()];
			}
			for (int i = 0; v != null && i < v.size(); i++) {
				Vector temp = (Vector) v.get(i);
				backs[i] = new UiSysInfoBackTable();
				backs[i].back_id = (String) temp.get(0);
				backs[i].obj_id = (String) temp.get(1);
				backs[i].obj_name = (String) temp.get(2);
				backs[i].back_oper_no = (String) temp.get(3);
				backs[i].back_oper_name = (String) temp.get(4);
				backs[i].back_info = (String) temp.get(5);
				backs[i].back_date = (String) temp.get(6);
				backs[i].back_status = (String) temp.get(7);
				backs[i].back_icon = (String) temp.get(8);
				backs[i].back_date_other = (String) temp.get(9);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return backs;

	}

	/**
	 * 提取反馈标识提取的反馈信息
	 * 
	 * @param obj_id
	 * @return
	 */
	public static UiSysInfoBackTable getBackInfoByBackId(String back_id) {
		UiSysInfoBackTable back = new UiSysInfoBackTable();
		try {
			String sql = SQLGenator.genSQL("Q1025", back_id);
			System.out.println("Q1025=========" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				Vector temp = (Vector) v.get(0);
				back.back_id = (String) temp.get(0);
				back.obj_id = (String) temp.get(1);
				back.obj_name = (String) temp.get(2);
				back.back_oper_no = (String) temp.get(3);
				back.back_oper_name = (String) temp.get(4);
				back.back_info = (String) temp.get(5);
				back.back_date = (String) temp.get(6);
				back.back_status = (String) temp.get(7);
				back.back_icon = (String) temp.get(8);
				back.back_date_other = (String) temp.get(9);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return back;

	}

	/**
	 * 取得反馈回复列表
	 * 
	 * @param back_id
	 * 
	 * @param obj_id
	 * @return
	 */
	public static UiSysInfoRebackTable[] getReBackInfo(String back_id) {
		UiSysInfoRebackTable[] backs = null;
		try {
			String sql = SQLGenator.genSQL("Q1023", back_id);
			System.out.println("Q1023=========" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				backs = new UiSysInfoRebackTable[v.size()];
			}
			for (int i = 0; v != null && i < v.size(); i++) {
				Vector temp = (Vector) v.get(i);
				backs[i] = new UiSysInfoRebackTable();
				backs[i].reback_id = (String) temp.get(0);
				backs[i].back_id = (String) temp.get(1);
				backs[i].obj_id = (String) temp.get(2);
				backs[i].obj_name = (String) temp.get(3);
				backs[i].reback_oper_no = (String) temp.get(4);
				backs[i].reback_oper_name = (String) temp.get(5);
				backs[i].reback_info = (String) temp.get(6);
				backs[i].reback_date = (String) temp.get(7);
				backs[i].reback_status = (String) temp.get(8);
				backs[i].reback_icon = (String) temp.get(9);
				backs[i].reback_date_other = (String) temp.get(10);

			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return backs;
	}

	/**
	 * 根据条件查询反馈信息
	 * 
	 * @param whereStr
	 * @return
	 */
	public static UiSysInfoBackTable[] getBackList(String whereStr) {
		UiSysInfoBackTable[] backs = null;
		//
		try {
			String sql = SQLGenator.genSQL("Q1024", whereStr);
			System.out.println("Q1024=========" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				backs = new UiSysInfoBackTable[v.size()];
			}
			for (int i = 0; v != null && i < v.size(); i++) {
				Vector temp = (Vector) v.get(i);
				backs[i] = new UiSysInfoBackTable();
				backs[i] = new UiSysInfoBackTable();
				backs[i].back_id = (String) temp.get(0);
				backs[i].obj_id = (String) temp.get(1);
				backs[i].obj_name = (String) temp.get(2);
				backs[i].back_oper_no = (String) temp.get(3);
				backs[i].back_oper_name = (String) temp.get(4);
				backs[i].back_info = (String) temp.get(5);
				backs[i].back_date = (String) temp.get(6);
				backs[i].back_status = (String) temp.get(7);
				backs[i].back_icon = (String) temp.get(8);
				backs[i].back_date_other = (String) temp.get(9);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return backs;
	}

	/**
	 * 取得查询条件
	 * 
	 * @param qryStruct
	 * @return
	 */
	public static String getWhereStrByStruct(LsbiQryStruct qryStruct) {
		String whereStr = " AND 1=1 ";
		if (qryStruct == null) {
			return whereStr;
		}
		// 起始日期
		if (!"".equals(qryStruct.begin_day)) {
			whereStr += " AND　to_char(back_date,'YYYYMMDD') >='"
					+ qryStruct.begin_day + "'";
		}
		// 结束日期
		if (!"".equals(qryStruct.end_day)) {
			whereStr += " AND　to_char(back_date,'YYYYMMDD') <='"
					+ qryStruct.end_day + "'";
		}
		// 反馈对象
		if (!"".equals(qryStruct.obj_name)) {
			whereStr += " AND　obj_name like '%" + qryStruct.obj_name + "%'";
		}

		// 对象
		if (!"".equals(qryStruct.obj_id)) {
			whereStr += " AND　obj_id ='" + qryStruct.obj_id + "'";
		}

		// 反馈
		if (!"".equals(qryStruct.back_id)) {
			whereStr += " AND　back_id =" + qryStruct.back_id;
		}

		// 反馈用户
		if (!"".equals(qryStruct.oper_name)) {
			whereStr += " AND　back_oper_name like '%" + qryStruct.oper_name
					+ "%'";
		}
		// 反馈状态
		if (!"".equals(qryStruct.back_status)) {
			whereStr += " AND　back_status ='" + qryStruct.back_status + "'";
		}
		return whereStr;
	}

	/**
	 * 删除用户反馈信息! 假如有反馈回复信息,则连带一起删除! 没有则,直接删除反馈信息!
	 * 
	 * @param back_id
	 * 
	 * @return
	 * @throws HTMLActionException
	 */
	public static int delBackInfo(HttpSession session, String back_id)
			throws HTMLActionException {

		int i = -1;
		String flag = hasReBack(back_id);

		if ("0".equals(flag)) {// 没有
			//
			try {
				String sql = SQLGenator.genSQL("C1029", back_id);
				i = WebDBUtil.execUpdate(sql);
			} catch (AppException ex) {
				ex.printStackTrace();
			}

		} else if ("1".equals(flag)) {// 有
			String sqla = "";
			// 删除反馈回复信息
			try {
				sqla = SQLGenator.genSQL("C1030", back_id);

			} catch (AppException ex) {
				ex.printStackTrace();
			}
			//
			String sqlb = "";
			// 删除反馈信息
			try {
				sqlb = SQLGenator.genSQL("C1029", back_id);

			} catch (AppException ex) {
				ex.printStackTrace();
			}
			//
			String sqlArr[] = { sqla, sqlb };
			i = 1;
			try {
				WebDBUtil.execTransUpdate(sqlArr);
			} catch (AppException e) {

				e.printStackTrace();
				i = -1;
			}
		}
		return i;
	}

	/**
	 * 判断当前反馈信息有没有回复信息
	 * 
	 * @param back_id
	 * @param back_type
	 * @param back_lvl
	 * @return
	 */
	public static String hasReBack(String back_id) {
		String flag = "0";
		try {
			String sql = SQLGenator.genSQL("Q1028", back_id);
			System.out.println("Q1028=========" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = "1";
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 变更反馈信息
	 * 
	 * @param session
	 * @param back_id
	 * @return
	 * @throws HTMLActionException
	 */
	public static int UpdateBackInfo(HttpSession session, String back_status,
			String back_info, String back_id) throws HTMLActionException {
		int i = -1;
		try {
			String sql = SQLGenator.genSQL("C1031", back_status, back_info,
					back_id);
			System.out.println("C1031=========" + sql);
			i = WebDBUtil.execUpdate(sql);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return i;
	}
}
