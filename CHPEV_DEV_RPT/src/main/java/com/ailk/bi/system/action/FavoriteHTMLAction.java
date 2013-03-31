package com.ailk.bi.system.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.LsbiQryStruct;

import com.ailk.bi.base.table.PubInfoFavoriteTable;

import com.ailk.bi.base.util.SQLGenator;

import com.ailk.bi.common.app.AppException;

import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.common.LSInfoFavorite;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unused", "rawtypes" })
public class FavoriteHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// HttpSession session = request.getSession();

		// 取得提交参数
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}

		if ("0".equals(submitType)) { // 新增
			LSInfoFavorite.addFavor(request);
		} else if ("1".equals(submitType) || "3".equals(submitType)) { // 修改
			LSInfoFavorite.updateFavor(request);
		}
		if ("2".equals(submitType)) { // 删除
			LSInfoFavorite.delFavor(request);
		}
		if ("4".equals(submitType)) { // 添加到收藏夹
			request.setAttribute("msg", LSInfoFavorite.addFavorRes(request));
			setNextScreen(request, "favoriteAdd.screen");
		}
		if ("5".equals(submitType)) { // 添加文件夹
			LSInfoFavorite.addFolder(request);
			String url = request.getParameter("gotoUrl");
			setNextScreen(request, url);
		}
	}

	/**
	 * 判断收藏夹中是否有记录
	 * 
	 * @param userid
	 * @param measureId
	 * @return
	 * @throws AppException
	 */
	private static boolean isHavingForavite(String userid, String itemid)
			throws AppException {
		String strSql = SQLGenator.genSQL("Q4010", userid, itemid);
		String[][] result = WebDBUtil.execQryArray(strSql, "");
		if (result == null || result.length == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 将信息加入收藏夹中
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemType
	 * @throws AppException
	 */
	private static void insertForvativeItem(String userId, String itemId,
			String itemType) throws AppException {
		try {
			String strSql = SQLGenator
					.genSQL("I4012", userId, itemId, itemType);
			int i = WebDBUtil.execUpdate(strSql);
			if (i != 1) {
				throw new AppException("添加收藏信息失败!");
			}
		} catch (Exception e) {
			throw new AppException("添加收藏信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 删除收藏夹中信息
	 * 
	 * @param userId
	 * @param itemId
	 * @throws AppException
	 */
	private static void deleteForvativeItem(String userid, String[] itemid)
			throws AppException {
		try {
			String[] strSql = null;
			if (itemid != null && itemid.length > 0) {
				strSql = new String[itemid.length];
			}
			for (int i = 0; itemid != null && i < itemid.length; i++) {
				strSql[i] = SQLGenator.genSQL("D4014", userid, itemid[i]);
			}
			if (strSql != null && strSql.length > 0) {
				WebDBUtil.execTransUpdate(strSql);
			}
		} catch (Exception e) {
			throw new AppException("删除收藏夹信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 设置常用收藏夹中信息
	 * 
	 * @param userId
	 * @param itemId
	 * @throws AppException
	 */
	private static void setHotForvativeItem(String userid, String[] itemid)
			throws AppException {
		try {
			String[] strSql = null;
			if (itemid != null && itemid.length > 0) {
				strSql = new String[itemid.length];
			}
			for (int i = 0; itemid != null && i < itemid.length; i++) {
				strSql[i] = SQLGenator.genSQL("C4017", userid, itemid[i]);
			}
			if (strSql != null && strSql.length > 0) {
				String tmp = SQLGenator.genSQL("C4016", userid);
				WebDBUtil.execUpdate(tmp);
				WebDBUtil.execTransUpdate(strSql);
			}
		} catch (Exception e) {
			throw new AppException("更新收藏夹信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 设置默认访问收藏信息
	 * 
	 * @param userId
	 * @param itemId
	 * @throws AppException
	 */
	private static void setDefaultForvativeItem(String userid, String[] itemid)
			throws AppException {
		try {
			String[] strSql = null;
			if (itemid != null && itemid.length > 0) {
				strSql = new String[itemid.length];
			}
			for (int i = 0; itemid != null && i < itemid.length; i++) {
				strSql[i] = SQLGenator.genSQL("C4019", userid, itemid[i]);
			}
			if (strSql != null && strSql.length > 0) {
				String tmp = SQLGenator.genSQL("C4018", userid);
				WebDBUtil.execUpdate(tmp);
				WebDBUtil.execTransUpdate(strSql);
			}
		} catch (Exception e) {
			throw new AppException("更新收藏夹信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 获取用户收藏列表
	 * 
	 * @param user_id
	 * @param lsbiQry
	 * @return
	 * @throws AppException
	 */
	private static PubInfoFavoriteTable[] genFavList(String user_id,
			LsbiQryStruct lsbiQry) throws AppException {
		PubInfoFavoriteTable[] favs = null;
		String strSql = SQLGenator.genSQL("Q4006", user_id);
		if (!"".equals(lsbiQry.hot_type)) {
			strSql += " AND A.item_type='" + lsbiQry.hot_type + "'";
		}
		if (!"".equals(lsbiQry.obj_name)) {
			strSql += " AND B.name like '%" + lsbiQry.obj_name + "%'";
		}
		// System.out.println("Sql Q4006==>" + strSql);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result != null && result.size() > 0) {
			favs = new PubInfoFavoriteTable[result.size()];
			int m = 0;
			for (int i = 0; i < favs.length; i++) {
				Vector tempv = (Vector) result.get(i);
				favs[i] = new PubInfoFavoriteTable();
				m = 0;
				favs[i].user_id = (String) tempv.get(m++);
				favs[i].res_id = (String) tempv.get(m++);
				favs[i].item_type = (String) tempv.get(m++);
				if ("0".equals(favs[i].item_type)) {
					favs[i].item_type_desc = "普通";
				} else if ("1".equals(favs[i].item_type)) {
					favs[i].item_type_desc = "常用";
				}
				favs[i].item_default = (String) tempv.get(m++);
				favs[i].name = (String) tempv.get(m++);
				favs[i].hot_type = (String) tempv.get(m++);
				favs[i].hot_type_desc = (String) tempv.get(m++);
			}
		}
		return favs;
	}

	/**
	 * 获取用户默认访问url地址
	 * 
	 * @param session
	 * @return
	 */
	public static String getDefaultUrl(HttpSession session) {
		String url = "";
		try {
			// 用户信息
			String user_id = CommonFacade.getLoginId(session);
			String strSql = SQLGenator.genSQL("Q4011", user_id);
			String[][] result = WebDBUtil.execQryArray(strSql, "");
			if (result != null && result.length > 0) {
				url = result[0][1];
			}
		} catch (AppException e) {
			url = "";
		}
		return url;
	}

}
