package com.ailk.bi.common.app;

import java.util.ArrayList;
import java.lang.StringBuffer;
import java.lang.Integer;
import java.lang.Double;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ArrayUtil {
	/**
	 * 二维对象数组
	 * 
	 * @param src
	 * @return 返回该二维对象数组的列的最短长度的INTEGER对象，如果对象为NULL，则返回NULL
	 */
	public static Integer getMincolLength(Object[][] src) {
		Integer colLen = null;
		if (src == null)
			return null;
		int minCols = Integer.MAX_VALUE;
		for (int i = 0; src != null && i < src.length; i++) {
			try {
				int tmp = src[i].length;
				if (minCols > tmp) {
					minCols = tmp;
				}
			} catch (NullPointerException e) {
				System.out.println("NullPointerException" + e.toString());
				return null;
			}
		}
		colLen = new Integer(minCols);
		return colLen;
	}

	/**
	 * 将指定列转化为数组,用于记录集生成String[] category, String[] series
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static String[] setStringArr(Object[] obj, String fieldName) {
		String[] value = null;
		String str = "";

		if (obj == null || obj.length == 0)
			return null;
		if (fieldName == null || "".equals(fieldName))
			return null;

		if ((obj != null) && (obj.length > 0)) {
			str = ",;," + ReflectUtil.getStringFromObj(obj[0], fieldName)
					+ ",;,";
			for (int i = 1; i < obj.length; i++) {
				String tmpv = ReflectUtil.getStringFromObj(obj[i], fieldName);
				if (str.indexOf(",;," + tmpv + ",;,") < 0)
					str += tmpv + ",;,";
			}
			str = str.substring(3, str.length());
		}
		value = str.split(",;,");
		return value;
	}

	/**
	 * 将指定列转化为数组,用于记录集生成String[] category, String[] series
	 * 
	 * @param result
	 * @param index
	 * @return
	 */
	public static String[] setStringArr(String[][] result, int index) {
		String[] value = null;
		Integer objCols = ArrayUtil.getMincolLength(result);
		int minCols = 0;
		if (objCols != null)
			minCols = objCols.intValue();
		if (index < 0 || index > minCols)
			return null;
		String str = "";
		if (result != null && result.length > 0) {
			str = ",;," + result[0][index] + ",;,";
			for (int i = 1; i < result.length; i++) {
				if (str.indexOf(",;," + result[i][index] + ",;,") < 0)
					str += result[i][index] + ",;,";
			}
			str = str.substring(3, str.length());
		}
		value = str.split(",;,");
		return value;
	}

	/**
	 * 求两个对象元素的并集运算
	 * 
	 * @param allItems
	 *            String[]
	 * @param someItems
	 *            ArrayList
	 * @return ArrayList
	 */
	public static ArrayList getMissingItems(String[] allItems,
			ArrayList someItems) {
		ArrayList missingItems = new ArrayList();
		if (allItems == null)
			return someItems;
		for (int i = 0; allItems != null && i < allItems.length; i++) {
			if (allItems[i].trim().length() > 0) { // filter out empty strings
				if (!someItems.contains(allItems[i].trim())) {
					missingItems.add(allItems[i]);
				}
			}
		}
		return missingItems;
	}

	/**
	 * 对于上面的函数进行重载 求两个对象元素的并集运算
	 * 
	 * @param allItems
	 *            String[]
	 * @param someItems
	 *            String[]
	 * @return String[]
	 */
	public static String[] getMissingItems(String[] allItems, String[] someItems) {
		ArrayList missingItems = new ArrayList();
		if (allItems == null)
			return someItems;
		if (someItems == null)
			return allItems;
		for (int i = 0; i < allItems.length; i++) {
			boolean match = false;
			try {
				if (allItems[i].trim().length() > 0) { // filter out empty
					// strings
					for (int j = 0; j < someItems.length; j++) {
						if (someItems[j].trim().equals(allItems[i].trim())) {
							match = true;
						}
					}
					if (!match) {
						missingItems.add(allItems[i]);
					}
				}
			} catch (NullPointerException e) {
				System.out.println("NullPointerException==>" + e.toString());
				return null;
			}
		}
		String[] aMissingItems = new String[missingItems.size()];
		missingItems.toArray(aMissingItems);

		return aMissingItems;
	}

	/**
	 * 删除数组中的重复项目
	 * 
	 * @param items
	 *            String[]
	 * @return String[]
	 */
	public static String[] removeDups(String[] items) {
		ArrayList noDups = new ArrayList();
		if (items == null)
			return null;
		for (int i = 0; items != null && i < items.length; i++) {
			try {
				if (!noDups.contains(items[i].trim())) {
					noDups.add(items[i].trim());
				}
			} catch (NullPointerException e) {
				System.out.println("NullPointerException" + e.toString());
				return null;
			}
		}
		// if (sort) noDups.; //sorts list alphabetically
		String[] uniqueItems = new String[noDups.size()];
		noDups.toArray(uniqueItems);
		return uniqueItems;
	}

	/**
	 * 将STRING数组转化为STRING,如果BOOLEAN为TURE则加上'引号
	 * 
	 * @param astr
	 *            String[]
	 * @param quotes
	 *            boolean
	 * @return String
	 */
	public static String stringArrayToString(String[] astr, boolean quotes) {
		StringBuffer sb = new StringBuffer();
		if (astr == null)
			return null;
		for (int i = 0; astr != null && i < astr.length; i++) {
			if (astr[i].trim().length() > 0) {
				String delim = i > 0 ? "," : String.valueOf("");
				if (quotes) {
					sb.append(delim + "'" + astr[i].trim() + "'");
				} else {
					sb.append(delim + astr[i].trim());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 取得数组的平均值
	 * 
	 * @param num
	 *            double[]
	 * @return double
	 */
	public static double getAvg(double[] num) {
		double sum = 0.0;
		if (num == null)
			return sum;
		for (int i = 0; num != null && i < num.length; i++) {
			try {
				sum += num[i];
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				System.out.println("input parameter value error");
				return sum;
			}
		}
		double avg = sum / Double.parseDouble(Integer.toString(num.length));

		return avg;
	}

	/**
	 * 求数组的和
	 * 
	 * @param num
	 *            double[]
	 * @return double
	 */
	public static double getSum(double[] num) {
		double sum = 0.0;
		if (num == null)
			return sum;
		for (int i = 0; num != null && i < num.length; i++) {
			try {
				sum += num[i];
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				System.out.println("input parameter value error");
				return sum;
			}
		}
		return sum;
	}

	/**
	 * 求数组的和,进行重载
	 * 
	 * @param num
	 *            double[][]
	 * @param col
	 *            int
	 * @return double
	 */
	public static double getSum(double[][] num, int col) {
		double sum = 0.0;
		if (num == null)
			return sum;
		int len = num.length;
		for (int i = 0; num != null && i < len; i++) {
			try {
				sum += num[i][col];
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				System.out.println("input parameter value error");
				return sum;
			}
		}

		return sum;
	}

	/**
	 * 求数组的和,进行重载
	 * 
	 * @param num
	 *            double[][]
	 * @param col
	 *            int
	 * @return double
	 */
	public static double getSum(String[][] num, int col) {
		double sum = 0.0;
		if (num == null)
			return sum;
		int len = num.length;
		for (int i = 0; num != null && i < len; i++) {
			try {
				String temp = num[i][col].toString();
				if (temp.equals(""))
					temp = "0.0";
				sum += Double.parseDouble(temp);
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				System.out.println("input parameter value error");
				return sum;
			} catch (NumberFormatException ne) {
				System.out.println("" + ne.toString());
				System.out.println("parameter can't numberformat ,error");
				return sum;
			}
		}
		return sum;
	}

	/**
	 * 求标准差
	 * 
	 * @param num
	 *            double[]
	 * @return double
	 */
	public static double getStandardDeviation(double[] num) {
		double Sum = 0.0, SumOfSqrs = 0.0;
		if (num == null)
			return Sum;
		for (int i = 0; num != null && i < num.length; i++) {
			try {
				Sum += num[i];
				SumOfSqrs += Math.pow(num[i], 2);
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				return Sum;
			} catch (NumberFormatException ne) {
				System.out.println("NumberFormatException==>" + ne.toString());
				return Sum;
			}
		}
		double topSum = (num.length * SumOfSqrs) - (Math.pow(Sum, 2));
		double n = (double) num.length;
		return Math.sqrt(topSum / (n * (n - 1)));
	}

	/**
	 * 求标准差,重载函数
	 * 
	 * @param num
	 *            double[][]
	 * @param col
	 *            int
	 * @return double
	 */
	public static double getStandardDeviation(double[][] num, int col) {
		double Sum = 0.0, SumOfSqrs = 0.0;
		if (num == null)
			return Sum;
		int len = num.length;
		for (int i = 0; num != null && i < len; i++) {
			try {
				Sum += num[i][col];
				SumOfSqrs += Math.pow(num[i][col], 2);
			} catch (NullPointerException e) {
				System.out.println("" + e.toString());
				return Sum;
			} catch (NumberFormatException ne) {
				System.out.println("NumberFormatException" + ne.toString());
				return Sum;
			}
		}
		double topSum = (len * SumOfSqrs) - (Math.pow(Sum, 2));
		double n = Double.parseDouble(Integer.toString(len));
		return Math.sqrt(topSum / (n * (n - 1)));
	}

}
