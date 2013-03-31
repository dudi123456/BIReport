package com.ailk.bi.adhoc.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocUserUploadConditon;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

//import com.oreilly.servlet.MultipartRequest;
//import com.oreilly.servlet.multipart.FileRenamePolicy;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class AdhocUserUpLoadConHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();
		// 操作类型
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "view";
		}

		logcommon.debug("operType:" + operType);

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		String oper_no = CommonFacade.getLoginId(session);

		if ("view".equals(operType)) {// 查询UI_ADHOC_CONDITION_USERUP表中，该用户上传的条件

			session.setAttribute("RECORD_CNT", null);

			String hoc_id = CommTool.getParameterGB(request,
					AdhocConstant.ADHOC_MULTI_SELECT_QRY_HOC_ID);
			if (null == hoc_id || "".equalsIgnoreCase(hoc_id)) {
				return;
			}
			session.setAttribute(
					AdhocConstant.ADHOC_MULTI_SELECT_HOC_ID_SESSION, hoc_id);

			// 即席查询条件con_id
			String con_id = CommTool.getParameterGB(request,
					AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_ID);
			if (null == con_id || "".equalsIgnoreCase(con_id)) {
				return;
			}
			session.setAttribute(
					AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION,
					con_id);

			// 当前已选值qry_name
			String qry_name_value = CommTool.getParameterGB(request,
					AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_QRY_NAME);
			session.removeAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION);
			session.setAttribute(
					AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION,
					qry_name_value);

			// 条件名称con_name
			//String con_name = CommTool.getParameter8895_1(request,
			//		AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_NAME);
			 String con_name =
					 request.getParameter(AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_NAME);
			try {
				con_name = java.net.URLDecoder.decode(con_name,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (null == con_name) {
				con_name = "";
			}
			session.setAttribute(
					AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION,
					con_name);
			request.setAttribute("OBJ_LIST",
					getUserUploadCondition(oper_no, con_id));

			this.setNextScreen(request, "AdhocUserUpConInfo.screen");

		} else if ("upfile".equals(operType)) {// 上传文件
			String con_id = "";
			String[] strRtn = null;

			strRtn = uploadConditionFile_ByApache(request, response, oper_no);

			if (strRtn == null) {
				request.setAttribute("HANDLE_INFO", "文件导入成功");
			} else {
				con_id = strRtn[0];
				request.setAttribute("OBJ_LIST",
						getUserUploadCondition(oper_no, con_id));
				request.setAttribute("HANDLE_INFO", strRtn[1]);
			}

			this.setNextScreen(request, "AdhocUserUpConInfo.screen");

		} else if ("delupfile".equals(operType)) {// 删除上传文件
			String id = request.getParameter("id");
			deleteUpConditionFile(id);

			String con_id = (String) session
					.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION);
			request.setAttribute("OBJ_LIST",
					getUserUploadCondition(oper_no, con_id));
			request.setAttribute("HANDLE_INFO", "数据删除成功");

			this.setNextScreen(request, "AdhocUserUpConInfo.screen");
		} else if ("viewdata".equals(operType)) {// 预览数据

			String id = request.getParameter("id");
			session.setAttribute("RECORD_CNT", viewUpConditionData_Count(id));
			session.setAttribute("OBJ_LIST", viewUpConditionData(id));

			this.setNextScreen(request, "AdhocUserViewUpData.screen");
		}

		// super.doTrans(request, response);
	}

	private String viewUpConditionData_Count(String id) {
		String sql = "select count(*) FROM ui_adhoc_condition_updetail WHERE id="
				+ id;
		try {
			String[][] reslt = WebDBUtil.execQryArray(sql, "");
			if (reslt != null && reslt.length > 0) {
				return reslt[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private String[][] viewUpConditionData(String id) {
		String sql = "select OBJ_VALUE FROM ui_adhoc_condition_updetail WHERE id="
				+ id + " and rownum<=1000";
		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private void deleteUpConditionFile(String id) {

		String sql1 = "delete from ui_adhoc_condition_userup where id=" + id;

		String sql2 = "delete from ui_adhoc_condition_updetail where id=" + id;
		String[] sqlDel = { sql1, sql2 };

		try {
			WebDBUtil.execTransUpdate(sqlDel);
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	private String[] uploadConditionFile_ByApache(HttpServletRequest req,
			HttpServletResponse res, String oper_no) {

		String[] strRetn = new String[2];

		if (ServletFileUpload.isMultipartContent(req)) {

			// req.setCharacterEncoding("utf-8");
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

			String icon_name = "";
			String icon_desc = "";
			String msu_id = "";
			FileItem itemUpload = null;

			while (iter != null && iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				logcommon.debug("File size：" + item.getSize());
				if (!item.isFormField()) {
					//
					// 解析文件数据入库！
					// item.getInputStream()
					itemUpload = item;
					// item.get

				} else if (item.isFormField()) {
					// 普通表单型
					try {
						if (item.getFieldName().equals("icon_name")) {
							icon_name = item.getString(charset);

						} else if (item.getFieldName().equals("icon_desc")) {
							icon_desc = item.getString(charset);

						} else if (item.getFieldName().equals("msu_id")) {
							msu_id = item.getString(charset);

						}
					} catch (UnsupportedEncodingException e) {

						e.printStackTrace();
					}

				}
			}

			// AdhocUserUploadConditon upCon = new AdhocUserUploadConditon();

			Calendar ca = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String strNow = formatter.format(ca.getTime());

			// 插入数据到UI_ADHOC_CONDITION_USERUP表中
			// String mainID =
			// AdhocUtil.dbGetMaxID("UI_ADHOC_CONDITION_USERUP");
			String mainID = CommTool.dbGetMaxIDBySeqName("COND_UP_SEQ");

			logcommon.debug("mainID:" + mainID);
			strRetn[0] = msu_id;
			strRetn[1] = "文件导入成功";
			if (insertUpConDetail(itemUpload, mainID, msu_id)) {
				String sqlInsertDtl = "INSERT INTO UI_ADHOC_CONDITION_UPDETAIL "
						+ "select distinct id,msu_id,OBJ_VALUE from UI_ADHOC_CONDITION_UPDTL_TMP where id="
						+ mainID;
				try {
					WebDBUtil.execUpdate(sqlInsertDtl);
				} catch (AppException e1) {

					e1.printStackTrace();
				}

				String sqlInsert = "insert into UI_ADHOC_CONDITION_USERUP(ID,MSU_ID,MSU_TYPE,NAME,NAME_DESC,OPER_NO,ADD_DATE) values("
						+ mainID
						+ ",'"
						+ msu_id
						+ "','1','"
						+ icon_name
						+ "','"
						+ icon_desc
						+ "','"
						+ oper_no
						+ "',to_date('"
						+ strNow + "','yyyy-mm-dd'))";

				String sqlDel = "delete from UI_ADHOC_CONDITION_UPDTL_TMP where id="
						+ mainID;
				try {

					WebDBUtil.execUpdate(sqlInsert);
					WebDBUtil.execUpdate(sqlDel);

				} catch (AppException e) {
					strRetn[1] = "文件导入失败";

					e.printStackTrace();
				}

			} else {
				strRetn[1] = "文件导入失败";
			}

			// 循环插入UI_ADHOC_CONDITION_UPDETAIL表

			return strRetn;
		} else {
			return null;
		}

	}

	private boolean insertUpConDetail(FileItem itemUpload, String mainID,
			String msu_id) {

		double recordCnt = 0;
		int perPageCnt = 5000;

		String fileName = itemUpload.getName();
		// 以下为文件名处理，将上传的文件保存在项目所在目录下。
		// 获取文件名字符串的长度
		int end = fileName.length();
		// 返回在此字符串中最右边出现的指定子字符串的索引。
		// logcommon.debug(fileName + "=======文件名称");
		int begin = fileName.lastIndexOf("\\");
		int start = fileName.lastIndexOf(".");
		// 输出上传文件类型，此处可以进行类型的过滤
		String fileType = fileName.substring(start + 1, end).toLowerCase();

		// logcommon.debug("文件类型:" + fileType);

		AdhocUserUploadConditon upCon = new AdhocUserUploadConditon();

		if (fileType.equalsIgnoreCase("txt")) {
			upCon.setFileType(0);
		} else if (fileType.equalsIgnoreCase("xls")) {
			upCon.setFileType(1);
		} else if (fileType.equalsIgnoreCase("xlsx")) {
			upCon.setFileType(2);
		}

		try {
			recordCnt = upCon.calculateFileRowCnt(itemUpload.getInputStream());
			// logcommon.debug("recordCnt:" + recordCnt);
		} catch (IOException e) {

			deleteInfoWhenError(mainID);
			e.printStackTrace();
			return false;
		}
		double pageCnt = Math.floor((recordCnt + perPageCnt - 1) / perPageCnt);
		logcommon.debug("pageCnt:" + pageCnt);

		for (int i = 0; i < pageCnt; i++) {

			List list = null;
			try {
				if (((int) pageCnt) == 1) {
					list = upCon.readContentFromLine(
							itemUpload.getInputStream(), i * perPageCnt,
							(int) recordCnt);
				} else {
					if (i == (pageCnt - 1)) {
						list = upCon.readContentFromLine(
								itemUpload.getInputStream(), i * perPageCnt,
								(int) recordCnt);
					} else {
						list = upCon.readContentFromLine(
								itemUpload.getInputStream(), i * perPageCnt,
								(i + 1) * perPageCnt);
					}

				}

			} catch (IOException e) {

				deleteInfoWhenError(mainID);
				e.printStackTrace();
				return false;
			} catch (Exception e) {

				deleteInfoWhenError(mainID);
				e.printStackTrace();
				return false;
			}
			if (list.size() > 0) {
				// String[][] args = new String[list.size()][3];
				// String sql =
				// "insert into UI_ADHOC_CONDITION_UPDETAIL(ID,MSU_ID,OBJ_VALUE) values(?,?,?)";
				List listSql = new ArrayList();

				for (int k = 0; k < list.size(); k++) {

					// String sql =
					// "insert into UI_ADHOC_CONDITION_UPDETAIL(ID,MSU_ID,OBJ_VALUE) values("
					// + mainID + ",'" + msu_id + "','";
					String sql = "insert into UI_ADHOC_CONDITION_UPDTL_TMP(ID,MSU_ID,OBJ_VALUE) values("
							+ mainID + ",'" + msu_id + "','";
					sql += (String) list.get(k) + "')";
					// logcommon.debug((String)list.get(k));
					listSql.add(sql);
					/*
					 * args[k][0] = mainID; args[k][1] = msu_id; args[k][2] =
					 * (String)list.get(k);
					 */

				}

				try {
					WebDBUtil.execTransUpdate((String[]) listSql
							.toArray(new String[listSql.size()]));
				} catch (AppException e) {

					deleteInfoWhenError(mainID);
					e.printStackTrace();
					return false;
				}

				// upCon.batch(sql,args);
			}

		}
		return true;
	}

	/*
	 * private String uploadConditionFile(HttpServletRequest request,
	 * HttpServletResponse response,String oper_no) throws Exception { String
	 * saveDirectory = request.getSession().getServletContext()
	 * .getRealPath("/") + "\\adhocUpLoad\\"; int maxPostSize = 5 * 1024 *
	 * 1024;// 最大5兆 String content = "";// 上传的手机号码（以“,”隔开） MultipartRequest
	 * multi = null; File uploadPath = new File(saveDirectory); if
	 * (!uploadPath.exists()) { uploadPath.mkdir(); } multi = new
	 * MultipartRequest(request, content, maxPostSize, content, null); try {
	 * multi = new MultipartRequest(request, saveDirectory, maxPostSize, "GBK",
	 * new FileRenamePolicy() { public File rename(File file) { String body =
	 * ""; String ext = ""; String name = file.getName(); int pot =
	 * name.lastIndexOf("."); if (pot != -1) { body = (new
	 * java.util.Date()).getTime() + ""; ext = name.substring(pot); } else {
	 * body = (new java.util.Date()).getTime() + ""; ext = ""; } String newName
	 * = body + ext; file = new File(file.getParent(), newName); return file; }
	 * }); } catch (IOException e) { e.printStackTrace(); }
	 * AdhocUserUploadConditon upCon = new AdhocUserUploadConditon();
	 *
	 * String icon_name = StringB.NulltoBlank(multi.getParameter("icon_name"));
	 * String icon_desc = StringB.NulltoBlank(multi.getParameter("icon_desc"));
	 * String msu_id = StringB.NulltoBlank(multi.getParameter("msu_id"));
	 *
	 * Calendar ca = Calendar.getInstance(); SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy-MM-dd"); String strNow =
	 * formatter.format(ca.getTime());
	 *
	 * //插入数据到UI_ADHOC_CONDITION_USERUP表中 //String mainID =
	 * AdhocUtil.dbGetMaxID("UI_ADHOC_CONDITION_USERUP"); String mainID =
	 * CommTool.dbGetMaxIDBySeqName("COND_UP_SEQ");
	 *
	 *
	 * String sqlInsert =
	 * "insert into UI_ADHOC_CONDITION_USERUP(ID,MSU_ID,MSU_TYPE,NAME,NAME_DESC,OPER_NO,ADD_DATE) values("
	 * + mainID + ",'" + msu_id + "','1','" + icon_name + "','" + icon_desc +
	 * "','" + oper_no + "',to_date('" + strNow + "','yyyy-mm-dd'))";
	 * upCon.excuteSql(sqlInsert);
	 *
	 * //循环插入UI_ADHOC_CONDITION_UPDETAIL表 double recordCnt = 0; int perPageCnt =
	 * 5000;
	 *
	 * Enumeration files = multi.getFileNames(); while (files.hasMoreElements())
	 * { String name = (String) files.nextElement(); File f =
	 * multi.getFile(name); recordCnt = upCon.calculateFileRowCnt(f);
	 *
	 * double pageCnt = Math.floor((recordCnt+perPageCnt-1)/perPageCnt);
	 *
	 * for (int i=0;i<pageCnt;i++){
	 *
	 * List list = upCon.readContentFromLine(f, i*perPageCnt, (i+1)*perPageCnt);
	 * if (list.size()>0){ List listSql = new ArrayList();
	 *
	 *
	 * for (int k = 0; k < list.size(); k++) { String sql =
	 * "insert into UI_ADHOC_CONDITION_UPDETAIL(ID,MSU_ID,OBJ_VALUE) values(" +
	 * mainID + ",'" + msu_id + "','";
	 *
	 * sql += (String)list.get(k) + "')";
	 *
	 * listSql.add(sql);
	 *
	 *
	 * }
	 *
	 *
	 *
	 * WebDBUtil.execTransUpdate((String[])listSql.toArray(new
	 * String[listSql.size()]));
	 *
	 * }
	 *
	 * }
	 *
	 *
	 * } return msu_id;
	 *
	 * }
	 */

	/**
	 *
	 * @param oper_no
	 * @param msu_id
	 * @return
	 */
	private String[][] getUserUploadCondition(String oper_no, String msu_id) {
		String sql = "select id,msu_id,name,name_desc,to_char(add_date,'yyyy-mm-dd') from UI_ADHOC_CONDITION_USERUP where"
				+ " msu_id='"
				+ msu_id
				+ "' and oper_no='"
				+ oper_no
				+ "' order by id desc";
		// return WebDBUtil.execQryArray(sql, "");
		AdhocUserUploadConditon uploadCon = new AdhocUserUploadConditon();
		return uploadCon.qryObjectInfoList(sql);
	}

	private void deleteInfoWhenError(String mainID) {
		String sql = "delete from ui_adhoc_condition_updetail where id="
				+ mainID;
		try {
			logcommon.debug(sql);
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}

	}
}
