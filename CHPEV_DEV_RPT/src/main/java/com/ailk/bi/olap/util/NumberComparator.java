package com.ailk.bi.olap.util;

import java.util.Comparator;

/**
 * 按数值型对象比较器
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class NumberComparator implements Comparator {
	/**
	 * 排序的列下标
	 */
	private int index = Integer.MIN_VALUE;

	/**
	 * 排序顺序
	 */
	private String order = null;

	/**
	 * 构造比较器
	 * 
	 * @param index
	 *            排序下标
	 * @param order
	 *            排序顺序
	 */
	public NumberComparator(int index, String order) {
		super();

		this.index = index;
		this.order = order;
	}

	/**
	 * 对行对象的列进行比较
	 * 
	 * @param comObj
	 *            被比较的行对象
	 * @param toObj
	 *            比较行对象
	 * @return
	 */
	public int compare(Object comObj, Object toObj) {

		String[] aryComStr = (String[]) comObj;
		String[] aryToStr = (String[]) toObj;
		double dCom = Double.parseDouble(aryComStr[index]);
		double dTo = Double.parseDouble(aryToStr[index]);
		if (dCom == dTo)
			return 0;
		if (order.equalsIgnoreCase(RptOlapConsts.SORT_ASC)) {
			return (dCom > dTo ? 1 : -1);
		} else {
			return (dCom > dTo ? -1 : 1);
		}
	}

}
