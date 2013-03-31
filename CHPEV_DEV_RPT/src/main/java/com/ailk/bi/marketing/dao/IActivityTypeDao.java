package com.ailk.bi.marketing.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ActivityTypeInfo;

public interface IActivityTypeDao extends GenericDAO<ActivityTypeInfo, Integer> {
	public List<ActivityTypeInfo> getAll(ActivityTypeInfo entity,int count);
	public boolean delect(String ids);

}
