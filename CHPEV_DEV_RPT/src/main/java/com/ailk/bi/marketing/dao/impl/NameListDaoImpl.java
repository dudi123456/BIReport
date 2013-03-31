package com.ailk.bi.marketing.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.INameListDao;
import com.ailk.bi.marketing.entity.NameListInfo;

/**
 *【DAO层接口实现类】客户类表
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class NameListDaoImpl extends BaseDAO<NameListInfo, Integer> implements
		INameListDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE NameListInfo WHERE nameListId IN (" + ids + ")";
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
	public List<NameListInfo> getAll(NameListInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(NameListInfo.class,"NameListInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			// 对名字进行模糊查询
			String name = "%";
			if (null != entity.getNameListName()) {
				name = "%" + entity.getNameListName() + "%";
				criter.add(Restrictions.like("NameListInfo.nameListName",name));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("NameListInfo.state",entity.getState()));
			}
			if (entity.getClientType() != -999) {
				criter.add(Restrictions.eq("NameListInfo.clientType",entity.getClientType()));
			}
			criter.addOrder( Property.forName("NameListInfo.nameListId").desc() );
			List<NameListInfo> results = criter.list();
			session.flush();
			return results;
		}
	}
}
