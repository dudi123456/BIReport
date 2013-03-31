package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.SellUserInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ISellUserDao extends
GenericDAO<SellUserInfo, Integer> {
	public int getCountbyGroupId(String groupId);
	public List<SellUserInfo> getAllByGroupId(String groupId,int count);
	public boolean delect(String ids);
}
