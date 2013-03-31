package com.ailk.bi.olap.util;

import java.util.Comparator;

import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes" })
public class TabColComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1) {
		RptOlapTableColumnStruct tCol1 = (RptOlapTableColumnStruct) arg0;
		RptOlapTableColumnStruct tCol2 = (RptOlapTableColumnStruct) arg1;
		int order1 = tCol1.getDisplayOrder();
		int order2 = tCol2.getDisplayOrder();
		if (order1 < order2)
			return -1;
		if (order1 > order2)
			return 1;
		return 0;
	}

}
