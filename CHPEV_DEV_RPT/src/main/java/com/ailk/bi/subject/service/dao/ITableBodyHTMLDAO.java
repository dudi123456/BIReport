package com.ailk.bi.subject.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author 获取表格表体接口
 */
@SuppressWarnings({ "rawtypes" })
public interface ITableBodyHTMLDAO {
	/**
	 * 获取表格表体HTML
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前状态对象
	 * @param rows
	 *            表格行对象列表
	 * @return 表体列表
	 * @throws SubjectException
	 */
	public abstract List getTableBodyHTML(SubjectCommTabDef subTable,
			TableCurFunc curFunc, List rows) throws SubjectException;

	public abstract List getTableExportBody() throws SubjectException;
}
