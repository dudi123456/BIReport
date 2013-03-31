package com.ailk.bi.metamanage.dao;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.model.Measure;

public interface IMsuDefDao {

	public Measure getMsu(String msuId);

	public void add(HttpServletRequest request);

	public void save(HttpServletRequest request);

	public void del(String msuId);

	public boolean isExistMsuId(String msuId);
}
