package com.ailk.bi.adhoc.util;

import java.util.ArrayList;

import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;

@SuppressWarnings( { "rawtypes", "unchecked" })
public class AdhocTreeHelper {

	private static final String BR_STR = "\n";

	private static final String BAR_STR = "submenu";

	/**
	 * 生成条件合度量属性分组层次菜单
	 * 
	 * @param conMeta
	 * @param hoc_id
	 * @param type
	 * @return
	 */
	public static String getGroupTreeHtml(UiAdhocGroupMetaTable[] conMeta,
			String hoc_id, String type) {

		StringBuffer sb = new StringBuffer();
		// 定制内容！
		sb.append(getGroupBarHtml(type));
		if (!"3".equals(type)) {// 纬度
			if ("1".equals(type)) {// 指标
				sb.append("<div class=\"childnodebox\">").append(BR_STR);
			} else {
				sb
						.append(
								"<div class=\"childnodebox\" style=\"display:none\">")
						.append(BR_STR);
			}
		}
		int childnodenum = 0; // 子菜单顺序，大于1的时候，默认收起
		// 二级菜单
		for (int i = 0; conMeta != null && i < conMeta.length; i++) {
			if (conMeta[i].getLtree().isEmpty()
					&& !"".equals(conMeta[i].getGroup_tag())) {// 根连接
				sb.append("<div class=\"node1\">").append(BR_STR);
				sb
						.append(
								"<a href=\"javascript:parent.adhoc_mainFrame.switchTable(parent.adhoc_mainFrame.tableArr,'"
										+ conMeta[i].getGroup_tag()
										+ "','"
										+ hoc_id
										+ "');\" title="
										+ conMeta[i].getGroup_desc()
										+ ">"
										+ conMeta[i].getGroup_name() + "</a>")
						.append(BR_STR);
				sb.append("</div>").append(BR_STR);
			} else {// 下级连接
				childnodenum++;
				String tmpclass = " class=\"icon1 open\"";
				if (childnodenum > 1) {
					tmpclass = " class=\"icon1\"";
				}
				sb.append("<div class=\"node1\">").append(BR_STR);
				sb.append(
						"<a href=\"javascript:;\" " + " id=icon"
								+ conMeta[i].getGroup_id() + tmpclass + ">"
								+ conMeta[i].getGroup_name() + "</a>").append(
						BR_STR);
				sb.append("</div>").append(BR_STR);
			}
			// 三级菜单
			if (conMeta[i].getLtree() != null
					&& "".equals(conMeta[i].getGroup_tag())) {
				if (childnodenum > 1) {
					sb
							.append(
									"<div class=\"node1_box childBox\" style=\"display: none\">")
							.append(BR_STR);
				} else {
					sb.append("<div class=\"node1_box childBox\">").append(
							BR_STR);
				}

				ArrayList tmplist = (ArrayList) conMeta[i].getLtree();
				UiAdhocGroupMetaTable[] tmpMeta = (UiAdhocGroupMetaTable[]) tmplist
						.toArray(new UiAdhocGroupMetaTable[tmplist.size()]);
				for (int j = 0; tmpMeta != null && j < tmpMeta.length; j++) {
					if (tmpMeta[j].getLtree().isEmpty()
							&& !"".equals(tmpMeta[j].getGroup_tag())) {
						sb.append("<div class=\"node3\">").append(BR_STR);
						sb
								.append(
										"<a class=\"icon1\" href=\"javascript:parent.adhoc_mainFrame.switchTable(parent.adhoc_mainFrame.tableArr,'"
												+ tmpMeta[j].getGroup_tag()
												+ "','"
												+ hoc_id
												+ "');\" title="
												+ tmpMeta[j].getGroup_desc()
												+ ">"
												+ tmpMeta[j].getGroup_name()
												+ "</a>").append(BR_STR);
						sb.append("</div>").append(BR_STR);
					} else {
						sb.append("<div class=\"node3\">").append(BR_STR);
						sb.append(tmpMeta[j].getGroup_name()).append(BR_STR);
						sb.append("</div>").append(BR_STR);
					}

					// 四级菜单
					if (tmpMeta[j].getLtree() != null
							&& "".equals(tmpMeta[j].getGroup_tag())) {
						ArrayList endList = (ArrayList) tmpMeta[j].getLtree();
						UiAdhocGroupMetaTable[] endMeta = (UiAdhocGroupMetaTable[]) endList
								.toArray(new UiAdhocGroupMetaTable[endList
										.size()]);
						for (int t = 0; endMeta != null && t < endMeta.length; t++) {
							sb.append("<tr>").append(BR_STR);
							sb.append("<td>&nbsp;</td>").append(BR_STR);
							sb
									.append(
											"<td><img src=\"../biimages/tree_step.gif\"> <a href=\"javascript:parent.parent.adhoc_mainFrame.switchTable(parent.parent.adhoc_mainFrame.tableArr,'"
													+ endMeta[t].getGroup_tag()
													+ "','"
													+ hoc_id
													+ "');\" title="
													+ endMeta[t]
															.getGroup_desc()
													+ " >"
													+ endMeta[t]
															.getGroup_name()
													+ "</a></td>").append(
											BR_STR);
							sb.append("</tr>").append(BR_STR);
							// img
							sb.append("<tr>").append(BR_STR);
							sb.append("</tr> ").append(BR_STR);
						}// end tree
					}

				}// end two
				sb.append("</div>").append(BR_STR);
			}

		}// end root
		if (!"2".equals(type)) {// 纬度
			sb.append("</div>").append(BR_STR);
		}

		return sb.toString();
	}

