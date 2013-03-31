package com.ailk.bi.common.event.utils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Vector;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.event.JBCombTablesBase;
import com.ailk.bi.common.event.JBTableBase;
import com.ailk.bi.common.event.TableKeys;
import com.ailk.bi.common.sysconfig.GetSystemConfig;
import com.ailk.bi.connect.DataPackage;

/**
 import test.Account;
 import test.Customer;
 import test.comb.CustAcct;
 */

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: DataPackage转换函数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class DataPackUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6082672558141244870L;

	public final static int OLD_TAG_INTERVAL = 5000; // 新旧之间的tag的差值

	/**
	 * 将table相关类转换为DataPackage 将tables或者table数组转换称DataPackage
	 * 
	 * @param obj
	 *            要转换的对象，这个对象只允许类型为table, tables或者table数组, obj 又增加了一种类型Vector,
	 *            vector里的内容为table, tables或者table数组， 可以是这几种类型的组合，如 v.add( tab1
	 *            ); v.add(tab2 ); v.add(tabArray1); v.add(combTabs);
	 *            其中tables的内容中制转换类型为table或table[]的成员变量 如下面的对象: 1. table[] ts; 2.
	 *            table t; 3. tables tbs; tabs{ table[] ts; table t; }
	 * @return 转换完成的datapackage
	 */
	public static DataPackage tabs2DP(Object obj) throws AppException {
		DataPackage dp = new DataPackage();
		tabs2DP(dp, obj);
		return dp;
	}

	/**
	 * 从DataPackage中转换出指定类型的table数组
	 * 
	 * @param pkg
	 * @param tabClass
	 *            table类的class
	 * @return 解析转换后的数组, 如果解析有误则返回 null 调用例子： AcctTable[]
	 *         accts=(AcctTable)DP2Tabs(pkg, AcctTable.class); if( accts==null )
	 *         { //error parser it... }else { //deal this array }
	 */
	public static JBTableBase[] DP2Tabs(DataPackage pkg, Class tabClass) {
		int iModuleID = GetSystemConfig.getTagFromClass(tabClass.getName());
		if (iModuleID == -1) {
			System.err.println("NO FATAL ERROR: 类" + tabClass.getName()
					+ "的tag映射值没有定义!");
		}

		return DP2Tabs(pkg, iModuleID);
	}

	/**
	 * 获取某一对象的OLD数组结构，通常old的tag比该对象tag大 OLD_TAG_INTERVAL
	 * 
	 * @param pkg
	 * @param tabClass
	 * @return
	 */
	public static JBTableBase[] DP2OldTabs(DataPackage pkg, Class tabClass) {
		int iModuleID = GetSystemConfig.getTagFromClass(tabClass.getName());
		if (iModuleID == -1) {
			System.err.println("NO FATAL ERROR: 类" + tabClass.getName()
					+ "的tag映射值没有定义!");
		}
		iModuleID += OLD_TAG_INTERVAL;

		return DP2Tabs(pkg, iModuleID);
	}

	/**
	 * 根据tag值取得对象数组
	 * 
	 * @param pkg
	 *            datapackage
	 * @param iTag
	 *            tag值
	 * @return 如果成功返回得到的table，否则返回null
	 */
	public static JBTableBase[] DP2Tabs(DataPackage pkg, int iTag) {
		int cTag = iTag;
		if (iTag > OLD_TAG_INTERVAL)
			cTag = iTag - OLD_TAG_INTERVAL;
		String cName = GetSystemConfig.getClassFromTag(cTag);
		if (cName == null) {
			System.err.println("FATAL ERROR: tag=" + cTag + "的类映射没有定义!");
		}
		if (pkg == null) {
			System.err.println("ERROR: pkg is NULL!");
			return null;
		}

		int iRows = pkg.size(iTag);

		if (iRows < 1)
			return null;

		Class tabClass = null;
		try {
			tabClass = Class.forName(cName);
		} catch (Exception ex2) {
			System.err.println("FATAL ERROR: class" + cName
					+ "的无法实例化，请检查其是否存在!");
			return null;
		}
		JBTableBase[] retTabs = (JBTableBase[]) Array.newInstance(tabClass,
				iRows);

		for (int i = 0; i < iRows; i++) {
			try {
				retTabs[i] = (JBTableBase) tabClass.newInstance();
				if (retTabs[i] != null)
					retTabs[i].setRecFlag(TableKeys.REC_CLEAN);
			} catch (IllegalAccessException ex) {
				return null;
			} catch (InstantiationException ex) {
				return null;
			}

			Field fields[] = tabClass.getFields();
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				String strVal = pkg.GetColContent(iTag, i + 1, field.getName());
				String typeName = field.getType().getName();
				try {
					if (typeName.equals("double")) {
						if (strVal != null && !"".equals(strVal))
							field.setDouble(retTabs[i], Double.valueOf(strVal)
									.doubleValue());
					} else if (typeName.equals("float")) {
						if (strVal != null && !"".equals(strVal))
							field.setFloat(retTabs[i], Double.valueOf(strVal)
									.floatValue());
					} else if (typeName.equals("int")) {
						if (strVal != null && !"".equals(strVal))
							field.setInt(retTabs[i], Integer.parseInt(strVal));
					} else if (typeName.equals("long")) {
						if (strVal != null && !"".equals(strVal))
							field.setLong(retTabs[i], Long.parseLong(strVal));
					} else if (typeName.equals("short")) {
						if (strVal != null && !"".equals(strVal))
							field.setShort(retTabs[i], Short.parseShort(strVal));
					} else if (typeName.equals("boolean")) {
						if (strVal != null && !"".equals(strVal))
							field.setBoolean(retTabs[i],
									Boolean.getBoolean(strVal));
					} else if (typeName.equals("java.lang.String")) {
						field.set(retTabs[i], strVal);
					}
				} catch (IllegalAccessException ex1) {
				} catch (IllegalArgumentException ex1) {
				}
			}
		}
		return retTabs;

	}

	private static void tabs2DP(DataPackage dp, Object obj) throws AppException {
		// 判断obj的类型
		if (obj == null)
			return;

		String objTypeName = obj.getClass().getName();

		if (objTypeName.startsWith("java.util.Vector")) {
			Vector vObj = (Vector) obj;
			Object subObj = null;
			for (int i = 0; i < vObj.size(); i++) {
				subObj = vObj.elementAt(i);
				tabs2DP(dp, subObj);
			}
		} else if (objTypeName.startsWith("[L")) {
			addArrayTables(dp, obj);
		} else {
			if (obj instanceof JBCombTablesBase) {
				Field fs[] = obj.getClass().getFields();
				for (int i = 0; i < fs.length; i++) {
					if (fs[i].getType().isPrimitive()) {
						continue;
					}
					String fTypeName = fs[i].getType().getName();
					Object fValue = null;
					try {
						fValue = fs[i].get(obj);
					} catch (Exception ex) {
						throw new AppException(ex.toString());
					}

					if (fTypeName.startsWith("[L")) {
						addArrayTables(dp, fValue);
					} else if (fValue instanceof JBTableBase) {
						addATable(dp, (JBTableBase) fValue, true);
					}
				}
			} else if (obj instanceof JBTableBase) {
				addATable(dp, (JBTableBase) obj, true);
			} else
				return;
		}

		return;
	}

	private static void addATable(DataPackage dp, JBTableBase obj,
			boolean hasHead) throws AppException {
		addATable(dp, obj, hasHead, false);
	}

	private static void addATable(DataPackage dp, JBTableBase obj,
			boolean hasHead, boolean oldFlag) throws AppException {

		if (obj == null)
			return;

		Field fs[] = obj.getClass().getFields();

		if (hasHead) {
			String module = "";
			for (int m = 0; m < fs.length; m++) {
				if (m > 0)
					module += ",";
				module += fs[m].getName();
			}
			// 增加extern_oper_flag字段 2004.03.25 king
			if ("".equals(module))
				module += "extern_oper_flag";
			else
				module += ",extern_oper_flag";
			int moduleID = obj.getModuleID();
			if (oldFlag)
				moduleID += OLD_TAG_INTERVAL;
			dp.AddModule(moduleID, module);
		}

		dp.AddRow();
		for (int k = 0; k < fs.length; k++) {
			String value = Util.getStringValueByField(obj, fs[k]);
			dp.SetColContent(fs[k].getName(), value);
		}
		// 增加extern_oper_flag字段 2004.03.25 king
		dp.SetColContent("extern_oper_flag", obj.getRecFlag() + "");
		dp.SaveRow();
	}

	private static int addArrayTables(DataPackage dp, Object arrayTables)
			throws AppException {
		return addArrayTables(dp, arrayTables, false);
	}

	/**
	 * 向DataPackage中添加一个tables数组,oldFlag表示是否为旧的module，如果为旧的则tag会自动+5000
	 * 
	 * @param dp
	 * @param arrayTables
	 * @param oldFlag
	 * @return 如果参数有错误，则返回-1
	 * @throws AppException
	 */
	public static int addArrayTables(DataPackage dp, Object arrayTables,
			boolean oldFlag) throws AppException {
		int iLen = Array.getLength(arrayTables);
		Object subObj = null;
		for (int i = 0; i < iLen; i++) {
			subObj = Array.get(arrayTables, i);
			if (subObj == null)
				continue;
			if (subObj instanceof JBTableBase) {
				if (i == 0) {
					addATable(dp, (JBTableBase) subObj, true, oldFlag);
				} else
					addATable(dp, (JBTableBase) subObj, false);
			} else
				return -1;
		}
		return 0;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * Vector v = new Vector(); test.Account acct = new test.Account();
	 * test.Customer cust = new test.Customer(); test.comb.CustAcct ca = new
	 * test.comb.CustAcct(); //ca.account = acct; ca.customer = cust;
	 * cust.custID="asfdasfsa"; test.Account[] accts = new test.Account[5];
	 * accts[0] = new test.Account(); accts[1] = new test.Account(); accts[2] =
	 * new test.Account();
	 * 
	 * accts[4] = new test.Account(); accts[0].acctID="12312";
	 * accts[0].acctName="wiseking"; accts[1].acctID="w12";
	 * accts[1].acctName="popo"; ca.ttt = accts; v.add(accts); //v.add(ca);
	 * v.add(new test.Customer[0]); try { DataPackage dp = tabs2DP(v);
	 * System.out.println("tabs2DP ok!\n dp="+dp); try {
	 * 
	 * dp.saveToXml("d:/t.xml"); } catch (DataException ex1) {
	 * ex1.printStackTrace(); } test.Account[] ats=(test.Account[])DP2Tabs(dp,
	 * test.Account.class); System.out.println("DP2Tabs ok!\n ats="+ats); if(
	 * ats!=null ) for (int i = 0; i < ats.length; i++) {
	 * System.out.println(ats[i].toXml()); } } catch (AppException ex) { } }
	 */
}