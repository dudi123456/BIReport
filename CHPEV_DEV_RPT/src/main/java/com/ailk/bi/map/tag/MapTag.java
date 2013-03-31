package com.ailk.bi.map.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.ailk.bi.map.servie.MapService;
//import com.ailk.bi.map.servie.XmlService;
public  class  MapTag extends BodyTagSupport {

	private MapService service = null;
	private static final long serialVersionUID = 1L;
	private String url;
	private String width ="600";
	private String height = "500";
	private String mapSrc;
	private String mapid;
	private String cityId = null;
	private String path; // 系统路径
	//private String xml = null;

	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getGatherDay() {
		return gatherDay;
	}
	public void setGatherDay(String gatherDay) {
		this.gatherDay = gatherDay;
	}
	public String getMsuId() {
		return msuId;
	}
	public void setMsuId(String msuId) {
		this.msuId = msuId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String gatherDay = null;
	private String msuId = null;

	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		String outStr =  initMapInfo();
		try {
			out.println(outStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
	public String initMapInfo()
	{
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath=request.getContextPath();
		HttpSession session = pageContext.getSession();
		service = new MapService(this.msuId);
		String formatTime = service.getTime(gatherDay);
		//XmlService xx = new XmlService();
		//this.xml = xx.getXml("xinjiang", "20111206");
		session.setAttribute("date",formatTime);
		session.setAttribute("mapid", msuId);
		width = service.mv.getMap_Width();
		height = service.mv.getMap_Height();
		url ="";//修改过
		mapSrc = service.mv.getMv_Name();
		StringBuffer results = new StringBuffer();
		results.append(
		"<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0\" ")
		.append("width=\"").append(width)
		.append("\" height=\"").append(height)
		.append("\" id=\"my_mv\" align=\"middle\">")
		.append("<embed src=\"")
		.append(mapSrc)
		.append("\" width=\"")
		.append(width)
		.append("\" height=\"")
		.append(height)
		.append("\"name=\"my_mv\" align=\"middle\" allowscriptaccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\" http://www.macromedia.com/go/getflashplayer\" />")
		.append("<param name=\"allowScriptAccess\" value=\"sameDomain\" />")
		.append("<param name=\"movie\" value=\"").append(contextPath+"/"+mapSrc).append("\"/>")
		.append("<param name=\"wmode\" value=\"Transparent\"/>")
		.append("<param name=\"quality\" value=\"high\" />")
		.append("<param name=\"FlashVars\" value=\"xmlurl=")
		.append(this.url.toString()).append("\"/></object>");
		return results.toString();
	}
	public MapService getService() {
		return service;
	}
	public void setService(MapService service) {
		this.service = service;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getMapSrc() {
		return mapSrc;
	}
	public void setMapSrc(String mapSrc) {
		this.mapSrc = mapSrc;
	}
	public String getMapid() {
		return mapid;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
