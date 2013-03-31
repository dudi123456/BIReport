package com.ailk.bi.marketing.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.ITacticDao;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.service.ITacticService;

@Service("tacticService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TacticServiceImpl implements ITacticService {
	@Resource
	private ITacticDao tacticDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TacticInfo entity) {
		return tacticDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return tacticDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TacticInfo getById(int id) {
		return tacticDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<TacticInfo> getAll(TacticInfo entity, int count) {
		List<TacticInfo> list =  tacticDao.getAll(entity, count);
		return list ;
	}
	public int getCountForPlat(String longUser) {
		return tacticDao.getCountForPlat(longUser);
	}
}
