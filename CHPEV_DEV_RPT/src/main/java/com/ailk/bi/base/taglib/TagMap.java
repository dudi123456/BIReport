package com.ailk.bi.base.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.ailk.bi.base.util.MapUtil;


public class TagMap extends BodyTagSupport {
	private static final long serialVersionUID = 4045019501137210299L;
	private String cityId = null;
	private String path; // 系统路径

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

	public int doStartTag() throws JspException {
		// 提取界面传递参数
		try {
			@SuppressWarnings("unused")
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		} catch (Exception e) {
			System.out.println("初始化图形标签(map)出现错误，有可能参数传递失败！");
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		String outStr = "";

		System.out.println("msuId:"+msuId);
		System.out.println("cityId:"+cityId);
		System.out.println("gatherDay:"+gatherDay);

		try {
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpSession session = pageContext.getSession();
			//request.setCharacterEncoding("UTF-8");

			//得到xml
			@SuppressWarnings("unused")
			String mapStr = MapUtil.genMapData(request,msuId,cityId,gatherDay);
			String mapXML = MapUtil.getMapDataXML(msuId,cityId,gatherDay);
			//测试用值
			//String mapStr = MapUtil.genMapData(request,"F1001","","20110412");

			if (session != null) {
				//request.setAttribute("mapChart", mapStr);
				//session.setAttribute("mapChart",mapStr);
				// 系统路径
				if (path == null || "".equals(path.trim())) {
					path = request.getContextPath();
				}
				StringBuffer chart = new StringBuffer();
				chart.append("<script defer=\"true\" type=\"text/javascript\">\n");
				chart.append("displayMap();\n");
				chart.append("function displayMap(){\n");
				chart.append("var mapXml = \""+mapXML.replaceAll("\n", "")+"\";\n");
				chart.append("var chart = new FusionMaps(\"" + path + "/js/FusionMaps/firstpage/map_china.swf\", \"mapChina\", \"600\", \"480\",\"0\",\"1\");\n");
				chart.append("chart.setDataXML(mapXml);\n");
				chart.append("chart.addParam(\"wmode\",\"Opaque\");\n");
				//chart.append("chart.setDataURL(\""+mapStr+"\");\n");
				chart.append("chart.render(\"mapChart\");\n");

				//chart.append("alert(mapXml);\n");
				chart.append("}\n");
				chart.append("</script>\n");
				outStr = chart.toString();
				out.println(outStr);

				//System.out.println(mapStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

}
