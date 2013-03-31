package com.ailk.bi.olap.util;

import java.util.Comparator;

import com.ailk.bi.base.table.RptOlapDimTable;

@SuppressWarnings({ "rawtypes" })
public class RptOlapDimComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1) {
		RptOlapDimTable dim1 = (RptOlapDimTable) arg0;
		RptOlapDimTable dim2 = (RptOlapDimTable) arg1;
		int order1 = Integer.parseInt(dim1.display_order);
		int order2 = Integer.parseInt(dim2.display_order);
		if (order1 < order2)
			return -1;
		if (order1 > order2)
			return 1;
		return 0;
	}

}
