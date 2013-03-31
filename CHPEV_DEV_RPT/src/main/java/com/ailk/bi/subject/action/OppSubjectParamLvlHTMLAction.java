package com.ailk.bi.subject.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.subject.dao.impl.OppDao;
import com.ailk.bi.subject.facade.OppFacade;
import com.ailk.bi.subject.service.impl.OppService;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "rawtypes" })
public class OppSubjectParamLvlHTMLAction extends HTMLActionSupport {

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

		//
		String oper_type = CommTool.getParameterGB(request, "oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "qry";
		}

		String param_id = CommTool.getParameterGB(request, "param_id");
		//
		OppFacade facade = new OppFacade();
		OppService service = new OppService();
		service.setDao(new OppDao());
		facade.setService(service);

		if ("qry".equals(oper_type)) {
			List list = facade.getOppParam(param_id);
			request.setAttribute(WebKeys.ATTR_OPP_SUBJECT_PARAM_LVL_lIST, list);
			this.setNextScreen(request, "OppSubjectParamLvl.screen");

		} else if ("add".equals(oper_type)) {

		} else if ("delete".equals(oper_type)) {

		} else if ("update".equals(oper_type)) {

		}

	}

}
