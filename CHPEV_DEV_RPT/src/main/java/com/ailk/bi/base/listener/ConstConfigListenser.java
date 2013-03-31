package com.ailk.bi.base.listener;

import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

//import com.ailk.bi.base.struct.InfoResStruct;
//import com.ailk.bi.base.table.InfoPageItemTable;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SecurityTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConstConfigListenser extends HttpServlet implements
		ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6772026145111143499L;

	// 定时器
	// private java.util.Timer timer = null;

	// Notification that the web application is ready to process requests
	public void contextInitialized(ServletContextEvent sce) {

		ServletContext context = sce.getServletContext();

		//
		// 统一资源表信息资源ID和描述初始化MAP*
		SecurityTool.setAllResConstsKeyValue(context);
		// 系统所有资源信息*
		// InfoResStruct[] infoRes = CommTool.getInfoResStruct();
		// context.setAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT, infoRes);

		// 区域(需要先创建区域视图,在进行提取)
		HashMap rgmap = SecurityTool.getConstMap("REGION");
		context.setAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME, rgmap);
		// 部门
		// HashMap dpmap = SecurityTool.getConstMap("DEPT");
		// context.setAttribute(WebConstKeys.ATTR_C_DEPT_ID_VS_NAME, dpmap);
		// 业务类型
		// HashMap svcmap = getConstMap("SVC_KND");
		// context.setAttribute(WebConstKeys.ATTR_C_SVC_ID_VS_NAME, svcmap);
	}

	// Notification that the servlet context is about to be shut down
	public void contextDestroyed(ServletContextEvent sce) {
		// 销毁定时器
		// timer.cancel();
	}

	/**
	 * 查询维表数据
	 * 
	 * @param type
	 * @return
	 * @author jcm
	 */
	public HashMap getConstMap(String type) {
		HashMap map = new HashMap();
		String sql = "";
		//
		if ("BRAND".equals(type.toUpperCase())) {
			sql = "SELECT BRAND_KND,BRANDKND_DESC FROM D_BRAND_KND";
		}
		//
		if ("PRODUCT".equals(type.toUpperCase())) {
			sql = "select distinct product as id ,comments as name from  D_CODE_PRODUCT ";
			sql += " union ";
			sql += "select distinct sub_brand_knd as id,subbrandknd_desc as name from D_SUB_BRAND_KND ";
			sql += " union ";
			sql += "select distinct brand_code as id ,brandcode_desc  as name from D_BRAND_CODE ";

		}
		//
		if ("CHANNEL".equals(type.toUpperCase())) {
			sql = "select  channel_id as id , channel_desc as name from  d_channel ";
			sql += " union ";
			sql += " select sub_channel_type as id ,subchanneltype_desc as name from  d_sub_channel_type";
			sql += " union ";
			sql += "select channel_type as id ,channeltype_desc  as name from d_channel_type";
		}
		if ("SALE_TYPE".equals(type.toUpperCase())) {
			sql = "SELECT SALE_TYPE,SALETYPE_DESC FROM D_SALE_TYPE";
		}
		if ("VIP".equals(type.toUpperCase())) {
			sql = "SELECT VIP_LEVEL,VIPLEVEL_DESC FROM D_VIP_LEVEL";
		}
		if ("DURA".equals(type.toUpperCase())) {
			sql = "SELECT ONL_DURA,ONLDURA_DESC FROM D_ONL_DURA";
		}
		if ("MAC_TYPE".equals(type.toUpperCase())) {
			sql = "SELECT MAC_TYPE,MACTYPE_DESC FROM D_MAC_TYPE";
		}
		if ("SVC_KND".equals(type.toUpperCase())) {
			sql = "select distinct svc_knd as id, svcknd_desc as name from d_svc_knd  where svc_knd_lvl in('10','20')";
			sql += " union ";
			sql += "select  distinct  vprepay_knd as id, vprepayknd_desc as name  from d_svc_knd  where svc_knd_lvl in('10','20')";
			sql += " union ";
			sql += "select  distinct  svc_knd_lvl as id, svckndlvl_desc as name from d_svc_knd  where svc_knd_lvl in('10','20')";
		}

		System.out.println("sql============context===" + sql);
		//
		String[][] arr = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				map.put(arr[i][0], arr[i][1]);
			}

		}
		//
		return map;
	}
}
