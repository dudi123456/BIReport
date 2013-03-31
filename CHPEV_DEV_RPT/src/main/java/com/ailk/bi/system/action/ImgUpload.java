package com.ailk.bi.system.action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.jsp.*;

import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.jspsmart.upload.Request;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ImgUpload extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5008378709126346606L;

	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		String root_url = request.getContextPath();
		if (null == root_url || "".equals(root_url)) {
			root_url = "/";
		} else {
			root_url = root_url + "/";
		}
		root_url = root_url.substring(0, root_url.length() - 1);

		UploadFile upload = new UploadFile();
		PageContext pageContext = JspFactory.getDefaultFactory()
				.getPageContext(this, request, response, null, // errorPageURL
						false, // needsSession
						JspWriter.NO_BUFFER, false // autoFlush
				);
		ServletConfig config = getServletConfig();
		upload.setUpload(pageContext, config, request, response);
		Request jrequest = upload.getRequest();

		//
		String loginName = CommonFacade.getLoginUser(session).oper_name;
		String date = "" + DateUtil.getNowDate().getTime();
		String filename = "img_" + loginName + "_" + date;
		String dir = "/upload";
		String img_dir = dir + "/biimages/";
		com.jspsmart.upload.File link = upload.getNextFile(0);
		String link_name = filename + "." + link.getFileExt();
		//
		String fileName = img_dir + link_name;

		//
		String url = "";
		try {
			url = jrequest.getParameter("url");
		} catch (Exception e) {
			return;
		}

		//
		String reUrl = root_url + url;

		if (upload.upload(0, img_dir, link_name)) {
			response.sendRedirect(reUrl + "?image_name=" + fileName
					+ "&upload_over=true");
		} else {
			link_name = "";
			response.sendRedirect(reUrl + "?upload_over=false");
			return;
		}

	}

	// Clean up resources
	public void destroy() {
	}
}