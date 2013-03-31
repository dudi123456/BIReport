package waf.controller.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;

import com.ailk.bi.common.event.*;

import waf.controller.web.util.WebKeys;

/**
 * This class is the default implementation of the WebAction
 *
 */
public abstract class HTMLActionSupport implements HTMLAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 3088875326127081997L;

	// context和config可以被HTMLAction直接使用，但是千万不可修改
	protected ServletContext context;

	protected ServletConfig config;

	public Logger logcommon = Logger.getLogger(getClass());

	public void setServletConfig(ServletConfig config) {
		this.config = config;
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	public void doStart(HttpServletRequest request) {
		// 清空旧的页面跳转信息
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}
		session.removeAttribute(WebKeys.HANDLER_SCREEN_KEY);

	}

	public JBTable perform(HttpServletRequest request)
			throws HTMLActionException {
		throw new HTMLActionException(request.getSession(),
				HTMLActionException.WARN_PAGE,
				"**FATAL ERROR! 基类HTMLActionException中的perform方法没有实现，请开发者必须实现！");
	}

	public void doEnd(HttpServletRequest request, JBTable eventResponse)
			throws HTMLActionException {
	}

	/**
	 * 进行类似Struts的调用，即用doTans()替代perform()和doEnd()方法
	 *
	 * @param request
	 * @param response
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		throw new HTMLActionException(request.getSession(),
				HTMLActionException.WARN_PAGE,
				"**FATAL ERROR! 基类HTMLActionException中的doTrans方法没有实现，请开发者必须实现！");
	}

	/**
	 * 设置返回结果的页面
	 *
	 * @param request
	 * @param result
	 */
	public void setNextScreen(HttpServletRequest request, String result) {
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}
		session.setAttribute(WebKeys.HANDLER_SCREEN_KEY, result);
	}

	/**
	 * 调用此方法使HTMLAction不返回下一屏幕(通常使用此方法时，在HTMLAction方法里已经指定了输出内容，如Excel的输出等)
	 *
	 * @param request
	 */
	public void setNvlNextScreen(HttpServletRequest request) {
		setNextScreen(request, WebKeys.NULL_SCREEN);
	}

	/**
	 * 设置session的属性值
	 *
	 * @param request
	 * @param attrName
	 *            设置的属性名
	 * @param objValue
	 *            设置的属性值
	 */

	public void setSesssionAttr(HttpServletRequest request, String attrName,
			Object objValue) {
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}
		session.setAttribute(attrName, objValue);
	}

	/**
	 * 清空指定的session内的属性
	 *
	 * @param request
	 * @param attrName
	 */
	public void clearSesssionAttr(HttpServletRequest request, String attrName) {
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}
		session.removeAttribute(attrName);
	}
}