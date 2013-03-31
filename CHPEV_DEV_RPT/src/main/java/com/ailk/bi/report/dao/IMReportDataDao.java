package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.report.struct.DimRuleStruct;

@SuppressWarnings({ "rawtypes" })
public interface IMReportDataDao {

	/**
	 * 获取报表指标
	 * 
	 * @param rpt_id
	 * @param roleStr
	 * @return
	 */
	public List getMeasure(String rpt_id, String roleStr);

	/**
	 * 获取报表指标数据
	 * 
	 * @param rpt_id
	 * @param region_id
	 * @param date_s
	 * @param date_e
	 * @return
	 */
	/*
	 * public String[][] getMeasureData(String rpt_id, String region_id, String
	 * date_s, String date_e);
	 */
	public List getMeasureData(String rpt_id, String region_id, String date_s,
			String date_e);

	/**
	 * 获取报表指标数据-按区域
	 * 
	 * @param measure
	 * @param region
	 * @param rpt_date
	 * @return
	 */
	public String[][] getMeasureDimData(String rptid, String measure,
			String dimValue, String rpt_date, String region_id,
			DimRuleStruct dimInfo);

	/**
	 * 获取报表区域
	 * 
	 * @param region_id
	 * @param roleStr
	 * @return
	 */
	public String[][] getRegion(String region_id, String roleStr);

	/**
	 * 获取报表显示列信息
	 * 
	 * @param rpt_id
	 * @return
	 */
	public List getMeasureShow(String rpt_id);

	/**
	 * 获取报表分析维度
	 * 
	 * @param region_id
	 * @param roleStr
	 * @return
	 */
	public DimRuleStruct getReportDim(String report_id);

	/**
	 * 获取报表分析维度数据
	 * 
	 * @param region_id
	 * @param roleStr
	 * @return
	 */
	public String[][] getReportDimValue(DimRuleStruct dimInfo);

	/**
	 * 获取指标类报表表头排序规则
	 * 
	 * @param region_id
	 * @param roleStr
	 * @return
	 */
	public String[][] getMeasureHeadRule(String rpt_id);

	/**
	 * 获取报表扩展列
	 * 
	 * @param rpt_id
	 * @return
	 */
	public String[][] getExpand(String rpt_id);
}
