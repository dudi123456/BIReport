package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IReportContentDao {

	/**
	 * 根据当前状态设置当前功能
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能
	 * @return 设置后的功能对象
	 * @throws ReportOlapException
	 */
	public abstract RptOlapFuncStruct setOlapFunc(List tableCols,
			RptOlapFuncStruct olapFun) throws ReportOlapException;

	/**
	 * 生成分析型报表的数据库查询内容
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param report
	 *            报表对象
	 * @param ds
	 *            日期对象
	 * @param olapFun
	 *            当前功能对象
	 * @return 表体内容
	 * @throws ReportOlapException
	 */
	public abstract String[][] getContent(List tableCols,
			PubInfoResourceTable report, RptOlapDateStruct ds,
			RptOlapFuncStruct olapFun, UserCtlRegionStruct userCtl,
			String svckndRight) throws ReportOlapException;

	/**
	 * 获取加减号展开时的内容,对于有层次的维，应该只选择该维，当选择其他维时，按顺序展开,如何选择
	 * 展现方式按钮,选择折叠、展开时要显示维度和指标,对于有层次维度，选择时其他维度不能选择，对于其他维度可以多选
	 * 一个是单选框，一个是多选框，当选择多选框时，所有多选框变灰，当选择单选框时，所有多选框变灰
	 * 多选框可以调整顺序,第一次时展开第一层，采用AJAX更新，还有对齐表头问题，收缩时使用javascript实现
	 * 还要考虑同时展开多个、折叠情况、排序,在List中要指出哪些伙单个维度
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param report
	 *            报表对象
	 * @param ds
	 *            日期对象
	 * @param olapFun
	 *            当前功能
	 * @param level
	 *            层次
	 * @return 展开后的内容
	 * @throws ReportOlapException
	 */
	public abstract String[][] getExpandContent(List tableCols,
			PubInfoResourceTable report, RptOlapDateStruct ds,
			RptOlapFuncStruct olapFun, UserCtlRegionStruct userCtl,
			String svckndRight, String level, boolean singleDim)
			throws ReportOlapException;
}
