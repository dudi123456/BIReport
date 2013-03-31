package com.ailk.bi.map.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.map.dao.IColorDao;
import com.ailk.bi.map.entity.ColorInfo;

public class ColorDaoImpl implements IColorDao {

	private List<ColorInfo> list = new ArrayList<ColorInfo>();
	private List<String> par = new ArrayList<String>();
	public List<ColorInfo> getAllColor(String mapid) {
		try {
			String strSql = SQLGenator.genSQL("M002F", mapid);
			list = WebDBUtil.find(ColorInfo.class, strSql, par);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}