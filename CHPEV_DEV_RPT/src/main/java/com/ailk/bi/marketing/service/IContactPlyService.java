/**
 * 【业务层接口】接触规则
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.service;

import java.util.List;

import com.ailk.bi.marketing.entity.ContactPlyInfo;

public interface IContactPlyService {
	public boolean save(ContactPlyInfo entity);
	public boolean deleteByTacticID(int id);
	public ContactPlyInfo getById(int id);
	public List<ContactPlyInfo> getAllByTacticID(int id);
}
