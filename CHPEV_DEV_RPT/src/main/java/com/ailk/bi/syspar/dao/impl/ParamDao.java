package com.ailk.bi.syspar.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.syspar.dao.IParamDAO;
import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;
import com.ailk.bi.syspar.util.ParamUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParamDao implements IParamDAO {
	/**
	 * 查询配置参数表元数据信息列表
	 *
	 * @param node_id
	 * @return
	 */
	public List getParamConfigMetaByNodeID(String param_id) {
		ArrayList list = new ArrayList();
		String[][] arr = ParamUtil.queryArrayFacade("QT132", param_id);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiParamMetaConfigTable confMeta = new UiParamMetaConfigTable();
				confMeta.setParam_id(arr[i][0].trim());
				confMeta.setColumn_cn_name(arr[i][1].trim());
				confMeta.setColumn_en_name(arr[i][2].trim());
				confMeta.setColumn_data_type(arr[i][3].trim());
				confMeta.setUnique_index(arr[i][4].trim());
				confMeta.setColumn_show_type(arr[i][5].trim());
				confMeta.setColumn_show_rule(arr[i][6].trim());
				confMeta.setSequence(arr[i][7].trim());
				confMeta.setColumn_format(arr[i][8].trim());
				confMeta.setStatus(arr[i][9].trim());
				confMeta.setColumn_event(arr[i][10].trim());
				confMeta.setColumn_data_digit(arr[i][11].trim());
				confMeta.setColumn_length(arr[i][12].trim());
				list.add(confMeta);
			}

		}
		return list;
	}

	/**
	 * 查询参数配置节点配置表信息
	 */
	public UiParamInfoConfigTable getParamConfigTreeMetaByNodeID(String param_id) {

		UiParamInfoConfigTable confMeta = new UiParamInfoConfigTable();
		String[][] arr = ParamUtil.queryArrayFacade("QT133", param_id);

		if (arr != null && arr.length > 0) {
			confMeta.setParam_id(arr[0][0].trim());
			confMeta.setParent_id(arr[0][1].trim());
			confMeta.setParam_name(arr[0][2].trim());
			confMeta.setParam_desc(arr[0][3].trim());
			confMeta.setTable_name(arr[0][4].trim());
			confMeta.setLog_table_name(arr[0][5].trim());
			confMeta.setStatus(arr[0][6].trim());
			confMeta.setIsleaf(arr[0][7].trim());
			confMeta.setIsdimtable(arr[0][8].trim());
			confMeta.setDefault_tag(arr[0][9].trim());

		}
		return confMeta;
	}

	public String[][] getParamLogInfoByNodeID(
			UiParamInfoConfigTable configInfo,
			UiParamMetaConfigTable[] metaInfo, String whereStr) {

		String[][] arr = null;
		// 字段
		String tableFiledStr = "";
		for (int i = 0; metaInfo != null && i < metaInfo.length; i++) {
			if (tableFiledStr.length() > 0) {
				tableFiledStr += ",";
			}
			tableFiledStr += metaInfo[i].getColumn_en_name();
		}
		// sql
		String sql = "SELECT SERVICE_SN,LOG_SEQ,OPER_OPER_NO,OPER_IP,OPER_TIME,OPER_TYPE,"
				+ tableFiledStr
				+ " FROM "
				+ configInfo.getLog_table_name()
				+ " where 1=1 ";
		// 条件
		if (whereStr.length() > 0) {
			sql += whereStr;
		}
		System.out.println("log sql="+sql);
		arr = ParamUtil.queryArrayFacade(sql);
		return arr;
	}

}
