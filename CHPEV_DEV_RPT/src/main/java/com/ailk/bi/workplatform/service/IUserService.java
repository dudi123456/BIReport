package com.ailk.bi.workplatform.service;
import java.util.List;
import com.ailk.bi.workplatform.entity.UserInfo;
/**
 *【业务层接口】用户
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IUserService {
	public List<UserInfo> getAllByDeptId(String deptid);
	public UserInfo getById(String userId);
}
