package com.ailk.bi.marketing.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.marketing.dao.IMessageDao;
import com.ailk.bi.marketing.entity.MessageInfo;
/**
 *【DAO层接口实现类】短信信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Repository
public class MessageDaoImpl extends
BaseDAO<MessageInfo, Integer> implements IMessageDao {
	@SuppressWarnings("unchecked")
	public List<MessageInfo> getAll(MessageInfo entity,int count)
	{
		Session session = getSession();
		Criteria criter = session.createCriteria(MessageInfo.class,"MessageInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}
			// 对内容进行模糊查询
			String content = "%";
			if (null != entity.getContent()) {
				content = "%" + entity.getContent() + "%";
				criter.add(Restrictions.like("MessageInfo.content",content));
			}
			if (entity.getMsgType() != -999) {
				criter.add(Restrictions.eq("MessageInfo.msgType",entity.getMsgType()));
			}
			if (entity.getMsgState() != -999) {
				criter.add(Restrictions.eq("MessageInfo.msgState",entity.getMsgState()));
			}
			criter.addOrder( Property.forName("MessageInfo.createTime").desc() );
			List<MessageInfo> results = criter.list();
			session.flush();
			return results;
		}
	}

	public boolean delect(String ids) {
		int count = 0;
		try {
			Session session = super.getSession();
			String HSQL = "DELETE MessageInfo WHERE msgId IN (" + ids + ")";
			count = session.createQuery(HSQL).executeUpdate();
			System.out.println("删除条数：" + count);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("批量删除出现问题");
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
