package com.ailk.bi.subject.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.subject.dao.impl.OppDao;
import com.ailk.bi.subject.facade.OppFacade;
import com.ailk.bi.subject.service.impl.OppService;
import com.ailk.bi.adhoc.util.AdhocBuildUserDetail;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "rawtypes" })
public class SubjectAjaxHTMLAction extends HTMLActionSupport {

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
		HttpSession session = request.getSession();
		String exportName = CommTool.getParameterGB(request, "exportName");
		String typeId = CommTool.getParameterGB(request, "type_id");
		String resId = CommTool.getParameterGB(request, "res_id");
		//
		String sqlCount = "";
		String sqlQryDetail = (String) session
				.getAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_SQL);
		String expDetailName = exportName;
		List listField = (List) session
				.getAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_TABLE_TITLE);
		AdhocBuildUserDetail expDetail = new AdhocBuildUserDetail(request,
				sqlCount, sqlQryDetail, listField, expDetailName, typeId, resId);
		int i = expDetail.doSaveExpDetailTask();
		//

		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			PrintWriter pw = response.getWriter();
			String strB = "";
			if (i >= 0) {
				strB = "导出任务提交成功！";
			} else {
				strB = "导出任务提交失败！";
			}
			pw.write(strB.toString());

			setNvlNextScreen(request);
		} catch (IOException ex) {

		}

	}

}
