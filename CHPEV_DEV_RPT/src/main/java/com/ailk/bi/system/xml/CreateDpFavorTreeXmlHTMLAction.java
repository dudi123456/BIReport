package com.ailk.bi.system.xml;

import waf.controller.web.action.*;
import javax.servlet.http.*;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import java.util.*;
import java.io.*;
import org.jdom.*;
import org.jdom.output.*;
import waf.controller.web.action.HTMLActionSupport;

/**
 * <p>
 * Title: LongShine System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author xh
 * @version 1.0
 */

public class CreateDpFavorTreeXmlHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = -5842994709901239982L;

	//
	@SuppressWarnings({ "rawtypes" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
			ex1.toString();
		}

		HttpSession session = request.getSession();

		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		// 收藏夹
		String favor_id = request.getParameter("dept_parent");

		String sql = "";
		if (favor_id != null && !"".equals(favor_id)) {
			try {
				sql = SQLGenator.genSQL("fav013", loginUser.user_id, favor_id);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		} else {
			try {
				sql = SQLGenator.genSQL("fav012", loginUser.user_id);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		}

		try {

			// 查询数据存入Vector或数组
			Vector result = WebDBUtil.execQryVector(sql, "");
			if (result == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "查询收藏夹信息失败！");

			}

			// 根据sql字符串，查询取得的数据生成源XML文档
			Document sourcedoc = XMLTranser.getDocument(sql, result);
			// 源xml文档结合指定xsl生成目标xml文档。注意xsl大小写。
			Document doc = XMLTranser.transWithXsl(sourcedoc,
					"createDpFavorTreeXML.xsl");
			// 从服务器端返回目标文档
			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
					.setEncoding("gb2312"));

			try {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

				PrintWriter pw = response.getWriter();

				outp.output(doc, pw);
				// 跳转为空
				this.setNvlNextScreen(request);
			} catch (IOException ex) {
				// System.out.println(ex.toString());
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "查询收藏夹信息失败！");

		}
	}
}