package com.ailk.bi.common.app;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: EJB Home定位器,可以指定EJB服务的IP, url和password
 * </p>
 * <ref> 举例 获取EJBHome接口的调用方式： XXXEJBHome
 * home=(XXXEJBHome)ServiceLocator.getInstance(url, user,
 * password).getHome(XXXEJB_JNDI,XXXEJBHome.class); </ref>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author wiseking
 * @version 1.0 2003年03月18日改进 增加了若干方法，并提供Factory方式访问EJB 2003年05月17日
 *          这样调用可能会有问题，因为在EJB层和WebLogic层是两个不同的实例
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ServiceLocatorR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1648330368205423269L;

	private static ServiceLocatorR me;

	Context context = null;

	private Map cache; // used to hold references to EJBHomes/JMS Resources for

	// re-use

	// 系统服务器信息，如果改动只需，设置CURRENTSERVER
	private static String WEBLOGICSERVER = "weblogic6.1";

	private static String CURRENTSERVER = "weblogic6.1";

	/**
	 * 初始化Context
	 * 
	 * @return
	 * @throws Exception
	 */
	private Context getInitialContext(String url, String user, String password)
			throws Exception {
		Properties properties = null;
		try {
			// 当前服务器为Weblogic6.1时
			if (CURRENTSERVER.equals(WEBLOGICSERVER)) {
				properties = new Properties();
				properties.put(Context.INITIAL_CONTEXT_FACTORY,
						"weblogic.jndi.WLInitialContextFactory");
				properties.put(Context.PROVIDER_URL, url);
				if (user != null) {
					properties.put(Context.SECURITY_PRINCIPAL, user);
					properties.put(Context.SECURITY_CREDENTIALS,
							password == null ? "" : password);
				}
				return new InitialContext(properties);
				// 当前服务器为BAS时
			} else {
				return new InitialContext();
			}
		} catch (Exception e) {
			e.printStackTrace();
			com.ailk.bi.common.app.Debug
					.println("Please make sure that the server is running.");
			throw e;
		}
	}

	private ServiceLocatorR(String url, String user, String password) {
		try {
			context = getInitialContext(url, user, password);
			cache = Collections.synchronizedMap(new HashMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得其它EJB服务器上的EJB定位器类
	 * 
	 * @param url
	 *            通常为t3://localhost:7001, 这里可以指定不同的IP和端口
	 * @param user
	 *            一般为null或者根据weblogic的权限设置指定
	 * @param password
	 *            一般为null或者根据weblogic的权限设置指定
	 * @return
	 */
	public static ServiceLocatorR getInstance(String url, String user,
			String password) {
		if (me == null) {
			me = new ServiceLocatorR(url, user, password);
		}
		return me;
	}

	/**
	 * 根据JNDI名字，取得EJB Home
	 * 
	 * @param name
	 *            JDNI名字
	 * @param clazz
	 *            Home class名字
	 * @return 找到的EJBHome
	 */
	public EJBHome getHome(String name, Class clazz) {
		try {
			EJBHome home = null;
			if (false && cache != null && cache.containsKey(name)) {
				home = (EJBHome) cache.get(name);
			} else {
				Object objref = context.lookup(name);
				home = (EJBHome) PortableRemoteObject.narrow(objref, clazz);
				if (cache != null)
					cache.put(name, home);
			}
			return home;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the String value corresponding to the env entry name. ATTION:
	 *         this env was can define in web tier
	 * 
	 *         private String getWebString(Context context, String envName)
	 *         throws ServiceLocatorException { String envEntry = null; try {
	 *         envEntry = (String)context.lookup(envName); } catch
	 *         (NamingException ne) { throw new ServiceLocatorException(ne); }
	 *         catch (Exception e) { throw new ServiceLocatorException(e); }
	 *         return envEntry ; }
	 */
}