package com.ailk.bi.subject.service.dao;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author 获取表格查询数据库内容接口
 */
public interface ITableContentDAO {
	/**
	 * 获取表格数据库内容
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前状态
	 * @return 查询结果二位数组
	 * @throws SubjectException
	 */
	public abstract String[][] getTableContent(SubjectCommTabDef subTable,
			TableCurFunc curFunc) throws SubjectException;
}
