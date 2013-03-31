package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IPassDao;
import com.ailk.bi.marketing.entity.PassInfo;
import com.ailk.bi.marketing.service.IPassService;

@Service("passService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PassServiceImpl implements IPassService {
	@Resource
	private IPassDao passDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(PassInfo entity) {
		return passDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByTypeId(int id){
		return passDao.delectByTypeId(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PassInfo getById(int id) {
		return passDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PassInfo> getAllByTypeId(int id) {
		List<PassInfo> list =  passDao.getAllByTypeId(id);
		return list ;
	}
}
