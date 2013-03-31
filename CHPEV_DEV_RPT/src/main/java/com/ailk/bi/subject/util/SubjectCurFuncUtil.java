package com.ailk.bi.subject.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.SubjectQryStruct;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author XDOU 表格功能对象工具类
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class SubjectCurFuncUtil {

	/**
	 * 将请求对象分析到表格功能对象
	 * 
	 * @param curFunc
	 *            表格功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的功能对象
	 */
	public static TableCurFunc parseRequest(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request)
			throws AppException {
		PubInfoConditionTable[] conditions = CommConditionUtil.genCondition(
				subTable.table_id, SubjectConst.PUB_INFO_RES_TABLE_TYPE);
		TableCurFunc curFunction = curFunc;
		// 分析基本请求
		curFunction = parseRequestFunc(curFunc, subTable, request);
		// 分析过滤请求
		curFunction = parseRequestFilter(curFunction, subTable, request);
		// 分析指标请求
		curFunction = parseRequestHaving(curFunction, subTable, request,
				conditions);
		// 分析权限请求
		curFunction = parseRequestRight(curFunction, subTable, request);
		// 分析样式请求
		curFunction = parseRequestStyle(curFunction, subTable, request);
		// 分析查询条件请求
		curFunction = parseRequestWhere(curFunction, subTable, request,
				conditions);
		conditions = null;
		return curFunction;
	}

	/**
	 * 分析请求中的一般请求
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的功能对象
	 */
	private static TableCurFunc parseRequestFunc(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request) {
		TableCurFunc curFunction = curFunc;
		// 此时先不考虑列显示顺序，和是否显示的变化
		// 先看是否全部展开
		String cur_func = request.getParameter(SubjectConst.REQ_TABLE_FUNC);
		if (null == cur_func || "".equals(cur_func)) {
			curFunction.curUserFunc = SubjectConst.TABLE_FUNC_INIT;
		} else {
			curFunction.curUserFunc = cur_func;
		}
		if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunction.curUserFunc)) {
			curFunction.banExpanded = true;
		} else {
			curFunction.banExpanded = false;
		}
		// 看看展开的行 ID
		String expandRowId = request
				.getParameter(SubjectConst.REQ_DIM_EXPAND_ROWID);
		if (null != expandRowId && !"".equals(expandRowId)) {
			curFunction.expandRwoId = expandRowId;
			curFunction.collpaseRowId = null;
		}
		// 看看收缩行
		String collapseRowId = request
				.getParameter(SubjectConst.REQ_DIM_COLLAPSE_ROWID);
		if (null != collapseRowId && !"".equals(collapseRowId)) {
			curFunction.collpaseRowId = collapseRowId;
			curFunction.expandRwoId = null;
		}
		// 看看排序
		String sortIndex = request.getParameter(SubjectConst.REQ_SORT_INDEX);
		if (null != sortIndex && !"".equals(sortIndex)) {
			curFunction.sortedColum = Integer.parseInt(sortIndex);
		}
		String sortOrder = request.getParameter(SubjectConst.REQ_SORT_ORDER);
		if (null != sortOrder && !"".equals(sortOrder)) {
			curFunction.sortOrder = sortOrder;
		}
		// 此时要根据列定义进行一下设置
		return curFunction;
	}

	/**
	 * 分析请求中的样式请求
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的功能对象
	 */
	private static TableCurFunc parseRequestStyle(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request) {
		TableCurFunc curFunction = curFunc;
		// 可以设置表格的各种样式
		String stylePara = request
				.getParameter(SubjectConst.REQ_STYLE_EVEN_TR_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabEvenTRClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_ODD_TR_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabOddTRClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_HEAD_TR_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabHeadTRClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_EVEN_TD_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabTDEvenClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_ODD_TD_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabTDOddClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_HEAD_TD_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tabHeadTDClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_TR_REST);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.trRest = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_STYLE_HREF_CLASS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.hrefLinkClass = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_IMG_PATH);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.imagePath = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_COLLAPSE_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.drillCollapseGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_EXPAND_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.drillExpandGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_RATIO_DOWN_GREEN_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.ratioDownGreenGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_RATIO_DOWN_RED_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.ratioDownRedGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_RATIO_RISE_GREEN_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.ratioRiseGreenGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_RATIO_RISE_RED_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.ratioRiseRedGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_RATIO_ZERO_IMG);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.ratioZeroGif = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_TABLE_HEIGHT);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.tableHeight = stylePara;
		return curFunction;
	}

	/**
	 * 分析请求中的权限对象
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后功能对象
	 */
	private static TableCurFunc parseRequestRight(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request) {
		TableCurFunc curFunction = curFunc;
		// 封装在where中了
		return curFunction;
	}

	/**
	 * 分析请求对象中的查询条件
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的请求对象
	 */
	private static TableCurFunc parseRequestWhere(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request,
			PubInfoConditionTable[] conditions) throws AppException {
		TableCurFunc curFunction = curFunc;
		HttpSession session = request.getSession();
		Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (null != tmpObj) {
			// SubjectQryStruct qry = (SubjectQryStruct) tmpObj;
			ReportQryStruct qry = (ReportQryStruct) tmpObj;
			String where = CommConditionUtil.getPubWhere(conditions, request,
					qry);
			// 这里如果有多产品或者单产品定制,如果到别的模块岂不也加上了
			// 还是配置去吧
			// if (null != qry.locked_products_with_a
			// && !"".equals(qry.locked_products_with_a))
			// where += qry.locked_products_with_a;
			if (null != where)
				curFunction.dataWhere = where;
			// 这里还要判断是否要扔掉以前的扩展
			if (SubjectConst.YES.equalsIgnoreCase(subTable.throw_old)) {
				where = where.toUpperCase();
				Iterator iter = subTable.tableCols.iterator();
				while (iter.hasNext()) {
					SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
					if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)
							&& SubjectConst.YES
									.equalsIgnoreCase(tabCol.is_expand_col)) {

						String codeFld = tabCol.code_field;
						codeFld = SubjectStringUtil.clearVirTabName(codeFld);
						codeFld = codeFld.toUpperCase();
						if (where.indexOf(codeFld) >= 0) {
							curFunction.expandLevel = SubjectConst.ZERO;
							curFunction.throwOldExpandContent = true;
							tabCol.level = curFunction.expandLevel;
							String nextLvl = SubjectDimUtil
									.getExpandNextLevel(tabCol);
							tabCol.level = nextLvl;
							curFunction.expandLevel = nextLvl;
						}
						List levels = tabCol.levels;
						if (null != levels) {
							Iterator levIter = levels.iterator();
							while (levIter.hasNext()) {
								SubjectCommDimHierarchy lev = (SubjectCommDimHierarchy) levIter
										.next();
								// 怎么判断呢
								codeFld = lev.src_idfld;
								codeFld = SubjectStringUtil
										.clearVirTabName(codeFld);
								codeFld = codeFld.toUpperCase();
								if (where.indexOf(codeFld) >= 0) {
									curFunction.expandLevel = lev.lev_id;
									curFunction.throwOldExpandContent = true;
									tabCol.level = curFunction.expandLevel;
									String nextLvl = SubjectDimUtil
											.getExpandNextLevel(tabCol);
									tabCol.level = nextLvl;
									curFunction.expandLevel = nextLvl;
								}
							}
						}
						// 设置该列的当前层次
						break;
					}
				}
			}
		}
		return curFunction;
	}

	/**
	 * 分析过滤请求
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的功能对象
	 */
	private static TableCurFunc parseRequestFilter(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request) {
		TableCurFunc curFunction = curFunc;
		String stylePara = request.getParameter(SubjectConst.REQ_FILTER_INDEXS);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.filterIndexs = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_FILTER_LEVEL);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.filterLevel = stylePara;
		stylePara = request.getParameter(SubjectConst.REQ_FILTER_VALUES);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.filterValues = stylePara;
		curFunction.firstFiltered = (null != curFunction.filterValues ? true
				: false);
		stylePara = request.getParameter(SubjectConst.REQ_FILTER_IS_MAX);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.isFilterMax = (SubjectConst.YES
					.equalsIgnoreCase(stylePara) ? true : false);
		stylePara = request.getParameter(SubjectConst.REQ_FILTER_IS_MAX);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.isFilterMax = (SubjectConst.YES
					.equalsIgnoreCase(stylePara) ? true : false);
		stylePara = request.getParameter(SubjectConst.REQ_FILTER_IS_MIN);
		if (null != stylePara && !"".equals(stylePara))
			curFunction.isFilterMin = (SubjectConst.YES
					.equalsIgnoreCase(stylePara) ? true : false);
		return curFunction;
	}

	/**
	 * 分析指标过滤条件
	 * 
	 * @param curFunc
	 *            功能对象
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            请求对象
	 * @return 分析后的功能对象
	 */
	private static TableCurFunc parseRequestHaving(TableCurFunc curFunc,
			SubjectCommTabDef subTable, HttpServletRequest request,
			PubInfoConditionTable[] conditions) {
		TableCurFunc curFunction = curFunc;
		// 数据过滤
		HttpSession session = request.getSession();
		Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (null != tmpObj) {
			// SubjectQryStruct qry = (SubjectQryStruct) tmpObj;
			ReportQryStruct qry = (ReportQryStruct) tmpObj;
			String having = CommConditionUtil.getPubHaving(conditions, request,
					qry, SubjectConst.PUB_INFO_QUERY_TYPE_REPLACE);
			if (null != having)
				curFunction.dataHaving = having;

		}
		return curFunction;
	}
}
