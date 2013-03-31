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

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLAction;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.InvalidSessionException;
import waf.controller.web.util.WebKeys;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.event.JBTable;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * This is the web tier controller for the sample application.
 * <p/>
 * This class is responsible for processing all requests received from the
 * Main.jsp and generating necessary events to modify data which are sent to the
 * WebController.
 */

@SuppressWarnings({ "rawtypes" })
public class RequestProcessor implements java.io.Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 6927362868116985432L;

	private HashMap urlMappings;

	private HashMap eventMappings;

	private ServletConfig config;

	private ServletContext context;

	public RequestProcessor() {

	}

	public void init(ServletConfig config) {
		context = config.getServletContext();
		this.config = config;
		urlMappings = (HashMap) context.getAttribute(WebKeys.URL_MAPPINGS);
		eventMappings = (HashMap) context.getAttribute(WebKeys.EVENT_MAPPINGS);
	}

	/**
	 * The UrlMapping object contains information that will match a url to a
	 * mapping object that contains information about the current screen, the
	 * HTMLAction that is needed to process a request, and the HTMLAction that
	 * is needed to insure that the propper screen is displayed.
	 */
	private URLMapping getURLMapping(String urlPattern) {
		if ((urlMappings != null) && urlMappings.containsKey(urlPattern)) {
			return (URLMapping) urlMappings.get(urlPattern);
		} else {
			System.err
					.println("FATAL ERROR: mappings.xml define url mapping error or can not define the url, Check it now!");
			return null;
		}
	}

	/**
	 * The EventMapping object contains information that will match a event
	 * class name to an EJBActionClass.
	 */
	private EventMapping getEventMapping(JBTable eventClass) {
		// get the fully qualified name of the event class
		String eventClassName = eventClass.getClass().getName();
		if ((eventMappings != null)
				&& eventMappings.containsKey(eventClassName)) {
			return (EventMapping) eventMappings.get(eventClassName);
		} else {
			return null;
		}
	}

	/**
	 * This method is the core of the RequestProcessor. It receives all requests
	 * and generates the necessary events.
	 */
	public boolean processRequest(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException,
			InvalidSessionException, AppException, ServletException {
		JBTable ev = null;
		String fullURL = request.getRequestURI();
		// get the screen name
		String selectedURL = null;
		int lastPathSeparator = fullURL.lastIndexOf("/") + 1;
		if (lastPathSeparator != -1) {
			selectedURL = fullURL
					.substring(lastPathSeparator, fullURL.length());
		}
		URLMapping urlMapping = getURLMapping(selectedURL);
		HTMLAction action = getAction(urlMapping);
		boolean ckLogin = (null == urlMapping ? true : urlMapping
				.isCheckLogin());
		if (action != null) {
			action.setServletConfig(config);
			action.setServletContext(context);
			if (urlMapping.isUseStrutMode()) {// King Changed it at
												// 20031209为了适应strut模式
				String url = GetSystemConfig.getBIBMConfig().getLoginURL();
				if (ckLogin) {
					isLoginUser(url, selectedURL, request.getSession());
				}
				action.doTrans(request, response);
				if (selectedURL.equalsIgnoreCase(url)) {
					HttpSession ss = request.getSession();
					if (ss != null) {
						ss.setAttribute(WebKeys.SYS_LOGINFLAG, "true");
					}
				}
			} else {
				action.doStart(request);
				ev = action.perform(request);
				JBTable eventResponse = null;
				if (ev != null) {
					// set the ejb action class name on the event
					EventMapping eventMapping = getEventMapping(ev);
					if (eventMapping != null) {
						ev.setEJBActionClassName(eventMapping
								.getEJBActionClassName());
					}
					/* 对DefaultComponetManager进行赋值处理 */
					ComponentManager sl = (ComponentManager) request
							.getSession().getAttribute(
									WebKeys.COMPONENT_MANAGER);
					if (sl == null) {
						sl = new DefaultComponentManager(request.getSession(),
								this.context);
					}
					WebController wcc = sl.getWebController(this.context);
					eventResponse = wcc.handleEvent(ev, request.getSession());
					if (eventResponse == null) {
						javax.servlet.http.HttpSession session = request
								.getSession();
						if (session == null) {
							throw new ServletException(
									"session为空， EJB层处理返回也为空！");
						} else {
							throw new HTMLActionException(session,
									HTMLActionException.WARN_PAGE,
									"EJB层处理的返回事件为空，请检查["
											+ eventMapping
													.getEJBActionClassName()
											+ "]是否已经正确发布到EJBContainer上!");
						}
					}
				}
				action.doEnd(request, eventResponse);
			}
		}
		HttpSession session = request.getSession();
		if (session == null) {
			return true;
		}
		String result = (String) session
				.getAttribute(WebKeys.HANDLER_SCREEN_KEY);
		if (WebKeys.NULL_SCREEN.equals(result)) {
			return false;
		}
		return true;
	}

	/**
	 * This method load the necessary HTMLAction class necessary to process a
	 * the request for the specified URL.
	 */
	private HTMLAction getAction(URLMapping urlMapping) {
		HTMLAction handler = null;
		String actionClassString = null;
		if (urlMapping != null) {
			actionClassString = urlMapping.getWebAction();
			if (urlMapping.isAction()) {
				try {
					handler = (HTMLAction) Class.forName(actionClassString)
							.newInstance();
				} catch (Exception ex) {
					System.err
							.println("RequestProcessor caught loading action: "
									+ ex);
				}
			}
		}
		return handler;
	}

	private boolean isLoginUser(String legalURL, String reqURL,
			HttpSession session) throws InvalidSessionException {
		if (legalURL.equalsIgnoreCase(reqURL)) { // 排除登录的校验
			return true;
		}
		if (session != null) {
			String loginFlag = (String) session
					.getAttribute(WebKeys.SYS_LOGINFLAG);
			if ("true".equalsIgnoreCase(loginFlag))
				return true;
		}
		throw new InvalidSessionException();
	}

}
