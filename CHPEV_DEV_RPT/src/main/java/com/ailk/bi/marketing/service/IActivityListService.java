package com.ailk.bi.marketing.service;
import java.util.List;

import com.ailk.bi.marketing.entity.ActivityListInfo;
/**
 *【业务层接口】文件信息
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityListService {
	public boolean save(ActivityListInfo entity);
	public List<ActivityListInfo> getAll(ActivityListInfo entity,int count);
	public boolean delectByActivityId(int id);
	public  List<ActivityListInfo> getAllActivityID(int id);
}
