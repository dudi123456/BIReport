package com.ailk.bi.content.dao;

import java.util.List;

import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ICustContentDao {

	/**
	 * 获取指定条件下的模板
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getContent(String whereStr);

	/**
	 * 获取指定条件下的模板的指标
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getContentCalMsu(String whereStr);

	/**
	 * 获取指定条件下的所有数据
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getContentData(String strSql);

	/**
	 * 获取数组数据
	 *
	 * @param strSql
	 * @return
	 */
	public String[][] getData(String strSql);
}
