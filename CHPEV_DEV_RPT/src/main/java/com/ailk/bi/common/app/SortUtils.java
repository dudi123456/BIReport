package com.ailk.bi.common.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.lang.reflect.Field;

import com.ailk.bi.report.util.ReportConsts;

/**
 * @author hailong.wang 本函数用于对于二维数组进行排序、一维对象数组进行排序
 */
/**
 * @author hailong.wang
 *
 */
public class SortUtils {
	private static String orderType = "desc";

	/**
	 * 将二维字符串数组转化为TOP前N加1的形式
	 *
	 * @param str
	 *            需要进行转化的二维字符串数组
	 * @param n
	 *            需要进行转化的长度定义
	 * @return 返回转化后的TOP前N加1的二维字符串数组,如果N为负数，则返回原数组
	 */
	public static String[][] getTopNplusOne(String[][] str, int n) {
		String[][] topPlusList = null;
		if (str == null)
			return null;
		int row = str.length;
		if (row <= n + 1 || n <= 0)
			return str;
		int col = str[0].length;
		topPlusList = new String[n + 1][col];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < col; j++) {
				topPlusList[i][j] = str[i][j];
			}
		}
		double[] tmp = new double[col];
		for (int r = n; r < row; r++) {
			for (int c = 0; c < col; c++) {
				String tmpCol = str[r][c];
				if (tmpCol.equals(""))
					tmpCol = "0.0";
				try {
					tmp[c] += new Double(tmpCol.trim()).doubleValue();
					topPlusList[n][c] = new Double(tmp[c]).toString();
				} catch (NumberFormatException e) {
					System.out.println("NumberFormatException==>" + e.toString());
					return null;
				} catch (NullPointerException ie) {
					System.out.println("NullPointerException" + ie.toString());
					return null;
				}
			}
		}

		return topPlusList;
	}

	/**
	 * 将字符串数组进行转化为TOP前N加1的数组输出
	 *
	 * @param str
	 *            需要进行转化的字符串数组
	 * @param n
	 *            需要进行转化的长度定义
	 * @return 返回转化后的TOP前N加1的字符串类型一维数组,如果N为负数，则返回原数组
	 */
	public static String[] getTopNplusOne(String[] str, int n) {
		String[] topPlus = null;
		if (str == null)
			return null;
		int row = str.length;
		if (row <= n + 1 || n < 0)
			return str;
		topPlus = new String[n + 1];
		for (int i = 0; i < n; i++) {
			topPlus[i] = str[i];
		}
		double tmp = new Double("0.0").doubleValue();

		for (int j = n; j < row; j++) {
			String strTmp = str[j];
			if ("".equals(strTmp))
				strTmp = "0.0";
			try {
				double strDbl = new Double(strTmp).doubleValue();
				tmp += strDbl;
			} catch (NumberFormatException e) {
				System.out.println("NumberFormatException==>" + e.toString());
				return null;
			} catch (NullPointerException ie) {
				System.out.println("NullPointerException==>" + ie.toString());
				return null;
			}
		}
		topPlus[n] = new Double(tmp).toString();
		return topPlus;
	}

	/**
	 * 按照数组的第N列进行数值排序，其结果返回排序后的二维数值数组
	 *
	 * @param str
	 *            需要进行按某列排序的数值数组
	 * @param column
	 *            以第N列为排序列
	 * @param isASC
	 *            升序(true)、降序(false)
	 * @return 返回按照指定N列排序后的二维数组,如果输入的数组为NULL，则输出NULL
	 */
	public static Double[][] sortValues(Double[][] str, int column, boolean isASC) {
		if (str == null)
			return null;
		Integer IntCols = ArrayUtil.getMincolLength(str);
		int minCols = IntCols.intValue();
		if (column < 0 || column >= minCols)
			return str;
		Double[][] result = new Double[str.length][minCols];
		String[] strClumn = new String[str.length];
		for (int i = 0; i < str.length; i++) {
			strClumn[i] = str[i][column].toString();
		}
		String[] strQuence = ShellSort.sortDouble((Object[]) strClumn);
		if (!isASC) {
			for (int i = 0; i < str.length; i++) {
				for (int j = 0; j < str[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[str.length - 1 - i]);
					result[i][j] = str[tmp][j];
				}
			}
		} else {
			for (int i = 0; i < strQuence.length; i++) {
				for (int j = 0; j < str[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[i]);
					result[i][j] = str[tmp][j];
				}
			}
		}
		return result;
	}

	/**
	 * 按照数组的第N列进行字符串排序，输出结果为排序后的字符串数组
	 *
	 * @param res
	 *            需要输入的二维字符串数组
	 * @param column
	 *            需要进行排序的列
	 * @param flag
	 *            升序(true)、降序(false)
	 * @return 返回按照指定N列排序后的二维数组,如果输入的数组为NULL，则输出NULL
	 */
	public static String[][] sortValues(String[][] res, int column, boolean flag) {

		if (res == null)
			return null;
		Integer IntCols = ArrayUtil.getMincolLength(res);
		int minCols = IntCols.intValue();
		if (column < 0 || column >= minCols)
			return res;
		String[][] result = new String[res.length][minCols]; // define
		// result
		// vector;
		String[] strClumn = new String[res.length];
		for (int i = 0; i < res.length; i++) {
			strClumn[i] = res[i][column];
		}
		String[] strQuence = ShellSort.sortStr((Object[]) strClumn, new StringComparator());
		if (!flag) {
			for (int i = 0; i < strQuence.length; i++) {
				for (int j = 0; j < res[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[strQuence.length - 1 - i]);
					result[i][j] = res[tmp][j];
				}
			}
		} else {
			for (int i = 0; i < strQuence.length; i++) {
				for (int j = 0; j < res[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[i]);
					result[i][j] = res[tmp][j];
				}
			}
		}
		return result;

	}

	/**
	 * 按照数组的第N列进行字符串排序，输出结果为排序后的字符串数组
	 *
	 * @param res
	 *            需要输入的二维字符串数组
	 * @param column
	 *            需要进行排序的列
	 * @param flag
	 *            升序(true)、降序(false)
	 * @param iLen
	 *            长度
	 * @return 返回按照指定N列排序后的二维数组,如果输入的数组为NULL，则输出NULL
	 */
	public static String[][] sortValuesStrNum(String[][] res, int column, boolean flag, int iLen) {
		if (res == null)
			return null;

		String[][] result = sortValuesStrNum(res, column, flag);
		int iRow = result.length;
		if (iRow < iLen) {
			iLen = iRow;
		}
		List<String[]> newList = new ArrayList<String[]>();
		for (int i = 0; i < iLen; i++) {
			newList.add(result[i]);
		}
		result = (String[][]) newList.toArray(new String[newList.size()][]);
		return result;
	}

	/**
	 * 按照数组的第N列进行字符串排序，输出结果为排序后的字符串数组
	 *
	 * @param res
	 *            需要输入的二维字符串数组
	 * @param column
	 *            需要进行排序的列
	 * @param flag
	 *            升序(true)、降序(false)
	 * @return 返回按照指定N列排序后的二维数组,如果输入的数组为NULL，则输出NULL
	 */
	public static String[][] sortValuesStrNum(String[][] res, int column, boolean flag) {

		if (res == null)
			return null;
		if (res.length == 1) {
			return res;
		}
		Integer IntCols = ArrayUtil.getMincolLength(res);
		int minCols = IntCols.intValue();
		if (column < 0 || column >= minCols)
			return res;
		String[][] result = new String[res.length][minCols]; // define
		// result
		// vector;
		String[] strClumn = new String[res.length];
		for (int i = 0; i < res.length; i++) {
			strClumn[i] = res[i][column];
		}
		// String[] strQuence = ShellSort.sortStr((Object[]) strClumn,
		// new StringComparator());
		String[] strQuence = ShellSort.sortDouble((Object[]) strClumn);
		if (!flag) {
			for (int i = 0; i < strQuence.length; i++) {
				for (int j = 0; j < res[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[strQuence.length - 1 - i]);
					result[i][j] = res[tmp][j];
				}
			}
		} else {
			for (int i = 0; i < strQuence.length; i++) {
				for (int j = 0; j < res[0].length; j++) {
					int tmp = Integer.parseInt(strQuence[i]);
					result[i][j] = res[tmp][j];
				}
			}
		}
		return result;

	}

	/**
	 * 带合计行的排序
	 *
	 * @param res
	 * @param column
	 * @param flag
	 * @return
	 */
	public static String[][] sortValuesStrNumHasSum(String[][] res, int column, boolean flag) {
		if (res == null)
			return null;
		if (res.length == 1) {
			return res;
		}
		Integer IntCols = ArrayUtil.getMincolLength(res);
		int minCols = IntCols.intValue();
		if (column < 0 || column >= minCols)
			return res;

		// 定义返回结果
		String[][] result = new String[res.length][minCols];
		// 结果集是否包含合计行
		boolean hasSum = false;

		List<String[]> sorList = new ArrayList<String[]>();
		String[] sumArr = new String[1];
		for (int i = 0; i < res.length; i++) {
			if (!ReportConsts.RPT_SUMROW_ID.equals(res[i][0])) {
				sorList.add(res[i]);
			} else {
				sumArr = res[i];
				hasSum = true;
			}
		}
		String[][] tmpArr = (String[][]) sorList.toArray(new String[sorList.size()][]);
		String[][] sorArr = sortValuesStrNum(tmpArr, column, flag);

		// 还原排序结果集
		for (int i = 0; i < sorArr.length; i++) {
			result[i] = sorArr[i];
		}
		if (hasSum) {
			result[res.length - 1] = sumArr;
		}

		return result;
	}

	/**
	 * 将对象数组进行数值排序
	 *
	 * @param userArray
	 *            需要进行排序的对象数组
	 * @param compareCol
	 *            需要按照某列的列名进行排序
	 * @param userOrderType
	 *            排序类型：desc为降序，asc为升序
	 */
	public static void sortByNum(Object[] userArray, String compareCol, String userOrderType) {
		orderType = userOrderType.toLowerCase();
		ShellSort.sort(userArray, new ComparatorByNum(compareCol, orderType));
	}

	/**
	 * 对于对象数组进行数值排序，默认为降序排序
	 *
	 * @param userArray
	 *            需要进行排序的对象数组
	 * @param compareCol
	 *            需要按照某列的列名进行排序
	 */
	public static void sortByNum(Object[] userArray, String compareCol) {
		ShellSort.sort(userArray, new ComparatorByNum(compareCol));
	}

	/**
	 * 对于对象数组进行字符串排序，默认为降序排序
	 *
	 * @param userArray
	 *            需要进行排序的对象数组
	 * @param compareCol
	 *            需要按照某列的列名进行排序的列名称
	 */
	public static void sortByStr(Object[] userArray, String compareCol) {
		ShellSort.sort(userArray, new ComparatorByStr(compareCol));
	}
}

