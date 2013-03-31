package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ActivityInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityDao extends
GenericDAO<ActivityInfo, Integer> {
	public List<ActivityInfo> getAll(ActivityInfo entity,int count);
	public List<ActivityInfo> getAlldownLevel(ActivityInfo entity,int count);
	public boolean delect(String ids);
	public int getCountByTacticID(int tacticId);
	public int getCountByProjectID(int projectId);
	public int getCountBytypeId(int typeId);
	public int getCountByChannleId(int channleId);
	public int getCountForPlat(String longUser);
}
