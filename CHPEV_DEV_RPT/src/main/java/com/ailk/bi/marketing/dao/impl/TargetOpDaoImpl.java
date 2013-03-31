package com.ailk.bi.marketing.dao.impl;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.ITargetOpDao;
import com.ailk.bi.marketing.entity.TargetOpInfo;
/**
 *【DAO层接口实现类】营销目标和策略关系类
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class TargetOpDaoImpl extends BaseDAO<TargetOpInfo, Integer> implements
		ITargetOpDao {
	public boolean delectByTacticID(int id) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE TargetOpInfo WHERE tacticId ="+id;
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
	public List<TargetOpInfo> getAllByTacticId(int tacticId) {
		Session session = getSession();
		Criteria criter = session.createCriteria(TargetOpInfo.class, "targetOpInfo");
		criter.add(Restrictions.eq("targetOpInfo.tacticId",tacticId));
			List<TargetOpInfo> results = criter.list();
			session.flush();
			return results;
	}
}
