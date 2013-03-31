package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTDStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.dao.ICustomTableHeadDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapMsuUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomTableHeadDao implements ICustomTableHeadDao {

	/**
	 * 导出的自定义表头
	 */
	private List exportHead = new ArrayList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.ICustomTableHeadDao#getCustomHead(com.asiabi
	 * .base.table.PubInfoResourceTable, java.util.List,
	 * com.asiabi.olap.domain.RptOlapFunc, boolean, java.lang.String,
	 * java.lang.String)
	 */
	public List getCustomHead(PubInfoResourceTable report, List tableCols,
			RptOlapFuncStruct olapFun, boolean fixedHead, String trStyle,
			String tdStyle) throws ReportOlapException {
		List head = new ArrayList();
		if (null == report || null == tableCols || null == olapFun)
			throw new IllegalArgumentException("生成定制表头时输入的参数为空");

		String trClass = trStyle;
		String tdClass = tdStyle;
		if (null == trClass)
			trClass = "";
		if (null == tdClass)
			tdClass = "";
		if (fixedHead) {
			StringBuffer left = new StringBuffer("");
			StringBuffer right = new StringBuffer("");
			String leftPart = RptOlapStringUtil.genFixedHeadLeftPart(trStyle,
					tdStyle);
			String rightPart = RptOlapStringUtil.genFixedHeadRightPart(trStyle,
					tdStyle);
			List list = genFixedCustomHead(report, tableCols, olapFun, trStyle,
					tdStyle);
			if (null != list && list.size() == 2) {
				String[] aryStr = (String[]) list.toArray(new String[list
						.size()]);
				leftPart = leftPart.replaceAll("::leftBody::", aryStr[0]);
				rightPart = rightPart.replaceAll("::rightBody::", aryStr[1]);
			}
			left.append(leftPart);
			right.append(rightPart);
			left.append(right);
			head.add(left.toString());
		} else {
			head.add(genNonFixedCustomHead(report, tableCols, olapFun, trClass,
					tdClass));
		}
		return head;
	}

	/**
	 * 
	 * @param head
	 * @param trClass
	 * @param tdClass
	 * @return
	 * @throws ReportOlapException
	 */
	private String repalceHeadStyle(String head, String trClass, String tdClass)
			throws ReportOlapException {
		if (null == head || "".equals(head))
			throw new ReportOlapException("替换自定义表头的样式时表头为空");

		String ret = head;
		// 清除tr样式
		String reg = "<tr(\\s*\\w*)(class=\"?\\w*\\W*\\w*\"?)(\\s*[^>]*)>";
		String replacement = "<tr$1$3>";
		ret = RptOlapStringUtil.regReplace(ret, reg, replacement, true);
		// 清除 td 样式
		reg = "<td(\\s*\\w*)(class=\"?\\w*\\W*\\w*\"?)(\\s*[^>]*)>";
		replacement = "<td$1$3>";
		ret = RptOlapStringUtil.regReplace(ret, reg, replacement, true);
		// 加上tr样式
		reg = "<tr(\\s*\\w*[^>]*)>";
		replacement = "<tr class=\"" + trClass + "\"$1>";
		ret = RptOlapStringUtil.regReplace(ret, reg, replacement, true);
		// 加上td样式
		reg = "<td(\\s*\\w*[^>]*)>";
		replacement = "<td class=\"" + tdClass + "\"$1>";
		ret = RptOlapStringUtil.regReplace(ret, reg, replacement, true);
		return ret;
	}

	private List genFixedCustomHead(PubInfoResourceTable report,
			List tableCols, RptOlapFuncStruct olapFun, String trClass,
			String tdClass) throws ReportOlapException {
		if (null == report || null == tableCols || null == olapFun
				|| 0 >= tableCols.size())
			throw new ReportOlapException("生成自定义表头时输入的参数为空");

		List head = null;
		// 先替换tr样式
		String rptHead = report.head.rpt_header;
		rptHead = repalceHeadStyle(rptHead, trClass, tdClass);
		// 要对维度进行收缩的可能，指标排序的可能，还有同比时，列要夸
		head = combineFixedHead(rptHead, tableCols, olapFun, report.cycle,
				trClass, tdClass);
		return head;
	}

	private String genNonFixedCustomHead(PubInfoResourceTable report,
			List tableCols, RptOlapFuncStruct olapFun, String trClass,
			String tdClass) throws ReportOlapException {
		if (null == report || null == tableCols || 0 >= tableCols.size()
				|| null == olapFun)
			throw new ReportOlapException("生成非固定自定义表头时输入的参数为空");

		String head = "";
		// 先清除 tr class 样式
		head = report.head.rpt_header;
		head = repalceHeadStyle(head, trClass, tdClass);
		// 要对维度进行收缩的可能，指标排序的可能，还有同比时，列要夸
		head = combineNonFixedHead(head, tableCols, olapFun, report.cycle,
				trClass, tdClass);
		return head;
	}

	private String combineNonFixedHead(String head, List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, String trClass,
			String tdClass) throws ReportOlapException {
		if (null == head || "".equals(head) || null == tableCols
				|| 0 >= tableCols.size() || null == olapFun
				|| null == statPeriod || "".equals(statPeriod))
			throw new ReportOlapException("组合非固定自定义表头时输入的参数为空");

		String sHead = "";

		// 这里只获取第一行的列单元格，对于第二层直接使用指标名称
		List list = parseTabHead(head);

		StringBuffer firRow = new StringBuffer("");
		StringBuffer secRow = new StringBuffer("");
		StringBuffer thrRow = new StringBuffer("");

		StringBuffer firExpRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");
		StringBuffer thrExpRow = new StringBuffer("");

		firRow.append("<tr class=\"").append(trClass).append("\">\n");
		secRow.append("<tr class=\"").append(trClass).append("\">\n");

		firExpRow.append("<tr>\n");
		secExpRow.append("<tr>\n");

		boolean hasRowSpan = false;
		String func = olapFun.getCurFunc();
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum())
				|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
				|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
				|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			hasRowSpan = true;
			thrRow.append("<tr class=\"").append(trClass).append("\">\n");
			thrExpRow.append("<tr class=\"").append(trClass).append("\">\n");
		}
		// 单元格域对象列表
		RptOlapTDStruct[] aTd = (RptOlapTDStruct[]) list
				.toArray(new RptOlapTDStruct[list.size()]);

		int totalSpan = 0;
		int colSpan = 0;
		// 跨单元格计数器
		int count = 0;
		int index = -1;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				// 列的索引
				index++;
				if (tCol.isDim()) {
					int dimRowSpan = 1;
					if (null != aTd[index].getRowspan())
						dimRowSpan = Integer.parseInt(aTd[index].getRowspan());
					if (hasRowSpan)
						dimRowSpan++;
					firRow.append("<td rowspan=\"").append(dimRowSpan)
							.append("\" nowrap align=\"left\" class=\"")
							.append(tdClass).append("\">\n");
					// 增加收缩链接
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel()))
						firRow.append(RptOlapDimUtil.genCollapseInitLink(tCol,
								tableCols, olapFun));
					firRow.append(tCol.getColName());// 必须是当前层次的名称
					firRow.append("</td>");

					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						firExpRow.append("<td rowspan=\"").append(dimRowSpan)
								.append("\" nowrap align=\"left\">\n");
						firExpRow.append(tCol.getColName());
						firExpRow.append("</td>");
					}
				} else {
					// 指标复杂
					if (null != aTd[index].getColspan()) {
						// 如果有列合并
						colSpan = Integer.parseInt(aTd[index].getColspan());

						int realSpan = 1;
						List right = RptOlapMsuUtil.genMsuHead(tableCols, tCol,
								olapFun, statPeriod, hasRowSpan, tdClass);
						StringBuffer right1 = (StringBuffer) right.get(0);
						StringBuffer right2 = (StringBuffer) right.get(1);
						StringBuffer exportRight1 = (StringBuffer) right.get(2);
						StringBuffer exportRight2 = (StringBuffer) right.get(3);
						// 还有导出呢
						secRow.append(right1);
						secExpRow.append(exportRight1);
						if (right2.length() > 0) {
							thrRow.append(right2);
							realSpan++;
						}
						if (exportRight2.length() > 0) {
							thrExpRow.append(exportRight2);
						}
						// 对于所有后续指标
						totalSpan += realSpan;
						if (count < (colSpan - 1)) {
							count++;
							index--;// 这样定住了colspan
						} else {
							// 新的了
							firRow.append("<td nowrap align=\"center\" ")
									.append("class=\"").append(tdClass)
									.append("\" colspan=\"").append(totalSpan)
									.append("\">");
							firRow.append(aTd[index].getContent());
							firRow.append("</td>");

							firExpRow.append("<td nowrap align=\"center\" ")
									.append("colspan=\"").append(totalSpan)
									.append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
							// 重置计数器
							count = 0;
							totalSpan = 0;
						}
					} else {
						// 单列，那就有rowspan 了
						int realSpan = 1;
						if (hasRowSpan) {
							// 有两层
							List right = RptOlapMsuUtil.genMsuHead(tableCols,
									tCol, olapFun, statPeriod, hasRowSpan,
									tdClass);
							StringBuffer right1 = (StringBuffer) right.get(0);
							StringBuffer right2 = (StringBuffer) right.get(1);
							StringBuffer exportRight1 = (StringBuffer) right
									.get(2);
							StringBuffer exportRight2 = (StringBuffer) right
									.get(3);
							// 还有导出呢
							secRow.append(right1);
							secExpRow.append(exportRight1);
							if (right2.length() > 0) {
								thrRow.append(right2);
								realSpan++;
							}
							if (exportRight2.length() > 0) {
								thrExpRow.append(exportRight2);
							}
							firRow.append(
									"<td nowrap align=\"center\" colspan=\"")
									.append(realSpan).append("\" class=\"")
									.append(tdClass).append("\" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firRow.append(aTd[index].getContent());
							firRow.append("</td>");

							firExpRow
									.append("<td nowrap align=\"center\" colspan=\"")
									.append(realSpan).append("\" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
						} else {
							// 单层
							firRow.append("<td nowrap align=\"center\" ")
									.append("class=\"").append(tdClass)
									.append("\"").append(" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firRow.append(aTd[index].getContent());
							firRow.append(RptOlapDimUtil.genSortLink(tCol,
									tableCols, RptOlapConsts.SORT_THIS_PERIOD,
									olapFun));
							firRow.append("</td>");

							firExpRow.append("<td nowrap align=\"center\" ")
									.append("rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
						}
					}
				}
			}
		}
		firRow.append("</tr>");
		secRow.append("</tr>");
		firRow.append(secRow);

		firExpRow.append("</tr>");
		secExpRow.append("</tr>");
		firExpRow.append(secExpRow);

		if (thrRow.length() > 0) {
			thrRow.append("</tr>");
			firRow.append(thrRow);
		}

		if (thrExpRow.length() > 0) {
			thrExpRow.append("</tr>");
			firExpRow.append(thrExpRow);
		}

		sHead = firRow.toString();
		exportHead.clear();
		exportHead.add(firExpRow.toString());

		return sHead;
	}

	private List combineFixedHead(String head, List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, String trClass,
			String tdClass) throws ReportOlapException {
		if (null == head || "".equals(head) || null == tableCols
				|| 0 >= tableCols.size() || null == olapFun
				|| null == statPeriod || "".equals(statPeriod))
			throw new ReportOlapException("生成固定自定义表头时输入的参数为空");

		List sHead = new ArrayList();

		List list = parseTabHead(head);

		StringBuffer left = new StringBuffer("");
		StringBuffer firRow = new StringBuffer("");
		StringBuffer secRow = new StringBuffer("");
		StringBuffer thrRow = new StringBuffer("");

		StringBuffer firExpRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");
		StringBuffer thrExpRow = new StringBuffer("");

		firRow.append("<tr class=\"").append(trClass).append("\">\n");
		secRow.append("<tr class=\"").append(trClass).append("\">\n");

		firExpRow.append("<tr>\n");
		secExpRow.append("<tr>\n");

		boolean hasRowSpan = false;

		String func = olapFun.getCurFunc();
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum())
				|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
				|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
				|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			hasRowSpan = true;
			thrRow.append("<tr class=\"").append(trClass).append("\">\n");
			thrExpRow.append("<tr>\n");
		}
		RptOlapTDStruct[] aTd = (RptOlapTDStruct[]) list
				.toArray(new RptOlapTDStruct[list.size()]);
		int totalSpan = 0;
		int colSpan = 0;
		int count = 0;
		int index = -1;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				// 列的索引
				index++;
				if (tCol.isDim()) {
					int dimRowSpan = 1;
					if (null != aTd[index].getRowspan())
						dimRowSpan = Integer.parseInt(aTd[index].getRowspan());
					if (hasRowSpan)
						dimRowSpan++;

					String html = "";
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						// 增加收缩链接
						html = RptOlapDimUtil.genCollapseInitLink(tCol,
								tableCols, olapFun);
					}
					html += tCol.getColName();
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						// 生成单层收缩
						String tmpStr = RptOlapDimUtil.genCollapseUpLink(tCol,
								tableCols, olapFun);
						html += tmpStr;
					}
					left.append("<td rowspan=\"").append(dimRowSpan)
							.append("\" nowrap align=\"left\" class=\"")
							.append(tdClass).append("\">\n");
					left.append(html);
					tCol.setColHeadHTML1(html);
					left.append("</td>");
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						firExpRow.append("<td rowspan=\"").append(dimRowSpan)
								.append("\" nowrap align=\"left\">\n");
						firExpRow.append(tCol.getColName());
						firExpRow.append("</td>");
					}
				} else {
					// 指标复杂
					if (null != aTd[index].getColspan()) {
						// 如果有列合并
						colSpan = Integer.parseInt(aTd[index].getColspan());
						int realSpan = 1;

						if (hasRowSpan) {
							List right = RptOlapMsuUtil.genMsuHead(tableCols,
									tCol, olapFun, statPeriod, hasRowSpan,
									tdClass);
							StringBuffer right1 = (StringBuffer) right.get(0);
							StringBuffer right2 = (StringBuffer) right.get(1);
							StringBuffer exportRight1 = (StringBuffer) right
									.get(2);
							StringBuffer exportRight2 = (StringBuffer) right
									.get(3);
							// 还有导出呢
							secRow.append(right1);
							secExpRow.append(exportRight1);
							if (right2.length() > 0) {
								thrRow.append(right2);
								realSpan++;
							}
							if (exportRight2.length() > 0) {
								thrExpRow.append(exportRight2);
							}
						} else {
							// 单层
							secRow.append("<td nowrap align=\"center\" ")
									.append("class=\"").append(tdClass)
									.append("\">");
							StringBuffer html = new StringBuffer(
									tCol.getColName());
							html.append(RptOlapDimUtil.genSortLink(tCol,
									tableCols, RptOlapConsts.SORT_THIS_PERIOD,
									olapFun));
							secRow.append(html);
							secRow.append("</td>");
							tCol.setColHeadHTML1(html.toString());

							secExpRow.append("<td nowrap align=\"center\">");
							secExpRow.append(tCol.getColName());
							secExpRow.append("</td>");
						}
						// 对于所有后续指标
						totalSpan += realSpan;
						if (count < (colSpan - 1)) {
							count++;
							index--;// 这样定住了colspan
						} else {
							// 新的了
							firRow.append("<td nowrap align=\"center\" ")
									.append("class=\"").append(tdClass)
									.append("\" colspan=\"").append(totalSpan)
									.append("\">");
							firRow.append(aTd[index].getContent());
							firRow.append("</td>");
							firExpRow
									.append("<td nowrap align=\"center\" colspan=\"")
									.append(totalSpan).append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
							count = 0;
							totalSpan = 0;
						}
					} else {
						// 单列，那就有rowspan 了
						int realSpan = 1;
						if (hasRowSpan) {
							List right = RptOlapMsuUtil.genMsuHead(tableCols,
									tCol, olapFun, statPeriod, hasRowSpan,
									tdClass);
							StringBuffer right1 = (StringBuffer) right.get(0);
							StringBuffer right2 = (StringBuffer) right.get(1);
							StringBuffer exportRight1 = (StringBuffer) right
									.get(2);
							StringBuffer exportRight2 = (StringBuffer) right
									.get(3);
							// 还有导出呢
							secRow.append(right1);
							secExpRow.append(exportRight1);
							if (right2.length() > 0) {
								thrRow.append(right2);
								realSpan++;
							}
							if (exportRight2.length() > 0) {
								thrExpRow.append(exportRight2);
							}
							firRow.append(
									"<td nowrap align=\"center\" colspan=\"")
									.append(realSpan).append("\" class=\"")
									.append(tdClass).append("\" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firRow.append(aTd[index].getContent());
							firRow.append("</td>");

							firExpRow
									.append("<td nowrap align=\"center\" colspan=\"")
									.append(realSpan).append("\" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
						} else {
							// 单层
							firRow.append("<td nowrap align=\"center\" ")
									.append("class=\"").append(tdClass)
									.append("\"").append(" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							StringBuffer html = new StringBuffer(
									aTd[index].getContent());

							html.append(RptOlapDimUtil.genSortLink(tCol,
									tableCols, RptOlapConsts.SORT_THIS_PERIOD,
									olapFun));
							firRow.append(html);
							firRow.append("</td>");
							tCol.setColHeadHTML1(html.toString());

							firExpRow
									.append("<td nowrap align=\"center\" rowspan=\"")
									.append(aTd[index].getRowspan())
									.append("\">");
							firExpRow.append(aTd[index].getContent());
							firExpRow.append("</td>");
						}
					}
				}
			}
		}
		sHead.add(left.toString());
		firRow.append("</tr>");
		secRow.append("</tr>");
		firRow.append(secRow);

		firExpRow.append("</tr>");
		secExpRow.append("</tr>");
		firExpRow.append(secExpRow);

		if (thrRow.length() > 0) {
			thrRow.append("</tr>");
			firRow.append(thrRow);
		}
		if (thrExpRow.length() > 0) {
			thrExpRow.append("</tr>");
			firExpRow.append(thrExpRow);
		}
		exportHead.clear();
		exportHead.add(firExpRow.toString());
		sHead.add(firRow.toString());
		return sHead;
	}

	private String getAssignedRow(String head, int rowNo) {
		String sRow = "";
		String tmpStr = head;
		// 由于下面的正则表达式不匹配回车
		tmpStr = tmpStr.replaceAll("\\\n", "");
		String reg = "<tr([^>]*)>(\\s*.*?)</tr>";
		Pattern p = null;
		p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		int rowCount = 0;
		Matcher matchs = p.matcher(tmpStr);
		while (matchs.find()) {
			rowCount++;
			if (rowNo == rowCount) {
				sRow = matchs.group(2);
			}
		}
		return sRow;
	}

	private List parseTabHead(String tabHead) {
		List tds = null;
		// 先要获取那些列
		tds = new ArrayList();
		String head = tabHead;
		// 获取哪一行呢
		head = getAssignedRow(tabHead, 1);
		String reg = "<td([^>]*)>(.*?)</td>";
		Pattern p = null;
		p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher matchs = p.matcher(head);
		while (matchs.find()) {
			RptOlapTDStruct tdObj = new RptOlapTDStruct();
			tdObj.setContent(matchs.group(2));
			String props = matchs.group(1);
			props = props.toLowerCase();
			String rowspan = getPropValue(props, "rowspan=");
			if (null != rowspan)
				tdObj.setRowspan(rowspan);
			String colspan = getPropValue(props, "colspan=");
			if (null != colspan)
				tdObj.setColspan(colspan);
			String style = getPropValue(props, "class=");
			if (null != style)
				tdObj.setStyle(style);
			String align = getPropValue(props, "align=");
			if (null != align)
				tdObj.setAlign(align);
			tds.add(tdObj);
		}
		return tds;
	}

	/**
	 * 获取表格定义的TD定义中的某个属性
	 * 
	 * @param props
	 *            属性列表串
	 * @param prop
	 *            属性
	 * @return 属性值
	 */
	private String getPropValue(String props, String prop) {
		String value = null;
		if (null != props) {
			String lookfor = prop;
			int index = props.indexOf(lookfor);
			if (index >= 0) {
				String restPart = props.substring(index + lookfor.length());
				int space_index = restPart.indexOf(" ");
				if (space_index >= 0) {
					String content = restPart.substring(0, space_index);
					content = content.replaceAll("\"", "");
					content = content.replaceAll("'", "");
					value = content;
				} else {
					restPart = restPart.replaceAll("\"", "");
					restPart = restPart.replaceAll("'", "");
					value = restPart;
				}
			}
		}
		return value;
	}

	public List getExportHead() {
		return exportHead;
	}

}
