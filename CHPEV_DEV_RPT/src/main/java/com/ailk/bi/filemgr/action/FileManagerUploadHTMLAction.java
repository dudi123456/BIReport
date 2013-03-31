package com.ailk.bi.filemgr.action;

//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
//import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.filemgr.common.Constants;
//import com.ailk.bi.filemgr.common.ListFile;
//import com.ailk.bi.filemgr.common.UpdateBlobField;
import com.ailk.bi.system.facade.impl.CommonFacade;
//import com.jspsmart.upload.SmartUpload;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

//import java.util.Timer;

@SuppressWarnings({ "unused", "rawtypes" })
public class FileManagerUploadHTMLAction extends HTMLActionSupport {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	/*
	 * public void doTrans(HttpServletRequest request, HttpServletResponse
	 * response) throws HTMLActionException { HttpSession session =
	 * request.getSession(); try {
	 *
	 * SmartUpload su = new SmartUpload();
	 *
	 * su.initialize(config, request, response);
	 * request.setCharacterEncoding("gb2312");
	 *
	 * su.upload();
	 *
	 * // 获取文件保存目录 String file_path = Constants.FILE_PATH_PREFIX; //
	 * 获取原来有的fileid
	 *
	 * for (int i = 0; i < su.getFiles().getCount(); i++) { String fileNameVis =
	 * StringB.NulltoBlank(su.getRequest().getParameter("icon_name")); String
	 * fileDesc= StringB.NulltoBlank(su.getRequest().getParameter("icon_desc"));
	 * String fileDir= su.getRequest().getParameter("fileDirId");
	 *
	 * com.jspsmart.upload.File file = su.getFiles().getFile(i); String fileName
	 * = file.getFileName(); String fileExt = file.getFileExt(); int fileSize =
	 * file.getSize(); // 获取文件ID String fileid =
	 * WebDBUtil.execQryArray("select SEQ_UI_SYS_INFO_FILE_STORE.NEXTVAL from dual"
	 * , null)[0][0]; String filepath = file_path + fileid + "." + fileExt;
	 * file.saveAs(filepath); //填写文件上传表 String sql =
	 * "insert into UI_SYS_INFO_FILE_STORE(FILE_ID, FILE_CREATEDATE, FILE_ORGNAME, FILE_FULPATH, FILE_EXT, FILE_STATUS, FILE_SIZE, FILE_RENAME,FILE_NAME,FILE_Desc,FILE_DIR) values("
	 * .concat(fileid) .concat(", sysdate, '") .concat(fileName) .concat("', '")
	 * .concat(filepath) .concat("', '") .concat(fileExt) .concat("', 'U', ")
	 * .concat(String.valueOf(fileSize)) .concat(",'") .concat(fileid)
	 * .concat(".") .concat(fileExt) .concat("',"); sql += "'" + fileNameVis +
	 * "','" + fileDesc + "'," + fileDir + ")"; WebDBUtil.execUpdate(sql);
	 * //记录操作日志 sql =
	 * "insert into UI_SYS_LOG_FILE_STORE(ID, FILE_ID, FILE_HANDLE_USER, FILE_HANDLE_DATE, FILE_HANDLE_ACTION) values(SEQ_UI_SYS_LOG_FILE_STORE.NEXTVAL, "
	 * .concat(fileid) .concat(", '")
	 * .concat(CommonFacade.getLoginId(request.getSession()))
	 * .concat("', sysdate, 'upload')"); WebDBUtil.execUpdate(sql);
	 * //request.setAttribute("uploadfiles", ListFile.getNormalFiles(null)); }
	 *
	 * request.setAttribute("CONST_SUBJECT_DEAL_INFO","上传成功！");
	 * this.setNextScreen(request, "sysAdminCtrlDealOk.screen");
	 *
	 *
	 * } catch (Exception e) { e.printStackTrace(); } }
	 *
	 *
	 *
	 *
	 * public void doTrans(HttpServletRequest request, HttpServletResponse
	 * response) throws HTMLActionException {
	 *
	 * HttpSession session = request.getSession(); String oper_no =
	 * CommonFacade.getLoginId(session); if
	 * (!com.asiabi.common.app.WebChecker.isLoginUser(request, response)) {
	 * return; }
	 *
	 * try { SmartUpload su = new SmartUpload(); su.initialize(config, request,
	 * response); su.upload();
	 *
	 * // 获取文件保存目录 //String file_path = Constants.FILE_PATH_PREFIX; String
	 * file_path = session.getServletContext().getRealPath("/")+
	 * File.separatorChar + "uploadfold"; // System.out.println("file_path:" +
	 * file_path); // String saveDirectory =
	 * req.getSession().getServletContext().getRealPath("/") +
	 * File.separatorChar + "uploadfold";
	 *
	 * // 获取原来有的fileid for (int i = 0; i < su.getFiles().getCount(); i++) {
	 * com.jspsmart.upload.File file = su.getFiles().getFile(i); String fileName
	 * = file.getFileName(); String fileExt = file.getFileExt(); int fileSize =
	 * file.getSize(); // 获取文件ID String fileid = WebDBUtil.execQryArray(
	 * "select SEQ_UI_SYS_INFO_FILE_STORE.NEXTVAL from dual", null)[0][0];
	 * String filepath = file_path + File.separatorChar + fileid + "." +
	 * fileExt; System.out.println("filepath:" + filepath);
	 * file.saveAs(filepath); // 填写文件上传表 String sql =
	 * "insert into UI_SYS_INFO_FILE_STORE(FILE_ID, FILE_CREATEDATE, FILE_ORGNAME, FILE_FULPATH, FILE_EXT, FILE_STATUS, FILE_SIZE, FILE_RENAME,oper_no) values("
	 * .concat(fileid).concat(", sysdate, '").concat(fileName)
	 * .concat("', '").concat(filepath).concat("', '").concat(
	 * fileExt).concat("', 'U', ").concat(
	 * String.valueOf(fileSize)).concat(",'").concat(
	 * fileid).concat(".").concat(
	 * fileExt).concat("','").concat(oper_no).concat("')");
	 * WebDBUtil.execUpdate(sql); // 记录操作日志 sql =
	 * "insert into UI_SYS_LOG_FILE_STORE(ID, FILE_ID, FILE_HANDLE_USER, FILE_HANDLE_DATE, FILE_HANDLE_ACTION) values(SEQ_UI_SYS_LOG_FILE_STORE.NEXTVAL, "
	 * .concat(fileid).concat(", '").concat(
	 * CommonFacade.getLoginId(request.getSession()))
	 * .concat("', sysdate, 'upload')");
	 *
	 *
	 * WebDBUtil.execUpdate(sql);
	 *
	 *
	 * //UpdateBlobField task = new UpdateBlobField("FILE_ID", "FILE_CONTENT",
	 * "UI_SYS_INFO_FILE_STORE", fileid, filepath); //Timer timer = new Timer();
	 * // timer.schedule(task, 5000);
	 *
	 * WebDBUtil.updateBlogFld("FILE_ID", "FILE_CONTENT",
	 * "UI_SYS_INFO_FILE_STORE", fileid, filepath);
	 *
	 *
	 * request.setAttribute("uploadfiles", ListFile .getNormalFiles(null)); }
	 * this.setNextScreen(request, "filemgrUpload.screen"); } catch (Exception
	 * e) { e.printStackTrace(); } }
	 */

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();
		String oper_no = CommonFacade.getLoginId(session);
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		uploadConditionFile_ByApache(request, response, oper_no);

