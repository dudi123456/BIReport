package com.ailk.bi.adhoc.util;

import java.util.Comparator;

@SuppressWarnings({ "rawtypes" })
public class CharacterComparator implements Comparator {

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
	public CharacterComparator(int index, String order) {
		super();

		this.index = index;
		this.order = order;
	}

	/**
	 * 列进行比较
	 * 
	 * @param arg0
	 *            被比较对象，这里是字符串行
	 * @param arg1
	 *            比较对象，这里是字符串行
	 * @return
	 */
	public int compare(Object arg0, Object arg1) {

		// 将两个对象转换
		String[] value1 = (String[]) arg0;
		String[] value2 = (String[]) arg1;
		// 开始进行比较
		if (order.equalsIgnoreCase(AdhocConstant.SORT_ASC))
			return value1[index].compareTo(value2[index]);
		else
			return value2[index].compareTo(value1[index]);
	}

}
