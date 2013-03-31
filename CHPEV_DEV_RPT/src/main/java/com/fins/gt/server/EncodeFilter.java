package com.fins.gt.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fins.gt.util.StringUtils;

public class EncodeFilter implements Filter {

	protected static String encoding = "UTF-8";
	protected static String ajaxEncoding = "UTF-8";
	protected static String ajaxHeaderName = "X-Requested-With";
	protected static String ajaxHeaderValue = "XMLHttpRequest";
	protected static boolean responseHeadersSetBeforeDoFilter = true;

	public String webRootRealPath = null;
	public ServletContext servletContext = null;
	protected FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		servletContext = filterConfig.getServletContext();
		webRootRealPath = servletContext.getRealPath("/");

		encoding = StringUtils.emptyConvert(
				filterConfig.getInitParameter("encoding"), encoding);
		ajaxEncoding = StringUtils.emptyConvert(
				filterConfig.getInitParameter("ajaxEncoding"), ajaxEncoding);
		ajaxHeaderName = StringUtils
				.emptyConvert(filterConfig.getInitParameter("ajaxHeaderName"),
						ajaxHeaderName);
		ajaxHeaderValue = StringUtils.emptyConvert(
				filterConfig.getInitParameter("ajaxHeaderValue"),
				ajaxHeaderValue);
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (isAjaxRequest(request) && isEnableEncoding(ajaxEncoding)) {
			request.setCharacterEncoding(ajaxEncoding);
			// response.setCharacterEncoding(ajaxEncoding);
			response.setContentType("text/xml;charset=" + ajaxEncoding);

			request.getParameter(ajaxHeaderName); // for lock encoding
		} else if (isEnableEncoding(encoding)) {
			request.setCharacterEncoding(encoding);
			// response.setCharacterEncoding(encoding);
			response.setContentType("text/xml;charset=" + encoding);
		}

		if (responseHeadersSetBeforeDoFilter) {
		}
		chain.doFilter(servletRequest, servletResponse);
		if (!responseHeadersSetBeforeDoFilter) {
		}
	}

	private boolean isEnableEncoding(String encoding) {
		return !"false".equalsIgnoreCase(encoding)
				&& !"none".equalsIgnoreCase(encoding);
	}

	protected boolean isAjaxRequest(HttpServletRequest request) {
		return ajaxHeaderValue.equalsIgnoreCase(request
				.getHeader(ajaxHeaderName));
	}

	public void destroy() {
		webRootRealPath = null;
		servletContext = null;
		filterConfig = null;
	}

}
