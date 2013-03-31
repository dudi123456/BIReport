package com.ailk.bi.marketing.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.PackageInfo;

public interface IPackageDao extends GenericDAO<PackageInfo, Integer> {
	public List<PackageInfo> getAll(PackageInfo entity,int count);
	public List<PackageInfo> getAllSonPackage(String ids);
	public boolean delect(String ids);

}
