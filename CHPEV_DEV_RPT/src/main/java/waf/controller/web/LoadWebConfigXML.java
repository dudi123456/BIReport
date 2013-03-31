package waf.controller.web;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import waf.controller.web.flow.ScreenFlowData;
import waf.controller.web.util.WebKeys;
import waf.view.template.ScreenDefinitionDAO;
import waf.view.template.Screens;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LoadWebConfigXML {

	private static HashMap urlMappings;

	private static HashMap globalFlows;

	private static HashMap eventMappings;

	public static HashMap allScreens;

	private static int loadFlag = 0;

	public LoadWebConfigXML() {

	}

	private static void initScreens(ServletContext context) {
		java.net.URL screenDefinitionURL = null;
		allScreens = new HashMap();
		try {
			screenDefinitionURL = context.getResource(WebKeys.SCREEN_FILE);
		} catch (java.net.MalformedURLException ex) {
			System.err.println("Cann't FIND screen definiation file: " + ex);
		}
		if (screenDefinitionURL != null) {
			Screens screenDefinitions = ScreenDefinitionDAO
					.loadScreenDefinitions(screenDefinitionURL);
			if (screenDefinitions != null) {
				allScreens.put(WebKeys.DEF_LANGUAGE, screenDefinitions);
			} else {
				System.err
						.println("Template Servlet Error Loading Screen Definitions: Confirm that file at URL "
								+ WebKeys.SCREEN_FILE
								+ " contains the screen definitions");
			}
		} else {
			System.err
					.println("Template Servlet Error Loading Screen Definitions: URL "
							+ WebKeys.SCREEN_FILE + " not found");
		}
	}

	public static void loadXML(ServletContext ctxt) throws ServletException {
		if (loadFlag == 0) {
			loadFlag = 1;
		} else {
			return;
		}
		ServletContext context = ctxt; // config.getServletContext();
		String requestMappingsURL = null;
		try {
			// mappings.xml映射配置文件
			requestMappingsURL = context.getResource(WebKeys.MAPPING_FILE)
					.toString();
		} catch (java.net.MalformedURLException ex) {
			System.err
					.println("MainServlet: initializing ScreenFlowManager malformed URL exception: "
							+ ex);
		}
		// 解析URLMapping和EventMapping
		urlMappings = URLMappingsXmlDAO.loadRequestMappings(requestMappingsURL);
		context.setAttribute(WebKeys.URL_MAPPINGS, urlMappings);
		eventMappings = URLMappingsXmlDAO.loadEventMappings(requestMappingsURL);
		context.setAttribute(WebKeys.EVENT_MAPPINGS, eventMappings);
		// wiseking add global flow handler at 2003/12/09
		globalFlows = URLMappingsXmlDAO.loadGlobalFlows(requestMappingsURL);
		context.setAttribute(WebKeys.GLOBAL_FLOWS, globalFlows);
		ScreenFlowData sfd = URLMappingsXmlDAO
				.loadScreenFlowData(requestMappingsURL);
		context.setAttribute(WebKeys.SCREEN_FLOW_DATA, sfd);
		initScreens(context);
	}

	public static void reload(ServletContext ctxt) throws ServletException {
		loadFlag = 0;
		// add it by wiseking 2003 06 12
		ctxt.removeAttribute(WebKeys.REQUEST_PROCESSOR);
		ctxt.removeAttribute(WebKeys.WEB_CONTROLLER);
		loadXML(ctxt);
	}

}