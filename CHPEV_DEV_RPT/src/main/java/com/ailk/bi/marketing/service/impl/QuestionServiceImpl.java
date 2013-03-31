package com.ailk.bi.marketing.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IQuestionDao;
import com.ailk.bi.marketing.entity.QuestionInfo;
import com.ailk.bi.marketing.service.IQuestionService;

@Service("questionService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class QuestionServiceImpl implements IQuestionService {

	@Resource
	private IQuestionDao questiondao;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(QuestionInfo entity) {
		return questiondao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return questiondao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public QuestionInfo getById(int id) {
		return questiondao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<QuestionInfo> getAll(QuestionInfo entity, int count) {
		return questiondao.getAll(entity, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean modify(QuestionInfo entity) {
		return questiondao.save(entity);
	}

}
