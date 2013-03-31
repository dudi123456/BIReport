package com.ailk.bi.favor.dao;

//import com.ailk.bi.common.app.AppException;
//import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.favor.entity.UserFavor;

public interface IFavorDao {

	/**
	 * 
	 * @param favor
	 * @desc:插入数据
	 */
	public abstract void insert(UserFavor favor);

	/**
	 * 
	 * @param favor
	 * @desc:删除数据
	 */
	public abstract void delete(UserFavor favor);

	/**
	 * 
	 * @param favor
	 * @desc:更新数据
	 */
	public abstract void update(UserFavor favor);

	/**
	 * 
	 * @param sql
	 */
	public abstract void excuteSql(String sql);
}
