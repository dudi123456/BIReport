package com.ailk.bi.marketing.service;
import com.ailk.bi.marketing.entity.ActivityModeInfo;
/**
 *【业务层接口】营销案例
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IActivityModeService {
	public boolean save(ActivityModeInfo entity);
	public ActivityModeInfo getById(int id);
	public ActivityModeInfo getByActivityId(int activityId) ;
}
