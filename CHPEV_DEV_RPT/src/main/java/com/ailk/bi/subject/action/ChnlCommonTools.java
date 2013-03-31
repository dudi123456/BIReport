package com.ailk.bi.subject.action;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;

import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.excel.ExcelUtil;
import com.ailk.bi.excel.TableHead;
import com.ailk.bi.excel.TableHeadTd;
import com.ailk.bi.excel.TableHeadTr;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 * 
 * 
 * 
 * 
 */
public class ChnlCommonTools {

	private static String sql = "";

	public static String sortIdToProvCode(String sort_id) {
		String roleStr = "";
		try {
			sql = SQLGenator.genSQL("SortToProv", sort_id);
			System.out.println(sql);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; arr != null && i < arr.length; i++) {
				roleStr = arr[i][i];
				System.out.println(arr[i][i]);
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		System.out.println(roleStr);
		return roleStr;
	}
	
	public static String provCodeToAreaId(String metro_id) {
		String geo_flag="";
		String sql_str="select distinct(a.geo_flag) from ST_DIM_AREA_PROVINCE a where a.province_code='"+ metro_id+"' ";
		try {
			String[][] flags=WebDBUtil.execQryArray(sql_str,"");
			if(flags!=null&&flags.length>0){
				geo_flag=flags[0][0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geo_flag;
	}

	public static void dataToExcle(OutputStream out, String exportId,
			String[] sqlargs, String sheetName) throws Exception, IOException {
		try {
			String stringHead = "";
			String dataSql = "";
			String headSql = SQLGenator.genSQL("ExportTabData", exportId);
			System.out.println(headSql);

			String[][] xml_data = WebDBUtil.execQryArray(headSql, "");
			if (xml_data != null) {
				stringHead = xml_data[0][0];
				dataSql = ChnlCommonTools.getSQL(xml_data[0][1], sqlargs);
				System.out.println(dataSql);
			} else
				return;
			String[][] values = WebDBUtil.execQryArray(dataSql, "");
			ByteArrayInputStream stream = new ByteArrayInputStream(
					stringHead.getBytes("utf-8"));

			boolean validate = false;
			SAXBuilder builder = new SAXBuilder(validate);
			InputStream in = stream;
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			if (null != values)
				ExcelUtil.toExcelMultiSheet(out,sheetName, getTabHead(root),values,30000);
			//ExcelUtil.toExcel(out, sheetName, getTabHead(root), values);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getSQL(String retSql, String[] strIdxValue)
			throws AppException {
		int iCount = StringB.countSubStr(retSql, "?");
		int iIdxC = 0;
		if (strIdxValue != null)
			iIdxC = strIdxValue.length;
		if (iIdxC != iCount) {
			throw new AppException("语句[ " + retSql + " ]的位置变量不配备");
		}
		String tmpStr = null;
		for (int i = 0; strIdxValue != null && i < strIdxValue.length; i++) {
			// 首先替换strIdxValue[i]中的问号
			tmpStr = strIdxValue[i];
			if (tmpStr == null)
				tmpStr = "";
			strIdxValue[i] = StringB.replace(tmpStr, "?", "&qbs;&");
			retSql = StringB.replaceFirst(retSql, "?", strIdxValue[i]);
			strIdxValue[i] = tmpStr;
		}

		// 将正常的问号还原
		retSql = StringB.replace(retSql, "&qbs;&", "?");

		return retSql;
	}

	@SuppressWarnings("unchecked")
	public static TableHead getTabHead(Element root) {
		if (root == null)
			return null;
		// 获取属性
		TableHead thead = new TableHead();

		String lastRowTag = null;
		String tdDataText = null; // 必填
		String tdRowSpan = null; // 跨列
		String tdColSpan = null; // 跨行
		short tdFontColor = HSSFFont.COLOR_NORMAL; // 字体颜色
		String tdDataAlign = null; // 对齐方式
		String tdDataFormat = null; // 数据格式化
		String tdListBoxStr = null; // 下拉选择框

		// tabName = root.getAttribute("tName").getValue();
		List<Element> childNodes = root.getChildren("Trow");
		if (childNodes != null && childNodes.size() > 0) {
			for (Element tRow : childNodes) {
				TableHeadTr trow = new TableHeadTr();
				lastRowTag = tRow.getAttributeValue("lastRow");
				System.out.println("lastRowTag :" + lastRowTag + "\n");
				if (lastRowTag != null && lastRowTag.equals("true"))
					trow.setLastRow(true);
				List<Element> TdaNodes = tRow.getChildren("Tdat");
				if (TdaNodes != null && TdaNodes.size() > 0) {
					for (Element tDat : TdaNodes) {
						ChnlCommonTools.coutAttrs(tDat);
						TableHeadTd tdata = new TableHeadTd();

						tdDataText = tDat.getAttributeValue("text");
						tdata.setText(tdDataText);
						// System.out.println("Text is :" + tdDataText + "\n");

						tdColSpan = tDat.getAttributeValue("colSpan");
						if (tdColSpan != null)
							tdata.setColSpan(Integer.parseInt(tdColSpan));

						tdRowSpan = tDat.getAttributeValue("rowSpan");
						if (tdRowSpan != null)
							tdata.setRowSpan(Integer.parseInt(tdRowSpan));

						if (tDat.getAttributeValue("fontColor") != null) {
							tdFontColor = Short.parseShort(tDat
									.getAttributeValue("fontColor"));
							tdata.setFontColor(tdFontColor);
						}

						tdDataAlign = tDat.getAttributeValue("dataAlign");
						if (tdDataAlign != null)
							tdata.setDataAlign(tdDataAlign);
						else
							tdata.setDataAlign("RIGHT");
						
						tdDataFormat = tDat.getAttributeValue("dataFormat");
						if (tdDataFormat != null)
							tdata.setDataFormat(tdDataFormat);

						tdListBoxStr = tDat.getAttributeValue("listBoxStr");
						if (tdListBoxStr != null)
							tdata.setListBoxStr(tdListBoxStr);

						trow.addTabeHeadTd(tdata);
					}
				}
				thead.addTableHeadRow(trow);
			}
		}
		return thead;
	}

	@SuppressWarnings("unchecked")
	public static void coutAttrs(Element tDat) {
		if (tDat == null)
			return;
		List<Attribute> attrs = tDat.getAttributes();
		if (attrs != null && attrs.size() > 0) {
			for (Attribute attr : attrs) {
				System.out.print(attr.getName() + ":" + attr.getValue() + " ");
			}
			System.out.println();
		}
	}
	
	  public static void main(String[] argvs){ try {
		  OutputStream os = null;
		  FileOutputStream b = new FileOutputStream("D:\\data.xls",true);
		  String [][] values={{"1","2"},{"11","12"},{"21","22"},{"31","32"}};
		 // String [] values={"1","2","3","4","5","6","7","8"};
		  TableHead thead = new TableHead();
		  TableHeadTr trow = new TableHeadTr();
		  TableHeadTd tdata1 = new TableHeadTd();
		  tdata1.setText("列名1");
		  TableHeadTd tdata2 = new TableHeadTd();
		  tdata2.setText("列名2");
		  trow.addTabeHeadTd(tdata1);
		  trow.addTabeHeadTd(tdata2);
		  trow.setLastRow(true);
		  thead.addTableHeadRow(trow);
		  List<String>  methods = new LinkedList<String>();
		  methods.add("toString");
		  //ExcelUtil.toExcelMultiSheet(b, "分页测试", thead, String.class, values, methods, 5);
		  ExcelUtil.toExcelMultiSheet(b, "分页测试", thead,values,3);
		  b.close();
	  	} catch (Exception e) {
	  		e.printStackTrace(); 
	  	} 
	  }

	  

}
