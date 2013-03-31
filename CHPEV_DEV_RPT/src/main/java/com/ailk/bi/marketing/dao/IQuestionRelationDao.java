package com.ailk.bi.marketing.dao;
/**
 *【DAO层接口】短信信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.QuestionRelationInfo;

public interface IQuestionRelationDao extends
GenericDAO<QuestionRelationInfo, Integer> {
	public List<QuestionRelationInfo> getAllBySurveyId(int surveyId);
	public boolean delect(String ids);
}
