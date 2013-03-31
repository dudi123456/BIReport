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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IActivityGroupDao;
import com.ailk.bi.marketing.entity.ActivityGroupInfo;

@Repository
public class ActivityGroupDaoImpl extends BaseDAO<ActivityGroupInfo, Integer> implements
IActivityGroupDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ActivityGroupInfo WHERE activityId =" + ids + "";
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

	public List<ActivityGroupInfo> getAll(ActivityGroupInfo entity, int count) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<ActivityGroupInfo> getAllByActivityID(int id) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityGroupInfo.class,"ActivityGroupInfo");
		criter.add(Restrictions.eq("ActivityGroupInfo.activityId",id));
		criter.addOrder( Property.forName("ActivityGroupInfo.groupTypeId").desc() );
		List<ActivityGroupInfo> results = criter.list();
		session.flush();
		return results;
	}

}
