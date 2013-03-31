package com.ailk.bi.common.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.sql.DataSource;

/**
 * Title: CNC营业系统 Description: BeanManaged EB的基类，在该类中设置了数据源，继承该类的Bean可以使用该数据源
 * Copyright: Copyright (c) 2001 Company: LongShine
 * 
 * @author caoyehong
 * @version 1.0
 */
public class EntityBeanAdapter implements EntityBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8682455395945530656L;

	public EntityContext entityContext;

	public DataSource dataSource;

	public void ejbActivate() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	public void ejbLoad() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	public void ejbPassivate() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	public void ejbRemove() throws javax.ejb.RemoveException,
			javax.ejb.EJBException, java.rmi.RemoteException {
	}

	public void ejbStore() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	public void closeConnection(Connection connection, Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
		}
	}

	public void setEntityContext(EntityContext entityContext)
			throws RemoteException {
		try {
			this.entityContext = entityContext;
			javax.naming.Context context = new javax.naming.InitialContext();
			try {
				dataSource = (DataSource) context
						.lookup("java:comp/env/jdbc/DataSource");
			} catch (Exception e) {
				throw new EJBException("Error looking up dataSource: "
						+ e.toString());
			}
		} catch (Exception e) {
			throw new EJBException("Error initializing context:" + e.toString());
		}
	}

	public void unsetEntityContext() throws RemoteException {
		this.entityContext = null;
	}

	public EntityContext getEJBContext() throws RemoteException {
		return this.entityContext;
	}
}