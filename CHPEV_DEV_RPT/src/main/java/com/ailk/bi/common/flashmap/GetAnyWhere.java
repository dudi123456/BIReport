package com.ailk.bi.common.flashmap;

import java.util.Vector;
//import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public class GetAnyWhere {

	private static String char_data = TableConsts.DATA_TYPE_STRING;

	private static String num_data = TableConsts.DATA_TYPE_NUMER;

	public GetAnyWhere() {
	}

	/**
	 * 根据条件ID得到条件的构造内容
	 *
	 * @param res_id
	 * @param res_type
	 * @return
	 * @throws AppException
	 */
	public static PubInfoConditionTable[] genCondition(String res_id,
			String res_type) throws AppException {
		PubInfoConditionTable[] conditions = null;
		String strSql = SQLGenator.genSQL("TQ3006", res_type, res_id);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result != null && result.size() > 0) {
			conditions = new PubInfoConditionTable[result.size()];
			int m = 0;
			for (int i = 0; i < conditions.length; i++) {
				Vector tempv = (Vector) result.get(i);
				conditions[i] = new PubInfoConditionTable();
				m = 0;
				conditions[i].res_id = (String) tempv.get(m++);
				conditions[i].res_type = (String) tempv.get(m++);
				conditions[i].qry_type = (String) tempv.get(m++);
				conditions[i].qry_code = (String) tempv.get(m++);
				conditions[i].con_code = (String) tempv.get(m++);
				conditions[i].data_type = (String) tempv.get(m++);
				conditions[i].con_tag = (String) tempv.get(m++);
			}
		}
		return conditions;
	}

	/**
	 * 根据条件构造内容以及条件实体，构造真实的where语句
	 *
	 * @param conTable
	 * @param qryStruct
	 * @return
	 */
	public static String getPubWhere(PubInfoConditionTable[] conTable,
			Object qryStruct) {
		String where = "";
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			String tmpCode = conTable[i].qry_code.toLowerCase();
			String tmpValue = "";
			String data_type = conTable[i].data_type;
			String con_code = conTable[i].con_code;
			String con_tag = conTable[i].con_tag;
			tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
			where += genWherePart(tmpValue, data_type, con_code, con_tag);
		}
		return where;
	}

	/**
	 * 根据传入的条件值，条件类型，条件字段，条件操作符构造SQL——where语句
	 *
	 * @param value
	 * @param data_type
	 * @param con_code
	 * @param con_tag
	 * @return
	 */
	public static String genWherePart(String value, String data_type,
			String con_code, String con_tag) {
		String where = "";
		con_tag = con_tag.toLowerCase();
		if (value != null && !"".equals(value.trim())) {
			if (con_code == null || "".equals(con_code.trim())) {
				where = " ";
			} else {
				where += " AND ";
			}
			if ("in".equals(con_tag)) {
				where += con_code;
				where += " " + con_tag + "(";
			} else if ("like_l".equals(con_tag)) {
				where += con_code;
				where += " like";
			} else if ("like_r".equals(con_tag)) {
				where += con_code;
				where += " like";
			} else {
				where += con_code;
				where += " " + con_tag;
			}

			if (char_data.equals(data_type)) {
				if ("in".equals(con_tag)) {
					value = StringB.replace(value, ":add:", ",");
					value = StringTool.changArrToStr(value.split(","), "'");
					where += value;
				} else if ("like".equals(con_tag)) {
					where += " '%" + value + "%'";
				} else if ("like_l".equals(con_tag)) {
					where += " '%" + value + "'";
				} else if ("like_r".equals(con_tag)) {
					where += " '" + value + "%'";
				} else {
					where += "'" + value + "'";
				}
			}
			if (num_data.equals(data_type)) {
				if ("in".equals(con_tag)) {
					value = StringB.replace(value, ":add:", ",");
					value = StringTool.changArrToStr(value.split(","), "");
					where += value;
				} else if ("like".equals(con_tag)) {
					where += " '%" + value + "%'";
				} else if ("like_l".equals(con_tag)) {
					where += " '%" + value + "'";
				} else if ("like_r".equals(con_tag)) {
					where += " '" + value + "%'";
				} else {
					where += value;
				}
			}

			if ("in".equals(con_tag)) {
				where += ")";
			}

			if ("AND".equals(where.trim())) {
				where = "";
			}
		}
		return where;
	}

}
