package com.ailk.bi.connect;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings({ "rawtypes" })
public class BackConnectors {
	public static final byte DEBUG_MODE = 0x01;

	public static final byte DEVELOP_MODE = 0x02;

	private static byte defaultMode = 0;

	private static ConnectorType defaultConnector = ConnectorType.TUX_CONNECTOR;

	private static boolean initComplete = false;

	private BackConnectors() {

	}

	public static void setDefaultDebugMode(boolean mode) {
		if (mode) {
			defaultMode |= DEBUG_MODE;
		} else {
			defaultMode &= ~DEBUG_MODE;
		}
	}

	public static void setDefaultDevelopMode(boolean mode) {
		if (mode) {
			defaultMode |= DEVELOP_MODE;
		} else {
			defaultMode &= ~DEVELOP_MODE;
		}
	}

	public static void setDefaultConnectType(ConnectorType connectorType) {
		defaultConnector = connectorType;
	}

	private static void initialize() {

		// 设定默认配置
		boolean mode = Boolean.valueOf(System.getProperty("connect.debug"))
				.booleanValue();
		setDefaultDebugMode(mode);

		mode = Boolean.valueOf(System.getProperty("connect.develop"))
				.booleanValue();
		setDefaultDevelopMode(mode);

		ConnectorType connectType = ConnectorType.getInstance(System
				.getProperty("connect.type"));
		if (connectType != null)
			setDefaultConnectType(connectType);
	}

	private static BackConnector newInstance(String connName)
			throws ConnectException {
		Class connClass = null;
		BackConnector backConnector = null;

		try {
			connClass = Class.forName("com.asiabi.connect.TuxConnector");
			backConnector = (BackConnector) connClass.newInstance();
		} catch (Exception e) {
			throw new ConnectException();
		}
		return backConnector;
	}

	public static BackConnector getDefaultConnector(byte mode)
			throws ConnectException {
		BackConnector connector = null;

		if (!initComplete) {
			initialize();
			initComplete = true;
		}

		if (defaultConnector == ConnectorType.TUX_CONNECTOR) {
			connector = (BackConnector) getTuxConnector(mode);
		} else if (defaultConnector == ConnectorType.CICS_CONNECTOR) {
			connector = (BackConnector) getCicsConnector(mode);
		} else if (defaultConnector == ConnectorType.TONG_CONNECTOR) {
			connector = (BackConnector) getTongConnector(mode);
		}

		return connector;
	}

	public static BackConnector getDefaultConnector() throws ConnectException {
		return getDefaultConnector(defaultMode);
	}

	public static BackConnector getCicsConnector(byte mode)
			throws ConnectException {
		return newInstance("CicsConnector");
	}

	public static BackConnector getCicsConnector() throws ConnectException {
		return getCicsConnector(defaultMode);
	}

	public static BackConnector getTuxConnector(byte mode)
			throws ConnectException {
		return newInstance("TuxConnector");
	}

	public static BackConnector getTuxConnector() throws ConnectException {
		return getTuxConnector(defaultMode);
	}

	public static BackConnector getTongConnector(byte mode)
			throws ConnectException {
		return newInstance("TongConnector");
	}

	public static BackConnector getTongConnector() throws ConnectException {
		return getTongConnector(defaultMode);
	}
}