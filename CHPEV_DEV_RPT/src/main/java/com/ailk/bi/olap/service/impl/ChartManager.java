package com.ailk.bi.olap.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.IChartManager;
import com.ailk.bi.olap.service.dao.IChartContentDao;
import com.ailk.bi.olap.service.dao.IChartDomainDao;
import com.ailk.bi.olap.service.dao.IChartTableBodyDao;
import com.ailk.bi.olap.service.dao.IChartTableHeadDao;
import com.ailk.bi.olap.service.dao.IReportDimDao;
import com.ailk.bi.olap.service.dao.IReportMsuDao;
import com.ailk.bi.olap.service.dao.impl.ChartContentDao;
import com.ailk.bi.olap.service.dao.impl.ChartDomainDao;
import com.ailk.bi.olap.service.dao.impl.ChartTableBodyDao;
import com.ailk.bi.olap.service.dao.impl.ChartTableHeadDao;
import com.ailk.bi.olap.service.dao.impl.ReportDimDao;
import com.ailk.bi.olap.service.dao.impl.ReportMsuDao;
import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChartManager implements IChartManager {
	/**
	 * 报表维度DAO对象
	 */
	private IReportDimDao reportDim = new ReportDimDao();

	/**
	 * 报表指标DAO对象
	 */
	private IReportMsuDao reportMsu = new ReportMsuDao();

	/**
	 * 图形域对象DAO
	 */
	private IChartDomainDao chartDomain = new ChartDomainDao();

	/**
	 * 获取图形数据的对象
	 */
	private IChartContentDao chartContent = new ChartContentDao();

	/**
	 * 获取图形数据表头DAO对象
	 */
	private IChartTableHeadDao chartHead = new ChartTableHeadDao();

	/**
	 * 获取图形表体DAO对象
	 */
	private IChartTableBodyDao chartBody = new ChartTableBodyDao();

	/**
	 * 报表维度对象
	 */
	private List rptDims = null;

	/**
	 * 报表指标对象
	 */
	private List rptMsus = null;

	public List genChartStructs(String reportId) throws ReportOlapException {
		rptDims = reportDim.getReportDim(reportId);
		rptMsus = reportMsu.getReportMsu(reportId);
		return chartDomain.genDefaultSetting(reportId, rptDims, rptMsus);
	}

	public String[][] getChartContent(List structs,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			RptOlapDateStruct ds) throws ReportOlapException {
		return chartContent.getChartContent(structs, report, olapFun, userCtl,
				svckndRight, ds);
	}

	public String[] getChartTableHTML(String[][] svces, List chartStructs,
			RptOlapFuncStruct olapFun, String statPeriod)
			throws ReportOlapException {
		if (null == chartStructs || 0 >= chartStructs.size() || null == olapFun
				|| null == statPeriod || "".equals(statPeriod))
			throw new ReportOlapException("生成图形分析的表格HTML时输入的参数为空");
		List tableHTML = new ArrayList();
		StringBuffer table = new StringBuffer();

		table.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\"")
				.append(" cellspacing=\"1\">\n");
		tableHTML.add(table.toString());

		if (null == svces || svces.length <= 0) {
			// 查询没有结果
			tableHTML.clear();
			table.delete(0, table.length());
			table.append(
					"</br><center><strong>当前条件组合下没有查询数据结果,</br>或者数据没有到达您所能控制区域的粒度<br>")
					.append("或者由于您长时间没有操作已经超时，</br>")
					.append("请重新登陆再试，如果问题依旧，请联系系统管理员！</strong></center>");
			tableHTML.add(table.toString());
			String[] html = (String[]) tableHTML.toArray(new String[tableHTML
					.size()]);
			return html;
		} else {
			tableHTML.addAll(chartHead.genChartHTMLHead(chartStructs, olapFun,
					RptOlapConsts.TABLE_HEAD_TR_CLASS,
					RptOlapConsts.TABLE_HEAD_TD_CLASS));
			tableHTML.addAll(chartBody
					.getTableBody(svces, chartStructs, olapFun, statPeriod,
							RptOlapConsts.TABLE_BODY_TR_CLASS, RptOlapConsts.TABLE_BODY_TD_CLASS));
			tableHTML.add("</table>");
		}
		String[] html = (String[]) tableHTML.toArray(new String[tableHTML
				.size()]);
		return html;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asiabi.olap.service.IChartManager#getRptDims(java.lang.String)
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
	 * @see com.asiabi.olap.service.IChartManager#getRptMsus(java.lang.String)
	 */
	public List getRptMsus(String reportId) throws ReportOlapException {
		if (null == reportId)
			throw new ReportOlapException("获取报表的维度对象列表时报表标识为空");
		if (null == rptMsus || 0 >= rptMsus.size())
			rptMsus = reportMsu.getReportMsu(reportId);
		return rptMsus;
	}
}
