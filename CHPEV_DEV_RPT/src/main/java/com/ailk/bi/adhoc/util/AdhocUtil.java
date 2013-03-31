package com.ailk.bi.adhoc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.csvreader.CsvWriter;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.pages.CommKeys;
import com.ailk.bi.report.struct.ReportQryStruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings({ "unused", "unchecked", "static-access", "rawtypes" })
public class AdhocUtil {

	private static Log logger = LogFactory.getLog(AdhocUtil.class);

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
			logger.debug(sqlId + "================" + sql);
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
			logger.debug(sqlId + "================" + sql);
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
			logger.debug(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 
	 * @param adhoc_id
	 * @param ctlStruct
	 * @param ctlSplitOk
	 * @param session
	 * @return
	 * @客户化用户权限控制，返回代码：返回数组，a[0]:table ，a[1]:条件
	 */
	public static String[] getAdhocRightControl(String adhoc_id,
			UserCtlRegionStruct ctlStruct, int ctlSplitOk, HttpSession session) {
		String strDeptTable = "";
		String strSqlDeptCon = "";
		String[] rtn = new String[2];

		String[] adhocDef = AdhocUtil.getAdhocRightCtrl(adhoc_id);
		int iFlagCtl = Integer.parseInt(adhocDef[0]);

		if (iFlagCtl == 1) {
			int iLevel = 0;
			if (ctlStruct.ctl_lvl.length() > 0) {
				iLevel = Integer.parseInt(ctlStruct.ctl_lvl);
			}

			if (iLevel > 1) {
				if (ctlStruct.ctl_sql.trim().length() > 0) {
					String ctlFlag = ctlStruct.ctl_flag.trim();
					int iCtlFlag = 0;
					if (ctlFlag.length() > 0) {
						iCtlFlag = Integer.parseInt(ctlFlag);
					}
					switch (iCtlFlag) {
					case 1:
						// 发展人部门
						int hasSecond = StringB.toInt(adhocDef[3], 0);
						int isSplit = StringB.toInt(adhocDef[4], 0);

						if (hasSecond == 1 && isSplit == 1 && ctlSplitOk == 1) {
							int ctl_lvl = StringB.toInt(ctlStruct.ctl_lvl, 1);
							switch (ctl_lvl) {
							case 1:

								break;
							case 2:
								// sql += ",(" + ctlStruct.ctl_sql +
								// ") tblCtrl ";
								strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID2 in("
										+ ctlStruct.ctl_city_str_add + ")";

								break;
							case 3:
								strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID3 in("
										+ ctlStruct.ctl_county_str_add + ")";
								strSqlDeptCon += " AND USER_DEVELOP_DEPART_ID2 in("
										+ ctlStruct.ctl_city_str_add + ")";

								break;
							case 4:

								break;
							case 5:

								break;
							}

						} else {
							strDeptTable = ",(" + ctlStruct.ctl_sql
									+ ") tblCtrl ";
							strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID=tblCtrl.dept_id ";
						}

						break;
					case 2:
						// 楼宇部门
						// 发展人部门
						// strDeptTable = ",(" + ctlStruct.ctl_sql +
						// ") tblCtrl ";
						// strSqlDeptCon =
						// " AND BUILDING_MGR_DEPT_ID=tblCtrl.dept_id ";

						// ENT_GNGJ_FLAG 国际:2 VARCHAR2
						// BI_U_GROUP_ID 政企:1 NUMBER
						String grp_id = DAOFactory.getCommonFac().getLoginUser(
								session).group_id;
						if (grp_id.equals("018")) {
							// 集团客户部：jtkh_ly 用户分群(客户类型)=政企 and 政企国内国际标志 <>国际
							String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'";
							String[][] tmp = AdhocUtil.getTableFieldType(
									"NEW_DW", adhocDef[2], fieldName);
							if (tmp != null && tmp.length > 0) {
								for (int m = 0; m < tmp.length; m++) {
									String dataType = tmp[m][0].substring(0, 3);
									String fldName = tmp[m][1];
									if (fldName
											.equalsIgnoreCase("ENT_GNGJ_FLAG")) {
										if (dataType.equalsIgnoreCase("VAR")) {
											strSqlDeptCon += " AND ENT_GNGJ_FLAG!='2'";
										} else {
											strSqlDeptCon += " AND ENT_GNGJ_FLAG!=2";
										}
									} else if (fldName
											.equalsIgnoreCase("BI_U_GROUP_ID")) {
										if (dataType.equalsIgnoreCase("VAR")) {
											strSqlDeptCon += " AND BI_U_GROUP_ID='1'";
										} else {
											strSqlDeptCon += " AND BI_U_GROUP_ID=1";
										}
									}

								}

							}

						} else if (grp_id.equals("019")) {
							// 如果是国际业务部 权限：用户分群(客户类型)=政企 and 政企国内国际标志 =国际
							String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'";
							String[][] tmp = AdhocUtil.getTableFieldType(
									"NEW_DW", adhocDef[2], fieldName);
							if (tmp != null && tmp.length > 0) {
								for (int m = 0; m < tmp.length; m++) {
									String dataType = tmp[m][0].substring(0, 3);
									String fldName = tmp[m][1];
									if (fldName
											.equalsIgnoreCase("ENT_GNGJ_FLAG")) {
										if (dataType.equalsIgnoreCase("VAR")) {
											strSqlDeptCon += " AND ENT_GNGJ_FLAG='2'";
										} else {
											strSqlDeptCon += " AND ENT_GNGJ_FLAG=2";
										}
									} else if (fldName
											.equalsIgnoreCase("BI_U_GROUP_ID")) {
										if (dataType.equalsIgnoreCase("VAR")) {
											strSqlDeptCon += " AND BI_U_GROUP_ID='1'";
										} else {
											strSqlDeptCon += " AND BI_U_GROUP_ID=1";
										}
									}

								}

							}
						} else {

							String limitFld = StringB.NulltoBlank(adhocDef[1]);
							if (limitFld.length() > 0) {
								if (limitFld
										.equalsIgnoreCase("BUILDING_BURA_ID")) {
									if (ctlStruct.CTL_BUILDING_BURA_ID.length() > 0) {
										strSqlDeptCon = " AND BUILDING_BURA_ID in("
												+ ctlStruct.CTL_BUILDING_BURA_ID
												+ ") ";
									}

								} else if (limitFld
										.equalsIgnoreCase("BUILDING_ID")) {
									if (ctlStruct.CTL_BUILDING_BURA_ID.length() > 0) {
										strDeptTable += ",(select distinct building_id BJ_CTLBUILDID from new_dim.d_building where BUILDING_BURA_ID in ("
												+ ctlStruct.CTL_BUILDING_BURA_ID
												+ ")) tblCtrl ";
										strSqlDeptCon = " AND BUILDING_ID=tblCtrl.BJ_CTLBUILDID ";
									}

								}
							}
						}

						break;
					}

				}
			}
		}
		rtn[0] = strDeptTable;
		rtn[1] = strSqlDeptCon;

		return rtn;
	}

	/**
	 * 
	 * @param adhoc_id
	 * @return
	 * @desc:获取某即席查询是否需要权限控制
	 * 
	 */
	public static String[] getAdhocRightCtrl(String adhoc_id) {
		String sql = "select ISRIGHT_CTL,CTRL_FIELD,DATA_TABLE,IS_SND_DPT,IS_SPLIT from ui_adhoc_info_def where adhoc_id='"
				+ adhoc_id + "'";
		// String sql =
		// "select ISRIGHT_CTL,CTRL_FIELD,DATA_TABLE,IS_SND_DPT,1 from ui_adhoc_info_def where adhoc_id='"
		// + adhoc_id + "'";
		// System.out.println(sql);
		String[][] arr = null;
		String[] arrRtn = { "1", "", "", "", "", "" };

		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			arrRtn[0] = arr[0][0];
			arrRtn[1] = arr[0][1];
			arrRtn[2] = arr[0][2];
			arrRtn[3] = arr[0][3];
			arrRtn[4] = arr[0][4];
		}
		return arrRtn;
	}

	public static String[][] getTableFieldType(String schema, String tableName,
			String fieldName) {
		String tbl = tableName;
		if (tableName.indexOf(".") > 0) {
			tbl = tableName.substring(tableName.indexOf(".") + 1);
		}

		String sql = "select DATA_TYPE,COLUMN_NAME from all_tab_cols where owner='"
				+ schema
				+ "' AND TABLE_NAME=UPPER('"
				+ tbl
				+ "') "
				+ "AND UPPER(COLUMN_NAME) IN (" + fieldName + ")";

		String[][] arr = null;

		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
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
			logger.debug(sql);
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
	public static String[][] queryArrayFacade(String sql, String[] strWhere) {
		String arr[][] = null;
		try {
			logger.debug(sql);
			arr = WebDBUtil.execQryArray(sql, strWhere, "");
		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询Vector
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static Vector queryVectorFacade(String sqlId, String whereStr) {
		String sql = "";
		Vector v = new Vector();
		try {
			sql = SQLGenator.genSQL(sqlId, whereStr);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return v;
	}

	/**
	 * 判断当前值是否被选择上,如果选择上,则返回真,否则返回假
	 * 
	 * @param arr
	 * @param id
	 * @return
	 */
	public static boolean isChecked(String[] arr, String id) {
		boolean isCheck = false;
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (arr[i].equals(id)) {
				isCheck = true;
				break;
			}
		}
		return isCheck;
	}

	/**
	 * 判断是否存在
	 * 
	 * @param set
	 * @param id
	 * @return
	 */
	public static boolean isDefaultChecked(HashSet set, String id) {
		boolean isCheck = false;
		if (set.contains(id)) {
			isCheck = true;
		}
		return isCheck;
	}

	/**
	 * 判断当前值是否被选择上,如果选择上,则返回真,否则返回假
	 * 
	 * @param arr
	 * @param id
	 * @return
	 */
	public static String getNbspTdInnerHtml(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append("<td>&nbsp;</td>\n");
		}
		return sb.toString();
	}

	/**
	 * 填充单引号
	 * 
	 * @param str
	 * @return
	 */
	public static String fillCharString(String str) {
		String reStr = "";
		if (str.equals(",")) {
			return "'" + str + "'";
		}

		String symSplit = "\\" + CommKeys.ID_DELI;
		String arr[] = str.split(symSplit);
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (reStr.length() > 0) {
				reStr += ",";
			}
			reStr += "'" + arr[i] + "'";
		}
		return reStr;
	}

	public static String getSQLFirstField(String sql) {
		// String sql = "select distinct  abcdd   ,bb from sdfdsf";
		boolean loop = true;
		int dotPos = sql.toUpperCase().indexOf(",");
		String strField = sql.substring(0, dotPos);
		System.out.println(strField);

		String tmp = "";
		int iTmp = 1;
		while (loop) {

			tmp = StringB.NulltoBlank(
					strField.substring(dotPos - iTmp - 1, dotPos - iTmp))
					.trim();
			if (tmp.length() > 0) {
				break;
			}
			iTmp++;
		}
		int savePos = dotPos - iTmp;
		iTmp = 1;
		while (loop) {
			tmp = StringB.NulltoBlank(
					strField.substring(savePos - iTmp, savePos - iTmp + 1))
					.trim();
			if (tmp.length() == 0) {
				break;
			}
			iTmp++;
		}

		return sql.substring(savePos - iTmp, savePos + 1);

	}

	/**
	 * 过滤合并
	 * 
	 * @param list
	 * @param arrIndex
	 * @return
	 */
	public static String[][] getCombineArr(String[][] list, int[] arrIndex) {

		return null;

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
		for (int i = 0; i < length; i++) {
			index[i] = i;
		}
		/*
		 * if ("0".equals(flag)) {
		 * 
		 * for (int i = 0; i < length; i++) { index[i] = 2 * i; }
		 * 
		 * } else { for (int i = 0; i < length; i++) { index[i] = 2 * i + 1; } }
		 */
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
				// System.out.println("t_rowIndex================="+t_rowIndex);
				// 设置二维数组为空
				for (int n_index = rowIndex + 1; n_index < row
						&& n_index < rowIndex + t_rowIndex; n_index++) {
					reList[n_index][t_index] = "";
				}
				rowIndex = rowIndex + t_rowIndex;

			}

		}//

		return reList;

	}

	/**
	 * 取得最大ID
	 * 
	 * @param seqTabName
	 * @return
	 * @throws AppException
	 */
	public synchronized static String dbGetMaxID(String seqTabName)
			throws AppException {
		String maxID = "1";
		String sql = SQLGenator.genSQL("QT1005", seqTabName);
		// 查询
		String res[][] = WebDBUtil.execQryArray(sql, "");
		if (res == null) {
			sql = SQLGenator.genSQL("IT1006", seqTabName);
		} else {
			maxID = res[0][0];
			sql = SQLGenator.genSQL("CT1007", maxID, seqTabName);
		}
		WebDBUtil.execUpdate(sql);

		return maxID;
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
	 * @param index
	 * @param startIndex
	 * @param colrap
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
			} else if (i >= 1
					&& (!list[i][t_index].equals(list[i - 1][t_index]))) {
				tmpList.add(list[i][t_index]);
			}
		}

