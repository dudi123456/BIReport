package com.ailk.bi.base.taglib;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 说明： 分页tag
 * 
 * xuxf
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TagPage extends TagSupport {

	private static final long serialVersionUID = 3382937681512990608L;

	private static final int DEFAULT_PAGE_SIZE = 20;
	public static final String PARAM_PAGE_NO = "reserved_page_no";
	public static final String PARAM_ACTION = "reserved_page_action";
	public static final String RETURN_NAME = "reserved_page_return";

	private String id = null;
	private String pageContextName = null; // session中存储pageContext的名字
	private String sqlName = null; //
	private String pageNoName = null; //

	private String sql = null;
	private String size = null;
	private String uri = null;
	private int pageSize = DEFAULT_PAGE_SIZE; // 页记录数

	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		String[][] result = null;
		int totalSize = 0; // 总记录数
		int totalPage = 0; // 总页数
		int pageNo = 0; // 当前页
		StringBuffer pageNavigator = new StringBuffer(); // 导航栏html

		// 初始化导出变量
		pageContext.setAttribute("_totalSize", new Integer(0));
		pageContext.setAttribute("_pageSize", new Integer(pageSize));
		pageContext.setAttribute("_totalPage", new Integer(0));
		pageContext.setAttribute("_pageNo", new Integer(0));
		pageContext.setAttribute("_pageResult", new String[1][1]);
		pageContext.setAttribute("_pageNavigator", "");

		// 取参数
		String sReturn = (String) pageContext.getRequest().getAttribute(
				RETURN_NAME);
		if (sReturn == null) {
			sReturn = (String) pageContext.getRequest().getParameter(
					RETURN_NAME);
		}
		boolean bReturn = (sReturn != null) && (sReturn.equals("true")); // 是否从其它页面返回

		String lastPageNo = (String) pageContext.getRequest().getParameter(
				PARAM_PAGE_NO); // 上一页
		String action = (String) pageContext.getRequest().getParameter(
				PARAM_ACTION); // 操作

		// 操作参数表明用户请求了翻页操作，或从其它页面返回需要恢复场景
		if (action != null || bReturn) {
			SimplePageContext spc = (SimplePageContext) pageContext
					.getSession().getAttribute(pageContextName);

			if (spc != null) {
				spc.restoreTo(pageContext);
				sql = (String) pageContext.getSession().getAttribute(sqlName);

				if (sql == null) {
					return EVAL_PAGE;
				}

			} else {
				return EVAL_PAGE;
			}
		} else {
			// 否则需要记录场景、sql
			pageContext.getSession().setAttribute(pageContextName,
					new SimplePageContext(pageContext));
			pageContext.getSession().setAttribute(sqlName, sql);
		}

		if (sql == null || sql.equals("")) {
			return EVAL_PAGE;
		}

		// 查询总记录数
		String countSql = "select count(*) from (" + sql + ")";

		try {
			result = WebDBUtil.execQryArray(countSql, "");

			if (result == null || result.length == 0) {
				return EVAL_PAGE;
			}

			totalSize = Integer.parseInt(result[0][0]);
			if (totalSize == 0) {
				return EVAL_PAGE;
			}

		} catch (AppException e2) {
			return EVAL_PAGE;
		}

		// 计算总页数
		totalPage = totalSize / pageSize;
		if (totalSize % pageSize != 0) {
			totalPage++;
		}

		// 计算当前页
		if (bReturn) {
			Integer integer = (Integer) pageContext.getSession().getAttribute(
					pageNoName);
			if (integer == null) {
				if (totalPage > 0) {
					integer = new Integer(1);
				} else {
					return EVAL_PAGE;
				}
			}

			pageNo = integer.intValue();
			if (pageNo < 1) {
				return EVAL_PAGE;
			}

		} else {
			if (action != null) {
				if (action.equals("first")) {
					pageNo = 1;
				} else if (action.equals("last")) {
					pageNo = totalPage;
				} else if (action.equals("prev")) {
					pageNo = Integer.parseInt(lastPageNo) - 1;
					if (pageNo < 1) {
						pageNo = totalPage;
					}
				} else if (action.equals("next")) {
					pageNo = Integer.parseInt(lastPageNo) + 1;
					if (pageNo > totalPage) {
						pageNo = 1;
					}
				} else {
					return EVAL_PAGE;
				}

			} else {
				if (lastPageNo != null) {
					pageNo = Integer.parseInt(lastPageNo);
					pageNo = pageNo > 0 ? pageNo : 1;
				} else {
					pageNo = 1;
				}
			}

			// 保留当前页
			pageContext.getSession().setAttribute(pageNoName,
					new Integer(pageNo));
		}

		// 查询当前页记录
		String parsedSql = null;

		parsedSql = "select temp_table2.* from (select temp_table1.*, rownum r from ("
				+ sql
				+ " ) temp_table1 ) temp_table2  where r between "
				+ ((pageNo - 1) * pageSize + 1) + " and " + (pageNo * pageSize);

		try {
			result = WebDBUtil.execQryArray(parsedSql, "");

		} catch (AppException e) {
			e.printStackTrace();
			return EVAL_PAGE;
		}

		// 设置导出变量
		pageContext.setAttribute("_totalSize", new Integer(totalSize));
		pageContext.setAttribute("_pageSize", new Integer(pageSize));
		pageContext.setAttribute("_totalPage", new Integer(totalPage));
		pageContext.setAttribute("_pageNo", new Integer(pageNo));
		pageContext.setAttribute("_pageResult", result);

		// 分页导航
		// HttpServletRequest request =
		// (HttpServletRequest)pageContext.getRequest();
		// String uri = request.getRequestURL() == null ? "" :
		// request.getRequestURL().toString();

		int startPage = (pageNo - 10 > 0) ? pageNo - 10 : 1;
		int endPage = (pageNo + 10 > totalPage) ? totalPage : pageNo + 9;
		String symbol = uri.indexOf("?") == -1 ? "?" : "&";

		if (pageNo > 1) {
			pageNavigator.append("<a href='").append(uri).append(symbol)
					.append(PARAM_ACTION).append("=first'>[首页]</a>");
			pageNavigator.append("<a href='").append(uri).append(symbol)
					.append(PARAM_PAGE_NO).append("=").append(pageNo)
					.append(symbol).append(PARAM_ACTION)
					.append("=prev'>[上一页]</a>");
		}

		for (int i = startPage; i <= endPage; i++) {
			pageNavigator.append("<a href='").append(uri).append(symbol)
					.append(PARAM_PAGE_NO).append("=").append(i)
					.append("'>[" + i + "]</a>");
		}

		if (pageNo < totalPage) {
			pageNavigator.append("<a href='").append(uri).append(symbol)
					.append(PARAM_PAGE_NO).append("=").append(pageNo)
					.append("&").append(PARAM_ACTION)
					.append("=next'>[下一页]</a>");

			pageNavigator.append("<a href='").append(uri).append(symbol)
					.append(PARAM_ACTION).append("=last'>[末页]</a>");
		}

		pageContext.setAttribute("_pageNavigator", pageNavigator.toString());

		return EVAL_PAGE;
	}

	/**
	 * @param string
	 */
	public void setSize(String string) {
		size = string;

		if (size != null) {
			pageSize = Integer.parseInt(size);
			if (pageSize <= 0) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
		}
	}

	/**
	 * @param string
	 */
	public void setSql(String string) {
		sql = string;
	}

	/**
	 * @param string
	 */
	public void setUri(String str) {
		uri = str;
	}

	class SimplePageContext implements java.io.Serializable {

		private static final long serialVersionUID = 6089605633080737764L;

		PageContext pageContext = null;

		String[] requestAttributeNames;
		Object[] requestAttributeValues;

		SimplePageContext(PageContext context) {
			setPageContext(context);
		}

		void setPageContext(PageContext context) {
			this.pageContext = context;
			Enumeration attrEnum = context.getRequest().getAttributeNames();
			Vector v = new Vector();
			while (attrEnum.hasMoreElements()) {
				String aName = (String) attrEnum.nextElement();
				if (aName != null && aName.indexOf("reserved") == -1) {
					v.add(aName);
				}
			}

			if (v.size() > 0) {
				requestAttributeNames = new String[v.size()];
				v.copyInto(requestAttributeNames);
			}

			if (requestAttributeNames != null) {
				requestAttributeValues = new Object[requestAttributeNames.length];
				for (int i = 0; i < requestAttributeNames.length; i++) {
					requestAttributeValues[i] = context.getRequest()
							.getAttribute(requestAttributeNames[i]);
				}
			}

		}

		void restoreTo(PageContext context) {
			if (requestAttributeNames != null) {
				for (int i = 0; i < requestAttributeNames.length; i++) {
					context.getRequest().setAttribute(requestAttributeNames[i],
							requestAttributeValues[i]);
				}
			}
		}

	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
		pageContextName = "reserved_page_page_context" + id;
		sqlName = "reserved_page_sql" + id;
		pageNoName = "reserved_page_page_no" + id;
	}

}
