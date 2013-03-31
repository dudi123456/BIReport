/**
 *【业务层接口】问题/问卷关系
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.service;

import java.util.List;
import com.ailk.bi.marketing.entity.QuestionRelationInfo;

public interface IQuestionRelationService {
	public boolean save(QuestionRelationInfo entity);
	public boolean delete(String ids);
	public QuestionRelationInfo getById(int id);
	public List<QuestionRelationInfo> getAllBySurveyId(int surveyId);
}
