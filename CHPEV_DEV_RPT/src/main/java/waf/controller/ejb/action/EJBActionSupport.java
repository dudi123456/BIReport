package waf.controller.ejb.action;

import waf.controller.ejb.StateMachine;

public abstract class EJBActionSupport implements java.io.Serializable,
		EJBAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2189007022720416145L;
	protected StateMachine machine = null;

	public void init(StateMachine machine) {
		this.machine = machine;
	}

	public void doStart() {
	}

	public void doEnd() {
	}
}
