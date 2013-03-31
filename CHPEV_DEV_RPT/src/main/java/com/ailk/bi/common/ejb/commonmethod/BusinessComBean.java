package com.ailk.bi.common.ejb.commonmethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.ejb.EJBException;

import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.ejb.SessionBeanAdapter;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 受理的底层Bean，定义了一些远程方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class BusinessComBean extends SessionBeanAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3831023547979595365L;

	/**
	 * 按照类型选择一个序列的值
	 * 
	 * @param strTable
	 *            序列所对应的表名，在数据库中序列是这个格式:SEQ_strTable_ID
	 * @param iLength
	 *            要求左补零后的长度
	 * @return
	 */
	@SuppressWarnings("resource")
	public java.lang.String getSeq(java.lang.String strTable, int iLength) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String strFindSeqSQL = "";

		String strSQL = "";
		String strSeq = "";
		String strIDPrefix = "";

		try {
			connection = super.getConn();
			connection.setAutoCommit(true);
			strFindSeqSQL = "select SEQUENCE_NAME  from USER_SEQUENCES  WHERE SEQUENCE_NAME='SEQ_"
					+ strTable.toUpperCase() + "_ID'";
			statement = connection.prepareStatement(strFindSeqSQL);
			resultSet = statement.executeQuery();
			// 首先判断序列是否存在，不存在就创建
			if (!resultSet.next()) {
				strSQL = "CREATE SEQUENCE  SEQ_" + strTable.toUpperCase()
						+ "_ID  START WITH 2 INCREMENT BY 1";
				statement = connection.prepareStatement(strSQL);
				statement.execute();
				strSeq = StringB.lpad("1", iLength, "0");
				statement.close();
			} else {
				strSQL = "select '" + strIDPrefix + "'||LPAD(SEQ_"
						+ strTable.toUpperCase() + "_ID.nextval,"
						+ String.valueOf(iLength - strIDPrefix.length())
						+ ",'0')  from dual";
				statement = connection.prepareStatement(strSQL);
				resultSet = statement.executeQuery();
				resultSet.next();
				strSeq = resultSet.getString(1).trim();
				statement.close();
			}
		} catch (SQLException e) {
			throw new EJBException("Error executing SQL : " + e.toString());
		} finally {
			super.freeConn(connection, statement);
		}
		return strSeq;
	}

	/**
	 * 一次取得多个序列
	 * 
	 * @param iTotal
	 *            需要的总数
	 * @param strTable
	 *            表名
	 * @param iLength
	 *            格式化后的位数
	 * @return 序列集合
	 */
	public java.lang.String[] getSeqs(int iTotal, java.lang.String strTable,
			int iLength) {

		String[] strSeqs = new String[iTotal];
		for (int i = 0; i < iTotal; i++) {
			strSeqs[i] = getSeq(strTable, iLength);
		}
		return strSeqs;
	}

	/**
	 * 
	 * @param strSql
	 *            查询的sql
	 * @param retVet
	 *            参数返回
	 * @param strDef
	 * @return
	 */
	public Vector getVectorFromSql(String strSql, String strDef)
			throws Exception {
		Vector retVect = null;

		retVect = super.getQueryStringVector(strSql, strDef);
		return retVect;
	}

	/**
	 * 
	 * @param strSql
	 *            查询的sql
	 * @param retVet
	 *            参数返回
	 * @param strDef
	 * @return
	 */
	public String[][] getArrayFromSql(String strSql, String strDef)
			throws Exception {
		String[][] retVet = null;

		retVet = super.getQueryArray(strSql, strDef);
		return retVet;
	}

	public int doUpdate(String strSql) throws Exception {
		return super.executeUpdate(strSql);
	}

	public void doUpdate(String[] strSqls) throws Exception {
		Connection conn = null;
		try {
			conn = super.getConn();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();

			for (int i = 0; i < strSqls.length; i++) {
				stmt.executeUpdate(strSqls[i]);
			}
			conn.commit();
			stmt.close();
		} catch (Exception e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex2) {
			}
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex1) {
				}
		}
	}

}