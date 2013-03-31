package com.ailk.bi.subject.util;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings({ "unused" })
public class SplitChartOpt {
	private static Log logger = LogFactory.getLog(SplitChartOpt.class);

	private String dbtable = "FUI_MONITOR_PRODUCT_D";

	private String countMsu = "";

	private String msuX = "";

	private String msuY = "";

	private String arith_msuX = "";

	private String arith_msuY = "";

	private String whereSql = "";

	private String tagBig = ">=";

	private String tagSmall = "<";

	private boolean calX = false;

	private boolean calY = false;

	private int digit_xlength = 0;

	private int digit_ylength = 0;

	public SplitChartOpt(String table) {
		this.dbtable = table;
	}

	/**
	 * 获取刻度分界值
	 * 
	 * @param arrSplitX
	 * @param arrSplitY
	 * @param wheresql
	 * @return
	 */
	public String[] getRate(String[] arrSplit, String msu) {
		String[] result = new String[arrSplit.length + 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = "0";
		}

		String[][] tmp = null;
		String strSql = "";

		strSql = getRateSqlNoCal(arrSplit, msu);
		try {
			tmp = WebDBUtil.execQryArray(strSql, "");
		} catch (AppException e) {
			tmp = null;
		}
		if (tmp == null || tmp.length == 0)
			return result;

		for (int i = 0; i < tmp[0].length; i++) {
			if ("per1".equals(msu))
				result[i] = FormatUtil.formatStr(tmp[0][i], digit_xlength,
						false);
			else if ("per2".equals(msu))
				result[i] = FormatUtil.formatStr(tmp[0][i], digit_ylength,
						false);
			;
		}
		return result;
	}

	/**
	 * 获取分位块数值
	 * 
	 * @param Rates
	 * @return
	 */
	public String[][] getRateData(String[] RatesX, String[] RatesY) {
		String[][] result = null;
		String strsql = getRateDataSql(RatesX, RatesY);
		try {
			result = WebDBUtil.execQryArray(strsql, "");
		} catch (AppException e) {
			result = null;
		}
		return result;
	}

	/**
	 * 组装分位点的边界值
	 * 
	 * @param RatesX
	 * @param RatesY
	 * @return
	 */
	public String[][] getRateValueArr(String[] RatesX, String[] RatesY) {
		String[] rx = new String[RatesX.length];
		for (int i = 0; i < rx.length; i++) {
			rx[i] = RatesX[i];
		}
		String[] ry = new String[RatesY.length];
		for (int i = 0; i < ry.length; i++) {
			ry[i] = RatesY[i];
		}

		String[] xc = new String[RatesX.length];
		for (int i = 0; i < xc.length; i++) {
			if (i == 0)
				xc[i] = ">=" + rx[0] + "," + this.tagSmall + rx[1];
			else if (i == xc.length - 1)
				xc[i] = ">=" + rx[0] + ",=<=" + rx[i];
			else if (i == xc.length - 2)
				xc[i] = "" + this.tagBig + rx[i] + ",=<=" + rx[i + 1];
			else
				xc[i] = "" + this.tagBig + rx[i] + ",=" + this.tagSmall
						+ rx[i + 1];
		}
		String[] yc = new String[RatesY.length];
		for (int i = 0; i < yc.length; i++) {
			if (i == yc.length - 1)
				yc[i] = ">=" + ry[0] + ",<=" + ry[yc.length - 1];
			else if (i == yc.length - 2)
				yc[i] = "" + this.tagBig + ry[i] + ",<=" + ry[i + 1];
			else if (i == 0)
				yc[i] = ">=" + ry[0] + "," + this.tagSmall + ry[i + 1];
			else
				yc[i] = "" + this.tagBig + ry[i] + "," + this.tagSmall
						+ ry[i + 1];
		}

		int m = 0;
		String[][] result = new String[xc.length][yc.length];
		for (int j = yc.length - 1; j >= 0; j--) {
			for (int i = 0; i < xc.length; i++) {
				result[m][i] = xc[i] + "," + yc[j];
			}
			m++;
		}
		return result;
	}

