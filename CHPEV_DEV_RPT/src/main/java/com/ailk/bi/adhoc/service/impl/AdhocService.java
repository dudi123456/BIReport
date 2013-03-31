package com.ailk.bi.adhoc.service.impl;

import java.util.List;

import com.ailk.bi.adhoc.dao.IAdHocDAO;
import com.ailk.bi.adhoc.dao.IAdHocFavDAO;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.service.IAdhocService;

/**
 * 即席查询服务定义类
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class AdhocService implements IAdhocService {

	// 即席查询
	private IAdHocDAO hocDAO = null;

	// 收藏夹
	private IAdHocFavDAO favDAO = null;

	/**
	 * 提取条件定制属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	public List getConGroupListByHocId(String hoc_id) {

		List list = hocDAO.getConGroupList(hoc_id);
		return list;

	}

	public List getConGroupListByHocId(String hocId, String roleId) {
		List list = hocDAO.getConGroupList(hocId, roleId);
		return list;
	}

	/**
	 * 提取结果定制纬度属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	public List getDimGroupListByHocId(String hoc_id) {

		List list = hocDAO.getDimGroupList(hoc_id);
		return list;

	}

	/**
	 * 提取结果定制纬度属性分组
	 * 
	 * @param hoc_id
	 * @param roleId
	 * @return
	 */
	public List getDimGroupListByHocId(String hoc_id, String roleId) {

		List list = hocDAO.getDimGroupList(hoc_id, roleId);
		return list;

	}

	/**
	 * 提取结果定制指标属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	public List getMsuGroupListByHocId(String hoc_id) {

		List list = hocDAO.getMsuGroupList(hoc_id);
		return list;

	}

	/**
	 * 提取结果定制指标属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	public List getMsuGroupListByHocId(String hoc_id, String roleId) {

		List list = hocDAO.getMsuGroupList(hoc_id, roleId);
		return list;

	}

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	public List getMsuListByGroupId(String group_id) {

		List list = hocDAO.getMsuList(group_id);
		return list;

	}

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	public List getMsuListByGroupId(String group_id, String hocId, String roleId) {

		List list = hocDAO.getMsuList(group_id, hocId, roleId);
		return list;

	}

	/**
	 * 提取特定纬度属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	public List getDimListByGroupId(String group_id) {

		List list = hocDAO.getDimList(group_id);
		return list;

	}

	/**
	 * 提取特定纬度属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	public List getDimListByGroupId(String group_id, String adhoc_id,
			String role_id) {

		List list = hocDAO.getDimList(group_id, adhoc_id, role_id);
		return list;

	}

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	public List getConditionListByGroupId(String group_id) {
		List list = hocDAO.getConditionList(group_id);
		return list;

	}

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @param role_id
	 * @return
	 */
	public List getConditionListByGroupId(String group_id, String adhoc_id,
			String role_id) {
		List list = hocDAO.getConditionList(group_id, adhoc_id, role_id);
		return list;

	}

	public List getDefaultConListByHocId(String hocId, String roleId) {
		List list = hocDAO.getDefaultConList(hocId, roleId);
		return list;
	}

	public List getDefaultConListByHocId(String hoc_id) {
		List list = hocDAO.getDefaultConList(hoc_id);
		return list;
	}

	public List getDefaultDimListByHocId(String hoc_id) {
		List list = hocDAO.getDefaultDimList(hoc_id);
		return list;
	}

	public List getDefaultDimListByHocId(String hoc_id, String roleId) {
		List list = hocDAO.getDefaultDimList(hoc_id, roleId);
		return list;
	}

	public List getDefaultMsuListByHocId(String hoc_id) {
		List list = hocDAO.getDefaultMsuList(hoc_id);
		return list;
	}

	public List getDefaultMsuListByHocId(String hoc_id, String roleId) {
		List list = hocDAO.getDefaultMsuList(hoc_id, roleId);
		return list;
	}

	public List getConListByString(String condition_str) {
		List list = hocDAO.getConListByString(condition_str);
		return list;
	}

	public List getConListByString(String condition_str, String hocId,
			String roleId) {
		List list = hocDAO.getConListByString(condition_str, hocId, roleId);
		return list;
	}

	public List getDimListByString(String dim_str) {
		List list = hocDAO.getDimListByString(dim_str);
		return list;
	}

	public List getDimListByString(String dim_str, String hocId, String roleId) {
		List list = hocDAO.getDimListByString(dim_str, hocId, roleId);
		return list;
	}

	public List getDimListByWhereString(String where_str) {
		List list = hocDAO.getDimListByWhereString(where_str);
		return list;
	}

	public List getMsuListByString(String msu_str) {
		List list = hocDAO.getMsuListByString(msu_str);
		return list;
	}

	public List getMsuListByString(String msu_str, String hocId, String roleId) {
		List list = hocDAO.getMsuListByString(msu_str, hocId, roleId);
		return list;
	}

	public IAdHocFavDAO getFavDAO() {
		return favDAO;
	}

	public void setFavDAO(IAdHocFavDAO favDAO) {
		this.favDAO = favDAO;
	}

	public IAdHocDAO getHocDAO() {
		return hocDAO;
	}

	public void setHocDAO(IAdHocDAO hocDAO) {
		this.hocDAO = hocDAO;
	}

	public int addNewFavorite(String favID, String favName, String fav_type,
			String conStr, String dimStr, String msuStr, String oper_no,
			String favTypeFlag) {
		return this.favDAO.addNewFavorite(favID, favName, fav_type, conStr,
				dimStr, msuStr, oper_no, favTypeFlag);
	}

	public int deleteFavorite() {

		return 0;
	}

	public List getFavoriteListByOper(String oper_no, String fav_type) {
		return favDAO.getFavoriteList(oper_no, fav_type);
	}

	public UiAdhocFavoriteDefTable getFavoriteInfo(String favId) {
		return favDAO.getFavoriteInfo(favId);
	}

	public List getFavoriteConList(String fav_id) {

		return favDAO.getFavoriteConList(fav_id);
	}

	public List getFavoriteDimList(String fav_id) {

		return favDAO.getFavoriteDimList(fav_id);
	}

	public List getFavoriteMsuList(String fav_id) {
		return favDAO.getFavoriteMsuList(fav_id);
	}

	public int deleteFavorite(String favID, String oper_no, String fav_type) {
		return favDAO.deleteFavorite(favID, oper_no, fav_type);
	}

	public UiAdhocConditionMetaTable getConditionMetaInfo(String con_id) {
		return hocDAO.getConditionMetaInfo(con_id);
	}

	public UiAdhocInfoDefTable getAdhocInfo(String hoc_id) {
		return hocDAO.getAdhocInfo(hoc_id);
	}

	public List getAdhocSumInfo(String hoc_id) {
		return hocDAO.getAdhocSumInfo(hoc_id);
	}

	public UiAdhocGroupMetaTable getAdhocGroupMetaInfo(String group_tag) {
		return hocDAO.getAdhocGroupMetaInfo(group_tag);
	}

	public String getDimMapCode(String hoc_id, String col_filed) {
		return hocDAO.getDimMapCode(hoc_id, col_filed);

	}

	public List getAdhocUserDimList(String hoc_id, String oper_no,
			String col_field) {

		return hocDAO.getAdhocUserDimList(hoc_id, oper_no, col_field);
	}

	public int saveAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value, String sequence) {

		return hocDAO.saveAdhocUserDimInfo(hoc_id, oper_no, col_field,
				lowValue, hignVlaue, dim_id, dim_value, sequence);
	}

	public int deleteAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value) {

		return hocDAO.deleteAdhocUserDimInfo(hoc_id, oper_no, col_field,
				lowValue, hignVlaue, dim_id, dim_value);
	}

	public List getAdhocUserListDefine(String hoc_id, String oper_no) {

		return hocDAO.getAdhocUserListDefine(hoc_id, oper_no);
	}

	public List getAdhocUserListDefine(String hoc_id, String oper_no,
			String favId) {

		return hocDAO.getAdhocUserListDefine(hoc_id, oper_no, favId);
	}

	public List getAdhocUserListDefineDefault(String hoc_id) {

		return hocDAO.getAdhocUserListDefineDefault(hoc_id);
	}

	public List getAdhocUserListDefineDefault(String hoc_id, String roleId) {

		return hocDAO.getAdhocUserListDefineDefault(hoc_id, roleId);
	}

	public List getAdhocMultiSelectCondition(String hoc_id, String con_id) {

		return hocDAO.getAdhocMultiSelectCondition(hoc_id, con_id);
	}

	public List getAdhocGroupPathInfo(String group_tag) {

		return hocDAO.getAdhocGroupPathInfo(group_tag);
	}

}
