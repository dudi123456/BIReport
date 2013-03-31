package com.ailk.bi.marketing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IChannleTypeDao;
import com.ailk.bi.marketing.entity.ChannleTypeInfo;
import com.ailk.bi.marketing.service.IChannleTypeService;

@Service("channleTypeService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ChannleTypeServiceImpl implements IChannleTypeService {

	@Resource
	private IChannleTypeDao channleTypeDao ;
	public IChannleTypeDao getTargetTypeDao() {
		return channleTypeDao;
	}
	public void setChannleTypeDao(IChannleTypeDao channleTypeDao) {
		this.channleTypeDao = channleTypeDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ChannleTypeInfo entity) {
		// TODO Auto-generated method stub
		return channleTypeDao.save(entity);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		// TODO Auto-generated method stub
		return channleTypeDao.delect(ids);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ChannleTypeInfo getById(int id) {
		// TODO Auto-generated method stub
		return channleTypeDao.find(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ChannleTypeInfo> getAll(ChannleTypeInfo entity, int count) {
		// TODO Auto-generated method stub
		List<ChannleTypeInfo> list =  channleTypeDao.getAll(entity, count);
		return list;
	}

}
