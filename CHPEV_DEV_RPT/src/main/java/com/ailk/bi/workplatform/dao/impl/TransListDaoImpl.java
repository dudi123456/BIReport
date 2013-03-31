package com.ailk.bi.workplatform.dao.impl;

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
import com.ailk.bi.workplatform.dao.ITransListDao;
import com.ailk.bi.workplatform.entity.TransListInfo;

@Repository
public class TransListDaoImpl extends BaseDAO<TransListInfo, Integer> implements
		ITransListDao {
	public List<TransListInfo>getAllByTransId(int transId){
		Session session = getSession();
		Criteria criter = session.createCriteria(TransListInfo.class, "TransListInfo");
			criter.add(Restrictions.eq("TransListInfo.transId", transId));
			criter.addOrder(Property.forName("TransListInfo.listId").desc());
			@SuppressWarnings("unchecked")
			List<TransListInfo> results = criter.list();
			session.flush();
			return results;
	}


	public boolean deleteByTransId(int transId) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE TransListInfo WHERE transId =" + transId + "";
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
}
