package com.ailk.bi.syspar.manage;

import java.util.ArrayList;

import com.ailk.bi.base.facade.IFacade;
import com.ailk.bi.syspar.dao.IParamDAO;
import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;
import com.ailk.bi.syspar.service.IParamService;
import com.ailk.bi.syspar.service.impl.ParamService;

@SuppressWarnings({ "rawtypes" })
public class ParamFacade implements IFacade {
	private IParamService service = null;

	public ParamFacade(IParamDAO dao) {
		ParamService service = new ParamService();
		service.setDao(dao);
		this.service = service;
	}

	/**
	 * 查询配置参数表元数据信息列表
	 * 
	 * @param node_id
	 * @return
	 */
	public ArrayList getParamConfigMetaByNodeID(String node_id) {
		return service.getParamConfigMetaByNodeID(node_id);
	}

	public UiParamInfoConfigTable getParamConfigTreeMetaByNodeID(String node_id) {
		return service.getParamConfigTreeMetaByNodeID(node_id);
	}

	public String[][] getParamLogInfoByNodeID(
			UiParamInfoConfigTable configInfo,
			UiParamMetaConfigTable[] metaInfo, String whereStr) {
		return service.getParamLogInfoByNodeID(configInfo, metaInfo, whereStr);
	}
}
