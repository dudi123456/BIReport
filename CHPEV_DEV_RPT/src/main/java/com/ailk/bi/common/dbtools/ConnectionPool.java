package com.ailk.bi.common.dbtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义的数据库连接池
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ConnectionPool implements Runnable {

	/**
	 * 初始化连接池
	 * 
	 * @param driverName
	 *            driver name
	 * @param dbURL
	 *            database'driverName url
	 * @param usr
	 *            database'driverName username
	 * @param pwd
	 *            database'driverName password
	 * @param minConn
	 *            database min connection count
	 * @param maxConn
	 *            database max connection count
	 * @param dbLogPath
	 *            database log path
	 * @param refreshInterval
	 *            更新时间 default 15 minuters
	 * @throws IOException
	 */
	public ConnectionPool(String driverName, String dbURL, String usr,
			String pwd, int minConn, int maxConn, String dbLogPath,
			double refreshInterval) throws IOException {
		available = true;
		connPool = new Connection[maxConn];
		connStatus = new int[maxConn];
		connLockTime = new long[maxConn];
		connCreateDate = new long[maxConn];
		connID = new String[maxConn];
		currConnections = minConn;
		maxConns = maxConn;
		dbDriver = driverName;
		dbServer = dbURL;
		dbLogin = usr;
		dbPassword = pwd;
		logFileString = dbLogPath;
		maxConnMSec = (int) (refreshInterval * 86400000D);
		if (maxConnMSec < 30000)
			maxConnMSec = 30000;
		File file = null;
		String sentDate = "";
		try {
			java.util.Date dd = new java.util.Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			sentDate = formatter.format(dd);
			dbLogPath = "DBPool_LOG." + sentDate;
			file = new File(logFileString);
			if (file.exists() == false)
				file.mkdirs();
			file = new File(file, dbLogPath + ".log");

			log = new PrintWriter(new FileOutputStream(file), true);
			log_r = new BufferedReader(new FileReader(file));

		} catch (IOException _ex) {
			try {
				file = new File(file, dbLogPath + ".log");
				log = new PrintWriter(new FileOutputStream(file), true);
				log_r = new BufferedReader(new FileReader(file));
			} catch (IOException _ex2) {
				throw new IOException("Can't open any log file");
			}
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
		Date date = new Date();
		pid = simpledateformat.format(date);
		File filepid = new File(logFileString, "pid");
		BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(
				filepid)); // logFileString + "pid"));
		bufferedwriter.write(pid);
		bufferedwriter.close();

		log.println("Starting ConnPool Version 1.0: Creating by King");
		log.println("dbDriver = " + driverName);
		log.println("dbServer = " + dbURL);
		log.println("dbLogin = " + usr);
		log.println("log file = " + dbLogPath);
		log.println("minconnections = " + minConn);
		log.println("maxconnections = " + maxConn);
		log.println("Total refresh interval = " + refreshInterval + " days");
		log.println("-----------------------------------------");
		boolean flag = false;
		byte byte0 = 20;
		try {
			for (int k = 1; k < byte0;)
				try {
					for (int l = 0; l < currConnections; l++) {
						createConn(l);
					}

					flag = true;
					break;
				} catch (SQLException sqlexception) {
					log.println("--->Attempt ("
							+ String.valueOf(k)
							+ " of "
							+ String.valueOf(byte0)
							+ ") failed to create new connections set at startup: ");
					log.println("    " + sqlexception);
					log.println("    Will try again in 15 seconds...");
					try {
						Thread.sleep(15000L);
					} catch (InterruptedException _ex) {
					}
					k++;
				}

			if (!flag) {
				log.println("\r\nAll attempts at connecting to Database exhausted");
				throw new IOException();
			}
		} catch (Exception _ex) {
			throw new IOException();
		}
		runner = new Thread(this);
		runner.start();
	}

	/**
	 * Reset mix connection count
	 * 
	 * @param mix
	 */
	public void setMixConnection(int mix) {
		minConns = mix;
		log.println(new Date().toString() + "  Mix connection count "
				+ String.valueOf(minConns) + " " + minConns);
	}

	/**
	 * Reset max connection count
	 * 
	 * @param max
	 */
	public void setMaxConnection(int max) {
		maxConns = max;
		log.println(new Date().toString() + "  Max connection count "
				+ String.valueOf(maxConns) + " " + maxConns);
	}

	protected BufferedReader log_r = null;

	/**
	 * Read log info
	 * 
	 * @return
	 */
	public String read() {

		StringBuffer buf = new StringBuffer();
		try {
			String tmp = log_r.readLine();
			while (tmp != null) {
				buf.append(tmp + "\n");
				tmp = log_r.readLine();
			}
		} catch (Exception e) {
			// Print a error message
			System.err.println("LoggerFile.read(BufferedReader):"
					+ e.toString());
		} finally {

		}
		return buf.toString();
	}

	/**
	 * Implement Thread.run() method
	 */
	public void run() {
		boolean flag = true;
		Statement statement = null;
		while (flag) {
			try {
				File file = new File(logFileString, "pid");
				BufferedReader bufferedreader = new BufferedReader(
						new FileReader(file)); // logFileString + "pid"));
				String s = bufferedreader.readLine();
				if (!s.equals(pid)) {
					log.close();
					for (int k = 0; k < currConnections; k++)
						try {
							connPool[k].close();
						} catch (SQLException _ex) {
						}
					bufferedreader.close();
					return;
				}
				bufferedreader.close();
			} catch (IOException _ex) {
				log.println("Can't read the file for pid info: "
						+ logFileString + "pid");
			}
			for (int i = 0; i < currConnections; i++)
				try {
					currSQLWarning = connPool[i].getWarnings();
					if (currSQLWarning != null) {
						log.println("Warnings on connection "
								+ String.valueOf(i) + " " + currSQLWarning);
						connPool[i].clearWarnings();
					}
				} catch (SQLException sqlexception) {
					log.println("Cannot access Warnings: " + sqlexception);
				}

			for (int j = 0; j < currConnections; j++) {
				long l = System.currentTimeMillis() - connCreateDate[j];
				// (connStatus)
				// {
				if (connStatus[j] > 0) {
					continue;
				}
				connStatus[j] = 2;
				// }
				try {
					if (l > (long) maxConnMSec)
						throw new SQLException();
					statement = connPool[j].createStatement();
					connStatus[j] = 0;
					if (connPool[j].isClosed())
						throw new SQLException();
				} catch (SQLException _ex) {
					try {
						log.println((new java.util.Date()).toString()
								+ " ***** Recycling connection "
								+ String.valueOf(j) + ":");
						connPool[j].close();
						createConn(j);
					} catch (SQLException sqlexception1) {
						log.println("Failed: " + sqlexception1);
						connStatus[j] = 0;
					}
				} finally {
					try {
						if (statement != null)
							statement.close();
					} catch (SQLException _ex) {
					}
				}
			}

			try {
				Thread.sleep(20000L);
			} catch (InterruptedException _ex) {
				return;
			}
		}

	}

	/**
	 * Get connection from pool
	 * 
	 * @return the database connection
	 */
	public Connection getConnection() {
		Connection connection = null;
		if (available) {
			boolean flag = false;
			for (int i = 1; i <= 10; i++) {
				try {
					int j = 0;
					int k = connLast + 1;
					if (k >= currConnections)
						k = 0;
					do
					// (connStatus)
					{
						if (connStatus[k] < 1 && !connPool[k].isClosed()) {
							connection = connPool[k];
							connStatus[k] = 1;
							connLockTime[k] = System.currentTimeMillis();
							connLast = k;
							flag = true;
							break;
						}
						j++;
						if (++k >= currConnections)
							k = 0;
					} while (!flag && j < currConnections);
				} catch (SQLException _ex) {
				}
				if (flag)
					break;
				// (this)
				// {
				if (currConnections < maxConns)
					try {
						createConn(currConnections);
						currConnections++;
					} catch (SQLException sqlexception) {
						log.println("Unable to create new connection: "
								+ sqlexception);
					}
				// }
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException _ex) {
				}
				log.println("-----> Connections Exhausted !  Will wait and try again in loop "
						+ String.valueOf(i));
			}

		} else {
			log.println("Unsuccessful getConnection() request during destroy()");
		}
		return connection;
	}

	/**
	 * Get the index of conneciton
	 * 
	 * @param connection
	 * @return the connection position
	 */
	public int idOfConnection(Connection connection) {
		String s;
		try {
			s = ((Object) connection).toString();
		} catch (NullPointerException _ex) {
			s = "none";
		}
		int i = -1;
		for (int j = 0; j < currConnections; j++) {
			if (!connID[j].equals(s))
				continue;
			i = j;
			break;
		}

		return i;
	}

	/**
	 * Recycle the connection into connection pool
	 * 
	 * @param connection
	 *            the currrent connection
	 * @return
	 */
	public String freeConnection(Connection connection) {
		String s = "";
		int i = idOfConnection(connection);
		if (i >= 0) {
			connStatus[i] = 0;
			s = "freed " + ((Object) connection).toString();
		} else {
			log.println("----> Could not free connection!!!");
		}
		return s;
	}

	/**
	 * Get connection time long
	 * 
	 * @param connection
	 * @return
	 */
	public long getAge(Connection connection) {
		int i = idOfConnection(connection);
		return System.currentTimeMillis() - connLockTime[i];
	}

	/**
	 * Create database connection
	 * 
	 * @param i
	 * @throws SQLException
	 */
	private void createConn(int i) throws SQLException {
		java.util.Date date = new java.util.Date();
		try {
			Class.forName(dbDriver);

			connPool[i] = DriverManager.getConnection(dbServer, dbLogin,
					dbPassword);
			connStatus[i] = 0;
			connID[i] = ((Object) connPool[i]).toString();
			connLockTime[i] = 0L;
			connCreateDate[i] = date.getTime();

		} catch (ClassNotFoundException _ex) {
			System.out.println("driver can not ne loading");
			_ex.printStackTrace();
		}
		log.println(date.toString() + "  Opening connection "
				+ String.valueOf(i) + " " + i); // connPool[i].toString() +
		// ":");
	}

	/**
	 * Close the specify connection
	 * 
	 * @param i
	 * @throws SQLException
	 */
	public void destroy(int i) throws SQLException {
		available = false;
		runner.interrupt();
		try {
			runner.join(i);
		} catch (InterruptedException _ex) {
		}
		int j;
		for (long l = System.currentTimeMillis(); (j = getUseCount()) > 0
				&& System.currentTimeMillis() - l <= (long) i;)
			try {
				Thread.sleep(500L);
			} catch (InterruptedException _ex) {
			}

		for (int k = 0; k < currConnections; k++)
			try {
				connPool[k].close();
			} catch (SQLException _ex) {
				log.println("Cannot close connections on Destroy");
			}

		if (j > 0) {
			String s = "Unsafe shutdown: Had to close " + j
					+ " active DB connections after " + i + "ms";
			log.println(s);
			log.close();
			throw new SQLException(s);
		} else {
			log.close();
			return;
		}
	}

	public void destroy() {
		try {
			destroy(10000);
			return;
		} catch (SQLException _ex) {
			return;
		}
	}

	/**
	 * Get current using connection count
	 * 
	 * @return
	 */
	public int getUseCount() {
		int i = 0;
		// (connStatus)
		// {
		for (int j = 0; j < currConnections; j++)
			if (connStatus[j] > 0)
				i++;

		// }
		return i;
	}

	/**
	 * Get current pool count
	 * 
	 * @return
	 */
	public int getSize() {
		return currConnections;
	}

	private Thread runner;

	private Connection connPool[];

	private int connStatus[];

	private long connLockTime[];

	private long connCreateDate[];

	private String connID[];

	private String dbDriver;

	private String dbServer;

	private String dbLogin;

	private String dbPassword;

	private String logFileString;

	private int currConnections;

	private int connLast;

	private int minConns;

	private int maxConns;

	private int maxConnMSec;

	private boolean available;

	private PrintWriter log;

	private SQLWarning currSQLWarning;

	private String pid;

}