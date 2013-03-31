package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.INameListDao;
import com.ailk.bi.marketing.entity.NameListInfo;
import com.ailk.bi.marketing.service.INameListService;

@Service("nameListService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class NameListServiceImpl implements INameListService {
	@Resource
	private INameListDao nameListDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(NameListInfo entity) {
		return nameListDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return nameListDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public NameListInfo getById(int id) {
		return nameListDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<NameListInfo> getAll(NameListInfo entity, int count) {
		List<NameListInfo> list =  nameListDao.getAll(entity, count);
		return list ;
	}
}
