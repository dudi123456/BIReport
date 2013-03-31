package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.subject.admin.dao.impl.AiModResultDaoImpl;

public class AIModResultDetailHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5562078505726965797L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		//String head_detail = "";
		// 判断用户是否有效登陆
//		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
//			return;
		HttpSession session = request.getSession();
		String param = StringB.NulltoBlank(request.getParameter("param"));
		String optype = StringB.NulltoBlank(request.getParameter("optype"));
		String import_type = "";
		String result_string = "";
		String deal_month = StringB.NulltoBlank(request.getParameter("deal_month"));
		//String head1 = "";
		String head2 = "";
		if (param != null && !param.equals("")) {
			import_type = param.split(",")[3];
		}
		if (optype != null && optype.equals("select")) {
			if (deal_month.isEmpty() || deal_month.equals("")) {
				deal_month = param.split(",")[0] + "01";
			}
			if (import_type.equals(AppConst.USER_TEMP) || import_type.equals(AppConst.USER_BI_TEMP)) {
				head2 = AppConst.USER_CHANL_NUMBER;
			} else {
				head2 = AppConst.PRODUCT_CHANL_NUMBER;
			}
			try {
			 if (param != null && !param.equals("")) {
				result_string = AiModResultDaoImpl.getResult3(deal_month,param.split(",")[1], param.split(",")[2], param.split(",")[3]);
			 }
			} catch (AppException e) {
				e.printStackTrace();
			}
			session.setAttribute("head2", head2);
			session.setAttribute("param", param);
			session.setAttribute("deal_month", deal_month);
			session.setAttribute("result_string", result_string);
			setNextScreen(request, "AiModResultDetail.screen");
			 return;  
		} else {
			try {
				AiModResultDaoImpl.excelOut(import_type,param.split(",")[1],param.split(",")[2], response);
			} catch (AppException e) {
				e.printStackTrace();
			}
		}
	}
}
