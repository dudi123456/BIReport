package com.ailk.bi.marketing.service;
import java.util.List;
import com.ailk.bi.marketing.entity.ActivityInfo;
/**
 *【业务层接口】活动信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityService {
	public boolean save(ActivityInfo entity);
	public boolean delete(String ids);
	public ActivityInfo getById(int id);
	/**
	 * 通过条件查询，返回count条记录
	 * @param  entity 当entity为null时，没有条件
	 * @param  count 返回指定的记录数，当count=0时，返回所有的记录
	 */
	public List<ActivityInfo> getAll(ActivityInfo entity,int count);
	public int getCountByTacticID(int tacticId);
	public int getCountByProjectID(int projectId);
	public int getCountBytypeId(int typeId);
	public int getCountByChannleId(int channleId);
	public int getCountForPlat(String longUser);
	public List<ActivityInfo> getAlldownLevel(ActivityInfo entity,int count);
}
