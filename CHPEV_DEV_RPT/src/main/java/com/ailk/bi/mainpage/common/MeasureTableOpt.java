package com.ailk.bi.mainpage.common;

import java.util.Vector;

import com.ailk.bi.base.table.UiFmKpiDTable;
import com.ailk.bi.base.table.UiMainTableCfg;
import com.ailk.bi.base.table.UiMainTableName;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public class MeasureTableOpt {
	/**
	 * 获取今日视点定义表
	 * 
	 * @return
	 * @throws AppException
	 */
	public static UiMainTableName[] getDayViewDef() throws AppException {
		String strSql = SQLGenator.genSQL("Q4120");
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			return null;
		}
		UiMainTableName[] tables = new UiMainTableName[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			tables[i] = new UiMainTableName();
			int m = 0;
			tables[i].table_id = (String) tempv.get(m++);
			tables[i].table_name = (String) tempv.get(m++);
			tables[i].data_table = (String) tempv.get(m++);
			tables[i].sql_where = (String) tempv.get(m++);
			tables[i].trs_table = (String) tempv.get(m++);
			tables[i].date_code = (String) tempv.get(m++);
			tables[i].region_code = (String) tempv.get(m++);
			tables[i].measure_code = (String) tempv.get(m++);
			tables[i].select_code = (String) tempv.get(m++);
		}
		return tables;
	}

	/**
	 * 获取今日视点定义表
	 * 
	 * @param user_id
	 * @return
	 * @throws AppException
	 */
	public static UiMainTableName[] getDayViewUserDef(String user_id)
			throws AppException {
		String strSql = SQLGenator.genSQL("Q4121", user_id);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			strSql = SQLGenator.genSQL("Q4121", "0");
			result = WebDBUtil.execQryVector(strSql, "");
		}
		if (result == null || result.size() == 0) {
			return null;
		}
		UiMainTableName[] tables = new UiMainTableName[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			tables[i] = new UiMainTableName();
			int m = 0;
			tables[i].table_id = (String) tempv.get(m++);
			tables[i].table_name = (String) tempv.get(m++);
			tables[i].data_table = (String) tempv.get(m++);
			tables[i].sql_where = (String) tempv.get(m++);
			tables[i].trs_table = (String) tempv.get(m++);
			tables[i].date_code = (String) tempv.get(m++);
			tables[i].region_code = (String) tempv.get(m++);
			tables[i].measure_code = (String) tempv.get(m++);
			tables[i].select_code = (String) tempv.get(m++);
		}
		return tables;
	}

	/**
	 * 得到今日视点表格定义
	 * 
	 * @param table_id
	 *            调用的显示表格ID
	 * @return String[][]数组
	 * @throws AppException
	 */
	public static UiMainTableCfg[] getTableDef(String table_id)
			throws AppException {
		String strSql = SQLGenator.genSQL("Q4122", table_id);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			return null;
		}
		UiMainTableCfg[] tables = new UiMainTableCfg[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			tables[i] = new UiMainTableCfg();
			int m = 0;
			tables[i].table_id = (String) tempv.get(m++);
			tables[i].item_id = (String) tempv.get(m++);
			tables[i].axis_type = (String) tempv.get(m++);
			tables[i].field_value = (String) tempv.get(m++);
			tables[i].field_code = (String) tempv.get(m++);
			tables[i].field_desc = (String) tempv.get(m++);
			tables[i].digit_len = (String) tempv.get(m++);
			tables[i].head_style = (String) tempv.get(m++);
		}
		return tables;
	}

	/**
	 * 得到今日视点表格定义
	 * 
	 * @param user_id
	 *            用户ID
	 * @param table_id
	 *            调用的显示表格ID
	 * @return String[][]数组
	 * @throws AppException
	 */
	public static UiMainTableCfg[] getTableUserDef(String user_id,
			String table_id) throws AppException {
		String strSql = SQLGenator.genSQL("Q4123", user_id, table_id);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			strSql = SQLGenator.genSQL("Q4123", "0", table_id);
			result = WebDBUtil.execQryVector(strSql, "");
		}
		if (result == null || result.size() == 0) {
			return null;
		}
		UiMainTableCfg[] tables = new UiMainTableCfg[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			tables[i] = new UiMainTableCfg();
			int m = 0;
			tables[i].table_id = (String) tempv.get(m++);
			tables[i].item_id = (String) tempv.get(m++);
			tables[i].axis_type = (String) tempv.get(m++);
			tables[i].field_value = (String) tempv.get(m++);
			tables[i].field_code = (String) tempv.get(m++);
			tables[i].field_desc = (String) tempv.get(m++);
			tables[i].digit_len = (String) tempv.get(m++);
			tables[i].head_style = (String) tempv.get(m++);
		}
		return tables;
	}

	/**
	 * 获取表格指定轴定义
	 * 
	 * @param tables
	 * @param axis_type
	 * @return
	 */
	private static UiMainTableCfg[] getTableDef(UiMainTableCfg[] tables,
			String axis_type) {
		UiMainTableCfg[] tabledef = null;
		int len = 0;
		for (int i = 0; tables != null && i < tables.length; i++) {
			if (axis_type.equals(tables[i].axis_type)) {
				len++;
			}
		}
		// 长度为0返回null
		if (len == 0) {
			return tabledef;
		}
		tabledef = new UiMainTableCfg[len];
		int m = 0;
		for (int i = 0; tables != null && i < tables.length; i++) {
			if (axis_type.equals(tables[i].axis_type)) {
				tabledef[m++] = tables[i];
			}
		}
		return tabledef;
	}

	/**
	 * 获取今日视点指标表格数据
	 * 
	 * @param table
	 *            定义表
	 * @param user_id
	 *            操作员ID
	 * @param region_id
	 *            用户所属地域
	 * @return String[]数组
	 * @throws AppException
	 */
	public static String[] getDayViewUserDefTable(UiMainTableName table,
			String user_id, String region_id, String svc_knd)
			throws AppException {
		String[] arrTable = null;// 返回的结果集
		boolean isTrans = false;// 是否为反转表格
		if ("Y".equals(table.trs_table)) {
			isTrans = true;
		}

		// 获取指定ID的表定义
		UiMainTableCfg[] tableDef = getTableUserDef(user_id, table.table_id);
		// 获取指定ID的表头定义(纬度值，需要显示的列数)
		UiMainTableCfg[] arrDim = getTableDef(tableDef, "0");
		if (arrDim == null || arrDim.length == 0) {
			arrTable = new String[1];
			arrTable[0] = table.table_name;
			return arrTable;
		}
		// 获取指定ID的侧表头定义(指标值，需要显示的行数)
		UiMainTableCfg[] arrMea = getTableDef(tableDef, "1");
		if (arrMea == null || arrMea.length == 0) {
			arrTable = new String[1];
			arrTable[0] = table.table_name;
			return arrTable;
		}
		// 纬度信息
		String ls_dim = StringTool.changArrToStrComma(arrDim, "field_value");
		// 纬度字段名
		String ls_dim_code = arrDim[0].field_code;
		// 指标ID
		String ls_mea = StringTool.changArrToStrComma(arrMea, "field_value");
		// 数据日期
		String ls_date = getMaxDate(table, ls_mea);
		// 获取该条件下的指标值
		String ls_where = table.sql_where;
		if (ls_mea != null && !"".equals(ls_mea))
			ls_where += " and " + table.measure_code + " in (" + ls_mea + ")";
		if (ls_dim != null && !"".equals(ls_dim))
			ls_where += " and " + ls_dim_code + " in (" + ls_dim + ")";
		if (region_id != null && !"".equals(region_id))
			ls_where += " and " + table.region_code + " in (" + region_id + ")";
		// 业务类型条件
		ls_where += " and SVCKND_ID='" + svc_knd + "'";
		// 时间条件
		if (ls_date != null && !"".equals(ls_date))
			ls_where += " and " + table.date_code + "=" + ls_date;

		// 得到表格数据
		String select_code = "SELECT " + table.select_code + " FROM "
				+ table.data_table;
		UiFmKpiDTable[] tabledata = getTableData(select_code, ls_where);
		// 初始化返回表格数据
		int addLen = 3;// 返回表格增加的行数
		arrTable = new String[arrMea.length + addLen];
		if (isTrans) {
			arrTable = new String[arrDim.length + addLen];
		}

		// 画出表格的<tr>表头部分
		arrTable[0] = table.table_name;
		arrTable[1] = StringTool.getCnDate(ls_date, "6");
		arrTable[2] = "<tr>";
		arrTable[2] += "<td width=\"26%\" height=\"16\">&nbsp;</td>";
		if (isTrans) {
			// 反转表格
			for (int i = 0; i < arrMea.length; i++) {
				String tdstyle = arrMea[i].head_style;
				arrTable[2] += "<td " + tdstyle + ">" + arrMea[i].field_desc
						+ "</td>";
			}
		} else {
			// 正常表格
			for (int i = 0; i < arrDim.length; i++) {
				String tdstyle = arrDim[i].head_style;
				arrTable[2] += "<td " + tdstyle + ">" + arrDim[i].field_desc
						+ "</td>";
			}
		}
		arrTable[2] += "</tr>";

		// 画出表格的<tr>数据部分
		if (isTrans) {
			// 反转表格
			for (int i = 0; i < arrDim.length; i++) {
				String td_value = ""; // td显示的值
				String tdstyle = arrMea[i].head_style;
				arrTable[i + addLen] = "<tr>";
				arrTable[i + addLen] += "<td nowrap height=\"19\" " + tdstyle
						+ ">" + arrDim[i].field_desc + "</td>";
				for (int j = 0; j < arrMea.length; j++) {
					String tmpMeaValue = arrMea[j].field_value; // 指标ID
					String tmpMeaCode = arrMea[j].field_code; // 指标值对应字段
					String tmpDimValue = arrDim[i].field_value; // 纬度值
					String tmpDimCode = arrDim[j].field_code.toLowerCase(); // 纬度对应字段
					for (int k = 0; tabledata != null && k < tabledata.length; k++) {
						UiFmKpiDTable temp = tabledata[k];
						if (tmpMeaValue.equals(temp.measure_id)
								&& tmpDimValue.equals(ReflectUtil
										.getStringFromObj(temp, tmpDimCode))) {
							td_value = ReflectUtil.getStringFromObj(temp,
									tmpMeaCode.toLowerCase());
							if (td_value == null || "".equals(td_value))
								td_value = "";
							else if (tmpMeaCode.toUpperCase().indexOf("PER") > -1)
								td_value = Arith.divPer(td_value, "100",
										StringB.toInt(temp.precision, 0));
							else
								td_value = FormatUtil.formatStr(td_value,
										StringB.toInt(temp.precision, 0), true);
						}
					}
					if ("".equals(td_value)) {
						td_value = "&nbsp;";
					}
					arrTable[i + addLen] += "<td align=\"right\" class=\"talbe-form\">"
							+ td_value + "</td>";
				}
				arrTable[i + addLen] += "</tr>";
			}
		} else {
			// 正常表格
			for (int i = 0; i < arrMea.length; i++) {
				String td_value = ""; // td显示的值
				String tdstyle = arrMea[i].head_style;
				arrTable[i + addLen] = "<tr>";
				arrTable[i + addLen] += "<td height=\"19\" " + tdstyle + ">"
						+ arrMea[i].field_desc + "</td>";
				for (int j = 0; j < arrDim.length; j++) {
					String tmpMeaValue = arrMea[i].field_value; // 指标ID
					String tmpMeaCode = arrMea[i].field_code; // 指标值对应字段
					String tmpDimValue = arrDim[j].field_value; // 纬度值
					String tmpDimCode = arrDim[j].field_code.toLowerCase(); // 纬度对应字段
					for (int k = 0; tabledata != null && k < tabledata.length; k++) {
						UiFmKpiDTable temp = tabledata[k];
						if (tmpMeaValue.equals(temp.measure_id)
								&& tmpDimValue.equals(ReflectUtil
										.getStringFromObj(temp, tmpDimCode))) {
							td_value = ReflectUtil.getStringFromObj(temp,
									tmpMeaCode.toLowerCase());
							if (td_value == null || "".equals(td_value))
								td_value = "";
							else if (tmpMeaCode.toUpperCase().indexOf("PER") > -1)
								td_value = Arith.divPer(td_value, "100",
										StringB.toInt(temp.precision, 0));
							else
								td_value = FormatUtil.formatStr(td_value,
										StringB.toInt(temp.precision, 0), true);
						}
					}
					if ("".equals(td_value)) {
						td_value = "&nbsp;";
					}
					arrTable[i + addLen] += "<td align=\"right\" class=\"talbe-form\">"
							+ td_value + "</td>";
				}
				arrTable[i + addLen] += "</tr>";
			}
		}
		return arrTable;
	}

	/**
	 * 获取今日视点指标表格数据
	 * 
	 * @param table
	 *            定义表
	 * @param user_id
	 *            操作员ID
	 * @param region_id
	 *            用户所属地域
	 * @return String[]数组
	 * @throws AppException
	 */
	public static String[] getDayViewAllDefTable(UiMainTableName table,
			String user_id, String region_id, String svc_knd)
			throws AppException {
		String[] arrTable = null;// 返回的结果集
		boolean isTrans = false;// 是否为反转表格
		if ("Y".equals(table.trs_table)) {
			isTrans = true;
		}

		// 获取指定ID的表定义
		UiMainTableCfg[] tableDef = getTableDef(table.table_id);
		UiMainTableCfg[] usertableDef = getTableUserDef(user_id, table.table_id);
		// 获取指定ID的表头定义(纬度值，需要显示的列数)
		UiMainTableCfg[] arrDim = getTableDef(tableDef, "0");
		UiMainTableCfg[] userarrDim = getTableDef(usertableDef, "0");
		if (arrDim == null || arrDim.length == 0) {
			arrTable = new String[1];
			arrTable[0] = table.table_name;
			return arrTable;
		}
		// 获取指定ID的侧表头定义(指标值，需要显示的行数)
		UiMainTableCfg[] arrMea = getTableDef(tableDef, "1");
		UiMainTableCfg[] userarrMea = getTableDef(usertableDef, "1");
		if (arrMea == null || arrMea.length == 0) {
			arrTable = new String[1];
			arrTable[0] = table.table_name;
			return arrTable;
		}
		// 纬度信息
		String ls_dim = StringTool.changArrToStrComma(arrDim, "field_value");
		// 纬度字段名
		String ls_dim_code = arrDim[0].field_code;
		// 指标ID
		String ls_mea = StringTool.changArrToStrComma(arrMea, "field_value");
		// 数据日期
		String ls_date = getMaxDate(table, ls_mea);
		// 获取该条件下的指标值
		String ls_where = table.sql_where;
		if (ls_mea != null && !"".equals(ls_mea))
			ls_where += " and " + table.measure_code + " in (" + ls_mea + ")";
		if (ls_dim != null && !"".equals(ls_dim))
			ls_where += " and " + ls_dim_code + " in (" + ls_dim + ")";
		if (region_id != null && !"".equals(region_id))
			ls_where += " and " + table.region_code + " in(" + region_id + ")";
		// 业务类型条件
		ls_where += " and SVCKND_ID='" + svc_knd + "'";
		// 时间条件
		if (ls_date != null && !"".equals(ls_date))
			ls_where += " and " + table.date_code + "=" + ls_date;

		// 得到表格数据
		String select_code = "SELECT " + table.select_code + " FROM "
				+ table.data_table;
		UiFmKpiDTable[] tabledata = getTableData(select_code, ls_where);
		// 初始化返回表格数据
		int addLen = 3;// 返回表格增加的行数
		arrTable = new String[arrMea.length + addLen];
		if (isTrans) {
			arrTable = new String[arrDim.length + addLen];
		}

		// 画出表格的<tr>表头部分
		arrTable[0] = table.table_name;
		arrTable[1] = StringTool.getCnDate(ls_date, "6");
		arrTable[2] = "<tr>";
		arrTable[2] += "<td width=\"26%\" height=\"16\">==需要显示请选中==</td>";
		if (isTrans) {
			// 反转表格
			for (int i = 0; i < arrMea.length; i++) {
				// 是否选中checkbox
				boolean isCheck = false;
				for (int k = 0; k < userarrMea.length; k++) {
					if (arrMea[i].table_id.equals(userarrMea[k].table_id)
							&& arrMea[i].item_id.equals(userarrMea[k].item_id)
							&& arrMea[i].axis_type
									.equals(userarrMea[k].axis_type)) {
						isCheck = true;
					}
				}
				// checkbox名称
				String box = arrMea[i].table_id + "|" + arrMea[i].item_id + "|"
						+ arrMea[i].axis_type;

				String tdstyle = arrMea[i].head_style;
				arrTable[2] += "<td " + tdstyle + ">";
				arrTable[2] += "<input type=\"checkbox\" id=" + box + " name="
						+ box;
				if (isCheck) {
					arrTable[2] += " checked";
				}
				arrTable[2] += " >";
				arrTable[2] += arrMea[i].field_desc + "</td>";
			}
		} else {
			// 正常表格
			for (int i = 0; i < arrDim.length; i++) {
				// 是否选中checkbox
				boolean isCheck = false;
				for (int k = 0; k < userarrDim.length; k++) {
					if (arrDim[i].table_id.equals(userarrDim[k].table_id)
							&& arrDim[i].item_id.equals(userarrDim[k].item_id)
							&& arrDim[i].axis_type
									.equals(userarrDim[k].axis_type)) {
						isCheck = true;
					}
				}
				// checkbox名称
				String box = arrDim[i].table_id + "|" + arrDim[i].item_id + "|"
						+ arrDim[i].axis_type;

				String tdstyle = arrDim[i].head_style;
				arrTable[2] += "<td " + tdstyle + ">";
				arrTable[2] += "<input type=\"checkbox\" id=" + box + " name="
						+ box;
				if (isCheck) {
					arrTable[2] += " checked";
				}
				arrTable[2] += " >";
				arrTable[2] += arrDim[i].field_desc + "</td>";
			}
		}
		arrTable[2] += "</tr>";

		// 画出表格的<tr>数据部分
		if (isTrans) {
			// 反转表格
			for (int i = 0; i < arrDim.length; i++) {
				// 是否选中checkbox
				boolean isCheck = false;
				for (int k = 0; k < userarrDim.length; k++) {
					if (arrDim[i].table_id.equals(userarrDim[k].table_id)
							&& arrDim[i].item_id.equals(userarrDim[k].item_id)
							&& arrDim[i].axis_type
									.equals(userarrDim[k].axis_type)) {
						isCheck = true;
					}
				}
				// checkbox名称
				String box = arrDim[i].table_id + "|" + arrDim[i].item_id + "|"
						+ arrDim[i].axis_type;

				String td_value = ""; // td显示的值
				String tdstyle = arrMea[i].head_style;
				arrTable[i + addLen] = "<tr>";
				arrTable[i + addLen] += "<td height=\"19\" " + tdstyle + ">";
				arrTable[i + addLen] += "<input type=\"checkbox\" id=" + box
						+ " name=" + box;
				if (isCheck) {
					arrTable[i + addLen] += " checked";
				}
				arrTable[i + addLen] += " >";
				arrTable[i + addLen] += arrDim[i].field_desc + "</td>";
				for (int j = 0; j < arrMea.length; j++) {
					String tmpMeaValue = arrMea[j].field_value; // 指标ID
					String tmpMeaCode = arrMea[j].field_code; // 指标值对应字段
					String tmpDimValue = arrDim[i].field_value; // 纬度值
					String tmpDimCode = arrDim[j].field_code.toLowerCase(); // 纬度对应字段
					for (int k = 0; tabledata != null && k < tabledata.length; k++) {
						UiFmKpiDTable temp = tabledata[k];
						if (tmpMeaValue.equals(temp.measure_id)
								&& tmpDimValue.equals(ReflectUtil
										.getStringFromObj(temp, tmpDimCode))) {
							td_value = ReflectUtil.getStringFromObj(temp,
									tmpMeaCode.toLowerCase());
							if (td_value == null || "".equals(td_value))
								td_value = "";
							else if (tmpMeaCode.toUpperCase().indexOf("PER") > -1)
								td_value = Arith.divPer(td_value, "100",
										StringB.toInt(temp.precision, 0));
							else
								td_value = FormatUtil.formatStr(td_value,
										StringB.toInt(temp.precision, 0), true);
						}
					}
					if ("".equals(td_value)) {
						td_value = "&nbsp;";
					}
					arrTable[i + addLen] += "<td align=\"right\" class=\"talbe-form\">"
							+ td_value + "</td>";
				}
				arrTable[i + addLen] += "</tr>";
			}
		} else {
			// 正常表格
			for (int i = 0; i < arrMea.length; i++) {
				// 是否选中checkbox
				boolean isCheck = false;
				for (int k = 0; k < userarrMea.length; k++) {
					if (arrMea[i].table_id.equals(userarrMea[k].table_id)
							&& arrMea[i].item_id.equals(userarrMea[k].item_id)
							&& arrMea[i].axis_type
									.equals(userarrMea[k].axis_type)) {
						isCheck = true;
					}
				}
				// checkbox名称
				String box = arrMea[i].table_id + "|" + arrMea[i].item_id + "|"
						+ arrMea[i].axis_type;

				String td_value = ""; // td显示的值
				String tdstyle = arrMea[i].head_style;
				arrTable[i + addLen] = "<tr>";
				arrTable[i + addLen] += "<td height=\"19\" " + tdstyle + ">";
				arrTable[i + addLen] += "<input type=\"checkbox\" id=" + box
						+ " name=" + box;
				if (isCheck) {
					arrTable[i + addLen] += " checked";
				}
				arrTable[i + addLen] += " >";
				arrTable[i + addLen] += arrMea[i].field_desc + "</td>";
				for (int j = 0; j < arrDim.length; j++) {
					String tmpMeaValue = arrMea[i].field_value; // 指标ID
					String tmpMeaCode = arrMea[i].field_code; // 指标值对应字段
					String tmpDimValue = arrDim[j].field_value; // 纬度值
					String tmpDimCode = arrDim[j].field_code.toLowerCase(); // 纬度对应字段
					for (int k = 0; tabledata != null && k < tabledata.length; k++) {
						UiFmKpiDTable temp = tabledata[k];
						if (tmpMeaValue.equals(temp.measure_id)
								&& tmpDimValue.equals(ReflectUtil
										.getStringFromObj(temp, tmpDimCode))) {
							td_value = ReflectUtil.getStringFromObj(temp,
									tmpMeaCode.toLowerCase());
							if (td_value == null || "".equals(td_value))
								td_value = "";
							else if (tmpMeaCode.toUpperCase().indexOf("PER") > -1)
								td_value = Arith.divPer(td_value, "100",
										StringB.toInt(temp.precision, 0));
							else
								td_value = FormatUtil.formatStr(td_value,
										StringB.toInt(temp.precision, 0), true);
						}
					}
					if ("".equals(td_value)) {
						td_value = "&nbsp;";
					}
					arrTable[i + addLen] += "<td align=\"right\" class=\"talbe-form\">"
							+ td_value + "</td>";
				}
				arrTable[i + addLen] += "</tr>";
			}
		}
		return arrTable;
	}

	/**
	 * 获取数据最大日期
	 * 
	 * @param table
	 * @param measure_id
	 * @return
	 * @throws AppException
	 */
	public static String getMaxDate(UiMainTableName table, String measure_id)
			throws AppException {
		String s = DateUtil.getDiffDay(1,
				DateUtil.dateToString(DateUtil.getNowDate()), "yyyy-MM-dd");
		if (measure_id == null || "".equals(measure_id))
			return s;
		String strSql = "SELECT MAX(" + table.date_code + ") FROM "
				+ table.data_table + " WHERE " + table.measure_code + " IN ("
				+ measure_id + ")";
		// System.out.println("MaxDate=" + strSql);
		String[][] tmp = WebDBUtil.execQryArray(strSql, "");
		if (tmp != null && tmp.length > 0) {
			s = tmp[0][0];
		}
		if (s == null || "".equals(s)) {
			s = DateUtil.getDiffDay(1,
					DateUtil.dateToString(DateUtil.getNowDate()), "yyyy-MM-dd");
		}
		return s;
	}

	/**
	 * 获取表格指标数据
	 * 
	 * @param ss_where
	 * @param ss_order
	 * @return
	 * @throws AppException
	 */
	private static UiFmKpiDTable[] getTableData(String select_code, String where)
			throws AppException {
		UiFmKpiDTable[] sdtmainpage = null;
		if (where == null)
			where = "";
		String ls_sql = select_code + " " + where;
		// System.out.println("mainPageTableData=" + ls_sql);
		Vector result = WebDBUtil.execQryVector(ls_sql, "");
		sdtmainpage = new UiFmKpiDTable[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			sdtmainpage[i] = new UiFmKpiDTable();
			int m = 0;
			sdtmainpage[i].day_id = (String) tempv.get(m++);
			sdtmainpage[i].area_id = (String) tempv.get(m++);
			sdtmainpage[i].svcknd_id = (String) tempv.get(m++);
			sdtmainpage[i].brand_knd = (String) tempv.get(m++);
			sdtmainpage[i].measure_id = (String) tempv.get(m++);
			sdtmainpage[i].measure_desc = (String) tempv.get(m++);
			sdtmainpage[i].unit = (String) tempv.get(m++);
			sdtmainpage[i].precision = (String) tempv.get(m++);
			sdtmainpage[i].curval = (String) tempv.get(m++);
			sdtmainpage[i].moncumval = (String) tempv.get(m++);
			sdtmainpage[i].lastweekval = (String) tempv.get(m++);
			sdtmainpage[i].recentweekavg = (String) tempv.get(m++);
			sdtmainpage[i].lastmoncumval = (String) tempv.get(m++);
			sdtmainpage[i].increaseval = (String) tempv.get(m++);
			sdtmainpage[i].increaserate = (String) tempv.get(m++);
			sdtmainpage[i].per1 = sdtmainpage[i].increaserate;
		}
		return sdtmainpage;
	}
}
