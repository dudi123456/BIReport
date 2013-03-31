package com.ailk.bi.adhoc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdhocTreeDAO;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.util.AdhocUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocTreeDao implements IAdhocTreeDAO {

	/**
	 * 查询条件，纬度，度量所有分组的层次对象数祖
	 * 
	 */
	public List getAdhocGroupMetaHierarchyList(String hocId) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT041", hocId);
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

	public List getAdhocGroupMetaHierarchyList(String hocId, String roleId) {

		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT041_1", hocId, roleId);
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
