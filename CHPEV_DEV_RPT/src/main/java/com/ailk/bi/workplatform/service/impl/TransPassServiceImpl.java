package com.ailk.bi.workplatform.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.ITransPassDao;
import com.ailk.bi.workplatform.entity.TransPassInfo;
import com.ailk.bi.workplatform.service.ITransPassService;

@Service("transPassService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TransPassServiceImpl implements ITransPassService {
	@Resource
	private ITransPassDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TransPassInfo getById(int id) {
		return dao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TransPassInfo entity) {
		return dao.save(entity);
	}
}
