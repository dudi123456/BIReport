package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.sigma.SigmaGridConstant;
import com.ailk.bi.sigma.SigmaPassParamBean;

/**
 * 
 * @author zhxiaojing
 * @desc 按饱和度导出用户清单FUI_ADV_ADDVAL_2_PP_M表
 * 
 * 
 */
@SuppressWarnings({ "unused" })
public class AddSubjectUserDetail extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String encoding = "UTF-8";
	protected static String ajaxEncoding = "UTF-8";

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();

		String opType = StringB.NulltoBlank(request.getParameter("opType"));
		// String cat_id = "1";//资费类别1:短信，2：手机上网

		if (opType.equalsIgnoreCase("showRpt")) {

			// cat_id = StringB.NulltoBlank(request.getParameter("cat_id"));

			String op_time = StringB.NulltoBlank(request
					.getParameter("OP_TIME"));
			String usage_ratio_lvl_id = StringB.NulltoBlank(request
					.getParameter("usage_ratio_lvl_id"));

			String addval_price_plan_id = StringB.NulltoBlank(request
					.getParameter("addval_price_plan_id"));
			String addval_price_plan_name = StringB.NulltoBlank(request
					.getParameter("addval_price_plan_name"));

			List<SigmaPassParamBean> listParam = new ArrayList<SigmaPassParamBean>();

			SigmaPassParamBean bean = new SigmaPassParamBean();
			bean.setPassParamName("in_param1");
			bean.setPassParamValue(op_time);
			bean.setPassParamDesc(op_time + "月");
			listParam.add(bean);

			bean = new SigmaPassParamBean();
			bean.setPassParamName("in_param2");
			bean.setPassParamValue(usage_ratio_lvl_id);
			bean.setPassParamDesc(usage_ratio_lvl_id + "级");
			listParam.add(bean);

			bean = new SigmaPassParamBean();
			bean.setPassParamName("in_param3");
			bean.setPassParamValue(addval_price_plan_id);
			bean.setPassParamDesc(addval_price_plan_name);
			listParam.add(bean);

			request.setAttribute(SigmaGridConstant.GRID_CON_PARAM_REQ,
					listParam);

			String sigmaId = request.getParameter("rpt_id");

			request.setAttribute("sigmaId", sigmaId);

			setNextScreen(request, "SigmaReportRedirectShow.screen");

		} else if (opType.equalsIgnoreCase("shortmsgComm")) {
			session.setAttribute("VIEW_TREE_LIST",
					shortMsgUserCommend(request, session, "1"));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_ShortMsgComm.screen");
		} else if (opType.equalsIgnoreCase("qry_AgainMsgComm")) {
			session.setAttribute("VIEW_TREE_LIST",
					qryAgain_shortMsgUserCommend(request, session, "1"));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_ShortMsgComm.screen");
		} else if (opType.equalsIgnoreCase("showflowComm")) {
			session.setAttribute("VIEW_TREE_LIST",
					shortMsgUserCommend(request, session, "2"));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_AddFlowComm.screen");

		} else if (opType.equalsIgnoreCase("qry_AgainMsgComm")) {

			session.setAttribute("VIEW_TREE_LIST",
					qryAgain_shortMsgUserCommend(request, session, "2"));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_AddFlowComm.screen");

		} else if (opType.equalsIgnoreCase("showConComm")) {
			// 内容订购推荐
			session.setAttribute("VIEW_TREE_LIST",
					shortConentCommend(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_AddContentComm.screen");

		} else if (opType.equalsIgnoreCase("qry_AgainConComm")) {
			// 内容订购推荐
			session.setAttribute("VIEW_TREE_LIST",
					qryAgain_ContentUserCommend(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_AddContentComm.screen");
		} else if (opType.equalsIgnoreCase("qryOffFcUser")) {
			// 清单提取
			session.setAttribute("VIEW_TREE_LIST",
					qryOffFcUser(request, session));
			request.setAttribute("init", true);
			setNextScreen(request, "Show_comm_off_fc.screen");
		} else if (opType.equalsIgnoreCase("qryCallDesc")) {
			// 清单提取
			String sql = "select user_id,acc_nbr,cust_name,USER_ONLINE_DURA,MAIN_PROD_NAME,OFFER_NAME,BILL_FEE,to_char(open_date,'yyyy-mm-dd'),CALL_TIMES,CALL_DURATION,LAST_1_TIMES,LAST_1_DURA,LAST_2_TIMES,LAST_2_DURA from FUI_CCH_CALL_DESCENT_USER_M where 1=1 ";
			boolean flag = true;
			String[] title = new String[] { "用户ID", "电话号码", "客户名称", "在网时长",
					"主产品", "销售品", "资费", "入网时间", "当月通话次数", "当月通话时长", "前1月通话次数",
					"前1月通话时长", "前2月通话次数", "前2月通话时长" };
			session.setAttribute("VIEW_TREE_LIST",
					qryCallDesc(sql, flag, request, session));
			request.setAttribute("init", true);
			session.setAttribute("lose_subject_flag", "true");
			session.setAttribute("lose_subject_title", title);
			setNextScreen(request, "Show_comm_call_desc.screen");
		} else if (opType.equalsIgnoreCase("qryLoseCf")) {
			// 清单提取
			String sql = "select user_id,acc_nbr,cust_name,USER_ONLINE_DURA,MAIN_PROD_NAME,OFFER_NAME,BILL_FEE,to_char(open_date,'yyyy-mm-dd'),USER_ARPU_LVL_NAME from FUI_CCH_CF_USER_D where 1=1 ";
			boolean flag = false;
			String[] title = new String[] { "用户ID", "电话号码", "客户名称", "在网时长",
					"主产品", "销售品", "资费", "入网时间", "ARPU分档" };
			session.setAttribute("VIEW_TREE_LIST",
					qryCallDesc(sql, flag, request, session));
			session.setAttribute("lose_subject_title", title);
			request.setAttribute("init", true);
			session.setAttribute("lose_subject_flag", "false");
			setNextScreen(request, "Show_comm_call_desc.screen");
		} else if (opType.equalsIgnoreCase("qryCallZero")) {
			// 清单提取
			String sql = "select user_id,acc_nbr,cust_name,USER_ONLINE_DURA,MAIN_PROD_NAME,OFFER_NAME,BILL_FEE,to_char(open_date,'yyyy-mm-dd'),USER_ARPU_LVL_NAME from FUI_CCH_CALL_ZERO_USER_M where 1=1 ";
			boolean flag = true;
			String[] title = new String[] { "用户ID", "电话号码", "客户名称", "在网时长",
					"主产品", "销售品", "资费", "入网时间", "ARPU分档" };
			session.setAttribute("VIEW_TREE_LIST",
					qryCallDesc(sql, flag, request, session));
			session.setAttribute("lose_subject_title", title);
			request.setAttribute("init", true);
			session.setAttribute("lose_subject_flag", "true");
			setNextScreen(request, "Show_comm_call_desc.screen");
		} else if (opType.equalsIgnoreCase("qryExpire")) {
			// 清单提取
			String sql = "select user_id,acc_nbr,cust_name,MAIN_PP_NAME,to_char(MAIN_PP_EXP_DATE,'yyyy-mm-dd'),BILL_FEE from FUI_CCH_PP_EXPIRE_USER_M where 1=1 ";

			String dataType = request.getParameter("data_type");
			String[] title = new String[6];
			if ("1".equals(dataType)) {
				title = new String[] { "用户ID", "电话号码", "客户名称", "租机协议名称",
						"协议到期时间", "出账费用" };
				sql += " and data_type=1 ";
			} else {
				title = new String[] { "用户ID", "电话号码", "客户名称", "无线宽带协议名称",
						"协议到期时间", "出账费用" };
				sql += " and data_type=2 ";
			}

			session.setAttribute("VIEW_TREE_LIST",
					qryExpire(sql, request, session));
			session.setAttribute("lose_subject_title", title);
			request.setAttribute("init", true);
			setNextScreen(request, "Show_comm_expire.screen");
		} else if (opType.equalsIgnoreCase("qryOffppc")) {
			// 清单提取
			String sql = "select user_id,acc_nbr,cust_name,USER_ONLINE_DURA,MAIN_PROD_NAME,OFFER_NAME,BILL_FEE,to_char(open_date,'yyyy-mm-dd') from FUI_CCH_OFF_PPC_CF_USER_M where 1=1 ";
			String[] title = new String[] { "用户ID", "电话号码", "客户名称", "在网时长",
					"主产品", "销售品", "资费", "入网时间" };
			session.setAttribute("VIEW_TREE_LIST",
					qryOffppc(sql, request, session));
			session.setAttribute("lose_subject_title", title);
			session.setAttribute("lose_subject_flag", "true");
			setNextScreen(request, "Show_comm_off_ppc.screen");
		}

	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return desc:短信套餐推荐
	 * 
	 */
	private String[][] qryAgain_shortMsgUserCommend(HttpServletRequest request,
			HttpSession session, String flag) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {
				e.printStackTrace();
			}

		}

		String op_time = qryStruct.gather_month;

		String user_online_lvl_id = qryStruct.dim4;

		// String user_online_lvl_name =
		// StringB.NulltoBlank(request.getParameter("user_online_lvl_name"));

		String calling_times_lvl_id = qryStruct.dim6;

		String called_times_lvl_id = qryStruct.dim8;

		String addval_price_plan_id = qryStruct.dim3;

		String arpulvl = qryStruct.dim10;

		String device_brand_id = qryStruct.dim15;
		String device_model_id = qryStruct.dim16;

		String sqlPrice = "select x.price_plan_id,x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id="
				+ flag + " and x.price_plan_id=" + addval_price_plan_id;

		try {
			String[][] resPrice = WebDBUtil.execQryArray(sqlPrice);
			if (resPrice != null && resPrice.length > 0) {
				qryStruct.price_plan_desc = resPrice[0][1];
			}
		} catch (AppException e1) {
			e1.printStackTrace();
		}

		String sql = "select t1.user_id,t1.cust_id,t1.cust_name,t1.acct_id,t1.acct_name,t1.acc_nbr,t. from new_bi_ui.FUI_ADV_USER_INFO_M t1 "
				+ "where op_time = "
				+ op_time
				+ " and USER_ARPU_LVL_ID = "
				+ arpulvl + " and USER_ONLINE_LVL_ID = " + user_online_lvl_id;
		// + " and CALLING_TIMES_LVL_ID = " + calling_times_lvl_id
		// + " and CALLed_TIMES_LVL_ID = " + called_times_lvl_id;

		if (device_brand_id.length() > 0) {
			sql += " and t1.device_brand_id='" + device_brand_id + "'";
		}

		if (device_model_id.length() > 0) {
			sql += " and t1.device_model_id='" + device_model_id + "'";
		}
		sql += " and not exists (select 1 from FUI_ADV_ADDVAL_2_PP_M "
				+ "where ADDVAL_PRICE_PLAN_ID = " + addval_price_plan_id
				+ " and user_id = t1.user_id)";

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return desc:短信套餐推荐
	 * 
	 */
	private String[][] shortMsgUserCommend(HttpServletRequest request,
			HttpSession session, String flag) {

		String op_time = StringB.NulltoBlank(request.getParameter("OP_TIME"));

		String user_online_lvl_id = StringB.NulltoBlank(request
				.getParameter("user_online_lvl_id"));
		String user_online_lvl_name = StringB.NulltoBlank(request
				.getParameter("user_online_lvl_name"));

		String calling_times_lvl_id = StringB.NulltoBlank(request
				.getParameter("calling_times_lvl_id"));
		String calling_times_lvl_name = StringB.NulltoBlank(request
				.getParameter("calling_times_lvl_name"));

		String called_times_lvl_id = StringB.NulltoBlank(request
				.getParameter("called_times_lvl_id"));
		String called_times_lvl_name = StringB.NulltoBlank(request
				.getParameter("called_times_lvl_name"));

		String addval_price_plan_id = StringB.NulltoBlank(request
				.getParameter("addval_price_plan_id"));

		String arpulvl = StringB.NulltoBlank(request
				.getParameter("user_arpu_lvl_id"));
		String arpulvl_name = StringB.NulltoBlank(request
				.getParameter("user_arpu_lvl_name"));

		String usage_ratio_lvl_id = StringB.NulltoBlank(request
				.getParameter("usage_ratio_lvl_id"));
		String usage_ratio_lvl_name = usage_ratio_lvl_id + "级";

		String device_brand_id = StringB.NulltoBlank(request
				.getParameter("device_brand_id"));
		String device_model_id = StringB.NulltoBlank(request
				.getParameter("device_model_id"));

		String sql = "select t1.user_id,t1.cust_id,t1.cust_name,t1.acct_id,t1.acct_name,t1.acc_nbr from new_bi_ui.FUI_ADV_USER_INFO_M t1 "
				+ "where op_time = "
				+ op_time
				+ " and USER_ARPU_LVL_ID = "
				+ arpulvl + " and USER_ONLINE_LVL_ID = " + user_online_lvl_id;
		// + " and CALLING_TIMES_LVL_ID = " + calling_times_lvl_id
		// + " and CALLed_TIMES_LVL_ID = " + called_times_lvl_id;

		if (device_brand_id.length() > 0) {
			sql += " and t1.device_brand_id='" + device_brand_id + "'";
		}

		if (device_model_id.length() > 0) {
			sql += " and t1.device_model_id='" + device_model_id + "'";
		}
		sql += " and not exists (select 1 from FUI_ADV_ADDVAL_2_PP_M "
				+ "where ADDVAL_PRICE_PLAN_ID = " + addval_price_plan_id
				+ " and user_id = t1.user_id)";

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.gather_month = op_time;
		qryStruct.dim2 = usage_ratio_lvl_id;
		qryStruct.dim3 = addval_price_plan_id;
		qryStruct.dim20 = usage_ratio_lvl_name;

		qryStruct.dim4 = user_online_lvl_id;
		qryStruct.dim5 = user_online_lvl_name;

		qryStruct.dim6 = calling_times_lvl_id;
		qryStruct.dim7 = calling_times_lvl_name;

		qryStruct.dim8 = called_times_lvl_id;
		qryStruct.dim9 = called_times_lvl_name;

		qryStruct.dim10 = arpulvl;
		qryStruct.dim11 = arpulvl_name;

		qryStruct.dim15 = device_brand_id;
		qryStruct.dim16 = device_model_id;

		String sqlPrice = "select x.price_plan_id,x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id="
				+ flag + " and x.price_plan_id=" + addval_price_plan_id;

		try {
			String[][] resPrice = WebDBUtil.execQryArray(sqlPrice);
			if (resPrice != null && resPrice.length > 0) {
				qryStruct.price_plan_desc = resPrice[0][1];
			}
		} catch (AppException e1) {
			e1.printStackTrace();
		}

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;
		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return desc:内容订购推荐
	 * 
	 */
	private String[][] shortConentCommend(HttpServletRequest request,
			HttpSession session) {

		String op_time = StringB.NulltoBlank(request.getParameter("OP_TIME"));

		String user_online_lvl_id = StringB.NulltoBlank(request
				.getParameter("user_online_lvl_id"));
		String user_online_lvl_name = StringB.NulltoBlank(request
				.getParameter("user_online_lvl_name"));

		String calling_times_lvl_id = StringB.NulltoBlank(request
				.getParameter("calling_times_lvl_id"));
		String calling_times_lvl_name = StringB.NulltoBlank(request
				.getParameter("calling_times_lvl_name"));

		String called_times_lvl_id = StringB.NulltoBlank(request
				.getParameter("called_times_lvl_id"));
		String called_times_lvl_name = StringB.NulltoBlank(request
				.getParameter("called_times_lvl_name"));

		String ADDVAL_PROD_ID = StringB.NulltoBlank(request
				.getParameter("ADDVAL_PROD_ID"));

		String arpulvl = StringB.NulltoBlank(request
				.getParameter("user_arpu_lvl_id"));
		String arpulvl_name = StringB.NulltoBlank(request
				.getParameter("user_arpu_lvl_name"));

		String device_brand_id = StringB.NulltoBlank(request
				.getParameter("device_brand_id"));
		String device_model_id = StringB.NulltoBlank(request
				.getParameter("device_model_id"));

		String sql = "select t1.user_id,t1.cust_id,t1.cust_name,t1.acct_id,t1.acct_name,t1.acc_nbr from new_bi_ui.FUI_ADV_USER_INFO_M t1 "
				+ "where op_time = "
				+ op_time
				+ " and USER_ARPU_LVL_ID = "
				+ arpulvl + " and USER_ONLINE_LVL_ID = " + user_online_lvl_id;

		/*
		 * if (calling_times_lvl_id.length()>0){ sql +=
		 * " and CALLING_TIMES_LVL_ID = " + calling_times_lvl_id; } if
		 * (called_times_lvl_id.length()>0){ sql +=
		 * " and CALLed_TIMES_LVL_ID = " + called_times_lvl_id; }
		 */

		if (device_brand_id.length() > 0) {
			sql += " and t1.device_brand_id='" + device_brand_id + "'";
		}

		if (device_model_id.length() > 0) {
			sql += " and t1.device_model_id='" + device_model_id + "'";
		}

		sql += " and not exists (select 1 from FUI_ADV_CONTENT_ORDER_M "
				+ "where addval_prod_id = " + ADDVAL_PROD_ID
				+ " and user_id = t1.user_id)";

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.gather_month = op_time;

		qryStruct.dim3 = ADDVAL_PROD_ID;

		qryStruct.dim4 = user_online_lvl_id;
		qryStruct.dim5 = user_online_lvl_name;

		qryStruct.dim6 = calling_times_lvl_id;
		qryStruct.dim7 = calling_times_lvl_name;

		qryStruct.dim8 = called_times_lvl_id;
		qryStruct.dim9 = called_times_lvl_name;

		qryStruct.dim10 = arpulvl;
		qryStruct.dim11 = arpulvl_name;

		String sqlPrice = "select lvl_name,remark from d_adv_param_lvl where param_id=107 and lvl_name="
				+ ADDVAL_PROD_ID;

		try {
			String[][] resPrice = WebDBUtil.execQryArray(sqlPrice);
			if (resPrice != null && resPrice.length > 0) {
				qryStruct.price_plan_desc = resPrice[0][1];
			}
		} catch (AppException e1) {
			e1.printStackTrace();
		}

		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;
		try {
			System.out.println(sql);
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @desc:内容订购
	 */
	private String[][] qryAgain_ContentUserCommend(HttpServletRequest request,
			HttpSession session) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {
				e.printStackTrace();
			}

		}

		String op_time = qryStruct.gather_month;

		String user_online_lvl_id = qryStruct.dim4;

		// String user_online_lvl_name =
		// StringB.NulltoBlank(request.getParameter("user_online_lvl_name"));

		String calling_times_lvl_id = qryStruct.dim6;

		String called_times_lvl_id = qryStruct.dim8;

		String addval_price_plan_id = qryStruct.dim3;

		String arpulvl = qryStruct.dim10;

		String device_brand_id = qryStruct.dim15;
		String device_model_id = qryStruct.dim16;

		String sqlPrice = "select lvl_name,remark from d_adv_param_lvl where param_id=107 and lvl_name="
				+ addval_price_plan_id;

		try {
			String[][] resPrice = WebDBUtil.execQryArray(sqlPrice);
			if (resPrice != null && resPrice.length > 0) {
				qryStruct.price_plan_desc = resPrice[0][1];
			}
		} catch (AppException e1) {
			e1.printStackTrace();
		}

		String sql = "select t1.user_id,t1.cust_id,t1.cust_name,t1.acct_id,t1.acct_name,t1.acc_nbr from new_bi_ui.FUI_ADV_USER_INFO_M t1 "
				+ "where op_time = "
				+ op_time
				+ " and USER_ARPU_LVL_ID = "
				+ arpulvl + " and USER_ONLINE_LVL_ID = " + user_online_lvl_id;

		/*
		 * if (calling_times_lvl_id.length()>0){ sql +=
		 * " and CALLING_TIMES_LVL_ID = " + calling_times_lvl_id; } if
		 * (called_times_lvl_id.length()>0){ sql +=
		 * " and CALLed_TIMES_LVL_ID = " + called_times_lvl_id; }
		 */

		if (device_brand_id.length() > 0) {
			sql += " and t1.device_brand_id='" + device_brand_id + "'";
		}

		if (device_model_id.length() > 0) {
			sql += " and t1.device_model_id='" + device_model_id + "'";
		}

		sql += " and not exists (select 1 from FUI_ADV_CONTENT_ORDER_M "
				+ "where addval_prod_id = " + addval_price_plan_id
				+ " and user_id = t1.user_id)";

		System.out.println(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @desc:离网清单提取
	 */
	private String[][] qryOffFcUser(HttpServletRequest request,
			HttpSession session) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {
				e.printStackTrace();
			}

		}

		String op_time = StringB.NulltoBlank(request.getParameter("OP_TIME"));
		String dim1 = qryStruct.dim1;
		String product_lv1_id = qryStruct.product_lv1_id;
		String product_lv2_id = qryStruct.product_lv2_id;

		if (op_time.length() > 0) {
			dim1 = StringB.NulltoBlank(request.getParameter("dim1"));
			product_lv1_id = StringB.NulltoBlank(request
					.getParameter("cch_prod_cat_id"));
			product_lv2_id = StringB.NulltoBlank(request
					.getParameter("cch_prod_id"));
			qryStruct.gather_month = op_time;
			qryStruct.dim1 = dim1;
			qryStruct.product_lv1_id = product_lv1_id;
			qryStruct.product_lv2_id = product_lv2_id;
		} else {
			op_time = qryStruct.gather_month;
		}

		String sql = "select  user_id,acc_nbr,cust_name,user_online_dura,main_prod_name,offer_name,main_pp_name,to_char(open_date,'yyyy-mm-dd') from FUI_CCH_OFF_FORECAST_USER_M "
				+ " where op_time=" + op_time;

		if (dim1.length() > 0) {
			sql += " and cch_remove_mode=" + dim1;
		}

		if (product_lv1_id.length() > 0) {
			sql += " and cch_prod_cat_id=" + product_lv1_id;
		}

		if (product_lv2_id.length() > 0) {
			sql += " and cch_prod_id=" + product_lv2_id;
		}

		System.out.println(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @desc:清单提取
	 */
	private String[][] qryCallDesc(String sql, boolean flag,
			HttpServletRequest request, HttpSession session) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		qryStruct.optype = StringB.NulltoBlank(request.getParameter("opType"));
		String op_time = StringB.NulltoBlank(request.getParameter("op_time"));
		if (op_time.length() == 0) {
			op_time = StringB.NulltoBlank(request.getParameter("OP_TIME"));
		}
		String dim1 = qryStruct.dim1;
		String product_lv1_id = qryStruct.product_lv1_id;
		String dim2 = qryStruct.dim2;
		String dim3 = qryStruct.dim3;
		String dim4 = qryStruct.dim4;
		if (op_time.length() > 0) {
			dim1 = StringB.NulltoBlank(request
					.getParameter("user_online_lvl_id")); // 在网时长
			dim2 = StringB
					.NulltoBlank(request.getParameter("user_arpu_lvl_id")); // arpu分档
			product_lv1_id = StringB.NulltoBlank(request
					.getParameter("main_prod_id")); // 主产品
			dim3 = StringB.NulltoBlank(request.getParameter("offer_id")); // 销售品
			dim4 = StringB.NulltoBlank(request.getParameter("main_pp_id")); // 主资费
			if (flag)
				qryStruct.gather_month = op_time;
			else
				qryStruct.gather_day = op_time;
			qryStruct.dim1 = dim1;
			qryStruct.product_lv1_id = product_lv1_id;
			qryStruct.dim2 = dim2;
			qryStruct.dim3 = dim3;
			qryStruct.dim4 = dim4;
		} else {
			if (flag)
				op_time = qryStruct.gather_month;
			else
				op_time = qryStruct.gather_day;

		}

		if (op_time.length() > 0) {
			sql += " and op_time=" + op_time;
		}
		if (dim1.length() > 0) {
			sql += " and user_online_lvl_id=" + dim1;
		}

		if (dim2.length() > 0) {
			sql += " and user_arpu_lvl_id=" + dim2;
		}

		if (product_lv1_id.length() > 0) {
			sql += " and main_prod_id=" + product_lv1_id;
		}
		if (dim3.length() > 0) {
			sql += " and offer_id=" + dim3;
		}
		if (dim4.length() > 0) {
			sql += " and main_pp_id=" + dim4;
		}
		System.out.println(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @desc:清单提取
	 */
	private String[][] qryExpire(String sql, HttpServletRequest request,
			HttpSession session) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		qryStruct.optype = StringB.NulltoBlank(request.getParameter("opType"));
		qryStruct.rpt_type = StringB.NulltoBlank(request
				.getParameter("data_type"));
		String op_time = StringB.NulltoBlank(request.getParameter("OP_TIME"));

		String dim1 = qryStruct.dim1;
		String dim2 = qryStruct.dim2;
		if (op_time.length() > 0) {
			dim1 = StringB.NulltoBlank(request.getParameter("main_pp_id")); // 主资费
			dim2 = StringB
					.NulltoBlank(request.getParameter("user_arpu_lvl_id")); // arpu分档
			qryStruct.gather_month = op_time;
			qryStruct.dim1 = dim1;
			qryStruct.dim2 = dim2;
		} else {
			op_time = qryStruct.gather_month;
		}

		if (op_time.length() > 0) {
			sql += " and op_time=" + op_time;
		}
		if (dim1.length() > 0) {
			sql += " and main_pp_id=" + dim1;
		}

		if (dim2.length() > 0) {
			sql += " and user_arpu_lvl_id=" + dim2;
		}

		System.out.println(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @desc:清单提取
	 */
	private String[][] qryOffppc(String sql, HttpServletRequest request,
			HttpSession session) {

		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		qryStruct.optype = StringB.NulltoBlank(request.getParameter("opType"));
		String op_time = StringB.NulltoBlank(request
				.getParameter("cch_off_date"));

		if (op_time.length() > 0) {
			qryStruct.gather_day = op_time;
		} else {
			op_time = qryStruct.gather_day;
		}

		if (op_time.length() > 0) {
			sql += " and cch_off_date=" + op_time;
		}

		System.out.println(sql);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		String[][] result = null;

		try {
			result = WebDBUtil.execQryArray(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return result;
	}
}
