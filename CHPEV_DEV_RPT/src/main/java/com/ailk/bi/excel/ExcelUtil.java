package com.ailk.bi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.contrib.RegionUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * 主要用于生成Excel文件以便用户进行下载
 * 使用：传入要输出的流，文件名或者工作薄名称，表头对象定义，数据对象类，数据对象数组，数据对象要调用的获取方法列表 要求：数据对象类必须存在获取数据方法
 */
public class ExcelUtil {
	private static Log log = LogFactory.getLog(ExcelUtil.class);

	/**
	 * 字体颜色为普通
	 */
	public static final short FONT_COLOR_NORMAL = HSSFFont.COLOR_NORMAL;
	/**
	 * 字体为红色
	 */
	public static final short FONT_COLOR_RED = HSSFFont.COLOR_RED;
	/**
	 * 默认的工作薄行数
	 */
	private static final int SHEET_DATA_ROWS = 1000;

	/**
	 * 声明一个默认工作薄
	 */
	private static HSSFWorkbook workBook = null;

	private static final String[] letters = new String[] { "A", "B", "C", "D",
			"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private ExcelUtil() {
		// 禁止初始化
	}

	public static int readerExcelNum = -1;

	/**
	 * 根据输入参数定义生成Excel文档，并发送到输出流，这里不负责关闭输出流 此方法不考虑多工作薄模式，因此，数据行数应小于65000
	 * 
	 * @param out
	 *            输出流
	 * @param sheetName
	 *            工作薄名称
	 * @param head
	 *            表头定义
	 * @param values
	 *            数值二位数组
	 * @throws CommnException
	 * @throws IOException
	 */
	public static void toExcel(OutputStream out, String sheetName,
			TableHead head, String[][] values) throws Exception, IOException {
		if (null == out || null == head || null == values) {
			if (log.isDebugEnabled())
				log.debug("input argument is null: "
						+ (out != null ? out : " out is null") + ":"
						+ (head != null ? head : "head is null") + ":"
						+ (values != null ? values : "values is null"));
			throw new Exception("input argument is null: "
					+ (out != null ? out : " out is null") + ":"
					+ (head != null ? head : "head is null") + ":"
					+ (values != null ? values : "values is null"));
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setGridsPrinted(false);
		sheet.setDisplayGridlines(false);

		HSSFDataFormat hssfdf = wb.createDataFormat();

		Map<String, HSSFFont> fontMap = new HashMap<String, HSSFFont>();
		Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		short rowIndex = 0;

		ExcelHeadResult result = genExcelHead(wb, sheet, head);

		rowIndex = result.getRowIndex();
		TableHeadTr lastHeadRow = result.getLastHeadRow();
		// 生成下拉列表
		int startRowIndex = rowIndex;
		int endCol = 0;
		for (int i = 0; i < values.length; i++) {
			HSSFRow row = sheet.createRow(rowIndex);
			short colIndex = 0;
			endCol = endCol < values[i].length ? values[i].length : endCol;
			for (int j = 0; j < values[i].length; j++) {
				String text = values[i][j];
				if (null != lastHeadRow && null != lastHeadRow.getTrSet()
						&& null != lastHeadRow.getTrSet().get(colIndex)) {
					TableHeadTd td = lastHeadRow.getTrSet().get(colIndex);
					String key = td.getFontColor() + "_" + td.getDataAlign();
					if (null != styleMap.get(key)) {
						style = styleMap.get(key);
					} else {
						style = wb.createCellStyle();
						style.setDataFormat(hssfdf.getFormat("@"));
						styleMap.put(key, style);
					}
					if (null != fontMap.get(key))
						cf = fontMap.get(key);
					else {
						cf = wb.createFont();
						fontMap.put(key, cf);
					}
					cf.setColor(td.getFontColor());
					cf.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
					style.setFont(cf);
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

					if (td.isNeedConvert()) {
						// 需要转换
						if (null != td.getTransSet()
								&& null != td.getTransSet().get(text)) {
							text = td.getTransSet().get(text);
						}
					}
					if (null != td.getDataFormat()) {
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								td.getDataFormat());
						text = df.format(text);
					}
					if (null != td.getDataAlign()) {
						String align = td.getDataAlign();
						if ("left".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						else if ("right".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						else
							style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					}
				}
				HSSFCell cell = row.createCell((int) colIndex);
				cell.setCellValue(new HSSFRichTextString(text));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				colIndex++;
			}
			rowIndex++;
		}
		int endRowIndex = rowIndex;
		// 循环一下表头

		if (null != lastHeadRow && null != lastHeadRow.getTrSet()) {
			Iterator<TableHeadTd> iter = lastHeadRow.getTrSet().iterator();
			short colIndex = 0;
			while (iter.hasNext()) {
				TableHeadTd td = iter.next();
				String strFormula = td.getListBoxStr();
				if (!StringUtils.isBlank(strFormula)) {
					// 如果没有双引号
					if (strFormula.indexOf("\"") < 0) {
						strFormula = "\"" + strFormula + "\"";
					}
					HSSFDataValidation dataValidation = createDataValidation(
							sheet, endCol, strFormula, startRowIndex,
							endRowIndex, colIndex, colIndex);
					sheet.addValidationData(dataValidation);
				}
				colIndex++;
			}
		}
		// 调整列宽
		int width=0;
		for (int i = 0; i < values[0].length; i++) {
			sheet.autoSizeColumn(i,true);
			width=sheet.getColumnWidth(i);
			width =(int)(width*1.5);
			sheet.setColumnWidth(i, width);
		}
		wb.write(out);
	}

	/**
	 * 根据输入参数定义生成Excel文档，并发送到输出流，这里不负责关闭输出流
	 * 此方法不考虑多工作薄模式，因此，数据行数应小于65000。另外要注意数据的大小一致
	 * 
	 * @param out
	 *            excel 输出流
	 * @param sheetName
	 *            工作薄名称
	 * @param head
	 *            表头对象
	 * @param clazz
	 *            数据对象类
	 * @param objs
	 *            数据对象数组
	 * @param methods
	 *            数据获取方法
	 * @throws CommnException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static void toExcel(OutputStream out, String sheetName,
			TableHead head, Class clazz, Object[] objs, List<String> methods)
			throws Exception, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IOException {
		if (null == out || null == head || null == clazz || null == objs
				|| null == methods) {
			if (log.isDebugEnabled())
				log.debug("input argument is null: "
						+ (out != null ? out : " out is null") + ":"
						+ (head != null ? head : "head is null") + ":"
						+ (clazz != null ? clazz : "clazz is null") + ":"
						+ (objs != null ? objs : "objs is null") + ":"
						+ (methods != null ? methods : "methods is null"));
			throw new Exception("input argument is null: "
					+ (out != null ? out : " out is null") + ":"
					+ (head != null ? head : "head is null") + ":"
					+ (clazz != null ? clazz : "clazz is null") + ":"
					+ (objs != null ? objs : "objs is null") + ":"
					+ (methods != null ? methods : "methods is null"));
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		HSSFDataFormat hssfdf = wb.createDataFormat();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		Map<Integer, HSSFFont> fontMap = new HashMap<Integer, HSSFFont>();
		Map<Integer, HSSFCellStyle> styleMap = new HashMap<Integer, HSSFCellStyle>();
		short rowIndex = 0;
		int endCol = methods.size();
		ExcelHeadResult result = genExcelHead(wb, sheet, head);

		rowIndex = result.getRowIndex();
		TableHeadTr lastHeadRow = result.getLastHeadRow();
		int startRowIndex = rowIndex;
		// 开始处理表体,可能还需要处理单元格样式,包括数据转换，如省份，小数位数等
		Object obj = null;
		for (int i = 0; i < objs.length; i++) {
			HSSFRow row = sheet.createRow(rowIndex);
			Iterator iter = methods.iterator();
			short colIndex = 0;
			while (iter.hasNext()) {
				String methodName = (String) iter.next();
				Method method = clazz.getDeclaredMethod(methodName,
						(Class[]) null);
				obj = method.invoke(objs[i], (Object[]) null);
				String text = obj != null ? obj.toString() : "";
				obj = null;
				if (null != lastHeadRow && null != lastHeadRow.getTrSet()
						&& null != lastHeadRow.getTrSet().get(colIndex)) {
					TableHeadTd td = lastHeadRow.getTrSet().get(colIndex);
					Integer key = Integer.valueOf(td.getFontColor());
					if (null != styleMap.get(key)) {
						style = styleMap.get(key);
					} else {
						style = wb.createCellStyle();
						style.setDataFormat(hssfdf.getFormat("@"));
						styleMap.put(key, style);
					}
					if (null != fontMap.get(key))
						cf = fontMap.get(key);
					else {
						cf = wb.createFont();
						fontMap.put(key, cf);
					}
					cf.setColor(td.getFontColor());
					cf.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
					style.setFont(cf);
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					if (td.isNeedConvert()) {
						// 需要转换
						if (null != td.getTransSet()
								&& null != td.getTransSet().get(text)) {
							text = td.getTransSet().get(text);
						}
					}
					if (null != td.getDataFormat()) {
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								td.getDataFormat());
						text = df.format(text);
					}
					if (null != td.getDataAlign()) {
						String align = td.getDataAlign();
						if ("left".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						else if ("right".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						else
							style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					}
				}
				HSSFCell cell = row.createCell((int) colIndex);
				cell.setCellValue(new HSSFRichTextString(text));
				cell.setCellStyle(style);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				colIndex++;
			}
			rowIndex++;
		}
		int endRowIndex = rowIndex;
		// 循环一下表头

		if (null != lastHeadRow && null != lastHeadRow.getTrSet()) {
			Iterator<TableHeadTd> iter = lastHeadRow.getTrSet().iterator();
			short colIndex = 0;
			while (iter.hasNext()) {
				TableHeadTd td = iter.next();
				String strFormula = td.getListBoxStr();
				if (!StringUtils.isBlank(strFormula)) {
					// 如果没有双引号
					if (strFormula.indexOf("\"") < 0) {
						strFormula = "\"" + strFormula + "\"";
					}
					HSSFDataValidation dataValidation = createDataValidation(
							sheet, endCol, strFormula, startRowIndex,
							endRowIndex, colIndex, colIndex);
					sheet.addValidationData(dataValidation);
				}
				colIndex++;
			}
		}
		wb.write(out);
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static void toExcel(OutputStream out, String sheetName,
			TableHead head, Object[] objs) throws Exception, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {
		if (null == out || null == head || null == objs) {
			if (log.isDebugEnabled())
				log.debug("input argument is null: "
						+ (out != null ? out : " out is null") + ":"
						+ (head != null ? head : "head is null") + ":" + ":"
						+ (objs != null ? objs : "objs is null") + ":");
			throw new Exception("input argument is null: "
					+ (out != null ? out : " out is null") + ":"
					+ (head != null ? head : "head is null") + ":" + ":"
					+ (objs != null ? objs : "objs is null") + ":");
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		HSSFDataFormat hssfdf = wb.createDataFormat();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		Map<Integer, HSSFFont> fontMap = new HashMap<Integer, HSSFFont>();
		Map<Integer, HSSFCellStyle> styleMap = new HashMap<Integer, HSSFCellStyle>();
		short rowIndex = 0;
		int endCol = 0;
		ExcelHeadResult result = genExcelHead(wb, sheet, head);
		rowIndex = result.getRowIndex();
		TableHeadTr lastHeadRow = result.getLastHeadRow();
		int startRowIndex = rowIndex;
		endCol = lastHeadRow.getTrSet().size();
		Class clazz = null;
		Object obj = null;
		// 开始处理表体,可能还需要处理单元格样式,包括数据转换，如省份，小数位数等
		for (int i = 0; i < objs.length; i++) {
			HSSFRow row = sheet.createRow(rowIndex);

			Iterator iter = lastHeadRow.getTrSet().iterator();
			short colIndex = 0;
			while (iter.hasNext()) {
				TableHeadTd tableHeadTd = (TableHeadTd) iter.next();
				String methodName = tableHeadTd.getDataMethod();
				clazz = objs[i].getClass();

				Method method = clazz.getDeclaredMethod(methodName, tableHeadTd
						.getDataMethodParameterTypes());
				// System.out.println(" method name " + methodName + " m : " +
				// method);
				obj = method.invoke(objs[i], tableHeadTd
						.getDataMethodParameters());
				String text = obj != null ? obj.toString() : "";
				obj = null;
				if (null != lastHeadRow && null != lastHeadRow.getTrSet()
						&& null != lastHeadRow.getTrSet().get(colIndex)) {
					TableHeadTd td = lastHeadRow.getTrSet().get(colIndex);
					Integer key = Integer.valueOf(td.getFontColor());
					if (null != styleMap.get(key)) {
						style = styleMap.get(key);
					} else {
						style = wb.createCellStyle();
						style.setDataFormat(hssfdf.getFormat("@"));
						styleMap.put(key, style);
					}
					if (null != fontMap.get(key))
						cf = fontMap.get(key);
					else {
						cf = wb.createFont();
						fontMap.put(key, cf);
					}
					cf.setColor(td.getFontColor());
					cf.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
					style.setFont(cf);
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					if (td.isNeedConvert()) {
						// 需要转换
						if (null != td.getTransSet()) {
							// 将excel中取不到数据的映射关系为空串
							if (null == td.getTransSet().get(text)) {
								text = "";
							} else {
								text = td.getTransSet().get(text);
							}
						}
					}
					if (null != td.getDataFormat()) {
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								td.getDataFormat());
						text = df.format(text);
					}
					if (null != td.getDataAlign()) {
						String align = td.getDataAlign();
						if ("left".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						else if ("right".equalsIgnoreCase(align))
							style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						else
							style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					}
				}
				HSSFCell cell = row.createCell((int) colIndex);
				cell.setCellValue(new HSSFRichTextString(text));
				cell.setCellStyle(style);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				colIndex++;
			}
			rowIndex++;
		}
		int endRowIndex = rowIndex;
		// 循环一下表头

		if (null != lastHeadRow && null != lastHeadRow.getTrSet()) {
			Iterator<TableHeadTd> iter = lastHeadRow.getTrSet().iterator();
			short colIndex = 0;
			while (iter.hasNext()) {
				TableHeadTd td = iter.next();
				String strFormula = td.getListBoxStr();
				if (!StringUtils.isBlank(strFormula)) {
					// 如果没有双引号
					if (strFormula.indexOf("\"") < 0) {
						strFormula = "\"" + strFormula + "\"";
					}
					HSSFDataValidation dataValidation = createDataValidation(
							sheet, endCol, strFormula, startRowIndex,
							endRowIndex, colIndex, colIndex);
					sheet.addValidationData(dataValidation);
				}
				colIndex++;
			}
		}
		wb.write(out);
	}

	/**
	 * 根据输入参数定义生成Excel文档，并发送到输出流，这里不负责关闭输出流 此方法考虑了多工作薄模式。另外要注意数据的大小一致
	 * 
	 * @param out
	 *            excel 输出流
	 * @param sheetName
	 *            工作薄名称
	 * @param head
	 *            表头对象
	 * @param clazz
	 *            数据对象类
	 * @param objs
	 *            数据对象数组
	 * @param methods
	 *            数据获取方法
	 * @param sheetDataRows
	 *            每个工作薄的数据行数
	 * @throws CommnException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static void toExcelMultiSheet(OutputStream out, String sheetName,
			TableHead head, Class clazz, Object[] objs, List<String> methods,
			int sheetDataRows) throws Exception, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {
		if (null == out || null == head || null == clazz || null == objs
				|| null == methods) {
			if (log.isDebugEnabled())
				log.debug("input argument is null: "
						+ (out != null ? out : " out is null") + ":"
						+ (head != null ? head : "head is null") + ":"
						+ (clazz != null ? clazz : "clazz is null") + ":"
						+ (objs != null ? objs : "objs is null") + ":"
						+ (methods != null ? methods : "methods is null"));
			throw new Exception("input argument is null: "
					+ (out != null ? out : " out is null") + ":"
					+ (head != null ? head : "head is null") + ":"
					+ (clazz != null ? clazz : "clazz is null") + ":"
					+ (objs != null ? objs : "objs is null") + ":"
					+ (methods != null ? methods : "methods is null"));
		}
		if (sheetDataRows <= 0)
			sheetDataRows = SHEET_DATA_ROWS;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFDataFormat hssfdf = wb.createDataFormat();

		int totalRows = objs.length;
		int sheetNum = totalRows / sheetDataRows + 1;
		int endCol = methods.size();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		Map<Integer, HSSFFont> fontMap = new HashMap<Integer, HSSFFont>();
		Map<Integer, HSSFCellStyle> styleMap = new HashMap<Integer, HSSFCellStyle>();
		for (int sht = 0; sht < sheetNum; sht++) {
			HSSFSheet sheet = wb.createSheet(sheetName + (sht + 1));

			short rowIndex = 0;

			ExcelHeadResult result = genExcelHead(wb, sheet, head);

			rowIndex = result.getRowIndex();
			TableHeadTr lastHeadRow = result.getLastHeadRow();
			int startRowIndex = rowIndex;
			// 开始处理表体,可能还需要处理单元格样式,包括数据转换，如省份，小数位数等
			int start = sht * sheetDataRows;
			int end = ((sht + 1) * sheetDataRows > objs.length ? objs.length
					: (sht + 1) * sheetDataRows);
			for (int i = start; i < end; i++) {
				HSSFRow row = sheet.createRow(rowIndex);
				Iterator iter = methods.iterator();
				short colIndex = 0;
				while (iter.hasNext()) {
					String methodName = (String) iter.next();
					Method method = clazz.getDeclaredMethod(methodName,
							(Class[]) null);
					String text = (String) method.invoke(objs[i],
							(Object[]) null);
					if (null != lastHeadRow && null != lastHeadRow.getTrSet()
							&& null != lastHeadRow.getTrSet().get(colIndex)) {
						TableHeadTd td = lastHeadRow.getTrSet().get(colIndex);
						Integer key = Integer.valueOf(td.getFontColor());
						if (null != styleMap.get(key)) {
							style = styleMap.get(key);
						} else {
							style = wb.createCellStyle();
							style.setDataFormat(hssfdf.getFormat("@"));
							styleMap.put(key, style);
						}
						if (null != fontMap.get(key))
							cf = fontMap.get(key);
						else {
							cf = wb.createFont();
							fontMap.put(key, cf);
						}
						cf.setColor(td.getFontColor());
						cf.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
						style.setFont(cf);
						style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						if (td.isNeedConvert()) {
							// 需要转换
							if (null != td.getTransSet()
									&& null != td.getTransSet().get(text)) {
								text = td.getTransSet().get(text);
							}
						}
						if (null != td.getDataFormat()) {
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									td.getDataFormat());
							text = df.format(text);
						}
						if (null != td.getDataAlign()) {
							String align = td.getDataAlign();
							if ("left".equalsIgnoreCase(align))
								style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							else if ("right".equalsIgnoreCase(align))
								style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
							else
								style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						}
					}
					HSSFCell cell = row.createCell((int) colIndex);
					cell.setCellValue(new HSSFRichTextString(text));
					cell.setCellStyle(style);
					style.setBorderTop(HSSFCellStyle.BORDER_THIN);
					style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					style.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					colIndex++;
				}
				rowIndex++;
			}
			int endRowIndex = rowIndex;
			// 循环一下表头

			if (null != lastHeadRow && null != lastHeadRow.getTrSet()) {
				Iterator<TableHeadTd> iter = lastHeadRow.getTrSet().iterator();
				short colIndex = 0;
				while (iter.hasNext()) {
					TableHeadTd td = iter.next();
					String strFormula = td.getListBoxStr();
					if (!StringUtils.isBlank(strFormula)) {
						// 如果没有双引号
						if (strFormula.indexOf("\"") < 0) {
							strFormula = "\"" + strFormula + "\"";
						}
						HSSFDataValidation dataValidation = createDataValidation(
								sheet, endCol, strFormula, startRowIndex,
								endRowIndex, colIndex, colIndex);
						sheet.addValidationData(dataValidation);
					}
					colIndex++;
				}
			}
		}
		wb.write(out);
	}
	
/**
 * 	新增
 * @param out
 * @param sheetName
 * @param head
 * @param values
 * @param sheetDataRows
 * @throws Exception
 * @throws IOException
 */
	public static void toExcelMultiSheet(OutputStream out, String sheetName,
			TableHead head, String[][] values,int sheetDataRows) throws Exception, IOException {
		if (null == out || null == head || null == values) {
			if (log.isDebugEnabled())
				log.debug("input argument is null: "
						+ (out != null ? out : " out is null") + ":"
						+ (head != null ? head : "head is null") + ":"
						+ (values != null ? values : "values is null"));
			throw new Exception("input argument is null: "
					+ (out != null ? out : " out is null") + ":"
					+ (head != null ? head : "head is null") + ":"
					+ (values != null ? values : "values is null"));
		}
		
		if (sheetDataRows <= 0)
			sheetDataRows = SHEET_DATA_ROWS;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFDataFormat hssfdf = wb.createDataFormat();

		int totalRows = values.length;
		int sheetNum = totalRows / sheetDataRows + 1;
		
		//int endCol = methods.size();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		Map<String, HSSFFont> fontMap = new HashMap<String, HSSFFont>();
		Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
		for (int sht = 0; sht < sheetNum; sht++) {
			String tab_name="";
			if(sheetNum==1){
				tab_name=sheetName;
			} else{
				tab_name=sheetName+"("+(sht + 1)+")";
			}
			HSSFSheet sheet = wb.createSheet(tab_name);	
			
			sheet.setGridsPrinted(false);
			sheet.setDisplayGridlines(false);
			short rowIndex = 0;
			ExcelHeadResult result = genExcelHead(wb, sheet, head);

			rowIndex = result.getRowIndex();
			TableHeadTr lastHeadRow = result.getLastHeadRow();
			// 生成下拉列表
			int startRowIndex = rowIndex;
			int endCol = 0;
			int rowLength = sheetDataRows*(sht+1)<=totalRows?sheetDataRows*(sht+1):totalRows;
			for (int i = sheetDataRows*sht; i < rowLength; i++) {
				HSSFRow row = sheet.createRow(rowIndex);
				short colIndex = 0;
				endCol = endCol < values[i].length ? values[i].length : endCol;
				for (int j = 0; j < values[i].length; j++) {
					String text = values[i][j];
					if (null != lastHeadRow && null != lastHeadRow.getTrSet()
							&& null != lastHeadRow.getTrSet().get(colIndex)) {
						TableHeadTd td = lastHeadRow.getTrSet().get(colIndex);
						String key = td.getFontColor() + "_" + td.getDataAlign();
						if (null != styleMap.get(key)) {
							style = styleMap.get(key);
						} else {
							style = wb.createCellStyle();
							style.setDataFormat(hssfdf.getFormat("@"));
							styleMap.put(key, style);
						}
						if (null != fontMap.get(key))
							cf = fontMap.get(key);
						else {
							cf = wb.createFont();
							fontMap.put(key, cf);
						}
						cf.setColor(td.getFontColor());
						cf.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
						style.setFont(cf);
						style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

						if (td.isNeedConvert()) {
							// 需要转换
							if (null != td.getTransSet()
									&& null != td.getTransSet().get(text)) {
								text = td.getTransSet().get(text);
							}
						}
						if (null != td.getDataFormat()) {
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									td.getDataFormat());
							text = df.format(text);
						}
						if (null != td.getDataAlign()) {
							String align = td.getDataAlign();
							if ("left".equalsIgnoreCase(align))
								style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							else if ("right".equalsIgnoreCase(align))
								style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
							else
								style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						}
					}
					HSSFCell cell = row.createCell((int) colIndex);
					cell.setCellValue(new HSSFRichTextString(text));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(style);
					style.setBorderTop(HSSFCellStyle.BORDER_THIN);
					style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					style.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					colIndex++;
				}
				rowIndex++;
			}
			int endRowIndex = rowIndex;
			// 循环一下表头

			if (null != lastHeadRow && null != lastHeadRow.getTrSet()) {
				Iterator<TableHeadTd> iter = lastHeadRow.getTrSet().iterator();
				short colIndex = 0;
				while (iter.hasNext()) {
					TableHeadTd td = iter.next();
					String strFormula = td.getListBoxStr();
					if (!StringUtils.isBlank(strFormula)) {
						// 如果没有双引号
						if (strFormula.indexOf("\"") < 0) {
							strFormula = "\"" + strFormula + "\"";
						}
						HSSFDataValidation dataValidation = createDataValidation(
								sheet, endCol, strFormula, startRowIndex,
								endRowIndex, colIndex, colIndex);
						sheet.addValidationData(dataValidation);
					}
					colIndex++;
				}
			}
			// 调整列宽
			int width=0;
			for (int i = 0; i < values[0].length; i++) {
				sheet.autoSizeColumn(i,true);
				width=sheet.getColumnWidth(i);
				width =(int)(width*1.5);
				sheet.setColumnWidth(i, width);
			}
		}
		wb.write(out);
	}
	/**
	 * 生成Excel表头
	 * 
	 * @param sheet
	 *            工作薄
	 * @param style
	 *            样式对象
	 * @param cf
	 *            字体对象
	 * @param head
	 *            表头对象
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	private static ExcelHeadResult genExcelHead(HSSFWorkbook wb,
			HSSFSheet sheet, TableHead head) {
		ExcelHeadResult result = ExcelHeadResult.getInstance();

		HSSFDataFormat hssfdf = wb.createDataFormat();
		HSSFCellStyle style = null;
		HSSFFont cf = null;
		Map<Integer, HSSFFont> fontMap = new HashMap<Integer, HSSFFont>();
		Map<Integer, HSSFCellStyle> styleMap = new HashMap<Integer, HSSFCellStyle>();
		short rowIndex = 2;

		List regions = new ArrayList();
		// 开始生成表头
		TableHeadTr lastHeadRow = null;
		List headTrs = head.getHead();
		Iterator iter = headTrs.iterator();
		while (iter.hasNext()) {
			TableHeadTr tr = (TableHeadTr) iter.next();
			if (tr.isLastRow())
				lastHeadRow = tr;
			HSSFRow row = sheet.createRow(rowIndex);
			List tds = tr.getTrSet();
			Iterator tdIter = tds.iterator();
			short colIndex = 0;
			while (tdIter.hasNext()) {
				// 列循环
				TableHeadTd td = (TableHeadTd) tdIter.next();
				HSSFCell cell = row.createCell((int) colIndex);
				cell.setCellValue(new HSSFRichTextString(td.getText()));
				Integer key = Integer.valueOf(td.getFontColor());
				if (null != styleMap.get(key)) {
					style = styleMap.get(key);
				} else {
					style = wb.createCellStyle();
					style.setDataFormat(hssfdf.getFormat("@"));
					styleMap.put(key, style);
				}
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style.setWrapText(false);
				if (null != fontMap.get(key))
					cf = fontMap.get(key);
				else {
					cf = wb.createFont();
					fontMap.put(key, cf);
				}
				cf.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				cf.setColor(td.getFontColor());
				style.setFont(cf);
				cell.setCellStyle(style);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				int rowspan = td.getRowSpan();
				int colspan = td.getColSpan();
				boolean needMergedRegion = false;
				if (rowspan != 0) {
					rowspan = rowIndex + rowspan - 1;
					needMergedRegion = true;
				} else
					rowspan = rowIndex;
				if (colspan != 0) {
					colspan = colspan + colIndex - 1;
					needMergedRegion = true;
				} else
					colspan = colIndex;
				if (needMergedRegion) {
					regions.add(new CellRangeAddress(rowIndex, rowspan,
							colIndex, (colspan)));
					if (colspan > 2)
						colIndex += td.getColSpan() - 1;
				}
				colIndex++;
			}
			rowIndex++;
		}
		// 此时再加合并区
		CellRangeAddress region = null;
		iter = regions.iterator();
		while (iter.hasNext()) {
			region = (CellRangeAddress) (iter.next());
			RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, region,
					sheet, wb);
			RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, region, sheet,
					wb);
			RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, region, sheet,
					wb);
			RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, region, sheet,
					wb);
			sheet.addMergedRegion(region);

		}
		result.setRowIndex(rowIndex);
		result.setLastHeadRow(lastHeadRow);
		return result;
	}

	/**
	 * 返回一个EXCEL文件某个工作薄的总行数
	 * 
	 * @param in
	 * @param sheetName
	 *            工作薄名字
	 * @return
	 * @throws CommnException
	 * @throws IOException
	 */
	public static int getRowCount(String sheetName) throws Exception,
			IOException {
		if (null == workBook)
			return 0;
		HSSFSheet sheet = workBook.getSheet(sheetName);
		if (null == sheet)
			return 0;
		return sheet.getLastRowNum() + 1;
	}

	/**
	 * 读取EXCEL时初始化
	 * 
	 * @param in
	 * @throws IOException
	 */
	public static void readInit(InputStream in) throws IOException {
		workBook = new HSSFWorkbook(in);
	}

	/**
	 * 读取完毕关闭
	 * 
	 * @throws IOException
	 */
	public static void readDestory() throws IOException {
		workBook = null;
	}

	/**
	 * 获取Excel某个工作薄的行数
	 * 
	 * @param in
	 * @param sheetIndex
	 *            工作薄的索引号，以0开始
	 * @return
	 * @throws CommnException
	 * @throws IOException
	 */
	public static int getRowCount(int sheetIndex) throws Exception, IOException {
		if (null == workBook)
			return 0;
		HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
		if (null == sheet)
			return 0;
		return sheet.getLastRowNum() + 1;
	}

	/**
	 * 读取指定工作薄的指定行内容
	 * 
	 * @param in
	 * @param sheetIndex
	 * @param lineNum
	 *            注意行号从0开始
	 * @return
	 * @throws IOException
	 */
	public static String[] readExcelLine(int sheetIndex, int lineNum)
			throws IOException {
		if (sheetIndex < 0 || lineNum < 0)
			return null;
		HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
		String[] strExcelLine = null;
		sheet = workBook.getSheetAt(sheetIndex);
		HSSFRow row = sheet.getRow(lineNum);

		int cellCount = row.getLastCellNum();
		strExcelLine = new String[cellCount + 1];
		for (int i = 0; i <= cellCount; i++) {
			strExcelLine[i] = readStringExcelCell(sheet, lineNum, i);
		}
		return strExcelLine;
	}

	/**
	 * 读取指定工作薄的指定行指定列的内容
	 * 
	 * @param in
	 * @param sheetIndex
	 * @param rowNum
	 * @param cellNum
	 * @return
	 * @throws IOException
	 */
	public static String readStringExcelCell(int sheetIndex, int rowNum,
			int cellNum) throws IOException {
		if (sheetIndex < 0 || rowNum < 0)
			return "";
		HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
		String strExcelCell = readStringExcelCell(sheet, rowNum, cellNum);
		return strExcelCell;
	}

	/**
	 * 读取工作薄的指定行指定列的单元格内容
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 * @return
	 * @throws IOException
	 */
	private static String readStringExcelCell(HSSFSheet sheet, int rowNum,
			int cellNum) throws IOException {
		if (cellNum < 0 || rowNum < 0)
			return "";
		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(cellNum);
		String strExcelCell = "";
		if (cell != null) { // add this condition
			// judge
			switch (row.getCell(cellNum).getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				strExcelCell = "FORMULA ";
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				double d = cell.getNumericCellValue();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					strExcelCell = df.format(cal.getTime());
				} else

					strExcelCell = new BigDecimal(row.getCell(cellNum)
							.getNumericCellValue()).toString();

				break;
			case HSSFCell.CELL_TYPE_STRING:
				strExcelCell = row.getCell(cellNum).getRichStringCellValue()
						.getString();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				strExcelCell = "";
				break;
			default:
				strExcelCell = "";
				break;
			}
		}
		return strExcelCell;
	}

