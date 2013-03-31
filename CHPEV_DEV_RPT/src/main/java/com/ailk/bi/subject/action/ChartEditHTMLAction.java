package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.table.PubInfoChartFunTable;
import com.ailk.bi.base.taglib.flashchart.ChartConsts;
import com.ailk.bi.base.util.CObjKnd;
import com.ailk.bi.base.util.CommChartUtil;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ChartEditHTMLAction extends HTMLActionSupport {

	/**
	 * 获取报表列表
	 */
	private static final long serialVersionUID = -5643321881510988393L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		// 获取报表查询条件
		ReportQryStruct qryStruct = new ReportQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}

		// 获取操作类型
		String optype = request.getParameter("optype");
		if (optype == null || "".equals(optype)) {
			optype = "list";
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
		if ("list".equals(optype)) {
			// 查询条件
			String whereSql = "";
			// 图形编码
			if (!StringTool.checkEmptyString(qryStruct.rpt_id)) {
				whereSql += " AND chart_id LIKE '%" + qryStruct.rpt_id + "%'";
			}
			// 图形名称
			if (!StringTool.checkEmptyString(qryStruct.rpt_name)) {
				whereSql += " AND chart_attribute LIKE '%" + qryStruct.rpt_name + "%'";
			}

			PubInfoChartDefTable[] chartDef = null;
			try {
				chartDef = CommChartUtil.getChartDefList(whereSql);
			} catch (AppException ex) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取图形列表内容失败！");
			}
			session.setAttribute(WebKeys.ATTR_REPORT_TABLES, chartDef);
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
			setNextScreen(request, "SubjectCommChartList.screen");

		} else if ("new".equals(optype)) {
			PubInfoChartDefTable chart = new PubInfoChartDefTable();
			chart.chart_distype = "2";
			chart.chart_attribute = "title:这里输入标题;width:600;height:260;";
			chart.series_attribute = "showValues:0;showValues:0;showValues:0;showValues:0;showValues:0;showValues:0";
			chart.chart_index = "0";
			chart.series_length = "0";
			chart.series_cut_len = "4";
			chart.isusecd = "0";
			chart.series_cut = "N";
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, chart);
			setNextScreen(request, "SubjectCommChartEdit.screen");
		} else if ("edit".equals(optype)) {
			// 查询条件
			String whereSql = "";
			// 图形编码
			String chart_id = request.getParameter("chart_id");
			if (!StringTool.checkEmptyString(chart_id)) {
				whereSql += " AND chart_id = '" + chart_id + "'";
			}
			PubInfoChartDefTable[] chartDef = null;
			try {
				chartDef = CommChartUtil.getChartDefList(whereSql);
			} catch (AppException ex) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取图形内容失败！");
			}
			PubInfoChartFunTable[] chartFun = null;
			try {
				chartFun = CommChartUtil.getChartFunDef(chart_id);
			} catch (AppException e) {
				throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
						"获取图形功能内容内容失败！");
			}

			PubInfoChartDefTable chart = null;
			if (chartDef != null && chartDef.length > 0) {
				chart = chartDef[0];
			}
			session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, chart);
			session.setAttribute(WebKeys.ATTR_REPORT_CHART_FUN, chartFun);
			setNextScreen(request, "SubjectCommChartEdit.screen");
		} else if ("chartedit".equals(optype)) {
			// 图形ID
			String chart_id = request.getParameter("chart_id");
			if ("insert".equals(opSubmit) || "update".equals(opSubmit)) {
				// 添加新图形先检查图形ID
				if ("insert".equals(opSubmit) && existChart(chart_id)) {
					session.setAttribute(WebKeys.ATTR_ANYFLAG, "2");
					throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
							"该图形ID已经存在！");
				}

				PubInfoChartDefTable chartDef = new PubInfoChartDefTable();
				chartDef.chart_id = chart_id;
				chartDef.chart_belong = request.getParameter("chart_belong");
				chartDef.chart_index = request.getParameter("chart_index");
				chartDef.chart_attribute = request.getParameter("chart_attribute");
				chartDef.series_attribute = request.getParameter("series_attribute");
				chartDef.sql_main = request.getParameter("sql_main");
				chartDef.sql_where = request.getParameter("sql_where");
				chartDef.sql_order = request.getParameter("sql_order");
				chartDef.isusecd = request.getParameter("isusecd");
				chartDef.category_index = request.getParameter("category_index");
				chartDef.series_index = request.getParameter("series_index");
				chartDef.series_length = request.getParameter("series_length");
				chartDef.series_cut = request.getParameter("series_cut");
				chartDef.series_cut_len = request.getParameter("series_cut_len");
				chartDef.value_index = request.getParameter("value_index");
				chartDef.category_desc = request.getParameter("category_desc");
				chartDef.category_desc_index = request.getParameter("category_desc_index");
				chartDef.chart_distype = request.getParameter("chart_distype");
				if (ChartConsts.CHART_TYPE_JFREECHART.equals(chartDef.chart_distype)) {
					chartDef.chart_type = request.getParameter("jfreechart");
				} else if(ChartConsts.CHART_TYPE_FUSION_MULTI.equals(chartDef.chart_distype)){
					chartDef.chart_type = request.getParameter("fusionchart_multi");
				} else if(ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)){
					chartDef.chart_type = request.getParameter("fusionchart_single");
				} else if(ChartConsts.CHART_TYPE_FUSION_SCATTER.equals(chartDef.chart_distype)){
					chartDef.chart_type = request.getParameter("fusionchart_scatter");
				} else if(ChartConsts.CHART_TYPE_FUSION_BUBBLE.equals(chartDef.chart_distype)){
					chartDef.chart_type = request.getParameter("fusionchart_bubble");
				}

				if ("insert".equals(opSubmit)) {
					try {
						insertChart(chartDef);
					} catch (AppException ex) {
						throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
								"添加图形失败！");
					}
				} else if ("update".equals(opSubmit)) {
					try {
						updateChart(chartDef);
					} catch (AppException ex) {
						throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
								"更新图形内容失败！");
					}
				}
				session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, chartDef);
				setNextScreen(request, "SubjectCommChartEdit.screen");
			}
		} else if ("chartfunedit".equals(optype)) {
			// 图形ID
			String chart_id = request.getParameter("chart_id");
			if ("insert".equals(opSubmit) || "update".equals(opSubmit)
					|| "chartfundelete".equals(opSubmit)) {
				PubInfoChartFunTable funDef = new PubInfoChartFunTable();
				if ("update".equals(opSubmit) || "chartfundelete".equals(opSubmit)) {
					String col = request.getParameter("opDirection");
					funDef.chart_id = chart_id;
					funDef.frame_name = chart_id;
					funDef.chart_desc = request.getParameter("fun_chart_desc" + col);
					funDef.category_desc = request.getParameter("fun_category_desc" + col);
					funDef.category_desc_index = request.getParameter("fun_category_desc_index"
							+ col);
					funDef.value_index = request.getParameter("fun_value_index" + col);
					if (funDef.value_index == null || "".equals(funDef.value_index)) {
						funDef.value_index = "null";
					}
					funDef.wheresql = request.getParameter("fun_wheresql" + col);
					funDef.replace_content = request.getParameter("fun_replace_content" + col);
					funDef.is_checked = request.getParameter("fun_is_checked" + col);
					funDef.col_sequence = request.getParameter("fun_col_sequence" + col);
				} else {
					funDef.chart_id = chart_id;
					funDef.frame_name = chart_id;
					funDef.chart_desc = request.getParameter("fun_chart_desc");
					funDef.category_desc = request.getParameter("fun_category_desc");
					funDef.category_desc_index = request.getParameter("fun_category_desc_index");
					funDef.value_index = request.getParameter("fun_value_index");
					if (funDef.value_index == null || "".equals(funDef.value_index)) {
						funDef.value_index = "null";
					}
					funDef.wheresql = request.getParameter("fun_wheresql");
					funDef.replace_content = request.getParameter("fun_replace_content");
					funDef.is_checked = request.getParameter("fun_is_checked");
				}
				if ("insert".equals(opSubmit)) {
					try {
						insertChartFun(funDef);
					} catch (AppException ex) {
						throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
								"添加图形功能失败！");
					}
				} else if ("update".equals(opSubmit)) {
					try {
						updateChartFun(funDef);
					} catch (AppException ex) {
						throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
								"更新图形内容功能失败！");
					}
				} else if ("chartfundelete".equals(opSubmit)) {
					try {
						deleteChartFun(funDef);
					} catch (AppException ex) {
						throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
								"删除图形内容功能失败！");
					}
				}

				PubInfoChartFunTable[] chartFun = null;
				try {
					chartFun = CommChartUtil.getChartFunDef(chart_id);
				} catch (AppException e) {
					throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
							"获取图形功能内容内容失败！");
				}

				session.setAttribute(WebKeys.ATTR_REPORT_CHART_FUN, chartFun);
				setNextScreen(request, "SubjectCommChartEdit.screen");
			}
		}

	}

	/**
	 * 图形是否已经存在
	 *
	 * @param chart_id
	 * @return
	 */
	public static boolean existChart(String chart_id) {
		boolean isExist = false;
		Object chartDef = null;
		try {
			chartDef = CommChartUtil.getChartDef(chart_id);
		} catch (AppException ex) {
			chartDef = null;
		}
		if (chartDef != null) {
			isExist = true;
		}
		return isExist;
	}

	public static void insertChart(Object chartDef) throws AppException {
		PubInfoChartDefTable chart = (PubInfoChartDefTable) chartDef;
		List svc = new ArrayList();
		svc.add(chart.chart_id);
		svc.add(chart.chart_belong);
		svc.add(chart.chart_type);
		svc.add(chart.chart_index);
		svc.add(chart.chart_attribute);
		// 转换单引号
		String sql_main = chart.sql_main;
		sql_main = StringB.replace(sql_main, "'", "'||chr(39)||'");
		svc.add(sql_main);
		String sql_where = chart.sql_where;
		sql_where = StringB.replace(sql_where, "'", "'||chr(39)||'");
		svc.add(sql_where);
		String sql_order = chart.sql_order;
		sql_order = StringB.replace(sql_order, "'", "'||chr(39)||'");
		svc.add(sql_order);
		svc.add(chart.isusecd);
		svc.add(chart.category_index);
		svc.add(chart.series_index);
		svc.add(chart.series_length);
		svc.add(chart.series_cut);
		svc.add(chart.series_cut_len);
		svc.add(chart.value_index);
		svc.add(chart.category_desc);
		svc.add(chart.category_desc_index);
		svc.add(chart.chart_distype);
		svc.add(chart.series_attribute);
		String[] paramChart = null;
		if (svc != null && svc.size() >= 0) {
			paramChart = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("I4702", paramChart);
		// logcommon.debug("Sql I4702==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	public void updateChart(Object chartDef) throws AppException {
		PubInfoChartDefTable chart = (PubInfoChartDefTable) chartDef;
		List svc = new ArrayList();
		svc.add(chart.chart_belong);
		svc.add(chart.chart_type);
		svc.add(chart.chart_index);
		svc.add(chart.chart_attribute);
		// 转换单引号
		String sql_main = chart.sql_main;
		sql_main = StringB.replace(sql_main, "'", "'||chr(39)||'");
		svc.add(sql_main);
		String sql_where = chart.sql_where;
		sql_where = StringB.replace(sql_where, "'", "'||chr(39)||'");
		svc.add(sql_where);
		String sql_order = chart.sql_order;
		sql_order = StringB.replace(sql_order, "'", "'||chr(39)||'");
		svc.add(sql_order);
		svc.add(chart.isusecd);
		svc.add(chart.category_index);
		svc.add(chart.series_index);
		svc.add(chart.series_length);
		svc.add(chart.series_cut);
		svc.add(chart.series_cut_len);
		svc.add(chart.value_index);
		svc.add(chart.category_desc);
		svc.add(chart.category_desc_index);
		svc.add(chart.chart_distype);
		svc.add(chart.series_attribute);
		svc.add(chart.chart_id);
		String[] paramChart = null;
		if (svc != null && svc.size() >= 0) {
			paramChart = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("U4703", paramChart);
		logcommon.debug("Sql U4703==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	public static void insertChartFun(Object funDef) throws AppException {
		PubInfoChartFunTable chart = (PubInfoChartFunTable) funDef;
		List svc = new ArrayList();
		svc.add(chart.chart_id);
		svc.add(chart.frame_name);
		svc.add(chart.chart_desc);
		svc.add(chart.category_desc);
		svc.add(chart.category_desc_index);
		svc.add(chart.value_index);
		svc.add(chart.wheresql);
		svc.add(chart.replace_content);
		svc.add(chart.is_checked);
		String tmpSql = "select COALESCE(max(COL_SEQUENCE)+1,1) from UI_PUB_INFO_CHART_FUN_DEF WHERE CHART_ID='"
				+ chart.chart_id + "' ";
		String[][] arrValue = WebDBUtil.execQryArray(tmpSql, "");
		String sequence = arrValue[0][0];
		svc.add(sequence);
		String[] paramChart = null;
		if (svc != null && svc.size() >= 0) {
			paramChart = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("I4705", paramChart);
		// logcommon.debug("Sql I4705==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	public static void updateChartFun(Object funDef) throws AppException {
		PubInfoChartFunTable chart = (PubInfoChartFunTable) funDef;
		List svc = new ArrayList();
		svc.add(chart.frame_name);
		svc.add(chart.chart_desc);
		svc.add(chart.category_desc);
		svc.add(chart.category_desc_index);
		svc.add(chart.value_index);
		svc.add(chart.wheresql);
		svc.add(chart.replace_content);
		svc.add(chart.is_checked);

		svc.add(chart.chart_id);
		svc.add(chart.col_sequence);
		String[] paramChart = null;
		if (svc != null && svc.size() >= 0) {
			paramChart = (String[]) svc.toArray(new String[svc.size()]);
		}

		String strSql = SQLGenator.genSQL("U4706", paramChart);
		// logcommon.debug("Sql U4706==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	public static void deleteChartFun(Object funDef) throws AppException {
		PubInfoChartFunTable chart = (PubInfoChartFunTable) funDef;
		String chart_id = chart.chart_id;
		String col_sequence = chart.col_sequence;

		String strSql = SQLGenator.genSQL("D4707", chart_id, col_sequence);
		// logcommon.debug("Sql D4707==>" + strSql);
		WebDBUtil.execUpdate(strSql);
	}
}
