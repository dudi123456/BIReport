package com.ailk.bi.adhoc.dao;

public interface IAdhocCheckDAO {

	String[][] getCheckSql(String obj_type);

	String[][] getCheckRule(String obj_type);

}
