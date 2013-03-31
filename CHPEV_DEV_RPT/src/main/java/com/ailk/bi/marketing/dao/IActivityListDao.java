package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ActivityListInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityListDao extends
GenericDAO<ActivityListInfo, Integer> {
	public List<ActivityListInfo> getAll(ActivityListInfo entity,int count);
	public boolean delectByActivityId(int id);
	public  List<ActivityListInfo> getAllActivityID(int id);
}
