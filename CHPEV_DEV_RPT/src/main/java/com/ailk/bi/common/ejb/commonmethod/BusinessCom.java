package com.ailk.bi.common.ejb.commonmethod;

import javax.ejb.*;
import java.util.*;
import java.rmi.*;

@SuppressWarnings({ "rawtypes" })
public interface BusinessCom extends EJBObject {
	public String getSeq(String strTable, int iLength) throws RemoteException;

	public String[] getSeqs(int iTotal, String strTable, int iLength)
			throws RemoteException;

	public Vector getVectorFromSql(String strSql, String strDef)
			throws Exception, RemoteException;

	public String[][] getArrayFromSql(String strSql, String strDef)
			throws Exception, RemoteException;

	public int doUpdate(String strSql) throws Exception, RemoteException;

	public void doUpdate(String[] strSqls) throws Exception, RemoteException;
}