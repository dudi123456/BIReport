package com.ailk.bi.adhoc.service;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;

/**
 * 即席查询服务接口
 * 
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface IAdhocService {
	/**
	 * 提取条件定制属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getConGroupListByHocId(String hoc_id);

	/**
	 * 提取条件定制属性分组
	 * 
	 * @param hoc_id
	 * @param role_id
	 * @return
	 */
	List getConGroupListByHocId(String hoc_id, String role_id);

	/**
	 * 提取结果定制纬度属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDimGroupListByHocId(String hoc_id);

	/**
	 * 提取结果定制纬度属性分组
	 * 
	 * @param hoc_id
	 * @param role_id
	 * @return
	 */
	List getDimGroupListByHocId(String hoc_id, String role_id);

	/**
	 * 提取结果定制指标属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getMsuGroupListByHocId(String hoc_id);

	/**
	 * 提取结果定制指标属性分组
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getMsuGroupListByHocId(String hoc_id, String role_id);

	/**
	 * 提取特定条件属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	List getConditionListByGroupId(String group_id);

	/**
	 * 提取特定条件属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @param role_id
	 * @return
	 */
	List getConditionListByGroupId(String group_id, String adhoc_id,
			String role_id);

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	List getMsuListByGroupId(String group_id);

	/**
	 * 提取特定指标属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	List getMsuListByGroupId(String group_id, String adhoc_id, String role_id);

	/**
	 * 提取特定纬度属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	List getDimListByGroupId(String group_id);

	/**
	 * 提取特定纬度属性分组定义的属性列表
	 * 
	 * @param group_id
	 * @return
	 */
	List getDimListByGroupId(String group_id, String adhoc_id, String role_id);

	/**
	 * 提取默认纬度列表
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDefaultDimListByHocId(String hoc_id);

	/**
	 * 提取默认纬度列表
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDefaultDimListByHocId(String hoc_id, String role_id);

	/**
	 * 提取默认指标列表
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDefaultMsuListByHocId(String hoc_id);

	/**
	 * 提取默认指标列表
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDefaultMsuListByHocId(String hoc_id, String role_id);

	/**
	 * 提取默认条件列表
	 * 
	 * @param hoc_id
	 * @return
	 */
	List getDefaultConListByHocId(String hoc_id);

	/**
	 * 提取默认条件列表
	 * 
	 * @param hoc_id
	 * @param role_id
	 * @return
	 */
	List getDefaultConListByHocId(String hoc_id, String role_id);

	/**
	 * 根据条件ID字符串查询条件信息列表
	 * 
	 * @param condition_str
	 * @return
	 */
	List getConListByString(String condition_str);

	/**
	 * 根据条件ID字符串查询条件信息列表
	 * 
	 * @param condition_str
	 * @return
	 */
	List getConListByString(String condition_str, String hocId, String roleId);

	/**
	 * 根据纬度ID字符串查询纬度信息列表
	 * 
	 * @param dim_str
	 * @return
	 */
	List getDimListByString(String dim_str);

	/**
	 * 根据纬度ID字符串查询纬度信息列表
	 * 
	 * @param dim_str
	 * @param roleId
	 * @return
	 */
	List getDimListByString(String dim_str, String hocId, String roleId);

	/**
	 * 根据条件获取维度信息列表
	 * 
	 * @param dim_str
	 * @return
	 */
	List getDimListByWhereString(String dim_str);

	/**
	 * 根据指标ID字符串查询指标信息列表
	 * 
	 * @param msu_str
	 * @return
	 */
	List getMsuListByString(String msu_str);

	/**
	 * 根据指标ID字符串查询指标信息列表
	 * 
	 * @param msu_str
	 * @return
	 */
	List getMsuListByString(String msu_str, String hocId, String roleId);

	/**
	 * 增加收藏夹
	 * 
	 * @return
	 */
	int addNewFavorite(String favID, String favName, String fav_type,
			String conStr, String dimStr, String msuStr, String oper_no,
			String favTypeFlag);

	/**
	 * 删除收藏夹
	 * 
	 * @return
	 */
	int deleteFavorite();

	UiAdhocFavoriteDefTable getFavoriteInfo(String favId);

	List getFavoriteListByOper(String oper_no, String fav_type);

	// 查询收藏夹条件列表
	List getFavoriteConList(String fav_id);

	// 查询收藏夹纬度列表
	List getFavoriteDimList(String fav_id);

	// 查询收藏夹指标列表
	List getFavoriteMsuList(String fav_id);

	int deleteFavorite(String favID, String oper_no, String fav_type);

	UiAdhocConditionMetaTable getConditionMetaInfo(String con_id);

	UiAdhocInfoDefTable getAdhocInfo(String hoc_id);

	List getAdhocSumInfo(String hoc_id);

	UiAdhocGroupMetaTable getAdhocGroupMetaInfo(String group_tag);

	List getAdhocGroupPathInfo(String group_tag);

	String getDimMapCode(String hoc_id, String col_filed);

	List getAdhocUserDimList(String hoc_id, String oper_no, String col_field);

	int saveAdhocUserDimInfo(String hoc_id, String oper_no, String col_field,
			String lowValue, String hignVlaue, String dim_id, String dim_value,
			String sequence);

	int deleteAdhocUserDimInfo(String hoc_id, String oper_no, String col_field,
			String lowValue, String hignVlaue, String dim_id, String dim_value);

	List getAdhocUserListDefine(String hoc_id, String oper_no);

	List getAdhocUserListDefine(String hoc_id, String oper_no, String favId);

	List getAdhocUserListDefineDefault(String hoc_id);

	List getAdhocUserListDefineDefault(String hoc_id, String roleId);

	List getAdhocMultiSelectCondition(String hoc_id, String con_id);
}
