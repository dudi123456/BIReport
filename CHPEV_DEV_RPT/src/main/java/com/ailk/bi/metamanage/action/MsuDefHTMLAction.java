package com.ailk.bi.metamanage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.metamanage.model.Measure;
import com.ailk.bi.metamanage.service.IMsuDefService;
import com.ailk.bi.metamanage.service.impl.MsuDefServiceImpl;

@SuppressWarnings({ "serial" })
public class MsuDefHTMLAction extends HTMLActionSupport {
	private IMsuDefService service = new MsuDefServiceImpl();

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String method = request.getParameter("method");
		if ("add".equalsIgnoreCase(method)) {
			boolean flag = service.isExistMsuId(request.getParameter("msu_id"));
			if (flag)
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "有重复的指标标识，请重新输入！");
			service.add(request);
			request.setAttribute("msg", "添加成功！");
		} else if ("save".equalsIgnoreCase(method)) {
			service.save(request);
			request.setAttribute("msg", "修改成功！");
		} else if ("delete".equalsIgnoreCase(method)) {
			service.del(request.getParameter("msu_id"));
			request.setAttribute("msg", "删除成功！");
		}
		Measure info = service.getMsu(request.getParameter("msu_id"));
		request.setAttribute("list", info);
		this.setNextScreen(request, "msuList.screen");
	}

}
