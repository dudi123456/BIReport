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

public class OppSubjectParamLvlAjaxHTMLAction extends HTMLActionSupport {

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
		String param_type = param[1];
		String lvl_id = param[2];
		String lvl_name = param[3];
		String start_val = param[4];
		String end_val = param[5];
		String h_lvl_id = param[6];

		if ("add".equals(oper_type)) {// add

			String[] value = { param_id, param_type, lvl_id, lvl_name,
					start_val, end_val };
			int i = facade.addNewParamLvl(value);
			if (i >= 0) {
				//
				strB.append(facade.genParamLvlTableHtml(param_id));
			}

		} else if ("update".equals(oper_type)) {// delete

			String updateStr = "";

			updateStr += " param_id ='" + param_id + "' , ";
			updateStr += " param_type ='" + param_type + "' , ";
			updateStr += " lvl_id =" + lvl_id + " , ";
			updateStr += " lvl_name ='" + lvl_name + "', ";
			updateStr += " start_val =" + start_val + " , ";
			updateStr += " end_val =" + end_val;

			String whereStr = " AND param_id ='" + param_id + "'";
			whereStr += " AND param_type ='" + param_type + "'";
			whereStr += " AND lvl_id =" + h_lvl_id;

			String[] value = { updateStr, whereStr };
			int i = facade.updateParamLvl(value);
			if (i >= 0) {
				//
				strB.append(facade.genParamLvlTableHtml(param_id));
			}
		} else if ("delete".equals(oper_type)) {
			String whereStr = " AND param_id ='" + param_id + "'";
			whereStr += " AND param_type ='" + param_type + "'";
			whereStr += " AND lvl_id =" + h_lvl_id;

			int i = facade.deleteParamLvl(whereStr);
			if (i >= 0) {
				//
				strB.append(facade.genParamLvlTableHtml(param_id));
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
