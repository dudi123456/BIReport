package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IContactPlyDao;
import com.ailk.bi.marketing.entity.ContactPlyInfo;
import com.ailk.bi.marketing.service.IContactPlyService;
@Service("contactPlyService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ContactPlyServiceImpl implements IContactPlyService {
	@Resource
	private IContactPlyDao contactPlyDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ContactPlyInfo entity) {
		return contactPlyDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByTacticID(int id) {
		return contactPlyDao.delectByTacticId(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ContactPlyInfo getById(int id) {
		return contactPlyDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ContactPlyInfo> getAllByTacticID(int id) {
		return contactPlyDao.getAllByTacticId(id);
	}

}
