package com.ailk.bi.system.common;

import java.util.Date;

import com.ailk.bi.common.app.DateUtil;

/**
 * 统一总部上传前端程序处理工具
 * 
 * @author jcm
 * 
 */
public class UnicomUploadUtil {

	/**
	 * 总部上传 文件状态文字转换
	 * 
	 * @param index
	 * @param status
	 * @return
	 */
	public static String getStateDesc(String index, String status) {
		String desc = "";
		if (status == null || "".equals(status)) {
			return desc;
		}
		if ("UPLOAD".equalsIgnoreCase(index)) {
			switch (Integer.valueOf(status).intValue()) {
			case -1:
				desc = "未上传";
				break;
			case 0:
				desc = "上传完成";
				break;
			default:
				desc = "未知状态";
			}
		} else if ("DOWN".equalsIgnoreCase(index)) {
			switch (Integer.valueOf(status).intValue()) {
			case -1:
				desc = "未取回执";
				break;
			case 0:
				desc = "已取回执";
				break;
			default:
				desc = "未知状态";
			}
		} else if ("CHK".equalsIgnoreCase(index)) {
			switch (Integer.valueOf(status).intValue()) {
			case -1:
				desc = "未处理";
				break;
			case 0:
				desc = "已处理且无错误";
				break;
			case 1:
				desc = "已处理但有错误";
				break;
			case 2:
				desc = "回执没有或为空";
				break;
			default:
				desc = "未知状态";
			}
		}
		return desc;
	}

	/**
	 * 总部上传-页面缺省日期
	 * 
	 * @param tag
	 * @return
	 */
	public static String getDefaultDate(String tag) {
		String date = "";
		if ("M".equals(tag)) { // 月
			date = DateUtil.getDiffMonth(-1, new Date());
		} else if ("W".equals(tag)) { // 周
			// 本周内的周一，以周一为一周的开始
			String[] mondays = DateUtil.getDiffWeek(
					DateUtil.getDiffDay(0, new Date()), 0, 2);
			if (mondays != null)
				date = mondays[0];
			else
				date = DateUtil.getDiffDay(-1, new Date());
		} else { // 日
			date = DateUtil.getDiffDay(-1, new Date());
		}
		System.out.println("Default date === " + date);
		return date;
	}

	/**
	 * 上传文件名中的日期字符串.日\周报格式:yymmdd,月报格式：yyyymm
	 * 
	 * @param tag
	 * @param date
	 * @return
	 */
	public static String getFileNameDateStr(String tag, String date) {
		if (("D".equals(tag) || "W".equals(tag)) && date != null
				&& date.length() == 8) {
			date = date.substring(2, date.length());
		}
		return date;
	}

}
