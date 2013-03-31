package com.ailk.bi.system.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.ailk.bi.base.util.CommTool;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.filemgr.common.ListFile;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class CreateFileDirXmlHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6413313600306403086L;

	@SuppressWarnings({ "rawtypes" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();
		//
		// InfoOperTable loginUser = CommonFacade.getLoginUser(session);

		String id = request.getParameter("id");
		// String pid = request.getParameter("pid");
		//
		String operType = CommTool.getParameterGB(request, "opType");
		if (operType == null || "".equals(operType)) {
			operType = "listFile";
		}
		ReportQryStruct struct = new ReportQryStruct();

		if ("listFile".equals(operType)) {// 查询某目录下的文件
			request.setAttribute("init", "true");
			String sql = "select id,parent_id,dir_name from UI_SYS_INFO_FILE_DIR where id="
					+ id;
			try {
				String[][] res = WebDBUtil.execQryArray(sql, "");
				if (res != null && res.length > 0) {
					struct.dim1 = res[0][0];
					struct.dim2 = res[0][1];
					struct.dim3 = res[0][2];
					// struct.dim3 = res[0][2];

				}

				session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, struct);

			} catch (AppException e) {

				e.printStackTrace();
			}

			session.setAttribute("VIEW_TREE_LIST",
					ListFile.getNormalFiles(id, null));
			setNextScreen(request, "ViewUploadFileInfo.screen");
		} else if ("addDir".equals(operType)) {
			// 增加目录
			String submitType = request.getParameter("submitType");
			if (submitType == null || "".equals(submitType)) {
				submitType = "0";
			}

			if ("0".equals(submitType)) { // 新增
				ListFile.addDir(request);
			} else if ("1".equals(submitType) || "3".equals(submitType)) { // 修改
				ListFile.updateDir(request);
			} else if ("2".equals(submitType)) { // 删除
				ListFile.delDir(request);
			}

		} else if ("delFile".equals(operType)) {
			// 删除文件

			ListFile.delUpfile(request);
			/*
			 * request.setAttribute("init","true"); String dirId =
			 * request.getParameter("dirId");
			 * 
			 * String sql =
			 * "select id,parent_id,dir_name from UI_SYS_INFO_FILE_DIR where id="
			 * + dirId; try { String[][] res = WebDBUtil.execQryArray(sql, "");
			 * if (res!=null && res.length>0){ struct.dim1 = res[0][0];
			 * struct.dim2 = res[0][1]; struct.dim3 = res[0][2]; //struct.dim3 =
			 * res[0][2];
			 * 
			 * 
			 * }
			 * 
			 * session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,struct);
			 * 
			 * } catch (AppException e) { e.printStackTrace(); }
			 * 
			 * System.out.println("VIEW_TREE_LIST:" + dirId);
			 * session.setAttribute("VIEW_TREE_LIST",
			 * ListFile.getNormalFiles(dirId, null)); setNextScreen(request,
			 * "ViewUploadFileInfo.screen");
			 */

		} else if ("xmltree".equals(operType)) {
			try {
				request.setCharacterEncoding("gb2312");
				response.setContentType("text/xml;charset=gb2312");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
			try {
				// 取sql语句

				// 查询数据存入Vector或数组
				String sql = "SELECT id,parent_id,dir_name,dir_desc FROM UI_SYS_INFO_FILE_DIR where parent_id=0 ORDER BY SORT_NUM,id";
				if (id != null && !"".equals(id)) {
					sql = "SELECT id,parent_id,dir_name,dir_desc FROM UI_SYS_INFO_FILE_DIR where parent_id="
							+ id + " ORDER BY SORT_NUM,id";
				}

				System.out.println("---" + sql);
				Vector result = WebDBUtil.execQryVector(sql, "");
				if (result == null) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "目录信息失败！");

				}

				// 根据sql字符串，查询取得的数据生成源XML文档
				Document sourcedoc = XMLTranser.getDocument(sql, result);
				// 源xml文档结合指定xsl生成目标xml文档
				Document doc = XMLTranser.transWithXsl(sourcedoc,
						"CreateFileDirTreeXML.xsl");
				// 从服务器端返回目标文档
				XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
						.setEncoding("gb2312"));
				try {
					response.setHeader("Pragma", "No-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);

					PrintWriter pw = response.getWriter();
					outp.output(doc, pw);
					// outp.output(doc, System.out);
					// 跳转
					this.setNvlNextScreen(request);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "目录信息失败！");

			}

		} else if ("dpxmltree".equals(operType)) {
			try {
				request.setCharacterEncoding("gb2312");
				response.setContentType("text/xml;charset=gb2312");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
			try {
				// 取sql语句

				// 查询数据存入Vector或数组
				String sql = "SELECT id,parent_id,dir_name,dir_desc FROM UI_SYS_INFO_FILE_DIR where parent_id=0 ORDER BY SORT_NUM,id";
				if (id != null && !"".equals(id)) {
					sql = "SELECT id,parent_id,dir_name,dir_desc FROM UI_SYS_INFO_FILE_DIR where parent_id="
							+ id + " ORDER BY SORT_NUM,id";
				}

				System.out.println("---" + sql);
				Vector result = WebDBUtil.execQryVector(sql, "");
				if (result == null) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "目录信息失败！");

				}

				// 根据sql字符串，查询取得的数据生成源XML文档
				Document sourcedoc = XMLTranser.getDocument(sql, result);
				// 源xml文档结合指定xsl生成目标xml文档
				Document doc = XMLTranser.transWithXsl(sourcedoc,
						"createDpDirTreeXML.xsl");
				// 从服务器端返回目标文档
				XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
						.setEncoding("gb2312"));
				try {
					response.setHeader("Pragma", "No-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);

					PrintWriter pw = response.getWriter();
					outp.output(doc, pw);
					// outp.output(doc, System.out);
					// 跳转
					this.setNvlNextScreen(request);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "目录信息失败！");

			}
		}

	}

}
