package com.ailk.bi.bulletin.dao;

import java.util.List;

import com.ailk.bi.bulletin.entity.MartInfoBulletin;
import com.ailk.bi.report.struct.ReportQryStruct;

@SuppressWarnings({ "rawtypes" })
public interface IBulletinDao {

	/**
	 * 
	 * @param obj
	 *            ：公告实体
	 * @desc:往UI_INFO_BULLETIN表中插入信息
	 */
	public abstract void insertMartBulletinInfo(MartInfoBulletin obj);

	/**
	 * 
	 * @param qryStruct
	 *            :查询结构
	 * @return
	 * @desc:查询UI_INFO_BULLETIN表
	 */
	public abstract List qryMartBulletinInfoList(ReportQryStruct qryStruct);

	/**
	 * 
	 * @param qryStruct
	 *            :查询结构
	 * @return
	 * @desc:查询UI_INFO_BULLETIN表
	 */
	public abstract String[][] qryMartBulletinInfoList(
			ReportQryStruct qryStruct, boolean isValid);

	/**
	 * 
	 * @param id
	 * @desc:根据ID删除UI_INFO_BULLETIN表记录
	 */
	public abstract void deleteMartBulletinInfo(String id);

	/**
	 * 
	 * @param sql
	 *            ;
	 * @return
	 * @desc:根据SQL查询记录
	 */
	public abstract String[][] qryObjectInfoList(String sql);

	/**
	 * 
	 * @param sql
	 *            ;
	 * @return
	 * @desc:执行SQL语句
	 */
	public abstract void excuteSql(String sql);
}
