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

import java.beans.Beans;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import waf.controller.ejb.EJBControllerRemote;
import waf.controller.ejb.EJBControllerRemoteHome;
import waf.controller.web.util.WebKeys;
import waf.exceptions.GeneralFailureException;
import waf.util.JNDINames;

import com.ailk.bi.common.app.ServiceLocator;

/**
 * This implmentation class of the ComponentManager provides access to services
 * in the web tier and ejb tier.
 * 
 */
public class DefaultComponentManager implements ComponentManager,
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4809503665519733544L;

	protected ServiceLocator sl = null;

	private ServletContext context;

	public DefaultComponentManager() {
		sl = ServiceLocator.getInstance();
	}

	public DefaultComponentManager(HttpSession session, ServletContext context) {
		sl = ServiceLocator.getInstance();
		session.setAttribute(WebKeys.COMPONENT_MANAGER, this);
		this.context = context;
	}

	public WebController getWebController(ServletContext context) {
		// ServletContext context = session.getServletContext();
		WebController wcc = (WebController) context
				.getAttribute(WebKeys.WEB_CONTROLLER);
		if (wcc == null) {
			try {
				String wccClassName = sl.getWebString(new InitialContext(),
						JNDINames.DEFAULT_WEB_CONTROLLER);
				com.ailk.bi.common.app.Debug.println("wccClassName="
						+ wccClassName);
				wcc = (WebController) Beans.instantiate(this.getClass()
						.getClassLoader(), wccClassName);
				wcc.init(context);
			} catch (Exception exc) {
				throw new RuntimeException(
						"Cannot create bean of class WebController: " + exc);
			}
		}
		return wcc;
	}

	public EJBControllerRemote getEJBController(HttpSession session) {
		EJBControllerRemote ccEjb = (EJBControllerRemote) session
				.getAttribute(WebKeys.EJB_CONTROLLER);
		if (ccEjb == null) {
			try {
				com.ailk.bi.common.app.Debug.println("EJB_CONTROLLER="
						+ JNDINames.EJB_CONTROLLER_EJBHOME);
				EJBControllerRemoteHome home = (EJBControllerRemoteHome) ServiceLocator
						.getInstance().getHome(
								JNDINames.EJB_CONTROLLER_EJBHOME,
								EJBControllerRemoteHome.class);
				com.ailk.bi.common.app.Debug.println("EJB_home=" + home);
				ccEjb = home.create();
				session.setAttribute(WebKeys.EJB_CONTROLLER, ccEjb);
				com.ailk.bi.common.app.Debug.println("EJB_ccEjb=" + ccEjb);
			} catch (Exception ce) {
				throw new GeneralFailureException(ce.getMessage());
			}
		}
		return ccEjb;
	}

	/**
	 * 
	 * Create the WebController which in turn should create the
	 * EJBClientController.
	 * 
	 */
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		sl = ServiceLocator.getInstance();
		session.setAttribute(WebKeys.COMPONENT_MANAGER, this);
	}

	/**
	 * 
	 * Destroy the WebClientController which in turn should destroy the
	 * EJBClientController.
	 * 
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		WebController wcc = getWebController(context);
		if (wcc != null) {
			wcc.destroy(session);
		}
	}
}
