package com.fins.gt.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Tester {
	public int value;

	public Tester() {
		value = 0;
	}

	public static Map classCache = Collections.synchronizedMap(new HashMap());

	public static Object loadAction(String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class clazz = (Class) classCache.get(className);
		if (clazz == null) {
			clazz = Class.forName(className);
			classCache.put(className, clazz);
		}
		return clazz.newInstance();
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		long b1;
		long b2;
		long e1;
		long e2;
		String className = "Tester";

		b1 = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			loadAction(className);
			// Class.forName(className).newInstance();
		}
		e1 = System.currentTimeMillis();
		System.out.println("Class.forName " + (e1 - b1));

		System.gc();

		b2 = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			new Tester();
		}
		e2 = System.currentTimeMillis();
		System.out.println("new Tester() " + (e2 - b2));

		System.out.println((e1 - b1) - (e2 - b2));

	}
}
