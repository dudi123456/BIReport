package com.fins.gt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fins.gt.server.DataAccessManagerLoader;
import com.fins.gt.util.LogHandler;
import com.fins.gt.util.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FrontController implements Filter {
	static final long serialVersionUID = 1L;

	public String webRootRealPath = null;
	public ServletContext servletContext = null;
	protected FilterConfig filterConfig = null;

	public static String DEFAULT_METHODNAME = "service";

	protected Map methodCache = Collections.synchronizedMap(new HashMap());

	protected Class[] types1 = { HttpServletRequest.class,
			HttpServletResponse.class };
	protected Class[] types2 = {};

	private static Map classCache = Collections.synchronizedMap(new HashMap());

	private String actionBasePath;
	private String viewBasePath;
	private String dispatchMethod;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		servletContext = filterConfig.getServletContext();
		webRootRealPath = servletContext.getRealPath("/");

		actionBasePath = filterConfig.getInitParameter("actionBasePath");
		if (actionBasePath == null) {
			actionBasePath = "action";
		}

		viewBasePath = filterConfig.getInitParameter("viewBasePath");
		if (viewBasePath == null) {
			viewBasePath = "/view";
		}
		dispatchMethod = filterConfig.getInitParameter("dispatchMethod");
		if (dispatchMethod == null) {
			dispatchMethod = "doMethod";
		}

		DataAccessManagerLoader.loadDataAccessManager(servletContext);
	}

	public BaseAction loadAction(HttpServletRequest request,
			HttpServletResponse response) {
		String servletPath = request.getServletPath();
		Class klazz = (Class) classCache.get(servletPath);
		String actionName = null;
		try {
			int si = servletPath.lastIndexOf("/");
			int di = servletPath.lastIndexOf(".");
			servletPath = servletPath.substring(si + 1, di);
			String viewPath = servletPath.replaceAll("\\.", "/");
			if (klazz == null) {
				di = servletPath.lastIndexOf(".") + 1;
				actionName = servletPath.substring(0, di)
						+ servletPath.substring(di, di + 1).toUpperCase()
						+ servletPath.substring(di + 1);
				klazz = Class.forName(actionBasePath + '.' + actionName);
				classCache.put(servletPath, klazz);
			}

			BaseAction action = (BaseAction) klazz.newInstance();
			action.init(request, response);
			action.setViewBasePath(viewBasePath);
			action.setViewPath("/" + viewPath);
			return action;

		} catch (Exception e) {
			LogHandler.warn(actionName + " Action not found");
			return null;
		}
	}

	private String getMethodName(HttpServletRequest request) {
		return request.getParameter(dispatchMethod);
	}

	protected Method getMethod(Class clazz, String name, Class[] pTypes) {
		try {
			return clazz.getMethod(name, pTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	protected Method getMethod(Class clazz, String name) {
		String key = clazz.getName() + "/" + name;
		Method method = (Method) methodCache.get(key);
		if (method == null) {
			Exception mE = null;
			try {
				method = clazz.getMethod(name, types1);
			} catch (Exception e1) {
				mE = e1;
				try {
					method = clazz.getMethod(name, types2);
				} catch (Exception e2) {
					mE = e2;
					method = null;
				}
			}

			if (method == null) {
				LogHandler.warn(this,
						new NoSuchMethodException(mE.getMessage()));
			} else {
				methodCache.put(key, method);
			}
		}
		return method;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// LogHandler.print("****************************");
		// LogHandler.print("*  getRequestURL : "+request.getRequestURL());
		// LogHandler.print("*  getRequestURI : "+request.getRequestURI());
		// LogHandler.print("*  getContextPath : "+request.getContextPath());
		// LogHandler.print("*  getServletPath : "+request.getServletPath());
		// LogHandler.print("****************************");

		BaseAction action = loadAction(request, response);
		String name = getMethodName(request);

		if (StringUtils.isEmpty(name)) {
			name = DEFAULT_METHODNAME;
		}
		if (action != null) {
			Object[] args = { request, response };
			Method method = getMethod(action.getClass(), name);
			try {
				if (method.getParameterTypes().length == 0) {
					method.invoke(action, (Object[]) null);
				} else {
					method.invoke(action, args);
				}
			} catch (Exception e) {
				LogHandler.warn(e);
				throw new ServletException(e);
			} finally {
				action.dispose();
			}
			return;
		} else {
			response.sendError(404);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><body>Action not found!</body></html>");
			out.flush();
			out.close();
		}
	}

	public String getWebRootRealPath() {
		return webRootRealPath;
	}

	public void destroy() {
		webRootRealPath = null;
		servletContext = null;
		filterConfig = null;

		methodCache = null;

		types1 = null;
		types2 = null;

		classCache = null;

		actionBasePath = null;
		viewBasePath = null;
		dispatchMethod = null;
		DataAccessManagerLoader.destroyDataAccessManager(servletContext);
	}

}
