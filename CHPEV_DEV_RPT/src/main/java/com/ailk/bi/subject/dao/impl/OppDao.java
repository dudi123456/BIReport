package com.ailk.bi.subject.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.subject.dao.IOppDao;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.subject.struct.OppParamStruct;
import com.ailk.bi.subject.struct.OppParamValueStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OppDao implements IOppDao {

	public List getOppParamList() {

		List list = new ArrayList();
		String[][] value = CommTool.queryArrayFacade("OPP1001");
		for (int i = 0; value != null && i < value.length; i++) {
			OppParamStruct struct = new OppParamStruct();
			struct.setParam_type(value[i][0]);
			struct.setParam_id(value[i][1]);
			struct.setParam_name(value[i][2]);
			struct.setParam_desc(value[i][3]);
			struct.setParam_rule(value[i][4]);
			struct.setParam_weight(value[i][5]);
			struct.setParam_status(value[i][6]);
			struct.setValueObjs(getOppParamValueList(struct.getParam_id()));
			list.add(struct);
		}

		return list;
	}

	public List getOppParamValueList(String param_id) {

		List list = new ArrayList();
		String[][] value = CommTool.queryArrayFacade("OPP1002", param_id);
		for (int i = 0; value != null && i < value.length; i++) {
			OppParamValueStruct struct = new OppParamValueStruct();
			struct.setParam_type(value[i][0]);
			struct.setParam_id(value[i][1]);
			struct.setLvl_id(value[i][2]);
			struct.setLvl_name(value[i][3]);
			struct.setStart_val(value[i][4]);
			struct.setEnd_val(value[i][5]);
			list.add(struct);
		}

		return list;
	}

	public int addNewParam(String[] value) {

		int i = -1;
		try {
			String sql = SQLGenator.genSQL("OPP1003", value);
			System.out.println("OPP1003===========" + sql);
			i = WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public int updateParam(String[] value) {

		int i = -1;
		try {
			String sql = SQLGenator.genSQL("OPP1004", value);
			System.out.println("OPP1004===========" + sql);
			i = WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public int deleteParam(String param_id) {

		int i = -1;
		try {
			String sql_a = SQLGenator.genSQL("OPP1005", param_id);
			System.out.println("OPP1005===========" + sql_a);

			String sql_b = SQLGenator.genSQL("OPP1006", param_id);
			System.out.println("OPP1006===========" + sql_b);
			String[] sqlArr = { sql_a, sql_b };
			i = 0;
			i = WebDBUtil.execTransUpdate(sqlArr);

		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public List getOppParam(String param_id) {

		List list = new ArrayList();
		String[][] value = CommTool.queryArrayFacade("OPP1007", param_id);
		for (int i = 0; value != null && i < value.length; i++) {
			OppParamStruct struct = new OppParamStruct();
			struct.setParam_type(value[i][0]);
			struct.setParam_id(value[i][1]);
			struct.setParam_name(value[i][2]);
			struct.setParam_desc(value[i][3]);
			struct.setParam_rule(value[i][4]);
			struct.setParam_weight(value[i][5]);
			struct.setParam_status(value[i][6]);
			struct.setValueObjs(getOppParamValueList(struct.getParam_id()));
			list.add(struct);
		}

		return list;
	}

	public int addNewParamLvl(String[] value) {

		int i = -1;
		try {
			String sql = SQLGenator.genSQL("OPP1008", value);
			System.out.println("OPP1008===========" + sql);
			i = WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public int deleteParamLvl(String whereStr) {

		int i = -1;
		try {
			String sql_a = SQLGenator.genSQL("OPP1005", whereStr);
			System.out.println("OPP1005===========" + sql_a);
			i = WebDBUtil.execUpdate(sql_a);

		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public int updateParamLvl(String[] value) {
		int i = -1;
		try {
			String sql = SQLGenator.genSQL("OPP1009", value);
			System.out.println("OPP1009===========" + sql);
			i = WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			i = -1;
			e.printStackTrace();
		}

		return i;
	}

	public List getOppParamList(String param_type) {

		List list = new ArrayList();
		String[][] value = CommTool.queryArrayFacade("OPP1010", param_type);
		for (int i = 0; value != null && i < value.length; i++) {
			OppParamStruct struct = new OppParamStruct();
			struct.setParam_type(value[i][0]);
			struct.setParam_id(value[i][1]);
			struct.setParam_name(value[i][2]);
			struct.setParam_desc(value[i][3]);
			struct.setParam_rule(value[i][4]);
			struct.setParam_weight(value[i][5]);
			struct.setParam_status(value[i][6]);
			struct.setValueObjs(getOppParamValueList(struct.getParam_id()));
			list.add(struct);
		}

		return list;

	}

}
