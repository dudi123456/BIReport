package waf.controller.ejb.action;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.event.*;

import waf.controller.ejb.StateMachine;

public interface EJBAction {

	public void init(StateMachine urc);

	public void doStart();

	public JBTable perform(JBTable ev) throws AppException;

	public void doEnd();
}
