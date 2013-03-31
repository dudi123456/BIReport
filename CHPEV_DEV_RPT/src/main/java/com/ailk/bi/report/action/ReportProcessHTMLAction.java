package com.ailk.bi.report.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.domain.RptProcessHisTable;
import com.ailk.bi.report.domain.RptProcessStepTable;
import com.ailk.bi.report.domain.RptProcessTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportProcessHTMLAction extends HTMLActionSupport {

	/**
	 * 报表审核流程管理
	 * 
	 * @param request
	 * @param response
	 * @throws HTMLActionException
	 */
	private static final long serialVersionUID = 1L;

	private static String process_msg = "";

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession();
		session.removeAttribute(WebKeys.ATTR_REPORT_PROCESSES);
		// 获取报表查询条件
		ReportQryStruct qryStruct = new ReportQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
		}
		// 获取操作类型
		String opType = request.getParameter("opType");
		if (StringTool.checkEmptyString(opType)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知的报表操作！");
		}
		// 获取提交操作类型
		String opSubmit = request.getParameter("opSubmit");
		if (opSubmit == null || "".equals(opSubmit)) {
			opSubmit = "view";
		}
		// 获取提交操作页面转向
		String opDirection = request.getParameter("opDirection");
		if (opDirection == null || "".equals(opDirection)) {
			opDirection = "current";
		}

		// 定义报表操作对象
		ILReportService rptService = new LReportServiceImpl();
		// 流程ID
		String p_id = request.getParameter("p_id");
		// 定义审核流程基本信息表
		RptProcessTable pTable = null;
		// 定义审核流程步骤信息表
		RptProcessStepTable[] pStepTable = null;

		if ("list".equals(opType)) {
			// 流程状态
			if (StringTool.checkEmptyString(qryStruct.rpt_status)) {
				qryStruct.rpt_status = "Y";
			}
			// 条件
			String where = "";
			if (!StringTool.checkEmptyString(qryStruct.rpt_name)) {
				where += " AND P_FLAG_NAME LIKE '%" + qryStruct.rpt_name + "%'";
			}
			if (!StringTool.checkEmptyString(qryStruct.rpt_status)) {
				where += " AND STATUS='" + qryStruct.rpt_status + "'";
			}
			RptProcessTable[] pTables = null;
			try {
				List listProcess = rptService.getProcesses(where);
				if (listProcess != null && listProcess.size() >= 0) {
					pTables = (RptProcessTable[]) listProcess
							.toArray(new RptProcessTable[listProcess.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "获取流程列表内容失败！");
			}
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESSES, pTables);
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			setNextScreen(request, "listRptProcess.screen");
		} else if ("listTree".equals(opType)) {
			RptProcessTable[] pTables = null;
			try {
				String where = " AND STATUS='" + ReportConsts.YES + "'";
				List listProcess = rptService.getProcesses(where);
				if (listProcess != null && listProcess.size() >= 0) {
					pTables = (RptProcessTable[]) listProcess
							.toArray(new RptProcessTable[listProcess.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "获取流程列表内容失败！");
			}
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESSES, pTables);
			setNextScreen(request, "rptProcessDispenseTree.screen");
		} else if ("listRptProcess".equals(opType)) {
			// 从session获取选取的流程定义
			pTable = (RptProcessTable) session
					.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
			if (pTable == null || !p_id.equals(pTable.p_id)) {
				pTable = new RptProcessTable();
				pTable.p_id = p_id;
				pTable.p_flag_name = request.getParameter("p_flag_name");
			}
			// 定义报表流程关系列表
			List listRptProcess = null;
			// 保存报表流程关系列表
			if ("save".equals(opSubmit)) {
				String[] reportCheck = request.getParameterValues("rptSel");
				listRptProcess = (List) session
						.getAttribute(WebKeys.ATTR_REPORT_PROCESS_RELT);
				try {
					rptService.insertReportProcess(pTable.p_id, listRptProcess,
							reportCheck);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "添加报表流程关系失败！");
				}
			}
			// 获取报表流程关系列表
			String where = "";
			if (!StringTool.checkEmptyString(qryStruct.rpt_kind)) {
				where += " AND (CASE WHEN A3.RES_ID='7700009' THEN A2.RES_ID ELSE A3.RES_ID END) = '"
						+ qryStruct.rpt_kind + "'";
			}
			if (!StringTool.checkEmptyString(qryStruct.rpt_cycle)) {
				where += " AND B.CYCLE = '" + qryStruct.rpt_cycle + "'";
			}
			if (!StringTool.checkEmptyString(qryStruct.rpt_name)) {
				where += " AND B.NAME LIKE '%" + qryStruct.rpt_name + "%'";
			}
			try {
				listRptProcess = rptService.getReportProcess(where);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "获取流程列表内容失败！");
			}

			session.setAttribute(WebKeys.ATTR_REPORT_PROCESS, pTable);
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_RELT,
					listRptProcess);
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			setNextScreen(request, "listRptProcessDispense.screen");
		} else if ("step1".equals(opType)) {
			// 审核流程定义步骤1

			if ("addnew".equals(opSubmit) || "view".equals(opSubmit)) {
				if (!StringTool.checkEmptyString(p_id)) {
					try {
						pTable = (RptProcessTable) rptService.getProcess(p_id);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "获取流程基本信息失败！");
					}
					try {
						List listStep = rptService.getProcessStep(p_id);
						if (listStep != null && listStep.size() > 0) {
							pStepTable = (RptProcessStepTable[]) listStep
									.toArray(new RptProcessStepTable[listStep
											.size()]);
						}
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "获取流程步骤信息失败！");
					}
				}
				session.setAttribute(WebKeys.ATTR_REPORT_PROCESS, pTable);
				session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_STEP,
						pStepTable);
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				setNextScreen(request, "rptProcessTab.screen");
			} else if ("insert".equals(opSubmit) || "update".equals(opSubmit)) {
				if ("insert".equals(opSubmit)) {
					pTable = new RptProcessTable();
					try {
						pTable.p_id = rptService.getSelfReportID();
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "获取自增ID信息失败！");
					}
				} else if ("update".equals(opSubmit)) {
					pTable = (RptProcessTable) session
							.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
				}
				pTable.p_flag_name = NullProcFactory.transNullToString(request
						.getParameter("p_flag_name").trim(),
						ReportConsts.NULL_TO_BLANK);
				pTable.status = NullProcFactory.transNullToString(
						request.getParameter("status"), ReportConsts.YES);
				pTable.p_note = NullProcFactory.transNullToString(
						request.getParameter("p_note"),
						ReportConsts.NULL_TO_BLANK);

				if ("insert".equals(opSubmit)) {
					if (rptService.existProcessName(pTable.p_flag_name)) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "该流程名称已经存在！");
					}
					try {
						rptService.insertProcess(pTable);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "添加流程定义信息失败！");
					}
				} else if ("update".equals(opSubmit)) {
					try {
						rptService.updateProcess(pTable);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "修改流程定义信息失败！");
					}
				}
				try {
					List listStep = rptService.getProcessStep(pTable.p_id);
					if (listStep != null && listStep.size() > 0) {
						pStepTable = (RptProcessStepTable[]) listStep
								.toArray(new RptProcessStepTable[listStep
										.size()]);
					}
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "获取流程步骤信息失败！");
				}

				if ("current".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				} else if ("next".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
				}
				session.setAttribute(WebKeys.ATTR_REPORT_PROCESS, pTable);
				session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_STEP,
						pStepTable);
				setNextScreen(request, "rptProcessTabBridge.screen");
			} else if ("delete".equals(opSubmit)) {
				// 从session中获取流程定义信息
				pTable = (RptProcessTable) session
						.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
				// 删除流程定义信息
				try {
					rptService.delProcess(pTable.p_id);
				} catch (AppException ex) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "删除流程定义信息操作失败！");
				}
				pTable = null;
				session.setAttribute(WebKeys.ATTR_REPORT_PROCESS, pTable);
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				setNextScreen(request, "rptProcessTabBridge.screen");
			}
		} else if ("step2".equals(opType)) {
			// 审核流程步骤定义

			// 从session中获取流程定义信息
			pTable = (RptProcessTable) session
					.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
			// 获取记录标示ID
			String[] rowcount = request.getParameterValues("col_id");
			// 数据可见的角色
			String visible_role = request.getParameter("visible_data");
			// 记录cols内容
			List listProcessStep = null;
			if (rowcount != null) {
				listProcessStep = new ArrayList();
			}
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				// 确定顺序号
				int sequence = i + 1;
				String p_step_flag = Integer.toString(sequence);
				if (i + 1 == rowcount.length) {
					p_step_flag = ReportConsts.STEP_FINAL;
				}
				// 确定数据可见
				String visible_data = ReportConsts.NO;
				if (visible_role != null && visible_role.equals(num)) {
					visible_data = ReportConsts.YES;
				} else {
					visible_data = ReportConsts.NO;
				}
				// 对象数组值
				String[] svces = genStepValue(pTable.p_id, p_step_flag,
						visible_data, num, request);
				// 定义流程步骤对象
				RptProcessStepTable step = RptProcessStepTable
						.getStepDefineFromArray(svces);
				// 增加到数组
				listProcessStep.add(step);
			}
			// 添加流程步骤定义信息
			try {
				rptService.delProcessStep(pTable.p_id);
				rptService.insertProcessStep(listProcessStep);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "保存流程步骤失败！");
			}

			// 重新获取流程步骤定义信息
			try {
				List listStep = rptService.getProcessStep(pTable.p_id);
				if (listStep != null && listStep.size() > 0) {
					pStepTable = (RptProcessStepTable[]) listStep
							.toArray(new RptProcessStepTable[listStep.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "获取流程步骤信息失败！");
			}
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_STEP, pStepTable);
			if ("current".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
			} else if ("pre".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
			}
			setNextScreen(request, "rptProcessTabBridge.screen");
		} else if ("process_auditing".equals(opType)) {
			// 流程审核

			// 获取报表基本信息
			RptResourceTable rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE);
			if (rptTable == null) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "该报表不存在！");
			}

			// 加入权限控制条件-用户控制区域
			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			if (ctlStruct == null) {
				ctlStruct = new UserCtlRegionStruct();
			}
			String user_region = ctlStruct.ctl_str_add;
			// 获取需要添加的审核内容
			RptProcessHisTable process_info = genHisTable(request, user_region);
			// 获取当前报表审核历史信息
			List listHis = null;
			try {
				listHis = rptService.getReportProcessHis(process_info.rpt_id,
						process_info.rpt_date, user_region);
			} catch (AppException e1) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "获取报表历史审核信息失败！");
			}
			RptProcessHisTable[] hisTables = null;
			if (listHis != null && listHis.size() > 0) {
				hisTables = (RptProcessHisTable[]) listHis
						.toArray(new RptProcessHisTable[listHis.size()]);
			}
			// 进行检查
			if (hasHisRecord(process_info, hisTables)) {
				session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, process_msg,
						"ReportView.rptdo?rpt_id=" + rptTable.rpt_id
								+ "&p_date=" + qryStruct.date_s);
			}

			// 添加审核操作记录
			try {
				rptService.insertReportProcessHis(process_info);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "审核操作失败！");
			}
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "审核操作成功！",
					"ReportView.rptdo?rpt_id=" + rptTable.rpt_id + "&p_date="
							+ qryStruct.date_s);
		} else if ("self_task".equals(opType)) {
			// 待办审核任务

			// 任务状态
			if (StringTool.checkEmptyString(qryStruct.rpt_status)) {
				qryStruct.rpt_status = "W";
			}
			// 统计月份
			if (StringTool.checkEmptyString(qryStruct.date_s)) {
				qryStruct.date_s = DateUtil.getDiffMonth(0,
						DateUtil.getNowDate());
			}

			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			setNextScreen(request, "rptSelfTask.screen");
		}
	}

	/**
	 * 获取流程步骤定义信息
	 * 
	 * @param p_id
	 * @param p_step_flag
	 * @param visible_data
	 * @param num
	 * @param request
	 * @return
	 */
	private static String[] genStepValue(String p_id, String p_step_flag,
			String visible_data, String num, HttpServletRequest request) {
		List svc = new ArrayList();

		// 流程对应日期，默认流程对应零
		svc.add(ReportConsts.ZERO);

		// 流程ID
		svc.add(p_id);

		// 流程步骤标志
		svc.add(p_step_flag);

		// 流程步骤名称
		String flag_name = request.getParameter("flag_name_" + num);
		svc.add(flag_name);

		// 获取角色ID
		String role_id = request.getParameter("role_id_" + num);
		svc.add(role_id);

		// 角色标示
		svc.add("2");

		// 数据可见性
		svc.add(visible_data);

		// 备注信息
		svc.add("");

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}

	/**
	 * 生成用户添加审核历史记录的UiRptInfoProcessHisTable
	 * 
	 * @param request
	 * @param user_region
	 * @return
	 */
	private static RptProcessHisTable genHisTable(HttpServletRequest request,
			String user_region) {
		// 审批意见
		String _decision = request.getParameter("p_decision");
		// 当前审核步骤
		String _now_step = request.getParameter("p_step_flag");
		// 回退审核步骤
		String _return = request.getParameter("p_return");
		// 向前审核步骤
		String _forward = request.getParameter("p_forward");
		// 默认下一审核步骤
		String _next_step = request.getParameter("p_step_next");

		RptProcessHisTable hisTable = new RptProcessHisTable();
		hisTable.rpt_id = request.getParameter("rpt_id");
		hisTable.rpt_date = request.getParameter("rpt_date");
		hisTable.region_id = user_region;
		hisTable.p_id = request.getParameter("p_id");
		hisTable.p_decision = _decision;
		hisTable.p_step_flag = _now_step;
		if (TableConsts.RETURN.equals(_decision))
			hisTable.p_step_next = _now_step;
		else if (TableConsts.NO.equals(_decision))
			hisTable.p_step_next = _return;
		else if (TableConsts.FORWARD.equals(_decision))
			hisTable.p_step_next = _forward;
		else if (TableConsts.YES.equals(_decision))
			hisTable.p_step_next = _next_step;
		hisTable.p_user_id = request.getParameter("p_user_id");
		hisTable.p_user_name = request.getParameter("p_user_name");
		hisTable.p_his_note = request.getParameter("p_step_note");
		return hisTable;
	}

	/**
	 * 检查需要录入的审核记录
	 * 
	 * @param hisTable
	 * @param hisTables
	 * @return
	 */
	private static boolean hasHisRecord(RptProcessHisTable hisTable,
			RptProcessHisTable[] hisTables) {
		boolean isTrue = false;
		String p_id = hisTable.p_id;
		String p_step_flag = hisTable.p_step_flag;
		String p_step_next = hisTable.p_step_next;

		if (hisTables != null && hisTables.length > 0) {
			String his_p_id = hisTables[0].p_id;
			String his_p_decision = hisTables[0].p_decision;
			String his_p_step_flag = hisTables[0].p_step_flag;
			String his_p_step_next = hisTables[0].p_step_next;
			String his_p_user_name = hisTables[0].p_user_name;
			int iNowStep = StringB.toInt(p_step_flag, 99);
			int iHisStep = StringB.toInt(his_p_step_flag, 99);

			if (p_id.equals(his_p_id) && TableConsts.YES.equals(his_p_decision)
					&& TableConsts.FINAL_STEP.equals(his_p_step_flag)
					&& TableConsts.FINAL_STEP.equals(his_p_step_next)) {
				// 报表审核流程结束
				isTrue = true;
				process_msg = "该报表审核流程已经结束！";
			} else if (p_id.equals(his_p_id)
					&& TableConsts.YES.equals(his_p_decision)
					&& p_step_flag.equals(his_p_step_flag)
					&& p_step_next.equals(his_p_step_next)) {
				// 报表此审核步骤已经完成
				isTrue = true;
				process_msg = "该报表此审核步骤已经由 " + his_p_user_name + " 审核完成！";
			} else if (iHisStep > iNowStep && p_id.equals(his_p_id)
					&& TableConsts.YES.equals(his_p_decision)) {
				// 报表已经进入后续审核步骤
				isTrue = true;
				process_msg = "该报表审核状态已经改变，请重新查看报表！";
			}
		}
		return isTrue;
	}
}
