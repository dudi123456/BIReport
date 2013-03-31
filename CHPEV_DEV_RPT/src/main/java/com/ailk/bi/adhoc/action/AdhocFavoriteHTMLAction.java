package com.ailk.bi.adhoc.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.dao.impl.AdhocFavDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.util.AdhocConditionTag;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.common.LSInfoFavorite;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocFavoriteHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 得到会话
		HttpSession session = request.getSession();
		// 得到用户工号
		String oper_no = CommonFacade.getLoginId(session);
		//
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "view";
		}
		// "adhoc_root"
		String root = request.getParameter(AdhocConstant.ADHOC_ROOT);
		if (root == null || "".equals(root)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "即席查询功能根节点缺失，请通知系统管理员！");

		}
		session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);
		// 表格ID
		String group_tag = request.getParameter(AdhocConstant.ADHOC_TABLE_WEBKEYS);// "group_tag"
		if (group_tag == null || "".equals(group_tag)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "即席查询界面属性表格标记缺失，请通知系统管理员！");

		}
		// 属性表格ID
		String group_msu_tag = request.getParameter(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);// "group_msu_tag"
		if (group_msu_tag == null || "".equals(group_msu_tag)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "即席查询界面指标表格标记缺失，请通知系统管理员！");

		}
		//
		if ("view".equals(operType)) {
			// 收藏夹ID
			String favID = request.getParameter("fav_id");
			if (favID == null || "".equals(favID)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询收藏夹缺失，请通知系统管理员！");
			}
			// 即席查询门户
			String conArr[] = null;
			String dimArr[] = null;
			String msuArr[] = null;

			String[][] ConArrSel = getFavInfoConArray(favID);
			ArrayList conList = new ArrayList();
			if (ConArrSel != null && ConArrSel.length > 0) {

				for (int i = 0; i < ConArrSel.length; i++) {
					conList.add(ConArrSel[i][0].trim());
				}

			}

			AdhocFacade face = new AdhocFacade(new AdhocFavDao());

			// ArrayList conList = (ArrayList)face.getFavoriteConList(favID);
			if (conList != null && !conList.isEmpty()) {
				conArr = (String[]) conList.toArray(new String[conList.size()]);
			}
			ArrayList dimList = (ArrayList) face.getFavoriteDimList(favID);
			if (dimList != null && !dimList.isEmpty()) {
				dimArr = (String[]) dimList.toArray(new String[dimList.size()]);
			}
			ArrayList msuList = (ArrayList) face.getFavoriteMsuList(favID);
			if (msuList != null && !msuList.isEmpty()) {
				msuArr = (String[]) msuList.toArray(new String[msuList.size()]);
			}
			//

			AdhocViewQryStruct struct = new AdhocViewQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", struct);
			} catch (AppException ex) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "查询条件受理信息有误！");
			}

			getDefaultConValue(ConArrSel, struct);

			String adhoc_root = request.getParameter(AdhocConstant.ADHOC_ROOT);
			AdhocFacade facade = new AdhocFacade(new AdhocDao());

			ArrayList conListDefault = (ArrayList) facade
					.getDefaultConListByHocId(adhoc_root);
			UiAdhocConditionMetaTable[] defaultMeta = (UiAdhocConditionMetaTable[]) conListDefault
					.toArray(new UiAdhocConditionMetaTable[conListDefault
							.size()]);

			Field[] tmpStuct = struct.getClass().getFields();
			for (int i = 0; i < defaultMeta.length; i++) {
				for (int j = 0; j < tmpStuct.length; j++) {
					if (tmpStuct[j].getName().equals(
							defaultMeta[i].getFiled_name())) {
						if (AdhocConditionTag.GATHER_MON_CONDITION_ID
								.equals(defaultMeta[i].getCon_id())) {
							struct.op_time = DateUtil.getDiffMonth(-1,
									DateUtil.getNowDate());
						}
						if (AdhocConditionTag.GATHER_DAY_CONDITION_ID
								.equals(defaultMeta[i].getCon_id())) {
							struct.op_time = DateUtil.getDiffDay(-1,
									DateUtil.getNowDate());
						}
					}
				}
			}

			session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
			session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, struct);

			session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS, conArr);

			session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS, dimArr);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS, msuArr);

			UiAdhocFavoriteDefTable info = face.getFavoriteInfo(favID);
			//
			session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
			session.setAttribute(
					AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS, info);

			//
			session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);

			session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS, group_tag);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS,
					group_msu_tag);
			String fav_flag = StringB.NulltoBlank(request
					.getParameter("fav_flag"));

			session.removeAttribute(AdhocConstant.ADHOC_FAV_FLAG);
			session.setAttribute(AdhocConstant.ADHOC_FAV_FLAG, fav_flag);

			if (fav_flag.equals("1")) {// 从收藏夹导航过来时

				this.setNextScreen(request, "AdhocMain.screen");

			} else {
				this.setNextScreen(request, "AdhocParam.screen");
			}

		} else if ("add".equals(operType)) {// 增加收藏夹
			// 条件
			String[] conArr = null;
			String conStr = request.getParameter("con_str");
			if (conStr == null) {
				conStr = "";
			}
			if (conStr != null && !"".equals(conStr)) {
				conArr = conStr.split(",");
			}
			// 纬度
			String[] dimArr = null;
			String dimStr = request.getParameter("dim_str");
			if (dimStr == null) {
				dimStr = "";
			}
			if (dimStr != null && !"".equals(dimStr)) {
				dimArr = dimStr.split(",");
			}
			// 度量
			String[] msuArr = null;
			String msuStr = request.getParameter("msu_str");
			if (msuStr == null) {
				msuStr = "";
			}
			if (msuStr != null && !"".equals(msuStr)) {
				msuArr = msuStr.split(",");
			}
			// 收藏夹名称
			String favName = request.getParameter("fav_name");
			logcommon.debug("favName=" + favName);
			session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS, conArr);

			session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS, dimArr);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS, msuArr);
			// 生成一个同步序列作为收藏夹标识
			String favID = "";
			try {
				favID = AdhocUtil
						.dbGetMaxID(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE);
			} catch (AppException e) {

				e.printStackTrace();
			}
			logcommon.debug("fav_id=================" + favID);
			//
			AdhocFacade face = new AdhocFacade(new AdhocFavDao());
			int result = face.addNewFavorite(favID, favName, root, conStr,
					dimStr, msuStr, oper_no, "1");
			if (result < 0) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询收藏夹保存失败，请通知系统管理员！");
			} else {

				// 对选择的条件值进行更新
				updateMetaConValue(request, conStr, favID);

				UiAdhocFavoriteDefTable info = face.getFavoriteInfo(favID);
				//
				//
				String favorite_id = request.getParameter("myFavor_id"); // 我的收藏夹
				// 添加我的收藏夹

				String resId = LSInfoFavorite.addAdhocfavor(request, favID,
						favName, favorite_id, root, group_tag, group_msu_tag);

				update_UI_SYS_RULE_FAVORITE_RESOURCE(oper_no, favID, resId,
						root);

				session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);

				session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS,
						group_tag);

				session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS,
						group_msu_tag);
				//
				session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
				session.setAttribute(
						AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS, info);

				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "即席查询收藏夹保存成功！",
						"adhoc/AdhocParam.screen?reloadtree=true");
			}

			//

		} else if ("addqrydtl".equals(operType)) {// 增加清单收藏夹
			// 条件

			UiAdhocFavoriteDefTable favInfo = (UiAdhocFavoriteDefTable) session
					.getAttribute("UiAdhocFavoriteDefTable");

			String[] conArr = null;

			String conStr = favInfo.getConStr();
			if (conStr == null) {
				conStr = "";
			}
			if (conStr != null && !"".equals(conStr)) {
				conArr = conStr.split(",");
			}
			// 纬度
			String[] dimArr = null;
			String dimStr = favInfo.getDimStr();
			if (dimStr == null) {
				dimStr = "";
			}
			if (dimStr != null && !"".equals(dimStr)) {
				dimArr = dimStr.split(",");
			}
			// 度量
			String[] msuArr = null;
			String msuStr = favInfo.getMsuStr();
			if (msuStr == null) {
				msuStr = "";
			}
			if (msuStr != null && !"".equals(msuStr)) {
				msuArr = msuStr.split(",");
			}
			// 收藏夹名称
			String favName = request.getParameter("fav_name");
			logcommon.debug("favName=" + favName);
			session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS, conArr);

			session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS, dimArr);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS, msuArr);
			// 生成一个同步序列作为收藏夹标识
			String favID = "";
			try {
				favID = AdhocUtil
						.dbGetMaxID(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE);
			} catch (AppException e) {

				e.printStackTrace();
			}
			logcommon.debug("fav_id=================" + favID);
			//
			AdhocFacade face = new AdhocFacade(new AdhocFavDao());
			int result = face.addNewFavorite(favID, favName, root, conStr,
					dimStr, msuStr, oper_no, "2");
			if (result < 0) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询收藏夹保存失败，请通知系统管理员！");
			} else {

				// 对选择的条件值进行更新
				updateMetaConValue(request, favInfo, favID);

				UiAdhocFavoriteDefTable info = face.getFavoriteInfo(favID);
				//
				//
				String favorite_id = request.getParameter("myFavor_id"); // 我的收藏夹
				// 添加我的收藏夹
				// logcommon.debug(favID + ":" + favName + ":" + favorite_id +
				// ":" + root+ ":" + group_tag+ ":" + group_msu_tag);

				String resId = LSInfoFavorite.addAdhocfavor(request, favID,
						favName, favorite_id, root, group_tag, group_msu_tag);
				update_UI_SYS_RULE_FAVORITE_RESOURCE(oper_no, favID, resId,
						root);

				// 保存选择的查询字段到UI_ADHOC_USER_LIST表中
				saveChooseFieldToTbl(request, favID, oper_no, root);

				session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);

				session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS,
						group_tag);

				session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS,
						group_msu_tag);
				//
				session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
				session.setAttribute(
						AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS, info);

				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "清单收藏夹保存成功！");
			}

			//

		} else if ("del".equals(operType)) {
			// 收藏夹ID
			String favID = request.getParameter("fav_id");
			if (favID == null || "".equals(favID)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询收藏夹缺失，请通知系统管理员！");
			}
			// 即席查询门户
			AdhocFacade face = new AdhocFacade(new AdhocFavDao());
			int i = face.deleteFavorite(favID, oper_no, root);
			if (i < 0) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询收藏夹删除失败，请通知系统管理员！");
			} else {
				// 删除收藏夹
				// LSInfoFavorite.delAdhocfavor(request,oper_no,favID, root);

				LSInfoFavorite.delAdhocfavor(oper_no, favID, root);

				session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);

				session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS,
						group_tag);

				session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS,
						group_msu_tag);

				session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);

				session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);

				session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
				//
				session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);

				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "即席查询收藏夹删除成功！",
						"AdhocParam.screen?reloadtree=true");
			}
		}

	}

	private void saveChooseFieldToTbl(HttpServletRequest request, String favId,
			String oper_no, String root) {

		String sqlDel = "delete from UI_ADHOC_USER_LIST where ADHOC_ID='"
				+ root + "' and FAV_ID='" + favId + "' and OPER_NO='" + oper_no
				+ "'";
		try {
			WebDBUtil.execUpdate(sqlDel);
		} catch (AppException e2) {

			e2.printStackTrace();
		}

		String sql = "insert into UI_ADHOC_USER_LIST(ADHOC_ID,OPER_NO,MSU_FIELD,MSU_UNIT,MSU_DIGIT,SEQUENCE,STATUS,MSU_TYPE,MSU_NAME"
				+ ",MAP_CODE,DEFAULT_VIEW,DATA_TYPE,GROUP_NAME,GROUP_ID,FAV_ID) select ADHOC_ID,'"
				+ oper_no
				+ "',MSU_FIELD,MSU_UNIT,MSU_DIGIT,"
				+ "SEQUENCE,STATUS,MSU_TYPE,MSU_NAME,"
				+ "MAP_CODE,'N',DATA_TYPE,GROUP_NAME,GROUP_ID,'"
				+ favId
				+ "'"
				+ " from  ui_adhoc_user_list t where t.adhoc_id='"
				+ root
				+ "' and oper_no='-1'";

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e1) {

			e1.printStackTrace();
		}

		String[] checkList = request.getParameterValues("msuSelected");
		List listSql = new ArrayList();

		for (int i = 0; checkList != null && i < checkList.length; i++) {
			String sqlUpdate = "update UI_ADHOC_USER_LIST set DEFAULT_VIEW='Y' WHERE ADHOC_ID='"
					+ root
					+ "' and MSU_FIELD='"
					+ checkList[i]
					+ "' and FAV_ID='"
					+ favId
					+ "' and OPER_NO='"
					+ oper_no
					+ "'";

			// System.out.println(sqlUpdate);
			listSql.add(sqlUpdate);

		}
		if (listSql.size() > 0) {
			try {
				WebDBUtil.execTransUpdate((String[]) listSql
						.toArray(new String[listSql.size()]));
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 *
	 * @param request
	 * @param conArr
	 *            ,条件ID串
	 * @param favId
	 *            ,收件夹ID
	 * @desc: 更新条件值
	 *
	 */
	private void updateMetaConValue(HttpServletRequest request, String conStr,
			String favId) {
		String[] conArr = null;
		if (conStr != null && !"".equals(conStr)) {
			conArr = conStr.split(",");
		}
		List listSql = new ArrayList();

		for (int i = 0; conArr != null && i < conArr.length; i++) {
			String[] value = getConditionValue(request, conArr[i]);
			String strUpdate = "update ui_adhoc_rule_favorite_meta set field_VALUE_01='"
					+ value[0]
					+ "', field_VALUE_02='"
					+ value[1]
					+ "',field_VALUE_01_desc='"
					+ value[2]
					+ "',field_VALUE_02_desc='"
					+ value[3]
					+ "' where favorite_id="
					+ favId
					+ " and META_CODE='"
					+ conArr[i] + "' AND META_TYPE='1'";
			// logcommon.debug("strUpdate:" + strUpdate);
			listSql.add(strUpdate);
		}

		if (listSql.size() > 0) {
			try {
				WebDBUtil.execTransUpdate((String[]) listSql
						.toArray(new String[listSql.size()]));
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

	}

	private void updateMetaConValue(HttpServletRequest request,
			UiAdhocFavoriteDefTable favInfo, String favId) {

		List listSql = favInfo.getListValue();
		List listConStr = favInfo.getListConStr();

		List listExecute = new ArrayList();
		for (int i = 0; i < listSql.size(); i++) {
			String[] value = (String[]) listSql.get(i);
			String strUpdate = "update ui_adhoc_rule_favorite_meta set field_VALUE_01='"
					+ value[0]
					+ "', field_VALUE_02='"
					+ value[1]
					+ "',field_VALUE_01_desc='"
					+ value[2]
					+ "',field_VALUE_02_desc='"
					+ value[3]
					+ "' where favorite_id="
					+ favId
					+ " and META_CODE='"
					+ (String) listConStr.get(i) + "' AND META_TYPE='1'";
			logcommon.debug("strUpdate:" + strUpdate);
			listExecute.add(strUpdate);
		}

		if (listSql.size() > 0) {
			try {
				WebDBUtil.execTransUpdate((String[]) listExecute
						.toArray(new String[listExecute.size()]));
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

	}

	private String[] getConditionValue(HttpServletRequest request, String conArr) {
		String sql = "select a.qry_name,a.con_type from ui_adhoc_condition_meta a where a.con_id='"
				+ conArr + "'";
		String[] strRetn = { "", "", "", "" };

		try {
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				int con_type = Integer.parseInt(arr[0][1]);
				switch (con_type) {
				case 2:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				case 9:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				case 10:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				default:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0]));
					strRetn[1] = "";
					strRetn[2] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_desc"));
					break;
				}

			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return strRetn;
	}

	/**
	 *
	 * @param fav_id
	 * @return
	 * @desc:获取收藏夹元数据信息
	 */

	private String[][] getFavInfoConArray(String fav_id) {
		@SuppressWarnings("unused")
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

	/**
	 *
	 * @param conArr
	 * @return
	 * @throws AppException
	 * @throws AppException
	 * @DESC 获取保存的默认值
	 */
	public static void getDefaultConValue(String conArr[][],
			AdhocViewQryStruct struct) {
		// AdhocViewQryStruct struct = new AdhocViewQryStruct();
		if (conArr != null && conArr.length > 0) {
			for (int xx = 0; xx < conArr.length; xx++) {
				String qry_name = StringB.NulltoBlank(conArr[xx][5]).substring(
						5);
				String param1 = StringB.NulltoBlank(conArr[xx][10]);
				String param2 = StringB.NulltoBlank(conArr[xx][11]);
				String param3 = StringB.NulltoBlank(conArr[xx][12]);
				// String param4 = StringB.NulltoBlank(conArr[xx][13]);

				// logcommon.debug(qry_name + ":" + param1 + ":" + param2);

				Class objClass = new AdhocViewQryStruct().getClass();
				Field[] fields = objClass.getFields();

				int con_type = Integer.parseInt(conArr[xx][2]);
				try {
					switch (con_type) {
					case 2:
						for (int i = 0; i < fields.length; i++) {
							String fTypeName = fields[i].getType().getName(); // 获得字段的类型名称
							String fName = fields[i].getName(); // 获得字段的名称

							if (fTypeName.lastIndexOf("java.lang.String") >= 0) {
								// logcommon.debug(fTypeName + ":" + fName);
								if (fName.equals(qry_name + "_A_11")) {

									setFieldValueFromObject(fields[i], struct,
											param1);

								} else if (fName.equals(qry_name + "_A_22")) {
									setFieldValueFromObject(fields[i], struct,
											param2);
								}

							}
						}

						break;
					case 9:
						for (int i = 0; i < fields.length; i++) {
							String fTypeName = fields[i].getType().getName(); // 获得字段的类型名称
							String fName = fields[i].getName(); // 获得字段的名称

							if (fTypeName.lastIndexOf("java.lang.String") >= 0) {
								// logcommon.debug(fTypeName + ":" + fName);
								if (fName.equals(qry_name + "_A_11")) {
									setFieldValueFromObject(fields[i], struct,
											param1);
								} else if (fName.equals(qry_name + "_A_22")) {
									setFieldValueFromObject(fields[i], struct,
											param2);
								}

							}
						}
						break;
					case 10:
						for (int i = 0; i < fields.length; i++) {
							String fTypeName = fields[i].getType().getName(); // 获得字段的类型名称
							String fName = fields[i].getName(); // 获得字段的名称

							if (fTypeName.lastIndexOf("java.lang.String") >= 0) {
								// logcommon.debug(fTypeName + ":" + fName);
								if (fName.equals(qry_name + "_A_11")) {
									setFieldValueFromObject(fields[i], struct,
											param1);
								} else if (fName.equals(qry_name + "_A_22")) {
									setFieldValueFromObject(fields[i], struct,
											param2);
								}

							}
						}
						break;
					default:
						for (int i = 0; i < fields.length; i++) {
							String fTypeName = fields[i].getType().getName(); // 获得字段的类型名称
							String fName = fields[i].getName(); // 获得字段的名称
							if (fTypeName.lastIndexOf("java.lang.String") >= 0) {
								// logcommon.debug(fTypeName + ":" + fName);
								if (fName.equals(qry_name)) {
									setFieldValueFromObject(fields[i], struct,
											param1);
								}
								if (fName.equals(qry_name + "_desc")) {
									setFieldValueFromObject(fields[i], struct,
											param3);
								}

							}
						}
						break;
					}
				} catch (AppException e) {

					e.printStackTrace();
				}

			}

		}
	}

	private void update_UI_SYS_RULE_FAVORITE_RESOURCE(String oper_no,
			String favId, String resId, String adhocId) {
		String sql = " update UI_SYS_RULE_FAVORITE_RESOURCE set RES_TYPE='ADHOC',ADHOC_FAV_ID="
				+ favId
				+ ",ADHOC_ID='"
				+ adhocId
				+ "' where USER_ID='"
				+ oper_no + "' and RES_ID='" + resId + "'";

		System.out.println(sql);

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	private static void setFieldValueFromObject(Field field, Object obj,
			Object val) throws AppException {

		String strVal = null;
		if (val == null)
			return;
		try {
			String typeName = field.getType().getName();
			if (typeName.equals("java.lang.String")) {
				// logcommon.debug("typeName:" + typeName);
				strVal = (String) val;
				field.set(obj, strVal);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex);
		}
	}
}
