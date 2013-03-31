package com.ailk.bi.olap.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.IReportManager;
import com.ailk.bi.olap.service.ITableManager;
import com.ailk.bi.olap.service.impl.ReportManager;
import com.ailk.bi.olap.service.impl.TableManager;
import com.ailk.bi.olap.util.HttpSessionUtil;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDateUtil;
import com.ailk.bi.olap.util.RptOlapFuncUtil;
import com.ailk.bi.olap.util.RptOlapTabDomainUtil;
import com.ailk.bi.olap.util.TabColComparator;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportOlapTableDigHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1498304273130066675L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		long startTime = System.currentTimeMillis();
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		}
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=GB2312");
		InfoOperTable userTable = CommonFacade.getLoginUser(session);
		String userId = userTable.oper_no;
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException ioe) {
			log.error("分析型报表表格分析－无法生成回应输出对象", ioe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法生成回应输出对象！");
		}
		// 获取用户的请求
		String reportId = request.getParameter("report_id");
		if (null == reportId || "".equals(reportId)) {
			log.error("分析型报表表格分析－无法获取报表标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表标识！");
		}
		// 是否单击了指标钻取
		boolean hasNewContent = false;
		String collapseMsu = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU);
		if (null != collapseMsu && !"".equals(collapseMsu)) {
			hasNewContent = true;
		}
		String clickDim = request.getParameter(RptOlapConsts.REQ_CLICK_DIM);
		if (null != clickDim && !"".equals(clickDim)) {
			hasNewContent = true;
		}
		String collapseDimInit = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_DIM_INIT);
		if (null != collapseDimInit && !"".equals(collapseDimInit)) {
			hasNewContent = true;
		}
		String collapseDim = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_DIM);
		if (null != collapseDim && !"".equals(collapseDim)) {
			hasNewContent = true;
		}
		String clickMsu = request.getParameter(RptOlapConsts.REQ_CLICK_MSU);
		if (null != clickMsu && !"".equals(clickMsu)) {
			hasNewContent = true;
		}
		String clickMsuId = request
				.getParameter(RptOlapConsts.REQ_CLICK_MSU_ID);
		if (null != clickMsuId && !"".equals(clickMsuId)) {
			hasNewContent = true;
		}
		String hasContent = request
				.getParameter(RptOlapConsts.REQ_HAS_NEW_CONTENT);
		if (null != hasContent && !"".equals(hasContent)) {
			hasNewContent = true;
		}
		// 清除缓存
		String clearSession = request
				.getParameter(RptOlapConsts.OLAP_CLEAR_SESSION);
		if (null != clearSession && RptOlapConsts.TRUE.equals(clearSession))
			HttpSessionUtil.clearCacheObj(session, reportId);
		// 获取报表信息对象
		PubInfoResourceTable report = null;
		Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			IReportManager reportManger = new ReportManager();
			try {
				report = reportManger.getReport(reportId);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－无法获取报表对象", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"无法获取报表对象！");
			}
		} else {
			report = (PubInfoResourceTable) tmpObj;
		}

		// 要看看rpeort对象的关键属性是否有值
		if (null == report || "".equals(report.name) || "".equals(report.cycle)
				|| "".equals(report.data_table)) {
			log.error("分析型报表表格分析－报表对象的关键信息为空");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"报表对象的关键信息为空！");
		}
		// 获取数据权限对象
		UserCtlRegionStruct userCtl = null;
		tmpObj = session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (null == tmpObj) {
			log.error("分析型报表表格分析－用户数据权限对象为空");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"用户数据权限对象为空！");
		} else {
			userCtl = (UserCtlRegionStruct) tmpObj;
		}
		// userCtl = null;
		// 获取业务类型权限
		String svckndRight = "";// CommTool.getSvcRights(session).toLowerCase();
		ITableManager tableManager = new TableManager();
		// 此处须取出用户是否进行了定制保存
		// 获取定制报表定义
		RptUserOlapTable userCustom = tableManager.getUserCustomReport(userId,
				reportId);
		List userCustomDims = null;
		List userCustomMsus = null;
		if (null != userCustom && !StringUtils.isBlank(userCustom.custom_rptid)) {
			// 获取维度定义
			userCustomDims = tableManager
					.getUserCustomDims(userCustom.custom_rptid);
			// 获取指标定义
			userCustomMsus = tableManager
					.getUserCustomMsus(userCustom.custom_rptid);
		}
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_PRE + "_"
				+ reportId);
		if (null != tmpObj) {
			tableManager.setExpTabBody((List) tmpObj);
			session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_PRE + "_"
					+ reportId);
		}
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_PRE_BODY + "_"
				+ reportId);
		if (null != tmpObj) {
			tableManager.setTabBody((List) tmpObj);
			session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_PRE_BODY + "_"
					+ reportId);
		}
		List tableCols = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
				+ reportId);
		RptOlapFuncStruct tmpCurFun = null;
		if (null != tmpObj) {
			// 如果是单击指标钻取，需要恢复以前的列对象定义，
			// 要保存以前的，怎么保存，何时保存
			// 还要看是否是数据查询
			String curFunc = request.getParameter(RptOlapConsts.REQ_OLAP_FUNC);

			Object obj = session.getAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ
					+ "_" + reportId);
			if (null != obj) {
				tmpCurFun = (RptOlapFuncStruct) obj;
			}
			if (hasNewContent && null != curFunc
					&& !curFunc.equals(tmpCurFun.getCurFunc())) {
				tmpObj = session
						.getAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ
								+ "_" + reportId);
				tableCols = (List) tmpObj;
				// 克隆一下，要不还是要改变
				try {
					tableCols = RptOlapTabDomainUtil.copyDomains(tableCols);
				} catch (CloneNotSupportedException cse) {

				}
			} else {
				tableCols = (List) tmpObj;
			}
		} else {
			try {
				tableCols = tableManager.genTableColStruct(reportId);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－组合报表域对象列表时发生错误", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"组合报表域对象列表时发生错误！");
			}
		}
		List rptMsus = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_"
				+ reportId);
		if (null != tmpObj) {
			rptMsus = (List) tmpObj;
		} else {
			try {
				rptMsus = tableManager.getRptMsus(reportId);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－组合报表域对象列表时发生错误", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"组合报表域对象列表时发生错误！");
			}
		}
		if (null == clearSession || !RptOlapConsts.TRUE.equals(clearSession)) {
			try {
				tableCols = tableManager.parseRequestToTableColStruct(request,
						reportId, tableCols, rptMsus);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－组合报表域对象列表时发生错误", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"组合报表域对象列表时发生错误！");
			}
		}
		if (null != clearSession && RptOlapConsts.TRUE.equals(clearSession)) {
			// 这里根据用户定制设置列域对象
			if (null != userCustomDims && null != userCustomMsus) {
				RptOlapTabDomainUtil.adjustTableCols(tableCols, userCustomDims,
						userCustomMsus);
			}
		}
		// 当前列域指标排序
		Collections.sort(tableCols, new TabColComparator());
		// 获取当前功能结构
		RptOlapFuncStruct olapFun = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
				+ reportId);
		String statPeriod = "";
		statPeriod = report.cycle;
		if (null != tmpObj)
			olapFun = (RptOlapFuncStruct) tmpObj;
		try {
			olapFun = RptOlapFuncUtil.genOlapFuncDomain(request, olapFun,
					tableCols, statPeriod, rptMsus);
			if (null != clearSession && RptOlapConsts.TRUE.equals(clearSession)) {
				// 需要考虑是否用户定制了，如果进行定制了，则显示定制模式
				if (null != userCustom
						&& !StringUtils.isBlank(userCustom.display_mode)) {
					olapFun = RptOlapFuncUtil.setUserCustomInitFunc(olapFun,
							userCustom, tableCols);
				}
			}
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表功能对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表功能对象！");
		}

		// 获取数据日期结构
		RptOlapDateStruct ds = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_"
				+ reportId);
		if (null != tmpObj) {
			ds = (RptOlapDateStruct) tmpObj;
		}
		try {
			ds = RptOlapDateUtil.genOlapDateDomain(request, ds, tableCols,
					report);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表时间对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表时间对象！");
		}

		boolean fixedHead = true;

		// 获取表格内容
		String[][] svces = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
				+ reportId);
		// 看看是不是有排序要求
		String hasSort = request.getParameter(RptOlapConsts.REQ_SORT);
		if (null != tmpObj
				&& !hasNewContent
				&& (RptOlapConsts.OLAP_FUN_PERCENT.equals(olapFun.getCurFunc())
						|| (null != hasSort && !"".equals(hasSort)) || (!RptOlapConsts.RESET_MODE_DIG
						.equals(olapFun.getDisplayMode()) && !olapFun
						.isHasNewExpand()))) {
			// 排序时到了这里,需要折叠时将svces也相应的减少
			svces = (String[][]) tmpObj;
		} else {
			try {
				svces = tableManager.getTableContent(tableCols, report,
						olapFun, userCtl, svckndRight, ds);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－无法获取报表数据库内容", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"无法获取报表数据库内容！");
			}
		}
		if (null != hasSort && !"".equals(hasSort)) {
			try {
				svces = tableManager.sortTableContent(tableCols, olapFun,
						report.cycle, svces);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－表格内容进行排序时发生错误", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"表格内容进行排序时发生错误！");
			}
		}
		// 获取以前的行对象列表，然后设置
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_ROW_CONTENT + "_"
				+ reportId);
		if (null != tmpObj) {
			List list = (List) tmpObj;
			tableManager.setTabBody(list);
		}
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_ROW_EXP_CONTENT + "_"
				+ reportId);
		if (null != tmpObj) {
			List list = (List) tmpObj;
			tableManager.setExpTabBody(list);
		}
		String[] HTML = null;
		String[] export = null;
		try {
			HTML = tableManager.getTableHTML(svces, tableCols, report, olapFun,
					fixedHead);
			export = tableManager.getExportTableHTML();
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表HTML内容", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表HTML内容！");
		}
		if (null != HTML) {
			for (int i = 0; i < HTML.length; i++) {
				String tmpHTML = HTML[i];
				tmpHTML = tmpHTML.replaceAll("::report_id::", reportId);
				tmpHTML = tmpHTML.replaceAll("::" + RptOlapConsts.REQ_OLAP_FUNC
						+ "::", olapFun.getCurFunc());
				out.write(tmpHTML);
				out.flush();
			}
		}
		if (null != ds)
			session.setAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_" + reportId,
					ds);
		if (null != olapFun)
			session.setAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
					+ reportId, olapFun);
		if (null != tableCols)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
					+ reportId, tableCols);
		if ((!hasNewContent || (hasNewContent && tmpCurFun.getCurFunc().equals(
				olapFun.getCurFunc())))
				&& null != tableCols) {
			// 由于此处是对象，改动原来的，这里的也改动
			// 需要克隆
			try {
				tableCols = RptOlapTabDomainUtil.copyDomains(tableCols);
				session.setAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ
						+ "_" + reportId, tableCols);
			} catch (CloneNotSupportedException cse) {

			}
		}
		if (null != report)
			session.setAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_" + reportId,
					report);
		if (null != svces)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
					+ reportId, svces);
		if (null != export)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_EXPORT + "_"
					+ reportId, export);
		List list = tableManager.getTabBody();
		if (null != list && 0 < list.size())
			session.setAttribute(
					WebKeys.ATTR_OLAP_ROW_CONTENT + "_" + reportId, list);
		List expTabRows = tableManager.getExpTabBody();
		if (null != expTabRows && 0 < expTabRows.size())
			session.setAttribute(WebKeys.ATTR_OLAP_ROW_EXP_CONTENT + "_"
					+ reportId, expTabRows);
		System.out.println("分析型报表表格分析总用时："
				+ (System.currentTimeMillis() - startTime) + "ms");
		setNvlNextScreen(request);
	}
}
