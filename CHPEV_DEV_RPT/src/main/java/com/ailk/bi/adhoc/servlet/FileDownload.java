package com.ailk.bi.adhoc.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.util.AdhocBuildDownLoadFile;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unused", "serial" })
public class FileDownload extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
