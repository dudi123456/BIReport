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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.event.JBTable;

/**
 * This class is essentially just a proxy object that calls methods on the EJB
 * tier using the waf.controller.ejb.ShoppingClientControllerEJB object. All the
 * methods that access the EJB are synchronized so that concurrent requests do
 * not happen to the stateful session bean.
 */
public interface WebController extends java.io.Serializable {

	/**
	 * constructor for an HTTP client.
	 * 
	 * @param the
	 *            ServletContext object of the application
	 */
	public void init(ServletContext context);

	/**
	 * feeds the specified event to the state machine of the business logic.
	 * 
	 * @param ev
	 *            is the current event
	 * @return an waf.event.EventResponse resulting in the processing of this
	 *         event.
	 * @exception waf.event.EventException
	 *                <description>
	 * @exception waf.exceptions.GeneralFailureException
	 */
	public JBTable handleEvent(JBTable ev, HttpSession session)
			throws AppException;

	/**
	 * frees up all the resources associated with this controller and destroys
	 * itself.
	 */
	public void destroy(HttpSession session);
}
