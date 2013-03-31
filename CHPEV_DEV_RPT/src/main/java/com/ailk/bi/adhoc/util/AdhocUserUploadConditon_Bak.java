package com.ailk.bi.adhoc.util;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocUserUploadConditon_Bak {
	private static Log logger = LogFactory
			.getLog(AdhocUserUploadConditon_Bak.class);

	public AdhocUserUploadConditon_Bak() {

	}

	// 文件类型0:文本，1：excel
	private int fileType;

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	private double txtRowCount = 0;

	public double calculateFileRowCnt(File f) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			// br = new BufferedReader(new In);

			while (br.ready()) {
				br.readLine();
				txtRowCount++;
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return txtRowCount;
	}

	public double calculateFileRowCnt(InputStream in) {

		switch (fileType) {
		case 0:
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(in));
				// br = new BufferedReader(new In);

				while (br.ready()) {
					br.readLine();
					txtRowCount++;
				}

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case 1:
			try {
				Workbook m_wb = Workbook.getWorkbook(in);
				txtRowCount = m_wb.getSheet(0).getRows() - 1;
				m_wb.close();

			} catch (BiffException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		}

		return txtRowCount;

	}

	public void excuteSql(String sql) {

		try {
			logger.info("sql-" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public List readContentFromLine(InputStream in, int startRow, int endRow)
			throws Exception {

		List list = new ArrayList();
		switch (fileType) {
		case 0:
			int curCnt = 0;
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			if (startRow != 0) {
				for (int i = 0; i < startRow; i++) {
					bf.readLine();
					curCnt++;
				}
			}
			// 读取指定行

			while (bf.ready()) {
				String tmp = StringB.NulltoBlank(bf.readLine());
				if (tmp.length() > 0) {
					// System.out.println(tmp);
					tmp = tmp.replaceAll("\\&nbsp;", "");
					tmp = tmp.replaceAll("\\？", "");
					tmp = tmp.replaceAll("\\'", "");
					tmp = tmp.replaceAll("\\,", "");
					list.add(tmp);
				}

				curCnt++;
				if ((curCnt - 1) == (endRow - 1)) {
					break;
				}
			}

			bf.close();
			break;
		case 1:// excel文件
			Workbook m_wb = Workbook.getWorkbook(in);
			Sheet sheet = m_wb.getSheet(0);
			int tmp_startRow = startRow + 1;
			int tmp_endRow = endRow + 1;
			System.out.println(tmp_startRow + ":" + tmp_endRow);
			for (int k = tmp_startRow; k < tmp_endRow; k++) {
				String readCntns = StringB.NulltoBlank(sheet.getCell(0, k)
						.getContents().trim());
				readCntns = readCntns.replaceAll("\\&nbsp;", "");
				readCntns = readCntns.replaceAll("\\？", "");
				readCntns = readCntns.replaceAll("\\'", "");
				readCntns = readCntns.replaceAll("\\,", "");
				if (readCntns.length() > 0) {
					list.add(readCntns);
				}
			}
			m_wb.close();

			break;
		}

		return list;
	}

	public List readContentFromLine(File f, int startRow, int endRow)
			throws Exception {

		int curCnt = 0;
		BufferedReader bf = new BufferedReader(new FileReader(f));
		if (startRow != 0) {
			for (int i = 0; i < startRow; i++) {
				bf.readLine();
				curCnt++;
			}
		}
		// 读取指定行
		List list = new ArrayList();
		while (bf.ready()) {
			String tmp = StringB.NulltoBlank(bf.readLine());
			if (tmp.length() > 0) {
				// System.out.println(tmp);
				list.add(tmp);
			}

			curCnt++;
			if ((curCnt - 1) == (endRow - 1)) {
				break;
			}
		}

		bf.close();
		return list;
	}

	public double getTxtRowCount() {
		return txtRowCount;
	}

	public String[][] qryObjectInfoList(String sql) {

		String[][] value = null;

		try {
			logger.info("sql-" + sql);
			value = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {

			e.printStackTrace();
		}

		return value;
	}


	public static void main(String[] args) throws IOException {
		File f = new File("c:\\test.txt");
		AdhocUserUploadConditon myUp = new AdhocUserUploadConditon();
		try {
			myUp.readContentFromLine(f, 30, 40);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
