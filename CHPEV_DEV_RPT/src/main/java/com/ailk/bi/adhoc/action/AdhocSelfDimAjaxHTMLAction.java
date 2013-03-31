package com.ailk.bi.adhoc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.util.AdhocHelper;
import com.ailk.bi.base.util.CommTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocSelfDimAjaxHTMLAction extends HTMLActionSupport {
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
		logcommon.debug("oper_type=====================" + oper_type);
		//
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		StringBuffer strB = new StringBuffer("");
		//
		String paramValue = CommTool.getParameterGB(request, "value");
		logcommon.debug("paramValue=====================" + paramValue);
		String arr[] = paramValue.split("[:]");
		String adhoc_id = arr[0];// CommTool.getParameterGB(request,"adhoc_id");
		String dim_relation_field = arr[1];// CommTool.getParameterGB(request,"dim_relation_field");
		String oper_no = arr[2];// CommTool.getParameterGB(request,"oper_no");

		String dim_id = arr[3];// CommTool.getParameterGB(request,"dim_id");
		String dim_name = arr[4];// CommTool.getParameterGB(request,"dim_name");
		String low_value = arr[5];// CommTool.getParameterGB(request,"low_value");
		if (low_value == null) {
			low_value = "";
		}
		String hign_value = arr[6];// CommTool.getParameterGB(request,"hign_value");
		if (hign_value == null) {
			hign_value = "";
		}

		if ("add".equals(oper_type)) {
			//
			String whereStr = "and adhoc_id='" + adhoc_id
					+ "' AND dim_relation_field='" + dim_relation_field
					+ "' AND oper_no='" + oper_no + "'";
			// 增加
			String sequence = CommTool.getMaxField("ui_adhoc_rule_user_dim",
					"sequence", whereStr);
			if (sequence == null || "".equals(sequence)) {
				sequence = "0";
			}
			sequence = String.valueOf(Integer.parseInt(sequence) + 1);
			// 插入
			int i = facade.saveAdhocUserDimInfo(adhoc_id, oper_no,
					dim_relation_field, low_value, hign_value, dim_id,
					dim_name, sequence);
			if (i >= 0) {
				UiAdhocRuleUserDimTable[] userDim = facade.getAdhocUserDimList(
						adhoc_id, oper_no, dim_relation_field);
				strB.append(AdhocHelper.getSelfDimValue(userDim));
			}
		} else {// delete
			int i = facade
					.deleteAdhocUserDimInfo(adhoc_id, oper_no,
							dim_relation_field, low_value, hign_value, dim_id,
							dim_name);
			if (i >= 0) {
				UiAdhocRuleUserDimTable[] userDim = facade.getAdhocUserDimList(
						adhoc_id, oper_no, dim_relation_field);
				strB.append(AdhocHelper.getSelfDimValue(userDim));
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
