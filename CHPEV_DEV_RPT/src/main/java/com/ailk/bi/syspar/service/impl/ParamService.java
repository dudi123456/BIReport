package com.ailk.bi.syspar.service.impl;

import java.util.ArrayList;

import com.ailk.bi.syspar.dao.IParamDAO;
import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;
import com.ailk.bi.syspar.service.IParamService;

@SuppressWarnings({ "rawtypes" })
public class ParamService implements IParamService {

	private IParamDAO dao = null;

	/**
	 * 查询配置参数表元数据信息列表
	 * 
	 * @param node_id
	 * @return
	 */
	public ArrayList getParamConfigMetaByNodeID(String param_id) {
		return (ArrayList) dao.getParamConfigMetaByNodeID(param_id);
	}

	public UiParamInfoConfigTable getParamConfigTreeMetaByNodeID(String param_id) {
		return dao.getParamConfigTreeMetaByNodeID(param_id);
	}

	public IParamDAO getDao() {
		return dao;
	}

	public void setDao(IParamDAO dao) {
		this.dao = dao;
	}

	public String[][] getParamLogInfoByNodeID(
			UiParamInfoConfigTable configInfo,
			UiParamMetaConfigTable[] metaInfo, String whereStr) {

		return dao.getParamLogInfoByNodeID(configInfo, metaInfo, whereStr);
	}

}
