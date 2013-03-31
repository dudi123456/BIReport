package com.ailk.bi.marketing.service;
import java.util.List;
import com.ailk.bi.marketing.entity.SellUserInfo;
/**
 *【业务层接口】活动 客户名单
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ISellUserService {
	public boolean save(SellUserInfo entity);
	public boolean delete(String ids);
	public SellUserInfo getById(int id);
	public List<SellUserInfo> getAllByGroupId(String groupId,int count);
	public int getCountbyGroupId(String groupId);
}
