package com.ailk.bi.system.xml;

import waf.controller.web.action.*;
import javax.servlet.http.*;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
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

public class CreateUserGroupXmlHTMLAction extends HTMLActionSupport {
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

		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		// 判断参数
		// String group_id = CommTool.getParameterGB(request, "group_id");

		String oper_type = CommTool.getParameterGB(request, "oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "0";
		}

		String sql = "";
		try {

			if ("0".equals(oper_type)) {
				// 取sql语句 角色列表页面
				sql = "select 0 as catid, code_id, code_name,code_seq from ui_code_list where type_code = 'group_type'";
			} else if ("1".equals(oper_type)) {
				String group_type = CommTool.getParameterGB(request,
						"group_type");
				sql = SQLGenator.genSQL("A0004", user.user_id, group_type);
				System.out.println("A0004=================" + sql);
			}

			System.out.println("userGroup==" + sql);

			// 查询数据存入Vector或数组
			if (!"".equals(sql)) {
				Vector result = WebDBUtil.execQryVector(sql, "");
				if (result == null) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "查询角色信息失败！");

				}

				// 根据sql字符串，查询取得的数据生成源XML文档
				Document sourcedoc = XMLTranser.getDocument(sql, result);
				// 源xml文档结合指定xsl生成目标xml文档
				Document doc = XMLTranser.transWithXsl(sourcedoc,
						"CreateUserGroupXML.xsl");
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
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "查询登陆角色信息失败！");

		}
	}
}