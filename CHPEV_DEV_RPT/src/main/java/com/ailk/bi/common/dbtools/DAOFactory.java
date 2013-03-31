package com.ailk.bi.common.dbtools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.bulletin.dao.IBulletinDao;
import com.ailk.bi.bulletin.dao.impl.BulletinDaoImpl;
import com.ailk.bi.favor.dao.IFavorDao;
import com.ailk.bi.favor.dao.impl.FavorDaoImpl;
import com.ailk.bi.login.dao.IUserDao;
import com.ailk.bi.login.dao.impl.UserDaoImpl;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.userlog.dao.IUserLogDao;
import com.ailk.bi.userlog.dao.impl.UserLogDaoImpl;

@SuppressWarnings({ "unused" })
public class DAOFactory {
	private static final Log log = LogFactory.getLog(DAOFactory.class);

	private DAOFactory() {
	}

	private static IBulletinDao bulletinDao = null;
	private static IFavorDao favDao = null;
	private static IUserDao userDao = null;
	private static IUserLogDao userLogDao = null;
	private static CommonFacade commonFac = null;

	public static CommonFacade getCommonFac() {
		return commonFac;
	}

	public static IUserLogDao getUserLogDao() {
		return userLogDao;
	}

	public static IUserDao getUserDao() {
		return userDao;
	}

	public static IFavorDao getFavDao() {
		return favDao;
	}

	public static IBulletinDao getBulletinDao() {
		return bulletinDao;
	}

	static {

		// please note that localMemberDAO should be instantiated first the
		// memberDAO
		// because the memberDAO can refer to the localMemberDAO
		bulletinDao = new BulletinDaoImpl();
		favDao = new FavorDaoImpl();
		userDao = new UserDaoImpl();
		userLogDao = new UserLogDaoImpl();
		commonFac = new CommonFacade();
	}
}
