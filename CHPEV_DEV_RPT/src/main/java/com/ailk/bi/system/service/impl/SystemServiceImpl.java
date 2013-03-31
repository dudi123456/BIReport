package com.ailk.bi.system.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.system.dao.ISystemDao;
import com.ailk.bi.system.dao.impl.SystemDaoImpl;
import com.ailk.bi.system.entity.SystemInfo;
import com.ailk.bi.system.service.ISystemService;

public class SystemServiceImpl implements ISystemService {

	private ISystemDao dao = new SystemDaoImpl();

	public SystemInfo getSystem(String systemId) {
		SystemInfo info = dao.getSystem(systemId);
		return info;
	}

	public boolean isExistSystemId(String systemId) {
		boolean flag = dao.isExistSystemId(systemId);
		return flag;
	}

	public void add(HttpServletRequest request) {
		dao.add(request);
	}

	public void save(HttpServletRequest request) {
		dao.save(request);
	}

	public void del(String systemId) {
		dao.del(systemId);
	}
}
