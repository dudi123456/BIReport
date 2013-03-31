package com.ailk.bi.marketing.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.PackageInfo;
import com.ailk.bi.marketing.entity.PackageParentInfo;

public interface IPackageParentDao extends GenericDAO<PackageParentInfo, Integer>{
	public List<PackageParentInfo> getParentAll(PackageInfo entity,int count);

}