		//
		for (int i = 0; i < tmpList.size(); i++) {
			// 关键字
			String key = (String) tmpList.get(i);
			// 截断数组
			String[][] t1 = arrayCutCopy(list, t_index, key);
			// 存储索引数据
			ArrayList rowLen = (ArrayList) arrList.get(startIndex);
			rowLen.add(String.valueOf(t1.length));
			// 递归嵌套
			compRowArr(t1, index, arrList, startIndex + rap, rap);
		}

	}

	/**
	 * 删除二维数组
	 * 
	 * @param value
	 * @param key
	 * @return
	 */
	public static String[] removeArr(String[] value, String key) {
		String[] tmpValue = null;
		if (value != null && value.length > 0) {
			tmpValue = new String[value.length - 1];
		}

		for (int i = 0, j = 0; value != null && i < value.length; i++) {
			if (value[i].equalsIgnoreCase(key)) {
				continue;
			} else {
				tmpValue[j++] = value[i];
			}
		}

		return tmpValue;

	}

	public static void main(String[] args) {
		/*
		 * String arr[][] = { { "jcm", "1980", "t2", "32" }, { "jcm", "1980",
		 * "t1", "32" }, { "xy", "1931", "t3", "32" }, { "xy", "1931", "t4",
		 * "32" }, { "xy", "1970", "t1", "32" }, { "xy", "1970", "t2", "32" }, {
		 * "xy", "1970", "t3", "32" }, { "xy", "1970", "t4", "32" }, { "jin",
		 * "1970", "t4", "32" } };
		 */

		String arr[][] = { { "-1", "不详", "2", "[1,3)月", "-1", "不详	" },
				{ "-1", "不详", "2", "[1,3)月", "0", "未停机	" },
				{ "-1", "不详", "3", "[3,6)月", "-1", "不详	" },
				{ "-1", "不详", "3", "[3,6)月", "0", "未停机	" },
				{ "-1", "不详", "3", "[3,6)月", "1", "(, 1)	" },
				{ "-1", "不详", "3", "[3,6)月", "2", "[1, 2)	" },
				{ "-1", "不详", "4", "[6,12)月", "-1", "不详	" },
				{ "-1", "不详", "4", "[6,12)月", "0", "未停机	" },
				{ "-1", "不详", "4", "[6,12)月", "1", "(, 1)	" },
				{ "-1", "不详", "4", "[6,12)月", "2", "[1, 2)	" },
				{ "22", "18岁", "4", "[6,12)月", "0", "未停机	" },
				{ "30", "22岁", "3", "[3,6)月", "0", "未停机	" },
				{ "30", "22岁", "4", "[6,12)月", "0", "未停机	" },
				{ "30", "22岁", "4", "[6,12)月", "2", "[1, 2)	" },
				{ "40", "30岁", "2", "[1,3)月", "0", "未停机	" },
				{ "40", "30岁", "3", "[3,6)月", "0", "未停机	" },
				{ "40", "30岁", "3", "[3,6)月", "2", "[1, 2)	" },
				{ "40", "30岁", "4", "[6,12)月", "-1", "不详	" },
				{ "40", "30岁", "4", "[6,12)月", "0", "未停机	" },
				{ "50", "40岁", "3", "[3,6)月", "0", "未停机	" },
				{ "50", "40岁", "4", "[6,12)月", "0", "未停机	" },
				{ "60", "50岁", "4", "[6,12)月", "0", "未停机	" }

		};

		int t1[] = { 1, 3, 5 };
		ArrayList list = getCombinRowArr(arr, t1, 0, 1);
		for (int i = 0; i < list.size(); i++) {
			ArrayList tmp = (ArrayList) list.get(i);
			for (int j = 0; j < tmp.size(); j++) {
				System.out.print(" " + tmp.get(j));
			}
			// logger.debug();
		}

		String[][] reList = getCombineHelperList(arr, t1, list);

		for (int i = 0; i < reList.length; i++) {
			for (int j = 0; j < reList[i].length; j++) {
				System.out.print("====> " + reList[i][j]);
			}
			// logger.debug();
		}
	}

	public static void sendInfoToSocket(String id, String serverIp) {
		try {
			String server = "172.19.31." + serverIp;

			Socket socket = new Socket(server, 10984);
			System.out.println("send request to server");

			PrintWriter out = null;
			try {

				out = new PrintWriter(socket.getOutputStream());
				System.out.println("send task id:" + id);
				out.println(id);
				out.flush();

			} finally {
				System.out.println("Finally");
				socket.close();
				out.close();

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void buildCSVFile(ReportQryStruct struct, OutputStream stream) {

		String pmsid = struct.dim6;
		String cservice_times_lvl_id = struct.dim2;
		String pms = "";
		String whereSql = " ";
		String dimFld = " ";
		if (pmsid.equalsIgnoreCase("1")) {

			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cm_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cm_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CM_O_CSERVICE_TIMES";
			pms = "与移动客服";
		} else if (pmsid.equalsIgnoreCase("2")) {
			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cu_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cu_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CU_O_CSERVICE_TIMES";
			pms = "与联通客服";
		} else if (pmsid.equalsIgnoreCase("3")) {
			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cm_cu_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cm_cu_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CM_CU_O_CSERVICE_TIMES";
			pms = "交叉客服";
		}

		String sqlSelect = "SELECT ACC_NBR " + dimFld
				+ " FROM FUI_OPP_ALERT_BUSI_USE_M A WHERE op_time="
				+ struct.dim1 + " AND  CARRIER_ID=10 " + whereSql;

		try {
			CsvWriter csvwr = new CsvWriter(stream, ',',
					Charset.forName("gb2312"));
			String[] writeRcd = new String[2];
			writeRcd[0] = "电话号码";
			writeRcd[1] = "次数";
			csvwr.writeRecord(writeRcd);

			double recordCnt = 0;
			String sqlCnt = "select count(*) from (" + sqlSelect + ")";

			String sqlQry = "select ACC_NBR " + dimFld
					+ " from (select JFTOT.*,ROWNUM RN from (" + sqlSelect
					+ ") JFTOT  Where rownum <= ?) where RN > ?";
			try {
				String[][] res = WebDBUtil.execQryArray(sqlCnt, "");
				if (res != null && res.length > 0) {
					recordCnt = Double.parseDouble(res[0][0]);
				}
			} catch (AppException e) {

				e.printStackTrace();
			}
			int perPageCnt = 10000;
			double pageCnt = Math.floor((recordCnt + perPageCnt - 1)
					/ perPageCnt);
			for (int i = 0; i < pageCnt; i++) {
				int dblTmp = (i + 1) * perPageCnt;
				String strWhere[] = new String[] { dblTmp + "|1",
						i * perPageCnt + "|1" };

				try {
					System.out.println(sqlQry);
					for (int m = 0; m < strWhere.length; m++) {
						System.out.println(strWhere[m]);

					}
					String arr[][] = WebDBUtil.execQryArray(sqlQry, strWhere,
							"");

					if (arr != null && arr.length > 0) {
						for (int j = 0; j < arr.length; j++) {
							String[] value = arr[j];
							csvwr.writeRecord(value);
						}
					}
				} catch (AppException e) {

					e.printStackTrace();
				}

			}

			csvwr.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 查询数组
	 * 
	 * @param sql
	 * @return
	 */
	public static int insertSqlLog(String sql, String adhoc_id, String oper_no,
			int iCnt, int qry_type) {
		int iRow = 0;
		try {
			sql = StringB.replace(sql, "'", "'||chr(39)||'");
			String insertSql = "INSERT INTO UI_ADHOC_BUILDSQL SELECT seq_adhoc_qry_log.nextval,'"
					+ oper_no + "',a.adhoc_id,a.adhoc_name";
			insertSql += "," + iCnt + ",'" + sql + "',SYSDATE,'" + qry_type
					+ "' FROM ui_adhoc_info_def a WHERE a.adhoc_id='"
					+ adhoc_id + "'";
			logger.debug(insertSql);
			iRow = WebDBUtil.execUpdate(insertSql);
		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return iRow;
	}
}
