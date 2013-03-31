package com.ailk.bi.subject.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;

/**
 * @author 表格行结构接口
 */
@SuppressWarnings({ "rawtypes" })
public interface ITableRowStructDAO {
	/**
	 * 组装表格行结构
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            二位表格数组
	 * @return 表格行结构列表
	 * @throws SubjectException
	 */
	public abstract List assemleTableRowStructs(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[][] svces) throws SubjectException;

	public abstract TableRowStruct genTableRowStruct(
			SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces,
			String[] sum, boolean isSumRow) throws SubjectException;

}
