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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IFileDao;
import com.ailk.bi.marketing.entity.FileInfo;

@Repository
public class FileDaoImpl extends BaseDAO<FileInfo, Integer> implements
		IFileDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE FileInfo WHERE relationshipId =" + ids + "";
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

	public List<FileInfo> getAll(FileInfo entity, int count) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<FileInfo> getAllByRelastionshipID(int id) {
		Session session = getSession();
		Criteria criter = session.createCriteria(FileInfo.class,"FileInfo");
		criter.add(Restrictions.eq("FileInfo.relationshipId",id));
		List<FileInfo> results = criter.list();
		session.flush();
		return results;
	}
}
