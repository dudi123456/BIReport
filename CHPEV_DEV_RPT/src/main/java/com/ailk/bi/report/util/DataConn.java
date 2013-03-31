package com.ailk.bi.report.util;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * <p>
 * Title: connect to database
 * </p>
 * 
 * <p>
 * Description:to mysql to db2,JNDI,JDBC
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: ailk
 * </p>
 * 
 * @author 
 * @version 1.0
 */
@SuppressWarnings({ "unused" })
public class DataConn {
	public DataConn() {
	}

	public static Connection connecttomysql() {
		// Connect to the database-mysql
		Connection con = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		} catch (ClassNotFoundException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		}
		// SQL Request
		try {
			con = DriverManager
					.getConnection(
							"jdbc:mysql://136.6.6.194:3306/bbscs?useUnicode=true&characterEncoding=gb2312",
							"root", "rising");
		} catch (SQLException ex1) {

		}
		return con;
	}

	public static Connection connecttomysql_sdf() {
		// Connect to the database-mysql
		Connection con = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		} catch (ClassNotFoundException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		}
		// SQL Request
		try {
			con = DriverManager
					.getConnection(
							"jdbc:mysql://136.5.199.27:3309/bbscs?useUnicode=true&characterEncoding=gb2312",
							"root", "rising");
		} catch (SQLException ex1) {

		}
		return con;
	}

	@SuppressWarnings("static-access")
	public static Connection connecttodb2() {
		// Connect to the database-ibm db2
		Connection con = null;
		try {
			Context ctx = new InitialContext();
			if (ctx == null) {
				throw new Exception("Boom - No Context");
			}
			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/DB2AIOMNI");
			if (ds != null) {
				con = ds.getConnection();
			}
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	@SuppressWarnings("static-access")
	public static Connection connecttodb2_cqedw() {
		// Connect to the database-ibm db2
		Connection con = null;
		try {
			Context ctx = new InitialContext();
			if (ctx == null) {
				throw new Exception("Boom - No Context");
			}
			DataSource ds = (DataSource) ctx.lookup("jdbc/DB2EDA");
			if (ds != null) {
				con = ds.getConnection();
			}
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	@SuppressWarnings("static-access")
	public static Connection connecttodb2_cqedw_dw(String para) {
		// Connect to the database-ibm db2
		Connection con = null;
		try {

			Context ctx = new InitialContext();
			if (ctx == null) {
				throw new Exception("Boom - No Context");
			}
			String url = "";
			if (para.equals("test")) {
				url = "java:comp/env/jdbc/DB2EDA_DW";
			} else {
				url = "jdbc/DB2EDA_DW";
			}
			DataSource ds = (DataSource) ctx.lookup(url);

			if (ds != null) {
				con = ds.getConnection();
			}
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public static Connection getDB2JdbcByUrl(String sys) throws SQLException {
		Connection conn = null;
		try {
			String server = "";
			String port = "";
			String sid = "";
			String user = "";
			String psw = "";
			if (sys.equals("test")) {
				server = "136.6.6.206";
				port = "5000";
				sid = "cqtest1";
				user = "dw";
				psw = "dw";
			} else if (sys.equals("online")) {
				server = "136.6.6.174";
				port = "5000";
				sid = "cqedw";
				user = "dw";
				psw = "dd23yb957";
			}
			String url = "jdbc:db2://" + server + ":" + port + "/" + sid;
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			conn = DriverManager.getConnection(url, user, psw);
			System.out.println("Andrew: getConnection ok");//$NON-NLS-1$
		} catch (InstantiationException e) {
			System.out.println("Andrew: InstantiationException!");//$NON-NLS-1$
		} catch (IllegalAccessException e) {
			System.out.println("Andrew: IllegalAccessException!");//$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			System.out.println("Andrew: ClassNotFoundException!");//$NON-NLS-1$
		}
		return conn;
	}

	public static final String DB2DRIVER = "com.ibm.db2.jcc.DB2Driver";
	public static final String DB2URL = "jdbc:db2://136.6.6.197:50000/AIOMNI";
	public static final String DB2USER = "aiomni";
	public static final String DB2PASSWORD = "aiomnivision";

	public static Connection getDB2ConnectionByJDBC() throws SQLException {
		Connection conn = null;
		try {
			Class.forName(DB2DRIVER).newInstance();
			conn = DriverManager.getConnection(DB2URL, DB2USER, DB2PASSWORD);
			System.out.println("Andrew: getConnection ok");//$NON-NLS-1$
		} catch (InstantiationException e) {
			System.out.println("Andrew: InstantiationException!");//$NON-NLS-1$
		} catch (IllegalAccessException e) {
			System.out.println("Andrew: IllegalAccessException!");//$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			System.out.println("Andrew: ClassNotFoundException!");//$NON-NLS-1$
		}
		return conn;
	}

}
