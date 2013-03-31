package waf.controller.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.ailk.bi.common.event.JBTable;

public class EJBControllerRemoteBean implements SessionBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 501178224859241487L;

	protected StateMachine sm;

	private SessionContext sessionContext;

	public void ejbCreate() {
		sm = new StateMachine(this, sessionContext);
	}

	public JBTable processEvent(JBTable ev)
			throws com.ailk.bi.common.app.AppException {
		return (sm.processEvent(ev));
	}

	public void ejbRemove() {
		sm = null;
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
}