package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportProcessDao;
import com.ailk.bi.report.domain.RptProcessHisTable;
import com.ailk.bi.report.domain.RptProcessStepTable;
import com.ailk.bi.report.domain.RptProcessTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.util.ReportConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LReportProcessDao implements ILReportProcessDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getProcess(java.lang.String)
	 */
	public Object getProcess(String p_id) throws AppException {
		if (StringTool.checkEmptyString(p_id))
			throw new AppException();

		RptProcessTable processTable = null;
		String whereStr = "AND P_ID='" + p_id + "'";
		List processes = getProcesses(whereStr);
		if (processes != null && processes.size() > 0) {
			processTable = (RptProcessTable) processes.toArray()[0];
		}
		return processTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getProcessDefine(java.lang.String
	 * )
	 */
	public List getProcesses(String whereStr) throws AppException {
		List processes = null;
		String strSql = SQLGenator.genSQL("Q3210", whereStr);
		// System.out.println("Sql Q3210==>" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			processes = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptProcessTable processTable = RptProcessTable
						.genProcessFromArray(svces[i]);
				processes.add(processTable);
			}
		}
		return processes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#insertProcess(java.lang.Object)
	 */
	public void insertProcess(Object RptProcessTable) throws AppException {
		if (RptProcessTable == null)
			throw new AppException();

		RptProcessTable processTable = (RptProcessTable) RptProcessTable;
		List svc = new ArrayList();
		svc.add(processTable.p_id);
		svc.add(processTable.p_flag_name);
		svc.add(processTable.p_note);
		svc.add(processTable.status);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("I3212", param);
		// System.out.println("Sql I3212==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#updateProcess(java.lang.Object)
	 */
	public void updateProcess(Object RptProcessTable) throws AppException {
		if (RptProcessTable == null)
			throw new AppException();

		RptProcessTable processTable = (RptProcessTable) RptProcessTable;
		List svc = new ArrayList();
		svc.add(processTable.p_flag_name);
		svc.add(processTable.p_note);
		svc.add(processTable.status);
		svc.add(processTable.p_id);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("C3214", param);
		// System.out.println("Sql C3214==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#delProcess(java.lang.String)
	 */
	public void delProcess(String p_id) throws AppException {
		if (StringTool.checkEmptyString(p_id))
			throw new AppException();

		String strSql = SQLGenator.genSQL("D3216", p_id);
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getProcessStep(java.lang.String)
	 */
	public List getProcessStep(String p_id) throws AppException {
		if (StringTool.checkEmptyString(p_id))
			throw new AppException();

		List stepes = null;
		// 获取报表审核流程步骤角色
		String strSql = SQLGenator.genSQL("Q3230");
		// System.out.println("Sql Q3230==>" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		RptProcessStepTable[] roleTable = null;
		if (null != svces) {
			roleTable = new RptProcessStepTable[svces.length];
			for (int i = 0; i < svces.length; i++) {
				roleTable[i] = RptProcessStepTable
						.getRoleDefineFromArray(svces[i]);
			}
		}

		// 获取报表流程步骤默认定义
		strSql = SQLGenator.genSQL("Q3231", ReportConsts.ZERO, p_id);
		// System.out.println("Sql Q3231==>" + strSql);
		svces = WebDBUtil.execQryArray(strSql, "");
		RptProcessStepTable[] stepTable = null;
		if (null != svces) {
			stepTable = new RptProcessStepTable[svces.length];
			for (int i = 0; i < svces.length; i++) {
				stepTable[i] = RptProcessStepTable
						.getStepDefineFromArray(svces[i]);
			}
		}

		if (null != roleTable) {
			stepes = new ArrayList();
			for (int i = 0; i < roleTable.length; i++) {
				RptProcessStepTable pStepTable = roleTable[i];
				pStepTable.rpt_date = ReportConsts.ZERO;
				pStepTable.p_id = p_id;

				for (int j = 0; stepTable != null && j < stepTable.length; j++) {
					if (pStepTable.role_code.equals(stepTable[j].r_role_id)) {
						pStepTable.p_step_flag = stepTable[j].p_step_flag;
						pStepTable.p_step_flag_name = stepTable[j].p_step_flag_name;
						pStepTable.r_role_id = stepTable[j].r_role_id;
						pStepTable.r_role_type = stepTable[j].r_role_type;
						pStepTable.p_step_visible_data = stepTable[j].p_step_visible_data;
						pStepTable.p_step_note = stepTable[j].p_step_note;
					}
				}
				stepes.add(pStepTable);
			}
		}
		return stepes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#insertProcessSetp(java.lang.
	 * Object)
	 */
	public void insertProcessStep(List RptProcessStepTable) throws AppException {
		if (RptProcessStepTable == null || RptProcessStepTable.size() == 0)
			return;

		List tmpSQL = new ArrayList();
		Iterator iter = RptProcessStepTable.iterator();
		while (iter.hasNext()) {
			RptProcessStepTable pStepTable = (RptProcessStepTable) iter.next();
			List svc = new ArrayList();
			svc.add(pStepTable.rpt_date);
			svc.add(pStepTable.p_id);
			svc.add(pStepTable.p_step_flag);
			svc.add(pStepTable.p_step_flag_name);
			svc.add(pStepTable.r_role_id);
			svc.add(pStepTable.r_role_type);
			svc.add(pStepTable.p_step_visible_data);
			svc.add(pStepTable.p_step_note);
			String[] param = null;
			if (svc != null && svc.size() >= 0) {
				param = (String[]) svc.toArray(new String[svc.size()]);
			}
			String strSql = SQLGenator.genSQL("I3234", param);
			// System.out.println("I3234=" + strSql);
			tmpSQL.add(strSql);
		}

		String[] strSql = (String[]) tmpSQL.toArray(new String[tmpSQL.size()]);
		WebDBUtil.execTransUpdate(strSql);
	}

	/**
	 * 添加对应日期报表审核流程步骤信息
	 * 
	 * @param rpt_id
	 * @param rpt_date
	 * @throws AppException
	 */
	private void insertProcessStep(String rpt_id, String rpt_date)
			throws AppException {
		// 输入值不合法返回null值
		if (StringTool.checkEmptyString(rpt_id))
			return;
		if (StringTool.checkEmptyString(rpt_date))
			return;

		// 获取该日期的审核流程定义
		String strSql = SQLGenator.genSQL("Q3032", rpt_date, rpt_id);
		// System.out.println("Sql Q3032 REAL==>" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (svces != null && svces.length > 0) {
			return;
		}

		// 如果没有定义，则获取该报表的默认定义，即rpt_date=0的定义
		strSql = SQLGenator.genSQL("Q3032", ReportConsts.ZERO, rpt_id);
		// System.out.println("Sql Q3032 ZERO==>" + strSql);
		svces = WebDBUtil.execQryArray(strSql, "");

		List listStep = null;
		if (svces != null) {
			listStep = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptProcessStepTable pStepTable = RptProcessStepTable
						.getStepDefineFromArray(svces[i]);
				pStepTable.rpt_date = rpt_date;
				listStep.add(pStepTable);
			}
			insertProcessStep(listStep);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#delProcessSetp(java.lang.String)
	 */
	public void delProcessStep(String p_id) throws AppException {
		if (StringTool.checkEmptyString(p_id))
			throw new AppException();

		String strSql = SQLGenator.genSQL("D3236", p_id);
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getReportProcess(java.lang.String
	 * )
	 */
	public List getReportProcess(String whereStr) throws AppException {
		List rptProcess = null;
		String strSql = SQLGenator.genSQL("Q3240", whereStr);
		// System.out.println("Sql Q3240==>" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			rptProcess = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptProcessTable processTable = RptProcessTable
						.genReportProcessFromArray(svces[i]);
				rptProcess.add(processTable);
			}
		}
		return rptProcess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#insertReportProcess(java.lang
	 * .String, java.util.List, java.lang.String[])
	 */
	public void insertReportProcess(String p_id, List reportProcess,
			String[] reportCheck) throws AppException {
		if (reportProcess == null || reportProcess.size() == 0)
			throw new AppException();

		List delSql = new ArrayList();
		List updateSql = new ArrayList();
		List addSql = null;
		if (reportCheck != null) {
			addSql = new ArrayList();
		}
		String del = "";
		// 先删除关系
		for (int i = 0; i < reportProcess.size(); i++) {
			RptProcessTable rptProcess = (RptProcessTable) reportProcess.get(i);
			// 先删除显示报表中传入审核流程下所有报表
			del = SQLGenator.genSQL("D3244", rptProcess.rpt_id, p_id);
			String update = SQLGenator.genSQL("C3246", ReportConsts.NO,
					rptProcess.rpt_id, p_id);
			delSql.add(del);
			updateSql.add(update);
			// System.out.println("------"+i+"---del----"+del);
			// System.out.println("------"+i+"----update---"+update);
		}
		// 再添加新关系
		for (int i = 0; reportCheck != null && i < reportCheck.length; i++) {
			// 删除选中报表
			del = SQLGenator.genSQL("D3245", reportCheck[i]);
			// System.out.println("==="+i+"===del==="+del);
			delSql.add(del);
			String update = SQLGenator.genSQL("C3243", ReportConsts.YES,
					reportCheck[i]);
			// System.out.println("===="+i+"==update=="+update);
			addSql.add(update);
			String add = SQLGenator.genSQL("I3242", reportCheck[i], p_id);
			addSql.add(add);
			// System.out.println("===="+i+"==add=="+add);
		}
		// 执行
		String[] sqlDel = (String[]) delSql.toArray(new String[delSql.size()]);
		String[] sqlUpdate = (String[]) updateSql.toArray(new String[updateSql
				.size()]);
		String[] sqlAdd = null;
		if (addSql != null) {
			sqlAdd = (String[]) addSql.toArray(new String[addSql.size()]);
		}
		if (sqlDel != null && sqlDel.length > 0)
			WebDBUtil.execTransUpdate(sqlDel);
		if (sqlUpdate != null && sqlUpdate.length > 0)
			WebDBUtil.execTransUpdate(sqlUpdate);
		if (sqlAdd != null && sqlAdd.length > 0)
			WebDBUtil.execTransUpdate(sqlAdd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getProcessID(java.lang.String)
	 */
	public String getReportProcessID(String rpt_id) throws AppException {
		if (StringTool.checkEmptyString(rpt_id))
			throw new AppException();

		String p_id = "";
		String strSql = SQLGenator.genSQL("Q3030", rpt_id);
		String[][] arr = WebDBUtil.execQryArray(strSql, "");
		if (arr != null && arr.length == 1) {
			p_id = arr[0][1];
		}
		return p_id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getReportProcessStep(java.lang
	 * .Object, java.lang.String)
	 */
	public List getReportProcessStep(Object rptTable, String rpt_date)
			throws AppException {
		if (rptTable == null)
			throw new AppException();

		RptResourceTable report = (RptResourceTable) rptTable;
		if (StringTool.checkEmptyString(rpt_date))
			rpt_date = "0";

		List listStep = null;
		String strSql = "";
		if ("2".equals(report.rpt_type.substring(0, 1))) {
			// 固定报表数据
			strSql = "SELECT * FROM " + report.data_table + " ";
			String where = report.data_where;
			if (StringTool.checkEmptyString(where)) {
				where = "WHERE 1=1";
			}
			where += " AND " + report.data_date + "='" + rpt_date + "'";
			where += " AND ROWNUM<=1";
			strSql += where;
		} else {
			// 指标类报表数据
			strSql = SQLGenator.genSQL("Q3012", report.rpt_id, rpt_date);
		}
		// System.out.println("Sql Q3012==>" + strSql);
		String[][] rptData = WebDBUtil.execQryArray(strSql, "");
		if (rptData == null || rptData.length == 0) {
			return null;
		}

		String[][] svces = null;
		// 检查历史审核记录，如果次报表审核流程已经开始，需要按该周期定义流程完成
		List hisTable = getReportProcessHis(report.rpt_id, rpt_date, "");
		if (hisTable != null && hisTable.size() > 0) {
			strSql = SQLGenator.genSQL("Q3032", rpt_date, report.rpt_id);
			svces = WebDBUtil.execQryArray(strSql, "");
		}
		// 获取该日期的审核流程定义
		if (svces == null) {
			strSql = SQLGenator.genSQL("Q3032", rpt_date, report.rpt_id);
			svces = WebDBUtil.execQryArray(strSql, "");
		}
		// 如果没有定义，则获取该报表的默认流程定义，即rpt_date=0的定义
		if (svces == null) {
			strSql = SQLGenator.genSQL("Q3032", ReportConsts.ZERO,
					report.rpt_id);
			svces = WebDBUtil.execQryArray(strSql, "");
		}

		if (svces != null) {
			listStep = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptProcessStepTable pStepTable = RptProcessStepTable
						.getStepDefineFromArray(svces[i]);
				listStep.add(pStepTable);
			}
		}
		return listStep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#getReportProcessHis(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public List getReportProcessHis(String rpt_id, String rpt_date,
			String region_id) throws AppException {
		if (StringTool.checkEmptyString(rpt_id))
			throw new AppException();
		if (StringTool.checkEmptyString(rpt_date))
			rpt_date = ReportConsts.ZERO;

		List rptHis = null;
		String strSql = "";
		if (StringTool.checkEmptyString(region_id)) {
			strSql = SQLGenator.genSQL("Q3035", rpt_date, rpt_id);
		} else {
			strSql = SQLGenator.genSQL("Q3036", rpt_date, rpt_id, region_id);
		}
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			rptHis = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptProcessHisTable processHis = RptProcessHisTable
						.genReportProcessHisFromArray(svces[i]);
				rptHis.add(processHis);
			}
		}
		return rptHis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#insertReportProcessHis(java.
	 * lang.Object)
	 */
	public void insertReportProcessHis(Object RptHisTable) throws AppException {
		if (RptHisTable == null)
			throw new AppException();
		RptProcessHisTable processHis = (RptProcessHisTable) RptHisTable;
		List svc = new ArrayList();
		svc.add(processHis.rpt_id);
		svc.add(processHis.rpt_date);
		svc.add(processHis.region_id);
		svc.add(processHis.p_id);
		svc.add(processHis.p_decision);
		svc.add(processHis.p_step_flag);
		svc.add(processHis.p_step_next);
		svc.add(processHis.p_user_id);
		svc.add(processHis.p_user_name);
		svc.add(processHis.p_his_note);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("I3038", param);
		// System.out.println("Sql I3038==>" + strSql);
		WebDBUtil.execUpdate(strSql);
		insertProcessStep(processHis.rpt_id, processHis.rpt_date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportProcessDao#insertReportDispense(java.lang
	 * .String, java.lang.Object, java.lang.String[])
	 */
	public void insertReportDispense(String role_id, Object RptTable,
			String[] reportCheck) throws AppException {
		RptResourceTable[] rptTables = (RptResourceTable[]) RptTable;
		if (rptTables == null || rptTables.length == 0)
			throw new AppException();

		List delSql = new ArrayList();
		List updateSql = new ArrayList();
		List addSql = null;
		if (reportCheck != null) {
			addSql = new ArrayList();
		}
		// 先删除关系
		for (int i = 0; i < rptTables.length; i++) {
			RptResourceTable rptTable = (RptResourceTable) rptTables[i];
			String del = SQLGenator.genSQL("D3480", role_id, rptTable.rpt_id);
			String update = SQLGenator.genSQL("C3481", ReportConsts.NO,
					rptTable.rpt_id);
			delSql.add(del);
			updateSql.add(update);
		}
		// 再添加新关系
		for (int i = 0; reportCheck != null && i < reportCheck.length; i++) {
			String update = SQLGenator.genSQL("C3481", ReportConsts.YES,
					reportCheck[i]);
			addSql.add(update);
			String add = SQLGenator.genSQL("I3482", reportCheck[i], role_id);
			addSql.add(add);
		}
		// 执行
		String[] sqlDel = (String[]) delSql.toArray(new String[delSql.size()]);
		String[] sqlUpdate = (String[]) updateSql.toArray(new String[updateSql
				.size()]);
		String[] sqlAdd = null;
		if (addSql != null) {
			sqlAdd = (String[]) addSql.toArray(new String[addSql.size()]);
		}
		if (sqlDel != null && sqlDel.length > 0)
			WebDBUtil.execTransUpdate(sqlDel);
		if (sqlUpdate != null && sqlUpdate.length > 0)
			WebDBUtil.execTransUpdate(sqlUpdate);
		if (sqlAdd != null && sqlAdd.length > 0)
			WebDBUtil.execTransUpdate(sqlAdd);
	}
}
