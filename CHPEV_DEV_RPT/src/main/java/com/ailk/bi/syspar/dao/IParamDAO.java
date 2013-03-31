package com.ailk.bi.syspar.dao;

import java.util.List;

import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;

@SuppressWarnings({ "rawtypes" })
public interface IParamDAO {

	/**
	 * 查询配置参数表元数据信息列表
	 * 
	 * @param parent_node_id
	 * @return
	 */
	List getParamConfigMetaByNodeID(String param_id);

	/**
	 * 查询配置参数表元数据信息列表
	 * 
	 * @param parent_node_id
	 * @return
	 */
	UiParamInfoConfigTable getParamConfigTreeMetaByNodeID(String param_id);

	/**
	 * 查询数据表配置日志信息
	 * 
	 * @param configInfo
	 * @param metaInfo
	 * @param whereStr
	 * @return
	 */
	String[][] getParamLogInfoByNodeID(UiParamInfoConfigTable configInfo,
			UiParamMetaConfigTable[] metaInfo, String whereStr);

}
