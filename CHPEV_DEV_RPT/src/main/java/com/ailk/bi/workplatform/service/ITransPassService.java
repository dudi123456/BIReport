package com.ailk.bi.workplatform.service;
import com.ailk.bi.workplatform.entity.TransPassInfo;
/**
 *【业务层接口】用户
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ITransPassService {
	public TransPassInfo getById(int id);
	public boolean save(TransPassInfo entity);

}