		request.setAttribute("CONST_SUBJECT_DEAL_INFO", "上传成功！");
		this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
	}

	private void uploadConditionFile_ByApache(HttpServletRequest req,
			HttpServletResponse res, String oper_no) {

		String[] strRetn = new String[2];
		//从hlbi.config.xml中读取服务器存放的文件路径。
		String file_path = Constants.FILE_PATH_PREFIX;

		if (ServletFileUpload.isMultipartContent(req)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// String foldPath = this.getInitParameter("filepath");
			// factory.setRepository(new File(foldPath));
			// 内存最大占用
			factory.setSizeThreshold(1024000);
			ServletFileUpload sfu = new ServletFileUpload(factory);

			String charset = req.getCharacterEncoding();
			logcommon.debug("charset::======" + charset);
			if (charset != null) {
				sfu.setHeaderEncoding(charset);
			} else {

			}

			// 单个文件最大值byte
			sfu.setSizeMax(1024 * 1024 * 20);
			// 所有上传文件的总和最大值byte
			// sfu.setSizeMax(204800000);
			List items = null;
			try {
				items = sfu.parseRequest(req);
			} catch (SizeLimitExceededException e) {
				logcommon.debug("size limit exception!");
			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator iter = items == null ? null : items.iterator();
			FileItem itemUpload = null;
			String fileName = "";
			String fileExt = "";
			long fileSize = 0;
			String icon_name = "";
			String icon_desc = "";
			String msu_id = "";
			String path = "";

			while (iter != null && iter.hasNext()) {

				FileItem item = (FileItem) iter.next();
				fileSize = item.getSize();
				logcommon.debug("File size：" + item.getSize());
				if (!item.isFormField()) {
					//
					// 解析文件数据入库！
					// item.getInputStream()
					itemUpload = item;
					// item.get

					path = itemUpload.getName();
					System.out.println(path);
					path = path.replaceAll("/", "\\");
					fileName = path.substring(path.lastIndexOf("\\") + 1);

					// 得到文件的扩展名(无扩展名时将得到全名)

					fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
				} else if (item.isFormField()) {
					// 普通表单型
					try {
						if (item.getFieldName().equals("icon_name")) {
							icon_name = item.getString(charset);

						} else if (item.getFieldName().equals("icon_desc")) {
							icon_desc = item.getString(charset);

						} else if (item.getFieldName().equals("fileDirId")) {
							msu_id = item.getString(charset);

						}
					} catch (UnsupportedEncodingException e) {

						e.printStackTrace();
					}

				}
			}
			String fileid = "";
			try {
				fileid = WebDBUtil.execQryArray(
						"select SEQ_UI_SYS_INFO_FILE_STORE.NEXTVAL from dual",
						null)[0][0];
			} catch (AppException e) {

				e.printStackTrace();
			}

			// String saveDirectory =
			// req.getSession().getServletContext().getRealPath("/") +
			// File.separatorChar + "uploadfold";
			String fileTmp = file_path + File.separatorChar + fileid + "."
					+ fileExt;
			try {
				itemUpload.write(new File(fileTmp));
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			String sql = "insert into UI_SYS_INFO_FILE_STORE(FILE_ID, FILE_CREATEDATE, FILE_ORGNAME, FILE_FULPATH, FILE_EXT, FILE_STATUS, FILE_SIZE, FILE_RENAME,OPER_NO,FILE_NAME,FILE_Desc,FILE_DIR) values("
					.concat(fileid).concat(", sysdate, '").concat(fileName)
					.concat("', '").concat(fileTmp).concat("', '")
					.concat(fileExt).concat("', 'U', ")
					.concat(String.valueOf(fileSize)).concat(",'")
					.concat(fileid).concat(".").concat(fileExt).concat("','")
					.concat(oper_no).concat("','");
			sql += icon_name + "','" + icon_desc + "'," + msu_id + ")";

			try {
				WebDBUtil.execUpdate(sql);
			} catch (AppException e) {

				e.printStackTrace();
			}
			// 记录操作日志
			sql = "insert into UI_SYS_LOG_FILE_STORE(ID, FILE_ID, FILE_HANDLE_USER, FILE_HANDLE_DATE, FILE_HANDLE_ACTION) values(SEQ_UI_SYS_LOG_FILE_STORE.NEXTVAL, "
					.concat(fileid).concat(", '")
					.concat(CommonFacade.getLoginId(req.getSession()))
					.concat("', sysdate, 'upload')");
			try {
				WebDBUtil.execUpdate(sql);
			} catch (AppException e) {

				e.printStackTrace();
			}

			// WebDBUtil.updateBlogFld("FILE_ID", "FILE_CONTENT",
			// "UI_SYS_INFO_FILE_STORE", fileid, fileTmp);
			// WebDBUtil.updateBlogFld("FILE_ID", "FILE_CONTENT",
			// "UI_SYS_INFO_FILE_STORE", fileid, itemUpload);

		}

	}

}
