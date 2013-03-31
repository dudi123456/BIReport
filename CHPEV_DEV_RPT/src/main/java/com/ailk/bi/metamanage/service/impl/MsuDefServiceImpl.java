package com.ailk.bi.metamanage.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.dao.IMsuDefDao;
import com.ailk.bi.metamanage.dao.impl.MsuDefDaoImpl;
import com.ailk.bi.metamanage.model.Measure;
import com.ailk.bi.metamanage.service.IMsuDefService;

public class MsuDefServiceImpl implements IMsuDefService {

	private IMsuDefDao dao = new MsuDefDaoImpl();

	public Measure getMsu(String msuId) {
		Measure info = dao.getMsu(msuId);
		return info;
	}

	public boolean isExistMsuId(String msuId) {
		boolean flag = dao.isExistMsuId(msuId);
		return flag;
	}

	public void add(HttpServletRequest request) {
		dao.add(request);
	}

	public void save(HttpServletRequest request) {
		dao.save(request);
	}

	public void del(String msuId) {
		dao.del(msuId);
	}
}
