package com.ailk.bi.common.event;

import java.io.Serializable;
import java.util.Vector;
import java.lang.reflect.Array;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableKeys implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1540699299190914477L;

	// 未被修改过
	public static final int REC_CLEAN = 0;

	// 新增
	public static final int REC_NEW = 1;

	// 变更
	public static final int REC_UPDATE = 2;

	// 删除
	public static final int REC_DELETE = 3;

	// 新增的删除
	public static final int REC_NEWDEL = 4;

	/**
	 * 清除指定类型的tables数组
	 * 
	 * @param tables
	 * @param iType
	 *            ，可选 默认为REC_DELETE
	 * @return
	 */
	public static JBTable[] trimTables(JBTable[] tables) {
		return trimTables(tables, REC_DELETE);
	}

	/**
	 * 清除指定类型的tables数组
	 * 
	 * @param tables
	 * @param iType
	 *            ，可选 默认为REC_DELETE
	 * @return
	 */
	public static JBTable[] trimTables(JBTable[] tables, int iType) {
		if (tables == null)
			return null;
		if (tables.length < 1)
			return tables;
		Vector v = new Vector();
		for (int i = 0; i < tables.length; i++) {
			if (tables[i] instanceof JBTable) {
				JBTable t = (JBTable) tables[i];
				if (t.getRecFlag() == iType) {
					continue;
				} else
					v.add(t);
			}
		}

		JBTable[] retA = (JBTable[]) Array.newInstance(tables[0].getClass(),
				v.size());
		for (int i = 0; i < retA.length; i++) {
			retA[i] = (JBTable) v.elementAt(i);
		}
		return retA;
	}

	public static void main(String[] args) {
		/*
		 * test.Account a=new test.Account(); a.acctID="wiseking";
		 * a.setRecFlag(REC_DELETE); test.Account b=new test.Account();
		 * b.acctID="popo"; b.setRecFlag(REC_DELETE); test.Account[] dog=new
		 * test.Account[2]; dog[0]=a; dog[1]=b; test.Account[]
		 * dd=(test.Account[])trimTables(dog,REC_DELETE); for (int i = 0; i <
		 * dd.length; i++) { System.out.println("dd="+dd[i].acctID); }
		 * 
		 * 
		 * test.Account[] dog = new test.Account[0]; test.Account[] dd =
		 * (test.Account[]) trimTables(dog, REC_DELETE);
		 */
	}
}