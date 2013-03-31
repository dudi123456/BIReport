package com.ailk.bi.adhoc.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.util.*;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes" })
public class AdhocSelfDimHTMLAction extends HTMLActionSupport {
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
		String adhoc_id = CommTool.getParameterGB(request, "hoc_id");
		if (adhoc_id == null || "".equals(adhoc_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "即席查询功能定义缺失，请通知系统管理员！");
		}
		//
		String dim_relation_field = CommTool.getParameterGB(request,
				"col_field");
		if (dim_relation_field == null || "".equals(dim_relation_field)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "自定义纬度分档标识字段缺失，请通知系统管理员！");
		}

		// 定义门户
		AdhocFacade face = new AdhocFacade(new AdhocDao());
		// 取得对应mapCode
		String mapCode = face.getDimMapCode(adhoc_id, dim_relation_field);

		// 默认纬度值，由于需要对应特定的纬度MAP,照顾ODS。
		HashMap codeMap = CodeParamUtil.codeListParamFetcher(request, mapCode);
		session.removeAttribute(AdhocConstant.ADHOC_DIM_DEFAULT_VALUE);
		session.setAttribute(AdhocConstant.ADHOC_DIM_DEFAULT_VALUE, codeMap);

		// 已经定制纬度
		UiAdhocRuleUserDimTable[] userDim = face.getAdhocUserDimList(adhoc_id,
				oper_no, dim_relation_field);
		session.removeAttribute(AdhocConstant.ADHOC_DIM_FIXED_VALUE);
		session.setAttribute(AdhocConstant.ADHOC_DIM_FIXED_VALUE, userDim);

		//
		session.setAttribute(AdhocConstant.ADHOC_DIM_FIXED_ADHOC_ID, adhoc_id);
		session.setAttribute(AdhocConstant.ADHOC_DIM_FIXED_RELATION_FIELD,
				dim_relation_field);
		session.setAttribute(AdhocConstant.ADHOC_DIM_FIXED_OPER_NO, oper_no);

		// 返回
		this.setNextScreen(request, "AdhocSelfDim.screen");

	}

}
