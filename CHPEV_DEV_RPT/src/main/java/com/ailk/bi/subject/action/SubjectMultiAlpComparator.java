package com.ailk.bi.subject.action;

import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.ailk.bi.subject.domain.TableRowStruct;
import com.ailk.bi.subject.util.SubjectAlphabeticComparator;
import com.ailk.bi.subject.util.SubjectConst;

public class SubjectMultiAlpComparator extends SubjectAlphabeticComparator {
	private Map<Integer, String> others;
	@SuppressWarnings("rawtypes")
	private Comparator c = Collator.getInstance(Locale.CHINA);

	public SubjectMultiAlpComparator(int index, String order,
			Map<Integer, String> others) {
		super(index, order);
		this.others = others;
	}

	@Override
	public int compare(Object comObj, Object toObj) {
		int ret = super.compare(comObj, toObj);
		if (ret == 0) {
			Iterator<Map.Entry<Integer, String>> iter = others.entrySet()
					.iterator();
			Map.Entry<Integer, String> entry = null;
			while (ret == 0 && iter.hasNext()) {
				entry = iter.next();
				ret = compareAsText(entry.getKey().intValue(),
						entry.getValue(), comObj, toObj);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private int compareAsText(int index, String order, Object comObj,
			Object toObj) {

		if (comObj instanceof TableRowStruct) {
			// 将两个对象转换
			TableRowStruct comRowStruct = (TableRowStruct) comObj;
			TableRowStruct toRowStruct = (TableRowStruct) toObj;
			// 开始进行比较
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC))
				return c.compare(toRowStruct.svces[index],
						comRowStruct.svces[index]);
			else
				return c.compare(comRowStruct.svces[index],
						toRowStruct.svces[index]);
		} else {
			String[] comRow = (String[]) comObj;
			String[] toRow = (String[]) toObj;
			// 开始进行比较
			if (order.equalsIgnoreCase(SubjectConst.SORT_ASC))
				return c.compare(toRow[index], comRow[index]);
			else
				return c.compare(comRow[index], toRow[index]);
		}
	}
}
