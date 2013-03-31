package com.ailk.bi.subject.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableHTMLStruct;
import com.ailk.bi.subject.service.ITableManager;
import com.ailk.bi.subject.service.dao.ITableBodyHTMLDAO;
import com.ailk.bi.subject.service.dao.ITableContentDAO;
import com.ailk.bi.subject.service.dao.ITableCurFuncDAO;
import com.ailk.bi.subject.service.dao.ITableDomainDAO;
import com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO;
import com.ailk.bi.subject.service.dao.ITableRowStructDAO;
import com.ailk.bi.subject.service.dao.impl.TableBodyHTMLDAO;
import com.ailk.bi.subject.service.dao.impl.TableContentDAO;
import com.ailk.bi.subject.service.dao.impl.TableCurFuncDAO;
import com.ailk.bi.subject.service.dao.impl.TableDomainDAO;
import com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO;
import com.ailk.bi.subject.service.dao.impl.TableRowStructDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectSortUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableManager implements ITableManager {
	/**
	 * 查询内容DAO
	 */
	private ITableContentDAO tableContentDao = new TableContentDAO();

	/**
	 * 表体DAO
	 */
	private ITableBodyHTMLDAO tableBodyHTMLDao = new TableBodyHTMLDAO();

	/**
	 * 域对象DAO
	 */
	private ITableDomainDAO tableDomainDao = new TableDomainDAO();

	/**
	 * 当前状态对象DAO
	 */
	private ITableCurFuncDAO tableCurFuncDao = new TableCurFuncDAO();

	/**
	 * 表头对象DAO
	 */
	private ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();

	/**
	 * 表格行对象DAO
	 */
	private ITableRowStructDAO tableRowStructDao = new TableRowStructDAO();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#genTableRowStructs(com.asiabi
	 * .base.table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc,
	 * java.lang.String[][])
	 */
	public List genTableRowStructs(SubjectCommTabDef subTable, TableCurFunc curFunc, String[][] svces)
			throws SubjectException {
		// 此处生成一遍表头为了表格对齐
		if (null == svces)
			return null;
		tableHeadDao.getTableHead(subTable, curFunc, svces);
		return tableRowStructDao.assemleTableRowStructs(subTable, curFunc, svces);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#getTableContent(com.asiabi.base
	 * .table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc)
	 */
	public String[][] getTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		if (SubjectConst.TABLE_FUNC_ROW_COLLAPSE.equalsIgnoreCase(curFunc.curUserFunc))
			return null;
		else
			return tableContentDao.getTableContent(subTable, curFunc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#getTableHTML(com.asiabi.base
	 * .table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc,
	 * java.util.List)
	 */
	public TableHTMLStruct getTableHTML(SubjectCommTabDef subTable, TableCurFunc curFunc, List rows, String[][] svces)
			throws SubjectException {
		long startTime = System.currentTimeMillis();
		TableHTMLStruct tableHTML = new TableHTMLStruct();
		List body = new ArrayList();
		List exportBody = new ArrayList();
		// 判断是否有表格内容
		if (null == rows || 0 >= rows.size()) {
			body.add("<table width='100%' height=\"100%\" border='0' cellpadding='0' cellspacing='0' " + ">\n");
			body.add("<tr><td align=\"center\" height=\"100%\"  class=\"searchnobg\" valign=\"middle\">");
			if (curFunc.withBar)
				body.add("<div class=\"searchno\">没有查询到相关信息</div>");
			else
				body.add("<div class=\"searchno_2\">没有查询到相关信息</div>");
			body.add("</td></tr>");
			body.add("</table>\n");
			exportBody.addAll(body);
		} else {
			// 这里要判断一下如果没有扩展，且带有分页情况,还不能这里
			// 这里只加上导航条,根据基础类编写，还不能在这里加
			// 加上对齐的最外层表格
			body.add("<table width='100%' border='0' cellpadding='0' cellspacing='0' " + ">\n");
			exportBody.add("<table width='100%' border='1' cellpadding='0' cellspacing='0' " + ">\n");
			body.add("<tr><td class=\"side-left\">");
			body.add("<table width='100%' border='0' height=\"" + curFunc.tableHeight + "\" "
					+ "cellpadding='0' cellspacing='0' " + "id=\"iTable_TableContainer\" >\n");
			// 怎么获取导出的部分
			body.addAll(tableHeadDao.getTableHead(subTable, curFunc, svces));
			body.addAll(tableBodyHTMLDao.getTableBodyHTML(subTable, curFunc, rows));
			exportBody.add(tableHeadDao.getTableExportHead().toString());
			exportBody.addAll(tableBodyHTMLDao.getTableExportBody());
			body.add("</table>\n</td></tr></table>");
			exportBody.add("</table>");
		}
		String[] html = (String[]) body.toArray(new String[body.size()]);
		String[] export = (String[]) exportBody.toArray(new String[exportBody.size()]);
		tableHTML.html = html;
		tableHTML.export = export;
		System.out.println("专题通用表格表格组装用时：" + (System.currentTimeMillis() - startTime) + "ms");
		return tableHTML;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#parseRequestToCurFunc(com.asiabi
	 * .subject.domain.TableCurFunc, com.asiabi.base.table.SubjectCommTabDef,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public TableCurFunc parseRequestToCurFunc(TableCurFunc curFunc, SubjectCommTabDef subTable,
			HttpServletRequest request) throws SubjectException, AppException {
		if (null == curFunc)
			throw new SubjectException("分析用户请求中的状态到表列域对象时功能域对象为空");
		if (null == request)
			throw new SubjectException("分析用户请求中的状态到表列域对象时请求对象为空");
		return tableCurFuncDao.parseRequestToCurFunc(curFunc, subTable, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#parseRequestToTableColStruct
	 * (com.asiabi.base.table.SubjectCommTabDef,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public SubjectCommTabDef parseRequestToTableColStruct(SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException {
		if (null == subTable)
			throw new SubjectException("分析用户请求中的状态到表列域对象时表格域对象为空");
		if (null == request)
			throw new SubjectException("分析用户请求中的状态到表列域对象时请求对象为空");
		return tableDomainDao.parseRequestToTableColStruct(subTable, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#sortTableContent(com.asiabi.
	 * base.table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc,
	 * java.util.List)
	 */
	public List sortTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc, List content,
			HttpServletRequest request) throws SubjectException {
		if (null == subTable)
			throw new SubjectException("对表格内容进行排序时表格域对象为空");
		if (null == curFunc)
			throw new SubjectException("对表格内容进行排序时当前状态域对象为空");
		if (null == content)
			throw new SubjectException("对表格内容进行排序时表格内容为空");
		if (null == request)
			throw new SubjectException("对表格内容进行排序时请求对象为空");
		return SubjectSortUtil.sortTableContent(subTable, curFunc, content, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#genCurFuncDomain(com.asiabi.
	 * base.table.SubjectCommTabDef)
	 */
	public TableCurFunc genCurFuncDomain(SubjectCommTabDef subTable) throws SubjectException {
		if (null == subTable)
			throw new SubjectException("生成表格功能对象时表格域对象为空");
		return tableCurFuncDao.genTableCurFunc(subTable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#genTableDomain(java.lang.String)
	 */
	public SubjectCommTabDef genTableDomain(String tableId, HttpServletRequest request) throws SubjectException {
		if (null == tableId || "" == tableId)
			throw new SubjectException("生成表格域对象时表格的标识为空");
		return tableDomainDao.getSubjectTable(tableId, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.ITableManager#genNewFuncRows(com.asiabi.base
	 * .table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc,
	 * java.util.List, java.util.List)
	 */
	public List genNewFuncRows(SubjectCommTabDef subTable, TableCurFunc curFunc, List preBody, List expandBody)
			throws SubjectException {
		if (null == subTable)
			throw new SubjectException("根据当前状态组合列表是表格定义对象为空");
		if (null == curFunc)
			throw new SubjectException("根据当前状态组合列表是表格定义对象为空");
		if (null == preBody)
			return expandBody;
		if (SubjectConst.TABLE_FUNC_INIT.equalsIgnoreCase(curFunc.curUserFunc)) {
			// 有可能是全部收缩
			curFunc.expandedContent.clear();
			curFunc.expandedRowIDs.clear();
			curFunc.expandedContent.addLast(expandBody);
			return expandBody;
		}
		if (SubjectConst.TABLE_FUNC_ROW_EXPAND.equalsIgnoreCase(curFunc.curUserFunc)) {
			curFunc.expandedContent.addLast(expandBody);
			curFunc.expandedRowIDs.addLast(curFunc.expandRwoId);
			return tableCurFuncDao.expand(curFunc, preBody, expandBody);
		}
		if (SubjectConst.TABLE_FUNC_ROW_COLLAPSE.equalsIgnoreCase(curFunc.curUserFunc)) {
			// 这里如果是最上层呢，或者是中间某层，得连续去掉几个
			// 这里还是有问题，如果别的层次，不是本层的呢，也是该层后面展开的，
			// 也会被去掉，不行,需要去除他后面，
			String collapseRowId = curFunc.collpaseRowId;
			int index = curFunc.expandedRowIDs.indexOf(collapseRowId);
			curFunc.expandedRowIDs.remove(index);
			curFunc.expandedContent.remove(index);
			List body = tableCurFuncDao.collapse(curFunc, preBody);
			// 看看expandedRowIDs在body中有没有，没有去掉,
			List removedRowIds = curFunc.removedRowIds;
			// 循环扩展列表，应该数量少
			LinkedList expandedRowIDs = new LinkedList();
			LinkedList expandedContent = new LinkedList();
			List indexs = new ArrayList();
			int row_index = 0;
			Iterator iter = curFunc.expandedRowIDs.iterator();
			while (iter.hasNext()) {
				String rowId = (String) iter.next();
				if (!removedRowIds.contains(rowId)) {
					indexs.add("" + row_index);
					expandedRowIDs.add(rowId);
				}
				row_index++;
			}
			iter = indexs.iterator();
			while (iter.hasNext()) {
				String rowIndex = (String) iter.next();
				// 分析为整形
				expandedContent.add(curFunc.expandedContent.get(Integer.parseInt(rowIndex)));
			}
			// 重新复制
			curFunc.expandedRowIDs = expandedRowIDs;
			curFunc.expandedContent = expandedContent;
			return body;
		}
		if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equalsIgnoreCase(curFunc.curUserFunc)) {
			curFunc.expandedContent.clear();
			curFunc.expandedRowIDs.clear();
			return tableCurFuncDao.expandAll(preBody, expandBody);
		}
		return null;
	}
}
