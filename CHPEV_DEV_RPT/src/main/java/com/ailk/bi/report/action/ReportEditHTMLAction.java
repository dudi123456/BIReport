package com.ailk.bi.report.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptFilterTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.report.util.ReportObjUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportEditHTMLAction extends HTMLActionSupport {

	/**
	 * 本地化报表维护
	 */
	private static final long serialVersionUID = -1357393628171937864L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession();
		// 获取用户信息
		String oper_no = CommonFacade.getLoginId(session);
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

		logcommon.debug("***opSubmit***" + opSubmit + "****opDirection***"
				+ opDirection + "******opType*****" + opType + "*****");
		// logcommon.debug("========================"+session.getAttribute("opSubmit"));
		// 定义报表操作对象
		ILReportService rptService = new LReportServiceImpl();
		// 获取传入报表ID
		String rpt_id = request.getParameter("rpt_id");
		// 定义报表信息表
		RptResourceTable rptTable = null;
		// 定义报表列信息
		RptColDictTable[] rptColTables = null;
		// 定义报表条件信息
		RptFilterTable[] rptFilterTables = null;

		// 如果是查看状态，首先获取报表信息
		if ("view".equals(opSubmit) && !StringTool.checkEmptyString(rpt_id)) {
			// 获取报表信息
			try {
				rptTable = (RptResourceTable) rptService.getReport(rpt_id);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表信息失败！");
			}
			// 获取报表列定义信息
			try {
				List listRptCol = rptService
						.getReportColDefine(rptTable.rpt_id);
				if (listRptCol != null && listRptCol.size() >= 0) {
					rptColTables = (RptColDictTable[]) listRptCol
							.toArray(new RptColDictTable[listRptCol.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表列信息失败！");
			}
			// 获取报表条件定义信息
			try {
				List listRptFilter = rptService
						.getReportFilterDefine(rptTable.rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
			}
			// 报表信息第一次显示时，清空新增标志
			// logcommon.debug("------------清空----------------");
			session.setAttribute("opSubmit", "");

			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
			session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE, rptColTables);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
					rptFilterTables);
			session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
			setNextScreen(request, "localRptTab.screen");
		}

		// 基本属性
		if ("step1".equals(opType)) {
			if ("addLocal".equals(opSubmit)) {
				rptTable = getLocalInit();
				session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
				session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE,
						rptColTables);
				session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
						rptFilterTables);
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				setNextScreen(request, "localRptTab.screen");
			}
			if ("insert".equals(opSubmit) || "update".equals(opSubmit)) {
				// 添加新报表先检查报表ID
				if ("insert".equals(opSubmit) && rptService.existReport(rpt_id)) {
					session.setAttribute(WebKeys.ATTR_ANYFLAG, "2");
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "该报表ID已经存在！");
				}

				// 从session中获取报表信息
				rptTable = (RptResourceTable) session
						.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
				rptTable.rpt_id = rpt_id;
				rptTable.local_res_code = request
						.getParameter("local_res_code");
				rptTable.name = request.getParameter("name");
				rptTable.cycle = request.getParameter("cycle");
				rptTable.start_date = request.getParameter("start_date");
				rptTable.privateflag = request.getParameter("privateflag");
				if (ReportConsts.YES.equals(rptTable.privateflag)) {
					rptTable.parent_id = request.getParameter("parent_id2");
				} else {
					rptTable.parent_id = request.getParameter("parent_id");
				}
				rptTable.title_note = request.getParameter("title_note");
				rptTable.rpt_type = request.getParameter("rpt_type");
				String pagecount_type = request.getParameter("pagecount_type");
				if (ReportConsts.YES.equals(pagecount_type)) {
					int ipagecount = StringB.toInt(
							request.getParameter("pagecount"), 0);
					rptTable.pagecount = Integer.toString(ipagecount);
				} else {
					rptTable.pagecount = "0";
				}
				rptTable.data_table = request.getParameter("data_table");
				rptTable.data_date = request.getParameter("data_date");
				rptTable.data_where = request.getParameter("data_where");
				rptTable.needdept = request.getParameter("needdept");
				rptTable.needperson = request.getParameter("needperson");
				rptTable.needreason = request.getParameter("needreason");
				rptTable.inputnote = request.getParameter("inputnote");
				rptTable.filldept = request.getParameter("filldept");
				rptTable.rp_status = request.getParameter("rp_status");
				rptTable.rp_engine = request.getParameter("rp_engine");
				if ("insert".equals(opSubmit)) {
					try {
						rptTable.d_user_id = oper_no;
						if (StringTool.checkEmptyString(rptTable.data_where)
								&& ReportConsts.RPT_DEFAUTL_DATATABLE
										.equals(rptTable.data_table)) {
							rptTable.data_where = "WHERE RPT_ID='"
									+ rptTable.rpt_id + "'";
						}
						rptService.insertReport(rptTable);
						// 设置新增标志
						// logcommon.debug("-------------设置---------------");
						session.setAttribute("opSubmit", opSubmit);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "添加报表失败！");
					}
				} else if ("update".equals(opSubmit)) {
					try {
						rptService.updateReport(rptTable);
						rptColTables = (RptColDictTable[]) session
								.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "修改报表信息失败！");
					}
				}

				session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
				session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE,
						rptColTables);
				session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
						rptFilterTables);
				if ("current".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				} else if ("next".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
				}
				setNextScreen(request, "localRptTabBridge.screen");
			}
			if ("delete".equals(opSubmit)) {
				// 从session中获取报表信息
				rptTable = (RptResourceTable) session
						.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
				try {
					rptService.delReport(rptTable.rpt_id);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "删除报表信息失败！");
				}

				rptTable = getLocalInit();
				session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
				setNextScreen(request, "localRptTabBridge.screen");
			}
		}

		// 选择模版
		if ("step2".equals(opType)) {
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
			// 获取指标体系支撑报表标志
			String rpt_metaflag = request.getParameter("rpt_metaflag");
			logcommon.debug("rpt_metaflag:" + rpt_metaflag);
			if (!rpt_metaflag.equals(rptTable.metaflag)) {
				try {
					rptTable.metaflag = rpt_metaflag;
					rptService.updateReport(rptTable);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "更新报表状态信息失败！");
				}
				try {
					rptService.delReportCol(rptTable.rpt_id);
					rptService.delReportFilter(rptTable.rpt_id);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "删除报表列信息失败！");
				}
				session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE,
						rptColTables);
				session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
						rptFilterTables);
			}

			// 对于非指标体系支撑的固定报表更新描述列和数值列个数
			if (ReportConsts.NO.equals(rptTable.metaflag)) {
				// 从session里面获取报表列信息对象
				rptColTables = (RptColDictTable[]) session
						.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);

				// 用来记录需要新添加列信息
				List listRptCol = new ArrayList();
				int iCharLen = 0;// 描述列的总数
				int maxCharSequence = 0;// 记录最大描述列的序号
				int iNumLen = 0;// 数值列的总数
				int maxNumSequence = 20;// 记录最大数值列的序号
				for (int i = 0; rptColTables != null && i < rptColTables.length; i++) {
					if (ReportConsts.DATA_TYPE_STRING
							.equals(rptColTables[i].data_type)) {
						iCharLen++;
						int tmpSequence = Integer
								.parseInt(rptColTables[i].col_sequence);
						if (tmpSequence < 20) {
							maxCharSequence = tmpSequence;
						}
					}
					if (ReportConsts.DATA_TYPE_NUMBER
							.equals(rptColTables[i].data_type)) {
						iNumLen++;
						maxNumSequence = Integer
								.parseInt(rptColTables[i].col_sequence);
					}
				}

				// 获取描述列定义数量
				int charcount = Integer.parseInt(request
						.getParameter("charcount"));
				for (int i = iCharLen; i < charcount; i++) {
					int sequence = maxCharSequence + 1;
					maxCharSequence++;
					// 对象数组值
					String[] svces = getColValueInit(rptTable.rpt_id,
							ReportConsts.DATA_TYPE_STRING,
							Integer.toString(i + 1), Integer.toString(sequence));
					// 定义报表列对象
					RptColDictTable reportCol = RptColDictTable
							.genReportColFromArray(svces);
					// 增加到数组
					listRptCol.add(reportCol);
				}
				// 获取数值列定义数量
				int numcount = Integer.parseInt(request
						.getParameter("numcount"));
				for (int i = iNumLen; i < numcount; i++) {
					int sequence = maxNumSequence + 1;
					maxNumSequence++;
					// 对象数组值
					String[] svces = getColValueInit(rptTable.rpt_id,
							ReportConsts.DATA_TYPE_NUMBER,
							Integer.toString(i + 1), Integer.toString(sequence));
					// 定义报表列对象
					RptColDictTable reportCol = RptColDictTable
							.genReportColFromArray(svces);
					// 增加到数组
					listRptCol.add(reportCol);
				}
				// 判断是否需要添加新的列定义
				if (listRptCol != null && listRptCol.size() > 0) {
					// 添加列定义信息
					try {
						rptService.insertReportCol(listRptCol);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "添加报表列信息失败！");
					}
					// 重新读取列定义
					try {
						listRptCol = rptService
								.getReportColDefine(rptTable.rpt_id);
						if (listRptCol != null && listRptCol.size() >= 0) {
							rptColTables = (RptColDictTable[]) listRptCol
									.toArray(new RptColDictTable[listRptCol
											.size()]);
						}
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "获取报表列信息失败！");
					}
					session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE,
							rptColTables);
					// 获取报表条件定义信息
					try {
						List listRptFilter = rptService
								.getReportFilterDefine(rptTable.rpt_id);
						if (listRptFilter != null && listRptFilter.size() >= 0) {
							rptFilterTables = (RptFilterTable[]) listRptFilter
									.toArray(new RptFilterTable[listRptFilter
											.size()]);
						}
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
					}
					session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
							rptFilterTables);
				}
			}

			// 更新模版定义格式
			String[] moban = request.getParameter("moban").split(",");
			if (!rptTable.ishead.equals(moban[0])
					|| !rptTable.isleft.equals(moban[1])) {
				try {
					rptTable.ishead = moban[0];
					rptTable.isleft = moban[1];
					rptService.updateReport(rptTable);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "修改报表信息失败！");
				}
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
			if ("current".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
			} else if ("pre".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "1");
			} else if ("next".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
			}
			setNextScreen(request, "localRptTabBridge.screen");
		}

		// 选择指标
		if ("customrpt_measure".equals(opType)) {
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);

			// 获取记录标示ID
			String[] rowcount = request.getParameterValues("col_id");
			// 记录cols内容
			List listRptCol = null;
			if (rowcount != null && rowcount.length > 0) {
				listRptCol = new ArrayList();
			}
			// 根据报表列数取得传进的值，要注意的是并不是每个值都有
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				// 确定顺序号，遇到number从20+开始编号
				int sequence = i + 21;
				// 对象数组值
				String[] svces = getColValueInit(rptTable.rpt_id,
						ReportConsts.DATA_TYPE_NUMBER, Integer.toString(i + 1),
						num, Integer.toString(sequence), request);
				// 定义报表列对象
				RptColDictTable reportCol = RptColDictTable
						.genReportColFromArray(svces);
				// 增加到数组
				listRptCol.add(reportCol);
			}
			// 添加列定义信息
			try {
				rptService.delReportNumberCol(rptTable.rpt_id);
				rptService.insertReportCol(listRptCol);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "修改报表列信息失败！");
			}
			// 获取列定义信息
			if (rowcount == null || rowcount.length == 0) {
				try {
					rptService.delReportCharCol(rptTable.rpt_id);
					rptService.delReportFilter(rptTable.rpt_id);
					// 删除所有指标后，将认证状态置为不可用
					rptTable.status = ReportConsts.NO;
					rptService.updateReport(rptTable);
				} catch (AppException e) {
					e.printStackTrace();
				}
				session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE, null);
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"该报表已经没有任何指标，选择指标后才可以正确显示！", "localRptTabBridge.screen");
			} else {
				try {
					listRptCol = rptService.getReportColDefine(rptTable.rpt_id);
					if (listRptCol != null && listRptCol.size() >= 0) {
						rptColTables = (RptColDictTable[]) listRptCol
								.toArray(new RptColDictTable[listRptCol.size()]);
					}
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "获取报表列信息失败！");
				}
				session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE,
						rptColTables);
			}

			// 设置session值，跳转页面
			if ("current".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
			} else if ("pre".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
			} else if ("next".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
			}
			setNextScreen(request, "localRptTabBridge.screen");
		}

		// 选择维度
		if ("customrpt_dim".equals(opType)) {
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);

			// 获取记录标示ID
			String[] rowcount = request.getParameterValues("col_id");
			// 记录cols内容
			List listRptCol = null;
			if (rowcount != null && rowcount.length > 0) {
				listRptCol = new ArrayList();
			}
			// 根据报表列数取得传进的值，要注意的是并不是每个值都有
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				// 确定顺序号
				int sequence = i + 1;
				// 对象数组值
				String[] svces = getColValueInit(rptTable.rpt_id,
						ReportConsts.DATA_TYPE_STRING, Integer.toString(i + 1),
						num, Integer.toString(sequence), request);
				// 定义报表列对象
				RptColDictTable reportCol = RptColDictTable
						.genReportColFromArray(svces);
				// 增加到数组
				listRptCol.add(reportCol);
			}
			// 添加列定义信息
			try {
				rptService.delReportCharCol(rptTable.rpt_id);
				rptService.insertReportCol(listRptCol);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "修改报表列信息失败！");
			}
			// 删除条件信息
			if (rowcount == null || rowcount.length == 0) {
				try {
					rptService.delReportFilter(rptTable.rpt_id);
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
			// 获取列定义信息
			try {
				listRptCol = rptService.getReportColDefine(rptTable.rpt_id);
				if (listRptCol != null && listRptCol.size() >= 0) {
					rptColTables = (RptColDictTable[]) listRptCol
							.toArray(new RptColDictTable[listRptCol.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表列信息失败！");
			}
			// 获取报表条件定义信息
			try {
				List listRptFilter = rptService
						.getReportFilterDefine(rptTable.rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE, rptColTables);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
					rptFilterTables);
			if ("current".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
			} else if ("pre".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
			} else if ("next".equals(opDirection)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
			}
			setNextScreen(request, "localRptTabBridge.screen");
		}

		// 展示定制（报表列定义操作）
		if ("step3".equals(opType)) {
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
			// 获取记录标示ID
			String[] rowcount = request.getParameterValues("col_id");
			if (rowcount == null || rowcount.length == 0) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "没有任何需要记录的内容！");
			}

			// 记录cols内容
			List listRptCol = new ArrayList();
			boolean isNumber = false;
			// 根据报表列数取得传进的值，要注意的是并不是每个值都有
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				// 获取该列的数据类型
				String data_type = request.getParameter("data_type_" + num);
				// 确定顺序号，遇到number从20+开始编号
				int sequence = i + 1;
				if (ReportConsts.DATA_TYPE_NUMBER.equals(data_type)) {
					isNumber = true;
				}
				if (isNumber) {
					sequence = 20 + sequence;
				}
				// 对象数组值
				String[] svces = getColValue(rptTable.rpt_id, num,
						Integer.toString(sequence), request);
				// 定义报表列对象
				RptColDictTable reportCol = RptColDictTable
						.genReportColFromArray(svces);
				// 增加到数组
				listRptCol.add(reportCol);
			}
			// 添加列定义信息
			try {
				rptService.delReportCol(rptTable.rpt_id);
				rptService.insertReportCol(listRptCol);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "修改报表列信息失败！");
			}
			// 获取列定义信息
			if (listRptCol != null && listRptCol.size() >= 0) {
				rptColTables = (RptColDictTable[]) listRptCol
						.toArray(new RptColDictTable[listRptCol.size()]);
			}
			String startcol = getExpandcol(listRptCol);
			if (!startcol.equals(rptTable.startcol)) {
				rptTable.startcol = startcol;
				try {
					rptService.updateReport(rptTable);
				} catch (AppException e) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "修改报表信息失败！");
				}
			}
			// 获取报表条件定义信息
			try {
				List listRptFilter = rptService
						.getReportFilterDefine(rptTable.rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
			session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE, rptColTables);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
					rptFilterTables);
			if (rptTable != null && ReportConsts.YES.equals(rptTable.metaflag)) {
				if (ReportConsts.YES.equals(rptTable.status)) {
					if ("current".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
					} else if ("pre".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
					} else if ("next".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
					}
				} else {
					if ("current".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
					} else if ("pre".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
					} else if ("next".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "6");
					}
				}
			} else {
				if ("current".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
				} else if ("pre".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "2");
				} else if ("next".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
				}
			}
			setNextScreen(request, "localRptTabBridge.screen");
		}

		// 条件定制（报表条件信息操作）
		if ("step4".equals(opType)) {
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
			// 获取记录标示ID
			String[] rowcount = request.getParameterValues("col_id");
			// if (rowcount == null || rowcount.length == 0) {
			// throw new HTMLActionException(session,
			// HTMLActionException.WARN_PAGE, "没有任何需要记录的内容！");
			// }

			// 记录filters内容
			List listRptFilter = null;
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				String dim_conditon = request.getParameter("dim_conditon_"
						+ num);
				if (dim_conditon != null)
					dim_conditon = ReportConsts.YES;
				else
					dim_conditon = ReportConsts.NO;
				if (ReportConsts.YES.equals(dim_conditon)) {
					listRptFilter = new ArrayList();
					break;
				}
			}
			// 根据报表列数取得传进的值，要注意的是并不是每个值都有
			for (int i = 0; rowcount != null && i < rowcount.length; i++) {
				// 获取指定行号标示
				String num = rowcount[i];
				// 确定顺序号
				int sequence = i + 1;
				String dim_conditon = request.getParameter("dim_conditon_"
						+ num);
				if (dim_conditon != null)
					dim_conditon = ReportConsts.YES;
				else
					dim_conditon = ReportConsts.NO;
				if (ReportConsts.YES.equals(dim_conditon)) {
					// 对象数组值
					String[] svces = getFilterValue(rptTable.rpt_id, num,
							Integer.toString(sequence), request);
					// 定义报表条件对象
					RptFilterTable rptFilter = RptFilterTable
							.genReportFilterDefineFromArray(svces);
					// 增加到数组
					listRptFilter.add(rptFilter);
				}
			}
			// 添加条件定义信息
			try {
				rptService.delReportFilter(rptTable.rpt_id);
				rptService.insertReportFilter(listRptFilter);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "修改报表条件信息失败！");
			}
			// 获取报表条件定义信息
			try {
				listRptFilter = rptService
						.getReportFilterDefine(rptTable.rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
					rptFilterTables);
			if (rptTable != null && ReportConsts.YES.equals(rptTable.metaflag)) {
				if (ReportConsts.YES.equals(rptTable.status)) {
					if ("current".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
					} else if ("pre".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
					} else if ("next".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
					}
				} else {
					if ("current".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "6");
					} else if ("pre".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
					} else if ("next".equals(opDirection)) {
						session.setAttribute(WebKeys.ATTR_REPORT_STEP, "7");
					}
				}
			} else {
				if ("current".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "4");
				} else if ("pre".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "3");
				} else if ("next".equals(opDirection)) {
					session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
				}
			}
			setNextScreen(request, "localRptTabBridge.screen");
		}

		// 报表认证操作
		if ("attstation".equals(opType)) {
			// 进入报表认证页面，获取报表信息
			try {
				rptTable = (RptResourceTable) rptService.getReport(rpt_id);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表信息失败！");
			}
			rptTable.res_id = ReportConsts.RPT_STATUS_WAIT;
			// 获取报表列定义信息
			try {
				List listRptCol = rptService
						.getReportColDefine(rptTable.rpt_id);
				if (listRptCol != null && listRptCol.size() >= 0) {
					rptColTables = (RptColDictTable[]) listRptCol
							.toArray(new RptColDictTable[listRptCol.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表列信息失败！");
			}
			// 获取报表条件定义信息
			try {
				List listRptFilter = rptService
						.getReportFilterDefine(rptTable.rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "获取报表条件信息失败！");
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
			session.setAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE, rptColTables);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE,
					rptFilterTables);
			if (rptTable != null && ReportConsts.YES.equals(rptTable.metaflag)) {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "7");
			} else {
				session.setAttribute(WebKeys.ATTR_REPORT_STEP, "5");
			}
			setNextScreen(request, "localRptTab.screen");
		}

		// 更新报表状态
		if ("change_status".equals(opType)) {
			// 更新报表各个状态
			String status = request.getParameter("status");
			// 从session中获取报表信息
			rptTable = (RptResourceTable) session
					.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
			try {
				// 更新报表状态
				if (!StringTool.checkEmptyString(status)) {
					rptTable.status = status;
					rptService.updateReport(rptTable);
				}
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "修改报表状态失败！");
			}

			// 设置session值，跳转页面
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
			setNextScreen(request, "localRptTab.screen");
		}

	}

	/**
	 * 获取固定报表初始化对象
	 *
	 * @return
	 */
	private static RptResourceTable getLocalInit() {
		RptResourceTable rptTable = new RptResourceTable();
		rptTable.cycle = SysConsts.STAT_PERIOD_MONTH;
		rptTable.start_date = SysConsts.STAT_PERIOD_LAST;
		rptTable.rpt_type = ReportConsts.RPT_HAS_SUMROW;
		rptTable.pagecount = ReportConsts.ZERO;
		rptTable.ishead = ReportConsts.NO;
		rptTable.isleft = ReportConsts.NO;
		rptTable.startcol = ReportConsts.NO;
		rptTable.dispenseflag = ReportConsts.NO;
		rptTable.metaflag = ReportConsts.NO;
		rptTable.privateflag = ReportConsts.NO;
		rptTable.data_table = ReportConsts.RPT_DEFAUTL_DATATABLE;
		rptTable.data_date = ReportConsts.RPT_DEFAULT_DATADATE;
		rptTable.status = ReportConsts.NO;
		return rptTable;
	}

	/**
	 * 初始化列定义值范围
	 *
	 * @param rpt_id
	 * @param data_type
	 * @param num
	 * @param sequence
	 * @return
	 */
	private static String[] getColValueInit(String rpt_id, String data_type,
			String num, String sequence) {
		List svc = new ArrayList();

		// 报表ID
		svc.add(rpt_id);

		// 列顺序
		svc.add(sequence);

		// 是否显示
		String default_display = ReportConsts.YES;
		svc.add(default_display);

		// 是否显示编码
		String dim_code_display = ReportConsts.NO;
		svc.add(dim_code_display);

		// 是否展开列
		String is_expand_col = ReportConsts.NO;
		svc.add(is_expand_col);

		// 是否进行小计
		String is_subsum = ReportConsts.NO;
		svc.add(is_subsum);

		// 合计值是否有效
		String valuable_sum = ReportConsts.YES;
		svc.add(valuable_sum);

		// 编码字段代码
		String field_dim_code = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_dim_code = "DATA_INT" + num;
		}
		svc.add(field_dim_code);

		// 显示字段代码
		String field_code = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_code = "DATA_CHAR" + num;
		} else {
			field_code = "DATA_NUM" + num;
		}
		svc.add(field_code);

		// 字段描述
		String field_title = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_title = "描述列名称" + num;
		} else {
			field_title = "数值列名称" + num;
		}
		svc.add(field_title);

		// 数据类型
		svc.add(data_type);

		// 指标精度
		String msu_length = "0";
		svc.add(msu_length);

		// 计量单位
		String msu_unit = "";
		svc.add(msu_unit);

		// 是否千分位分隔
		String comma_splitted = ReportConsts.YES;
		svc.add(comma_splitted);

		// 0 值处理
		String zero_proc = ReportConsts.ZERO_TO_ZERO;
		svc.add(zero_proc);

		// 数据转化为%
		String ratio_displayed = ReportConsts.NO;
		svc.add(ratio_displayed);

		// 是否占比
		String has_comratio = ReportConsts.NO;
		svc.add(has_comratio);

		// 是否同比
		String has_same = ReportConsts.NO;
		svc.add(has_same);

		// 是否环比
		String has_last = ReportConsts.NO;
		svc.add(has_last);

		// 是否链接
		String has_link = ReportConsts.NO;
		svc.add(has_link);

		// 链接URL
		String link_url = "";
		svc.add(link_url);

		// 链接TARGET
		String link_target = ReportConsts.TARGET_SELF;
		svc.add(link_target);

		// 是否排序
		String data_order = ReportConsts.NO;
		svc.add(data_order);

		// 短信标志
		String sms_flag = ReportConsts.NO;
		svc.add(sms_flag);

		// 文字是否换行
		String td_wrap = ReportConsts.NO;
		svc.add(td_wrap);

		// 表头样式
		String title_style = "";
		svc.add(title_style);

		// 列样式
		String col_style = "";
		svc.add(col_style);

		// 打印表头样式
		String print_title_style = "";
		svc.add(print_title_style);

		// 打印列样式
		String print_col_style = "";
		svc.add(print_col_style);

		// 是否预警
		String need_alert = ReportConsts.NO;
		svc.add(need_alert);

		// 比较对象
		String compare_to = ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD;
		svc.add(compare_to);

		// 是否比率
		String ratio_compare = ReportConsts.YES;
		svc.add(ratio_compare);

		// 阈值上限
		String high_value = "0.1";
		svc.add(high_value);

		// 阈值下限
		String low_value = "-0.1";
		svc.add(low_value);

		// 预警方式
		String alert_mode = ReportConsts.ALERT_MODE_ARROW;
		svc.add(alert_mode);

		// 阈值大于上限颜色
		String rise_color = ReportConsts.ALERT_COLOR_GREEN;
		svc.add(rise_color);

		// 阈值小于下限颜色
		String down_color = ReportConsts.ALERT_COLOR_RED;
		svc.add(down_color);

		// 是否指标字段
		String is_msu = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			is_msu = ReportConsts.NO;
		} else {
			is_msu = ReportConsts.YES;
		}
		svc.add(is_msu);

		// 是否衍生指标
		String is_user_msu = ReportConsts.YES;
		svc.add(is_user_msu);

		// 字段标识
		String msu_id = "";
		svc.add(msu_id);

		// 数据源标识
		String datatable = "";
		svc.add(datatable);

		// 状态
		String status = ReportConsts.YES;
		svc.add(status);

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}

	/**
	 * 获取列定义值设置
	 *
	 * @param rpt_id
	 * @param data_type
	 * @param codeNum
	 * @param num
	 * @param sequence
	 * @param request
	 * @return
	 */
	private static String[] getColValueInit(String rpt_id, String data_type,
			String codeNum, String num, String sequence,
			HttpServletRequest request) {
		List svc = new ArrayList();

		// 报表ID
		svc.add(rpt_id);

		// 列顺序
		svc.add(sequence);

		// 是否显示
		String default_display = request.getParameter("default_display_" + num);
		if (default_display == null)
			default_display = ReportConsts.YES;
		svc.add(default_display);

		// 是否显示编码
		String dim_code_display = request.getParameter("dim_code_display_"
				+ num);
		if (dim_code_display == null)
			dim_code_display = ReportConsts.NO;
		svc.add(dim_code_display);

		// 是否展开列
		String is_expand_col = request.getParameter("is_expand_col_" + num);
		if (is_expand_col == null)
			is_expand_col = ReportConsts.NO;
		svc.add(is_expand_col);

		// 是否进行小计
		String is_subsum = request.getParameter("is_subsum_" + num);
		if (is_subsum == null)
			is_subsum = ReportConsts.NO;
		svc.add(is_subsum);

		// 合计值是否有效
		String valuable_sum = request.getParameter("valuable_sum_" + num);
		if (valuable_sum == null)
			valuable_sum = ReportConsts.YES;
		svc.add(valuable_sum);

		// 编码字段代码
		String field_dim_code = request.getParameter("field_dim_code_" + num);
		if (field_dim_code == null)
			field_dim_code = "";

		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_dim_code = "DATA_INT" + codeNum;
		}
		svc.add(field_dim_code);

		// 显示字段代码
		String field_code = request.getParameter("field_code_" + num);
		if (field_code == null || "".equals(field_code)) {
			if (ReportConsts.DATA_TYPE_NUMBER.equals(data_type))
				field_code = "DATA_NUM" + codeNum;
			else
				field_code = "DATA_CHAR" + codeNum;
		}
		svc.add(field_code);

		// 字段描述
		String field_title = request.getParameter("field_title_" + num);
		svc.add(field_title);

		// 数据类型
		svc.add(data_type);

		// 指标精度
		String msu_length = request.getParameter("msu_length_" + num);
		if (msu_length == null)
			msu_length = "0";
		svc.add(msu_length);

		// 计量单位
		String msu_unit = request.getParameter("msu_unit_" + num);
		if (msu_unit == null)
			msu_unit = "";
		svc.add(msu_unit);

		// 是否千分位分隔
		String comma_splitted = request.getParameter("comma_splitted_" + num);
		if (comma_splitted == null)
			comma_splitted = ReportConsts.YES;
		svc.add(comma_splitted);

		// 0 值处理
		String zero_proc = request.getParameter("zero_proc_" + num);
		if (zero_proc == null)
			zero_proc = ReportConsts.ZERO_TO_ZERO;
		svc.add(zero_proc);

		// 数据转化为%
		String ratio_displayed = request.getParameter("ratio_displayed_" + num);
		if (ratio_displayed == null || "".equals(ratio_displayed))
			ratio_displayed = ReportConsts.NO;
		svc.add(ratio_displayed);

		// 是否占比
		String has_comratio = request.getParameter("has_comratio_" + num);
		if (has_comratio == null)
			has_comratio = ReportConsts.NO;
		svc.add(has_comratio);

		// 是否同比
		String has_same = request.getParameter("has_same_" + num);
		if (has_same == null)
			has_same = ReportConsts.NO;
		svc.add(has_same);

		// 是否环比
		String has_last = request.getParameter("has_last_" + num);
		if (has_last == null)
			has_last = ReportConsts.NO;
		svc.add(has_last);

		// 是否链接
		String has_link = request.getParameter("has_link_" + num);
		if (has_link == null)
			has_link = ReportConsts.NO;
		svc.add(has_link);

		// 链接URL
		String link_url = request.getParameter("link_url_" + num);
		if (link_url == null)
			link_url = "";
		svc.add(link_url);

		// 链接TARGET
		String link_target = request.getParameter("link_target_" + num);
		if (link_target == null)
			link_target = ReportConsts.TARGET_SELF;
		svc.add(link_target);

		// 是否排序
		String data_order = request.getParameter("data_order_" + num);
		if (data_order == null)
			data_order = ReportConsts.NO;
		svc.add(data_order);

		// 短信标志
		String sms_flag = request.getParameter("sms_flag_" + num);
		if (sms_flag == null)
			sms_flag = ReportConsts.NO;
		svc.add(sms_flag);

		// 文字是否换行
		String td_wrap = request.getParameter("td_wrap_" + num);
		if (td_wrap == null)
			td_wrap = ReportConsts.NO;
		svc.add(td_wrap);

		// 表头样式
		String title_style = request.getParameter("title_style_" + num);
		if (title_style == null)
			title_style = "";
		svc.add(title_style);

		// 列样式
		String col_style = request.getParameter("col_style_" + num);
		if (col_style == null)
			col_style = "";
		svc.add(col_style);

		// 打印表头样式
		String print_title_style = request.getParameter("print_title_style_"
				+ num);
		if (print_title_style == null)
			print_title_style = "";
		svc.add(print_title_style);

		// 打印列样式
		String print_col_style = request.getParameter("print_col_style_" + num);
		if (print_col_style == null)
			print_col_style = "";
		svc.add(print_col_style);

		// 是否预警
		String need_alert = request.getParameter("need_alert_" + num);
		if (need_alert == null)
			need_alert = ReportConsts.NO;
		svc.add(need_alert);

		// 比较对象
		String compare_to = request.getParameter("compare_to_" + num);
		if (compare_to == null || "".equals(compare_to))
			compare_to = ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD;
		svc.add(compare_to);

		// 是否比率
		String ratio_compare = request.getParameter("ratio_compare_" + num);
		if (ratio_compare == null)
			ratio_compare = ReportConsts.YES;
		svc.add(ratio_compare);

		// 阈值上限
		String high_value = request.getParameter("high_value_" + num);
		if (high_value == null)
			high_value = "0.1";
		svc.add(high_value);

		// 阈值下限
		String low_value = request.getParameter("low_value_" + num);
		if (low_value == null)
			low_value = "-0.1";
		svc.add(low_value);

		// 预警方式
		String alert_mode = request.getParameter("alert_mode_" + num);
		if (alert_mode == null || "".equals(alert_mode))
			alert_mode = ReportConsts.ALERT_MODE_ARROW;
		svc.add(alert_mode);

		// 阈值大于上限颜色
		String rise_color = request.getParameter("rise_color_" + num);
		if (rise_color == null || "".equals(rise_color))
			rise_color = ReportConsts.ALERT_COLOR_GREEN;
		svc.add(rise_color);

		// 阈值小于下限颜色
		String down_color = request.getParameter("down_color_" + num);
		if (down_color == null || "".equals(down_color))
			down_color = ReportConsts.ALERT_COLOR_RED;
		svc.add(down_color);

		// 是否指标字段
		String is_msu = request.getParameter("is_msu_" + num);
		svc.add(is_msu);

		// 是否衍生指标
		String is_user_msu = request.getParameter("is_user_msu_" + num);
		svc.add(is_user_msu);

		// 字段标识
		String msu_id = request.getParameter("msu_id_" + num);
		svc.add(msu_id);

		// 数据源标识
		String datatable = request.getParameter("datatable_" + num);
		svc.add(datatable);

		// 状态
		String status = request.getParameter("status_" + num);
		if (status == null)
			status = ReportConsts.YES;
		svc.add(status);

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}

	/**
	 * 获取列定义值设置
	 *
	 * @param rpt_id
	 * @param num
	 * @param sequence
	 * @param request
	 * @return
	 */
	private static String[] getColValue(String rpt_id, String num,
			String sequence, HttpServletRequest request) {
		List svc = new ArrayList();

		// 报表ID
		svc.add(rpt_id);

		// 列顺序
		svc.add(sequence);

		// 是否显示
		String default_display = request.getParameter("default_display_" + num);
		if (default_display != null)
			default_display = ReportConsts.YES;
		else
			default_display = ReportConsts.NO;
		svc.add(default_display);

		// 是否显示编码
		String dim_code_display = request.getParameter("dim_code_display_"
				+ num);
		if (dim_code_display != null)
			dim_code_display = ReportConsts.YES;
		else
			dim_code_display = ReportConsts.NO;
		svc.add(dim_code_display);

		// 是否展开列
		String is_expand_col = request.getParameter("is_expand_col_" + num);
		if (is_expand_col != null)
			is_expand_col = ReportConsts.YES;
		else
			is_expand_col = ReportConsts.NO;
		svc.add(is_expand_col);

		// 是否进行小计
		String is_subsum = request.getParameter("is_subsum_" + num);
		if (is_subsum != null)
			is_subsum = ReportConsts.YES;
		else
			is_subsum = ReportConsts.NO;
		svc.add(is_subsum);

		// 合计值是否有效
		String valuable_sum = request.getParameter("valuable_sum_" + num);
		if (valuable_sum != null)
			valuable_sum = ReportConsts.YES;
		else
			valuable_sum = ReportConsts.NO;
		svc.add(valuable_sum);

		// 编码字段代码
		String field_dim_code = request.getParameter("field_dim_code_" + num);
		svc.add(field_dim_code);

		// 显示字段代码
		String field_code = request.getParameter("field_code_" + num);
		svc.add(field_code);

		// 字段描述
		String field_title = request.getParameter("field_title_" + num);
		svc.add(field_title);

		// 数据类型
		String data_type = request.getParameter("data_type_" + num);
		svc.add(data_type);

		// 指标精度
		String msu_length = request.getParameter("msu_length_" + num);
		svc.add(msu_length);

		// 计量单位
		String msu_unit = request.getParameter("msu_unit_" + num);
		svc.add(msu_unit);

		// 是否千分位分隔
		String comma_splitted = request.getParameter("comma_splitted_" + num);
		if (comma_splitted != null)
			comma_splitted = ReportConsts.YES;
		else
			comma_splitted = ReportConsts.NO;
		svc.add(comma_splitted);

		// 0 值处理
		String zero_proc = request.getParameter("zero_proc_" + num);
		if (zero_proc != null)
			zero_proc = ReportConsts.ZERO_TO_BLANK;
		else
			zero_proc = ReportConsts.ZERO_TO_ZERO;
		svc.add(zero_proc);

		// 数据转化为%
		String ratio_displayed = request.getParameter("ratio_displayed_" + num);
		if (ratio_displayed == null || "".equals(ratio_displayed)) {
			ratio_displayed = ReportConsts.NO;
		}
		svc.add(ratio_displayed);

		// 是否占比
		String has_comratio = request.getParameter("has_comratio_" + num);
		if (has_comratio != null)
			has_comratio = ReportConsts.YES;
		else
			has_comratio = ReportConsts.NO;
		svc.add(has_comratio);

		// 是否同比
		String has_same = request.getParameter("has_same_" + num);
		if (has_same != null)
			has_same = ReportConsts.YES;
		else
			has_same = ReportConsts.NO;
		svc.add(has_same);

		// 是否环比
		String has_last = request.getParameter("has_last_" + num);
		if (has_last != null)
			has_last = ReportConsts.YES;
		else
			has_last = ReportConsts.NO;
		svc.add(has_last);

		// 是否链接
		String has_link = request.getParameter("has_link_" + num);
		if (has_link != null)
			has_link = ReportConsts.YES;
		else
			has_link = ReportConsts.NO;
		svc.add(has_link);

		// 链接URL
		String link_url = request.getParameter("link_url_" + num);
		svc.add(link_url);

		// 链接TARGET
		String link_target = request.getParameter("link_target_" + num);
		if (link_target != null)
			link_target = ReportConsts.TARGET_BLANK;
		else
			link_target = ReportConsts.TARGET_SELF;
		svc.add(link_target);

		// 是否排序
		String data_order = request.getParameter("data_order_" + num);
		if (data_order != null)
			data_order = ReportConsts.YES;
		else
			data_order = ReportConsts.NO;
		svc.add(data_order);

		// 短信标志
		String sms_flag = request.getParameter("sms_flag_" + num);
		svc.add(sms_flag);

		// 文字是否换行
		String td_wrap = request.getParameter("td_wrap_" + num);
		if (td_wrap != null)
			td_wrap = ReportConsts.YES;
		else
			td_wrap = ReportConsts.NO;
		svc.add(td_wrap);

		// 表头样式
		String title_style = request.getParameter("title_style_" + num);
		svc.add(title_style);

		// 列样式
		String col_style = request.getParameter("col_style_" + num);
		svc.add(col_style);

		// 打印表头样式
		String print_title_style = request.getParameter("print_title_style_"
				+ num);
		svc.add(print_title_style);

		// 打印列样式
		String print_col_style = request.getParameter("print_col_style_" + num);
		svc.add(print_col_style);

		// 是否预警
		String need_alert = request.getParameter("need_alert_" + num);
		if (need_alert != null)
			need_alert = ReportConsts.YES;
		else
			need_alert = ReportConsts.NO;
		svc.add(need_alert);

		// 比较对象
		String compare_to = request.getParameter("compare_to_" + num);
		if (compare_to == null || "".equals(compare_to)) {
			compare_to = ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD;
		}
		svc.add(compare_to);

		// 是否比率
		String ratio_compare = request.getParameter("ratio_compare_" + num);
		if (ratio_compare != null)
			ratio_compare = ReportConsts.YES;
		else
			ratio_compare = ReportConsts.NO;
		svc.add(ratio_compare);

		// 阈值上限
		String high_value = request.getParameter("high_value_" + num);
		svc.add(high_value);

		// 阈值下限
		String low_value = request.getParameter("low_value_" + num);
		svc.add(low_value);

		// 预警方式
		String alert_mode = request.getParameter("alert_mode_" + num);
		if (alert_mode == null || "".equals(alert_mode)) {
			alert_mode = ReportConsts.ALERT_MODE_ARROW;
		}
		svc.add(alert_mode);

		// 阈值大于上限颜色
		String rise_color = request.getParameter("rise_color_" + num);
		if (rise_color == null || "".equals(rise_color)) {
			rise_color = ReportConsts.ALERT_COLOR_GREEN;
		}
		svc.add(rise_color);

		// 阈值小于下限颜色
		String down_color = request.getParameter("down_color_" + num);
		if (down_color == null || "".equals(down_color)) {
			down_color = ReportConsts.ALERT_COLOR_RED;
		}
		svc.add(down_color);

		// 是否指标字段
		String is_msu = request.getParameter("is_msu_" + num);
		svc.add(is_msu);

		// 是否衍生指标
		String is_user_msu = request.getParameter("is_user_msu_" + num);
		svc.add(is_user_msu);

		// 字段标识
		String msu_id = request.getParameter("msu_id_" + num);
		svc.add(msu_id);

		// 数据源标识
		String datatable = request.getParameter("datatable_" + num);
		svc.add(datatable);

		// 状态
		String status = request.getParameter("status_" + num);
		if (status != null)
			status = ReportConsts.YES;
		else
			status = ReportConsts.NO;
		svc.add(status);

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}

	/**
	 * 获取条件定义
	 *
	 * @param rpt_id
	 * @param num
	 * @param sequence
	 * @param request
	 * @return
	 */
	private static String[] getFilterValue(String rpt_id, String num,
			String sequence, HttpServletRequest request) {
		List svc = new ArrayList();

		// 报表ID
		svc.add(rpt_id);

		// 列顺序
		String col_sequence = request.getParameter("col_sequence_" + num);
		svc.add(col_sequence);

		// 排列顺序
		svc.add(sequence);

		// 是否为条件
		String dim_conditon = request.getParameter("dim_conditon_" + num);
		if (dim_conditon != null)
			dim_conditon = ReportConsts.YES;
		else
			dim_conditon = ReportConsts.NO;
		svc.add(dim_conditon);

		// 编码字段代码
		String field_dim_code = request.getParameter("field_dim_code_" + num);
		svc.add(field_dim_code);

		// 显示字段代码
		String field_code = request.getParameter("field_code_" + num);
		svc.add(field_code);

		// 字段描述
		String field_title = request.getParameter("field_title_" + num);
		svc.add(field_title);

		// 条件类型
		String filter_type = request.getParameter("filter_type_" + num);
		svc.add(filter_type);

		// 脚本代码
		String filter_script = request.getParameter("filter_script_" + num);
		svc.add(filter_script);

		// 数据来源
		String filter_datasource = request.getParameter("filter_datasource_"
				+ num);
		svc.add(filter_datasource);

		// 提取数据语句
		String filter_sql = request.getParameter("filter_sql_" + num);
		svc.add(filter_sql);

		// 显示全部
		String filter_all = request.getParameter("filter_all_" + num);
		if (filter_all != null)
			filter_all = ReportConsts.YES;
		else
			filter_all = ReportConsts.NO;
		svc.add(filter_all);

		// 默认值
		String filter_default = request.getParameter("filter_default_" + num);
		svc.add(filter_default);

		// 是否有效
		String status = request.getParameter("status_" + num);
		if (status != null)
			status = ReportConsts.YES;
		else
			status = ReportConsts.NO;
		svc.add(status);

		// 字段类型
		String data_type = request.getParameter("data_type_" + num);
		svc.add(data_type);

		// 条件操作符号
		String con_tag = request.getParameter("con_tag_" + num);
		svc.add(con_tag);

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}

	/**
	 * 确定报表是否存在展开列
	 *
	 * @param rptColTable
	 * @return
	 */
	private static String getExpandcol(List rptColTable) {
		String expandcol = "N";
		int colFirst = Integer.parseInt(ReportObjUtil
				.getExpandFirstCol(rptColTable));
		int colLast = Integer.parseInt(ReportObjUtil
				.getExpandLastCol(rptColTable));
		int expcol = 0;
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)
					&& ReportConsts.YES.equals(dict.default_display)) {
				expcol = Integer.parseInt(dict.col_sequence);
				if (expcol == colFirst && i + 1 < rptColTable.size()) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i + 1);
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING
									.equals(tmpdict.data_type)
							&& ReportConsts.NO.equals(tmpdict.default_display)
							&& ReportConsts.YES.equals(tmpdict.is_expand_col)) {
						expandcol = ReportConsts.DIRECTION_UP
								+ tmpdict.col_sequence;
					}
				} else if (expcol == colLast && i > 0) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i - 1);
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING
									.equals(tmpdict.data_type)
							&& ReportConsts.YES.equals(tmpdict.default_display)
							&& ReportConsts.YES.equals(tmpdict.is_expand_col)) {
						expandcol = ReportConsts.DIRECTION_DOWN
								+ tmpdict.col_sequence;
						break;
					}
				} else if (expcol > colFirst && expcol < colLast) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i + 1);
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING
									.equals(tmpdict.data_type)
							&& ReportConsts.NO.equals(tmpdict.default_display)
							&& ReportConsts.YES.equals(tmpdict.is_expand_col)) {
						expandcol = ReportConsts.DIRECTION_UP
								+ tmpdict.col_sequence;
					}
				}
			}
		}
		return expandcol;
	}
}
