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
import com.ailk.bi.marketing.dao.IProjectDao;
import com.ailk.bi.marketing.entity.ProjectInfo;

@Repository
public class ProjectDaoImpl extends BaseDAO<ProjectInfo, Integer> implements
		IProjectDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ProjectInfo WHERE projectId IN (" + ids + ")";
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
	public List<ProjectInfo> getAll(ProjectInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ProjectInfo.class,"ProjectInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String tprojectName = "%";
			if (null != entity.getProjectName()) {
				tprojectName = "%" + entity.getProjectName() + "%";
				criter.add(Restrictions.like("ProjectInfo.projectName",tprojectName));
			}
			if (entity.getWarnName() != null) {
				criter.add(Restrictions.eq("ProjectInfo.warnName",entity.getWarnName()));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("ProjectInfo.state",entity.getState()));
			}
			if (entity.getProjectLevel() != -999) {
				criter.add(Restrictions.eq("ProjectInfo.projectLevel",entity.getProjectLevel()));
			}
			if (entity.getPriority() != -999) {
				criter.add(Restrictions.eq("ProjectInfo.priority",entity.getPriority()));
			}
			if (entity.getProjectType() != -999) {
				criter.add(Restrictions.eq("ProjectInfo.projectType",entity.getProjectType()));
			}
			criter.addOrder( Property.forName("ProjectInfo.createDate").desc() );
			List<ProjectInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	@SuppressWarnings("unchecked")
	public int getByTacticId(int tacticId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ProjectInfo.class,"ProjectInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
			criter.add(Restrictions.eq("ProjectInfo.tacticInfo.tacticId",tacticId));
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
		Criteria criter = session.createCriteria(ProjectInfo.class,"ProjectInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
			criter.add(Restrictions.eq("ProjectInfo.channleInfo.channleId",channleId));
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
		Criteria criter = session.createCriteria(ProjectInfo.class,"ProjectInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ProjectInfo.state",1));
			criter.add(Restrictions.eq("ProjectInfo.warnName",longUser));
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
