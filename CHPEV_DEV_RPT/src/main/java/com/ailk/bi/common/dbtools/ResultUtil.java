package com.ailk.bi.common.dbtools;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ResultUtil {
	private ResultUtil() {

	}

	/**
	 * 返回对象列表
	 *
	 * @param clazz
	 * @param result
	 * @param strDefalut
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends JBTableBase> List<T> transResultToObjList(
			Class<T> clazz, ResultSet result, String strDefalut)
			throws SQLException, InstantiationException, IllegalAccessException {
		List<T> domains = null;
		// 需要用到反射机制
		Field[] fields = clazz.getDeclaredFields();
		if (null != result) {
			domains = new ArrayList<T>();
			T domain = null;
			while (result.next()) {
				domain = clazz.newInstance();
				if (null != fields && fields.length > 0) {
					// 循环取出所有的字段，进行赋值
					String fieldClaszzName = null;
					for (Field field : fields) {
						try {
							fieldClaszzName = field.getType().getSimpleName()
									.toLowerCase().trim();
							if ("bigdecimal".equals(fieldClaszzName)) {
								field.set(domain,
										result.getBigDecimal(field.getName()));
							} else if ("boolean".equals(fieldClaszzName)) {
								field.set(domain,
										result.getBoolean(field.getName()));
							} else if ("byte".equals(fieldClaszzName)) {
								field.set(domain,
										result.getByte(field.getName()));
							} else if ("date".equals(fieldClaszzName)) {
								field.set(domain,
										result.getDate(field.getName()));
							} else if ("double".equals(fieldClaszzName)) {
								field.set(domain,
										result.getDouble(field.getName()));
							} else if ("float".equals(fieldClaszzName)) {
								field.set(domain,
										result.getFloat(field.getName()));
							} else if ("int".equals(fieldClaszzName)) {
								field.set(domain,
										result.getInt(field.getName()));
							} else if ("long".equals(fieldClaszzName)) {
								field.set(domain,
										result.getLong(field.getName()));
							} else if ("short".equals(fieldClaszzName)) {
								field.set(domain,
										result.getShort(field.getName()));
							} else if ("string".equals(fieldClaszzName)) {
								field.set(domain,
										result.getString(field.getName()));
							} else {
								field.set(domain,
										result.getObject(field.getName()));
							}
						} catch (Exception e) {
							// 这里不做处理，可能结果集中没有这个列
							//e.printStackTrace();
							//System.out.println("undefine column:"+field.getName());
						}
					}
				}
				domains.add(domain);
			}
		}
		return domains;
	}

	/**
	 * 从结果集转换到2维数组中
	 *
	 * @return
	 * @throws SQLException
	 */
	public static String[][] transResultToArray(ResultSet result,
			String strDefalut) throws SQLException {
		if (result == null)
			return null;
		// 得到结果集的定义结构
		ResultSetMetaData resultmd = result.getMetaData();
		// 得到结果集的列的总数
		int colCount = resultmd.getColumnCount();
		// 取得所有的行数
		result.last();
		int iTotalRow = result.getRow();
		// System.out.println("iTotalRow:" + iTotalRow);
		result.first();
		if (iTotalRow == 0)
		{
			result.close();
			result = null;
			return null;
		}
		// 定义结果数组
		String[][] strArray = new String[iTotalRow][colCount];
		// 对放回的全部数据逐一处理
		int intRow = 0;
		do {
			for (int i = 1; i <= colCount; i++) {
				// 获取字段类型
				int type = resultmd.getColumnType(i);
				// 取得数据
				strArray[intRow][i - 1] = formatObjectToString(
						getValue(result, i, type), strDefalut);
			}
			intRow++;
		} while (result.next());
		result.close();
		return strArray;
	}

	/**
	 * 从结果集中取得数据
	 *
	 * @param rs
	 * @param colNum
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static Object getValue(final ResultSet rs, int colNum, int type)
			throws SQLException {
		switch (type) {
		case Types.ARRAY:
		case Types.BLOB:
		case Types.CLOB:
		case Types.DISTINCT:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
		case Types.REF:
		case Types.STRUCT:
			return null;
		default: {
			Object value = rs.getObject(colNum);
			if (rs.wasNull() || (value == null))
				return null;
			else
				return value;
		}
		}
	}

	/**
	 * 从结果集转换成将行封装好hashmap的Arraylist
	 *
	 * @return
	 * @throws SQLException
	 */
	public static List transResultToArrayMap(ResultSet result)
			throws SQLException {
		ArrayList arrayMap = new ArrayList();
		// 得到结果集的定义结构
		ResultSetMetaData resultmd = result.getMetaData();
		// 对放回的全部数据逐一处理
		while (result != null && result.next()) {
			HashMap hashtable = new HashMap();
			for (int i = 1; i <= resultmd.getColumnCount(); i++) {
				hashtable.put(resultmd.getColumnName(i),
						result.getObject(i) == null ? "" : result.getObject(i)
								.toString());
			}
			arrayMap.add(hashtable);
		}
		if (result != null) {
			result.close();
			result = null;
		}
		return arrayMap;
	}

	/**
	 * 把对象值转化为字符串，
	 *
	 * @param obj
	 * @param strDefalut
	 * @return
	 */
	public static String formatObjectToString(Object obj, String strDefalut) {
		// 日期型java.sql.Date
		if (obj instanceof java.sql.Date) {
			return DateUtil.dateToString((java.sql.Date) obj, strDefalut);
			// 日期型java.sql.Timestamp
		} else if (obj instanceof Timestamp) {
			return DateUtil.TimeStampToString((java.sql.Timestamp) obj,
					strDefalut);
			// 数字型
		} else if (obj instanceof java.math.BigDecimal) {
			if (obj == null)
				return strDefalut;
			return ((java.math.BigDecimal) obj).toString();
		} else {
			if (obj == null)
				return strDefalut;
			return obj.toString();
		}
	}

	/**
	 * 从结果集转换到向量中
	 *
	 * @return
	 * @throws SQLException
	 */
	public static Vector transResultToVector(ResultSet result)
			throws SQLException {
		if (result == null)
			return null;
		// 得到结果集的定义结构
		ResultSetMetaData resultmd = result.getMetaData();
		// 得到列的总数
		int colCount = resultmd.getColumnCount();
		// 对放回的全部数据逐一处理
		Vector vTotal = new Vector();
		while (result.next()) {
			Vector vRow = new Vector();
			for (int i = 1; i <= colCount; i++) {
				// 获取字段类型
				int type = resultmd.getColumnType(i);
				// 取得数据
				vRow.add(getValue(result, i, type));
			}
			vTotal.add(vRow);
		}
		result.close();
		return vTotal;
	}

	public static Vector transResultToStringVector(ResultSet result,
			String strDefalut) throws SQLException {
		if (result == null)
			return null;
		// 得到结果集的定义结构
		ResultSetMetaData resultmd = result.getMetaData();
		// 得到列的总数
		int colCount = resultmd.getColumnCount();
		// 对放回的全部数据逐一处理
		Vector vTotal = new Vector();
		while (result.next()) {
			Vector vRow = new Vector();
			for (int i = 1; i <= colCount; i++) {
				// 获取字段类型
				int type = resultmd.getColumnType(i);
				// 取得数据
				vRow.add(formatObjectToString(getValue(result, i, type),
						strDefalut));
			}
			vTotal.add(vRow);
		}
		result.close();
		return vTotal;
	}

}
