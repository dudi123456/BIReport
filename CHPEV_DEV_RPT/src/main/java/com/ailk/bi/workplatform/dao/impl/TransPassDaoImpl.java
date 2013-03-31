package com.ailk.bi.workplatform.dao.impl;

/**
 *【DAO层接口实现类】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.workplatform.dao.ITransPassDao;
import com.ailk.bi.workplatform.entity.TransPassInfo;

@Repository
public class TransPassDaoImpl extends BaseDAO<TransPassInfo, Integer> implements
		ITransPassDao {
}
