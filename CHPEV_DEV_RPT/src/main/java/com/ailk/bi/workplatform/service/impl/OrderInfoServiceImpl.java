package com.ailk.bi.workplatform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ailk.bi.workplatform.dao.IOrderInfoDao;
import com.ailk.bi.workplatform.entity.OrderInfo;
import com.ailk.bi.workplatform.service.IOrderInfoService;

@Service("orderInfoService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class OrderInfoServiceImpl implements IOrderInfoService {
	@Resource
	private IOrderInfoDao orderInfoDao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<OrderInfo> getTodayOrderList(OrderInfo entity, int count) {
		List<OrderInfo> results = orderInfoDao.getTodayOrderList(entity, count);
		return results;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String[][] getOutChannelOrderList(int count) {
		String[][] results = orderInfoDao.getOutChannelOrderList(count);
		return results;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<OrderInfo> getTwiceTodayOrderList(OrderInfo entity, int count) {
		List<OrderInfo> results = orderInfoDao.getTwiceTodayOrderList(entity, count);
		return results;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String[][] getAllOrderList(){
		String[][] results = orderInfoDao.getAllOrderList();
		return results;
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<OrderInfo> getAllListBySearch(OrderInfo entity, int count) {
		return orderInfoDao.getAllListBySearch(entity, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public OrderInfo getOneById(int orderId) {
		return orderInfoDao.find(orderId);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(OrderInfo entity) {
		return orderInfoDao.save(entity);
	}
}
