package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.dao.IChartTableBodyDao;
import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChartTableBodyDao implements IChartTableBodyDao {

	public List getTableBody(String[][] svces, List chartStructs,
			RptOlapFuncStruct olapFun, String statPeriod, String trStyle,
			String tdStyle) throws ReportOlapException {
		if (null == svces || 0 >= svces.length || null == chartStructs
				|| null == olapFun)
			throw new ReportOlapException("生成图形分析的表体时输入的参数为空");
		long startTime = System.currentTimeMillis();
		List body = new ArrayList();
		if (null == trStyle)
			trStyle = "";
		if (null == tdStyle)
			tdStyle = "";
		StringBuffer row = new StringBuffer();
		for (int i = 0; i < svces.length; i++) {
			row.delete(0, row.length());
			row.append("<tr class=\"").append(trStyle).append("\">");
			int index = 0;
			Iterator iter = chartStructs.iterator();
			while (iter.hasNext()) {
				RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
						.next();
				if (chartStruct.isDisplay()) {
					if (chartStruct.isDim()) {
						if (chartStruct.isUsedForGroup()) {
							row.append("<td nowrap class=\"").append(tdStyle)
									.append("\">");
							index++;
							row.append(svces[i][index]);
							index++;
						}
					} else {
						row.append("<td nowrap align=\"right\" class=\"")
								.append(tdStyle).append("\">");
						RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
								.getRptStruct();
						row.append(FormatUtil.formatStr(
								svces[i][index],
								Integer.parseInt(rptMsu.msuInfo.pricesion),
								RptOlapConsts.YES
										.equalsIgnoreCase(rptMsu.msuInfo.comma_splitted) ? true
										: false));
						index++;
					}
					row.append("</td>");
				}
			}
			row.append("</tr>");
			body.add(row.toString());
		}
		System.out.println("组装图形表体用时："
				+ (System.currentTimeMillis() - startTime) + "ms");
		return body;
	}

}
