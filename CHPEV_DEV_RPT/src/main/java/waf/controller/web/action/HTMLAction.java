package waf.controller.web.action;

import waf.controller.web.action.HTMLActionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import com.ailk.bi.common.event.*;

import javax.servlet.ServletConfig;

/**
 * This class is the base interface to request handlers on the web tier.
 * 
 */
public interface HTMLAction extends java.io.Serializable {
	public void setServletConfig(ServletConfig config);

	public void setServletContext(ServletContext context);

	public void doStart(HttpServletRequest request) throws HTMLActionException;

	public JBTable perform(HttpServletRequest request)
			throws HTMLActionException;

	public void doEnd(HttpServletRequest request, JBTable eventResponse)
			throws HTMLActionException;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException;

	/**
	 * 设置返回结果的页面
	 * 
	 * @param request
	 * @param result
	 */
	public void setNextScreen(HttpServletRequest request, String result);

	/**
	 * 不设置下一个应答页面
	 * 
	 * @param request
	 */
	public void setNvlNextScreen(HttpServletRequest request);

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
			Object objValue);

	/**
	 * 清空指定的session内的属性
	 * 
	 * @param request
	 * @param attrName
	 */
	public void clearSesssionAttr(HttpServletRequest request, String attrName);
}
