package com.ailk.bi.filemgr.action;

import com.ailk.bi.filemgr.common.ListFile;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2010-1-19 Time: 11:12:30
 * To change this template use File | Settings | File Templates.
 */
public class FileManagerListFileHTMLAction extends HTMLActionSupport {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		try {
			request.setAttribute("uploadfiles", ListFile.getNormalFiles(null));
			this.setNextScreen(request, "filemgrUpload.screen");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
