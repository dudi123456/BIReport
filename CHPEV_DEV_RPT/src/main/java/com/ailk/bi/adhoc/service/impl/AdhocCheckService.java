package com.ailk.bi.adhoc.service.impl;

import com.ailk.bi.adhoc.dao.IAdhocCheckDAO;
import com.ailk.bi.adhoc.service.IAdhocCheckService;

public class AdhocCheckService implements IAdhocCheckService {

	// 即席查询
	private IAdhocCheckDAO checkDAO = null;

	public IAdhocCheckDAO getCheckDAO() {
		return checkDAO;
	}

	public void setCheckDAO(IAdhocCheckDAO checkDAO) {
		this.checkDAO = checkDAO;
	}

	public String[][] getCheckSql(String obj_type) {
		return checkDAO.getCheckSql(obj_type);
	}

	public String[][] getCheckRule(String obj_type) {
		return checkDAO.getCheckRule(obj_type);
	}

}
