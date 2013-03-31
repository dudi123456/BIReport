package com.ailk.bi.system.action;

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
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes" })
public class CreateFavoriteTreeHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {

		}
		// 变量申明区
		HttpSession session = request.getSession();
		// 用户ID
		String user_id = CommonFacade.getLoginId(session);
		// 父ID
		String parent_id = request.getParameter("parent_id");
		if (parent_id == null || "".equals(parent_id)) {
			parent_id = "0";
		}
		String sql = "";
		if ("0".equals(parent_id)) {
			try {
				sql = SQLGenator.genSQL("Q4002", "9902004");
				sql += " UNION ";
				sql += SQLGenator.genSQL("Q4000", parent_id, user_id);
				// System.out.println("Q4000===================" + sql);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		} else if ("0001zzz".equals(parent_id)) {
			// 特殊的热点收藏夹
			try {
				sql = SQLGenator.genSQL("Q4002", "0001zzz");
				sql += " UNION ";
				sql += SQLGenator.genSQL("Q4001", user_id, "1");
				// System.out.println("Q4001===================" + sql);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		} else {
			try {
				sql = SQLGenator.genSQL("Q4000", parent_id, user_id);
				// System.out.println("Q4000===============" + sql);
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
						HTMLActionException.WARN_PAGE, "获取收藏夹树信息失败！");
			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"CreateFavoriteTreeXML.xsl");
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
				setNvlNextScreen(request);
			} catch (IOException ex) {
				// System.out.println(ex.toString());
			}
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "获取收藏夹树信息失败！");

		}
	}

}
