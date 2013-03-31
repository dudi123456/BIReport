package com.ailk.bi.olap.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.IChartManager;
import com.ailk.bi.olap.service.IReportManager;
import com.ailk.bi.olap.service.impl.ChartManager;
import com.ailk.bi.olap.service.impl.ReportManager;
import com.ailk.bi.olap.util.RptOlapChartStructUtil;
import com.ailk.bi.olap.util.RptOlapDateUtil;
import com.ailk.bi.olap.util.RptOlapFuncUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "rawtypes" })
public class ReportOlapChartDigHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -7508905625727202421L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		long startTime = System.currentTimeMillis();
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("分析型报表表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		}
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException ioe) {
			log.error("分析型报表表格分析－无法生成回应输出对象", ioe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法生成回应输出对象！");
		}
		String reportId = request.getParameter("report_id");
		if (null == reportId || "".equals(reportId)) {
			log.error("分析型报表表格分析－无法获取报表标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表标识！");
		}
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
						HTMLActionException.WARN_PAGE, "无法获取报表对象！");
			}
		} else {
			report = (PubInfoResourceTable) tmpObj;
		}
		// 要看看rpeort对象的关键属性是否有值
		if (null == report || "".equals(report.name) || "".equals(report.cycle)
				|| "".equals(report.data_table)) {
			log.error("分析型报表表格分析－报表对象的关键信息为空");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "报表对象的关键信息为空！");
		}
		// 获取数据权限对象
		UserCtlRegionStruct userCtl = null;
		tmpObj = session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (null == tmpObj) {
			log.error("分析型报表表格分析－用户数据权限对象为空");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "用户数据权限对象为空！");
		} else {
			userCtl = (UserCtlRegionStruct) tmpObj;
		}
		// userCtl=null;
		// 获取业务类型权限
		String svckndRight = "";// CommTool.getSvcRights(session).toLowerCase();
		// 声明管理对象
		IChartManager chartManager = new ChartManager();
		// 图形要先获得功能对象
		RptOlapFuncStruct olapFun = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
				+ reportId);
		String statPeriod = "";
		statPeriod = report.cycle;
		if (null != tmpObj)
			olapFun = (RptOlapFuncStruct) tmpObj;
		try {
			olapFun = RptOlapFuncUtil.genOlapFuncDomain(request, olapFun);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表功能对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表功能对象！");
		}
		List chartStructs = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			try {
				chartStructs = chartManager.genChartStructs(reportId);
				chartStructs = RptOlapChartStructUtil.genChartDefaultSetting(
						chartStructs, olapFun);
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－无法获取报表功能对象", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "无法获取报表功能对象！");
			}
		} else {
			chartStructs = (List) tmpObj;
		}
		try {
			chartStructs = RptOlapChartStructUtil.parseRequest(request,
					chartStructs);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表功能对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表功能对象！");
		}
		// 域对象设置好了，时间对象
		RptOlapDateStruct ds = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_"
				+ reportId);
		if (null != tmpObj) {
			ds = (RptOlapDateStruct) tmpObj;
		}
		try {
			ds = RptOlapDateUtil.genOlapChartDateDomain(request, ds,
					chartStructs, report, olapFun, false);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表时间对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表时间对象！");
		}
		// 所有对象准备好了，获取数据
		String[][] svces = null;
		try {
			svces = chartManager.getChartContent(chartStructs, report, olapFun,
					userCtl, svckndRight, ds);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表时间对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表时间对象！");
		}

		String[] HTML = null;
		try {
			HTML = chartManager.getChartTableHTML(svces, chartStructs, olapFun,
					statPeriod);
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表时间对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表时间对象！");
		}
		if (null != HTML) {
			for (int i = 0; i < HTML.length; i++) {
				out.write(HTML[i]);
				out.flush();
			}
		}
		if (null != ds)
			session.setAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_" + reportId,
					ds);
		if (null != olapFun)
			session.setAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
					+ reportId, olapFun);
		if (null != chartStructs)
			session.setAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ + "_"
					+ reportId, chartStructs);
		if (null != report)
			session.setAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_" + reportId,
					report);
		if (null != HTML)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_HTML + "_" + reportId,
					HTML);
		session.setAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_" + reportId,
				svces);
		request.setAttribute("report_id", reportId);
		System.out.println("分析型报表图形分析总用时："
				+ (System.currentTimeMillis() - startTime) + "ms");
		setNvlNextScreen(request);
	}

}
