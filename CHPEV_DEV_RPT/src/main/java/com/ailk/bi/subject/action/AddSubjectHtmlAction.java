package com.ailk.bi.subject.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CodeParamUtil;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AddSubjectHtmlAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();

		// 获取页面screen标示
		String optype = request.getParameter("optype");


		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}

		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
			}
		}
		qryStruct.optype = optype;

		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}

		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		if (qryStruct.date_s == null || "".equals(qryStruct.date_s)) {
			qryStruct.date_s = qryStruct.gather_month.substring(0, 4) + "01";
		}
		if (qryStruct.date_e == null || "".equals(qryStruct.date_e)) {
			qryStruct.date_e = DateUtil.getDiffMonth(0, DateUtil.getNowDate());
		}

		if ("bhdfx".equals(optype)) {
			if (qryStruct.price_plan_id == null
					|| "".equals(qryStruct.price_plan_id)) {
				qryStruct.price_plan_id = "20314";
			}

			qryStruct.price_plan_desc = CodeParamUtil.codeListParamFetcher(
					request, "add_price_plan", qryStruct.price_plan_id);

			if (qryStruct.add_dim_id == null || "".equals(qryStruct.add_dim_id)) {
				qryStruct.add_dim_id = "add_subject_bhd_01";
			}

			try {
				qryStruct.add_dim_desc = getAddDimDesc("106");
				qryStruct.add_dim_desc += getAddDimDesc(qryStruct.add_dim_id);
			} catch (AppException e) {
				e.printStackTrace();
			}

		} else {

		}
		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype + ".screen");

	}

	private String getAddDimDesc(String add_dim_id) throws AppException {
		String str = "";
		String[][] value = null;
		if ("add_subject_bhd_01".equals(add_dim_id)) {
			str = "ARPU分档数值分布:";
			// 103-arpu
			String sql = "select lvl_id ,lvl_name from d_adv_param_lvl where param_id = "
					+ 103 + "order by lvl_id ";
			value = WebDBUtil.execQryArray(sql);

		} else if ("add_subject_bhd_02".equals(add_dim_id)) {
			str = "在网时长分档数值分布:";
			// 101 -在网时长
			String sql = "select lvl_id ,lvl_name from d_adv_param_lvl where param_id = "
					+ 101 + "order by lvl_id ";
			value = WebDBUtil.execQryArray(sql);

		} else if ("add_subject_bhd_03".equals(add_dim_id)) {
			str = "主叫次数分档数值分布:";
			// 104-主叫次数
			String sql = "select lvl_id ,lvl_name from d_adv_param_lvl where param_id = "
					+ 104 + "order by lvl_id ";
			value = WebDBUtil.execQryArray(sql);

		} else if ("add_subject_bhd_04".equals(add_dim_id)) {
			str = "被叫次数分档数值分布:";
			// 105 -被叫次数
			String sql = "select lvl_id ,lvl_name from d_adv_param_lvl where param_id = "
					+ 105 + "order by lvl_id ";
			value = WebDBUtil.execQryArray(sql);
		} else {
			str = "饱和度数值分布:";
			// 105 -被叫次数
			String sql = "select lvl_id ,lvl_name from d_adv_param_lvl where param_id = "
					+ 106 + "order by lvl_id ";
			value = WebDBUtil.execQryArray(sql);
		}
		if (value != null && value.length > 0) {
			for (int i = 0; i < value.length; i++) {
				if (i % 3 == 0) {
					str += "<tr width=\"100%\">";
				}

				str += "<td width=\"33%\">" + value[i][0] + ":" + value[i][1]
						+ "</td>";

				if (i % 3 == 2 || (i == value.length - 1)) {
					str += "</tr>";
				}

			}

		}

		return "<table width=\"100%\">" + str + "</table>";

	}
}
