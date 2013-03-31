package com.ailk.bi.workplatform.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.IOrderAdjustDao;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;
import com.ailk.bi.workplatform.service.IAdjustService;

@Service("adjustService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class OrderAdjustServiceImpl implements IAdjustService {
	@Resource
	private IOrderAdjustDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<OrderAdjustInfo> getAll(OrderAdjustInfo entity, int count) {
		return dao.getAll(entity, count);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(OrderAdjustInfo entity) {
		return dao.save(entity);
	}

}
