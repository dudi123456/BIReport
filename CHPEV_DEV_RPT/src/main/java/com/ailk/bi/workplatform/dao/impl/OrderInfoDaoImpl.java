package com.ailk.bi.workplatform.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.workplatform.dao.IOrderInfoDao;
import com.ailk.bi.workplatform.entity.OrderInfo;

@Repository
public class OrderInfoDaoImpl extends BaseDAO<OrderInfo, Integer> implements IOrderInfoDao {

	public List<OrderInfo> getAllListBySearch(OrderInfo entity, int count){
		Session session = getSession();
		Criteria criter = session.createCriteria(OrderInfo.class, "OrderInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}

			//是否是协同工单
			if(entity.getOuter_channel()==0){
				criter.add(Restrictions.eqProperty("OrderInfo.channelInfo.channleId", "OrderInfo.oldChannelInfo.channleId"));
			}else if(entity.getOuter_channel()==1){
				criter.add(Restrictions.neProperty("OrderInfo.channelInfo.channleId", "OrderInfo.oldChannelInfo.channleId"));
			}
			//客户名称
			String custName="%";
			if(entity.getCust_name()!=null){
				custName="%"+entity.getCust_name()+"%";
				criter.add(Restrictions.like("OrderInfo.cust_name",custName));
			}
			//服务号码
			String servNumber="%";
			if(entity.getServ_number()!=null){
				servNumber="%"+entity.getServ_number()+"%";
				criter.add(Restrictions.like("OrderInfo.serv_number",servNumber));
			}
			if(entity.getPerformer_id()!=null){
				criter.add(Restrictions.eq("OrderInfo.performer_id",entity.getPerformer_id()));
			}
			//执行人
			if("3".equals(entity.setType)){
				criter.add(Restrictions.isNotNull("OrderInfo.performer_id"));
			}else if("2".equals(entity.setType)){
				criter.add(Restrictions.isNull("OrderInfo.performer_id"));
			}
			//客户经理
			String custSvcMgr ="%";
			if(entity.getCust_svc_mgr_id()!=null){
				custSvcMgr="%"+entity.getCust_svc_mgr_id()+"%";
				criter.add(Restrictions.like("OrderInfo.cust_svc_mgr_id",custSvcMgr));
			}

			//工单状态
			if(entity.getOrder_state()!=-999){
				criter.add(Restrictions.eq("OrderInfo.order_state",entity.getOrder_state()));
			}
			//工单级别
			if(entity.getCust_level()!=null){
				criter.add(Restrictions.eq("OrderInfo.cust_level",entity.getCust_level()));
			}
			//是否是2次工单
			if(entity.getPerform_state()!=-999){
				criter.add(Restrictions.eq("OrderInfo.perform_state",entity.getPerform_state()+1));
			}
			// 工单开始执行日期
			if (entity.date01 != null) {
				criter.add(Restrictions.ge("OrderInfo.contact_start_date", entity.date01));
			}
			if (entity.date02 != null) {
				criter.add(Restrictions.le("OrderInfo.contact_start_date", entity.date02));
			}
			// 工单开始执行日期
			if (entity.date03 != null) {
					criter.add(Restrictions.ge("OrderInfo.next_contact_date", entity.date03));
			}
			// 工单开始执行日期
			if (entity.date04 != null) {
					criter.add(Restrictions.le("OrderInfo.next_contact_date", entity.date04));
			}
			//活动
			if(entity.getActivityInfo()!=null){
				criter.add(Restrictions.eq("OrderInfo.activityInfo.activityId",
						entity.getActivityInfo().getActivityId()));
			}

			// 工单状态
			if(-999!=entity.getOrder_state()){
				criter.add(Restrictions.eq("OrderInfo.order_state", entity.getOrder_state()));
			}
			// 客户ID
			if(null!=entity.getCust_id()){
					criter.add(Restrictions.eq("OrderInfo.cust_id", entity.getCust_id()));
			}
		   if(entity.channleAdmin){
			   criter.add(Restrictions.eq("OrderInfo.channelInfo", entity.getChannelInfo()));
		   }
			// 工单渠道判断
			//	criter.add(Restrictions.eqProperty("OrderInfo.channelInfo.channleId", "OrderInfo.old_channel_id"));
			criter.addOrder(Property.forName("OrderInfo.createdate").desc());
			@SuppressWarnings("unchecked")
			List<OrderInfo> results = criter.list();
			session.flush();
			return results;
			}
		}
	@SuppressWarnings("unchecked")
	public List<OrderInfo> getTodayOrderList(OrderInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(OrderInfo.class, "OrderInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 工单开始执行日期
			if (entity.getContact_start_date() != null) {
				criter.add(Restrictions.eq("OrderInfo.contact_start_date",
						entity.getContact_start_date()));
			}
			// 工单状态
			criter.add(Restrictions.eq("OrderInfo.order_state", entity.getOrder_state()));
			// 工单渠道判断
			criter.add(Restrictions.eqProperty("OrderInfo.channelInfo.channleId", "OrderInfo.oldChannelInfo.channleId"));

			criter.addOrder(Property.forName("OrderInfo.createdate").desc());
			List<OrderInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public String[][] getOutChannelOrderList(int count) {
		StringBuffer sb = new StringBuffer();

		sb.append("select a.activity_id,b.activity_name,count(*),sum(case when a.order_state=1 then 1 else 0 end),sum(case when a.order_state=3 then 1 else 0 end),c.channle_name");
		sb.append(" from mk_pl_channle_info c, mk_ch_order_info a left join mk_pl_activity_info b on a.activity_id=b.activity_id");
		sb.append(" where  a.old_channel_id = c.channle_id and a.channel_id<>a.old_channel_id and a.outer_channel=1 and rownum<="
				+ count);
		sb.append(" group by a.activity_id,b.activity_name,c.channle_name");

		String[][] results = null;
		try {
			results = WebDBUtil.execQryArray(sb.toString());
		} catch (AppException e) {
			e.printStackTrace();
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<OrderInfo> getTwiceTodayOrderList(OrderInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(OrderInfo.class, "OrderInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}

			// 工单再次执行日期
			if (entity.getNext_contact_date() != null) {
				criter.add(Restrictions.eq("OrderInfo.next_contact_date",
						entity.getNext_contact_date()));
			}
			// 工单状态
			criter.add(Restrictions.eq("OrderInfo.order_state", entity.getOrder_state()));
			// 工单渠道判断
			criter.add(Restrictions.eqProperty("OrderInfo.channelInfo.channleId", "OrderInfo.oldChannelInfo.channleId"));

			criter.addOrder(Property.forName("OrderInfo.createdate").desc());
			List<OrderInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public String[][] getAllOrderList() {
		StringBuffer sb = new StringBuffer();

		sb.append("select a.activity_id,b.activity_name,count(*) as all_count");
		sb.append(",nvl(sum(case when a.order_state=3 then 1 else 0 end),0) completed_count");
		sb.append(",nvl(sum(case when a.order_state=3 and to_char(sysdate, 'yyyymmdd') = to_char(a.contact_start_date, 'yyyymmdd') then 1 else 0 end),0) todayComplete_count");
		sb.append(",nvl(sum(case when a.order_state=1 THEN 1 END),0) uncompleted_value");
		sb.append(",nvl(sum(case when (sysdate - a.CONTACT_START_DATE) <= time_limit and a.order_state=1 then 1 else 0 end),0) uncompleted_value");
		sb.append(",nvl(sum(case when (sysdate - a.CONTACT_START_DATE) > time_limit and a.order_state=1 THEN 1 else 0 END),0) unexpire_value");
		sb.append(",nvl(sum(case when a.order_state = -1 then 1 else 0 end),0) close_value");
		sb.append(" from mk_ch_order_info a left join mk_pl_activity_info b on a.activity_id=b.activity_id");
		sb.append(" where a.channel_id=a.old_channel_id and a.outer_channel=0");
		sb.append(" group by a.activity_id,b.activity_name");

		String[][] results = null;
		try {
			results = WebDBUtil.execQryArray(sb.toString());
		} catch (AppException e) {
			e.printStackTrace();
		}
		return results;
	}

//	public OrderInfo find(Integer id) {
//		Session session = getSession();
//		session.flush();
//		return super.find(id);
//	}
}