/**
 * @author hailong.wang 按照字符对象进行比较
 */
@SuppressWarnings({ "rawtypes" })
class StringComparator implements Comparator {

	// Compare two Strings. Callback for sort.
	// effectively returns a-b;
	// e.g. +1 (or any +ve number) if a > b
	// 0 if a == b
	// -1 (or any -ve number) if a < b
	public final int compare(Object a, Object b) {
		try {
			return ((String) a).compareTo((String) b);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException==>" + e.toString());
			return 0;
		}
	} // end compare

	/* compare comparators */
	public final boolean equals(Object a, Object b) {
		return (a == b);
	} // end equals

} // end class StringComparator

/**
 * @author hailong.wang 按照数字进行排序
 */
@SuppressWarnings({ "rawtypes" })
class ComparatorByNum implements Comparator {
	public String compareCol = "";

	private String orderType = "desc";

	public ComparatorByNum(String compareCol) {
		this.compareCol = compareCol;
	}

	public ComparatorByNum(String compareCol, String orderType) {
		this.compareCol = compareCol;
		this.orderType = orderType;
	}

	public int compare(Object o1, Object o2) {
		Object op1 = o1;
		Object op2 = o2;

		Class opClass1 = op1.getClass();
		Field opField1 = null;
		double currentValue1 = 0;
		try {
			opField1 = opClass1.getDeclaredField(compareCol);
			Object temp = opField1.get(op1);
			if (temp != null) {
				try {
					currentValue1 = Double.parseDouble((String) temp);
				} catch (NumberFormatException ne) {
					System.out.println("NumberFormatException==>" + ne.toString());
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Class opClass2 = op2.getClass();
		Field opField2 = null;
		double currentValue2 = 0;
		try {
			opField2 = opClass2.getDeclaredField(compareCol);
			Object temp = opField2.get(op2);
			if (temp != null) {
				try {
					currentValue2 = Double.parseDouble((String) temp);
				} catch (NumberFormatException ae) {
					System.out.println("NumberFormatException==>" + ae.toString());
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("desc".equals(orderType)) {
			if (currentValue1 > currentValue2) {
				return -1;
			} else if (currentValue1 < currentValue2) {
				return 1;
			}
		}

		if ("asc".equals(orderType)) {
			if (currentValue1 > currentValue2) {
				return 1;
			} else if (currentValue1 < currentValue2) {
				return -1;
			}
		}

		return 0;
	}
}

/**
 * @author hailong.wang 按照字符串进行排序
 */
@SuppressWarnings({ "rawtypes" })
class ComparatorByStr implements Comparator {
	public String compareCol = "";

	public ComparatorByStr(String compareCol) {
		this.compareCol = compareCol;
	}

	public int compare(Object o1, Object o2) {
		Object op1 = o1;
		Object op2 = o2;

		Class opClass1 = op1.getClass();
		Field opField1 = null;
		String currentValue1 = "";
		try {
			opField1 = opClass1.getDeclaredField(compareCol);
			Object temp = opField1.get(op1);
			if (temp != null)
				currentValue1 = (String) temp;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Class opClass2 = op2.getClass();
		Field opField2 = null;
		String currentValue2 = "";
		try {
			opField2 = opClass2.getDeclaredField(compareCol);
			Object temp = opField2.get(op2);
			if (temp != null)
				currentValue2 = (String) temp;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return currentValue1.compareTo(currentValue2);
	}
}
