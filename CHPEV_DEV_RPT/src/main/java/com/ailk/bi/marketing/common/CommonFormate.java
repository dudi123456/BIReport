package com.ailk.bi.marketing.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonFormate {

	public static String dateFormate(Date date) {
		if (null == date) {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);

	}

	public static String dateFormate2(Date date) {
		if (null == date) {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
