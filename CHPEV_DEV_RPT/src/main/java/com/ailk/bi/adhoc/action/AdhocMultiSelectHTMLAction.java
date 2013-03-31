package com.ailk.bi.adhoc.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocMultiConFilter;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocMultiSelectHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();

		/********************************* 固定条件提取开始 ****************************************************/
		// javascript:MultipleSelected(this,'ADH01','202',qry__innet_channel,qry__innet_channel_desc,'入网渠道');
		// function
		// MultipleSelected(theObj,hoc_id,con_id,qry_name,qry_desc,con_name){
		// AdhocMultiSelect.do?adhoc_id="+hoc_id+
		// "&con_id="+con_id+
		// "&qry_name="+qry_name.value+
		// "&con_name="+con_name+conStr
		// 即席查询code
		String hoc_id = CommTool.getParameterGB(request,
				AdhocConstant.ADHOC_MULTI_SELECT_QRY_HOC_ID);
		if (null == hoc_id || "".equalsIgnoreCase(hoc_id)) {
			return;
		}
		session.setAttribute(AdhocConstant.ADHOC_MULTI_SELECT_HOC_ID_SESSION,
				hoc_id);

		// 即席查询条件con_id
		String con_id = CommTool.getParameterGB(request,
				AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_ID);
		if (null == con_id || "".equalsIgnoreCase(con_id)) {
			return;
		}
		session.setAttribute(
				AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION, con_id);

		// 当前已选值qry_name:ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION
		String qry_name_value = CommTool.getParameterGB(request,
				AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_QRY_NAME);
		session.removeAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION);
		session.setAttribute(
				AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION,
				qry_name_value);

		// 条件名称con_name
		String con_name = request.getParameter(AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_NAME);
		try {
			con_name = java.net.URLDecoder.decode(con_name,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (null == con_name) {
			con_name = "";
		}
		session.setAttribute(
				AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION,
				con_name);

		// logcommon.debug("1111");
		/********************************* 固定条件提取结束 ****************************************************/

		AdhocFacade facade = new AdhocFacade(new AdhocDao());

		/******************************** 配置前置条件提取开始 *****************************************************/
		// 公用条件svc_knd
		/*
		 * String svcknd = CommTool.getParameterGB(request,AdhocConstant.
		 * ADHOC_MULTI_SELECT_QRY_SVC_KND); if (null == svcknd) { svcknd = ""; }
		 */
		// 区域或者部门信息area_id
		/*
		 * String areaid = CommTool.getParameterGB(request,AdhocConstant.
		 * ADHOC_MULTI_SELECT_QRY_AREA_ID); if (areaid == null) { areaid = ""; }
		 */

		/*
		 * String sec_area = CommTool.getParameterGB(request,AdhocConstant.
		 * ADHOC_MULTI_SELECT_QRY_SEC_AREA_ID); if (sec_area == null) { sec_area
		 * = ""; }
		 */

		UiAdhocMultiConFilter[] multiList = facade
				.getAdhocMultiSelectCondition(hoc_id, con_id);
		if (multiList == null) {
			multiList = new UiAdhocMultiConFilter[0];
		}

		String sqlcon = "";
		for (int i = 0; i < multiList.length; i++) {

			String value = CommTool.getParameterGB(request,
					multiList[i].getFilter_con_str());
			if (value != null && !"".equals(value)) {
				sqlcon += " AND " + multiList[i].getFilter_con_str() + "=";
				if ("1".equals(multiList[i].getColumn_type())) {
					sqlcon += value;
				} else if ("2".equals(multiList[i].getColumn_type())) {
					sqlcon += "'" + value + "'";
				} else {
					sqlcon += "'" + value + "'";
				}

			}

		}

		/********************************* 配置前置条件提取结束 ****************************************************/
		// logcommon.debug("2222");

		// 得到当前条件的元数据

		UiAdhocConditionMetaTable conMeta = facade.getConditionMetaInfo(con_id);

		// 取出查询的SQL,先不考虑过虑，过虑的话构造WHERE 条件
		String sql = conMeta.getCon_rule();

		if (sql.indexOf("?") > -1) {
			sql = sql.replaceAll("\\?", sqlcon);
		}
		logcommon.debug("根据配置条件UI_ADHOC_MULTI_CON_FILTER过滤=========" + sql);

		String svces[][] = null;
		try {
			svces = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			ex1.printStackTrace();
		}

		if (null != svces && svces.length > 0) {
			session.removeAttribute(AdhocConstant.ADHOC_MULTI_SELECT_VAlUE_SESSION);
			session.setAttribute(
					AdhocConstant.ADHOC_MULTI_SELECT_VAlUE_SESSION, svces);
		}

		this.setNextScreen(request, "AdhocMultiSelect.screen");

	}
}
