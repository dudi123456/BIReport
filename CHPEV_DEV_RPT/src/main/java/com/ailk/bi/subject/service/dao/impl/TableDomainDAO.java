package com.ailk.bi.subject.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.table.SubjectCommTabHead;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.subject.service.dao.ITableDomainDAO;
import com.ailk.bi.subject.util.SubjectConst;

/**
 * @author xdou 表格域对象类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableDomainDAO implements ITableDomainDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableDomainDAO#getSubjectTable(java.lang
	 * .String)
	 */
	public SubjectCommTabDef getSubjectTable(String tableId, HttpServletRequest request) throws SubjectException {
		if (null == tableId || "".equals(tableId))
			throw new SubjectException("获取专题表格定义时表格标识为空");
		SubjectCommTabDef sub_table = null;
		// 生成表格对象，基本属性
		sub_table = getSubjectTableDef(tableId);
		// 获得列定义
		List tableCols = getTableCols(tableId, sub_table.table_type, request);
		sub_table.tableCols = tableCols;
		// 保存以前的列对象，为全部收缩保存,此处估计要克隆
		sub_table.preTableCols = tableCols;
		return sub_table;
	}

	/**
	 * 生成表格对象
	 * 
	 * @param tableId
	 *            表格标识
	 * @return 表格对象
	 * @throws SubjectException
	 */
	private SubjectCommTabDef getSubjectTableDef(String tableId) throws SubjectException {
		if (null == tableId || "".equals(tableId))
			throw new SubjectException("获取专题表格基本定义时表格标识为空");
		SubjectCommTabDef sub_table = null;
		try {
			// 查询所有列定义，包括计算用的字段
			String sql = SQLGenator.genSQL("Q6500", tableId);
			System.out.println("Q6500=============" + sql);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				for (int i = 0; i < svces.length; i++) {
					sub_table = new SubjectCommTabDef();
					sub_table.table_id = svces[i][0];
					sub_table.table_name = svces[i][1];
					sub_table.table_desc = svces[i][2];
					sub_table.data_table = svces[i][3];
					sub_table.data_where = svces[i][4];
					sub_table.has_expand = svces[i][5];
					sub_table.throw_old = svces[i][6];
					sub_table.has_paging = svces[i][7];
					sub_table.time_field = svces[i][8];
					sub_table.field_type = svces[i][9];
					sub_table.time_level = svces[i][10];
					sub_table.row_clicked_chartchange = svces[i][11];
					sub_table.rlt_chart_ids = svces[i][12];
					sub_table.has_head = svces[i][13];
					sub_table.sum_display = svces[i][14];
					sub_table.dim_ascol = svces[i][15];
					sub_table.custom_msu = svces[i][16];
					sub_table.has_expandall = svces[i][17];
					// 如果有表头对象
					if (null != svces[i][18] && !"".equals(svces[i][18])) {
						SubjectCommTabHead tabHead = new SubjectCommTabHead();
						tabHead.table_id = svces[i][18];
						tabHead.table_head = svces[i][19];
						tabHead.row_span = svces[i][20];
						tabHead.row_head_swap_header = svces[i][21];
						sub_table.tabHead = tabHead;
					}
					sub_table.table_type = svces[i][22];

					sub_table.row_clicked_tablechange = svces[i][23];
					sub_table.rlt_table_ids = svces[i][24];
					sub_table.has_sort = svces[i][25];
					sub_table.page_rows = svces[i][26];
					sub_table.row_head_swap = svces[i][27];
					sub_table.right_col_id = svces[i][28];
					sub_table.right_lvl = svces[i][29];
					sub_table.has_avg = svces[i][30];
					sub_table.avg_pos = svces[i][31];
					sub_table.head_swap_ratio_index = svces[i][32];
					sub_table.head_swap_ratio_digit = svces[i][33];
					sub_table.head_swap_col_digit = svces[i][34];
					sub_table.sum_pos = svces[i][35];
					sub_table.condition_in = svces[i][36];
					sub_table.no_groupby = svces[i][37];
					sub_table.no_orderby = svces[i][38];
				}
			}
		} catch (AppException ae) {
			sub_table = null;
			throw new SubjectException("获取专题表格基本定义时发生数据库错误", ae);
		}
		return sub_table;
	}

	/**
	 * 获取表格列定义
	 * 
	 * @param tableId
	 *            表格标识
	 * @return 列定义列表
	 * @throws SubjectException
	 */
	private List getTableCols(String tableId, String table_type, HttpServletRequest request) throws SubjectException {
		if (null == tableId || "".equals(tableId))
			throw new SubjectException("获取专题表格列定义时表格标识为空");
		List tableCols = null;

		HttpSession session = request.getSession();
		Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		ReportQryStruct qry = null;
		if (null != tmpObj) {
			qry = (ReportQryStruct) tmpObj;
		}

		if (table_type == null) {
			// 初始化为默认报表类型
			table_type = "100";
		}
		// 获取数据日期
		String date = qry.gather_date;
		if ((date == null || "".equals(date)) && table_type.startsWith("101")) {
			date = qry.gather_day;
		} else if ((date == null || "".equals(date)) && table_type.startsWith("102")) {
			date = qry.gather_month;
		}

		String datestart = "";
		String dateend = "";
		int dateLen = 0;
		if (table_type.startsWith("101") && date != null && date.length() == 8) {
			datestart = date.substring(0, 6);
			dateend = date.substring(6, 8);
			if (table_type.indexOf("_") > 0) {
				int sIndex = table_type.indexOf("_") + 1;
				dateLen = StringB.toInt(table_type.substring(sIndex), 0);
				String sdate = DateUtil.getDiffDay(0 - dateLen, date);
				qry.date_s = sdate;
				session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qry);
			}
		} else if (table_type.startsWith("102") && date != null && date.length() == 6) {
			datestart = date.substring(0, 4);
			dateend = date.substring(4, 6);
			if (table_type.indexOf("_") > 0) {
				int sIndex = table_type.indexOf("_") + 1;
				dateLen = StringB.toInt(table_type.substring(sIndex), 0);
				String sdate = DateUtil.getDiffMonth(0 - dateLen, date);
				qry.date_s = sdate;
				session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qry);
			}
		}
		try {
			// 查询所有列定义，包括计算用的字段
			String sql = SQLGenator.genSQL("Q6520", tableId);
			// System.out.println("Q6520============="+sql);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				Map levels = getColsLevels(tableId);
				tableCols = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					int col1 = Integer.parseInt(svces[i][1]);
					int col2 = Integer.parseInt("99" + dateend);
					// 从1号开始的日期的报表
					if (svces[i][1].startsWith("99") && col1 > col2 && dateLen == 0) {
						continue;
					}
					// 按日期周期长度显示的报表
					if (svces[i][1].startsWith("99") && dateLen > 0 && i > dateLen) {
						continue;
					}

					SubjectCommTabCol colObj = new SubjectCommTabCol();
					colObj.table_id = svces[i][0];
					colObj.col_id = svces[i][1];
					colObj.col_name = svces[i][2];
					if (colObj.col_id.startsWith("99") && dateLen > 0) {
						String tmp = colObj.col_name;
						String tmpdate = date;
						if (date.length() == 8) {
							tmpdate = DateUtil.getDiffDay(i - dateLen, tmpdate);
							String curM = tmpdate.substring(4, 6);
							String curD = tmpdate.substring(6, 8);
							if ("01".equals(colObj.col_id.substring(2, 4))) {
								tmp = curM + "月" + curD + "号";
							} else {
								String predate = DateUtil.getDiffDay(-1, tmpdate);
								String preM = predate.substring(4, 6);
								if (preM.equals(curM)) {
									tmp = curD + "号";
								} else {
									tmp = curM + "月" + curD + "号";
								}
							}
						} else if (date.length() == 6) {
							tmpdate = DateUtil.getDiffMonth(i - dateLen, tmpdate);
							String curY = tmpdate.substring(0, 4);
							String curM = tmpdate.substring(4, 6);
							if ("01".equals(colObj.col_id.substring(2, 4))) {
								tmp = curY + "年" + curM + "月";
							} else {
								String predate = DateUtil.getDiffMonth(-1, tmpdate);
								String preY = predate.substring(0, 4);
								if (preY.equals(curY)) {
									tmp = curM + "月";
								} else {
									tmp = curY + "年" + curM + "月";
								}
							}
						}
						colObj.col_name = tmp;
					}

					colObj.col_desc = svces[i][3];
					colObj.col_sequence = svces[i][4];
					colObj.is_measure = svces[i][5];
					colObj.dim_aswhere = svces[i][6];
					colObj.dim_ascol = svces[i][7];
					colObj.default_display = svces[i][8];
					colObj.is_expand_col = svces[i][9];
					if (SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
						if (null != levels && null != levels.get(colObj.col_id)) {
							colObj.levels = (List) levels.get(colObj.col_id);
						}
					}
					colObj.defalut_drilled = svces[i][10];
					colObj.init_level = svces[i][11];

					if (("101".equals(table_type) || "102".equals(table_type)) && colObj.col_id.startsWith("99")
							&& dateLen == 0) {
						String tmp = svces[i][12];
						tmp = StringB.replace(tmp, "#date#", datestart + colObj.col_id.substring(2, 4));
						colObj.code_field = tmp;
					} else if (colObj.col_id.startsWith("99") && dateLen > 0) {
						String tmp = svces[i][12];
						String tmpdate = date;
						if (date.length() == 8) {
							tmpdate = DateUtil.getDiffDay(i - dateLen, tmpdate);
						} else if (date.length() == 6) {
							tmpdate = DateUtil.getDiffMonth(i - dateLen, tmpdate);
						}
						tmp = StringB.replace(tmp, "#date#", tmpdate);
						colObj.code_field = tmp;
					} else {
						colObj.code_field = svces[i][12];
					}

					colObj.init_code_field = colObj.code_field;
					colObj.desc_field = svces[i][13];
					colObj.desc_astitle = svces[i][14];
					colObj.is_ratio = svces[i][15];
					colObj.data_type = svces[i][16];
					if (null == colObj.data_type || "".equals(colObj.data_type)) {
						colObj.data_type = SubjectConst.DATA_TYPE_STRING;
					}
					colObj.digit_length = svces[i][17];
					colObj.has_comratio = svces[i][18];
					colObj.has_link = svces[i][19];
					colObj.link_url = svces[i][20];
					colObj.link_target = svces[i][21];
					colObj.has_last = svces[i][22];
					colObj.last_display = svces[i][23];
					colObj.rise_arrow_color = svces[i][24];
					colObj.has_last_link = svces[i][25];
					colObj.last_url = svces[i][26];
					colObj.last_target = svces[i][27];
					colObj.has_loop = svces[i][28];
					colObj.loop_display = svces[i][29];
					colObj.has_loop_link = svces[i][30];
					colObj.loop_url = svces[i][31];
					colObj.loop_target = svces[i][32];
					colObj.is_colclk = svces[i][33];
					colObj.col_chart_ids = svces[i][34];
					colObj.is_cellclk = svces[i][35];
					colObj.cell_chart_ids = svces[i][36];
					colObj.status = svces[i][37];
					colObj.total_displayed = svces[i][38];
					colObj.row_span = svces[i][39];
					colObj.head_place = svces[i][40];
					colObj.default_sort = svces[i][41];
					colObj.sort_order = svces[i][42];
					colObj.has_rank = svces[i][43];
					colObj.rank_mode = svces[i][44];
					colObj.rank_order = svces[i][45];
					colObj.rank_varity = svces[i][46];
					colObj.rank_last = svces[i][47];
					colObj.row_css_class = svces[i][48];
					colObj.title_align = svces[i][49];
					colObj.link_limit_right = svces[i][50];
					colObj.avg_display = svces[i][51];
					colObj.pos_neg_process = svces[i][52];
					colObj.pos_process = svces[i][53];
					colObj.digit_process = svces[i][54];
					colObj.need_substr = svces[i][55];
					colObj.substr_num = svces[i][56];
					// 对排名进行下处理
					if (SubjectConst.RANK_MODE_DISCON.equalsIgnoreCase(colObj.rank_mode)) {
						colObj.rank_mode = SubjectConst.RANK_FUNC_DISCON;
					} else {
						colObj.rank_mode = SubjectConst.RANK_FUNC_CON;
					}
					// 如果是默认就展开的列
					if (SubjectConst.YES.equalsIgnoreCase(colObj.defalut_drilled)
							&& SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
						// 将从没有钻取属性设置为FALSE
						colObj.canDrill = true;
					} else if (SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
						// 如果是需要展开列，设置为TRUE
						colObj.canDrill = true;
					} else {
						// 如果不需要展开，设置为FAlSE
						colObj.canDrill = false;
					}
					// 如果是默认就展开列
					if (SubjectConst.YES.equalsIgnoreCase(colObj.defalut_drilled)) {
						// 设置初始层次为定义
						colObj.level = colObj.init_level;
					} else {
						// 否则设置为0
						colObj.level = SubjectConst.ZERO;
					}
					// 设置默认的索引
					colObj.index = Integer.parseInt(colObj.col_sequence);
					colObj.display_order = colObj.index;
					// 这里设置一下最后
					colObj.descAsTitle.put(SubjectConst.ZERO, colObj.desc_astitle);
					if (null != colObj.levels) {
						Iterator iter = colObj.levels.iterator();
						while (iter.hasNext()) {
							SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) iter.next();
							colObj.descAsTitle.put(levObj.lev_id, levObj.desc_astitle);
						}
					}
					if (Integer.parseInt(colObj.row_span) > 0) {
						colObj.rowSpaned = true;
					}
					tableCols.add(colObj);
				}
			}
		} catch (AppException ae) {
			tableCols = null;
			throw new SubjectException("获取专题表格列定义时发生数据库错误", ae);
		}
		return tableCols;
	}

	/**
	 * 获取表格的所有的列层次定义对象
	 * 
	 * @param tableId
	 *            表格标识
	 * @return 以列标识为键值的MAP列表
	 * @throws SubjectException
	 */
	private Map getColsLevels(String tableId) throws SubjectException {
		if (null == tableId || "".equals(tableId))
			throw new SubjectException("获取专题表格列的层次定义时表格标识为空");
		Map colsLevels = null;
		try {
			// 层次查询
			String sql = SQLGenator.genSQL("Q6530", tableId);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				colsLevels = new HashMap();
				List levels = new ArrayList();
				String key = "";
				for (int i = 0; i < svces.length; i++) {
					SubjectCommDimHierarchy levObj = new SubjectCommDimHierarchy();
					levObj.table_id = svces[i][0];
					levObj.col_id = svces[i][1];
					levObj.lev_id = svces[i][2];
					levObj.lev_name = svces[i][3];
					levObj.lev_memo = svces[i][4];
					levObj.src_idfld = svces[i][5];
					levObj.idfld_type = svces[i][6];
					levObj.src_namefld = svces[i][7];
					levObj.desc_astitle = svces[i][8];
					levObj.has_link = svces[i][9];
					levObj.link_url = svces[i][10];
					levObj.link_target = svces[i][11];
					if (!key.equalsIgnoreCase(levObj.col_id)) {
						if (!"".equalsIgnoreCase(key)) {
							colsLevels.put(key, levels);
							levels = new ArrayList();
						}
						levels.clear();
						levels.add(levObj);
						key = levObj.col_id;
					} else {
						levels.add(levObj);
					}
				}
				colsLevels.put(key, levels);
			}
		} catch (AppException ae) {
			colsLevels = null;
			throw new SubjectException("获取专题表格列的层次定义时发生数据库错误", ae);
		}
		return colsLevels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableDomainDAO#parseRequestToTableColStruct
	 * (com.asiabi.base.table.SubjectCommTabDef,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public SubjectCommTabDef parseRequestToTableColStruct(SubjectCommTabDef subTable, HttpServletRequest request)
			throws SubjectException {
		if (null == subTable)
			throw new SubjectException("分析用户请求中的状态到表列域对象时表格域对象为空");
		if (null == request)
			throw new SubjectException("分析用户请求中的状态到表列域对象时请求对象为空");
		return parseRequest(subTable, request);
	}

	/**
	 * 分析用户请求对象到表格对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param request
	 *            用户请求对象
	 * @return 分析后的表格对象
	 */
	private SubjectCommTabDef parseRequest(SubjectCommTabDef subTable, HttpServletRequest request) {
		SubjectCommTabDef retTable = subTable;
		// 先看看是否第一次展现，此时没有任何其他请求
		String func = request.getParameter(SubjectConst.REQ_TABLE_FUNC);
		if (null == func || "".equals(func)) {
			return retTable;
		}
		// 表格本身定义不会有什么变化，只有列定义
		List cols = new ArrayList();
		List tabCols = retTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			String index = "" + tabCol.index;
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				// 维度,区分为扩展列，普通列
				// 要考虑用户定制维度，可能选择另一个扩展列和普通列
				// 先考虑扩展列
				// 先看看是否有 dim_level_index值，有的化显示，没有不显示
				String dimLevel = request.getParameter(SubjectConst.REQ_DIM_LEVEL_PREFIX + index);
				if (null == dimLevel || "".equals(dimLevel)) {
					// 列不显示，既然不显示，则不进行任何进一步的分析
					tabCol.default_display = SubjectConst.NO;
				} else {
					tabCol.default_display = SubjectConst.YES;
					if (SubjectConst.TABLE_FUNC_INIT.equals(func)) {
						// 初始化最初的
						tabCol.level = SubjectConst.ZERO;
					} else {
						tabCol.level = dimLevel;
					}
					// 下面检查 各个层次的值，按理说只要到目前就可以了，还是全部检查
					Map values = new HashMap();
					String value = request.getParameter(SubjectConst.REQ_DIM_VALUE_PREFIX + index + "_"
							+ SubjectConst.ZERO);
					if (null != value && !"".equals(value)) {
						values.put(SubjectConst.ZERO, value);
					}
					if (null != tabCol.levels && 0 < tabCol.levels.size()) {
						Iterator levIter = tabCol.levels.iterator();
						while (levIter.hasNext()) {
							SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) levIter.next();
							value = request.getParameter(SubjectConst.REQ_DIM_VALUE_PREFIX + index + "_"
									+ colLev.lev_id);
							if (null != value && !"".equals(value)) {
								values.put(colLev.lev_id, value);
							}
						}
					}
					// 这是要判断一下values的值
					if (values.size() <= 0) {
						tabCol.values = null;
					} else {
						tabCol.values = values;
					}
				}
			} else {
				// 指标，对于指标,暂时不考虑钻取，值看指标
				String msuLevel = request.getParameter(SubjectConst.REQ_MSU_LEVEL_PREFIX + index);
				if (null == msuLevel || "".equals(msuLevel)) {
					tabCol.default_display = SubjectConst.NO;
				} else {
					tabCol.default_display = SubjectConst.YES;
					if (SubjectConst.TABLE_FUNC_INIT.equals(func)) {
						// 初始化最初的
						tabCol.level = SubjectConst.ZERO;
					} else
						tabCol.level = msuLevel;
				}
			}
			cols.add(tabCol);
		}
		retTable.tableCols = cols;
		return retTable;
	}
}
