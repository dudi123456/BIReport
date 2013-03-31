package com.ailk.bi.marketing.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IChannleDao;
import com.ailk.bi.marketing.entity.ChannleInfo;
/**
 *【DAO层接口实现类】营销渠道
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class ChannleDaoImpl extends BaseDAO<ChannleInfo, Integer> implements
		IChannleDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ChannleInfo WHERE channleId IN (" + ids + ")";
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
	public List<ChannleInfo> getAll(ChannleInfo entity, int count) {
		Session session = getSession();
		Criteria criter  = session.createCriteria(ChannleInfo.class,"ChannleInfo");
		if(null == entity && count == 0){
			return super.findAll();
	   	}else	{
			if(count>0){
				criter.setMaxResults(count);
			}
			// 对渠道类型名称进行模糊查询
			String channleName = "%";
			if (null != entity.getChannleName()) {
				channleName = "%" + entity.getChannleName() + "%";
				criter.add(Restrictions.like("ChannleInfo.channleName",channleName));
			}
			String channleCode = "%";
			if (null != entity.getChannleCode()) {
				channleCode = "%" + entity.getChannleCode() + "%";
				criter.add(Restrictions.like("ChannleInfo.channleCode",channleCode));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("ChannleInfo.state",entity.getState()));
			}

			if (entity.getChannleType() != null) {
				criter.add(Restrictions.eq("ChannleInfo.channleType.channleTypeId",entity.getChannleType().getChannleTypeId()));
			}
			if (entity.getCreateDate() != null) {
				criter.add(Restrictions.eq("ChannleInfo.createDate",entity.getCreateDate()));
			}
			criter.addOrder(Property.forName("ChannleInfo.createDate").desc());
			@SuppressWarnings("unchecked")
			List<ChannleInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	@SuppressWarnings("unchecked")
	public int getCountByTypeId(int typeId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ChannleInfo.class,"ChannleInfo");
		criter.setProjection( Projections.projectionList().add( Projections.rowCount()));
		criter.add(Restrictions.eq("ChannleInfo.channleType.channleTypeId",typeId));
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
