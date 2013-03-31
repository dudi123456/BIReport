package com.ailk.bi.marketing.dao;
/**
 *【DAO层接口】短信信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.MessageInfo;

public interface IMessageDao extends
GenericDAO<MessageInfo, Integer> {
	public List<MessageInfo> getAll(MessageInfo entity,int count);
	public boolean delect(String ids);
}
