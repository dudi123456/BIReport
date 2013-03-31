package com.ailk.bi.system.dao;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.system.entity.SystemInfo;

public interface ISystemDao {

	public SystemInfo getSystem(String systemId);

	public void add(HttpServletRequest request);

	public void save(HttpServletRequest request);

	public void del(String systemId);

	public boolean isExistSystemId(String systemId);
}
