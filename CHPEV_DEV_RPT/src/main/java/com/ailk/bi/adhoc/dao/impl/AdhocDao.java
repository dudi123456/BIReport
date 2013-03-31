package com.ailk.bi.adhoc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdHocDAO;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocDimMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocMsuMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocMultiConFilter;
import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.domain.UiAdhocSumInfoTable;
import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocDao implements IAdHocDAO {

	/**
	 * 提取条件属性分类
	 */
	public List getConGroupList(String hocId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT001", hocId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;
	}

	/**
	 * 提取条件属性分类
	 */
	public List getConGroupList(String hocId, String role_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT001_1", hocId, role_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;
	}

	/**
	 * 提取纬度分类
	 */
	public List getDimGroupList(String hocId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT002", hocId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;

	}

	/**
	 * 提取纬度分类
	 */
	public List getDimGroupList(String hocId, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT002_1", hocId, roleId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;

	}

	/**
	 * 提取指标属性分类
	 */
	public List getMsuGroupList(String hocId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT003", hocId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;
	}

	/**
	 * 提取指标属性分类
	 */
	public List getMsuGroupList(String hocId, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT003_1", hocId, roleId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;
	}

	/**
	 * 提取指标列表
	 */
	public List getMsuList(String groupId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT006", groupId);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setMsu_id(arr[i][1].trim());
				metaGroup.setMsu_name(arr[i][2].trim());
				metaGroup.setMsu_field(arr[i][3].trim());
				metaGroup.setUnit(arr[i][4].trim());
				metaGroup.setDigit(arr[i][5].trim());
				metaGroup.setIsduraflag(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setMsu_desc(arr[i][9].trim());// 这里是增加的
				list.add(metaGroup);
			}

		}
		return list;
	}

	/**
	 * 提取指标列表
	 */
	public List getMsuList(String groupId, String hocId, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT006_1", groupId, hocId,
				roleId);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setMsu_id(arr[i][1].trim());
				metaGroup.setMsu_name(arr[i][2].trim());
				metaGroup.setMsu_field(arr[i][3].trim());
				metaGroup.setUnit(arr[i][4].trim());
				metaGroup.setDigit(arr[i][5].trim());
				metaGroup.setIsduraflag(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setMsu_desc(arr[i][9].trim());// 这里是增加的
				list.add(metaGroup);
			}

		}
		return list;
	}

	/**
	 * 提取纬度列表
	 */
	public List getDimList(String groupId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT005", groupId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setDim_id(arr[i][1].trim());
				metaGroup.setDim_name(arr[i][2].trim());
				metaGroup.setDim_field_name(arr[i][3].trim());
				metaGroup.setDim_field_desc(arr[i][4].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDim_relation_field(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;

	}

	/**
	 * 提取纬度列表
	 */
	public List getDimList(String groupId, String adhoc_id, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT005_1", groupId,
				adhoc_id, roleId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setDim_id(arr[i][1].trim());
				metaGroup.setDim_name(arr[i][2].trim());
				metaGroup.setDim_field_name(arr[i][3].trim());
				metaGroup.setDim_field_desc(arr[i][4].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDim_relation_field(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;

	}

	/**
	 * 提取条件列表
	 */
	public List getConditionList(String groupId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT004", groupId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setCon_id(arr[i][1].trim());
				metaGroup.setCon_name(arr[i][2].trim());
				metaGroup.setCon_type(arr[i][3].trim());
				metaGroup.setCon_rule(arr[i][4].trim());
				metaGroup.setCon_tag(arr[i][5].trim());
				metaGroup.setQry_name(arr[i][6].trim());
				metaGroup.setFiled_name(arr[i][7].trim());
				metaGroup.setStatus(arr[i][8].trim());
				metaGroup.setValidator(arr[i][9].trim());
				metaGroup.setSequence(arr[i][10].trim());
				metaGroup.setCon_desc(arr[i][11].trim());// 这里是增加的
				list.add(metaGroup);
			}

		}
		return list;
	}

	/**
	 * 提取条件列表
	 */
	public List getConditionList(String groupId, String adhoc_id, String role_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT004_1", groupId,
				adhoc_id, role_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setCon_id(arr[i][1].trim());
				metaGroup.setCon_name(arr[i][2].trim());
				metaGroup.setCon_type(arr[i][3].trim());
				metaGroup.setCon_rule(arr[i][4].trim());
				metaGroup.setCon_tag(arr[i][5].trim());
				metaGroup.setQry_name(arr[i][6].trim());
				metaGroup.setFiled_name(arr[i][7].trim());
				metaGroup.setStatus(arr[i][8].trim());
				metaGroup.setValidator(arr[i][9].trim());
				metaGroup.setSequence(arr[i][10].trim());
				metaGroup.setCon_desc(arr[i][11].trim());// 这里是增加的
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultConList(String hoc_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT007", hoc_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setCon_id(arr[i][1].trim());
				metaGroup.setCon_name(arr[i][2].trim());
				metaGroup.setCon_type(arr[i][3].trim());
				metaGroup.setCon_rule(arr[i][4].trim());
				metaGroup.setCon_tag(arr[i][5].trim());
				metaGroup.setQry_name(arr[i][6].trim());
				metaGroup.setFiled_name(arr[i][7].trim());
				metaGroup.setStatus(arr[i][8].trim());
				metaGroup.setValidator(arr[i][9].trim());
				metaGroup.setSequence(arr[i][10].trim());
				metaGroup.setCon_desc(arr[i][11].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultConList(String hoc_id, String role_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT007_1", hoc_id, role_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setCon_id(arr[i][1].trim());
				metaGroup.setCon_name(arr[i][2].trim());
				metaGroup.setCon_type(arr[i][3].trim());
				metaGroup.setCon_rule(arr[i][4].trim());
				metaGroup.setCon_tag(arr[i][5].trim());
				metaGroup.setQry_name(arr[i][6].trim());
				metaGroup.setFiled_name(arr[i][7].trim());
				metaGroup.setStatus(arr[i][8].trim());
				metaGroup.setValidator(arr[i][9].trim());
				metaGroup.setSequence(arr[i][10].trim());
				metaGroup.setCon_desc(arr[i][11].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultDimList(String hoc_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT008", hoc_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setDim_id(arr[i][1].trim());
				metaGroup.setDim_name(arr[i][2].trim());
				metaGroup.setDim_field_name(arr[i][3].trim());
				metaGroup.setDim_field_desc(arr[i][4].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDim_relation_field(arr[i][9].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultDimList(String hoc_id, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT008_1", hoc_id, roleId);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setDim_id(arr[i][1].trim());
				metaGroup.setDim_name(arr[i][2].trim());
				metaGroup.setDim_field_name(arr[i][3].trim());
				metaGroup.setDim_field_desc(arr[i][4].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDim_relation_field(arr[i][9].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultMsuList(String hoc_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT009", hoc_id);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setMsu_id(arr[i][1].trim());
				metaGroup.setMsu_name(arr[i][2].trim());
				metaGroup.setMsu_field(arr[i][3].trim());
				metaGroup.setUnit(arr[i][4].trim());
				metaGroup.setDigit(arr[i][5].trim());
				metaGroup.setIsduraflag(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDefaultMsuList(String hoc_id, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT009_1", hoc_id, roleId);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setGroup_id(arr[i][0].trim());
				metaGroup.setMsu_id(arr[i][1].trim());
				metaGroup.setMsu_name(arr[i][2].trim());
				metaGroup.setMsu_field(arr[i][3].trim());
				metaGroup.setUnit(arr[i][4].trim());
				metaGroup.setDigit(arr[i][5].trim());
				metaGroup.setIsduraflag(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setSequence(arr[i][8].trim());
				metaGroup.setDefault(true);
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getConListByString(String condition_str) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT010", condition_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setCon_id(arr[i][0].trim());
				metaGroup.setCon_name(arr[i][1].trim());
				metaGroup.setCon_type(arr[i][2].trim());
				metaGroup.setCon_rule(arr[i][3].trim());
				metaGroup.setCon_tag(arr[i][4].trim());
				metaGroup.setQry_name(arr[i][5].trim());
				metaGroup.setFiled_name(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setValidator(arr[i][8].trim());
				metaGroup.setCon_desc(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getConListByString(String condition_str, String hocId,
			String roleId) {

		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT010_1", hocId, roleId,
				condition_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
				metaGroup.setCon_id(arr[i][0].trim());
				metaGroup.setCon_name(arr[i][1].trim());
				metaGroup.setCon_type(arr[i][2].trim());
				metaGroup.setCon_rule(arr[i][3].trim());
				metaGroup.setCon_tag(arr[i][4].trim());
				metaGroup.setQry_name(arr[i][5].trim());
				metaGroup.setFiled_name(arr[i][6].trim());
				metaGroup.setStatus(arr[i][7].trim());
				metaGroup.setValidator(arr[i][8].trim());
				metaGroup.setCon_desc(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDimListByString(String dim_str) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT011", dim_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setDim_id(arr[i][0].trim());
				metaGroup.setDim_name(arr[i][1].trim());
				metaGroup.setDim_field_name(arr[i][2].trim());
				metaGroup.setDim_field_desc(arr[i][3].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][4].trim());
				metaGroup.setStatus(arr[i][6].trim());
				metaGroup.setDim_relation_field(arr[i][7].trim());
				metaGroup.setDim_desc(arr[i][8].trim());
				metaGroup.setDim_expand_flag(arr[i][9].trim());
				metaGroup.setDim_expand_group(arr[i][10].trim());
				metaGroup.setSequence(arr[i][11].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDimListByString(String dim_str, String hocId, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT011_1", hocId, roleId,
				dim_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setDim_id(arr[i][0].trim());
				metaGroup.setDim_name(arr[i][1].trim());
				metaGroup.setDim_field_name(arr[i][2].trim());
				metaGroup.setDim_field_desc(arr[i][3].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][4].trim());
				metaGroup.setStatus(arr[i][6].trim());
				metaGroup.setDim_relation_field(arr[i][7].trim());
				metaGroup.setDim_desc(arr[i][8].trim());
				metaGroup.setDim_expand_flag(arr[i][9].trim());
				metaGroup.setDim_expand_group(arr[i][10].trim());
				metaGroup.setSequence(arr[i][11].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getDimListByWhereString(String whereStr) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT011A", whereStr);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocDimMetaTable metaGroup = new UiAdhocDimMetaTable();
				metaGroup.setDim_id(arr[i][0].trim());
				metaGroup.setDim_name(arr[i][1].trim());
				metaGroup.setDim_field_name(arr[i][2].trim());
				metaGroup.setDim_field_desc(arr[i][3].trim());
				metaGroup.setDim_desc(arr[i][5].trim());
				metaGroup.setDim_map_code(arr[i][4].trim());
				metaGroup.setStatus(arr[i][6].trim());
				metaGroup.setDim_relation_field(arr[i][7].trim());
				metaGroup.setDim_desc(arr[i][8].trim());
				metaGroup.setDim_expand_flag(arr[i][9].trim());
				metaGroup.setDim_expand_group(arr[i][10].trim());
				metaGroup.setSequence(arr[i][11].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getMsuListByString(String msu_str) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT012", msu_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setMsu_id(arr[i][0].trim());
				metaGroup.setMsu_name(arr[i][1].trim());
				metaGroup.setMsu_field(arr[i][2].trim());
				metaGroup.setUnit(arr[i][3].trim());
				metaGroup.setDigit(arr[i][4].trim());
				metaGroup.setIsduraflag(arr[i][5].trim());
				metaGroup.setStatus(arr[i][6].trim());
				metaGroup.setCal_rule(arr[i][7].trim());
				metaGroup.setCal_msu(arr[i][8].trim());
				metaGroup.setMsu_desc(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	public List getMsuListByString(String msu_str, String hocId, String role_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT012_1", hocId, role_id,
				msu_str);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMsuMetaTable metaGroup = new UiAdhocMsuMetaTable();
				metaGroup.setMsu_id(arr[i][0].trim());
				metaGroup.setMsu_name(arr[i][1].trim());
				metaGroup.setMsu_field(arr[i][2].trim());
				metaGroup.setUnit(arr[i][3].trim());
				metaGroup.setDigit(arr[i][4].trim());
				metaGroup.setIsduraflag(arr[i][5].trim());
				metaGroup.setStatus(arr[i][6].trim());
				metaGroup.setCal_rule(arr[i][7].trim());
				metaGroup.setCal_msu(arr[i][8].trim());
				metaGroup.setMsu_desc(arr[i][9].trim());
				list.add(metaGroup);
			}

		}
		return list;
	}

	/**
	 * 查询特定条件元数据
	 * 
	 */
	public UiAdhocConditionMetaTable getConditionMetaInfo(String con_id) {
		UiAdhocConditionMetaTable metaGroup = new UiAdhocConditionMetaTable();
		String[][] arr = AdhocUtil
				.queryArrayFacade("QT010", "'" + con_id + "'");
		if (arr != null && arr.length > 0) {
			metaGroup.setCon_id(arr[0][0].trim());
			metaGroup.setCon_name(arr[0][1].trim());
			metaGroup.setCon_type(arr[0][2].trim());
			metaGroup.setCon_rule(arr[0][3].trim());
			metaGroup.setCon_tag(arr[0][4].trim());
			metaGroup.setQry_name(arr[0][5].trim());
			metaGroup.setFiled_name(arr[0][6].trim());
			metaGroup.setStatus(arr[0][7].trim());
			metaGroup.setValidator(arr[0][8].trim());
		}
		return metaGroup;
	}

	/**
	 * 查询即席查询的信息
	 */
	public UiAdhocInfoDefTable getAdhocInfo(String hoc_id) {
		UiAdhocInfoDefTable hocInfo = new UiAdhocInfoDefTable();
		String[][] arr = AdhocUtil.queryArrayFacade("QT025", hoc_id);
		if (arr != null && arr.length > 0) {

			hocInfo.setAdhoc_id(arr[0][0].trim());
			hocInfo.setAdhoc_name(arr[0][1].trim());
			hocInfo.setAdhoc_desc(arr[0][2].trim());
			hocInfo.setData_table(arr[0][3].trim());
			hocInfo.setStatus(arr[0][4].trim());
			hocInfo.setIsGroup(Integer.parseInt(arr[0][5].trim()));
			hocInfo.setIsSplit(StringB.toInt(arr[0][6], 0));
			hocInfo.setPrefixTbl(StringB.NulltoBlank(arr[0][7]));
			hocInfo.setAccountNum(StringB.toInt(arr[0][8], 0));
			hocInfo.setConId(StringB.NulltoBlank(arr[0][9]));
			hocInfo.setAdhocType(StringB.toInt(arr[0][10], 0));

		}
		return hocInfo;
	}

	/**
	 * 查询即席查询视图汇总信息
	 */
	public List getAdhocSumInfo(String hoc_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT026", hoc_id);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocSumInfoTable sumInfo = new UiAdhocSumInfoTable();
				sumInfo.setAdhoc_id(arr[i][0].trim());
				sumInfo.setMsu_name(arr[i][1].trim());
				sumInfo.setMsu_filed(arr[i][2].trim());
				sumInfo.setUnit(arr[i][3].trim());
				sumInfo.setDigit(arr[i][4].trim());
				sumInfo.setStatus(arr[i][5].trim());
				sumInfo.setSequence(arr[i][6].trim());
				sumInfo.setCal_rule(arr[i][7].trim());
				sumInfo.setData_source(arr[i][8].trim());
				list.add(sumInfo);
			}

		}
		return list;

	}

	/**
	 * 查询即席查询属性分组信息
	 */
	public UiAdhocGroupMetaTable getAdhocGroupMetaInfo(String group_tag) {
		UiAdhocGroupMetaTable hocInfo = new UiAdhocGroupMetaTable();
		String[][] arr = AdhocUtil.queryArrayFacade("QT031", group_tag);
		if (arr != null && arr.length > 0) {
			hocInfo.setGroup_id(arr[0][0].trim());
			hocInfo.setGroup_name(arr[0][1].trim());
			hocInfo.setGroup_desc(arr[0][2].trim());
			hocInfo.setGroup_belong(arr[0][3].trim());
			hocInfo.setIsshow(arr[0][4].trim());
			hocInfo.setGroup_tag(arr[0][5].trim());
		}
		return hocInfo;
	}

	/**
	 * 即席查询纬度定义MAP标识
	 */
	public String getDimMapCode(String hoc_id, String relateCol) {
		String value = "";
		String[][] arr = AdhocUtil.queryArrayFacade("QT032", hoc_id, relateCol);
		if (arr != null && arr.length > 0) {
			value = arr[0][0];
		}
		return value;
	}

	/**
	 * 提取用户自定义纬度列表清单
	 */
	public List getAdhocUserDimList(String hoc_id, String oper_no,
			String col_field) {

		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT033", hoc_id, oper_no,
				col_field);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocRuleUserDimTable sumInfo = new UiAdhocRuleUserDimTable();
				sumInfo.setDim_relation_field(arr[i][0].trim());
				sumInfo.setSelf_dim_id(arr[i][1].trim());
				sumInfo.setSelf_dim_name(arr[i][2].trim());
				sumInfo.setLow_value(arr[i][3].trim());
				sumInfo.setHign_value(arr[i][4].trim());
				list.add(sumInfo);
			}

		}
		return list;
	}

	/**
	 * 保存用户自定义纬度值
	 */
	public int saveAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value, String sequence) {
		int i = -1;
		String arr[] = { hoc_id, oper_no, col_field, lowValue, hignVlaue,
				dim_id, dim_value, sequence, "1" };
		try {
			String sql = SQLGenator.genSQL("QT034", arr);
			// System.out.println("QT034================"+sql);
			i = WebDBUtil.execUpdate(sql);

		} catch (AppException ex1) {
			//
			i = -1;
			ex1.printStackTrace();
		}

		return i;
	}

	/**
	 * 删除用户自定义纬度值
	 */
	public int deleteAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value) {
		int i = -1;

		String sql = "";
		try {

			if (lowValue == null || "".equals(lowValue)) {
				String arr[] = { hoc_id, oper_no, col_field, hignVlaue, dim_id,
						dim_value };
				sql = SQLGenator.genSQL("QT037", arr);
			} else if (hignVlaue == null || "".equals(hignVlaue)) {
				String arr[] = { hoc_id, oper_no, col_field, lowValue, dim_id,
						dim_value };
				sql = SQLGenator.genSQL("QT038", arr);
			} else {
				String arr[] = { hoc_id, oper_no, col_field, lowValue,
						hignVlaue, dim_id, dim_value };
				sql = SQLGenator.genSQL("QT035", arr);
			}

			// System.out.println("deleteAdhocUserDimInfo================"+sql);
			i = WebDBUtil.execUpdate(sql);

		} catch (AppException ex1) {
			//
			i = -1;
			ex1.printStackTrace();
		}
		return i;
	}

	/**
	 * 用户清单配置信息
	 */
	public List getAdhocUserListDefine(String hoc_id, String oper_no) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT036", hoc_id, oper_no);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocUserListTable Info = new UiAdhocUserListTable();
				Info.setAdhoc_id(arr[i][0].trim());
				Info.setOper_no(arr[i][1].trim());
				Info.setMsu_field(arr[i][2].trim());
				Info.setMsu_name(arr[i][3].trim());
				Info.setMsu_type(arr[i][4].trim());
				Info.setMsu_unit(arr[i][5].trim());
				Info.setMsu_digit(arr[i][6].trim());
				Info.setSequence(arr[i][7].trim());
				Info.setStatus(arr[i][8].trim());
				Info.setMap_code(arr[i][9].trim());
				Info.setDefault_view(arr[i][10].trim());
				Info.setGroup_Name(arr[i][11].trim());
				list.add(Info);
			}

		}
		return list;
	}

	/**
	 * 用户清单配置信息
	 */
	public List getAdhocUserListDefine(String hoc_id, String oper_no,
			String favId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT036_Collect", favId,
				hoc_id, oper_no);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocUserListTable Info = new UiAdhocUserListTable();
				Info.setAdhoc_id(arr[i][0].trim());
				Info.setOper_no(arr[i][1].trim());
				Info.setMsu_field(arr[i][2].trim());
				Info.setMsu_name(arr[i][3].trim());
				Info.setMsu_type(arr[i][4].trim());
				Info.setMsu_unit(arr[i][5].trim());
				Info.setMsu_digit(arr[i][6].trim());
				Info.setSequence(arr[i][7].trim());
				Info.setStatus(arr[i][8].trim());
				Info.setMap_code(arr[i][9].trim());
				Info.setDefault_view(arr[i][10].trim());
				Info.setGroup_Name(arr[i][11].trim());

				list.add(Info);
			}

		}
		return list;
	}

	/**
	 * 默认用户清单配置信息
	 */
	public List getAdhocUserListDefineDefault(String hoc_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT036", hoc_id, "-1");
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocUserListTable Info = new UiAdhocUserListTable();
				Info.setAdhoc_id(arr[i][0].trim());
				Info.setOper_no(arr[i][1].trim());
				Info.setMsu_field(arr[i][2].trim());
				Info.setMsu_name(arr[i][3].trim());
				Info.setMsu_type(arr[i][4].trim());
				Info.setMsu_unit(arr[i][5].trim());
				Info.setMsu_digit(arr[i][6].trim());
				Info.setSequence(arr[i][7].trim());
				Info.setStatus(arr[i][8].trim());
				Info.setMap_code(arr[i][9].trim());
				Info.setDefault_view(arr[i][10].trim());
				Info.setGroup_Name(arr[i][11].trim());

				list.add(Info);
			}

		}
		return list;
	}

	/**
	 * 默认用户清单配置信息
	 */
	public List getAdhocUserListDefineDefault(String hoc_id, String roleId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT036_1", hoc_id, roleId);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocUserListTable Info = new UiAdhocUserListTable();
				Info.setAdhoc_id(arr[i][0].trim());
				Info.setOper_no(arr[i][1].trim());
				Info.setMsu_field(arr[i][2].trim());
				Info.setMsu_name(arr[i][3].trim());
				Info.setMsu_type(arr[i][4].trim());
				Info.setMsu_unit(arr[i][5].trim());
				Info.setMsu_digit(arr[i][6].trim());
				Info.setSequence(arr[i][7].trim());
				Info.setStatus(arr[i][8].trim());
				Info.setMap_code(arr[i][9].trim());
				Info.setDefault_view(arr[i][10].trim());
				Info.setGroup_Name(arr[i][11].trim());

				list.add(Info);
			}

		}
		return list;
	}

	public List getAdhocMultiSelectCondition(String hoc_id, String con_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT039", hoc_id, con_id);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocMultiConFilter Info = new UiAdhocMultiConFilter();
				Info.setAdhoc_id(arr[i][0].trim());
				Info.setCon_id(arr[i][1].trim());
				Info.setFilter_type(arr[i][2].trim());
				Info.setFilter_con_id(arr[i][3].trim());
				Info.setFilter_qry_name(arr[i][4].trim());
				Info.setFilter_con_str(arr[i][5].trim());
				Info.setSecurity_type(arr[i][6].trim());
				Info.setColumn_type(arr[i][7].trim());
				list.add(Info);
			}

		}
		return list;
	}

	/**
	 * 查询属性分组的层次关系
	 * 
	 */
	public List getAdhocGroupPathInfo(String group_tag) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT042", group_tag);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				UiAdhocGroupMetaTable hocGroup = new UiAdhocGroupMetaTable();
				hocGroup.setGroup_id(arr[i][0].trim());
				hocGroup.setParent_group_id(arr[i][1].trim());
				hocGroup.setGroup_name(arr[i][2].trim());
				hocGroup.setGroup_desc(arr[i][3].trim());
				hocGroup.setGroup_belong(arr[i][4].trim());
				hocGroup.setIsshow(arr[i][5].trim());
				hocGroup.setGroup_tag(arr[i][6].trim());
				hocGroup.setSequence(Integer.parseInt(arr[i][7].trim()));
				list.add(hocGroup);
			}

		}
		return list;
	}

}
