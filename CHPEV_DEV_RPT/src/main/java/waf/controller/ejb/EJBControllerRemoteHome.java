package waf.controller.ejb;

import java.rmi.*;
import javax.ejb.*;

public interface EJBControllerRemoteHome extends EJBHome {
	public EJBControllerRemote create() throws RemoteException, CreateException;
}