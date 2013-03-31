package com.ailk.bi.marketing.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.ITargetDao;
import com.ailk.bi.marketing.entity.TargetInfo;
import com.ailk.bi.marketing.service.ITargetService;

@Service("targetService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TargetServiceImpl implements ITargetService {
	@Resource
	private ITargetDao targetDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TargetInfo entity) {
		return targetDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return targetDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TargetInfo getById(int id) {
		return targetDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<TargetInfo> getAll(TargetInfo entity, int count) {
		List<TargetInfo> list =  targetDao.getAll(entity, count);
		return list ;
	}

}
