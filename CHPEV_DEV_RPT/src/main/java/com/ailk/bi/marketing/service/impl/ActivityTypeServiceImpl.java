package com.ailk.bi.marketing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ailk.bi.marketing.dao.IActivityTypeDao;
import com.ailk.bi.marketing.entity.ActivityTypeInfo;
import com.ailk.bi.marketing.service.IActivityTypeService;
@Service("activityTypeService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ActivityTypeServiceImpl implements IActivityTypeService {

	@Resource
	private IActivityTypeDao activityTypeDao ;
	public IActivityTypeDao getActivityTypeDao() {
		return activityTypeDao;
	}

	public void setActivityTypeDao(IActivityTypeDao activityTypeDao) {
		this.activityTypeDao = activityTypeDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ActivityTypeInfo entity) {
		// TODO Auto-generated method stub
		return activityTypeDao.save(entity);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		// TODO Auto-generated method stub
		return activityTypeDao.delect(ids);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ActivityTypeInfo getById(int id) {
		// TODO Auto-generated method stub
		return activityTypeDao.find(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityTypeInfo> getAll(ActivityTypeInfo entity, int count) {
		// TODO Auto-generated method stub
		List<ActivityTypeInfo> list =  activityTypeDao.getAll(entity, count);
		return list;
	}

}
