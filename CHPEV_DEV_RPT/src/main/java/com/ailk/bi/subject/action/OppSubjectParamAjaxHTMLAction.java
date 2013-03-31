package com.ailk.bi.subject.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ailk.bi.subject.dao.impl.OppDao;
import com.ailk.bi.subject.facade.OppFacade;
import com.ailk.bi.subject.service.impl.OppService;
import com.ailk.bi.base.util.CommTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class OppSubjectParamAjaxHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		//
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
			ex1.printStackTrace();
		}
		//
		String oper_type = CommTool.getParameterGB(request, "oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "add";
		}

		//
		OppFacade facade = new OppFacade();
		OppService service = new OppService();
		service.setDao(new OppDao());
		facade.setService(service);

		StringBuffer strB = new StringBuffer("");
		//
		String paramValue = CommTool.getParameterGB(request, "value");
		String[] param = paramValue.split("[:]");
		String param_id = param[0];
		String param_name = param[1];
		String param_type = param[2];
		String param_rule = param[3];
		String param_weight = param[4];
		String param_status = param[5];
		String param_desc = param[6];
		String h_param_id = param[7];

		if ("add".equals(oper_type)) {// add

			String[] value = { param_id, param_name, param_type, param_rule,
					param_weight, param_status, param_desc };
			int i = facade.addNewParam(value);
			if (i >= 0) {
				//
				strB.append(facade.genParamTableHtml());
			}

		} else if ("update".equals(oper_type)) {// delete

			String updateStr = "";

			updateStr += " param_id ='" + param_id + "' , ";
			updateStr += " param_name ='" + param_name + "' , ";
			updateStr += " param_type ='" + param_type + "' , ";
			updateStr += " param_rule ='" + param_rule + "' , ";
			updateStr += " param_weight =" + param_weight + ", ";
			updateStr += " param_status ='" + param_status + "' , ";
			updateStr += " param_desc ='" + param_desc + "'";

			String whereStr = " AND param_id ='" + h_param_id + "'";

			String[] value = { updateStr, whereStr };
			int i = facade.updateParam(value);
			if (i >= 0) {
				//
				strB.append(facade.genParamTableHtml());
			}
		} else if ("delete".equals(oper_type)) {
			String whereStr = " AND param_id ='" + h_param_id + "'";

			int i = facade.deleteParam(whereStr);
			if (i >= 0) {
				//
				strB.append(facade.genParamTableHtml());
			}
		}

		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			PrintWriter pw = response.getWriter();
			pw.write(strB.toString());

			setNvlNextScreen(request);
		} catch (IOException ex) {

		}

	}

}
