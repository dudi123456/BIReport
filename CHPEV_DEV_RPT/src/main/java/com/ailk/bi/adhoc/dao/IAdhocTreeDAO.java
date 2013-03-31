package com.ailk.bi.adhoc.dao;

import java.util.List;

@SuppressWarnings({ "rawtypes" })
public interface IAdhocTreeDAO {
	// 得到层次树各节点数据
	List getAdhocGroupMetaHierarchyList(String hocId);

	List getAdhocGroupMetaHierarchyList(String hocId, String role_id);

}
