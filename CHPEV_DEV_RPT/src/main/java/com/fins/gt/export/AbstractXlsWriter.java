package com.fins.gt.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractXlsWriter {

	private OutputStream out;
	private String encoding = null;

	public AbstractXlsWriter() {
	}

	public abstract void start();

	public abstract void end();

	public abstract void writeCell(int row, short col, Object value)
			throws Exception;

	private int row = 0;

	private List errRows = new ArrayList();

	public List getErrRows() {
		return errRows;
	}

	public void init() {
		setRow(0);
		errRows.clear();
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void addRow(Object[] record) {
		for (short i = 0; i < record.length; i++) {
			try {
				writeCell(this.row, i, record[i]);
			} catch (Exception e) {
				errRows.add(Integer.valueOf(this.row));
			}
		}
		this.row++;
	}

	public void close() {
		try {
			getOut().close();
		} catch (IOException e) {
			// do nothing;
		}
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public static boolean isNumber(Object value) {
		return value instanceof Number;
	}

}
