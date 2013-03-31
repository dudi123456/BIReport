package com.ailk.bi.base.listener;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import waf.controller.web.util.WebKeys;

import javax.servlet.FilterConfig;

import com.ailk.bi.common.app.WebChecker;
import com.ailk.client.filter.SSOClientFilter;
import java.util.ArrayList;

/**
 *剔除一些不需要的过滤的url
 *对已登录的用户不需要过滤
 * @author tanglian
 * 
 */
public class UserLoginFilter extends SSOClientFilter {
	private List<String> exactMatchExcludedURLs = new ArrayList<String>();
	private List<String> approximateMatchExcludedURLs = new ArrayList<String>();	
	private List<String> exactLoginUrl = new ArrayList<String>();
	
	/*
	 * 判断是否可以过滤？
	 */
	public boolean needDoFilter(String uri) throws IOException,ServletException {
		boolean is2BeFiltered = true;
		if (!this.exactMatchExcludedURLs.isEmpty())
		{
			for (String exactMatchExcludedURL : this.exactMatchExcludedURLs) {
				if (uri.equals(exactMatchExcludedURL)) {
					is2BeFiltered = false;
					break;
				}
			}
		}
		if (!this.approximateMatchExcludedURLs.isEmpty() && is2BeFiltered)
		{
			for (String approximateMatchExcludedURL : this.approximateMatchExcludedURLs) {
				if (uri.indexOf(approximateMatchExcludedURL) != -1) {
					is2BeFiltered = false;
					break;
				}
			}
		}
		return is2BeFiltered;
	}
	
	/**
	 * 判断是否不用登录
	 */	
	public boolean needDoLogin(String uri) throws IOException,ServletException {
		boolean is2BeFiltered = true;
		if (!this.exactLoginUrl.isEmpty())
		{
			for (String exactLoginString : this.exactLoginUrl) {
				if (uri.equals(exactLoginString)) {
					is2BeFiltered = false;
					break;
				}
			}
		}
		return is2BeFiltered;
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String spUri = request.getRequestURI();
		String spConPath = request.getContextPath();
		//去除工程名称
		if (!StringUtils.isBlank(spConPath)) {
			spUri = spUri.replaceFirst(spConPath, "");
		}
		//保留session
		response.setHeader("P3P", "CP=CAO PSA OUR");
		// 判断是否需要过滤
		
	   if (isLogin(request)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		} else {
			if(!needDoFilter(spUri)){
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}else{
				if(!needDoLogin(spUri)){
					request.getSession().setAttribute(WebKeys.SYS_LOGINFLAG, "true");
					request.getSession().setAttribute(WebChecker.LOGIN_FLAG, "1");
					filterChain.doFilter(servletRequest, servletResponse);
					return;
				}
				super.doFilter(servletRequest, servletResponse, filterChain);				
			}
		}
		
	}

	/**
	 * 验证用户是否登录
	 */
	private static boolean isLogin(ServletRequest request) {
		boolean result = false;
		if (request != null)
			result = isLogin(((HttpServletRequest) request).getSession());
		return result;
	}

	private static boolean isLogin(HttpSession ssn) {
		boolean result = false;
		if (ssn != null) {
			String login_flag = (String) ssn.getAttribute(WebChecker.LOGIN_FLAG);
			String login_flag2 = (String) ssn.getAttribute(WebKeys.SYS_LOGINFLAG);
			if (login_flag != null && login_flag.equals("1")) {
				if (login_flag2 != null && login_flag2.equals("true")) {
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//从web.xml文件中获取需要剔除的url
		String exactMatchedURLString = filterConfig.getInitParameter("exactMatchExcludedURLs");
		String approximateMatchedURLString = filterConfig.getInitParameter("approximateMatchExcludedURLs");
		String exactLoginString = filterConfig.getInitParameter("exactLoginUrl");
		if (null != exactMatchedURLString) {
			String[] tmps = exactMatchedURLString.split(",");
			for (String tmp : tmps) {
				tmp = tmp.trim();
				if (tmp.length() > 0) {
					this.exactMatchExcludedURLs.add(tmp);
				}
			}
		}
		if (null != approximateMatchedURLString) {
			String[] tmps = approximateMatchedURLString.split(",");
			for (String tmp : tmps) {
				tmp = tmp.trim();
				if (tmp.length() > 0) {
					this.approximateMatchExcludedURLs.add(tmp);
				}
			}
		}
		if (null != exactLoginString) {
			String[] tmps = exactLoginString.split(",");
			for (String tmp : tmps) {
				tmp = tmp.trim();
				if (tmp.length() > 0) {
					this.exactLoginUrl.add(tmp);
				}
			}
		}
		super.init(filterConfig);
	}
}
