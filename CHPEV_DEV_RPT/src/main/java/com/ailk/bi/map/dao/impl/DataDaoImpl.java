package com.ailk.bi.map.dao.impl;

import java.util.ArrayList;
import java.util.List;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.map.dao.IDataDao;
import com.ailk.bi.map.entity.DataInfo;

public class DataDaoImpl implements IDataDao {

	private List<DataInfo> list = new ArrayList<DataInfo>();
	private List<String> par = new ArrayList<String>();
	public List<DataInfo> getData(String sql) {
		try {
			list = WebDBUtil.find(DataInfo.class, sql, par);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}