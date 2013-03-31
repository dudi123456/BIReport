package com.ailk.bi.map.selvelt;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.leader.struct.LeaderKpiInfoStruct;
import com.ailk.bi.leader.util.LeaderKpiUtil;
import com.ailk.bi.map.entity.MapTemplate;
import com.ailk.bi.map.servie.MapService;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class GetXml extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		String date = String.valueOf(session.getAttribute("date"));
		String mapid = String.valueOf(session.getAttribute("mapid"));
		MapService service = new MapService(mapid);
		Object qryStruct = session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		String conndition =CommConditionUtil.getPubWhere("map_xinjiang", request, qryStruct);
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		//ctlStruct.ctl_city_str="18,91,95,98,99,96,11,19,94,13,12";//测试权限
		LeaderKpiInfoStruct kpiStruct = null;
		String msu_id = (String)session.getAttribute("msu_id");
		if (msu_id == null || "".equals(msu_id)) {
			kpiStruct = (LeaderKpiInfoStruct) session
			.getAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT_FIRST);
			msu_id = kpiStruct.msu_id;
		} else {
			kpiStruct = LeaderKpiUtil.getKpiInfoStruct(msu_id);
		}
		String title = kpiStruct.msu_name+"("+kpiStruct.unit+")";
		MapTemplate mapTemplate = service.getMapXML(title,date,conndition,ctlStruct.ctl_city_str);
		Map<String,Object>map = new HashMap<String, Object>();
		map.put("mapInfo", mapTemplate);
		Configuration config=new Configuration();
		config.setServletContextForTemplateLoading(context,"WEB-INF/templates");
		config.setObjectWrapper(new DefaultObjectWrapper());
		try {
			Template template = config.getTemplate("mapInfo.ftl", "UTF-8");
			response.setContentType("text/xml; charset=UTF-8");
			Writer out = response.getWriter();
			template.process(map, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
