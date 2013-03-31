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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.workplatform.dao.IUserDao;
import com.ailk.bi.workplatform.entity.UserInfo;

@Repository
public class UserDaoImpl extends BaseDAO<UserInfo, String> implements
		IUserDao {
	public List<UserInfo> getAllByDeptId(String deptid) {
		Session session = getSession();
		Criteria criter = session.createCriteria(UserInfo.class,"UserInfo");
		if(deptid!=null){
			criter.add(Restrictions.eq("UserInfo.deptInfo.deptId",deptid));
		}
		@SuppressWarnings("unchecked")
		List<UserInfo> results = criter.list();
		session.flush();
		return results;
	}

}
