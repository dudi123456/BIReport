package com.ailk.bi.base.common;

import java.util.Comparator;

import com.ailk.bi.base.struct.*;
import com.ailk.bi.base.util.*;

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
	 * @param arg0
	 *            被比较的行对象
	 * @param arg1
	 *            比较行对象
	 * @return
	 */
	public int compare(Object arg0, Object arg1) {

		RowStruct rowObj1 = (RowStruct) arg0;
		RowStruct rowObj2 = (RowStruct) arg1;
		String[] value1 = rowObj1.cols_value;
		String[] value2 = rowObj2.cols_value;
		double d_value1 = Double.parseDouble(value1[index]);
		double d_value2 = Double.parseDouble(value2[index]);
		if (d_value1 == d_value2)
			return 0;
		if (order.equalsIgnoreCase(ConstantKeys.SORT_ASC)) {
			if (d_value1 > d_value2)
				return 1;
			else
				return -1;
		} else {
			if (d_value1 > d_value2)
				return -1;
			else
				return 1;
		}
	}

}
