package com.ailk.bi.olap.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.table.RptOlapUserDimTable;
import com.ailk.bi.base.table.RptOlapUserMsuTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.ITableManager;
import com.ailk.bi.olap.service.dao.ICustomReportDao;
import com.ailk.bi.olap.service.dao.ICustomReportDimDao;
import com.ailk.bi.olap.service.dao.ICustomReportMsuDao;
import com.ailk.bi.olap.service.dao.ICustomTableHeadDao;
import com.ailk.bi.olap.service.dao.IReportContentDao;
import com.ailk.bi.olap.service.dao.IReportDimDao;
import com.ailk.bi.olap.service.dao.IReportDomainDao;
import com.ailk.bi.olap.service.dao.IReportMsuDao;
import com.ailk.bi.olap.service.dao.IReportSortDao;
import com.ailk.bi.olap.service.dao.ITableBodyDao;
import com.ailk.bi.olap.service.dao.ITableHeadDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportDimDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportMsuDao;
import com.ailk.bi.olap.service.dao.impl.CustomTableHeadDao;
import com.ailk.bi.olap.service.dao.impl.ReportContentDao;
import com.ailk.bi.olap.service.dao.impl.ReportDimDao;
import com.ailk.bi.olap.service.dao.impl.ReportDomainDao;
import com.ailk.bi.olap.service.dao.impl.ReportMsuDao;
import com.ailk.bi.olap.service.dao.impl.ReportSortDao;
import com.ailk.bi.olap.service.dao.impl.TableBodyDao;
import com.ailk.bi.olap.service.dao.impl.TableHeadDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapTabDomainUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableManager implements ITableManager {
	/**
	 * 自定义表头DAO对象
	 */
	private ICustomTableHeadDao customHead = new CustomTableHeadDao();

	/**
	 * 报表内容DAO对象
	 */
	private IReportContentDao reportContent = new ReportContentDao();

	/**
	 * 报表维度DAO对象
	 */
	private IReportDimDao reportDim = new ReportDimDao();

	/**
	 * 报表域DAO对象
	 */
	private IReportDomainDao reportDomain = new ReportDomainDao();

	/**
	 * 报表指标DAO对象
	 */
	private IReportMsuDao reportMsu = new ReportMsuDao();

	/**
	 * 报表表体DAO对象
	 */
	private ITableBodyDao tableBody = new TableBodyDao();

	/**
	 * 报表表头对象
	 */
	private ITableHeadDao tableHead = new TableHeadDao();

	/**
	 * 排序DAO对象
	 */
	private IReportSortDao sortDao = new ReportSortDao();

	/**
	 * 用户自定义报表信息DAO对象
	 */
	private ICustomReportDao cusReportDao = new CustomReportDao();

	/**
	 * 用户自定义报表维度DAO对象
	 */
	private ICustomReportDimDao cusReportDimDao = new CustomReportDimDao();

	/**
	 * 用户自定义报表指标DAO对象
	 */
	private ICustomReportMsuDao cusReportMsuDao = new CustomReportMsuDao();

	/**
	 * 报表维度对象
	 */
	private List rptDims = null;

	/**
	 * 报表指标对象
	 */
	private List rptMsus = null;

	/**
	 * 获取行结构的列表
	 */
	private List tabBody = new ArrayList();

	/**
	 * 保存导出内容的行结构列表
	 */
	private List expTabBody = new ArrayList();

	/**
	 * 导出的字符串列表
	 */
	private List exportTabBodyHTML = new ArrayList();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#genTableColStruct(javax.servlet
	 * .http.HttpServletRequest, java.lang.String, java.util.List, boolean)
	 */
	public List genTableColStruct(String reportId) throws ReportOlapException {
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("生成表格的DOMAIN对象时报表标识参数为空");
		List tableCols = null;
		// 第一次访问
		rptDims = reportDim.getReportDim(reportId);
		rptMsus = reportMsu.getReportMsu(reportId);
		tableCols = reportDomain.genTableColStruct(rptDims, rptMsus);
		return tableCols;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#parseRequestToTableColStruct(javax
	 * .servlet.http.HttpServletRequest, java.lang.String, java.util.List,
	 * java.util.List)
	 */
	public List parseRequestToTableColStruct(HttpServletRequest request,
			String reportId, List tabCols, List rptMsus)
			throws ReportOlapException {
		if (null == request || null == reportId || "".equals(reportId)
				|| null == tabCols || 0 >= tabCols.size() || null == rptMsus
				|| 0 >= rptMsus.size())
			throw new ReportOlapException("分析请求到表格域对象时输入的参数维空");
		List tableCols = RptOlapTabDomainUtil.pareseRequestToTableColStruct(
				request, tabCols, rptMsus);
		return tableCols;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#getTableContent(java.util.List,
	 * com.asiabi.base.table.PubInfoResourceTable,
	 * com.asiabi.olap.domain.RptOlapFunc,
	 * com.asiabi.olap.domain.RptOlapDateStruct)
	 */
	public String[][] getTableContent(List tableCols,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			RptOlapDateStruct ds) throws ReportOlapException {
		String[][] svces = null;
		if (null == tableCols || tableCols.size() <= 0 || null == report
				|| null == olapFun || null == ds)
			throw new IllegalArgumentException("获取表格内容时输入的参数为空");
		// 表体
		if (RptOlapConsts.RESET_MODE_DIG.equals(olapFun.getDisplayMode())) {
			svces = reportContent.getContent(tableCols, report, ds, olapFun,
					userCtl, svckndRight);
		} else {
			svces = reportContent.getExpandContent(tableCols, report, ds,
					olapFun, userCtl, svckndRight, olapFun.getCurExpandLevel()
							+ "", olapFun.isSingleDimExpand());
		}
		return svces;
	}

	public String[] getTableHTML(String[][] svces, List tableCols,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			boolean fixedHead) throws ReportOlapException {
		if (null == tableCols || tableCols.size() <= 0 || null == report
				|| null == olapFun)
			throw new IllegalArgumentException("生成表格HTML代码时输入的参数为空");
		List lTableHTML = new ArrayList();
		StringBuffer sb = new StringBuffer("");

		if (olapFun.isFirstExpand())
			exportTabBodyHTML.clear();
		String height = RptOlapConsts.ALIGN_TABLE_HEIGHT;
		if (!StringUtils.isBlank(report.sequence)) {
			int prefHeight = 0;
			try {
				prefHeight = Integer.parseInt(report.sequence.trim());
			} catch (Exception e) {

			}
			if (prefHeight > Integer.parseInt(RptOlapConsts.ALIGN_TABLE_HEIGHT)) {
				height = prefHeight + "";
			}
		}
		if (fixedHead) {
			sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\"")
					.append(" style=\"border-collapse: collapse\" style=\"table-layout:fixed\" cellspacing=\"0\">\n");
			sb.append("<col>");
			sb.append("<tr><td class=\"side-left\">");
			sb.append("<table width=\"100%\" border=\"0\" height=\"")
					.append(height)
					.append("\" style=\"border-collapse: collapse\"  style=\"table-layout:auto\" cellpadding=\"0\" cellspacing=\"0\" ")
					.append("id=\"iTable_TableContainer\">\n");
			sb.append("<col><col><col><col>");
		} else {
			sb.append(
					"<table width=\"100%\" border=\"0\"  style=\"table-layout:fixed\" cellpadding=\"0\"")
					.append(" style=\"border-collapse: collapse\" cellspacing=\"0\">\n");
		}
		lTableHTML.add(sb.toString());
		List head = null;
		// 设置表格
		exportTabBodyHTML
				.add("<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\">\n");
		// 用户定制情况下、指标钻取情况下都不适用自定义指标
		if (RptOlapConsts.YES.equalsIgnoreCase(report.ishead)
				&& olapFun.isCanUseCustomHead()) {
			head = customHead.getCustomHead(report, tableCols, olapFun,
					fixedHead, RptOlapConsts.TABLE_HEAD_TR_CLASS,
					RptOlapConsts.TABLE_HEAD_TD_CLASS);
			exportTabBodyHTML.addAll(customHead.getExportHead());
		} else {
			if (RptOlapConsts.RESET_MODE_DIG.equals(olapFun.getDisplayMode())) {
				head = tableHead.genHTMLTableHead(report, tableCols, olapFun,
						report.cycle, fixedHead,
						RptOlapConsts.TABLE_HEAD_TR_CLASS,
						RptOlapConsts.TABLE_HEAD_TD_CLASS);
			} else {
				head = tableHead.genExpandHTMLTableHead(tableCols, olapFun,
						report.cycle, fixedHead,
						RptOlapConsts.TABLE_HEAD_TR_CLASS,
						RptOlapConsts.TABLE_HEAD_TD_CLASS, "0", false);
			}
			exportTabBodyHTML.addAll(tableHead.getExportHead());
		}
		if (null != head)
			lTableHTML.addAll(head);
		// 表体
		if (null == svces || svces.length <= 1) {
			// 查询没有结果
			lTableHTML.clear();
			sb.delete(0, sb.length());
			sb.append("<br><strong>当前条件组合下没有查询数据结果，或者数据没有到达您所能控制区域的粒度<br>")
					.append("或者由于您长时间没有操作已经超时，")
					.append("请重新登陆再试，如果问题依旧，请联系系统管理员！</strong>");
			lTableHTML.add(sb.toString());
			String[] tableHTML = (String[]) lTableHTML
					.toArray(new String[lTableHTML.size()]);
			exportTabBodyHTML.clear();
			exportTabBodyHTML.add(sb.toString());
			return tableHTML;
		} else {
			List body = null;
			if (RptOlapConsts.RESET_MODE_DIG.equals(olapFun.getDisplayMode())) {
				body = tableBody.getTableBody(svces, report, tableCols,
						olapFun, fixedHead, RptOlapConsts.TABLE_BODY_TR_CLASS,
						RptOlapConsts.TABLE_BODY_TD_CLASS);
			} else {
				tableBody.setPreExpBody(expTabBody);
				tabBody = tableBody.getExpandTableBody(tabBody, svces, report,
						tableCols, olapFun, fixedHead,
						RptOlapConsts.TABLE_BODY_TR_CLASS,
						RptOlapConsts.TABLE_BODY_TD_CLASS,
						olapFun.getCurExpandLevel() + "",
						olapFun.isSingleDimExpand());

				// 此时的body为RptOlapRowStruct对象列表,缓存，然后取出，进行叠加
				body = tableBody.convertRowStructToHtml(tabBody, olapFun);
				expTabBody = tableBody.getPreExpBody();
				// 将行对象转换为真正的字符串数组

			}
			exportTabBodyHTML.addAll(tableBody.getExportBody());
			if (null != body)
				lTableHTML.addAll(body);
			if (fixedHead) {
				lTableHTML.add("</table></td></tr></table>");
			} else {
				lTableHTML.add("</table>");
			}
			String[] tableHTML = (String[]) lTableHTML
					.toArray(new String[lTableHTML.size()]);
			exportTabBodyHTML.add("</table>\n");

			return tableHTML;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#sortTableContent(java.util.List,
	 * com.asiabi.olap.domain.RptOlapFunc, java.lang.String,
	 * java.lang.String[][])
	 */
	public String[][] sortTableContent(List tabCols, RptOlapFuncStruct olapFun,
			String statPeriod, String[][] svces) throws ReportOlapException {
		return sortDao.sortTableContent(tabCols, olapFun, statPeriod, svces);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.ITableManager#getRptDims(java.lang.String)
	 */
	public List getRptDims(String reportId) throws ReportOlapException {
		if (null == reportId)
			throw new ReportOlapException("获取报表的维度对象列表时报表标识为空");
		if (null == rptDims || 0 >= rptDims.size())
			rptDims = reportDim.getReportDim(reportId);
		return rptDims;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.ITableManager#getRptMsus(java.lang.String)
	 */
	public List getRptMsus(String reportId) throws ReportOlapException {
		if (null == reportId)
			throw new ReportOlapException("获取报表的维度对象列表时报表标识为空");
		if (null == rptMsus || 0 >= rptMsus.size())
			rptMsus = reportMsu.getReportMsu(reportId);
		return rptMsus;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.ITableManager#getTabBody()
	 */
	public List getTabBody() {
		return tabBody;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.ITableManager#setTabBody(java.util.List)
	 */
	public void setTabBody(List tabBody) {
		this.tabBody = tabBody;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#saveUserCustomReport(java.util.
	 * List, com.asiabi.olap.domain.RptOlapFunc, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void saveUserCustomReport(String reportId, String userId,
			String cusRptName, String displayMode, List<String> userDims,
			List<String> userMsus) throws ReportOlapException {
		if (StringUtils.isBlank(reportId) || StringUtils.isBlank(userId)
				|| StringUtils.isBlank(displayMode) || null == userDims
				|| userDims.size() == 0 || null == userMsus
				|| userMsus.size() == 0) {
			throw new ReportOlapException("保存用户自定义的属性时输入的参数为空");
		}
		String cusReportId = cusReportDao.saveReport(userId, cusRptName,
				reportId, displayMode);
		cusReportDimDao.deleteReportDim(cusReportId);
		cusReportMsuDao.deleteReportMsu(cusReportId);
		cusReportDimDao.saveReportDim(cusReportId, userDims);
		cusReportMsuDao.saveReportMsu(cusReportId, userMsus);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.ITableManager#processTableDomainWithCusRpt(java
	 * .util.List, java.util.Map, java.util.Map)
	 */
	public List processTableDomainWithCusRpt(List tabCols, Map cusRptDims,
			Map cusRptMsus) throws ReportOlapException {
		if (null == tabCols || null == cusRptDims || null == cusRptMsus
				|| 0 >= tabCols.size() || 0 >= cusRptDims.size()
				|| 0 >= cusRptMsus.size())
			throw new ReportOlapException("根据用户定制设置表格域对象列表时输入的参数为空");
		List tableCols = new ArrayList();
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				// 对于时间维，无论如何都要加上，只是不显示
				Object obj = cusRptDims.get(rptDim.dim_id);
				if (null != obj) {
					tCol.setDisplay(true);
					RptOlapUserDimTable cusRptDim = (RptOlapUserDimTable) obj;
					tCol.setDisplayOrder(Integer
							.parseInt(cusRptDim.display_order));
					tableCols.add(tCol);
				} else if (tCol.isTimeDim()) {
					tCol.setDisplay(false);
					tableCols.add(tCol);
				}
			} else {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				Object obj = cusRptMsus.get(rptMsu.msu_id);
				if (null != obj) {
					tCol.setDisplay(true);
					RptOlapUserMsuTable cusRptMsu = (RptOlapUserMsuTable) obj;
					tCol.setDisplayOrder(Integer
							.parseInt(cusRptMsu.display_order));
					tableCols.add(tCol);
				}
			}
		}
		return tableCols;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.ITableManager#getExportTableHTML()
	 */
	public String[] getExportTableHTML() {
		String[] html = null;
		if (null != exportTabBodyHTML && 0 < exportTabBodyHTML.size()) {
			html = (String[]) exportTabBodyHTML
					.toArray(new String[exportTabBodyHTML.size()]);
		}
		return html;
	}

	public List getExpTabBody() {
		return expTabBody;
	}

	public void setExpTabBody(List expTabBody) {
		this.expTabBody = expTabBody;
	}

	public List getUserCustomReport(String userId) throws ReportOlapException {
		if (null == userId || "".equals(userId))
			throw new ReportOlapException("获取用户定义的分析型报表列表时用户标识为空");
		return cusReportDao.getUserCusReports(userId);
	}

	public RptUserOlapTable getUserCustomReport(String userId, String reportId)
			throws ReportOlapException {
		if (null == userId || "".equals(userId) || null == reportId
				|| "".equals(reportId))
			throw new ReportOlapException("获取用户定义的分析型报表列表时用户标识为空");
		return cusReportDao.getUserCusReport(userId, reportId);
	}

	public void deleteUserCustomReport(String cusRptId)
			throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("删除用户自定义的分析型报表时报表标识为空");
		cusReportDao.deleteReport(cusRptId);
	}

	public List getUserCustomDims(String cusRptId) throws ReportOlapException {
		return cusReportDimDao.getCustomDims(cusRptId);
	}

	public List getUserCustomMsus(String cusRptId) throws ReportOlapException {
		return cusReportMsuDao.getCustomMsus(cusRptId);
	}
}