	private static String colIndexToLetter(int colIndex) {
		StringBuffer sb = new StringBuffer();
		int iterCount = colIndex / 26;
		int modCount = colIndex % 26;
		if (iterCount > 0) {
			sb.append(letters[iterCount - 1]);
		}
		sb.append(letters[modCount]);
		if (log.isDebugEnabled()) {
			log.debug(colIndex + " toLetter := " + sb);
		}
		return sb.toString();
	}

	private static HSSFDataValidation createDataValidation(HSSFSheet sheet,
			int maxCol, String strFormula, int startRow, int endRow,
			int startCol, int endCol) throws UnsupportedEncodingException {
		DVConstraint constraint = null;

		CellRangeAddressList regions = new CellRangeAddressList(startRow,
				endRow, startCol, endCol);

		// 此处不能大于255个字符，不知道POI为什么这么规定
		// 如果小于就直接生产吧，如果大于那得先写到某行某列以便使用吧
		if (strFormula.getBytes("UTF-8").length < 255) {
			constraint = DVConstraint.createFormulaListConstraint(strFormula);
		} else {
			// 需要写东西了
			String split = strFormula;
			// 去除两个双引号
			split = split.replaceAll("\"", "");
			String[] splits = split.split(",");
			if (null != splits) {
				int rowIndex = 0;
				short colIndex = (short) (maxCol + 1);
				for (int i = 0; i < splits.length; i++) {
					// 开始循环生产单元格
					HSSFRow row = sheet.createRow(rowIndex);
					HSSFCell cell = row.createCell((int) colIndex);
					cell.setCellValue(new HSSFRichTextString(splits[i]));
					rowIndex++;
				}
				String colLetter = colIndexToLetter(colIndex);
				sheet.setColumnHidden((int) colIndex, true);
				constraint = DVConstraint.createFormulaListConstraint("$"
						+ colLetter + "$1:$" + colLetter + "$" + rowIndex);
			}
		}

		HSSFDataValidation dataValidation = new HSSFDataValidation(regions,
				constraint);
		dataValidation.setEmptyCellAllowed(false);
		dataValidation.setShowPromptBox(false);
		dataValidation.createErrorBox("Input error!",
				"Pls. select proper value!");
		return dataValidation;
	}

