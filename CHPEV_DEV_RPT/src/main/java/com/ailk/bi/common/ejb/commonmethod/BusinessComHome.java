package com.ailk.bi.common.ejb.commonmethod;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface BusinessComHome extends javax.ejb.EJBHome {
	public BusinessCom create() throws CreateException, RemoteException;
}