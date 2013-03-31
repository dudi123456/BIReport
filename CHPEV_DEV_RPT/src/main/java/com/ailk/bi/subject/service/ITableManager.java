package com.ailk.bi.subject.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableHTMLStruct;

/**
 * @author xdou
 * 专题统一模版表格部分对外统一接口
 */
/**
 * @author xdou
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface ITableManager {
	/**
	 * 生成表格对象
	 * 
	 * @param tableId
	 *            表格标识
	 * @return 表格对象
	 * @throws SubjectException
	 */
	public abstract SubjectCommTabDef genTableDomain(String tableId,
			HttpServletRequest request) throws SubjectException;

	/**
	 * 由表格对象生成表格当前状态对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @return 状态对象
	 * @throws SubjectException
	 */
	public abstract TableCurFunc genCurFuncDomain(SubjectCommTabDef subTable)
			throws SubjectException;

	/**
	 * 对表格内容进行排序
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            状态对象
	 * @param content
	 *            排序前表格内容
	 * @return 排序后表格内容
	 * @throws SubjectException
	 */
	public abstract List sortTableContent(SubjectCommTabDef subTable,
			TableCurFunc curFunc, List content, HttpServletRequest request)
			throws SubjectException;

	/**
	 * 将用户请求分析到表格对象 支持具体列是否显示和顺序
	 * 
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 表格对象
	 * @throws SubjectException
	 */
	public abstract SubjectCommTabDef parseRequestToTableColStruct(
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException;

	/**
	 * 将用户请求分析到当前状态对象
	 * 
	 * @param curFunc
	 *            当前状态对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 状态对象
	 * @throws SubjectException
	 */
	public abstract TableCurFunc parseRequestToCurFunc(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException, AppException;

	/**
	 * 获取查询结果
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            状态对象
	 * @return 查询结果数组
	 * @throws SubjectException
	 */
	public abstract String[][] getTableContent(SubjectCommTabDef subTable,
			TableCurFunc curFunc) throws SubjectException;

	/**
	 * 生成表格行对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            状态对象
	 * @param svces
	 *            结果数组
	 * @return 行对象列表
	 * @throws SubjectException
	 */
	public abstract List genTableRowStructs(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[][] svces) throws SubjectException;

	/**
	 * 生成输出的HTML
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            状态对象
	 * @param rows
	 *            行对象列表
	 * @return
	 * @throws SubjectException
	 */
	public abstract TableHTMLStruct getTableHTML(SubjectCommTabDef subTable,
			TableCurFunc curFunc, List rows, String[][] svces)
			throws SubjectException;

	/**
	 * 根据当前状态生成新的行对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            状态对象
	 * @param preBody
	 *            以前行对象列表
	 * @param body
	 *            当前行对象列表
	 * @return 合并后的行对象列表
	 * @throws SubjectException
	 */
	public abstract List genNewFuncRows(SubjectCommTabDef subTable,
			TableCurFunc curFunc, List preBody, List body)
			throws SubjectException;
}
