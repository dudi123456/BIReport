package com.ailk.bi.marketing.dao;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.ProjectInfo;

public interface IProjectDao extends
GenericDAO<ProjectInfo, Integer> {
	public List<ProjectInfo> getAll(ProjectInfo entity,int count);
	public boolean delect(String ids);
	public  int getByTacticId(int tacticId);
	public int getCountByChannleId(int channleId);
	public int getCountForPlat(String longUser);
}
