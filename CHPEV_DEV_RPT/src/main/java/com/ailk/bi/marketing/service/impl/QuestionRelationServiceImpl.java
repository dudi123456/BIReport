package com.ailk.bi.marketing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IQuestionRelationDao;
import com.ailk.bi.marketing.entity.QuestionRelationInfo;
import com.ailk.bi.marketing.service.IQuestionRelationService;

@Service("questionRelationService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class QuestionRelationServiceImpl implements IQuestionRelationService {

	@Resource
	private IQuestionRelationDao qrdao;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(QuestionRelationInfo entity) {
		return qrdao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return qrdao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public QuestionRelationInfo getById(int id) {
		return qrdao.find(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean modify(QuestionRelationInfo entity) {
		return qrdao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<QuestionRelationInfo> getAllBySurveyId(int surveyId) {
		return qrdao.getAllBySurveyId(surveyId);
	}

}
