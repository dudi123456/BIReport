package com.ailk.bi.mainpage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.util.DBTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * @author d90080502
 * 
 */
@SuppressWarnings({ "unused" })
public class SendSmsHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 变量申明区
		HttpSession session = request.getSession();

		String opType = request.getParameter("opType");
		if (opType == null || "".equals(opType))
			opType = "view";
		System.out.println("opType=" + opType);

		if ("view".equals(opType)) {
			// 进入发送页面
			setNextScreen(request, "SendSms.screen");
		}
		if ("send".equals(opType) || "rptsend".equals(opType)) {
			// 发送短信
			// 手机号码
			String deviceNumber = request.getParameter("deviceNumber");
			if (deviceNumber == null || "".equals(deviceNumber)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"<font size=2><b>手机号码错误！</b></font>");
			}
			// 短信内容
			String message = request.getParameter("message");
			if (message == null || "".equals(message)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"<font size=2><b>短信内容为空！</b></font>");
			}
			try {
				sendSms(deviceNumber, message);
				if ("rptsend".equals(opType)) {
					// 报表基本信息
					PubInfoResourceTable rptTable = (PubInfoResourceTable) session
							.getAttribute(WebKeys.ATTR_REPORT_TABLE);
					// 查询条件
					LsbiQryStruct lsbiQry = new LsbiQryStruct();
					try {
						AppWebUtil.getHtmlObject(request, "qry", lsbiQry);
					} catch (AppException ex) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE,
								"提取界面查询信息失败，请注意是否登陆超时！");
					}
					if (lsbiQry == null) {
						lsbiQry = new LsbiQryStruct();
					}
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE,
							"<font size=2><b>短信发送成功！</b></font>",
							"ReportView.rptdo?rpt_id=" + rptTable.rpt_id
									+ "&p_date=" + lsbiQry.date_s);
				} else {
					session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE,
							"<font size=2><b>短信发送成功！</b></font>");
				}
			} catch (AppException ex) {
				ex.printStackTrace();
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"<font size=2><b>短信发送失败！</b></font>");
			}
		}
	}

	private static void sendSms(String deviceNumber, String message)
			throws AppException {
		String[] strSql = new String[2];
		String[] arrNum = deviceNumber.split(";");
		for (int i = 0; arrNum != null && i < arrNum.length; i++) {
			String[] arrmessage = message.split(";");
			for (int m = 0; arrmessage != null && m < arrmessage.length; m++) {
				strSql[0] = SQLGenator.genSQL("I4110", arrNum[i],
						arrmessage[m].trim());
				strSql[1] = SQLGenator.genSQL("I4111", arrNum[i],
						arrmessage[m].trim());
				System.out.println(strSql[0] + "\n\n\n" + strSql[1]);
				WebDBUtil.execUpdates(DBTool.getWLSConn(), strSql);
			}
		}
	}
}
