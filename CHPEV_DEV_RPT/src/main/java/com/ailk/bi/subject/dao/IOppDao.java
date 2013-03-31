package com.ailk.bi.subject.dao;

import java.util.List;

/**
 * 
 * 竞争对手DAO
 * 
 * @author jcm
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface IOppDao {

	public List getOppParamList();

	public List getOppParamList(String param_type);

	public List getOppParam(String param_id);

	public List getOppParamValueList(String param_id);

	public int addNewParam(String[] value);

	public int updateParam(String[] value);

	public int deleteParam(String param_id);

	public int addNewParamLvl(String[] value);

	public int updateParamLvl(String[] value);

	public int deleteParamLvl(String whereStr);

}
