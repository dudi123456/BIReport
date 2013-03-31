package com.ailk.bi.adhoc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdhocTreeDAO;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.service.IAdhocTreeService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocTreeService implements IAdhocTreeService {

	private IAdhocTreeDAO treeDao = null;

	public IAdhocTreeDAO getTreeDao() {
		return treeDao;
	}

	public void setTreeDao(IAdhocTreeDAO treeDao) {
		this.treeDao = treeDao;
	}

	/**
	 * 生成条件层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocConditionGroupMetaHierarchyTree(
			String hocId) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤条件元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("C")) {// condition
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 生成条件层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocConditionGroupMetaHierarchyTree(
			String hocId, String role_id) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId, role_id);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤条件元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("C")) {// condition
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 生成维度层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocDimGroupMetaHierarchyTree(
			String hocId) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤维度元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("D")) {// dimension
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 生成维度层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocDimGroupMetaHierarchyTree(
			String hocId, String role_id) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId, role_id);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤维度元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("D")) {// dimension
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 生成指标层次树
	 * 
	 * @param hocId
	 * 
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocMsuGroupMetaHierarchyTree(
			String hocId) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤指标元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("M")) {// measure
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 生成指标层次树
	 * 
	 * @param hocId
	 * @param role_id
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocMsuGroupMetaHierarchyTree(
			String hocId, String role_id) {
		ArrayList conList = new ArrayList();
		// 元数据集合
		ArrayList metaList = (ArrayList) treeDao
				.getAdhocGroupMetaHierarchyList(hocId, role_id);
		UiAdhocGroupMetaTable[] conMeta = (UiAdhocGroupMetaTable[]) metaList
				.toArray(new UiAdhocGroupMetaTable[metaList.size()]);
		// 过滤指标元数据
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getGroup_belong().toUpperCase().equals("M")) {// measure
				conList.add(conMeta[i]);
			}
		}
		// 深度B+树
		UiAdhocGroupMetaTable[] rootTree = getBPlusTree(conList, "0");

		// 返回
		return rootTree;
	}

	/**
	 * 取得B+层次树（层次不等规则）
	 * 
	 * @param list
	 * @return
	 */
	private static UiAdhocGroupMetaTable[] getBPlusTree(List list, String node) {

		UiAdhocGroupMetaTable[] meta = (UiAdhocGroupMetaTable[]) list
				.toArray(new UiAdhocGroupMetaTable[list.size()]);
		ArrayList root = new ArrayList();
		// root
		for (int i = 0; meta != null && i < meta.length; i++) {
			if (meta[i].getParent_group_id().equals(node)) {
				root.add(meta[i]);
			}

		}
		UiAdhocGroupMetaTable[] rootTree = (UiAdhocGroupMetaTable[]) root
				.toArray(new UiAdhocGroupMetaTable[root.size()]);
		// 遍历
		for (int i = 0; rootTree != null && i < rootTree.length; i++) {
			ArrayList tmpList = new ArrayList();
			for (int j = 0; j < meta.length; j++) {
				if (meta[j].getParent_group_id().equals(
						rootTree[i].getGroup_id())) {
					tmpList.add(meta[j]);
				}
			}
			//
			getBPlusTree(list, rootTree[i].getGroup_id());
			//
			rootTree[i].setLtree(tmpList);

		}
		Arrays.sort(rootTree);

		return rootTree;
	}

	// 贫血服务
	public List getAdhocGroupMetaHierarchyList(String hocId) {

		return treeDao.getAdhocGroupMetaHierarchyList(hocId);
	}

	public List getAdhocGroupMetaHierarchyList(String hocId, String roleId) {

		return treeDao.getAdhocGroupMetaHierarchyList(hocId, roleId);
	}
}
