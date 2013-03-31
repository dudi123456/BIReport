package com.ailk.bi.subject.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;

public class TableRowHeadSwapUtil {
	private TableRowHeadSwapUtil() {

	}

	/**
	 * 生产纵转横的镖头左侧部分，必须在定制表头中定义,不支持全部扩展等
	 * 
	 * @param subTable
	 * @param curFunc
	 * @param customHead
	 * @return
	 */
	public static Map<String, String> genSwapHeadLeftPart(
			SubjectCommTabDef subTable, TableCurFunc curFunc) {
		Map<String, String> result = new HashMap<String, String>();
		StringBuffer left = new StringBuffer();
		StringBuffer export = new StringBuffer();
		// left.append("		<td align=center valign='top'>\n");
		// left.append("<table id=\"iTable_LeftHeadTable1\" "
		// + "width=\"100%\" border=\"0\" "
		// + "cellpadding=\"0\" cellspacing=\"0\" " + "class=\"table\">\n");
		left.append("<tr class=\"").append(curFunc.tabHeadTRClass)
				.append("\">\n");
		// 直接将定义的表头加上
		String head = null;
		if (null != subTable.tabHead
				&& null != subTable.tabHead.row_head_swap_header) {
			head = subTable.tabHead.row_head_swap_header;
		}
		if (StringUtils.isBlank(head)) {
			head = "指标";
			StringBuffer sb = new StringBuffer();
			sb.append("<td nowrap align=\"center\" class=\"")
					.append(curFunc.tabHeadTDClass).append("\" ");
			sb.append(">\n");
			sb.append(head);
			sb.append("</td>");
			left.append(sb);
			export.append(sb);
		} else {
			// 这里需要去掉tr标志如果有的话话
			head = SubjectStringUtil.removeHtmlTag(head, "tr");
			left.append(head);
			export.append(head);
		}
		// left.append("</tr>\n");
		// left.append("</table>\n");
		// left.append("</td>");
		result.put(SubjectConst.HEAD_KEYS_LEFT, left.toString());
		result.put(SubjectConst.HEAD_KEYS_LEFT_EXPORT, export.toString());
		return result;
	}

	/**
	 * 纵转横后，表头右侧部分，只支持一个维度
	 * 
	 * @param subTable
	 * @param curFunc
	 * @param customHead
	 * @param sves
	 * @return
	 */
	public static Map<String, String> genSwapHeadRightPart(
			SubjectCommTabDef subTable, TableCurFunc curFunc,
			String customHead, String[][] sves) {
		Map<String, String> result = new HashMap<String, String>();
		StringBuffer right = new StringBuffer();
		StringBuffer export = new StringBuffer();
		// right.append("<td width=\"100%\" align=\"left\" valign=\"top\">\n");
		// right.append("	<div id=\"Layer1\" "
		// + "style=\"position:absolute; width:100%; z-index:1; "
		// + "overflow: hidden;\">\n");
		// right.append("	<table width=\"100%\" border=\"0\" "
		// + "cellpadding=\"0\" cellspacing=\"0\" "
		// + "class=\"iTable\" id=\"iTable_HeadTable1\">\n");
		// 相当于有多少个行就有多少单元格
		// right.append("<tr class=\"").append(curFunc.tabHeadTRClass)
		// .append("\">\n");
		StringBuffer singleCell = new StringBuffer();
		for (int i = 0; i < sves.length-1; i++) {
			singleCell.append("<td nowrap");
			singleCell.append(" align=\"center\" class=\"")
					.append(curFunc.tabHeadTDClass).append("\" ").append(">\n");
			singleCell.append(sves[i][1]);
			singleCell.append("</td>");
		}
		right.append(singleCell);
		export.append(singleCell);
		//right.append("</tr></table>");
		// right.append("</div></td>\n");

		result.put(SubjectConst.HEAD_KEYS_RIGHT, right.toString());
		result.put(SubjectConst.HEAD_KEYS_RIGHT_EXPORT, export.toString());
		return result;
	}
}
