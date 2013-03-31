/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package waf.controller.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.flow.ScreenFlowManager;
import waf.controller.web.util.WebKeys;

import com.ailk.bi.common.app.AppWebUtil;

public class MainServlet extends HttpServlet {
	/**
     *
     */
	private static final long serialVersionUID = 5304229261370889969L;

	private final static String ERR_HTML_CLASS = "waf.controller.web.action.HTMLActionException";

	private ServletContext context;

	private ServletConfig config;

	public void init(ServletConfig config) throws ServletException {
		this.context = config.getServletContext();
		this.config = config;
		waf.controller.web.LoadWebConfigXML.loadXML(context);
		getScreenFlowManager();
		// 获取请求处理器，将其instance存放在context中
		getRequestProcessor();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doProcess(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doProcess(request, response);
	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String nextScreen = null;
		// 清除session中的指定的某些属性 2004.03.18 add by wiseking 目的IBAS项目指定信息的清除
		if (request.getParameter(waf.util.CleanSessionTool.WEB_PARA_CS) != null) {
			waf.util.CleanSessionTool.clear(request.getSession());
		}
		// 设置menuCode到Session中 2004.03.25
		String menuCode = request
				.getParameter(waf.util.CleanSessionTool.WEB_PARA_MENUCODE);
		if (menuCode != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute(waf.util.CleanSessionTool.ATTR_MENUCODE,
						menuCode);
			}
		}
		try {
			// 判断重复提交
			if (AppWebUtil.isRptCommit(request)) {
				AppWebUtil.clearRptToken(request.getSession());
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "此页面不得重复提交!");
			}
			if (getRequestProcessor().processRequest(request, response)) {
				getScreenFlowManager().forwardToNextScreen(request, response);
			}
		} catch (Throwable ex) {
			String className = ex.getClass().getName();
			nextScreen = getScreenFlowManager().getExceptionScreen(ex);
			if (nextScreen == null) {
				// send to general error screen
				try {
					nextScreen = getScreenFlowManager().getExceptionScreen(
							ERR_HTML_CLASS);
					StackTraceElement stE[] = ex.getStackTrace();
					String err = "";
					for (int i = 0; i < stE.length; i++) {
						err += stE[i].toString() + "\n";
					}
					System.err.println(ex.getMessage());
					System.err.println(err);
					if (nextScreen == null) {
						throw new ServletException(
								"WAF MainServlet: error screen undefine exception! ");
					}
				} catch (Exception eee) {
					throw new ServletException(
							"WAF MainServlet: unknown exception: "
									+ className
									+ " maybe session is invalid，pls relogin system!");
				}
			}
			try {
				context.getRequestDispatcher(nextScreen).forward(request,
						response);
			} catch (Exception e) { // 这里还有疑问，对forward要进行深入了解
				response.sendRedirect(nextScreen);
			}
		}
	}

	private RequestProcessor getRequestProcessor() {
		RequestProcessor rp = (RequestProcessor) context
				.getAttribute(WebKeys.REQUEST_PROCESSOR);
		if (rp == null) {
			rp = new RequestProcessor();
			rp.init(config);
			context.setAttribute(WebKeys.REQUEST_PROCESSOR, rp);
		}
		return rp;
	}

	private ScreenFlowManager getScreenFlowManager() {
		ScreenFlowManager screenManager = (ScreenFlowManager) context
				.getAttribute(WebKeys.SCREEN_FLOW_MANAGER);
		if (screenManager == null) {
			screenManager = new ScreenFlowManager();
			screenManager.init(context);
			context.setAttribute(WebKeys.SCREEN_FLOW_MANAGER, screenManager);
		}
		return screenManager;
	}
}