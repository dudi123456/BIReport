package com.ailk.bi.marketing.dao.impl;

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
import com.ailk.bi.marketing.dao.IActivityDao;
import com.ailk.bi.marketing.entity.ActivityInfo;

@Repository
public class ActivityDaoImpl extends BaseDAO<ActivityInfo, Integer> implements
		IActivityDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ActivityInfo WHERE activityId IN (" + ids + ")";
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
	@SuppressWarnings("unchecked")
	public List<ActivityInfo> getAll(ActivityInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String activityName = "%";
			if (null != entity.getActivityName()) {
				activityName = "%" + entity.getActivityName() + "%";
				criter.add(Restrictions.like("ActivityInfo.activityName",activityName));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("ActivityInfo.state",entity.getState()));
			}
			if (entity.getDecider() != null) {
				criter.add(Restrictions.eq("ActivityInfo.decider",entity.getDecider()));
			}
			if (entity.getActivityType() != null) {
				criter.add(Restrictions.eq("ActivityInfo.activityType",entity.getActivityType()));
			}
			if (entity.getClientType() != -999) {
				criter.add(Restrictions.eq("ActivityInfo.clientType",entity.getClientType()));
			}
			if (entity.getProjectInfo() != null) {
				criter.add(Restrictions.eq("ActivityInfo.projectInfo.projectId",entity.getProjectInfo().getProjectId()));
			}
			criter.addOrder( Property.forName("ActivityInfo.createDate").desc() );
			List<ActivityInfo> results = criter.list();

			session.flush();
			return results;
		}
	}

	@SuppressWarnings("unchecked")
	public int getCountByTacticID(int tacticId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ActivityInfo.tacticInfo.tacticId",tacticId));
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
	public int getCountByProjectID(int projectId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ActivityInfo.projectInfo.projectId",projectId));
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
	public int getCountBytypeId(int typeId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ActivityInfo.activityType.activityTypeId",typeId));
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
	public int getCountByChannleId(int channleId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ActivityInfo.channleInfo.channleId",channleId));
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
	public int getCountForPlat(String longUser) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ActivityInfo.state",1));
		criter.add(Restrictions.eq("ActivityInfo.decider",longUser));
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
	public List<ActivityInfo> getAlldownLevel(ActivityInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ActivityInfo.class,"ActivityInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String activityName = "%";
			if (null != entity.getActivityName()) {
				activityName = "%" + entity.getActivityName() + "%";
				criter.add(Restrictions.like("ActivityInfo.activityName",activityName));
			}
			criter.add(Restrictions.eq("ActivityInfo.state",2));
			if (entity.getActivityType() != null) {
				criter.add(Restrictions.eq("ActivityInfo.activityType",entity.getActivityType()));
			}
			criter.add(Restrictions.gt("ActivityInfo.activityLevel",entity.showLevel));
			criter.addOrder( Property.forName("ActivityInfo.createDate").desc() );
			List<ActivityInfo> results = criter.list();
			session.flush();
			return results;
		}
	}
}
