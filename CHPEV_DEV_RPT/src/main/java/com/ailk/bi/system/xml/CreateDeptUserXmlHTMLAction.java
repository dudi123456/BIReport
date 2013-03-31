package com.ailk.bi.system.xml;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import org.jdom.*;
import org.jdom.output.*;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public class CreateDeptUserXmlHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 设置响应返回编码和文件类型
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/xml;charset=utf-8");
		} catch (UnsupportedEncodingException ex1) {
		}
		// 取得会话
		HttpSession session = request.getSession();
		// 取得传递参数
		String regionParent = request.getParameter("region_parent");
		String dept_parent = request.getParameter("dept_parent");
		String first = request.getParameter("first");
		// 根据参数具体提取具体SQL语句
		String sql = "";
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (dept_parent != null && !"".equals(dept_parent)) {
			// 区域部门用户列表树--点击部门展开
			try {
				sql = SQLGenator.genSQL("A1000", regionParent, dept_parent,
						user.system_id);
			} catch (AppException ex2) {
				ex2.printStackTrace();
			}
		} else if (regionParent != null && !"".equals(regionParent)) {
			try {
				// 区域部门用户列表树--点击区域展开
				String areaStr = "";
				if ("1".equals(first)) {
					areaStr = "region_id in (" + regionParent + ")";
					String regionStr = "'0'";
					sql = SQLGenator.genSQL("A1001", areaStr, regionStr, regionStr,
							regionStr, user.system_id);
				} else {
					areaStr = "parent_id in ('" + regionParent + "')";
					String regionStr = "'"+regionParent+"'";
					sql = SQLGenator.genSQL("A1001", areaStr, regionStr,
							regionStr, regionStr, user.system_id);
				}

				// sql = SQLGenator.genSQL("A1001", areaStr,
				// regionParent,regionParent, regionParent);
			} catch (AppException ex3) {
				ex3.printStackTrace();
			}
		}
		try {
			System.out
					.println("CreateDeptUserXmlHTMLAction=============" + sql);
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
					"createDeptUserXML.xsl");
			// 从服务器端返回目标文档
			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()
					.setEncoding("utf-8"));
			// 设置相应头
			try {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

				PrintWriter pw = response.getWriter();
				outp.output(doc, pw);
				// 跳转为空
				this.setNvlNextScreen(request);
			} catch (IOException ex) {
			}
		} catch (AppException e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "区域部门用户列表树信息失败！");

		}

	}

}
