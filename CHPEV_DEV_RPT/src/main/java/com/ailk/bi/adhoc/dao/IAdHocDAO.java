package com.ailk.bi.adhoc.dao;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;

@SuppressWarnings({ "rawtypes" })
public interface IAdHocDAO {

	// 提取条件属性分类列表
	public abstract List getConGroupList(String hocId);

	public abstract List getConGroupList(String hocId, String role_id);

	// 提取纬度属性分类列表
	public abstract List getDimGroupList(String hocId);

	// 提取纬度属性分类列表
	public abstract List getDimGroupList(String hocId, String role_id);

	// 提取指标属性分类列表
	public abstract List getMsuGroupList(String hocId);

	// 提取指标属性分类列表
	public abstract List getMsuGroupList(String hocId, String role_id);

	// 提取制定属性分组的条件列表
	public abstract List getConditionList(String group_id);

	// 提取制定属性分组的条件列表
	public abstract List getConditionList(String group_id, String adhoc_id,
			String role_id);

	// 提取指定纬度分组纬度列表
	public abstract List getDimList(String group_id);

	// 提取指定纬度分组纬度列表
	public abstract List getDimList(String group_id, String adhoc_id,
			String role_id);

	// 提取指标属性分组指标列表
	public abstract List getMsuList(String group_id);

	// 提取指标属性分组指标列表
	public abstract List getMsuList(String group_id, String adhoc_id,
			String role_id);

	// 提取默认条件列表
	public abstract List getDefaultConList(String hoc_id);

	// 提取默认条件列表
	public abstract List getDefaultConList(String hoc_id, String role_id);

	// 提取默认纬度列表
	public abstract List getDefaultDimList(String hoc_id);

	// 提取默认纬度列表
	public abstract List getDefaultDimList(String hoc_id, String role_id);

	// 提取默认指标列表
	public abstract List getDefaultMsuList(String hoc_id);

	// 提取默认指标列表
	public abstract List getDefaultMsuList(String hoc_id, String role_id);

	// 根据列表查询特定的条件元数据
	public abstract List getConListByString(String condition_str);

	// 根据列表查询特定的条件元数据
	public abstract List getConListByString(String condition_str, String hocId,
			String role_id);

	// 根据列表查询特定的纬度元数据
	public abstract List getDimListByString(String dim_str);

	// 根据列表查询特定的纬度元数据
	public abstract List getDimListByString(String dim_str, String hocId,
			String role_id);

	// 根据列表查询特定的纬度元数据
	public abstract List getDimListByWhereString(String where_str);

	// 根据列表查询特定的指标元数据
	public abstract List getMsuListByString(String msu_str);

	// 根据列表查询特定的指标元数据
	public abstract List getMsuListByString(String msu_str, String hocId,
			String role_id);

	// 根据条件ID查询特定的条件元数据
	public abstract UiAdhocConditionMetaTable getConditionMetaInfo(String con_id);

	// 查询即席查询定义信息
	public abstract UiAdhocInfoDefTable getAdhocInfo(String hoc_id);

	// 查询即席查询视图汇总信息
	public abstract List getAdhocSumInfo(String hoc_id);

	// 查询即席查询属性分组定义信息
	public abstract UiAdhocGroupMetaTable getAdhocGroupMetaInfo(String group_tag);

	// 查询即席查询属性分组的层次路径
	public abstract List getAdhocGroupPathInfo(String group_tag);

	// 提取纬度键值对映射MAP代码
	public abstract String getDimMapCode(String hoc_id, String relateCol);

	// 查询用户自定义纬度列表
	public abstract List getAdhocUserDimList(String hoc_id, String oper_no,
			String col_field);

	// 增加用户自定义纬度
	public abstract int saveAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value, String sequence);

	// 删除用户自定义纬度
	public abstract int deleteAdhocUserDimInfo(String hoc_id, String oper_no,
			String col_field, String lowValue, String hignVlaue, String dim_id,
			String dim_value);

	// 用户清单列表
	public abstract List getAdhocUserListDefine(String hoc_id, String oper_no);

	// 用户清单列表
	public abstract List getAdhocUserListDefine(String hoc_id, String oper_no,
			String favId);

	// 默认用户清单列表
	public abstract List getAdhocUserListDefineDefault(String hoc_id);

	// 默认用户清单列表
	public abstract List getAdhocUserListDefineDefault(String hoc_id,
			String roleId);

	// 提取前置条件
	public abstract List getAdhocMultiSelectCondition(String hoc_id,
			String con_id);

}
