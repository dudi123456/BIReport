package com.ailk.bi.filemgr.action;

//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.dbtools.WebDBUtil;
//import com.ailk.bi.filemgr.common.Constants;
import com.ailk.bi.filemgr.common.ListFile;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.jspsmart.upload.SmartUpload;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2010-1-18 Time: 21:10:19
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({ "rawtypes" })
public class FileManagerDownloadHTMLAction extends HTMLActionSupport {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		try {

			SmartUpload su = new SmartUpload();
			su.initialize(config, request, response);
			su.upload();

			// 获取页面条件
			String file_id = su.getRequest().getParameter("file_id");
			List lstFiles = WebDBUtil.execQryArrayMap(
					"select * from UI_SYS_INFO_FILE_STORE where FILE_ID="
							+ file_id, new String[] {});
			if (lstFiles != null && lstFiles.size() > 0) {
				HashMap map = (HashMap) lstFiles.get(0);
				String filePath = map.get("FILE_FULPATH").toString();
				String fileName = map.get("FILE_ORGNAME").toString();
				// filePath = config.getServletContext().getRealPath("/") +
				// filePath;
				// 记录文件访问记录
				String sql = "insert into UI_SYS_LOG_FILE_STORE(ID, FILE_ID, FILE_HANDLE_USER, FILE_HANDLE_DATE, FILE_HANDLE_ACTION) values(SEQ_UI_SYS_LOG_FILE_STORE.NEXTVAL, "
						.concat(file_id).concat(", '")
						.concat(CommonFacade.getLoginId(request.getSession()))
						.concat("', sysdate, 'download')");
				WebDBUtil.execUpdate(sql);
				System.out.println(filePath);
				su.setContentDisposition(null);
				su.downloadFile(filePath, null, fileName);// .downloadFile(filePath,
															// null, fileName);
				// su.downloadFile(s)
			}
			request.setAttribute("uploadfiles", ListFile.getNormalFiles(null));
			request.setAttribute("filenum", String.valueOf(lstFiles.size()));

			this.setNvlNextScreen(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void doTrans(HttpServletRequest request, HttpServletResponse
	 * response) throws HTMLActionException { try {
	 * 
	 * 
	 * // 获取页面条件 String file_id = request.getParameter("file_id"); List lstFiles
	 * = WebDBUtil.execQryArrayMap(
	 * "select * from UI_SYS_INFO_FILE_STORE where FILE_ID=" + file_id, new
	 * String[] {}); if (lstFiles != null && lstFiles.size() > 0) { HashMap map
	 * = (HashMap) lstFiles.get(0); String filePath =
	 * map.get("FILE_FULPATH").toString(); String fileName =
	 * map.get("FILE_ORGNAME").toString(); // filePath =
	 * config.getServletContext().getRealPath("/") + // filePath; // 记录文件访问记录
	 * String sql =
	 * "insert into UI_SYS_LOG_FILE_STORE(ID, FILE_ID, FILE_HANDLE_USER, FILE_HANDLE_DATE, FILE_HANDLE_ACTION) values(SEQ_UI_SYS_LOG_FILE_STORE.NEXTVAL, "
	 * .concat(file_id).concat(", '").concat(
	 * CommonFacade.getLoginId(request.getSession()))
	 * .concat("', sysdate, 'download')"); WebDBUtil.execUpdate(sql); //
	 * System.out.println(filePath);
	 * 
	 * //select file_orgname from UI_SYS_INFO_FILE_STORE where file
	 * 
	 * InputStream is = WebDBUtil.readBlogContent("FILE_ID", "FILE_CONTENT",
	 * "UI_SYS_INFO_FILE_STORE", file_id);// // keyFld, blobFld, tablename,
	 * keyId); response.setContentType("application/x-msdownload"); //
	 * response.setContentLength((int)blob.length());
	 * response.setHeader("Content-Disposition",
	 * "attachment; filename=".concat(fileName));
	 * 
	 * // InputStream is = blob.getBinaryStream(); OutputStream os =
	 * response.getOutputStream();
	 * 
	 * byte[] buffer = new byte[4000]; int length; while ((length =
	 * is.read(buffer)) != -1) os.write(buffer, 0, length);
	 * 
	 * // Close input and output streams is.close(); os.close();
	 * 
	 * 
	 * } request.setAttribute("uploadfiles", ListFile.getNormalFiles(null));
	 * request.setAttribute("filenum", String.valueOf(lstFiles.size()));
	 * this.setNvlNextScreen(request); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
}
