package com.ailk.bi.workplatform.dao.impl;

/**
 *【DAO层接口实现类】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.workplatform.dao.IOrderAdjustDao;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;

@Repository
public class OrderAdjustDaoImpl extends BaseDAO<OrderAdjustInfo, Integer> implements
		IOrderAdjustDao {
	public List<OrderAdjustInfo> getAll(OrderAdjustInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(OrderAdjustInfo.class,"OrderAdjustInfo");
		if(null!=entity){
			if(null!=entity.getPerformer()){
				criter.add(Restrictions.eq("OrderAdjustInfo.performer.userId",entity.getPerformer().getUserId()));
			}
			if(null!=entity.getUpdatePersonal()){
				criter.add(Restrictions.eq("OrderAdjustInfo.updatePersonal",entity.getUpdatePersonal()));
			}
			if(-999!=entity.getStatus()){
				criter.add(Restrictions.eq("OrderAdjustInfo.status",entity.getStatus()));
			}
		}
		@SuppressWarnings("unchecked")
		List<OrderAdjustInfo> results = criter.list();
		session.flush();
		return results;
	}


}
