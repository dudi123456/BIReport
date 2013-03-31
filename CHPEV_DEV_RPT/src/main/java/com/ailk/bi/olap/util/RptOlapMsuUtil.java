package com.ailk.bi.olap.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.MeasureTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.table.RptOlapUserMsuTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapMsuUtil {
	/**
	 * 所有指标对象MAP集合
	 */
	private static Map allMsu = null;

	public static List adjustUserMsus(List rptMsus, List userCustomMsus) {
		List userMsus = rptMsus;
		if (null == userCustomMsus || userCustomMsus.size() == 0) {
			return userMsus;
		}
		Iterator iter = userCustomMsus.iterator();
		RptOlapUserMsuTable userMsu = null;
		Map<String, RptOlapUserMsuTable> mUserCustomMsus = new HashMap<String, RptOlapUserMsuTable>();
		while (iter.hasNext()) {
			userMsu = (RptOlapUserMsuTable) iter.next();
			mUserCustomMsus.put(userMsu.msu_id, userMsu);
		}
		iter = userMsus.iterator();
		RptOlapMsuTable rptMsu = null;
		Object obj = null;
		int i = 1;
		while (iter.hasNext()) {
			rptMsu = (RptOlapMsuTable) iter.next();
			obj = null;
			obj = mUserCustomMsus.get(rptMsu.msu_id);
			if (null != obj) {
				rptMsu.default_display = RptOlapConsts.YES;
				rptMsu.order_id = ((RptOlapUserMsuTable) obj).display_order;
				i++;
			} else {
				rptMsu.default_display = RptOlapConsts.NO;
			}
		}
		// 还得设置
		iter = userMsus.iterator();
		while (iter.hasNext()) {
			rptMsu = (RptOlapMsuTable) iter.next();
			if (rptMsu.default_display.equalsIgnoreCase(RptOlapConsts.NO)) {
				rptMsu.order_id = "" + i++;
			}
		}
		// 排序
		Collections.sort(userMsus, new RptOlapMsuComparator());
		return userMsus;
	}

	private static StringBuffer getMsuRatio(MeasureTable msu,
			StringBuffer thisPeriod, StringBuffer lastPeriod, String ratioPeriod) {
		StringBuffer ratio = new StringBuffer();
		if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
			String initFld = RptOlapStringUtil.removeCalMsuSum(msu.real_fld);
			initFld = RptOlapStringUtil.clearVirTabName(initFld);
			thisPeriod.delete(0, thisPeriod.length());
			thisPeriod.append(RptOlapStringUtil.getPeriodMsuFld(initFld,
					RptOlapConsts.TIME_VIR_TAB_NAME,
					RptOlapConsts.SQL_PERIOD_TYPE,
					RptOlapConsts.SQL_THIS_PERIOD));

			lastPeriod.delete(0, lastPeriod.length());
			lastPeriod.append(RptOlapStringUtil.getPeriodMsuFld(initFld,
					RptOlapConsts.TIME_VIR_TAB_NAME,
					RptOlapConsts.SQL_PERIOD_TYPE, ratioPeriod));
			String sameRatio = "(CASE WHEN " + lastPeriod
					+ "=0 THEN NULL ELSE " + "(" + thisPeriod + "-"
					+ lastPeriod + ")/" + lastPeriod + " END)";
			sameRatio = RptOlapConsts.NVL_PROC
					.replaceAll("::NULL::", sameRatio);
			ratio.append(sameRatio);
		} else {
			// 周同比值
			String sameRatio = "(CASE WHEN " + lastPeriod
					+ "=0 THEN NULL ELSE " + "(" + thisPeriod + "-"
					+ lastPeriod + ")/" + lastPeriod + " END)";
			sameRatio = RptOlapConsts.NVL_PROC
					.replaceAll("::NULL::", sameRatio);
			ratio.append(sameRatio);
		}
		return ratio;
	}

	public static boolean isCollapseToInitLevel(String parentId, List rptMsus)
			throws ReportOlapException {
		if (null == parentId || "".equals(parentId) || null == rptMsus
				|| 0 >= rptMsus.size())
			throw new ReportOlapException("判断是否指标收缩到最初状态时输入的参数为空");
		boolean toInit = false;
		if (RptOlapConsts.ZERO_STR.equals(parentId))
			return true;
		Iterator iter = rptMsus.iterator();
		while (iter.hasNext()) {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) iter.next();
			if (rptMsu.msu_id.equals(parentId)) {
				toInit = true;
				break;
			}
		}
		return toInit;
	}

	public static RptOlapMsuTable getCollapseParentMsu(RptOlapMsuTable rptMsu,
			String childMsuId) throws ReportOlapException {
		if (null == rptMsu || null == childMsuId || "".equals(childMsuId))
			throw new ReportOlapException("确定指标收缩的父指标时输入的参数维空");
		RptOlapMsuTable parentRptMsu = null;
		try {
			String sql = SQLGenator.genSQL("Q5730", "'"
					+ rptMsu.msuInfo.parent_id + "'");
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				MeasureTable parentMsu = genMsuStruct(svces[0]);
				parentRptMsu = genVirRptMsu(rptMsu, parentMsu);
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取报表的指标定义发生错误", ae);
		}
		return parentRptMsu;
	}

	public static String getCollapseMsuParentId(List tabCols,
			String collapseMsuId) throws ReportOlapException {
		if (null == tabCols || null == collapseMsuId || 0 >= tabCols.size()
				|| "".equals(collapseMsuId))
			throw new ReportOlapException("确定指标收缩的父指标编码时输入的参数维空");
		String parentId = null;
		RptOlapMsuTable collapseMsu = null;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (!tCol.isDim()) {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				if (collapseMsuId.equals(rptMsu.msuInfo.msu_id)) {
					collapseMsu = rptMsu;
					break;
				}
			}
		}
		if (null != collapseMsu) {
			parentId = collapseMsu.msuInfo.parent_id;
		}
		return parentId;
	}

	public static List getReportMsu(String reportId) throws ReportOlapException {
		List msus = null;
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("获取报表的指标定义时报表标识为空");
		try {
			String sql = SQLGenator.genSQL("Q5710", reportId);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				msus = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					RptOlapMsuTable rptMsu = genRptMsuStruct(svces[i]);
					msus.add(rptMsu);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取报表的指标定义发生错误", ae);
		}
		return msus;
	}

	/**
	 * 生成报表指标对象
	 *
	 * @param svces
	 * @return
	 */
	private static RptOlapMsuTable genRptMsuStruct(String[] svces)
			throws ReportOlapException {
		RptOlapMsuTable rptMsu = null;
		if (null == svces || svces.length <= 0)
			throw new ReportOlapException("生成指标对象输入参数为空");
		Map mAllMsu = getAllMsu();
		rptMsu = new RptOlapMsuTable();
		rptMsu.report_id = svces[0];
		rptMsu.msu_id = svces[1];
		rptMsu.col_name = svces[2];
		rptMsu.default_display = svces[3];
		rptMsu.order_id = svces[4];
		rptMsu.is_dig = svces[5];
		rptMsu.dig_level = svces[6];
		rptMsu.is_sumbydate = svces[7];
		rptMsu.has_link = svces[8];
		rptMsu.link_url = svces[9];
		rptMsu.link_target = svces[10];
		rptMsu.need_alert = svces[11];
		rptMsu.compare_to = svces[12];
		rptMsu.ratio_compare = svces[13];
		rptMsu.high_value = svces[14];
		rptMsu.low_value = svces[15];
		rptMsu.alert_mode = svces[16];
		rptMsu.rise_color = svces[17];
		rptMsu.down_color = svces[18];
		rptMsu.is_valid = svces[19];
		if (null != mAllMsu && null != mAllMsu.get(rptMsu.msu_id)) {
			rptMsu.msuInfo = (MeasureTable) mAllMsu.get(rptMsu.msu_id);
		}
		return rptMsu;
	}

	public static Map getAllMsu() {
		if (null == allMsu)
			allMsu = genAllMsu();
		return allMsu;
	}

	private static Map genAllMsu() throws ReportOlapException {
		Map msus = null;
		try {
			String sql = SQLGenator.genSQL("Q5700");
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				msus = new HashMap();
				for (int i = 0; i < svces.length; i++) {
					MeasureTable msu = RptOlapMsuUtil.genMsuStruct(svces[i]);
					msus.put(msu.msu_id, msu);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取所有指标时发生异常", ae);
		}
		return msus;
	}

	/**
	 * 获取孩子节点
	 *
	 * @param msuId
	 * @return
	 * @throws ReportOlapException
	 */
	public static List getMsuChildren(String msuId) throws ReportOlapException {
		if (null == msuId || "".equals(msuId))
			throw new ReportOlapException("获取指标的子指标时输入的指标标识为空");
		List children = null;
		try {
			String select = SQLGenator.genSQL("Q5720", "'" + msuId + "'");
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces) {
				children = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					MeasureTable msu = genMsuStruct(svces[i]);
					children.add(msu);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取指标的子指标时发生异常", ae);
		}
		return children;
	}

	public static List genRptMsus(RptOlapMsuTable rptMsu, List msus)
			throws ReportOlapException {
		if (null == rptMsu || null == msus || 0 >= msus.size())
			throw new ReportOlapException("将指标定义对象列表转换为报表对象列表时输入的参数为空");
		List rptMsus = new ArrayList();
		Iterator iter = msus.iterator();
		while (iter.hasNext()) {
			MeasureTable msu = (MeasureTable) iter.next();
			RptOlapMsuTable virRptMsu = genVirRptMsu(rptMsu, msu);
			rptMsus.add(virRptMsu);
		}
		return rptMsus;
	}

	public static RptOlapMsuTable genVirRptMsu(RptOlapMsuTable rptMsu,
			MeasureTable msu) throws ReportOlapException {
		if (null == rptMsu || null == msu)
			throw new ReportOlapException("生成虚拟报表指标对象时输入的参数为空");
		RptOlapMsuTable virRptMsu = null;
		virRptMsu = cloneRptMsu(rptMsu);
		// msu_id重新设置
		virRptMsu.msu_id = msu.msu_id;
		// 列名称重新设置
		virRptMsu.col_name = msu.msu_name;
		// 链接对象重新设置
		virRptMsu.msuInfo = msu;
		virRptMsu.default_display = RptOlapConsts.YES;
		virRptMsu.is_dig = RptOlapConsts.YES;
		// 重新设置新的最大层次
		virRptMsu.dig_level = rptMsu.dig_level;
		return virRptMsu;
	}

	public static RptOlapMsuTable cloneRptMsu(RptOlapMsuTable rptMsu)
			throws ReportOlapException {
		if (null == rptMsu)
			throw new ReportOlapException("克隆报表指标对象时输入的参数为空");
		RptOlapMsuTable repRptMsu = null;
		repRptMsu = new RptOlapMsuTable();
		repRptMsu.report_id = rptMsu.report_id;
		repRptMsu.msu_id = rptMsu.msu_id;
		repRptMsu.col_name = rptMsu.col_name;
		repRptMsu.default_display = rptMsu.default_display;
		repRptMsu.order_id = rptMsu.order_id;
		repRptMsu.is_dig = rptMsu.is_dig;
		repRptMsu.dig_level = rptMsu.dig_level;
		repRptMsu.is_sumbydate = rptMsu.is_sumbydate;
		repRptMsu.has_link = rptMsu.has_link;
		repRptMsu.link_url = rptMsu.link_url;
		repRptMsu.link_target = rptMsu.link_target;
		repRptMsu.need_alert = rptMsu.need_alert;
		repRptMsu.compare_to = rptMsu.compare_to;
		repRptMsu.ratio_compare = rptMsu.ratio_compare;
		repRptMsu.high_value = rptMsu.high_value;
		repRptMsu.low_value = rptMsu.low_value;
		repRptMsu.alert_mode = rptMsu.alert_mode;
		repRptMsu.rise_color = rptMsu.rise_color;
		repRptMsu.down_color = rptMsu.down_color;
		repRptMsu.is_valid = rptMsu.is_valid;
		return repRptMsu;
	}

	/**
	 * 生成指标对象
	 *
	 * @param svces
	 * @return
	 * @throws ReportOlapException
	 */
	public static MeasureTable genMsuStruct(String[] svces)
			throws ReportOlapException {
		MeasureTable msu = null;
		if (null == svces || svces.length <= 0)
			throw new ReportOlapException("生成指标对象时参数为空");
		msu = new MeasureTable();
		msu.msu_id = svces[0];
		msu.msu_name = svces[1];
		msu.msu_target = svces[2];
		msu.is_calmsu = svces[3];
		msu.is_timemsu = svces[4];
		msu.is_deri = svces[5];
		msu.msu_type = svces[6];
		msu.parent_id = svces[7];
		msu.msu_rule = svces[8];
		msu.rule_desc = svces[9];
		msu.msu_desc = svces[10];
		msu.msu_unit = svces[11];
		msu.pricesion = svces[12];
		msu.comma_splitted = svces[13];
		msu.ratio_displayed = svces[14];
		msu.nvl_proc = svces[15];
		msu.zero_proc = svces[16];
		msu.msu_fld = svces[17];
		msu.real_fld = svces[18];
		msu.fld_type = svces[19];
		msu.is_valid = svces[20];
		msu.val_date = svces[21];
		msu.exp_date = svces[22];
		msu.rsv_fld1 = svces[23];
		msu.rsv_fld2 = svces[24];
		msu.rsv_fld3 = svces[25];
		// 由于存在嵌套，使用数据库查询
		return msu;
	}

	/**
	 * 生成带有链接的指标表头
	 *
	 * @param tCol
	 * @param tabCols
	 * @param olapFun
	 * @param content
	 * @return
	 * @throws ReportOlapException
	 */
	public static String genMsuHeadContent(RptOlapTableColumnStruct tCol,
			List tabCols, RptOlapFuncStruct olapFun, String content)
			throws ReportOlapException {
		String html = null;
		if (null == tCol || null == tabCols || null == olapFun
				|| null == content || tabCols.size() <= 0)
			throw new ReportOlapException("生成指标的HTML内容输入的参数为空");
		if (!tCol.isDim()) {
			// 保证是指标
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
			String url = RptOlapTabDomainUtil.convertTableColStructToUrl(
					tabCols, olapFun);

			String level = tCol.getDigLevel();
			String maxLevel = rptMsu.dig_level;
			int iLevel = 0;
			int iMaxLevel = 0;
			if (null != level && !"".equals(level))
				iLevel = Integer.parseInt(level);
			// 要保证maxLevel不要超出可钻取的最大层次，一般最大3层
			if (null != maxLevel && !"".equals(maxLevel))
				iMaxLevel = Integer.parseInt(maxLevel);
			// 要考虑指标如果钻取了，得收缩回来,还有多层次时
			// 先考虑全部收缩、单级收缩
			StringBuffer link = new StringBuffer("");
			// 下面先判断是否生成收缩链接
			StringBuffer collpase = new StringBuffer("");
			if (!RptOlapConsts.ZERO_STR.equals(level)
					&& !RptOlapConsts.NO_DIGGED_LEVEL.equals(level)
					&& tCol.isSumMsu()) {
				// 是指标钻取得单元格
				collpase.append(RptOlapConsts.OLAP_DIG_ACTION);
				collpase.append("?").append(RptOlapConsts.REQ_REPORT_ID)
						.append("=::report_id::");
				collpase.append(url);
				collpase.append("&")
						.append(RptOlapConsts.REQ_COLLAPSE_MSU_INIT)
						.append("=").append(RptOlapConsts.YES);
				String tmpStr = collpase.toString();
				collpase.delete(0, collpase.length());
				collpase.append("<a href=\"javascript:")
						.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
						.append("('").append(tmpStr).append("')\"><img ")
						.append("src=\"../images/shrink_init.gif\" ")
						.append("width=\"7\" height=\"11\" border=\"0\"></a>");
			}
			link.append(collpase);
			if (tCol.isNeedDig() && iLevel < iMaxLevel && !tCol.isSumMsu()) {
				link.delete(0, link.length());
				link.append(RptOlapConsts.OLAP_DIG_ACTION);
				link.append("?").append(RptOlapConsts.REQ_REPORT_ID)
						.append("=::report_id::");
				link.append(url);
				// 加上click_dim
				link.append("&").append(RptOlapConsts.REQ_CLICK_MSU)
						.append("=").append(tCol.getIndex());
				link.append("&").append(RptOlapConsts.REQ_CLICK_MSU_ID)
						.append("=").append(rptMsu.msuInfo.msu_id);
				String tmpStr = link.toString();
				link.delete(0, link.length());
				// 这里需要判断到底有没有孩子
				List children = RptOlapMsuUtil
						.getMsuChildren(rptMsu.msuInfo.msu_id);
				if (null != children && children.size() > 0) {
					link.append("<a href=\"javascript:")
							.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
							.append("('").append(tmpStr)
							.append("')\" class=\"")
							.append(RptOlapConsts.HREF_LINK_CLASS)
							.append("\">").append(content).append("</a>");
				} else {
					link.append(content);
				}
			} else {
				link.append(content);
			}
			// 清除collpase信息
			collpase.delete(0, collpase.length());
			if (!RptOlapConsts.ZERO_STR.equals(level)
					&& !RptOlapConsts.NO_DIGGED_LEVEL.equals(level)
					&& tCol.isSumMsu()) {
				collpase.append(RptOlapConsts.OLAP_DIG_ACTION);
				collpase.append("?").append(RptOlapConsts.REQ_REPORT_ID)
						.append("=::report_id::");
				collpase.append(url);
				collpase.append("&").append(RptOlapConsts.REQ_COLLAPSE_MSU)
						.append("=").append(tCol.getIndex());
				collpase.append("&").append(RptOlapConsts.REQ_COLLAPSE_MSU_ID)
						.append("=").append(rptMsu.msuInfo.msu_id);
				String tmpStr = collpase.toString();
				collpase.delete(0, collpase.length());
				collpase.append("<a href=\"javascript:")
						.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
						.append("('").append(tmpStr).append("')\"><img ")
						.append("src=\"../images/shrink_up.gif\" ")
						.append("width=\"7\" height=\"11\" border=\"0\"></a>");
			}
			link.append(collpase);
			html = link.toString();
		}
		return html;
	}

	public static Map genMsuChildrenSelectParts(RptOlapTableColumnStruct tCol,
			boolean hasSum, String begDate, String dateFld,
			RptOlapFuncStruct olapFun, String reportPeriod)
			throws ReportOlapException {
		Map parts = null;
		if (null == tCol || null == begDate || null == dateFld
				|| null == olapFun || null == reportPeriod)
			throw new ReportOlapException("生成指标的子指标的查询语句各部分时输入的参数为空");
		// 获取指标的子节点
		List children = null;
		RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
		if (null == rptMsu.msuInfo.children) {
			children = getMsuChildren(rptMsu.msuInfo.msu_id);
			rptMsu.msuInfo.children = children;
		} else {
			children = rptMsu.msuInfo.children;
		}

		StringBuffer select = new StringBuffer("");
		StringBuffer subSelect = new StringBuffer("");
		StringBuffer from = new StringBuffer("");
		StringBuffer where = new StringBuffer("");
		StringBuffer groupby = new StringBuffer("");
		// StringBuffer orderby=new StringBuffer("");
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			MeasureTable msu = (MeasureTable) iter.next();
			Map msuParts = genMsuSelectParts(msu, hasSum, begDate, dateFld,
					olapFun, reportPeriod);
			select.append((String) msuParts.get("select"));
			subSelect.append((String) msuParts.get("subSelect"));
			from.append((String) msuParts.get("from"));
			where.append((String) msuParts.get("where"));
			groupby.append((String) msuParts.get("groupby"));
		}
		// 加上父指标
		Map msuParts = genMsuSelectParts(rptMsu.msuInfo, hasSum, begDate,
				dateFld, olapFun, reportPeriod);
		select.append((String) msuParts.get("select"));
		subSelect.append((String) msuParts.get("subSelect"));
		from.append((String) msuParts.get("from"));
		where.append((String) msuParts.get("where"));
		groupby.append((String) msuParts.get("groupby"));

		parts = new HashMap();
		parts.put("select", select.toString());
		parts.put("subSelect", subSelect.toString());
		parts.put("from", from.toString());
		parts.put("where", where.toString());
		parts.put("groupby", groupby.toString());
		return parts;
	}

	/**
	 * 生成查询的指标各部分
	 *
	 * @param tCol
	 *            列域对象
	 * @param hasSum
	 *            是否有汇总
	 * @param begDate
	 *            开始日期
	 * @param dateFld
	 *            时间字段
	 * @param olapFun
	 *            当前功能对象
	 * @param reportPeriod
	 *            报表统计周期
	 * @return
	 */
	public static Map genMsuSelectParts(MeasureTable msu, boolean hasSum,
			String begDate, String dateFld, RptOlapFuncStruct olapFun,
			String reportPeriod) throws ReportOlapException {
		if (null == msu || null == begDate || null == dateFld
				|| null == olapFun || null == reportPeriod)
			throw new ReportOlapException("生成指标的查询语句各部分时输入的参数为空");
		Map part = new HashMap();

		StringBuffer select = new StringBuffer("");
		StringBuffer having = new StringBuffer("");
		StringBuffer subSelect = new StringBuffer("");
		StringBuffer groupby = new StringBuffer("");
		StringBuffer orderby = new StringBuffer("");

		// 取出字段
		String msuFld = msu.msu_fld;
		String realFld = msu.real_fld;

		// 清除虚表名
		msuFld = RptOlapStringUtil.replaceVirTabName(msuFld,
				RptOlapConsts.THIS_VIR_TMP_TAB_NAME);
		realFld = RptOlapStringUtil.replaceVirTabName(realFld,
				RptOlapConsts.THIS_VIR_TMP_TAB_NAME);
		// 这里如果出现除法怎么办，会有除0情况，必须处理
		msuFld = RptOlapStringUtil.clearVirTabName(msuFld);
		// 如果是计算公式，则必须处理
		if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
			subSelect.append(RptOlapStringUtil.getCalMsuFactors(realFld,
					RptOlapConsts.THIS_VIR_TMP_TAB_NAME));
		} else {
			String nvlFld = RptOlapConsts.NVL_PROC.replaceAll("::NULL::",
					realFld);
			subSelect.append(nvlFld).append(" AS ").append(msuFld).append(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
		}
		StringBuffer thisPeriod = new StringBuffer();
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;
		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;
		if (hasSum) {
			if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
				// 本期值计算
				thisPeriod.append(RptOlapStringUtil.processThisPeriodCalMsu(
						realFld, RptOlapConsts.THIS_VIR_TAB_NAME,
						RptOlapConsts.SQL_PERIOD_TYPE,
						RptOlapConsts.SQL_THIS_PERIOD, dateFld, begDate));
				select.append(thisPeriod).append(",");
				having.append("ABS(").append(thisPeriod).append(")+");
			} else {
				// 本期值
				msuFld = RptOlapStringUtil.replaceVirTabName(msuFld,
						RptOlapConsts.THIS_VIR_TAB_NAME);
				thisPeriod.append(RptOlapStringUtil.getPeriodMsuSumFld(msuFld,
						RptOlapConsts.TIME_VIR_TAB_NAME,
						RptOlapConsts.SQL_PERIOD_TYPE,
						RptOlapConsts.SQL_THIS_PERIOD, dateFld, begDate));
				select.append(thisPeriod).append(",");
				having.append("ABS(").append(thisPeriod).append(")+");
			}
		}
		StringBuffer thisPeriodSum = new StringBuffer();
		if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
			// 计算指标,需要按这种方式替换所有的指标
			thisPeriodSum.delete(0, thisPeriodSum.length());
			thisPeriodSum.append(RptOlapStringUtil.processCalMsu(realFld,
					RptOlapConsts.TIME_VIR_TAB_NAME,
					RptOlapConsts.SQL_PERIOD_TYPE,
					RptOlapConsts.SQL_THIS_PERIOD));
			// 累计值,是否要预警,预警太复杂了，这里暂时不预警，看以后的需求
			select.append(thisPeriodSum).append(",");
			having.append("ABS(").append(thisPeriodSum).append(")+");
		} else {
			thisPeriodSum.delete(0, thisPeriodSum.length());
			thisPeriodSum.append(RptOlapStringUtil.getPeriodMsuFld(msuFld,
					RptOlapConsts.TIME_VIR_TAB_NAME,
					RptOlapConsts.SQL_PERIOD_TYPE,
					RptOlapConsts.SQL_THIS_PERIOD));
			// 累计值,是否要预警,预警太复杂了，这里暂时不预警，看以后的需求
			select.append(thisPeriodSum).append(",");
			having.append("ABS(").append(thisPeriodSum).append(")+");
		}
		// 同比时
		if (RptOlapConsts.OLAP_FUN_SAME.equals(olapFun.getCurFunc())
				|| hasSameAlert) {
			// 判断是否有周或日同期比
			String sameFun = olapFun.getSameRatioPeriod();
			if (!hasSum) {
				// 如果没有累计
				thisPeriod.delete(0, thisPeriod.length());
				thisPeriod.append(thisPeriodSum);
			}
			// 周本期
			StringBuffer weekPeriod = new StringBuffer();
			// 月本期
			StringBuffer monthPeriod = new StringBuffer();
			if (hasSum) {
				weekPeriod.delete(0, weekPeriod.length());
				monthPeriod.delete(0, monthPeriod.length());
				if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
					weekPeriod.append(RptOlapStringUtil
							.processThisPeriodCalMsu(realFld,
									RptOlapConsts.THIS_VIR_TAB_NAME,
									RptOlapConsts.SQL_PERIOD_TYPE,
									RptOlapConsts.SQL_SAME_WEEK_PERIOD,
									dateFld, begDate));

					monthPeriod.append(RptOlapStringUtil
							.processThisPeriodCalMsu(realFld,
									RptOlapConsts.THIS_VIR_TAB_NAME,
									RptOlapConsts.SQL_PERIOD_TYPE,
									RptOlapConsts.SQL_SAME_PERIOD, dateFld,
									begDate));
				} else {
					weekPeriod.append(RptOlapStringUtil.getPeriodMsuSumFld(
							msuFld, RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_WEEK_PERIOD, dateFld,
							begDate));

					monthPeriod.append(RptOlapStringUtil.getPeriodMsuSumFld(
							msuFld, RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_PERIOD, dateFld, begDate));
				}
			} else {
				weekPeriod.delete(0, weekPeriod.length());
				monthPeriod.delete(0, monthPeriod.length());
				if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
					weekPeriod.append(RptOlapStringUtil.processCalMsu(realFld,
							RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_WEEK_PERIOD));

					monthPeriod.append(RptOlapStringUtil.processCalMsu(realFld,
							RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_PERIOD));
				} else {
					weekPeriod.append(RptOlapStringUtil.getPeriodMsuFld(msuFld,
							RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_WEEK_PERIOD));

					monthPeriod.delete(0, monthPeriod.length());
					monthPeriod.append(RptOlapStringUtil.getPeriodMsuFld(
							msuFld, RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_SAME_PERIOD));
				}
			}
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(reportPeriod)
					&& RptOlapConsts.SAME_RATIO_BOTH_PERIOD.equals(sameFun)) {
				// 周本期值
				select.append(weekPeriod).append(",");
				having.append("ABS(").append(weekPeriod).append(")+");
				// 周同比比值
				// 分母有可能为0
				// 为0时
				StringBuffer sameWeekRatio = getMsuRatio(msu, thisPeriod,
						weekPeriod, RptOlapConsts.SQL_SAME_WEEK_PERIOD);
				select.append(sameWeekRatio).append(",");
				having.append("ABS(").append(sameWeekRatio).append(")+");
				// 月本期值
				select.append(monthPeriod).append(",");
				having.append("ABS(").append(monthPeriod).append(")+");

				// 月同比值
				StringBuffer sameRatio = getMsuRatio(msu, thisPeriod,
						weekPeriod, RptOlapConsts.SQL_SAME_PERIOD);
				select.append(sameRatio).append(",");
				having.append("ABS(").append(sameRatio).append(")+");
			} else if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(reportPeriod)
					&& RptOlapConsts.SAME_RATIO_WEEK_PERIOD.equals(sameFun)) {
				// 周本期值
				select.append(weekPeriod).append(",");
				having.append("ABS(").append(weekPeriod).append(")+");
				StringBuffer sameWeekRatio = getMsuRatio(msu, thisPeriod,
						weekPeriod, RptOlapConsts.SQL_SAME_WEEK_PERIOD);
				select.append(sameWeekRatio).append(",");
				having.append("ABS(").append(sameWeekRatio).append(")+");
			} else {
				// 月本期值
				select.append(monthPeriod).append(",");
				having.append("ABS(").append(monthPeriod).append(")+");
				StringBuffer sameRatio = getMsuRatio(msu, thisPeriod,
						weekPeriod, RptOlapConsts.SQL_SAME_PERIOD);
				select.append(sameRatio).append(",");
				having.append("ABS(").append(sameRatio).append(")+");
			}
		}

		// 环比时
		if (RptOlapConsts.OLAP_FUN_LAST.equals(olapFun.getCurFunc())
				|| hasLastAlert) {
			// 判断是否有周或日同期比
			StringBuffer lastPeriod = new StringBuffer();
			if (!hasSum) {
				// 如果没有累计
				thisPeriod.delete(0, thisPeriod.length());
				thisPeriod.append(thisPeriodSum);
			}
			if (hasSum) {
				lastPeriod.delete(0, lastPeriod.length());
				if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
					lastPeriod.append(RptOlapStringUtil
							.processThisPeriodCalMsu(realFld,
									RptOlapConsts.THIS_VIR_TAB_NAME,
									RptOlapConsts.SQL_PERIOD_TYPE,
									RptOlapConsts.SQL_LAST_PERIOD, dateFld,
									begDate));

				} else {
					lastPeriod.append(RptOlapStringUtil.getPeriodMsuSumFld(
							msuFld, RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_LAST_PERIOD, dateFld, begDate));
				}
			} else {
				lastPeriod.delete(0, lastPeriod.length());
				if (RptOlapConsts.YES.equalsIgnoreCase(msu.is_calmsu)) {
					lastPeriod.append(RptOlapStringUtil.processCalMsu(realFld,
							RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_LAST_PERIOD));
				} else {
					lastPeriod.append(RptOlapStringUtil.getPeriodMsuFld(msuFld,
							RptOlapConsts.TIME_VIR_TAB_NAME,
							RptOlapConsts.SQL_PERIOD_TYPE,
							RptOlapConsts.SQL_LAST_PERIOD));
				}
			}
			// 环比本期值
			select.append(lastPeriod).append(",");
			having.append("ABS(").append(lastPeriod).append(")+");
			// 月环比值
			StringBuffer lastRatio = getMsuRatio(msu, thisPeriod, lastPeriod,
					RptOlapConsts.SQL_LAST_PERIOD);
			select.append(lastRatio).append(",");
			having.append("ABS(").append(lastRatio).append(")+");
		}
		part.put("select", select.toString());
		part.put("subSelect", subSelect.toString());
		part.put("having", having.toString());
		part.put("groupby", groupby.toString());
		part.put("orderby", orderby.toString());
		return part;
	}

	/**
	 * 获取排序的数组本期值下标
	 *
	 * @param tabCols
	 *            域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @return
	 * @throws ReportOlapException
	 */
	public static String getSortAryIndex(List tabCols,
			RptOlapFuncStruct olapFun, String statPeriod)
			throws ReportOlapException {
		String aryIndex = null;
		if (null == tabCols || null == olapFun || null == statPeriod
				|| 0 >= tabCols.size())
			throw new ReportOlapException("对表格进行排序时输入的参数为空");
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;
		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;

		String sortIndex = olapFun.getSort();
		String func = olapFun.getCurFunc();
		int index = -1;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					// 对于折叠展开来说，每次只能是两个
					index++;
					index++;
					if (RptOlapConsts.RESET_MODE_EXPAND.equals(olapFun
							.getDisplayMode())) {
						index = -1;
						index++;
						index++;
					}
				} else {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
					// 本期值
					index++;
					// 判断是否到了
					if (sortIndex.equals(tCol.getIndex() + "")) {
						aryIndex = index + "";
						break;
					}
					// 开始判断下面是否有累加、同比、环比
					if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && RptOlapConsts.YES
							.equalsIgnoreCase(rptMsu.is_sumbydate))) {
						// 折叠、展开时肯定有
						index++;
					}
					if (RptOlapConsts.OLAP_FUN_SAME.equals(func)
							|| hasSameAlert) {
						index++;
						index++;
						// 看看有没有周同比
						if (RptOlapConsts.RPT_STATIC_DAY_PERIOD
								.equals(statPeriod)
								&& RptOlapConsts.SAME_RATIO_BOTH_PERIOD
										.equals(olapFun.getSameRatioPeriod()))
							index++;
					}
					if (RptOlapConsts.OLAP_FUN_LAST.equals(func)
							|| hasLastAlert) {
						index++;
						index++;
					}

				}
			}
		}
		return aryIndex;
	}

	public static List genMsuHead(List tableCols,
			RptOlapTableColumnStruct tCol, RptOlapFuncStruct olapFun,
			String statPeriod, boolean hasRowSpan, String tdClass)
			throws ReportOlapException {
		if (null == tableCols || tableCols.size() <= 0 || null == tCol
				|| null == olapFun || null == statPeriod)
			throw new ReportOlapException("生成指标表头时输入的参数为空");

		List right = new ArrayList();

		StringBuffer rightFirRow = new StringBuffer("");
		StringBuffer rightSecRow = new StringBuffer("");

		StringBuffer firExpRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");

		if (hasRowSpan) {
			// 有两层
			// 第一层
			int colspan = 1;
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
			String func = olapFun.getCurFunc();
			if (RptOlapConsts.OLAP_FUN_SAME.equals(func)
					|| RptOlapConsts.OLAP_FUN_LAST.equals(func)
					|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
					|| RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate))
				colspan++;
			// 对于同比，当周同比和月同比都存在还需要重新累加
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod)
					&& RptOlapConsts.SAME_RATIO_BOTH_PERIOD.equals(olapFun
							.getSameRatioPeriod())) {
				colspan++;
			}
			rightFirRow.append("<td nowrap align=\"center\" colspan=\"")
					.append(colspan).append("\" class=\"").append(tdClass)
					.append("\">");
			// 考虑指标钻取
			rightFirRow.append(RptOlapMsuUtil.genMsuHeadContent(tCol,
					tableCols, olapFun, tCol.getColName()));
			rightFirRow.append("</td>");
			firExpRow.append("<td nowrap align=\"center\" colspan=\"")
					.append(colspan).append("\">");
			String colName = tCol.getColName();
			firExpRow.append(colName);
			firExpRow.append("</td>");
			// 第二层

			String html = "";
			html += "本期";
			html += RptOlapDimUtil.genSortLink(tCol, tableCols,
					RptOlapConsts.SORT_THIS_PERIOD, olapFun);

			rightSecRow.append("<td nowrap align=\"right\" class=\"")
					.append(tdClass).append("\">\n");
			rightSecRow.append(html);
			// 这里判断一下
			int colNameLen = RptOlapStringUtil.getHTMLStrLen(colName);
			int htmlLen = RptOlapStringUtil.getHTMLStrLen(html);
			if (htmlLen < (colNameLen / 2)) {
				try {
					byte[] bytes = colName.getBytes("GBK");
					html = new String(bytes, 0, bytes.length / 2);
				} catch (UnsupportedEncodingException uee) {

				}
			}
			tCol.setColHeadHTML2(html);
			rightSecRow.append("</td>");

			secExpRow.append("<td nowrap align=\"right\">\n");
			secExpRow.append("本期");
			secExpRow.append("</td>");

			String tmpName = "";
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate))
				tmpName = "累计";
			if (RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
				if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod)
						&& (RptOlapConsts.SAME_RATIO_BOTH_PERIOD.equals(olapFun
								.getSameRatioPeriod()) || RptOlapConsts.SAME_RATIO_WEEK_PERIOD
								.equals(olapFun.getSameRatioPeriod()))) {
					tmpName = "周同比";
				} else {
					tmpName = "同比";
				}
			}
			if (RptOlapConsts.OLAP_FUN_LAST.equals(func))
				tmpName = "环比";
			if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func))
				tmpName = "占比";

			html = "";
			html += tmpName;

			if (RptOlapConsts.OLAP_FUN_DATA.equals(func)
					&& RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate))
				html += RptOlapDimUtil.genSortLink(tCol, tableCols,
						RptOlapConsts.SORT_THIS_SUM, olapFun);
			else if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func)) {
				html += RptOlapDimUtil.genSortLink(tCol, tableCols,
						RptOlapConsts.SORT_THIS_PERIOD, olapFun);
			} else {
				html += RptOlapDimUtil.genSortLink(tCol, tableCols,
						RptOlapConsts.SORT_THIS_RATIO, olapFun);
			}
			if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && RptOlapConsts.YES
					.equalsIgnoreCase(rptMsu.is_sumbydate))
					|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
					|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
					|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
				rightSecRow.append("<td nowrap align=\"right\" class=\"")
						.append(tdClass).append("\">\n");
				rightSecRow.append(html);
				htmlLen = RptOlapStringUtil.getHTMLStrLen(html);
				if (htmlLen < (colNameLen / 2)) {
					try {
						byte[] bytes = colName.getBytes("GBK");
						html = new String(bytes, 0, bytes.length / 2);
					} catch (UnsupportedEncodingException uee) {

					}
				}
				tCol.setColHeadHTML3(html);
				rightSecRow.append("</td>");

				secExpRow.append("<td nowrap align=\"right\">\n");
				secExpRow.append(tmpName);
				secExpRow.append("</td>");

				if (RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
					if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod)
							&& (RptOlapConsts.SAME_RATIO_BOTH_PERIOD
									.equals(olapFun.getSameRatioPeriod()))) {
						tmpName = "月同比";
						html = "";
						html += tmpName;
						html += RptOlapDimUtil.genSortLink(tCol, tableCols,
								RptOlapConsts.SORT_THIS_WEEK_RATIO, olapFun);
						rightSecRow
								.append("<td nowrap align=\"right\" class=\"")
								.append(tdClass).append("\">\n");
						rightSecRow.append(html);
						tCol.setColHeadHTML3(html);
						rightSecRow.append("</td>");

						secExpRow.append("<td nowrap align=\"right\">\n");
						secExpRow.append(tmpName);
						secExpRow.append("</td>");
					}
				}
			}
		} else {
			// 单层
			String html = "";
			// 考虑指标钻取
			rightFirRow.append("<td nowrap align=\"center\" ")
					.append("class=\"").append(tdClass).append("\">");
			html = RptOlapMsuUtil.genMsuHeadContent(tCol, tableCols, olapFun,
					tCol.getColName());

			html += RptOlapDimUtil.genSortLink(tCol, tableCols,
					RptOlapConsts.SORT_THIS_PERIOD, olapFun);
			rightFirRow.append(html);
			tCol.setColHeadHTML1(html);
			rightFirRow.append("</td>");

			firExpRow.append("<td nowrap align=\"center\">");
			firExpRow.append(tCol.getColName());
			firExpRow.append("</td>");
		}
		right.add(rightFirRow);
		right.add(rightSecRow);
		right.add(firExpRow);
		right.add(secExpRow);
		return right;
	}
}
