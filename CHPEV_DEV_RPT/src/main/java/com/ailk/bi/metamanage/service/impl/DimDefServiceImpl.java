package com.ailk.bi.metamanage.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.dao.IDimDefDao;
import com.ailk.bi.metamanage.dao.impl.DimDefDaoImpl;
import com.ailk.bi.metamanage.model.Dimension;
import com.ailk.bi.metamanage.service.IDimDefService;

public class DimDefServiceImpl implements IDimDefService {

	private IDimDefDao dao = new DimDefDaoImpl();

	public Dimension getDim(String dimId) {
		Dimension info = dao.getDim(dimId);
		return info;
	}

	public boolean isExistDimId(String dimId) {
		boolean flag = dao.isExistDimId(dimId);
		return flag;
	}

	public void add(HttpServletRequest request) {
		dao.add(request);
	}

	public void save(HttpServletRequest request) {
		dao.save(request);
	}

	public void del(String dimId) {
		dao.del(dimId);
	}
}
