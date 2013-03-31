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

public class CreateRegionRoleXmlHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 6622307100306914565L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		//
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
		}
		HttpSession session = request.getSession();
		//
		// InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String first = request.getParameter("first");
		String regionParent = request.getParameter("region_id");
		// 根据参数具体提取具体SQL语句
		String sql = "";

		if (regionParent != null && !"".equals(regionParent)) {
			try {
				// 区域部门用户列表树--点击区域展开
				sql = SQLGenator.genSQL("A1002", regionParent);
			} catch (AppException ex3) {
				ex3.printStackTrace();
			}
		} else {
			try {
				// 区域部门用户列表树--点击区域展开
				sql = SQLGenator.genSQL("A1003", first);
			} catch (AppException ex3) {
				ex3.printStackTrace();
			}
		}

		try {
			// 取sql语句
			System.out.println("sql==" + sql);
			// 查询数据存入Vector或数组
			@SuppressWarnings({ "rawtypes" })
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "菜单树信息失败！");

			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"CreateRegionRoleXML.xsl");
			// 从服务器端返回目标文档
			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
					.setEncoding("gb2312"));
			try {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

				PrintWriter pw = response.getWriter();
				outp.output(doc, pw);
				this.setNvlNextScreen(request);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "区域部门用户列表树信息失败！");

		}

	}

}
