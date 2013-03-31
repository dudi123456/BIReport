package com.ailk.bi.common.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppWebUtil;

/**
 * 通用重复提交的校验
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author
 * @version 1.0 在form内使用格式为：
 * 
 *          <BIBM:RptCommitTag />
 */

public class RptCommit extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -584485034867297960L;

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			HttpSession session = pageContext.getSession();
			if (session != null) {
				String tokenStr = AppWebUtil.genToken();
				session.setAttribute(AppConst.TOKEN_STRING, tokenStr);
				out.print("<input type=\"hidden\" name=\""
						+ AppConst.TOKEN_STRING + "\" value=\"" + tokenStr
						+ "\">");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	public void release() {

	}

}