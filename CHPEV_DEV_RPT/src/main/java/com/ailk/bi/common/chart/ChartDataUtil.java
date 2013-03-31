package com.ailk.bi.common.chart;

import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;

/**
 * 图形对象的数据操作
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author renhui
 * @version 1.0
 */
public class ChartDataUtil {

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            分组描述数组
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p1
	 *            分组描述对应名称
	 * @param p2
	 *            X轴描述对应名称
	 * @param p3
	 *            数据数组对应名称
	 * @return
	 */
	public static double[][] setCategoryValue(String[] category,
			String[] series, Object[] value, String p1, String p2, String p3) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		if (category == null || category.length == 0) {
			category = new String[1];
			category[0] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; i < category.length; i++) {
			for (int j = 0; j < series.length; j++) {
				String tmp1 = category[i];
				String tmp2 = series[j];
				for (int m = 0; m < value.length; m++) {
					String res1 = ReflectUtil.getStringFromObj(value[m], p1);
					String res2 = ReflectUtil.getStringFromObj(value[m], p2);
					if (tmp1.equals(res1) && tmp2.equals(res2)) {
						String tmp = ReflectUtil.getStringFromObj(value[m], p3);
						if (tmp == null || "".equals(tmp)) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(tmp, -999999999);
						}
						break;
					} else {
						result[i][j] = -999999999;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            分组描述数组
	 * @param category_code
	 *            分组描述数组对应字段
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p2
	 *            X轴描述对应名称
	 * @return
	 */
	public static double[][] setCategoryValue(String[] category,
			String[] category_code, String[] series, Object[] value, String p2) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		if (category_code == null || category_code.length == 0)
			return null;
		if (category == null || category.length == 0) {
			category = new String[1];
			category[0] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; category != null && i < category.length; i++) {
			String tmpCat = category_code[i].toLowerCase();
			for (int j = 0; series != null && j < series.length; j++) {
				String tmp1 = series[j];
				for (int m = 0; value != null && m < value.length; m++) {
					String res1 = ReflectUtil.getStringFromObj(value[m], p2);
					if (tmp1.equals(res1)) {
						String tmp = ReflectUtil.getStringFromObj(value[m],
								tmpCat);
						if (tmp == null || "".equals(tmp)) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(tmp, -999999999);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据字段名称
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p2
	 *            X轴描述对应名称
	 * @return
	 */
	public static double[][] setCategoryValue(String[][] category,
			String[] series, Object[] value, String p2) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		if (category == null || category.length == 0) {
			category = new String[1][2];
			category[0][0] = "";
			category[0][1] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; category != null && i < category.length; i++) {
			String tmpCat = category[i][1].toLowerCase();
			for (int j = 0; series != null && j < series.length; j++) {
				String tmp1 = series[j];
				for (int m = 0; value != null && m < value.length; m++) {
					String res1 = ReflectUtil.getStringFromObj(value[m], p2);
					if (tmp1.equals(res1)) {
						String tmp = ReflectUtil.getStringFromObj(value[m],
								tmpCat);
						if (tmp == null || "".equals(tmp)) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(tmp, -999999999);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            分组描述数组
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p1
	 *            分组描述对应索引
	 * @param p2
	 *            X轴描述对应索引
	 * @param p3
	 *            数据数组对应索引
	 * @return
	 */
	public static double[][] setCategoryValue(String[] category,
			String[] series, String[][] value, int p1, int p2, int p3) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		if (category == null || category.length == 0) {
			category = new String[1];
			category[0] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; i < category.length; i++) {
			for (int j = 0; j < series.length; j++) {
				String tmp1 = category[i];
				String tmp2 = series[j];
				for (int m = 0; m < value.length; m++) {
					String res1 = "";
					if (p1 >= 0)
						res1 = value[m][p1];
					String res2 = value[m][p2];
					if (tmp1.equals(res1) && tmp2.equals(res2)) {
						if (value[m][p3] == null || "".equals(value[m][p3])) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(value[m][p3],
									-999999999);
						}
						// result[i][j] = StringB.toDouble(value[m][p3], 0);
						break;
					} else {
						result[i][j] = -999999999;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            分组描述数组
	 * @param category_code
	 *            分组描述数组对应索引
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p2
	 *            X轴描述对应索引
	 * @return
	 */
	public static double[][] setCategoryValue(String[] category,
			String[] category_code, String[] series, String[][] value, int p2) {
		if (value == null)
			return null;
		if (category_code == null || category_code.length == 0)
			return null;
		if (series == null || series.length == 0) {
			series = new String[] { "" };
		}
		if (category == null || category.length == 0) {
			category = new String[1];
			category[0] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; category != null && i < category.length; i++) {
			int iCate = StringB.toInt(category_code[i], 0);
			for (int j = 0; series != null && j < series.length; j++) {
				String tmp1 = series[j];
				for (int m = 0; value != null && m < value.length; m++) {
					String res1 = null;
					if (p2 < 0) {
						res1 = tmp1;
					} else {
						res1 = value[m][p2];
					}
					if (tmp1.equals(res1)) {
						if (value[m][iCate] == null
								|| "".equals(value[m][iCate])) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(value[m][iCate],
									-999999999);
						}
						// result[i][j] = StringB.toDouble(value[m][iCate], 0);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Category类型图形生成double[][] value
	 *
	 * @param category
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据字段索引
	 * @param series
	 *            X轴描述数组
	 * @param value
	 *            数据数组对象
	 * @param p2
	 *            X轴描述对应索引
	 * @return
	 */
	public static double[][] setCategoryValue(String[][] category,
			String[] series, String[][] value, int p2) {
		if (value == null)
			return null;
		if (series == null || series.length == 0) {
			series = new String[] { "" };
		}
		if (category == null || category.length == 0) {
			category = new String[1][2];
			category[0][0] = "";
			category[0][1] = "";
		}
		double[][] result = new double[category.length][series.length];

		for (int i = 0; category != null && i < category.length; i++) {
			int iCate = StringB.toInt(category[i][1], 0);
			for (int j = 0; series != null && j < series.length; j++) {
				String tmp1 = series[j];
				for (int m = 0; value != null && m < value.length; m++) {
					String res1 = null;
					if (p2 < 0) {
						res1 = tmp1;
					} else {
						res1 = value[m][p2];
					}
					if (tmp1.equals(res1)) {
						if (value[m][iCate] == null
								|| "".equals(value[m][iCate])) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(value[m][iCate],
									-999999999);
						}
						// result[i][j] = StringB.toDouble(value[m][iCate], 0);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 用于Pie类型图形生成double[][] value
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param p2
	 *            数据描述的字段名称
	 * @param p3
	 *            数据数据的字段名称
	 * @return
	 */
	public static double[][] setPieValue(String[] series, Object[] value,
			String p2, String p3) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		double[][] result = new double[1][series.length];

		for (int i = 0; i < series.length; i++) {
			String tmp1 = series[i];
			for (int j = 0; j < value.length; j++) {
				String res1 = ReflectUtil.getStringFromObj(value[j], p2);
				if (tmp1.equals(res1)) {
					String tmp = ReflectUtil.getStringFromObj(value[j], p3);
					result[0][i] = StringB.toDouble(tmp, 0);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 用于Pie类型图形生成double[][] value
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param seriesname
	 *            描述的列表数组的字段名称
	 * @return
	 */
	public static double[][] setPieValue(String[] series, Object[] value,
			String[] seriesname) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		double[][] result = new double[value.length][series.length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < series.length && j < seriesname.length; j++) {
				String tmp1 = seriesname[j];
				String tmp = ReflectUtil.getStringFromObj(value[i], tmp1);
				result[i][j] = StringB.toDouble(tmp, 0);
			}
		}
		return result;
	}

	/**
	 * 用于Pie类型图形生成double[][] value
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param p2
	 *            数据描述的字段索引
	 * @param p3
	 *            数据数据的字段索引
	 * @return
	 */
	public static double[][] setPieValue(String[] series, String[][] value,
			int p2, int p3) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		double[][] result = new double[1][series.length];

		for (int i = 0; i < series.length; i++) {
			String tmp1 = series[i];
			for (int j = 0; j < value.length; j++) {
				String res1 = value[j][p2];
				if (tmp1.equals(res1)) {
					String tmp = value[j][p3];
					result[0][i] = StringB.toDouble(tmp, 0);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 用于Pie类型图形生成double[][] value
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param seriesindex
	 *            描述的列表数组的字段名称
	 * @return
	 */
	public static double[][] setPieValue(String[] series, String[][] value,
			String[] seriesindex) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		double[][] result = new double[value.length][series.length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < series.length && j < seriesindex.length; j++) {
				int vIndex = StringB.toInt(seriesindex[j], 0);
				String tmp = value[i][vIndex];
				result[i][j] = StringB.toDouble(tmp, 0);
			}
		}
		return result;
	}

	/**
	 * 用于记录集生成气泡图数据
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param p1
	 *            数据分组的字段所在索引
	 * @param p3
	 *            数据数据的字段所在索引
	 * @return
	 */
	public static double[][] setBubbleValue(String[] series, Object[] value,
			String p1, String p3) {
		if (value == null)
			return null;

		double[][] result = null;
		if (series == null || series.length == 0 || value == null
				|| value.length == 0)
			return null;

		result = new double[series.length][];
		String tmpSerie = "";
		String tmpValue = "";
		for (int i = 0; i < series.length; i++) {
			tmpValue = "";
			for (int j = 0; j < value.length; j++) {
				tmpSerie = ReflectUtil.getStringFromObj(value[j], p1);
				if (series[i].equals(tmpSerie)) {
					tmpValue += ReflectUtil.getStringFromObj(value[j], p3)
							+ ",";
				}
			}
			String[] tmpStringArr = tmpValue.split(",");
			result[i] = FormatUtil.fmtStrToDouble(tmpStringArr);
		}
		return result;
	}

	/**
	 * 用于记录集生成气泡图数据
	 *
	 * @param series
	 *            描述的列表数组
	 * @param value
	 *            数据值数组
	 * @param p1
	 *            数据分组的字段所在索引
	 * @param p3
	 *            数据数据的字段所在索引
	 * @return
	 */
	public static double[][] setBubbleValue(String[] series, String[][] value,
			int indexSeries, String[] indexValue) {
		if (value == null)
			return null;

		double[][] result = null;
		if (series == null || series.length == 0 || value == null)
			return null;

		result = new double[indexValue.length][];
		String tmpSerie = "";
		String tmpValue = "";
		for (int i = 0; i < indexValue.length; i++) {
			tmpValue = "";
			for (int k = 0; k < series.length; k++) {
				for (int j = 0; j < value.length; j++) {
					tmpSerie = value[j][indexSeries];
					if (series[k].equals(tmpSerie)) {
						int itmp = Integer.parseInt(indexValue[i]);
						tmpValue += value[j][itmp] + ",";
					}
				}
			}
			String[] tmpStringArr = tmpValue.split(",");
			result[i] = FormatUtil.fmtStrToDouble(tmpStringArr);
		}
		return result;
	}

	/**
	 * 将double数组改变为string数组
	 *
	 * @param arrValue
	 * @return
	 */
	public static String[][] changeArrDoubleToString(double[][] arrValue) {
		String[][] result = null;// 格式化后的数值
		if (arrValue != null && arrValue.length > 0) {
			result = new String[arrValue.length][arrValue[0].length];
		}

		for (int i = 0; arrValue != null && i < arrValue.length; i++) {
			for (int j = 0; j < arrValue[i].length; j++) {
				if (arrValue[i][j] == -999999999) {
					//arrValue[i][j] = 0;
				}
				result[i][j] = Double.toString(arrValue[i][j]);
			}
		}
		return result;
	}
}
