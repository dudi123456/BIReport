package com.ailk.bi.syspar.service;

import java.util.ArrayList;

import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;

@SuppressWarnings({ "rawtypes" })
public interface IParamService {
	ArrayList getParamConfigMetaByNodeID(String param_id);

	UiParamInfoConfigTable getParamConfigTreeMetaByNodeID(String param_id);

	String[][] getParamLogInfoByNodeID(UiParamInfoConfigTable configInfo,
			UiParamMetaConfigTable[] metaInfo, String whereStr);
}
