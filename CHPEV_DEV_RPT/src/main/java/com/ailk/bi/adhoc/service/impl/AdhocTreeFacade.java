package com.ailk.bi.adhoc.service.impl;

import java.util.List;

import com.ailk.bi.adhoc.dao.IAdhocTreeDAO;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.service.IAdhocTreeService;
import com.ailk.bi.adhoc.util.AdhocTreeHelper;
import com.ailk.bi.base.facade.IFacade;

/**
 * 服务门户
 * 
 * @author chunming
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class AdhocTreeFacade implements IFacade {
	private IAdhocTreeService service = null;

	public IAdhocTreeService getService() {
		return service;
	}

	public void setService(IAdhocTreeService service) {
		this.service = service;
	}

	public AdhocTreeFacade(IAdhocTreeDAO dao) {
		AdhocTreeService service = new AdhocTreeService();
		service.setTreeDao(dao);
		this.service = service;
	}

	// 贫血门户
	public List getAdhocGroupMetaHierarchyList(String hocId) {

		return service.getAdhocGroupMetaHierarchyList(hocId);
	}

	// 贫血门户
	public List getAdhocGroupMetaHierarchyList(String hocId, String role_id) {

		return service.getAdhocGroupMetaHierarchyList(hocId, role_id);
	}

	/**
	 * 条件层次
	 * 
	 * @param hoc_id
	 * @return
	 */
	public String getConditionHtmlTree(String hoc_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocConditionGroupMetaHierarchyTree(hoc_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "1");
	}

	/**
	 * 条件层次
	 * 
	 * @param hoc_id
	 * @param role_id
	 * @return
	 */
	public String getConditionHtmlTree(String hoc_id, String role_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocConditionGroupMetaHierarchyTree(hoc_id, role_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "1");
	}

	/**
	 * 维度层次
	 * 
	 * @param hoc_id
	 */
	public String getDimHtmlTree(String hoc_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocDimGroupMetaHierarchyTree(hoc_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "2");
	}

	/**
	 * 维度层次
	 * 
	 * @param hoc_id
	 */
	public String getDimHtmlTree(String hoc_id, String role_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocDimGroupMetaHierarchyTree(hoc_id, role_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "2");
	}

	/**
	 * 指标层次
	 * 
	 * @param hoc_id
	 */
	public String getMsuHtmlTree(String hoc_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocMsuGroupMetaHierarchyTree(hoc_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "3");
	}

	/**
	 * 指标层次
	 * 
	 * @param hoc_id
	 */
	public String getMsuHtmlTree(String hoc_id, String role_id) {
		UiAdhocGroupMetaTable[] conMeta = service
				.getAdhocMsuGroupMetaHierarchyTree(hoc_id, role_id);
		return AdhocTreeHelper.getGroupTreeHtml(conMeta, hoc_id, "3");
	}

}
