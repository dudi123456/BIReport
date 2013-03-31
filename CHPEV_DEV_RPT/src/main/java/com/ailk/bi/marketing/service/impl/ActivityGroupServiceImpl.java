package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IActivityGroupDao;
import com.ailk.bi.marketing.entity.ActivityGroupInfo;
import com.ailk.bi.marketing.service.IActivityGroupService;

@Service("activityGroupService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ActivityGroupServiceImpl implements IActivityGroupService {
	@Resource
	private IActivityGroupDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ActivityGroupInfo entity) {
		return dao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return dao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ActivityGroupInfo getById(int id) {
		return dao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ActivityGroupInfo> getAll(ActivityGroupInfo entity, int count) {
		List<ActivityGroupInfo> list =  dao.getAll(entity, count);
		return list ;
	}
	public List<ActivityGroupInfo> getAllByActivityID(int id) {
		return dao.getAllByActivityID(id);
	}
}
