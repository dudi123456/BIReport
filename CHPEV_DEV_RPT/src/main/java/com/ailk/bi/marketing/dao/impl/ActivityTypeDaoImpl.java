package com.ailk.bi.marketing.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IActivityTypeDao;
import com.ailk.bi.marketing.entity.ActivityTypeInfo;
@Repository
public class ActivityTypeDaoImpl extends BaseDAO<ActivityTypeInfo, Integer> implements
		IActivityTypeDao {

	public List<ActivityTypeInfo> getAll(ActivityTypeInfo entity, int count) {
		Session session = getSession();
		 Criteria criter  = session.createCriteria(ActivityTypeInfo.class,"ActivityTypeInfo");
		if(null==entity&&count==0){
			return super.findAll();
	   	}else	{
			if(count>0){
				criter.setMaxResults(count);
			}
			// 对活动类型名称进行模糊查询
			String activityTypeName = "%";
			if (null != entity.getActivityTypeName()) {
				activityTypeName = "%" + entity.getActivityTypeName() + "%";
				criter.add(Restrictions.like("ActivityTypeInfo.activityTypeName",activityTypeName));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("ActivityTypeInfo.state",entity.getState()));
			}
			if (entity.getContactCount() != -999) {
				criter.add(Restrictions.eq("ActivityTypeInfo.contactCount",entity.getContactCount()));
			}
			@SuppressWarnings("unchecked")
			List<ActivityTypeInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public boolean delect(String ids) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ActivityTypeInfo WHERE activityTypeId IN (" + ids + ")";
			count = session.createQuery(HSQL).executeUpdate();
			System.out.println("删除条数：" + count);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("批量删除出现问题");
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
