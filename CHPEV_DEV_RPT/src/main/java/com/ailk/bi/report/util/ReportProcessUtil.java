package com.ailk.bi.report.util;

import java.util.List;

import com.ailk.bi.base.util.CObjKnd;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.domain.RptProcessHisTable;
import com.ailk.bi.report.domain.RptProcessStateTable;
import com.ailk.bi.report.domain.RptProcessStepTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;

import org.apache.log4j.Logger;

/**
 * 报表流程处理方法
 * 
 * @author renhui
 * 
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class ReportProcessUtil {

	private Logger logger = Logger.getLogger(ReportProcessUtil.class);

	/**
	 * 审核报表的数据可见性
	 * 
	 * @param listProcessStep
	 * @param listProcessHis
	 * @param user_role
	 * @return
	 */
	public static boolean visibleReportData(List listProcessStep,
			List listProcessHis, String user_role) {
		boolean visible = false;

		// 获取审核历史信息
		if (listProcessHis != null && listProcessHis.size() > 0) {
			RptProcessHisTable nowStatus = (RptProcessHisTable) listProcessHis
					.get(0);
			if (ReportConsts.YES.equals(nowStatus.p_decision)
					&& ReportConsts.STEP_FINAL.equals(nowStatus.p_step_flag)
					&& ReportConsts.STEP_FINAL.equals(nowStatus.p_step_next)) {
				// 审核步骤已经完成，数据肯定可见
				return true;
			}
		}

		// 当前的审核步骤
		String nowStep = getRptNowStep(listProcessStep, listProcessHis);
		// 数据可见步骤
		String visible_step = getDefineVisibleDataStep(listProcessStep);
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep
					.get(i);
			// 审核步骤角色
			String step_role = ",'" + pStep.r_role_id + "',";
			// 用户角色
			user_role = "," + user_role + ",";

			if (user_role.indexOf(CObjKnd.PREOCESS_RETURN_ROLE) >= 0) {
				visible = true;
				break;
			}
			if (user_role.indexOf(step_role) >= 0) {
				visible = true;
				break;
			}
			if (nowStep.equals(pStep.p_step_flag)) {
				break;
			}
			if (visible_step.equals(pStep.p_step_flag)) {
				visible = true;
				break;
			}
		}
		return visible;
	}

	/**
	 * 获取报表当前审核信息
	 * 
	 * @param rptTable
	 * @param rpt_date
	 * @return
	 */
	public static RptProcessStateTable getRptStepInfo(Object rptTable,
			String rpt_date) {
		List listProcessStep = null;
		List listProcessHis = null;
		try {
			// 定义报表操作对象
			ILReportService rptService = new LReportServiceImpl();
			listProcessStep = rptService.getReportProcessStep(rptTable,
					rpt_date);
			listProcessHis = rptService
					.getReportProcessStep(rptTable, rpt_date);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return getRptProcessState(listProcessStep, listProcessHis);
	}

	/**
	 * 获取报表当前审核信息
	 * 
	 * @param listProcessStep
	 * @param listProcessHis
	 * @return
	 */
	public static RptProcessStateTable getRptProcessState(List listProcessStep,
			List listProcessHis) {
		RptProcessStateTable state = new RptProcessStateTable();
		if (listProcessStep != null && listProcessStep.size() > 0) {
			state.p_step_length = Integer.toString(listProcessStep.size());
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep
					.get(0);
			state.p_id = pStep.p_id;
			state.rpt_date = pStep.rpt_date;
		} else {
			state.p_step_length = "0";
			state.p_id = "";
		}
		// 当前审核状态
		state.now_decision = getRptNowDecision(listProcessHis);
		// 当前审核步骤
		state.now_step = getRptNowStep(listProcessStep, listProcessHis);
		// 当前审核步骤名称
		state.now_step_name = getRptStepName(listProcessStep, state.now_step);
		// 下步审核步骤
		state.next_step = getRptNextStep(listProcessStep, state.now_step);
		// 下步审核步骤名称
		state.next_step_name = getRptStepName(listProcessStep, state.next_step);
		return state;
	}

	/**
	 * 审核流程是否完毕
	 * 
	 * @param listProcessHis
	 * @return
	 */
	public static boolean hasProcessOver(List listProcessHis) {
		boolean isTrue = false;
		if (listProcessHis == null || listProcessHis.size() == 0) {
			return isTrue;
		}

		RptProcessHisTable nowState = (RptProcessHisTable) listProcessHis
				.get(0);
		if (ReportConsts.YES.equals(nowState.p_decision)
				&& ReportConsts.STEP_FINAL.equals(nowState.p_step_flag)
				&& ReportConsts.STEP_FINAL.equals(nowState.p_step_next)) {
			isTrue = true;
		}
		return isTrue;
	}

	/**
	 * 获取当前的审核意见
	 * 
	 * @param hisTable
	 * @return
	 */
	private static String getRptNowDecision(List listProcessHis) {
		if (listProcessHis == null || listProcessHis.size() == 0) {
			return "";
		}
		RptProcessHisTable hisTable = (RptProcessHisTable) listProcessHis
				.get(0);
		return hisTable.p_decision;
	}

	/**
	 * 获取当前报表所处的审核步骤
	 * 
	 * @param listProcessStep
	 * @param listProcessHis
	 * @return
	 */
	private static String getRptNowStep(List listProcessStep,
			List listProcessHis) {
		if (listProcessStep == null)
			return "";

		String now_step = ReportConsts.STEP_FIRST;
		if (listProcessStep.size() == 1) {
			now_step = ReportConsts.STEP_FINAL;
		} else {
			if (listProcessHis != null && listProcessHis.size() > 0) {
				RptProcessHisTable nowStatus = (RptProcessHisTable) listProcessHis
						.get(0);
				now_step = nowStatus.p_step_next;
			}
		}
		if (now_step == null || "".equals(now_step))
			now_step = ReportConsts.STEP_FIRST;
		return now_step;
	}

	/**
	 * 获取当前报表所处的审核步骤名称
	 * 
	 * @param pStep
	 * @param hisTable
	 * @return
	 */
	public static String getRptStepName(List listProcessStep, String step) {
		if (step == null || "".equals(step))
			return "";

		String step_name = "";
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep
					.get(i);
			if (step.equals(pStep.p_step_flag))
				step_name = pStep.p_step_flag_name;
		}
		return step_name;
	}

	/**
	 * 获取当前报表下步审核步骤标示
	 * 
	 * @param listProcessStep
	 * @param now_step
	 * @return
	 */
	private static String getRptNextStep(List listProcessStep, String now_step) {
		String next_step = now_step;
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable nowStep = (RptProcessStepTable) listProcessStep
					.get(i);
			if (now_step.equals(nowStep.p_step_flag)) {
				if (i == listProcessStep.size() - 1) {
					next_step = ReportConsts.STEP_FINAL;
				} else {
					RptProcessStepTable nextStep = (RptProcessStepTable) listProcessStep
							.get(i + 1);
					next_step = nextStep.p_step_flag;
				}
			}
		}
		return next_step;
	}

	/**
	 * 流程定义中可见数据的步骤
	 * 
	 * @param listProcessStep
	 * @return
	 */
	private static String getDefineVisibleDataStep(List listProcessStep) {
		String visible_step = ReportConsts.STEP_FINAL;
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep
					.get(i);
			if (ReportConsts.YES.equals(pStep.p_step_visible_data)) {
				visible_step = pStep.p_step_flag;
				break;
			}
		}
		return visible_step;
	}

}
