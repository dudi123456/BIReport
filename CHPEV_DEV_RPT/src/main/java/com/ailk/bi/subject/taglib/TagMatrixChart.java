package com.ailk.bi.subject.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.common.app.Arith;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.subject.util.SplitChartOpt;
import com.ailk.bi.subject.util.SubjectMatrixChartUtil;

public class TagMatrixChart extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805085389577186305L;

	/**
	 * 图形ID
	 */
	private String chartId;

	private ReportQryStruct qryStruct;

	private String rootPath = "";

	public String getRootPath() {

		return rootPath;

	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public int doEndTag() throws JspException {

		SubjectMatrixChartUtil chartUtil = new SubjectMatrixChartUtil(chartId,
				qryStruct, (HttpServletRequest) pageContext.getRequest());
		MatrixChartBean bean = chartUtil.getMatrixChartDef();
		SplitChartOpt s_chart = chartUtil.getSplitChartOpt_Info(bean);

		String tableid = bean.getAnaTableId();

		String[] arrX = new String[bean.getSplitCntX()];
		switch (bean.getSplitCntX()) {
		case SubjectMatrixChartUtil.CONST_SPLITX_ONE:
			arrX[0] = qryStruct.splitx_one;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_TWO:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_THREE:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			arrX[2] = qryStruct.splitx_three;

			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_FOUR:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			arrX[2] = qryStruct.splitx_three;
			arrX[3] = qryStruct.splitx_four;

			break;

		}

		String[] arrY = new String[bean.getSplitCntY()];
		switch (bean.getSplitCntX()) {
		case SubjectMatrixChartUtil.CONST_SPLITY_ONE:
			arrY[0] = qryStruct.splity_one;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_TWO:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_THREE:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			arrY[2] = qryStruct.splity_three;

			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_FOUR:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			arrY[2] = qryStruct.splity_three;
			arrY[3] = qryStruct.splity_four;

			break;
		}

		String[] RatesX = s_chart.getRate(arrX, "per1");
		String[] RatesY = s_chart.getRate(arrY, "per2");

		// 刻度
		String measure_col = bean.getFilterXY();
		String xDesc = bean.getXDesc();
		String yDesc = bean.getYDesc();
		String yColDesc = bean.getYColDesc();
		String[][] arrValue = s_chart.getRateValueArr(RatesX, RatesY);
		String[][] arrDesc = s_chart.getRateValueArr(RatesX, RatesY,
				bean.getChartName() + "----" + xDesc + "：", " " + yDesc + "：");
		String[][] RateDatas = s_chart.getRateData(RatesX, RatesY);
		int m = 0;
		int irow = 0;

		int irowtable = RatesY.length;
		int icoltable = 2 + RatesX.length;
		int itablewidth = bean.getTableWidth();
		int itableheight = bean.getTableHeight();
		int irowchartwidth = itablewidth * (icoltable - 1);

		StringBuffer sb = new StringBuffer();

		sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
		sb.append("<tr>\r\n");
		sb.append("<td width=\"100%\" height=\"30\" align=\"center\"><span id=\"desc\"></span></td>\r\n");
		sb.append("</tr>\r\n");
		sb.append("</table>\r\n");
		sb.append("<table  border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
		sb.append("<tr>\r\n");
		int iTmp = irowtable + 5;
		sb.append("<td width=\"40\" align=\"right\" valign=\"center\" rowspan=\""
				+ iTmp + "\">" + yColDesc + "</td>");
		sb.append("<td colspan=\"3\" align=\"center\" valign=\"bottom\"><img src=\""
				+ rootPath
				+ "/biimages/ctable/arrow2.gif\" width=\"7\" height=\"7\"></td>");
		iTmp = icoltable - 1;
		sb.append("<td colspan=\"" + iTmp + "\" valign=\"bottom\"></td>\r\n");
		sb.append("</tr>\r\n");
		sb.append("<tr>\r\n");
		iTmp = itablewidth + 2;
		sb.append("<td width=\"" + iTmp
				+ "\"  height=\"10\" align=\"right\" valign=\"top\"></td>");
		iTmp = irowtable + 1;
		sb.append("<td width=\"2\" rowspan=\"" + iTmp
				+ "\" align=\"center\" valign=\"bottom\" background=\""
				+ rootPath + "/biimages/ctable/line1.gif\"></td>");
		sb.append("<td colspan=\"" + icoltable + "\" align=\"center\"></td>");
		sb.append("</tr>\r\n");

		sb.append("<tr>\r\n");
		sb.append("<td height=\"50\" valign=\"bottom\"><table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tr>\r\n");
		sb.append("<td height=\"30\" align=\"right\">列合计</td>");
		sb.append("</tr>\r\n");
		sb.append("<tr>\r\n");
		sb.append("<td align=\"right\">高:" + RatesY[RatesY.length - 1]
				+ "</td>");
		sb.append("</tr>\r\n");
		sb.append("</table></td>\r\n");
		sb.append("<td colspan=\"" + RatesX.length + "\" rowspan=\""
				+ RatesY.length + "\" valign=\"top\">");
		sb.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

		boolean blnIsAnaTable = true;

		if (bean.getAnaTableId().length() == 0) {
			blnIsAnaTable = false;
		}

		for (int j = RatesY.length - 1; j >= 0; j--) {
			sb.append("<tr>\r\n");

			for (int i = 0; i < RatesX.length; i++) {
				if (blnIsAnaTable) {// js控制分析型表格
					if (j == RatesY.length - 1 && i == RatesX.length - 1) {
						sb.append("<td width=\""
								+ itablewidth
								+ "\" height=\""
								+ itableheight
								+ "\" align=\"center\" bgcolor=\"#FFFF99\" id=\"X"
								+ i + "Y" + j + "\" onclick=\"tableori(this,'"
								+ tableid + "','" + arrDesc[irow][i]
								+ "')\" style=\"cursor:hand\">");
					} else {
						sb.append("<td width=\""
								+ itablewidth
								+ "\" height=\""
								+ itableheight
								+ "\" align=\"center\" bgcolor=\"#FFFF99\" id=\"X"
								+ i + "Y" + j + "\" onclick=\"tablec(this,'"
								+ tableid + "','" + measure_col + "','"
								+ arrValue[irow][i] + "','F','F','"
								+ arrDesc[irow][i]
								+ "')\" style=\"cursor:hand\">");
					}
				} else {// js调用自定义函数
					if (j == RatesY.length - 1 && i == RatesX.length - 1) {
						sb.append("<td width=\""
								+ itablewidth
								+ "\" height=\""
								+ itableheight
								+ "\" align=\"center\" bgcolor=\"#FFFF99\" id=\"X"
								+ i + "Y" + j + "\" onclick=\""
								+ bean.getJsFunName() + "(this,'" + tableid
								+ "','" + arrDesc[irow][i]
								+ "')\" style=\"cursor:hand\">");
					} else {
						sb.append("<td width=\""
								+ itablewidth
								+ "\" height=\""
								+ itableheight
								+ "\" align=\"center\" bgcolor=\"#FFFF99\" id=\"X"
								+ i + "Y" + j + "\" onclick=\""
								+ bean.getJsFunName() + "(this,'" + tableid
								+ "','" + measure_col + "','"
								+ arrValue[irow][i] + "','F','F','"
								+ arrDesc[irow][i]
								+ "')\" style=\"cursor:hand\">");
					}
				}

				sb.append("共" + RateDatas[0][m++] + "个<br>");
				sb.append("(" + Arith.divPerZero(RateDatas[0][m++], "1", 2)
						+ ")</td>");
				sb.append("<td width=\"1\" background=\"" + rootPath
						+ "/biimages/ctable/line1.gif\"></td>");
			}
			irow++;
			sb.append("</tr>\r\n");
			if (j != 0) {
				sb.append("<tr background=\"" + rootPath
						+ "/biimages/ctable/line2.gif\">");
				sb.append("<td height=\"1\" colspan=\"11\" background=\""
						+ rootPath + "/biimages/ctable/line2.gif\"></td>");
				sb.append("</tr>\r\n");

			}
		}
		sb.append("</table></td>\r\n");
		sb.append("<td align=\"center\" height=\"" + itableheight
				+ "\">&nbsp;</td>");
		sb.append("<td align=\"center\" height=\"" + itableheight
				+ "\">&nbsp;</td>");
		sb.append("</tr>\r\n");

		for (int j = RatesY.length - 2; j >= 0; j--) {
			sb.append("<tr>\r\n");
			sb.append("<td height=\"51\" valign=\"bottom\"><table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			sb.append("<tr>\r\n");
			sb.append("<td align=\"right\">");
			if (j == 0) {
				sb.append("低:");
			}
			sb.append(RatesY[j] + "</td>");
			sb.append("</tr>\r\n");
			sb.append("</table></td>\r\n");
			sb.append("<td align=\"center\" height=\"" + itableheight
					+ "\">&nbsp;</td>");

			if (j == 0) {
				sb.append("<td rowspan=\"3\" align=\"center\"><img src=\""
						+ rootPath
						+ "/biimages/ctable/arrow.gif\" width=\"7\" height=\"9\"></td>");

			} else {
				sb.append("<td align=\"center\" height=\"" + itableheight
						+ "\">&nbsp;</td>");
			}
			sb.append("</tr>\r\n");

		}
		sb.append("<tr>\r\n");

		sb.append("<td height=\"2\" colspan=\"" + icoltable + 1
				+ "\" align=\"right\"><img src=\"" + rootPath
				+ "/biimages/ctable/line2.gif\" width=\"" + irowchartwidth
				+ "\" height=\"2\"></td>");
		sb.append("</tr>\r\n");
		sb.append("<tr>\r\n");
		sb.append("<td height=\"25\" align=\"right\" valign=\"top\">低:"
				+ RatesX[0] + "</td>");
		sb.append("<td width=\"2\" background=\"" + rootPath
				+ "/biimages/ctable/line1.gif\"></td>");
		sb.append("<td colspan=\"" + RatesX.length
				+ "\" align=\"center\" valign=\"top\">");
		sb.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");

		for (int j = 1; j < RatesX.length + 1; j++) {
			if (j == RatesX.length) {
				sb.append("<td width=\"" + itablewidth
						+ "\">高&nbsp;&nbsp;&nbsp;&nbsp;行合计</td>");
				sb.append("");
				sb.append("");

			} else {
				sb.append("<td width=\"" + itablewidth
						+ "\" height=\"25\" align=\"right\">" + RatesX[j]
						+ "</td>");
			}
		}
		sb.append("</tr></table>\r\n");
		sb.append("<br>" + xDesc + "</td>");
		sb.append("<td align=\"center\" width=\"" + itablewidth / 4
				+ "\" height=\"" + itableheight + "\">&nbsp;</td>");

		sb.append("</tr></table>\r\n");

		JspWriter out = pageContext.getOut();
		try {
			out.println(sb.toString());
			out.println(chartUtil.printScriptInfo().toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return super.doEndTag();
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public ReportQryStruct getQryStruct() {
		return qryStruct;
	}

	public void setQryStruct(ReportQryStruct qryStruct) {
		this.qryStruct = qryStruct;
	}

}
