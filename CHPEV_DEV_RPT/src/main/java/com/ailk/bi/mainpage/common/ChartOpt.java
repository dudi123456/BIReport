package com.ailk.bi.mainpage.common;

import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.MainpageInfoChart;
import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.table.UiMainTableName;
import com.ailk.bi.base.util.CommChartUtil;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unused", "rawtypes" })
public class ChartOpt {

	/**
	 * 获取用户图形定义
	 * 
	 * @param user_id
	 * @return
	 * @throws AppException
	 */
	public static MainpageInfoChart[] getChartDef(String user_id)
			throws AppException {
		String strSql = SQLGenator.genSQL("Q4140", user_id);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			// 如果结果为空，检查公共配置
			user_id = "0";// 目前没有用户定制，用户id都取0公共配置
			strSql = SQLGenator.genSQL("Q4140", user_id);
			result = WebDBUtil.execQryVector(strSql, "");
		}
		if (result == null || result.size() == 0) {
			return null;
		}
		MainpageInfoChart[] tables = new MainpageInfoChart[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			tables[i] = new MainpageInfoChart();
			int m = 0;
			tables[i].chart_id = (String) tempv.get(m++);
			tables[i].page_name = (String) tempv.get(m++);
			tables[i].chart_name = (String) tempv.get(m++);
			tables[i].chart_categroy = (String) tempv.get(m++);
			tables[i].chart_categroy_index = (String) tempv.get(m++);
			tables[i].user_id = (String) tempv.get(m++);
			tables[i].chart_type = (String) tempv.get(m++);
			tables[i].chart_index = (String) tempv.get(m++);
			tables[i].user_categroy = (String) tempv.get(m++);
			tables[i].user_categroy_index = (String) tempv.get(m++);
			tables[i].series_length = (String) tempv.get(m++);
			tables[i].data_table = (String) tempv.get(m++);
			tables[i].measures = (String) tempv.get(m++);
			tables[i].date_code = (String) tempv.get(m++);
			tables[i].measure_code = (String) tempv.get(m++);
		}
		return tables;
	}

	/**
	 * 获取首页图形对象
	 * 
	 * @param ss_obj_id
	 *            制定模块ID
	 * @param area_id
	 *            用户地域权限
	 * @return 返回WebChart对象
	 * @throws AppException
	 */
	public static String getMainChart(MainpageInfoChart mainChart,
			String area_id, HttpServletRequest request, PrintWriter pw)
			throws AppException {
		HttpSession session = request.getSession();
		// 图形ID
		String chart_id = mainChart.chart_id;
		// 查询结构
		LsbiQryStruct qry = new LsbiQryStruct();
		// 地域条件
		qry.area_id = area_id;
		UiMainTableName table = new UiMainTableName();
		table.date_code = mainChart.date_code;
		table.data_table = mainChart.data_table;
		table.measure_code = mainChart.measure_code;
		// 日期条件
		qry.gather_day = MeasureTableOpt.getMaxDate(table, mainChart.measures);
		// 业务类型条件
		qry.svc_knd = "";// CommTool.getSvcRights(session).toLowerCase();
		if ("all".equals(qry.svc_knd) || "".equals(qry.svc_knd)) {
			qry.svc_knd = "1";
		}

		// 图形定义
		PubInfoChartDefTable chartDef = CommChartUtil.getChartDef(chart_id);
		// 图形数据
		String[][] data = CommChartUtil.genChartData(chartDef, request, qry);
		// 图形对象
		WebChart chart = CommChartUtil.genChartObj(chartDef, data);
		// 设置图形属性
		CommChartUtil.setChartAttribute(chart, chartDef.chart_attribute);
		// 设置用户属性
		chartDef.chart_type = mainChart.chart_type;// 图形类型
		chartDef.chart_index = mainChart.chart_index;// 特殊索引标志
		// 生成图形
		if ("14".equals(chartDef.chart_type) && data == null) {
			// 空数据雷达图bug，改变为其他图形处理
			chartDef.chart_type = "3";
		}
		return CommChartUtil.genChartHTML(chartDef, chart, request, pw);
	}
}
