package com.ailk.bi.subject.util;

import java.util.Iterator;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author xdou 专题通用模版过滤工具类
 */
@SuppressWarnings({ "rawtypes" })
public class SubjectFilterUtil {
	/**
	 * 判断行是否可以显示
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param row
	 *            行结构
	 * @return 是否可以显示
	 */
	public static boolean rowCanDisplayed(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[] values, String[] sum) {
		boolean canDisplayed = false;
		if (null != values && null != curFunc.filterIndexs
				&& null != curFunc.filterLevel && null != curFunc.filterValues) {
			// 判断只有完全满足条件的行，这样应该快一些
			int maxFilterLevel = Integer.parseInt(curFunc.filterLevel);
			if (null != values) {
				String[] splitIndexs = curFunc.filterIndexs.split(",");
				String[] splitValues = curFunc.filterValues.split(",");
				for (int i = 0; i < splitIndexs.length; i++) {
					String value = getFilterIndexValue(subTable, values, sum,
							splitIndexs[i], maxFilterLevel);
					canDisplayed = fitRowFilterCondition(curFunc, i, value,
							splitValues);
					if (!canDisplayed) {
						// 只要一个不满足，退出
						break;
					}
				}
			}
		}
		return canDisplayed;
	}

	/**
	 * 获得过滤的索引的值
	 * 
	 * @param subTable
	 *            表格对象
	 * @param colsValue
	 *            一维数组
	 * @param index
	 *            列对象索引
	 * @return 列的值
	 */
	private static String getFilterIndexValue(SubjectCommTabDef subTable,
			String[] colsValue, String[] sum, String index, int maxFilterLevel) {
		String value = null;
		if (null != colsValue && null != index) {
			String firOp = null;
			String secOp = null;
			char oper = index.charAt(0);
			switch (oper) {
			case SubjectConst.FILTER_OPERATOR_COMPOSTION:
				// 取占比
				firOp = index.substring(1);
				secOp = null;
				value = getColValue(subTable, colsValue, sum, firOp, true,
						maxFilterLevel);
				break;
			case SubjectConst.FILTER_OPERATOR_RATION:
				// 进行除法运算
				String temp = index.substring(1);
				int pos = temp.indexOf("/");
				if (pos >= 0) {
					firOp = temp.substring(0, pos);
					secOp = temp.substring(pos + 1);
					value = NullProcFactory.transNullToZero(Arith.divs(
							getColValue(subTable, colsValue, sum, firOp, false,
									maxFilterLevel),
							getColValue(subTable, colsValue, sum, secOp, false,
									maxFilterLevel), 5));
				}
				break;
			case SubjectConst.FILTER_OPERATOR_VALUE:
				// 取某个值
				firOp = index.substring(1);
				value = getColValue(subTable, colsValue, sum, firOp, false,
						maxFilterLevel);
				break;
			default:
				break;
			}
		}
		return value;
	}

	/**
	 * 判断表格列的值是否满足过滤条件
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param index
	 *            列索引
	 * @param value
	 *            列值
	 * @param filter_values
	 *            过滤条件数组
	 * @return 是否满足过滤条件
	 */
	private static boolean fitRowFilterCondition(TableCurFunc curFunc,
			int index, String value, String[] filter_values) {
		boolean fitted = false;
		if (null != value && null != filter_values) {
			int low = 2 * index;
			int high = low + 1;
			String low_value = filter_values[low];
			String high_value = filter_values[high];
			try {
				int pos = low_value.indexOf(">=");
				if (pos >= 0) {
					low_value = low_value.substring(pos + 2);
				}
				pos = low_value.indexOf(">");
				if (pos >= 0) {
					low_value = low_value.substring(pos + 1);
				}
				boolean hasEqual = false;
				pos = high_value.indexOf("<=");
				if (pos >= 0) {
					high_value = high_value.substring(pos + 2);
					hasEqual = true;
				}
				pos = high_value.indexOf("<");
				if (pos >= 0) {
					high_value = high_value.substring(pos + 1);
				}
				double ld = Double.parseDouble(low_value);
				double hd = Double.parseDouble(high_value);
				double dv = Double.parseDouble(value);
				if (hasEqual) {
					if (dv >= ld && dv <= hd) {
						fitted = true;
					} else {
						fitted = false;
					}
				} else {
					if (dv >= ld && dv < hd) {
						fitted = true;
					} else {
						fitted = false;
					}
				}
				if (curFunc.isFilterMin
						&& value.equalsIgnoreCase(Integer.MIN_VALUE + "")) {
					fitted = false;
				}
				if (curFunc.isFilterMax
						&& value.equalsIgnoreCase(Integer.MAX_VALUE + "")) {
					fitted = true;
				}
			} catch (NumberFormatException nfe) {
				fitted = false;
			}
		}
		return fitted;
	}

	/**
	 * 获得指定列的值
	 * 
	 * @param subTable
	 *            表格对象
	 * @param colsValue
	 *            一维数组
	 * @param index
	 *            列索引
	 * @param isCamRatio
	 *            是否占比
	 * @return 列值
	 */
	private static String getColValue(SubjectCommTabDef subTable,
			String[] colsValue, String[] sum, String index, boolean isCamRatio,
			int maxFilterLevel) {
		String value = null;
		if (null != colsValue && null != index) {
			int realIndex = -1;
			int msuIndex = -1;
			int dimsCount = -1;
			Iterator iter = subTable.tableCols.iterator();
			String tempValue = null;
			while (iter.hasNext()) {
				realIndex++;
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
					// 首先是显示列
					if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
						// 维度
						if (SubjectConst.YES
								.equalsIgnoreCase(tabCol.is_expand_col)) {
							dimsCount++;
							realIndex++;
							dimsCount += maxFilterLevel;
							realIndex += maxFilterLevel;
							msuIndex = realIndex + dimsCount;
						} else {
							dimsCount++;
							msuIndex = realIndex + dimsCount;
						}
					} else {
						// 指标
						msuIndex++;
						// 本期值
						tempValue = colsValue[msuIndex];
						if (!isCamRatio && (tabCol.col_id).equals(index)) {
							value = tempValue;
						}

						// 占总比
						tempValue = Arith.divs(colsValue[msuIndex],
								sum[msuIndex], 5);
						if (isCamRatio && (tabCol.col_id).equals(index)) {
							value = tempValue;
							break;
						}
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
							msuIndex++;
							if (SubjectConst.YES
									.equalsIgnoreCase(tabCol.has_loop)) {
								msuIndex++;
							}
							msuIndex++;
						}
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
							if (SubjectConst.NO
									.equalsIgnoreCase(tabCol.has_last)) {
								msuIndex++;
							}
							msuIndex++;
						}
					}
				}
			}
		}
		return value;
	}
}
