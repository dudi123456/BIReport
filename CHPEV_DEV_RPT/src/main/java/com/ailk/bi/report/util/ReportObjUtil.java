package com.ailk.bi.report.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.HtmlPosStruct;
import com.ailk.bi.report.struct.ReportQryStruct;

import org.apache.log4j.Logger;

/**
 * 报表相关信息的处理
 *
 * @author renhui
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportObjUtil {

	private Logger logger = Logger.getLogger(ReportObjUtil.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#getReports(java.lang.String)
	 */
	public static String[][] getReportChart(String rpt_id) throws AppException {
		String strSql = "select rpt_id,chart_id from ui_rpt_info_chart";
		if (StringTool.checkEmptyString(rpt_id)) {
			return null;
		}
		strSql += " where rpt_id='" + rpt_id + "'";
		// logger.debug("chart list=" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		return svces;
	}

	/**
	 * 查询语句是否需要日期字段
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	public static boolean hasQryDateCode(RptResourceTable rptTable,
			List rptColTable) {
		// 是否需要提取数据库中的日期字段
		boolean isTrue = false;
		if (rptTable.displaydate) {
			return true;
		}

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			// 是否默认显示
			if (!ReportConsts.YES.equals(dict.default_display)) {
				continue;
			}
			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				continue;
			}

			if (ReportConsts.YES.equals(dict.has_same)) {
				isTrue = true;
			}
			if (ReportConsts.YES.equals(dict.has_last)) {
				isTrue = true;
			}
			if (ReportConsts.YES.equals(dict.need_alert)
					&& (ReportConsts.ALERT_COMPARE_TO_SAME_PERIOD
							.equals(dict.compare_to) || ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD
							.equals(dict.compare_to))) {
				isTrue = true;
			}
			if (isTrue) {
				break;
			}
		}
		return isTrue;
	}

	/**
	 * 是否为有合计行的报表
	 *
	 * @param type
	 * @return
	 */
	public static boolean hasSumRow(String type, List rptColTable) {
		if (type == null || type.length() != 3) {
			return false;
		}

		// 是否有合计行
		boolean isTrue = false;
		if ("201".equals(type)) {
			isTrue = true;
		}
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			// 是否显示占比
			if (ReportConsts.YES.equals(dict.has_comratio)) {
				isTrue = true;
				break;
			}
		}
		return isTrue;
	}

	/**
	 * 是否有小计行的报表
	 *
	 * @param rptColTable
	 * @return
	 */
	public static boolean hasSubSumRow(List rptColTable) {
		// 是否有小计行
		boolean isTrue = false;
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			// 是否默认显示
			if (!ReportConsts.YES.equals(dict.default_display)) {
				continue;
			}

			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)
					&& ReportConsts.YES.equals(dict.is_subsum)) {
				isTrue = true;
				break;
			}
		}
		return isTrue;
	}

	/**
	 * 获取表格起始部分需要合并列长度
	 *
	 * @param rptTable
	 * @param dicts
	 * @return
	 */
	public static int getDimLen(RptResourceTable rptTable, List rptColTable) {
		int iLen = 0;
		// 特殊显示方式
		if (ReportConsts.DIS_SEQUENCE_COL.equals(rptTable.row_flag)
				|| ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
			iLen = iLen + 1;
		} else if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
			iLen = iLen + 2;
		}

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			// 是否默认显示
			if (!ReportConsts.YES.equals(dict.default_display)) {
				continue;
			}

			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ReportConsts.YES.equals(dict.dim_code_display)) {
					iLen++;
				}
				iLen++;
			}
			if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
				break;
			}
		}
		return iLen;
	}

	/**
	 * 获取需要显示数据字段长度
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	public static int getDictLen(RptResourceTable rptTable, List rptColTable) {
		int iLen = 0;
		// 特殊显示方式
		if (ReportConsts.DIS_SEQUENCE_COL.equals(rptTable.row_flag)
				|| ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
			iLen = iLen + 1;
		} else if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
			iLen = iLen + 2;
		}

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			// 是否默认显示
			if (!ReportConsts.YES.equals(dict.default_display)) {
				continue;
			}

			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ReportConsts.YES.equals(dict.dim_code_display)) {
					iLen++;
				}
				iLen++;
			}
			if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
				iLen++;
				if (ReportConsts.YES.equals(dict.has_comratio)) {
					iLen++;
				}
				if (ReportConsts.YES.equals(dict.has_same)) {
					iLen++;
				}
				if (ReportConsts.YES.equals(dict.has_last)) {
					iLen++;
				}
			}
		}
		return iLen;
	}

	/**
	 * 是否需要提取同比数据的报表
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @return
	 */
	public static boolean hasSameData(RptResourceTable rptTable,
			List rptColTable, ReportQryStruct qryStruct) {
		// 是否需要同比值
		boolean isTrue = false;
		// 目前只有日报和月报支持同比
		if (SysConsts.STAT_PERIOD_MONTH.equals(rptTable.cycle)
				|| SysConsts.STAT_PERIOD_DAY.equals(rptTable.cycle)) {
			// 区间日期的报表,日期区间跨段不支持同比
			if (SysConsts.STAT_PERIOD_PART.equals(rptTable.start_date)
					&& qryStruct != null
					&& !qryStruct.date_s.equals(qryStruct.date_e)) {
				return false;
			}
			Iterator iter = rptColTable.iterator();
			while (iter.hasNext()) {
				RptColDictTable dict = (RptColDictTable) iter.next();
				// 是否默认显示
				if (!ReportConsts.YES.equals(dict.default_display)) {
					continue;
				}

				if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					if (ReportConsts.YES.equals(dict.has_same)) {
						// 需要显示同比列的报表
						isTrue = true;
					}
					if (ReportConsts.YES.equals(dict.need_alert)
							&& ReportConsts.ALERT_COMPARE_TO_SAME_PERIOD
									.equals(dict.compare_to)) {
						// 需要用同比预警的报表
						isTrue = true;
					}
					if (isTrue) {
						break;
					}
				}
			}
		}
		return isTrue;
	}

	/**
	 * 是否需要提取环比数据的报表
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @return
	 */
	public static boolean hasLastData(RptResourceTable rptTable,
			List rptColTable, ReportQryStruct qryStruct) {
		// 是否需要同比值
		boolean isTrue = false;
		// 目前只有日报和月报支持环比
		if (SysConsts.STAT_PERIOD_MONTH.equals(rptTable.cycle)
				|| SysConsts.STAT_PERIOD_DAY.equals(rptTable.cycle)) {
			// 区间日期的报表,日期区间跨段不支持环比
			if (SysConsts.STAT_PERIOD_PART.equals(rptTable.start_date)
					&& qryStruct != null
					&& !qryStruct.date_s.equals(qryStruct.date_e)) {
				return false;
			}
			Iterator iter = rptColTable.iterator();
			while (iter.hasNext()) {
				RptColDictTable dict = (RptColDictTable) iter.next();
				if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					if (ReportConsts.YES.equals(dict.has_last)) {
						// 需要显示环比列的报表
						isTrue = true;
					}
					if (ReportConsts.YES.equals(dict.need_alert)
							&& ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD
									.equals(dict.compare_to)) {
						// 需要用环比预警的报表
						isTrue = true;
					}
					if (isTrue) {
						break;
					}
				}
			}
		}
		return isTrue;
	}

	/**
	 * 获取第一个展开列序号
	 *
	 * @param rptColTable
	 * @return
	 */
	public static String getExpandFirstCol(List rptColTable) {
		String ret = "-1";
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if (ReportConsts.YES.equals(dict.is_expand_col)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				ret = dict.col_sequence;
				break;
			}
		}
		return ret;
	}

	/**
	 * 获取最后一个展开列序号
	 *
	 * @param rptColTable
	 * @return
	 */
	public static String getExpandLastCol(List rptColTable) {
		String ret = "-1";
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if (ReportConsts.YES.equals(dict.is_expand_col)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				ret = dict.col_sequence;
			}
		}
		return ret;
	}

	/**
	 * 获取合并固定表头colspan个数
	 *
	 * @param rptColTable
	 * @param expandcol
	 * @return
	 */
	public static String getExpandColSpanNum(List rptColTable, String expandcol) {
		int colspan = 0;
		if (!StringTool.checkEmptyString(expandcol)) {
			String direction = expandcol.substring(0, 1);
			int expcol = Integer.parseInt(expandcol.substring(1));

			for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
				RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
				int iCol = Integer.parseInt(dict.col_sequence);
				if (ReportConsts.DIRECTION_DOWN.equals(direction)) {
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING
									.equals(dict.data_type)) {
						if (iCol > expcol && i < rptColTable.size() - 1) {
							colspan++;
							break;
						} else {
							colspan++;
						}
					}
				} else if (ReportConsts.DIRECTION_UP.equals(direction)) {
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING
									.equals(dict.data_type)) {
						if (iCol < expcol && i < rptColTable.size() - 1) {
							colspan++;
						} else if (iCol >= expcol) {
							break;
						}
					}
				}
			}
		}

		String ret = "";
		if (colspan > 1) {
			ret = "colspan=" + colspan;
		}
		return ret;
	}

	/**
	 * 获取向上合并的连接
	 *
	 * @param rptColTable
	 * @param expandcol
	 * @param url
	 * @return
	 */
	public static String getExpandDirectionUp(List rptColTable,
			String expandcol, String now_sequence, String url) {
		String ret = "";
		if (!StringTool.checkEmptyString(expandcol) && expandcol.length() == 2) {
			int colFirst = Integer.parseInt(getExpandFirstCol(rptColTable));
			int colLast = Integer.parseInt(getExpandLastCol(rptColTable));
			String direction = expandcol.substring(0, 1);
			int expcol = Integer.parseInt(expandcol.substring(1));

			for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
				RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
				int iCol = Integer.parseInt(dict.col_sequence);
				if (ReportConsts.DIRECTION_DOWN.equals(direction)
						&& iCol == expcol && iCol < colLast) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i + 1);
					if (now_sequence == null
							|| now_sequence.equals(tmpdict.col_sequence)) {
						ret += "<a href=\"" + url
								+ "&p_condition=Y&expandcol=u"
								+ tmpdict.col_sequence + "\">";
						ret += "<img src=\"../images/left_arrow.gif\" width=\"9\" height=\"9\" border=\"0\"></a>&nbsp;";
					}
				} else if (ReportConsts.DIRECTION_UP.equals(direction)
						&& iCol == expcol && iCol > colFirst) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i - 1);
					int itmpCol = Integer.parseInt(tmpdict.col_sequence);
					if (itmpCol > colFirst) {
						if (now_sequence == null
								|| now_sequence.equals(tmpdict.col_sequence)) {
							ret += "<a href=\"" + url
									+ "&p_condition=Y&expandcol=u"
									+ tmpdict.col_sequence + "\">";
							ret += "<img src=\"../images/left_arrow.gif\" width=\"9\" height=\"9\" border=\"0\"></a>&nbsp;";
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 获取向下合并的连接
	 *
	 * @param rptColTable
	 * @param expandcol
	 * @param url
	 * @return
	 */
	public static String getExpandDirectionDown(List rptColTable,
			String expandcol, String now_sequence, String url) {
		String ret = "";
		if (!StringTool.checkEmptyString(expandcol)) {
			int colFirst = Integer.parseInt(getExpandFirstCol(rptColTable));
			int colLast = Integer.parseInt(getExpandLastCol(rptColTable));
			String direction = expandcol.substring(0, 1);
			int expcol = Integer.parseInt(expandcol.substring(1));

			for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
				RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
				int iCol = Integer.parseInt(dict.col_sequence);
				if (ReportConsts.DIRECTION_DOWN.equals(direction)
						&& iCol == expcol && iCol < colLast) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i + 1);
					int itmpCol = Integer.parseInt(tmpdict.col_sequence);
					if (itmpCol < colLast) {
						if (now_sequence == null
								|| now_sequence.equals(tmpdict.col_sequence)) {
							ret += "&nbsp;<a href=\"" + url
									+ "&p_condition=Y&expandcol=d"
									+ tmpdict.col_sequence + "\">";
							ret += "<img src=\"../images/right_arrow.gif\" width=\"9\" height=\"9\" border=\"0\"></a>";
						}
					}
				} else if (ReportConsts.DIRECTION_UP.equals(direction)
						&& iCol == expcol && iCol > colFirst) {
					RptColDictTable tmpdict = (RptColDictTable) rptColTable
							.get(i - 1);
					if (now_sequence == null
							|| now_sequence.equals(tmpdict.col_sequence)) {
						ret += "&nbsp;<a href=\"" + url
								+ "&p_condition=Y&expandcol=d"
								+ tmpdict.col_sequence + "\">";
						ret += "<img src=\"../images/right_arrow.gif\" width=\"9\" height=\"9\" border=\"0\"></a>";
					}
				}
			}
		}
		return ret;
	}

	// 解析复杂表头的html，如果有下钻或排序则做标志
	public static String getHtmlparser(String rptHead, String rpt_id) {
		ILReportService rptService = new LReportServiceImpl();
		List rptColTable = new ArrayList();
		try {
			rptColTable = rptService.getReportColDefine(rpt_id);
		} catch (AppException e) {

			e.printStackTrace();
		}
		String[] arrTr = rptHead.split("</tr>");
		String[][] arrTd = new String[arrTr.length][];
		int i = 0;
		int j = 0;
		int len = arrTr.length;

		for (i = 0; i < arrTr.length; i++) {
			arrTr[i] = arrTr[i].trim();
			arrTd[i] = arrTr[i].split("</td>");
			// 去掉每个<td前面的字符
			for (j = 0; j < arrTd[i].length; j++) {
				int tdpos = arrTd[i][j].indexOf("<td");
				arrTd[i][j] = arrTd[i][j].substring(tdpos);
			}
		}

		// 判断第一列是否要下钻，如果要设标志
		for (int k = 0; k < rptColTable.size(); k++) {
			RptColDictTable colDict = (RptColDictTable) rptColTable.get(k);
			if (ReportConsts.NO.equals(colDict.default_display))
				continue;
			if ("2".equals(colDict.data_type)
					&& ReportConsts.YES.equals(colDict.is_expand_col)) {
				int firstTd = arrTd[0][0].indexOf("rowspan");
				String temp = "";
				if (firstTd > 0) {
					temp = arrTd[0][0].substring(0, firstTd) + " #colspan# "
							+ arrTd[0][0].substring(firstTd);
				}
				rptHead = rptHead.replace(arrTd[0][0],
						getColName(temp, "#direction_up#", "#direction_down#"));
				break;
			}
		}
		rptHead = getSpecialDeal(rptHead, rptColTable, len, arrTd);
		return rptHead;
	}

	public static String getSpecialDeal(String rptHead, List rptColTable,
			int len, String[][] arrTd) {
		int i = 0, j = 0;
		boolean colFlag = false;
		HtmlPosStruct[] hp = new HtmlPosStruct[len];
		HashMap map = new HashMap();
		for (i = 0; i < len; i++) {
			hp[i] = new HtmlPosStruct();
		}
		i = 0;
		for (int k = 0; k < rptColTable.size(); k++) {
			RptColDictTable colDict = (RptColDictTable) rptColTable.get(k);
			if (ReportConsts.NO.equals(colDict.default_display))
				continue;

			while (i < len && j < arrTd[i].length) {
				//先零时解决
				String temp = StringB.replace(arrTd[i][j].trim(),")","）");
				temp = StringB.replace(temp,"(","（");
				int colpos = temp.indexOf("colspan");

				if (colpos > 0) {
					String[] tdColspan = (temp.substring(colpos)).split("\"");
					hp[i].colspan = Integer.parseInt(tdColspan[1]);
					colFlag = true;
					hp[i].pos = j + 1; // 定位当前tr行的下一个td
					++i;
					j = hp[i].pos;
				} else {
					hp[i].pos = j + 1;
					j = hp[i].pos;
					int c = 0;
					// 如果上个<tr>的colspan大于0，则逐级递减colspan数量
					if (i >= 1 && hp[i - 1].colspan >= 1) {
						for (c = i; c >= 0; c--) {
							if (hp[c].colspan >= 1)
								--hp[c].colspan;
						}
					}
					if ("1".equals(colDict.data_type)) {
						int rptPos = 0;
						String[] isRepeatCol = rptHead.split(temp);
						if (isRepeatCol.length > 2) {
							// 如果有相同的列名，就从上次定位的基础上重新定位
							if (map.get(temp) == null) {
								rptPos = rptHead.indexOf(temp);
								map.put(temp, rptPos + "");
							} else {
								rptPos = rptHead
										.indexOf(temp, Integer.parseInt((map
												.get(temp) + "")) + 1);
								map.put(temp, rptPos + "");
							}
						} else {
							rptPos = rptHead.indexOf(temp);
						}
						if (ReportConsts.YES.equals(colDict.data_order)) {
							String iCol = colDict.col_sequence;
							String str = rptHead.substring(0, rptPos);
							rptHead = rptHead.replace(
									str + temp,
									str
											+ getColName(temp, "#order" + iCol
													+ "#", null));
						}
					}
					if (i >= 1 && hp[i - 1].colspan == 0 && colFlag) {
						--i;
						j = hp[i].pos;
						// 如果上层<td>已经扫描完毕，则指向上上层
						if (i >= 1 && j == arrTd[i].length) {
							--i;
							j = hp[i].pos;
						}
						// 逐级扫描colspan，如果为0，则指向上层
						for (c = i; c >= 1; c--) {
							if (hp[c - 1].colspan == 0) {
								--i;
								j = hp[i].pos;
							}
						}
						for (c = i; c >= 0; c--) {
							if (hp[c].colspan != 0) {
								colFlag = true;
								break;
							} else {
								colFlag = false;
							}
						}
					}
					break;
				}
			}
		}

		return rptHead;
	}

	public static String getColName(String str, String rep1, String rep2) {
		String[] arrStr = str.split(">");
		String tempFirst = "";
		String temp = "";
		for (int s = 0; s < arrStr.length; s++) {
			arrStr[s] = arrStr[s].trim();
			tempFirst += arrStr[s];
			if (s != arrStr.length - 1) {
				tempFirst += ">";
			}
		}
		int pos = tempFirst.indexOf(">");

		while (pos >= 0) {
			if (!"<".equals(tempFirst.substring(pos + 1, pos + 2))) {
				int last = tempFirst.indexOf("<", pos + 1);
				if (rep2 != null) {
					if (last >= 0)
						temp = tempFirst.substring(0, pos + 1) + rep1
								+ tempFirst.substring(pos + 1, last) + rep2
								+ tempFirst.substring(last);
					else
						temp = tempFirst.substring(0, pos + 1) + rep1
								+ tempFirst.substring(pos + 1) + rep2;
				} else {
					if (last >= 0)
						temp = tempFirst.substring(0, pos + 1)
								+ tempFirst.substring(pos + 1, last) + rep1
								+ tempFirst.substring(last);
					else
						temp = tempFirst.substring(0, pos + 1)
								+ tempFirst.substring(pos + 1) + rep1;
				}
				break;
			}
			int ff = tempFirst.indexOf(">", pos + 1);
			pos = ff;

		}
		return temp;
	}

	/**
	 * 处理本地化表头
	 *
	 * @param rptHead
	 * @param rptColTable
	 * @param expandcol
	 * @param url
	 * @return
	 */
	public static String genLocalRptHead(String rptHead, List rptColTable,
			String expandcol, String url, String order, String order_code) {
		String rpt_id = "";
		for (int k = 0; k < rptColTable.size(); k++) {
			RptColDictTable colDict = (RptColDictTable) rptColTable.get(k);
			rpt_id = colDict.rpt_id;
		}
		rptHead = getHtmlparser(rptHead, rpt_id);
		String colspan = getExpandColSpanNum(rptColTable, expandcol);
		rptHead = StringB.replace(rptHead, "#colspan#", colspan);
		String up = getExpandDirectionUp(rptColTable, expandcol, null, url);
		rptHead = StringB.replace(rptHead, "#direction_up#", up);
		String down = getExpandDirectionDown(rptColTable, expandcol, null, url);
		rptHead = StringB.replace(rptHead, "#direction_down#", down);
		rptHead = getOrderCol(rptColTable, rptHead, order, order_code);
		return rptHead;
	}

	// 复杂表头排序
	public static String getOrderCol(List rptColTable, String head,
			String order, String order_code) {
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if ("2".equals(dict.data_type))
				continue;
			int iCol = Integer.parseInt(dict.col_sequence);
			if (ReportConsts.YES.equals(dict.data_order)
					&& head.indexOf("#order" + iCol + "#") > 0) {
				String temp = "";
				if (("MSU" + iCol).equals(order_code)) {
					temp += "<a href=\"javascript:_order('";
					temp += "MSU" + iCol + "',";
					if ("DESC".equals(order)) {
						temp += "'ASC')\">";
						temp += "<img src=\"../images/menu_down.gif\" width=\"9\" border=\"0\">";
					} else {
						temp += "'DESC')\">";
						temp += "<img src=\"../images/menu_up.gif\" width=\"9\" border=\"0\">";
					}
					temp += "</a>";
				} else {
					temp += "<a href=\"javascript:_order('";
					temp += "MSU" + iCol + "',";
					temp += "'" + order + "')\">";
					temp += "<img src=\"../images/menu_init.gif\" width=\"9\" border=\"0\">";
					temp += "</a>";
				}
				head = StringB.replace(head, "#order" + iCol + "#", temp);
			}

		}

		return head;
	}

	/**
	 * 计算左侧列合并行列数
	 *
	 * @param iAddCol
	 * @param rptColTable
	 * @param result
	 * @param rows
	 * @param cols
	 */
	public static void genLeftArr(int iAddCol, List rptColTable,
			String[][] result, String[][] rows, String[][] cols) {
		// 定义行标记索引
		int iRow = 0;
		// 定义合并行数
		int iRowCount = 0;
		// 定义列标记索引
		int iCol = 0;
		// 定义合并列数
		int iColCount = 0;
		// 需要显示的结果行数,如果报表显示合计行，记录行数为全部，否则减一
		int iLen2 = result.length;

		// 列定义对象
		RptColDictTable[] dicts = (RptColDictTable[]) rptColTable
				.toArray(new RptColDictTable[rptColTable.size()]);

		for (int j = 0; j < dicts.length; j++) {
			if (!ReportConsts.YES.equals(dicts[j].default_display)) {
				continue;
			}
			// 字段类型为字符型符合要求
			if (ReportConsts.DATA_TYPE_STRING.equals(dicts[j].data_type)) {
				if (j > 0
						&& ReportConsts.DATA_TYPE_NUMBER
								.equals(dicts[j - 1].data_type)) {
					break;
				}

				for (int i = 0; i < iLen2; i++) {
					// 当前行值
					String sCur = result[i][2 * j + iAddCol].trim();
					// 当前行前一列值
					String sCurCol = "";
					if (j > 0) {
						sCurCol = result[i][2 * j + 1].trim();
					}
					// 前一行值
					String sOld = "";
					if (i > 0) {
						sOld = result[i - 1][2 * j + iAddCol].trim();
					}
					// 前一行前一列值
					String sOldCol = "";
					if (i > 0 && j > 0) {
						sOldCol = result[i - 1][2 * j + 1].trim();
					}
					// 当前值的前两列值
					String sBeforeVal1 = "";
					String sBeforeVal2 = "";
					// 上行同列的前两列值
					String sOldBeforeVal1 = "";
					String sOldBeforeVal2 = "";
					if (j >= 2) {
						sBeforeVal1 = result[i][2 * j].trim();
						sBeforeVal2 = result[i][2 * j - 2].trim();
					}
					if (i > 0 && j >= 2) {
						sOldBeforeVal1 = result[i - 1][2 * j].trim();
						sOldBeforeVal2 = result[i - 1][2 * j - 2].trim();
					}

					if (i == 0) { // 第一行处理
						// 初始化行标记索引
						iRow = 0;
						// 初始化合并行数
						iRowCount = 1;
						if (j == 0) {
							// 第一行第一列不管是否为空都将显示
							rows[iRow][j] = "1";
						} else if (sCur == null || "".equals(sCur)) {
							// 当前值为null或者空即向前合并列
							rows[iRow][j] = null;
							iCol = j - 1; // 初始化合并列索引
							iColCount = 2; // 初始化合并列数
							for (int k = j - 1; k > 1; k--) {
								String tmpOld = result[i][2 * k + iAddCol]
										.trim();
								if (tmpOld == null || "".equals(tmpOld)) {
									iColCount++;
									iCol = k - 1;
								}
							}
							cols[iRow][iCol] = iColCount + " align=center";
						}
					} else if (i == iLen2 - 1) { // 最后一行处理
						if (sCurCol.equals(sOldCol) && sCur.equals(sOld)
								&& sCur != null && !"".equals(sCur)) {
							// 当最后一行也符合行合并条件，直接合并
							iRowCount = iRowCount + 1;
							rows[iRow][j] = Integer.toString(iRowCount);
						} else {
							if (j > 0 && (sOld == null || "".equals(sOld))) {
								rows[iRow][j] = null;
								iCol = j - 1; // 初始化列索引
								iColCount = 2; // 初始化列数
								for (int k = j - 1; k > 0; k--) {
									String tmpOld = result[i][2 * k + iAddCol]
											.trim();
									if (tmpOld == null || "".equals(tmpOld)) {
										iColCount++;
										iCol = k - 1;
									}
								}
								cols[iRow][iCol] = iColCount + " align=center";
							} else {
								rows[iRow][j] = Integer.toString(iRowCount);
							}
							iRow = i;
							if (j > 0 && (sCur == null || "".equals(sCur))) {
								rows[iRow][j] = null;
								iCol = j - 1; // 初始化列索引
								iColCount = 2; // 初始化列数
								for (int k = j - 1; k > 0; k--) {
									String tmpOld = result[i][2 * k + iAddCol]
											.trim();
									if (tmpOld == null || "".equals(tmpOld)) {
										iColCount++;
										iCol = k - 1;
									}
								}
								cols[iRow][iCol] = iColCount + " align=center";
							} else {
								rows[iRow][j] = "1";
							}
						}
					} else if (!sCurCol.equals(sOldCol) || !sCur.equals(sOld)
							|| sCur == null || "".equals(sCur)
							|| !sBeforeVal1.equals(sOldBeforeVal1)
							|| !sBeforeVal2.equals(sOldBeforeVal2)) { // 其他行处理
						if (j > 0 && (sCur == null || "".equals(sCur))) {
							if (sOld != null && !"".equals(sOld)) {
								rows[iRow][j] = Integer.toString(iRowCount);

								iRow = iRow + 1;
								rows[iRow][j] = null;
								iCol = j - 1; // 初始化列索引
								iColCount = 2; // 初始化列数
								for (int k = j - 1; k > 0; k--) {
									String tmpOld = result[i][2 * k + iAddCol]
											.trim();
									if (tmpOld == null || "".equals(tmpOld)) {
										iColCount++;
										iCol = k - 1;
									}
								}
								cols[iRow][iCol] = iColCount + " align=center";
							} else {
								rows[iRow][j] = null;
								iCol = j - 1; // 初始化列索引
								iColCount = 2; // 初始化列数
								for (int k = j - 1; k > 0; k--) {
									String tmpOld = result[i][2 * k + iAddCol]
											.trim();
									if (tmpOld == null || "".equals(tmpOld)) {
										iColCount++;
										iCol = k - 1;
									}
								}
								cols[iRow][iCol] = iColCount + " align=center";

								if (sCur == null || "".equals(sCur)) {
									iRow = iRow + 1;
									rows[iRow][j] = null;
									iCol = j - 1; // 初始化列索引
									iColCount = 2; // 初始化列数
									for (int k = j - 1; k > 0; k--) {
										String tmpOld = result[i][2 * k
												+ iAddCol].trim();
										if (tmpOld == null || "".equals(tmpOld)) {
											iColCount++;
											iCol = k - 1;
										}
									}
									cols[iRow][iCol] = iColCount
											+ " align=center";
								}
							}
						} else {
							if (j > 0 && (sCur == null || "".equals(sCur))) {
								rows[iRow][j] = null;
								iCol = j - 1; // 初始化列索引
								iColCount = 2; // 初始化列数
								for (int k = j - 1; k > 0; k--) {
									String tmpOld = result[i][2 * k + iAddCol]
											.trim();
									if (tmpOld == null || "".equals(tmpOld)) {
										iColCount++;
										iCol = k - 1;
									}
								}
								cols[iRow][iCol] = iColCount + " align=center";
							} else if (sOld != null && !"".equals(sOld)) {
								rows[iRow][j] = Integer.toString(iRowCount);
							} else {
								rows[iRow][j] = null;
							}
						}
						iRow = i;
						iRowCount = 1;
					} else {
						iRowCount = iRowCount + 1;
					}
				}
			} else {
				// 遇到数值型字段终止循环
				break;
			}
		}
	}

	/**
	 * 构造报表日期导航HTML代码
	 *
	 * @param rptTable
	 *            报表信息
	 * @param date_s
	 *            报表开始日期
	 * @param date_e
	 *            报表结束日期
	 * @return
	 * @throws AppException
	 */
	public static String genDateLayout(HttpSession session, String date_s,
			String date_e) {
		// 报表基本信息
		RptResourceTable rptTable = (RptResourceTable) session
				.getAttribute(WebKeys.ATTR_REPORT_TABLE);
		// 定义返回代码
		String strLay = "";
		// 报表数据周期
		String cycle = rptTable.cycle;
		// 日期描述
		String desc = "报表日期：";
		if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
			// 区间日期更换日期描述名称
			desc = "起始日期：";
		}
		// 当前日期
		Date nowDate = DateUtil.getNowDate();
		String dd = DateUtil.dateToString(nowDate, "yyyy-MM-dd");
		String CurDt[] = dd.split("-");
		int y_now = Integer.parseInt(CurDt[0]);// 得到年

		// 年报
		if (TableConsts.STAT_PERIOD_YEAR.equals(cycle)) {
			strLay += "<td width=\"5%\" nowrap>" + desc + "</td>\n";
			strLay += "<td nowrap><SELECT id=qry__date_s name=qry__date_s>\n";
			for (int i = 1995; i < y_now + 1; i++) {
				strLay += "<OPTION value='" + i + "' >" + i + "</OPTION>\n";
			}
			strLay += "</SELECT></td>\n";
			if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
				strLay += "<td width=\"5%\" nowrap>终止日期：</td>\n";
				strLay += "<td nowrap><SELECT ID=qry__date_e name=qry__date_e>\n";
				for (int i = 1995; i < y_now + 1; i++) {
					strLay += "<OPTION value=" + i + " >" + i + "</OPTION>\n";
				}
				strLay += "</SELECT></td>\n";
			}
		}
		// 月报
		if (TableConsts.STAT_PERIOD_MONTH.equals(cycle)) {
			strLay += "<td width=\"5%\" nowrap>" + desc + "</td>\n";
			//strLay += "<td nowrap><input type=\"text\" size=\"10\" name=\"qry__date_s\" readonly onClick=\"scwShowM(this,this);\" class=\"input-text\" onFocus=\"switchClass(this)\" onBlur=\"switchClass(this)\"></td>\n";
			strLay += "<td nowrap><input class=\"Wdate\" type=\"text\" id=\"qry__date_s\" name=\"qry__date_s\" size=\"15\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\"/></td>\n";
			if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
				strLay += "<td width=\"5%\" nowrap>终止日期：</td>\n";
				strLay += "<td nowrap><input class=\"Wdate\" type=\"text\" id=\"qry__date_e\" name=\"qry__date_e\" size=\"15\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\"/></td>\n";
			}
		}
		// 日报
		if (TableConsts.STAT_PERIOD_DAY.equals(cycle)) {
			strLay += "<td width=\"5%\" nowrap>" + desc + "</td>\n";
			//strLay += "<td nowrap><input type=\"text\" size=\"10\" name=\"qry__date_s\" readonly onClick=\"scwShow(this,this);\" class=\"input-text\" onFocus=\"switchClass(this)\" onBlur=\"switchClass(this)\"></td>\n";
			strLay += "<td nowrap><input class=\"Wdate\" type=\"text\" id=\"qry__date_s\" name=\"qry__date_s\" size=\"15\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\"/></td>\n";
			if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
				strLay += "<td width=\"5%\" nowrap>终止日期：</td>\n";
				//strLay += "<td nowrap><input type=\"text\" size=\"10\" name=\"qry__date_e\" readonly onClick=\"scwShow(this,this);\" class=\"input-text\" onFocus=\"switchClass(this)\" onBlur=\"switchClass(this)\"></td>\n";
				strLay += "<td nowrap><input class=\"Wdate\" type=\"text\" id=\"qry__date_e\" name=\"qry__date_e\" size=\"15\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\"/></td>\n";
			}
		}
		return strLay;
	}

	/**
	 * 得到报表的日期
	 *
	 * @param rptTable
	 *            报表的信息
	 * @return String
	 * @throws AppException
	 */
	public static String genRptDate(RptResourceTable rptTable) {
		// 定义报表日期
		String rptDate = "";
		// 返回数据表有数据的最近日期
		if (TableConsts.RPT_DATE_LAST.equals(rptTable.start_date)
				|| TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
			if ("100".equals(rptTable.rpt_type)
					|| "101".equals(rptTable.rpt_type)
					|| "102".equals(rptTable.rpt_type)
					|| "103".equals(rptTable.rpt_type)) {
				rptDate = genMaxRptDate(rptTable.rpt_id);
			} else {
				rptDate = genMaxRptDate(rptTable);
			}
			if (!"".equals(rptDate)) {
				if (rptDate.length() >= 4
						&& TableConsts.STAT_PERIOD_YEAR.equals(rptTable.cycle)) {
					rptDate = rptDate.substring(0, 4);
				}
				if (rptDate.length() >= 6
						&& TableConsts.STAT_PERIOD_MONTH.equals(rptTable.cycle)) {
					rptDate = rptDate.substring(0, 6);
				}
				return rptDate;
			}
		}

		String tmp_start = rptTable.start_date;
		// 报表默认提取周期
		if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
			tmp_start = "-1";
		}
		if (TableConsts.RPT_DATE_LAST.equals(rptTable.start_date)) {
			tmp_start = "-1";
		}
		int _date = StringB.toInt(tmp_start, -1);
		// 区间日期段返回系统时间上期值
		if (TableConsts.RPT_DATE_PART.equals(rptTable.start_date)) {
			_date = -1;
		}
		// 报表数据周期
		String cycle = rptTable.cycle;
		// 当前日期
		Date nowDate = DateUtil.getNowDate();
		String dd = DateUtil.dateToString(nowDate, "yyyy-MM-dd");
		String CurDt[] = dd.split("-");
		int y = Integer.parseInt(CurDt[0]);// 得到年
		// int m = Integer.parseInt(CurDt[1]);// 得到月
		// int d = Integer.parseInt(CurDt[2]);// 得到日

		// 年报
		if (TableConsts.STAT_PERIOD_YEAR.equals(cycle)) {
			y = y + _date;
			rptDate = new Integer(y).toString();
		}
		// 月报
		if (TableConsts.STAT_PERIOD_MONTH.equals(cycle)) {
			rptDate = DateUtil.getDiffMonth(_date, nowDate);
		}
		// 日报
		if (TableConsts.STAT_PERIOD_DAY.equals(cycle)) {
			rptDate = DateUtil.getDiffDay(_date, nowDate);
		}
		return rptDate;
	}

	/**
	 * 获取报表的最大日期
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public static String genMaxRptDate(RptResourceTable rptTable) {
		String maxDate = "";
		String strSql = "";
		String[][] result = null;
		String data_date = rptTable.data_date;
		String data_table = rptTable.data_table;
		data_table += " " + rptTable.data_where;
		try {
			strSql = SQLGenator.genSQL("Q3010", data_date, data_table);
			// logger.debug("===Q3010 SQL===" + strSql);
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (result == null || result.length == 0)
			maxDate = "";
		else
			maxDate = result[0][0].trim();

		if (maxDate != null && maxDate.length() > 6
				&& TableConsts.STAT_PERIOD_MONTH.equals(rptTable.cycle)) {
			maxDate = maxDate.substring(0, 6);
		}
		return maxDate;
	}

	/**
	 * 获取报表的最大日期
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public static String genMaxRptDate(String rpt_id) {
		String maxDate = "";
		String strSql = "";
		String[][] result = null;
		try {
			strSql = SQLGenator.genSQL("Q3009", rpt_id);
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (result == null || result.length == 0)
			maxDate = "";
		else
			maxDate = result[0][0].trim();
		return maxDate;
	}

	/**
	 * genRptTree 生成报表左侧目录树
	 *
	 * @param dataArr
	 *            展现数据集合：dataArr[i][0]存放名称；dataArr[i][1]存放URL连接；dataArr[i][2]
	 *            存放parentID；
	 * @param parentID
	 *            第一层目录中对应的parent_id
	 * @return
	 */
	public static String genRptTree(String[][] dataArr) throws AppException {
		if (dataArr == null || dataArr.length == 0)
			return "";

		String menu_name = "";
		String result = "<script>  var t ;";
		for (int i = 0; i < dataArr.length; i++) {
			if (i == 0) {
				menu_name = dataArr[i][1];
				result += " t=outlookbar.addtitle('" + dataArr[i][1] + "');";
				result += " outlookbar.additem('" + dataArr[i][3] + "',t,'"
						+ dataArr[i][4] + "');";
			} else {
				if (!menu_name.equals(dataArr[i][1])) {
					menu_name = dataArr[i][1];
					result += " t=outlookbar.addtitle('" + dataArr[i][1]
							+ "');";
					result += " outlookbar.additem('" + dataArr[i][3] + "',t,'"
							+ dataArr[i][4] + "');";
				} else {
					result += " outlookbar.additem('" + dataArr[i][3] + "',t,'"
							+ dataArr[i][4] + "');";
				}
			}
		}
		return result + " </script>";
	}

	/**
	 * 普通报表资源数据数据
	 *
	 * @param parentID
	 * @return
	 * @throws AppException
	 */
	public static String[][] getCommonRptArr(String parent_id)
			throws AppException {
		if (parent_id == null || "".equals(parent_id.trim()))
			return null;

		// 目前缺少角色权限的控制
		String strSql = SQLGenator.genSQL("Q3002", parent_id);
		// logger.debug("genCommonRptArr=" + strSql);
		return WebDBUtil.execQryArray(strSql, "");
	}

	/**
	 * 我的报表资源数据
	 *
	 * @param user_id
	 * @param user_role
	 * @param parent_id
	 * @return
	 * @throws AppException
	 */
	public static String[][] getMyRptArr(String user_id, String user_role,
			String parent_id) throws AppException {
		if (StringTool.checkEmptyString(parent_id))
			return null;

		String strSql = SQLGenator.genSQL("Q3004", parent_id, user_id);
		if (!StringTool.checkEmptyString(user_role)) {
			strSql += " UNION ";
			strSql += SQLGenator.genSQL("Q3005", user_role, parent_id, user_id);
		}
		strSql += " ORDER BY PARENT_ID, SEQUENCE, RPT_ID";
		// logger.debug("getMyRptArr=" + strSql);
		return WebDBUtil.execQryArray(strSql, "");
	}
}