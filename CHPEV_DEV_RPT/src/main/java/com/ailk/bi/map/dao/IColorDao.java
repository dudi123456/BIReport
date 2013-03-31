package com.ailk.bi.map.dao;

import java.util.List;

import com.ailk.bi.map.entity.ColorInfo;

public interface IColorDao {
	public List<ColorInfo> getAllColor(String mapid) ;
}
