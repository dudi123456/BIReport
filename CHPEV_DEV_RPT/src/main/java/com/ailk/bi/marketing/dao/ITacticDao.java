package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.TacticInfo;
/**
 *【DAO层接口】营销策略
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ITacticDao extends
GenericDAO<TacticInfo, Integer> {
	public List<TacticInfo> getAll(TacticInfo entity,int count);
	public boolean delect(String ids);
	public int getCountForPlat(String longUser);
}
