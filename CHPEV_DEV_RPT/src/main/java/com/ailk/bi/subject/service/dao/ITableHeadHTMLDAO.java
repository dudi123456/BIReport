package com.ailk.bi.subject.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author 表格表头接口
 */
@SuppressWarnings({ "rawtypes" })
public interface ITableHeadHTMLDAO {
	/**
	 * 获取表格表头
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @return 表头列表
	 * @throws SubjectException
	 */
	public abstract List getTableHead(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[][] svces) throws SubjectException;

	/**
	 * 获得导出表头
	 * 
	 * @return
	 * @throws SubjectException
	 */
	public abstract StringBuffer getTableExportHead() throws SubjectException;
}
