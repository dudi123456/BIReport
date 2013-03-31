package com.ailk.bi.leader.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.map.servie.MapService;
import com.ailk.bi.map.tag.MapBus;

/**
 * 领导首页处理
 *
 * @author Renhui
 *
 */
public class LeaderMapHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 获取用户session
		HttpSession session = request.getSession(true);

		String msu_id = CommTool.getParameterGB(request, "msu_id");
		request.setAttribute("msu_id", msu_id);
		session.setAttribute("msu_id", msu_id);
		// 提取参数条件
		LeaderQryStruct qryStruct = (LeaderQryStruct) session
				.getAttribute(WebKeys.ATTR_ailk_QRY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new LeaderQryStruct();
		}
		// 方慧 全国地图
		String mapid = "";
		Object  id = session.getAttribute("mapid");
		if (id == null) {
			// mapid = "xinjiang";//目前测试新疆地图
			mapid = "quanguo";// 目前测试全国地图
		}else
		{
			mapid=String.valueOf(id);
		}
		session.setAttribute("mv", null);
		MapService s = new MapService(mapid);
		String formatTime = s.getTime(qryStruct.gather_day);
		MapBus m = new MapBus(mapid);
		String result = m.initMapInfo(formatTime, request);
		session.setAttribute("date", formatTime);
		session.setAttribute("mapid", mapid);
		session.setAttribute("mv", result);
		this.setNextScreen(request, "LeaderKpiViewMap.screen");
	}
}
