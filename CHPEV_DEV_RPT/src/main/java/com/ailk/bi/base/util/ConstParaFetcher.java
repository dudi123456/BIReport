package com.ailk.bi.base.util;

import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.InfoResStruct;

//import com.ailk.bi.base.table.InfoPageItemTable;

/**
 * 此类为经营分析常用纬度信息提取器
 * 
 * 
 * @author jcm
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ConstParaFetcher {

	/**
	 * 提取经营分析公用资源信息（ui_pub_info_resource）
	 * 
	 * @param session
	 * @return
	 */
	public static InfoResStruct[] getPubInfoResource(HttpSession session) {
		ServletContext context = session.getServletContext();
		InfoResStruct[] infoRes = (InfoResStruct[]) context
				.getAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT);
		return infoRes;

	}

	/**
	 * 取得经营分析区域纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getRegionMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap regionmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME);
		return regionmap;

	}

	/**
	 * 取得经营分析区域名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getRegionName(HttpSession session, String key) {
		String name = "";
		HashMap map = getRegionMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析部门纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getDeptMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap deptmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_DEPT_ID_VS_NAME);
		return deptmap;

	}

	/**
	 * 取得经营分析部门名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getDeptName(HttpSession session, String key) {
		String name = "";
		HashMap map = getDeptMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析品牌纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getBrandMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap brandmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_BRAND_ID_VS_NAME);
		return brandmap;

	}

	/**
	 * 取得经营分析品牌名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getBrandName(HttpSession session, String key) {
		String name = "";
		HashMap map = getBrandMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析产品纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getProductMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap productmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_PRODUCT_ID_VS_NAME);
		return productmap;

	}

	/**
	 * 取得经营分析产品名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getProductName(HttpSession session, String key) {
		String name = "";
		HashMap map = getProductMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析渠道纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getChannelMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap channelmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CHANNEL_ID_VS_NAME);
		return channelmap;

	}

	/**
	 * 取得经营分析渠道名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getChannelName(HttpSession session, String key) {
		String name = "";
		HashMap map = getChannelMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析销售模式纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getSaleTypeMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap salemap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_SALE_ID_VS_NAME);
		return salemap;

	}

	/**
	 * 取得经营分析销售模式名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getSaleTypeName(HttpSession session, String key) {
		String name = "";
		HashMap map = getSaleTypeMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析手机机型纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getMacTypeMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap macmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_MAC_ID_VS_NAME);
		return macmap;

	}

	/**
	 * 取得经营分析手机机型名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getMacTypeName(HttpSession session, String key) {
		String name = "";
		HashMap map = getMacTypeMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析业务类型纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getSvcKndMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap svcmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_SVC_ID_VS_NAME);
		return svcmap;

	}

	/**
	 * 取得经营分析业务类型名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getSvcKndName(HttpSession session, String key) {
		String name = "";
		HashMap map = getSvcKndMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析用户等级纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getVipLevelMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap vipmap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_VIP_ID_VS_NAME);
		return vipmap;

	}

	/**
	 * 取得经营分析用户等级名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getVipLevelName(HttpSession session, String key) {
		String name = "";
		HashMap map = getVipLevelMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

	/**
	 * 取得经营分析在网时长纬度
	 * 
	 * @param session
	 * @return
	 */
	public static HashMap getOnlDuraMap(HttpSession session) {
		ServletContext context = session.getServletContext();
		HashMap duramap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_DURA_ID_VS_NAME);
		return duramap;

	}

	/**
	 * 取得经营分析用户等级名称
	 * 
	 * @param session
	 * @return
	 */
	public static String getOnlDuraName(HttpSession session, String key) {
		String name = "";
		HashMap map = getOnlDuraMap(session);
		if (map != null) {
			name = map.get(key).toString();
		}
		return name;
	}

}
