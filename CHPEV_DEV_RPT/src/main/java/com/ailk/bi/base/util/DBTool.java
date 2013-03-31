package com.ailk.bi.base.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.DBUtil;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBTool {

	/**
	 * 取得数据源连接（指定）
	 * 
	 * @return
	 * @throws AppException
	 */
	public static Connection getWLSConn() throws AppException {
		Connection connection = null;
		try {
			// 取得JNDI名字
			String ds = GetSystemConfig.getBIBMConfig().getExtParam(
					"trans_datasource");

			// 取得事务URL
			String url = GetSystemConfig.getBIBMConfig().getExtParam(
					"trans_url");

			// 取得weblogic数据源连接
			connection = getConnFromWLS(url, ds);

			return connection;

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("从XA连接池中获得数据库连接失败!");
		}
	}

	public static Connection getWLSConnWithJNDI() throws AppException {
		Connection connection = null;
		try {
			// 取得JNDI名字
			String ds = GetSystemConfig.getBIBMConfig().getAppDataSource();
			String url = GetSystemConfig.getBIBMConfig().getAppURL();
			// 取得weblogic数据源连接
			connection = getConnFromWLS(url, ds);

			return connection;

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("从连接池["
					+ GetSystemConfig.getBIBMConfig().getAppDataSource()
					+ "]中获得数据库连接失败!");
		}
	}

	/**
	 * 区的特定的数据库连接
	 * 
	 * @param source
	 * @param url
	 * @return
	 * @throws AppException
	 */
	public static Connection getFixedWLSConn(String source, String url)
			throws AppException {
		Connection connection = null;
		try {

			// 取得weblogic数据源连接
			connection = getConnFromWLS(url, source);

			return connection;

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("从ACCT连接池中获得数据库连接失败!");
		}
	}

	/**
	 * 取得weblogic连接
	 * 
	 * @param url
	 * @param strDS
	 * @return
	 * @throws AppException
	 */
	private static Connection getConnFromWLS(String url, String strDS)
			throws AppException {
		Connection conn = null;
		if (url == null) {
			url = "t3://localhost:7001";
		}
		try {
			Hashtable env = new Hashtable();
			env.put(InitialContext.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			env.put(InitialContext.PROVIDER_URL, url);
			//
			Context ctx = new InitialContext(env);
			//
			DataSource ds = (DataSource) ctx.lookup(strDS);
			//
			conn = ds.getConnection();
			return conn;
		} catch (Exception ex) {
			throw new AppException(
					"Fatal Error: 从XA连接池中获取连接失败，请查看系统配置文件中weblogic的XA连接池相关信息是否正确配置和使用!["
							+ ex.toString() + "]");
		}

	}

	private static void freeWLSConn(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			;
		}
	}

	/**
	 * 根据SQL查找结果，并存放在Vector中，Vector的内容为每行记录的Vector
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDef
	 *            如果查找到的字段值为空时，相应的替换值
	 * @return 返回二维Vector
	 */
	public static Vector getVectorFromSQL(String strSql, String strDef)
			throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getWLSConn();
				DBUtil dbutil = new DBUtil(connection);
				Vector v = dbutil.executeQueryGetStringVector(strSql, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeWLSConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}

	/**
	 * 根据SQL查找结果，并存放在二维数组中
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDef
	 *            如果查找到的字段值为空时，相应的替换值
	 * @return 返回二维数组
	 */
	public static String[][] getArrayFromSQL(String strSql, String strDef)
			throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getWLSConn();
				DBUtil dbutil = new DBUtil(connection);
				String[][] v = dbutil.executeQueryGetArray(strSql, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeWLSConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}

	}

	/**
	 * 执行更新语句
	 * 
	 * @param strSql
	 * @return 成功更新的记录数目，如果SQL错误 则为－1
	 */
	public static int execUpdate(String strSql) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getWLSConn();
				DBUtil dbutil = new DBUtil(connection);
				int v = dbutil.executeUpdate(strSql);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeWLSConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}

	}

	/**
	 * 在一个事务中执行多条DML语句,如果事务过程中失败则执行回滚
	 * 
	 * @param strSqls
	 *            存放多条sql的数组
	 * @throws AppException
	 */
	public static int execTransUpdate(String[] strSqls) throws AppException {
		int ret = AppConst.ERRORRESULT;
		if (strSqls == null || strSqls.length < 1) {
			throw new AppException("执行的sql数组为空，请检查");
		}

		try {
			Connection connection = null;
			try {
				connection = getWLSConn();
				DBUtil dbutil = new DBUtil(connection);
				ret = dbutil.execTransUpdate(strSqls);
				dbutil.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeWLSConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
		return ret;
	}

	/**
	 * 使用已经获取的连接执行数据库更新语句
	 * 
	 * @param conn
	 *            已经获得的连接 通常使用DBUtil.getConnectionFromDS()方法获得连接
	 * @param sql
	 *            SQL语句
	 * @return 成功更新的条数
	 */
	public static int s(Connection conn, String sql) throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeUpdate:数据库连接为空，请先获取数据库连接connection");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (stmt != null) {
				return stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			throw new AppException("SQL异常;[" + e.toString() + "]");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 按事务执行多条sql更新语句,如果全部成功，则提交，否则全部回滚
	 * 
	 * @param conn
	 *            已经获得的数据库连接，并且autoCommit为false;
	 * @param sqls
	 *            要执行的更新语句数组
	 * @return 成功执行语句的条数
	 * @throws AppException
	 */
	public static int execTransUpdate(Connection conn, String[] sqls)
			throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeUpdates:数据库连接为空，请先获取数据库连接connection");
			if (conn.getAutoCommit()) {
				throw new AppException("DBUtil.executeUpdates:数据库连接必须设置为非自动提交!");
			}
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (stmt != null) {
				for (int i = 0; sqls != null && i < sqls.length; i++) {
					stmt.execute(sqls[i]);
				}
				conn.commit();
				if (sqls != null)
					return sqls.length;
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex1) {
			}
			throw new AppException("SQL异常;[" + e.toString() + "]");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			}
		}
		return ret;
	}

	/**
	 * 执行查询返回Array结果,是二维结果
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDefault
	 *            如果返回结果的字段值为空时的替换串,通常为""
	 * @return 查询到的结果集
	 * @throws Exception
	 */
	public static String[][] getArrayFromSQL(Connection conn, String strSql,
			String strDefault) throws AppException {
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeQryArray:数据库连接为空，请先获取数据库连接connection");
			DBUtil dbutil = new DBUtil(conn);
			String[][] strTemp = dbutil
					.executeQueryGetArray(strSql, strDefault);
			return strTemp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("DBUtil.executeQryArray:exception;["
					+ e.getMessage() + "]");
		}
	}

	/**
	 * 执行查询，返回结果集Vector,并且已经把对象转化为String,如果字段对象为null,则使用缺省值
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDefault
	 *            如果返回结果的字段值为空时的替换串,通常为""
	 * @return 查询到的结果集
	 * @throws Exception
	 */
	public static Vector getVectorFromSQL(Connection conn, String strSql,
			String strDefault) throws AppException {
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeQryVector:数据库连接为空，请先获取数据库连接connection");
			DBUtil dbutil = new DBUtil(conn);
			Vector v = dbutil.executeQueryGetStringVector(strSql, strDefault);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("DBUtil.executeQryVector:exception;["
					+ e.getMessage() + "]");
		}
	}

	/**
	 * 更新表的long raw字段
	 * 
	 * @param sql
	 * @param content
	 */
	public static boolean execUpdateLongRawField(Connection conn, String sql,
			byte[] content) throws AppException {
		boolean ret = false;
		if (conn == null)
			throw new AppException("conn数据连接为null!");
		try {
			InputStream fin = new ByteArrayInputStream(content);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setBinaryStream(1, fin, content.length);
			ret = pstmt.execute();
			pstmt.close();
			fin.close();
		} catch (Exception ex) {
			throw new AppException("更新Lang Raw字段失败!原因[" + ex.toString() + "]");
		}
		return ret;
	}

	/*
	 * //////////////////测试的jsp <%@ page contentType="text/html;charset=GBK"%>
	 * <%@ page errorPage="/web/common/SystemError.jsp"%> <%@ page
	 * import="java.sql.*,java.io.*,java.util.*,com.asiabi.common.app.*" %> <%
	 * out.println("deal now...<br>"); UpFileInfo fileInfo =
	 * FileUtil.uploadFile(request); if( fileInfo!=null ){
	 * out.println("fullfile:"+fileInfo.getFullFileName()+"
	 * file:"+fileInfo.getFileName()+"<br>");
	 * FileUtil.saveFile(fileInfo.getContent(), "d:/tt/",
	 * "up_"+fileInfo.getFileName());
	 * //FileUtil.saveFile(fileInfo.getContent(),"d:/",fileInfo.getFileName());
	 * Connection conn = DBUtil.getConnectionFromDS(null, null,
	 * null,"DataSource"); if( conn !=null ){ String sql = "UPDATE rep_detail
	 * set MODU_CONTENT=? where rownum=1"; try{ DBUtil.execUpdateLongRawField(
	 * conn, sql, fileInfo.getContent()); out.println("save db ok!");
	 * }catch(Exception e ){ e.printStackTrace(); out.println("save db err!");
	 * conn.close(); return; } sql = "select MODU_CONTENT from rep_detail where
	 * rownum=1"; try{ byte[] cont=DBUtil.getLongRawField( conn, sql);
	 * FileUtil.saveFile(cont, "d:/tt/", "db_"+fileInfo.getFileName());
	 * out.println(cont.length+":"+fileInfo.getContent().length);
	 * out.println("read db ok!"); }catch(Exception e ){ e.printStackTrace();
	 * out.println("read db err!"); conn.close(); return; } conn.close(); }else{
	 * out.println("conn is null!"); } } out.print("deal ok."); %>
	 */
	/**
	 * 查找满足条件的第一行long raw字段 格式: SELECT longrawfield FROM ttt WHERE ．．．
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws AppException
	 */
	public static byte[] getLongRawField(Connection conn, String sql)
			throws AppException {
		byte[] content = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			InputStream is = null;
			int iTotal = 0;
			int iLen = 0;
			byte[] b = new byte[1024];
			byte[] pOld = null;
			if (rs.next()) {
				is = rs.getBinaryStream(1);

				if (is == null) {
					stmt.close();
					throw new AppException("查找Long Raw字段失败! 没有满足条件的记录!");
				}

				for (;;) {
					if ((iLen = is.read(b)) == -1)
						break;
					if (content == null) {
						content = new byte[iLen];
					} else {
						pOld = content;
						content = new byte[iTotal + iLen];
					}
					if (pOld != null) {
						for (int i = 0; i < pOld.length; i++) {
							content[i] = pOld[i];
						}
					}
					for (int i = 0; i < iLen; i++) {
						content[i + iTotal] = b[i];
					}
					iTotal += iLen;
				}
			}

			if (rs.next()) {
				stmt.close();
				throw new AppException("查找Long Raw字段失败! 必须控制WHERE条件，返回一条记录!");
			}

			rs.close();

			return content;
		} catch (IOException ioe) {
			throw new AppException("IO查找Long Raw字段失败!原因[" + ioe.toString()
					+ "]");
		} catch (SQLException ex) {
			throw new AppException("SQL查找Long Raw字段失败!原因[" + ex.toString()
					+ "]");
		}
	}

	/**
	 * 执行查询返回Array结果,是二维结果
	 * 
	 * @param strSql
	 *            SQL语句
	 * @param strDefault
	 *            如果返回结果的字段值为空时的替换串,通常为""
	 * @return 查询到的结果集
	 * @throws Exception
	 */
	public static String[][] execQryArray(Connection conn, String strSql,
			String strDefault) throws AppException {
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeQryArray:数据库连接为空，请先获取数据库连接connection");
			DBUtil dbutil = new DBUtil(conn);
			String[][] strTemp = dbutil
					.executeQueryGetArray(strSql, strDefault);
			dbutil.closeConnection();
			return strTemp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("DBUtil.executeQryArray:exception;["
					+ e.getMessage() + "]");
		} finally {
			freeConn(conn);
		}
	}

	public static void freeConn(Connection conn) {
		try {
			if (conn != null) {
				if (!conn.isClosed())
					conn.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (null != conn) {
				try {
					if (!conn.isClosed())
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * 使用已经获取的连接执行数据库更新语句
	 * 
	 * @param conn
	 *            已经获得的连接 通常使用DBUtil.getConnectionFromDS()方法获得连接
	 * @param sql
	 *            SQL语句
	 * @return 成功更新的条数
	 */
	public static int execUpdate(Connection conn, String sql)
			throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeUpdate:数据库连接为空，请先获取数据库连接connection");

			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			if (stmt != null) {

				return stmt.executeUpdate(sql);
			}

		} catch (SQLException e) {
			throw new AppException("SQL异常;[" + e.toString() + "]");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			freeConn(conn);

		}
		return ret;
	}

	/**
	 * 按事务执行多条sql更新语句,如果全部成功，则提交，否则全部回滚
	 * 
	 * @param conn
	 *            已经获得的数据库连接，并且autoCommit为false;
	 * @param sqls
	 *            要执行的更新语句数组
	 * @return 成功执行语句的条数
	 * @throws AppException
	 */
	public static int execUpdates(Connection conn, String[] sqls)
			throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException(
						"DBUtil.executeUpdates:数据库连接为空，请先获取数据库连接connection");
			if (conn.getAutoCommit()) {
				throw new AppException("DBUtil.executeUpdates:数据库连接必须设置为非自动提交!");
			}
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (stmt != null) {
				for (int i = 0; sqls != null && i < sqls.length; i++) {
					stmt.execute(sqls[i]);
				}
				conn.commit();
				if (sqls != null)
					return sqls.length;
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex1) {
			}
			throw new AppException("SQL异常;[" + e.toString() + "]");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			}
			freeConn(conn);

		}
		return ret;
	}

}
