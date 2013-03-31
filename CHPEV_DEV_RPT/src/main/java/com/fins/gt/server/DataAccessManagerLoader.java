package com.fins.gt.server;

import javax.servlet.ServletContext;

import com.fins.gt.dataaccess.DataAccessManager;
import com.fins.gt.dataaccess.IDataBaseManager;
import com.fins.gt.util.LogHandler;
import com.fins.gt.util.StringUtils;

public class DataAccessManagerLoader {

	public static void loadDataAccessManager(ServletContext servletContext) {

		String manager = getParameter(servletContext, "db.manager",
				"com.fins.gt.dataaccess.H2DBManager");
		if (StringUtils.isNotEmpty(manager)) {
			try {
				DataAccessManager.setCurrentManager((IDataBaseManager) Class
						.forName(manager).newInstance());
			} catch (Exception e) {
				LogHandler.warn(" invaild db.manager [" + manager + "]");
			}
		}
		String user = getParameter(servletContext, "db.user", "sa");
		String password = getParameter(servletContext, "db.password", "");
		String url = getParameter(servletContext, "db.url", "");

		String maxconn = getParameter(servletContext, "db.maxconn", "none");

		if (!DataAccessManager.initDataSource(url, user, password)) {
			LogHandler.error(" invaild [" + url + "," + user + "," + password
					+ "]");
			return;
		}
		try {
			int maxconnI = Integer.parseInt(maxconn);
			DataAccessManager.initConnectionPool(maxconnI);
		} catch (NumberFormatException e) {
			LogHandler.warn(" invaild db.maxconn [" + maxconn + "]");
			DataAccessManager.initConnectionPool();
		}

		if (!DataAccessManager.testDataSource()) {
			LogHandler.error(" invaild [" + url + "," + user + "," + password
					+ "]");
			return;
		}
		LogHandler.info(" ======= server started ======= ");
	}

	private static String getParameter(ServletContext servletContext,
			String key, String defaultValue) {
		String value = servletContext.getInitParameter(key);
		return value == null ? defaultValue : value;
	}

	public static void destroyDataAccessManager(ServletContext servletContext) {
		try {
			DataAccessManager.destroy();
		} catch (Exception e) {
			LogHandler.error(DataAccessManagerLoader.class, e);
		}
		LogHandler.info(" ======= server stoped ======= ");
	}
}
