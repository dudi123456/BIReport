package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.system.entity.SystemInfo;
import com.ailk.bi.system.service.ISystemService;
import com.ailk.bi.system.service.impl.SystemServiceImpl;

@SuppressWarnings("serial")
public class SystemHTMLAction extends HTMLActionSupport
{
	private ISystemService service = new SystemServiceImpl();

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException
	{
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		//取得jsp页面hiddern的，并且id为method的值。不是form表单的method方法。
		String method = request.getParameter("method");

		if ("add".equalsIgnoreCase(method))
		{
			boolean flag = service.isExistSystemId(request
					.getParameter("system_id"));
			if (flag)
			{
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "有重复的系统标识，请重新输入！");
			}

			service.add(request);
			//servletRequest中的setAttribute(String name,Object o)
			request.setAttribute("msg", "添加成功！");
		} else if ("save".equalsIgnoreCase(method)) {
			service.save(request);
			request.setAttribute("msg", "修改成功！");
		} else if ("delete".equalsIgnoreCase(method)) {
			service.del(request.getParameter("system_id"));
			request.setAttribute("msg", "删除成功！");
		}
		SystemInfo info = service.getSystem(request.getParameter("system_id"));
		request.setAttribute("list", info);
		//结果返回页面
		this.setNextScreen(request, "systemList.screen");
	}
}
