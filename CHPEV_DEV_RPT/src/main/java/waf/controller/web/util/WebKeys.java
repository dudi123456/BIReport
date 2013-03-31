package waf.controller.web.util;

/**
 * This interface contains all the keys that are used to store data in the
 * different scopes of web-tier. These values are the same as those used in the
 * JSP pages (useBean tags).
 */
public class WebKeys {

	protected WebKeys() {
	} // prevent instanciation

	public static final String COMPONENT_MANAGER = "waf.COMPONENT_MANAGER";

	public static final String SCREEN_FLOW_MANAGER = "waf.SCREEN_FLOW_MANAGER";

	public static final String SCREEN_FLOW_DATA = "waf.SCREEN_FLOW_DATA";

	public static final String REQUEST_PROCESSOR = "waf.REQUEST_PROCESSOR";

	public static final String CURRENT_SCREEN = "waf.CURRENT_SCREEN";

	public static final String PREVIOUS_SCREEN = "waf.PREVIOUS_SCREEN";

	public static final String CURRENT_URL = "waf.CURRENT_URL";

	public static final String PREVIOUS_URL = "waf.PREVIOUS_URL";

	public static final String PREVIOUS_REQUEST_PARAMETERS = "waf.PREVIOUS_REQUEST_PARAMETERS";

	public static final String PREVIOUS_REQUEST_ATTRIBUTES = "waf.PREVIOUS_REQUEST_ATTRIBUTES";

	public static final String URL_MAPPINGS = "waf.URL_MAPPINGS";

	public static final String EVENT_MAPPINGS = "waf.EVENT_MAPPINGS";

	public static final String GLOBAL_FLOWS = "waf.GLOBAL_FLOWS"; // king

	// add at
	// 2003/12/09

	public static final String MISSING_FORM_DATA = "waf.MISSING_FORM_DATA";

	public static final String SERVER_TYPE = "waf.SERVER_TYPE";

	public static final String WEB_CONTROLLER = "waf.WEB_CLIENT_CONTROLLER";

	public static final String EJB_CONTROLLER = "waf.EJB_CLIENT_CONTROLLER";

	// 在页面处理屏幕变化时，存放在session中的页面key变量
	public static final String HANDLER_SCREEN_KEY = "waf.HANDLER_SCREEN_KEY";

	// 提示信息session属性key
	public static final String EXPTINFO = "waf.INFOMESSAGE";

	public static final String ALERTINFO = "waf.ALERTINFOMESSAGE";

	public static final String EXPTICON = "waf.INFOICON";

	public static final String EXPURL = "waf.EXPURL";

	// 下一个页面是否为空的
	public static final String NULL_SCREEN = "waf.NuLlScReEn";

	public static final String MAPPING_FILE = "/WEB-INF/config/mappings.xml";

	public static final String SCREEN_FILE = "/WEB-INF/config/screendefinitions.xml";

	public static final String DEF_LANGUAGE = "en_US";

	public static final String SYS_LOGINFLAG = "waf.islogin";
}
