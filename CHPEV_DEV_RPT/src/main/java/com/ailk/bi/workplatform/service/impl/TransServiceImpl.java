package com.ailk.bi.workplatform.service.impl;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.ITransDao;
import com.ailk.bi.workplatform.entity.TransInfo;
import com.ailk.bi.workplatform.service.ITransService;

@Service("transService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TransServiceImpl implements ITransService {
	@Resource
	private ITransDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TransInfo getById(int id) {
		return dao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TransInfo entity) {
		return dao.save(entity);
	}
	public List<TransInfo> getAll(TransInfo entity, int count) {
		return dao.getAll(entity, count);
	}
	public int getCountForTrans(String loginUser) {
		return dao.getCountForTrans(loginUser);
	}
	public int getCountForCreator(String loginUser) {
		return dao.getCountForCreator(loginUser);
	}
}
