package com.ailk.bi.metamanage.dao;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.model.Dimension;

public interface IDimDefDao {

	public Dimension getDim(String dimId);

	public void add(HttpServletRequest request);

	public void save(HttpServletRequest request);

	public void del(String dimId);

	public boolean isExistDimId(String dimId);
}
