package com.ailk.bi.map.dao;

import java.util.List;

import com.ailk.bi.map.entity.DataInfo;

public interface IDataDao {
	public List<DataInfo> getData(String sql) ;
}
