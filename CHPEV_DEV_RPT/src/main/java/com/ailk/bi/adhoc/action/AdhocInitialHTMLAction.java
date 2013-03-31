package com.ailk.bi.adhoc.action;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
//import com.ailk.bi.adhoc.util.AdhocConditionTag;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocHelper;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocInitialHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		HttpSession session = request.getSession();

		// 当前分析即席查询功能点
		String adhoc_root = CommTool.getParameterGB(request, AdhocConstant.ADHOC_ROOT);
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		// UiAdhocInfoDefTable hocInfo = facade.getAdhocInfo(adhoc_root);
		session.removeAttribute(AdhocConstant.ADHOC_FAV_FLAG);

		// 清空标志
		String clear_flag = request.getParameter(AdhocConstant.ADHOC_CLEAR_SESSION_TAG);
		if (clear_flag != null && "1".equals(clear_flag)) {
			AdhocHelper.clearAdhocSessionTag(session);
		}
		// 默认选择条件
		ArrayList conList = (ArrayList) facade.getDefaultConListByHocId(adhoc_root);
		UiAdhocConditionMetaTable[] defaultMeta = (UiAdhocConditionMetaTable[]) conList
				.toArray(new UiAdhocConditionMetaTable[conList.size()]);

		// 即席查询视图结构
		AdhocViewQryStruct qryStruct = new AdhocViewQryStruct();
		// 取得查询结构数据
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
					"查询条件受理信息有误！");
		}
		// 这一块有问题，还需要设计！
		Field[] tmpStuct = qryStruct.getClass().getFields();
		for (int i = 0; i < defaultMeta.length; i++) {
			for (int j = 0; j < tmpStuct.length; j++) {
				if (tmpStuct[j].getName().equals(defaultMeta[i].getFiled_name())) {
					if (defaultMeta[i].getCon_type().equals("7")) {// 日期
						qryStruct.op_time = DateUtil.getDiffDay(-1, DateUtil.getNowDate());
					}
					if (defaultMeta[i].getCon_type().equals("8")) {// 月份
						qryStruct.op_time = DateUtil.getDiffMonth(-1, DateUtil.getNowDate());
					}
					if (defaultMeta[i].getCon_type().equals("9")) {// 日期段
						qryStruct.op_time_A_11 = DateUtil.getDiffDay(-1, DateUtil.getNowDate());
						qryStruct.op_time_A_22 = DateUtil.getDiffDay(-1, DateUtil.getNowDate());
					}
					if (defaultMeta[i].getCon_type().equals("10")) {// 月份段
						qryStruct.op_time_A_11 = DateUtil.getDiffMonth(-1, DateUtil.getNowDate());
						qryStruct.op_time_A_22 = DateUtil.getDiffMonth(-1, DateUtil.getNowDate());
					}
					/*
					 * if(AdhocConditionTag.GATHER_MON_CONDITION_ID.equals(
					 * defaultMeta[i].getCon_id())){ qryStruct.op_time =
					 * DateUtil.getDiffMonth(-1, DateUtil.getNowDate()); }
					 * if(AdhocConditionTag
					 * .GATHER_DAY_CONDITION_ID.equals(defaultMeta
					 * [i].getCon_id())){ qryStruct.op_time =
					 * DateUtil.getDiffDay(-1, DateUtil.getNowDate()); }
					 */
				}
			}
		}
		// 保持即席查询ID
		qryStruct.adhoc_id = adhoc_root;

		// 保存查询结构
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);
		this.setNextScreen(request, "AdhocMain.screen");
	}
}
