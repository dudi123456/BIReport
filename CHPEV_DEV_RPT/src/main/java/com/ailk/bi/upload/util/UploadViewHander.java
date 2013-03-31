package com.ailk.bi.upload.util;

import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;
import com.ailk.bi.upload.domain.UiMsuUpReportMetaInfoTable;

/**
 * 总部上传视图（界面）代码处理类，区分于特定工具类和业务逻辑类
 *
 *
 * @author Chunming
 *
 */
public class UploadViewHander {

	/**
	 * 生成表格头
	 *
	 * @param msuMeta
	 * @return
	 */
	public static String getHeadString(UiMsuUpReportMetaInfoTable[] msuMeta,
			String rpt_type) {
		StringBuffer head = new StringBuffer("");

		if (msuMeta != null && msuMeta.length > 0) {
			head.append("<tr class=\"tl\" align=\"center\">");
			if ("M".equals(rpt_type)) {
				head.append("<td nowrap >月 份</td>");
			} else {
				head.append("<td nowrap >日 期</td>");
			}
			//
			for (int i = 0; i < msuMeta.length; i++) {
				head.append("<td");
				head.append(" title=\"" + msuMeta[i].getTitle_desc() + "\"");
				head.append(" onclick=\"chartSeries('"
						+ msuMeta[i].getNew_title() + "')\");>");
				head.append("<a href=\"javascript:;\"	 align=\"center\" >"
						+ msuMeta[i].getNew_title() + msuMeta[i].getUnit()
						+ "</a>");
				head.append("</td>");

			}
			head.append("</tr>");
		}

		return head.toString();

	}

	/**
	 * 取得表格内容
	 *
	 * @param list
	 * @param reportInfo
	 * @param msuMeta
	 * @return
	 */
	public static String getTableString(String[][] list,
			UiMsuUpReportConfigTable reportInfo,
			UiMsuUpReportMetaInfoTable[] msuMeta) {
		StringBuffer tab = new StringBuffer("");

		if (list != null && list.length > 0) {
			for (int i = list.length - 1; i >= 0; i--) {
				if (i % 2 == 0) {
					tab.append("<tr class=\"bgwl\" >");
				} else {
					tab.append("<tr class=\"jg\" >");
				}
				tab.append("<td nowrap align=\"center\">");
				tab.append("<a href=\"uploadmsu_info_detail.jsp?qry__begin_day="
						+ list[i][0]
						+ "&report_id="
						+ reportInfo.getReport_id()
						+ "\" target=\"_blank\" style=\"cursor:hand\"><b>" + list[i][0] + "</b></a>");
				tab.append("</td>");

				for (int j = 1; j < list[i].length; j++) {
					String tempSum = list[i][j];
					int digit = 0;
					if (!"".equals(msuMeta[j - 1].getDigit())
							&& msuMeta[j - 1].getDigit().length() > 0) {
						digit = Integer.parseInt(msuMeta[j - 1].getDigit());
					}
					tab.append("<td nowrap  align=\"right\">"
							+ FormatUtil.formatStr(tempSum, digit, true)
							+ "</td>");
				}
				tab.append("</tr>");
			}
		} else {

			tab.append("<tr class=\"bgwl\">");
			tab.append("<td colspan=\"");
			if (msuMeta != null && msuMeta.length > 0) {
				tab.append(msuMeta.length + 1);
			} else {
				tab.append(1);
			}
			tab.append("\" align=\"center\" >无数据</td>");
			tab.append("</tr>");
		}

		return tab.toString();

	}

	/**
	 *
	 * @param category
	 * @param series
	 * @param value
	 * @param index
	 * @return
	 */

	public static WebChart getChartObj(String[] category, String[] series,
			String[][] value, int index) {
		WebChart jfreechart = null;
		if (index == 0) {
			index = 1;
		}

		double[][] values = setDoubleV(category, series, value, index);
		double[][] others = getTarget(category, series, values);
		jfreechart = new WebChart(series, category, others);
		return jfreechart;
	}

	/**
	 *
	 * @param category
	 * @param series
	 * @param Source
	 * @return
	 */

	public static double[][] getTarget(String[] category, String[] series,
			double[][] Source) {

		double[][] target = new double[series.length][category.length];
		for (int i = 0; i < Source.length; i++) {
			for (int j = 0; j < Source[i].length; j++) {
				target[j][i] = Source[i][j];
			}
		}
		return target;
	}

	/**
	 * 用于记录集生成double[][] value
	 *
	 * @param category
	 * @param series
	 * @param value
	 * @param indexValue
	 * @return
	 */
	public static double[][] setDoubleV(String[] category, String[] series,
			String[][] value, int indexValue) {
		double[][] result = null;
		if (category != null && category.length > 0 && series != null
				&& series.length > 0)
			result = new double[category.length][series.length];
		else
			return null;

		String tmp1 = "";
		String tmp2 = "";
		String res1 = "";
		String res2 = "";

		for (int i = 0; category != null && i < category.length; i++) {
			for (int j = 0; series != null && j < series.length; j++) {
				tmp1 = category[i];
				tmp2 = series[j];
				try {
					for (int m = 0; value != null && m < value.length; m++) {
						res1 = value[m][0];
						if (tmp1.equals(res1)) {
							for (int n = 0; series != null && n < series.length; n++) {
								res2 = series[n];
								if (tmp2.equals(res2)) {
									String tmp = value[m][indexValue + n];
									result[i][j] = Double.parseDouble(tmp);
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					result[i][j] = 0;
				}
			}
		}
		return result;
	}

}
