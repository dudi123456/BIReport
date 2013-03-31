package com.ailk.bi.subject.service;

import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes" })
public interface IOppService {

	public List getOppParamList();

	public int addNewParam(String[] value);

	public int updateParam(String[] value);

	public String genParamTableHtml();

	public int deleteParam(String param_id);

	public List getOppParamValueList(String param_id);

	public List getOppParam(String param_id);

	public int addNewParamLvl(String[] value);

	public int updateParamLvl(String[] value);

	public int deleteParamLvl(String whereStr);

	public String genParamLvlTableHtml(String param_id);

	public List getOppParamList(String param_type);

	public Map getOppParamWeightMap(String param_type);

}
