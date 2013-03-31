package com.ailk.bi.marketing.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IChannleTypeDao;
import com.ailk.bi.marketing.entity.ChannleTypeInfo;
@Repository
public class ChannleTypeDaoImpl extends BaseDAO<ChannleTypeInfo, Integer> implements
		IChannleTypeDao {

	public List<ChannleTypeInfo> getAll(ChannleTypeInfo entity, int count) {
		Session session = getSession();
		 Criteria criter  = session.createCriteria(ChannleTypeInfo.class,"ChannleTypeInfo");
		if(null==entity&&count==0){
			return super.findAll();
	   	}else	{
			if(count>0){
				criter.setMaxResults(count);
			}
			// 对渠道类型名称进行模糊查询
			String channleTypeName = "%";
			if (null != entity.getChannleTypeName()) {
				channleTypeName = "%" + entity.getChannleTypeName() + "%";
				criter.add(Restrictions.like("ChannleTypeInfo.channleTypeName",channleTypeName));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("ChannleTypeInfo.state",entity.getState()));
			}
			if (entity.getChannleBigNumber() != -999) {
				criter.add(Restrictions.eq("ChannleTypeInfo.channleBigNumber",entity.getChannleBigNumber()));
			}
			@SuppressWarnings("unchecked")
			List<ChannleTypeInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public boolean delect(String ids) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE ChannleTypeInfo WHERE channleTypeId IN (" + ids + ")";
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
