package com.ailk.bi.marketing.dao.impl;

/**
 *【DAO层接口实现类】营销案例
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
import com.ailk.bi.marketing.dao.IActivityModeDao;
import com.ailk.bi.marketing.entity.ActivityModeInfo;
@Repository
public class ActivityModeDaoImpl extends BaseDAO<ActivityModeInfo, Integer> implements
IActivityModeDao {

	public ActivityModeInfo getByActivityId(int activityId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityModeInfo.class,"ActivityModeInfo");
		criter.add(Restrictions.eq("ActivityModeInfo.activityId",activityId));
		@SuppressWarnings("unchecked")
		List<ActivityModeInfo> results = criter.list();
		if(null!=results&&results.size()>0){
			return results.get(0);
		}else{
			return null;
		}
	}
}
