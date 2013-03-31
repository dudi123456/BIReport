/**
 *【业务层接口】审批信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.service;

import java.util.List;
import com.ailk.bi.marketing.entity.PassInfo;

public interface IPassService {
	public boolean save(PassInfo entity);
	public boolean deleteByTypeId(int id);
	public PassInfo getById(int id);
	public List<PassInfo> getAllByTypeId(int id);
}
