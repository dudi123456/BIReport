package com.ailk.bi.subject.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.pages.*;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableHTMLStruct;
import com.ailk.bi.subject.domain.TableRowStruct;
import com.ailk.bi.subject.service.ITableManager;
import com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO;
import com.ailk.bi.subject.service.dao.ITableRowStructDAO;
import com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO;
import com.ailk.bi.subject.service.dao.impl.TableRowStructDAO;
import com.ailk.bi.subject.service.impl.TableManager;
import com.ailk.bi.subject.util.SubjectCacheUtil;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectSortUtil;
import com.ailk.bi.subject.util.SubjectStringUtil;

/**
 * @author xdou 专题通用模版表格部分 支持常规和AJAX模式
 *         AJAX模式需要附加SubjectConst.REQ_AJAX_REQUEST请求参数
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectCommontablePageOrderAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * waf.controller.web.action.HTMLActionSupport#doTrans(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		// 开始时间
		long startTime = System.currentTimeMillis();
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("专题通用表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("通用表格分析－请求或回应对象为空");
		}
		HttpSession session = request.getSession();

		// 获取回应输出
		response.setContentType("text/html; charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException ioe) {
			log.error("通用表格分析－无法生成回应输出对象", ioe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}

		// 获取用户的请求,表格标识
		String tableId = request.getParameter(SubjectConst.REQ_TABLE_ID);
		SubjectConst.TABLE_ACTION_DOT_DO = "SubjectCommTable.rptdo";
		if (null == tableId || "".equals(tableId)) {
			log.error("通用表格分析－无法获取表格标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		// 判断一下是否第一次加载
		String firstAccess = request
				.getParameter(SubjectConst.REQ_FIRST_ACCESS);
		if (null != firstAccess) {
			// 清空以前的缓存
			SubjectCacheUtil.clearWebCache(session, tableId);
		}
		// 判断是否是AJAX请求
		String ajaxRequest = request
				.getParameter(SubjectConst.REQ_AJAX_REQUEST);
		boolean hasAjaxRequest = false;
		if (null != ajaxRequest && !"".equals(ajaxRequest))
			hasAjaxRequest = true;
		try {
			// 声名服务
			ITableManager tableManager = new TableManager();
			// 开始获取表格定义对象,看session 中有没有，没有生成一个
			SubjectCommTabDef subTable = null;
			Object tmpObj = session
					.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ + "_"
							+ tableId);
			if (null != tmpObj) {
				subTable = (SubjectCommTabDef) tmpObj;
			} else {
				subTable = tableManager.genTableDomain(tableId, request);
			}
			subTable = tableManager.parseRequestToTableColStruct(subTable,
					request);
			// 生成当前功能对象
			String preFunc = null;
			TableCurFunc curFunc = null;
			tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ
					+ "_" + tableId);
			if (null != tmpObj) {
				curFunc = (TableCurFunc) tmpObj;
			} else {
				curFunc = tableManager.genCurFuncDomain(subTable);
			}
			preFunc = curFunc.curUserFunc;
			// 开始将用户的状态分析到这两个对象
			try {
				curFunc = tableManager.parseRequestToCurFunc(curFunc, subTable,
						request);
			} catch (AppException e) {
				e.printStackTrace();
				log.error(e);
			}
			String[][] svces = null;
			List body = null;
			// 对于排序请求单独处理
			if (SubjectConst.TABLE_FUNC_SORT.equals(curFunc.curUserFunc)) {

				tmpObj = session
						.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_LIST
								+ "_" + tableId);
				if (null != tmpObj) {
					body = (List) tmpObj;
				}
				// 这里设置不能有行合并了
				curFunc.hasDimNotAsWhere = false;
				if (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand)) {
					// 需要对数组排序
					tmpObj = session
							.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
									+ "_" + tableId);
					if (null != tmpObj) {
						svces = (String[][]) tmpObj;
						svces = SubjectSortUtil.sortTableContent(subTable,
								curFunc, svces, request);
					}
				} else {
				}
				// 这时需要回复以前的状态,比如全展开
				if (null != preFunc) {
					curFunc.curUserFunc = preFunc;
				}
			}
			// 生成HTML输出
			TableHTMLStruct tableHTML = tableManager.getTableHTML(subTable,
					curFunc, body, svces);
			String[] export = tableHTML.export;
			String[] html = tableHTML.html;
			if (null != subTable)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
						+ "_" + tableId, subTable);
			if (null != curFunc)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ + "_"
						+ tableId, curFunc);
			if (null != html && 0 < html.length)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_HTML + "_"
						+ tableId, html);
			if (null != export)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT + "_"
						+ tableId, export);
			if (null != body && 0 <= body.size())
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_LIST
						+ "_" + tableId, body);
			if (null != svces && 0 <= svces.length)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
						+ "_" + tableId, svces);

			if (!hasAjaxRequest) {
				if (null != subTable
						&& SubjectConst.NO
								.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES
								.equalsIgnoreCase(subTable.has_paging)) {
					// 有分页功能这里，这里重新组织
					// 生成

					tmpObj = session
							.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
									+ "_" + tableId);
					if (null != tmpObj) {
						svces = (String[][]) tmpObj;
					}
					if (null != svces) {
						out.println("<form name=\"frmTableContent\" target=\"_self\">");
						out.println("<input type=\"hidden\" name=\"table_id\" value=\""
								+ tableId + "\"/>");
						// 获取翻页相关信息
						PagesInfoStruct pageInfo = null;
						pageInfo = WebPageTool.getPageInfo(request,
								svces.length - 1, SubjectConst.ROWS_PER_PAGE);
						out.println(WebPageTool.pageHidden(pageInfo));
						// 输出导航条
						// out.println(WebPageTool.pagePolit(pageInfo));
						// 开始组装表头
						out.println("<table width='100%' border='0' cellpadding='0' cellspacing='0' "
								+ ">\n");
						out.println("<tr><td class=\"side-left\">");
						out.println("<table width='100%' border='0' height=\""
								+ curFunc.tableHeight + "\" "
								+ "cellpadding='0' cellspacing='0' "
								+ "id=\"iTable_TableContainer\">\n");
						ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
						List head = tableHeadDao.getTableHead(subTable,
								curFunc, svces);
						if (null != head) {
							Iterator iter = head.iterator();
							while (iter.hasNext()) {
								out.println(iter.next());
							}
						}
						body = new ArrayList();
						List left = new ArrayList();
						List right = new ArrayList();
						ITableRowStructDAO tableRowDAO = new TableRowStructDAO();
						for (int i = 0; i < pageInfo.iLinesPerPage
								&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
							int index = i + pageInfo.absRowNoCurPage();
							TableRowStruct row = tableRowDAO.genTableRowStruct(
									subTable, curFunc, svces[index],
									svces[svces.length - 1], false);
							left.add(row.leftHTML.toString());
							right.add(row.rightHTML.toString());
						}

						body.add(SubjectStringUtil.genFixedBodyLeftPart1());
						body.addAll(left);
						// 在这要加上一行多余的行,为了对齐高度
						body.add("<tr >");
						List tabCols = subTable.tableCols;
						Iterator iter = tabCols.iterator();
						while (iter.hasNext()) {
							SubjectCommTabCol tabCol = (SubjectCommTabCol) iter
									.next();
							if (SubjectConst.YES
									.equalsIgnoreCase(tabCol.default_display)
									&& SubjectConst.NO
											.equalsIgnoreCase(tabCol.is_measure))
								body.add("<td class=\"table-td-withbg\" >&nbsp;</td>");
						}
						body.add("</tr>");
						body.add(SubjectStringUtil.genFixedBodyLeftPart2());
						body.add(SubjectStringUtil.genFixedBodyRightPart1());
						body.addAll(right);
						body.add(SubjectStringUtil.genFixedBodyRightPart2());
						// 开始输出
						boolean isOdd = true;
						String trStyle = curFunc.tabEvenTRClass;
						String tdStyle = curFunc.tabTDEvenClass;
						iter = body.iterator();
						while (iter.hasNext()) {
							String temp = (String) iter.next();
							if (temp.indexOf(SubjectConst.TR_CLASS_REPLACE) >= 0) {
								isOdd = !isOdd;
							} else {
								isOdd = true;
							}
							if (isOdd) {
								trStyle = curFunc.tabOddTRClass;
								tdStyle = curFunc.tabTDOddClass;
							} else {
								trStyle = curFunc.tabEvenTRClass;
								tdStyle = curFunc.tabTDEvenClass;
							}
							temp = temp.replaceAll(
									SubjectConst.TR_CLASS_REPLACE, trStyle);
							temp = temp.replaceAll(
									SubjectConst.TD_CLASS_REPLACE, tdStyle);
							out.println(temp);
						}
						out.println("</table>\n</td></tr></table>");
						out.println(WebPageTool.pagePolit(pageInfo));
						out.println("</form>");
						out.flush();
					}
				} else {
				}
				// 取出session 中的值

			}
			// 存入session

		} catch (SubjectException e) {
			e.printStackTrace();
			log.error(e);
		}
		logcommon.debug("通用表格总用时：" + (System.currentTimeMillis() - startTime)
				+ "ms");
		request.setAttribute(SubjectConst.REQ_TABLE_ID, tableId);
		String needAlginTable = request.getParameter("align_table");
		if (null != needAlginTable)
			request.setAttribute("align_table", needAlginTable);
		setNvlNextScreen(request);
	}
}
