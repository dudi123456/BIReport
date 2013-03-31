package com.ailk.bi.marketing.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IQuestionRelationDao;
import com.ailk.bi.marketing.entity.QuestionRelationInfo;
/**
 *【DAO层接口实现类】问题关系
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class QuestionRealtionDaoImpl extends
BaseDAO<QuestionRelationInfo, Integer> implements IQuestionRelationDao {
	@SuppressWarnings("unchecked")
	public List<QuestionRelationInfo> getAllBySurveyId(int surveyId)
	{
		Session session = getSession();
		Criteria criter = session.createCriteria(QuestionRelationInfo.class,"QuestionRelationInfo");

			if (surveyId!= -999) {
				criter.add(Restrictions.eq("QuestionRelationInfo.surveyId",surveyId));
			}
			List<QuestionRelationInfo> results = criter.list();
			session.flush();
			return results;
	}

	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE QuestionRelationInfo WHERE surveyId IN (" + ids + ")";
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
