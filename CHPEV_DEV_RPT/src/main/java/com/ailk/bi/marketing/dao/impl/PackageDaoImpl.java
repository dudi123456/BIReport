package com.ailk.bi.marketing.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IPackageDao;
import com.ailk.bi.marketing.entity.PackageInfo;

@Repository
public class PackageDaoImpl extends BaseDAO<PackageInfo, Integer> implements IPackageDao {

	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE PackageInfo WHERE packageId IN (" + ids + ")";
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
	public List<PackageInfo> getAll(PackageInfo entity, int count) {
		Session session = getSession();
		Criteria criter  = session.createCriteria(PackageInfo.class,"PackageInfo");
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
				criter.add(Restrictions.like("PackageInfo.packageName",packageName));
			}
			if (entity.getPackageType() != -999) {
				criter.add(Restrictions.eq("PackageInfo.packageType",entity.getPackageType()));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("PackageInfo.state",entity.getState()));
			}
			criter.addOrder(Property.forName("PackageInfo.createDate").desc());
			@SuppressWarnings("unchecked")
			List<PackageInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public List<PackageInfo> getAllSonPackage(String ids) {
		Session session = getSession();
		Criteria criter  = session.createCriteria(PackageInfo.class,"PackageInfo");
        String[] values = ids.split(",");
        Integer[] parentPackageIdArr = new Integer[values.length];
        for(int i=0;i<values.length;i++){
        	parentPackageIdArr[i] = Integer.parseInt(values[i]);
        }
		criter.add(Restrictions.in("PackageInfo.parentPackageId", parentPackageIdArr));
		@SuppressWarnings("unchecked")
		List<PackageInfo> results = criter.list();
		session.flush();
		return results;

	}

}
