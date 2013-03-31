package com.ailk.bi.workplatform.dao.impl;

/**
 *【DAO层接口实现类】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.workplatform.dao.ITransDao;
import com.ailk.bi.workplatform.entity.TransInfo;

@Repository
public class TransDaoImpl extends BaseDAO<TransInfo, Integer> implements
		ITransDao {
	public List<TransInfo> getAll(TransInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(TransInfo.class, "TransInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			if(entity.showType==2){
				// 我是审批人
					criter.add(Restrictions.eq("TransInfo.passUser.userId",entity.loginUser));
			}
			if(entity.showType==1){
				// 我是申请人
					criter.add(Restrictions.eq("TransInfo.creator.userId",entity.loginUser));
			}
			// 状态
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("TransInfo.state", entity.getState()));
			}
			criter.addOrder(Property.forName("TransInfo.transDate").desc());
			@SuppressWarnings("unchecked")
			List<TransInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	@SuppressWarnings("unchecked")
	public int getCountForTrans(String loginUser) {
		Session session = getSession();
		Criteria criter = session.createCriteria(TransInfo.class,"TransInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("TransInfo.state",0));
		criter.add(Restrictions.eq("TransInfo.passUser.userId",loginUser));
		List<Long> results = new ArrayList<Long>();
		results =criter.list();
		int count = 0;
		if(results.size()>0){
			String countStr =String.valueOf( results.get(0));
			count =Integer.parseInt(countStr);
			session.flush();
		}
		return count;
	}
	@SuppressWarnings("unchecked")
	public int getCountForCreator(String loginUser){
		Session session = getSession();
		Criteria criter = session.createCriteria(TransInfo.class,"TransInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("TransInfo.state",-1));//-1代表未提交
		criter.add(Restrictions.eq("TransInfo.creator.userId",loginUser));
		List<Long> results = new ArrayList<Long>();
		results =criter.list();
		int count = 0;
		if(results.size()>0){
			String countStr =String.valueOf( results.get(0));
			count =Integer.parseInt(countStr);
			session.flush();
		}
		return count;
	}
}
