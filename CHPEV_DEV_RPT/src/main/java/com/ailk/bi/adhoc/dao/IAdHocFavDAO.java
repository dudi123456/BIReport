package com.ailk.bi.adhoc.dao;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;

@SuppressWarnings({ "rawtypes" })
public interface IAdHocFavDAO {
	// 增加收藏夹信息
	int addNewFavorite(String favID, String favName, String fav_type,
			String conStr, String dimStr, String msuStr, String oper_no,
			String favTypeFlag);

	// 修改收藏夹信息
	int deleteFavorite(String favID, String oper_no, String fav_type);

	// 判断收藏夹是否同名
	boolean isDupName(String name);

	// 根据收藏夹ID提取收藏夹信息
	UiAdhocFavoriteDefTable getFavoriteInfo(String favId);

	// 提取收藏夹列表
	List getFavoriteList(String oper_no, String fav_type);

	// 查询收藏夹条件列表
	List getFavoriteConList(String fav_id);

	// 查询收藏夹纬度列表
	List getFavoriteDimList(String fav_id);

	// 查询收藏夹指标列表
	List getFavoriteMsuList(String fav_id);

}
