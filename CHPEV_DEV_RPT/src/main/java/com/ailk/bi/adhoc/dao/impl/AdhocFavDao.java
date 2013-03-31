package com.ailk.bi.adhoc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdHocFavDAO;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocFavDao implements IAdHocFavDAO {

	/**
	 * 新增收藏夹
	 */
	public int addNewFavorite(String favID, String favName, String fav_type,
			String conStr, String dimStr, String msuStr, String oper_no,
			String favTypeFlag) {
		int num = -1;
		ArrayList sqlList = new ArrayList();

		try {
			String maxId = favID;
			// 新增实体
			String addsql = SQLGenator.genSQL("QI014", maxId, favName, favName,
					"1", favTypeFlag);
			sqlList.add(addsql);
			// 新增收藏夹内容
			if (conStr != null && !"".equals(conStr)) {
				String conArr[] = conStr.split(",");
				for (int i = 0; conArr != null && i < conArr.length; i++) {
					String sql = SQLGenator.genSQL("QI016", maxId, "1",
							conArr[i], "" + (i + 1));
					sqlList.add(sql);
				}
			}

			if (dimStr != null && !"".equals(dimStr)) {
				String dimArr[] = dimStr.split(",");
				for (int i = 0; dimArr != null && i < dimArr.length; i++) {
					String sql = SQLGenator.genSQL("QI016", maxId, "2",
							dimArr[i], "" + (i + 1));
					sqlList.add(sql);
				}
			}

			if (msuStr != null && !"".equals(msuStr)) {
				String msuArr[] = msuStr.split(",");
				for (int i = 0; msuArr != null && i < msuArr.length; i++) {
					String sql = SQLGenator.genSQL("QI016", maxId, "3",
							msuArr[i], "" + (i + 1));
					sqlList.add(sql);
				}
			}
			// 新增用户收藏夹关系
			String resql = SQLGenator.genSQL("QI017", oper_no, maxId, fav_type);
			sqlList.add(resql);

			// 批量更新
			String sqlArr[] = (String[]) sqlList.toArray(new String[sqlList
					.size()]);

			WebDBUtil.execTransUpdate(sqlArr);
			num = 1;

		} catch (AppException ex1) {
			num = -1;
			ex1.printStackTrace();
		}

		return num;

	}

	/**
	 * 删除收藏夹
	 */
	public int deleteFavorite(String favID, String oper_no, String fav_type) {
		int reNum = -1;
		ArrayList sqlList = new ArrayList();
		try {

			// 删除操作员关系
			String delsql = SQLGenator
					.genSQL("QD022", oper_no, favID, fav_type);
			sqlList.add(delsql);
			// 删除收藏夹信息
			String delsql1 = SQLGenator.genSQL("QD023", favID);
			sqlList.add(delsql1);
			// 删除收藏夹
			String delsql2 = SQLGenator.genSQL("QD024", favID);
			sqlList.add(delsql2);
			// 批量更新
			String sqlArr[] = (String[]) sqlList.toArray(new String[sqlList
					.size()]);

			WebDBUtil.execTransUpdate(sqlArr);
			reNum = 1;

		} catch (AppException ex1) {
			reNum = -1;
			ex1.printStackTrace();
		}

		return reNum;
	}

	/**
	 * 判断是否重名
	 */
	public boolean isDupName(String name) {

		return true;
	}

	/**
	 * 提取用户收藏夹列表
	 */
	public List getFavoriteList(String oper_no, String fav_type) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT013", fav_type, oper_no);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiAdhocFavoriteDefTable favInfo = new UiAdhocFavoriteDefTable();
				favInfo.setFavorite_id(arr[i][0].trim());
				favInfo.setFavorite_name(arr[i][1].trim());
				favInfo.setFavorite_desc(arr[i][2].trim());
				favInfo.setStatus(arr[i][3]);
				favInfo.setFavTypeFlag(Integer.parseInt(arr[i][4]));
				list.add(favInfo);
			}

		}
		return list;
	}

	/**
	 * 查询即席查询收藏夹信息
	 * 
	 */
	public UiAdhocFavoriteDefTable getFavoriteInfo(String favId) {
		UiAdhocFavoriteDefTable favInfo = new UiAdhocFavoriteDefTable();
		String[][] arr = AdhocUtil.queryArrayFacade("QT018", favId);
		if (arr != null && arr.length > 0) {
			favInfo.setFavorite_id(arr[0][0].trim());
			favInfo.setFavorite_name(arr[0][1].trim());
			favInfo.setFavorite_desc(arr[0][2].trim());
			favInfo.setStatus(arr[0][3].trim());
		}
		return favInfo;
	}

	/**
	 * 查询收藏夹条件列表
	 * 
	 */
	public List getFavoriteConList(String fav_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT019", fav_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i][0].trim());
			}

		}
		return list;
	}

	/**
	 * 查询收藏夹纬度列表
	 * 
	 */
	public List getFavoriteDimList(String fav_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT020", fav_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {

				list.add(arr[i][0].trim());
			}

		}
		return list;
	}

	/**
	 * 查询收藏夹指标列表
	 * 
	 */
	public List getFavoriteMsuList(String fav_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QT021", fav_id);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i][0].trim());
			}

		}
		return list;
	}

}
