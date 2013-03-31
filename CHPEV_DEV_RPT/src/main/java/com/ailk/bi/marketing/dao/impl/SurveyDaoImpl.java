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
import com.ailk.bi.marketing.dao.ISurveyDao;
import com.ailk.bi.marketing.entity.SurveyInfo;

@Repository
public class SurveyDaoImpl extends BaseDAO<SurveyInfo, Integer> implements
		ISurveyDao {
	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE SurveyInfo WHERE surveyId IN (" + ids + ")";
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
	public List<SurveyInfo> getAll(SurveyInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(SurveyInfo.class,"SurveyInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对名字进行模糊查询
			String name = "%";
			if (null != entity.getSurveyName()) {
				name = "%" + entity.getSurveyName() + "%";
				criter.add(Restrictions.like("SurveyInfo.surveyName",name));
			}
			if (entity.getState() != -999) {
				criter.add(Restrictions.eq("SurveyInfo.state",entity.getState()));
			}

			if (entity.getSurveyType() != -999) {
				criter.add(Restrictions.eq("SurveyInfo.surveyType",entity.getSurveyType()));
			}
			criter.addOrder( Property.forName("SurveyInfo.createDate").desc() );
			List<SurveyInfo> results = criter.list();
			session.flush();
			return results;
		}
	}
}
