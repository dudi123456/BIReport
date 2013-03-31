package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ChannleInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IChannleDao extends
GenericDAO<ChannleInfo, Integer> {
	public List<ChannleInfo> getAll(ChannleInfo entity,int count);
	public boolean delect(String ids);
	public int getCountByTypeId(int typeId);
}
