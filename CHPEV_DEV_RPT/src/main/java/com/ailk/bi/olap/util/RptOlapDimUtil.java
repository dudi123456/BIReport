package com.ailk.bi.olap.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapTimeTable;
import com.ailk.bi.base.table.RptOlapUserDimTable;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapDimUtil {
	public static List adjustUserDims(List rptDims, List userCustomDims) {
		List userDims = rptDims;
		if (null == userCustomDims || userCustomDims.size() == 0) {
			return userDims;
		}
		Iterator iter = userCustomDims.iterator();
		RptOlapUserDimTable userDim = null;
		Map<String, RptOlapUserDimTable> mUserCustomDims = new HashMap<String, RptOlapUserDimTable>();
		while (iter.hasNext()) {
			userDim = (RptOlapUserDimTable) iter.next();
			mUserCustomDims.put(userDim.dim_id, userDim);
		}
		iter = userDims.iterator();
		RptOlapDimTable rptDim = null;
		Object obj = null;
		int i = 1;
		while (iter.hasNext()) {
			rptDim = (RptOlapDimTable) iter.next();
			obj = null;
			obj = mUserCustomDims.get(rptDim.dim_id);
			if (null != obj) {
				rptDim.default_display = RptOlapConsts.YES;
				rptDim.display_order = ((RptOlapUserDimTable) obj).display_order;
				i++;
			} else {
				rptDim.default_display = RptOlapConsts.NO;
			}
		}
		// 还得设置
		iter = userDims.iterator();
		while (iter.hasNext()) {
			rptDim = (RptOlapDimTable) iter.next();
			if (rptDim.default_display.equalsIgnoreCase(RptOlapConsts.NO)) {
				rptDim.display_order = "" + i++;
			}
		}
		// 排序
		Collections.sort(userDims, new RptOlapDimComparator());
		return userDims;
	}

	public static String getDimRightSQL(RptOlapDimTable rptDim,
			String dataTabVirName, UserCtlRegionStruct userCtl)
			throws ReportOlapException {
		if (null == rptDim || null == dataTabVirName)
			throw new ReportOlapException("生成维度的权限FROM部分时输入的参数为空");
		StringBuffer joinSQL = new StringBuffer();
		if (null == userCtl)
			return joinSQL.toString();
		if (RptOlapConsts.DATA_RIGHT_BELONG_AREA.equals(userCtl.ctl_flag)
				&& RptOlapConsts.DATA_RIGHT_LEVEL_PROVINCE
						.equals(userCtl.ctl_lvl))
			return joinSQL.toString();
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		String virTabNamePrefix = "D_R_";

		String ctlLevel = userCtl.ctl_lvl;
		List levels = rptDim.dimInfo.dim_levels;
		if ((null == levels || 0 >= levels.size() || RptOlapConsts.ZERO_STR
				.equals(rptDim.max_level)
				&& !RptOlapConsts.ZERO_STR.equals(ctlLevel)))
			throw new ReportOlapException("数据粒度没有达到您控制的级别");
		// 上面的情况下没法组成SQL语句
		// 如果是4层结构，或者5层结构
		// 得逐层判断，怎么对应呢,
		// 判断层次水平的数目
		// 这里需要克隆所有的层次水平
		List cloneLevs = new ArrayList();
		Iterator iter = levels.iterator();
		while (iter.hasNext()) {
			DimLevelTable levObj = (DimLevelTable) iter.next();
			DimLevelTable cloneObj = (DimLevelTable) levObj.clone();
			cloneLevs.add(cloneObj);
		}
		String maxLevel = rptDim.max_level;
		DimLevelTable[] levs = (DimLevelTable[]) cloneLevs
				.toArray(new DimLevelTable[levels.size()]);
		// 加上最上面，是5层
		String virTabName = "D_R_" + maxLevel;
		joinSQL.append(" JOIN(SELECT ");
		// 需要选择出最细粒度的code,以便和数据表关联
		String coldFld = RptOlapDimUtil.getDimDataFld(rptDim);
		coldFld = RptOlapStringUtil.replaceVirTabName(coldFld, virTabName);
		// 只选择最细速据粒度就可以了
		joinSQL.append(coldFld).append(" FROM ");
		// 找到最后一个
		int ctlLev = Integer.parseInt(ctlLevel);
		ctlLev--;
		int init = 0;
		int to = 0;
		for (int i = 0; i < levs.length; i++) {
			if (levs[i].lvl_id.equals(maxLevel))
				init = i;
			if (levs[i].lvl_id.equals(ctlLev + ""))
				// 如果控制级别是0
				to = i;
		}
		// 开始关联
		for (int i = init; i >= to; i--) {
			String tabName = virTabNamePrefix + levs[i].lvl_id;
			joinSQL.append(levs[i].lvl_table).append(" ").append(tabName)
					.append(",");
			if (i != init) {
				String preTabName = virTabNamePrefix + levs[i + 1].lvl_id;
				where.append(" AND ").append(preTabName).append(".")
						.append(levs[i + 1].parent_idfld).append("=")
						.append(tabName).append(".").append(levs[i].code_idfld);
			}
			if (ctlLev != 0 && i == to && !StringUtils.isEmpty(userCtl.ctl_str)) {
				where.append(" AND ").append(tabName).append(".")
						.append(levs[i].code_idfld);
				String inOrEqual = userCtl.ctl_in_or_equals;
				where.append(" ");
				inOrEqual = inOrEqual.toUpperCase();
				where.append(inOrEqual);

				if (inOrEqual.equals("IN")) {
					where.append("(");
					where.append(userCtl.ctl_str);
					where.append(")");
				} else
					where.append(userCtl.ctl_str);
			}
		}
		// 如果
		if (ctlLev == 0) {
			joinSQL.append(rptDim.dimInfo.dim_table).append(" ")
					.append(virTabNamePrefix + "0").append(",");
			where.append(" AND ").append(virTabNamePrefix + levs[0].lvl_id)
					.append(".").append(levs[0].parent_idfld).append("=")
					.append(virTabNamePrefix + "0.")
					.append(rptDim.dimInfo.code_idfld);
			if (userCtl.ctl_str != null && !"".equals(userCtl.ctl_str.trim())) {
				where.append(" AND ").append(virTabNamePrefix + "0")
						.append(".").append(rptDim.dimInfo.code_idfld);

				String inOrEqual = userCtl.ctl_in_or_equals;
				where.append(" ");
				inOrEqual = inOrEqual.toUpperCase();
				where.append(inOrEqual);
				if (inOrEqual.equals("IN")) {
					where.append("(");
					where.append(userCtl.ctl_str);
					where.append(")");
				} else
					where.append(userCtl.ctl_str);
			}
		}
		// 这里还没有加上用户的条件

		String tmpStr = joinSQL.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		joinSQL.delete(0, joinSQL.length());
		joinSQL.append(tmpStr);
		joinSQL.append(where);
		joinSQL.append(") ");
		joinSQL.append(virTabName).append(" ON ");
		coldFld = RptOlapStringUtil.replaceVirTabName(coldFld, dataTabVirName);
		joinSQL.append(coldFld).append("=");
		coldFld = RptOlapStringUtil.replaceVirTabName(coldFld, virTabName);
		joinSQL.append(coldFld);
		return joinSQL.toString();
	}

	public static String getDimDataFld(RptOlapDimTable rptDim)
			throws ReportOlapException {
		if (null == rptDim)
			throw new ReportOlapException("获取维度的最细数据粒度字段名称时输入的参数为空");
		String codeFld = null;
		List levels = rptDim.dimInfo.dim_levels;
		if (null == levels || 0 >= levels.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			codeFld = rptDim.dimInfo.code_idfld;
		} else {
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				DimLevelTable levStruct = (DimLevelTable) iter.next();
				if (rptDim.max_level.equals(levStruct.lvl_id)) {
					codeFld = levStruct.code_idfld;
					break;
				}
			}
		}
		return codeFld;
	}

	private static List getChartSelectFld(RptOlapChartAttrStruct rptStruct)
			throws ReportOlapException {
		if (null == rptStruct)
			throw new ReportOlapException("获取维度的字段时输入的参数为空");
		List dimSelect = new ArrayList();
		String level = rptStruct.getLevel();
		RptOlapDimTable rptDim = (RptOlapDimTable) rptStruct.getRptStruct();
		List list = rptDim.dimInfo.dim_levels;
		if (null == list || 0 >= list.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)
				|| RptOlapConsts.ZERO_STR.equals(level)) {
			String dimFld = rptDim.dimInfo.code_idfld;
			String descFld = rptDim.dimInfo.code_descfld;
			dimSelect.add(dimFld);
			dimSelect.add(descFld);
			return dimSelect;
		}
		// 到这里了应该是多层次了
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			DimLevelTable lvlObj = (DimLevelTable) iter.next();
			if (level.equals(lvlObj.lvl_id)) {
				// 找到了
				String dimFld = lvlObj.code_idfld;
				String descFld = lvlObj.code_descfld;
				dimSelect.add(dimFld);
				dimSelect.add(descFld);
				break;
			}
		}
		return dimSelect;
	}

	public static String getChartFromPart(RptOlapChartAttrStruct rptStruct)
			throws ReportOlapException {
		if (null == rptStruct)
			throw new ReportOlapException("获取维度的字段时输入的参数为空");
		String from = "";
		String level = rptStruct.getLevel();
		RptOlapDimTable rptDim = (RptOlapDimTable) rptStruct.getRptStruct();
		List list = rptDim.dimInfo.dim_levels;
		String virName = "D" + rptStruct.getIndex() + "_" + level;
		if (null == list || 0 >= list.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {

			from = rptDim.dimInfo.dim_table + " " + virName + ",";
			return from;
		}
		// 将list转换为数组
		DimLevelTable[] levels = (DimLevelTable[]) list
				.toArray(new DimLevelTable[list.size()]);
		int maxLevel = Integer.parseInt(rptDim.max_level);
		int realMaxLevel = Integer.parseInt(levels[levels.length - 1].lvl_id);
		if (maxLevel > realMaxLevel)
			maxLevel = realMaxLevel;
		int init = 0;
		int min = 0;
		for (int i = 0; i < levels.length; i++) {
			if ((maxLevel + "").equals(levels[i].lvl_id)) {
				init = i;
			}
			if (level.equals(levels[i].lvl_id)) {
				min = i;
			}
		}
		from = "";
		for (int i = init; i >= min; i--) {
			from += levels[i].lvl_table;
			from += " D" + rptStruct.getIndex() + "_" + (i + 1) + ",";
		}
		// 如果是第0层
		if (RptOlapConsts.ZERO_STR.equals(level)) {
			from += rptDim.dimInfo.dim_table;
			from += " D" + rptStruct.getIndex() + "_" + RptOlapConsts.ZERO_STR
					+ ",";
		}
		return from;
	}

	private static String getChartMinLevelCodeFld(
			RptOlapChartAttrStruct rptStruct) throws ReportOlapException {
		if (null == rptStruct)
			throw new ReportOlapException("生成图形分析查询语句的关联部分是输入的参数为空");
		String codeFld = "";
		String level = rptStruct.getLevel();
		RptOlapDimTable rptDim = (RptOlapDimTable) rptStruct.getRptStruct();
		List list = rptDim.dimInfo.dim_levels;
		String prefix = "D" + rptStruct.getIndex() + "_" + level;
		if (null == list || 0 >= list.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			codeFld = prefix + "." + rptDim.dimInfo.code_idfld;
			return codeFld;
		} else {
			DimLevelTable[] levels = (DimLevelTable[]) list
					.toArray(new DimLevelTable[list.size()]);
			int maxLevel = Integer.parseInt(rptDim.max_level);
			int realMaxLevel = Integer
					.parseInt(levels[levels.length - 1].lvl_id);
			if (maxLevel > realMaxLevel)
				maxLevel = realMaxLevel;
			int init = 0;
			for (int i = 0; i < levels.length; i++) {
				if ((maxLevel + "").equals(levels[i].lvl_id)) {
					init = i;
					break;
				}
			}
			prefix = "D" + rptStruct.getIndex() + "_" + levels[init].lvl_id;
			codeFld = prefix + "." + levels[init].code_idfld;
		}
		return codeFld;
	}

	private static String getChartWherePart(RptOlapChartAttrStruct rptStruct,
			String virTabName) throws ReportOlapException {
		if (null == rptStruct)
			throw new ReportOlapException("生成图形分析查询语句的条件部分是输入的参数为空");
		String where = "";
		String level = rptStruct.getLevel();
		RptOlapDimTable rptDim = (RptOlapDimTable) rptStruct.getRptStruct();
		List list = rptDim.dimInfo.dim_levels;
		if (null == list || 0 >= list.size()
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			List values = rptStruct.getCurValues();
			if (rptStruct.isUseAllValues() || null == values
					|| 0 >= values.size()) {
				// 没有限制，那么全部选择,
			} else {
				// 有值，要加上
				where += " AND ";
				String prefix = "D" + rptStruct.getIndex() + "_" + level;
				String codeFld = prefix + "." + rptDim.dimInfo.code_idfld;
				String fldType = rptDim.dimInfo.idfld_type;
				if (rptStruct.isIncludeValues())
					where += codeFld + " IN(";
				else
					where += codeFld + " NOT IN(";
				Iterator iter = values.iterator();
				while (iter.hasNext()) {
					String value = (String) iter.next();
					if (RptOlapConsts.FLD_STRING_TYPE.equals(fldType))
						value = "'" + value + "'";
					where += value + ",";
				}
				// 去掉最后一个
				where = RptOlapStringUtil.removeLastSubStr(where, ",");
				where += ")";
			}
			return where;
		}
		// 这是可能当前是第0层，也可能是其他层，但肯定有其他层次
		DimLevelTable[] levels = (DimLevelTable[]) list
				.toArray(new DimLevelTable[list.size()]);
		int maxLevel = Integer.parseInt(rptDim.max_level);
		int realMaxLevel = Integer.parseInt(levels[levels.length - 1].lvl_id);
		if (maxLevel > realMaxLevel)
			maxLevel = realMaxLevel;
		int init = 0;
		int min = 0;
		for (int i = 0; i < levels.length; i++) {
			if ((maxLevel + "").equals(levels[i].lvl_id)) {
				init = i;
			}
			if (level.equals(levels[i].lvl_id)) {
				min = i;
			}
		}
		String prefix = "";
		String codeFld = "";
		String parentFld = "";
		String fldType = "";
		for (int i = init; i >= min; i--) {
			prefix = " D" + rptStruct.getIndex() + "_" + (i + 1);
			codeFld = levels[i].code_idfld;
			parentFld = levels[i].parent_idfld;
			fldType = levels[i].id_fldtype;
			if (i == init) {
				// 关联数据表和维表
			} else {
				// 本层关联上层
				String upPrefix = " D" + rptStruct.getIndex() + "_" + (i + 2);
				where += " AND " + prefix + "." + levels[i + 1].parent_idfld
						+ "=" + upPrefix + "." + levels[i].code_idfld;
			}
		}
		// 如果是第0层
		if (RptOlapConsts.ZERO_STR.equals(level)) {

			where += " AND D" + rptStruct.getIndex() + "_0" + "."
					+ rptDim.dimInfo.code_idfld + "=" + prefix + "."
					+ parentFld;
			prefix = " D" + rptStruct.getIndex() + "_0";
			codeFld = rptDim.dimInfo.code_idfld;
			fldType = rptDim.dimInfo.idfld_type;
		}
		// 看看有值没有
		List values = rptStruct.getCurValues();
		if (rptStruct.isUseAllValues() || null == values || 0 >= values.size()) {
			// 没有或者使用全部,选择全部时也应该如此
		} else {
			// 此时选择了其他
			where += " AND ";
			if (rptStruct.isIncludeValues())
				where += prefix + "." + codeFld + " IN(";
			else
				where += prefix + "." + codeFld + " NOT IN(";
			Iterator iter = values.iterator();
			while (iter.hasNext()) {
				String value = (String) iter.next();
				if (RptOlapConsts.FLD_STRING_TYPE.equals(fldType))
					value = "'" + value + "'";
				where += value + ",";
			}
			// 去掉最后一个
			where = RptOlapStringUtil.removeLastSubStr(where, ",");
			where += ")";
		}
		return where;
	}

	public static Map genChartSelectParts(RptOlapChartAttrStruct rptStruct,
			String virTabName) throws ReportOlapException {
		if (null == rptStruct)
			throw new ReportOlapException("组装维度的查询语句各部分时输入的参数为空");
		Map parts = new HashMap();
		StringBuffer select = new StringBuffer();
		StringBuffer subSelect = new StringBuffer();
		StringBuffer joinSQL = new StringBuffer();
		StringBuffer from = new StringBuffer();
		StringBuffer where = new StringBuffer();
		StringBuffer groupby = new StringBuffer();
		StringBuffer orderby = new StringBuffer();

		RptOlapDimTable rptDim = (RptOlapDimTable) rptStruct.getRptStruct();
		// 开始分析
		if (rptStruct.isUsedForGroup()) {
			// 获取
			List list = getChartSelectFld(rptStruct);
			// 有可能找不到
			if (list.size() >= 2) {
				String prefix = "D" + rptStruct.getIndex() + "_"
						+ rptStruct.getLevel();
				String codeFld = (String) list.get(0);
				String descFld = (String) list.get(1);
				String minCodeFld = getChartMinLevelCodeFld(rptStruct);
				codeFld = RptOlapStringUtil.replaceVirTabName(codeFld, prefix);
				descFld = RptOlapStringUtil.replaceVirTabName(descFld, prefix);
				List values = rptStruct.getCurValues();
				if (null == values || 0 >= values.size())
					joinSQL.append(" LEFT OUTER JOIN (SELECT ");
				else
					joinSQL.append(" JOIN (SELECT ");
				joinSQL.append(codeFld).append(",");
				joinSQL.append(descFld);
				String tmpStr1 = codeFld;
				String tmpStr2 = minCodeFld;
				if (tmpStr1.indexOf(".") >= 0) {
					tmpStr1 = tmpStr1.substring(tmpStr1.indexOf(".") + 1);
				}
				if (tmpStr2.indexOf(".") >= 0) {
					tmpStr2 = tmpStr2.substring(tmpStr2.indexOf(".") + 1);
				}
				tmpStr1 = tmpStr1.trim();
				tmpStr2 = tmpStr2.trim();
				if (!tmpStr1.equals(tmpStr2)) {
					joinSQL.append(",");
					joinSQL.append(minCodeFld);
				}
				String tmpFrom = getChartFromPart(rptStruct);
				tmpFrom = RptOlapStringUtil.removeLastSubStr(tmpFrom, ",");
				joinSQL.append(" FROM ").append(tmpFrom);
				joinSQL.append(" WHERE 1=1 ").append(
						getChartWherePart(rptStruct, virTabName));
				String tmpFld = RptOlapStringUtil.replaceVirTabName(minCodeFld,
						virTabName);
				minCodeFld = RptOlapStringUtil.replaceVirTabName(minCodeFld,
						prefix);
				joinSQL.append(") ").append(prefix).append(" ON ")
						.append(tmpFld).append("=").append(minCodeFld);
				List levels = rptDim.dimInfo.dim_levels;
				if (null == levels || 0 >= levels.size()
						|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level))
					codeFld = RptOlapStringUtil.replaceVirTabName(codeFld,
							virTabName);
				// 最外层
				select.append(codeFld).append(",");
				select.append(descFld).append(",");
				subSelect.append(tmpFld).append(",");
				groupby.append(tmpFld).append(",");
				orderby.append(tmpFld).append(",");
			}
		} else {
			List values = rptStruct.getCurValues();
			if (null != values && 0 < values.size()) {
				List list = getChartSelectFld(rptStruct);
				// 有可能找不到
				if (list.size() >= 2) {
					String prefix = "D" + rptStruct.getIndex() + "_"
							+ rptStruct.getLevel();
					String codeFld = (String) list.get(0);
					String descFld = (String) list.get(1);
					String minCodeFld = getChartMinLevelCodeFld(rptStruct);
					codeFld = RptOlapStringUtil.replaceVirTabName(codeFld,
							prefix);
					descFld = RptOlapStringUtil.replaceVirTabName(descFld,
							prefix);
					joinSQL.append(" JOIN (SELECT ");
					joinSQL.append(codeFld).append(",");
					joinSQL.append(descFld);
					String tmpStr1 = codeFld;
					String tmpStr2 = minCodeFld;
					if (tmpStr1.indexOf(".") >= 0) {
						tmpStr1 = tmpStr1.substring(tmpStr1.indexOf(".") + 1);
					}
					if (tmpStr2.indexOf(".") >= 0) {
						tmpStr2 = tmpStr2.substring(tmpStr2.indexOf(".") + 1);
					}
					tmpStr1 = tmpStr1.trim();
					tmpStr2 = tmpStr2.trim();
					if (!tmpStr1.equals(tmpStr2)) {
						joinSQL.append(",");
						joinSQL.append(minCodeFld);
					}
					String tmpFrom = getChartFromPart(rptStruct);
					tmpFrom = RptOlapStringUtil.removeLastSubStr(tmpFrom, ",");
					joinSQL.append(" FROM ").append(tmpFrom);
					joinSQL.append(" WHERE 1=1 ").append(
							getChartWherePart(rptStruct, virTabName));
					String tmpFld = RptOlapStringUtil.replaceVirTabName(
							minCodeFld, virTabName);
					minCodeFld = RptOlapStringUtil.replaceVirTabName(
							minCodeFld, prefix);
					joinSQL.append(") ").append(prefix).append(" ON ")
							.append(tmpFld).append("=").append(minCodeFld);
					subSelect.append(tmpFld).append(",");
					groupby.append(tmpFld).append(",");
				}
			}
		}
		parts.put("select", select);
		parts.put("subselect", subSelect);
		parts.put("join", joinSQL);
		parts.put("from", from);
		parts.put("where", where);
		parts.put("groupby", groupby);
		parts.put("orderby", orderby);
		return parts;
	}

	public static String getExpandPreLevel(String curLevel, List tableCols)
			throws ReportOlapException {
		if (null == curLevel || "".equals(curLevel) || null == tableCols
				|| 0 >= tableCols.size())
			throw new ReportOlapException("获取折叠展开当前层次的上一层次水平时输入的参数为空");
		String preLevel = null;
		int iCurlevel = Integer.parseInt(curLevel);
		iCurlevel--;
		preLevel = iCurlevel + "";
		// 下面应该是针对时间是多层次情况而设定
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay() && tCol.isDim() && tCol.isTimeDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				if (!RptOlapConsts.ZERO_STR.equals(rptDim.max_level)
						&& null != rptDim.timeLevel) {
					String level = tCol.getDigLevel();
					tCol.setDigLevel(curLevel);
					preLevel = getDimTimePreLevel(tCol);
					tCol.setDigLevel(level);
					break;
				}
			}
		}
		return preLevel;
	}

	/**
	 * 获取时间维的最大层次名
	 *
	 * @param rptDim
	 * @return
	 */
	public static String getTimeTopLevelName(RptOlapDimTable rptDim) {
		String levelName = "";
		if (null != rptDim) {
			RptOlapTimeTable time = rptDim.timeLevel;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_year))
				return RptOlapConsts.BY_YEAR;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_quarter))
				return RptOlapConsts.BY_QUARTER;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_month))
				return RptOlapConsts.BY_MONTH;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_tendays))
				return RptOlapConsts.BY_TENDAYS;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_week))
				return RptOlapConsts.BY_WEEK;
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_day))
				return RptOlapConsts.BY_DAY;
			return rptDim.dimInfo.dim_name;
		}
		return levelName;
	}

	public static List genTimeNonTopLevelNames(RptOlapDimTable rptDim) {
		List names = new ArrayList();
		if (null != rptDim) {
			String topLevel = getTimeTopLevelName(rptDim);
			RptOlapTimeTable time = rptDim.timeLevel;
			String levelName = "";
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_year)) {
				levelName = RptOlapConsts.BY_YEAR;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_quarter)) {
				levelName = RptOlapConsts.BY_QUARTER;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_month)) {
				levelName = RptOlapConsts.BY_MONTH;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_tendays)) {
				levelName = RptOlapConsts.BY_TENDAYS;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_week)) {
				levelName = RptOlapConsts.BY_WEEK;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(time.by_day)) {
				levelName = RptOlapConsts.BY_DAY;
				if (!topLevel.equals(levelName))
					names.add(levelName);
			}
		}
		return names;
	}

	/**
	 * @param tableCols
	 * @param tCol
	 * @param olapFun
	 * @param dimRowSpan
	 * @param tdClass
	 * @return
	 * @throws ReportOlapException
	 */
	public static List genDimHead(List tableCols,
			RptOlapTableColumnStruct tCol, RptOlapFuncStruct olapFun,
			int dimRowSpan, String tdClass) throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size() || null == tCol
				|| null == olapFun)
			throw new ReportOlapException("生成维度表头时输入的参数为空");

		List dimHead = new ArrayList();
		StringBuffer firRow = new StringBuffer("");
		StringBuffer firExpRow = new StringBuffer("");
		firRow.append("<td rowspan=\"").append(dimRowSpan)
				.append("\" nowrap align=\"left\" class=\"").append(tdClass)
				.append("\">\n");
		firExpRow.append("<td rowspan=\"").append(dimRowSpan)
				.append("\" nowrap align=\"left\">\n");
		String html = "";
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		boolean needDig = rptDim.need_dig.equalsIgnoreCase(RptOlapConsts.YES);
		if (needDig
				&& !RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {
			// 增加收缩链接
			html = RptOlapDimUtil.genCollapseInitLink(tCol, tableCols, olapFun);
		}
		html += tCol.getColName();
		if (needDig
				&& !RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {
			// 生成单层收缩
			String tmpStr = RptOlapDimUtil.genCollapseUpLink(tCol, tableCols,
					olapFun);
			html += tmpStr;
		}
		firRow.append(html);
		firExpRow.append(tCol.getColName());
		tCol.setColHeadHTML1(html);
		firRow.append("</td>");
		firExpRow.append("</td>");
		dimHead.add(firRow);
		dimHead.add(firExpRow);
		return dimHead;
	}

	/**
	 * 判断是否可以折叠、展开模式
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	public static boolean canExpandMode(List tableCols) {
		boolean expandMode = true;
		if (null == tableCols)
			return false;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay() && tCol.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				if (null != rptDim.dimInfo.dim_levels) {
					expandMode = false;
					break;
				}
			}
		}
		return expandMode;
	}

	/**
	 * 生成维度收缩链接
	 *
	 * @param tCol
	 *            列域对象
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @return
	 */
	public static String genCollapseInitLink(RptOlapTableColumnStruct tCol,
			List tableCols, RptOlapFuncStruct olapFun)
			throws ReportOlapException {
		if (null == tCol || null == tableCols || 0 >= tableCols.size()
				|| null == olapFun)
			throw new ReportOlapException("生成维度收缩到初始链接输入的参数为空");
		StringBuffer link = new StringBuffer("");
		link.append(RptOlapConsts.OLAP_DIG_ACTION);
		link.append("?report_id=::report_id::");
		link.append(RptOlapTabDomainUtil.convertTableColStructToUrl(tableCols,
				olapFun));
		link.append("&").append(RptOlapConsts.REQ_COLLAPSE_DIM_INIT)
				.append("=").append(tCol.getIndex());
		String tmpStr = link.toString();
		link.delete(0, link.length());
		link.append("<a href=\"javascript:")
				.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION).append("('")
				.append(tmpStr).append("')\"><img ")
				.append("src=\"../images/shrink_init.gif\" ")
				.append("width=\"7\" height=\"11\" border=\"0\"></a>");
		return link.toString();
	}

	public static String genCollapseUpLink(RptOlapTableColumnStruct tCol,
			List tableCols, RptOlapFuncStruct olapFun)
			throws ReportOlapException {
		if (null == tCol || null == tableCols || 0 >= tableCols.size()
				|| null == olapFun)
			throw new ReportOlapException("生成维度收缩到初始链接输入的参数为空");

		StringBuffer link = new StringBuffer("");
		link.append(RptOlapConsts.OLAP_DIG_ACTION);
		link.append("?report_id=::report_id::");
		link.append(RptOlapTabDomainUtil.convertTableColStructToUrl(tableCols,
				olapFun));
		link.append("&").append(RptOlapConsts.REQ_COLLAPSE_DIM).append("=")
				.append(tCol.getIndex());
		String tmpStr = link.toString();
		link.delete(0, link.length());
		link.append("<a href=\"javascript:")
				.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION).append("('")
				.append(tmpStr).append("')\"><img ")
				.append("src=\"../images/shrink_up.gif\" ")
				.append("width=\"7\" height=\"11\" border=\"0\"></a>");
		return link.toString();
	}

	/**
	 * 生成排序链接
	 *
	 * @param tCol
	 *            列域对象
	 * @param tableCols
	 *            列域对象列表
	 * @param sortThis
	 *            是否排序本期值
	 * @param olapFun
	 *            当前功能对象
	 * @return
	 */
	public static String genSortLink(RptOlapTableColumnStruct tCol,
			List tableCols, String sortIndex, RptOlapFuncStruct olapFun)
			throws ReportOlapException {
		if (null == tCol || null == tableCols || null == olapFun
				|| 0 >= tableCols.size() || null == sortIndex)
			throw new ReportOlapException("生成排序链接时输入的参数为空");
		StringBuffer link = new StringBuffer("");
		link.append(RptOlapConsts.OLAP_DIG_ACTION);
		link.append("?report_id=::report_id::").append(
				RptOlapTabDomainUtil.convertTableColStructToUrl(tableCols,
						olapFun));
		link.append("&").append(RptOlapConsts.REQ_SORT).append("=")
				.append(tCol.getIndex());
		String sort = olapFun.getSort();
		String sortOrder = olapFun.getSortOrder();
		String sortThis = olapFun.getSortThis();
		String imgName = RptOlapConsts.SORT_INIT_IMG;
		if (null == sort) {
			link.append("&").append(RptOlapConsts.REQ_SORT_ORDER).append("=")
					.append(RptOlapConsts.SORT_DESC);
		} else {
			if (sort.equals(tCol.getIndex() + "") && sortThis.equals(sortIndex)) {
				if (RptOlapConsts.SORT_ASC.equals(sortOrder)) {
					link.append("&").append(RptOlapConsts.REQ_SORT_ORDER)
							.append("=").append(RptOlapConsts.SORT_DESC);
					imgName = RptOlapConsts.SORT_ASC_IMG;
				} else {
					link.append("&").append(RptOlapConsts.REQ_SORT_ORDER)
							.append("=").append(RptOlapConsts.SORT_ASC);
					imgName = RptOlapConsts.SORT_DESC_IMG;
				}
			} else {
				link.append("&").append(RptOlapConsts.REQ_SORT_ORDER)
						.append("=").append(RptOlapConsts.SORT_DESC);
			}
		}
		link.append("&").append(RptOlapConsts.REQ_SORT_THIS).append("=")
				.append(sortIndex);
		String tmpStr = link.toString();
		link.delete(0, link.length());
		link.append("<a href=\"javascript:")
				.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION).append("('")
				.append(tmpStr).append("')\"><img ").append("src=\"../images/")
				.append(imgName).append("\" ")
				.append(" width=\"9\" border=\"0\"></a>");
		return link.toString();
	}

	/**
	 * 生成外连接的ON部分
	 *
	 * @param select
	 *            查询语句
	 * @param firVirTabName
	 *            第一个别名
	 * @param secVirTabName
	 *            第二个别名
	 * @return
	 */
	public static String genLeftJoinOn(String select, String firVirTabName,
			String secVirTabName) throws ReportOlapException {
		if (null == select || null == firVirTabName || null == secVirTabName)
			throw new ReportOlapException("生成查询语句的外连接部分时输入的参数为空");
		StringBuffer leftJoinOn = new StringBuffer("");
		if (null != select && null != firVirTabName && null != secVirTabName) {
			String subSelect = select;
			// 去除SELECT
			subSelect = subSelect.replaceAll("SELECT ", "");
			String[] dims = subSelect.split(",");
			for (int i = 0; i < dims.length; i++) {
				String temp = dims[i];
				// 看看是不是带有SUM标志
				if (temp.indexOf("SUM(") < 0) {
					// 是维度
					temp = RptOlapStringUtil.clearVirTabName(temp);
					leftJoinOn.append(" AND ").append(firVirTabName)
							.append(".").append(temp).append("=")
							.append(secVirTabName).append(".").append(temp);
				}
			}
		}
		// 去掉第一个 AND
		String tmpStr = leftJoinOn.toString();
		int pos = tmpStr.indexOf(" AND ");
		if (pos >= 0) {
			leftJoinOn.delete(0, leftJoinOn.length());
			leftJoinOn.append(tmpStr.substring(pos + " AND ".length()));
		}
		return leftJoinOn.toString();
	}

	/**
	 * 生成维度的查询语句各部分
	 *
	 * @param tCol
	 *            标识当前维度的列域对象
	 * @return SQL的各部分
	 */
	public static Map genDimSelectParts(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, String statPeriod, RptOlapDateStruct ds)
			throws ReportOlapException {
		if (null == tCol || null == olapFun || null == statPeriod
				|| "".equals(statPeriod) || null == ds)
			throw new ReportOlapException("生成查询语句的维度各部分输入参数为空");
		Map part = null;
		part = new HashMap();
		StringBuffer select = new StringBuffer("");
		StringBuffer subSelect = new StringBuffer("");
		StringBuffer joinSQL = new StringBuffer(" JOIN( SELECT ");
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		StringBuffer groupby = new StringBuffer("");
		StringBuffer orderby = new StringBuffer("");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		// 还没有点击开，其他部分应该不用增加
		String prefix = "";

		prefix = "D" + tCol.getIndex() + "_" + tCol.getDigLevel();
		if (RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {
			select.append("'").append(tCol.getIndex()).append("',");
			select.append("'").append(tCol.getIndex()).append("',");
		} else if (!tCol.isNeedDig()
				|| (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())
						&& RptOlapConsts.ZERO_STR.equals(rptDim.max_level) && null == rptDim.dimInfo.dim_levels)
				|| (!tCol.isTimeDim() && rptDim.dimInfo.dim_levels.size() <= 0)) {
			// 最后主表的选择
			if (!tCol.isTimeDim()) {
				select.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append(",");
			} else {
				select.append(RptOlapConsts.TIME_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append(",");
			}
			select.append(prefix).append(".")
					.append(rptDim.dimInfo.code_descfld).append(",");
			// 关联表的选择
			joinSQL.append(prefix).append(".")
					.append(rptDim.dimInfo.code_idfld).append(",");
			joinSQL.append(prefix).append(".")
					.append(rptDim.dimInfo.code_descfld).append(" ");
			// 数据表的选择
			subSelect.append(RptOlapConsts.THIS_VIR_TMP_TAB_NAME).append(".")
					.append(rptDim.dimInfo.code_idfld).append(",");
			// joinSQL继续补
			joinSQL.append(" FROM ").append(rptDim.dimInfo.dim_table)
					.append(" ").append(prefix);
			if (!tCol.isTimeDim()) {
				// 加上可能所带有的值
				where.append(getDimWhere(tCol, olapFun));
			} else {
				where.append(RptOlapDimUtil.getDimTimeWhere(tCol, olapFun,
						prefix, statPeriod, ds));
			}
			// 加上条件
			joinSQL.append(where);
			// 完成选择,条件关联
			joinSQL.append(" ) ").append(prefix).append(" ON ");
			if (!tCol.isTimeDim()) {
				joinSQL.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append("=")
						.append(prefix).append(".")
						.append(rptDim.dimInfo.code_idfld);
			} else {
				joinSQL.append(RptOlapConsts.TIME_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append("=")
						.append(prefix).append(".")
						.append(rptDim.dimInfo.code_idfld);
			}
			joinSQL.append(" ");
			// 主表的分组和排序
			if (!tCol.isTimeDim()) {
				groupby.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append(",");
			} else {
				groupby.append(RptOlapConsts.TIME_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append(",");
			}
			groupby.append(prefix).append(".")
					.append(rptDim.dimInfo.code_descfld).append(",");
			orderby.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
					.append(rptDim.dimInfo.code_idfld).append(",");
		} else {
			// 需要钻取,还有其他层次
			if (tCol.isTimeDim()) {
				// 时间维度,
				String codeIdFld = null;
				String codeDescFld = null;
				String level = tCol.getDigLevel();
				codeIdFld = getDimTimeFld(tCol, level, statPeriod);
				codeDescFld = getDimTimeDescFld(tCol, level);
				select.append(prefix).append(".").append(codeIdFld).append(",");
				select.append(prefix).append(".").append(codeDescFld)
						.append(",");
				// 数据表的选择
				subSelect.append(RptOlapConsts.THIS_VIR_TMP_TAB_NAME)
						.append(".").append(rptDim.dimInfo.code_idfld)
						.append(",");
				// 当达到最细粒度时就不需要单独加上一个了,这里使用两个字段相等判断，
				// 还有其他更好的方法吗?怎么判断到了最细粒度或者没有下一层
				String nextLevel = getDimTimeNextLevel(tCol);
				if (!RptOlapConsts.NO_NEXT_LEVEL.equals(nextLevel))
					joinSQL.append(prefix).append(".")
							.append(rptDim.dimInfo.code_idfld).append(",");
				joinSQL.append(prefix).append(".").append(codeIdFld)
						.append(",");
				joinSQL.append(prefix).append(".").append(codeDescFld)
						.append(" ");
				// joinSQL继续补
				joinSQL.append(" FROM ").append(rptDim.dimInfo.dim_table)
						.append(" ").append(prefix);
				// 加上可能所带有的值
				where.append(RptOlapDimUtil.getDimTimeWhere(tCol, olapFun,
						prefix, statPeriod, ds));
				// 加上条件
				joinSQL.append(where);
				// 完成选择,条件关联
				joinSQL.append(" ) ").append(prefix).append(" ON ");

				joinSQL.append(RptOlapConsts.TIME_VIR_TAB_NAME).append(".")
						.append(rptDim.dimInfo.code_idfld).append("=")
						.append(prefix).append(".")
						.append(rptDim.dimInfo.code_idfld);
				joinSQL.append(" ");
				// 主表的分组和排序

				groupby.append(prefix).append(".").append(codeIdFld)
						.append(",");
				groupby.append(prefix).append(".").append(codeDescFld)
						.append(",");
				orderby.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
						.append(codeIdFld).append(",");
			} else {
				// 需要钻取，且不是完全没有钻取状态,且有层次
				// from需要从最细粒度关联起来
				String codeIdFld = null;
				String codeDescFld = null;
				// 当前层次的字段名、描述字段
				codeIdFld = getDimIdFld(tCol);
				codeDescFld = getDimDescFld(tCol);
				// 取得最细粒度的字段，需要关联
				DimLevelTable minLevel = getDetailLevel(tCol);
				// 维表虚表名
				prefix = "D" + tCol.getIndex() + "_" + tCol.getDigLevel();
				// 主表选择当前维表的
				select.append(prefix).append(".").append(codeIdFld).append(",");
				select.append(prefix).append(".").append(codeDescFld)
						.append(",");
				// 数据表选择最细粒度
				subSelect.append(RptOlapConsts.THIS_VIR_TMP_TAB_NAME)
						.append(".").append(minLevel.code_idfld).append(",");

				// 构造维表查询语句
				String detailPrefix = "D" + tCol.getIndex() + "_"
						+ minLevel.lvl_id;
				// 最细粒度数据要选择出来，为了和数据表关联
				// 还有判断当前是否就是最细粒度了
				String level = tCol.getDigLevel();
				if (!RptOlapConsts.NO_NEXT_LEVEL.equals(level)
						&& !level.equals(minLevel.lvl_id))
					joinSQL.append(detailPrefix).append(".")
							.append(minLevel.code_idfld).append(",");
				// 要显示用的
				joinSQL.append(prefix).append(".").append(codeIdFld)
						.append(",");
				joinSQL.append(prefix).append(".").append(codeDescFld)
						.append(" ");
				// 加上各个维表关联
				joinSQL.append(" FROM ");
				joinSQL.append(getDimFrom(tCol, olapFun));
				where.append(getDimWhere(tCol, olapFun));
				joinSQL.append(where);

				// 完成选择,条件关联
				joinSQL.append(" ) ").append(prefix).append(" ON ");
				joinSQL.append(RptOlapConsts.THIS_VIR_TAB_NAME).append(".")
						.append(minLevel.code_idfld).append("=").append(prefix)
						.append(".").append(minLevel.code_idfld);
				joinSQL.append(" ");
				// 主表的分组和排序
				groupby.append(prefix).append(".").append(codeIdFld)
						.append(",");
				groupby.append(prefix).append(".").append(codeDescFld)
						.append(",");
				orderby.append(prefix).append(".").append(codeIdFld)
						.append(",");
			}
		}
		part.put("select", select.toString());
		part.put("subSelect", subSelect.toString());
		part.put("joinSQL", joinSQL.toString());
		part.put("groupby", groupby.toString());
		part.put("orderby", orderby.toString());
		return part;
	}

	private static DimLevelTable getDetailLevel(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取维度最细粒度的层次对象时输入的参数为空");
		DimLevelTable minLevel = null;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String maxLevel = rptDim.max_level;
		List levels = rptDim.dimInfo.dim_levels;
		Iterator iter = levels.iterator();
		while (iter.hasNext()) {
			// 保证即使补匹配也能找到最后一个
			minLevel = (DimLevelTable) iter.next();
			if (minLevel.lvl_id.equals(maxLevel)) {
				break;
			}
		}
		return minLevel;
	}

	/**
	 * 根据当前层次获取维度编码字段
	 *
	 * @param tCol
	 * @return
	 */
	private static String getDimIdFld(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取维度字段时输入的参数为空");
		String codeFld = null;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		if (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			codeFld = rptDim.dimInfo.code_idfld;
		} else if (RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {

		} else {
			codeFld = RptOlapDimUtil.getDimCurLevel(tCol.getDigLevel(),
					rptDim.dimInfo.dim_levels).code_idfld;
		}
		return codeFld;
	}

	private static String getDimFldType(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取维度字段类型时输入的参数为空");
		String fldType = RptOlapConsts.FLD_STRING_TYPE;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		if (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			fldType = rptDim.dimInfo.idfld_type;
		} else if (RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {

		} else {
			fldType = RptOlapDimUtil.getDimCurLevel(tCol.getDigLevel(),
					rptDim.dimInfo.dim_levels).id_fldtype;
		}
		return fldType;
	}

	/**
	 * 根据当前层次获取描述字段
	 *
	 * @param tCol
	 * @return
	 */
	private static String getDimDescFld(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取维度字段时输入的参数为空");
		String descFld = null;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		if (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())
				|| RptOlapConsts.ZERO_STR.equals(rptDim.max_level)) {
			descFld = rptDim.dimInfo.code_descfld;
		} else if (RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol.getDigLevel())) {

		} else {
			descFld = RptOlapDimUtil.getDimCurLevel(tCol.getDigLevel(),
					rptDim.dimInfo.dim_levels).code_descfld;
		}
		return descFld;
	}

	/**
	 * 根据当前维度层次获取FROM部分
	 *
	 * @param tCol
	 * @return
	 */
	public static String getDimFrom(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun) throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取维度字段时输入的参数为空");
		StringBuffer sb = new StringBuffer("");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		int maxLevel = Integer.parseInt(rptDim.max_level);
		int curLevel = Integer.parseInt(tCol.getDigLevel());
		List levels = rptDim.dimInfo.dim_levels;
		DimLevelTable[] aLevels = (DimLevelTable[]) levels
				.toArray(new DimLevelTable[levels.size()]);
		if (maxLevel > aLevels.length) {
			maxLevel = aLevels.length;
		}
		// 从最底层关联,要考虑有上层值的情况
		String value = tCol.getDimValue();
		if (null != value && !"".equals(value)) {
			if (tCol.isClickDim() || olapFun.isSingleDimExpand()) {
				// 需要多关联一层,找前一层的
				Object tmpObj = RptOlapDimUtil.getDimPreLevel(curLevel + "",
						rptDim);
				if (tmpObj instanceof DimensionTable)
					curLevel = 0;
				if (tmpObj instanceof DimLevelTable) {
					DimLevelTable tmpLvl = (DimLevelTable) tmpObj;
					curLevel = Integer.parseInt(tmpLvl.lvl_id);
				}
			}
		}
		for (int i = (aLevels.length - 1); i >= 0; i--) {
			int tmpLevel = Integer.parseInt(aLevels[i].lvl_id);
			if (tmpLevel <= maxLevel && tmpLevel >= curLevel) {
				// 开始关联
				sb.append(aLevels[i].lvl_table).append(" D")
						.append(tCol.getIndex()).append("_")
						.append(aLevels[i].lvl_id).append(",");
			}
		}
		// 判断一下是否当前层是0
		if (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())
				|| RptOlapConsts.ZERO_STR.equals(curLevel + "")) {
			sb.append(rptDim.dimInfo.dim_table).append(" D")
					.append(tCol.getIndex()).append("_")
					.append(RptOlapConsts.ZERO_STR).append(",");
		}
		String from = sb.toString();
		from = RptOlapStringUtil.removeLastSubStr(from, ",");
		return from;
	}

	/**
	 * 根据当前条件生成WHERE部分
	 *
	 * @param tCol
	 * @return
	 */
	public static String getDimWhere(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun) throws ReportOlapException {
		if (null == tCol || null == olapFun)
			throw new ReportOlapException("获取维度的查询条件时输入的参数为空");
		StringBuffer sb = new StringBuffer("");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String maxLevel = rptDim.max_level;
		String curLevel = tCol.getDigLevel();
		List levels = rptDim.dimInfo.dim_levels;
		String lastFld = null;
		if (null != levels && 0 < levels.size()) {
			DimLevelTable[] aLevels = (DimLevelTable[]) levels
					.toArray(new DimLevelTable[levels.size()]);
			int init = 0;
			int to = 0;
			for (int i = 0; i < aLevels.length; i++) {
				if (aLevels[i].lvl_id.equals(maxLevel))
					init = i;
				if (aLevels[i].lvl_id.equals(curLevel))
					// 如果控制级别是0
					to = i;
			}
			// 从最底层关联
			for (int i = init; i >= to; i--) {
				if (i != init) {
					// 开始关联
					sb.append(" AND ").append(" D").append(tCol.getIndex())
							.append("_").append(aLevels[i + 1].lvl_id)
							.append(".").append(aLevels[i + 1].parent_idfld)
							.append("=").append(" D").append(tCol.getIndex())
							.append("_").append(aLevels[i].lvl_id).append(".")
							.append(aLevels[i].code_idfld);
				}
				lastFld = " D" + tCol.getIndex() + "_" + aLevels[i].lvl_id
						+ "." + aLevels[i].parent_idfld;
			}
			//
			// 这里为什么要这样
			if (RptOlapConsts.ZERO_STR.equals(tCol.getDigLevel())) {
				sb.append(" AND D").append(tCol.getIndex()).append("_")
						.append(aLevels[0].lvl_id).append(".")
						.append(aLevels[0].parent_idfld).append("=D")
						.append(tCol.getIndex()).append("_")
						.append(RptOlapConsts.ZERO_STR).append(".")
						.append(rptDim.dimInfo.code_idfld);
			}
		}
		String value = tCol.getDimValue();
		if (null != value && !"".equals(value)) {
			// 这里要判断是否是钻取维度，不是的化应该找本级了
			if (tCol.isClickDim() || olapFun.isSingleDimExpand()) {
				Object preLevel = RptOlapDimUtil.getDimPreLevel(
						tCol.getDigLevel(), rptDim);
				String fieldCode = "";
				String fieldType = "";
				String preLvl = RptOlapConsts.ZERO_STR;
				if (preLevel instanceof DimensionTable) {
					fieldCode = rptDim.dimInfo.code_idfld;
					fieldType = rptDim.dimInfo.idfld_type;
					preLvl = RptOlapConsts.ZERO_STR;
					if (RptOlapConsts.NO_NEXT_LEVEL.equals(tCol.getDigLevel()))
						preLvl = RptOlapConsts.NO_NEXT_LEVEL;

				}
				if (preLevel instanceof DimLevelTable) {
					DimLevelTable level = (DimLevelTable) preLevel;
					fieldCode = level.code_idfld;
					fieldType = level.id_fldtype;
					preLvl = level.lvl_id;
				}
				if (RptOlapConsts.FLD_STRING_TYPE.equals(fieldType))
					value = "'" + value + "'";
				// 还要关联一下
				if (null != lastFld)
					sb.append(" AND ").append(" D").append(tCol.getIndex())
							.append("_").append(preLvl).append(".")
							.append(fieldCode).append("=").append(lastFld);
				sb.append(" AND ").append(" D").append(tCol.getIndex())
						.append("_").append(preLvl).append(".")
						.append(fieldCode).append("=").append(value);
			} else {
				String coldFld = getDimIdFld(tCol);
				String fldType = getDimFldType(tCol);
				String prefix = "D" + tCol.getIndex() + "_"
						+ tCol.getDigLevel();
				if (RptOlapConsts.FLD_STRING_TYPE.equals(fldType))
					value = "'" + value + "'";
				sb.append(" AND ").append(prefix).append(".").append(coldFld)
						.append("=").append(value);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取当前层次对象
	 *
	 * @param level
	 * @param levels
	 * @return
	 */
	public static DimLevelTable getDimCurLevel(String level, List levels)
			throws ReportOlapException {
		if (null == level || null == levels || 0 >= levels.size())
			throw new ReportOlapException("获取当前维度对象时输入的参数为空");
		DimLevelTable levelObj = null;
		if (null != levels && null != level) {
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				DimLevelTable dlt = (DimLevelTable) iter.next();
				levelObj = dlt;
				if (level.equals(dlt.lvl_id)) {
					break;
				}
			}
		}
		return levelObj;
	}

	/**
	 * 返回非时间维的前一层次对象
	 *
	 * @param curLevel
	 * @param rptDim
	 * @return
	 */
	private static Object getDimPreLevel(String curLevel, RptOlapDimTable rptDim) {
		Object preLevel = null;
		if (null != curLevel && null != rptDim) {
			List levels = rptDim.dimInfo.dim_levels;
			preLevel = rptDim.dimInfo;
			if (RptOlapConsts.ZERO_STR.equals(curLevel))
				return preLevel;
			if (null != levels) {
				DimLevelTable pre = null;
				Iterator iter = levels.iterator();
				while (iter.hasNext()) {
					DimLevelTable level = (DimLevelTable) iter.next();
					if (level.lvl_id.equals(curLevel)) {
						break;
					}
					pre = level;
				}
				// 循环完毕，没找到，那说明到最细粒度
				if (null != pre)
					preLevel = pre;
			}
		}
		return preLevel;
	}

	/**
	 * 获取当非时间维的层次
	 *
	 * @param col
	 *            列域对象
	 * @param dir
	 *            方向/上或下一层,RptOlapConsts.LEVEL_NEXT,RptOlapConsts.LEVEL_PRE
	 * @return
	 * @throws ReportOlapException
	 */
	public static String getNonTimeDimLevel(RptOlapTableColumnStruct col,
			int dir) throws ReportOlapException {
		if (null == col)
			throw new ReportOlapException("获取非时间维的层次时输入的参数为空");
		String level = null;
		Object tmpObj = col.getStruct();
		RptOlapDimTable rptDim = (RptOlapDimTable) tmpObj;
		String maxLevel = rptDim.max_level;
		String curLevel = col.getDigLevel();
		List levels = rptDim.dimInfo.dim_levels;
		level = getNonTimeDimLevel(curLevel, levels, dir);
		if (null == level
				|| Integer.parseInt(level) > Integer.parseInt(maxLevel))
			level = maxLevel;
		return level;
	}

	public static String getNonTimeDimLevelName(RptOlapTableColumnStruct tCol,
			String level) throws ReportOlapException {
		if (null == tCol || null == level || "".equals(level))
			throw new ReportOlapException("获取维度层次名称时输入得参数为空");
		String levelName = null;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level)
				|| RptOlapConsts.ZERO_STR.equals(level)
				|| null == rptDim.dimInfo.dim_levels
				|| 0 >= rptDim.dimInfo.dim_levels.size()
				|| (RptOlapConsts.NO_NEXT_LEVEL.equals(level) && (null == rptDim.dimInfo.dim_levels || 0 >= rptDim.dimInfo.dim_levels
						.size())))
			return rptDim.dimInfo.dim_name;
		// 以上把所有特殊情况考虑到了
		List levels = rptDim.dimInfo.dim_levels;
		Iterator iter = levels.iterator();
		while (iter.hasNext()) {
			DimLevelTable levelObj = (DimLevelTable) iter.next();
			if (levelObj.lvl_id.equals(level)) {
				levelName = levelObj.lvl_name;
				break;
			}
		}
		return levelName;
	}

	/**
	 * 获取当非时间维的层次
	 *
	 * @param level
	 *            当前层次
	 * @param levels
	 *            层次对象列表
	 * @param dir
	 *            方向/上或下一层
	 * @return
	 */
	private static String getNonTimeDimLevel(String level, List levels, int dir)
			throws ReportOlapException {
		if (null == level)
			throw new ReportOlapException("获取非时间维的层次时输入的参数为空");
		String retLvl = null;
		String preLvl = RptOlapConsts.NO_DIGGED_LEVEL;
		String nextLevel = RptOlapConsts.NO_NEXT_LEVEL;
		if (dir == RptOlapConsts.LEVEL_PRE) {
			if (RptOlapConsts.ZERO_STR.equals(level)
					|| RptOlapConsts.NO_DIGGED_LEVEL.equals(level))
				return preLvl;
			if (null == levels || 0 >= levels.size())
				return preLvl;
		}
		if (dir == RptOlapConsts.LEVEL_NEXT) {
			if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level))
				return RptOlapConsts.ZERO_STR;
			if (null == levels || 0 >= levels.size())
				return nextLevel;
		}
		if (null != levels && 0 < levels.size()) {
			DimLevelTable[] lvls = (DimLevelTable[]) levels
					.toArray(new DimLevelTable[levels.size()]);
			if (dir == RptOlapConsts.LEVEL_NEXT) {
				if (RptOlapConsts.ZERO_STR.equals(level))
					return lvls[0].lvl_id;
			}
			int count = 0;
			for (int i = 0; i < lvls.length; i++) {
				if (level.equals(lvls[i].lvl_id)) {
					count = i;
					break;
				}
			}
			if (0 == count) {
				preLvl = RptOlapConsts.ZERO_STR;
			} else {
				preLvl = lvls[count - 1].lvl_id;
			}
			if (count == lvls.length - 1) {
				nextLevel = RptOlapConsts.NO_NEXT_LEVEL;
			} else {
				nextLevel = lvls[count + 1].lvl_id;
			}
			if (dir == RptOlapConsts.LEVEL_PRE) {
				retLvl = preLvl;
			} else {
				retLvl = nextLevel;
			}
		}
		return retLvl;
	}

	/**
	 * 获取时间字段
	 *
	 * @param level
	 * @return
	 */
	public static String getDimTimeFld(RptOlapTableColumnStruct tCol,
			String level, String statPeriod) throws ReportOlapException {
		if (null == tCol || null == level || null == statPeriod
				|| "".equals(statPeriod))
			throw new ReportOlapException("获取时间维度字段时输入的参数为空");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String codeFld = null;
		if (RptOlapConsts.DIM_TIME_YEAR_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_YEAR_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_QUARTER_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_QUARTER_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_MONTH_LEVEL.equals(level)) {
			if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(statPeriod))
				codeFld = rptDim.dimInfo.code_idfld;
			else
				codeFld = RptOlapConsts.TIME_DIM_MONTH_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_TENDAYS_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_TENDAYS_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_WEEK_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_WEEK_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_DAY_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_DAY_FLD;
			return codeFld;
		}
		if (RptOlapConsts.ZERO_STR.equals(level)
				|| RptOlapConsts.NO_NEXT_LEVEL.equals(level)) {
			codeFld = rptDim.dimInfo.code_idfld;
			return codeFld;
		}
		return codeFld;
	}

	public static String getDimTimeDescFld(RptOlapTableColumnStruct tCol,
			String level) throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取时间维度字段时输入的参数为空");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String codeFld = null;
		if (RptOlapConsts.DIM_TIME_YEAR_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_YEAR_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_QUARTER_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_QUARTER_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_MONTH_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_MONTH_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_TENDAYS_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_TENDAYS_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_WEEK_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_WEEK_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.DIM_TIME_DAY_LEVEL.equals(level)) {
			codeFld = RptOlapConsts.TIME_DIM_DAY_DESC_FLD;
			return codeFld;
		}
		if (RptOlapConsts.ZERO_STR.equals(level)
				|| RptOlapConsts.NO_NEXT_LEVEL.equals(level)) {
			codeFld = rptDim.dimInfo.code_descfld;
			return codeFld;
		}
		return codeFld;
	}

	/**
	 * 生成维度时间WHERE部分
	 *
	 * @param tCol
	 * @return
	 */
	public static String getDimTimeWhere(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, String virTabName, String statPeriod,
			RptOlapDateStruct ds) throws ReportOlapException {
		if (null == tCol || null == olapFun || null == virTabName
				|| "".equals(virTabName) || null == statPeriod
				|| "".equals(statPeriod) || null == ds)
			throw new ReportOlapException("生成时间维度的查询条件时输入的参数为空");
		StringBuffer sb = new StringBuffer("");
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		sb.append(" AND ").append(rptDim.dimInfo.code_idfld).append(">=")
				.append(ds.getStart());
		sb.append(" AND ").append(rptDim.dimInfo.code_idfld).append("<=")
				.append(ds.getEnd());
		String value = tCol.getDimValue();
		if (null != value && !"".equals(value)) {
			if (RptOlapConsts.FLD_STRING_TYPE.equals(rptDim.dimInfo.idfld_type))
				value = "'" + value + "'";
			String preLevel = null;
			if (tCol.isClickDim() || olapFun.isSingleDimExpand()) {
				if (olapFun.isSingleDimExpand()) {
					String level = olapFun.getCurExpandLevel() + "";
					String realLevel = tCol.getDigLevel();
					tCol.setDigLevel(level);
					preLevel = getDimTimePreLevel(tCol);
					tCol.setDigLevel(realLevel);
				} else
					preLevel = getDimTimePreLevel(tCol);
			} else
				preLevel = tCol.getDigLevel();
			if (null != preLevel) {
				String preFld = getDimTimeFld(tCol, preLevel, statPeriod);
				sb.append(" AND ").append(virTabName + "." + preFld)
						.append("=").append(value);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取时间维度当前层次的上一层次
	 *
	 * @param tCol
	 * @return
	 */
	public static String getDimTimePreLevel(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取时间维度的上一层次时输入的参数为空");
		String preLevel = RptOlapConsts.NO_DIGGED_LEVEL;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String curLevel = tCol.getDigLevel();

		if (RptOlapConsts.NO_NEXT_LEVEL.equals(curLevel)) {
			// 当前为最细了
			if (RptOlapConsts.ZERO_STR.equals(rptDim.max_level)
					|| null == rptDim.timeLevel)
				return RptOlapConsts.NO_DIGGED_LEVEL;
			// 下面肯定是有时间定义了,找到最细的当前定义
			RptOlapTimeTable rptTime = rptDim.timeLevel;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_day))
				curLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
			else if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_week))
				curLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
			else if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_tendays))
				curLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
			else if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_month))
				curLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
			else if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_quarter))
				curLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
			else if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_year))
				curLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
		}
		if (RptOlapConsts.DIM_TIME_YEAR_LEVEL.equals(curLevel)) {
			return preLevel;
		}
		if (RptOlapConsts.DIM_TIME_QUARTER_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_year)) {
				preLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				return preLevel;
			}
			return preLevel;
		}
		if (RptOlapConsts.DIM_TIME_MONTH_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_quarter)) {
				preLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_year)) {
				preLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				return preLevel;
			}
			return preLevel;
		}
		if (RptOlapConsts.DIM_TIME_TENDAYS_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_month)) {
				preLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_quarter)) {
				preLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_year)) {
				preLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				return preLevel;
			}
			return preLevel;
		}
		if (RptOlapConsts.DIM_TIME_WEEK_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_tendays)) {
				preLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_month)) {
				preLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_quarter)) {
				preLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_year)) {
				preLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				return preLevel;
			}
			return preLevel;
		}
		if (RptOlapConsts.DIM_TIME_DAY_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_week)) {
				preLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_tendays)) {
				preLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_month)) {
				preLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_quarter)) {
				preLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				return preLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_year)) {
				preLevel = RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				return preLevel;
			}
			return preLevel;
		}
		return preLevel;
	}

	/**
	 * 获取时间维度下一层次
	 *
	 * @param tCol
	 * @return
	 */
	public static String getDimTimeNextLevel(RptOlapTableColumnStruct tCol)
			throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("获取时间维度的下一层次时输入的参数为空");
		String nextLevel = RptOlapConsts.NO_NEXT_LEVEL;
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		String curLevel = tCol.getDigLevel();
		// 还没钻取过，则从最高层
		if (RptOlapConsts.NO_DIGGED_LEVEL.equals(curLevel)) {
			RptOlapTimeTable rptTime = rptDim.timeLevel;
			if (null != rptTime) {
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_year)) {
					return RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_quarter)) {
					return RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_month)) {
					return RptOlapConsts.DIM_TIME_YEAR_LEVEL;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_tendays)) {
					return RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_week)) {
					return RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_day)) {
					return RptOlapConsts.DIM_TIME_DAY_LEVEL;
				}
				return RptOlapConsts.ZERO_STR;
			} else {
				return RptOlapConsts.ZERO_STR;
			}
		}
		if (RptOlapConsts.DIM_TIME_DAY_LEVEL.equals(curLevel)) {
			return nextLevel;
		}
		if (RptOlapConsts.DIM_TIME_WEEK_LEVEL.equals(curLevel)) {
			nextLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
			return nextLevel;
		}
		if (RptOlapConsts.DIM_TIME_TENDAYS_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_week)) {
				nextLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_day)) {
				nextLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
				return nextLevel;
			}
			return nextLevel;
		}
		if (RptOlapConsts.DIM_TIME_MONTH_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_tendays)) {
				nextLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_week)) {
				nextLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_day)) {
				nextLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
				return nextLevel;
			}
			return nextLevel;
		}
		if (RptOlapConsts.DIM_TIME_QUARTER_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_month)) {
				nextLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_tendays)) {
				nextLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_week)) {
				nextLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_day)) {
				nextLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
				return nextLevel;
			}
			return nextLevel;
		}
		if (RptOlapConsts.DIM_TIME_YEAR_LEVEL.equals(curLevel)) {
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_quarter)) {
				nextLevel = RptOlapConsts.DIM_TIME_QUARTER_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_month)) {
				nextLevel = RptOlapConsts.DIM_TIME_MONTH_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_tendays)) {
				nextLevel = RptOlapConsts.DIM_TIME_TENDAYS_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_week)) {
				nextLevel = RptOlapConsts.DIM_TIME_WEEK_LEVEL;
				return nextLevel;
			}
			if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.timeLevel.by_day)) {
				nextLevel = RptOlapConsts.DIM_TIME_DAY_LEVEL;
				return nextLevel;
			}
			return nextLevel;
		}
		return nextLevel;
	}

	/**
	 * 获取时间当前层次的名称
	 *
	 * @param level
	 * @return
	 */
	public static String getTimeLevelName(String level,
			RptOlapTableColumnStruct tCol) throws ReportOlapException {
		if (null == level || null == tCol)
			throw new ReportOlapException("获取时间维度的层次名称时输入的参数为空");
		String levelName = "";
		RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
		RptOlapTimeTable rptTime = rptDim.timeLevel;
		if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level)) {
			// 什么都没展开,显示最大的年度、季度、或者月份
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_year))
				return RptOlapConsts.BY_YEAR;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_quarter))
				return RptOlapConsts.BY_QUARTER;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_month))
				return RptOlapConsts.BY_MONTH;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_tendays))
				return RptOlapConsts.BY_TENDAYS;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_week))
				return RptOlapConsts.BY_WEEK;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptTime.by_day))
				return RptOlapConsts.BY_DAY;
			// 到这时还没有返回，则返回维度的描述
			return rptDim.dimInfo.dim_name;
		}
		if (RptOlapConsts.ZERO_STR.equals(level)) {
			return tCol.getColName();
		}
		if (RptOlapConsts.DIM_TIME_YEAR_LEVEL.equals(level))
			return RptOlapConsts.BY_YEAR;
		if (RptOlapConsts.DIM_TIME_QUARTER_LEVEL.equals(level))
			return RptOlapConsts.BY_QUARTER;
		if (RptOlapConsts.DIM_TIME_MONTH_LEVEL.equals(level))
			return RptOlapConsts.BY_MONTH;
		if (RptOlapConsts.DIM_TIME_TENDAYS_LEVEL.equals(level))
			return RptOlapConsts.BY_TENDAYS;
		if (RptOlapConsts.DIM_TIME_WEEK_LEVEL.equals(level))
			return RptOlapConsts.BY_WEEK;
		if (RptOlapConsts.DIM_TIME_DAY_LEVEL.equals(level))
			return RptOlapConsts.BY_DAY;
		return levelName;
	}
}
