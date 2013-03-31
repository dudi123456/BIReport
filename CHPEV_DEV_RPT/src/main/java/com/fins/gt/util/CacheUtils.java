package com.fins.gt.util;

import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CacheUtils {

	public static Object getFromCache(Map cache, Object key) {
		return cache.get(key);
	}

	public static void addToCache(Map cache, Object key, Object obj) {
		cache.put(key, obj);
	}
}
