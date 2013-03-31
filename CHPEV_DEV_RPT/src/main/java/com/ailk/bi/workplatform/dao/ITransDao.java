package com.ailk.bi.workplatform.dao;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.workplatform.entity.TransInfo;
/**
 *【DAO层接口】营销方案
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ITransDao extends
GenericDAO<TransInfo, Integer> {
	public List<TransInfo>getAll(TransInfo entity,int count);
	public int getCountForTrans(String loginUser);
	public int getCountForCreator(String loginUser);
}
