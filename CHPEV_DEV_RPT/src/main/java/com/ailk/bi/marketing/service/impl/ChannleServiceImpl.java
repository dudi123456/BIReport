package com.ailk.bi.marketing.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ailk.bi.marketing.dao.IChannleDao;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.service.IChannleService;

@Service("channleService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ChannleServiceImpl implements IChannleService {
	@Resource
	private IChannleDao channleDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ChannleInfo entity) {
		return channleDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return channleDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ChannleInfo getById(int id) {
		return channleDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ChannleInfo> getAll(ChannleInfo entity, int count) {
		List<ChannleInfo> list =  channleDao.getAll(entity, count);
		return list ;
	}
	public int getCountByTypeId(int typeId) {
		return channleDao.getCountByTypeId(typeId);
	}
}
