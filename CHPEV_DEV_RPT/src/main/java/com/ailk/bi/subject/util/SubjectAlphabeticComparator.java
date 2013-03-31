package com.ailk.bi.subject.util;

import java.util.Comparator;

import com.ailk.bi.subject.domain.TableRowStruct;

/**
 * 对表格行对象中的列值按字符继续比较的比较判断器
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class SubjectAlphabeticComparator implements Comparator {
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
	public SubjectAlphabeticComparator(int index, String order) {
		super();
		this.index = index;
		this.order = order;
	}

	/**
	 * 列进行比较
	 * 
	 * @param comObj
	 *            被比较对象，这里是字符串行
	 * @param toObj
	 *            比较对象，这里是字符串行
	 * @return
	 */
	public int compare(Object comObj, Object toObj) {
		if (comObj instanceof TableRowStruct) {
			// 将两个对象转换
			TableRowStruct comRowStruct = (TableRowStruct) comObj;
			TableRowStruct toRowStruct = (TableRowStruct) toObj;
			// 开始进行比较
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC))
				return toRowStruct.svces[index]
						.compareTo(comRowStruct.svces[index]);
			else
				return comRowStruct.svces[index]
						.compareTo(toRowStruct.svces[index]);
		} else {
			String[] comRow = (String[]) comObj;
			String[] toRow = (String[]) toObj;
			// 开始进行比较
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC))
				return toRow[index].compareTo(comRow[index]);
			else
				return comRow[index].compareTo(toRow[index]);
		}
	}
}
