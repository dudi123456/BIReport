package com.ailk.bi.jlog.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description: 在Apach Log4J的基础上进行了一点封装，用于日志的输出
 * 如果能在你的程序中正确使用，首先要将log4j.jar文件放在运行的classpath中(二Container问题)
 * 其次，还要在你的webapp的web-info下设置log4j的配置文件；
 * 再次，要在web.xml中设置初始lisner程序com.asiabi.jlog.utils.Log4JInitServer
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
public class Log4JInitServlet extends HttpServlet implements
		ServletContextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 785605448631153232L;

	// Notification that the web application is ready to process requests
	public void contextInitialized(ServletContextEvent sce) {
		// String prefix = sce.getServletContext().getRealPath("/");
		ServletContext context = sce.getServletContext();
		String file = context.getInitParameter("log4j");
		// if the log4j-init-file is not set, then no point in trying
		System.out.println("................log4j start");
		System.out
				.println("................log4j properties file=" + file + "");

		java.net.URL log4jURL = null;

		if (file != null) {
			try {
				log4jURL = context.getResource(file);
				PropertyConfigurator.configure(log4jURL);
			} catch (Exception ex) {
				System.err
						.println("Cann't FIND log2j mappping definiation file["
								+ file + "]: " + ex);
			}
		}
	}

	// Notification that the servlet context is about to be shut down
	public void contextDestroyed(ServletContextEvent sce) {
		// throw new java.lang.UnsupportedOperationException("Method
		// contextDestroyed() not yet implemented.");
	}

}