package com.ailk.bi.metamanage.service;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.model.EtlJob;

public interface IEtlJobService {

	public EtlJob getEtlJob(String jobId);

	public void add(HttpServletRequest request);

	public void save(HttpServletRequest request);

	public void del(String jobId);

	public boolean isExistJobId(String jobId);
}
