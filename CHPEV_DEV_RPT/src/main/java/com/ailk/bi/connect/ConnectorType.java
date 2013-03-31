package com.ailk.bi.connect;

public class ConnectorType {
	private String name;

	private ConnectorType(String str) {
		name = str;
	}

	public static ConnectorType getInstance(String str) {
		if (str == null)
			return null;
		if (str.equalsIgnoreCase("tuxedo")) {
			return TUX_CONNECTOR;
		} else if (str.equalsIgnoreCase("cics")) {
			return CICS_CONNECTOR;
		} else if (str.equalsIgnoreCase("tong")) {
			return TONG_CONNECTOR;
		}

		return null;
	}

	public String toString() {
		return name;
	}

	public static final ConnectorType TUX_CONNECTOR = new ConnectorType(
			"TuxConnector");

	public static final ConnectorType CICS_CONNECTOR = new ConnectorType(
			"CicsConnector");

	public static final ConnectorType TONG_CONNECTOR = new ConnectorType(
			"TongConnector");
}
