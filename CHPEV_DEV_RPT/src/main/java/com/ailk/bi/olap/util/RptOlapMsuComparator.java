package com.ailk.bi.olap.util;

import java.util.Comparator;

import com.ailk.bi.base.table.RptOlapMsuTable;

@SuppressWarnings({ "rawtypes" })
public class RptOlapMsuComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1) {
		RptOlapMsuTable msu1 = (RptOlapMsuTable) arg0;
		RptOlapMsuTable msu2 = (RptOlapMsuTable) arg1;
		int order1 = Integer.parseInt(msu1.order_id);
		int order2 = Integer.parseInt(msu2.order_id);
		if (order1 < order2)
			return -1;
		if (order1 > order2)
			return 1;
		return 0;
	}

}
