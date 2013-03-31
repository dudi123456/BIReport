package com.ailk.bi.common.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ailk.bi.common.dbtools.DBUtil;

/**
 * Title: CNC营业系统 Description: SB的基类，在该类中设置了数据对数据库操作的一些方法，继承该类的Bean可以使用该数据操作函数
 * Copyright: Copyright (c) 2001 Company: LongShine
 * 
 * @author caoyehong
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class SessionBeanAdapter implements SessionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214615600235993315L;

	public SessionBeanAdapter() {
	}

	/** SessionContext */
	protected SessionContext context = null;

	public void ejbCreate() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(SessionContext parm1) {
		this.context = parm1;
	}

	// 获得数据源的连接
	protected Connection getConn() throws SQLException {
		Connection conn = null;
		try {
			javax.naming.Context context = new javax.naming.InitialContext();
			DataSource ds = (DataSource) context.lookup(EjbConst.DATASOURCE);
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
			freeConn(conn);
			throw new EJBException(e);
		}
		return conn;
	}

	/**
	 * 执行查询返回Vector结果
	 * 
	 * @param strSql
	 * @return
	 * @throws Exception
	 */
	protected Vector getQueryVector(String strSql) throws Exception {
		Connection connection = null;
		try {
			connection = this.getConn();
			DBUtil dbutil = new DBUtil(connection);
			Vector v = dbutil.executeQueryGetVector(strSql);
			dbutil.closeConnection();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.freeConn(connection);
		}
	}

	/**
	 * 执行查询，返回结果集Vector,并且已经把对象转化为String,如果对象为null, 侧使用缺省值
	 * 
	 * @param strSql
	 * @return
	 * @throws Exception
	 */
	protected Vector getQueryStringVector(String strSql, String strDefault)
			throws Exception {
		Connection connection = null;
		try {
			connection = this.getConn();
			DBUtil dbutil = new DBUtil(connection);
			Vector v = dbutil.executeQueryGetStringVector(strSql, strDefault);
			dbutil.closeConnection();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null)
				this.freeConn(connection);
		}
	}

	/**
	 * 执行查询返回Array结果,是二维结果
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDefault
	 * @return
	 * @throws Exception
	 */
	protected String[][] getQueryArray(String strSql, String strDefault)
			throws Exception {
		Connection connection = null;
		try {
			connection = this.getConn();
			DBUtil dbutil = new DBUtil(connection);
			String[][] strTemp = dbutil
					.executeQueryGetArray(strSql, strDefault);
			dbutil.closeConnection();
			return strTemp;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.freeConn(connection);
		}
	}

	/**
	 * 执行Inserte,update,delete等SQL
	 * 
	 * @param strSql
	 * @throws Exception
	 */
	protected int executeUpdate(String strSql) throws Exception {
		Connection connection = null;
		int iRet = 0;
		try {
			connection = this.getConn();
			DBUtil dbutil = new DBUtil(connection);
			iRet = dbutil.executeUpdate(strSql);
			dbutil.closeConnection();
			return iRet;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			this.freeConn(connection);
		}
	}

	/** 释放数据源的连接 */
	protected void freeConn(Connection conn) {
		freeConn(conn, null, null);
	}

	/** 释放数据源的连接 */
	protected void freeConn(Connection connection, Statement statement) {
		freeConn(connection, statement, null);
	}

	/** 释放数据源的连接 */
	protected void freeConn(Connection connection, PreparedStatement statement) {
		freeConn(connection, statement, null);
	}

	/** 释放数据操作的相关实例 */
	protected void freeConn(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			try {
				if (rs != null)
					rs.close();
			} finally {
				try {
					if (ps != null)
						ps.close();
				} finally {
					if (conn != null)
						conn.close();
				}
			}
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}

	/** 释放数据操作的相关实例 */
	protected void freeConn(Connection conn, Statement ps, ResultSet rs) {
		try {
			try {
				if (rs != null)
					rs.close();
			} finally {
				try {
					if (ps != null)
						ps.close();
				} finally {
					if (conn != null)
						conn.close();
				}
			}
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}
}