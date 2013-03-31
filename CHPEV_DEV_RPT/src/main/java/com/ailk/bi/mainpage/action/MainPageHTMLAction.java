package com.ailk.bi.mainpage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class MainPageHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 变量申明区
		HttpSession session = request.getSession();
		// 用于控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>获取用户控制信息失败，不能访问首页！</b></font>");
		}
		// 用户的控制等级
		String ctl_lvl = ctlStruct.ctl_lvl;
		// 用户的控制区域
		String area_id = "";
		if ("1".equals(ctl_lvl)) {
			// 重庆用户
			area_id = "'0'";
		} else if ("2".equals(ctl_lvl)) {
			// 地市用户
			if ("0".equals(ctlStruct.ctl_flag)) {
				area_id = ctlStruct.ctl_city_str;
			} else {
				area_id = ctlStruct.ctl_city_str;
			}
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>Sorry，你的用户没有访问首页的权限！</b></font>");
		}
		System.out.println("area_id=" + area_id);
		System.out.println("ctl_lvl=" + ctl_lvl);

		session.setAttribute(WebKeys.ATTR_MAINPAGE_AREA_ID, area_id);
		session.setAttribute(WebKeys.ATTR_MAINPAGE_CTRL_LVL, ctl_lvl);
		setNextScreen(request, "MainPage.screen");
	}

}
