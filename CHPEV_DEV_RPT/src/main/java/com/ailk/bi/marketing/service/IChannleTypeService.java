/**
 * 【业务层接口】渠道类型
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.service;

import java.util.List;
import com.ailk.bi.marketing.entity.ChannleTypeInfo;

public interface IChannleTypeService {
	public boolean save(ChannleTypeInfo entity);
	public boolean delete(String ids);
	public ChannleTypeInfo getById(int id);
	/**
	 * 通过条件查询，返回count条记录
	 * @param  entity 当entity为null时，没有条件
	 * @param  count 返回指定的记录数，当count=0时，返回所有的记录
	 * @return 返回list结果集，将返回结果记录放到list集合里面
	 */
	public List<ChannleTypeInfo> getAll(ChannleTypeInfo entity,int count);
}
