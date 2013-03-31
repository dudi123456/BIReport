package com.ailk.bi.report.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.util.CodeParamUtil;
import com.ailk.bi.base.util.ZipUtil;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.domain.RptGroupTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILTableService;
import com.ailk.bi.report.service.impl.LTableServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class BatchExportUtil {

	private static String fileName = "BatchExport";

	/**
	 * 初始化导出文件目录
	 * 
	 * @param user_no
	 *            操作员工号
	 * @param basePath
	 *            文件保存路径
	 * @return
	 */
	public static String initDirectoryPath(String user_no, String basePath) {
		return basePath + "/" + user_no + "/";
	}

	/**
	 * 批量导出之前，删除当前用户以前导出的文件
	 * 
	 * @param fileDir
	 *            存放文件目录文件夹
	 * @throws IOException
	 */
	public static void cleanSaveFile(String fileDir) throws IOException {

		File dir = new File(fileDir);
		if (dir == null) {
			return;
		}
		File[] dirs = dir.listFiles();
		for (int i = 0; dirs != null && i < dirs.length; i++) {
			if (dirs[i].isDirectory()) {
				deleteDirectory(dirs[i]);
			} else {
				dirs[i].delete();
			}

		}
	}

	/**
	 * 提供删除文件夹及文件
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDirectory(File dir) throws IOException {
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir
					+ " is not a directory. ");
		}

		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				deleteDirectory(entries[i]);
			} else {
				entries[i].delete();
			}
		}
		dir.delete();
	}

	/**
	 * 创建主文件
	 * 
	 * @param reportName
	 * @throws IOException
	 */
	public static void createMainFile(HttpServletRequest request,
			RptResourceTable[] infoRpt, String dirPath) {
		// 创建主文件
		PrintWriter out = null;

		try {
			File mainFile = new File(dirPath + fileName + ".xls");
			mainFile.createNewFile();
			out = new PrintWriter(new BufferedWriter(new FileWriter(mainFile)));
			out.println(genMainFileBody(request, infoRpt));
			out.close();
		} catch (IOException e) {

			if (out != null) {
				out.close();
			}
			e.printStackTrace();
		}

	}

	/**
	 * 创建主文件内容
	 * 
	 * @param reportName
	 * @return
	 * @throws IOException
	 */
	public static String genMainFileBody(HttpServletRequest request,
			RptResourceTable[] infoRpt) {
		StringBuffer mainSB = new StringBuffer("");
		// 表体
		mainSB.append("<html xmlns:x=\"urn:schemas-microsoft-com:office:excel\">"
				+ "\n"
				+ "<head>"
				+ "\n"
				+ "<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">"
				+ "\n"
				+ "<meta name=\"Excel Workbook Frameset\">"
				+ "\n"
				+ "<link rel=File-List href=\""
				+ fileName
				+ ".files/filelist.xml\"> \n");

		mainSB.append("<xml>\n")
				.append("<x:ExcelWorkbook>\n")
				.append("<x:ExcelWorksheets>\n")
				.append("<x:ExcelWorksheet><x:Name>报表目录</x:Name>"
						+ "<x:WorksheetSource HRef=\"./" + fileName
						+ ".files/sheet000.htm\"/>" + "</x:ExcelWorksheet>\n");

		// sheet页
		int index = 1;
		for (int i = 0; infoRpt != null && i < infoRpt.length; i++) {
			/********************************************************************/

			String name = infoRpt[i].name;
			String rule = infoRpt[i].rpt_export_rule;
			// System.out.println("rule=========="+rule);
			String[] rule_type = rule.trim().split(",");
			for (int a = 0; rule_type != null && a < rule_type.length; a++) {
				// sheet 连接
				index = appendXSLLink(request, rule_type[a], name, index,
						mainSB);
			}

			/*******************************************************************/

		}

		// 结尾
		mainSB.append(
				"</x:ExcelWorksheets>"
						+ "<x:WindowHeight>9225</x:WindowHeight>\n"
						+ "<x:WindowWidth>14940</x:WindowWidth>\n"
						+ "<x:WindowTopX>240</x:WindowTopX>\n"
						+ "<x:WindowTopY>120</x:WindowTopY>\n"
						+ "<x:ProtectStructure>False</x:ProtectStructure>\n"
						+ "<x:ProtectWindows>False</x:ProtectWindows>\n"
						+ "</x:ExcelWorkbook>\n").append("</xml>\n")
				.append("</head>\n").append("</html>\n");

		return mainSB.toString();

	}

	/**
	 * 生成XSL标签连接
	 * 
	 * @param request
	 * @param type
	 * @param sheetName
	 * @param index
	 * @param mainSB
	 * @return
	 */
	public static int appendXSLLink(HttpServletRequest request, String type,
			String sheetName, int index, StringBuffer mainSB) {
		if (BatchExportConst.BATCH_EXPORT_RULE_REGION.equalsIgnoreCase(type)) {// 区域
			// 全省
			mainSB.append("<x:ExcelWorksheet>")
					.append("<x:Name>")
					.append(sheetName/* +"(全省)" */)
					.append("</x:Name>")
					.append("<x:WorksheetSource HRef=\"./" + fileName
							+ ".files/sheet00" + (index) + ".htm\"/>")
					.append("</x:ExcelWorksheet>").append("\n");
			index++;
			// 各地市
			HashMap cityMap = CodeParamUtil.codeListParamFetcher(request,
					"CITY");
			String[] cityArr = (String[]) cityMap.keySet().toArray(
					new String[cityMap.size()]);
			Arrays.sort(cityArr);
			for (int b = 0; cityArr != null && b < cityArr.length; b++) {
				mainSB.append("<x:ExcelWorksheet>")
						.append("<x:Name>")
						.append(sheetName + "(" + cityMap.get(cityArr[b]) + ")")
						.append("</x:Name>")
						.append("<x:WorksheetSource HRef=\"./" + fileName
								+ ".files/sheet00" + (index) + ".htm\"/>")
						.append("</x:ExcelWorksheet>").append("\n");
				index++;
			}
		} else if (BatchExportConst.BATCH_EXPORT_RULE_DEPT
				.equalsIgnoreCase(type)) {
			// 全部
			mainSB.append("<x:ExcelWorksheet>")
					.append("<x:Name>")
					.append(sheetName)
					.append("</x:Name>")
					.append("<x:WorksheetSource HRef=\"./" + fileName
							+ ".files/sheet00" + (index) + ".htm\"/>")
					.append("</x:ExcelWorksheet>").append("\n");
			index++;
			// 各地市
			HashMap deptMap = CodeParamUtil.codeListParamFetcher(request,
					"DEPT");
			String[] deptArr = (String[]) deptMap.keySet().toArray(
					new String[deptMap.size()]);
			Arrays.sort(deptArr);
			for (int b = 0; deptArr != null && b < deptArr.length; b++) {
				mainSB.append("<x:ExcelWorksheet>")
						.append("<x:Name>")
						.append(sheetName + "(" + deptMap.get(deptArr[b]) + ")")
						.append("</x:Name>")
						.append("<x:WorksheetSource HRef=\"./" + fileName
								+ ".files/sheet00" + (index) + ".htm\"/>")
						.append("</x:ExcelWorksheet>").append("\n");
				index++;
			}
		} else {
			mainSB.append("<x:ExcelWorksheet>")
					.append("<x:Name>")
					.append(sheetName)
					.append("</x:Name>")
					.append("<x:WorksheetSource HRef=\"./" + fileName
							+ ".files/sheet00" + (index) + ".htm\"/>")
					.append("</x:ExcelWorksheet>").append("\n");
			index++;
		}

		return index;

	}

	/**
	 * 创建索引目录文件
	 * 
	 * @throws IOException
	 * 
	 * @throws IOException
	 */
	public static void createIndexFile(HttpServletRequest request,
			RptResourceTable[] infoRpt, String dirPath) {
		PrintWriter indexOut = null;
		try {
			File indexFile = new File(dirPath + fileName
					+ ".files/sheet000.htm");
			indexFile.createNewFile();
			indexOut = new PrintWriter(new BufferedWriter(new FileWriter(
					indexFile)));
			indexOut.println(genIndexFileBody(request, infoRpt));
			indexOut.close();
		} catch (IOException e) {

			if (indexOut != null) {
				indexOut.close();
			}
			e.printStackTrace();
		}

	}

	/**
	 * 创建主文件夹
	 * 
	 */
	public static void creatSheetFileDir(String dirPath) {
		File sheetDir = new File(dirPath + fileName + ".files");
		sheetDir.mkdir();
	}

	/**
	 * 创建列表文件filelist.xml 兼容文件，由于office版本改动较大，导致生成文件平台出现问题
	 * 
	 * @throws IOException
	 */
	public static void creatXmlFile(String dirPath) {
		File xmlFiles = new File(dirPath + fileName + ".files/filelist.xml");
		try {
			xmlFiles.createNewFile();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 创建目录文件内容
	 * 
	 * @param reportName
	 * @return
	 */
	public static String genIndexFileBody(HttpServletRequest request,
			RptResourceTable[] infoRpt) {
		StringBuffer indexSB = new StringBuffer("");

		indexSB.append("<html><table border=1>");
		int count = 1;

		if (infoRpt == null || infoRpt.length == 0) {
			indexSB.append("<tr><td rowspan=2 colspan=4 align=center>无报表</td></tr>\n");
		} else {
			for (int i = 0; i < infoRpt.length; i++) {

				String name = infoRpt[i].name;
				String rule = infoRpt[i].rpt_export_rule;
				String[] rule_type = rule.trim().split(",");
				for (int a = 0; rule_type != null && a < rule_type.length; a++) {
					if (BatchExportConst.BATCH_EXPORT_RULE_REGION
							.equalsIgnoreCase(rule_type[a])) {// 区域
						indexSB.append(appendIndex(request, name, count,
								BatchExportConst.BATCH_EXPORT_RULE_REGION_TYPE));
						// String allName = "全省";
						// 全省

						/*
						 * indexSB.append( "<tr>"+ "<td nowarp>" +
						 * "<a href=\"sheet00" + count + ".htm#RANGE!A1\">" +
						 * name //+ //"("+allName+")" + "</a></td>").append(
						 * "<td nowarp></td></tr>\n"); count++; // 各地市 HashMap
						 * cityMap = CodeParamUtil.codeListParamFetcher(
						 * request, "CITY"); String[] cityArr = (String[])
						 * cityMap.keySet().toArray( new
						 * String[cityMap.size()]); Arrays.sort(cityArr); for
						 * (int b = 0; cityArr != null && b < cityArr.length;
						 * b++) { indexSB.append( "<tr>"+ "<td nowarp>" +
						 * "<a href=\"sheet00" + count + ".htm#RANGE!A1\">" +
						 * name + "(" + cityMap.get(cityArr[b]) + ")" +
						 * "</a></td>").append( "<td nowarp></td></tr>\n");
						 * count++; }
						 */

					} else if ("02".equalsIgnoreCase(rule_type[a])) {// 部门

					} else if ("03".equalsIgnoreCase(rule_type[a])) {// 业务

					} else {
						indexSB.append(
								"<tr>" + "<td nowarp>" + "<a href=\"sheet00"
										+ count + ".htm#RANGE!A1\">" + name
										+ "</a></td>").append(
								"<td nowarp></td></tr>\n");
						count++;
					}
				}
			}
		}

		indexSB.append("</table></html>");

		return indexSB.toString();
	}

	/**
	 * 生成报表体
	 * 
	 * @throws IOException
	 * @throws AppException
	 * 
	 */
	public static void creatSheetReportFile(HttpServletRequest request,
			RptResourceTable[] infoRpt, ReportQryStruct qryStruct,
			String dirPath) {

		int index = 1;
		for (int i = 0; i < infoRpt.length; i++) {
			//
			String rule = infoRpt[i].rpt_export_rule;
			String[] rule_type = rule.trim().split(",");
			for (int a = 0; rule_type != null && a < rule_type.length; a++) {
				if (BatchExportConst.BATCH_EXPORT_RULE_REGION
						.equalsIgnoreCase(rule_type[a])) {// 区域

					// BatchExportConst.BATCH_EXPORT_RULE_REGION_TYPE
					// 各地市
					HashMap cityMap = CodeParamUtil.codeListParamFetcher(
							request,
							BatchExportConst.BATCH_EXPORT_RULE_REGION_TYPE);
					String[] cityArr = (String[]) cityMap.keySet().toArray(
							new String[cityMap.size()]);
					Arrays.sort(cityArr);
					// 全省
					index = createSheetFile(
							cloneQryStruct(qryStruct, rule_type[a], ""),
							infoRpt[i], dirPath, index);

					// 各地市
					for (int b = 0; cityArr != null && b < cityArr.length; b++) {
						index = createSheetFile(
								cloneQryStruct(qryStruct, rule_type[a],
										cityArr[b]), infoRpt[i], dirPath, index);

					}

				} else if ("02".equalsIgnoreCase(rule_type[a])) {// 部门

				} else if ("03".equalsIgnoreCase(rule_type[a])) {// 业务

				} else {// 不区分
					index = createSheetFile(qryStruct, infoRpt[i], dirPath,
							index);

				}
			}

		}

	}

	/**
	 * 生成excel sheet页
	 * 
	 * @param qryStruct
	 * @param infoRpt
	 * @param dirPath
	 * @param index
	 * @return
	 */
	public static int createSheetFile(ReportQryStruct qryStruct,
			RptResourceTable infoRpt, String dirPath, int index) {

		PrintWriter sheetWriter = null;

		try {

			File sheet = new File(dirPath + fileName + ".files/sheet00"
					+ (index) + ".htm");

			/**
			 * *****************************************************************
			 * ***
			 */
			sheetWriter = new PrintWriter(new BufferedWriter(new FileWriter(
					sheet)));
			sheetWriter
					.println("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
							+ "xmlns:x=\"urn:schemas-microsoft-com:office:excel\">"
							+ "<head><meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">"
							+ "<meta name=ProgId content=Excel.Sheet>"
							+ "</head>");

			/**
			 * ***********************报表业务逻辑************************************
			 * *****
			 */
			// 定义HTML输出接口
			ILTableService tableService = new LTableServiceImpl();

			//
			String[] html = tableService.getReportHtml(infoRpt.res_id,
					qryStruct);

			/**
			 * ************************报表业务逻辑***********************************
			 * *****
			 */
			for (int j = 0; j < html.length; j++) {
				sheetWriter.println(html[j]);
			}
			sheetWriter.close();

		} catch (IOException e) {
			if (sheetWriter != null) {
				sheetWriter.close();
			}

			e.printStackTrace();
		}

		return ++index;
	}

	/**
	 * 增加索引页内容
	 * 
	 * @param request
	 * @param name
	 * @param count
	 * @param type
	 * @return
	 */
	public static String appendIndex(HttpServletRequest request, String name,
			int count, String type) {
		// String allName = "全省";
		StringBuffer indexSB = new StringBuffer();
		// 全省
		indexSB.append(
				"<tr>" + "<td nowarp>" + "<a href=\"sheet00" + count
						+ ".htm#RANGE!A1\">" + name // + "("+allName+")"
						+ "</a></td>").append("<td nowarp></td></tr>\n");
		count++;
		// 各地市
		// type = "city"
		HashMap cityMap = CodeParamUtil.codeListParamFetcher(request, type);
		String[] cityArr = (String[]) cityMap.keySet().toArray(
				new String[cityMap.size()]);
		Arrays.sort(cityArr);
		for (int b = 0; cityArr != null && b < cityArr.length; b++) {
			indexSB.append(
					"<tr>" + "<td nowarp>" + "<a href=\"sheet00" + count
							+ ".htm#RANGE!A1\">" + name + "("
							+ cityMap.get(cityArr[b]) + ")" + "</a></td>")
					.append("<td nowarp></td></tr>\n");
			count++;
		}

		return indexSB.toString();
	}

	/**
	 * 拷贝查询结构
	 * 
	 * @param qryStruct
	 * @param type
	 * @param value
	 * @return
	 */
	public static ReportQryStruct cloneQryStruct(ReportQryStruct qryStruct,
			String type, String value) {
		ReportQryStruct reQryStruct = (ReportQryStruct) qryStruct.clone();
		if ("01".equals(type)) {
			reQryStruct.city_id = value;
		} else {//

		}
		return reQryStruct;
	}

	/**
	 * 单文件分类导出
	 * 
	 * @param request
	 * @param infoRpt
	 * @param qryStruct
	 * @param operPath
	 */
	public static void exportProcess(HttpServletRequest request,
			RptResourceTable[] infoRpt, ReportQryStruct qryStruct,
			String operPath) {
		// 创建主文件
		createMainFile(request, infoRpt, operPath);
		// 创建内容文件夹
		creatSheetFileDir(operPath);
		// 创建XML配置文件
		creatXmlFile(operPath);
		// 创建索引文件
		createIndexFile(request, infoRpt, operPath);
		// 创建sheet报表
		creatSheetReportFile(request, infoRpt, qryStruct, operPath);
	}

	/**
	 * 执行生成文件,根据分类生成多个文件,落地打包
	 * 
	 * @param request
	 * @param infoRpt
	 * @param qryStruct
	 * @param user_no
	 * @param basePath
	 * @return
	 * @throws AppException
	 */
	public static int excute(HttpServletRequest request,
			RptResourceTable[] infoRpt, ReportQryStruct qryStruct,
			String user_no, String groupTag, String zipDir, String fileName)
			throws AppException {
		// String returnFileName = "";
		int flag = 0;
		try {
			// 初始化存储路径
			String operPath = initDirectoryPath(user_no, zipDir);
			// 删除
			cleanSaveFile(operPath);
			// 创建用户目录
			File newdir1 = new File(operPath);
			newdir1.mkdir();
			// 判断是否落地多个文件
			if (groupTag.equalsIgnoreCase("true")) {
				// 统计分组
				HashSet set = new HashSet();
				for (int i = 0; infoRpt != null && i < infoRpt.length; i++) {
					RptGroupTable[] group = infoRpt[i].rpt_group;
					if (group != null && group.length > 0) {
						for (int j = 0; j < group.length; j++) {
							if ("".equalsIgnoreCase(qryStruct.rpt_type)) {
								set.add(group[j].group_id + ":"
										+ group[j].group_name);
							} else if (qryStruct.rpt_type
									.indexOf(group[j].group_id) > -1) {// 当前选定分组才能够导出
								set.add(group[j].group_id + ":"
										+ group[j].group_name);
							}

						}
					} else {// 没有报表分类，默认特定分类
						set.add("-1:没有归属分组");
					}
				}
				// 统计分组报表对象
				Iterator it = set.iterator();
				while (it.hasNext()) {// 遍历循环生成文件
					String[] value = it.next().toString().split(":");
					/*
					 * if(value[0].equals("-1")){//特殊处理 value[0] = ""; }
					 */
					String groupPath = operPath + "/" + value[1] + "/";
					// 创建分组文件夹
					File groupDir = new File(groupPath);
					groupDir.mkdir();
					// 过滤数据列表
					ArrayList groupList = new ArrayList();
					for (int i = 0; infoRpt != null && i < infoRpt.length; i++) {
						RptGroupTable[] group = infoRpt[i].rpt_group;
						if (group != null && group.length > 0) {
							for (int j = 0; j < group.length; j++) {
								if (group[j].group_id
										.equalsIgnoreCase(value[0])) {
									groupList.add(infoRpt[i]);
									break;
								}

							}
						} else {// 没有就是-1存在
							groupList.add(infoRpt[i]);
						}
					}// end for
					RptResourceTable[] groupTable = (RptResourceTable[]) groupList
							.toArray(new RptResourceTable[groupList.size()]);
					// 导出操作
					exportProcess(request, groupTable, qryStruct, groupPath);
				}

			} else {
				// 遍历循环生成文件
				exportProcess(request, infoRpt, qryStruct, operPath);
			}

			// 压缩文件
			// zip("C:\\batchexport\\1","C:\\batchexport\\1\\BatcheExport.zip");
			ZipUtil.zip(operPath, operPath + fileName);
			// 返回文件
			// returnFileName = operPath+fileName;

		} catch (IOException e) {

			flag = -1;
			e.printStackTrace();
		} catch (Exception e) {

			flag = -1;
			e.printStackTrace();
		}

		return flag;

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
