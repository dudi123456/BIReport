package com.ailk.bi.common.dbtools;

import com.fins.gt.dataaccess.helper.DataAccessUtils;
import com.ailk.bi.common.annotation.AnnotationUtil;
import com.ailk.bi.common.annotation.PriKey;
import com.ailk.bi.common.annotation.TableCol;
import com.ailk.bi.common.annotation.TableName;
import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.JBTableBase;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

//import org.apache.commons.fileupload.FileItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 公共底层函数，提供直接执行数据库存储的方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class WebDBUtil {
	public static final int TCONN_SINGLE = 99; // 直接取得连接，不通过连接池
	public static final int TCONN_WEBLOGIC = 0; // weblogic连接池
	public static final int TCONN_TOMCAT = 1; // tomcat连接池
	public static final int TCONN_WEBSPHERE = 2; // webshpere连接池

	private int totalCount;

	public WebDBUtil() {
		totalCount = 0;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public static Connection getConn() throws AppException {
		Connection connection = null;
		try {
			String cType = GetSystemConfig.getBIBMConfig().getDBCType();
			int iType = StringB.toInt(cType, TCONN_WEBLOGIC);

			String ds = GetSystemConfig.getBIBMConfig().getAppDataSource();
			String pwd = GetSystemConfig.getBIBMConfig().getAppPWD();
			String url = GetSystemConfig.getBIBMConfig().getAppURL();
			String usr = GetSystemConfig.getBIBMConfig().getAppUser();
			connection = getConnectionFromDS(iType, url, usr, pwd, ds);
			return connection;
			/*
			 * Context initCtx=new InitialContext(); DataSource ds =
			 * (DataSource)initCtx.lookup("java:comp/env/jdbc/"+dataSourceJNDI);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("从连接池中获得数据库连接失败!");
		}
	}
	
	public static Connection getConn2() throws AppException {
		Connection connection = null;
		try {
			String ds = GetSystemConfig.getBIBMConfig().getAppDataSys();
			Context initCtx = new InitialContext();
			DataSource dates = (DataSource) initCtx.lookup("java:comp/env/" + ds);
			return dates.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("从连接池中获得数据库连接失败!");
		}
	}
	
	private static void freeConn(Connection conn) {
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
	 * 插入对象列表
	 *
	 * @param domains
	 * @throws Exception
	 */
	public static <T extends JBTableBase> void insert(List<T> domains) throws Exception {
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		try {
			connection = getConn();
			List result = prepareInsertSQL(domains.get(0));
			String insertSQL = (String) result.get(0);
			Map param = (Map) result.get(1);
			npsmt = new NamedParameterStatement(connection, insertSQL);
			Iterator domainIter = domains.iterator();
			Iterator iter = null;
			T domain = null;
			List params = new ArrayList();
			while (domainIter.hasNext()) {
				domain = (T) domainIter.next();
				params.add(genDomainsParam(domain, true));
			}
			domainIter = params.iterator();
			Map.Entry<String, Object> entry = null;
			while (domainIter.hasNext()) {
				param = (Map) domainIter.next();
				iter = param.entrySet().iterator();
				while (iter.hasNext()) {
					entry = (Map.Entry) iter.next();
					npsmt.setObject(entry.getKey(), entry.getValue());
				}
				npsmt.addBatch();
			}
			npsmt.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
		}
	}

	/**
	 * 插入一个对象
	 *
	 * @param domain
	 * @throws Exception
	 */
	public static <T extends JBTableBase> void insert(T domain) throws Exception {
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		try {
			connection = getConn();
			List result = prepareInsertSQL(domain);
			String insertSQL = (String) result.get(0);
			Map param = (Map) result.get(1);
			npsmt = new NamedParameterStatement(connection, insertSQL);
			Iterator iter = param.entrySet().iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Map.Entry<String, Object> entry = null;
			while (iter.hasNext()) {
				entry = (Map.Entry) iter.next();
				npsmt.setObject(entry.getKey(), entry.getValue());
			}
			npsmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
		}
	}

	private static <T extends JBTableBase> List prepareInsertSQL(T domain) throws Exception {
		List result = new ArrayList();
		Class clazz = domain.getClass();
		Map param = new HashMap();
		StringBuffer insert = new StringBuffer();
		StringBuffer values = new StringBuffer(" VALUES(");
		insert.append("INSERT INTO ")
				.append(AnnotationUtil.getValue(clazz.getAnnotation(TableName.class))).append("(");

		Field[] fields = clazz.getFields();
		if (null != fields) {
			for (Field field : fields) {
				if (null != field.getAnnotation(TableCol.class)) {
					insert.append(" ").append(field.getName()).append(",");
					values.append(":").append(field.getName()).append(",");
					param.put(field.getName(), field.get(domain));
				}
			}
			// append where
			if (insert.indexOf(",") >= 0) {
				insert.deleteCharAt(insert.length() - 1);
			}
			if (values.indexOf(",") >= 0) {
				values.deleteCharAt(values.length() - 1);
			}
			insert.append(" )");
			values.append(" )");
			result.add(insert.append(values).toString());
			result.add(param);
		}
		return result;
	}

	/**
	 * 更新对象列表
	 *
	 * @param domains
	 * @throws Exception
	 */
	public static <T extends JBTableBase> void update(List<T> domains, boolean nullUpdate)
			throws Exception {
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		try {
			connection = getConn();
			List result = prepareUpdateSQL(domains.get(0), nullUpdate);
			String updateSQL = (String) result.get(0);
			Map param = (Map) result.get(1);
			npsmt = new NamedParameterStatement(connection, updateSQL);
			Iterator domainIter = domains.iterator();
			Iterator iter;
			T domain = null;
			List params = new ArrayList();
			while (domainIter.hasNext()) {
				domain = (T) domainIter.next();
				params.add(genDomainsParam(domain, nullUpdate));
			}
			domainIter = params.iterator();
			Map.Entry<String, Object> entry = null;
			while (domainIter.hasNext()) {
				param = (Map) domainIter.next();
				iter = param.entrySet().iterator();

				while (iter.hasNext()) {
					entry = (Map.Entry) iter.next();
					npsmt.setObject(entry.getKey(), entry.getValue());
				}
				npsmt.addBatch();
			}
			npsmt.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
		}
	}

	private static <T extends JBTableBase> Map genDomainsParam(T domain, boolean nullUpdate)
			throws Exception {
		Map param = new HashMap();
		Class clazz = domain.getClass();
		Field[] fields = clazz.getFields();
		if (null != fields) {
			for (Field field : fields) {
				if (null != field.getAnnotation(TableCol.class)) {
					if (!nullUpdate && null != field.get(domain)
							&& !StringUtils.isBlank(field.get(domain).toString())) {
						param.put(field.getName(), field.get(domain));
					} else if (nullUpdate) {
						param.put(field.getName(), field.get(domain));
					}
				}
			}
		}
		return param;
	}

	private static <T extends JBTableBase> List prepareUpdateSQL(T domain, boolean nullUpdate)
			throws Exception {
		List result = new ArrayList();
		Class clazz = domain.getClass();
		Map param = new HashMap();
		StringBuffer updateSQL = new StringBuffer();
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		updateSQL.append("UPDATE ")
				.append(AnnotationUtil.getValue(clazz.getAnnotation(TableName.class)))
				.append(" SET ");
		Field[] fields = clazz.getFields();
		String filedCol = null;
		if (null != fields) {
			for (Field field : fields) {
				if (null != field.getAnnotation(TableCol.class)) {
					filedCol = (String) AnnotationUtil
							.getValue(field.getAnnotation(TableCol.class));
					if (!nullUpdate && null != field.get(domain)
							&& !StringUtils.isBlank(field.get(domain).toString())) {
						updateSQL.append(" ").append(filedCol).append("=:").append(field.getName())
								.append(",");
						param.put(field.getName(), field.get(domain));
					} else if (nullUpdate) {
						updateSQL.append(" ").append(filedCol).append("=:").append(field.getName())
								.append(",");
						param.put(field.getName(), field.get(domain));
					}
					if (null != field.getAnnotation(PriKey.class)) {
						where.append(" AND ").append(filedCol).append("=:").append(field.getName());
						param.put(field.getName(), field.get(domain));
					}
				}
			}
			// append where
			if (updateSQL.indexOf(",") >= 0) {
				updateSQL.deleteCharAt(updateSQL.length() - 1);
			}
			result.add(updateSQL.append(where).toString());
			result.add(param);
		}
		return result;
	}

	/**
	 * 更新对象，需要设置了标注
	 *
	 * @param domain
	 * @throws Exception
	 */
	public static <T extends JBTableBase> void update(T domain, boolean nullUpdate)
			throws Exception {
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		try {
			connection = getConn();
			List result = prepareUpdateSQL(domain, nullUpdate);
			String updateSQL = (String) result.get(0);
			Map param = (Map) result.get(1);
			npsmt = new NamedParameterStatement(connection, updateSQL);
			Iterator iter = param.entrySet().iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Map.Entry<String, Object> entry = null;
			while (iter.hasNext()) {
				entry = (Map.Entry) iter.next();
				npsmt.setObject(entry.getKey(), entry.getValue());
			}
			npsmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
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
	public static Vector execQryVector(String strSql, String strDef) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				Vector v = dbutil.executeQueryGetStringVector(strSql, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}

	public static <T extends JBTableBase> List<T> find(Class<T> clazz, String strSql, List parameter)
			throws Exception {
		return find(clazz, strSql, parameter, "");
	}

	/**
	 *
	 * @param clazz
	 * @param strSql
	 * @param parameter
	 * @param strDefalut
	 * @return
	 * @throws Exception
	 */
	public static <T extends JBTableBase> List<T> find(Class<T> clazz, String strSql,
			List parameter, String strDefalut) throws Exception {
		List<T> result = null;
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			connection = getConn();
			psmt = connection.prepareStatement(strSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			Iterator iter = parameter.iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Object obj = null;
			int i = 0;
			while (iter.hasNext()) {
				obj = iter.next();
				psmt.setObject(++i, obj);
			}
			rs = psmt.executeQuery();
			result = ResultUtil.transResultToObjList(clazz, rs, strDefalut);
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != rs) {
				rs.close();
			}

			if (null != psmt) {
				psmt.close();
			}
			freeConn(connection);
		}
		return result;
	}

	public static <T extends JBTableBase> List<T> find(Class<T> clazz, String strSql, Map parameter)
			throws Exception {
		return find(clazz, strSql, parameter, "");
	}

	/**
	 * @param strSql
	 * @param parameter
	 * @param strDefalut
	 *            返回值为null时，默认字符串
	 * @return
	 */
	public static <T extends JBTableBase> List<T> find(Class<T> clazz, String strSql,
			Map parameter, String strDefalut) throws Exception {
		List<T> result = null;
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		ResultSet rs = null;
		try {
			connection = getConn();
			npsmt = new NamedParameterStatement(connection, strSql);
			Map indexs = npsmt.getIndexMap();
			Iterator iter = indexs.entrySet().iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Map.Entry entry = null;
			while (iter.hasNext()) {
				entry = (Map.Entry) iter.next();
				npsmt.setObject((String) entry.getKey(), parameter.get(entry.getKey()));
			}
			rs = npsmt.executeQuery();
			result = ResultUtil.transResultToObjList(clazz, rs, strDefalut);
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != rs) {
				rs.close();
			}

			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
		}
		return result;
	}

	public static String[][] execQuery(String strSql, List parameter) throws Exception {
		return execQuery(strSql, parameter, "");
	}

	/**
	 * 实现查询，支持问号顺序
	 *
	 * @param strSql
	 * @param parameter
	 * @param strDef
	 * @return
	 * @throws Exception
	 */
	public static String[][] execQuery(String strSql, List parameter, String strDef)
			throws Exception {
		String[][] v = null;
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			connection = getConn();
			psmt = connection.prepareStatement(strSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			Iterator iter = parameter.iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Object obj = null;
			int i = 0;
			while (iter.hasNext()) {
				obj = iter.next();
				psmt.setObject(++i, obj);
			}
			rs = psmt.executeQuery();
			v = ResultUtil.transResultToArray(rs, strDef);
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != rs) {
				rs.close();
			}

			if (null != psmt) {
				psmt.close();
			}
			freeConn(connection);
		}
		return v;
	}

	/**
	 * 使用preparestatement 替换传入参数，以便利用数据库缓存
	 *
	 * @param strSql
	 *            此处 SQL必须是以:别名+空格为
	 * @param parameter
	 * @param strDef
	 *            默认字符串
	 * @return
	 */
	public static String[][] execQuery(String strSql, Map parameter, String strDef)
			throws Exception {
		String[][] v = null;
		Connection connection = null;
		NamedParameterStatement npsmt = null;
		ResultSet rs = null;
		try {
			connection = getConn();
			npsmt = new NamedParameterStatement(connection, strSql);
			Map indexs = npsmt.getIndexMap();
			Iterator iter = indexs.entrySet().iterator();
			// 开始设置参数，如果分析的某个参数在传入中没有，则抛异常
			Map.Entry entry = null;
			while (iter.hasNext()) {
				entry = (Map.Entry) iter.next();
				npsmt.setObject((String) entry.getKey(), parameter.get(entry.getKey()));
			}
			rs = npsmt.executeQuery();
			v = ResultUtil.transResultToArray(rs, strDef);
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != rs) {
				rs.close();
			}

			if (null != npsmt) {
				npsmt.close();
			}
			freeConn(connection);
		}
		return v;
	}

	public static String[][] execQuery(String strSql, Map parameter) throws Exception {
		return execQuery(strSql, parameter, "");
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
	public static String[][] execQryArray(String strSql, String strDef) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				String[][] v = dbutil.executeQueryGetArray(strSql, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
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
	public String[][] execQryArray(String strSql, String strDef, int offset, int length)
			throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);

				String sqlcount = "select count(*) from (" + strSql + ")";

				System.out.println("sqlcount=" + sqlcount);
				String[][] vCount = dbutil.executeQueryGetArray(sqlcount, strDef);
				if (vCount != null) {
					setTotalCount(Integer.parseInt(vCount[0][0]));
				}
				if (offset >= 0 && length > 0) {
					strSql = "select * from (" + strSql + ") where rownum<=" + (offset*length)
							+ " minus " + "select * from (" + strSql + ") where rownum<=" + (offset-1)*length;
				}
				System.out.println("strSql=" + strSql);
				String[][] v = dbutil.executeQueryGetArray(strSql, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}

	public static String[][] execQryArray(String strSql) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				String[][] v = dbutil.executeQueryGetArray(strSql, "");
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}
	
	public static List execQryArrayMapSys(String strSql, String[] where) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn2();
				DBUtil dbutil = new DBUtil(connection);
				List list = dbutil.executeQueryGetArrayMap(strSql, where);
				dbutil.closeConnection();
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}
	
	public static List execQryArrayMap(String strSql, String[] where) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				List list = dbutil.executeQueryGetArrayMap(strSql, where);
				dbutil.closeConnection();
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
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
	public static String[][] execQryArray(String strSql, String[] where, String strDef)
			throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				String[][] v = dbutil.executeQueryGetArray(strSql, where, strDef);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
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
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				int v = dbutil.executeUpdate(strSql);
				dbutil.closeConnection();
				return v;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
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
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				ret = dbutil.execTransUpdate(strSqls);
				dbutil.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
		return ret;
	}

	/**
	 * 根据DataSource的JNDI名字，在weblogicJNDI（连接池）中获得一个连接，用户获得这个连接后可以纪行
	 * 事务等相关的操作，注意:!!!!! 要在使用完成后释放该连接
	 *
	 * @param url
	 *            访问的url, 如： "t3://localhost:7001", 默认为null
	 * @param user
	 *            管理用户名，如："system", 默认为null
	 * @param pwd
	 *            管理用户的密码, 默认为null
	 * @param strDS
	 *            DataSource的jndi名字或者对于connType为Single时厚为dirverName
	 * @return 获得成功的连接， 如果失败则返回空
	 */
	public static Connection getConnectionFromDS(int connType, String url, String user, String pwd,
			String strDS) throws AppException {
		switch (connType) {
		case TCONN_SINGLE:
			return getConnFromSingle(strDS, url, user, pwd);
		case TCONN_WEBLOGIC:
			return getConnFromWLS(url, user, pwd, strDS);
		case TCONN_TOMCAT:
			return getConnFromTomcat(strDS);
		case TCONN_WEBSPHERE:
			return getConnFromWebshere(url, user, pwd, strDS);
		default:
			throw new AppException("Fatal Error: 目前系统不支持使用此方式获取连接!");
		}
	}

	private static Connection getConnFromTomcat(String strDS) throws AppException {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/" + strDS);
			return ds.getConnection();
		} catch (Exception ex) {
			throw new AppException("Fatal Error: 从连接池中获取连接失败，请查看系统配置文件中tomcat的连接池相关信息是否正确配置和使用!["
					+ ex.toString() + "]");
		}

	}

	private static Connection getConnFromWebshere(String url, String user, String pwd, String strDS)
			throws AppException {
		try {
			Context ctx = new InitialContext();
			if (ctx == null) {
				throw new Exception("Boom - No Context");
			}
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/" + strDS);
			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception ex) {
			System.out.println(ex);
			throw new AppException("Fatal Error: 从连接池中获取连接失败，请查看系统配置文件中webshere的连接池相关信息是否正确配置和使用!["
					+ ex.toString() + "]");
		}

	}

	/**
	 * 直接获取一个连接，不通过连接池
	 *
	 * @param dbDriver
	 *            如： ORACLE oracle.jdbc.driver.OracleDriver SQLSERVER
	 *            com.microsoft.jdbc.sqlserver.SQLServerDriver MYSQL
	 *            org.gjt.mm.mysql.Driver DB2 com.ibm.db2.jdbc.net.DB2Driver
	 *            SYBASE com.sybase.jdbc2.jdbc.SybDriver Access
	 *            sun.jdbc.odbc.JdbcOdbcDriver ODBC sun.jdbc.odbc.JdbcOdbcDriver
	 * @param dbServer
	 *            ORACLE jdbc:oracle:thin:@oracleIP:oraclePort:ORA-SID i.e.
	 *            jdbc:oracle:thin:@172.18.99.253:1521:oracle9 SQLSERVER
	 *            jdbc:microsoft
	 *            :sqlserver://sqlserverip:port;DatabaseName=dbname i.e.
	 *            jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=pubs
	 *            MYSQL jdbc:mysql://MyDbComputerNameOrIP:port/myDatabaseName
	 *            i.e. jdbc:mysql://192.168.0.16/SUBRDB DB2
	 *            jdbc:db2://MyDbComputerNameOrIP:port/myDatabaseName i.e.
	 *            jdbc:db2://localhost:6789/SAMPLE SYBASE
	 *            jdbc:sybase:Tds:MyDbComputerNameOrIP:2638 i.e Access
	 *            jdbc:odbc:driver={Microsoft Access Driver
	 *            (*.mdb)};DBQ=mdbFileN ODBC jdbc:odbc:"+sDsn
	 * @param dbLogin
	 * @param dbPassword
	 * @return
	 * @throws AppException
	 */
	private static Connection getConnFromSingle(String dbDriver, String dbServer, String dbLogin,
			String dbPassword) throws AppException {
		Connection connection = null;
		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbServer, dbLogin, dbPassword);
			return connection;
		} catch (Exception e) {
			throw new AppException("从连接池中获得数据库连接失败!");
		}
	}

	private static Connection getConnFromWLS(String url, String user, String pwd, String strDS)
			throws AppException {
		Connection conn = null;
		if (url == null) {
			url = "t3://localhost:7001";
		}
		try {
			Hashtable env = new Hashtable();
			env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			env.put(InitialContext.PROVIDER_URL, url);
			if (user != null && !"".equals(user) && user.trim().length() > 1) {
				env.put(InitialContext.SECURITY_PRINCIPAL, user);
				env.put(InitialContext.SECURITY_CREDENTIALS, pwd);
			}
			Context ctx = new InitialContext(env);
			DataSource ds = (DataSource) ctx.lookup(strDS);
			conn = ds.getConnection();
			return conn;
		} catch (Exception ex) {
			throw new AppException("Fatal Error: 从连接池中获取连接失败，请查看系统配置文件中weblogic的连接池相关信息是否正确配置和使用!["
					+ ex.toString() + "]");
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
	public static int execUpdate(Connection conn, String sql) throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException("DBUtil.executeUpdate:数据库连接为空，请先获取数据库连接connection");
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
	public static int execUpdates(Connection conn, String[] sqls) throws AppException {
		int ret = AppConst.ERRORRESULT;
		Statement stmt = null;
		try {
			if (conn == null)
				throw new AppException("DBUtil.executeUpdates:数据库连接为空，请先获取数据库连接connection");
			conn.setAutoCommit(false);
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
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex1) {
				ex1.printStackTrace();
			}
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
	 * 执行查询返回Array结果,是二维结果
	 *
	 * @param strSql
	 *            SQL语句
	 * @param strDefault
	 *            如果返回结果的字段值为空时的替换串,通常为""
	 * @return 查询到的结果集
	 * @throws Exception
	 */
	public static String[][] execQryArray(Connection conn, String strSql, String strDefault)
			throws AppException {
		try {
			if (conn == null)
				throw new AppException("DBUtil.executeQryArray:数据库连接为空，请先获取数据库连接connection");
			DBUtil dbutil = new DBUtil(conn);
			String[][] strTemp = dbutil.executeQueryGetArray(strSql, strDefault);
			dbutil.closeConnection();
			return strTemp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("DBUtil.executeQryArray:exception;[" + e.getMessage() + "]");
		} finally {
			freeConn(conn);
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
	public static Vector execQryVector(Connection conn, String strSql, String strDefault)
			throws AppException {
		try {
			if (conn == null)
				throw new AppException("DBUtil.executeQryVector:数据库连接为空，请先获取数据库连接connection");
			DBUtil dbutil = new DBUtil(conn);
			Vector v = dbutil.executeQueryGetStringVector(strSql, strDefault);
			dbutil.closeConnection();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("DBUtil.executeQryVector:exception;[" + e.getMessage() + "]");
		} finally {
			freeConn(conn);
		}
	}

	/**
	 * 更新表的long raw字段
	 *
	 * @param sql
	 * @param content
	 */
	public static boolean execUpdateLongRawField(Connection conn, String sql, byte[] content)
			throws AppException {
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

	/**
	 * 查找满足条件的第一行long raw字段 格式: SELECT longrawfield FROM ttt WHERE ．．．
	 *
	 * @param conn
	 * @param sql
	 * @return
	 * @throws AppException
	 */
	public static byte[] getLongRawField(Connection conn, String sql) throws AppException {
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
			throw new AppException("IO查找Long Raw字段失败!原因[" + ioe.toString() + "]");
		} catch (SQLException ex) {
			throw new AppException("SQL查找Long Raw字段失败!原因[" + ex.toString() + "]");
		}
	}

	public static void main(String[] args) {
		try {
			String[][] res = execQryArray("SELECT table_name FROM user_tables", "");
			if (res == null)
				System.out.println("res is null");
			else {
				for (int i = 0; i < res.length; i++) {
					System.out.println("" + res[i][0]);
				}
			}
		} catch (AppException ex) {
			System.out.println("ex=" + ex.getMessage());
		}
	}

	public static InputStream readBlogContent(String keyFld, String blobFld, String tablename,
			String keyId) {
		InputStream ins = null;
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				ins = dbutil.readBlogContent(keyFld, blobFld, tablename, keyId);
				dbutil.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ins;
	}

	public static List getBeans_withSortSupport_limit_general(int offset, int rowsToReturn,
			String sql) {

		// System.out.println(offset + ":" + rowsToReturn);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List retValue = new ArrayList();

		try {
			try {
				connection = getConn();
			} catch (AppException e) {

				e.printStackTrace();
			}
			statement = connection.prepareStatement(sql.toString(),
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			statement.setMaxRows(offset + rowsToReturn);
			try {
				statement.setFetchSize(Math.min(rowsToReturn, AppConst.MAX_FETCH_SIZE));
			} catch (SQLException sqle) {
				// do nothing, postgreSQL does not support this method
			}
			resultSet = statement.executeQuery();
			boolean loop = resultSet.absolute(offset + 1);// the absolute method
															// begin with 1
															// instead of 0 as
															// in the LIMIT
															// clause
			while (loop) {
				String[] columnName = DataAccessUtils.getColumnName(resultSet);
				// retValue=new ArrayList();
				Map record = null;

				record = new HashMap();
				DataAccessUtils.buildRecord(resultSet, columnName, record);
				retValue.add(record);

				if (retValue.size() == rowsToReturn)
					break;// Fix the Sybase bug
				loop = resultSet.next();
			}

			return retValue;
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}

			if (statement != null) {
				try {
					statement.setMaxRows(0); // reset to the default value
				} catch (SQLException e) {
					e.printStackTrace();

				}

				try {
					statement.setFetchSize(0); // reset to the default value
				} catch (SQLException sqle) {
					// do nothing, postgreSQL does not support this method
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

			freeConn(connection);

		}
		return retValue;
	}

	public static List getBeans_withSortSupport_limit_general(String sql) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List retValue = new ArrayList();

		try {
			try {
				connection = getConn();
			} catch (AppException e) {

				e.printStackTrace();
			}
			statement = connection.prepareStatement(sql.toString(),
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			resultSet = statement.executeQuery();

			if (resultSet != null) {

				String[] columnName = DataAccessUtils.getColumnName(resultSet);
				retValue = new ArrayList();
				Map record = null;
				while (resultSet.next()) {
					record = new HashMap();
					DataAccessUtils.buildRecord(resultSet, columnName, record);
					// record.put("id", resultSet.getString("id"));
					// record.put("flag", resultSet.getString("flag"));

					retValue.add(record);
				}
			}

			return retValue;
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}

			if (statement != null) {
				try {
					statement.setMaxRows(0); // reset to the default value
				} catch (SQLException e) {
					e.printStackTrace();

				}

				try {
					statement.setFetchSize(0); // reset to the default value
				} catch (SQLException sqle) {
					// do nothing, postgreSQL does not support this method
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

			freeConn(connection);

		}
		return retValue;
	}

	public static String getSingleValue(String sql) throws SQLException {
		Statement st = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (java.sql.SQLException e) {
			conn.rollback();
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return "";
	}
	
	/**
	 * 查询List<Map>结果集
	 * @param strSql
	 * @param where
	 * @return
	 * @throws AppException
	 */
	public static List execQryArrayMapSys2(String strSql, String[] where) throws AppException {
		try {
			Connection connection = null;
			try {
				connection = getConn();
				DBUtil dbutil = new DBUtil(connection);
				List list = dbutil.executeQueryGetArrayMap(strSql, where);
				dbutil.closeConnection();
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}
	//BES获取数据源
	public static Connection getConnPro(String datasource) throws AppException {
		Connection connection = null;
		Context initCtx = null;
		try {
		    initCtx = new InitialContext(); 
			//DataSource dates = (DataSource) initCtx.lookup("java:comp/env/" + datasource);
			System.out.println("lookUp:"+datasource);
		    DataSource dates = (DataSource) initCtx.lookup(datasource);
		    System.out.println("lookUp ok:"+datasource);
		    connection =  dates.getConnection();
		    System.out.println("conn:"+connection);
		    return connection;
		} catch (Exception e) {
			throw new AppException("从连接池中获得数据库连接失败!"+ e.toString());
		}finally{
            try {
                if(initCtx != null){
                    initCtx.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public static List execQryArrayMapPro(String strSql, String[] where,String datasource) throws AppException {
	    System.out.println("//////////////webUtil://///////////////////");
	    System.out.println("sql:"+strSql);
	    System.out.println("datasource:"+datasource);
	    for (String string : where) {
            System.out.println("参数值:"+string);
        }
		try {
			Connection connection = null;
			try {
				connection = getConnPro(datasource);
				DBUtil dbutil = new DBUtil(connection);
				List list = dbutil.executeQueryGetArrayMap(strSql, where);
				dbutil.closeConnection();
				System.out.println("查询OK");
				String usercount = "0";
		        String sumfee = "0.00";
		        String amount = "0.00";
		        String feezhan = "0%";
		        DecimalFormat df = new DecimalFormat("0.00");
				for (int i = 0; i < list.size(); i++) {
	                Map result = (Map) list.get(0);
	                usercount = (String) result.get("USERCOUNT");
	                amount = (String) result.get("AMOUNT");
	                sumfee = (String) result.get("SUMFEE");
	                if (amount.equals("0.00")) {
	                    feezhan = "+∞";
	                } else {
	                    Double zhanbi = Double.parseDouble(sumfee) / Double.parseDouble(amount) * 100;
	                    feezhan = df.format(zhanbi) + "%";
	                }
	                System.out.println("usercount:"+usercount);
	                System.out.println("sumfee:"+usercount);
	                System.out.println("amount:"+usercount);
	            }
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				freeConn(connection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}
}