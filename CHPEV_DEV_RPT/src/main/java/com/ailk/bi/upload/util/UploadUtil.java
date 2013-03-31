package com.ailk.bi.upload.util;

import java.util.ArrayList;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UploadUtil {

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String whereStr) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, whereStr);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String paramA,
			String paramB) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, paramA, paramB);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String paramA,
			String paramB, String paramC) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, paramA, paramB, paramC);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sql
	 * @return
	 */
	public static String[][] queryArrayFacade(String sql) {
		String arr[][] = null;
		try {
			System.out.println(sql);
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sql
	 * @return
	 */
	public static int queryArrayFacade(String[] sql) {
		int i = -1;
		try {
			i = 1;
			WebDBUtil.execTransUpdate(sql);
		} catch (AppException ex1) {
			//
			i = -1;
			ex1.printStackTrace();
		}
		return i;
	}

	/**
	 * 将二纬字符串数组转成下拉列表字符串
	 * 
	 * @param arr
	 * @param iLen
	 * @return
	 */
	public static String changArrsToStr(String[][] arr, int iLen) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			for (int j = 0; j < iLen; j++) {
				ret += arr[i][j] + ",";
			}
			if (ret.length() > 1)
				ret = ret.substring(0, ret.length() - 1);
			ret += ";";
		}
		if (ret.length() > 1)
			ret = ret.substring(0, ret.length() - 1);
		return ret;
	}

	/**
	 * 
	 * @param list
	 * @param Index
	 * @param key
	 * @return
	 */
	public static String[][] arrayCutCopy(String[][] list, int Index, String key) {

		String[][] tmpArr = null;
		int row = 0;
		int col = 0;
		for (int i = 0; list != null && i < list.length; i++) {
			col = list[i].length;
			if (list[i][Index].equals(key)) {
				row++;
			}
		}

		tmpArr = new String[row][col];

		int a = 0;
		for (int i = 0; list != null && i < list.length; i++) {
			if (list[i][Index].equals(key)) {
				int b = 0;
				for (int j = 0; j < list[i].length; j++) {
					tmpArr[a][b++] = list[i][j];
				}
				a++;
			}
		}

		return tmpArr;
	}

	/**
	 * 
	 * @param list
	 *            数据值
	 * @param index
	 *            合并索引
	 * @param startIndex
	 *            开始索引
	 * @param colrap
	 *            列间隔
	 * @return
	 */
	public static ArrayList getCombinRowArr(String[][] list, int[] index,
			int startIndex, int colrap) {
		// 初始化数组
		ArrayList arrList = new ArrayList();

		for (int i = 0; index != null && i < index.length; i++) {
			ArrayList tmpList = new ArrayList();
			arrList.add(i, tmpList);
		}
		// 递归统计列合并
		compRowArr(list, index, arrList, startIndex, colrap);
		return arrList;

	}

	/**
	 * 
	 * @param list
	 *            二维数组
	 * @param index
	 *            索引数组
	 * @param arrList
	 *            表格列列表
	 * @param startIndex
	 *            开始索引
	 * @param rap
	 *            索引间隔
	 */
	public static void compRowArr(String[][] list, int[] index,
			ArrayList arrList, int startIndex, int rap) {
		ArrayList tmpList = new ArrayList();

		if (startIndex > index.length - 1) {
			return;
		}

		int t_index = index[startIndex];
		// 找出a列的关键字
		for (int i = 0; list != null && i < list.length; i++) {
			// 不存在就存入
			if (!tmpList.contains(list[i][t_index])) {
				tmpList.add(list[i][t_index]);
			}
		}
		//
		for (int i = 0; i < tmpList.size(); i++) {
			String key = (String) tmpList.get(i);
			String[][] t1 = arrayCutCopy(list, t_index, key);
			ArrayList rowLen = (ArrayList) arrList.get(startIndex);
			rowLen.add(String.valueOf(t1.length));
			//

			compRowArr(t1, index, arrList, startIndex + rap, rap);
		}

	}

	/**
	 * 合并数组索引数组
	 * 
	 * @param length
	 * @param flag
	 * @return
	 */
	public static int[] getCombineArrIndex(int length, String flag) {
		int index[] = new int[length];
		if ("0".equals(flag)) {
			for (int i = 0; i < length; i++) {
				index[i] = 2 * i;
			}
		} else {
			for (int i = 0; i < length; i++) {
				index[i] = 2 * i + 1;
			}
		}
		return index;
	}

	/**
	 * 过滤合并
	 * 
	 * @param list
	 * @param arrIndex
	 * @return
	 */
	public static String[][] getCombineHelperList(String[][] list,
			int[] arrIndex, ArrayList arrList) {

		if (list == null) {
			return null;
		}
		// copy a array
		int row = list.length;
		int col = list[0].length;

		String[][] reList = new String[row][col];
		for (int a = 0; a < row; a++) {
			for (int b = 0; b < col; b++) {
				reList[a][b] = list[a][b];
			}
		}
		// process
		for (int i = 0; i < arrIndex.length; i++) {
			// 索引
			int t_index = arrIndex[i];
			// 索引列表
			ArrayList tmpList = (ArrayList) arrList.get(i);
			// 索引统计字段
			int rowIndex = 0;
			// 如果不为空，则遍历索引列表
			for (int a_index = 0; tmpList != null && !tmpList.isEmpty()
					&& a_index < tmpList.size(); a_index++) {
				// 取出列表值（也就是跨行数据）
				int t_rowIndex = Integer
						.parseInt((String) tmpList.get(a_index));

				// 设置二维数组为空
				for (int n_index = rowIndex + 1; n_index < rowIndex
						+ t_rowIndex; n_index++) {
					reList[n_index][t_index] = "";
				}
				rowIndex = rowIndex + t_rowIndex;

			}

		}//

		return reList;

	}

	/**
	 * 求数组单列汇总值
	 * 
	 * @param index
	 * @param list
	 * @return
	 */
	public static String getResultSum(int index, String[][] list) {
		String result = "0";
		String tmp = "0";
		for (int i = 0; i < list.length; i++) {
			if (list[i][index].length() > 0
					&& !list[i][index].trim().equals("")) {
				tmp = list[i][index];
			} else {
				tmp = "0";
			}
			result = Arith.add(result, tmp);
		}
		return result;
	}

}
