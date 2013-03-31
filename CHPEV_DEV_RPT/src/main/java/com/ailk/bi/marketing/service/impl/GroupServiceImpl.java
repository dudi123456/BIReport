package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IGroupDao;
import com.ailk.bi.marketing.entity.GroupInfo;
import com.ailk.bi.marketing.service.IGroupService;

@Service("groupService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class GroupServiceImpl implements IGroupService {
	@Resource
	private IGroupDao groupDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(GroupInfo entity) {
		return groupDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return groupDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public GroupInfo getById(String id) {
		return groupDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<GroupInfo> getAll(GroupInfo entity, int count) {
		List<GroupInfo> list =  groupDao.getAll(entity, count);
		return list ;
	}
}
