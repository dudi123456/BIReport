package com.ailk.bi.workplatform.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.workplatform.entity.OrderInfo;

/**
 * 【DAO层接口】工作平台
 *
 * @author 任辉
 * @version [版本号, 2012-05-2]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

public interface IOrderInfoDao extends GenericDAO<OrderInfo, Integer> {
	public List<OrderInfo> getAllListBySearch(OrderInfo entity, int count);
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
