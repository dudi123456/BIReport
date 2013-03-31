package com.ailk.bi.common.app;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: EJB Home定位器
 * </p>
 * <ref> 举例 获取EJBHome接口的调用方式： XXXEJBHome
 * home=(XXXEJBHome)ServiceLocator.getInstance
 * ().getHome(XXXEJB_JNDI,XXXEJBHome.class); </ref>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ServiceLocator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6797267397863398611L;

	private static ServiceLocator me;

	Context context = null;

	private Map cache; // used to hold references to EJBHomes/JMS Resources for

	// re-use

	// 系统服务器信息，如果改动只需，设置CURRENTSERVER
	private static String WEBLOGICSERVER = "weblogic";

	private static String CURRENTSERVER = "weblogic";

	// 联接EJB服务器参数
	private static String EJBSERVERURL = "t3://localhost:7001";

	// AIX服务器

	// weblogic的guest用户如果不删除，可以使用null
	private final static String EJBUSERNAME = null;

	private final static String EJBPASSWORD = null;

	/**
	 * 初始化Context
	 * 
	 * @return
	 * @throws Exception
	 */
	private Context getInitialContext() throws Exception {
		String url = GetSystemConfig.getBIBMConfig().getAppURL();
		if (url == null || "".equals(url))
			url = EJBSERVERURL;
		String user = GetSystemConfig.getBIBMConfig().getAppUser();
		if (user == null || "".equals(user))
			user = EJBUSERNAME;
		String password = GetSystemConfig.getBIBMConfig().getAppPWD();
		if (password == null || "".equals(password))
			password = EJBPASSWORD;
		Properties properties = null;
		try {
			// 当前服务器为Weblogic时
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

	private ServiceLocator() {
		try {
			context = getInitialContext();
			cache = Collections.synchronizedMap(new HashMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化ServiceLocator
	 * 
	 * @return
	 */
	public static ServiceLocator getInstance() {
		if (me == null) {
			me = new ServiceLocator();
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
			if (false && cache.containsKey(name)) {
				home = (EJBHome) cache.get(name);
			} else {
				Object objref = context.lookup(name);
				home = (EJBHome) PortableRemoteObject.narrow(objref, clazz);
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
	 */
	public String getWebString(Context context, String envName) {
		String envEntry = null;
		try {
			envEntry = (String) context.lookup(envName);
		} catch (NamingException ne) {
			// throw new AppException(ne);
		} catch (Exception e) {
			// throw new AppException(e);
		}
		return envEntry;
	}
}