package waf.controller.ejb;

import java.rmi.*;
import javax.ejb.*;

public interface EJBControllerRemote extends EJBObject {
	public com.ailk.bi.common.event.JBTable processEvent(
			com.ailk.bi.common.event.JBTable ev)
			throws com.ailk.bi.common.app.AppException, RemoteException;
}