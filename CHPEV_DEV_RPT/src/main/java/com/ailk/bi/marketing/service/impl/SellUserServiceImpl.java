package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.ISellUserDao;
import com.ailk.bi.marketing.entity.SellUserInfo;
import com.ailk.bi.marketing.service.ISellUserService;

@Service("sellUserService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SellUserServiceImpl implements ISellUserService {
	@Resource
	private ISellUserDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(SellUserInfo entity) {
		return dao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return dao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public SellUserInfo getById(int id) {
		return dao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<SellUserInfo> getAllByGroupId(String groupId, int count) {
		return dao.getAllByGroupId(groupId, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int getCountbyGroupId(String groupId) {
		return dao.getCountbyGroupId(groupId);
	}
}
