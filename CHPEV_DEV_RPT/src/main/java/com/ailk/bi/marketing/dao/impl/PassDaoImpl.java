package com.ailk.bi.marketing.dao.impl;

/**
 *【DAO层接口实现类】审批意见
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
import com.ailk.bi.marketing.dao.IPassDao;
import com.ailk.bi.marketing.entity.PassInfo;

@Repository
public class PassDaoImpl extends BaseDAO<PassInfo, Integer> implements
		IPassDao {
	public boolean delectByTypeId(int id){
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE PassInfo WHERE typeId ="+id;
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
	public List<PassInfo> getAllByTypeId(int id){
		Session session = getSession();
		Criteria criter = session.createCriteria(PassInfo.class,"PassInfo");
			if (id!= -999) {
				criter.add(Restrictions.eq("PassInfo.typeId",id));
			}
			criter.addOrder( Property.forName("PassInfo.step").asc() );
			List<PassInfo> results = criter.list();
			session.flush();
			return results;
		}
}
