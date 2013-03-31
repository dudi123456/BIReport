package com.ailk.bi.olap.action;

import java.util.ArrayList;
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
import com.ailk.bi.olap.domain.RptOlapRowStruct;
import com.ailk.bi.olap.service.IReportManager;
import com.ailk.bi.olap.service.ITableManager;
import com.ailk.bi.olap.service.impl.ReportManager;
import com.ailk.bi.olap.service.impl.TableManager;
import com.ailk.bi.olap.util.HttpSessionUtil;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDateUtil;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapFuncUtil;
import com.ailk.bi.olap.util.RptOlapMsuUtil;
import com.ailk.bi.olap.util.RptOlapTabDomainUtil;
import com.ailk.bi.olap.util.TabColComparator;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * @author DXF 第一次生成所有的列对象 需要缓存所有的列对象,因为它才有链接到维度和指标定义 钻取时要更新相应列对象的属性，包括生成URL，全链接
 *         如果用户选择了不同的维度和指标和顺序 调整顺序时要进行排序
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportOlapTableHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -4429644208886602720L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		long startTime = System.currentTimeMillis();
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("分析型报表表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		}
		HttpSession session = request.getSession();
		InfoOperTable userTable = CommonFacade.getLoginUser(session);
		String userId = userTable.oper_no;

		String reportId = request.getParameter("report_id");
		if (null == reportId || "".equals(reportId)) {
			log.error("分析型报表表格分析－无法获取报表标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表标识！");
		}
		// 清除缓存
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
		// 获取业务类型权限
		String svckndRight = "";// CommTool.getSvcRights(session).toLowerCase();
		// userCtl=null;
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
		// 这里根据用户定制设置列域对象
		if (null != userCustomDims && null != userCustomMsus) {
			RptOlapTabDomainUtil.adjustTableCols(tableCols, userCustomDims,
					userCustomMsus);
		}
		List rptMsus = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_"
				+ reportId);
		if (null != tmpObj) {
			rptMsus = (List) tmpObj;
		} else {
			try {
				rptMsus = tableManager.getRptMsus(reportId);
				// 需要合并用户的指标
			} catch (ReportOlapException roe) {
				log.error("分析型报表表格分析－组合报表域对象列表时发生错误", roe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"组合报表域对象列表时发生错误！");
			}
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
			// 需要考虑是否用户定制了，如果进行定制了，则显示定制模式
			if (null != userCustom
					&& !StringUtils.isBlank(userCustom.display_mode)) {
				olapFun = RptOlapFuncUtil.setUserCustomInitFunc(olapFun,
						userCustom, tableCols);
			}
		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表功能对象", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表功能对象！");
		}
		// 转换第一次的URL，好在页面使用
		String firUrl = RptOlapTabDomainUtil.convertTableColStructToUrl(
				tableCols, olapFun);
		firUrl = RptOlapConsts.OLAP_DIG_ACTION + "?report_id=" + reportId;
		// 这里为什么要清除缓存呢
		firUrl += "&" + RptOlapConsts.OLAP_CLEAR_SESSION + "="
				+ RptOlapConsts.TRUE;
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
			for (int i = 0; i < HTML.length; i++) {
				String tmpHTML = HTML[i];
				tmpHTML = tmpHTML.replaceAll("::report_id::", reportId);
				tmpHTML = tmpHTML.replaceAll("::" + RptOlapConsts.REQ_OLAP_FUNC
						+ "::", olapFun.getCurFunc());
				HTML[i] = tmpHTML;
			}
			export = tableManager.getExportTableHTML();

		} catch (ReportOlapException roe) {
			log.error("分析型报表表格分析－无法获取报表HTML内容", roe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表HTML内容！");
		}

		List userReports = tableManager.getUserCustomReport(userId);
		if (null != ds)
			session.setAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_" + reportId,
					ds);
		if (null != olapFun)
			session.setAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
					+ reportId, olapFun);
		if (null != tableCols)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
					+ reportId, tableCols);
		if (null != tableCols) {
			try {
				tableCols = RptOlapTabDomainUtil.copyDomains(tableCols);
				session.setAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ
						+ "_" + reportId, tableCols);
			} catch (CloneNotSupportedException cse) {

			}
		}
		if (null != tableManager.getExpTabBody()
				&& tableManager.getExpTabBody().size() > 0) {
			List dest = new ArrayList(tableManager.getExpTabBody().size());
			RptOlapRowStruct row = null;
			try {
				for (Object obj : tableManager.getExpTabBody()) {
					row = (RptOlapRowStruct) obj;
					dest.add(row.clone());
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_PRE + "_" + reportId,
					dest);
		}

		if (null != tableManager.getTabBody()
				&& tableManager.getTabBody().size() > 0) {
			List dest = new ArrayList(tableManager.getTabBody().size());
			RptOlapRowStruct row = null;
			try {
				for (Object obj : tableManager.getTabBody()) {
					row = (RptOlapRowStruct) obj;
					dest.add(row.clone());
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_PRE_BODY + "_"
					+ reportId, dest);
		}
		tableManager = null;
		if (null != report)
			session.setAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_" + reportId,
					report);
		if (null != HTML)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_HTML + "_" + reportId,
					HTML);
		if (null != export)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_EXPORT + "_"
					+ reportId, export);
		if (null != firUrl)
			session.setAttribute(WebKeys.ATTR_OLAP_FIR_URL + "_" + reportId,
					firUrl);
		if (null != svces)
			session.setAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
					+ reportId, svces);
		if (null != rptDims && 0 < rptDims.size()) {
			// 如果有用户的属性按用户的走
			rptDims = RptOlapDimUtil.adjustUserDims(rptDims, userCustomDims);
			session.setAttribute(WebKeys.ATTR_OLAP_RPT_DIMS + "_" + reportId,
					rptDims);
		}
		if (null != rptMsus && 0 < rptMsus.size()) {
			// 如果有用户的属性按用户的走
			rptMsus = RptOlapMsuUtil.adjustUserMsus(rptMsus, userCustomMsus);
			session.setAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_" + reportId,
					rptMsus);
		}
		if (null != rptMsus && 0 < rptMsus.size())
			session.setAttribute(WebKeys.ATTR_OLAP_RPT_PRE_MSUS + "_"
					+ reportId, rptMsus);
		if (null != userReports && 0 < userReports.size())
			session.setAttribute(WebKeys.ATTR_OLAP_USER_REPORTS + "_"
					+ reportId, userReports);
		request.setAttribute("report_id", reportId);
		System.out.println("分析型报表表格分析总用时："
				+ (System.currentTimeMillis() - startTime) + "ms");
		setNextScreen(request, "rptOlapTable.screen");
	}
}
