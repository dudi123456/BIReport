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

import com.ailk.bi.base.table.InfoOperTable;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class CreateMenuTreeXmlHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2004927904622198469L;

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
		//
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);

		// 菜单
		String menu_code = request.getParameter("menu_code");
		//
		String sql = "";
		System.out.println("---zxj------");
		if (menu_code != null && !"".equals(menu_code)) {
			try {
				sql = SQLGenator.genSQL("Q2935", loginUser.system_id,
						loginUser.user_id, loginUser.user_id, menu_code);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		} else {
			try {

				sql = SQLGenator.genSQL("Q2936", loginUser.system_id,
						loginUser.user_id, loginUser.user_id);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		}

		try {
			// 取sql语句

			// 查询数据存入Vector或数组
			System.out.println("---" + sql);
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "菜单树信息失败！");

			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"CreateMenuTreeXML.xsl");
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
					HTMLActionException.WARN_PAGE, "区域部门用户列表树信息失败！");

		}

	}
}
