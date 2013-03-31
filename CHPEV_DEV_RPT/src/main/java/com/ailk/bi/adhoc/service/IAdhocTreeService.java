package com.ailk.bi.adhoc.service;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;

@SuppressWarnings({ "rawtypes" })
public interface IAdhocTreeService {

	/**
	 * 查询属性分组元数据信息(单一层次)
	 * 
	 * @param hocId
	 * @return
	 */
	List getAdhocGroupMetaHierarchyList(String hocId);

	/**
	 * 查询属性分组元数据信息(单一层次)
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	List getAdhocGroupMetaHierarchyList(String hocId, String role_id);

	/**
	 * 生成条件层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocConditionGroupMetaHierarchyTree(String hocId);

	/**
	 * 生成条件层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocConditionGroupMetaHierarchyTree(
			String hocId, String role_id);

	/**
	 * 生成纬度层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocDimGroupMetaHierarchyTree(String hocId);

	/**
	 * 生成纬度层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocDimGroupMetaHierarchyTree(String hocId,
			String role_id);

	/**
	 * 生成度量层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocMsuGroupMetaHierarchyTree(String hocId);

	/**
	 * 生成度量层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	UiAdhocGroupMetaTable[] getAdhocMsuGroupMetaHierarchyTree(String hocId,
			String role_id);

}