	/**
	 * 组装分位点的边界值
	 * 
	 * @param RatesX
	 * @param RatesY
	 * @return
	 */
	public String[][] getRateValueArr(String[] RatesX, String[] RatesY,
			String xDesc, String yDesc) {
		String[] rx = new String[RatesX.length];
		for (int i = 0; i < rx.length; i++) {
			rx[i] = RatesX[i];
		}
		String[] ry = new String[RatesY.length];
		for (int i = 0; i < ry.length; i++) {
			ry[i] = RatesY[i];
		}

		String[] xc = new String[RatesX.length];
		for (int i = 0; i < xc.length; i++) {
			if (i == 0)
				xc[i] = ">=" + rx[0] + "," + this.tagSmall + rx[1];
			else if (i == xc.length - 1)
				xc[i] = ">=" + rx[0] + ",<=" + rx[i];
			else if (i == xc.length - 2)
				xc[i] = this.tagBig + rx[i] + ",<=" + rx[i + 1];
			else
				xc[i] = this.tagBig + rx[i] + "," + this.tagSmall + rx[i + 1];
		}
		String[] yc = new String[RatesY.length];
		for (int i = 0; i < yc.length; i++) {
			if (i == yc.length - 1)
				yc[i] = ">=" + ry[0] + ",<=" + ry[yc.length - 1];
			else if (i == yc.length - 2)
				yc[i] = this.tagBig + ry[i] + ",<=" + ry[i + 1];
			else if (i == 0)
				yc[i] = ">=" + ry[0] + "," + this.tagSmall + ry[i + 1];
			else
				yc[i] = this.tagBig + ry[i] + "," + this.tagSmall + ry[i + 1];
		}

		int m = 0;
		String[][] result = new String[xc.length][yc.length];
		for (int j = yc.length - 1; j >= 0; j--) {
			for (int i = 0; i < xc.length; i++) {
				result[m][i] = xDesc + xc[i] + "," + yDesc + yc[j];
			}
			m++;
		}
		return result;
	}

	/**
	 * 没有计算的刻度计算
	 * 
	 * @param arrSplit
	 * @param msu
	 * @return
	 */
	private String getRateSqlNoCal(String[] arrSplit, String msu) {
		String min_rate = "COALESCE(min(" + msu + "),0)";
		String max_rate = "COALESCE(max(" + msu + "),0)";

		String strsql = "select " + min_rate;
		for (int i = 0; arrSplit != null && i < arrSplit.length; i++) {
			strsql += "," + min_rate + "+" + arrSplit[i] + "*(" + max_rate
					+ "-" + min_rate + ")";
		}
		strsql += "," + max_rate;
		strsql += " from(";
		strsql += getSumDataSql() + ") rate1";
		logger.debug("getRateSqlNoCal=" + strsql);
		return strsql;
	}

	/**
	 * 带计算的刻度计算
	 * 
	 * @param v_x0
	 * @param v_x1
	 * @param v_x2
	 * @param v_x3
	 * @param v_x4
	 * @param v_x5
	 * @param y4
	 * @param wheresql
	 * @return
	 */
	private String getRateSqlCal(String v_x0, String v_x1, String v_x2,
			String v_x3, String v_x4, String v_x5, String y4, String wheresql) {
		String x0 = "NEW_UNUM>=" + v_x0;
		String x1 = "NEW_UNUM>=" + v_x1;
		String x2 = "NEW_UNUM>=" + v_x2;
		String x3 = "NEW_UNUM>=" + v_x3;
		String x4 = "NEW_UNUM>=" + v_x4;
		String x5 = "NEW_UNUM>" + v_x5;
		String conx = " THEN NEW_UNUM END)/decode(sum(NEW_UNUM),0,NULL,sum(NEW_UNUM))";

		String strsql = "select ";
		strsql += " COALESCE(round(sum(CASE WHEN " + x0 + conx + ",4),0)";
		strsql += ",COALESCE(round(sum(CASE WHEN " + x1 + conx + ",4),0)";
		strsql += ",COALESCE(round(sum(CASE WHEN " + x2 + conx + ",4),0)";
		strsql += ",COALESCE(round(sum(CASE WHEN " + x3 + conx + ",4),0)";
		strsql += ",COALESCE(round(sum(CASE WHEN " + x4 + conx + ",4),0)";
		strsql += ",COALESCE(round(sum(CASE WHEN " + x5 + conx + ",4),0)";
		strsql += " from(";
		strsql += getSumDataSql() + ")";
		logger.debug("getRateSqlCal=" + strsql);
		return strsql;
	}

	/**
	 * 汇总数据
	 * 
	 * @return
	 */
	private String getSumDataSql() {
		String strsql = "";
		strsql += " select " + this.countMsu;
		strsql += "," + this.arith_msuX + " as per1";
		strsql += "," + this.arith_msuY + " as per2";
		strsql += " FROM " + this.dbtable;
		strsql += " " + this.whereSql;
		strsql += " group by " + this.countMsu + "";
		return strsql;
	}

