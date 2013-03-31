package com.ailk.bi.workplatform.service;

import java.util.List;

import com.ailk.bi.workplatform.entity.OrderInfo;

public interface IOrderInfoService {
	public List<OrderInfo> getAllListBySearch(OrderInfo entity, int count);
	public OrderInfo getOneById(int orderId);
	public boolean save(OrderInfo entity);
	/**
	 * 获取今日工单
	 *
	 * @param entity
	 * @param count
	 * @return
	 */
	public List<OrderInfo> getTodayOrderList(OrderInfo entity, int count);

	/**
	 * 获取外渠道转派执行工单
	 *
	 * @param entity
	 * @param count
	 * @return
	 */
	public String[][] getOutChannelOrderList(int count);

	/**
	 * 获取二次执行工单
	 *
	 * @param entity
	 * @param count
	 * @return
	 */
	public List<OrderInfo> getTwiceTodayOrderList(OrderInfo entity, int count);

	/**
	 * 获取所有活动工单状态
	 *
	 * @return
	 */
	public String[][] getAllOrderList();
}
