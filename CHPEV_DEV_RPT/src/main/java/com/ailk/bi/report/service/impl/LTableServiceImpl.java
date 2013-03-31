package com.ailk.bi.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.CObjKnd;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.SortUtils;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportDataDao;
import com.ailk.bi.report.dao.ILReportHeadDao;
import com.ailk.bi.report.dao.IMReportDataDao;
import com.ailk.bi.report.dao.impl.LReportDataDao;
import com.ailk.bi.report.dao.impl.LReportHeadDao;
import com.ailk.bi.report.dao.impl.MReportDataDao;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptMeasureShowTable;
import com.ailk.bi.report.domain.RptMeasureTable;
import com.ailk.bi.report.domain.RptProcessHisTable;
import com.ailk.bi.report.domain.RptProcessStateTable;
import com.ailk.bi.report.domain.RptProcessStepTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.ILTableService;
import com.ailk.bi.report.struct.DimRuleStruct;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.report.util.ReportObjUtil;
import com.ailk.bi.report.util.ReportProcessUtil;
import com.ailk.bi.system.facade.impl.ServiceFacade;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class LTableServiceImpl implements ILTableService {

	private ILReportDataDao dataDao = new LReportDataDao();

	private IMReportDataDao imDataDao = new MReportDataDao();

	private ILReportHeadDao commentDataDao = new LReportHeadDao();

	private static String alert_msg = "";

	private static Map headMap = null;

	private static Map fieldMap = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ailk.bi.report.service.ILTableService#getReportTitle(java.lang
	 * .Object, java.lang.Object)
	 */
	public String getReportTitle(Object rptTable, Object qryStruct) {
		StringBuffer ret = new StringBuffer();

		RptResourceTable report = (RptResourceTable) rptTable;
		ReportQryStruct query = (ReportQryStruct) qryStruct;
		// 日期说明
		String strDate = StringTool.getCnDate(query.date_s, report.cycle);
		if (SysConsts.STAT_PERIOD_PART.equals(report.start_date)) {
			strDate += " 至 " + StringTool.getCnDate(query.date_e, report.cycle);
		}
		// 其他说明1
		if (report.title_note == null) {
			report.title_note = "";
		}

		// 报表标题部分
		ret.append("<tr><td align=\"center\" colspan=\"2\"><div class=\"listtitle\">");
		ret.append("<span class=\"bigtitle\">" + report.name + "</span></div></td></tr>");
		// 表头显示的报表相关信息
		ret.append("<TR><TD colspan=\"2\">");
		ret.append("<TABLE width=\"100%\">");
		String[][] comment = commentDataDao.getGLRptComment(report.rpt_id);

		if (comment != null && comment.length > 0) {
			String head = comment[0][0];

			if (head.indexOf("#rpt_date#") > 0) {
				head = head.replace("#rpt_date#", strDate);
			}
			if (head.indexOf("#rpt_id#") > 0) {
				head = head.replace("#rpt_id#", report.rpt_id);
			}
			String creatDate = StringB.NulltoBlank(commentDataDao.getGLRptCreateDate(report.rpt_id,
					query.date_s));
			if (head.indexOf("#creat_date#") > 0) {
				head = head.replace("#creat_date#", creatDate);
			}
			if (head.indexOf("#dept_name#") > 0) {
				ServiceFacade service = new ServiceFacade();
				String deptName = StringB.NulltoBlank(service.getDeptName(report.fillperson));
				head = head.replace("#dept_name#", deptName);
			}
			if (head.indexOf("#title_note#") > 0) {
				head = head.replace("#title_note#", report.title_note);
			}
			ret.append(head);
		} else {
			ret.append("<TR>\n");
			ret.append("<TD nowrap align=\"left\">统计日期：" + strDate + "</TD>\n");
			ret.append("<TD nowrap align=\"right\">" + report.title_note + "</TD>\n");
			ret.append("</TR>\n");
		}
		ret.append("</TABLE>");
		ret.append("</TD></TR>\n");
		return ret.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ailk.bi.report.service.ILTableService#getRptBottom(com.ailk
	 * .bi.report.domain.RptResourceTable)
	 */
	public void getRptBottom(RptResourceTable rptTable) {
		String[][] comment = commentDataDao.getGLRptComment(rptTable.rpt_id);

		if (comment != null && comment.length > 0) {
			rptTable.rp_bottom = comment[0][1];
		}
	}

	/*
	 * 获取固定报表的表体。
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getReportBody(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	public String[] getReportBody(Object rptTable, Object listReportCol, Object qryStruct,
			PubInfoConditionTable[] cdnTables) {
		List table = new ArrayList();
		RptResourceTable report = (RptResourceTable) rptTable;
		List listRptCol = (List) listReportCol;
		ReportQryStruct query = (ReportQryStruct) qryStruct;

		// 报表列数,用于空值或错误提示时用
		int iCol = ReportObjUtil.getDictLen(report, listRptCol);
		// 报表表头
		String rptHead = genTableHead(report, listRptCol, query, iCol);
		// 报表数据
		String[][] result = null;
		// 分页大小
		int pagecount = StringB.toInt(report.pagecount, 0);
		WebDBUtil db = new WebDBUtil();
		try {
			if (pagecount > 0) {
				result = dataDao.getReportData(rptTable, listRptCol, qryStruct, db, cdnTables);

				// 处理报表排序
				if (query.order_code != null && !"".equals(query.order_code)) {
					int column = StringB.toInt(query.order_icolumn, 0);
					boolean flag = false;
					if ("ASC".equals(query.order)) {
						flag = true;
					}
					result = SortUtils.sortValuesStrNumHasSum(result, column, flag);
				}
			} else {
				result = dataDao.getReportData(rptTable, listRptCol, qryStruct, cdnTables);
			}
		} catch (AppException e) {
			result = null;
		}

		if (result == null || result.length == 0) {
			table.add(rptHead);
			if (pagecount > 0) {
				table.add(Integer.toString(db.getTotalCount()));
			}
			String msgInfo = "";
			if (ReportConsts.YES.equals(query.first_view)) {
				msgInfo += msgInfo += "<TR " + ReportConsts.STYLE_TR_HEAD + ">";
				msgInfo += "<TD height=\"18\" colspan=" + iCol
						+ " align=\"center\">&nbsp;</TD></TR>\n";
			} else {
				msgInfo += "<TR " + ReportConsts.STYLE_TR_HEAD + ">";
				msgInfo += "<TD height=\"18\" colspan=" + iCol
						+ " align=\"center\">目前还没有可供查看的数据</TD></TR>\n";
			}
			table.add(msgInfo);
		} else {
			// 报表处理方式
			String type = report.rpt_type;
			if (type == null || type.length() != 3) {
				type = "";
			}
			if ("".equals(type)) {
				String msgInfo = rptHead;
				msgInfo += "<TR " + ReportConsts.STYLE_TR_HEAD + ">";
				msgInfo += "<TD height=\"18\" colspan=" + iCol
						+ " align=\"center\">未知的报表处理方式，请检查配置表！</TD></TR>\n";
				table.add(msgInfo);
			} else if ("20".equals(type.substring(0, 2)) || "3".equals(type.substring(0, 1))) {
				table.add(rptHead);
				if (pagecount > 0) {
					table.add(Integer.toString(db.getTotalCount()));
				}
				// 本地化报表常规处理
				genCommonTable(report, listRptCol, query, result, table);
			} else if ("290".equals(type)) {
				// 按报表字段定义纵向显示带合计行数据
			}
		}

		return (String[]) table.toArray(new String[table.size()]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getMeasureReportTitle(java.lang
	 * .Object , java.lang.Object)
	 */
	public String getMeasureReportTitle(Object rptTable, Object qryStruct) {
		StringBuffer ret = new StringBuffer();

		RptResourceTable report = (RptResourceTable) rptTable;
		ReportQryStruct query = (ReportQryStruct) qryStruct;
		// 日期说明
		String strDate = StringTool.getCnDate(query.date_s, report.cycle);
		if (TableConsts.RPT_DATE_PART.equals(report.start_date)) {
			strDate += " 至 " + StringTool.getCnDate(query.date_e, report.cycle);
		}

		// 报表标题部分
		ret.append("<TR><TD align=\"center\" colspan=\"2\"><div class=\"listtitle\"><span class=\"bigtitle\">"
				+ report.name + "</span></div></TD></TR>\n");
		// 表头显示的报表相关信息
		ret.append("<TR><TD colspan=\"2\">");
		ret.append("<TABLE width=\"100%\">");
		ret.append("<TR>\n");
		ret.append("<TD nowrap width=\"5%\" align=\"left\">填报单位：</TD>\n");
		ret.append("<TD nowrap align=\"left\">" + report.filldept + "</TD>\n");
		ret.append("<TD nowrap align=\"right\">制表单位：</TD>\n");
		ret.append("<TD nowrap width=\"5%\" align=\"left\">" + report.makedept + "</TD>\n");
		ret.append("</TR>\n");
		ret.append("<TR>\n");
		ret.append("<TD nowrap width=\"5%\" align=\"left\">统计日期：</TD>\n");
		ret.append("<TD nowrap align=\"left\">" + strDate + "</TD>\n");
		ret.append("<TD nowrap align=\"right\">报表编码：</TD>\n");
		ret.append("<TD nowrap width=\"5%\" align=\"left\">" + report.code + "</TD>\n");
		ret.append("</TR>\n");
		ret.append("</TABLE>");
		ret.append("</td></TR>\n");

		return ret.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getMeasureReportBody(java.lang
	 * .Object, java.lang.Object)
	 */
	public String[] getMeasureReportBody(Object rptTable, Object qryStruct) {
		List table = new ArrayList();

		RptResourceTable report = (RptResourceTable) rptTable;
		ReportQryStruct query = (ReportQryStruct) qryStruct;

		String roleStr = "";// 指标未建立权限关系，保留
		// 获取报表指标
		List listMeasure = imDataDao.getMeasure(report.rpt_id, roleStr);
		if (listMeasure == null || listMeasure.size() == 0) {
			return null;
		}
		// 处理地域信息
		if (StringTool.checkEmptyString(query.region_id_r)) {
			query.region_id_r = "0";
		}

		if (ReportConsts.FIRST.equals(report.divcity_flag)) {

			// 1.提取分析维度信息
			DimRuleStruct dimRule = imDataDao.getReportDim(report.rpt_id);
			if (dimRule == null) {
				dimRule = new DimRuleStruct(report.rpt_id, "region_id",
						"SELECT CITY_ID, CITY_DESC FROM D_CITY ", "", "", "");
			}

			// 2.配置特定维度分析
			String[][] dimValue = imDataDao.getReportDimValue(dimRule);

			// 3.提取分析维度信息

			// 按照子区域查看

			String[][] result = null;

			// 获得指标ID字符串
			RptMeasureTable[] measure = (RptMeasureTable[]) listMeasure
					.toArray(new RptMeasureTable[listMeasure.size()]);
			String measure_str = StringTool.changArrToStrComma(measure, "id");
			// 获得区域ID字符串
			String dim_str = StringTool.changArrToStrComma(dimValue, 0);
			if (!ReportConsts.RPTM_DEFAULT_SHOW.equals(report.rpt_type)) {
				if (dim_str.length() > 0) {
					dim_str += ",'9'";
				}
			}
			if (ReportConsts.YES.equals(query.visible_data)
					&& ReportConsts.NO.equals(query.first_view)) {
				result = imDataDao.getMeasureDimData(report.rpt_id, measure_str, dim_str,
						query.date_s, query.region_id_r, dimRule);
			}

			// 指标横向
			if (ReportConsts.FIRST.equals(query.row_flag)) {
				genMeasureTablebyRegion(measure, dimValue, result, table, dimRule);
			}
			// 指标纵向
			if (ReportConsts.SECOND.equals(query.row_flag)) {
				if (ReportConsts.RPTM_DEFAULT_SHOW.equals(report.rpt_type)) {
					genMeasureTablebyRegion(dimValue, measure, result, table, dimRule);
					// genMeasureTablebyRegion(region, measure, result, table);
				} else {
					// String[][] region=null;
					List listShow = imDataDao.getMeasureShow(report.rpt_id);
					genMeasureTablebyExpand(dimValue, listShow, measure, result, table);
				}
				// genMeasureTablebyRegion(dimValue, measure, result,
				// table,dimRule);

			}

		} else {
			// 不按子区域查看
			List listData = null;
			if (ReportConsts.YES.equals(query.visible_data)
					&& ReportConsts.NO.equals(query.first_view)) {
				listData = imDataDao.getMeasureData(report.rpt_id, query.region_id_r, query.date_s,
						query.date_e);
			}
			RptMeasureTable[] measureData = (RptMeasureTable[]) listData
					.toArray(new RptMeasureTable[listData.size()]);
			if (ReportConsts.RPTM_DEFAULT_SHOW.equals(report.rpt_type)) {
				genMeasureTable(listMeasure, measureData, table);
			} else {
				List listShow = imDataDao.getMeasureShow(report.rpt_id);
				genMeasureExpandTable(listMeasure, listShow, measureData, table);
			}
			/*
			 * // 不按子区域查看 String[][] measureData = null; if
			 * (ReportConsts.YES.equals(query.visible_data) &&
			 * ReportConsts.NO.equals(query.first_view)) { measureData =
			 * imDataDao.getMeasureData(report.rpt_id,query.region_id_r,
			 * query.date_s, query.date_e); }
			 */
			// genMeasureTable(listMeasure, measureData, table);
		}
		return (String[]) table.toArray(new String[table.size()]);
	}

	/**
	 * 指标型报表按子区域显示的Table
	 *
	 * @param region
	 * @param measure
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genMeasureTablebyExpand(String[][] region, List listShow,
			RptMeasureTable[] measure, String[][] result, List table) {
		double totalSum = 0;
		double rowSum = 0;

		// 标题行start
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		if (listShow == null) {
			ret += "<TD ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD_HEAD + ">未定义显示列信息</TD>\n";
			ret += "</TR>\n";
			table.add(ret);
			return;
		}

		RptMeasureShowTable[] showTable = (RptMeasureShowTable[]) listShow
				.toArray(new RptMeasureShowTable[listShow.size()]);
		for (int i = 0; i < showTable.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + showTable[i].name + "</TD>\n";
		}
		ret += " <TD " + ReportConsts.STYLE_TD_HEAD + ">合计</TD>\n";
		for (int i = 0; region != null && i < region.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + region[i][1] + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);
		// 标题行end

		if (result == null) {
			int iCol = 0;
			if (region != null && region.length > 0)
				iCol = region.length;
			iCol = iCol + listShow.size() + 1;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + iCol
					+ " ALIGN=\"CENTER\">没有数据</TD></TR>\n";
			table.add(ret);
			return;
		}

		// 数据行start
		for (int i = 0; measure != null && i < measure.length; i++) {
			rowSum = 0;
			if (ReportConsts.SECOND.equals(measure[i].ismeasure)) {
				ret = "<TR " + ReportConsts.STYLE_TR_JPG + ">\n";
			} else {
				ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			}
			for (int j = 0; j < showTable.length; j++) {
				String tmp = showTable[j].code.toLowerCase();
				String tmpValue = ReflectUtil.getStringFromObj(measure[i], tmp);
				if ("value".equals(tmp)) {
					tmpValue = genMeasureValue(measure[i]);
					ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + tmpValue
							+ "</TD>\n";
				} else {
					ret += "<TD " + ReportConsts.STYLE_TD + ">" + tmpValue + "</TD>\n";
				}
			}
			// ret += "<TD>" + measure[i].id + "</TD>\n";
			// ret += "<TD height=\"20\" nowrap>"
			// + measure[i].name.replaceAll(" ", "&nbsp;&nbsp;")
			// + "</TD>\n";
			// ret += "<TD>" + measure[i].unit + "</TD>\n";
			for (int x = 0; result != null && x < result.length; x++) {
				String measureValue = "0";
				if (result[x][0].equals(measure[i].id) && "9".equals(result[x][1])) {
					measureValue = result[x][2];
					rowSum += StringB.toDouble(measureValue, 0);
					continue;
				}
				for (int j = 0; region != null && j < region.length; j++) {
					if ((result[x][0].equals(measure[i].id)) && (result[x][1].equals(region[j][0]))) {
						measureValue = result[x][2];
					}
				}
				// 计算合计行值
				if (!"-999999".equals(getValue(measureValue))) {
					rowSum += StringB.toDouble(measureValue, 0);
				}
			}
			if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
				String measureValue = Double.toString(rowSum);
				int iDil = StringB.toInt(measure[i].digit_len, 0);
				measureValue = setValue(measureValue, iDil);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			} else {
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">&nbsp;</TD>\n";
			}
			for (int j = 0; region != null && j < region.length; j++) {
				String measureValue = "0";
				for (int x = 0; result != null && x < result.length; x++) {
					if ((result[x][0].equals(measure[i].id)) && (result[x][1].equals(region[j][0]))) {
						measureValue = result[x][2];
					}
				}
				/*
				 * 计算合计行值 if (!"-999999".equals(getValue(measureValue))) {
				 * rowSum += StringB.toDouble(measureValue, 0); } if
				 * (!"-999999".equals(getValue(measureValue))) { colSum[j] +=
				 * StringB.toDouble(measureValue, 0); }
				 */

				if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
					int iDil = StringB.toInt(measure[i].digit_len, 0);
					measureValue = setValue(measureValue, iDil);
				} else {
					measureValue = "&nbsp;";
				}
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			}
			totalSum += rowSum;
			ret += "</TR>\n";
			table.add(ret);
		}
		// 数据行end
	}

	/**
	 * 指标型报表不按子区域显示的Table
	 *
	 * @param listMeasure
	 * @param measureData
	 * @param table
	 * @return
	 */
	private static void genMeasureExpandTable(List listMeasure, List listShow,
			RptMeasureTable[] measureData, List table) {
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		if (listShow == null) {
			ret += "<TD ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD_HEAD + ">未定义显示列信息</TD>\n";
			ret += "</TR>\n";
			table.add(ret);
			return;
		}

		RptMeasureShowTable[] showTable = (RptMeasureShowTable[]) listShow
				.toArray(new RptMeasureShowTable[listShow.size()]);
		for (int i = 0; i < showTable.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + showTable[i].name + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);

		if (listMeasure == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">报表没有初始化</TD></TR>\n";
			table.add(ret);
		} else if (measureData == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">&nbsp;</TD></TR>\n";
			table.add(ret);
		} else {
			for (int i = 0; i < measureData.length; i++) {
				if (ReportConsts.SECOND.equals(measureData[i].ismeasure)) {
					ret = "<TR " + ReportConsts.STYLE_TR_JPG + ">\n";
				} else {
					ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
				}
				for (int j = 0; j < showTable.length; j++) {
					String tmp = showTable[j].code.toLowerCase();
					String tmpValue = ReflectUtil.getStringFromObj(measureData[i], tmp);
					if ("value".equals(tmp)) {
						tmpValue = genMeasureValue(measureData[i]);
						ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + tmpValue
								+ "</TD>\n";
					} else {
						ret += "<TD " + ReportConsts.STYLE_TD + ">" + tmpValue + "</TD>\n";
					}
				}
				ret += "</TR>\n";
				table.add(ret);
			}
		}
	}

	/**
	 * 获取指标数据
	 *
	 * @param measureData
	 * @param iDataCol
	 * @return
	 */
	private static String genMeasureValue(RptMeasureTable measureData) {
		String sValue = measureData.measure_value;
		if (sValue == null || "".equals(sValue))
			return "";

		if (ReportConsts.ZERO.equals(measureData.istoleader)) {
			sValue = "免报";
		} else if (ReportConsts.SECOND.equals(measureData.ismeasure)) {
			// 非指标，标题部分
			sValue = "";
		} else {
			int dig_len = StringB.toInt(measureData.digit_len, 0);
			sValue = FormatUtil.formatStr(sValue, dig_len, true);
			sValue = NullProcFactory.transNullToString(sValue, "&nbsp;");
		}
		return sValue;
	}

	/**
	 * 指标型报表按子区域显示的Table
	 *
	 * @param measure
	 * @param region
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genMeasureTablebyMeasure(RptMeasureTable[] measure, String[][] region,
			String[][] result, List table) {
		double colSum[] = null;
		double totalSum = 0;
		double rowSum = 0;
		if (measure != null && measure.length > 0)
			colSum = new double[measure.length];

		// 标题行start
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">地域</TD>\n";
		for (int i = 0; measure != null && i < measure.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + measure[i].name + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);
		// 标题行end

		if (result == null) {
			int iCol = 0;
			if (measure != null && measure.length > 0)
				iCol = measure.length;
			iCol = iCol + 1;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + iCol
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">没有数据</TD></TR>\n";
			table.add(ret);
			return;
		}

		// 数据行start
		for (int i = 0; region != null && i < region.length; i++) {
			rowSum = 0;

			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			ret += "<TD height=\"20\" " + ReportConsts.STYLE_TD + " nowrap>"
					+ region[i][1].replaceAll("    ", "&nbsp;&nbsp;") + "</TD>\n";
			for (int j = 0; measure != null && j < measure.length; j++) {
				String measureValue = "0";
				for (int x = 0; result != null && x < result.length; x++) {
					if ((result[x][0].equals(measure[j].id)) && (result[x][1].equals(region[i][0]))) {
						measureValue = result[x][2];
					}
				}
				// 计算合计行值
				if (!"-999999".equals(getValue(measureValue))) {
					rowSum += StringB.toDouble(measureValue, 0);
				}
				if (!"-999999".equals(getValue(measureValue))) {
					colSum[j] += StringB.toDouble(measureValue, 0);
				}
				int iDil = StringB.toInt(measure[j].digit_len, 0);
				measureValue = setValue(measureValue, iDil);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			}
			totalSum += rowSum;
			ret += "</TR>\n";
			table.add(ret);
		}
		// 数据行end

		// 合计行--指标横向start
		ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD + ">合计</TD>\n";
		for (int j = 0; measure != null && j < measure.length; j++) {
			String measureValue = Double.toString(colSum[j]);
			int iDil = StringB.toInt(measure[j].digit_len, 0);
			measureValue = setValue(measureValue, iDil);
			ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);
		// 合计行--指标横向end
	}

	/**
	 * 指标型报表不按子区域显示的Table
	 *
	 * @param listMeasure
	 * @param measureData
	 * @param table
	 * @return
	 */
	private static void genMeasureTable(List listMeasure, RptMeasureTable[] measureData, List table) {
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标代码</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">数据名称</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标单位</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标值</TD>\n";
		ret += "</TR>\n";
		table.add(ret);

		if (listMeasure == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">报表没有初始化</TD></TR>\n";
			table.add(ret);
		} else if (measureData == null) {
			// for (int i = 0; i < listMeasure.size(); i++) {
			// RptMeasureTable measure = (RptMeasureTable) listMeasure.get(i);
			// ret = "<TR " + ReportConsts.STYLE_TR_WHITE +">\n";
			// ret += "<TD>" + measure.code + "</TD>\n";
			// ret += "<TD>" + measure.name + "</TD>\n";
			// ret += "<TD>" + measure.unit + "</TD>\n";
			// ret += "<TD>&nbsp;</TD>\n";
			// table.add(ret);
			// }
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">&nbsp;</TD></TR>\n";
			table.add(ret);
		} else {
			for (int i = 0; i < measureData.length; i++) {

				if (ReportConsts.SECOND.equals(measureData[i].ismeasure)) {
					ret = "<TR " + ReportConsts.STYLE_TR_JPG + ">\n";
				} else {
					ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
				}
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i].code + "</TD>\n";
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i].name + "</TD>\n";
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i].unit + "</TD>\n";
				String sValue = genMeasureValue(measureData[i]);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + sValue + "</TD>\n";
				ret += "</TR>\n";
				table.add(ret);
			}
		}
	}

	/**
	 * 指标型报表不按子区域显示的Table
	 *
	 * @param listMeasure
	 * @param measureData
	 * @param table
	 * @return
	 */
	private static void genMeasureTable(List listMeasure, String[][] measureData, List table) {
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标名称</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">数据类型</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标单位</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标值</TD>\n";
		ret += "</TR>\n";
		table.add(ret);

		if (listMeasure == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">报表没有初始化</TD></TR>\n";
			table.add(ret);
		} else if (measureData == null) {
			// for (int i = 0; i < listMeasure.size(); i++) {
			// RptMeasureTable measure = (RptMeasureTable) listMeasure.get(i);
			// ret = "<TR "+ReportConsts.STYLE_TR_WHITE+">\n";
			// ret += "<TD>" + measure.code + "</TD>\n";
			// ret += "<TD>" + measure.name + "</TD>\n";
			// ret += "<TD>" + measure.unit + "</TD>\n";
			// ret += "<TD>&nbsp;</TD>\n";
			// table.add(ret);
			// }
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=\"4\" ALIGN=\"CENTER\" "
					+ ReportConsts.STYLE_TD + ">&nbsp;</TD></TR>\n";
			table.add(ret);
		} else {
			for (int i = 0; i < measureData.length; i++) {
				ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i][3] + "</TD>\n";
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i][9] + "</TD>\n";
				ret += "<TD " + ReportConsts.STYLE_TD + ">" + measureData[i][4] + "</TD>\n";
				String sValue = genMeasureValue(measureData[i], 10);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + sValue + "</TD>\n";
				ret += "</TR>\n";
				table.add(ret);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getReportHtml(java.lang.String,
	 * java.lang.Object)
	 */
	public String[] getReportHtml(String rpt_id, Object qryStruct) {
		List table = new ArrayList();
		ReportQryStruct query = (ReportQryStruct) qryStruct;

		// 定义报表操作接口
		ILReportService rptService = new LReportServiceImpl();
		// 报表基本信息
		RptResourceTable rptTable = null;
		try {
			rptTable = (RptResourceTable) rptService.getReport(rpt_id);
		} catch (AppException e) {
			e.printStackTrace();
		}
		// 获取固定报表列对象
		List listRptCol = null;
		try {
			listRptCol = rptService.getReportCol(rptTable.rpt_id, query.expandcol);
		} catch (AppException e) {
			e.printStackTrace();
		}

		table.add("<table id=\"AutoNumber1\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

		String trStart = "<tr>";
		trStart += "<td colspan=\"2\" class=\"tab-side2\" >";
		trStart += "<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse\" bordercolor=\"#999999\">";

		String trEnd = "</table>";
		trEnd += "</td>";
		trEnd += "</tr>";

		if ("100".equals(rptTable.rpt_type) || "101".equals(rptTable.rpt_type)) {
			// 报表标题部分的HTML
			table.add(getMeasureReportTitle(rptTable, qryStruct));
			// 报表HTML
			table.add(trStart);
			String[] body = getMeasureReportBody(rptTable, qryStruct);
			for (int i = 0; body != null && i < body.length; i++) {
				table.add(body[i]);
			}
			table.add(trEnd);
		} else {
			// 报表标题部分的HTML
			table.add(getReportTitle(rptTable, qryStruct));

			// 获取条件定义表中报表的条件的定义信息
			PubInfoConditionTable[] cdnTables = null;
			try {
				cdnTables = CommConditionUtil.genCondition(rpt_id, ReportConsts.CONDITION_PUB);
			} catch (AppException e) {
				e.printStackTrace();
			}
			// 处理报表条件
			rptTable.data_where_sql = " " + rptTable.data_where;
			if (rptTable.data_where_sql.toUpperCase().indexOf(" WHERE ") >= 0) {
				rptTable.data_where_sql += CommConditionUtil.getRptWhere(cdnTables, qryStruct);
			} else {
				rptTable.data_where_sql = "WHERE 1=1 "
						+ CommConditionUtil.getRptWhere(cdnTables, qryStruct);
			}
			System.out.println("rptTable.data_where_sql=" + rptTable.data_where_sql);

			// 处理报表排序
			if (query.order_code != null && !"".equals(query.order_code)) {
				String ret = " ORDER BY DATA_ID," + query.order_code;
				if (StringTool.checkEmptyString(query.order)) {
					query.order = "DESC";
				}
				ret += " " + query.order;
				rptTable.data_order = ret;
			}
			// 报表HTML
			table.add(trStart);
			String[] body = getReportBody(rptTable, listRptCol, qryStruct, cdnTables);
			for (int i = 0; body != null && i < body.length; i++) {
				table.add(body[i]);
			}
			table.add(trEnd);
		}
		table.add("</table>");
		return (String[]) table.toArray(new String[table.size()]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getProcessBody(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.util.List,
	 * java.util.List)
	 */
	public String getProcessBody(String oper_no, String oper_name, String user_role, String rpt_id,
			String rpt_date, List listProcessStep, List listProcessHis) {
		// 判断审核步骤是否为空
		if (listProcessStep == null || listProcessStep.size() == 0)
			return "";

		String res = "";
		try {
			if (ReportProcessUtil.hasProcessOver(listProcessHis)) {
				// 审核流程步骤结束
				res += genProcessOverLayout(listProcessStep, listProcessHis);
			} else {
				// 获取审核描述代码
				res += genProcessLayout(user_role, listProcessStep, listProcessHis);

				// 获取报表审核状态
				RptProcessStateTable rptState = ReportProcessUtil.getRptProcessState(
						listProcessStep, listProcessHis);
				String sqlReturn = SQLGenator.genSQL("Q3024", rptState.now_step, rpt_date,
						rptState.p_id);
				String sqlForward = SQLGenator.genSQL("Q3025", rptState.now_step, rpt_date,
						rptState.p_id);
				res += genProcessOperateLayout(rpt_id, rpt_date, rptState, oper_no, oper_name,
						sqlReturn, sqlForward);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILTableService#getProcessBodyPrint(java.lang
	 * .Object, java.lang.String, java.lang.String)
	 */
	public String getProcessBodyPrint(Object rptTable, String rpt_date, String region_id) {
		String res = "";
		try {
			// 定义报表操作接口
			ILReportService rptService = new LReportServiceImpl();
			// 获取审核流程步骤
			List listProcessStep = rptService.getReportProcessStep(rptTable, rpt_date);
			// 获取审核历史信息
			RptResourceTable rpt = (RptResourceTable) rptTable;
			List listProcessHis = rptService.getReportProcessHis(rpt.rpt_id, rpt_date, region_id);
			// 审核流程步骤结束
			res += genProcessOverLayout(listProcessStep, listProcessHis);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取报表表头HTML代码
	 *
	 * @param rptTable
	 * @param dicts
	 * @param qryStruct
	 * @param iCol
	 * @return
	 */
	private static String genTableHead(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, int iCol) {
		// 报表表头HTML代码
		String strHead = "";
		// 特殊显示方式
		String row_flag = rptTable.row_flag;
		// 报表数值列号
		int iColumn = -1;
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			iColumn++;
		}

		if (ReportConsts.NO.equals(rptTable.ishead)) {
			// 没有自定表头，按指标定义表生成表头
			strHead += "<TR " + ReportConsts.STYLE_TR_HEAD + " align=\"center\">\n";
			if (StringB.toInt(rptTable.divcity_flag, 0) > 0) {
				if (ReportConsts.NO.equals(rptTable.isleft)) {
					// 加入扩展显示列
					if (ReportConsts.DIS_SEQUENCE_COL.equals(row_flag)) {
						strHead += "<TD width=40 " + ReportConsts.STYLE_TD_HEAD + ">序号</TD>";
					}
					if (ReportConsts.DIS_DATE_COL.equals(row_flag)) {
						iColumn++;
						strHead += "<TD width=80 " + ReportConsts.STYLE_TD_HEAD + ">日期</TD>";
					}
					if (ReportConsts.DIS_ALL_COL.equals(row_flag)) {
						strHead += "<TD width=40 " + ReportConsts.STYLE_TD_HEAD + ">序号</TD>";
						iColumn++;
						strHead += "<TD width=80 " + ReportConsts.STYLE_TD_HEAD + ">日期</TD>";
					}
				}
			}

			Iterator iter = rptColTable.iterator();
			while (iter.hasNext()) {
				RptColDictTable dict = (RptColDictTable) iter.next();
				if (!ReportConsts.YES.equals(dict.default_display)) {
					continue;
				}

				// 维度列号计算
				if (ReportConsts.YES.equals(dict.default_display)
						&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
					if (!StringTool.checkEmptyString(dict.field_dim_code)) {
						iColumn++;
					}
					iColumn++;
				}

				// 是否显示纬度编码
				if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)
						&& ReportConsts.YES.equals(dict.dim_code_display)) {
					strHead += "<TD " + ReportConsts.STYLE_TD_HEAD + ">";
					strHead += dict.field_title + "编码";
					strHead += "</TD>";
				}

				// 计算数值型字段序号
				if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					iColumn++;
				}
				String colNum = dict.col_sequence;
				// 正常字段
				strHead += "<TD " + ReportConsts.STYLE_TD_HEAD + ">";
				if (ReportConsts.YES.equals(dict.is_expand_col)) {
					// 加入钻取
					strHead += ReportObjUtil.getExpandDirectionUp(rptColTable, qryStruct.expandcol,
							dict.col_sequence, rptTable.url);
				}
				strHead += dict.field_title;
				if (ReportConsts.YES.equals(dict.is_expand_col)) {
					// 加入钻取
					strHead += ReportObjUtil.getExpandDirectionDown(rptColTable,
							qryStruct.expandcol, dict.col_sequence, rptTable.url);
				}
				if (ReportConsts.YES.equals(dict.data_order)) {
					// 加入排序
					if (("MSU" + colNum).equals(qryStruct.order_code)) {
						strHead += "<a href=\"javascript:_order('";
						strHead += "MSU" + colNum + "',";
						if ("DESC".equals(qryStruct.order)) {
							strHead += "'ASC','" + iColumn + "')\">";
							strHead += "<img src=\"../images/menu_down.gif\" width=\"9\" border=\"0\">";
						} else {
							strHead += "'DESC','" + iColumn + "')\">";
							strHead += "<img src=\"../images/menu_up.gif\" width=\"9\" border=\"0\">";
						}
						strHead += "</a>";
					} else {
						strHead += "<a href=\"javascript:_order('";
						strHead += "MSU" + colNum + "',";
						strHead += "'" + qryStruct.order + "','" + iColumn + "')\">";
						strHead += "<img src=\"../images/menu_init.gif\" width=\"9\" border=\"0\">";
						strHead += "</a>";
					}
				}
				strHead += "</TD>\n";
				if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					// 数值型字段，可能需要加入占比，同比，环比列
					if (ReportConsts.YES.equals(dict.has_comratio)) {
						iColumn++;
						strHead += "<TD " + ReportConsts.STYLE_TD_HEAD + ">占比</TD>\n";
					}
					if (ReportObjUtil.hasSameData(rptTable, rptColTable, qryStruct)
							&& ReportConsts.YES.equals(dict.has_same)) {
						iColumn++;
						strHead += "<TD " + ReportConsts.STYLE_TD_HEAD + ">同比</TD>\n";
					}
					if (ReportObjUtil.hasLastData(rptTable, rptColTable, qryStruct)
							&& ReportConsts.YES.equals(dict.has_last)) {
						iColumn++;
						strHead += "<TD " + ReportConsts.STYLE_TD_HEAD + ">环比</TD>\n";
					}
				}
			}
			strHead += "</TR>\n";
		} else {
			// 从数据库中读出表头的HTML代码
			ILReportService rptService = new LReportServiceImpl();
			String rpt_head = rptService.getReportHead(rptTable.rpt_id);
			rpt_head = ReportObjUtil.genLocalRptHead(rpt_head, rptColTable, qryStruct.expandcol,
					rptTable.url, qryStruct.order, qryStruct.order_code);
			strHead += rpt_head;
		}
		return strHead;
	}

	/**
	 * 本地化报表展现
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genCommonTable(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, String[][] result, List table) {
		// 是否有同比数据
		boolean has_same = ReportObjUtil.hasSameData(rptTable, rptColTable, qryStruct);
		// 是否有环比数据
		boolean has_last = ReportObjUtil.hasLastData(rptTable, rptColTable, qryStruct);
		// 能进行合并的纬度列数
		int iDimLen = ReportObjUtil.getDimLen(rptTable, rptColTable);
		// 数据行数
		int iDataLen = result.length;

		// 重新封装rptColTable，过滤不显示的列
		List rptCol = new ArrayList();
		for (int j = 0; rptColTable != null && j < rptColTable.size(); j++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(j);
			if (ReportConsts.YES.equals(dict.default_display)) {
				rptCol.add(dict);
			}
		}
		rptColTable = rptCol;
		// 列定义对象
		RptColDictTable[] dicts = (RptColDictTable[]) rptColTable
				.toArray(new RptColDictTable[rptColTable.size()]);

		// 如果需要对左侧进行合并列，开始计算合并行列数
		String[][] rows = new String[iDataLen][dicts.length];
		String[][] cols = new String[iDataLen][dicts.length];
		// 是否有左表头
		String sLeft = rptTable.isleft;
		if (ReportConsts.YES.equals(sLeft)) {
			int iAddCol = 0;
			if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
				iAddCol = 2;
			}
			if (rptTable.displaydate) {
				iAddCol = 3;
			}
			ReportObjUtil.genLeftArr(iAddCol, rptColTable, result, rows, cols);
		}

		// 输出的HTML展示结果
		for (int i = 0; i < iDataLen; i++) {
			String str = "";
			if (i % 2 == 0) {
				str = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			} else {
				str = "<TR " + ReportConsts.STYLE_TR_JPG + ">\n";
			}
			// 是否需要合并左侧列
			String tmp_sLeft = sLeft;
			// 是否为第一列
			boolean firstCol = true;
			// 是否为合计行
			boolean sumRow = false;
			if (ReportConsts.RPT_SUMROW_ID.equals(result[i][0])) {
				sumRow = true;
			}
			// 是否为小计行
			boolean subSumRow = false;
			if (ReportConsts.RPT_SUBSUMROW_ID.equals(result[i][0])) {
				subSumRow = true;
			}
			// 是否为跟在数值列后的描述列
			boolean charcolAfterNum = false;
			// 用来记录当前数据显示的列索引,data_id列直接跳过不显示

			int iNum = 0;
			if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
				iNum = 1;
			}
			// 每个值对应一个完整tr部分
			// String str = "<TR " + ReportConsts.STYLE_TR + ">\n";

			if (sumRow || subSumRow) {
				// 合计行和小计行不做序号和日期显示
			} else {
				if (ReportConsts.DIS_SEQUENCE_COL.equals(rptTable.row_flag)) {
					// 显示序号
					String value = Integer.toString(i + 1);
					str += genCharTd(value, ReportConsts.NO);
				}
				if (ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
					// 显示日期
					String value = result[i][iNum];
					str += genCharTd(value, ReportConsts.NO);
					iNum++;
				}
				if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
					// 显示序号
					String value = Integer.toString(i + 1);
					str += genCharTd(value, ReportConsts.NO);
					// 显示日期
					value = result[i][iNum];
					str += genCharTd(value, ReportConsts.NO);
					iNum++;
				}
			}

			for (int j = 0; j < dicts.length; j++) {

				if (!ReportConsts.YES.equals(dicts[j].default_display)) {
					continue;
				}

				if (ReportConsts.DATA_TYPE_STRING.equals(dicts[j].data_type)) {
					String dimValue = "";
					String value = "";
					// 纬度编码列处理
					if (!StringTool.checkEmptyString(dicts[j].field_dim_code)) {
						if (ReportConsts.YES.equals(dicts[j].dim_code_display) && !sumRow) {
							value = result[i][iNum];
							str += genCharTd(value, dicts[j]);
						}
						dimValue = result[i][iNum];
						iNum++;
					}
					value = result[i][iNum];
					if (org.apache.commons.lang.math.NumberUtils.isNumber(value)
							&& value.length() > 11) {
						value = "编号：" + value;

					}
					str += genCharTd(value, dicts[j], tmp_sLeft, rows[i][j], cols[i][j], sumRow,
							firstCol, charcolAfterNum, iDimLen, dimValue);
				}

				if (ReportConsts.DATA_TYPE_NUMBER.equals(dicts[j].data_type)) {
					String value = result[i][iNum];
					if (ReportConsts.ZERO_TO_BLANK.equals(dicts[j].zero_proc)) {
						// 符合0值处理为空值的条件时做相应处理
						if (value != null && "0".equals(value)) {
							value = "&nbsp;";
						}
					}
					if ("Y".equals(dicts[j].ratio_displayed)) {
						// 小数位数
						int digit = StringB.toInt(dicts[j].msu_length, 0);
						// 符合需要转换为百分比值
						value = Arith.divPer(value, "1", digit);
					} else if ("Z".equals(dicts[j].ratio_displayed)) {
						// 小数位数
						int digit = StringB.toInt(dicts[j].msu_length, 0);
						// 符合需要转换为百分比值
						value = Arith.divPer(value, "100", digit);
					}
					// 添加数值型字段列
					str += genNumTd(value, result, dicts, iNum, i, j, has_same, has_last);
					// 是否有同比数据
					if (has_same) {
						iNum++;
						iNum++;
					}
					// 是否有环比数据
					if (has_last) {
						iNum++;
						iNum++;
					}
					// 遇到数值列，将合并左侧列的开关关闭
					tmp_sLeft = ReportConsts.NO;
					// 发现第一个数值列后，跟在数值列后的描述列某些特性要特殊处理，开关打开
					charcolAfterNum = true;
				}
				iNum++;
				// 循环开始后，是否为第一列的开关关闭
				firstCol = false;
			}
			str += "</TR>\n";
			table.add(str);
		}
	}

	/**
	 * 出账报表展现
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genAccountTable(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, String[][] result, List table) {
		// 是否有同比数据
		boolean has_same = ReportObjUtil.hasSameData(rptTable, rptColTable, qryStruct);
		// 是否有环比数据
		boolean has_last = ReportObjUtil.hasLastData(rptTable, rptColTable, qryStruct);
		// 能进行合并的纬度列数
		int iDimLen = ReportObjUtil.getDimLen(rptTable, rptColTable);
		// 数据行数
		int iDataLen = result.length;
		// 列定义对象
		RptColDictTable[] dicts = (RptColDictTable[]) rptColTable
				.toArray(new RptColDictTable[rptColTable.size()]);

		// 如果需要对左侧进行合并列，开始计算合并行列数
		String[][] rows = new String[iDataLen * 6][dicts.length];
		String[][] cols = new String[iDataLen * 6][dicts.length];
		// 是否有左表头
		String sLeft = rptTable.isleft;
		if (ReportConsts.YES.equals(sLeft)) {
			int iAddCol = 2;
			if (rptTable.displaydate) {
				iAddCol = 3;
			}
			ReportObjUtil.genLeftArr(iAddCol, rptColTable, result, rows, cols);
		}

		// 输出的HTML展示结果
		for (int i = 0; i < iDataLen; i++) {
			// 是否需要合并左侧列
			String tmp_sLeft = sLeft;
			// 是否为第一列
			boolean firstCol = true;
			// 是否为合计行
			boolean sumRow = false;
			if (ReportConsts.RPT_SUMROW_ID.equals(result[i][0])) {
				sumRow = true;
			}
			// 是否为小计行
			boolean subSumRow = false;
			if (ReportConsts.RPT_SUBSUMROW_ID.equals(result[i][0])) {
				subSumRow = true;
			}
			// 是否为跟在数值列后的描述列
			boolean charcolAfterNum = false;
			// 用来记录当前数据显示的列索引,data_id列直接跳过不显示
			int iNum = 1;

			// 每个值对应一个完整tr部分
			String str = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			if (sumRow || subSumRow) {
				// 合计行和小计行不做序号和日期显示
			} else {
				if (ReportConsts.DIS_SEQUENCE_COL.equals(rptTable.row_flag)) {
					// 显示序号
					String value = Integer.toString(i + 1);
					str += genCharTd(value, ReportConsts.NO);
				}
				if (ReportConsts.DIS_DATE_COL.equals(rptTable.row_flag)) {
					// 显示日期
					String value = result[i][iNum];
					str += genCharTd(value, ReportConsts.NO);
					iNum++;
				}
				if (ReportConsts.DIS_ALL_COL.equals(rptTable.row_flag)) {
					// 显示序号
					String value = Integer.toString(i + 1);
					str += genCharTd(value, ReportConsts.NO);
					// 显示日期
					value = result[i][iNum];
					str += genCharTd(value, ReportConsts.NO);
					iNum++;
				}
			}

			for (int j = 0; j < dicts.length; j++) {
				if (!ReportConsts.YES.equals(dicts[j].default_display)) {
					continue;
				}

				if (ReportConsts.DATA_TYPE_STRING.equals(dicts[j].data_type)) {
					String dimValue = "";
					String value = "";
					// 纬度编码列处理
					if (!StringTool.checkEmptyString(dicts[j].field_dim_code)) {
						if (ReportConsts.YES.equals(dicts[j].dim_code_display) && !sumRow) {
							value = result[i][iNum];
							str += genCharTd(value, dicts[j]);
						}
						dimValue = result[i][iNum];
						iNum++;
					}

					value = result[i][iNum];
					str += genCharTd(value, dicts[j], tmp_sLeft, rows[i][j], cols[i][j], sumRow,
							firstCol, charcolAfterNum, iDimLen, dimValue);
				}

				if (ReportConsts.DATA_TYPE_NUMBER.equals(dicts[j].data_type)) {
					String value = result[i][iNum];
					if (ReportConsts.ZERO_TO_BLANK.equals(dicts[j].zero_proc)) {
						// 符合0值处理为空值的条件时做相应处理
						if (value != null && "0".equals(value)) {
							value = "&nbsp;";
						}
					}
					if ("Y".equals(dicts[j].ratio_displayed)) {
						// 小数位数
						int digit = StringB.toInt(dicts[j].msu_length, 0);
						// 符合需要转换为百分比值
						value = Arith.divPer(value, "1", digit);
					} else if ("Z".equals(dicts[j].ratio_displayed)) {
						// 小数位数
						int digit = StringB.toInt(dicts[j].msu_length, 0);
						// 符合需要转换为百分比值
						value = Arith.divPer(value, "100", digit);
					}
					// 添加数值型字段列
					str += genNumTd(value, result, dicts, iNum, i, j, has_same, has_last);

					/*
					 * // 是否有同比数据 if (has_same) { iNum++; iNum++; } // 是否有环比数据
					 * if (has_last) { iNum++; iNum++; }
					 */
					// 遇到数值列，将合并左侧列的开关关闭
					tmp_sLeft = ReportConsts.NO;
					// 发现第一个数值列后，跟在数值列后的描述列某些特性要特殊处理，开关打开
					charcolAfterNum = true;
				}
				iNum++;
				// 循环开始后，是否为第一列的开关关闭
				firstCol = false;
			}
			str += "</TR>\n";
			table.add(str);
		}
	}

	/**
	 * 指标型报表不按子区域显示的Table
	 *
	 * @param listMeasure
	 * @param measureData
	 * @param table
	 * @return
	 */
	private static void genMeasureTable(List listMeasure, String[][] measureData, List table,
			String[][] rule) {
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		getHeadMap(); // 初始化
		String[] ruleArray = rule[0][0].split(",");
		int len = ruleArray.length;
		Iterator it = headMap.entrySet().iterator();
		for (int i = 0; i < len; i++) {
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (ruleArray[i].equals(entry.getKey())) {
					ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + entry.getValue().toString()
							+ "</TD>\n";
					headMap.remove(entry.getKey());
					it = headMap.entrySet().iterator();
					break;
				}
			}
		}
		ret += "</TR>\n";
		table.add(ret);

		if (listMeasure == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + len
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">报表没有初始化</TD></TR>\n";
			table.add(ret);
		} else if (measureData == null) {
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + len
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">&nbsp;</TD></TR>\n";
			table.add(ret);
		} else {
			getFieldMap(); // 初始化

			for (int i = 0; i < measureData.length; i++) {
				ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
				getFieldMap();
				it = fieldMap.entrySet().iterator();
				for (int j = 0; j < len; j++) {
					if (ruleArray[j].equals("v")) { // "v"表示指标值,其他都按表头顺序取值
						String sValue = genMeasureValue(measureData[i], 10);
						ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + sValue
								+ "</TD>\n";
					} else {
						while (it.hasNext()) {
							Map.Entry entry = (Map.Entry) it.next();
							if (ruleArray[j].equals(entry.getKey())) {
								ret += "<TD "
										+ ReportConsts.STYLE_TD
										+ ">"
										+ measureData[i][Integer.parseInt(entry.getValue()
												.toString())] + "</TD>\n";
								fieldMap.remove(entry.getKey());
								it = fieldMap.entrySet().iterator();
								break;
							}
						}
					}
				}
				ret += "</TR>\n";
				table.add(ret);
			}
		}
	}

	/**
	 * 指标型报表按子区域显示的Table
	 *
	 * @param measure
	 * @param region
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genMeasureTablebyRegion(RptMeasureTable[] measure, String[][] dim,
			String[][] result, List table, DimRuleStruct dimInfo) {
		double colSum[] = null;
		double totalSum = 0;
		double rowSum = 0;
		if (measure != null && measure.length > 0)
			colSum = new double[measure.length];

		// 标题行start
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		if (dimInfo == null) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">区域</TD>\n";
		} else {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + dimInfo.getDim_name() + "</TD>\n";
		}

		for (int i = 0; measure != null && i < measure.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + measure[i].name + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);
		// 标题行end

		// 查询结果保存在table的第二列。如果result为空，也要保存。
		if (result == null) {
			int iCol = 0;
			if (measure != null && measure.length > 0)
				iCol = measure.length;
			iCol = iCol + 1;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + iCol
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">没有数据</TD></TR>\n";

			table.add(ret);
			return;
		}

		// 数据行start
		for (int i = 0; dim != null && i < dim.length; i++) {
			rowSum = 0;

			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			ret += "<TD height=\"20\" " + ReportConsts.STYLE_TD + " nowrap>"
					+ dim[i][1].replaceAll("    ", "&nbsp;&nbsp;") + "</TD>\n";
			for (int j = 0; measure != null && j < measure.length; j++) {
				String measureValue = "0";
				for (int x = 0; result != null && x < result.length; x++) {
					if ((result[x][0].equals(measure[j].id)) && (result[x][1].equals(dim[i][0]))) {
						measureValue = result[x][2];
					}
				}
				// 计算合计行值
				if (!"-999999".equals(getValue(measureValue))) {
					rowSum += StringB.toDouble(measureValue, 0);
				}
				if (!"-999999".equals(getValue(measureValue))) {
					colSum[j] += StringB.toDouble(measureValue, 0);
				}
				int iDil = StringB.toInt(measure[j].digit_len, 0);
				measureValue = setValue(measureValue, iDil);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			}
			totalSum += rowSum;
			ret += "</TR>\n";
			table.add(ret);
		}
		// 数据行end

		// 合计行--指标横向start
		ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD + ">合计</TD>\n";
		for (int j = 0; measure != null && j < measure.length; j++) {
			String measureValue = Double.toString(colSum[j]);
			int iDil = StringB.toInt(measure[j].digit_len, 0);
			measureValue = setValue(measureValue, iDil);
			ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue + "</TD>\n";
		}
		ret += "</TR>\n";
		table.add(ret);
		// 合计行--指标横向end
	}

	/**
	 * 指标型报表按子区域显示的Table
	 *
	 * @param region
	 * @param measure
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genMeasureTablebyRegion(String[][] dim, RptMeasureTable[] measure,
			String[][] result, List table, DimRuleStruct dimInfo) {
		double colSum[] = null;
		double totalSum = 0;
		double rowSum = 0;
		if (dim != null && dim.length > 0)
			colSum = new double[dim.length];

		String ret = "";

		if (null != dimInfo.getTop_head() && !dimInfo.getTop_head().equals("")) {

			ret += dimInfo.getTop_head();
		} else {
			// 标题行start
			ret += "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标编码</TD>\n";
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标名称</TD>\n";
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标单位</TD>\n";
			for (int i = 0; dim != null && i < dim.length; i++) {
				ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + dim[i][1] + "</TD>\n";
			}
			ret += " <TD " + ReportConsts.STYLE_TD_HEAD + ">合计</TD>\n";
			ret += "</TR>\n";
		}
		table.add(ret);
		// 标题行end

		if (result == null) {
			int iCol = 0;
			if (dim != null && dim.length > 0)
				iCol = dim.length;
			iCol = iCol + 4;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + iCol
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">没有数据</TD></TR>\n";

			table.add(ret);
			return;
		}

		// 数据行start
		for (int i = 0; measure != null && i < measure.length; i++) {
			rowSum = 0;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			ret += "<TD " + ReportConsts.STYLE_TD + ">" + measure[i].code + "</TD>\n";
			ret += "<TD height=\"20\" " + ReportConsts.STYLE_TD + " nowrap>"
					+ measure[i].name.replaceAll("    ", "&nbsp;&nbsp;") + "</TD>\n";
			ret += "<TD " + ReportConsts.STYLE_TD + ">" + measure[i].unit + "</TD>\n";
			for (int j = 0; dim != null && j < dim.length; j++) {
				String measureValue = "0";
				for (int x = 0; result != null && x < result.length; x++) {
					if ((result[x][0].equals(measure[i].id)) && (result[x][1].equals(dim[j][0]))) {
						measureValue = result[x][2];
					}
				}
				// 计算合计行值
				if (!"-999999".equals(getValue(measureValue))) {
					rowSum += StringB.toDouble(measureValue, 0);
				}
				if (!"-999999".equals(getValue(measureValue))) {
					colSum[j] += StringB.toDouble(measureValue, 0);
				}

				if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
					int iDil = StringB.toInt(measure[i].digit_len, 0);
					measureValue = setValue(measureValue, iDil);
				} else {
					measureValue = "&nbsp;";
				}
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			}
			totalSum += rowSum;
			if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
				String measureValue = Double.toString(rowSum);
				int iDil = StringB.toInt(measure[i].digit_len, 0);
				measureValue = setValue(measureValue, iDil);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			} else {
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">&nbsp;</TD>\n";
			}
			ret += "</TR>\n";
			table.add(ret);
		}
		// 数据行end
	}

	/**
	 * 指标型报表按子区域显示的Table add by jcm
	 *
	 * @param region
	 * @param measure
	 * @param result
	 * @param table
	 * @return
	 */
	private static void genMeasureTablebyRegion103(String[][] dim, RptMeasureTable[] measure,
			String[][] result, List table, DimRuleStruct dimInfo) {
		double colSum[] = null;
		double totalSum = 0;
		double rowSum = 0;
		if (dim != null && dim.length > 0)
			colSum = new double[dim.length];

		// 标题行start
		String ret = "<TR align=\"center\" " + ReportConsts.STYLE_TR_HEAD + ">\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标名称</TD>\n";
		ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">指标单位</TD>\n";
		for (int i = 0; dim != null && i < dim.length; i++) {
			ret += "<TD " + ReportConsts.STYLE_TD_HEAD + ">" + dim[i][1] + "</TD>\n";
		}
		ret += " <TD " + ReportConsts.STYLE_TD_HEAD + ">合计</TD>\n";
		ret += "</TR>\n";
		table.add(ret);
		// 标题行end

		if (result == null) {
			int iCol = 0;
			if (dim != null && dim.length > 0)
				iCol = dim.length;
			iCol = iCol + 3;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + "><TD colspan=" + iCol
					+ " ALIGN=\"CENTER\" " + ReportConsts.STYLE_TD + ">没有数据</TD></TR>\n";
			table.add(ret);
			return;
		}

		// 数据行start
		for (int i = 0; measure != null && i < measure.length; i++) {
			rowSum = 0;
			ret = "<TR " + ReportConsts.STYLE_TR_WHITE + ">\n";
			ret += "<TD height=\"20\" " + ReportConsts.STYLE_TD + " nowrap>"
					+ measure[i].name.replaceAll("    ", "&nbsp;&nbsp;") + "</TD>\n";
			ret += "<TD " + ReportConsts.STYLE_TD + ">" + measure[i].unit + "</TD>\n";
			for (int j = 0; dim != null && j < dim.length; j++) {
				String measureValue = "0";
				for (int x = 0; result != null && x < result.length; x++) {
					if ((result[x][0].equals(measure[i].id)) && (result[x][1].equals(dim[j][0]))) {
						measureValue = result[x][2];
					}
				}
				// 计算合计行值
				if (!"-999999".equals(getValue(measureValue))) {
					rowSum += StringB.toDouble(measureValue, 0);
				}
				if (!"-999999".equals(getValue(measureValue))) {
					colSum[j] += StringB.toDouble(measureValue, 0);
				}

				if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
					int iDil = StringB.toInt(measure[i].digit_len, 0);
					measureValue = setValue(measureValue, iDil);
				} else {
					measureValue = "&nbsp;";
				}
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			}
			totalSum += rowSum;
			if (ReportConsts.FIRST.equals(measure[i].ismeasure)) {
				String measureValue = Double.toString(rowSum);
				int iDil = StringB.toInt(measure[i].digit_len, 0);
				measureValue = setValue(measureValue, iDil);
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">" + measureValue
						+ "</TD>\n";
			} else {
				ret += "<TD align=\"right\" " + ReportConsts.STYLE_TD + ">&nbsp;</TD>\n";
			}
			ret += "</TR>\n";
			table.add(ret);
		}
		// 数据行end
	}

	/**
	 * 简单字符单元格处理
	 *
	 * @param value
	 * @param td_wrap
	 * @return
	 */
	private static String genCharTd(String value, String td_wrap) {
		String ret = "";
		// 是否进行换行
		String wrap = "";
		if (ReportConsts.NO.equals(td_wrap)) {
			wrap = " nowrap";
		}
		// 对null值进行处理
		value = NullProcFactory.transNullToString(value, ReportConsts.FIRST);
		ret += "<TD" + wrap + " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
		return ret;
	}

	/**
	 * 简单字符单元格处理
	 *
	 * @param value
	 * @param td_wrap
	 * @return
	 */
	private static String genCharTd(String value, RptColDictTable td) {
		String ret = "";
		// 是否进行换行
		String wrap = "";
		if (ReportConsts.NO.equals(td.td_wrap)) {
			wrap = " nowrap";
		}
		// 对null值进行处理
		value = NullProcFactory.transNullToString(value, ReportConsts.FIRST);
		ret += "<TD" + wrap + " " + ReportConsts.STYLE_TD + ">";
		if (ReportConsts.YES.equals(td.has_link)) {
			ret += "<a href=" + td.link_url + ">";
		}
		ret += value;
		if (ReportConsts.YES.equals(td.has_link)) {
			ret += "</a>";
		}
		ret += "</TD>\n";
		return ret;
	}

	/**
	 * 一般字符单元格处理
	 *
	 * @param value
	 * @param sLeft
	 * @param rows
	 * @param cols
	 * @param sumRow
	 * @param firstCol
	 * @param charcolAfterNum
	 * @param dimLen
	 * @return
	 */
	private static String genCharTd(String value, RptColDictTable td, String sLeft, String rows,
			String cols, boolean sumRow, boolean firstCol, boolean charcolAfterNum, int dimLen,
			String dimValue) {
		String ret = "";
		// 是否进行换行
		String wrap = "";
		if (ReportConsts.NO.equals(td.td_wrap)) {
			wrap = " nowrap";
		}
		// 对null值进行处理
		value = NullProcFactory.transNullToString(value, ReportConsts.FIRST);
		// 小计日期值处理
		if (ReportConsts.RPT_SUBSUMROW_ID.equals(value)) {
			value = "&nbsp;";
		}
		if (ReportConsts.YES.equals(sLeft)) {
			if (sumRow && firstCol) {
				ret += "<TD" + wrap + " align=\"center\"";
				if (dimLen > 1) {
					ret += " colspan=" + dimLen;
				}
				ret += " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
			} else if (sumRow && charcolAfterNum) {
				ret += "<TD" + wrap + " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
			} else if (!sumRow && rows != null) {
				ret += "<TD" + wrap;
				if (!"".equals(rows)) {
					ret += " rowspan=" + rows;
				}
				if (cols != null && !"".equals(cols)) {
					ret += " colspan=" + cols;
				}
				ret += " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
			}
		} else {
			if (sumRow && firstCol) {
				ret += "<TD" + wrap + " align=\"center\"";
				if (dimLen > 1) {
					ret += " colspan=" + dimLen;
				}
				ret += " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
			} else if (sumRow && charcolAfterNum) {
				ret += "<TD" + wrap + " " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
			} else if (!sumRow) {
				ret += "<TD" + wrap + " " + ReportConsts.STYLE_TD + ">";
				if (ReportConsts.YES.equals(td.has_link)) {
					ret += "<a href=" + td.link_url + "&" + td.field_dim_code + "=" + dimValue
							+ ">";
				}
				ret += value;
				if (ReportConsts.YES.equals(td.has_link)) {
					ret += "</a>";
				}
				ret += "</TD>\n";
			}
		}
		return ret;
	}

	/**
	 * 获取表格数字型的td
	 *
	 * @param value
	 * @param result
	 * @param dicts
	 * @param iNum
	 * @param i
	 * @param j
	 * @param has_last
	 * @param has_loop
	 * @return
	 */
	private static String genNumTd(String value, String[][] result, RptColDictTable[] dicts,
			int iNum, int i, int j, boolean has_last, boolean has_loop) {
		// 对null值进行处理
		value = NullProcFactory.transNullToString(value, ReportConsts.FIRST);
		// 处理数值
		if (value.indexOf("%") < 0) {
			// 小数位数
			int digit = StringB.toInt(dicts[j].msu_length, 0);
			// 一般数字的处理
			if ("Y".equals(dicts[j].comma_splitted))
				value = FormatUtil.formatStr(value, digit, true);
			else
				value = FormatUtil.formatStr(value, digit, false);
		} else {
			// 对百分比特殊的处理
			if (value.length() > 1 && ".".equals(value.substring(0, 1)))
				value = "0" + value;
			if (value.length() > 2 && "-.".equals(value.substring(0, 2)))
				value = "-0" + value.substring(1, value.length());
		}
		// 对null值进行处理
		value = NullProcFactory.transNullToString(value, "&nbsp;");

		// td的title预警提示
		String tdtitle = "";
		// td的样式，预警用
		String tdstyle = "";
		// 箭头图片代码，预警用
		String arrow = "";
		// 字体颜色，预警用
		String fontcolor = "";
		// 如果有预警
		if ("Y".equals(dicts[j].need_alert)) {
			// 单元格颜色预警
			if (ReportConsts.ALERT_MODE_TDBGCOLOR.equals(dicts[j].alert_mode)) {
				tdstyle = genTdBgAlert(dicts[j], result[i], iNum, has_last, has_loop);
			}
			// 箭头颜色
			if (ReportConsts.ALERT_MODE_ARROW.equals(dicts[j].alert_mode)) {
				arrow = genArrowAlert(dicts[j], result[i], iNum, has_last, has_loop);
			}
			// 文字颜色
			if (ReportConsts.ALERT_MODE_FONTCOLOR.equals(dicts[j].alert_mode)) {
				fontcolor = genFontAlert(dicts[j], result[i], iNum, has_last, has_loop);
				if (fontcolor != null && !"".equals(fontcolor)) {
					value = "<font color=\"" + fontcolor + "\">" + value + "</font>";
				}
			}
			// 预警提示
			if (has_last || has_loop) {
				tdtitle = alert_msg + genAlertTdTile(dicts[j], result[i], iNum, has_last);
			}
			arrow = StringB.replace(arrow, "::ALT::", tdtitle);
		}

		// 开始组合td元素
		String ret = "";
		if ("Y".equals(dicts[j].has_link)) {
			String url = genUrl(dicts, result, i, j);
			String target = dicts[j].link_target;
			ret += "<TD title=\"" + tdtitle + "\" " + tdstyle + " nowrap align=\"right\">";
			ret += "<a href=\"" + url + "\" target=\"" + target + "\" class=bule>";
			ret += value + "</a>" + arrow + "</TD>\n";
		} else {
			ret += "<TD title=\"" + tdtitle + "\" " + tdstyle + " nowrap align=\"right\">";
			ret += value + arrow + "</TD>\n";
		}

		// 添加占比列
		if ("Y".equals(dicts[j].has_comratio)) {
			// 合计行值，用于计算占比
			String[] colsum = result[result.length - 1];
			if (i == result.length - 1) {
				if (colsum[iNum] == null || "".equals(colsum[iNum]))
					value = "&nbsp;";
				else
					value = "100%";
			} else {
				String tValue = result[i][iNum];
				value = Arith.divPer(tValue, colsum[iNum], 2);
			}
			value = NullProcFactory.transNullToString(value, "&nbsp;");
			ret += "<TD nowrap align=\"right\" " + ReportConsts.STYLE_TD + ">" + value + "</TD>\n";
		}

		// 添加同比列
		if (has_last && ReportConsts.YES.equals(dicts[j].has_same)) {
			iNum++;
			String last = result[i][iNum];
			if (last == null || "".equals(last)) {
				last = "不存在";
			}
			iNum++;
			value = result[i][iNum];
			value = Arith.divPer(value, "1", 2);
			value = NullProcFactory.transNullToString(value, "&nbsp;");
			ret += "<TD nowrap title=\"同比值：" + last + "\" align=\"right\" " + ReportConsts.STYLE_TD
					+ ">";
			ret += value + "</TD>\n";
		}

		// 添加环比列
		if (has_loop && ReportConsts.YES.equals(dicts[j].has_last)) {
			iNum++;
			String loop = result[i][iNum];
			if (loop == null || "".equals(loop)) {
				loop = "不存在";
			}
			iNum++;
			value = result[i][iNum];
			value = Arith.divPer(value, "1", 2);
			value = NullProcFactory.transNullToString(value, "&nbsp;");
			ret += "<TD nowrap title=\"环比值：" + loop + "\" align=\"right\" " + ReportConsts.STYLE_TD
					+ ">";
			ret += value + "</TD>\n";
		}
		return ret;
	}

	/**
	 * 获取指标数据
	 *
	 * @param measureData
	 * @param iDataCol
	 * @return
	 */
	private static String genMeasureValue(String[] measureData, int iDataCol) {
		String sValue = measureData[iDataCol];
		/*
		 * if (sValue == null || "".equals(sValue)) return "";
		 */

		if (ReportConsts.ZERO.equals(measureData[8])) {
			sValue = "免报";
		} else if (ReportConsts.SECOND.equals(measureData[1])) {
			// 非指标，标题部分
			sValue = "";
		} else {
			int dig_len = StringB.toInt(measureData[5], 0);
			sValue = FormatUtil.formatStr(sValue, dig_len, true);
			// sValue = NullProcFactory.transNullToString(sValue, "&nbsp;");
			sValue = NullProcFactory.transNullEmptyToZero(sValue);
		}
		return sValue;
	}

	/**
	 * 获取指标数据
	 *
	 * @param sValue
	 * @return
	 */
	private static String getValue(String sValue) {
		if (sValue == null || "".equals(sValue)) {
			return "";
		}
		return Arith.round(sValue, 0);
	}

	/**
	 * 设置指标数据
	 *
	 * @param sValue
	 * @param iDil
	 * @return
	 */
	private static String setValue(String sValue, int iDil) {
		if (sValue == null || "".equals(sValue)) {
			return "";
		}
		// System.out.println("sValue=" + sValue);
		String tmpV = Arith.round(sValue, 0);
		if ("-999999".equals(tmpV)) {
			sValue = "免报";
		} else {
			sValue = FormatUtil.formatStr(sValue, iDil, true);
			sValue = NullProcFactory.transNullToString(sValue, "&nbsp;");
		}
		return sValue;
	}

	/**
	 * 单元格底色预警
	 *
	 * @param dict
	 * @param result
	 * @param iNum
	 * @param has_last
	 * @param has_loop
	 * @return
	 */
	private static String genTdBgAlert(RptColDictTable dict, String[] result, int iNum,
			boolean has_last, boolean has_loop) {
		String ret = "";
		String highValue = dict.high_value;
		String lowValue = dict.low_value;
		String ratio = genAlertRatio(dict, result, iNum, has_last, has_loop);

		if (null != ratio && !"".equals(ratio) && null != highValue && !"".equals(highValue)
				&& null != lowValue && !"".equals(lowValue)) {
			double dRatio = StringB.toDouble(ratio, 0);
			double dHighValue = StringB.toDouble(highValue, 0);
			double dLowValue = StringB.toDouble(lowValue, 0);
			if (dRatio >= dHighValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.rise_color)) {
					// 绿色
					ret = ReportConsts.STYLE_ALERT_BG_GREEN;
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.rise_color)) {
					// 红色
					ret = ReportConsts.STYLE_ALERT_BG_RED;
				}
				alert_msg += "高位预警值 = " + dHighValue + "\n";
			}
			if (dRatio <= dLowValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.down_color)) {
					// 绿色
					ret = ReportConsts.STYLE_ALERT_BG_GREEN;
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.down_color)) {
					// 红色
					ret = ReportConsts.STYLE_ALERT_BG_RED;
				}
				alert_msg += "低位预警值 = " + dLowValue + "\n";
			}
		}
		return ret;
	}

	/**
	 * 箭头预警
	 *
	 * @param dict
	 * @param result
	 * @param iNum
	 * @param has_last
	 * @param has_loop
	 * @return
	 */
	private static String genArrowAlert(RptColDictTable dict, String[] result, int iNum,
			boolean has_last, boolean has_loop) {
		String ret = "";
		String highValue = dict.high_value;
		String lowValue = dict.low_value;
		String ratio = genAlertRatio(dict, result, iNum, has_last, has_loop);

		if (null != ratio && !"".equals(ratio) && null != highValue && !"".equals(highValue)
				&& null != lowValue && !"".equals(lowValue)) {
			double dRatio = StringB.toDouble(ratio, 0);
			double dHighValue = StringB.toDouble(highValue, 0);
			double dLowValue = StringB.toDouble(lowValue, 0);
			if (dRatio >= dHighValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.rise_color)) {
					// 绿色
					ret = ReportConsts.IMG_ALERT_ARROW_RISE_GREEN;
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.rise_color)) {
					// 红色
					ret = ReportConsts.IMG_ALERT_ARROW_RISE_RED;
				}
				alert_msg += "高位预警值 = " + dHighValue + "\n";
			}
			if (dRatio <= dLowValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.down_color)) {
					// 绿色
					ret = ReportConsts.IMG_ALERT_ARROW_DOWN_GREEN;
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.down_color)) {
					// 红色
					ret = ReportConsts.IMG_ALERT_ARROW_DOWN_RED;
				}
				alert_msg += "低位预警值 = " + dLowValue + "\n";
			}
		}
		if (ret == null || "".equals(ret.trim())) {
			// 为了对齐，需要放置一个空图片占位置
			ret = ReportConsts.IMG_ALERT_ARROW_BLANK;
		}
		return ret;
	}

	/**
	 * 文字颜色预警
	 *
	 * @param dict
	 * @param result
	 * @param iNum
	 * @param has_last
	 * @param has_loop
	 * @return
	 */
	private static String genFontAlert(RptColDictTable dict, String[] result, int iNum,
			boolean has_last, boolean has_loop) {
		String ret = "";
		String highValue = dict.high_value;
		String lowValue = dict.low_value;
		String ratio = genAlertRatio(dict, result, iNum, has_last, has_loop);

		if (null != ratio && !"".equals(ratio) && null != highValue && !"".equals(highValue)
				&& null != lowValue && !"".equals(lowValue)) {
			double dRatio = StringB.toDouble(ratio, 0);
			double dHighValue = StringB.toDouble(highValue, 0);
			double dLowValue = StringB.toDouble(lowValue, 0);
			if (dRatio >= dHighValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.rise_color)) {
					// 绿色
					ret = "green";
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.rise_color)) {
					// 红色
					ret = "red";
				}
				alert_msg += "高位预警值 = " + dHighValue + "\n";
			}
			if (dRatio <= dLowValue) {
				// 超出了高限
				if (ReportConsts.ALERT_COLOR_GREEN.equalsIgnoreCase(dict.down_color)) {
					// 绿色
					ret = "green";
				}
				if (ReportConsts.ALERT_COLOR_RED.equalsIgnoreCase(dict.down_color)) {
					// 红色
					ret = "red";
				}
				alert_msg += "低位预警值 = " + dLowValue + "\n";
			}
		}
		return ret;
	}

	/**
	 * 需要预警的对象值
	 *
	 * @param dict
	 * @param result
	 * @param iNum
	 * @param has_same
	 * @param has_last
	 * @return
	 */
	private static String genAlertRatio(RptColDictTable dict, String[] result, int iNum,
			boolean has_same, boolean has_last) {
		String ratio = "";
		if (ReportConsts.ALERT_COMPARE_TO_THIS_PERIOD.equals(dict.compare_to)) {
			// 和本身值比较
			ratio = result[iNum];
			alert_msg = "自身预警方式\n";
		}
		if (has_same && ReportConsts.ALERT_COMPARE_TO_SAME_PERIOD.equals(dict.compare_to)) {
			int realIndex = iNum;
			if (ReportConsts.YES.equalsIgnoreCase(dict.ratio_compare)) {
				// 同比比率
				realIndex = iNum + 2;
				alert_msg = "同比比率预警方式\n";
			} else {
				// 同比值
				realIndex = iNum + 1;
				alert_msg = "同比值预警方式\n";
			}
			ratio = result[realIndex];
		}
		if (has_last && ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD.equals(dict.compare_to)) {
			int realIndex = iNum;
			if (has_same) {
				iNum = iNum + 2;
			}
			if (ReportConsts.YES.equalsIgnoreCase(dict.ratio_compare)) {
				// 环比比率
				realIndex = iNum + 2;
				alert_msg = "环比比率预警方式\n";
			} else {
				// 环比值
				realIndex = iNum + 1;
				alert_msg = "环比值预警方式\n";
			}
			ratio = result[realIndex];
		}
		return ratio;
	}

	/**
	 * 预警提示
	 *
	 * @param dict
	 * @param result
	 * @param iNum
	 * @return
	 */
	private static String genAlertTdTile(RptColDictTable dict, String[] result, int iNum,
			boolean has_last) {
		String ret = "";
		String ratio = "";
		if (null != result && result.length > (iNum + 2)) {
			if (ReportConsts.ALERT_COMPARE_TO_SAME_PERIOD.equals(dict.compare_to)) {
				// 同比比率
				ratio = result[iNum + 2];
				if (ratio == null || "".equals(ratio.trim())) {
					ratio = "不存在";
				} else {
					ratio = Arith.divPer(ratio, "1", 2);
					ratio = NullProcFactory.transNullToString(ratio, "&nbsp;");
				}
				ret += "同比：" + ratio;
				// 同比值
				ratio = result[iNum + 1];
				if (ratio == null || "".equals(ratio.trim()))
					ratio = "不存在";
				ret += "(" + ratio + ")";
			}
			if (ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD.equals(dict.compare_to)) {
				if (has_last) {
					iNum = iNum + 2;
				}
				// 环比比率
				ratio = result[iNum + 2];
				if (ratio == null || "".equals(ratio.trim())) {
					ratio = "不存在";
				} else {
					ratio = Arith.divPer(ratio, "1", 2);
					ratio = NullProcFactory.transNullToString(ratio, "&nbsp;");
				}
				ret += "环比：" + ratio;
				// 环比值
				ratio = result[iNum + 1];
				if (ratio == null || "".equals(ratio.trim()))
					ratio = "不存在";
				ret += "(" + ratio + ")";
			}
		}
		return ret;
	}

	/**
	 * 获取超链接地址
	 *
	 * @param dicts
	 * @param result
	 * @param i
	 * @param j
	 * @return
	 */
	private static String genUrl(RptColDictTable[] dicts, String[][] result, int i, int j) {
		String ret = dicts[j].link_url;
		for (int m = 0; dicts != null && m < dicts.length; m++) {
			if (ReportConsts.DATA_TYPE_STRING.equals(dicts[m].data_type)) {
				if (m > 0 && ReportConsts.DATA_TYPE_NUMBER.equals(dicts[m - 1].data_type)) {
					break;
				}

				if (ret.length() > 0 && ret.indexOf("?") > 0)
					ret += "&";
				else
					ret += "?";
				String value = result[i][2 * m + 2];
				if ("zzzzzzzz".equals(value) || "zzzzzzzzzz".equals(value))
					value = "";
				ret += dicts[m].field_code + "=" + value;
			}
		}
		return ret;
	}

	/**
	 * 审核步骤结束，列出审核人
	 *
	 * @param listRrocessStep
	 * @param listProcessHis
	 * @return
	 * @throws AppException
	 */
	private static String genProcessOverLayout(List listProcessStep, List listProcessHis)
			throws AppException {
		String res = "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\">\n";
		res += "<tr>\n";
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep.get(i);
			// 审核步骤标志
			String step = pStep.p_step_flag;
			// 审核步骤名称
			String t_name = pStep.p_step_flag_name;
			for (int j = 0; listProcessHis != null && j < listProcessHis.size(); j++) {
				RptProcessHisTable hisTable = (RptProcessHisTable) listProcessHis.get(j);
				// 审核意见
				String tmp_decision = hisTable.p_decision;
				// 审核步骤
				String tmp_step = hisTable.p_step_flag;
				if (step.equals(tmp_step) && ReportConsts.YES.equals(tmp_decision)) {
					res += "<td nowrap>";
					res += t_name + "：" + hisTable.p_user_name;
					res += "</td>\n";
				}
			}
		}
		res += "</tr>\n";
		res += "</table>\n";
		return res;
	}

	/**
	 * 生成审核流程HTML代码
	 *
	 * @param user_role
	 * @param listProcessStep
	 * @param listProcessHis
	 * @return
	 * @throws AppException
	 */
	private static String genProcessLayout(String user_role, List listProcessStep,
			List listProcessHis) throws AppException {

		// RptProcessStepTable[] pStep, RptProcessHisTable[] hisTable

		// 当前用户拥有的角色
		String tu_role = "," + user_role + ",";
		// 报表当前审核步骤

		// 报表最后审核意见
		String now_decision = ReportConsts.YES;
		// 当前报表的前一个审核步骤
		String now_pre_step = "";
		// 报表最后审核操作员
		String l_user_name = "";
		if (listProcessHis != null && listProcessHis.size() > 0) {
			RptProcessHisTable last_his = (RptProcessHisTable) listProcessHis.get(0);
			now_decision = last_his.p_decision;
			now_pre_step = last_his.p_step_flag;
			l_user_name = last_his.p_user_name;
		}

		// 当前报表所处的审核状态信息
		RptProcessStateTable rptState = ReportProcessUtil.getRptProcessState(listProcessStep,
				listProcessHis);
		String now_step = rptState.now_step;
		int i_now_step = StringB.toInt(now_step, 99);
		String now_step_name = rptState.now_step_name;

		// 当前报表审核状态描述
		String now_desc = "";
		if (TableConsts.RETURN.equals(now_decision)) {
			now_desc = "用户“" + l_user_name + "”提出回退请求！";
		} else if (TableConsts.NO.equals(now_decision)) {
			now_desc = "用户“" + l_user_name + "”将审核步骤回退至“" + now_step_name + "”审核！";
		} else if (TableConsts.FORWARD.equals(now_decision)) {
			now_desc = "用户“" + l_user_name + "”将审核步骤置于“" + now_step_name + "”审核！";
		} else if (TableConsts.YES.equals(now_decision)) {
			if (TableConsts.FINAL_STEP.equals(now_pre_step)
					&& TableConsts.FINAL_STEP.equals(now_step)) {
				now_desc = "审核完成！";
			} else {
				now_desc = "待“" + now_step_name + "”审核！";
			}
		}

		// 按照审核流程步骤定义画出审核流程状态HTML
		String tr1 = "";
		String tr2 = "";
		for (int i = 0; listProcessStep != null && i < listProcessStep.size(); i++) {
			RptProcessStepTable pStep = (RptProcessStepTable) listProcessStep.get(i);
			// 审核步骤标志
			String t_step = pStep.p_step_flag;
			int i_t_step = StringB.toInt(t_step, 99);
			// 审核步骤名称
			String t_name = pStep.p_step_flag_name;
			// 可以审核该步骤角色
			String tp_role = ",'" + pStep.r_role_id + "',";
			// 可以审核回退的角色
			String tr_role = ",'" + CObjKnd.PREOCESS_RETURN_ROLE + "',";
			// 该步骤的审核历史记录内容
			String his_note = "";
			for (int j = 0; listProcessHis != null && j < listProcessHis.size(); j++) {
				RptProcessHisTable hisTable = (RptProcessHisTable) listProcessHis.get(j);
				String tmp_decision = hisTable.p_decision;
				// 先对当前审核步骤进行描述说明
				if (j == 0 && t_step.equals(hisTable.p_step_next)) {
					if (TableConsts.RETURN.equals(tmp_decision)) {
						his_note = "“" + t_name + "”提出回退请求！\n\n";
					} else if (TableConsts.NO.equals(tmp_decision)) {
						his_note = "待“" + t_name + "”审核！\n";
						his_note += "用户“" + l_user_name + "”将审核步骤回退至“" + now_step_name + "”审核！\n\n";
					} else if (TableConsts.FORWARD.equals(tmp_decision)) {
						his_note = "待“" + t_name + "”审核！\n";
						his_note += "用户“" + l_user_name + "”将审核步骤置于“" + now_step_name + "”审核！\n\n";
					} else if (TableConsts.YES.equals(tmp_decision)) {
						if (!TableConsts.FINAL_STEP.equals(now_pre_step)
								&& !TableConsts.FINAL_STEP.equals(now_step)) {
							his_note = "待“" + t_name + "”审核！\n\n";
						}
					}
				}

				if (t_step.equals(hisTable.p_step_flag)) {
					// 符合的审核历史记录
					if (his_note.length() > 0) {
						his_note += "历史审核记录：\n";
					}
					his_note += "审核时间：" + hisTable.p_time + "\n";
					his_note += "审核意见：";
					if (TableConsts.RETURN.equals(tmp_decision)) {
						his_note += "审核回退请求\n";
						his_note += "回退请求人：" + hisTable.p_user_name + "\n";
					} else if (TableConsts.NO.equals(tmp_decision)) {
						his_note += "审核回退。回退至“"
								+ ReportProcessUtil.getRptStepName(listProcessStep,
										hisTable.p_step_next) + "”审核\n";
						his_note += "回退批准人：" + hisTable.p_user_name + "\n";
					} else if (TableConsts.FORWARD.equals(tmp_decision)) {
						his_note += "审核被批准通过。前行至“"
								+ ReportProcessUtil.getRptStepName(listProcessStep,
										hisTable.p_step_next) + "”审核\n";
						his_note += "通过批准人：" + hisTable.p_user_name + "\n";
					} else {
						his_note += "审核通过\n";
						his_note += "审 核 人：" + hisTable.p_user_name + "\n";
					}
					his_note += "备注内容：" + hisTable.p_his_note + "\n";
					his_note += "\n";
				}
			}
			if (his_note == null || "".equals(his_note)) {
				// 未审批的步骤提示
				his_note = "当前步骤还没有被审核通过！";
			}

			if (i_t_step < i_now_step) {
				// 审核通过的步骤
				tr1 += "<td title=\"" + his_note + "\" class=\"auditing-bg\">";
				tr1 += "<img src=\"../images/auditing_pass.gif\" width=\"12\" height=\"14\">";
				tr1 += "<span class=\"fig-black\">" + (i + 1) + "</span>";
				tr1 += t_name;
				tr1 += "</td>\n";
				tr2 += "<td height=\"4\" bgcolor=\"#879EEA\"></td>";
			} else if (i_now_step == i_t_step) {
				// 当前审核步骤
				if (TableConsts.YES.equals(now_decision)
						&& TableConsts.FINAL_STEP.equals(now_pre_step)
						&& TableConsts.FINAL_STEP.equals(now_step)) {
					// 终审通过的步骤
					tr1 += "<td title=\"" + his_note + "\" class=\"auditing-bg\">";
					tr1 += "<img src=\"../images/auditing_pass.gif\" width=\"12\" height=\"14\">";
					tr1 += "<span class=\"fig-black\">" + (i + 1) + "</span>";
					tr1 += t_name;
					tr1 += "</td>\n";
					tr2 += "<td height=\"4\" bgcolor=\"#879EEA\"></td>";
				} else if (TableConsts.RETURN.equals(now_decision)) {
					// 回退请求状态
					tr1 += "<td title=\"" + his_note + "\" class=\"auditing-bg\">";
					tr1 += "<img src=\"../images/auditing_back.gif\" width=\"13\" height=\"13\">";
					tr1 += "<span class=\"fig-black\">" + (i + 1) + "</span>";
					if (tu_role.indexOf(tr_role) >= 0)
						tr1 += "<a href=\"javascript:;\" class=\"green1\" onMousedown=\"MM_showHideLayers('Layer1','','show')\">"
								+ t_name + "</a>\n";
					else
						tr1 += t_name;
					tr1 += "</td>\n";
					tr2 += "<td height=\"4\" bgcolor=\"#FCA426\"></td>";
				} else {
					// 正常当前审核步骤
					tr1 += "<td title=\"" + his_note + "\" class=\"auditing-bg\">";
					tr1 += "<img src=\"../images/auditing_now.gif\" width=\"17\" height=\"10\">";
					tr1 += "<span class=\"fig-red\">" + (i + 1) + "</span>";
					if (tu_role.indexOf(tp_role) >= 0)
						tr1 += "<a href=\"javascript:;\" class=\"red\" onMousedown=\"MM_showHideLayers('Layer1','','show')\">"
								+ t_name + "</a>\n";
					else
						tr1 += t_name;
					tr1 += "</td>\n";
					tr2 += "<td height=\"4\" bgcolor=\"#E23127\"></td>";
				}
			} else if (i_t_step > i_now_step) {
				// 未审核通过的步骤
				tr1 += "<td title=\"" + his_note + "\" class=\"auditing-bg\">";
				tr1 += "<span class=\"fig-ash\">" + (i + 1) + "</span>";
				tr1 += "<span class=\"ash\">" + t_name + "</span>";
				tr1 += "</td>\n";
				tr2 += "<td height=\"4\" bgcolor=\"#AFAFAF\"></td>";
			}
		}

		String res = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\">\n";
		res += "<tr>\n";
		res += tr1;
		res += "</tr>";
		res += "<tr>\n";
		res += tr2;
		res += "</tr>";
		res += "<tr>";
		res += "<td height=\"20\" valign=\"bottom\" colspan=" + listProcessStep.size() + ">";
		res += "<span class=\"croci\">报表状态：</span> " + now_desc;
		res += "</td>";
		res += "</tr>";
		res += "</table>\n";

		return res;
	}

	/**
	 * 获取审核操作HTML代码
	 *
	 * @param rpt_id
	 * @param rpt_date
	 * @param rptProcessState
	 * @param oper_no
	 * @param oper_name
	 * @return
	 */
	private static StringBuffer genProcessOperateLayout(String rpt_id, String rpt_date,
			RptProcessStateTable rptProcessState, String oper_no, String oper_name,
			String sqlReturn, String sqlForward) throws AppException {
		StringBuffer strLay = new StringBuffer("");

		strLay.append("<input type=\"hidden\" id=\"rpt_id\" name=\"rpt_id\" value=\"" + rpt_id
				+ "\" />");
		strLay.append("<input type=\"hidden\" id=\"rpt_date\" name=\"rpt_date\" value=\""
				+ rpt_date + "\" />");
		strLay.append("<input type=\"hidden\" id=\"p_id\" name=\"p_id\" value=\""
				+ rptProcessState.p_id + "\" />");
		strLay.append("<input type=\"hidden\" id=\"p_step_flag\" name=\"p_step_flag\" value=\""
				+ rptProcessState.now_step + "\" />");
		strLay.append("<input type=\"hidden\" id=\"p_step_next\" name=\"p_step_next\" value=\""
				+ rptProcessState.next_step + "\" />");
		strLay.append("<input type=\"hidden\" id=\"p_user_id\" name=\"p_user_id\" value=\""
				+ oper_no + "\" />");
		strLay.append("<div id=\"Layer1\" style=\"position:absolute; left:225px; top:148px; width:330px; height:179px; z-index:1; visibility: hidden;\">");
		strLay.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
		strLay.append("<tr>");
		strLay.append("<td width=\"7\"><img src=\"../images/sh/tab_corner_1.gif\" width=\"7\" height=\"22\"></td>");
		strLay.append("<td valign=\"baseline\" background=\"../images/sh/tab_corner_bg.gif\" >&nbsp;</td>");
		strLay.append("<td width=\"10\" align=\"right\" valign=\"baseline\" background=\"../images/sh/tab_corner_bg.gif\" ><img src=\"../images/sh/tab_corner_5.gif\" width=\"7\" height=\"22\"></td>");
		strLay.append("</tr>");
		strLay.append("</table>");
		strLay.append("<TABLE width=100% height=100% border=0 cellPadding=0 cellspacing=\"0\" bgcolor=\"#FFFFFF\">");
		strLay.append("<TBODY>");
		strLay.append("<TR>");
		strLay.append("<TD width=4 background=\"../images/sh/biao_l_m.gif\"></TD>");
		strLay.append("<TD width=\"100%\" bgcolor=\"#FFFFFF\">");
		strLay.append("<table width=\"95%\" align=\"center\">");
		strLay.append("<tr>");
		strLay.append("<td width=\"21%\"><p>审核意见：</p></td>");
		strLay.append("<td width=\"79%\">");
		if (TableConsts.RETURN.equals(rptProcessState.now_decision)) {
			// 当前步骤状态为回退请求时
			strLay.append("<input type=\"radio\" name=\"p_decision\" value=\"N\" checked onClick=\"showReturn(this);\">同意回退");
			strLay.append("<input type=\"radio\" name=\"p_decision\" value=\"F\" onClick=\"showReturn(this);\">审核通过");
		} else if (TableConsts.FRIST_STEP.equals(rptProcessState.now_step)) {
			// 当前步骤为第一步的时候
			strLay.append("<input type=\"radio\" name=\"p_decision\" value=\"Y\" checked>审核通过");
		} else {
			strLay.append("<input type=\"radio\" name=\"p_decision\" value=\"Y\" checked>审核通过");
			strLay.append("<input type=\"radio\" name=\"p_decision\" value=\"R\">回退请求");
		}
		strLay.append("</td>");
		strLay.append("</tr>");

		if (TableConsts.RETURN.equals(rptProcessState.now_decision)) {
			String[][] arr1 = WebDBUtil.execQryArray(sqlReturn, "");
			String[][] arr2 = WebDBUtil.execQryArray(sqlForward, "");

			strLay.append("<tr id=\"tr_p_return\">");
			strLay.append("<td>回退步骤：</td>");
			strLay.append("<td>");
			strLay.append("<SELECT ID=p_return name=p_return>");
			for (int i = 0; arr1 != null && i < arr1.length; i++) {
				strLay.append("<OPTION value='" + arr1[i][0] + "'>");
				strLay.append(arr1[i][1]);
				strLay.append("</OPTION>");
			}
			strLay.append("</SELECT>");
			strLay.append("</td>");
			strLay.append("</tr>");
			strLay.append("<tr id=\"tr_p_forward\" style=\"display:none\">");
			// if (!TableConsts.FINAL_STEP.equals(stepInfo[4])) {
			strLay.append("<td>通过步骤：</td>");
			strLay.append("<td>");
			strLay.append("<SELECT ID=p_forward name=p_forward>");
			for (int i = 0; arr2 != null && i < arr2.length; i++) {
				strLay.append("<OPTION value='" + arr2[i][0] + "'>");
				strLay.append(arr2[i][1]);
				strLay.append("</OPTION>");
			}
			strLay.append("</SELECT>");
			strLay.append("</td>");
			// }
			strLay.append("</tr>");
		}

		strLay.append("<tr>");
		strLay.append("<td>审 核 人：</td>");
		strLay.append("<td><input name=\"p_user_name\" type=\"text\" class=\"input-text\" value=\""
				+ oper_name + "\" readonly></td>");
		strLay.append("</tr>");
		strLay.append("<tr>");
		strLay.append("<td valign=\"top\">备　　注：</td>");
		strLay.append("<td><textarea name=\"p_step_note\" cols=\"35\" rows=\"4\" class=\"input-text\"></textarea></td>");
		strLay.append("</tr>");
		strLay.append("<tr>");
		strLay.append("<td>&nbsp;</td>");
		strLay.append("<td height=\"30\">");
		strLay.append("<input type=\"submit\" id=\"tj\" name=\"tj\" class=\"button\" onClick=\"MM_goToURL('self','rptProcess.rptdo?opType=process_auditing');return document.MM_returnValue\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" value=\"确定\"> ");
		strLay.append("<input type=\"button\" name=\"qx\" class=\"button\" onClick=\"MM_showHideLayers('Layer1','','hidden')\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" value=\"取消\">");
		strLay.append("</td>");
		strLay.append("</tr>");
		strLay.append("</table></TD>");
		strLay.append("<TD width=4 background=\"../images/sh/biao_r_m.gif\"></TD>");
		strLay.append("</TR>");
		strLay.append("<TR>");
		strLay.append("<TD width=4><IMG height=4 src=\"../images/sh/biao_l_b.gif\" width=4 border=0></TD>");
		strLay.append("<TD height=\"4\" background=\"../images/sh/biao_b_m.gif\"></TD>");
		strLay.append("<TD width=4><IMG height=4 src=\"../images/sh/biao_r_b.gif\" width=4 border=0></TD>");
		strLay.append("</TR>");
		strLay.append("</TBODY>");
		strLay.append("</TABLE>");
		strLay.append("</div>");

		return strLay;
	}

	public DimRuleStruct getMeasureReportDim(String report_id) {

		DimRuleStruct struct = imDataDao.getReportDim(report_id);
		return struct;
	}

	// 表头名
	public static void getHeadMap() {
		headMap = new HashMap();
		headMap.put("1", "指标编码");
		headMap.put("2", "指标名称");
		headMap.put("3", "指标单位");
		headMap.put("4", "数据类型");
		headMap.put("v", "指标值");
	}

	// Q3105对应的字段序列，从0开始算
	public static void getFieldMap() {
		fieldMap = new HashMap();
		fieldMap.put("1", "2");
		fieldMap.put("2", "3");
		fieldMap.put("3", "4");
		fieldMap.put("4", "9");
	}

}
