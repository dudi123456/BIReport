package com.ailk.bi.metamanage.action;

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

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.util.XMLTranser;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "serial" })
public class CreateMsuTreeXmlAction extends HTMLActionSupport {

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		//
		try {
			request.setCharacterEncoding("gb2312");

			response.setContentType("text/xml;charset=gb2312");
		} catch (UnsupportedEncodingException ex1) {
		}
		HttpSession session = request.getSession();

		String msu_type_id = request.getParameter("msu_type_id");

		String sql = "";

		if (msu_type_id != null && !"".equals(msu_type_id)) {
			sql = "select 1 AS catid,msu_type_id,msu_type_name,'' as msu_id,'' as msu_name from UI_META_INFO_MEASURE_TYPE where parent_type_id='"
					+ msu_type_id
					+ "' union "
					+ "select 2 AS catid,'' as msu_type_id,'' as msu_type_name,msu_id,msu_name from UI_META_INFO_MEASURE where msu_type_id ='"
					+ msu_type_id + "'";
		} else {
			sql = "select 1 AS catid,msu_type_id,msu_type_name from UI_META_INFO_MEASURE_TYPE where parent_type_id='-1'";
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
					"createMsuTreeXML.xsl");

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
