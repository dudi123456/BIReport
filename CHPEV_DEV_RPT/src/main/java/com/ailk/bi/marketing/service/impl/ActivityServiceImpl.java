package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IActivityDao;
import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.service.IActivityService;

@Service("activityService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ActivityServiceImpl implements IActivityService {
	@Resource
	private IActivityDao activityDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ActivityInfo entity) {
		return activityDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return activityDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ActivityInfo getById(int id) {
		return activityDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityInfo> getAll(ActivityInfo entity, int count) {
		List<ActivityInfo> list =  activityDao.getAll(entity, count);
		return list ;
	}
	public int getCountByTacticID(int tacticId) {
		return activityDao.getCountByTacticID(tacticId);
	}
	public int getCountByProjectID(int projectId) {
		return activityDao.getCountByProjectID(projectId);
	}
	public int getCountBytypeId(int typeId) {
		return activityDao.getCountBytypeId(typeId);
	}
	public int getCountByChannleId(int channleId) {
		return activityDao.getCountByChannleId(channleId);
	}
	public int getCountForPlat(String longUser) {
		return activityDao.getCountForPlat(longUser);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityInfo> getAlldownLevel(ActivityInfo entity, int count) {
		return activityDao.getAlldownLevel(entity, count);
	}
}
