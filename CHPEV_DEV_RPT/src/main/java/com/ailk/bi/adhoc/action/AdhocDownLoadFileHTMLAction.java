package com.ailk.bi.adhoc.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.filemgr.common.Constants;
import com.jspsmart.upload.SmartUpload;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocDownLoadFileHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 8607442130730549649L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();
		// String oper_no = CommonFacade.getLoginId(session);

		// 类型1:下载生成的清单文件,2：下载指标说明文档

		String flagType = StringB
				.NulltoBlank(request.getParameter("adhocType"));
		try {
			SmartUpload su = new SmartUpload();
			su.initialize(config, request, response);

			if (flagType.equals("2")) {
				String saveDirectory = session.getServletContext().getRealPath(
						"/")
						+ File.separatorChar + "adhoc";
				String srcFileName = "zhibiaodesc.doc";
				String aimFileName = "指标解释(口径).doc";
				su.setContentDisposition(null);
				su.downloadFile(saveDirectory + File.separatorChar
						+ srcFileName, null, aimFileName);
			} else if (flagType.equals("1")) {
				String id = StringB.NulltoBlank(request.getParameter("id"));
				String sql = "select xls_name,file_name from ui_adhoc_buildxls_task t where id="
						+ id;
				String res[][] = WebDBUtil.execQryArray(sql);
				String srcFileName = "";
				String aimFileName = "";
				if (res != null && res.length > 0) {
					srcFileName = res[0][1];
					aimFileName = res[0][0] + ".zip";
				}

				su.setContentDisposition(null);
				String file_path = Constants.FILE_PATH_PREFIX;
				su.downloadFile(file_path + File.separatorChar + "adhocFile/"
						+ srcFileName, null, aimFileName);
			}
			setNvlNextScreen(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
