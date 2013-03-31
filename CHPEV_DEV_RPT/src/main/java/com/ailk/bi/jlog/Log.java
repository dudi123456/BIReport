package com.ailk.bi.jlog;

import org.apache.log4j.*;

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
public class Log {
	public static boolean setPropertyFlag = true;

	private static void setProperty() {
		PropertyConfigurator
				.configure("E:/JAVA_DEV/LSWebBase/MVCWeb/WEB-INF/log4j.properties");
		setPropertyFlag = true;
	}

	/**
	 * 打印信息级日志
	 * 
	 * @param message
	 */
	public static void info(Object message) {
		info("Undefined", message.toString());
	}

	/**
	 * 打印调试级日志
	 * 
	 * @param message
	 */
	public static void debug(Object message) {
		debug("Undefined", message.toString());
	}

	/**
	 * 打印警告级日志
	 * 
	 * @param message
	 */
	public static void warn(Object message) {
		warn("Undefined", message.toString());
	}

	/**
	 * 打印错误级日志
	 * 
	 * @param message
	 */
	public static void error(Object message) {
		error("Undefined", message.toString());
	}

	/**
	 * 打印致命级错误日志
	 * 
	 * @param message
	 */
	public static void fatal(Object message) {
		fatal("Undefined", message.toString());
	}

	/**
	 * 打印信息类日志
	 * 
	 * @param className
	 *            指定哪个类打印的信息一般为， xx.class.getName()
	 * @param message
	 */
	public static void info(String className, Object message) {
		if (!setPropertyFlag)
			setProperty();
		Logger logger = Logger.getLogger(className);
		logger.info(message.toString());
	}

	/**
	 * 打印调试类日志
	 * 
	 * @param className
	 *            指定哪个类打印的信息一般为， xx.class.getName()
	 * @param message
	 */
	public static void debug(String className, Object message) {
		if (!setPropertyFlag)
			setProperty();
		Logger logger = Logger.getLogger(className);
		logger.debug(message.toString());
	}

	/**
	 * 打印警告类日志
	 * 
	 * @param className
	 *            指定哪个类打印的信息一般为， xx.class.getName()
	 * @param message
	 */
	public static void warn(String className, Object message) {
		if (!setPropertyFlag)
			setProperty();
		Logger logger = Logger.getLogger(className);
		logger.warn(message.toString());
	}

	/**
	 * 打印错误类日志
	 * 
	 * @param className
	 *            指定哪个类打印的信息一般为， xx.class.getName()
	 * @param message
	 */
	public static void error(String className, Object message) {
		if (!setPropertyFlag)
			setProperty();
		Logger logger = Logger.getLogger(className);
		logger.error(message.toString());
	}

	/**
	 * 打印致命类日志
	 * 
	 * @param className
	 *            指定哪个类打印的信息一般为， xx.class.getName()
	 * @param message
	 */
	public static void fatal(String className, Object message) {
		if (!setPropertyFlag)
			setProperty();
		Logger logger = Logger.getLogger(className);
		logger.fatal(message.toString());
	}
}