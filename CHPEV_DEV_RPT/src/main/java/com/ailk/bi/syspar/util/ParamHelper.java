package com.ailk.bi.syspar.util;

import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;

public class ParamHelper {
	/**
	 * 拼接下拉列表框的内存列表数据展示
	 * 
	 * @param list
	 * @return
	 */
	public static String getSelectListParam(UiParamMetaConfigTable[] list) {
		String reStr = "";
		if (list != null && list.length > 0) {
			for (int i = 0; i < list.length; i++) {
				reStr += list[i].getColumn_en_name() + "|"
						+ list[i].getColumn_data_type().toUpperCase() + ","
						+ list[i].getColumn_cn_name() + ";";
			}
		}
		return reStr;

	}

	/**
	 * 　清除会话
	 * 
	 * @param session
	 */
	public static void clearSession(HttpSession session) {
		session.removeAttribute(ParamConstant.PARAM_CONDITION_NODE_ID);
		session.removeAttribute(ParamConstant.PARAM_CONDITION_RESULT);
		session.removeAttribute(ParamConstant.PARAM_CONF_INFO_TABLE);
		session.removeAttribute(ParamConstant.PARAM_CONF_META_TABLE);
		session.removeAttribute(ParamConstant.PARAM_SQL_VALUE);
		session.removeAttribute(ParamConstant.PARAM_TABLE_NAME);
		session.removeAttribute(ParamConstant.PARAM_CLEAR_FLAG);
	}

	/**
	 * 查询分析条件数据列表
	 * 
	 * @param nodeId
	 *            参数标识
	 * @param tableName
	 *            表格名称
	 * @param lsConStr
	 *            查询条件
	 * @return
	 */
	public static String[][] getQueryData(String nodeId, String tableName,
			String lsConStr) {
		// 字段英文名称
		String columnEnName = "";
		// 数据类型
		String columnDataType = "";
		// 数据格式
		String dateFmt = "";
		// 显示类型
		String showType = "";
		// 特殊规则
		String showRule = "";

		String parSql = "";

		parSql = "Select  \n";

		// 取某个表的配置字段信息，组合成查询该表数据的查询字串parSql
		String arr[][] = null;

		arr = ParamUtil.queryArrayFacade("QT132", nodeId);
		for (int i = 0; arr != null && i < arr.length; i++) {
			columnEnName = arr[i][2];
			columnDataType = arr[i][3];
			dateFmt = arr[i][8];
			showType = arr[i][5];
			showRule = arr[i][6];
			//
			if (columnDataType.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {// 时间字段

				if (showType.trim().equals(ParamConstant.COLUMN_SHOW_TYPE_3)) {
					if (dateFmt == null || dateFmt.equals("")) {
						dateFmt = "'yyyymm'";
					}
				} else if (showType.trim().equals(
						ParamConstant.COLUMN_SHOW_TYPE_2)) {
					if (dateFmt == null || dateFmt.equals("")) {
						dateFmt = "'yyyymmdd'";
					}
				}
				parSql += "to_char(" + tableName + '.' + columnEnName + ','
						+ dateFmt + ")" + ',' + "\n";
			} else {// 普通字段
				if (showType.equals(ParamConstant.COLUMN_SHOW_TYPE_8)
						&& !"".equals(showRule)) {// 单选
					parSql += tableName + '.' + showRule + ',' + "\n";
				}
				parSql += tableName + '.' + columnEnName + ',' + "\n";
			}

		}
		parSql = parSql.substring(0, parSql.lastIndexOf(','));
		parSql += " " + "from" + "  ";
		parSql += tableName;
		parSql += "\n";
		parSql += "  " + "where" + " 1=1  " + "\n";

		if (lsConStr.length() > 0) {
			parSql += "and" + " " + "(" + lsConStr + ")";
		}

		//
		System.out.println("parSql==================" + parSql);
		//
		String[][] rs_data = null;
		try {
			rs_data = WebDBUtil.execQryArray(parSql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs_data;
	}

	/**
	 * 判断是否有唯一索引存在
	 * 
	 * @param configInfo
	 * @return
	 */
	public static boolean hasUniqeIndex(UiParamMetaConfigTable[] configInfo) {
		boolean flag = false;
		for (int i = 0; configInfo != null && i < configInfo.length; i++) {
			if (configInfo[i].getUnique_index().toUpperCase().equals("Y")) {
				flag = true;
				break;
			}
		}

		return flag;

	}

	/**
	 * 判断值是否重复
	 * 
	 * @param SQL
	 * @return
	 */
	public static boolean isDupValue(String SQL) {
		boolean flag = false;
		try {
			String[][] rs_data = WebDBUtil.execQryArray(SQL, "");
			if (rs_data != null && rs_data.length > 0) {
				flag = true;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 判断值是否重复
	 * 
	 * @param SQL
	 * @return
	 */
	public static String getSeqNextValue(String SQL) {
		String seq_value = "-1";
		try {
			String[][] rs_data = WebDBUtil.execQryArray(SQL, "");
			if (rs_data != null && rs_data.length > 0) {
				seq_value = rs_data[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return seq_value;
	}

}
