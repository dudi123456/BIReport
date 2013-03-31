package com.ailk.bi.map.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.map.dao.IMapDao;
import com.ailk.bi.map.entity.MapInfo;

public class MapDaoImpl implements IMapDao {

	private List<MapInfo> list = new ArrayList<MapInfo>();
	private List<String> par = new ArrayList<String>();
	public MapInfo getMapInfo(String mapId) {
		try {
			String strSql = SQLGenator.genSQL("M001F", mapId);
			list = WebDBUtil.find(MapInfo.class, strSql, par);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.get(0);
	}
}