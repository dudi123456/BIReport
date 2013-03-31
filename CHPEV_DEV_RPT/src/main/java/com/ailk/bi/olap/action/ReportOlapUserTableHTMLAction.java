package com.ailk.bi.olap.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportOlapUserTableHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -8724464332060799590L;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * waf.controller.web.action.HTMLActionSupport#doTrans(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 需要重新设置一下olapFun和tabCols,将不必要的去掉
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("分析型报表表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		}
		HttpSession session = request.getSession();
		String actionType = request.getParameter("actionType");
		if (null != actionType && actionType.equals("save")) {
			saveUserCustom(request, response);
			// 输出结果
			try {
				response.getWriter().print("Success");
			} catch (IOException e) {
				e.printStackTrace();
			}
			setNvlNextScreen(request);
			return;
		}
		String cusrpt_id = request.getParameter("cusrpt_id");
		if (null == cusrpt_id || "".equals(cusrpt_id)) {
			log.error("分析型报表表格分析－无法获取自定义报表标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取自定义报表标识！");
		}
		RptUserOlapTable cusReport = null;
		Map cusRptDims = null;
		Map cusRptMsus = null;
		try {
			IReportManager reportManger = new ReportManager();
			cusReport = reportManger.getCusReport(cusrpt_id);
			cusRptDims = reportManger.getCusRptDims(cusrpt_id);
			cusRptMsus = reportManger.getCusRptMsus(cusrpt_id);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表对象！");
		}
		if (null == cusReport || null == cusRptDims || null == cusRptMsus
				|| 0 >= cusRptMsus.size() || 0 >= cusRptDims.size()) {
			log.error("分析型报表表格分析－无法获取报表对象及其维度和指标");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表对象及其维度和指标！");
		}
		String reportId = cusReport.report_id;
		// 清除缓存
		HttpSessionUtil.clearCacheObj(session, reportId);
		// 获取报表信息对象
		PubInfoResourceTable report = null;
		Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			report = cusReport.report;
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
		// 获取业务类型权限
		String svckndRight = "";// CommTool.getSvcRights(session).toLowerCase();
		ITableManager tableManager = new TableManager();
		List tableCols = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
				+ reportId);
		if (null != tmpObj) {
			tableCols = (List) tmpObj;
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
		try {
			tableCols = tableManager.processTableDomainWithCusRpt(tableCols,
					cusRptDims, cusRptMsus);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－组合报表域对象列表时发生错误", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"组合报表域对象列表时发生错误！");
		}
		// 当前列域指标排序
		Collections.sort(tableCols, new TabColComparator());
		// 缓存报表维度列表和指标列表
		List rptDims = tableManager.getRptDims(reportId);
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
			// 此处要设置到底是什么模式
			olapFun = RptOlapFuncUtil.setUserCustomInitFunc(olapFun, cusReport,
					tableCols);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表功能对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表功能对象！");
		}
		// 转换第一次的URL，好在页面使用
		String firUrl = RptOlapTabDomainUtil.convertTableColStructToUrl(
				tableCols, olapFun);
		firUrl = RptOlapConsts.OLAP_DIG_ACTION + "?report_id=" + reportId
				+ firUrl;
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
		try {
			svces = tableManager.getTableContent(tableCols, report, olapFun,
					userCtl, svckndRight, ds);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表数据库内容", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表数据库内容！");
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
				HTML[i] = tmpHTML;
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
		if (null != report)
			session.setAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_" + reportId,
					report);
		if (null != HTML)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_HTML + "_" + reportId,
					HTML);
		if (null != firUrl)
			session.setAttribute(WebKeys.ATTR_OLAP_FIR_URL + "_" + reportId,
					firUrl);
		if (null != export)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_EXPORT + "_"
					+ reportId, export);
		if (null != svces)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
					+ reportId, svces);
		if (null != rptDims && 0 < rptDims.size())
			session.setAttribute(WebKeys.ATTR_OLAP_RPT_DIMS + "_" + reportId,
					rptDims);
		if (null != rptMsus && 0 < rptMsus.size())
			session.setAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_" + reportId,
					rptMsus);
		List list = tableManager.getTabBody();
		if (null != list && 0 < list.size())
			session.setAttribute(
					WebKeys.ATTR_OLAP_ROW_CONTENT + "_" + reportId, list);
		List expTabRows = tableManager.getExpTabBody();
		if (null != expTabRows && 0 < expTabRows.size())
			session.setAttribute(WebKeys.ATTR_OLAP_ROW_EXP_CONTENT + "_"
					+ reportId, expTabRows);
		request.setAttribute("report_id", reportId);
		setNextScreen(request, "rptOlapUserTable.screen");
	}

	/**
	 * 保存用户定制
	 *
	 * @param request
	 * @param response
	 * @throws HTMLActionException
	 */
	private void saveUserCustom(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		String reportId = request.getParameter("reportId");
		String displayMode = request.getParameter("displayMode");
		String userDim = request.getParameter("userDim");
		String userMsu = request.getParameter("userMsu");
		// 处理维度和指标
		List<String> customDims = new ArrayList<String>();
		List<String> customMsus = new ArrayList<String>();
		customDims.addAll(getSubIndexs(userDim));
		customMsus.addAll(getSubIndexs(userMsu));
		// 开始保存
		InfoOperTable userTable = CommonFacade.getLoginUser(request
				.getSession());
		String userId = userTable.oper_no;
		ITableManager tableManager = new TableManager();
		tableManager.saveUserCustomReport(reportId, userId, " ", displayMode,
				customDims, customMsus);
	}

	/**
	 * 处理维度和指标
	 *
	 * @param indexs
	 * @return
	 */
	private List<String> getSubIndexs(String indexs) {
		String[] splits = indexs.split(",");
		List<String> customs = new ArrayList<String>();
		if (null != splits && splits.length > 0) {
			String[] subs = null;
			for (String split : splits) {
				subs = null;
				subs = split.split("_");
				if (null != subs && subs.length >= 4) {
					customs.add(subs[1]);
				} else {
					customs.add(split);
				}
			}
		}
		return customs;
	}
}
