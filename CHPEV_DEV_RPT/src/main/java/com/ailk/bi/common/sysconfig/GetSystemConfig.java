package com.ailk.bi.common.sysconfig;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.StringB;

@SuppressWarnings({ "rawtypes" })
public class GetSystemConfig implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7603539556187944403L;

	// 存放系统的配置信息
	private static BIBMConfig bibmConfig = new BIBMConfig();

	// 是否已经解析了配置信息
	private static boolean parseFlag = false;

	// 存放jsp页面映射信息

	private static HashMap mapJsp = new HashMap();

	// 存放错误配置信息

	private static HashMap mapErrorInfo = new HashMap();

	// 存放类名和Tag的对应关系

	private static HashMap classTagInfo = new HashMap();

	// 存放Tag和类名的对应关系

	private static HashMap tagClassInfo = new HashMap();

	// 文件名称
	private static final String CONFIG_FILE_NAME = "ailk_config.xml";

	/**
	 * 根据配置文件中的jspID返回对应的url串
	 */
	public static String getJspURL(String strJspID) {
		if (!parseFlag)
			return null;
		else
			return (String) mapJsp.get(strJspID);
	}

	/**
	 * 根据配置文件中的错误号获得错误信息串
	 */
	public static String getErrMsg(String strErrID) {
		if (!parseFlag)
			return null;
		else
			return (String) mapErrorInfo.get(strErrID);
	}

	/**
	 * 根据配置文件中的class获得tag号
	 */
	public static int getTagFromClass(String cName) {

		if (!parseFlag)
			return -1;
		else {
			// System.out.println("classTagInfo="+classTagInfo);
			String tag = (String) classTagInfo.get(cName);
			return StringB.hex2Int(tag, -1);
		}
	}

	/**
	 * 根据配置文件中的class获得tag号
	 */
	public static String getClassFromTag(int iTag) {
		if (!parseFlag)
			return null;

		String cName = (String) tagClassInfo.get(iTag + "");
		return cName;
	}

	/**
	 * 取得配置类
	 *
	 * @return
	 */
	public static BIBMConfig getBIBMConfig() {
		return bibmConfig;
	}

	/**
	 * 初始化BIBMConfig，读取配置相关信息，进行初始设置
	 */
	public static void initBIBMConfig() {
		if (parseFlag) // 已经解析过了
			return;

		String configFile = "";

		// 在启动参数中读取配置文件所在路径和名称

		configFile = CommTool.getWebInfPath()
				+ "/config/" + CONFIG_FILE_NAME;

		if (configFile == null || "".equals(configFile)) {
			System.err.println("系统启动错误，未找到配置文件[" + CONFIG_FILE_NAME + "]");
			System.err.println("系统强制停止");
			System.exit(1);
		}
		// 解析系统配置文件

		// 读取系统配置信息
		BIBMConfig config = new BIBMConfig();
		if ((config.doConfig(configFile)) == null) {
			System.err.println("系统解析配置文件[" + configFile + "]错误");
			System.err.println("系统强制停止");
			System.exit(1);
		}

		config.printInfo();
		// 读取日志和调试信息
		bibmConfig = config;

		parseFlag = true;

		return;
	}

	/**
	 * 初始化BIBMConfig，读取配置相关信息，进行初始设置
	 */
	public static void initBIBMConfig(ServletContextEvent event) {
		if (parseFlag) // 已经解析过了
			return;

		String configFile = "";

		// 在启动参数中读取配置文件所在路径和名称

		// configFile = System.getProperty(ConfigFile_KEY);

		String classPath = event.getServletContext().getRealPath("/WEB-INF");
		System.out.println("classPath:" + classPath);
		configFile = classPath + "/config/" + CONFIG_FILE_NAME;

		System.out.println("configFile:" + configFile);
		if (configFile == null || "".equals(configFile)) {
			System.err.println("系统启动错误，未找到配置文件[" + CONFIG_FILE_NAME + "]");
			System.err.println("系统强制停止");
			System.exit(1);
		}
		// 解析系统配置文件

		// 读取系统配置信息
		BIBMConfig config = new BIBMConfig();
		if ((config.doConfig(configFile)) == null) {
			System.err.println("系统解析配置文件[" + configFile + "]错误");
			System.err.println("系统强制停止");
			System.exit(1);
		}

		config.printInfo();
		// 读取日志和调试信息
		bibmConfig = config;

		parseFlag = true;

		return;
	}

	/**
	 * 重新读取配置的相关信息 读取的文件包括：config.xml, errsConfig.xml, pagesConfig.xml,
	 * class2tag.xml
	 */
	public static void reload() {
		parseFlag = false;
		initBIBMConfig();
	}

	public static void main(String[] args) {
		System.err.println("run");
		initBIBMConfig();
	}
}