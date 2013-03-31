package com.ailk.bi.mainpage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.PubInfoNewsTable;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.mainpage.common.NewsOpt;
import com.ailk.bi.system.facade.impl.CommonFacade;

/**
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author renhui
 * @version 1.0
 */

public class NewsViewHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 经营公告处理
	 * 
	 * @param request
	 * @param response
	 * @throws HTMLActionException
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 变量申明区
		HttpSession session = request.getSession();
		// 用户信息
		String oper_no = CommonFacade.getLoginId(session);
		String oper_name = CommonFacade.getLoginName(session);
		// 获取操作类型值
		String opType = request.getParameter("opType");
		if (opType == null || "".equals(opType))
			opType = "list";
		// 获取记录ID值
		String news_id = request.getParameter("news_id");
		if (news_id == null) {
			news_id = "0";
		}
		// 查询条件
		LsbiQryStruct lsbiQry = new LsbiQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", lsbiQry);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
		}

		if ("list".equals(opType)) {
			// 开始日期
			if (lsbiQry.date_s == null || "".equals(lsbiQry.date_s)) {
				lsbiQry.date_s = "";
			}
			// 结束日期
			if (lsbiQry.date_e == null || "".equals(lsbiQry.date_e)) {
				lsbiQry.date_e = lsbiQry.date_s;
			}
			// 标题
			if (lsbiQry.obj_name == null || "".equals(lsbiQry.obj_name)) {
				lsbiQry.obj_name = "";
			}
			// 发布人
			if (lsbiQry.oper_name == null || "".equals(lsbiQry.oper_name)) {
				lsbiQry.oper_name = "";
			}
			// 排序字段
			if (lsbiQry.order_code == null || "".equals(lsbiQry.order_code)) {
				lsbiQry.order_code = "";
			}
			// 排序顺序
			if (lsbiQry.order == null || "".equals(lsbiQry.order)) {
				lsbiQry.order = "";
			}

			try {
				session.setAttribute(WebKeys.ATTR_NEWS_LISTS,
						NewsOpt.getNewsInfos("", lsbiQry, 0));
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"<font size=2><b>获取经营公告信息失败！</b></font>");
			}
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct, lsbiQry);
			setNextScreen(request, "NewsList.screen");
		}
		if ("info".equals(opType)) {
			try {
				session.setAttribute(WebKeys.ATTR_NEWS_INFO,
						NewsOpt.getNewsInfo(news_id));
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"<font size=2><b>获取经营公告信息失败！</b></font>");
			}
			setNextScreen(request, "NewsInfo.screen");
		}
		if ("add".equals(opType)) {
			String subject = StringB.toGB(request.getParameter("newsSubject")); // 取得主题
			String descr = StringB.toGB(request.getParameter("newsDesc")); // 取得内容
			String news_type = request.getParameter("newsType");// 类型

			try {
				NewsOpt.insertNews(oper_no, oper_name, subject, descr,
						news_type);
				session.setAttribute(WebKeys.ATTR_NEWS_LISTS,
						NewsOpt.getNewsInfos("", lsbiQry, 0));
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "添加经营公告内容成功！",
						"NewsList.screen");
			} catch (AppException ux) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "添加经营公告内容失败！",
						"/main/newsedit.jsp?opType=add");
			}
		}
		if ("edit".equals(opType)) {
			String subject = StringB.toGB(request.getParameter("newsSubject")); // 取得主题
			String descr = StringB.toGB(request.getParameter("newsDesc")); // 取得内容
			String news_type = request.getParameter("newsType");// 类型

			try {
				NewsOpt.updateNews(news_id, subject, descr, news_type);
				session.setAttribute(WebKeys.ATTR_NEWS_LISTS,
						NewsOpt.getNewsInfos("", lsbiQry, 0));
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "修改经营公告内容成功！",
						"NewsList.screen");
			} catch (AppException ux) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "修改经营公告内容失败！",
						"NewsList.screen");
			}
		}
		if ("del".equals(opType)) {
			try {
				NewsOpt.deleteNews(news_id);
			} catch (AppException ux) {
				ux.printStackTrace();
			}
			PubInfoNewsTable[] news = (PubInfoNewsTable[]) session
					.getAttribute(WebKeys.ATTR_NEWS_LISTS);
			if (news != null && news.length > 0) {
				int k = 0;
				PubInfoNewsTable nnews[] = new PubInfoNewsTable[news.length - 1];
				for (int i = 0; i < news.length; i++) {
					if (news[i].news_id.equals(news_id)) {
						continue;
					} else {
						nnews[k++] = news[i];
					}
				}
				session.setAttribute(WebKeys.ATTR_NEWS_LISTS, nnews);
			}
			setNextScreen(request, "NewsList.screen");
		}
	}
}