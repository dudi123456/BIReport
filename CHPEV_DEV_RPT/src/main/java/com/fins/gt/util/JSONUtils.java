package com.fins.gt.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fins.gt.common.Const;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JSONUtils {
	public static int getJsonInt(JSONObject jo, String key) {
		int ret = Const.nullInt;
		try {
			ret = jo.getInt(key);
		} catch (Exception e) {
		}
		return ret;
	}

	public static String[] getNames(JSONObject jo) {
		int length = jo.size();

		if (length == 0) {
			return null;
		}
		Iterator i = jo.keys();
		String[] names = new String[length];
		int j = 0;
		while (i.hasNext()) {
			names[j] = ((String) i.next());
			j++;
		}
		return names;
	}

	public static JSONObject Bean2JSONObject(Object bean) {
		return Bean2JSONObject(bean,
				BeanUtils.getCacheGetterMethodInfo(bean.getClass()));
	}

	public static JSONObject Bean2JSONObject(Object bean, Object[] methodInfo) {
		JSONObject jsonObj = null;
		if (bean == null) {
			jsonObj = JSONObject.fromObject(bean);
		} else {
			jsonObj = JSONObject.fromObject(bean);
		}
		return jsonObj;
	}

	public static Object JSONObject2Bean(JSONObject jsonObj, Class beanClass) {
		Object[] methodInfo = BeanUtils.getCacheSetterMethodInfo(beanClass);
		return JSONObject2Bean(jsonObj, beanClass, methodInfo);
	}

	public static Object JSONObject2Bean(JSONObject jsonObj, Class beanClass,
			Object[] methodInfo) {
		Object bean = null;
		try {
			bean = beanClass.newInstance();
		} catch (Exception e1) {
			jsonObj = null;
		}
		if (jsonObj == null)
			return null;

		String[] methodNames = (String[]) methodInfo[0];
		Method[] methods = (Method[]) methodInfo[1];
		Class[] paramTypes = (Class[]) methodInfo[2];
		for (int i = 0; i < methods.length; i++) {
			try {
				Class paramType = paramTypes[i];
				Object[] param = null;
				if (paramType.equals(String.class)) {
					param = new Object[] { jsonObj.getString(methodNames[i]) };
				} else if (paramType.equals(Integer.class)) {
					param = new Object[] { new Integer(
							jsonObj.getInt(methodNames[i])) };
				} else if (paramType.equals(Long.class)) {
					param = new Object[] { new Long(
							jsonObj.getLong(methodNames[i])) };
				} else if (paramType.equals(Double.class)) {
					param = new Object[] { new Double(
							jsonObj.getDouble(methodNames[i])) };
				} else if (paramType.equals(Boolean.class)) {
					param = new Object[] { new Boolean(
							jsonObj.getBoolean(methodNames[i])) };
				} else {
					param = new Object[] { jsonObj.get(methodNames[i]) };
				}

				methods[i].invoke(bean, param);
			} catch (Exception e) {
				LogHandler.error(methodNames[i] + "  " + e.getMessage());
			}
		}
		return bean;
	}

	public static JSONArray BeanList2JSONArray(List list, Class beanClass) {
		Object[] info = BeanUtils.getCacheGetterMethodInfo(beanClass);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0, end = list.size(); i < end; i++) {
			jsonArray.add(Bean2JSONObject(list.get(i), info));
			// jsonArray.ad
		}
		return jsonArray;
	}

	public static Map JSONObject2Map(JSONObject obj_JS) {
		Map map = new HashMap();
		Iterator keyItr = obj_JS.keys();
		while (keyItr.hasNext()) {
			String key = (String) keyItr.next();
			Object e;
			try {
				e = obj_JS.get(key);
				if (e instanceof JSONObject) {
					e = JSONObject2Map((JSONObject) e);
				}
				map.put(key, e);
			} catch (JSONException e1) {
			}

		}

		return map;
	}

	/*
	 * public static void main(String[] args) throws JSONException { StudentVO
	 * svo=new StudentVO(); svo.setBirthday("1982-01-01"); svo.setDepartment(new
	 * Integer(11)); svo.setGender("1"); svo.setMemo("testmemo asdasd ");
	 * svo.setName("fins"); svo.setNo(new Integer(11));
	 *
	 * JSONObject jo=Bean2JSONObject(svo); System.out.println(jo.toString(4));
	 *
	 * StudentVO svo2 = (StudentVO)JSONObject2Bean(jo,StudentVO.class);
	 * System.out.println(svo2); JSONObject jo2=Bean2JSONObject(svo2);
	 * System.out.println(jo2.toString(4));
	 *
	 * }
	 */
}
