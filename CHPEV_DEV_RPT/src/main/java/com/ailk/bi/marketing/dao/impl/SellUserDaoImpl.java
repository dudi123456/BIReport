package com.ailk.bi.marketing.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.ISellUserDao;
import com.ailk.bi.marketing.entity.SellUserInfo;

/**
 *【DAO层接口实现类】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class SellUserDaoImpl extends BaseDAO<SellUserInfo, Integer> implements
		ISellUserDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE SellUserInfo WHERE id IN (" + ids + ")";
			count = session.createQuery(HSQL).executeUpdate();
			System.out.println(this.getClass()+"删除条数：" + count);
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
	public List<SellUserInfo> getAllByGroupId(String groupId, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(SellUserInfo.class,"SellUserInfo");
			if (count > 0) {
				criter.setMaxResults(count);
			}
			if (groupId != null) {
				criter.add(Restrictions.eq("SellUserInfo.groupId",groupId));
			}
			criter.addOrder( Property.forName("SellUserInfo.groupId").desc() );
			List<SellUserInfo> results = criter.list();
			session.flush();
			return results;
	}
	@SuppressWarnings("unchecked")
	public int getCountbyGroupId(String groupId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(SellUserInfo.class,"SellUserInfo");
			if (groupId != null) {
				criter.add(Restrictions.eq("SellUserInfo.groupId",groupId));
			}
			criter.addOrder( Property.forName("SellUserInfo.groupId").desc() );
			List<SellUserInfo> results =  new ArrayList<SellUserInfo>() ;
			results=	criter.list();
			session.flush();
			return results.size();
	}
}
