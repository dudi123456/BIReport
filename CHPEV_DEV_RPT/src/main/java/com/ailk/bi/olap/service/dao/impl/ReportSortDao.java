package com.ailk.bi.olap.service.dao.impl;

import java.util.Arrays;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.dao.IReportSortDao;
import com.ailk.bi.olap.util.NumberComparator;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapMsuUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportSortDao implements IReportSortDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportSortDao#sortTableContent(java.util
	 * .List, com.asiabi.olap.domain.RptOlapFunc, java.lang.String[][])
	 */
	public String[][] sortTableContent(List tabCols, RptOlapFuncStruct olapFun,
			String statPeriod, String[][] svces) throws ReportOlapException {
		if (null == tabCols || null == olapFun || null == svces
				|| 0 >= tabCols.size() || 0 >= svces.length)
			throw new ReportOlapException("对表格进行排序时输入的参数为空");
		String aryIndex = RptOlapMsuUtil.getSortAryIndex(tabCols, olapFun,
				statPeriod);
		int index = Integer.parseInt(aryIndex);
		// 累计值排序
		if (RptOlapConsts.SORT_THIS_SUM.equals(olapFun.getSortThis()))
			index++;
		// 环比或者同比排序
		if (RptOlapConsts.SORT_THIS_RATIO.equals(olapFun.getSortThis())) {
			if (RptOlapConsts.RESET_MODE_EXPAND
					.equals(olapFun.getDisplayMode())
					&& RptOlapConsts.OLAP_FUN_LAST.equals(olapFun.getCurFunc())) {
				index++;
				index++;
			}
			index++;
			index++;
		}
		// 周同比排序
		if (RptOlapConsts.SORT_THIS_WEEK_RATIO.equals(olapFun.getSortThis())) {
			index++;
			index++;
			index++;
			index++;
		}
		String sortOrder = olapFun.getSortOrder();
		Arrays.sort(svces, 0, svces.length - 1, new NumberComparator(index,
				sortOrder));
		return svces;
	}

}
