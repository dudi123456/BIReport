package com.ailk.bi.workplatform.dao.impl;

/**
 *【DAO层接口实现类】营销目标
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.workplatform.dao.IContactDao;
import com.ailk.bi.workplatform.entity.ContactInfo;

@Repository
public class ContactDaoImpl extends BaseDAO<ContactInfo, Integer> implements
		IContactDao {
	@SuppressWarnings("unchecked")
	public List<ContactInfo> getAllByUserId(String userId, int OrderNo) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ContactInfo.class,
				"ContactInfo");
		if (null != userId) {
			criter.add(Restrictions.eq("ContactInfo.cust_id", userId));
		}
		criter.add(Restrictions.eq("ContactInfo.order_no", OrderNo));
		criter.addOrder(Property.forName("ContactInfo.contact_date").desc());
		List<ContactInfo> results = criter.list();
		session.flush();
		return results;
	}

	public List<ContactInfo> getAll(ContactInfo entity, int count) {
		Session session = getSession();
		Criteria criter = session.createCriteria(ContactInfo.class,
				"ContactInfo");
		if (null == entity && count == 0) {
			return super.findAll();
		} else {
			if (count > 0) {
				criter.setMaxResults(count);
			}

			// 客户名称
			String custName = "%";
			if (entity.getCust_name() != null) {
				custName = "%" + entity.getCust_name() + "%";
				criter.add(Restrictions.like("ContactInfo.cust_name", custName));
			}
			// 服务号码
			String servNumber = "%";
			if (entity.getServ_number() != null) {
				servNumber = "%" + entity.getServ_number() + "%";
				criter.add(Restrictions.like("ContactInfo.serv_number",
						servNumber));
			}
			// 工单类型
			if (entity.getOrder_type() != -999) {
				criter.add(Restrictions.eq("ContactInfo.order_type",
						entity.getOrder_type()));
			}
			// 访问性质
			if (entity.getInterview_nature() != -999) {
				criter.add(Restrictions.eq("ContactInfo.interview_nature",
						entity.getInterview_nature()));
			}
			// 访问类型
			if (entity.getInterview_type() != -999) {
				criter.add(Restrictions.eq("ContactInfo.interview_type",
						entity.getInterview_type()));
			}
			// 接触方式
			if (entity.getContactMode() != -999) {
				criter.add(Restrictions.eq("ContactInfo.contactMode",
						entity.getContactMode()));
			}
			//活动
			if(entity.getActivity_id()!=-999){
				criter.add(Restrictions.eq("ContactInfo.activity_id",entity.getActivity_id()));
			}
			//是否接触成功
			if(entity.getInterview_state()!=-999){
				criter.add(Restrictions.eq("ContactInfo.interview_state",entity.getInterview_state()));
			}
			//是否满意
			if(entity.getPleased_state()!=-999){
				criter.add(Restrictions.eq("ContactInfo.pleased_state",entity.getPleased_state()));
			}
			@SuppressWarnings("unchecked")
			List<ContactInfo> results = criter.list();
			session.flush();
			return results;
		}
	}
}