	static class ExcelHeadResult {
		private static ExcelHeadResult result = null;
		private short rowIndex = 0;
		private TableHeadTr lastHeadRow = null;

		static ExcelHeadResult getInstance() {
			if (null == result)
				result = new ExcelHeadResult();
			return result;
		}

		public short getRowIndex() {
			return rowIndex;
		}

		public void setRowIndex(short rowIndex) {
			this.rowIndex = rowIndex;
		}

		public TableHeadTr getLastHeadRow() {
			return lastHeadRow;
		}

		public void setLastHeadRow(TableHeadTr lastHeadRow) {
			this.lastHeadRow = lastHeadRow;
		}
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @param is
	 *            文件输入流
	 * @param decollator
	 *            读取拼接的符号,默认为 :
	 * @return 拼接成的字符串数组
	 * @throws IOException
	 */
	public static List<String> read2003Excel(InputStream is, String decollator)
			throws IOException {
		List<String> list = new LinkedList<String>();
		HSSFWorkbook hwb = new HSSFWorkbook(is);

		HSSFSheet sheet = hwb.getSheetAt(0);

		String value = null;

		HSSFRow row = null;

		HSSFCell cell = null;
		if (decollator == null) {
			decollator = ":";
		}
		// 行遍历
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			// List<Object> linked = new LinkedList<Object>();
			StringBuffer sb = new StringBuffer();
			// 列遍历
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String

				// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00"); // 格式化数字
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				// linked.add(value);
				sb.append(value).append(decollator);// 添加读取值
			}
			// 表头标识,只用于本次数据处理,若需要其它数据格式，修改相应位置即可
			if (i == 0) {
				sb.append("title");
			} else {
				sb.append("|");
			}
			list.add(sb.toString());
		}
		return list;
	}

	/**
	 * 
	 * @param is
	 *            输入流
	 * @param decollator
	 *            分割符
	 * @return HashMap,该集合中使用指定键:size,可获取读取Excel实际数据行数,值为数值型
	 * @throws IOException
	 */
	public static Map<String, String> read2003ExcelToMap(InputStream is,
			String decollator) throws IOException {
		/**
		 * 知识说明：在此映射中关联指定值与指定键。如果该映射以前包含了一个该键的映射关系，则旧值被替换。 所有不会出现重复key
		 */
		Map<String, String> map = new HashMap<String, String>();
		String key = "";
		String values = "";
		int count = 0; // Excel实际多少行数据
		HSSFWorkbook hwb = new HSSFWorkbook(is);

		HSSFSheet sheet = hwb.getSheetAt(0); // 获取工作薄第0个sheet
		String value = null;

		HSSFRow row = null;

		HSSFCell cell = null;
		if (decollator == null) {
			decollator = ":";
		}
		// 行遍历
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
			// 列遍历
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String

				// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0"); // 格式化数字
				// if (cell == null) {
				// value = "BATCH" + i;
				// } else {
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					value = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				// }
				if (j == 0) {
					key = value;

					count++;
				} else if (j == 1) {
					values = sb.append(value).toString();
				}
			}
			map.put(key, values);
		}
		/*
		 * 将原集合key为空值项删除，并添加到新的集合中
		 */
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			Set<Entry<String, String>> set = map.entrySet();
			for (Entry<String, String> str : set) {
				if (!"".equals(str.getKey().trim())) {
					returnMap.put(str.getKey(), str.getValue());
				} else {
					count--;
				}
			}
			// Excel实际多上行数据
			returnMap.put("size", String.valueOf(count));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	/**
	 * 返回生成的工作薄对象
	 * 
	 * @return
	 */
	public static HSSFWorkbook getWorkBook() {

		if (workBook != null) {
			return workBook;
		}
		return null;
	}

	/**
	 * 读取 office 2003 excel - 单元格为空时添加分隔符
	 * 
	 * @param is
	 *            文件输入流
	 * @param decollator
	 *            读取拼接的符号,默认为 :
	 * @return 拼接成的字符串数组
	 * @throws IOException
	 */
	public static List<String> read2003ExcelNotNull(InputStream is,
			String decollator) throws Exception {
		List<String> list = new LinkedList<String>();
		HSSFWorkbook hwb = new HSSFWorkbook(is);
		HSSFSheet sheet = hwb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;

		if (decollator == null) {
			decollator = ":";
		}
		// 行遍历
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
			// 列遍历
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					sb.append(decollator);
					continue;
				} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					// 日期格式化处理
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");// 格式化日期字符串
						sb.append(sdf.format(cell.getDateCellValue()) + "")
								.append(decollator);
						continue;
					}
				}
				sb.append(cell.toString()).append(decollator);// 添加读取值
			}
			list.add(sb.toString());
		}
		return list;
	}

	// public boolean getValue(HSSFCell cellval){
	// cellval.getRichStringCellValue();
	//
	// }

	public static void main(String[] args) {
		System.out.println("".equals("   ".trim()));
	}

	/**
	 * 创建xls或者csv文件公共方法 单个excel页
	 * 
	 * @param fileName
	 *            输出文件名包含路径
	 * @param sheets
	 *            sheet页集合
	 * @param data
	 *            每个sheet页
	 * @param headIndex
	 *            对应每个excel的下标索引数值
	 * @throws Exception
	 * @author shenfl 添加读取Excel的模块方法
	 */
	public static void recordFile(OutputStream out, String fileName,
			List<String> sheets, List<List<Map<String, Object>>> data,
			int headIndex, HSSFWorkbook workbook) throws Exception {
		// 创建xls文件
		HSSFWorkbook wk = workbook;
		// 创建sheet
		if (sheets.size() != data.size()) {
			throw new Exception("sheet页与数据数量项不一致");
		}
		HSSFSheet sht = null;
		HSSFRow xlsRow = null;
		List<Map<String, Object>> weap = null;
		Map<String, Object> row = null;
		Set<String> keySet = null;

		for (int i = 0; i < sheets.size(); i++) {
			// 取得当前的sheet页
			sht = wk.getSheetAt(i);
			// 设置样式
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			// 得到当前数据源
			weap = data.get(i);
			int cellNum = 0;
			// 循环数据，依次填充到Excel中
			if (headIndex < weap.size() + headIndex) {
				keySet = weap.get(weap.size() - 1).keySet();
			}
			// 创建行数据 headIndex = 4; 5
			for (int j = 1 + headIndex; j <= weap.size() + headIndex; j++) {
				// 创建xls的索引
				xlsRow = sht.createRow(j - 1);
				// 得到要填充内容行
				row = weap.get(j - headIndex - 1);
				cellNum = 0;
				// 创建单元格
				for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
					HSSFCell datecell = xlsRow.createCell(cellNum);
					datecell.setCellValue(new HSSFRichTextString(row.get(it
							.next())
							+ ""));
					datecell.setCellStyle(cellStyle);
					cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
					datecell.setCellType(HSSFCell.CELL_TYPE_STRING);
					++cellNum;
				}
			}
		}
		wk.write(out);
	}
}
