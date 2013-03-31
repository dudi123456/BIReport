package com.fins.gt.export;

import java.io.IOException;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.fins.gt.util.LogHandler;

public class JxlXlsWriter extends AbstractXlsWriter {

	private WritableWorkbook workbook;
	private WritableSheet sheet;
	private WorkbookSettings workbookSettings = new WorkbookSettings();

	public void writeNumCell(int row, short col, double value)
			throws RowsExceededException, WriteException {
		Number number = new Number(col, row, value);
		sheet.addCell(number);
	}

	public void writeStringCell(int row, short col, String value)
			throws RowsExceededException, WriteException {
		Label label = new Label(col, row, value);
		sheet.addCell(label);
	}

	public void writeCell(int row, short col, Object value) throws Exception {
		if (isNumber(value)) {
			double dNum = Double.parseDouble(String.valueOf(value));
			writeNumCell(row, col, dNum);
		} else {
			writeStringCell(row, col, String.valueOf(value));
		}
	}

	public void end() {

		try {
			workbook.write();
			workbook.close();
		} catch (WriteException e) {
			LogHandler.error(this, e);
		} catch (IOException e) {
			LogHandler.error(this, e);
		}
	}

	public void start() {
		try {

			workbookSettings.setUseTemporaryFileDuringWrite(true);
			if (getEncoding() != null) {
				workbookSettings.setEncoding(getEncoding());
			}

			workbook = Workbook.createWorkbook(getOut(), workbookSettings);

			sheet = workbook.createSheet("Sheet 1", 0);
		} catch (IOException e) {
			LogHandler.error(this, e);
		}
	}

}
