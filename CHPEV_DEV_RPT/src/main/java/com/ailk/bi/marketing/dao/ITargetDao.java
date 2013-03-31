package com.ailk.bi.marketing.dao;
/**
 *【DAO层接口】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.TargetInfo;

public interface ITargetDao extends
GenericDAO<TargetInfo, Integer> {
	public List<TargetInfo> getAll(TargetInfo entity,int count);
	public boolean delect(String ids);
}
