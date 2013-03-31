package com.ailk.bi.adhoc.util;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocUserUploadConditon {
	private static Log logger = LogFactory
			.getLog(AdhocUserUploadConditon.class);

	public AdhocUserUploadConditon() {

	}

	// 文件类型0:文本，1：excel,2:excel2007格式
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
		if (fileType == 0) {
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
		} else {
			Workbook m_wb = null;
			switch (fileType) {

			case 1:
				// excel2003
				try {
					m_wb = new HSSFWorkbook(in);
				} catch (IOException e) {

					e.printStackTrace();
				}
				txtRowCount = m_wb.getSheetAt(0).getPhysicalNumberOfRows() - 0;
				break;

			case 2:
				// excel2007
				try {
					m_wb = new XSSFWorkbook(in);
				} catch (IOException e) {

					e.printStackTrace();
				}
				txtRowCount = m_wb.getSheetAt(0).getPhysicalNumberOfRows() - 1;
				break;
			}

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
		System.out.println("fileType=" + fileType);
		if (fileType == 0) {
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
		} else {
			Workbook m_wb = null;
			switch (fileType) {
			case 1:
				// excel2003
				try {
					m_wb = new HSSFWorkbook(in);
				} catch (IOException e) {

					e.printStackTrace();
				}

				break;

			case 2:
				// excel2007
				try {
					m_wb = new XSSFWorkbook(in);
				} catch (IOException e) {

					e.printStackTrace();
				}

				break;
			}

			Sheet sheet = m_wb.getSheetAt(0);

			int tmp_startRow = startRow + 1;
			int tmp_endRow = endRow + 1;
			if (fileType == 1) {
				tmp_endRow = endRow;
			}
			System.out.println(tmp_startRow + ":" + tmp_endRow);
			for (int k = tmp_startRow; k < tmp_endRow; k++) {

				int type = sheet.getRow(k).getCell(0).getCellType();
				String readCntns = "";
				if (type == 0) {
					// 防止科学计数法实现
					String str = NumberFormat.getNumberInstance().format(
							sheet.getRow(k).getCell(0).getNumericCellValue());
					while (str.indexOf(",") > -1) {
						str = str.substring(0, str.indexOf(","))
								+ str.substring(str.indexOf(",") + 1);
					}
					readCntns = str;
				} else {
					readCntns = StringB.NulltoBlank(sheet.getRow(k).getCell(0)
							.getStringCellValue());
				}

				// String readCntns =
				// StringB.NulltoBlank(sheet.getRow(k).getCell(0).getStringCellValue());
				readCntns = readCntns.replaceAll("\\&nbsp;", "");
				readCntns = readCntns.replaceAll("\\？", "");
				readCntns = readCntns.replaceAll("\\'", "");
				readCntns = readCntns.replaceAll("\\,", "");
				if (readCntns.length() > 0) {
					list.add(readCntns);
				}
			}

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

	/*
	 * public static int[] batch(String sql, Object[][] args) { return
	 * batch(null, sql, args); }
	 * 
	 * public static int[] batch(String[] sql) { try { return
	 * batch(getConnection(), sql); } catch (Exception e) {
	 * System.out.println("DBUtil.query error:" + e.getMessage());
	 * e.printStackTrace(); } return null; }
	 * 
	 * public static int[] batch(Connection conn, String[] sql) { Statement stmt
	 * = null; int[] updateCounts = null; //System.out.println("DBUtil.sql:" +
	 * sql); try { if (conn == null) { conn = getConnection(); } stmt =
	 * conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	 * ResultSet.CONCUR_UPDATABLE); conn.setAutoCommit(false);
	 * stmt.clearBatch(); for (String tmpStr : sql) { stmt.addBatch(tmpStr); }
	 * updateCounts = stmt.executeBatch(); conn.commit(); //
	 * System.out.println(updateCounts); } catch (BatchUpdateException bue) {
	 * System.out.println("SQLException: " + bue.getMessage());
	 * System.out.println("SQLState: " + bue.getSQLState());
	 * System.out.println("Message: " + bue.getMessage());
	 * System.out.println("Vendor error code: " + bue.getErrorCode());
	 * System.out.print("Update counts: "); updateCounts =
	 * bue.getUpdateCounts(); for (int i = 0; i < updateCounts.length; i++) {
	 * System.out.print(updateCounts[i] + " "); } } catch (SQLException ex) {
	 * System.out.println("SQLException: " + ex.getMessage());
	 * System.out.println("SQLState: " + ex.getSQLState());
	 * System.out.println("Message: " + ex.getMessage());
	 * System.out.println("Vendor error code: " + ex.getErrorCode());
	 * ex.printStackTrace(); } catch (Exception e) {
	 * System.out.println("DBUtil.batch error:" + e.getMessage());
	 * e.printStackTrace(); } finally { free(conn, null, stmt, null); } return
	 * updateCounts; }
	 * 
	 * public static int[] batch(Connection conn, String sql, Object[][] args) {
	 * PreparedStatement pstmt = null; int[] updateCounts = null;
	 * //System.out.println("DBUtil.sql:" + sql); try { if (conn == null) { conn
	 * = getConnection(); } pstmt = conn.prepareStatement(sql);
	 * conn.setAutoCommit(false); //System.out.println("args:" + args.length +
	 * ":" + args[0].length);
	 * 
	 * for (int i = 0; i < args.length; i++) { for (int j = 0; j <
	 * args[i].length; j++) { //System.out.println("args[i][j] " + args[i][j]);
	 * pstmt.setObject(j + 1, args[i][j]); } pstmt.addBatch(); //
	 * pstmt.executeUpdate(); } updateCounts = pstmt.executeBatch();
	 * conn.commit(); //System.out.println("sdf:" + updateCounts.length); }
	 * catch (BatchUpdateException bue) { System.out.println("SQLException: " +
	 * bue.getMessage()); System.out.println("SQLState: " + bue.getSQLState());
	 * System.out.println("Message: " + bue.getMessage());
	 * System.out.println("Vendor error code: " + bue.getErrorCode());
	 * System.out.print("Update counts: "); updateCounts =
	 * bue.getUpdateCounts(); for (int i = 0; i < updateCounts.length; i++) {
	 * System.out.print(updateCounts[i] + " "); } } catch (SQLException ex) {
	 * System.out.println("SQLException: " + ex.getMessage());
	 * System.out.println("SQLState: " + ex.getSQLState());
	 * System.out.println("Message: " + ex.getMessage());
	 * System.out.println("Vendor error code: " + ex.getErrorCode());
	 * ex.printStackTrace(); } catch (Exception e) {
	 * System.out.println("DBUtil.batch error:" + e.getMessage());
	 * e.printStackTrace(); } finally { free(conn, null, null, pstmt); } return
	 * updateCounts; }
	 * 
	 * public static void free(Connection conn, ResultSet rs, Statement st,
	 * PreparedStatement ps) { try { if (rs != null) { rs.close(); rs = null; }
	 * if (ps != null) { ps.close(); ps = null; } if (st != null) { st.close();
	 * st = null; } if (conn != null) { conn.close(); conn = null; } } catch
	 * (SQLException ex) { System.out.println("SQLException: " +
	 * ex.getMessage()); System.out.println("SQLState: " + ex.getSQLState());
	 * System.out.println("Message: " + ex.getMessage());
	 * System.out.println("Vendor error code: " + ex.getErrorCode()); } catch
	 * (Exception e) { e.printStackTrace(); System.err.println("Exception: " +
	 * e.getMessage()); } finally { try { if (rs != null) { rs.close(); rs =
	 * null; } if (ps != null) { ps.close(); ps = null; } if (st != null) {
	 * st.close(); st = null; } if (conn != null) { conn.close(); conn = null; }
	 * } catch (Exception e) { e.printStackTrace();
	 * System.err.println("Exception: " + e.getMessage()); } } }
	 */
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static Connection getConnection() throws Exception { return
	 * WebDBUtil.getConn();
	 * 
	 * }
	 */

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
