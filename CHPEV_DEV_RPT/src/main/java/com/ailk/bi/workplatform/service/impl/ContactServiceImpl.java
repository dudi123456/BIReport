package com.ailk.bi.workplatform.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.IContactDao;
import com.ailk.bi.workplatform.entity.ContactInfo;
import com.ailk.bi.workplatform.service.IContactService;

@Service("contactService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ContactServiceImpl implements IContactService {
	@Resource
	private IContactDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ContactInfo> getAllByUserId(String userId,int OrderNo) {
		return dao.getAllByUserId(userId, OrderNo);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ContactInfo entity) {
		return dao.save(entity);
	}
	public List<ContactInfo> getAll(ContactInfo entity, int count) {
		return dao.getAll(entity, 0);
	}
}
