package com.ailk.bi.workplatform.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.workplatform.entity.UserInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IUserDao extends
GenericDAO<UserInfo, String> {
	public List<UserInfo> getAllByDeptId(String deptid);
}
