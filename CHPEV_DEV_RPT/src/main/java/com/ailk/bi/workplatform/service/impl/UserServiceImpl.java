package com.ailk.bi.workplatform.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.workplatform.dao.IUserDao;
import com.ailk.bi.workplatform.entity.UserInfo;
import com.ailk.bi.workplatform.service.IUserService;

@Service("userService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UserServiceImpl implements IUserService {
	@Resource
	private IUserDao dao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<UserInfo> getAllByDeptId(String deptid) {
		return dao.getAllByDeptId(deptid);
	}
	public UserInfo getById(String userId) {
		return dao.find(userId);
	}

}
