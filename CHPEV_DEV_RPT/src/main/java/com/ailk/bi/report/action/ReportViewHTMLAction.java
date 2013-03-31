package com.ailk.bi.report.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.domain.RptChartTable;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptFilterTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.ILTableService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.service.impl.LTableServiceImpl;
import com.ailk.bi.report.struct.DimRuleStruct;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.report.util.ReportFilterUtil;
import com.ailk.bi.report.util.ReportObjUtil;
import com.ailk.bi.report.util.ReportProcessUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * 报表显示页面处理信息，处理报表基本信息，条件信息，最终的展示形式
 *
 * @author Renhui
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportViewHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * waf.controller.web.action.HTMLActionSupport#doTrans(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession();
		// 获取用户信息
		String oper_no = CommonFacade.getLoginId(session);
		String oper_name = CommonFacade.getLoginName(session);
		// 由于数据可见性和审批未开放，此处先固定
		String user_role = "1";

		// 判断是否需要更新session中的值
		boolean clearSession = false;

		// 是否预览报表
		String preview = request.getParameter("preview");
		if (StringTool.checkEmptyString(preview)) {
			preview = ReportConsts.NO;
		}

		// 是否只显示表格
		String tableFlag = request.getParameter("tableFlag");
		if (StringTool.checkEmptyString(tableFlag)) {
			tableFlag = ReportConsts.NO;
		}

		// 定义报表操作接口
		ILReportService rptService = new LReportServiceImpl();
		// 定义HTML输出接口
		ILTableService tableService = new LTableServiceImpl();

		// 获取传入报表ID
		String rpt_id = request.getParameter("rpt_id");

		// 报表基本信息
		RptResourceTable rptTable = (RptResourceTable) session
				.getAttribute(WebKeys.ATTR_REPORT_TABLE);

		// 报表过滤条件
		RptFilterTable[] rptFilterTables = (RptFilterTable[]) session
				.getAttribute(WebKeys.ATTR_REPORT_FILTERS);

		// 报表过滤条件取值
		String[] filtersValue = (String[]) session
				.getAttribute(WebKeys.ATTR_REPORT_FILTERS_VALUEALL);

		// 报表条件解析
		PubInfoConditionTable[] cdnTables = (PubInfoConditionTable[]) session
				.getAttribute(WebKeys.ATTR_REPORT_CONDITION_TABLES);

		// 报表图形定义
		RptChartTable[] rptChartTables = (RptChartTable[]) session
				.getAttribute(WebKeys.ATTR_REPORT_CHARTS);

		// 审批流程处理的HTML
		String processHTML = "";

		if (rptTable == null || (rptTable != null && !rptTable.rpt_id.equals(rpt_id))
				|| ReportConsts.YES.equals(preview)) {
			// 判断浏览的报表是否改变，若改变需要重新读取报表定义
			rptTable = null;
			rptFilterTables = null;
			filtersValue = null;
			rptChartTables = null;

			clearSession = true;
		}
		if (clearSession) {
			// 清空session后，重新读取报表基本信息
			try {
				rptTable = (RptResourceTable) rptService.getReport(rpt_id);
				if (rptTable == null) {
					throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "该报表不存在！");
				}
			} catch (AppException e) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取报表信息失败！");
			}
			// 读取报表过滤条件
			try {
				List listRptFilter = rptService.getReportFilter(rpt_id);
				if (listRptFilter != null && listRptFilter.size() >= 0) {
					rptFilterTables = (RptFilterTable[]) listRptFilter
							.toArray(new RptFilterTable[listRptFilter.size()]);
				}
			} catch (AppException ex) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "读取报表条件定义失败！");
			}
			// 读取报表条件解析
			try {
				cdnTables = CommConditionUtil.genCondition(rpt_id, ReportConsts.CONDITION_PUB);
			} catch (AppException e) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取报表条件信息失败！");
			}
			// 读取报表图形定义
			try {
				List listRptCharts = rptService.getReportCharts(rpt_id);
				if (listRptCharts != null && listRptCharts.size() >= 0) {
					rptChartTables = (RptChartTable[]) listRptCharts
							.toArray(new RptChartTable[listRptCharts.size()]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取报表图形信息失败！");
			}
		}

		// 报表查询条件对象
		ReportQryStruct qryStruct = new ReportQryStruct();
		// 判断该报表是否有外部传入条件，p_condition约定名称
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition)) {
			p_condition = ReportConsts.NO;
		}
		// 报表查询条件
		if (ReportConsts.YES.equals(preview)) {
			rptTable.preview_data = ReportConsts.YES;
			// rptTable.data_where = " WHERE RPT_ID='AMDOCS_PREVIEW_DATA'";
		} else {
			rptTable.preview_data = ReportConsts.NO;
		}
		rptTable.data_where_sql = " " + rptTable.data_where;
		try {
			if (ReportConsts.YES.equals(p_condition)) {
				qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
				logcommon.debug(qryStruct.toXml());

				// 外部传入条件
				String getwhere = CommConditionUtil.getRptWhere(cdnTables, request);
				// 回写外部条件
				String setwhere = CommConditionUtil.setRptWhere(cdnTables, request);
				if (rptTable.data_where_sql.toUpperCase().indexOf(" WHERE ") >= 0) {
					rptTable.data_where_sql += getwhere;
				} else {
					rptTable.data_where_sql = "WHERE 1=1 " + getwhere;
				}
				logcommon.debug("requestwhere=" + setwhere);
				request.setAttribute("requestwhere", setwhere);
			} else {// 重新提取
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}

		// 查看报表分页页码
		String nowPage = request.getParameter("page__iCurPage");
		if (!StringTool.checkEmptyString(nowPage)) {
			qryStruct.nowpage = nowPage;
		}
		// 报表开始日期初始值
		if (StringTool.checkEmptyString(qryStruct.date_s)) {
			// 用于直接跳转到该日期
			String p_date = request.getParameter("p_date");
			if (!StringTool.checkEmptyString(p_date))
				qryStruct.date_s = p_date;
			else
				qryStruct.date_s = ReportObjUtil.genRptDate(rptTable);
		}
		// 报表结束日期初始值
		if (StringTool.checkEmptyString(qryStruct.date_e)) {
			qryStruct.date_e = qryStruct.date_s;
		}
		// 扩展显示标志初始值
		if (StringTool.checkEmptyString(qryStruct.divcity_flag)) {
			qryStruct.divcity_flag = rptTable.divcity_flag;
		}
		rptTable.divcity_flag = qryStruct.divcity_flag;
		// 扩展显示方式初始值
		if (StringTool.checkEmptyString(qryStruct.row_flag)) {
			qryStruct.row_flag = rptTable.row_flag;
		}
		// 扩展显示列
		String expandcol = request.getParameter("expandcol");
		if (!StringTool.checkEmptyString(expandcol)) {
			qryStruct.expandcol = expandcol;
		} else {
			if (!StringTool.checkEmptyString(rptTable.startcol)
					&& !ReportConsts.NO.equals(rptTable.startcol)) {
				qryStruct.expandcol = rptTable.startcol;
			}
		}

		// 加入权限控制条件-业务类型
		qryStruct.svc_knd = "all";// CommTool.getSvcRights(session).toLowerCase();
		if ("all".equals(qryStruct.svc_knd)) {
			qryStruct.svc_knd = "";
		}
		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		// logcommon.debug("ctlStruct.ctl_str_add=" + ctlStruct.ctl_str_add);
		// 归属数据区域
		if (StringTool.checkEmptyString(qryStruct.attach_region)) {
			if (SysConsts.RIGHT_LVL_METRO.equals(ctlStruct.ctl_lvl)) {
				qryStruct.right_attach_region = "";
			} else if (SysConsts.RIGHT_LVL_CITY.equals(ctlStruct.ctl_lvl)) {
				qryStruct.right_attach_region = ctlStruct.ctl_city_str_add;
			} else if (SysConsts.RIGHT_LVL_COUNTY.equals(ctlStruct.ctl_lvl)) {
				qryStruct.right_attach_region = ctlStruct.ctl_county_str_add;
			} else if (SysConsts.RIGHT_LVL_SEC.equals(ctlStruct.ctl_lvl)) {
				qryStruct.right_attach_region = ctlStruct.ctl_sec_str_add;
			} else if (SysConsts.RIGHT_LVL_AREA.equals(ctlStruct.ctl_lvl)) {
				qryStruct.right_attach_region = ctlStruct.ctl_area_str_add;
			}
		}
		if (StringTool.checkEmptyString(qryStruct.city_id)) {// 地市，控制地市
			qryStruct.right_city_id = ctlStruct.ctl_city_str_add;
		}
		if (StringTool.checkEmptyString(qryStruct.county_id)) {// 区县，控制区县
			qryStruct.right_county_id = ctlStruct.ctl_county_str_add;
		}
		if (StringTool.checkEmptyString(qryStruct.sec_area_id)) {// 片区，控制片区
			qryStruct.right_sec_area_id = ctlStruct.ctl_sec_str_add;
		}
		if (StringTool.checkEmptyString(qryStruct.area_id)) {// 行区，控制行区
			qryStruct.right_area_id = ctlStruct.ctl_area_str_add;
		}

		// 处理报表其他过滤条件初始值
		for (int i = 0; rptFilterTables != null && i < rptFilterTables.length; i++) {
			// 多选处理
			if (ReportConsts.FILTER_TYPE_CHECKBOX.equals(rptFilterTables[i].filter_type)
					&& ReportConsts.NO.equals(p_condition)) {
				String tmpF = "dim" + (i + 1);
				String[] arrValue = request.getParameterValues("qry__" + tmpF + "_def");
				if (null == arrValue) {
					arrValue = null;
				}
				String focusValue = StringTool.changArrToStr(arrValue, "");
				ReflectUtil.setStringToObj(qryStruct, tmpF, focusValue);
			}
		}
		if (clearSession) {
			try {
				filtersValue = ReportFilterUtil.getRptFilterValue(rptTable, rptFilterTables,
						qryStruct);
			} catch (AppException e1) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
						"读取报表条件列表值失败！");
			}
			for (int i = 0; rptFilterTables != null && i < rptFilterTables.length; i++) {
				String tmpF = "dim" + (i + 1);
				String tmpVal = ReflectUtil.getStringFromObj(qryStruct, tmpF);
				if (tmpVal == null || "".equals(tmpVal)) {
					String paramDftValue = ReportFilterUtil.getRptFilterDefault(rptFilterTables[i],
							filtersValue[i]);
					ReflectUtil.setStringToObj(qryStruct, tmpF, paramDftValue);
				}
			}
		}
		// 处理没有全部的默认值
		for (int i = 0; rptFilterTables != null && i < rptFilterTables.length; i++) {
			String tmpF = "dim" + (i + 1);
			String tmpVal = ReflectUtil.getStringFromObj(qryStruct, tmpF);
			if ((tmpVal == null || "".equals(tmpVal))
					&& ReportConsts.NO.equals(rptFilterTables[i].filter_all)
					&& !StringTool.checkEmptyString(rptFilterTables[i].filter_default)) {
				String paramDftValue = ReportFilterUtil.getRptFilterDefault(rptFilterTables[i],
						filtersValue[i]);
				ReflectUtil.setStringToObj(qryStruct, tmpF, paramDftValue);
			}
		}

		// 处理报表条件
		if (rptTable.data_where_sql.toUpperCase().indexOf(" WHERE ") >= 0) {
			rptTable.data_where_sql += CommConditionUtil.getRptWhere(cdnTables, qryStruct);
		} else {
			rptTable.data_where_sql = "WHERE 1=1 "
					+ CommConditionUtil.getRptWhere(cdnTables, qryStruct);
		}
		logcommon.debug("rptTable.data_where=" + rptTable.data_where_sql);

		// 处理报表排序
		if (qryStruct.order_code != null && !"".equals(qryStruct.order_code)) {
			String ret = " ORDER BY DATA_ID," + qryStruct.order_code;
			if (StringTool.checkEmptyString(qryStruct.order)) {
				qryStruct.order = "DESC";
			}
			ret += " " + qryStruct.order;
			rptTable.data_order = ret;
		}

		// 判断报表是否为审核报表
		if (ReportConsts.NO.equals(rptTable.processflag)) {
			if (rptService.processReport(rptTable, qryStruct.date_s)) {
				rptTable.processflag = ReportConsts.YES;
			}
		}
		// 是审核报表需要判断数据可见性
		if (ReportConsts.YES.equals(rptTable.processflag)) {
			// 初始化数据可见性为不可见
			qryStruct.visible_data = ReportConsts.NO;
			try {
				List listStep = rptService.getReportProcessStep(rptTable, qryStruct.date_s);
				List listHis = rptService.getReportProcessHis(rpt_id, qryStruct.date_s,
						qryStruct.attach_region);
				if (ReportProcessUtil.visibleReportData(listStep, listHis, user_role)) {
					qryStruct.visible_data = ReportConsts.YES;
				}
				processHTML = tableService.getProcessBody(oper_no, oper_name, user_role, rpt_id,
						qryStruct.date_e, listStep, listHis);
				// logcommon.debug("processHTML=" + processHTML);
			} catch (AppException ex) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "读取报表流程信息失败！");
			}
		} else {
			qryStruct.visible_data = ReportConsts.YES;
		}

		if (ReportConsts.RPTM_DEFAULT_SHOW.equals(rptTable.rpt_type)
				|| ReportConsts.RPTM_EXPAND_SHOW.equals(rptTable.rpt_type)
				|| ReportConsts.RPTM_EXPAND_SHOW1.equals(rptTable.rpt_type)
				|| ReportConsts.RPTM_EXPAND_SHOW2.equals(rptTable.rpt_type)) {
			// 报表标题部分的HTML
			String strTitle = tableService.getMeasureReportTitle(rptTable, qryStruct);

			// 报表HTML
			String[] body = tableService.getMeasureReportBody(rptTable, qryStruct);

			DimRuleStruct dimInfo = tableService.getMeasureReportDim(rptTable.rpt_id);

			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE, rptTable);

			session.setAttribute(WebKeys.ATTR_REPORT_TITLE_HTML, strTitle);
			session.setAttribute(WebKeys.ATTR_REPORT_BODY_HTML, body);
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_HTML, processHTML);

			session.setAttribute(WebKeys.ATTR_REPORT_MEASURE_RULE_DIM, dimInfo);

			setNextScreen(request, "rptFirstView.screen");
		} else {
			// 获取固定报表列对象
			List listRptCol = null;
			try {
				listRptCol = rptService.getReportCol(rptTable.rpt_id, qryStruct.expandcol);
			} catch (AppException e) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "读取报表列定义失败！");
			}
			if (listRptCol == null || listRptCol.size() == 0) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "报表未初始化完成！");
			}
			rptTable.fillperson = oper_no;
			// 报表标题部分的HTML
			String strTitle = tableService.getReportTitle(rptTable, qryStruct);

			// 报表HTML
			String[] body = tableService.getReportBody(rptTable, listRptCol, qryStruct, cdnTables);

			tableService.getRptBottom(rptTable);
			// 本地化报表的处理
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE, rptTable);
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS, rptFilterTables);
			session.setAttribute(WebKeys.ATTR_REPORT_FILTERS_VALUEALL, filtersValue);
			session.setAttribute(WebKeys.ATTR_REPORT_CONDITION_TABLES, cdnTables);
			session.setAttribute(WebKeys.ATTR_REPORT_CHARTS, rptChartTables);

			session.setAttribute(WebKeys.ATTR_REPORT_TITLE_HTML, strTitle);
			session.setAttribute(WebKeys.ATTR_REPORT_BODY_HTML, body);
			session.setAttribute(WebKeys.ATTR_REPORT_PROCESS_HTML, processHTML);

			if (ReportConsts.YES.equals(tableFlag)) {
				setNextScreen(request, "rptLocalViewTable.screen");
			} else {
				setNextScreen(request, "rptLocalView.screen");
			}
		}
	}
}
