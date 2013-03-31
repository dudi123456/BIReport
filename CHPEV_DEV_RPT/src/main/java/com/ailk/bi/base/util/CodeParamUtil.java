package com.ailk.bi.base.util;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 由CodeListEngineListenser 在应用容器起来时装载特定的资源信息
 * 
 * 本工具类负责提取数据
 * 
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class CodeParamUtil {

	/**
	 * 取得一个特定MAP特定KEY的值
	 * 
	 * @param request
	 * @param map_code
	 * @param key
	 * @return
	 */
	public static String codeListParamFetcher(HttpServletRequest request,
			String map_code, String key) {
		String value = "";
		// System.out.println("================"+map_code);
		key = key.trim();
		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if (codeMap != null) {
			HashMap map = (HashMap) codeMap.get(map_code.trim().toUpperCase());
			if (map != null) {
				if (map.get(key) == null) {
					// value = "";
					value = key;
				} else {

					value = (String) map.get(key);
				}

			}
		}
		return value;
	}

	public static String codeListParamFetcher(HttpServletRequest request,
			HashMap map, String key) {
		String value = "";
		// System.out.println("================"+map_code);
		key = key.trim();
		if (map != null) {
			if (map.get(key) == null) {
				// value = "";
				value = key;
			} else {

				value = (String) map.get(key);
			}

		}
		// System.out.println("================"+value);
		return value;
	}

	/**
	 * 取得一个特定MAP
	 * 
	 * @param request
	 * @param map_code
	 * @return
	 */
	public static HashMap codeListParamFetcher(HttpServletRequest request,
			String map_code) {
		HashMap map = null;
		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if (codeMap != null) {
			map = (HashMap) codeMap.get(map_code.toUpperCase());
		}
		return map;
	}

}
