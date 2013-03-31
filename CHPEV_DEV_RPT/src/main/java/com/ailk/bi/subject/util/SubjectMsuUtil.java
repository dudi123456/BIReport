package com.ailk.bi.subject.util;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableSelectQuery;

/**
 * @author xdou 专题通用模版指标工具类
 */
public class SubjectMsuUtil {

	/**
	 * 生成指标非扩展查询结构
	 * 
	 * @param tabCol
	 * @param curFunc
	 * @param virTableName
	 * @return
	 */
	public static TableSelectQuery genMsuNoExpandSelectParts(SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String virTableName) {
		TableSelectQuery parts = new TableSelectQuery();

		// 由于是最基本的形式，
		// 且这里暂不关注分页，分页还是采取架构的一次取出
		// 对于指标，比较复杂，要考虑是否有占比、环比、同比、预警等
		String msu_fld = tabCol.code_field;
		// 替换伪表名
		msu_fld = SubjectStringUtil.replaceVirTabName(msu_fld, virTableName);

		//
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
			if (curFunc.hasRatio) {
				// 有比率计算，为了简化计算,所有的指标都计算
				// 本期值
				parts.select.append(SubjectConst.NULL_PORCESS.replaceAll("::NULL::", msu_fld)).append(" AS M")
						.append(tabCol.col_sequence).append(",");
				parts.select.append(genMsuRankParts(tabCol, virTableName));
				// 判断是否有同比
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
					String last_fld = msu_fld;
					last_fld = SubjectStringUtil.replaceVirTabName(last_fld, SubjectConst.DATA_TABLE_LAST_VIR_NAME);
					// 加上同比期
					parts.select.append(SubjectConst.NULL_PORCESS.replaceAll("::NULL::", last_fld)).append(",");
					// 计算同比
					String last_ratio = "(" + msu_fld + "- (" + last_fld + "))/(CASE WHEN (" + last_fld
							+ ")=0 THEN NULL ELSE " + last_fld + " END)";
					parts.select.append(SubjectConst.NULL_PORCESS.replaceAll("::NULL::", last_ratio)).append(",");
				}
				// 判断是否有环比
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
					String loop_fld = msu_fld;
					loop_fld = SubjectStringUtil.replaceVirTabName(loop_fld, SubjectConst.DATA_TABLE_LOOP_VIR_NAME);
					// 加上环比期
					parts.select.append(SubjectConst.NULL_PORCESS.replaceAll("::NULL::", loop_fld)).append(",");
					// 计算同比
					String loop_ratio = "(" + msu_fld + "- (" + loop_fld + "))/(CASE WHEN (" + loop_fld
							+ ")=0 THEN NULL ELSE " + loop_fld + " END)";
					parts.select.append(SubjectConst.NULL_PORCESS.replaceAll("::NULL::", loop_ratio)).append(",");
				}
			} else {
				// 什么比率也没有
				// 直接构造
				msu_fld = SubjectConst.NULL_PORCESS.replaceAll("::NULL::", msu_fld);
				parts.select.append(msu_fld).append(" AS M").append(tabCol.col_sequence).append(",");
				// 加上排名
				// 排序

				parts.select.append(genMsuRankParts(tabCol, virTableName));
			}
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_sort)) {
				parts.orderby.append("M").append(tabCol.col_sequence).append(" ").append(tabCol.sort_order).append(",");
			}
		}
		return parts;
	}

	private static String genMsuRankParts(SubjectCommTabCol tabCol, String virTableName) {
		StringBuffer sb = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
			String msu_fld = tabCol.code_field;
			// 替换伪表名
			msu_fld = SubjectStringUtil.replaceVirTabName(msu_fld, virTableName);
			StringBuffer rankFld = new StringBuffer();
			rankFld.append(" (").append(tabCol.rank_mode).append("() OVER ( ORDER BY ").append(msu_fld)
					.append(tabCol.rank_order).append(")");
			if ("DESC".equalsIgnoreCase(tabCol.rank_order)) {
				// 此处需要去掉总排名
				rankFld.append(" -1");
			}
			rankFld.append(") ");
			sb.append(rankFld).append("AS RANK_").append(tabCol.col_id).append(",");
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
				// 是否有排名波动
				String last_msu_fld = tabCol.rank_last;
				// 替换伪表名
				last_msu_fld = SubjectStringUtil.replaceVirTabName(last_msu_fld, virTableName);
				StringBuffer lastRankFld = new StringBuffer();
				lastRankFld.append(tabCol.rank_mode).append("() OVER ( ORDER BY ").append(last_msu_fld);

				lastRankFld.append(tabCol.rank_order).append(") ");
				if ("DESC".equalsIgnoreCase(tabCol.rank_order)) {
					// 此处需要去掉总排名
					lastRankFld.append(" -1");
				}
				sb.append("(").append(lastRankFld).append("-").append(rankFld).append(") AS RANK_VAR_")
						.append(tabCol.col_id).append(",");
			}
		}
		return sb.toString();
	}
}
