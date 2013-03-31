package com.ailk.bi.marketing.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IPackageParentDao;
import com.ailk.bi.marketing.entity.PackageInfo;
import com.ailk.bi.marketing.entity.PackageParentInfo;

@Repository
public class PackageParentDaoImpl extends BaseDAO<PackageParentInfo, Integer> implements
IPackageParentDao {

	public List<PackageParentInfo> getParentAll(PackageInfo entity, int count) {
		Session session = getSession();
		Criteria criter  = session.createCriteria(PackageParentInfo.class,"PackageParentInfo");
		if(null == entity && count == 0){
			return super.findAll();
	   	}else	{
			if(count>0){
				criter.setMaxResults(count);
			}

			// 对套餐名称进行模糊查询
			String packageName = "%";
			if (null != entity.getPackageName()) {
				packageName = "%" + entity.getPackageName() + "%";
				criter.add(Restrictions.like("PackageParentInfo.packageName",packageName));
			}
			if (entity.getPackageType() != -999) {
				criter.add(Restrictions.eq("PackageParentInfo.packageType",entity.getPackageType()));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("PackageParentInfo.state",entity.getState()));
			}
			criter.addOrder(Property.forName("PackageParentInfo.createDate").desc());
			@SuppressWarnings("unchecked")
			List<PackageParentInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

}
