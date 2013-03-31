package com.ailk.bi.adhoc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.dao.impl.AdhocFavDao;
import com.ailk.bi.adhoc.dao.impl.AdhocTreeDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocDimMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocGroupMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocMsuMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.domain.UiAdhocSumInfoTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.service.impl.AdhocTreeFacade;
import com.ailk.bi.adhoc.service.impl.ChannelPlugInFactory;
import com.ailk.bi.adhoc.struct.AdhocViewStruct;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

/**
 * AdhocHelper类是业务辅助类，它区别于常规的工具类但是与之相似
 *
 * 在职责上辅助类只处理特定业务逻辑处理
 *
 * @author chunming
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocHelper {

	private static final String BR_STR = "\n";

	/**
	 * 根据即席查询业务定义hoc_id 取的条件定制区分组列表
	 *
	 * @param hoc_id
	 * @return
	 */
	public static String getConditionTreeHtml(String hoc_id) {
		StringBuffer sb = new StringBuffer("");
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		ArrayList conList = facade.getConGroupListByHocId(hoc_id);
		if (conList != null && !conList.isEmpty()) {
			//
			sb.append("<tr>\n");
			sb.append("<td width=\"32\"><img src=\"../biimages/left-ico1.gif\" width=\"32\" height=\"23\"></td>\n");
			sb.append("<td width=\"100%\" class=\"left-menu\"><a href=\"javascript:;\" >条件区定制</a></td>\n");
			sb.append("<td width=\"5\"><img src=\"../biimages/left-ico2.gif\" width=\"5\" height=\"23\"></td>\n");
			sb.append("</tr>\n");

			//
			sb.append("<tr id=\"submenu1\">\n");
			sb.append("<td colspan=\"3\">\n");
			sb.append("<table width=\"100%\" border=\"0\">\n");
			UiAdhocGroupMetaTable[] conGroup = (UiAdhocGroupMetaTable[]) conList
					.toArray(new UiAdhocGroupMetaTable[conList.size()]);
			for (int i = 0; conGroup != null && i < conGroup.length; i++) {
				sb.append("<tr>\n");
				sb.append("<td width=\"25\" align=\"center\"><img src=\"../biimages/arrow7.gif\" width=\"7\" height=\"7\"></td>\n");
				sb.append("<td><a href=\"javascript:parent.parent.adhoc_mainFrame.switchTable(parent.parent.adhoc_mainFrame.tableArr,'"
						+ conGroup[i].getGroup_tag()
						+ "','"
						+ hoc_id
						+ "');\">"
						+ conGroup[i].getGroup_name()
						+ "</a></td>\n");
				sb.append("</tr>\n");
				sb.append("<tr>\n");
				sb.append("<td height=\"1\" colspan=\"2\" background=\"../biimages/black-dot.gif\" ></td>\n");
				sb.append("</tr>\n");
			}
			sb.append("</table>\n");
			sb.append("</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td colspan=\"3\" height=\"2\"></td>\n");
			sb.append("</tr>\n");

		}

		return sb.toString();

	}

	/**
	 * 提取用户收藏夹
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getFavoriteTreeHtml(String hoc_id, HttpSession session) {
		StringBuffer sb = new StringBuffer("");

		String oper_no = CommonFacade.getLoginId(session);
		AdhocFacade facade = new AdhocFacade(new AdhocFavDao());
		ArrayList favList = facade.getFavoriteListByOper(oper_no, hoc_id);

		ArrayList listFav = new ArrayList();
		ArrayList listFavDetail = new ArrayList();

		if (favList != null && !favList.isEmpty()) {

			for (int m = 0; m < favList.size(); m++) {
				UiAdhocFavoriteDefTable fav = (UiAdhocFavoriteDefTable) favList
						.get(m);

				if (fav.getFavTypeFlag() == 1) {// 即席查询
					listFav.add(fav);
				} else {// 清单
					listFavDetail.add(fav);
				}
			}

			sb.append("<div class=\"childtreenode\">热点收藏夹</div>").append(BR_STR);
			sb.append("<div class=\"childnodebox\">");//js-add

			if (listFav.size() > 0) {
				sb.append("<div class=\"node1\"><a href=\"javascript:;\" id=iconMYADHOCFAV class=\"icon1 open\">即席收藏</a></div>");
				sb.append("<div class=\"node1_box childBox\">");
			}

			UiAdhocFavoriteDefTable[] favArr = (UiAdhocFavoriteDefTable[]) listFav
					.toArray(new UiAdhocFavoriteDefTable[listFav.size()]);
			for (int i = 0; favArr != null && i < favArr.length; i++) {
				sb.append("<div class=\"node3\">");
				sb.append("<a class=\"icon1\" href=\"javascript:parent.adhoc_mainFrame.ShowFavorite('"
						+ favArr[i].getFavorite_id()
						+ "','"
						+ favArr[i].getFavorite_name()
						+ "');\">"
						+ favArr[i].getFavorite_name() + "</a>");
				sb.append("</div>");
			}

			if (listFav.size() > 0) {
				sb.append("</div>");
			}

			if (listFavDetail.size() > 0) {
				sb.append("<div class=\"node1\"><a href=\"javascript:;\" id=iconMYADHOCDTL class=\"icon1 open\">清单收藏</a></div>");
				sb.append("<div class=\"node1_box childBox\">");
			}

			favArr = (UiAdhocFavoriteDefTable[]) listFavDetail
					.toArray(new UiAdhocFavoriteDefTable[listFavDetail.size()]);
			for (int i = 0; favArr != null && i < favArr.length; i++) {
				sb.append("<div class=\"node3\">");
				sb.append("<a class=\"icon1\" href=\"javascript:parent.adhoc_mainFrame.ShowFavorite('"
						+ favArr[i].getFavorite_id()
						+ "','"
						+ favArr[i].getFavorite_name()
						+ "');\">"
						+ favArr[i].getFavorite_name() + "</a>");
				sb.append("</div>");
			}
			if (listFavDetail.size() > 0) {
				sb.append("</div>");
			}

			sb.append("</div>");
		}

		return sb.toString();

	}

	/**
	 * 提取即席查询属性分组标记
	 *
	 * @param hoc_id
	 * @return
	 */
	public static String getAdhocTag(String hoc_id) {
		StringBuffer sb = new StringBuffer("");
		AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
		List conList = facade.getAdhocGroupMetaHierarchyList(hoc_id);
		if (!conList.isEmpty()) {
			ArrayList conArr = (ArrayList) conList;
			UiAdhocGroupMetaTable[] conGroup = (UiAdhocGroupMetaTable[]) conArr
					.toArray(new UiAdhocGroupMetaTable[conArr.size()]);
			for (int i = 0; conGroup != null && i < conGroup.length; i++) {
				if (conGroup[i].getGroup_tag() != null
						&& !"".equals(conGroup[i].getGroup_tag())) {
					sb.append("\"" + conGroup[i].getGroup_tag() + "\",");
				}
			}
		}

		if (sb.toString().length() == 0) {
			return "";
		} else {
			return sb.toString().substring(0, sb.toString().length() - 1);
		}

	}

	/**
	 * 提取即席查询属性分组标记
	 *
	 * @param hoc_id
	 * @return
	 */
	public static String getAdhocTag(String hoc_id, HttpSession session) {
		String role_id = CommonFacade.getUserAdhocRole(session);

		StringBuffer sb = new StringBuffer("");
		AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
		List conList = facade.getAdhocGroupMetaHierarchyList(hoc_id, role_id);
		if (!conList.isEmpty()) {
			ArrayList conArr = (ArrayList) conList;
			UiAdhocGroupMetaTable[] conGroup = (UiAdhocGroupMetaTable[]) conArr
					.toArray(new UiAdhocGroupMetaTable[conArr.size()]);
			for (int i = 0; conGroup != null && i < conGroup.length; i++) {
				if (conGroup[i].getGroup_tag() != null
						&& !"".equals(conGroup[i].getGroup_tag())) {
					sb.append("\"" + conGroup[i].getGroup_tag() + "\",");
				}
			}
		}

		if (sb.toString().length() == 0) {
			return "";
		} else {
			return sb.toString().substring(0, sb.toString().length() - 1);
		}

	}

	/**
	 * 提取即席查询默认属性分组标记
	 *
	 * @param hoc_id
	 * @return
	 */
	public static String getAdhocDefaultTag(String hoc_id) {
		String defaultTag = "";
		AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
		List conList = facade.getAdhocGroupMetaHierarchyList(hoc_id);
		if (conList != null && !conList.isEmpty()) {
			ArrayList conArr = (ArrayList) conList;
			UiAdhocGroupMetaTable[] conGroup = (UiAdhocGroupMetaTable[]) conArr
					.toArray(new UiAdhocGroupMetaTable[conArr.size()]);
			if (conGroup != null && conGroup.length > 0) {
				for (int i = 0; conGroup != null && i < conGroup.length; i++) {
					if (conGroup[i].getGroup_tag() != null
							&& !"".equals(conGroup[i].getGroup_tag())) {
						defaultTag = conGroup[i].getGroup_tag();
						break;
					}
				}

			}
		}
		return defaultTag;
	}

	/**
	 * 提取即席查询默认属性分组标记
	 *
	 * @param hoc_id
	 * @return
	 */
	public static String getAdhocMsuDefaultTag(String hoc_id) {
		String defaultTag = "";
		AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
		List msuList = facade.getAdhocGroupMetaHierarchyList(hoc_id);
		if (msuList != null && !msuList.isEmpty()) {
			ArrayList conArr = (ArrayList) msuList;
			UiAdhocGroupMetaTable[] msuGroup = (UiAdhocGroupMetaTable[]) conArr
					.toArray(new UiAdhocGroupMetaTable[conArr.size()]);
			if (msuGroup != null && msuGroup.length > 0) {
				for (int i = 0; msuGroup != null && i < msuGroup.length; i++) {
					if (msuGroup[i].getGroup_tag() != null
							&& !"".equals(msuGroup[i].getGroup_tag())
							&& msuGroup[i].getGroup_belong().equals("M")) {
						defaultTag = msuGroup[i].getGroup_tag();
						break;
					}
				}
			}
		}
		return defaultTag;
	}

	/**
	 * 条件选择区定制表格生成
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getConditionTableHtml(String hoc_id,
			HttpSession session) {
		StringBuffer sb = new StringBuffer("");

		String role_id = CommonFacade.getUserAdhocRole(session);

		// 当前条件数组
		String[] sessionList = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
		if (sessionList == null) {
			sessionList = new String[0];
		}
		//
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		// 默认条件需要控制不能够再次选择
		HashSet defaultset = new HashSet();
		ArrayList defaultList = (ArrayList) facade.getDefaultConListByHocId(
				hoc_id, role_id);
		// 压入默认条件
		session.setAttribute(AdhocConstant.ADHOC_DEFAULT_CONDITION_LIST,
				defaultList);

		if (defaultList != null && !defaultList.isEmpty()) {
			for (int i = 0; i < defaultList.size(); i++) {
				UiAdhocConditionMetaTable meta = (UiAdhocConditionMetaTable) defaultList
						.get(i);
				defaultset.add(meta.getCon_id());
			}
		}
		// 条件区定制分组属性
		List conList = facade.getConGroupListByHocId(hoc_id, role_id);
		if (conList != null && !conList.isEmpty()) {
			ArrayList conArr = (ArrayList) conList;
			UiAdhocGroupMetaTable[] conGroup = (UiAdhocGroupMetaTable[]) conArr
					.toArray(new UiAdhocGroupMetaTable[conArr.size()]);
			for (int i = 0; conGroup != null && i < conGroup.length; i++) {
				String tmpTableID = conGroup[i].getGroup_tag();
				ArrayList conition = (ArrayList) facade
						.getConditionListByGroupId(conGroup[i].getGroup_id(),
								hoc_id, role_id);
				UiAdhocConditionMetaTable[] con_meta = (UiAdhocConditionMetaTable[]) conition
						.toArray(new UiAdhocConditionMetaTable[conition.size()]);
				sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" id=\""
						+ tmpTableID + "\" style=\"display:none\">\n");

				for (int j = 0; con_meta != null && j < con_meta.length; j++) {
					if (j % 5 == 0) {
						sb.append("<tr>\n");
					}

					String conditionValue = "con" + con_meta[j].getCon_id();

					if (AdhocUtil.isChecked(sessionList,
							con_meta[j].getCon_id())) {
						sb.append("<td height=\"18\" title=\""
								+ con_meta[j].getCon_desc()
								+ "\"><input type=\"checkbox\" name=\""
								+ AdhocConstant.ADHOC_CONDITION_NAME
								+ "\" value=\"" + conditionValue
								+ "\" checked>" + con_meta[j].getCon_name()
								+ "</td>\n");
					} else {// 默认既是选中
						if (AdhocUtil.isDefaultChecked(defaultset,
								con_meta[j].getCon_id())) {
							sb.append("<td height=\"18\" title=\""
									+ con_meta[j].getCon_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_CONDITION_NAME
									+ "\" value=\"" + conditionValue
									+ "\" checked disabled>"
									+ con_meta[j].getCon_name() + "</td>\n");
						} else {
							sb.append("<td height=\"18\" title=\""
									+ con_meta[j].getCon_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_CONDITION_NAME
									+ "\" value=\"" + conditionValue + "\">"
									+ con_meta[j].getCon_name() + "</td>\n");
						}

					}

					if ((j % 5 == 4) || (j == con_meta.length - 1)) {
						if ((j == con_meta.length - 1) && (j % 5 != 4)) {
							int count = 5 - ((con_meta.length) % 5);
							sb.append(AdhocUtil.getNbspTdInnerHtml(count));
						}
						sb.append("</tr>\n");
					}

				}
				sb.append("</table>\n");
			}

		}

		return sb.toString();

	}

	/**
	 * 纬度定制区表格生成
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getDimTableHtml(String hoc_id, HttpSession session) {
		StringBuffer sb = new StringBuffer("");

		String role_id = CommonFacade.getUserAdhocRole(session);

		// 当前纬度数组
		String[] sessionList = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
		if (sessionList == null) {
			sessionList = new String[0];
		}
		//
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		// 默认纬度需要控制不能够再次选择
		HashSet defaultset = new HashSet();
		ArrayList defaultList = (ArrayList) facade.getDefaultDimListByHocId(
				hoc_id, role_id);
		if (defaultList != null && !defaultList.isEmpty()) {
			for (int i = 0; i < defaultList.size(); i++) {
				UiAdhocDimMetaTable meta = (UiAdhocDimMetaTable) defaultList
						.get(i);
				defaultset.add(meta.getDim_id());
			}
		}
		// 纬度定制分组属性
		List dimList = facade.getDimGroupListByHocId(hoc_id, role_id);
		if (dimList != null && !dimList.isEmpty()) {
			ArrayList dimArr = (ArrayList) dimList;
			UiAdhocGroupMetaTable[] dimGroup = (UiAdhocGroupMetaTable[]) dimArr
					.toArray(new UiAdhocGroupMetaTable[dimArr.size()]);
			for (int i = 0; dimGroup != null && i < dimGroup.length; i++) {
				// String tmpTableID = AdhocConstant.ADHOC_DIM_TABLE_TAG;
				String tmpTableID = dimGroup[i].getGroup_tag();
				ArrayList dim = (ArrayList) facade.getDimListByGroupId(
						dimGroup[i].getGroup_id(), hoc_id, role_id);
				UiAdhocDimMetaTable[] dim_meta = (UiAdhocDimMetaTable[]) dim
						.toArray(new UiAdhocDimMetaTable[dim.size()]);

				sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" id=\""
						+ tmpTableID + "\" style=\"display:none\">\n");

				for (int j = 0; dim_meta != null && j < dim_meta.length; j++) {
					if (j % 5 == 0) {
						sb.append("<tr>\n");
					}

					String dimValue = "wd" + dim_meta[j].getDim_id();

					if (AdhocUtil.isChecked(sessionList,
							dim_meta[j].getDim_id())) {
						sb.append("<td height=\"18\" title=\""
								+ dim_meta[j].getDim_desc()
								+ "\"><input type=\"checkbox\" name=\""
								+ AdhocConstant.ADHOC_DIM_NAME + "\" value=\""
								+ dimValue + "\" checked>"
								+ dim_meta[j].getDim_name());
						if (!"".equals(dim_meta[j].getDim_relation_field())) {
							sb.append("&nbsp;<a href=\"javascript:;\" title=\"自定义纬度\" ><img src=\"../biimages/set.gif\"  border=\"0\" onclick=\"doFixedDim('"
									+ hoc_id
									+ "','"
									+ dim_meta[j].getDim_relation_field()
									+ "')\"></a>");
						}
						sb.append("</td>\n");
					} else {
						if (AdhocUtil.isDefaultChecked(defaultset,
								dim_meta[j].getDim_id())) {
							sb.append("<td height=\"18\" title=\""
									+ dim_meta[j].getDim_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_DIM_NAME
									+ "\" value=\"" + dimValue
									+ "\" checked disabled>"
									+ dim_meta[j].getDim_name());
							if (!"".equals(dim_meta[j].getDim_relation_field())) {
								sb.append("&nbsp;<a href=\"javascript:;\" title=\"自定义纬度\" ><img src=\"../biimages/set.gif\"  border=\"0\" onclick=\"doFixedDim('"
										+ hoc_id
										+ "','"
										+ dim_meta[j].getDim_relation_field()
										+ "')\"></a>");
							}
							sb.append("</td>\n");
						} else {
							sb.append("<td height=\"18\" title=\""
									+ dim_meta[j].getDim_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_DIM_NAME
									+ "\" value=\"" + dimValue + "\">"
									+ dim_meta[j].getDim_name());
							if (!"".equals(dim_meta[j].getDim_relation_field())) {
								sb.append("&nbsp;<a href=\"javascript:;\" title=\"自定义纬度\" ><img src=\"../biimages/set.gif\"  border=\"0\" onclick=\"doFixedDim('"
										+ hoc_id
										+ "','"
										+ dim_meta[j].getDim_relation_field()
										+ "')\"></a>");
							}
							sb.append("</td>\n");
						}
					}

					if ((j % 5 == 4) || (j == dim_meta.length - 1)) {
						if ((j == dim_meta.length - 1) && (j % 5 != 4)) {
							int count = 5 - ((dim_meta.length) % 5);
							sb.append(AdhocUtil.getNbspTdInnerHtml(count));
						}
						sb.append("</tr>\n");
					}

				}
				sb.append("</table>\n");
			}

		}

		return sb.toString();
	}

	/**
	 * 指标定制区表格生成
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getMsuTableHtml(String hoc_id, HttpSession session) {
		StringBuffer sb = new StringBuffer("");
		String role_id = CommonFacade.getUserAdhocRole(session);

		// 当前纬度数组
		String[] sessionList = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
		if (sessionList == null) {
			sessionList = new String[0];
		}
		//
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		// 默认纬度需要控制不能够再次选择
		HashSet defaultset = new HashSet();
		ArrayList defaultList = (ArrayList) facade.getDefaultMsuListByHocId(
				hoc_id, role_id);
		if (defaultList != null && !defaultList.isEmpty()) {
			for (int i = 0; i < defaultList.size(); i++) {
				UiAdhocMsuMetaTable meta = (UiAdhocMsuMetaTable) defaultList
						.get(i);
				defaultset.add(meta.getMsu_id());
			}
		}
		// 纬度定制分组属性
		List msuList = facade.getMsuGroupListByHocId(hoc_id, role_id);
		if (msuList != null && !msuList.isEmpty()) {
			ArrayList msuArr = (ArrayList) msuList;
			UiAdhocGroupMetaTable[] msuGroup = (UiAdhocGroupMetaTable[]) msuArr
					.toArray(new UiAdhocGroupMetaTable[msuArr.size()]);

			for (int i = 0; msuGroup != null && i < msuGroup.length; i++) {
				String tmpTableID = msuGroup[i].getGroup_tag();
				sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" id=\""
						+ tmpTableID + "\" style=\"display:none\">\n");

				ArrayList msu = (ArrayList) facade.getMsuListByGroupId(
						msuGroup[i].getGroup_id(), hoc_id, role_id);
				UiAdhocMsuMetaTable[] msu_meta = (UiAdhocMsuMetaTable[]) msu
						.toArray(new UiAdhocMsuMetaTable[msu.size()]);
				for (int j = 0; msu_meta != null && j < msu_meta.length; j++) {
					if (j % 5 == 0) {
						sb.append("<tr>\n");
					}
					String msuValue = "msu" + msu_meta[j].getMsu_id();
					if (AdhocUtil.isChecked(sessionList,
							msu_meta[j].getMsu_id())) {
						sb.append("<td width=\"20%\" height=\"18\" title=\""
								+ msu_meta[j].getMsu_desc()
								+ "\"><input type=\"checkbox\" name=\""
								+ AdhocConstant.ADHOC_MSU_NAME + "\" value=\""
								+ msuValue + "\" checked>"
								+ msu_meta[j].getMsu_name() + "</td>\n");

					} else {

						if (AdhocUtil.isDefaultChecked(defaultset,
								msu_meta[j].getMsu_id())) {
							sb.append("<td width=\"20%\" height=\"18\" title=\""
									+ msu_meta[j].getMsu_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_MSU_NAME
									+ "\" value=\""
									+ msuValue
									+ "\" checked disabled>"
									+ msu_meta[j].getMsu_name() + "</td>\n");
						} else {
							sb.append("<td width=\"20%\" height=\"18\" title=\""
									+ msu_meta[j].getMsu_desc()
									+ "\"><input type=\"checkbox\" name=\""
									+ AdhocConstant.ADHOC_MSU_NAME
									+ "\" value=\""
									+ msuValue
									+ "\">"
									+ msu_meta[j].getMsu_name() + "</td>\n");
						}

					}
					if ((j % 5 == 4) || (j == msu_meta.length - 1)) {
						if ((j == msu_meta.length - 1) && (j % 5 != 4)) {
							int count = 5 - ((msu_meta.length) % 5);
							sb.append(AdhocUtil.getNbspTdInnerHtml(count));
						}
						sb.append("</tr>\n");
					}

				}
				sb.append("</table>\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 定制区HTML代码
	 *
	 * @param hoc_id
	 *            即席查询功能ID
	 * @param session
	 *            会话
	 * @return
	 */
	public static String getCustomizeZoneTableHtml(String hoc_id,
			HttpSession session) {
		StringBuffer sb = new StringBuffer("<tr>\n");
		sb.append("<td colspan=\"3\" class=\"search-bg\">\n");
		sb.append(getConditionTableHtml(hoc_id, session));
		sb.append(getDimTableHtml(hoc_id, session));
		sb.append(getMsuTableHtml(hoc_id, session));
		sb.append("</td>\n");
		sb.append("</tr>\n");

		return sb.toString();
	}

	/**
	 * 已选指标
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getSelectedMsuResult(String hoc_id, HttpSession session) {
		String role_id = CommonFacade.getUserAdhocRole(session);

		StringBuffer sb = new StringBuffer("<tr>\n");
		sb.append("<td colspan=\"3\" class=\"search-bg_old\">\n");

		// 已选纬度（度量）
		String[] msuArr = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
		if (msuArr == null) {
			msuArr = new String[0];
		}
		String msuStr = "";
		for (int i = 0; msuArr != null && i < msuArr.length; i++) {
			if (msuStr.length() > 0) {
				msuStr += ",";
			}
			msuStr += "'" + msuArr[i] + "'";
		}
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		ArrayList selectedList = null;
		// 已经选中
		if (!"".equals(msuStr)) {
			selectedList = (ArrayList) facade.getMsuListByString(msuStr,
					hoc_id, role_id);
		}

		// 默认选择纬度
		ArrayList msuList = (ArrayList) facade.getDefaultMsuListByHocId(hoc_id,
				role_id);

		sb.append("<div class=\"fd_content\"><div class=\"widget_zj\">");
		sb.append("<table >");
		sb.append("<tr align=\"center\">");
		sb.append("<th colspan=\"4\" align=\"center\" class=\"title-bulebg_old\">已选择指标</th>");
		sb.append("</tr>");
		sb.append("<tr class=\"table-gay-bg2\">");
		sb.append("<th align=\"center\" class=\"subject-title_old\" width=\"25%\">显示内容</th>");
		sb.append("<th align=\"center\" class=\"subject-title_old\" width=\"25%\">是否显示</th>");
		sb.append("<th align=\"center\" class=\"subject-title_old\" width=\"25%\">删  除</th>");
		sb.append("<th align=\"center\" class=\"last\" width=\"25%\">顺序调整</th>");
		sb.append("</tr>");

		// 融和
		ArrayList list = new ArrayList();
		if (msuList != null && !msuList.isEmpty()) {
			for (int i = 0; i < msuList.size(); i++) {
				list.add(msuList.get(i));

			}

		}
		//
		if (selectedList != null && !selectedList.isEmpty()) {
			for (int i = 0; i < selectedList.size(); i++) {
				list.add(selectedList.get(i));
			}
		}
		//
		if (list != null && !list.isEmpty()) {
			//
			ArrayList arr = (ArrayList) list;
			UiAdhocMsuMetaTable[] msu_meta = (UiAdhocMsuMetaTable[]) arr
					.toArray(new UiAdhocMsuMetaTable[arr.size()]);
			for (int i = 0; msu_meta != null && i < msu_meta.length; i++) {

				sb.append("<tr class=\"table-white-bg_old\">");
				sb.append(
						"<td width=\"25%\" align=\"center\" title=\""
								+ msu_meta[i].getMsu_desc().trim() + "\">")
						.append(msu_meta[i].getMsu_name());
				sb.append(
						"<input type=\"hidden\" name=\"H_"
								+ AdhocConstant.ADHOC_MSU_CHECK_NAME
								+ "\" value=\"" + msu_meta[i].getMsu_id()
								+ "\"/>").append("\n");
				sb.append("</td>");

				if (msu_meta[i].getGroup_id().equals("")) {
					sb.append("<td width=\"25%\" align=\"center\">&nbsp;<input type=\"checkbox\" name=\""
							+ AdhocConstant.ADHOC_MSU_CHECK_NAME
							+ "\" value=\""
							+ msu_meta[i].getMsu_id()
							+ "\" checked ></td>");
				} else {
					sb.append("<td width=\"25%\" align=\"center\">&nbsp;<input type=\"checkbox\" name=\""
							+ AdhocConstant.ADHOC_MSU_CHECK_NAME
							+ "\" value=\""
							+ msu_meta[i].getMsu_id()
							+ "\" checked disabled ></td>");
				}
				if (msu_meta[i].isDefault()) {
					sb.append("<td align=\"center\"><a href=\":;\" class=\"icon del\"></a></td>\n");

				} else {
					sb.append("<td align=\"center\"><a href=\"javascript:;\" class=\"icon del\" onclick=\"deleteFixed('msu','" + msu_meta[i].getMsu_id() + "')\"></a></td>\n");
				}

				sb.append("<td align=\"center\" class=\"last\">");
				sb.append("<a href=\"javascript:;\" class=\"icon1 up\" onclick=\"moveUp(this,2)\"></a>&nbsp;<a href=\"javascript:;\" class=\"icon1 down\"  onclick=\"moveDown(this)\"></a>");
				sb.append("</td>");


				sb.append("</tr>");

			}
		}

		sb.append("</table>\n");
		sb.append("</td>\n");
		sb.append("</tr>\n");
		return sb.toString();
	}

	/**
	 * 已选纬度
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */
	public static String getSelectedDimResult(String hoc_id, HttpSession session) {

		String role_id = CommonFacade.getUserAdhocRole(session);

		StringBuffer sb = new StringBuffer("<tr>\n");
		sb.append("<td colspan=\"3\" class=\"search-bg_old\">\n");

		// 已选纬度
		String[] dimArr = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
		if (dimArr == null) {
			dimArr = new String[0];
		}
		String dimStr = "";
		for (int i = 0; dimArr != null && i < dimArr.length; i++) {
			if (dimStr.length() > 0) {
				dimStr += ",";
			}
			dimStr += "'" + dimArr[i] + "'";
		}
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		ArrayList selectedList = null;
		// 已经选中
		if (!"".equals(dimStr)) {
			selectedList = (ArrayList) facade.getDimListByString(dimStr,
					hoc_id, role_id);
		}

		// 默认选择纬度
		ArrayList dimList = (ArrayList) facade.getDefaultDimListByHocId(hoc_id,
				role_id);
		sb.append("<div class=\"fd_content\"><div class=\"widget_zj\">");

		sb.append("<table >\n");
		sb.append("<tr align=\"center\">\n");
		sb.append("<th colspan=\"4\" class=\"title-bulebg\">已选择维度</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr class=\"table-gay-bg2_old\">\n");
		sb.append("<th align=\"center\" class=\"\" width=\"25%\">显示内容</td>\n");
		sb.append("<th align=\"center\" class=\"\" width=\"25%\">是否显示</td>\n");
		sb.append("<th align=\"center\" class=\"\" width=\"25%\">删  除</td>\n");
		sb.append("<th align=\"center\" class=\"last\" width=\"25%\">显示顺序</td>\n");

		sb.append("</tr>\n");

		// 融和
		ArrayList list = new ArrayList();
		if (dimList != null && !dimList.isEmpty()) {
			for (int i = 0; i < dimList.size(); i++) {
				list.add(dimList.get(i));

			}

		}
		//
		if (selectedList != null && !selectedList.isEmpty()) {
			for (int i = 0; i < selectedList.size(); i++) {
				list.add(selectedList.get(i));
			}
		}
		//
		if (list != null && !list.isEmpty()) {
			//
			ArrayList arr = (ArrayList) list;
			UiAdhocDimMetaTable[] dim_meta = (UiAdhocDimMetaTable[]) arr
					.toArray(new UiAdhocDimMetaTable[arr.size()]);
			for (int i = 0; dim_meta != null && i < dim_meta.length; i++) {

				sb.append("<tr class=\"table-white-bg_old\">\n");
				sb.append(
						"<td width=\"25%\" align=\"center\" title=\""
								+ dim_meta[i].getDim_desc().trim() + "\">")
						.append(dim_meta[i].getDim_name().trim());
				sb.append(
						"<input type=\"hidden\" name=\"H_"
								+ AdhocConstant.ADHOC_DIM_CHECK_NAME
								+ "\" value=\"" + dim_meta[i].getDim_id()
								+ "\"/>").append("\n");
				sb.append("</td>\n");

				if (dim_meta[i].getGroup_id().equals("")) {
					sb.append("<td width=\"25%\" align=\"center\">&nbsp;<input type=\"checkbox\" name=\""
							+ AdhocConstant.ADHOC_DIM_CHECK_NAME
							+ "\" value=\""
							+ dim_meta[i].getDim_id()
							+ "\" checked ></td>\n");
				} else {
					sb.append("<td width=\"25%\" align=\"center\">&nbsp;<input type=\"checkbox\" name=\""
							+ AdhocConstant.ADHOC_DIM_CHECK_NAME
							+ "\" value=\""
							+ dim_meta[i].getDim_id()
							+ "\" checked disabled ></td>\n");
				}

				if (dim_meta[i].isDefault()) {
					sb.append("<td align=\"center\"><a href=\":;\" class=\"icon del\"></a></td>\n");
				} else {
					sb.append("<td align=\"center\">");
					sb.append("<a href=\"javascript:;\" class=\"icon del\" onclick=\"deleteFixed('dim','"	+ dim_meta[i].getDim_id() + "')\"></a>");
					sb.append("</td>\n");
				}

				sb.append("<td align=\"center\" class=\"last\">");
				sb.append("<a href=\"javascript:;\" class=\"icon1 up\" onclick=\"moveUp(this,2)\"></a>&nbsp;<a href=\"javascript:;\" class=\"icon1 down\"  onclick=\"moveDown(this)\"></a>");
				sb.append("</td>");
				sb.append("</tr>\n");

			}
		}

		sb.append("</table>\n");
		sb.append("</div></div>\n");

		sb.append("</td>\n");
		sb.append("</tr>\n");
		return sb.toString();
	}

	/**
	 * 生成定制条件（不支持自定义标签）
	 *
	 * @param hoc_id
	 * @param session
	 * @return
	 */

	public static String getSelectedConResult(String hoc_id, HttpSession session) {
		StringBuffer sb = new StringBuffer("<tr>\n");
		sb.append("<td colspan=\"3\" class=\"search-bg\">\n");
		// 已选纬度
		String[] conArr = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
		if (conArr == null) {
			conArr = new String[0];
		}
		String conStr = "";
		for (int i = 0; conArr != null && i < conArr.length; i++) {
			if (conStr.length() > 0) {
				conStr += ",";
			}
			conStr += "'" + conArr[i] + "'";
		}
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		ArrayList selectedList = null;
		// 已经选中
		if (!"".equals(conStr)) {
			selectedList = (ArrayList) facade.getConListByString(conStr);
		}

		// 默认选择纬度
		ArrayList conList = (ArrayList) facade.getDefaultConListByHocId(hoc_id);

		sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\"  cellspacing=\"1\" class=\"choice-table-bg\">\n");
		sb.append("<tr class=\"table-gay-bg2\">");
		sb.append("<td align=\"center\" class=\"subject-title\" width=\"20%\">条件说明</td>\n");
		sb.append("<td align=\"center\" class=\"subject-title\" width=\"20%\">条件数值1</td>\n");
		sb.append("<td align=\"center\" class=\"subject-title\" width=\"20%\">条件数值2</td>\n");
		sb.append("<td align=\"center\" class=\"subject-title\" width=\"20%\">是否应用</td>\n");
		sb.append("<td align=\"center\" class=\"subject-title\" width=\"20%\">显示顺序</td>\n");
		sb.append("</tr>\n");

		// 融和
		ArrayList list = new ArrayList();
		if (conList != null && !conList.isEmpty()) {
			for (int i = 0; i < conList.size(); i++) {
				list.add(conList.get(i));
			}
		}

		//
		if (selectedList != null && !selectedList.isEmpty()) {
			for (int i = 0; i < selectedList.size(); i++) {
				list.add(selectedList.get(i));
			}
		}

		//
		if (list != null && !list.isEmpty()) {
			//
			ArrayList arr = (ArrayList) list;
			UiAdhocConditionMetaTable[] con_meta = (UiAdhocConditionMetaTable[]) arr
					.toArray(new UiAdhocConditionMetaTable[arr.size()]);
			for (int i = 0; con_meta != null && i < con_meta.length; i++) {

				sb.append("<tr class=\"table-white-bg\">\n");
				sb.append("<td width=\"20%\" align=\"center\">"
						+ con_meta[i].getCon_name().trim() + "</td>\n");
				sb.append("<td width=\"20%\" align=\"center\">"
						+ conditionParser(con_meta[i]) + "</td>\n");
				sb.append("<td width=\"20%\" align=\"center\">"
						+ duraConditionParser(con_meta[i]) + "</td>\n");

				if (con_meta[i].getGroup_id().equals("")) {
					sb.append("<td width=\"20%\" align=\"center\"><input type=\"checkbox\" name=\"con_check\" value=\""
							+ con_meta[i].getCon_id() + "\" checked ></td>\n");
				} else {
					sb.append("<td width=\"20%\" align=\"center\"><input type=\"checkbox\" name=\"con_check\" value=\""
							+ con_meta[i].getCon_id()
							+ "\" checked disabled ></td>\n");
				}

				sb.append("<td class=\"subject-title\" width=\"20%\" align=\"center\">"
						+ "<img src=\"../biimages/kw/icon-up.gif\" width=\"15\" height=\"15\" border=\"0\" style=\"cursor:hand\" onclick=\"moveUp(this,1)\">"
						+ "<img src=\"../biimages/kw/icon-down.gif\" width=\"15\" height=\"15\" border=\"0\" style=\"cursor:hand\" onclick=\"moveDown(this)\">"
						+ "</td>\n");
				sb.append("</tr>\n");

			}
		}

		sb.append("</table>\n");
		sb.append("</td>\n");
		sb.append("</tr>\n");
		return sb.toString();
	}

	/**
	 * 条件区生成解析器
	 *
	 * @param meta
	 * @return
	 */
	public static String conditionParser(UiAdhocConditionMetaTable meta) {
		StringBuffer sb = new StringBuffer("");
		if (AdhocConstant.ADHOC_CONDITION_TYPE_1.equals(meta.getCon_type())) {// 简单文本框
			sb.append("<input type=\"text\" name=\""
					+ meta.getQry_name().trim() + "\" value=\"\" "
					+ meta.getValidator() + ">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_2.equals(meta
				.getCon_type())) {// 起始文本框，结束文本框
			sb.append("<input type=\"text\" name=\""
					+ meta.getQry_name().trim() + "_A_11"
					+ "\" class=\"query-input-value\" value=\"\" "
					+ meta.getValidator() + ">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_3.equals(meta
				.getCon_type())) {// 下拉列表框（指定SQLID）
			sb.append("<BIBM:TagSelectList listName=\""
					+ meta.getQry_name().trim() + "\"  listID=\""
					+ meta.getCon_rule() + "\"/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_4.equals(meta
				.getCon_type())) {// 简单单选纽（指定SQLID）
			sb.append("<BIBM:TagRadio listName=\"" + meta.getQry_name().trim()
					+ "\"  listID=\"" + meta.getCon_rule() + "\"/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_5.equals(meta
				.getCon_type())) {// 多选button
			sb.append("<input type=\"text\" name=\""
					+ meta.getQry_name().trim() + "\" value=\"\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_6.equals(meta
				.getCon_type())) {// 单选button
			sb.append("<input type=\"text\" name=\""
					+ meta.getQry_name().trim() + "\" value=\"\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_7.equals(meta
				.getCon_type())) {// 日
			sb.append("<input type=\"text\"	name=\""
					+ meta.getQry_name().trim()
					+ "\" size=\"12\" value=\"\"	onClick=\"scwShow(this,this);\" class=\"input-text\" readonly/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_8.equals(meta
				.getCon_type())) {// 月
			sb.append("<input type=\"text\" class=\"query-input-value\"  name=\""
					+ meta.getQry_name().trim()
					+ "\" value=\"\" onClick=\"scwShowM(this,this);\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_9.equals(meta
				.getCon_type())) {// 日期时间断
			sb.append("<input type=\"text\" class=\"query-input-value\"  name=\""
					+ meta.getQry_name().trim()
					+ "_A_11"
					+ "\" value=\"\" onClick=\"scwShow(this,this);\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_10.equals(meta
				.getCon_type())) {// 月份时间段
			sb.append("<input type=\"text\" class=\"query-input-value\"  name=\""
					+ meta.getQry_name().trim()
					+ "_A_11"
					+ "\" value=\"\" onClick=\"scwShowM(this,this);\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_11.equals(meta
				.getCon_type())) {// 下拉列表框（自定义SQL）
			sb.append("<BIBM:TagSelectList listName=\""
					+ meta.getQry_name().trim()
					+ "\"    listID=\"0\" selfSQL=\"" + meta.getCon_rule()
					+ "\"/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_12.equals(meta
				.getCon_type())) {// 下拉列表框（内存参数）
			sb.append("<BIBM:TagSelectList listName=\""
					+ meta.getQry_name().trim()
					+ "\"    listID=\"#\" selfSQL=\"" + meta.getCon_rule()
					+ "\"/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_13.equals(meta
				.getCon_type())) {// 简单单选纽（自定义SQL）
			sb.append("<BIBM:TagRadio listName=\"" + meta.getQry_name().trim()
					+ "\"    listID=\"0\" selfSQL=\"" + meta.getCon_rule()
					+ "\"/>");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_14.equals(meta
				.getCon_type())) {// 简单单选纽（内存参数）
			sb.append("<BIBM:TagRadio listName=\"" + meta.getQry_name().trim()
					+ "\"    listID=\"#\" selfSQL=\"" + meta.getCon_rule()
					+ "\"/>");

		}
		return sb.toString();
	}

	/**
	 * 条件区生成解析器
	 *
	 * @param meta
	 * @return
	 */
	public static String duraConditionParser(UiAdhocConditionMetaTable meta) {
		StringBuffer sb = new StringBuffer("");
		if (AdhocConstant.ADHOC_CONDITION_TYPE_2.equals(meta.getCon_type())) {// 起始文本框，结束文本框
			sb.append("<=<input type=\"text\" name=\""
					+ meta.getQry_name().trim() + "_A_22"
					+ "\" size=\"15\" class=\"txtinput\" value=\"\" "
					+ meta.getValidator() + "  onblur=\"javascript:checkDura(this)\">");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_9.equals(meta
				.getCon_type())) {// 日期时间断
			sb.append("<=<input class=\"Wdate\" type=\"text\" name=\""
					+ meta.getQry_name().trim()
					+ "_A_22"
					+ "\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\" />");

		} else if (AdhocConstant.ADHOC_CONDITION_TYPE_10.equals(meta
				.getCon_type())) {// 月份时间段
			sb.append("<=<input class=\"Wdate\" type=\"text\" name=\""
					+ meta.getQry_name().trim()
					+ "_A_22"
					+ "\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\" />");

		}

		//System.out.println("-------------------"+sb.toString().trim().length());
		if(sb.toString().trim().length()==0){
			sb.append("&nbsp;");
		}

		return sb.toString();
	}

	/**
	 * 获取联动下拉框生成器
	 *
	 * @param appType
	 *            应用类型
	 * @param baseCode
	 *            基本编码参数
	 *
	 * @param session
	 *            会话
	 * @return
	 */
	public static String adhocRelateInfoGenerator(String appType,
			String baseCode, HttpSession session) {
		String sql = "";

		// 业务逻辑段（可扩充为抽象工厂方法来实现具体的业务逻辑）

		if (appType.toUpperCase().equals(
				AdhocConstant.ADHOC_MULTI_SELECT_APP_TYPE_CHANNEL)) {
			sql = new ChannelPlugInFactory().newInstance().AdhocBusiness(
					baseCode, session);
			//
		} else if (appType.toUpperCase().equals(
				AdhocConstant.ADHOC_MULTI_SELECT_APP_TYPE_DEPT)) {

		} else if (appType.toUpperCase().equals(
				AdhocConstant.ADHOC_MULTI_SELECT_APP_TYPE_DNNR)) {


		} else if (appType.toUpperCase().equals(
				AdhocConstant.ADHOC_MULTI_SELECT_APP_TYPE_SVC)) {

		}
		// 业务逻辑段
		System.out.println("adhocRelateInfoGenerator====================="
				+ sql);
		String[][] Records = null;
		try {
			Records = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		String str = "";
		String content = "";
		for (int i = 0; Records != null && i < Records.length; i++) {
			content += "subcat[" + i + "]= new Array('" + Records[i][0] + "','"
					+ Records[i][1] + "','" + Records[i][2] + "');";
		}
		str += "<script language = \"JavaScript\">";
		str += "var onecount;";
		str += "onecount=0;";
		str += "subcat = new Array();";
		str += content;

		if (Records != null) {
			str += "onecount=" + Records.length + ";";
		}
		str += "function changelocation(locationid)";
		str += "{";
		str += "document.mainfrm.Nclassid.length = 0; ";
		str += "var locationid=locationid;";
		str += "var i;";

		str += "for (i=0;i < onecount; i++)";
		str += "{";
		str += "if (subcat[i][1] == locationid)";
		str += "{ ";
		str += "document.mainfrm.Nclassid.options[document.mainfrm.Nclassid.length] = new Option(subcat[i][0], subcat[i][2]);";
		str += "} ";
		str += "}";
		str += "} ";
		str += "</script>";
		return str;
	}

	/**
	 * 提取当前条件查询数据结果
	 *
	 * @param con_id
	 * @param sec_area_id
	 * @param area_id
	 * @param svc_knd
	 * @param session
	 * @return
	 */
	public static String[][] getConditionListArray(String con_id,
			String sec_area_id, String area_id, String svc_knd,
			HttpSession session) {
		String[][] arr = null;
		// 进行条件选择
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		UiAdhocConditionMetaTable conMeta = facade.getConditionMetaInfo(con_id);

		// 取出查询的SQL,先不考虑过虑，过虑的话构造WHERE 条件
		String sql = conMeta.getCon_rule();
		// 条件
		String sqlcon = "";

		// 对套餐处理
		if (con_id.equals(AdhocConditionTag.OLD_USER_DNNR_CONDITION_ID)
				|| con_id.equals(AdhocConditionTag.USER_DNNR_CONDITION_ID)) {// 112

		}
		// 对渠道处理
		if (con_id.equals(AdhocConditionTag.CHANNEL_CONDITION_ID)) { // 202

		}
		if (sql.indexOf("?") > -1) {
			sql = sql.replaceAll("\\?", sqlcon);
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>=" + sql);
		try {
			arr = WebDBUtil.execQryArray(sql, "");
			System.out.println("arr.length===" + arr.length);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		return arr;

	}

	/**
	 * 清空会话
	 *
	 * @param session
	 */
	public static void clearAdhocSessionTag(HttpSession session) {
		session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
		//
		session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);

	}

	/**
	 * 查询具体汇总值填充
	 *
	 * @param sunInfo
	 * @return
	 */
	public static UiAdhocSumInfoTable[] getAdhocSumValueStruct(
			UiAdhocSumInfoTable[] sunInfo, String condition, String tableName) {
		UiAdhocSumInfoTable[] info = sunInfo;
		String selectStr = "SELECT ";
		for (int i = 0; sunInfo != null && i < sunInfo.length; i++) {
			//
			if (sunInfo[i].getCal_rule().equals(AdhocConstant.CAL_RULE_TYPE_C)) {
				selectStr += "COUNT(" + sunInfo[i].getMsu_filed() + "),";
			} else if (sunInfo[i].getCal_rule().equals(
					AdhocConstant.CAL_RULE_TYPE_S)) {
				selectStr += "COALESCE(SUM(" + sunInfo[i].getMsu_filed()
						+ "),0),";
			} else {
				selectStr += "COALESCE(SUM(" + sunInfo[i].getMsu_filed()
						+ "),0),";
			}

		}

		String sql = selectStr.substring(0, selectStr.lastIndexOf(","))
				+ " FROM " + tableName + " " + condition;

		System.out.println("--->" + sql);

		String[][] arr = AdhocUtil.queryArrayFacade(sql);
		if (arr != null && arr.length > 0) {
			for (int i = 0; sunInfo != null && i < sunInfo.length; i++) {
				info[i].setValue(FormatUtil.formatStr(arr[0][i],
						Integer.parseInt(sunInfo[i].getDigit()), true));
			}
		}
		return info;
	}

	public static String getAdhocSumValueQrySQL(UiAdhocSumInfoTable[] sunInfo) {

		String selectStr = "";
		for (int i = 0; sunInfo != null && i < sunInfo.length; i++) {
			//
			if (sunInfo[i].getCal_rule().equals(AdhocConstant.CAL_RULE_TYPE_C)) {
				selectStr += "COUNT(" + sunInfo[i].getMsu_filed() + "),";
			} else if (sunInfo[i].getCal_rule().equals(
					AdhocConstant.CAL_RULE_TYPE_S)) {
				selectStr += "COALESCE(SUM(" + sunInfo[i].getMsu_filed()
						+ "),0),";
			} else {
				selectStr += "COALESCE(SUM(" + sunInfo[i].getMsu_filed()
						+ "),0),";
			}

		}
		if ("".equals(selectStr)) {
			return "";
		} else {
			return selectStr.substring(0, selectStr.lastIndexOf(","));
		}

	}

	/**
	 * 即席查询视图汇总表格描述
	 *
	 * @param session
	 * @return
	 */
	public static String getSumTableHTML(HttpSession session) {
		StringBuffer sb = new StringBuffer(
				"<table width=\"100%\" border=\"0\" align=\"center\">\n");
		String str = " <tr> <td height=\"1\" colspan=\"4\" nowrap background=\"../biimages/black-dot.gif\" ></td></tr>";

		UiAdhocSumInfoTable[] sumInfo = (UiAdhocSumInfoTable[]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_SUM);
		for (int i = 0; sumInfo != null && i < sumInfo.length; i++) {
			if (i % 4 == 0) {
				sb.append("<tr>\n");
			}
			sb.append("<td nowrap >" + sumInfo[i].getMsu_name()
					+ ":<span class=\"blackbold\">" + sumInfo[i].getValue()
					+ "</span>" + sumInfo[i].getUnit() + "</td>");

			if ((i % 4 == 3) || (i == sumInfo.length - 1)) {
				if ((i == sumInfo.length - 1) && (i % 4 != 3)) {
					int count = 4 - ((sumInfo.length) % 4);
					sb.append(AdhocUtil.getNbspTdInnerHtml(count));
				}
				sb.append("</tr>\n");
				sb.append(str + "\n");
			}
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	public static String getReportStyleTable(HttpSession session) {

		// 当前条件
		UiAdhocConditionMetaTable[] conMeta = (UiAdhocConditionMetaTable[]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY);
		if (conMeta == null) {
			conMeta = new UiAdhocConditionMetaTable[0];
		}
		// 当前纬度
		UiAdhocDimMetaTable[] dimMeta = (UiAdhocDimMetaTable[]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY);
		if (dimMeta == null) {
			dimMeta = new UiAdhocDimMetaTable[0];
		}
		// 当前指标
		UiAdhocMsuMetaTable[] msuMeta = (UiAdhocMsuMetaTable[]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY);
		if (msuMeta == null) {
			msuMeta = new UiAdhocMsuMetaTable[0];
		}

		// 报表数据二维数组
		String[][] viewRpt = (String[][]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_REPORT);
		if (viewRpt == null) {
			viewRpt = new String[0][0];
		}

		// 列索引数组
		int[] colArr = (int[]) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_ARRAY);
		if (colArr == null) {
			colArr = new int[0];
		}

		// 辅助列表
		ArrayList helperList = (ArrayList) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_LIST);
		if (helperList == null || helperList.isEmpty()) {
			helperList = new ArrayList();
		}

		// 视图信息
		AdhocViewStruct adhocView = (AdhocViewStruct) session
				.getAttribute(AdhocConstant.ADHOC_VIEW_STRUCT);
		if (adhocView == null) {
			adhocView = new AdhocViewStruct();
		}

		// 标题头
		String[] headArr = adhocView.headStr.split(",");
		String[] headDesc = adhocView.headDesc.split(",");// 这里是 新加的 用来表示 表头的

		StringBuffer sb = new StringBuffer(
				"<table width=\"100%\" border=\"1\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"  style=\"border-collapse: collapse\" bordercolor=\"#777777\">")
				.append("\n");
		sb.append("<tr align=\"center\">").append("\n");

		for (int i = 0; i < headArr.length; i++) {
			if (i < dimMeta.length) {
				// 这里是 新加的 用来表示 表头的
				sb.append(
						"<td class=\"tab-title2\" title=\"" + headDesc[i]
								+ "\">" + headArr[i] + "</td>").append("\n");
			} else {
				sb.append(
						"<td class=\"tab-title2\" title=\"" + headDesc[i]
								+ "\">" + headArr[i] + "("
								+ msuMeta[i - dimMeta.length].getUnit()
								+ ")</td>").append("\n");
			}
		}

		sb.append("</tr>").append("\n");

		for (int i = 0; i < viewRpt.length; i++) {
			sb.append("<tr class=\"table-white-bg\">").append("\n");
			for (int j = 0; j < headArr.length; j++) {
				if (j < dimMeta.length) {// 纬度
					ArrayList tmpList = (ArrayList) helperList.get(j);
					if (i == 0) {// 第一行
						int rowspan = Integer.parseInt((String) tmpList.get(0));
						sb.append(
								"<td align=\"left\" rowspan=\"" + rowspan
										+ "\" >" + viewRpt[i][j] + "</td>")
								.append("\n");
					} else {
						int countRowIndex = 0;
						for (int t_index = 0; t_index < tmpList.size(); t_index++) {
							countRowIndex += Integer.parseInt((String) tmpList
									.get(t_index));
							if (i == countRowIndex) {
								int rowspan = Integer.parseInt((String) tmpList
										.get(t_index + 1));
								sb.append(
										"<td align=\"left\" rowspan=\""
												+ rowspan + "\" >"
												+ viewRpt[i][j] + "</td>")
										.append("\n");
								break;
							}
						}
					}

				} else {// 指标
					sb.append(
							"<td height=\"22\" align=\"right\">"
									+ FormatUtil.formatStr(viewRpt[i][j],
											Integer.parseInt(msuMeta[j
													- dimMeta.length]
													.getDigit()), true)
									+ "</td>").append("\n");
				}

			}
			sb.append("</tr>").append("\n");

		}

		sb.append("</table>").append("\n");
		return sb.toString();
	}

	/*
	 * public static String getReportStyleTable(HttpSession session) {
	 *
	 * // 当前条件 UiAdhocConditionMetaTable[] conMeta =
	 * (UiAdhocConditionMetaTable[]) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY); if
	 * (conMeta == null) { conMeta = new UiAdhocConditionMetaTable[0]; } // 当前纬度
	 * UiAdhocDimMetaTable[] dimMeta = (UiAdhocDimMetaTable[]) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY); if (dimMeta ==
	 * null) { dimMeta = new UiAdhocDimMetaTable[0]; } // 当前指标
	 * UiAdhocMsuMetaTable[] msuMeta = (UiAdhocMsuMetaTable[]) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY); if (msuMeta ==
	 * null) { msuMeta = new UiAdhocMsuMetaTable[0]; }
	 *
	 * // 报表数据二维数组 String[][] viewRpt = (String[][]) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_REPORT); if (viewRpt == null) {
	 * viewRpt = new String[0][0]; }
	 *
	 * // 列索引数组 int[] colArr = (int[]) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_ARRAY); if (colArr
	 * == null) { colArr = new int[0]; }
	 *
	 * // 辅助列表 ArrayList helperList = (ArrayList) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_LIST); if
	 * (helperList == null || helperList.isEmpty()) { helperList = new
	 * ArrayList(); }
	 *
	 * // 视图信息 AdhocViewStruct adhocView = (AdhocViewStruct) session
	 * .getAttribute(AdhocConstant.ADHOC_VIEW_STRUCT); if (adhocView == null) {
	 * adhocView = new AdhocViewStruct(); }
	 *
	 * // 标题头 String[] headArr = adhocView.headStr.split(","); String[] headDesc
	 * = adhocView.headDesc.split(",");// 这里是 新加的 用来表示 表头的
	 *
	 * StringBuffer sb = new StringBuffer(
	 * "<table width=\"100%\" border=\"1\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"  style=\"border-collapse: collapse\" bordercolor=\"#777777\">"
	 * ) .append("\n"); sb.append("<tr align=\"center\">").append("\n");
	 *
	 * for (int i = 0; i < headArr.length; i++) { if (i < dimMeta.length) { //
	 * 这里是 新加的 用来表示 表头的 sb.append( "<td class=\"tab-title2\" title=\"" +
	 * headDesc[i] + "\">" + headArr[i] + "</td>").append("\n"); } else {
	 * sb.append( "<td class=\"tab-title2\" title=\"" + headDesc[i] + "\">" +
	 * headArr[i] + "(" + msuMeta[i - dimMeta.length].getUnit() +
	 * ")</td>").append("\n"); } }
	 *
	 * sb.append("</tr>").append("\n");
	 *
	 * for (int i = 0; i < viewRpt.length; i++) {
	 * sb.append("<tr class=\"table-white-bg\">").append("\n"); for (int j = 0;
	 * j < headArr.length; j++) { if (j < dimMeta.length) {// 纬度 ArrayList
	 * tmpList = (ArrayList) helperList.get(j); if (i == 0) {// 第一行 int rowspan
	 * = Integer.parseInt((String) tmpList.get(0)); sb.append(
	 * "<td  align=\"center\" rowspan=\"" + rowspan + "\" >" + viewRpt[i][2 * j
	 * + 1] + "</td>").append("\n"); } else { int countRowIndex = 0; for (int
	 * t_index = 0; t_index < tmpList.size(); t_index++) { countRowIndex +=
	 * Integer.parseInt((String) tmpList .get(t_index)); if (i == countRowIndex)
	 * { int rowspan = Integer.parseInt((String) tmpList .get(t_index + 1));
	 * sb.append( "<td  align=\"center\" rowspan=\"" + rowspan + "\" >" +
	 * viewRpt[i][2 * j + 1] + "</td>").append("\n"); break; } } }
	 *
	 * } else {// 指标 sb.append( "<td height=\"22\">" + FormatUtil.formatStr(
	 * viewRpt[i][dimMeta.length + j], Integer.parseInt(msuMeta[j -
	 * dimMeta.length] .getDigit()), true) + "</td>").append("\n"); }
	 *
	 * } sb.append("</tr>").append("\n");
	 *
	 * }
	 *
	 * sb.append("</table>").append("\n"); return sb.toString(); }
	 */
	/**
	 * 纬度定制默认纬度值（ 系统维表）
	 *
	 * @param session
	 * @return
	 */
	public static String getSelfDimDefaultValue(HttpSession session) {
		StringBuffer sb = new StringBuffer();

		HashMap map = (HashMap) session
				.getAttribute(AdhocConstant.ADHOC_DIM_DEFAULT_VALUE);
		if (map == null || map.size() <= 0) {
			sb.append("<tr class=\"table-white-bg\">").append("\n");
			sb.append(
					"<td width=\"100%\" colspan=\"2\" align=\"center\"><B>没有默认纬度值</B></td>")
					.append("\n");
			sb.append("</tr>").append("\n");
		} else {
			ArrayList list = new ArrayList();
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				list.add(it.next().toString());
			}
			// 排序
			Collections.sort(list);

			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i).toString();
				String value = (String) map.get(key);
				sb.append("<tr class=\"table-white-bg\">").append("\n");
				sb.append("<td width=\"40%\" align=\"center\">" + key + "</td>")
						.append("\n");
				sb.append(
						"<td width=\"60%\" align=\"center\">" + value + "</td>")
						.append("\n");
				sb.append("</tr>").append("\n");

			}

		}

		return sb.toString();

	}

	/**
	 * 纬度定制定制纬度值
	 *
	 * @param session
	 * @return
	 */
	public static String getSelfDimValue(UiAdhocRuleUserDimTable[] list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table  width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"choice-table-bg\" >");
		sb.append("<tr align=\"center\" class=\"table-gay-bg2\">");

		sb.append("<td align=\"center\" width=\"15%\" class=\"subject-title\">编码</td>");
		sb.append("<td align=\"center\" width=\"30%\" class=\"subject-title\">名称</td>");
		sb.append("<td align=\"center\" width=\"20%\" class=\"subject-title\">值上域（闭）</td>");
		sb.append("<td align=\"center\" width=\"20%\" class=\"subject-title\">值下域（开）</td>");
		sb.append("<td align=\"center\" width=\"15%\" class=\"subject-title\">删除</td>");
		sb.append("</tr>");

		if (list == null || list.length <= 0) {
			sb.append("<tr class=\"table-white-bg\">").append("\n");
			sb.append("<td colspan=\"5\" align=\"center\"><B>目前没有定制纬度</B></td>")
					.append("\n");
			sb.append("</tr>").append("\n");
		} else {
			for (int i = 0; list != null && i < list.length; i++) {

				sb.append("<tr class=\"table-white-bg\">").append("\n");

				sb.append(
						"<td width=\"15%\" align=\"center\">"
								+ list[i].getSelf_dim_id() + "</td>").append(
						"\n");
				sb.append(
						"<td width=\"30%\" align=\"center\">"
								+ list[i].getSelf_dim_name() + "</td>").append(
						"\n");
				sb.append(
						"<td width=\"20%\" align=\"center\">"
								+ list[i].getLow_value() + "</td>")
						.append("\n");
				sb.append(
						"<td width=\"20%\" align=\"center\">"
								+ list[i].getHign_value() + "</td>").append(
						"\n");

				sb.append("<td  width=\"15%\" align=\"center\" >"
						+ "<a href=\"javascript:;\"><img src=\"../biimages/delete.gif\"  border=\"0\" onclick=\"delectDimValue('"
						+ list[i].getSelf_dim_id() + "','"
						+ list[i].getSelf_dim_name() + "','"
						+ list[i].getLow_value() + "','"
						+ list[i].getHign_value() + "')\"></a>" + "</td>\n");

				sb.append("</tr>").append("\n");
			}
		}
		sb.append("</table>");
		return sb.toString();

	}

	/**
	 * 判断是否
	 *
	 * @param parseDim
	 * @param hocInfo
	 * @param oper_no
	 * @return
	 */
	public static boolean hasUserSelfDim(UiAdhocDimMetaTable parseDim,
			UiAdhocInfoDefTable hocInfo, String oper_no) {
		boolean flag = false;
		AdhocFacade facade = new AdhocFacade(new AdhocDao());

		if (parseDim != null) {
			if (!"".equals(parseDim.getDim_relation_field())) {
				UiAdhocRuleUserDimTable[] arr = facade.getAdhocUserDimList(
						hocInfo.getAdhoc_id(), oper_no,
						parseDim.getDim_relation_field());
				if (arr != null && arr.length > 0) {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 生成自定义纬度的表达式
	 *
	 * @param parseDim
	 * @param hocInfo
	 * @param oper_no
	 * @return
	 */
	public static String genCaseWhenString(UiAdhocDimMetaTable parseDim,
			UiAdhocInfoDefTable hocInfo, String oper_no) {
		StringBuffer sb = new StringBuffer("");
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		if (parseDim != null) {
			if (!"".equals(parseDim.getDim_relation_field())) {
				UiAdhocRuleUserDimTable[] arr = facade.getAdhocUserDimList(
						hocInfo.getAdhoc_id(), oper_no,
						parseDim.getDim_relation_field());
				if (arr != null && arr.length > 0) {
					sb.append("CASE").append("\n");
					for (int i = 0; i < arr.length; i++) {
						String cacuRule = "";
						if (!"".equals(arr[i].getLow_value())) {
							if (cacuRule.length() > 0) {
								cacuRule += " AND ";
							}
							cacuRule += arr[i].getDim_relation_field() + " >= "
									+ arr[i].getLow_value();
						}
						if (!"".equals(arr[i].getHign_value())) {
							if (cacuRule.length() > 0) {
								cacuRule += " AND ";
							}
							cacuRule += arr[i].getDim_relation_field() + " < "
									+ arr[i].getHign_value();
						}
						sb.append(" WHEN " + cacuRule);
						sb.append(" THEN '" + arr[i].getSelf_dim_id() + "'")
								.append("\n");
					}
					sb.append(" END ");
				}
			}
		}

		return sb.toString();
	}

	/**
	 *
	 * @param leftOuterTag
	 * @return
	 * @Desc:获取普通关联查询信息
	 */
	/*
	 * public static List getInnerLinkQry(String leftOuterTag) { List listRtn =
	 * new ArrayList(); AdhocViewStruct view = null;
	 *
	 * String tableAlias = "adhocOuterTbl";
	 *
	 * String leftTable = " ui_adhoc_condition_updetail " + tableAlias;
	 *
	 * // leftOuterTag格式要求：条件映射的字段1,条件值1|条件映射的字段2,条件值2, if
	 * (leftOuterTag.length() == 0) { return listRtn; } String splitTable[] =
	 * leftOuterTag.split("\\|");
	 *
	 * for (int i = 0; i < splitTable.length; i++) { view = new
	 * AdhocViewStruct(); String tmp = splitTable[i]; String tmpSplit[] =
	 * tmp.split("\\,"); view.sqlLinkTable = leftTable + i;
	 *
	 * view.sqlLinkTableCon = tmpSplit[0] + "=" + tableAlias + i +
	 * ".OBJ_VALUE and " + tableAlias + i + ".id=" + tmpSplit[1] + " "; //
	 * System.out.println("view.sqlLinkTable:" + view.sqlLinkTable); //
	 * System.out.println("view.sqlLinkTableCon:" + // view.sqlLinkTableCon);
	 *
	 * listRtn.add(view); } return listRtn; }
	 */
	public static List getInnerLinkQry(String leftOuterTag) {
		List listRtn = new ArrayList();
		AdhocViewStruct view = null;

		String tableAlias = "adhocOuterTbl";

		String leftTable = " ui_adhoc_condition_updetail " + tableAlias;

		// leftOuterTag格式要求：条件映射的字段1,条件值1|条件映射的字段2,条件值2,
		if (leftOuterTag.length() == 0) {
			return listRtn;
		}
		String splitTable[] = leftOuterTag.split("\\|");

		for (int i = 0; i < splitTable.length; i++) {
			view = new AdhocViewStruct();
			String tmp = splitTable[i];
			String tmpSplit[] = tmp.split("\\,");
			view.sqlLinkTable = " right outer join " + leftTable + i + " on "
					+ tmpSplit[0] + "=" + tableAlias + i + ".OBJ_VALUE";

			view.sqlLinkTableCon = tableAlias + i + ".id=" + tmpSplit[1] + " ";

			listRtn.add(view);
		}
		return listRtn;
	}

	@SuppressWarnings("unused")
	public static void printJsFun_SelfDisp(JspWriter out,
			HttpServletRequest request, String formName,
			UiAdhocFavoriteDefTable favinfo, String adhoc_root) {
		UiAdhocConditionMetaTable[] parseCondition = null;

		parseCondition = (UiAdhocConditionMetaTable[]) request.getSession()
				.getAttribute(AdhocConstant.ADHOC_CONDITION_SELECTED);

		ArrayList defaultList = (ArrayList) request.getSession().getAttribute(
				AdhocConstant.ADHOC_DEFAULT_CONDITION_LIST);
		StringBuffer sb = new StringBuffer();
		try {

			out.print("<script language='JavaScript'>\n");
			out.print("function selfDisp()\n");
			out.print("{\n");

			out.print(" if( SYS_ManAlwaysForm.ReLove.value==\"1\" )\n");
			out.print("  return;\n");
			out.print("\n  SYS_ManAlwaysForm.ReLove.value=\"1\";\n");

			sb.append("<script language='JavaScript'>\n");
			sb.append("function selfDisp()\n");
			sb.append("{\n");

			sb.append(" if( SYS_ManAlwaysForm.ReLove.value==\"1\" )\n");
			sb.append("  return;\n");
			sb.append("\n  SYS_ManAlwaysForm.ReLove.value=\"1\";\n");

			String strOut = "";
			String strValue = "";

			if (favinfo.getFavorite_id().length() == 0) {// 非收藏夹形式
				if (parseCondition == null || parseCondition.length == 0) {

					for (int j = 0; j < defaultList.size(); j++) {
						out.print("  try{\n");
						sb.append("  try{\n");

						UiAdhocConditionMetaTable defaultCon = (UiAdhocConditionMetaTable) defaultList
								.get(j);
						if (defaultCon.getCon_type().equals("7")) {// 日期

							strOut = formName
									+ "."
									+ defaultCon.getQry_name()
									+ ".value = \""
									+ DateUtil.getDiffDay(-1,
											DateUtil.getNowDate()) + "\";\n";
							out.print(strOut);
							sb.append(strOut);

						}
						if (defaultCon.getCon_type().equals("8")) {// 月份
							strOut = formName
									+ "."
									+ defaultCon.getQry_name()
									+ ".value = \""
									+ DateUtil.getDiffMonth(-1,
											DateUtil.getNowDate()) + "\";\n";
							out.print(strOut);
							sb.append(strOut);

						}

						if (defaultCon.getCon_type().equals("9")) {// 日期段

							strValue = DateUtil.getDiffDay(-1,
									DateUtil.getNowDate());

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
						}

						if (defaultCon.getCon_type().equals("10")) {// 月份段
							strValue = DateUtil.getDiffMonth(-1,
									DateUtil.getNowDate());

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
						}
						out.print("  }catch(e){;}\n\n");
						sb.append("  }catch(e){;}\n\n");

					}

				}

				if (parseCondition != null && parseCondition.length > 0) {

					for (int i = 0; i < parseCondition.length; i++) {

						UiAdhocConditionMetaTable defaultCon = null;

						boolean blnDefault = false;

						for (int j = 0; j < defaultList.size(); j++) {
							defaultCon = (UiAdhocConditionMetaTable) defaultList
									.get(j);
							if (defaultCon.getCon_id().equals(
									parseCondition[i].getCon_id())) {
								blnDefault = true;
								break;
							}
						}

						int conType = Integer.parseInt(parseCondition[i]
								.getCon_type());
						strValue = StringB.NulltoBlank(request
								.getParameter(parseCondition[i].getQry_name()));
						switch (conType) {
						case 2:
							out.print("  try{\n");
							sb.append("  try{\n");

							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_11"));
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_11.value= \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_22"));
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;

						case 7:
							out.print("  try{\n");
							sb.append("  try{\n");
							if (strValue.length() == 0 && blnDefault) {

								strValue = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}

							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 8:
							out.print("  try{\n");
							sb.append("  try{\n");
							if (strValue.length() == 0 && blnDefault) {
								strValue = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}

							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;

						case 9:
							out.print("  try{\n");
							sb.append("  try{\n");
							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_11"));
							if (strValue.length() == 0 && blnDefault) {
								strValue = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_22"));
							if (strValue.length() == 0 && blnDefault) {
								strValue = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 10:
							out.print("  try{\n");
							sb.append("  try{\n");

							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_11"));
							if (strValue.length() == 0 && blnDefault) {
								strValue = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_A_22"));
							if (strValue.length() == 0 && blnDefault) {
								strValue = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 13:
							// 单选按钮
							out.print("  try{\n");
							sb.append("  try{\n");

							if (strValue.length() > 0) {
								out.print("    for(i=0; i<" + formName + "."
										+ parseCondition[i].getQry_name()
										+ ".length; i++) {\n");
								out.print("      if( " + formName + "."
										+ parseCondition[i].getQry_name()
										+ "[i].value==" + strValue + " ) {\n");
								out.print("        " + formName + "."
										+ parseCondition[i].getQry_name()
										+ "[i].checked=true;\n");
								out.print("        break; \n");
								out.print("      }\n");
								out.print("    }\n");

								sb.append("    for(i=0; i<" + formName + "."
										+ parseCondition[i].getQry_name()
										+ ".length; i++) {\n");
								sb.append("      if( " + formName + "."
										+ parseCondition[i].getQry_name()
										+ "[i].value==" + strValue + " ) {\n");
								sb.append("        " + formName + "."
										+ parseCondition[i].getQry_name()
										+ "[i].checked=true;\n");
								sb.append("        break; \n");
								sb.append("      }\n");
								sb.append("    }\n");
							}

							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 16:
							// 文本区域,TEXTAREA
							out.print("  try{\n");
							sb.append("  try{\n");

							String[] noSplit = strValue.split("\r\n");
							String strOutArea = "";
							for (int kk = 0; kk < noSplit.length; kk++) {

								String num = noSplit[kk].trim();
								if (num.length() > 0) {
									if (strOutArea.length() == 0) {
										strOutArea = num;
									} else {

										strOutArea += "\\r\\n" + num;
									}
								}

							}

							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".innerText = \"" + strOutArea + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 18:
							out.print("  try{\n");
							sb.append("  try{\n");
							strValue = "是";
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						case 19:
							out.print("  try{\n");
							sb.append("  try{\n");
							strValue = "是";
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						default:
							out.print("  try{\n");
							sb.append("  try{\n");
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ ".value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							out.print("  try{\n");
							sb.append("  try{\n");

							strValue = StringB.NulltoBlank(request
									.getParameter(parseCondition[i]
											.getQry_name() + "_desc"));
							strOut = formName + "."
									+ parseCondition[i].getQry_name()
									+ "_desc.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");

							break;
						}

					}
				}
			} else {

				for (int j = 0; j < defaultList.size(); j++) {
					out.print("  try{\n");
					sb.append("  try{\n");

					UiAdhocConditionMetaTable defaultCon = (UiAdhocConditionMetaTable) defaultList
							.get(j);
					strValue = StringB.NulltoBlank(request
							.getParameter(defaultCon.getQry_name()));

					if (strValue.length() == 0) {

						if (defaultCon.getCon_type().equals("7")) {// 日期

							strOut = formName
									+ "."
									+ defaultCon.getQry_name()
									+ ".value = \""
									+ DateUtil.getDiffDay(-1,
											DateUtil.getNowDate()) + "\";\n";
							out.print(strOut);
							sb.append(strOut);

						}

						if (defaultCon.getCon_type().equals("8")) {// 月份
							strOut = formName
									+ "."
									+ defaultCon.getQry_name()
									+ ".value = \""
									+ DateUtil.getDiffMonth(-1,
											DateUtil.getNowDate()) + "\";\n";
							out.print(strOut);
							sb.append(strOut);

						}
					} else {
						strOut = formName + "." + defaultCon.getQry_name()
								+ ".value = \"" + strValue + "\";\n";
						out.print(strOut);
						sb.append(strOut);

					}

					strValue = StringB.NulltoBlank(request
							.getParameter(defaultCon.getQry_name() + "_A_11"));
					if (strValue.length() == 0) {
						if (defaultCon.getCon_type().equals("9")) {// 日期段

							strValue = DateUtil.getDiffDay(-1,
									DateUtil.getNowDate());

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
						}
						if (defaultCon.getCon_type().equals("10")) {// 月份段
							strValue = DateUtil.getDiffMonth(-1,
									DateUtil.getNowDate());

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_22.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);
						}
					} else {
						if (defaultCon.getCon_type().equals("9")
								|| defaultCon.getCon_type().equals("10")) {// 日期段


							strOut = formName + "." + defaultCon.getQry_name()
									+ "_A_11.value = \"" + strValue + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strOut = formName
									+ "."
									+ defaultCon.getQry_name()
									+ "_A_22.value = \""
									+ StringB.NulltoBlank(request
											.getParameter(defaultCon
													.getQry_name() + "_A_22"))
									+ "\";\n";
							out.print(strOut);
							sb.append(strOut);
						}

					}

					out.print("  }catch(e){;}\n\n");
					sb.append("  }catch(e){;}\n\n");

				}

				String[][] conArr = getFavInfoConArray(favinfo.getFavorite_id());

				if (conArr != null && conArr.length > 0) {
					for (int xx = 0; xx < conArr.length; xx++) {
						String qry_name = StringB.NulltoBlank(conArr[xx][5]);
						String param1 = StringB.NulltoBlank(conArr[xx][10]);
						String param2 = StringB.NulltoBlank(conArr[xx][11]);
						String param3 = StringB.NulltoBlank(conArr[xx][12]);
						String param4 = StringB.NulltoBlank(conArr[xx][13]);
						int con_type = Integer.parseInt(conArr[xx][2]);

						strValue = StringB.NulltoBlank(request
								.getParameter(qry_name));
						UiAdhocConditionMetaTable defaultCon = null;

						boolean blnDefault = false;

						for (int j = 0; j < defaultList.size(); j++) {
							defaultCon = (UiAdhocConditionMetaTable) defaultList
									.get(j);
							if (defaultCon.getCon_id().equals(conArr[xx][0])) {
								blnDefault = true;
								break;
							}
						}

						out.print("  try{\n");
						sb.append("  try{\n");
						switch (con_type) {
						case 2:

							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_11"));

							if (strValue.length() > 0) {
								param1 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_A_11.value= \"" + param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_22"));

							if (strValue.length() > 0) {
								param2 = strValue;
							}
							strOut = formName + "." + qry_name
									+ "_A_22.value = \"" + param2 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;

						case 7:

							if (strValue.length() == 0 && blnDefault) {

								param1 = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}
							if (strValue.length() > 0) {

								param1 = strValue;
							}


							strOut = formName + "." + qry_name + ".value = \""
									+ param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;
						case 8:

							if (strValue.length() == 0 && blnDefault) {
								param1 = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}

							if (strValue.length() > 0) {

								param1 = strValue;
							}

							strOut = formName + "." + qry_name + ".value = \""
									+ param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;

						case 9:
							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_11"));

							if (strValue.length() == 0 && blnDefault) {
								param1 = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}
							if (strValue.length() > 0) {

								param1 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_A_11.value= \"" + param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_22"));

							if (strValue.length() == 0 && blnDefault) {
								param2 = DateUtil.getDiffDay(-1,
										DateUtil.getNowDate());
							}

							if (strValue.length() > 0) {

								param2 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_A_22.value = \"" + param2 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;
						case 10:
							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_11"));
							if (strValue.length() == 0 && blnDefault) {
								param1 = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}

							if (strValue.length() > 0) {

								param1 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_A_11.value= \"" + param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);

							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_A_22"));
							if (strValue.length() == 0 && blnDefault) {
								param2 = DateUtil.getDiffMonth(-1,
										DateUtil.getNowDate());
							}
							if (strValue.length() > 0) {

								param2 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_A_22.value = \"" + param2 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;
						case 13:
							// 单选按钮
							if (strValue.length() > 0) {

								param1 = strValue;
							}

							if (param1.length() > 0) {
								out.print("    for(i=0; i<" + formName + "."
										+ qry_name + ".length; i++) {\n");
								out.print("      if( " + formName + "."
										+ qry_name + "[i].value==" + param1
										+ " ) {\n");
								out.print("        " + formName + "."
										+ qry_name + "[i].checked=true;\n");
								out.print("        break; \n");
								out.print("      }\n");
								out.print("    }\n");

								sb.append("    for(i=0; i<" + formName + "."
										+ qry_name + ".length; i++) {\n");
								sb.append("      if( " + formName + "."
										+ qry_name + "[i].value==" + param1
										+ " ) {\n");
								sb.append("        " + formName + "."
										+ qry_name + "[i].checked=true;\n");
								sb.append("        break; \n");
								sb.append("      }\n");
								sb.append("    }\n");
							}

							break;
						case 16:
							// 文本区域,TEXTAREA
							if (strValue.length() > 0) {

								param1 = strValue;
							}

							String[] noSplit = param1.split("\r\n");
							String strOutArea = "";
							for (int kk = 0; kk < noSplit.length; kk++) {

								String num = noSplit[kk].trim();
								if (num.length() > 0) {
									if (strOutArea.length() == 0) {
										strOutArea = num;
									} else {

										strOutArea += "\\r\\n" + num;
									}
								}

							}

							strOut = formName + "." + qry_name
									+ ".innerText = \"" + strOutArea + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							break;
						default:
							out.print("  try{\n");
							sb.append("  try{\n");

							if (strValue.length() > 0) {

								param1 = strValue;
							}

							strOut = formName + "." + qry_name + ".value = \""
									+ param1 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");
							out.print("  try{\n");
							sb.append("  try{\n");
							strValue = StringB.NulltoBlank(request
									.getParameter(qry_name + "_desc"));

							if (strValue.length() > 0) {

								param3 = strValue;
							}

							strOut = formName + "." + qry_name
									+ "_desc.value = \"" + param3 + "\";\n";
							out.print(strOut);
							sb.append(strOut);
							out.print("  }catch(e){;}\n\n");
							sb.append("  }catch(e){;}\n\n");
							break;
						}
						out.print("  }catch(e){;}\n\n");
						sb.append("  }catch(e){;}\n\n");
					}

				}

			}

			out.print("}\n");
			out.print("</script>\n");
			out.print("\n");
			out.print("<form name=\"SYS_ManAlwaysForm\">\n");
			out.print("<input type=\"hidden\" name=\"ReLove\">\n");
			out.print("</form>\n\n");

			sb.append("}\n");
			sb.append("</script>\n");
			sb.append("\n");
			sb.append("<form name=\"SYS_ManAlwaysForm\">\n");
			sb.append("<input type=\"hidden\" name=\"ReLove\">\n");
			sb.append("</form>\n\n");

			request.getSession().setAttribute(
					AdhocConstant.ADHOC_CONDITION_SCRIPT_FUN, sb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @param fav_id
	 * @return
	 * @desc:获取收藏夹元数据信息
	 */
	@SuppressWarnings("unused")
	private static String[][] getFavInfoConArray(String fav_id) {
		ArrayList list = new ArrayList();
		String sql = " select B.CON_ID , B.CON_NAME , B.CON_TYPE , B.CON_RULE ,"
				+ "B.CON_TAG , B.QRY_NAME , B.FILED_NAME ,"
				+ "B.STATUS,B.VALIDATOR,A.SEQUENCE,a.field_VALUE_01,a.field_VALUE_02,a.field_VALUE_01_desc,a.field_VALUE_02_desc from ui_adhoc_rule_favorite_meta "
				+ "A , ui_adhoc_condition_meta B WHERE A.FAVORITE_ID = "
				+ fav_id
				+ " AND "
				+ "A.META_TYPE = '1' AND A.META_CODE = B.CON_ID order by a.SEQUENCE";

		String[][] arr = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}

		return arr;
	}
}
