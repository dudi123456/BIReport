package com.ailk.bi.subject.service.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableSelectQuery;
import com.ailk.bi.subject.service.dao.ITableContentDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectDataTableUtil;
import com.ailk.bi.subject.util.SubjectDimUtil;
import com.ailk.bi.subject.util.SubjectMsuUtil;
import com.ailk.bi.subject.util.SubjectSQLUtil;
import com.ailk.bi.subject.util.SubjectStringUtil;

/**
 * @author xdou 表格数据查询类
 */
@SuppressWarnings({ "rawtypes" })
public class TableContentDAO implements ITableContentDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableContentDAO#getTableContent(com.asiabi
	 * .base.table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc)
	 */
	public String[][] getTableContent(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		if (null == curFunc)
			throw new SubjectException("获取专题表格的数据是当前功能对象为空");
		if (null == subTable)
			throw new SubjectException("获取专题表格的数据是表格定义对象为空");
		String[][] svces = null;
		try {
			// 生成查询语句
			long startTime = System.currentTimeMillis();
			String SQL = null;
			// 对于分页，需要判断是否第一次访问，
			if (SubjectConst.YES.equalsIgnoreCase(subTable.has_paging)) {
				SQL = subTable.querySQL;
				if (StringUtils.isBlank(SQL)) {
					SQL = genTableContentSQL(subTable, curFunc);
				}
				if (subTable.totalRows < 0) {
					// 需要获取记录总数
					String totalSQL = SubjectSQLUtil.genTotalSQL(SQL,
							SubjectConst.YES.equalsIgnoreCase(subTable.no_groupby) ? false : true,
							SubjectConst.YES.equalsIgnoreCase(subTable.no_orderby) ? false : true);
					System.out.println("专题通用表格语句拼装分页总数SQL：" + totalSQL);
					// 执行
					svces = WebDBUtil.execQryArray(totalSQL, "");
					// 设置
					try {
						if (null != svces) {
							int totalRows = Integer.parseInt(svces[0][0]);
							subTable.totalRows = totalRows;
						}
					} catch (NumberFormatException e) {

					}
				}
				subTable.querySQL = SQL;
				// 整成按每页数和行号拼写SQL
				int pageRows = SubjectConst.ROWS_PER_PAGE;
				pageRows = Integer.parseInt(subTable.page_rows);
				SQL = SubjectSQLUtil.genPageingSQL(SQL, curFunc.pageNum, pageRows);
			} else {
				SQL = genTableContentSQL(subTable, curFunc);
			}
			System.out.println("专题通用表格语句拼装：" + SQL);
			startTime = System.currentTimeMillis();
			svces = WebDBUtil.execQryArray(SQL, "");
			System.out.println("专题通用表格语句拼装查询用时：" + (System.currentTimeMillis() - startTime) + "ms");
		} catch (AppException ae) {
		}
		return svces;

	}

	/**
	 * 生成表格的查询语句
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @return 查询语句
	 * @throws SubjectException
	 */
	private String genTableContentSQL(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		String contentSQL = null;
		// 如果维度值作为列显示
		if (SubjectConst.YES.equalsIgnoreCase(subTable.dim_ascol)) {
			// 先按照条件获取维度的所有值，然后按CASE WHEN 扩展指标
			contentSQL = genDimAsColSQL(subTable, curFunc);

		} else {
			// 普通的维度不作为列展示的表格
			contentSQL = genGeneralSQL(subTable, curFunc);

		}
		return contentSQL;
	}

	/**
	 * 生成维度作为列的查询语句
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @return 查询语句
	 * @throws SubjectException
	 */
	private String genDimAsColSQL(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		String contentSQL = null;
		// 需要先找到要作为横向列的维度
		List tabCols = subTable.tableCols;
		if (null == tabCols || 0 >= tabCols.size())
			throw new SubjectException("生成非扩展表格的查询语句时，列对象定义为空");
		// 找到维度作为列的维度
		String virTableName = SubjectConst.DATA_TABLE_VIR_NAME;
		SubjectCommTabCol colDim = null;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)
					&& SubjectConst.YES.equalsIgnoreCase(tabCol.dim_ascol)) {
				colDim = tabCol;
				curFunc.hasDimCol = true;
				break;
			}
		}
		if (null != colDim) {
			// 1。取得当前条件下该维度的所有值
			List dimStructs = SubjectDimUtil.getDimValues(subTable, colDim, curFunc, virTableName);
			// 还需要保存下来当前结构
			curFunc.tabColDimStructs = dimStructs;
			// 2。扩展表格定义的指标
			List list = SubjectDataTableUtil.genNewTableCols(subTable, dimStructs);
			subTable.tableCols = list;
			// 3。构造新的语句
			contentSQL = genGeneralSQL(subTable, curFunc);
		}
		return contentSQL;
	}

	/**
	 * 生成普通查询语句
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @return 查询语句
	 * @throws SubjectException
	 */
	private String genGeneralSQL(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		String contentSQL = null;
		// 普通的表格
		// 还要判断是否展开
		if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)) {
			// 有扩展列
			contentSQL = genExpandSQL(subTable, curFunc);
		} else {
			// 没有扩展列，只需简单的拼装SQL就可以了，
			contentSQL = genNoExpandSQL(subTable, curFunc);
		}
		return contentSQL;
	}

	/**
	 * 生成没有折叠展开模式的
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能状态对象
	 * @return 非展开扩展列
	 * @throws SubjectException
	 */
	private String genNoExpandSQL(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		String contentSQL = null;
		List tabCols = subTable.tableCols;
		if (null == tabCols || 0 >= tabCols.size())
			throw new SubjectException("生成非扩展表格的查询语句时，列对象定义为空");

		StringBuffer select = new StringBuffer();
		StringBuffer from = new StringBuffer();
		StringBuffer where = new StringBuffer();
		StringBuffer groupby = new StringBuffer();
		StringBuffer orderby = new StringBuffer();
		StringBuffer having = new StringBuffer();

		where.append(" WHERE ");
		String virTableName = SubjectConst.DATA_TABLE_VIR_NAME;
		String dataWhere = SubjectStringUtil.processDataTableWHERE(curFunc, virTableName);
		// 先将数据表关联上
		from.append(SubjectDataTableUtil.genDataTableFROM(subTable, virTableName, dataWhere));
		// 数据表的定义的查询条件
		where.append(SubjectDataTableUtil.genDataTableWHERE(subTable, virTableName));
		// 由其他程序传进来的条件
		where.append(dataWhere);
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			TableSelectQuery parts = null;
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				// 维度定义
				parts = SubjectDimUtil.genDimNoExpandSelectParts(subTable, tabCol, curFunc, virTableName);
			} else {
				// 指标定义
				parts = SubjectMsuUtil.genMsuNoExpandSelectParts(tabCol, curFunc, virTableName);

				// parts = SubjectMsuUtil.genMsuNoExpandSelectParts(tabCol,
				// curFunc, "");
			}
			// System.out.println("===parts.from=============="+parts.from);
			select.append(parts.select);
			from.append(parts.from);
			where.append(parts.where);
			groupby.append(parts.groupby);
			// 指标优先
			if (SubjectConst.YES.equalsIgnoreCase(subTable.has_paging)
					&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure) && parts.orderby.length() > 0) {
				orderby.delete(0, orderby.length());
				orderby.append(parts.orderby);
			} else {
				orderby.append(parts.orderby);
			}
			having.append(parts.having);
		}
		// 开始处理
		// 开始除去多余的,
		if (select.lastIndexOf(",") >= 0) {
			select.deleteCharAt(select.lastIndexOf(","));
		}
		if (groupby.lastIndexOf(",") >= 0) {
			groupby.deleteCharAt(groupby.lastIndexOf(","));
		}
		if (orderby.lastIndexOf(",") >= 0) {
			orderby.deleteCharAt(orderby.lastIndexOf(","));
		}
		// 如果有同比或者环比
		if (curFunc.hasRatio) {
			// 生成关联语句
			String joinFrom = SubjectDataTableUtil.genRatioSubFrom(subTable, where.toString(), groupby.toString(),
					virTableName);
			from.delete(0, from.length());
			from.append(joinFrom);

		}
		select.append(from);
		if (!curFunc.hasRatio) {
			select.append(where);
		}
		if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) && groupby.length() > 0) {
			String groupBy = groupby.toString();
			groupBy = SubjectStringUtil.clearDupFld(groupBy);
			select.append(" GROUP BY ROLLUP(").append(groupBy).append(")");
			having.append(genDimHaving(groupBy));
		}
		if (null != curFunc.dataHaving && !"".equals(curFunc.dataHaving)) {
			String tmpStr = curFunc.dataHaving;
			tmpStr = SubjectStringUtil.replaceVirTabName(tmpStr, virTableName);
			having.append(tmpStr);
		}
		if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) && having.length() > 0) {
			select.append(" HAVING ").append(having);
		}
		if (SubjectConst.NO.equalsIgnoreCase(subTable.no_orderby) && orderby.length() > 0) {
			select.append(" ORDER BY ").append(orderby);
		}
		contentSQL = "SELECT " + select.toString();
		return contentSQL;
	}

	/**
	 * 生成带折叠展开的查询语句
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @return 戴展开功能的查询语句
	 * @throws SubjectException
	 */
	private String genExpandSQL(SubjectCommTabDef subTable, TableCurFunc curFunc) throws SubjectException {
		String contentSQL = null;
		List tabCols = subTable.tableCols;
		if (null == tabCols || 0 >= tabCols.size())
			throw new SubjectException("生成非扩展表格的查询语句时，列对象定义为空");

		StringBuffer select = new StringBuffer();
		StringBuffer from = new StringBuffer();
		StringBuffer where = new StringBuffer();
		StringBuffer groupby = new StringBuffer();
		StringBuffer orderby = new StringBuffer();
		StringBuffer having = new StringBuffer();

		where.append(" WHERE ");
		String virTableName = SubjectConst.DATA_TABLE_VIR_NAME;// A
		String data_where = SubjectStringUtil.processDataTableWHERE(curFunc, virTableName);
		// 先将数据表关联上
		from.append(SubjectDataTableUtil.genDataTableFROM(subTable, virTableName, data_where));

		// 数据表的定义的查询条件
		where.append(SubjectDataTableUtil.genDataTableWHERE(subTable, virTableName));

		// 由其他程序传进来的条件
		where.append(data_where);

		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			TableSelectQuery parts = null;
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				// 维度定义
				// 判断是否有维度不作为条件
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_expand_col)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.dim_aswhere)) {
					curFunc.hasDimNotAsWhere = true;
				}
				parts = SubjectDimUtil.genDimExpandSelectParts(subTable, tabCol, curFunc, virTableName);
			} else {
				// 指标定义
				parts = SubjectMsuUtil.genMsuNoExpandSelectParts(tabCol, curFunc, virTableName);
			}
			select.append(parts.select);
			from.append(parts.from);
			where.append(parts.where);
			groupby.append(parts.groupby);
			orderby.append(parts.orderby);
			having.append(parts.having);

		}
		// 开始处理
		// 开始除去多余的,
		if (select.lastIndexOf(",") >= 0) {
			select.deleteCharAt(select.lastIndexOf(","));
		}
		if (groupby.lastIndexOf(",") >= 0) {
			groupby.deleteCharAt(groupby.lastIndexOf(","));
		}
		if (orderby.lastIndexOf(",") >= 0) {
			orderby.deleteCharAt(orderby.lastIndexOf(","));
		}

		if (curFunc.hasRatio) {
			String joinFrom = SubjectDataTableUtil.genRatioSubFrom(subTable, where.toString(), groupby.toString(),
					virTableName);
			from.delete(0, from.length());
			from.append(joinFrom);

		}

		select.append(from);

		if (!curFunc.hasRatio) {
			select.append(where);
		}
		if (groupby.length() > 0) {
			String groupBy = groupby.toString();
			groupBy = SubjectStringUtil.clearDupFld(groupBy);
			select.append(" GROUP BY ROLLUP(").append(groupBy).append(")");
			having.append(genDimHaving(groupBy));
		}
		if (null != curFunc.dataHaving && !"".equals(curFunc.dataHaving)) {
			String tmpStr = curFunc.dataHaving;
			tmpStr = SubjectStringUtil.replaceVirTabName(tmpStr, virTableName);
			having.append(tmpStr);
		}
		if (having.length() > 0) {
			select.append(" HAVING ").append(having);
		}
		if (orderby.length() > 0) {
			select.append(" ORDER BY ").append(orderby);
		}
		contentSQL = "SELECT " + select.toString();
		return contentSQL;
	}

	/**
	 * 由于使用了ROLLUP函数，生成去掉各小计过滤条件
	 * 
	 * @param groupby
	 *            分组条件
	 * @return 过滤部分
	 */
	private String genDimHaving(String groupby) {
		StringBuffer having = new StringBuffer();
		if (null != groupby) {
			String[] dims = groupby.split(",");
			if (dims.length > 0) {
				having.append("(");
				StringBuffer sbNotNull = new StringBuffer();
				StringBuffer sbNull = new StringBuffer();
				for (int i = 0; i < dims.length; i++) {
					sbNotNull.append(dims[i]).append(" IS NOT NULL AND ");
					sbNull.append(dims[i]).append(" IS NULL AND ");
				}
				if (sbNotNull.lastIndexOf(" AND ") > 0)
					having.append(RptOlapStringUtil.removeLastSubStr(sbNotNull.toString(), " AND "));
				having.append(")");
				if (sbNull.length() > 0) {
					having.append(" OR (");
					if (sbNull.lastIndexOf(" AND ") > 0)
						having.append(RptOlapStringUtil.removeLastSubStr(sbNull.toString(), " AND "));
					having.append(")");
				}
			}
		}
		return having.toString();
	}
}
