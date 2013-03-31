package com.ailk.bi.subject.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.action.SubjectMultiAlpComparator;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;

/**
 * @author xdou 专题通用模版排序工具类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectSortUtil {
	/**
	 * 排序表格内容
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param body
	 *            表格列表
	 * @param request
	 *            请求对象
	 * @return 排序后的表格
	 */
	public static List sortTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc, List body,
			HttpServletRequest request) {
		List content = body;
		String sortIndex = request.getParameter(SubjectConst.REQ_SORT_INDEX);
		String sortOrder = request.getParameter(SubjectConst.REQ_SORT_ORDER);
		String dataType = request.getParameter(SubjectConst.REQ_SORT_DATA_TYPE);
		if (null == dataType)
			dataType = SubjectConst.DATA_TYPE_NUMBER;
		if (null != content && null != sortIndex && null != sortOrder) {
			int index = Integer.parseInt(sortIndex);
			TableRowStruct sumRow = null;
			TableRowStruct avgRow = null;
			if (0 == curFunc.expandedRowIDs.size()) {
				content = body;
				if (null == curFunc.filterIndexs && null == curFunc.filterLevel && null == curFunc.filterValues) {
					if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby)) {
						if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.sum_pos)) {
							sumRow = (TableRowStruct) content.get(0);
							content.remove(0);
						} else {
							sumRow = (TableRowStruct) content.get(content.size() - 1);
							content.remove(content.size() - 1);
						}

						if (SubjectConst.YES.equalsIgnoreCase(subTable.has_avg)) {
							if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos)) {
								avgRow = (TableRowStruct) content.get(0);
							} else {
								avgRow = (TableRowStruct) content.get(content.size() - 2);
							}
						}

						if (SubjectConst.YES.equalsIgnoreCase(subTable.has_avg)) {
							if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos)) {
								content.remove(0);
							} else {
								content.remove(content.size() - 1);
							}
						}
					}
				}
			} else {
				content = (List) curFunc.expandedContent.getLast();
				if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby)) {
					if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.sum_pos)) {
						curFunc.expandedContent.removeFirst();
					} else {
						curFunc.expandedContent.removeLast();
					}

					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_avg)) {
						if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos)) {
							curFunc.expandedContent.removeFirst();
						} else {
							curFunc.expandedContent.removeLast();
						}
					}
				}
			}
			// 不在这里确定，应该在生成链接时实现
			if (SubjectConst.DATA_TYPE_NUMBER.equals(dataType))
				Collections.sort(content, new SubjectNumberComparator(index, sortOrder));
			else
				Collections.sort(content, new SubjectAlphabeticComparator(index, sortOrder));

			if (null != avgRow) {
				if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos)) {
					List temp = new ArrayList();
					temp.add(avgRow);
					temp.addAll(content);
					content.clear();
					content.addAll(temp);
					temp.clear();
					temp = null;
				} else {
					content.add(avgRow);
				}
			}
			if (null != sumRow) {
				if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.sum_pos)) {
					List temp = new ArrayList();
					temp.add(sumRow);
					temp.addAll(content);
					content.clear();
					content.addAll(temp);
					temp.clear();
					temp = null;
				} else {
					content.add(sumRow);
				}
			}
			curFunc.expandedContent.addLast(content);
			content = assembleList(curFunc, body, content);
		}
		return content;
	}

	public static String[][] sortDeafaultTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc, String[][] svces) {
		String[][] content = svces;
		String sortIndex = null;
		String sortOrder = null;
		// 需要判断一下，不是排序，则按默认排序
		// 循环一下
		List<SubjectCommTabCol> tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		SubjectCommTabCol tabCol = null;
		int aryIndex = 0;
		while (iter.hasNext()) {
			tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {

				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure) && SubjectConst.NO.equals(tabCol.dim_ascol)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							// 全部展开完了,加上收缩链接
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								Iterator levIter = levels.iterator();
								while (levIter.hasNext()) {
									levIter.next();
									aryIndex++;
									aryIndex++;
								}
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							// 矩阵过滤
							int level = 0;
							int maxLevel = Integer.parseInt(curFunc.filterLevel);
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && level <= maxLevel) {
									levIter.next();
									level++;
									aryIndex++;
									aryIndex++;
								}
							}
						} else {
							// 展开列,加上全部展开链接
							aryIndex++;
							aryIndex++;
						}
					} else {
						// 非展开列
						aryIndex++;
						aryIndex++;
					}
				}

				if (SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_sort)) {
						sortIndex = aryIndex + "";
						sortOrder = tabCol.sort_order;
						break;
					}
					aryIndex++;
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.last_display)) {
						aryIndex++;
					} else if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
						// 有同比
						aryIndex++;
						aryIndex++;
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.loop_display)) {
						// 环比显示
						aryIndex++;
						aryIndex++;
					} else if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
						// 有环比
						aryIndex++;
						aryIndex++;
					}
				}

			}
		}
		String dataType = SubjectConst.DATA_TYPE_NUMBER;
		if (null != sortOrder) {
			if (sortOrder.equalsIgnoreCase("ASC")) {
				sortOrder = SubjectConst.SORT_ASC;
			} else {
				sortOrder = SubjectConst.SORT_DESC;
			}
		}
		if (null != content && null != sortIndex && null != sortOrder) {

			int index = Integer.parseInt(sortIndex);
			curFunc.sortOrder = sortOrder;
			curFunc.sortedColum = index;
			// 不在这里确定，应该在生成链接时实现
			Map<Integer, String> others = new HashMap<Integer, String>();
			// 这里写死了，按照第一个维度的名称排序
			others.put(Integer.valueOf(1), SubjectConst.SORT_DESC);
			if (SubjectConst.DATA_TYPE_NUMBER.equals(dataType))
				Arrays.sort(content, 0, SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) ? content.length - 1
						: content.length, new SubjectMultiNumComparator(index, sortOrder, others));
			else
				Arrays.sort(content, 0, SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) ? content.length - 1
						: content.length, new SubjectMultiAlpComparator(index, sortOrder, others));
		}
		return content;
	}

	public static String[][] sortTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc, String[][] svces,
			HttpServletRequest request) {
		String[][] content = svces;
		String sortIndex = request.getParameter(SubjectConst.REQ_SORT_INDEX);
		String sortOrder = request.getParameter(SubjectConst.REQ_SORT_ORDER);
		String dataType = request.getParameter(SubjectConst.REQ_SORT_DATA_TYPE);
		if (null == dataType)
			dataType = SubjectConst.DATA_TYPE_NUMBER;
		if (null != content && null != sortIndex && null != sortOrder) {
			int index = Integer.parseInt(sortIndex);
			// 不在这里确定，应该在生成链接时实现
			if (SubjectConst.DATA_TYPE_NUMBER.equals(dataType))
				Arrays.sort(content, 0, SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) ? content.length - 1
						: content.length, new SubjectNumberComparator(index, sortOrder));
			else
				Arrays.sort(content, 0, SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) ? content.length - 1
						: content.length, new SubjectAlphabeticComparator(index, sortOrder));
		}
		return content;
	}

	/**
	 * 将缓存的展开链表的最后一个行对象集合 排序后正确插入到原来的位置处
	 * 
	 * @return 插入后的行对象集合
	 */
	private static List assembleList(TableCurFunc curFunc, List body, List content) {
		List ret = null;
		if (null != curFunc.expandedContent && 0 < curFunc.expandedRowIDs.size()) {
			// 如果缓存的链表不为空
			if (null == body) {
				// 如果要插入的集合为空,直接取缓存的最后一个
				ret = content;
			} else {
				// 插入额集合不为空
				List tmpCon = new ArrayList();
				// 循环插入集合
				Iterator iter = body.iterator();
				// 获取被插入集合
				List tempList = (List) curFunc.expandedContent.getLast();
				// 获取插入位置的行的唯一标识
				String temp_row_id = (String) curFunc.expandedRowIDs.getLast();
				// 要插入的行对象数目
				int count = tempList.size();
				// 行的序号
				int index = -1;
				// 插入位置处
				int from = -1;
				// 去除原来的行对象
				while (iter.hasNext()) {
					index++;
					// 获取行对象
					TableRowStruct rowObj = (TableRowStruct) iter.next();
					if (null != rowObj && null != rowObj.row_id && null != temp_row_id
							&& temp_row_id.equals(rowObj.row_id)) {
						// 判断是否是插入位置处，是
						// 将源行对象插入
						tmpCon.add(rowObj);
						// 设置插入位置
						from = index;
					} else if (from == -1 || index > (from + count)) {
						// 如果不是插入位置，则添加进，先将插入处的行对象去掉
						tmpCon.add(rowObj);
					}
				}
				ret = new ArrayList();
				iter = tmpCon.iterator();
				// 插入新的排序后的对象
				while (iter.hasNext()) {
					TableRowStruct rowObj = (TableRowStruct) iter.next();
					if (null != rowObj && null != rowObj.row_id && null != temp_row_id
							&& temp_row_id.equals(rowObj.row_id)) {
						// 判断是否是插入位置处，是
						// 将源行对象插入
						ret.add(rowObj);
						// 插入排序后的行对象
						ret.addAll(tempList);
					} else {
						// 插入其他对象
						ret.add(rowObj);
					}
				}
			}
		} else {
			// 如果要插入的对象为空，直接返回原来的
			ret = content;
		}
		return ret;
	}
}
