package com.ailk.bi.marketing.dao.impl;
/**
 *【DAO层接口实现类】营销策略
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
import com.ailk.bi.marketing.dao.ITacticDao;
import com.ailk.bi.marketing.entity.TacticInfo;

@Repository
public class TacticDaoImpl extends BaseDAO<TacticInfo, Integer> implements
		ITacticDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE TacticInfo WHERE tacticId IN (" + ids + ")";
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
	public List<TacticInfo> getAll(TacticInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(TacticInfo.class, "tacticInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String tacticName = "%";
			if (null != entity.getTacticName()) {
				tacticName = "%" + entity.getTacticName() + "%";
				//connditionLis.add(Restrictions.like("tacticInfo.tacticName",tacticName));
				criter.add(Restrictions.like("tacticInfo.tacticName",tacticName));
			}
			String creator = "%";
			if (null != entity.getCreator()) {
				creator = "%" + entity.getCreator() + "%";
				//connditionLis.add(Restrictions.like("tacticInfo.creator",creator));
				criter.add(Restrictions.like("tacticInfo.creator",creator));
			}
			if (null != entity.getDecider()) {
				criter.add(Restrictions.like("tacticInfo.decider", entity.getDecider()));
			}
			if(entity.getState()!=-999)
			{
				criter.add(Restrictions.eq("tacticInfo.state",entity.getState()));
			}
			if(entity.getTacticType()!=-999)
			{
				criter.add(Restrictions.eq("tacticInfo.tacticType",entity.getTacticType()));
			}
			criter.addOrder( Property.forName("tacticInfo.createDate").desc() );
			List<TacticInfo> results = 	criter.list();
			session.flush();
			return results;
		}
	}

	@SuppressWarnings("unchecked")
	public int getCountForPlat(String longUser) {
		Session session = getSession();
		Criteria criter = session.createCriteria(TacticInfo.class, "tacticInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("tacticInfo.state",1));
		criter.add(Restrictions.eq("tacticInfo.decider",longUser));
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
