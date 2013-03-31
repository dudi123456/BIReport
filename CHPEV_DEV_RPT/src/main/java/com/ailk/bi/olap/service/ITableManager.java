package com.ailk.bi.olap.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface ITableManager {
	/**
	 * 获取用户定制的维度
	 *
	 * @param cusRptId
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List getUserCustomDims(String cusRptId)
			throws ReportOlapException;

	/**
	 * 获取用户定制的指标
	 *
	 * @param cusRptId
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List getUserCustomMsus(String cusRptId)
			throws ReportOlapException;

	/**
	 * 删除用户自定义的分析型报表
	 *
	 * @param cusRptId
	 * @throws ReportOlapException
	 */
	public abstract void deleteUserCustomReport(String cusRptId)
			throws ReportOlapException;

	/**
	 * 获得此用户的所有定义的自定义分析型报表
	 *
	 * @param userId
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List getUserCustomReport(String userId)
			throws ReportOlapException;

	/**
	 * 获取用户的定制
	 *
	 * @param userId
	 * @param reportId
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract RptUserOlapTable getUserCustomReport(String userId,
			String reportId) throws ReportOlapException;

	/**
	 * 将用户的选择进行保存
	 *
	 * @param tabCols
	 * @param olapFun
	 * @param reportId
	 * @param userId
	 * @throws ReportOlapException
	 */
	public abstract void saveUserCustomReport(String reportId, String userId,
			String cusRptName, String displayMode, List<String> userDims,
			List<String> userMsus) throws ReportOlapException;

	/**
	 * 对表格内容进行排序
	 *
	 * @param tabCols
	 * @param olapFun
	 * @param statPeriod
	 * @param svces
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract String[][] sortTableContent(List tabCols,
			RptOlapFuncStruct olapFun, String statPeriod, String[][] svces)
			throws ReportOlapException;

	/**
	 * 根据用户请求生成或更新数据表格列域对象的各种状态
	 *
	 * @param request
	 *            用户请求对象
	 * @param reportId
	 *            分析型报表标识
	 * @param tabCols
	 *            列对象列表
	 * @param parseRequest
	 *            是否根据请求对象更新列对象，首次加载时否
	 * @return 更新后的列对象
	 * @throws ReportOlapException
	 */
	public abstract List genTableColStruct(String reportId)
			throws ReportOlapException;

	/**
	 * 分析请求信息到表格列域对象
	 *
	 * @param request
	 *            请求对象
	 * @param reportId
	 *            报表标识
	 * @param tabCols
	 *            表格列域对象
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List parseRequestToTableColStruct(
			HttpServletRequest request, String reportId, List tabCols,
			List rptMsus) throws ReportOlapException;

	/**
	 * 根据列对象和当前分析功能获取数据库内容
	 *
	 * @param tableCols
	 *            列对象列表
	 * @param report
	 *            报表对象
	 * @param olapFun
	 *            当前功能对象
	 * @param ds
	 *            日期对象
	 * @return 数据库查询二维数组
	 * @throws ReportOlapException
	 */
	public abstract String[][] getTableContent(List tableCols,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			RptOlapDateStruct ds) throws ReportOlapException;

	/**
	 * 根据表格内容生成HTML样式表格
	 *
	 * @param svces
	 *            表格内容数组
	 * @param tableCols
	 *            列对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param fixedHead
	 *            是否固定表头
	 * @return 表格的HTML的二维数组
	 * @throws ReportOlapException
	 */
	public abstract String[] getTableHTML(String[][] svces, List tableCols,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			boolean fixedHead) throws ReportOlapException;

	/**
	 * 获取报表的维度对象列表
	 *
	 * @param reportId
	 *            报表标识
	 * @return 维度对象列表
	 * @throws ReportOlapException
	 */
	public abstract List getRptDims(String reportId) throws ReportOlapException;

	/**
	 * 获取报表的指标对象列表
	 *
	 * @param reportId
	 *            报表标识
	 * @return 指标对象列表
	 * @throws ReportOlapException
	 */
	public abstract List getRptMsus(String reportId) throws ReportOlapException;

	/**
	 * 获取表格表体内容
	 *
	 * @return
	 */
	public abstract List getTabBody();

	/**
	 * 设置表报表体内容
	 *
	 * @param tabBody
	 */
	public abstract void setTabBody(List tabBody);

	/**
	 * 根据用户定制的属性设置表格域对象
	 *
	 * @param tabCols
	 *            表格域对象列表
	 * @param cusRptDims
	 *            用户定制的维度列表
	 * @param cusRptMsus
	 *            用户定制的指标列表
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List processTableDomainWithCusRpt(List tabCols,
			Map cusRptDims, Map cusRptMsus) throws ReportOlapException;

	/**
	 * 获取导出的表格内容
	 *
	 * @return
	 */
	public abstract String[] getExportTableHTML();

	/**
	 * 取得当前的导出内容的行结构的列表
	 *
	 * @return
	 */
	public abstract List getExpTabBody();

	/**
	 * 设置当前的导出内容的行结构列表
	 *
	 * @param expTabBody
	 */
	public abstract void setExpTabBody(List expTabBody);
}
