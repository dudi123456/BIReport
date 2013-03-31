package com.ailk.bi.workplatform.service;
import java.util.List;
import com.ailk.bi.workplatform.entity.ContactInfo;
/**
 *【业务层接口】活动信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IContactService {
	public List<ContactInfo> getAllByUserId(String userId,int OrderNo);
	public boolean save(ContactInfo entity);
	public List <ContactInfo> getAll(ContactInfo entity,int count);
}
