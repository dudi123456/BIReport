package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.ISurveyDao;
import com.ailk.bi.marketing.entity.SurveyInfo;
import com.ailk.bi.marketing.service.ISurveyService;

@Service("surveyService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SurveyServiceImpl implements ISurveyService {
	@Resource
	private ISurveyDao surveyDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(SurveyInfo entity) {
		return surveyDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return surveyDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public SurveyInfo getById(int id) {
		return surveyDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<SurveyInfo> getAll(SurveyInfo entity, int count) {
		List<SurveyInfo> list =  surveyDao.getAll(entity, count);
		return list ;
	}
}
