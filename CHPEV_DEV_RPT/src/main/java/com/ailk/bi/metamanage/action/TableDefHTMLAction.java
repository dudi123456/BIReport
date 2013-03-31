package com.ailk.bi.metamanage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.metamanage.model.TableDef;
import com.ailk.bi.metamanage.service.ITableDefService;
import com.ailk.bi.metamanage.service.impl.TableDefServiceImpl;

@SuppressWarnings({ "serial" })
public class TableDefHTMLAction extends HTMLActionSupport {
	private ITableDefService service = new TableDefServiceImpl();

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String method = request.getParameter("method");
		if ("add".equalsIgnoreCase(method)) {
			boolean flag = service.isExistTableId(request
					.getParameter("table_id"));
			if (flag)
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "有重复的数据表标识，请重新输入！");
			service.add(request);
			request.setAttribute("msg", "添加成功！");
		} else if ("save".equalsIgnoreCase(method)) {
			service.save(request);
			request.setAttribute("msg", "修改成功！");
		} else if ("delete".equalsIgnoreCase(method)) {
			service.del(request.getParameter("table_id"));
			request.setAttribute("msg", "删除成功！");
		}
		TableDef info = service.getTableDef(request.getParameter("table_id"));
		request.setAttribute("domainSql",
				"select domain_id,domain_name from UI_META_INFO_DOMAIN where layer_id='"
						+ info.getLayer_id() + "' order by sequence");
		request.setAttribute("jobSql",
				"select job_id,job_name from UI_META_INFO_ETL_JOB where flow_id='"
						+ info.getFlow_id() + "' order by job_name");
		request.setAttribute("list", info);
		this.setNextScreen(request, "tableList.screen");
	}

}
