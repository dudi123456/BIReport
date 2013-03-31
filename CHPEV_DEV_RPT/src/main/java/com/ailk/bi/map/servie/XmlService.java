package com.ailk.bi.map.servie;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.sysconfig.GetSystemConfig;
import com.ailk.bi.map.entity.MapTemplate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class XmlService {

	public String getXml( String mapid,String date)
	{
		MapService s = new MapService(mapid);
		MapTemplate mapTemplate = s.getMapXML("",date,"","");
		Map<String,Object>map = new HashMap<String, Object>();
		map.put("mapInfo", mapTemplate);
		Configuration config=new Configuration();
		//config.setServletContextForTemplateLoading(getServletContext,"WEB-INF/templates");
		String fileurl = CommTool.getWebInfPath()+ GetSystemConfig.getBIBMConfig().getExtParam("free_marker");
		try {
			config.setDirectoryForTemplateLoading(new File(fileurl));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		StringWriter out = null;
		config.setObjectWrapper(new DefaultObjectWrapper());
		try {
			Template template = config.getTemplate("mapInfo.ftl", "UTF-8");
			out = new StringWriter();
			template.process(map, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}
}
