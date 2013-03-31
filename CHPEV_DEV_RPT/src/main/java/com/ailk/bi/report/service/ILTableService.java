package com.ailk.bi.report.service;

import java.util.List;

import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.struct.DimRuleStruct;

@SuppressWarnings({ "rawtypes" })
public interface ILTableService {

	/**
	 * 获取报表标题内容
	 *
	 * @param rptTable
	 * @param qryStruct
	 * @return
	 */
	public String getReportTitle(Object rptTable, Object qryStruct);

	/**
	 * 获取报表表格内容
	 *
	 * @param rptTable
	 * @param listReportCol
	 * @param qryStruct
	 * @return
	 */
	public String[] getReportBody(Object rptTable, Object listReportCol,
			Object qryStruct, PubInfoConditionTable[] cdnTables);

	/**
	 * 获取指标类报表的标题内容
	 *
	 * @param rptTable
	 * @param qryStruct
	 * @return
	 */
	public String getMeasureReportTitle(Object rptTable, Object qryStruct);

	/**
	 * 获取指标类报表的表格内容
	 *
	 * @param rptTable
	 * @param qryStruct
	 * @return
	 */
	public String[] getMeasureReportBody(Object rptTable, Object qryStruct);

	/**
	 * 获取指标类报表的定制分析维度内容
	 *
	 * @param rptTable
	 * @param qryStruct
	 * @return
	 */
	public DimRuleStruct getMeasureReportDim(String report_id);

	/**
	 * 获取报表
	 *
	 * @param rpt_id
	 * @param qryStruct
	 * @return
	 */
	public String[] getReportHtml(String rpt_id, Object qryStruct);

	/**
	 * 获取审核流程HTML代码
	 *
	 * @param oper_no
	 * @param oper_name
	 * @param user_role
	 * @param rpt_id
	 * @param rpt_date
	 * @param listRrocessStep
	 * @param listProcessHis
	 * @return
	 */
	public String getProcessBody(String oper_no, String oper_name,
			String user_role, String rpt_id, String rpt_date,
			List listRrocessStep, List listProcessHis);

	/**
	 * 获取审核流程HTML代码(报表打印使用)
	 *
	 * @param rptTable
	 * @param rpt_date
	 * @param region_id
	 * @return
	 */
	public String getProcessBodyPrint(Object rptTable, String rpt_date,
			String region_id);

	public void getRptBottom(RptResourceTable rptTable);
}
