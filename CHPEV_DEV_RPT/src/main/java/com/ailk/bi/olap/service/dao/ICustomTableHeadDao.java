package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

/**
 * 定制表头仅适用于用户没有定制维度和指标的情况下， 如果用户进行了定制，无法知道具体对应和数目
 * 
 * @author DXF
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface ICustomTableHeadDao {
	/**
	 * 生成自定义表头
	 * 
	 * @param report
	 *            报表对象
	 * @param tableCols
	 *            表格列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param fixedHead
	 *            是否固定表头
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            单元格样式
	 * @return 表头的HTML列表
	 * @throws ReportOlapException
	 */
	public abstract List getCustomHead(PubInfoResourceTable report,
			List tableCols, RptOlapFuncStruct olapFun, boolean fixedHead,
			String trStyle, String tdStyle) throws ReportOlapException;

	/**
	 * 获取自定义表头的导出部分
	 * 
	 * @return
	 */
	public abstract List getExportHead();
}