	/**
	 * 查询单一层次树
	 * 
	 * @param meta
	 * @param hoc_id
	 * @param type
	 * @return
	 */
	public static String getSingleGroupTreeHtml(UiAdhocGroupMetaTable[] meta,
			String hoc_id, String type) {

		StringBuffer sb = new StringBuffer();
		// 定制内容！
		sb.append(getGroupBarHtml(type));
		sb.append("<tr id=\"" + BAR_STR + type + "\"> ").append(BR_STR);
		sb.append("<td colspan=\"3\"> ").append(BR_STR);
		sb.append("<table width=\"100%\" border=\"0\"> ").append(BR_STR);
		//
		for (int i = 0; i < meta.length; i++) {
			sb.append("<tr>").append(BR_STR);
			sb
					.append(
							"<td align=\"right\"><img src=\"../biimages/button_open.gif\" width=\"8\" height=\"8\"></td>")
					.append(BR_STR);
			sb
					.append(
							"<td width=\"90%\"> <a href=\"javascript:parent.parent.adhoc_mainFrame.switchTable(parent.parent.adhoc_mainFrame.tableArr,'"
									+ meta[i].getGroup_tag()
									+ "','"
									+ hoc_id
									+ "');\" title="
									+ meta[i].getGroup_desc()
									+ ">"
									+ meta[i].getGroup_name()
									+ "</a></td>").append(BR_STR);
			sb.append("</tr> ").append(BR_STR);
			// img
			sb.append("<tr>").append(BR_STR);
			// sb.append("<td height=\"1\" colspan=\"2\"
			// background=\"../biimages/black-dot.gif\"></td>");
			sb.append("</tr> ").append(BR_STR);
		}
		sb.append("</table> ").append(BR_STR);
		sb.append("</td> ").append(BR_STR);
		sb.append("</tr> ").append(BR_STR);

		return sb.toString();

	}

	/**
	 * 定制分析条页面代码menu_1
	 * 
	 * @param type
	 * @return
	 */
	public static String getGroupBarHtml(String type) {
		StringBuffer sb = new StringBuffer();

		if ("1".equals(type)) {// 条件
			sb.append("<div class=\"childtreenode\">条件定制</div>").append(BR_STR);
		} else if ("2".equals(type)) {// 纬度
			sb.append("<div class=\"childtreenode\">结果定制</div>").append(BR_STR);
		} else if ("3".equals(type)) {// 度量

		} else if ("4".equals(type)) {// 收藏夹
			sb.append("<div class=\"childtreenode\">热点收藏夹</div>")
					.append(BR_STR);
		}
		return sb.toString();
	}

}
