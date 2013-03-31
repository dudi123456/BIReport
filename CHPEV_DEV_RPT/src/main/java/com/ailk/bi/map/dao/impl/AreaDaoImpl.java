package com.ailk.bi.map.dao.impl;

import java.util.ArrayList;
import java.util.List;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.map.dao.IAreaDao;
import com.ailk.bi.map.entity.AreaInfo;

public class AreaDaoImpl implements IAreaDao {

	private List<AreaInfo> list = new ArrayList<AreaInfo>();
	private List<String> par = new ArrayList<String>();

	public List<AreaInfo> getArea(String sql) {
		try {
			list = WebDBUtil.find(AreaInfo.class, sql, par);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public AreaInfo getAreaById(String sql) {
		list = new ArrayList<AreaInfo>();
		try {
			list = WebDBUtil.find(AreaInfo.class, sql, par);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}