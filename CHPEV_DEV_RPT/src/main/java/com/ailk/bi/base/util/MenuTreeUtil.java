package com.ailk.bi.base.util;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class MenuTreeUtil {

	public static String getTreeHtml(String parentId) {
		StringBuffer sb = new StringBuffer("");
		StringBuffer sql = new StringBuffer();
		try {
			/* db2的查询语句 */
			/*
			 * sql.append(
			 * "WITH RS (res_id,res_name,url,parent_id,local_res_code) AS (")
			 * .append(
			 * "SELECT a.res_id,a.name,a.url,a.parent_id,a.local_res_code FROM UI_PUB_INFO_RESOURCE a WHERE a.parent_id = '"
			 * +parentId+"' ") .append("UNION ALL ") .append(
			 * "SELECT b.res_id,b.name,b.url,b.parent_id,b.local_res_code FROM RS p, UI_PUB_INFO_RESOURCE b "
			 * ) .append("WHERE b.parent_id=p.res_id ) ") .append(
			 * "SELECT DISTINCT res_id, res_name, url,parent_id,local_res_code FROM RS ORDER BY res_id "
			 * );
			 */

			/* Oracle的查询语句 */
			sql.append(
					"select res_id,name,url,parent_id,local_res_code from UI_PUB_INFO_RESOURCE ")
					.append("start with parent_id = '" + parentId
							+ "' connect by prior res_id=parent_id ")
					.append("order by res_id");

			String[][] rs = WebDBUtil.execQryArray(sql.toString(), "");
			int count = 0;
			for (int i = 0; i < rs.length; i++) {

				if (parentId.equals(rs[i][3])) {
					count++;
					sb.append("<tr> \n");
					sb.append("    <td id='menuButton' onclick='display(this,"
							+ count + ");' style=\"cursor:hand\"> \n");
					if ((i + 1) < rs.length && !rs[i + 1][3].equals(parentId)) { // 表示有子节点
						sb.append("       <p class='submenu_menutitle'> "
								+ rs[i][1] + " </p>\n");
						sb.append("    </td> \n");
						sb.append("</tr> \n");
					} else {
						sb.append("       <p class='submenu_l1'> <a href='"
								+ rs[i][2]);
						sb.append("' target='" + rs[i][4]
								+ "' onclick='setColor(this);'>" + rs[i][1]
								+ "</a> \n");
						sb.append("   </p>\n </td> \n");
						sb.append("</tr> \n");
					}
				} else {
					sb.append("<tr style='display:none'> \n");
					sb.append("	<td nowrap id='sub_title" + count + "'> \n");
					sb.append("		<p class='submenu_l2'><a href='" + rs[i][2]);
					sb.append("' target='" + rs[i][4]
							+ "' onclick='setChildColor(this);'>" + rs[i][1]
							+ "</a></p> \n");
					sb.append("	</td> \n");
					sb.append("</tr> \n");
				}
			}

		} catch (AppException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	public static String getTreeHtml(String parentId, String login_no,
			String target) {
		if (target.length() == 0) {
			target = "analysis_contents";
		}
		StringBuffer sb = new StringBuffer("");
		StringBuffer sql = new StringBuffer();
		try {
			/* db2的查询语句 */
			/*
			 * sql.append(
			 * "WITH RS (res_id,res_name,url,parent_id,local_res_code) AS (")
			 * .append(
			 * "SELECT a.res_id,a.name,a.url,a.parent_id,a.local_res_code FROM UI_PUB_INFO_RESOURCE a WHERE a.parent_id = '"
			 * +parentId+"' ") .append("UNION ALL ") .append(
			 * "SELECT b.res_id,b.name,b.url,b.parent_id,b.local_res_code FROM RS p, UI_PUB_INFO_RESOURCE b "
			 * ) .append("WHERE b.parent_id=p.res_id ) ") .append(
			 * "SELECT DISTINCT res_id, res_name, url,parent_id,local_res_code FROM RS ORDER BY res_id "
			 * );
			 */

			/* Oracle的查询语句 */

			sql.append("select menu_code,menu_title,menu_url,parent_menu_code from info_menu");
			sql.append(" where menu_code in(select K.MENU_CODE from rule_oper_role m,info_role n,info_menu k,rule_role_func g");
			sql.append(" where '"
					+ login_no
					+ "'=m.oper_no and m.role_code=n.role_code and g.role_code=m.role_code and g.entity_code=k.menu_code");
			sql.append(" and g.entity_type='M')");

			sql.append(" start with parent_menu_code = '"
					+ parentId
					+ "' connect by prior menu_code=parent_menu_code order by menu_row");

			/*
			 * sql.append(
			 * "select menu_code,menu_title,menu_url,parent_menu_code from info_menu"
			 * ); sql.append(
			 * " where menu_code in(select K.MENU_CODE from rule_oper_role m,info_role n,info_menu k,rule_role_func g"
			 * ); sql.append(" where '" + login_no +
			 * "'=m.oper_no and m.role_code=n.role_code and g.role_code=m.role_code and g.entity_code=k.menu_code"
			 * ); sql.append(" and g.entity_type='M') order by menu_row");
			 */
			// sql.append("select res_id,name,url,parent_id,local_res_code from UI_PUB_INFO_RESOURCE ")
			// .append("start with parent_id = '"+parentId+"' connect by prior res_id=parent_id ")
			// .append("order by res_id");
			System.out.println(sql.toString());

			String[][] rs = WebDBUtil.execQryArray(sql.toString(), "");
			int count = 0;
			for (int i = 0; i < rs.length; i++) {

				if (parentId.equals(rs[i][3])) {
					count++;
					sb.append("<tr> \n");
					sb.append("    <td id='menuButton' onclick='display(this,"
							+ count + ");' style=\"cursor:hand\"> \n");
					if ((i + 1) < rs.length && !rs[i + 1][3].equals(parentId)) { // 表示有子节点
						sb.append("       <p class='submenu_menutitle'> "
								+ rs[i][1] + " </p>\n");
						sb.append("    </td> \n");
						sb.append("</tr> \n");
					} else {
						sb.append("       <p class='submenu_l1'> <a href='"
								+ rs[i][2]);
						sb.append("' target='" + target
								+ "' onclick='setColor(this);'>" + rs[i][1]
								+ "</a> \n");
						sb.append("   </p>\n </td> \n");
						sb.append("</tr> \n");
					}
				} else {
					sb.append("<tr style='display:none'> \n");
					sb.append("	<td nowrap id='sub_title" + count + "'> \n");
					sb.append("		<p class='submenu_l2'><a href='" + rs[i][2]);
					sb.append("' target='" + target
							+ "' onclick='setChildColor(this);'>" + rs[i][1]
							+ "</a></p> \n");
					sb.append("	</td> \n");
					sb.append("</tr> \n");
				}
			}

		} catch (AppException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}
}
