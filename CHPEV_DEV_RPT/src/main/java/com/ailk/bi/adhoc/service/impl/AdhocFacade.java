package com.ailk.bi.adhoc.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdHocDAO;
import com.ailk.bi.adhoc.dao.IAdHocFavDAO;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocMultiConFilter;
import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.domain.UiAdhocSumInfoTable;
import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.service.IAdhocService;
import com.ailk.bi.base.facade.IFacade;

/**
 * 即席查询服务包装类
 * 
 * 
 * 
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocFacade implements IFacade {

	private IAdhocService hocService = null;

	public void setHocService(IAdhocService hocService) {
		this.hocService = hocService;
	}

	public AdhocFacade(IAdHocDAO dao) {
		AdhocService service = new AdhocService();
		service.setHocDAO(dao);
		this.hocService = service;
	}

	public AdhocFacade(IAdHocFavDAO dao) {
		AdhocService service = new AdhocService();
		service.setFavDAO(dao);
		this.hocService = service;
	}

	/**
	 * 条件属性分组
	 */
	public ArrayList getConGroupListByHocId(String hoc_id) {
		return (ArrayList) hocService.getConGroupListByHocId(hoc_id);
	}

	/**
	 * 条件属性分组
	 */
	public ArrayList getConGroupListByHocId(String hoc_id, String role_id) {
		return (ArrayList) hocService.getConGroupListByHocId(hoc_id, role_id);
	}

	/**
	 * 纬度属性分组
	 */
	public List getDimGroupListByHocId(String hoc_id) {
		return hocService.getDimGroupListByHocId(hoc_id);
	}

	/**
	 * 纬度属性分组
	 */
	public List getDimGroupListByHocId(String hoc_id, String role_id) {
		return hocService.getDimGroupListByHocId(hoc_id, role_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getMsuGroupListByHocId(String hoc_id) {
		return hocService.getMsuGroupListByHocId(hoc_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getMsuGroupListByHocId(String hoc_id, String role_id) {
		return hocService.getMsuGroupListByHocId(hoc_id, role_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getMsuListByGroupId(String group_id) {
		return hocService.getMsuListByGroupId(group_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getMsuListByGroupId(String group_id, String hoc_id,
			String roleId) {
		return hocService.getMsuListByGroupId(group_id, hoc_id, roleId);
	}

	/**
	 * 指标属性分组
	 */
	public List getDimListByGroupId(String group_id) {
		return hocService.getDimListByGroupId(group_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getDimListByGroupId(String group_id, String adhoc_id,
			String role_id) {
		return hocService.getDimListByGroupId(group_id, adhoc_id, role_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getConditionListByGroupId(String group_id) {
		return hocService.getConditionListByGroupId(group_id);
	}

	/**
	 * 指标属性分组
	 */
	public List getConditionListByGroupId(String group_id, String adhoc_id,
			String role_id) {
		return hocService
				.getConditionListByGroupId(group_id, adhoc_id, role_id);
	}

	/**
	 * 提取默认条件列表
	 */
	public List getDefaultConListByHocId(String hoc_id) {
		return hocService.getDefaultConListByHocId(hoc_id);
	}

	/**
	 * 提取默认条件列表
	 */
	public List getDefaultConListByHocId(String hoc_id, String role_id) {
		return hocService.getDefaultConListByHocId(hoc_id, role_id);
	}

	/**
	 * 提取默认纬度列表
	 */
	public List getDefaultDimListByHocId(String hoc_id) {
		return hocService.getDefaultDimListByHocId(hoc_id);
	}

	/**
	 * 提取默认纬度列表
	 */
	public List getDefaultDimListByHocId(String hoc_id, String role_id) {
		return hocService.getDefaultDimListByHocId(hoc_id, role_id);
	}

	/**
	 * 提取默认指标列表
	 */
	public List getDefaultMsuListByHocId(String hoc_id) {
		return hocService.getDefaultMsuListByHocId(hoc_id);
	}

	/**
	 * 提取默认指标列表
	 */
	public List getDefaultMsuListByHocId(String hoc_id, String role_id) {
		return hocService.getDefaultMsuListByHocId(hoc_id, role_id);
	}

	/**
	 * 根据条件列表查询条件详情列表
	 * 
	 * @param condition_str
	 * @return
	 */
	public List getConListByString(String condition_str) {
		List list = hocService.getConListByString(condition_str);
		return list;
	}

	/**
	 * 根据条件列表查询条件详情列表
	 * 
	 * @param condition_str
	 * @return
	 */
	public List getConListByString(String condition_str, String hocId,
			String role_id) {
		List list = hocService
				.getConListByString(condition_str, hocId, role_id);
		return list;
	}

	/**
	 * 根据纬度列表查询纬度详情列表
	 * 
	 * @param dim_str
	 * @return
	 */
	public List getDimListByString(String dim_str) {
		List list = hocService.getDimListByString(dim_str);
		return list;
	}

	/**
	 * 根据纬度列表查询纬度详情列表
	 * 
	 * @param dim_str
	 * @param role_id
	 * @return
	 */
	public List getDimListByString(String dim_str, String hocId, String role_id) {
		List list = hocService.getDimListByString(dim_str, hocId, role_id);
		return list;
	}

	/**
	 * 根据条件获取维度信息列表
	 * 
	 * @param dim_str
	 * @return
	 */
	public List getDimListByWhereString(String dim_str) {
		List list = hocService.getDimListByWhereString(dim_str);
		return list;
	}

	/**
	 * 根据指标列表查询指标详情列表
	 * 
	 * @param msu_str
	 * @return
	 */
	public List getMsuListByString(String msu_str) {
		List list = hocService.getMsuListByString(msu_str);
		return list;
	}

	/**
	 * 根据指标列表查询指标详情列表
	 * 
	 * @param msu_str
	 * @return
	 */
	public List getMsuListByString(String msu_str, String hocId, String roleId) {
		List list = hocService.getMsuListByString(msu_str, hocId, roleId);
		return list;
	}

	/**
	 * 提取用户当前业务类型收藏夹列表
	 * 
	 * @param oper_no
	 * @param fav_type
	 * @return
	 */
	public ArrayList getFavoriteListByOper(String oper_no, String fav_type) {
		return (ArrayList) hocService.getFavoriteListByOper(oper_no, fav_type);
	}

	public int addNewFavorite(String favID, String favName, String fav_type,
			String conStr, String dimStr, String msuStr, String oper_no,
			String favTypeFlag) {
		return hocService.addNewFavorite(favID, favName, fav_type, conStr,
				dimStr, msuStr, oper_no, favTypeFlag);
	}

	public UiAdhocFavoriteDefTable getFavoriteInfo(String favId) {
		return hocService.getFavoriteInfo(favId);
	}

	public List getFavoriteConList(String fav_id) {

		return hocService.getFavoriteConList(fav_id);
	}

	public List getFavoriteDimList(String fav_id) {

		return hocService.getFavoriteDimList(fav_id);
	}

	public List getFavoriteMsuList(String fav_id) {
		return hocService.getFavoriteMsuList(fav_id);
	}

	public int deleteFavorite(String favID, String oper_no, String fav_type) {
		return hocService.deleteFavorite(favID, oper_no, fav_type);
	}

	public UiAdhocConditionMetaTable getConditionMetaInfo(String con_id) {
		return hocService.getConditionMetaInfo(con_id);
	}

	public UiAdhocInfoDefTable getAdhocInfo(String hoc_id) {
		return hocService.getAdhocInfo(hoc_id);
	}

	public UiAdhocGroupMetaTable getAdhocGroupMetaInfo(String group_tag) {
		return hocService.getAdhocGroupMetaInfo(group_tag);
	}

	/**
	 * 属性层次路经
	 * 
	 * @param group_tag
	 * @return
	 */
	public UiAdhocGroupMetaTable[] getAdhocGroupPathInfo(String group_tag) {
		ArrayList tmpList = (ArrayList) hocService
				.getAdhocGroupPathInfo(group_tag);
		UiAdhocGroupMetaTable[] pathInfo = (UiAdhocGroupMetaTable[]) tmpList
				.toArray(new UiAdhocGroupMetaTable[tmpList.size()]);
		return pathInfo;
	}

	/**
	 * 取得当前即席查询汇总信息定义
	 * 
	 * @param hoc_id
	 * @return
	 */
	public UiAdhocSumInfoTable[] getAdhocSumInfo(String hoc_id) {
		ArrayList tmpList = (ArrayList) hocService.getAdhocSumInfo(hoc_id);
		UiAdhocSumInfoTable[] sumInfo = (UiAdhocSumInfoTable[]) tmpList
				.toArray(new UiAdhocSumInfoTable[tmpList.size()]);
		return sumInfo;
	}

	/**
	 * 即席查询纬度默认MAPCODE
	 * 
	 * @param hoc_id
	 * @param col_field
	 * @return
	 */
	public String getDimMapCode(String hoc_id, String col_field) {
		return hocService.getDimMapCode(hoc_id, col_field);
	}

	/**
	 * 查询用户自定义的纬度分档信息
	 * 
	 * @param hoc_id
	 * @param oper_no
	 * @param col_field
	 * @return
	 */
	public UiAdhocRuleUserDimTable[] getAdhocUserDimList(String hoc_id,
			String oper_no, String col_field) {
		ArrayList tmpList = (ArrayList) hocService.getAdhocUserDimList(hoc_id,
				oper_no, col_field);
		UiAdhocRuleUserDimTable[] sumInfo = (UiAdhocRuleUserDimTable[]) tmpList
				.toArray(new UiAdhocRuleUserDimTable[tmpList.size()]);
		return sumInfo;
	}

	/**
	 * 增加自定义纬度
	 * 
	 * @param hoc_id
	 * @param oper_no
	 * @param col_field
	 * @param lowValue
	 * @param hignVlaue
	 * @param dim_id
	 * @param dim_value
	 * @param sequence
	 * @return
	 */
	public int saveAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value, String sequence) {

		return hocService.saveAdhocUserDimInfo(hoc_id, oper_no, col_field,
				lowValue, hignVlaue, dim_id, dim_value, sequence);

	}

	/**
	 * 删除自定义纬度
	 * 
	 * @param hoc_id
	 * @param oper_no
	 * @param col_field
	 * @param lowValue
	 * @param hignVlaue
	 * @param dim_id
	 * @param dim_value
	 * @return
	 */
	public int deleteAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value) {

		return hocService.deleteAdhocUserDimInfo(hoc_id, oper_no, col_field,
				lowValue, hignVlaue, dim_id, dim_value);

	}

	/**
	 * 即席查询用户清单配置
	 * 
	 * @param hoc_id
	 * @param oper_no
	 * @return
	 */
	public UiAdhocUserListTable[] getAdhocUserListDefine(String hoc_id,
			String oper_no) {
		ArrayList tmpList = (ArrayList) hocService.getAdhocUserListDefine(
				hoc_id, oper_no);
		UiAdhocUserListTable[] info = (UiAdhocUserListTable[]) tmpList
				.toArray(new UiAdhocUserListTable[tmpList.size()]);
		return info;
	}

	public UiAdhocUserListTable[] getAdhocUserListDefine(String hoc_id,
			String oper_no, String favId) {

		ArrayList tmpList = (ArrayList) hocService.getAdhocUserListDefine(
				hoc_id, oper_no, favId);
		UiAdhocUserListTable[] info = (UiAdhocUserListTable[]) tmpList
				.toArray(new UiAdhocUserListTable[tmpList.size()]);
		return info;
	}

	/**
	 * 即席查询默认用户清单配置
	 * 
	 * @param hoc_id
	 * @return
	 */
	public UiAdhocUserListTable[] getAdhocUserListDefineDefault(String hoc_id) {
		ArrayList tmpList = (ArrayList) hocService
				.getAdhocUserListDefineDefault(hoc_id);
		UiAdhocUserListTable[] info = (UiAdhocUserListTable[]) tmpList
				.toArray(new UiAdhocUserListTable[tmpList.size()]);
		return info;
	}

	/**
	 * 即席查询默认用户清单配置
	 * 
	 * @param hoc_id
	 * @param roleId
	 * @return
	 */
	public UiAdhocUserListTable[] getAdhocUserListDefineDefault(String hoc_id,
			String roleId) {
		ArrayList tmpList = (ArrayList) hocService
				.getAdhocUserListDefineDefault(hoc_id, roleId);
		UiAdhocUserListTable[] info = (UiAdhocUserListTable[]) tmpList
				.toArray(new UiAdhocUserListTable[tmpList.size()]);
		return info;
	}

	/**
	 * 提取即席查询多选前置条件配置信息表
	 * 
	 * @param hoc_id
	 * @param con_id
	 * @return
	 */
	public UiAdhocMultiConFilter[] getAdhocMultiSelectCondition(String hoc_id,
			String con_id) {
		ArrayList tmpList = (ArrayList) hocService
				.getAdhocMultiSelectCondition(hoc_id, con_id);
		UiAdhocMultiConFilter[] info = (UiAdhocMultiConFilter[]) tmpList
				.toArray(new UiAdhocMultiConFilter[tmpList.size()]);
		return info;
	}

}
