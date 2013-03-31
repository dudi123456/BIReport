package com.ailk.bi.common.event;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.utils.Util;
import com.ailk.bi.common.taglib.TaglibHelper;
import com.ailk.bi.connect.DataPackage;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataSet implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6013424850127877493L;

	public static final int TAG_DATASET = 99;

	private long lRows = 0; /* 结果集包含的行数 */

	private int iCols = 0; /* 结果集每行的字段数 */

	private Vector fields = new Vector(); /* 数据库中对应的各个字段的名字 */

	private Vector content = new Vector(); /* 结果集的各个字段值，如第一行的第二个字段 */

	/**
	 * 实现实体的复制
	 * 
	 * @return
	 */
	public Object clone() {
		DataSet o = null;
		try {
			o = (DataSet) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * DataSet的内容显示
	 * 
	 * @return
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("***************DataSet***************\n");
		sb.append("lRows=" + lRows + "  iCols=" + iCols + "\n");
		String strFields = "";
		for (int i = 0; i < fields.size(); i++) {
			if (i != 0)
				strFields += ", ";
			strFields += (String) fields.elementAt(i);
		}
		sb.append("fields=\"" + strFields + "\"\n");

		sb.append("data is:\n");
		for (int l = 0; l < lRows && l < content.size(); l++) {
			Vector row = (Vector) content.elementAt(l);
			for (int i = 0; i < iCols && i < row.size(); i++) {
				if (i != 0)
					sb.append(", ");
				sb.append(row.elementAt(i));
			}
			sb.append("\n");
		}
		sb.append("*************** END ***************\n");
		return sb.toString();
	}

	/**
	 * 获取结果集中各字段名
	 * 
	 * @return
	 */
	public Vector getColNames() {
		return fields;
	}

	/**
	 * 得到指定指定字段所在的列
	 * 
	 * @param colName
	 * @return 如果找到则返回所在的列数，否则返回-1
	 */
	public int getColNoByName(String colName) {
		if (colName == null)
			return -1;
		colName = colName.trim();
		for (int i = 0; i < fields.size(); i++) {
			if (colName.equalsIgnoreCase((String) fields.elementAt(i)))
				return i;
		}
		return -1;
	}

	/**
	 * 读取指定行指定列的数据值
	 * 
	 * @param iRow
	 * @param iCol
	 * @return 如果取得失败返回null
	 */
	public String elementAt(int iRow, int iCol) {
		if (iRow >= lRows || iCol >= iCols)
			return null;
		Vector t = (Vector) content.elementAt(iRow);
		if (t != null)
			return (String) t.elementAt(iCol);
		return null;
	}

	/**
	 * 读取指定行指定列名的数据值
	 * 
	 * @param iRow
	 * @param colName
	 * @return
	 */
	public String elementAt(int iRow, String colName) {
		int iCol = getColNoByName(colName);
		if (iCol < 0)
			return null;
		return elementAt(iRow, iCol);
	}

	/**
	 * 设定指定行的指定列名的新值
	 * 
	 * @param iRow
	 * @param colName
	 * @param value
	 * @return 如果设置失败返回-1，否则返回0
	 */
	public int setElementAt(int iRow, String colName, String value) {
		int iCol = getColNoByName(colName);
		if (iCol < 0)
			return -1;

		setElementAt(iRow, iCol, value);

		return 0;
	}

	/**
	 * 设定指定行的指定列的新值
	 * 
	 * @param iRow
	 * @param iCol
	 * @param value
	 * @return 如果设置失败返回-1，否则返回0
	 */
	public int setElementAt(int iRow, int iCol, String value) {
		if (iRow >= lRows || iCol >= iCols)
			return -1;
		Vector t = (Vector) content.elementAt(iRow);
		if (t != null)
			t.setElementAt(value, iCol);
		else
			return -1;
		return 0;
	}

	/**
	 * 将DataSet打包成DataPackage
	 * 
	 * @param ds
	 * @return
	 */
	public static DataPackage pkg(DataSet ds) {
		DataPackage pkg = new DataPackage();
		String strFields = "";
		for (int i = 0; i < ds.fields.size(); i++) {
			if (i != 0)
				strFields += ", ";
			strFields += (String) ds.fields.elementAt(i);
		}

		pkg.AddModule(TAG_DATASET, strFields);

		for (int l = 0; l < ds.lRows && l < ds.content.size(); l++) {
			pkg.AddRow();
			Vector row = (Vector) ds.content.elementAt(l);
			for (int i = 0; i < ds.iCols && i < row.size(); i++) {
				pkg.SetColContent(i, (String) row.elementAt(i));
			}
			pkg.SaveRow();
		}

		return pkg;
	}

	/**
	 * 从DataPackage到DataSet的转换
	 * 
	 * @param pkg
	 * @return
	 */
	public static DataSet unPkg(DataPackage pkg) {
		DataSet ds = new DataSet();
		int iRows = pkg.size(TAG_DATASET);
		if (iRows <= 0)
			return null;
		String fields = pkg.GetColNameList(TAG_DATASET);
		Vector fieldV = StringB.parseStringTokenizer(fields, ",");
		if (fieldV == null)
			return null;
		for (int i = 0; i < fieldV.size(); i++) {
			String t = (String) fieldV.get(i);
			if (t != null)
				t = t.trim();
			fieldV.set(i, t);
		}

		ds.content = pkg.getContent(TAG_DATASET);
		ds.lRows = iRows;
		ds.iCols = fieldV.size();
		ds.fields = fieldV;
		return ds;
	}

	/**
	 * 两个Vector连接
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static Vector vectorCat(Vector a, Vector b) {
		Vector v = new Vector();
		for (int i = 0; i < a.size(); i++) {
			v.add(a.elementAt(i));
		}
		for (int i = 0; i < b.size(); i++) {
			v.add(b.elementAt(i));
		}
		return v;
	}

	/**
	 * 将两个table数组，按指定字段关联，注意aTbs是主表（通常它的记录数比较大，并且是主要显示的区域）
	 * 
	 * @param aTbs
	 *            主表的数组
	 * @param bTbs
	 *            从表的数组
	 * @param aCol
	 *            主表关联的字段
	 * @param bCol
	 *            从表关联的字段
	 * @return 返回能够匹配的两个表的合并后的结构集合，注意字段会自动加上a.xxxx和b.xxxx来区分主表和从表
	 * @throws AppException
	 */
	public static DataSet tabs2DS(JBTable aTbs[], JBTable bTbs[], String aCol,
			String bCol) throws AppException {
		// 解析strWhere
		DataSet aDS = tabs2DS(aTbs);
		if (aDS == null)
			throw new AppException("DataSet.tabs2DS() 第一个参数不是正确的table数组");
		int iA = aDS.getColNoByName(aCol);
		if (iA == -1)
			throw new AppException("DataSet.tabs2DS() 第三个参数不正确");
		DataSet bDS = tabs2DS(bTbs);
		if (bDS == null)
			throw new AppException("DataSet.tabs2DS() 第二个参数不是正确的table数组");
		int iB = bDS.getColNoByName(bCol);
		if (iB == -1)
			throw new AppException("DataSet.tabs2DS() 第四个参数不正确");
		Vector newContents = new Vector();
		for (int i = 0; i < aDS.getLRows(); i++) {
			String aV = aDS.elementAt(i, iA);
			if (aV == null)
				continue;
			for (int j = 0; j < bDS.getLRows(); j++) {
				String bV = bDS.elementAt(j, iB);
				if (bV == null)
					break;
				if (aV.equals(bV)) {
					newContents.add(vectorCat(
							(Vector) aDS.content.elementAt(i),
							(Vector) bDS.content.elementAt(j)));
					;
				}
			}
		}

		if (newContents.size() == 0)
			return null;
		Vector vFields = new Vector();
		for (int i = 0; i < aDS.fields.size(); i++) {
			vFields.add("a." + aDS.fields.elementAt(i).toString());
		}
		for (int i = 0; i < bDS.fields.size(); i++) {
			vFields.add("b." + bDS.fields.elementAt(i).toString());
		}
		DataSet ds = new DataSet();
		ds.lRows = newContents.size();
		ds.iCols = vFields.size();
		ds.fields = vFields;
		ds.content = newContents;

		return ds;
	}

	/**
	 * 由多表数组转换为DataSet
	 * 
	 * @param tables
	 *            是继承JBTable的数组结构，单元变量为JBTableBase或JBCombTable
	 * @return
	 */
	public static DataSet tabs2DS(JBTable tables[]) {
		if (tables == null || tables.length == 0)
			return null;
		Vector vFields = new Vector();
		Vector content = new Vector();
		Vector mcontent = new Vector();
		for (int i = 0; i < tables.length; i++) {
			JBTable table = tables[i];
			if (table == null)
				continue;
			Vector row = new Vector();
			if (table instanceof JBTableBase) {
				if (i == 0)
					row = getValuesFromTable(table, vFields);
				else
					row = getValuesFromTable(table, null);

				content.add(row);
			} else
			// 多表情况
			if (table instanceof JBCombTablesBase) {
				Field[] fields = table.getClass().getFields();
				Vector mrow = new Vector();

				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getType().getSuperclass() == null)
						continue;
					String fType = fields[j].getType().getSuperclass()
							.getName();

					if ("com.asiabi.common.event.JBTableBase".equals(fType)) {
						JBTable jt = null;
						try {
							jt = (JBTable) fields[j].get(table);
						} catch (IllegalAccessException ex1) {
						} catch (IllegalArgumentException ex1) {
						}
						if (jt == null && i == 0)
							try {
								jt = (JBTable) fields[j].getType()
										.newInstance();
							} catch (IllegalAccessException ex) {
								continue;
							} catch (InstantiationException ex) {
								continue;
							}
						mrow.add(jt);
						// System.out.println("jt="+jt+"\n");
					} else
						continue;
				}
				mcontent.add(mrow);
			}
		}

		// 多表合并
		Vector v = null;
		if (mcontent.size() > 0) {
			int iColsCount[] = new int[mcontent.size()];
			for (int i = 0; i < 1; i++) {
				v = (Vector) mcontent.elementAt(i);
				for (int j = 0; j < v.size(); j++) {
					JBTable table = (JBTable) v.elementAt(j);
					iColsCount[j] = table.getClass().getFields().length;
				}
			}
			Vector row = null;
			// System.out.println("mcontent="+mcontent);
			for (int i = 0; i < mcontent.size(); i++) {
				v = (Vector) mcontent.elementAt(i);
				Vector trueRow = new Vector();
				for (int j = 0; j < v.size(); j++) {
					JBTable table = (JBTable) v.elementAt(j);
					if (i == 0)
						row = getValuesFromTable(table, vFields);
					else if (table != null)
						row = getValuesFromTable(table, null);
					else {
						row = new Vector();
						for (int k = 0; k < iColsCount[j]; k++) {
							row.add("");
						}
					}

					for (int k = 0; k < row.size(); k++) {
						trueRow.add(row.elementAt(k));
					}
				}
				content.add(trueRow);
			}
		}

		DataSet ds = new DataSet();
		ds.lRows = content.size();
		ds.iCols = vFields.size();
		ds.fields = vFields;
		ds.content = content;
		return ds;
	}

	private static Vector getValuesFromTable(Object table, Vector vFields) {
		Vector row = new Vector();
		Field[] fields = table.getClass().getFields();

		for (int j = 0; j < fields.length; j++) {
			if (vFields != null)
				vFields.add(fields[j].getName().toLowerCase());
			String v = "";
			try {
				v = StringB.getFieldValueByStr(fields[j], table);
			} catch (IllegalArgumentException ex) {
			} catch (IllegalAccessException ex) {
			}
			row.add(v);
		}
		return row;
	}

	public int getICols() {
		return iCols;
	}

	public long getLRows() {
		return lRows;
	}

	/**
	 * 将tables的内容中id名的字段替换成codeList相对应的显示值
	 * 
	 * @param tables
	 *            必须为:DataSet, Table, Tables[]中的一种类型
	 * @param idsStr
	 * @param codesStr
	 * @return
	 * @throws AppException
	 */
	public static Object transContent(Object tables, String idsStr,
			String codesStr) throws AppException {
		if (tables == null)
			throw new AppException("tables参数为空");
		if (tables instanceof JBTableBase) {
			JBTableBase table = (JBTableBase) tables;
			return transTable(table, idsStr, codesStr);
		}

		if (tables instanceof JBTableBase[]) {
			System.out.println("afsdasdflkjfadslkjsadfl;kjsdfad;lkjsadfl;k");
			JBTableBase[] mtable = (JBTableBase[]) tables;
			return transTables(mtable, idsStr, codesStr);
		}

		if (tables instanceof DataSet) {
			DataSet ds = (DataSet) tables;
			return transDS(ds, idsStr, codesStr);
		}
		throw new AppException("tables参数必须为:DataSet, Table, Tables[]中的一种类型");
	}

	/**
	 * 将tables的内容中id名的字段替换成codeList相对应的显示值
	 * 
	 * @param tables
	 * @param idsStr
	 * @param codesStr
	 * @return
	 * @throws AppException
	 */
	private static JBTableBase[] transTables(JBTableBase[] tables,
			String idsStr, String codesStr) throws AppException {
		if (tables == null)
			throw new AppException("tables参数为空");

		JBTableBase[] retTbs = (JBTableBase[]) Array.newInstance(
				tables[0].getClass(), tables.length);

		for (int i = 0; i < tables.length; i++) {
			retTbs[i] = null;
			if (tables[i] == null)
				continue;

			retTbs[i] = transTable(tables[i], idsStr, codesStr);
		}
		return retTbs;
	}

	/**
	 * 将table的内容中id名的字段替换成codeList相对应的显示值
	 * 
	 * @param table
	 * @param idsStr
	 * @param codesStr
	 * @return
	 * @throws AppException
	 */
	private static JBTableBase transTable(JBTableBase table, String idsStr,
			String codesStr) throws AppException {
		if (idsStr == null)
			throw new AppException("idsStr参数为空");
		if (codesStr == null)
			throw new AppException("codesStr参数为空");
		if (table == null)
			throw new AppException("table参数为空");
		if (TaglibHelper.isSetTaglibMapps_BK() == false)
			throw new AppException("系统未曾读取过参数列表信息，映射转换无法完成!");

		Vector vIDs = StringB.parseStringTokenizer(idsStr, ",");
		if (vIDs == null)
			throw new AppException("无效的ids定义串");
		Vector vCodes = StringB.parseStringTokenizer(codesStr, ",");
		if (vCodes == null)
			throw new AppException("无效的codes定义串");

		HashMap codeList = TaglibHelper.getTaglibMapps_BK();
		if (codeList == null)
			throw new AppException("参数列表信息不存在，映射转换无法完成!");

		String id = null;
		String typeCode = null;
		String dispName = null;
		String fieldName = null;
		JBTableBase retTable = (JBTableBase) table.clone();

		for (int j = 0; j < vIDs.size(); j++) {
			typeCode = (String) vCodes.elementAt(j);
			if (typeCode == null)
				continue;
			fieldName = (String) vIDs.elementAt(j);
			if (fieldName == null)
				continue;
			id = Util.getStringValueByField(retTable, fieldName);
			if (id == null)
				continue;
			dispName = getCodeNameByID(codeList, typeCode, id);
			Util.setStringValueByField(retTable, fieldName, dispName);
		}
		return retTable;
	}

	/**
	 * 将DataSet的内容中的id值替换成codeList相对应的显示值
	 * 
	 * @param ds
	 * @param idsStr
	 * @param codesStr
	 * @return
	 * @throws AppException
	 */
	private static DataSet transDS(DataSet ds, String idsStr, String codesStr)
			throws AppException {
		if (idsStr == null)
			throw new AppException("idsStr参数为空");
		if (codesStr == null)
			throw new AppException("codesStr参数为空");
		if (ds == null)
			throw new AppException("ds参数为空");
		if (TaglibHelper.isSetTaglibMapps_BK() == false)
			throw new AppException("系统未曾读取过参数列表信息，映射转换无法完成!");

		Vector vIDs = StringB.parseStringTokenizer(idsStr, ",");
		if (vIDs == null)
			throw new AppException("无效的ids定义串");
		Vector vCodes = StringB.parseStringTokenizer(codesStr, ",");
		if (vCodes == null)
			throw new AppException("无效的codes定义串");

		HashMap codeList = TaglibHelper.getTaglibMapps_BK();
		if (codeList == null)
			throw new AppException("参数列表信息不存在，映射转换无法完成!");

		String id = null;
		String typeCode = null;
		String dispName = null;
		String fieldName = null;
		DataSet retDS = (DataSet) ds.clone();
		for (int i = 0; i < ds.getLRows(); i++) {
			for (int j = 0; j < vIDs.size(); j++) {
				typeCode = (String) vCodes.elementAt(j);
				if (typeCode == null)
					continue;
				fieldName = (String) vIDs.elementAt(j);
				if (fieldName == null)
					continue;
				id = retDS.elementAt(i, fieldName);
				if (id == null)
					continue;
				dispName = getCodeNameByID(codeList, typeCode, id);
				retDS.setElementAt(i, fieldName, dispName);
			}
		}
		return retDS;
	}

	/**
	 * 根据指定typeCode的ID所对应的显示名字，如果没找到则直接返回ID
	 * 
	 * @param codeList
	 * @param typeCode
	 * @param strID
	 * @return
	 */
	private static String getCodeNameByID(HashMap codeList, String typeCode,
			String strID) {
		if (strID == null)
			return strID;

		Vector v = (Vector) codeList.get(typeCode);
		if (v == null)
			return strID;
		for (int i = 0; i < v.size(); i++) {
			Vector vSub = (Vector) v.elementAt(i);
			if (vSub.elementAt(0) != null && strID.equals(vSub.elementAt(0))) {
				return (String) vSub.elementAt(1);
			}
		}
		return strID;
	}

	/*
	 * public static void main(String[] args) { test.Account a=new
	 * test.Account(); a.acctID="123"; a.acctName = "wiseking"; a.custID ="001";
	 * 
	 * test.Account b=new test.Account(); b.acctID="123"; b.acctName =
	 * "wiseking"; b.custID="001";
	 * 
	 * test.Customer c=new test.Customer(); c.custID="001"; c.custName= "popo";
	 * 
	 * test.Account[] as=new test.Account[2]; as[0]=a; as[1]=b;
	 * 
	 * test.Customer[] cs=new test.Customer[1]; cs[0]=c;
	 * 
	 * try { DataSet ds = DataSet.tabs2DS(as, cs, "custID", "custID");
	 * System.out.println("ds="+ds.toString()); } catch (AppException ex) { } }
	 */
}