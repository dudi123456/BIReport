package com.ailk.bi.common.event.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.JBCombTablesBase;
import com.ailk.bi.common.event.JBTableBase;
import com.ailk.bi.common.event.TableKeys;

@SuppressWarnings({ "rawtypes" })
public class Util {
	public final static String DEFAULT_ENCODING = "GB2312";

	public final static String TAGROOT = "JAVA_BEAN";

	public final static String TAGJBSROOT = "JAVA_BEANS";

	public final static String TAGPUB = "0";

	public final static String TAGTAB = "1";

	public final static String TYPE_ATTR = "type";

	public final static String AREA_ATTR = "area";

	public final static String TAGARRAY = "ARRAY";

	public final static String TAG_ATTR = "tag";

	public final static String RECFLAG_ATTR = "flag";

	/**
	 * 根据字段名取得对象所在的字段
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws AppException
	 */
	public static Field getFieldByName(Object obj, String fieldName)
			throws AppException {
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(fieldName))
				return fields[i];
		}
		return null;
	}

	/**
	 * 根据指定字段名获得改字段的String类型的值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws AppException
	 */
	public static String getStringValueByField(Object obj, String fieldName)
			throws AppException {
		Field field = getFieldByName(obj, fieldName);
		if (field == null)
			throw new AppException("fieldName[" + fieldName + "]不是对象的属性!");
		return getStringValueByField(obj, field);
	}

	/**
	 * 根据指定字段获得改字段的String类型的值
	 * 
	 * @param obj
	 * @param field
	 * @return
	 * @throws AppException
	 */
	public static String getStringValueByField(Object obj, Field field)
			throws AppException {
		String retStr = null;
		String typeName = field.getType().getName();
		try {
			if (typeName.equals("double")) {
				retStr = "" + field.getDouble(obj);
			} else if (typeName.equals("float")) {
				retStr = "" + field.getFloat(obj);
			} else if (typeName.equals("int")) {
				retStr = "" + field.getInt(obj);
			} else if (typeName.equals("long")) {
				retStr = "" + field.getLong(obj);
			} else if (typeName.equals("short")) {
				retStr = "" + field.getShort(obj);
			} else if (typeName.equals("boolean")) {
				retStr = "" + field.getBoolean(obj);
			} else if (typeName.equals("java.lang.String")) {
				retStr = "" + field.get(obj);
			} else
				retStr = "" + field.get(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex);
		}
		return retStr;
	}

	public static void setStringValueByField(Object obj, String fieldName,
			String val) throws AppException {
		Field field = getFieldByName(obj, fieldName);
		if (field == null)
			throw new AppException("fieldName[" + fieldName + "]不是对象的属性!");
		setStringValueByField(obj, field, val);
	}

	/**
	 * 设置指定field的属性的值 注意这些属性类型只能为: double, float, int, long, short, boolean,
	 * String 否则将不作处理
	 * 
	 * @param obj
	 * @param field
	 * @param val
	 * @return
	 * @throws AppException
	 */
	public static void setStringValueByField(Object obj, Field field, String val)
			throws AppException {
		if (obj == null)
			throw new AppException("对象为空!");
		try {
			String strVal = (String) val;
			String typeName = field.getType().getName();
			if (typeName.equals("double")) {
				if (strVal != null && !"".equals(strVal))
					field.setDouble(obj, Double.valueOf(strVal).doubleValue());
			} else if (typeName.equals("float")) {
				if (strVal != null && !"".equals(strVal))
					field.setFloat(obj, Double.valueOf(strVal).floatValue());
			} else if (typeName.equals("int")) {
				if (strVal != null && !"".equals(strVal))
					field.setInt(obj, Integer.parseInt(strVal));
			} else if (typeName.equals("long")) {
				if (strVal != null && !"".equals(strVal))
					field.setLong(obj, Long.parseLong(strVal));
			} else if (typeName.equals("short")) {
				if (strVal != null && !"".equals(strVal))
					field.setShort(obj, Short.parseShort(strVal));
			} else if (typeName.equals("boolean")) {
				if (strVal != null && !"".equals(strVal))
					field.setBoolean(obj, Boolean.getBoolean(strVal));
			} else if (typeName.equals("java.lang.String")) {
				field.set(obj, strVal);
			} else {
				// 不作设置处理
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex);
		}
	}

	private static Element createElement(Document doc, String fName,
			String fTypeName, String v, String area, boolean simp) {
		Element element = doc.createElement(fName);
		if (fTypeName != null && !simp)
			element.setAttribute(TYPE_ATTR, fTypeName);
		if (area != null && !simp)
			element.setAttribute(AREA_ATTR, area);
		if (v != null) {
			Text textseg = doc.createTextNode(v);
			element.appendChild(textseg);
		}

		return element;
	}

	public static Element genAJavaBean(Document doc, Object obj,
			String objName, boolean simp) throws AppException {
		if (obj == null || doc == null)
			return null;

		Element root = doc.createElement(TAGROOT); // Create Root Element
		root.setAttribute(TAG_ATTR, objName);
		Class c = obj.getClass();
		if (obj instanceof JBTableBase) {
			String recFlag = "";
			JBTableBase jbT = (JBTableBase) obj;
			switch (jbT.getRecFlag()) {
			case TableKeys.REC_NEW:
				recFlag = "new";
				break;
			case TableKeys.REC_CLEAN:
				recFlag = "clean";
				break;
			case TableKeys.REC_UPDATE:
				recFlag = "update";
				break;
			case TableKeys.REC_DELETE:
				recFlag = "delete";
				break;
			}
			root.setAttribute(RECFLAG_ATTR, recFlag);
		}

		Field[] fields = c.getFields();
		Field[] dFields = c.getDeclaredFields();
		String[] dFieldNames = new String[dFields.length];
		for (int j = 0; j < dFields.length; j++) {
			dFieldNames[j] = dFields[j].getName();
		}
		String fName = null;
		String fTypeName = null;
		String v = null;
		for (int i = 0; i < fields.length; i++) {
			// 不显示上级接口定义的内容
			if (fields[i].getDeclaringClass().isInterface())
				continue;

			fName = fields[i].getName();
			fTypeName = fields[i].getType().getName();
			v = Util.getStringValueByField(obj, fields[i]);
			v = (v == null) ? "" : v;
			Element element = null;
			String area = null;
			if (StringB.inStringArray(dFieldNames, fName) == -1) {
				area = TAGPUB;
			} else
				area = TAGTAB;

			element = Util.createElement(doc, fName, fTypeName, v, area, simp);
			root.appendChild(element);
		}

		return root;
	}

	public static Element genJavaBeans(Document doc, Object obj,
			String objName, boolean simp) throws AppException {
		Class c = obj.getClass();
		Field[] fields = c.getFields();
		Field[] dFields = c.getDeclaredFields();
		String[] dFieldNames = new String[dFields.length];
		for (int j = 0; j < dFields.length; j++) {
			dFieldNames[j] = dFields[j].getName();
		}

		String fName = null;
		String fTypeName = null;
		Element root = doc.createElement(TAGJBSROOT); // Create Root Element
		root.setAttribute(TAG_ATTR, objName);
		if (obj instanceof JBCombTablesBase) {
			String recFlag = "";
			JBCombTablesBase jbT = (JBCombTablesBase) obj;
			switch (jbT.getRecFlag()) {
			case TableKeys.REC_NEW:
				recFlag = "new";
				break;
			case TableKeys.REC_CLEAN:
				recFlag = "clean";
				break;
			case TableKeys.REC_UPDATE:
				recFlag = "update";
				break;
			case TableKeys.REC_DELETE:
				recFlag = "delete";
				break;
			}
			root.setAttribute(RECFLAG_ATTR, recFlag);
		}

		try {
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getDeclaringClass().isInterface()) {
					continue;
				}
				fName = fields[i].getName();
				fTypeName = fields[i].getType().getName();
				Element element = null;
				String area = null;

				if (StringB.inStringArray(dFieldNames, fName) == -1) {
					area = TAGPUB;
				} else
					area = TAGTAB;

				if (fTypeName.startsWith("[[")) {
					element = createElement(doc, fName, fTypeName,
							"!ERROR PARSING 2Dimension Array", area, simp);
				} else if (fTypeName.startsWith("[")) {
					Object oarray = (Object) fields[i].get(obj);
					// com.asiabi.common.app.Debug.println("oarray="+oarray);
					if (oarray == null) {
						element = createElement(doc, fName, fTypeName, "null",
								area, simp);
					} else {
						int iLen = Array.getLength(oarray);
						String oarrayTypeName = oarray.getClass().getName();
						element = createElement(doc, TAGARRAY, oarrayTypeName,
								null, area, simp);
						if (fTypeName.startsWith("[L")) {

							Object subObj = null;
							Element subE = null;
							for (int k = 0; k < iLen; k++) {
								subObj = Array.get(oarray, k);

								if (oarrayTypeName.indexOf("java.lang.String") > -1) {
									subE = createElement(doc, fName, null,
											subObj.toString(), area, simp);
								} else if (subObj instanceof JBCombTablesBase) {
									subE = genJavaBeans(doc, subObj,
											((JBCombTablesBase) subObj)
													.getTableName(), simp);
								} else if (subObj instanceof JBTableBase) {
									subE = genAJavaBean(doc, subObj,
											((JBTableBase) subObj)
													.getTableName(), simp);
								}
								if (subE != null)
									element.appendChild(subE);
							}
						} else {

						}
					}
				} else {

					if (fields[i].getType().isPrimitive()) // 是源语类型，如：int long等
					{
						String retVal = Util.getStringValueByField(obj,
								fields[i]);
						element = createElement(doc, fName, fTypeName, retVal,
								area, simp);
					} else if (fields[i].getType().newInstance() instanceof JBCombTablesBase) {
						if (fields[i].getType().getName()
								.equals(obj.getClass().getName())) // 不能进行嵌套定义
						{
							element = createElement(doc, fName, fTypeName,
									"!RECURSION ERROR", area, simp);
						} else {
							JBCombTablesBase jtbs = (JBCombTablesBase) fields[i]
									.get(obj);
							element = genJavaBeans(doc, jtbs,
									jtbs.getTableName(), simp);
						}
					} else if (fields[i].getType().newInstance() instanceof JBTableBase) {
						JBTableBase jtb = (JBTableBase) fields[i].get(obj);
						if (jtb == null) {
							element = Util.createElement(doc, fName, fTypeName,
									"null", area, simp);
						} else
							element = Util.genAJavaBean(doc, jtb,
									jtb.getTableName(), simp);
					} else {
						String retVal = Util.getStringValueByField(obj,
								fields[i]);
						element = Util.createElement(doc, fName, fTypeName,
								retVal, area, simp);
					}
				}
				if (element == null) {
					continue;
				}
				root.appendChild(element);
			}
		} catch (Exception ex) {
			throw new AppException(ex);
		}

		return root;
	}
}