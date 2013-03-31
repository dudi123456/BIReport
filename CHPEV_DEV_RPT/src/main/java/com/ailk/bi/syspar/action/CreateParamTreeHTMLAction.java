package com.ailk.bi.syspar.action;

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
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes" })
public class CreateParamTreeHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7716249360984371012L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		//
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
			ex1.printStackTrace();
		}
		String parent_id = CommTool.getParameterGB(request, "parent_id");
		if (parent_id == null || "".equals(parent_id)) {
			parent_id = "0";
		}

		String sql = "";
		try {
			sql = SQLGenator.genSQL("QT131", parent_id);
		} catch (AppException ex3) {
			ex3.printStackTrace();
		}

		try {
			// 取sql语句
			System.out.println("sql========CreateParamTreeHTMLAction========="
					+ sql);
			// 查询数据存入Vector或数组
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "提取参数列表树信息失败！");

			}
			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"CreateParamTreeXML.xsl");
			// 从服务器端返回目标文档
			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
					.setEncoding("gb2312"));
			try {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

				PrintWriter pw = response.getWriter();
				outp.output(doc, pw);

				setNvlNextScreen(request);
			} catch (IOException ex) {

			}
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "功能列表树信息失败！");

		}

	}
}
