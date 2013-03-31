package com.ailk.bi.workplatform.service.impl;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.ITransListDao;
import com.ailk.bi.workplatform.entity.TransListInfo;
import com.ailk.bi.workplatform.service.ITransListService;

@Service("transListService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TransListServiceImpl implements ITransListService {
	@Resource
	private ITransListDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TransListInfo getById(int id) {
		return dao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(TransListInfo entity) {
		return dao.save(entity);
	}
	public List<TransListInfo> getAllByTransId(int transId) {
		return dao.getAllByTransId(transId);
	}
	public boolean deleteByTransId(int transId) {
		return dao.deleteByTransId(transId);
	}
}
