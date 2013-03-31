package com.ailk.bi.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapDimStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapChartUtil {
	public static RptOlapChartAttrStruct getTimeStruct(List chartStructs)
			throws ReportOlapException {
		if (null == chartStructs || 0 >= chartStructs.size())
			throw new ReportOlapException("获取时间维时输入的参数维空");
		RptOlapChartAttrStruct chartStruct = null;
		Iterator iter = chartStructs.iterator();
		while (iter.hasNext()) {
			chartStruct = (RptOlapChartAttrStruct) iter.next();
			if (chartStruct.isTime()) {
				break;
			}
		}
		return chartStruct;
	}

	public static RptOlapChartAttrStruct getSelectedChartStruct(String dimId,
			List chartStructs) throws ReportOlapException {
		if (null == dimId || "".equals(dimId) || null == chartStructs
				|| 0 >= chartStructs.size())
			throw new ReportOlapException("获得选择的维度域对象时输入的参数为空");
		RptOlapChartAttrStruct chartStruct = null;
		Iterator iter = chartStructs.iterator();
		while (iter.hasNext()) {
			chartStruct = (RptOlapChartAttrStruct) iter.next();
			if (chartStruct.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
						.getRptStruct();
				if (dimId.equals(rptDim.dim_id)) {
					break;
				}
			}
		}
		return chartStruct;
	}

	/**
	 * 此处获得某个维度对于数据表而言的维度值
	 *
	 * @param rptDim
	 * @param report
	 * @param ds
	 * @return
	 * @throws ReportOlapException
	 */
	public static List getDimValues(RptOlapChartAttrStruct chartStruct,
			RptOlapChartAttrStruct rptTime, PubInfoResourceTable report,
			RptOlapDateStruct ds) throws ReportOlapException {
		if (null == chartStruct || null == report || null == ds)
			throw new ReportOlapException("获取维度的维度值时输入的参数为空");
		// 这里时间应已经正确设置
		StringBuffer select = new StringBuffer("");
		String level = chartStruct.getLevel();
		if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level))
			level = RptOlapConsts.ZERO_STR;
		String start = ds.getEnd();
		if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(report.cycle))
			start = start.substring(0, 4) + "01";
		if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle))
			start = start.substring(0, 6) + "01";
		String end = ds.getEnd();
		if (end.length() > 6) {
			end = com.ailk.bi.common.app.DateUtil.getDiffDay(1, end);
		} else {
			end = com.ailk.bi.common.app.DateUtil.getDiffMonth(1, end);
		}

		RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct.getRptStruct();
		RptOlapDimTable time = (RptOlapDimTable) rptTime.getRptStruct();
		String timeFld = time.dimInfo.code_idfld;
		List list = rptDim.dimInfo.dim_levels;
		if (null == list || 0 >= list.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			// 没有其他层次
			select.append("SELECT /*+ ORDERED */ ");
			select.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
					.append(rptDim.dimInfo.code_idfld).append(",");
			select.append("D").append(chartStruct.getIndex()).append("_")
					.append(level).append(".")
					.append(rptDim.dimInfo.code_descfld);
			select.append(" FROM ");
			select.append(rptDim.dimInfo.dim_table).append(" D")
					.append(chartStruct.getIndex()).append("_").append(level);
			select.append(",");
			select.append(report.data_table).append(" ")
					.append(RptOlapConsts.THIS_VIR_TAB_NAME);
			select.append(" WHERE 1=1 ");
			String dataWhere = report.data_where;
			if (null != dataWhere && !"".equals(dataWhere)) {
				dataWhere = dataWhere.toUpperCase();
				dataWhere = dataWhere.replaceAll("WHERE 1=1", "");
				dataWhere = dataWhere.replaceAll("WHERE", "");
				dataWhere = RptOlapStringUtil.replaceVirTabName(dataWhere,
						RptOlapConsts.THIS_VIR_TAB_NAME);
				if (null != dataWhere && !"".equals(dataWhere))
					select.append(" AND ").append(dataWhere);
			}
			// 时间范围还没加上，
			select.append(" AND ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
					.append(".").append(timeFld).append(">=").append(start);
			select.append(" AND ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
					.append(".").append(timeFld).append("<").append(end);
			select.append(" AND ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
					.append(".").append(rptDim.dimInfo.code_idfld).append("=");
			select.append("D").append(chartStruct.getIndex()).append("_")
					.append(level).append(".")
					.append(rptDim.dimInfo.code_idfld);
			select.append(" GROUP BY ");
			select.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
					.append(rptDim.dimInfo.code_idfld).append(",");
			select.append("D").append(chartStruct.getIndex()).append("_")
					.append(level).append(".")
					.append(rptDim.dimInfo.code_descfld);
		} else {
			// 多层次,也有可能是第0层
			select.delete(0, select.length());
			DimLevelTable[] levels = (DimLevelTable[]) list
					.toArray(new DimLevelTable[list.size()]);
			StringBuffer where = new StringBuffer();
			int realMaxLevel = Integer
					.parseInt(levels[levels.length - 1].lvl_id);
			int maxLevel = Integer.parseInt(rptDim.max_level);
			if (maxLevel > realMaxLevel)
				maxLevel = realMaxLevel;
			int init = 0;
			int min = 0;
			for (int i = 0; i < levels.length; i++) {
				if ((maxLevel + "").equals(levels[i].lvl_id)) {
					init = i;
				}
				if (level.equals(levels[i].lvl_id)) {
					min = i;
				}
			}
			String prefix = "";
			String codeFld = "";
			String descFld = "";
			String parentFld = "";
			for (int i = init; i >= min; i--) {
				prefix = " D" + chartStruct.getIndex() + "_" + (i + 1);
				codeFld = levels[i].code_idfld;
				descFld = levels[i].code_descfld;
				parentFld = levels[i].parent_idfld;
				if (i == init) {
					// 关联数据表和维表
					where.append(" AND ")
							.append(RptOlapConsts.THIS_VIR_TAB_NAME)
							.append(".").append(levels[i].code_idfld)
							.append("=").append(prefix + ".")
							.append(levels[i].code_idfld);
				} else {
					// 本层关联上层
					String upPrefix = " D" + chartStruct.getIndex() + "_"
							+ (i + 2);
					where.append(" AND ").append(prefix + ".")
							.append(levels[i + 1].parent_idfld).append("=")
							.append(upPrefix).append(".")
							.append(levels[i].code_idfld);
				}
			}
			// 如果是第0层
			if (RptOlapConsts.ZERO_STR.equals(level)) {

				where.append(" AND D").append(chartStruct.getIndex())
						.append("_0").append(".")
						.append(rptDim.dimInfo.code_idfld).append("=")
						.append(prefix).append(".").append(parentFld);
				prefix = " D" + chartStruct.getIndex() + "_0";
				codeFld = rptDim.dimInfo.code_idfld;
				descFld = rptDim.dimInfo.code_descfld;
			}
			// 查询加上
			select.append(" SELECT /*+ ORDERED */ ");
			select.append(prefix).append(".").append(codeFld).append(",");
			select.append(prefix).append(".").append(descFld);
			select.append(" FROM ");
			select.append(RptOlapDimUtil.getChartFromPart(chartStruct));
			select.append(report.data_table).append(" ")
					.append(RptOlapConsts.THIS_VIR_TAB_NAME);
			select.append(",");
			String tmpStr = select.toString();
			tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
			select.delete(0, select.length());
			select.append(tmpStr);
			select.append(" WHERE 1=1 ");
			String dataWhere = report.data_where;
			if (null != dataWhere && !"".equals(dataWhere)) {
				dataWhere = dataWhere.toUpperCase();
				dataWhere = dataWhere.replaceAll("WHERE 1=1", "");
				dataWhere = dataWhere.replaceAll("WHERE", "");
				dataWhere = RptOlapStringUtil.replaceVirTabName(dataWhere,
						RptOlapConsts.THIS_VIR_TAB_NAME);
				if (null != dataWhere && !"".equals(dataWhere))
					select.append(" AND ").append(dataWhere);
			}
			select.append(where);
			select.append(" AND ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
					.append(".").append(timeFld).append(">=").append(start);
			select.append(" AND ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
					.append(".").append(timeFld).append("<").append(end);
			select.append(" GROUP BY ");
			select.append(prefix).append(".").append(codeFld).append(",");
			select.append(prefix).append(".").append(descFld);
			select.append(" ORDER BY ");
			select.append(prefix).append(".").append(codeFld);
		}
		List dimValues = new ArrayList();
		try {
			String sql = select.toString();
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				for (int i = 0; i < svces.length; i++) {
					RptOlapDimStruct dimStruct = new RptOlapDimStruct();
					dimStruct.setDim_id(svces[i][0]);
					dimStruct.setDim_desc(svces[i][1]);
					dimValues.add(dimStruct);
				}
			}
		} catch (AppException e) {
			throw new ReportOlapException("获取数据维度值时发生数据库错误", e);
		}
		return dimValues;
	}

	public static List setDimStructChecked(RptOlapChartAttrStruct chartStruct,
			List dimValues) throws ReportOlapException {
		if (null == chartStruct || null == dimValues)
			throw new ReportOlapException("设置维度的是否选中时输入的参数为空");
		List values = new ArrayList();
		boolean useAllValues = chartStruct.isUseAllValues();
		List selectedValues = chartStruct.getCurValues();
		Iterator iter = dimValues.iterator();
		while (iter.hasNext()) {
			RptOlapDimStruct valueStruct = (RptOlapDimStruct) iter.next();
			if (useAllValues) {
				valueStruct.setChecked(true);
			} else {
				if (null != selectedValues
						&& selectedValues.contains(valueStruct.getDim_id()))
					valueStruct.setChecked(true);
				else
					valueStruct.setChecked(false);
			}
			values.add(valueStruct);
		}
		return values;
	}
}
