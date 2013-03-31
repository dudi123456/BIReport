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

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class CreateLNewRoleUserXmlHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = -7056875223207134595L;

	@SuppressWarnings({ "rawtypes" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		//
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
		}
		HttpSession session = request.getSession();

		String role_code = request.getParameter("role_code");
		String parent_id = request.getParameter("parent_id");
		String codeStr[][] = null;
		String pathCodeStr = "";
		try {
			// 取sql语句
			String sqlA = SQLGenator.genSQL("Q1033", role_code);
			System.out.println("Q1033======" + sqlA);
			codeStr = WebDBUtil.execQryArray(sqlA, "");
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "角色选择用户左侧区域列表树信息失败！");

		}
		if (codeStr == null || codeStr.length <= 0) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该角色没有设置相应的相关区域,操作终止！");

		}
		for (int i = 0; codeStr != null && i < codeStr.length; i++) {
			pathCodeStr += "'" + codeStr[i][0] + "',";
		}
		if (pathCodeStr.length() > 0) {
			pathCodeStr = pathCodeStr.substring(0, pathCodeStr.length() - 1);
		}
		//
		try {
			String sql = SQLGenator.genSQL("Q1034", role_code, parent_id,
					role_code, role_code, parent_id, role_code, pathCodeStr);
			System.out.println("Q1034======" + sql);
			// 查询数据存入Vector或数组
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "角色选择用户左侧部门列表树信息失败！");

			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"createLNewRoleUserXML.xsl");
			// 从服务器端返回目标文档
			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
					.setEncoding("gb2312"));
			try {
				// response.flushBuffer();
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

				response.resetBuffer();
				PrintWriter pw = response.getWriter();
				outp.output(doc, pw);
				// outp.output(doc, System.out);
				// 跳转
				this.setNvlNextScreen(request);
			} catch (IOException ex) {
				// //System.out.println(ex.toString());
			}

		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "角色选择用户左侧区域列表树信息失败！");

		}
	}

}
