package com.ailk.bi.connect;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class ControlEntry {
	static class Field {
		private static int nextOrdinal = 0;

		public final int ordinal = nextOrdinal++;

		Field() {

		}

		public static final Field COMPRESS_LENGTH = new Field(); // 压缩后数据包长度

		public static final Field TERM_IP = new Field(); // 终端IP地址

		public static final Field TERM_NAME = new Field(); // 终端名称

		public static final Field SERV_NAME = new Field(); // 服务程序名

		public static final Field SERC_NAME = new Field(); // 服务名

		public static final Field DATE_TIME = new Field(); // 发起时间

		public static final Field CONN_TYPE = new Field(); // 连接器类型

		public static final Field MODE = new Field(); // 运行方式

		public static final Field RESERVE = new Field(); // 保留字段

		public static final Field[] VALUES = { COMPRESS_LENGTH, TERM_IP,
				TERM_NAME, SERV_NAME, SERC_NAME, DATE_TIME, CONN_TYPE, MODE,
				RESERVE };

		public static final int[] LENGTHES = { 16, 12, 4, 8, 8, 10, 1, 1, 4 };
	}

	private int compressLength = 0;

	private String termIp;

	private String termName;

	private String servName;

	private String serviceName;

	private Date dateTime;

	private byte connectorType;

	private byte mode;

	int[] fieldLength = new int[Field.VALUES.length];

	ControlEntry() {
		compressLength = 0;
		termIp = "";
		termName = "";
		servName = "";
		serviceName = "";
		dateTime = new Date();
		connectorType = '0';
		mode = '0';
	}

	ControlEntry setCompressLength(int length) {
		this.compressLength = length;
		return this;
	}

	int getCompressLength() {

		return compressLength;
	}

	ControlEntry setTermIp(String termIp) {
		this.termIp = termIp;
		return this;
	}

	String getTermIp() {
		return termIp;
	}

	ControlEntry setTermName(String termName) {
		this.termName = termName;
		return this;
	}

	String getTermName() {
		return termName;
	}

	ControlEntry setServName(String servName) {
		this.servName = servName;
		return this;
	}

	String getServName() {
		return servName;
	}

	ControlEntry setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	String getServiceName() {
		return serviceName;
	}

	ControlEntry setDateTime(Date dateTime) {
		this.dateTime = dateTime;
		return this;
	}

	Date getDateTime() {
		return dateTime;
	}

	ControlEntry setConnectorType(ConnectorType type) {
		if (type == ConnectorType.TUX_CONNECTOR) {
			this.connectorType = '1';
		} else if (type == ConnectorType.CICS_CONNECTOR) {
			this.connectorType = '2';
		} else if (type == ConnectorType.TONG_CONNECTOR) {
			this.connectorType = '3';
		}
		return this;
	}

	ConnectorType getConnectorType() {
		ConnectorType type;
		switch (connectorType) {
		case '1':
			type = ConnectorType.TUX_CONNECTOR;
			break;
		case '2':
			type = ConnectorType.CICS_CONNECTOR;
			break;
		case '3':
			type = ConnectorType.TONG_CONNECTOR;
			break;
		default:
			type = null;
		}
		return type;
	}

	static int size() {
		int i = 0;
		for (int j = 0; j < Field.LENGTHES.length; j++)
			i += Field.LENGTHES[j];

		return i;
	}

	static ControlEntry parse(String str) {
		byte[] byteBuf = str.getBytes();
		ControlEntry entry = new ControlEntry();

		byte[] tempBuf;
		for (int i = 0, offset = 0; i < Field.VALUES.length; offset += Field.LENGTHES[i], i++) {
			tempBuf = new byte[Field.LENGTHES[i]];

			System.arraycopy(byteBuf, offset, tempBuf, 0,
					Math.min(tempBuf.length, byteBuf.length - offset));
			if (Field.VALUES[i] == Field.COMPRESS_LENGTH) {
				entry.setCompressLength(Integer.parseInt(new String(tempBuf)
						.trim()));
			} else if (Field.VALUES[i] == Field.TERM_IP) {
				entry.setTermIp(new String(tempBuf));
			} else if (Field.VALUES[i] == Field.TERM_NAME) {
				entry.setTermName(new String(tempBuf));
			} else if (Field.VALUES[i] == Field.SERV_NAME) {
				entry.setServName(new String(tempBuf));
			} else if (Field.VALUES[i] == Field.SERC_NAME) {
				entry.setServiceName(new String(tempBuf));
			} else if (Field.VALUES[i] == Field.DATE_TIME) {
				Date dateTime = null;
				try {
					dateTime = new SimpleDateFormat("MMddHHmmss")
							.parse(new String(tempBuf));
				} catch (ParseException e) {
					dateTime = new Date();
				}
				entry.setDateTime(dateTime);
			} else if (Field.VALUES[i] == Field.CONN_TYPE) {
				entry.connectorType = tempBuf[0];
			} else if (Field.VALUES[i] == Field.MODE) {
				entry.mode = tempBuf[0];
			}
		}

		return entry;
	}

	public String format() {
		byte[] byteBuf = new byte[size()];
		byte[] tempBuf;

		java.util.Arrays.fill(byteBuf, 0, byteBuf.length, (byte) ' ');
		for (int i = 0, position = 0; i < Field.VALUES.length; i++) {
			if (Field.VALUES[i] == Field.COMPRESS_LENGTH) {
				tempBuf = String.valueOf(compressLength).getBytes();
			} else if (Field.VALUES[i] == Field.TERM_IP) {
				tempBuf = termIp.getBytes();
			} else if (Field.VALUES[i] == Field.TERM_NAME) {
				tempBuf = termName.getBytes();
			} else if (Field.VALUES[i] == Field.SERV_NAME) {
				tempBuf = servName.getBytes();
			} else if (Field.VALUES[i] == Field.SERC_NAME) {
				tempBuf = serviceName.getBytes();
			} else if (Field.VALUES[i] == Field.DATE_TIME) {
				tempBuf = new SimpleDateFormat("MMddHHmmss").format(dateTime)
						.getBytes();
			} else if (Field.VALUES[i] == Field.CONN_TYPE) {
				tempBuf = new byte[1];
				tempBuf[0] = connectorType;
			} else if (Field.VALUES[i] == Field.MODE) {
				tempBuf = new byte[1];
				tempBuf[0] = mode;
			} else {
				tempBuf = new byte[0];
			}
			System.arraycopy(tempBuf, 0, byteBuf, position,
					Math.min(Field.LENGTHES[i], tempBuf.length));
			position += Field.LENGTHES[i];
		}

		return new String(byteBuf);
	}

	public String toString() {
		return format();
	}
}
