package com.ailk.bi.common.dbtools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.crystaldecisions.enterprise.ocaframework.idl.OCA.OCAs.interface_num;
import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 数据库相关操作的方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author CNC
 * @version 1.0
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class DBUtil {

	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement prepstmt = null;

	/**
	 * 构造数据库的连接和访问类
	 *
	 * @param conn
	 *            数据库的连接
	 * @throws Exception
	 */
	public DBUtil(Connection conn) throws Exception {
		this.conn = conn;
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * 构造数据库的连接和访问类
	 *
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public DBUtil(Connection conn, String sql) throws Exception {
		this.conn = conn;
		this.prepstmt = conn.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * 返回连接
	 *
	 * @return Connection 连接
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * 取得prepareStatement
	 *
	 * @param sql
	 * @throws SQLException
	 */
	public void createPrepareStatement(String sql) throws SQLException {
		prepstmt = conn.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * 执行查询，返回Vector结果
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Vector executeQueryGetVector(String sql) throws SQLException {
		return transResultToVector(executeQuery(sql));
	}

	/**
	 * 得到结果集Vector,并且已经转化为String,如果为null,侧返回缺省值
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Vector executeQueryGetStringVector(String sql, String strDeault)
			throws SQLException {
		return transResultToStringVector(executeQuery(sql), strDeault);
	}

	/**
	 * 执行查询，返回Array
	 *
	 * @param sql
	 * @param strTemp
	 * @return
	 * @throws SQLException
	 */
	public String[][] executeQueryGetArray(String sql, String strTemp)
			throws SQLException {
		return transResultToArray(executeQuery(sql), strTemp);
	}

	/**
	 * 执行查询，返回Array
	 *
	 * @param sql
	 * @param strTemp
	 * @return
	 * @throws SQLException
	 */
	public String[][] executeQueryGetArray(String sql, String[] where,
			String strTemp) throws SQLException {
		return transResultToArray(executeQuery(sql, where), strTemp);
	}

	/**
	 * 执行查询，返回ArrayMap
	 *
	 * @param sql
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public List executeQueryGetArrayMap(String sql, String[] where)
			throws SQLException {
		return transResultToArrayMap(executeQuery(sql, where));
	}

	/**
	 * 执行查询，返回ResultSet结果
	 *
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		try {
			if (prepstmt != null) {
				return prepstmt.executeQuery();
			} else
				return null;
		} catch (SQLException e) {
			if (stmt != null)
				stmt.close();
			throw e;
		}
	}

	/**
	 * 执行查询，返回Vector结果，每行也是Vector
	 *
	 * @return
	 * @throws SQLException
	 */
	public Vector executeQueryGetVector() throws SQLException {
		return transResultToVector(executeQuery());
	}

	/**
	 * 得到结果集Vector,并且已经转化为String,如果为null,侧返回缺省值
	 *
	 * @param strDeault
	 * @return
	 * @throws SQLException
	 */
	public Vector executeQueryGetStringVector(String strDeault)
			throws SQLException {
		return transResultToStringVector(executeQuery(), strDeault);
	}

	/**
	 * 执行查询，返回Array结果，是二维数组
	 *
	 * @param strTemp
	 *            如果对象为null,侧使用缺省值
	 * @return
	 * @throws SQLException
	 */
	public String[][] executeQueryGetArray(String strTemp) throws SQLException {
		return transResultToArray(executeQuery(), strTemp);
	}

	/**
	 * 执行update类的sql语句
	 *
	 * @param sql
	 *            SQL语句
	 */
	public int executeUpdate(String sql) throws SQLException {
		int ret = AppConst.ERRORRESULT;
		try {
			if (stmt != null) {
				return stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			if (stmt != null)
				stmt.close();
			throw e;
		}
		return ret;
	}

	/**
	 * 执行prepare的sql语句
	 *
	 * @return 执行结果行数
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException {
		int ret = AppConst.ERRORRESULT;
		try {
			if (prepstmt != null)
				ret = prepstmt.executeUpdate();
		} catch (SQLException e) {
			if (stmt != null)
				stmt.close();
			throw e;
		}
		return ret;
	}

	/**
	 * 以事务的方式执行若干条sql语句
	 *
	 * @param strSqls
	 * @return 成功执行的语句的条数
	 * @throws AppException
	 */
	public int execTransUpdate(String[] strSqls) throws AppException {
		int ret = AppConst.ERRORRESULT;   //-1
		if (conn == null)
			{
				return ret;
			}

		boolean oldCommit = true;
		try {
			boolean flag = false;
			oldCommit = conn.getAutoCommit();
			conn.setAutoCommit(oldCommit);
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (stmt != null) {
				for (int i = 0; strSqls != null && i < strSqls.length; i++)
				{
					flag = stmt.execute(strSqls[i]);
					if(flag)
					{
						ret = 1;
					}
				}
				conn.commit();
				conn.setAutoCommit(oldCommit);
				if (strSqls != null)
					return strSqls.length; //ret = 2
			}
		} catch (Exception e) {
			try {
				conn.setAutoCommit(oldCommit);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			throw new AppException(e.toString());  //抛异常
		}
		return ret;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnection() throws Exception {
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (prepstmt != null) {
			prepstmt.close();
			prepstmt = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	/**
	 * 从结果集中取得数据
	 *
	 * @param rs
	 * @param colNum
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	private Object getValue(final ResultSet rs, int colNum, int type)
			throws SQLException {
		return ResultUtil.getValue(rs, colNum, type);
	}

	/**
	 * 从结果集转换到向量中
	 *
	 * @return
	 * @throws SQLException
	 */
	private Vector transResultToVector(ResultSet result) throws SQLException {
		return ResultUtil.transResultToStringVector(result, "");
	}

	private Vector transResultToStringVector(ResultSet result, String strDefalut)
			throws SQLException {
		return ResultUtil.transResultToStringVector(result, strDefalut);
	}

	/**
	 * 从结果集转换到2维数组中
	 *
	 * @return
	 * @throws SQLException
	 */
	private String[][] transResultToArray(ResultSet result, String strDefalut)
			throws SQLException {
		return ResultUtil.transResultToArray(result, strDefalut);
	}

	/**
	 * 从结果集转换成将行封装好hashmap的Arraylist
	 *
	 * @return
	 * @throws SQLException
	 */
	private List transResultToArrayMap(ResultSet result) throws SQLException {
		return ResultUtil.transResultToArrayMap(result);
	}

	/**
	 * 把对象值转化为字符串，
	 *
	 * @param obj
	 * @param strDefalut
	 * @return
	 */
	private String formatObjectToString(Object obj, String strDefalut) {
		return ResultUtil.formatObjectToString(obj, strDefalut);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setString(int index, String value) throws SQLException {
		prepstmt.setString(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setInt(int index, int value) throws SQLException {
		prepstmt.setInt(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setBoolean(int index, boolean value) throws SQLException {
		prepstmt.setBoolean(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setDate(int index, java.sql.Date value) throws SQLException {
		prepstmt.setDate(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setLong(int index, long value) throws SQLException {
		prepstmt.setLong(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param value
	 *            对应值
	 */
	public void setFloat(int index, float value) throws SQLException {
		prepstmt.setFloat(index, value);
	}

	/**
	 * 设置preStmt的对应位置的对应值，从1开始
	 *
	 * @param index
	 *            参数索引
	 * @param iType
	 *            对应值
	 */
	public void setNULL(int index, int iType) throws SQLException {
		prepstmt.setNull(index, iType);
	}

	/**
	 * Clears the current parameter values immediately.
	 */
	public void clearParameters() throws SQLException {
		prepstmt.clearParameters();
	}

	/**
	 * 执行SQL语句返回字段集
	 *
	 * @param sql
	 *            SQL语句
	 * @return ResultSet 字段集
	 */
	private ResultSet executeQuery(String sql) throws SQLException {
		try {
			if (stmt != null) {
				return stmt.executeQuery(sql);
			} else
				return null;
		} catch (SQLException e) {
			if (stmt != null)
				stmt.close();
			throw e;
		}
	}

	/**
	 * 执行SQL语句返回字段集
	 *
	 * @param sql
	 *            SQL语句
	 * @return ResultSet 字段集
	 */
	private ResultSet executeQuery(String sql, String where[])
			throws SQLException {
		createPrepareStatement(sql);
		try {
			if (prepstmt != null) {
				for (int i = 0; where != null && i < where.length; i++) {
					String[] tmp = where[i].split("\\|");
					if (tmp.length > 1) {
						if (tmp[1].equals("1")) {// 数字
							setInt(i + 1, Integer.parseInt(tmp[0]));
						} else if (tmp[1].equals("0")) {
							// 文本
							setString(i + 1, tmp[0]);
						}
					} else {
						setString(i + 1, where[i]);
					}

				}
				// ResultSet rs = prepstmt.executeQuery();

				return prepstmt.executeQuery();
			} else
				return null;
		} catch (SQLException e) {
			if (prepstmt != null)
				prepstmt.close();
			throw e;
		}
	}

	public java.io.InputStream readBlogContent(String keyFld, String blobFld,
			String tablename, String keyId) {
		java.io.InputStream ins = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			st = conn.createStatement();
			String sql = "select " + blobFld + " from " + tablename + " where "
					+ keyFld + "=" + keyId;
			System.out.println(sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				java.sql.Blob blob = rs.getBlob(1);

				ins = blob.getBinaryStream();
			}
			conn.commit();

			return ins;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// return ins;

	}

	/**
	 *
	 * @param id
	 * @param blobFld
	 * @desc:更新blob字段
	 */
	/*
	 * public int updateBlogFld(String keyFld,String blobFld,String
	 * tablename,String keyId,String fileName){ int ret = AppConst.ERRORRESULT;
	 * if (conn == null) return ret;
	 *
	 * try { conn.setAutoCommit(false); Statement stmt = conn.createStatement();
	 *
	 * if (stmt != null) { String sql = "select " + blobFld + " from " +
	 * tablename + " where " + keyFld + "=" + keyId + " for update";
	 *
	 * ResultSet rs=stmt.executeQuery(sql);
	 *
	 * oracle.sql.BLOB blob = null; //weblogic.jdbc.vendor.oracle.OracleThinBlob
	 * blob = null; if(rs.next()){ System.out.println(sql);
	 * blob=(oracle.sql.BLOB)rs.getBlob(1);
	 *
	 * OutputStream outStream=blob.getBinaryOutputStream();
	 *
	 * //这里用一个文件模拟输入流 File file=new File(fileName); System.out.println("blod:" +
	 * fileName); InputStream fin=new FileInputStream(file); //将输入流写到输出流 byte[]
	 * b = new byte[blob.getBufferSize()]; //System.out.println(fileName + ":" +
	 * blob.getBufferSize()); int len = 0; while((len=fin.read(b))!=-1){
	 * outStream.write(b,0,len); } fin.close(); outStream.flush();
	 * outStream.close(); }
	 *
	 *
	 *
	 * conn.commit();
	 *
	 * if (rs != null) { rs.close(); rs = null; } if (stmt != null) {
	 * stmt.close(); stmt = null; } if (prepstmt != null) { prepstmt.close();
	 * prepstmt = null; } } } catch (SQLException eX) { eX.printStackTrace(); }
	 * catch (Exception e) { e.printStackTrace(); } return ret;
	 *
	 * }
	 */
	/**
	 *
	 * @param id
	 * @param blobFld
	 * @desc:更新blob字段
	 */
	/*
	 * public int updateBlogFld(String keyFld,String blobFld,String
	 * tablename,String keyId,FileItem itemUpload){ int ret =
	 * AppConst.ERRORRESULT; if (conn == null) return ret;
	 *
	 * try { conn.setAutoCommit(false); Statement stmt = conn.createStatement();
	 *
	 * if (stmt != null) { String sql = "select " + blobFld + " from " +
	 * tablename + " where " + keyFld + "=" + keyId + " for update";
	 * System.out.println(sql); ResultSet rs=stmt.executeQuery(sql);
	 * if(rs.next()){ oracle.sql.BLOB blob=(oracle.sql.BLOB)rs.getBlob(1);
	 * //到数据库的输出流 //OutputStream outStream=blob.getBinaryOutputStream();
	 * PrintStream ps = new PrintStream(blob.getBinaryOutputStream());
	 * BufferedInputStream bis = new
	 * BufferedInputStream(itemUpload.getInputStream());
	 *
	 *
	 * byte[] buff = new byte[1024]; int n = 0; //从输入到输出 while ((n =
	 * bis.read(buff)) != -1) { ps.write(buff, 0, n);
	 *
	 * } //清空流的缓存 ps.flush(); //关闭流，注意一定要关 ps.close(); bis.close();
	 *
	 *
	 * } //依次关闭 conn.commit();
	 *
	 * if (rs != null) { rs.close(); rs = null; } if (stmt != null) {
	 * stmt.close(); stmt = null; } } } catch (Exception e) {
	 * e.printStackTrace(); } return ret;
	 *
	 * }
	 */

}