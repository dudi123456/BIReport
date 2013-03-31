/**
 * 
 */
package com.ailk.bi.common.app;

import java.lang.reflect.*;

/**
 * @author jcm008
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ReflectUtil {
	/**
	 * 从对象中得到指定属性的数据值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return 指定属性的数据值
	 */
	public static double getDoubleFromObj(Object obj, String fieldName) {

		double tempVal = 0.0;

		if (obj == null) {
			return tempVal;
		}
		if (fieldName == null || "".equals(fieldName)) {
			return tempVal;
		}

		Class tempClass = obj.getClass();
		Field tempField = null;

		try {
			tempField = tempClass.getDeclaredField(fieldName);
			Object temp = tempField.get(obj);
			if (temp != null)
				tempVal = Double.parseDouble((String) temp);
			else
				tempVal = 0.0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempVal;
	}

	/**
	 * 从对象中得到指定属性的String值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return 指定属性的String值
	 */
	public static String getStringFromObj(Object obj, String fieldName) {
		Field tempField = null;
		String tempVal = "";

		if (obj == null) {
			return tempVal;
		}
		if (fieldName == null || "".equals(fieldName)) {
			return tempVal;
		}

		Class tempClass = obj.getClass();

		try {
			tempField = tempClass.getDeclaredField(fieldName);
			Object temp = tempField.get(obj);
			if (temp != null)
				tempVal = (String) temp;
			else
				tempVal = "";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempVal;
	}

	/**
	 * 给对象中属性赋double值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static void setDoubleToObj(Object obj, String fieldName, double value) {
		Class tempClass = obj.getClass();
		Field tempField = null;
		try {
			tempField = tempClass.getDeclaredField(fieldName);
			tempField.setDouble(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 给对象中属性赋String值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setStringToObj(Object obj, String fieldName, String value) {
		Class tempClass = obj.getClass();
		Field tempField = null;
		try {
			tempField = tempClass.getDeclaredField(fieldName);
			tempField.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 从对象集合中得到其中符合条件的对象数组
	 * 
	 * @param obj
	 * @param fieldName
	 * @param fieldValue
	 *            比较值
	 * @return
	 */
	public static Object[] getObjSet(Object[] obj, String fieldName,
			String fieldValue) {

		Object[] resultObj = null;

		if (obj == null || obj.length < 0) {
			return resultObj;
		}
		if (fieldName == null || "".equals(fieldName)) {
			return resultObj;
		}
		if (fieldValue == null || "".equals(fieldValue)) {
			return obj;
		}
		int objSum = obj.length;
		int dataSum = 0;

		// 符合条件的对象数目
		for (int i = 0; i < objSum; i++) {
			Object temp = obj[i];
			String tempVal = getStringFromObj(temp, fieldName);
			if (fieldValue.equals(tempVal))
				dataSum++;
		}

		// 得到集合
		resultObj = new Object[dataSum];
		for (int i = 0, j = 0; i < objSum; i++) {
			Object temp = obj[i];
			String tempVal = getStringFromObj(temp, fieldName);
			if (fieldValue.equals(tempVal)) {
				resultObj[j] = temp;
				j++;
			}
		}

		return resultObj;
	}
}
