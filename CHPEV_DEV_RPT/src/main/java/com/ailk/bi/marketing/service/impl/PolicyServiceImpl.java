package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IPolicyDao;
import com.ailk.bi.marketing.entity.PolicyInfo;
import com.ailk.bi.marketing.service.IPolicyService;

@Service("policyService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PolicyServiceImpl implements IPolicyService {
	@Resource
	private IPolicyDao policyDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(PolicyInfo entity) {
		return policyDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return policyDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PolicyInfo getById(int id) {
		return policyDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PolicyInfo> getAll(PolicyInfo entity, int count) {
		List<PolicyInfo> list =  policyDao.getAll(entity, count);
		return list ;
	}
}
