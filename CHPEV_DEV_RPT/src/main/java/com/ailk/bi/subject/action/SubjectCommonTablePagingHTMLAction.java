package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.service.ITableManager;
import com.ailk.bi.subject.service.impl.TableManager;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectSortUtil;

/**
 * @author xdou 翻页响应 AJAX模式需要附加SubjectConst.REQ_AJAX_REQUEST请求参数
 */

public class SubjectCommonTablePagingHTMLAction extends HTMLActionSupport {

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
	public void doTrans(HttpServletRequest request, HttpServletResponse response) throws HTMLActionException {

		// 开始时间
		long startTime = System.currentTimeMillis();
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("专题通用表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("通用表格分析－请求或回应对象为空");
		}
		HttpSession session = request.getSession();
		UserCtlRegionStruct userRight = null;
		Object obj = session.getAttribute("ATTR_C_UserCtlStruct");
		// 获取权限
		if (null != obj)
			userRight = (UserCtlRegionStruct) obj;

		String with_bar = request.getParameter("with_bar");
		// 获取用户的请求,表格标识
		String tableId = request.getParameter(SubjectConst.REQ_TABLE_ID);
		SubjectConst.TABLE_ACTION_DOT_DO = "SubjectCommTable.rptdo";
		if (null == tableId || "".equals(tableId)) {
			log.error("通用表格分析－无法获取表格标识");
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。"
					+ "如果此问题依然存在，请联系系统管理员，" + "由此造成对您的工作不便，深表歉意！");
		}
		String page_size = request.getParameter(SubjectConst.REQ_TABLE_PAGESIZE);
		if (null == page_size || "".equals(page_size)) {
			page_size = Integer.toString(SubjectConst.ROWS_PER_PAGE);
		}
		// 获取当前页书
		String stPageNo = request.getParameter("page__iCurPage");

		try {
			// 声名服务
			ITableManager tableManager = new TableManager();
			// 开始获取表格定义对象,看session 中有没有，没有生成一个
			SubjectCommTabDef subTable = null;
			Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ + "_" + tableId);
			if (null != tmpObj) {
				subTable = (SubjectCommTabDef) tmpObj;
			} else {
				subTable = tableManager.genTableDomain(tableId, request);
			}
			subTable = tableManager.parseRequestToTableColStruct(subTable, request);
			// 设置用户权限
			subTable.userRight = userRight;

			if (!StringUtils.isBlank(subTable.page_rows)) {
				page_size = subTable.page_rows;
			}
			// 生成当前功能对象
			TableCurFunc curFunc = null;
			tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ + "_" + tableId);
			if (null != tmpObj) {
				curFunc = (TableCurFunc) tmpObj;
			} else {
				curFunc = tableManager.genCurFuncDomain(subTable);
			}
			try {
				int pageNo = Integer.parseInt(stPageNo);
				curFunc.pageNum = pageNo;
			} catch (NumberFormatException e) {

			}
			if (null != with_bar && SubjectConst.NO.equalsIgnoreCase(with_bar)) {
				curFunc.withBar = false;
			}
			String[][] svces = null;
			// 对于排序请求单独处理
			// 获取数据库数据
			svces = tableManager.getTableContent(subTable, curFunc);
			// 这里排序
			svces = SubjectSortUtil.sortDeafaultTableContent(subTable, curFunc, svces);

			// 存入session
			if (null != subTable)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ + "_" + tableId, subTable);
			if (null != curFunc)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ + "_" + tableId, curFunc);
			if (null != svces && 0 <= svces.length)
				session.setAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES + "_" + tableId, svces);
		} catch (SubjectException e) {
			e.printStackTrace();
			log.error(e);
		}

		logcommon.debug("通用表格总用时：" + (System.currentTimeMillis() - startTime) + "ms");
		request.setAttribute(SubjectConst.REQ_TABLE_ID, tableId);
		request.setAttribute(SubjectConst.REQ_TABLE_PAGESIZE, page_size);
		String needAlginTable = request.getParameter("align_table");
		if (null != needAlginTable)
			request.setAttribute("align_table", needAlginTable);
		String setParentHeight=request.getParameter("setParentHight");
		if(null!=setParentHeight)
			request.setAttribute("setParentHight", setParentHeight);
		request.setAttribute("with_bar", with_bar);
		setNextScreen(request, "SubjectCommTable.screen");
	}
}
