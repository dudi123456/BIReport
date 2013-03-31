package com.ailk.bi.base.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.content.service.ICustContentHTML;
import com.ailk.bi.content.service.impl.CustContentHTML;
import com.ailk.bi.report.struct.ReportQryStruct;

public class TagContent extends BodyTagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String contentId = null;// 文字模板标识

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public int doEndTag() throws JspException {
		String outStr = "";

		try {
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			HttpSession session = pageContext.getSession();
			if (session != null) {
				ICustContentHTML content = new CustContentHTML();

				ReportQryStruct qryStruct = (ReportQryStruct) session
						.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
				String whereSql = CommConditionUtil.getPubWhere(contentId,
						request, qryStruct);

				String whereSqlnoDate = "";
				String[] ruleIdArr = whereSql.split(" AND ");
				for (int i = 0; ruleIdArr != null && i < ruleIdArr.length; i++) {
					String tmpValue = ruleIdArr[i].toLowerCase();
					if (tmpValue.indexOf("gather_day ") >= 0) {
						continue;
					}
					if (tmpValue.indexOf("gather_month ") >= 0) {
						continue;
					}
					if (!StringTool.checkEmptyString(tmpValue.trim())) {
						whereSqlnoDate += " AND " + ruleIdArr[i];
					}
				}

				outStr = content.getContentDesc(contentId, whereSql,
						whereSqlnoDate, qryStruct);

				out.print(outStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}
}
