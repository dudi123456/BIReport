package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class AdhocUserListInitHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();

		// 当前分析即席查询功能点
		String adhoc_root = CommTool.getParameterGB(request,
				AdhocConstant.ADHOC_ROOT);
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		UiAdhocInfoDefTable hocInfo = facade.getAdhocInfo(adhoc_root);
		// 操作员
		String oper_no = CommonFacade.getLoginId(session);

		// 即席查询视图结构
		AdhocViewQryStruct qryStruct = new AdhocViewQryStruct();
		// 取得查询结构数据
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(request.getSession(),
					HTMLActionException.WARN_PAGE, "查询条件受理信息有误！");
		}
		//
		if (qryStruct.gather_mon == null || "".equals(qryStruct.gather_mon)) {
			qryStruct.gather_mon = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		//
		qryStruct.adhoc_id = adhoc_root;
		// 保存查询结构
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);
		logcommon.debug("qryStruct.gather_mon=" + qryStruct.gather_mon);
		logcommon.debug("qryStruct.cust_type=" + qryStruct.cust_type);
		logcommon.debug("qryStruct.adhoc_id=" + qryStruct.adhoc_id);

		this.setNextScreen(request, "AdhocUserListSelect.screen");
	}

}
