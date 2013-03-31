package com.ailk.bi.adhoc.service;

public interface IAdhocCheckService {
	String[][] getCheckSql(String obj_type);

	String[][] getCheckRule(String obj_type);

}
