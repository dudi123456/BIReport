package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IActivityListDao;
import com.ailk.bi.marketing.entity.ActivityListInfo;
import com.ailk.bi.marketing.service.IActivityListService;

@Service("activityListService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ActivityListServiceImpl implements IActivityListService {
	@Resource
	private IActivityListDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ActivityListInfo entity) {
		return dao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityListInfo> getAll(ActivityListInfo entity, int count) {
		return dao.getAll(entity, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delectByActivityId(int id) {
		return dao.delectByActivityId(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityListInfo> getAllActivityID(int id) {
		return dao.getAllActivityID(id);
	}
}
