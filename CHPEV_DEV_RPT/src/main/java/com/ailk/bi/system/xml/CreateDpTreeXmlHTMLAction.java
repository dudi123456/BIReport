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

public class CreateDpTreeXmlHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 6444873235346062548L;

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
		String dept_parent = request.getParameter("dept_parent");
		// String oper_no=request.getParameter("oper_no");
		String sql = "";
		if (dept_parent != null && !"".equals(dept_parent)) {
			try {
				sql = SQLGenator.genSQL("Q2309", dept_parent);
				System.out.println("Q2309===================" + sql);
			} catch (AppException ex2) {
			}
		} else {
			try {
				sql = SQLGenator.genSQL("Q2308");
				System.out.println("Q2308===============" + sql);
			} catch (AppException ex3) {
				ex3.printStackTrace();
			}
		}
		try {
			// 取sql语句

			// 查询数据存入Vector或数组
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "区域部门用户列表树信息失败！");

			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"createDpTreeXML.xsl");
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
				// this.setForwardFlow(request,false)
			} catch (IOException ex) {
				// //System.out.println(ex.toString());
			}
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "区域部门用户列表树信息失败！");

		}

	}

}
