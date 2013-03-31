package com.ailk.bi.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapChartTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapChartStructUtil {
	public static List parseRequest(HttpServletRequest request,
			List chartStructs) throws ReportOlapException {
		if (null == request || null == chartStructs || 0 >= chartStructs.size())
			throw new ReportOlapException("分析型报表图形分析生成结合请求对象生成图形域对象时输入的参数为空");
		List structs = chartStructs;
		// level_dim.index//表示当前维度层次，group_dim.index标识观察维度
		// dim_all_index
		// dim_index标识维度的值，可能多个，msu_index标识指标是否选择
		Iterator iter = structs.iterator();
		while (iter.hasNext()) {
			RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
					.next();
			// 这里不能再管是否显示
			if (chartStruct.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
						.getRptStruct();
				// 要排出时间维
				if (!chartStruct.isTime()) {
					// 看是否分组
					String groupDim = request
							.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP);
					boolean isWhereDim = false;
					String[] whereDims = request
							.getParameterValues(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE);
					isWhereDim = isArrayIncludeValue(whereDims, rptDim.dim_id);
					if (null != groupDim && !"".equals(groupDim)
							&& rptDim.dim_id.equals(groupDim)) {
						// 此维度是分组维度
						chartStruct.setDisplay(true);
						chartStruct.setUsedForGroup(true);
						String level = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL);
						if (null != level && !"".equals(level))
							chartStruct.setLevel(level);
						else
							chartStruct.setLevel(RptOlapConsts.ZERO_STR);
						String include = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE);
						if (null != include && !"".equals(include)
								&& RptOlapConsts.TRUE.equalsIgnoreCase(include))
							chartStruct.setIncludeValues(true);
						else
							chartStruct.setIncludeValues(false);
						String allValues = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL);
						if (null != allValues
								&& !"".equals(allValues)
								&& RptOlapConsts.TRUE
										.equalsIgnoreCase(allValues))
							chartStruct.setUseAllValues(true);
						else
							chartStruct.setUseAllValues(false);
						String[] values = request
								.getParameterValues(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE);
						List list = null;
						if (null != values) {
							list = new ArrayList();
							for (int i = 0; i < values.length; i++) {
								list.add(values[i]);
							}
						}
						chartStruct.setCurValues(list);
					} else if (isWhereDim) {
						// 是条件维度
						chartStruct.setDisplay(true);
						chartStruct.setUsedForGroup(false);
						String level = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL
										+ rptDim.dim_id);
						if (null != level && !"".equals(level))
							chartStruct.setLevel(level);
						else
							chartStruct.setLevel(RptOlapConsts.ZERO_STR);
						String include = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE
										+ rptDim.dim_id);
						if (null != include && !"".equals(include)
								&& RptOlapConsts.TRUE.equalsIgnoreCase(include))
							chartStruct.setIncludeValues(true);
						else
							chartStruct.setIncludeValues(false);
						String allValues = request
								.getParameter(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL
										+ rptDim.dim_id);
						if (null != allValues
								&& !"".equals(allValues)
								&& RptOlapConsts.TRUE
										.equalsIgnoreCase(allValues))
							chartStruct.setUseAllValues(true);
						else
							chartStruct.setUseAllValues(false);
						String[] values = request
								.getParameterValues(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE
										+ rptDim.dim_id);
						List list = null;
						if (null != values) {
							list = new ArrayList();
							for (int i = 0; i < values.length; i++) {
								list.add(values[i]);
							}
						}
						chartStruct.setCurValues(list);
					} else {
						// 什么都不是，不显示
						chartStruct.setDisplay(false);
					}
				}
			} else {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
						.getRptStruct();
				String[] msus = request
						.getParameterValues(RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP);
				boolean isInclude = isArrayIncludeValue(msus, rptMsu.msu_id);
				chartStruct.setDisplay(isInclude);
			}
		}
		return structs;
	}

	public static List genChartDefaultSetting(List chartStructs,
			RptOlapFuncStruct olapFun) throws ReportOlapException {
		if (null == chartStructs || 0 >= chartStructs.size() || null == olapFun)
			throw new ReportOlapException("生成图形的默认设置时输入的参数为空");
		List structs = new ArrayList();
		String func = olapFun.getCurFunc();
		Iterator iter = chartStructs.iterator();
		while (iter.hasNext()) {
			RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
					.next();
			if (chartStruct.isTime()
					&& RptOlapConsts.OLAP_FUN_LINE.equals(func)) {
				chartStruct.setDisplay(true);
				chartStruct.setUsedForGroup(true);
			}
			Map settings = chartStruct.getSets();
			if (null != settings) {
				Object tmpObj = settings.get(func);
				if (null == tmpObj) {
					chartStruct.setDisplay(false);
				} else {
					chartStruct.setDisplay(true);
					RptOlapChartTable set = (RptOlapChartTable) tmpObj;
					// 取出设置，这里不管指标或维度了
					chartStruct.setLevel(set.dim_level);
					if (RptOlapConsts.CHART_SETTING_GROUP.equals(set.attr_id))
						chartStruct.setUsedForGroup(true);
					else
						chartStruct.setUsedForGroup(false);
					if (RptOlapConsts.YES.equalsIgnoreCase(set.all_dim_values))
						chartStruct.setUseAllValues(true);
					else {
						chartStruct.setUseAllValues(false);
						// 设置值
						Map map = chartStruct.getValues();
						if (null != map) {
							tmpObj = map.get(func);
							if (null != tmpObj) {
								List values = (List) tmpObj;
								chartStruct.setCurValues(values);
							}
						}
					}
				}
			}
			structs.add(chartStruct);
		}
		return structs;
	}

	private static boolean isArrayIncludeValue(String[] values, String value)
			throws ReportOlapException {
		boolean has = false;
		if (null != values && null != value) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].equals(value)) {
					has = true;
					break;
				}
			}
		}
		return has;
	}
}
