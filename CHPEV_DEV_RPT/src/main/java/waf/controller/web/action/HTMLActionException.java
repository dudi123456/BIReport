package waf.controller.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.util.WebKeys;

import com.ailk.bi.common.app.AppWebUtil;

/**
 * This exception will be thrown when there is an error processing a flow
 * handler 在页面处理Action类中可以抛出这个异常，如页面输入错误，查询数据库错误时，可以进行 throw new
 * HTMLActionException(HttpSession session, int eType, String strMsg);
 * 这样错误消息就可以在页面中进行显示。
 */
public class HTMLActionException extends Exception implements
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2789791525319346214L;

	/**
	 * web异常处理页
	 */
	public static final String HTML_ERROR_SCREEN = "htmlError.screen";

	/**
	 * 警告类型异常
	 */
	public static final int WARN_PAGE = 0;

	/**
	 * 错误类型异常
	 */
	public static final int ERROR_PAGE = 1;

	/**
	 * 操作成功提示
	 */
	public static final int SUCCESS_PAGE = 2;

	/**
	 * 营业成功业务 进行打印
	 */
	public static final int PRINT_PAGE = 3;

	/**
	 * 帐务成功业务 进行打印
	 */
	public static final int ACCT_PRINT_PAGE = 4;

	int excptionType;

	String excptionMsg = "";

	public HTMLActionException() {
	}

	public HTMLActionException(String str) {
		super(str);
	}

	/**
	 * 函数名：页面处理抛出函数
	 * 
	 * @param session
	 *            页面中的session信息
	 * @param eType
	 *            异常的类型 可以为本函数定义的三种静态变量
	 * @param strMsg
	 *            错误提示消息
	 */
	public HTMLActionException(HttpSession session, int eType, String strMsg) {
		AppWebUtil.clearRptToken(session);
		this.excptionType = eType;
		this.excptionMsg = strMsg;

		if (session != null) {
			session.setAttribute(WebKeys.EXPTICON, eType + "");
			session.setAttribute(WebKeys.EXPTINFO, strMsg);
		}
	}

	/**
	 * 函数名：页面处理抛出函数
	 * 
	 * @param session
	 *            页面中的session信息
	 * @param eType
	 *            异常的类型 可以为本函数定义的三种静态变量
	 * @param strMsg
	 *            错误提示消息
	 */
	public HTMLActionException(HttpSession session, int eType, String strMsg,
			String strURL) {
		// 清除重复提交判断
		AppWebUtil.clearRptToken(session);
		this.excptionType = eType;
		this.excptionMsg = strMsg;
		if (session != null) {
			session.setAttribute(WebKeys.EXPTICON, eType + "");
			session.setAttribute(WebKeys.EXPTINFO, strMsg);
			session.setAttribute(WebKeys.EXPURL, strURL);
		}
	}

	/**
	 * 在jsp中调用的抛出异常页面的处理类
	 * 
	 * @param context
	 * @param request
	 * @param response
	 * @param eType
	 * @param strMsg
	 * @param strURL
	 */
	public static synchronized void WebException(HttpServletRequest request,
			HttpServletResponse response, int eType, String strMsg,
			String strURL) {

		String nextScreen = HTML_ERROR_SCREEN;
		// put the exception in the request
		HttpSession session = request.getSession();
		AppWebUtil.clearRptToken(session);
		if (session != null) {
			session.setAttribute(WebKeys.EXPTICON, eType + "");
			session.setAttribute(WebKeys.EXPTINFO, strMsg);
			session.setAttribute(WebKeys.EXPURL, strURL);
		}
		try {
			request.getRequestDispatcher(nextScreen).forward(request, response);
		} catch (Exception ex) {
			System.err
					.println("**FATAL ERROR:WebException Page Display Error! ["
							+ ex.toString() + "]");
		}
	}

	public static synchronized void WebException(HttpServletRequest request,
			HttpServletResponse response, int eType, String strMsg) {
		WebException(request, response, eType, strMsg, "");
	}

	public String getExcptionMsg() {
		return excptionMsg;
	}

	public int getExcptionType() {
		return excptionType;
	}
}