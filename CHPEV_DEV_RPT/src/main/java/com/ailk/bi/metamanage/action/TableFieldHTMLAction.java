package com.ailk.bi.metamanage.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.metamanage.service.ITableDefService;
import com.ailk.bi.metamanage.service.impl.TableDefServiceImpl;

@SuppressWarnings({ "rawtypes", "serial" })
public class TableFieldHTMLAction extends HTMLActionSupport {
	private ITableDefService service = new TableDefServiceImpl();

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String method = request.getParameter("method");
		if ("add".equalsIgnoreCase(method)) {
			service.addTableField(request);
			request.setAttribute("msg", "添加成功！");
		} else if ("save".equalsIgnoreCase(method)) {
			service.saveTableField(request);
			request.setAttribute("msg", "修改成功！");
		} else if ("delete".equalsIgnoreCase(method)) {
			service.delTableField(request.getParameter("table_id"),
					request.getParameter("field_id"));
			request.setAttribute("msg", "删除成功！");
		}

		List info = service.getTableField(request.getParameter("table_id"));
		request.setAttribute("list", info);
		this.setNextScreen(request, "tableFieldList.screen");
	}

}
