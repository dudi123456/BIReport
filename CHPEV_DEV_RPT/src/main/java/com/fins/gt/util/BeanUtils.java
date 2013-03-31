package com.fins.gt.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BeanUtils {

	private static Map getterMethodInfoCache = Collections
			.synchronizedMap(new HashMap());
	private static Map setterMethodInfoCache = Collections
			.synchronizedMap(new HashMap());
	public static boolean useCache = true;

	public static Object[] map2Array(Map map, String[] properiesName) {
		int len = properiesName.length;
		Object[] objs = new Object[len];
		for (int i = 0; i < len; i++) {
			objs[i] = map.get(properiesName[i]);
		}
		return objs;
	}

	public static Object[] bean2Array(Object bean, String[] properiesName,
			Class beanClass) {
		int len = properiesName.length;
		Object[] objs = new Object[len];

		Object[] methodInfos = getCacheGetterMethodInfo(beanClass);
		Map methodsMap = (Map) methodInfos[2];
		for (int i = 0; i < len; i++) {
			try {
				Method method = (Method) methodsMap.get(properiesName[i]);
				objs[i] = method.invoke(bean, (Object[]) null);
			} catch (Exception e) {
			}
		}
		return objs;
	}

	public static Map bean2Map(Object bean, Class beanClass) {
		Map map = new HashMap();
		Object[] methodInfos = getCacheGetterMethodInfo(beanClass);
		String[] properties = (String[]) methodInfos[0];
		Method[] methods = (Method[]) methodInfos[1];
		for (int i = 0, len = methods.length; i < len; i++) {
			try {
				map.put(properties[i], methods[i].invoke(bean, (Object[]) null));
			} catch (Exception e) {
			}
		}
		return map;
	}

	public static boolean isStandardProperty(Class clazz) {
		return clazz.isPrimitive() || clazz.isAssignableFrom(Byte.class)
				|| clazz.isAssignableFrom(Short.class)
				|| clazz.isAssignableFrom(Integer.class)
				|| clazz.isAssignableFrom(Long.class)
				|| clazz.isAssignableFrom(Float.class)
				|| clazz.isAssignableFrom(Double.class)
				|| clazz.isAssignableFrom(Character.class)
				|| clazz.isAssignableFrom(String.class)
				|| clazz.isAssignableFrom(Boolean.class);
	}

	// public static Object getPropertyValue(Object bean , String propertyName ,
	// Class beanClass){
	// return null;//getCacheGetterMethods(beanClass ,true );
	// }

	public static Object[] getCacheGetterMethodInfo(Class klass) {
		return getCacheGetterMethods(klass, true);
	}

	public static Object[] getCacheGetterMethods(Class klass,
			boolean includeSuperClass) {
		String className = klass.getName();
		Object[] methodInfo = (Object[]) getterMethodInfoCache.get(className);
		if (methodInfo != null) {
			return methodInfo;
		}
		methodInfo = BeanUtils.getGetterMethods(klass, includeSuperClass);
		getterMethodInfoCache.put(className, methodInfo);
		return methodInfo;
	}

	public static Object[] getCacheSetterMethodInfo(Class klass) {
		return getCacheSetterMethods(klass, true);
	}

	public static Object[] getCacheSetterMethods(Class klass,
			boolean includeSuperClass) {
		String className = klass.getName();
		Object[] methodInfo = (Object[]) setterMethodInfoCache.get(className);
		if (methodInfo != null) {
			return methodInfo;
		}
		methodInfo = BeanUtils.getSetterMethods(klass, includeSuperClass);
		setterMethodInfoCache.put(className, methodInfo);
		return methodInfo;
	}

	public static Object[] getGetterMethodInfo(Class klass) {
		return getGetterMethods(klass, true);
	}

	public static Object[] getGetterMethods(Class klass,
			boolean includeSuperClass) {
		Object[] methodInfo = null;
		Method[] methods = includeSuperClass ? klass.getMethods() : klass
				.getDeclaredMethods();
		ArrayList getterMethods = new ArrayList();
		ArrayList keyNames = new ArrayList();

		Map methodMap = new HashMap();
		for (int i = 0; i < methods.length; i++) {
			try {
				Method method = methods[i];
				String name = method.getName();
				String key = null;
				if (name.startsWith("get")) {
					key = name.substring(3);
				} else if (name.startsWith("is")) {
					key = name.substring(2);
				}
				if (StringUtils.isNotEmpty(key)
						&& Character.isUpperCase(key.charAt(0))
						&& method.getParameterTypes().length == 0
						&& !key.equals("Class")) {
					if (key.length() == 1) {
						key = key.toLowerCase();
					} else if (!Character.isUpperCase(key.charAt(1))) {
						key = key.substring(0, 1).toLowerCase()
								+ key.substring(1);
					}
					getterMethods.add(method);
					keyNames.add(key);
					methodMap.put(key, method);
				}
			} catch (Exception e) {
				/* forget about it */
			}
		}
		String[] a = new String[keyNames.size()];
		Method[] m = new Method[getterMethods.size()];
		methodInfo = new Object[] { keyNames.toArray(a),
				(Method[]) getterMethods.toArray(m), methodMap };
		return methodInfo;
	}

	public static Object[] getSetterMethodInfo(Class klass) {
		return getSetterMethods(klass, true);
	}

	public static Object[] getSetterMethods(Class klass,
			boolean includeSuperClass) {
		Object[] methodInfo = null;
		Method[] methods = includeSuperClass ? klass.getMethods() : klass
				.getDeclaredMethods();
		ArrayList setterMethods = new ArrayList();
		ArrayList keyNames = new ArrayList();
		ArrayList paramTypes = new ArrayList();

		for (int i = 0; i < methods.length; i++) {
			try {
				Method method = methods[i];
				String name = method.getName();
				String key = null;

				if (name.startsWith("set")) {
					key = name.substring(3);
				}
				Class[] pt = method.getParameterTypes();
				if (StringUtils.isNotEmpty(key)
						&& Character.isUpperCase(key.charAt(0))
						&& pt.length == 1 && !key.equals("Class")) {
					if (key.length() == 1) {
						key = key.toLowerCase();
					} else if (!Character.isUpperCase(key.charAt(1))) {
						key = key.substring(0, 1).toLowerCase()
								+ key.substring(1);
					}
					paramTypes.add(pt[0]);
					setterMethods.add(method);
					keyNames.add(key);
				}
			} catch (Exception e) {
				LogHandler.error(JSONUtils.class, e);
				/* forget about it */
			}
		}
		String[] a = new String[keyNames.size()];
		Method[] m = new Method[setterMethods.size()];
		Class[] c = new Class[paramTypes.size()];
		methodInfo = new Object[] { keyNames.toArray(a),
				(Method[]) setterMethods.toArray(m),
				(Class[]) paramTypes.toArray(c) };
		return methodInfo;
	}
}
