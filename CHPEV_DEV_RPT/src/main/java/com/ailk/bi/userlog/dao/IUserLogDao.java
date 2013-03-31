package com.ailk.bi.userlog.dao;

import com.ailk.bi.userlog.entity.UiInfoUserLog;

public interface IUserLogDao {
	/**
	 * 
	 * @param sql
	 *            ;
	 * @return
	 * @desc:执行SQL语句
	 */
	public abstract void excuteSql(String sql);

	/**
	 * 
	 * @param obj
	 *            ：日志实体
	 * @desc:往UI_SYS_LOG表中插入信息
	 */
	public abstract void insert(UiInfoUserLog obj);

	/**
	 * 
	 * @param obj
	 *            ：日志实体
	 * @desc:往UI_SYS_LOG表中更新信息
	 */
	public abstract void update(UiInfoUserLog obj);

	public abstract String[][] qryObjectInfoList(String sql);

}
