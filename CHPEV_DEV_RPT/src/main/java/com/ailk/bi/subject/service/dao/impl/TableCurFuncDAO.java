package com.ailk.bi.subject.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;
import com.ailk.bi.subject.service.dao.ITableCurFuncDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectCurFuncUtil;

/**
 * @author xdou 实现表格功能对象的当前功能类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableCurFuncDAO implements ITableCurFuncDAO {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#collapse(com.asiabi.subject
	 * .domain.TableCurFunc, java.util.List)
	 */
	public List collapse(TableCurFunc curFunc, List preBody)
			throws SubjectException {
		if (null == curFunc)
			throw new SubjectException("表格收缩时功能对象为空");
		if (null == preBody)
			throw new SubjectException("表格收缩时表格行对象列表为空");
		String collpseRowId = curFunc.collpaseRowId;
		if (null == collpseRowId)
			throw new SubjectException("表格收缩时收缩行标识为空");
		boolean removeRowStarted = false;
		// 行标识的组成，扩展列标识_层次水平_维度值_非扩展列标识_维度值
		// 取出前两位
		String[] subStrs = collpseRowId.split("_");
		if (null == subStrs || subStrs.length < 2)
			throw new SubjectException("表格收缩时收缩行标识不正确");
		curFunc.rowSpanDimValue = null;
		// 收缩水平
		String collapseLevel = subStrs[1];
		// 收缩后的行结构
		List body = new ArrayList();
		List removedRowIds = new ArrayList();
		// 跨行数
		int rowSpan = 0;
		int count = 0;
		// 定位到行标识
		boolean found = false;
		Iterator iter = preBody.iterator();
		while (iter.hasNext()) {
			TableRowStruct row = (TableRowStruct) iter.next();
			if (collpseRowId.equals(row.row_id)) {
				// 找到这个行标识，设置折叠
				row.leftHTML.delete(0, row.leftHTML.length());
				row.leftHTML.append(row.leftExpandHTML);
				body.add(row);
				found = true;
				rowSpan = row.row_span;
				count = 0;
			} else {
				if (curFunc.hasDimNotAsWhere) {
					// 有跨行设置
					if (count >= rowSpan && found) {
						// 定位到以后，且当前行超出跨行
						removeRowStarted = true;
					} else {
						removeRowStarted = false;
					}
				} else {
					// 没有跨行要求
					removeRowStarted = found;
				}
				if (removeRowStarted) {
					// 折叠开始后
					String tmpRowId = row.row_id;
					String curLevel = null;
					if (null != tmpRowId) {
						String[] tmpValues = tmpRowId.split("_");
						if (null != tmpValues && tmpValues.length >= 2)
							curLevel = tmpValues[1];
					}
					// 这里还有考虑行合并情况,找到后
					if (null != curLevel
							&& null != collapseLevel
							&& Integer.parseInt(collapseLevel) < Integer
									.parseInt(curLevel)) {
						// 当前行大于收缩行处的层次水平
						// 删除
						removeRowStarted = true;
					} else {
						removeRowStarted = false;
						found = false;
					}
				}
				if (!removeRowStarted) {
					body.add(row);
				} else {
					// 被去掉的行对象，收集这些rowID
					removedRowIds.add(row.row_id);
				}
			}

			count++;
		}
		// 付给当前功能对象保存
		curFunc.removedRowIds = removedRowIds;
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#collapseAll(java.util
	 * .List, java.util.List)
	 */
	public List collapseAll(List preBody, List expandBody)
			throws SubjectException {
		if (null == expandBody)
			throw new SubjectException("表格全收缩时展开后行对象列表为空");
		return expandBody;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#expand(com.asiabi.subject
	 * .domain.TableCurFunc, java.util.List, java.util.List)
	 */
	public List expand(TableCurFunc curFunc, List preBody, List expandBody)
			throws SubjectException {
		if (null == curFunc)
			throw new SubjectException("表格展开时功能对象为空");
		if (null == expandBody)
			throw new SubjectException("表格展开时展开后行对象列表为空");
		if (null == preBody)
			return expandBody;
		// 展开后的行列表
		List body = new ArrayList();
		// 展开行标识
		String expandRowId = curFunc.expandRwoId;
		curFunc.rowSpanDimValue = null;
		if (null == expandRowId)
			throw new SubjectException("表格展开时展开行的标识为空");
		boolean reachInsertRow = false;
		int rowSpan = 0;
		int count = 0;
		Iterator iter = preBody.iterator();
		while (iter.hasNext()) {
			TableRowStruct row = (TableRowStruct) iter.next();
			if (row.row_id.equals(expandRowId)) {
				// 找到展开点了
				// 加上本行，需要转换操作为收缩
				row.leftHTML.delete(0, row.leftHTML.length());
				row.leftHTML.append(row.leftCollapseHTML);
				body.add(row);
				// 这是要考虑行合并情况,这是找到了第一行，那需要知道合并
				// 了几行，然后后面行加上，保存行合并数
				// 这时需要考虑是否显示合计
				reachInsertRow = true;
				if (curFunc.hasDimNotAsWhere) {
					rowSpan = row.row_span;
				} else {
					body.addAll(expandBody);
				}
			} else {
				if (reachInsertRow) {
					count++;
				}
				if (count == rowSpan && reachInsertRow) {
					body.addAll(expandBody);
					reachInsertRow = false;
					count = 0;
				}
				body.add(row);
			}
		}
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#expandAll(java.util.List,
	 * java.util.List)
	 */
	public List expandAll(List preBody, List expandBody)
			throws SubjectException {
		if (null == expandBody)
			throw new SubjectException("表格全展开时展开后行对象列表为空");
		return expandBody;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#genTableCurFunc(com.asiabi
	 * .base.table.SubjectCommTabDef)
	 */
	public TableCurFunc genTableCurFunc(SubjectCommTabDef subTable)
			throws SubjectException {
		if (null == subTable)
			throw new SubjectException("生成表格功能对象时表格域对象为空");
		TableCurFunc curFunc = new TableCurFunc();
		curFunc = setFunc(subTable, curFunc);
		return curFunc;
	}

	/**
	 * 设置当前功能对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @return 设置后的功能对象
	 */
	private TableCurFunc setFunc(SubjectCommTabDef subTable,
			TableCurFunc curFunc) {
		TableCurFunc retFunc = curFunc;
		if (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand)) {
			retFunc.banExpanded = true;
		}
		boolean hasRatio = false;
		boolean hasLastRatio = false;
		boolean hasLoopRatio = false;
		boolean hasComRatio = false;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_comratio)) {
					hasComRatio = true;
				}
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
					hasRatio = true;
					hasLastRatio = true;
				}
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
					hasRatio = true;
					hasLoopRatio = true;
				}
			}
		}
		retFunc.hasRatio = hasRatio;
		retFunc.hasComRatio = hasComRatio;
		retFunc.hasLastRaito = hasLastRatio;
		retFunc.hasLoopRatio = hasLoopRatio;
		return retFunc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableCurFuncDAO#parseRequestToCurFunc
	 * (com.asiabi.subject.domain.TableCurFunc,
	 * com.asiabi.base.table.SubjectCommTabDef,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public TableCurFunc parseRequestToCurFunc(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException, AppException {
		if (null == curFunc)
			throw new SubjectException("分析用户请求中的状态到表列域对象时功能域对象为空");
		if (null == request)
			throw new SubjectException("分析用户请求中的状态到表列域对象时请求对象为空");
		TableCurFunc func = SubjectCurFuncUtil.parseRequest(curFunc, subTable,
				request);
		return setFunc(subTable, func);
	}
}
