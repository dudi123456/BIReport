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
import com.ailk.bi.marketing.dao.IGroupDao;
import com.ailk.bi.marketing.entity.GroupInfo;

@Repository
public class GroupDaoImpl extends BaseDAO<GroupInfo, String> implements
		IGroupDao {
	public boolean delect(String ids) {
		int count = 0;
//		try {
//			Session session = super.getSession();
//			String HSQL = "DELETE GroupInfo WHERE activityId IN (" + ids + ")";
//			count = session.createQuery(HSQL).executeUpdate();
//			System.out.println("删除条数：" + count);
//			session.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("批量删除出现问题");
//		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	public List<GroupInfo> getAll(GroupInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(GroupInfo.class,"GroupInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String groupName = "%";
			if (null != entity.getGroupName()) {
				groupName = "%" + entity.getGroupName() + "%";
				criter.add(Restrictions.like("GroupInfo.groupName",groupName));
			}

			criter.add(Restrictions.eq("GroupInfo.delFlag",'1'));

			if (entity.getGroupTypeId() != -999) {
				criter.add(Restrictions.eq("GroupInfo.groupTypeId",entity.getGroupTypeId()));
			}
			if (entity.getCreateType() != '-') {
				criter.add(Restrictions.eq("GroupInfo.createType",entity.getCreateType()));
			}
			if (entity.getStatus() != null) {
				criter.add(Restrictions.eq("GroupInfo.status",entity.getStatus()));
			}
			if (entity.getBrandOf() != -999) {
				criter.add(Restrictions.eq("GroupInfo.brandOf",entity.getBrandOf()));
			}
			criter.addOrder( Property.forName("GroupInfo.operTime").desc() );
			List<GroupInfo> results = criter.list();
			session.flush();
			return results;
		}
	}
}
