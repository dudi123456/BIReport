package com.ailk.bi.base.common;

import java.util.Comparator;

import com.ailk.bi.base.struct.*;
import com.ailk.bi.base.util.*;

/**
 * 对表格行对象中的列值按字符继续比较的比较判断器
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class AlphabeticComparator implements Comparator {
	/**
	 * 要比较的列下标
	 */
	private int index = Integer.MIN_VALUE;

	/**
	 * 排序顺序
	 */
	private String order = null;

	/**
	 * 按指定的参数进行比较器的构造
	 * 
	 * @param index
	 *            要比较的列下标
	 * @param order
	 *            排序顺序，升序或降序
	 */
	public AlphabeticComparator(int index, String order) {
		super();

		this.index = index;
		this.order = order;
	}

	/**
	 * 列进行比较
	 * 
	 * @param arg0
	 *            被比较对象，这里是行对象
	 * @param arg1
	 *            比较对象，这里是行对象
	 * @return
	 */
	public int compare(Object arg0, Object arg1) {

		// 将两个对象转换
		RowStruct rowObj1 = (RowStruct) arg0;
		RowStruct rowObj2 = (RowStruct) arg1;
		// 取出行对象中的列值
		String[] value1 = rowObj1.cols_value;
		String[] value2 = rowObj2.cols_value;
		// 开始进行比较
		if (order.equalsIgnoreCase(ConstantKeys.SORT_ASC))
			return value1[index].compareTo(value2[index]);
		else
			return value2[index].compareTo(value1[index]);
	}

}
