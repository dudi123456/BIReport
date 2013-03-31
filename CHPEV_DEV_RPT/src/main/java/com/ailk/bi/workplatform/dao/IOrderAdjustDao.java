package com.ailk.bi.workplatform.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;

public interface IOrderAdjustDao extends GenericDAO<OrderAdjustInfo, Integer> {
	public List <OrderAdjustInfo> getAll(OrderAdjustInfo entity,int count);
}
