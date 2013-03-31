package com.ailk.bi.common.sysconfig;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

public class SysConfigListener extends HttpServlet implements
		ServletContextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6772026145111143499L;

	// Notification that the web application is ready to process requests
	public void contextInitialized(ServletContextEvent sce) {
		// 初始化配置参数

		GetSystemConfig.initBIBMConfig(sce);

	}

	// Notification that the servlet context is about to be shut down
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
