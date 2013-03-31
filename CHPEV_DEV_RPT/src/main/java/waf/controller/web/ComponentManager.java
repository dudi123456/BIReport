package waf.controller.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionListener;

/**
 * This interface defines the services that need to be accessed from multiple
 * components in the web tier. This class extends the HttpSessionListener to
 * ensure that this class is loaded at startup.
 * 
 * Implementations of this class can be used to initialize objects used in the
 * persentation tier.
 * 
 */
public interface ComponentManager extends HttpSessionListener {

	/**
	 * Save the WebController in the ServletContext. The controller should be
	 * not tied to any user and should be stateless
	 */
	// public WebController getWebController(HttpSession session);
	public WebController getWebController(ServletContext context);
}
