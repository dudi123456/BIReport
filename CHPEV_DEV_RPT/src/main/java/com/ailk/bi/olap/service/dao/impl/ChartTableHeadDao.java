package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.dao.IChartTableHeadDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChartTableHeadDao implements IChartTableHeadDao {

	public List genChartHTMLHead(List chartStructs, RptOlapFuncStruct olapFun,
			String trStyle, String tdStyle) throws ReportOlapException {
		if (null == chartStructs || 0 >= chartStructs.size() || null == olapFun)
			throw new ReportOlapException("生成图形分析的表头时输入的参数为空");
		List head = new ArrayList();
		if (null == trStyle)
			trStyle = "";
		if (null == tdStyle)
			tdStyle = "";
		StringBuffer firstRow = new StringBuffer();
		firstRow.append("<tr class=\"").append(trStyle).append("\">");
		Iterator iter = chartStructs.iterator();
		while (iter.hasNext()) {
			RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
					.next();
			if (chartStruct.isDisplay()) {
				if (chartStruct.isDim()) {
					if (chartStruct.isUsedForGroup()) {
						// 维度需要根据
						RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
								.getRptStruct();
						firstRow.append("<td nowrap class=\"").append(tdStyle)
								.append("\">");
						firstRow.append(rptDim.dimInfo.dim_name);
						firstRow.append("</td>");
					}
				} else {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
							.getRptStruct();
					firstRow.append("<td nowrap align=\"right\" class=\"")
							.append(tdStyle).append("\">");
					firstRow.append("".equals(rptMsu.col_name) ? rptMsu.msuInfo.msu_name
							: rptMsu.col_name);
					firstRow.append("</td>");
				}
			}
		}
		firstRow.append("</tr>");
		head.add(firstRow.toString());
		return head;
	}
}
