package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ActivityGroupInfo;
/**
 *【DAO层接口】活动  客户名单
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityGroupDao extends
GenericDAO<ActivityGroupInfo, Integer> {
	public List<ActivityGroupInfo> getAll(ActivityGroupInfo entity,int count);
	public boolean delect(String ids);
	public  List<ActivityGroupInfo> getAllByActivityID(int id);
}
