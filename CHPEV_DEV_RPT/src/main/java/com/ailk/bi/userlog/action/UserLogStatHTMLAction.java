package com.ailk.bi.userlog.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class UserLogStatHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int CONST_TOP_TEN = 10;

	private StringBuffer V_UI_SYS_LOG_DETAIL = null;
	private StringBuffer V_BROWSER_DETAIL = null;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		V_UI_SYS_LOG_DETAIL = new StringBuffer();
		V_UI_SYS_LOG_DETAIL
				.append(" (select x.log_seq,x.log_oper_no,y.user_name,to_char(x.log_oper_time, 'yyyymmdd') login_time,to_char(x.leave_time, 'yyyymmdd') "
						+ "leave_time,(x.leave_time- x.log_oper_time)*24*60 dualTime,z.dept_name,x.log_ip,x.log_oper_time,x.leave_time leaveYMD");
		V_UI_SYS_LOG_DETAIL
				.append(" from ui_sys_log x, ui_info_user y, ui_info_dept z where x.log_oper_no = y.user_id and y.dept_id=z.dept_id");
		V_UI_SYS_LOG_DETAIL
				.append(" and x.log_oper_time is not null and x.leave_time is not null and x.log_type='1') ");

		V_BROWSER_DETAIL = new StringBuffer();
		V_BROWSER_DETAIL
				.append("(select x.log_seq,x.log_oper_no,y.user_name,to_char(x.log_oper_time, 'yyyymmdd') login_time,"
						+ "to_char(x.leave_time, 'yyyymmdd') leave_time,z.dept_name,x.log_ip,x.log_oper_time,x.leave_time leaveYMD,x.obj_id,x.obj_name");
		V_BROWSER_DETAIL
				.append(" from ui_sys_log x, ui_info_user y, ui_info_dept z where x.log_oper_no = y.user_id and y.dept_id=z.dept_id");
		V_BROWSER_DETAIL
				.append(" and x.log_oper_time is not null and x.log_type='2') ");

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String opType = StringB.NulltoBlank(request.getParameter("opType"))
				.toLowerCase();
		HttpSession session = request.getSession(true);

		// 对于登录系统的处理
		String strReturn = "";

		if (opType.equals("loginstat")) {// 登录统计
			session.setAttribute("VIEW_TREE_LIST",
					userLoginStatistic(request, response, session));
			strReturn = "loginstatic";

		} else if (opType.equals("logindetail")) {// 登录详情
			session.setAttribute("VIEW_TREE_LIST",
					userLoginDetail(request, response, session));
			strReturn = "logindetail";

		} else if (opType.equals("browserdetail")) {// sessionid访问详情
			session.setAttribute("VIEW_TREE_LIST",
					userBrowserDetail(request, response, session));
			strReturn = "browserdetail";

		} else if (opType.equals("rptclickstatic")) {// 报表访问统计
			session.setAttribute("VIEW_TREE_LIST",
					reportClickStatistic(request, response, session));
			strReturn = "rptclickstatic";

		} else if (opType.equals("rptclickdetail")) {// 报表访问统计
			session.setAttribute("REPORT_CLICK_LIST",
					reportClickDetail_byUser(request, response, session));
			strReturn = "rptclickdetail";

		} else if (opType.equals("visitsort")) {// 排名统计
			visitSortStatic(request, response, session);
			// session.setAttribute("REPORT_CLICK_LIST",
			// visitSortStatic(request,response,session));
			strReturn = "visitsortstatic";
		} else if (opType.equals("resvisitdtl")) {// 资源访问明细(访问日志)
			session.setAttribute("VIEW_TREE_LIST",
					resourceVisitDetail(request, response, session));
			strReturn = "resvisitdtl";

		} else if (opType.equals("adhocexp")) {// 即席查询清单导出
			session.setAttribute("VIEW_TREE_LIST",
					userAdhocExpListsDtl(request, response, session));
			request.setAttribute("init", "true");
			strReturn = "adhocexp_dtl";

		}

		setNextScreen(request, strReturn + ".screen");
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @desc:查询即席查询导出清单日志
	 * @return
	 */
	private String[][] userAdhocExpListsDtl(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String qryStartDate = StringB.NulltoBlank(request
				.getParameter("qryStartDate"));
		if (qryStartDate.length() == 0) {
			Calendar ca = Calendar.getInstance();
			// Date now = ca.getTime();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			qryStartDate = formatter.format(ca.getTime());
		}
		String qryEndDate = StringB.NulltoBlank(request
				.getParameter("qryEndDate"));
		if (qryEndDate.length() == 0) {
			qryEndDate = qryStartDate;
		}

		String userId = StringB.NulltoBlank(request.getParameter("userId"));
		String userName = StringB.NulltoBlank(request.getParameter("userName"));

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryStartDate;
		qryStruct.dim2 = qryEndDate;
		qryStruct.dim3 = userId;
		qryStruct.dim4 = userName;

		String sql = "select t.oper_oper_no,b.user_name,to_char(t.oper_time, 'yyyy-MM-dd HH24:mm:ss'),t.oper_ip,t.qry_sql"
				+ " from ui_adhoc_user_qry_log t, ui_info_user b where  t.oper_oper_no = b.user_id ";

		if (qryStruct.dim1.length() > 0) {
			sql += " and to_char(t.oper_time,'yyyymmdd')>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and to_char(t.oper_time,'yyyymmdd')<=" + qryStruct.dim2;

		}

		if (qryStruct.dim3.length() > 0) {
			sql += " and t.oper_oper_no='" + qryStruct.dim3 + "'";

		}
		if (qryStruct.dim4.length() > 0) {
			sql += " and b.user_name like '%" + qryStruct.dim4 + "%'";

		}
		sql += " order by t.oper_time desc";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	private String[][] resourceVisitDetail(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String qryStartDate = StringB.NulltoBlank(request
				.getParameter("qryStartDate"));
		if (qryStartDate.length() == 0) {
			Calendar ca = Calendar.getInstance();

			// Date now = ca.getTime();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			qryStartDate = formatter.format(ca.getTime());
		}
		String qryEndDate = StringB.NulltoBlank(request
				.getParameter("qryEndDate"));
		if (qryEndDate.length() == 0) {
			qryEndDate = qryStartDate;
		}

		String userId = StringB.NulltoBlank(request.getParameter("userId"));
		String resname = StringB.NulltoBlank(request.getParameter("resname"));

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryStartDate;
		qryStruct.dim2 = qryEndDate;
		qryStruct.dim3 = userId;
		qryStruct.dim4 = resname;

		String sql = "Select a.log_oper_no,a.user_name,a.dept_name,to_char(a.log_oper_time,'yyyy-mm-dd hh24:mm:ss'),a.log_ip,a.obj_name from "
				+ V_BROWSER_DETAIL.toString() + " a " + "where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		if (qryStruct.dim3.length() > 0) {
			sql += " and a.log_oper_no='" + qryStruct.dim3 + "'";

		}
		if (qryStruct.dim4.length() > 0) {
			sql += " and a.obj_name like '%" + qryStruct.dim4 + "%'";

		}
		sql += " order by a.log_oper_time desc";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @desc：登录访问统计
	 */
	private void visitSortStatic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String qryStartDate = StringB.NulltoBlank(request
				.getParameter("qryStartDate"));
		if (qryStartDate.length() == 0) {
			Calendar ca = Calendar.getInstance();
			// Date now = ca.getTime();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			qryStartDate = formatter.format(ca.getTime());
		}
		String qryEndDate = StringB.NulltoBlank(request
				.getParameter("qryEndDate"));
		if (qryEndDate.length() == 0) {
			qryEndDate = qryStartDate;
		}

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryStartDate;
		qryStruct.dim2 = qryEndDate;

		String sql = "select user_name,count(*) cnt from "
				+ V_UI_SYS_LOG_DETAIL.toString() + " a " + " where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		sql += " group by user_name ORDER BY CNT DESC ";
		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		request.setAttribute("USER_LOG_DTL", result);

		StringBuffer sb = new StringBuffer();
		sb.append("<chart palette='4' decimals='0' enableSmartLabels='1' enableRotation='0' inThousandSeparator=',' inDecimalSeparator=',' formatNumberScale='2' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70'>");
		if (result == null || result.length == 0) {

		} else {
			for (int i = 0; i < result.length; i++) {
				if (i == CONST_TOP_TEN) {
					break;
				}
				sb.append("<set label='" + result[i][0] + "' value='"
						+ result[i][1] + "' /> ");
			}
		}

		sb.append("</chart>");
		request.setAttribute("USER_LOG_DTL_CHART", sb.toString());

		// 部门统计访问
		sql = "select dept_name,count(*) cnt from "
				+ V_UI_SYS_LOG_DETAIL.toString() + " a " + " where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}
		sql += " group by dept_name ORDER BY CNT DESC ";
		result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		request.setAttribute("DEPT_LOG_DTL", result);

		sb = new StringBuffer();
		sb.append("<chart palette='4' decimals='0' inThousandSeparator=',' inDecimalSeparator=',' formatNumberScale='2' enableSmartLabels='1' enableRotation='0' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70'>");
		if (result == null || result.length == 0) {

		} else {
			for (int i = 0; i < result.length; i++) {
				if (i == CONST_TOP_TEN) {
					break;
				}
				sb.append("<set label='" + result[i][0] + "' value='"
						+ result[i][1] + "' /> ");
			}
		}

		sb.append("</chart>");
		request.setAttribute("DEPT_LOG_DTL_CHART", sb.toString());

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

	}

	private String[][] userBrowserDetail(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		ReportQryStruct qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		String sql = "select a.log_oper_no,a.user_name,to_char(a.log_oper_time,'yyyy-mm-dd hh24:mm:ss'),a.log_ip,a.log_seq,a.obj_id,a.obj_name from "
				+ V_BROWSER_DETAIL.toString() + " a where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		String sessionid = StringB.NulltoBlank(request
				.getParameter("sessionid"));
		if (sessionid.length() > 0) {
			sql += " and a.log_seq='" + sessionid + "'";
		}

		sql += " order by a.log_oper_time";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		// request.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	private String[][] userLoginDetail(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		ReportQryStruct qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		String sql = "select a.log_oper_no,a.user_name,to_char(a.log_oper_time,'yyyy-mm-dd hh24:mm:ss'),to_char(a.leaveYMD,'yyyy-mm-dd hh24:mm:ss'),a.log_ip,a.log_seq from "
				+ V_UI_SYS_LOG_DETAIL.toString() + " a where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		String userId = StringB.NulltoBlank(request.getParameter("userid"));
		sql += " and a.log_oper_no='" + userId + "'";

		sql += " order by a.log_oper_time desc";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		// request.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	private String[][] userLoginStatistic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String qryStartDate = StringB.NulltoBlank(request
				.getParameter("qryStartDate"));
		if (qryStartDate.length() == 0) {
			Calendar ca = Calendar.getInstance();
			// Date now = ca.getTime();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			qryStartDate = formatter.format(ca.getTime());
		}
		String qryEndDate = StringB.NulltoBlank(request
				.getParameter("qryEndDate"));
		if (qryEndDate.length() == 0) {
			qryEndDate = qryStartDate;
		}

		String userId = StringB.NulltoBlank(request.getParameter("userId"));
		String userName = StringB.NulltoBlank(request.getParameter("userName"));

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryStartDate;
		qryStruct.dim2 = qryEndDate;
		qryStruct.dim3 = userId;
		qryStruct.dim4 = userName;

		String sql = "Select a.log_oper_no,a.user_name,a.dept_name,count(*),sum(a.dualTime) from "
				+ V_UI_SYS_LOG_DETAIL.toString() + " a " + "where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		if (qryStruct.dim3.length() > 0) {
			sql += " and a.log_oper_no='" + qryStruct.dim3 + "'";

		}
		if (qryStruct.dim4.length() > 0) {
			sql += " and a.user_name like '%" + qryStruct.dim4 + "%'";

		}
		sql += " group by a.log_oper_no,a.user_name,a.dept_name order by a.log_oper_no";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	private String[][] reportClickStatistic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		String qryStartDate = StringB.NulltoBlank(request
				.getParameter("qryStartDate"));
		if (qryStartDate.length() == 0) {
			Calendar ca = Calendar.getInstance();
			// Date now = ca.getTime();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			qryStartDate = formatter.format(ca.getTime());
		}
		String qryEndDate = StringB.NulltoBlank(request
				.getParameter("qryEndDate"));
		if (qryEndDate.length() == 0) {
			qryEndDate = qryStartDate;
		}

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim1 = qryStartDate;
		qryStruct.dim2 = qryEndDate;

		String sql = "select rank() over(order by COUNT(*) DESC) as xx, OBJ_ID, OBJ_NAME, COUNT(*) CNT "
				+ "FROM " + V_BROWSER_DETAIL.toString() + " a " + " where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}

		sql += " GROUP BY OBJ_ID, OBJ_NAME ORDER BY CNT DESC ";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);
		StringBuffer sb = new StringBuffer();
		sb.append("<chart palette='4' decimals='0' inThousandSeparator=',' inDecimalSeparator=',' formatNumberScale='2' enableSmartLabels='1' enableRotation='0' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70'>");
		if (result == null || result.length == 0) {

		} else {
			for (int i = 0; i < result.length; i++) {
				if (i == CONST_TOP_TEN) {
					break;
				}
				sb.append("<set label='" + result[i][2] + "' value='"
						+ result[i][3] + "' /> ");
			}
		}

		sb.append("</chart>");
		// System.out.println(sb.toString());
		session.setAttribute(WebKeys.ATTR_KpiDateForChart, sb.toString());
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

	private String[][] reportClickDetail_byUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		ReportQryStruct qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		String res_id = StringB.NulltoBlank(request.getParameter("res_id"));
		String sql = "select a.log_oper_no,a.user_name,a.dept_name,count(*) CNT"
				+ " FROM "
				+ V_BROWSER_DETAIL.toString()
				+ " a "
				+ " where 1=1 ";
		if (qryStruct.dim1.length() > 0) {
			sql += " and a.login_time>=" + qryStruct.dim1;

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and a.login_time<=" + qryStruct.dim2;

		}
		if (res_id.length() > 0) {
			sql += " and a.obj_id='" + res_id + "'";
		}

		sql += " GROUP BY a.log_oper_no,a.user_name,a.dept_name ORDER BY CNT DESC ";

		String[][] result = DAOFactory.getBulletinDao().qryObjectInfoList(sql);

		request.setAttribute("res_id", res_id);
		// session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		return result;
	}

}
