package waf.util;

import java.io.Serializable;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;

/**
 * 清楚session中不用的信息，此功能定义的两个类为ibas项目中定义session属性的类
 * 要清除的类必须定义成某些类的public属性，并将这些类定义到启动参数文件的clear_classes tag中,多个类要以";"号进行间隔，如:
 * lswu.busi.common.WebKeys;lswu.acct.common.WebKeys
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
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
public class CleanSessionTool implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7433930090633394250L;

	// 为了清楚session中过期信息而设置的url参数
	public static final String WEB_PARA_CS = "CSSC";

	// 为了取得菜单号码的url中的参数,
	public static final String WEB_PARA_MENUCODE = "dolog";

	// 作为session的attrName保存在session中
	public static final String ATTR_MENUCODE = "Sys.MenuCodeAttr";

	public static final String CLEAR_CLASS_TAG = "clear_classes";

	/**
	 * 设置为可以清理session的url
	 * 
	 * @param url
	 *            url必须是*.screen或者*.do格式的
	 * @return
	 */
	public static synchronized String setClearURL(String url) {
		if (url == null)
			return null;
		String conn = "?";
		if (url.indexOf("?") > -1)
			conn = "&";
		return url + conn + WEB_PARA_CS + "=1";
	}

	public static synchronized void clear(HttpSession session) {
		if (session == null) {
			return;
		}
		String clearObjs = com.ailk.bi.common.sysconfig.GetSystemConfig
				.getBIBMConfig().getExtParam(CLEAR_CLASS_TAG);
		if (clearObjs == null) {
			System.err.println("启动文件中没有定义clear_classes tag，不能进行自动清除!");
			return;
		}
		String[] objs = clearObjs.split(";");
		for (int i = 0; i < objs.length; i++) {
			try {
				Object acctWebKeys = Class.forName(objs[i].trim())
						.newInstance();
				clearObj(session, acctWebKeys);
			} catch (Exception e) {
				// e.printStackTrace();
				// clear acct web keys error
			}
		}
	}

	private static void clearObj(HttpSession session, Object obj) {
		Field[] fields = obj.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			String attr = "";
			try {
				attr = (String) fields[i].get(obj);
			} catch (Exception ex) {
				continue;
			}
			session.removeAttribute(attr);
		}
	}
}
