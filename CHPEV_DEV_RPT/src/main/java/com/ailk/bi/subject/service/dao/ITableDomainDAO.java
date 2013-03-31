package com.ailk.bi.subject.service.dao;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;

/**
 * @author 表格域对象接口
 */
public interface ITableDomainDAO {
	/**
	 * 取得表格域对象
	 * 
	 * @param tableId
	 *            表格标识
	 * @return 表格对象
	 * @throws SubjectException
	 */
	public abstract SubjectCommTabDef getSubjectTable(String tableId,
			HttpServletRequest request) throws SubjectException;

	/**
	 * 分析用户请求到表格对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            用户请求对象
	 * @return 表格对象
	 * @throws SubjectException
	 */
	public abstract SubjectCommTabDef parseRequestToTableColStruct(
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException;
}
