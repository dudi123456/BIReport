package com.fins.gt.dataaccess;

import java.sql.Connection;

public interface IDataBaseManager {

	// public void setDriver(String driver);

	// public void startServer();

	public void setURL(String url);

	public void setUser(String user);

	public void setPassword(String password);

	public void setMaxConnections(int max);

	public void dispose();

	public Connection getConnection();

}
