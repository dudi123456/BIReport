package com.ailk.bi.metamanage.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.dao.IEtlJobDao;
import com.ailk.bi.metamanage.dao.impl.EtlJobDaoImpl;
import com.ailk.bi.metamanage.model.EtlJob;
import com.ailk.bi.metamanage.service.IEtlJobService;

public class EtlJobServiceImpl implements IEtlJobService {

	private IEtlJobDao dao = new EtlJobDaoImpl();

	public EtlJob getEtlJob(String jobId) {
		EtlJob info = dao.getEtlJob(jobId);
		return info;
	}

	public boolean isExistJobId(String jobId) {
		boolean flag = dao.isExistJobId(jobId);
		return flag;
	}

	public void add(HttpServletRequest request) {
		dao.add(request);
	}

	public void save(HttpServletRequest request) {
		dao.save(request);
	}

	public void del(String jobId) {
		dao.del(jobId);
	}
}
