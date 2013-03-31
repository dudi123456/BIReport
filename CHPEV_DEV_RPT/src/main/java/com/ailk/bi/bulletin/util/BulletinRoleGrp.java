package com.ailk.bi.bulletin.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.bulletin.entity.MartInfoBulletin;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BulletinRoleGrp {

	private static Log logger = LogFactory.getLog(BulletinRoleGrp.class);

	/**
	 * 取得所有用户组信息及相关用户信息
	 * 
	 * @return
	 */
	private static List getAllGroupAndUserInfo() {

		String sql = "select group_id,group_name,parent_id from ui_info_user_group   "
				+ "start with PARENT_ID is null  connect by prior group_id = parent_id  order by sequence";
		logger.debug(sql);

		List obj = new ArrayList();
		try {
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					MartInfoBulletin ent = new MartInfoBulletin();
					ent.setGroupId(StringB.NulltoBlank(arr[i][0]));
					ent.setGroupName(StringB.NulltoBlank(arr[i][1]));
					String grpId = ent.getGroupId();
					String pId = StringB.NulltoBlank(arr[i][2]);
					ent.setParentId("G_" + pId);
					if (pId.length() == 0 || pId.equals("0")) {
						ent.setLevel(1);
					} else {
						ent.setLevel(2);
					}

					int level = ent.getLevel();

					ent.setTypeId("2");// 用户组
					obj.add(ent);
					String sqlUser = "select a.user_id,a.user_name from ui_info_user a,ui_rule_user_group b "
							+ "where a.user_id=b.user_id and b.group_id='"
							+ ent.getGroupId() + "'";

					String[][] arrUser = WebDBUtil.execQryArray(sqlUser, "");
					if (arrUser != null && arrUser.length > 0) {
						for (int j = 0; j < arrUser.length; j++) {
							ent = new MartInfoBulletin();
							ent.setUserId(StringB.NulltoBlank(arrUser[j][0]));
							ent.setUserName(StringB.NulltoBlank(arrUser[j][1]));
							ent.setParentId("G_" + grpId);
							ent.setLevel(level + 1);
							ent.setTypeId("1");// 用户类型
							obj.add(ent);
						}
					}
				}
			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 生成用户组用户树形
	 * 
	 * @return
	 */
	public static String buildMenuTree() {
		StringBuffer sb = new StringBuffer();
		List obj = getAllGroupAndUserInfo();
		sb.append("var tree = new WebFXTree('用户组/用户信息');\n");
		sb.append("tree.setBehavior('classic');\n");

		for (int i = 0; i < obj.size(); i++) {
			MartInfoBulletin ent = (MartInfoBulletin) obj.get(i);
			String MENU_ID = "";
			String name = "";
			if (ent.getTypeId().equals("1")) {
				MENU_ID = "U_" + ent.getUserId();
				name = ent.getUserName();
			} else {
				MENU_ID = "G_" + ent.getGroupId();
				name = ent.getGroupName();
			}

			switch (ent.getLevel()) {
			case 1:
				sb.append("var " + MENU_ID + " = new WebFXCheckBoxTreeItem('"
						+ name + "','','" + MENU_ID + "',false);\n");
				sb.append("tree.add(" + MENU_ID + ");\n");
				break;
			default:
				sb.append("var " + MENU_ID + " = new WebFXCheckBoxTreeItem('"
						+ name + "','','" + MENU_ID + "',false);\n");
				sb.append(ent.getParentId() + ".add(" + MENU_ID + ");\n");
				break;
			/*
			 * case 2: sb.append("var " + MENU_ID +
			 * " = new WebFXCheckBoxTreeItem('" + name + "','','" + MENU_ID +
			 * "',false);\n"); sb.append(ent.getParentId() + ".add(" + MENU_ID +
			 * ");\n"); break; case 3:
			 * 
			 * break;
			 */
			}

		}
		sb.append("document.write(tree);\n");
		return sb.toString();
	}

	public static List transferSelectIdToType(String input) {
		String[] strInputDim = input.split("\\,");
		List list = new ArrayList();

		for (int i = 0; i < strInputDim.length; i++) {
			MartInfoBulletin ent = new MartInfoBulletin();
			ent.setId(strInputDim[i].substring(2));
			if (strInputDim[i].substring(0, 2).equalsIgnoreCase("U_")) {
				ent.setTypeId("1");
			} else {
				ent.setTypeId("2");
			}
			list.add(ent);
		}
		return list;
	}

	public static String getSelectUserOrGroup(String input) {
		StringBuffer sb = new StringBuffer("");
		String[] strInputDim = input.split("\\,");
		String userId = "";
		String groupId = "";
		for (int i = 0; i < strInputDim.length; i++) {
			if (strInputDim[i].substring(0, 2).equalsIgnoreCase("U_")) {
				if (userId.length() == 0) {
					userId = "'" + strInputDim[i].substring(2) + "'";
				} else {
					userId += ",'" + strInputDim[i].substring(2) + "'";
				}
			} else {
				if (groupId.length() == 0) {
					groupId = "'" + strInputDim[i].substring(2) + "'";
				} else {
					groupId += ",'" + strInputDim[i].substring(2) + "'";
				}
			}
		}

		if (userId.length() > 0) {
			String sql = "select a.user_id,a.user_name from ui_info_user a where a.user_id in("
					+ userId + ")";
			// System.out.println(sql);
			try {
				String[][] arr = WebDBUtil.execQryArray(sql, "");
				if (arr != null && arr.length > 0) {
					for (int i = 0; i < arr.length; i++) {
						sb.append("用户:" + arr[i][0] + " 名称:" + arr[i][1]
								+ "\r\n");
					}
				}
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

		if (groupId.length() > 0) {
			String sql = "select group_id,group_name from ui_info_user_group where group_id in("
					+ groupId + ")";
			// System.out.println(sql);
			try {
				String[][] arr = WebDBUtil.execQryArray(sql, "");
				if (arr != null && arr.length > 0) {
					for (int i = 0; i < arr.length; i++) {
						sb.append("用户组:" + arr[i][0] + " 名称:" + arr[i][1]
								+ "\r\n");
					}
				}
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		// System.out.println("xx:" + sb.toString().trim());
		return sb.toString().trim();
	}
}
