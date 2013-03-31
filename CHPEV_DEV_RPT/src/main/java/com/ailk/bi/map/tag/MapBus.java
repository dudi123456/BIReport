package com.ailk.bi.map.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.map.servie.MapService;

public class MapBus extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	private MapService service = null;
	private String url;
	private String width = "600";
	private String height = "500";
	private String mapSrc;
	private String mapid;

	public MapBus(String mapid) {
		this.mapid = mapid;
		service = new MapService(this.mapid);
	}

	public String initMapInfo(String time, HttpServletRequest request) {
		String contextPath = request.getContextPath();
		width = service.mv.getMap_Width();
		height = service.mv.getMap_Height();
		url = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/getxml.rptdo" + "?rand="+Math.random();
		mapSrc = service.mv.getMv_Name();
		StringBuffer results = new StringBuffer();
		results.append(
				"<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0\" ")
				.append("width=\"")
				.append(width)
				.append("\" height=\"")
				.append(height)
				.append("\" id=\"my_mv\" align=\"middle\">")
				.append("<embed src=\"")
				.append(contextPath + "/" + mapSrc)
				.append("\" width=\"")
				.append(width)
				.append("\" height=\"")
				.append(height)
				.append("\"name=\"my_mv\" align=\"middle\" allowscriptaccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\" http://www.macromedia.com/go/getflashplayer\" />")
				.append("<param name=\"allowScriptAccess\" value=\"sameDomain\" />")
				.append("<param name=\"movie\" value=\"").append(contextPath + "/" + mapSrc)
				.append("\"/>").append("<param name=\"wmode\" value=\"Transparent\"/>")
				.append("<param name=\"quality\" value=\"high\" />")
				.append("<param name=\"FlashVars\" value=\"xmlurl=").append(url.toString())
				.append("\" /></object>");
		return results.toString();
	}

}
