package com.ailk.bi.marketing.dao.impl;

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
import com.ailk.bi.marketing.dao.IActivityListDao;
import com.ailk.bi.marketing.entity.ActivityListInfo;

@Repository
public class ActivityListDaoImpl extends BaseDAO<ActivityListInfo, Integer> implements
		IActivityListDao {
	public boolean delectByActivityId(int id) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ActivityListInfo WHERE activityId =" + id+ "";
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

	public List<ActivityListInfo> getAll(ActivityListInfo entity, int count) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<ActivityListInfo> getAllActivityID(int id) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityListInfo.class,"ActivityListInfo");
		criter.add(Restrictions.eq("ActivityListInfo.activityId",id));
		List<ActivityListInfo> results = criter.list();
		session.flush();
		return results;
	}
}
