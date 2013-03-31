package com.ailk.bi.adhoc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.log4j.Logger;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.pages.PagesInfoStruct;
import com.ailk.bi.pages.WebPageTool;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocBuildDownLoadFile {

	private Logger logger = Logger.getLogger(AdhocBuildDownLoadFile.class);
	private String mrootPath;
	private String mfileName = "用户清单";

	private String mqrySql;
	private String mcntSql;
	private int mrowCnt = -1;
	private HttpServletRequest request;
	private int perFileRowLimit = 50000;// 每个文件写多少行

	private boolean blnFileExist = false;

	private String mZipName = "Export.zip";

	private Workbook m_wb;
	private WritableWorkbook m_wwb;
	private WritableSheet m_ws;

	private int isheetCnt = 1;

	public AdhocBuildDownLoadFile() {

	}

	public AdhocBuildDownLoadFile(String rootPath, String fileName,
			String qrySql, String cntSql, int rowCnt, HttpServletRequest request) {
		this.mrootPath = rootPath;
		this.mfileName = fileName;
		this.mqrySql = qrySql;
		this.mcntSql = cntSql;
		this.mrowCnt = rowCnt;
		this.request = request;

	}

	private int perPageCnt = 10000;

	private UiAdhocUserListTable[] mdefineInfo;

	public String buildCsvFile() throws AppException {

		long t1 = System.currentTimeMillis();
		long t2 = 0;
		FileWriter fw = null;

		File uploadPath = new File(mrootPath);
		if (!uploadPath.exists()) {
			uploadPath.mkdir();
		}

		double recordCnt = 0;

		if (mrowCnt == -1) {// 全部记录
			String[][] totalCnt = AdhocUtil.queryArrayFacade(mcntSql);// 总记录数
			recordCnt = Double.parseDouble(totalCnt[0][0]);
		} else {
			recordCnt = mrowCnt;
		}

		double pageCnt = Math.floor((recordCnt + perPageCnt - 1) / perPageCnt);
		// pageCount=(totalRowCount+pageSize-1)/pageSize; //获取分页数目

		logger.debug("totalcnt:" + recordCnt + ":" + pageCnt);

		long lngCurCnt = 0;
		List listCreateFile = new ArrayList();
		int fileCnt = 0;

		for (int i = 0; i < pageCnt; i++) {
			int dblTmp = (i + 1) * perPageCnt;

			if (mrowCnt != -1) {
				if (dblTmp > mrowCnt) {
					dblTmp = mrowCnt;
				}
			}

			String strWhere[] = new String[] { dblTmp + "|1",
					i * perPageCnt + "|1" };

			logger.debug(mqrySql);
			for (int it = 0; it < strWhere.length; it++) {
				logger.debug(" strWhere :" + strWhere[it]);
			}

			String arr[][] = WebDBUtil.execQryArray(mqrySql, strWhere, "");
			AdhocViewHandler.fillDescByMapCode(request, arr, mdefineInfo);

			if (arr != null && arr.length > 0) {
				for (int j = 0; j < arr.length; j++) {

					if (lngCurCnt % perFileRowLimit == 0) {// 如果达到文件行数限定，需要换新文件
						fileCnt++;
						String fileTmp = mfileName + "_" + fileCnt + ".csv";
						String retnFileName = mrootPath + File.separatorChar
								+ fileTmp;
						// String retnFileName = mrootPath + mfileName;

						retnFileName = retnFileName.replace(File.separatorChar,
								'/');

						if (FileOperator.fileExist(retnFileName)) {
							FileOperator.delFile(retnFileName);
						}

						try {
							fw = new FileWriter(retnFileName);
						} catch (IOException e) {
							e.printStackTrace();
						}
						listCreateFile.add(buildCSVHeader(fw, fileTmp));
					}

					String[] value = arr[j];
					String strTmp = "";
					for (int m = 0; m < value.length; m++) {

						if ("".equals(mdefineInfo[m].getMsu_type())) {
							if (strTmp.length() == 0) {
								strTmp = value[m];
							} else {
								strTmp += "," + value[m];
							}

						} else {
							if (strTmp.length() == 0) {
								strTmp = FormatUtil.formatStr(value[m],
										Integer.parseInt(mdefineInfo[m]
												.getMsu_digit()), true);
							} else {
								strTmp += ","
										+ FormatUtil.formatStr(value[m],
												Integer.parseInt(mdefineInfo[m]
														.getMsu_digit()), true);
							}

						}

					}
					lngCurCnt++;

					strTmp += "\r\n";

					try {
						fw.write(strTmp);
					} catch (IOException e) {

						e.printStackTrace();
					}
					if (lngCurCnt % perFileRowLimit == 0) {// 如果写完一个文件，关闭它
						try {
							fw.close();
							// logger.debug("关闭文件：" + mfileName + "_" + fileCnt
							// + ".csv" );
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

				}
			}

		}

		try {
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

		t2 = System.currentTimeMillis();
		logger.debug("Your  program  has  executed  for  "
				+ (int) ((t2 - t1) / 1000) + "  seconds  " + ((t2 - t1) % 1000)
				+ "  micro  seconds");

		// 压缩成ZIP文件
		ZipFileUtil zipUtil = new ZipFileUtil();
		zipUtil.setListFile(listCreateFile);
		zipUtil.setRootPath(mrootPath);
		zipUtil.setZipName(mZipName);
		String retn = zipUtil.doZipFile();

		// logger.debug(i*perPageCnt +":" + (i+1)*perPageCnt);
		return retn;
	}

	/**
	 * 
	 * @return
	 * @desc：生成excel文件
	 * @throws AppException
	 */

	@SuppressWarnings("unused")
	public String buildXLSFile(HttpServletRequest request) throws AppException {
		long t1 = System.currentTimeMillis();
		long t2 = 0;
		HttpSession session = request.getSession();

		File uploadPath = new File(mrootPath);
		if (!uploadPath.exists()) {
			uploadPath.mkdir();
		}

		String fileTmp = mfileName + ".xls";
		String retnFileName = mrootPath + File.separatorChar + fileTmp;
		retnFileName = retnFileName.replace(File.separatorChar, '/');

		if (FileOperator.fileExist(retnFileName)) {
			FileOperator.delFile(retnFileName);
		}

		String strRetnFile = "";

		double recordCnt = 0;
		double pageCnt = 0;

		long lngCurCnt = 0;

		int rowCntTmp = 0;

		if (this.mrowCnt == 10000) {// 10000条记录情况，直接走session值
			rowCntTmp = 1;
			strRetnFile = initXlsWriteBook();
			buildXLSHeader();
			String[][] list = (String[][]) session
					.getAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE);
			if (list == null) {
				list = new String[0][0];
			}
			PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request,
					list.length, list.length);
			for (int i = 0; i < pageInfo.iLinesPerPage
					&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
				String[] value = list[i + pageInfo.absRowNoCurPage()];
				String strTmp = "";
				int flag = 0;
				for (int m = 0; m < value.length; m++) {

					Label lb = null;
					jxl.write.Number labelN = null;
					if ("".equalsIgnoreCase(mdefineInfo[m].getMsu_type())
							|| "D".equalsIgnoreCase(mdefineInfo[m]
									.getMsu_type())
							|| "M".equalsIgnoreCase(mdefineInfo[m]
									.getMsu_type())
							|| "SS".equalsIgnoreCase(mdefineInfo[m]
									.getMsu_type())) {
						strTmp = value[m];
						flag = 0;
						lb = new jxl.write.Label(m, rowCntTmp, strTmp);
					} else {
						flag = 1;

						strTmp = FormatUtil
								.formatStr(value[m],
										Integer.parseInt(mdefineInfo[m]
												.getMsu_digit()), true);

						strTmp = strTmp.replace(",", "");

						String strFormat = "#";

						if (Integer.parseInt(mdefineInfo[m].getMsu_digit()) > 0) {
							strFormat += ".";
						}

						for (int f = 0; f < Integer.parseInt(mdefineInfo[m]
								.getMsu_digit()); f++) {
							strFormat += "#";
						}
						jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
								strFormat);

						jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(
								nf);
						String valueTmp = StringB.NulltoBlank(value[m]);
						if (valueTmp.length() == 0) {
							valueTmp = "0";
						}

						// labelN = new
						// jxl.write.Number(m,rowCntTmp,Double.parseDouble(valueTmp),wcfN);
						labelN = new jxl.write.Number(m, rowCntTmp,
								Double.parseDouble(strTmp));

					}
					// Label lb = new jxl.write.Label(m,rowCntTmp,strTmp);
					try {
						switch (flag) {
						case 0:
							m_ws.addCell(lb);
							break;
						case 1:
							m_ws.addCell(labelN);
							break;
						}

					} catch (RowsExceededException e) {

						e.printStackTrace();
					} catch (WriteException e) {

						e.printStackTrace();
					}

				}
				rowCntTmp++;
				lngCurCnt++;
			}

		} else {
			String[][] totalCnt = AdhocUtil.queryArrayFacade(mcntSql);// 总记录数
			recordCnt = Double.parseDouble(totalCnt[0][0]);

			if (mrowCnt == -1) {// 全部记录
				// String[][] totalCnt =
				// AdhocUtil.queryArrayFacade(mcntSql);//总记录数
				// recordCnt = Double.parseDouble(totalCnt[0][0]);
			} else {
				if (recordCnt > mrowCnt) {
					recordCnt = mrowCnt;
				}

			}

			pageCnt = Math.floor((recordCnt + perPageCnt - 1) / perPageCnt);
			// pageCount=(totalRowCount+pageSize-1)/pageSize; //获取分页数目

			logger.debug("totalcnt:" + recordCnt + ":" + pageCnt);

			for (int i = 0; i < pageCnt; i++) {

				int dblTmp = (i + 1) * perPageCnt;
				if (((int) pageCnt) == 1) {
					dblTmp = (int) recordCnt;
				}

				if (mrowCnt != -1) {
					if (dblTmp > mrowCnt) {
						dblTmp = mrowCnt;
					}
				}

				String strWhere[] = new String[] { dblTmp + "|1",
						i * perPageCnt + "|1" };

				logger.debug(mqrySql);
				for (int it = 0; it < strWhere.length; it++) {
					logger.debug(" strWhere :" + strWhere[it]);
				}

				String arr[][] = WebDBUtil.execQryArray(mqrySql, strWhere, "");
				AdhocViewHandler.fillDescByMapCode(request, arr, mdefineInfo);

				if (arr != null && arr.length > 0) {
					for (int j = 0; j < arr.length; j++) {

						if (lngCurCnt % perFileRowLimit == 0) {// 如果达到文件行数限定，需要换新文件
							rowCntTmp = 1;
							strRetnFile = initXlsWriteBook();
							buildXLSHeader();

						}

						String[] value = arr[j];
						String strTmp = "";
						int flag = 0;
						for (int m = 0; m < value.length; m++) {
							Label lb = null;
							jxl.write.Number labelN = null;
							if ("".equalsIgnoreCase(mdefineInfo[m]
									.getMsu_type())
									|| "D".equalsIgnoreCase(mdefineInfo[m]
											.getMsu_type())
									|| "M".equalsIgnoreCase(mdefineInfo[m]
											.getMsu_type())
									|| "SS".equalsIgnoreCase(mdefineInfo[m]
											.getMsu_type())) {
								strTmp = value[m];
								flag = 0;
								lb = new jxl.write.Label(m, rowCntTmp, strTmp);
							} else {
								flag = 1;

								strTmp = FormatUtil.formatStr(value[m],
										Integer.parseInt(mdefineInfo[m]
												.getMsu_digit()), true);

								strTmp = strTmp.replace(",", "");

								String strFormat = "#";

								if (Integer.parseInt(mdefineInfo[m]
										.getMsu_digit()) > 0) {
									strFormat += ".";
								}

								for (int f = 0; f < Integer
										.parseInt(mdefineInfo[m].getMsu_digit()); f++) {
									strFormat += "#";
								}
								jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
										strFormat);

								jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(
										nf);
								String valueTmp = StringB.NulltoBlank(value[m]);
								if (valueTmp.length() == 0) {
									valueTmp = "0";
								}

								// labelN = new
								// jxl.write.Number(m,rowCntTmp,Double.parseDouble(valueTmp),wcfN);
								labelN = new jxl.write.Number(m, rowCntTmp,
										Double.parseDouble(strTmp));

							}
							// Label lb = new
							// jxl.write.Label(m,rowCntTmp,strTmp);
							try {
								switch (flag) {
								case 0:
									m_ws.addCell(lb);
									break;
								case 1:
									m_ws.addCell(labelN);
									break;
								}

							} catch (RowsExceededException e) {

								e.printStackTrace();
							} catch (WriteException e) {

								e.printStackTrace();
							}
						}
						rowCntTmp++;
						lngCurCnt++;

						if (lngCurCnt % perFileRowLimit == 0) {// 如果写完一个文件，关闭它

							closeXlsWriteBook();
						}

					}
				}

			}

		}

		closeXlsWriteBook();

		t2 = System.currentTimeMillis();
		logger.debug("Your  program  has  executed  for  "
				+ (int) ((t2 - t1) / 1000) + "  seconds  " + ((t2 - t1) % 1000)
				+ "  micro  seconds");

		// 压缩成ZIP文件
		/*
		 * ZipFileUtil zipUtil = new ZipFileUtil();
		 * zipUtil.setListFile(listCreateFile); zipUtil.setRootPath(mrootPath);
		 * zipUtil.setZipName(mZipName); String retn = zipUtil.doZipFile();
		 */
		// logger.debug(i*perPageCnt +":" + (i+1)*perPageCnt);
		mZipName = mfileName + ".xls";

		return strRetnFile;
	}

	private String initXlsWriteBook() {
		String fileTmp = mfileName + ".xls";
		String retnFileName = mrootPath + File.separatorChar + fileTmp;
		retnFileName = retnFileName.replace(File.separatorChar, '/');

		try {

			if (blnFileExist) {
				m_wb = Workbook.getWorkbook(new FileInputStream(retnFileName));
				m_wwb = Workbook.createWorkbook(new File(retnFileName), m_wb);

			} else {
				blnFileExist = true;
				m_wwb = Workbook.createWorkbook(new File(retnFileName));

			}
			m_ws = m_wwb.createSheet("sheet" + isheetCnt, isheetCnt - 1);
			isheetCnt++;
			return retnFileName;
		} catch (BiffException ex) {
			ex.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return retnFileName;
	}

	private void closeXlsWriteBook() {
		try {
			m_wwb.write();
			m_wwb.close();
			m_wb.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private String buildCSVHeader(FileWriter fw, String fileTmp) {
		// mfileName = (new java.util.Date()).getTime() + ".csv" ;

		try {

			String strHead = "";

			for (int i = 0; mdefineInfo != null && i < mdefineInfo.length; i++) {

				if ("".equals(mdefineInfo[i].getMsu_unit())) {
					if (strHead.length() == 0) {
						strHead = mdefineInfo[i].getMsu_name();
					} else {
						strHead += "," + mdefineInfo[i].getMsu_name();
					}

				} else {
					if (strHead.length() == 0) {
						strHead = mdefineInfo[i].getMsu_name() + "("
								+ mdefineInfo[i].getMsu_unit() + ")";
					} else {
						strHead += "," + mdefineInfo[i].getMsu_name() + "("
								+ mdefineInfo[i].getMsu_unit() + ")";
					}

				}

			}
			strHead += "\r\n";
			fw.write(strHead);

			// fw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return fileTmp;
	}

	private void buildXLSHeader() {
		// mfileName = (new java.util.Date()).getTime() + ".csv" ;

		try {

			String strHead = "";

			for (int i = 0; mdefineInfo != null && i < mdefineInfo.length; i++) {

				if ("".equals(mdefineInfo[i].getMsu_unit())) {
					strHead = mdefineInfo[i].getMsu_name();

				} else {
					strHead = mdefineInfo[i].getMsu_name() + "("
							+ mdefineInfo[i].getMsu_unit() + ")";
				}

				Label lb = new jxl.write.Label(i, 0, strHead);
				m_ws.addCell(lb);

			}

			// fw.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public int getPerFileRowLimit() {
		return perFileRowLimit;
	}

	public void setPerFileRowLimit(int perFileRowLimit) {
		this.perFileRowLimit = perFileRowLimit;
	}

	public String getMrootPath() {
		return mrootPath;
	}

	public void setMrootPath(String mrootPath) {
		this.mrootPath = mrootPath;
	}

	public String getMfileName() {
		return mfileName;
	}

	public void setMfileName(String mfileName) {
		this.mfileName = mfileName;
	}

	public String getMqrySql() {
		return mqrySql;
	}

	public void setMqrySql(String mqrySql) {
		this.mqrySql = mqrySql;
	}

	public String getMcntSql() {
		return mcntSql;
	}

	public void setMcntSql(String mcntSql) {
		this.mcntSql = mcntSql;
	}

	public int getMrowCnt() {
		return mrowCnt;
	}

	public void setMrowCnt(int mrowCnt) {
		this.mrowCnt = mrowCnt;
	}

	public UiAdhocUserListTable[] getMdefineInfo() {
		return mdefineInfo;
	}

	public void setMdefineInfo(UiAdhocUserListTable[] mdefineInfo) {
		this.mdefineInfo = mdefineInfo;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getPerPageCnt() {
		return perPageCnt;
	}

	public void setPerPageCnt(int perPageCnt) {
		this.perPageCnt = perPageCnt;
	}

	public String getmZipName() {
		return mZipName;
	}

	public void setmZipName(String mZipName) {
		this.mZipName = mZipName;
	}

}
