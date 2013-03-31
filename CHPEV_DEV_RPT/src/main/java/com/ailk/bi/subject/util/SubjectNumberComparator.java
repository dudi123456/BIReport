package com.ailk.bi.subject.util;

import java.util.Comparator;

import com.ailk.bi.subject.domain.TableRowStruct;

/**
 * 按数值型对象比较器
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class SubjectNumberComparator implements Comparator {
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
	public SubjectNumberComparator(int index, String order) {
		super();
		this.index = index;
		this.order = order;
	}

	public void setOrder(int index, String order) {
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
		if (comObj instanceof TableRowStruct) {
			TableRowStruct comRowStruct = (TableRowStruct) comObj;
			TableRowStruct toRowStruct = (TableRowStruct) toObj;
			double dCom = Double.parseDouble(comRowStruct.svces[index]);
			double dTo = Double.parseDouble(toRowStruct.svces[index]);
			if (dCom == dTo)
				return 0;
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC)) {
				return (dCom > dTo ? 1 : -1);
			} else {
				return (dCom > dTo ? -1 : 1);
			}
		} else {
			String[] comRow = (String[]) comObj;
			String[] toRow = (String[]) toObj;
			double dCom = Double.parseDouble(comRow[index]);
			double dTo = Double.parseDouble(toRow[index]);
			if (dCom == dTo)
				return 0;
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC)) {
				return (dCom > dTo ? 1 : -1);
			} else {
				return (dCom > dTo ? -1 : 1);
			}
		}
	}

}
