package com.ailk.bi.subject.facade;

import java.util.List;
import java.util.Map;

import com.ailk.bi.subject.service.IOppService;
import com.ailk.bi.base.facade.IFacade;

@SuppressWarnings({ "rawtypes" })
public class OppFacade implements IFacade {

	private IOppService service;

	public IOppService getService() {
		return service;
	}

	public void setService(IOppService service) {
		this.service = service;
	}

	public List getOppParamList() {
		return service.getOppParamList();
	}

	public int addNewParam(String[] value) {

		return service.addNewParam(value);
	}

	public int updateParam(String[] value) {

		return service.updateParam(value);
	}

	public String genParamTableHtml() {
		return service.genParamTableHtml();
	}

	public int deleteParam(String param_id) {

		return service.deleteParam(param_id);
	}

	public List getOppParamValueList(String param_id) {
		return service.getOppParamValueList(param_id);
	}

	public List getOppParam(String param_id) {
		return service.getOppParam(param_id);
	}

	public int addNewParamLvl(String[] value) {

		return service.addNewParamLvl(value);
	}

	public int deleteParamLvl(String whereStr) {

		return service.deleteParamLvl(whereStr);
	}

	public int updateParamLvl(String[] value) {

		return service.updateParamLvl(value);
	}

	public String genParamLvlTableHtml(String param_id) {
		return service.genParamLvlTableHtml(param_id);
	}

	public List getOppParamList(String param_type) {

		return service.getOppParamList(param_type);
	}

	public Map getOppParamWeightMap(String param_type) {
		return service.getOppParamWeightMap(param_type);
	}

}
