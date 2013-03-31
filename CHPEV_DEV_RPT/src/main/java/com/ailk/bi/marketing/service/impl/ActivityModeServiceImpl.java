package com.ailk.bi.marketing.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IActivityModeDao;
import com.ailk.bi.marketing.entity.ActivityModeInfo;
import com.ailk.bi.marketing.service.IActivityModeService;

@Service("activityModeService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ActivityModeServiceImpl implements IActivityModeService {
	@Resource
	private IActivityModeDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ActivityModeInfo entity) {
		return dao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ActivityModeInfo getById(int id) {
		return dao.find(id);
	}
	public ActivityModeInfo getByActivityId(int activityId) {
		return dao.getByActivityId(activityId);
	}
}
