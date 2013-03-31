package com.ailk.bi.metamanage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.metamanage.model.Measure;
import com.ailk.bi.metamanage.service.IMsuDefService;
import com.ailk.bi.metamanage.service.impl.MsuDefServiceImpl;

@SuppressWarnings({ "unused", "serial" })
public class MsuDetHTMLAction extends HTMLActionSupport {
	private IMsuDefService service = new MsuDefServiceImpl();

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String method = request.getParameter("method");

		Measure info = service.getMsu(request.getParameter("msu_id"));
		request.setAttribute("list", info);
		this.setNextScreen(request, "msuDetList.screen");
	}

}
