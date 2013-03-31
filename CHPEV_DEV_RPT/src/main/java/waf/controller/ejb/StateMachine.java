package waf.controller.ejb;

import java.util.HashMap;

import javax.ejb.SessionContext;

import waf.controller.ejb.action.EJBAction;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.event.JBTable;

/**
 * This class is a responsible for processing Events recieved from the client
 * tier. Af part of the WAF framework the events are generated by web actions.
 * 
 * The State Machine ties all EJB components together dynamically at runtime
 * thus providing support for reusable components.
 * 
 * This class should not be updated to handle various event types. This class
 * will use ActionHandlers to handle events that require processing beyond the
 * scope of this class.
 * 
 * The mapping of the event names to handlers is mangaged by the JNDI key
 * contained in the Event:getEventName() which is looked up from an environment
 * entry located in the EJB Deployment descriptor of the EJBClientController. A
 * second option to event handling is to do so in the XML file.
 * 
 * State may be stored in the attributeMap
 * 
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StateMachine implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 653376515616029851L;

	private EJBControllerRemoteBean ccejb;

	private HashMap attributeMap;

	private HashMap actionMap;

	private SessionContext sc;

	public StateMachine(EJBControllerRemoteBean ccejb, SessionContext sc) {
		this.ccejb = ccejb;
		this.sc = sc;
		attributeMap = new HashMap();
		actionMap = new HashMap();
	}

	public JBTable processEvent(JBTable ev) throws AppException {
		String actionName = ev.getEJBActionClassName();
		JBTable response = null;
		if (actionName != null) {
			EJBAction action = null;
			try {
				if (actionMap.get(actionName) != null) {
					action = (EJBAction) actionMap.get(actionName);
				} else {
					com.ailk.bi.common.app.Debug.println("actionName="
							+ actionName);
					// Class.forName("").getClassLoader().getSystemClassLoader()
					action = (EJBAction) Class.forName(actionName)
							.newInstance();
					com.ailk.bi.common.app.Debug.println("action=" + action);
					actionMap.put(actionName, action);
				}
			} catch (Exception ex) {
				System.err.println("StateMachine: error loading " + actionName
						+ " :" + ex);
			}
			if (action != null) {
				action.init(this);
				// do the magic
				action.doStart();
				response = action.perform(ev);
				action.doEnd();
			}
		}
		return response;
	}

	public void setAttribute(String key, Object value) {
		attributeMap.put(key, value);
	}

	public Object getAttribute(String key) {
		return attributeMap.get(key);
	}

	public EJBControllerRemoteBean getEJBController() {
		return ccejb;
	}

	public SessionContext getSessionContext() {
		return sc;
	}

}
