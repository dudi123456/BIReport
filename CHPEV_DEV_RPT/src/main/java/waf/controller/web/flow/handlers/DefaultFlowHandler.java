package waf.controller.web.flow.handlers;

import waf.controller.web.flow.FlowHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import waf.controller.web.flow.FlowHandlerException;
import waf.controller.web.util.WebKeys;

/**
 * <p>
 * Title: 北京朗新信息系统公司 CNC语音项目
 * </p>
 * <p>
 * Description: 公共底层函数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DefaultFlowHandler implements FlowHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748308298657583108L;

	public DefaultFlowHandler() {
	}

	public void doStart(HttpServletRequest request) {
	}

	public String processFlow(HttpServletRequest request)
			throws FlowHandlerException {
		/** @todo Implement this waf.controller.web.flow.FlowHandler method */
		HttpSession session = request.getSession();
		if (session == null) {
			return null;
		}
		String forwardScreen = (String) session
				.getAttribute(WebKeys.HANDLER_SCREEN_KEY);
		return forwardScreen;
	}

	public void doEnd(HttpServletRequest request) {
		// 将session中的屏幕流转信息删除
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}
		session.removeAttribute(WebKeys.HANDLER_SCREEN_KEY);
	}
}