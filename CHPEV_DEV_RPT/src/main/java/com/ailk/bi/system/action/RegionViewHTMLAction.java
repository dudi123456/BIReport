package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoRegionTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoRegion;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class RegionViewHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		//
		String oper_type = request.getParameter("oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "0";
		}

		//
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}
		//
		InfoRegionTable infoRegion = new InfoRegionTable();
		if ("1".equals(submitType)) {
			// 取页面用户信息
			try {
				AppWebUtil.getHtmlObject(request, "region", infoRegion);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "区域受理信息有误！");
			}
			// 调试

			if ("1".equals(oper_type)) {// 新增
				LSInfoRegion.addNewRegion(request, infoRegion);
			} else {
				LSInfoRegion.updateRegionInfo(request, infoRegion);
			}

		}
		if ("2".equals(submitType)) {// 删除
			// 取页面部门信息
			try {
				AppWebUtil.getHtmlObject(request, "region", infoRegion);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "区域受理信息有误！");
			}
			// 调试
			LSInfoRegion.deleteRegionInfo(request, infoRegion);

		}
	}

}