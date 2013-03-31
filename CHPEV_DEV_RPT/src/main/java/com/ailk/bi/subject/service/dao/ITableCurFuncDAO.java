package com.ailk.bi.subject.service.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author 用户功能接口
 */
@SuppressWarnings({ "rawtypes" })
public interface ITableCurFuncDAO {
	/**
	 * 展开某一行
	 * 
	 * @param curFunc
	 *            当前功能对象
	 * @param preBody
	 *            展开前表体
	 * @param expandBody
	 *            展开表体
	 * @return 组合后的表体
	 * @throws SubjectException
	 */
	public abstract List expand(TableCurFunc curFunc, List preBody,
			List expandBody) throws SubjectException;

	/**
	 * 收缩某个展开行
	 * 
	 * @param curFunc
	 *            当前功能对象
	 * @param preBody
	 *            收缩前表体
	 * @return 收缩后表体
	 * @throws SubjectException
	 */
	public abstract List collapse(TableCurFunc curFunc, List preBody)
			throws SubjectException;

	/**
	 * 全部展开
	 * 
	 * @param preBody
	 *            展开前表体
	 * @param expandBody
	 *            展开后表体
	 * @return 展开后表体
	 * @throws SubjectException
	 */
	public abstract List expandAll(List preBody, List expandBody)
			throws SubjectException;

	/**
	 * 全部收缩
	 * 
	 * @param preBody
	 *            收缩前表体
	 * @param expandBody
	 *            全部收缩后表体
	 * @return 全部收缩后表体
	 * @throws SubjectException
	 */
	public abstract List collapseAll(List preBody, List expandBody)
			throws SubjectException;

	/**
	 * 生成表格当前对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @return 表格状态对象
	 * @throws SubjectException
	 */
	public abstract TableCurFunc genTableCurFunc(SubjectCommTabDef subTable)
			throws SubjectException;

	/**
	 * 分析用户请求到当前功能对象
	 * 
	 * @param curFunc
	 *            当前功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            用户请求对象
	 * @return 分析后的功能对象
	 * @throws SubjectException
	 */
	public abstract TableCurFunc parseRequestToCurFunc(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException, AppException;
}
