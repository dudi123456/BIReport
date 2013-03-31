package com.ailk.bi.subject.admin.dao;

import java.util.List;

import com.ailk.bi.subject.admin.entity.UiPubInfoChartDef;
import com.ailk.bi.subject.admin.entity.UiPubInfoCondition;
import com.ailk.bi.subject.admin.entity.UiSubjectCommDimhierarchy;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonColDef;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonRptHead;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonTableDef;

@SuppressWarnings({ "rawtypes" })
public interface ISubjectTableDefDao {

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @param tbl_name
	 *            ：报表名称
	 * @param rpt_cycle
	 *            ：统计周期
	 * @desc:获取UI_SUBJECT_COMMON_TABLE_DEF表中信息
	 * @return
	 */
	public abstract List getSubjectCommonTblDefInfo(String tbl_id,
			String tbl_name, String rpt_cycle);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:根据报表ID获取UI_SUBJECT_COMMON_TABLE_DEF表中信息
	 * @return
	 */
	public abstract UiSubjectCommonTableDef getSubjectCommonTblDefInfo(
			String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入UI_SUBJECT_COMMON_TABLE_DEF数据
	 */
	public abstract void insertTableDef(UiSubjectCommonTableDef obj);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:根据obj删除UI_SUBJECT_COMMON_TABLE_DEF数据
	 */
	public abstract void deleteTableDef(String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入UiSubjectCommonRptHead数据
	 */
	public abstract void insertCommonRptHead(UiSubjectCommonRptHead obj);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:删除UiSubjectCommonRptHead数据
	 */
	public abstract void deleteCommonRptHead(String tbl_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:根据报表ID获取UiSubjectCommonRptHead表中信息
	 * @return
	 */
	public abstract UiSubjectCommonRptHead getCommonRptHeadInfo(String tbl_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:获取ui_subject_common_dimhierarchy表中信息
	 * @return
	 */
	public abstract List getSubjectCommonTblDefDrillInfo(String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入ui_subject_common_dimhierarchy数据
	 */
	public abstract void insertTableDefDrill(UiSubjectCommDimhierarchy obj);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:根据obj删除ui_subject_common_dimhierarchy数据
	 */
	public abstract void deleteTableDefDrill(String tbl_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:获取ui_subject_common_table_coldef表中信息
	 * @return
	 */
	public abstract List getSubjectCommonTblCoInfo(String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入ui_subject_common_table_coldef数据
	 */
	public abstract void insertCommonTblCoInfo(UiSubjectCommonColDef obj);

	/**
	 * 
	 * @param row_id
	 *            :
	 * @desc:根据报表ID获取ui_subject_common_table_coldef表中信息
	 * @return
	 */
	public abstract UiSubjectCommonColDef getSubjectCommonTblColInfo(
			String row_id);

	/**
	 * 
	 * @param row_id
	 *            :
	 * @desc:根据obj删除ui_subject_common_table_coldef数据
	 */
	public abstract void deleteTableColDef(String row_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:获取UI_PUB_INFO_CONDITION表中信息
	 * @return
	 */
	public abstract List getSubjectCommonTblConditionInfo(String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入UI_PUB_INFO_CONDITION数据
	 */
	public abstract void insertCommonTblCondition(UiPubInfoCondition obj);

	/**
	 * 
	 * @param row_id
	 *            :
	 * @desc:根据row_id获取UI_PUB_INFO_CONDITION表中信息
	 * @return
	 */
	public abstract UiPubInfoCondition getSubjectCommonTblCondition(
			String row_id);

	/**
	 * 
	 * @param row_id
	 *            :
	 * @desc:根据row_id删除UI_PUB_INFO_CONDITION数据
	 */
	public abstract void deleteTableCondition(String row_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：chart_ID
	 * @desc:获取UI_PUB_INFO_CHART_DEF表中信息
	 * @return
	 */
	public abstract UiPubInfoChartDef getSubjectCommonChartInfo(String tbl_id);

	/**
	 * 
	 * @param obj
	 *            :
	 * @desc:插入UI_PUB_INFO_CHART_DEF数据
	 */
	public abstract void insertSubjectCommonChart(UiPubInfoChartDef obj);

	/**
	 * 
	 * @param table_id
	 *            :
	 * @desc:根据table_id删除UI_PUB_INFO_CHART_DEF数据
	 */
	public abstract void deleteTableChartInfo(String table_id);

	/**
	 * 
	 * @param tbl_id
	 *            ：报表ID
	 * @desc:获取UI_PUB_INFO_CHART_DEF表中信息
	 * @return
	 */
	public abstract List getSubjectCommonChartInfoList(String tbl_id);

}