	/**
	 * 最终结果
	 * 
	 * @param Rates
	 * @return
	 */
	private String getRateDataSql(String[] RatesX, String[] RatesY) {
		String[] rx = new String[RatesX.length];
		for (int i = 0; i < rx.length; i++) {
			rx[i] = RatesX[i];
		}
		String[] ry = new String[RatesY.length];
		for (int i = 0; i < ry.length; i++) {
			ry[i] = RatesY[i];
		}

		String[] xc = new String[RatesX.length];
		for (int i = 0; i < xc.length; i++) {
			if (i == 0)
				xc[i] = "per1>=" + rx[0] + " AND per1" + this.tagSmall + rx[1];
			else if (i == xc.length - 1)
				xc[i] = "per1>=" + rx[0] + " AND per1<=" + rx[i];
			else if (i == xc.length - 2)
				xc[i] = "per1" + this.tagBig + rx[i] + " AND per1<="
						+ rx[i + 1];
			else
				xc[i] = "per1" + this.tagBig + rx[i] + " AND per1"
						+ this.tagSmall + rx[i + 1];
		}
		String[] yc = new String[RatesY.length];
		for (int i = 0; i < yc.length; i++) {
			if (i == yc.length - 1)
				yc[i] = "per2>=" + ry[0] + " AND per2<=" + ry[yc.length - 1];
			else if (i == yc.length - 2)
				yc[i] = "per2" + this.tagBig + ry[i] + " AND per2<="
						+ ry[i + 1];
			else if (i == 0)
				yc[i] = "per2>=" + ry[0] + " AND per2" + this.tagSmall
						+ ry[i + 1];
			else
				yc[i] = "per2" + this.tagBig + ry[i] + " AND per2"
						+ this.tagSmall + ry[i + 1];
		}

		String strsql = "select ";
		int m = 1;
		for (int j = yc.length - 1; j >= 0; j--) {
			for (int i = 0; i < xc.length; i++) {
				if (!"select ".equals(strsql)) {
					strsql += ",";
				}
				strsql += "NUM"
						+ (m)
						+ (i + 1)
						+ ",round(1.0000*NUM"
						+ (m)
						+ (i + 1)
						+ "/case when total_num=0 then null else total_num end,4)";
			}
			m++;
		}
		strsql += " FROM(select ";
		strsql += "count(" + this.countMsu + ") total_num";
		m = 1;
		for (int j = yc.length - 1; j >= 0; j--) {
			for (int i = 0; i < xc.length; i++) {
				if (i == xc.length - 1) {
					strsql += ",count(CASE WHEN " + yc[j] + " THEN "
							+ this.countMsu + " END) NUM" + (m) + (i + 1);
				} else {
					strsql += ",count(CASE WHEN (" + xc[i] + ") AND (" + yc[j]
							+ ") THEN " + this.countMsu + " END) NUM" + (m)
							+ (i + 1);
				}
			}
			m++;
		}
		strsql += " FROM (";
		strsql += getSumDataSql();
		strsql += ") data1 where 1=1 ) data2";
		logger.debug("getRateDataSql=" + strsql);
		return strsql;
	}

	public String getCountMsu() {
		return countMsu;
	}

	public void setCountMsu(String countMsu) {
		this.countMsu = countMsu;
	}

	public String getMsuX() {
		return msuX;
	}

	public void setMsuX(String msuX) {
		this.msuX = msuX;
	}

	public String getMsuY() {
		return msuY;
	}

	public void setMsuY(String msuY) {
		this.msuY = msuY;
	}

	public String getArith_msuX() {
		return arith_msuX;
	}

	public void setArith_msuX(String arith_msuX) {
		this.arith_msuX = arith_msuX;
	}

	public String getArith_msuY() {
		return arith_msuY;
	}

	public void setArith_msuY(String arith_msuY) {
		this.arith_msuY = arith_msuY;
	}

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public String getTagBig() {
		return tagBig;
	}

	public void setTagBig(String tagBig) {
		this.tagBig = tagBig;
	}

	public String getTagSmall() {
		return tagSmall;
	}

	public void setTagSmall(String tagSmall) {
		this.tagSmall = tagSmall;
	}

	public boolean isCalX() {
		return calX;
	}

	public void setCalX(boolean calX) {
		this.calX = calX;
	}

	public boolean isCalY() {
		return calY;
	}

	public void setCalY(boolean calY) {
		this.calY = calY;
	}

	public int getDigit_xlength() {
		return digit_xlength;
	}

	public void setDigit_xlength(int digit_xlength) {
		this.digit_xlength = digit_xlength;
	}

	public int getDigit_ylength() {
		return digit_ylength;
	}

	public void setDigit_ylength(int digit_ylength) {
		this.digit_ylength = digit_ylength;
	}
}
