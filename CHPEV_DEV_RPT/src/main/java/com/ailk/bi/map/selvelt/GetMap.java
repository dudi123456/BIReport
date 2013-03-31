package com.ailk.bi.map.selvelt;

//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

//import com.ailk.bi.map.servie.MapService;
//import com.ailk.bi.map.tag.MapTag;

public class GetMap extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		try {
			HttpSession session = request.getSession();
			String mapid = request.getParameter("mapid");
			if (null == mapid) {
				mapid = "xinjiang";
			}
			session.setAttribute("mapid", mapid);
			request.setAttribute("mapid", mapid);
			request.getRequestDispatcher("leaderViewAction.rptdo").forward(
					request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// HttpSession session = request.getSession();
		// // 方慧 全国地图
		// String mapid = request.getParameter("mapid");
		// if (null == mapid) {
		// mapid = "xinjiang";
		// }
		// session.setAttribute("mapid", mapid);
		// MapService s = new MapService(mapid);
		// String formatTime = s.getTime(null);
		// MapBus m = new MapBus(mapid);
		// String result = m.initMapInfo(formatTime, request);
		// session.setAttribute("date", formatTime);
		// session.setAttribute("mapid", mapid);
		// session.setAttribute("mv", result);
		// try {
		// request.getRequestDispatcher("/leader/leaderview.jsp").forward(
		// request, response);
		// } catch (ServletException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
