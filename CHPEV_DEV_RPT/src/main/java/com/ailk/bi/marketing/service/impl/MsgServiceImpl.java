package com.ailk.bi.marketing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ailk.bi.marketing.dao.IMessageDao;
import com.ailk.bi.marketing.entity.MessageInfo;
import com.ailk.bi.marketing.service.IMsgService;

@Service("msgService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class MsgServiceImpl implements IMsgService {

	@Resource
	private IMessageDao msgdao;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(MessageInfo entity) {
		return msgdao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return msgdao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public MessageInfo getById(int id) {
		return msgdao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<MessageInfo> getAll(MessageInfo entity, int count) {
		return msgdao.getAll(entity, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean modify(MessageInfo entity) {
		return msgdao.save(entity);
	}

}
