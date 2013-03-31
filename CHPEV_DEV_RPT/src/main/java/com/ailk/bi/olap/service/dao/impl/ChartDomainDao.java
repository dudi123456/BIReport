package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapChartTable;
import com.ailk.bi.base.table.RptOlapChartTimeTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.service.dao.IChartDomainDao;
import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChartDomainDao implements IChartDomainDao {

	public List genDefaultSetting(String reportId, List rptDims, List rptMsus)
			throws ReportOlapException {
		if (null == reportId || "".equals(reportId))
			throw new ReportOlapException("生成图形分析的默认设置时输入的参数为空为空");
		List chartStructs = new ArrayList();
		Map sets = getDefaultSetting(reportId);
		int index = 0;
		Iterator iter = rptDims.iterator();
		while (iter.hasNext()) {
			RptOlapDimTable rptDim = (RptOlapDimTable) iter.next();
			RptOlapChartAttrStruct chartStruct = new RptOlapChartAttrStruct();
			chartStruct.setDim(true);
			chartStruct.setTime(RptOlapConsts.YES
					.equalsIgnoreCase(rptDim.is_timedim) ? true : false);
			chartStruct.setRptStruct(rptDim);
			chartStruct = assambleAttr(chartStruct, rptDim, sets,
					RptOlapConsts.OLAP_FUN_LINE);
			chartStruct = assambleAttr(chartStruct, rptDim, sets,
					RptOlapConsts.OLAP_FUN_BAR);
			chartStruct = assambleAttr(chartStruct, rptDim, sets,
					RptOlapConsts.OLAP_FUN_PIE);
			chartStruct.setIndex(index + "");
			index++;
			chartStructs.add(chartStruct);
		}
		iter = rptMsus.iterator();
		while (iter.hasNext()) {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) iter.next();
			RptOlapChartAttrStruct chartStruct = new RptOlapChartAttrStruct();
			chartStruct.setDim(false);
			chartStruct.setTime(false);
			chartStruct.setRptStruct(rptMsu);
			chartStruct.setUseAllValues(false);
			chartStruct.setUsedForGroup(false);
			RptOlapChartTable set = null;
			Object tmpObj = sets.get(rptMsu.msu_id + "_"
					+ RptOlapConsts.OLAP_FUN_LINE);
			if (null != tmpObj) {
				set = (RptOlapChartTable) tmpObj;
				Map map = chartStruct.getSets();
				if (null == map)
					map = new HashMap();
				map.put(RptOlapConsts.OLAP_FUN_LINE, set);
				chartStruct.setSets(map);
			}
			tmpObj = sets.get(rptMsu.msu_id + "_" + RptOlapConsts.OLAP_FUN_BAR);
			if (null != tmpObj) {
				set = (RptOlapChartTable) tmpObj;
				Map map = chartStruct.getSets();
				if (null == map)
					map = new HashMap();
				map.put(RptOlapConsts.OLAP_FUN_BAR, set);
				chartStruct.setSets(map);
			}
			tmpObj = sets.get(rptMsu.msu_id + "_" + RptOlapConsts.OLAP_FUN_PIE);
			if (null != tmpObj) {
				set = (RptOlapChartTable) tmpObj;
				Map map = chartStruct.getSets();
				if (null == map)
					map = new HashMap();
				map.put(RptOlapConsts.OLAP_FUN_PIE, set);
				chartStruct.setSets(map);
			}
			index++;
			chartStruct.setIndex(index + "");
			chartStructs.add(chartStruct);
		}
		return chartStructs;
	}

	private RptOlapChartAttrStruct assambleAttr(
			RptOlapChartAttrStruct chartStruct, RptOlapDimTable rptDim,
			Map sets, String chartType) throws ReportOlapException {
		if (null == chartStruct || null == rptDim || null == sets
				|| null == chartType || "".equals(chartType))
			throw new ReportOlapException("分析设置图形域对象时输入的参数为空");
		RptOlapChartAttrStruct struct = chartStruct;
		RptOlapChartTable set = null;
		Object tmpObj = sets.get(rptDim.dim_id + "_" + chartType);
		if (null != tmpObj) {
			set = (RptOlapChartTable) tmpObj;
			Map map = struct.getSets();
			if (null == map)
				map = new HashMap();
			map.put(chartType, set);
			struct.setSets(map);
			struct.setDisplay(true);
			if (RptOlapConsts.CHART_SETTING_GROUP.equals(set.attr_id)) {
				struct.setUsedForGroup(true);
			} else {
				struct.setUsedForGroup(false);
			}
			//
			if (RptOlapConsts.YES.equalsIgnoreCase(set.all_dim_values)) {
				struct.setUseAllValues(true);
			} else {
				struct.setUseAllValues(false);
				List values = new ArrayList();
				String value = set.dim_value;
				String[] arry = value.split(",");
				for (int i = 0; i < arry.length; i++)
					values.add(arry[i]);
				Map mValues = chartStruct.getValues();
				if (null == mValues)
					mValues = new HashMap();
				mValues.put(chartType, values);
				struct.setValues(mValues);
			}
		}
		return struct;
	}

	private Map getDefaultSetting(String reportId) throws ReportOlapException {
		if (null == reportId || "".equals(reportId))
			throw new ReportOlapException("获取图形分析的默认设置时输入的报表标识为空");
		Map settings = new HashMap();
		try {
			String select = SQLGenator.genSQL("Q5800", reportId);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces) {
				for (int i = 0; i < svces.length; i++) {
					RptOlapChartTable set = genChartSet(svces[i]);
					// 默认规则是一个维度在一种图形属性下不会出现多次
					settings.put(set.dim_msu_id + "_" + set.chart_type, set);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取图形分析的默认设置时发生数据库错误", ae);
		}
		return settings;
	}

	private RptOlapChartTable genChartSet(String[] svces)
			throws ReportOlapException {
		if (null == svces || 0 >= svces.length)
			throw new ReportOlapException("生成图形默认设置对象时输入的参数为空");
		RptOlapChartTable set = new RptOlapChartTable();
		set.report_id = svces[0];
		set.chart_type = svces[1];
		set.attr_id = svces[2];
		set.dim_msu_id = svces[3];
		set.all_dim_values = svces[4];
		set.dim_level = svces[5];
		set.dim_value = svces[6];
		RptOlapChartTimeTable chartTime = new RptOlapChartTimeTable();
		chartTime.report_id = set.report_id;
		chartTime.chart_type = set.chart_type;
		if (null != svces[7] && !"".equals(svces[7]))
			chartTime.time_due = svces[7];
		else
			chartTime.time_due = RptOlapConsts.ZERO_STR;
		set.chartTime = chartTime;
		return set;
	}
}
