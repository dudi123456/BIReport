package com.ailk.bi.marketing.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ChannleTypeInfo;

public interface IChannleTypeDao extends GenericDAO<ChannleTypeInfo,Integer> {
	public List<ChannleTypeInfo> getAll(ChannleTypeInfo entity,int count);
	public boolean delect(String ids);

}
