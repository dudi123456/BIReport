package com.ailk.bi.map.dao;

import java.util.List;

import com.ailk.bi.map.entity.AreaInfo;

public interface IAreaDao {
	public List<AreaInfo> getArea(String sql);
	public AreaInfo getAreaById(String sql);
}
