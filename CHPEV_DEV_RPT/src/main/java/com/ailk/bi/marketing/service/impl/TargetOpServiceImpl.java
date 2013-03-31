package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.ITargetOpDao;
import com.ailk.bi.marketing.entity.TargetOpInfo;
import com.ailk.bi.marketing.service.ITargetOpService;
@Service("targetOpService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TargetOpServiceImpl implements ITargetOpService {
	@Resource
	private ITargetOpDao targetOpDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TargetOpInfo entity) {
		return targetOpDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByTacticID(int id) {
		return targetOpDao.delectByTacticID(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TargetOpInfo getById(int id) {
		return targetOpDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<TargetOpInfo> getAllByTacticID(int id) {
		return targetOpDao.getAllByTacticId(id);
	}
}
