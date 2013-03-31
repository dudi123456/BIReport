/**
 * 
 */
package com.ailk.bi.base.util;

/**
 * 数学运算函数返回值特定封装工场类 通过调用特定函数的返回值判定输出的数据以及相应格式 一般用于处理业务数据的数学运算函数的包装函数
 * 此工场类只用于数学运算，其他不做补充
 * 
 * @author jcm008
 */
public abstract class NullProcFactory {

	/**
	 * 转换NULL值为空字符串""
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToEmptyString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();

	}

	/**
	 * 转换NULL值为特定数据值
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * if obj==null flag == 1 return "" flag == 2 return 0 other
	 * flag.toString();
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToString(Object obj, String flag) {
		if (obj == null) {
			if ("1".equals(flag)) {
				return transNullToEmptyString(obj);
			} else if ("2".equals(flag)) {
				return transNullToZero(obj);
			} else {
				return flag.toString();
			}
		}
		return obj.toString();

	}

	/**
	 * 转换NULL值为数据值零
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToZero(Object obj) {
		if (obj == null) {
			return "0";
		}
		return obj.toString();

	}

	/**
	 * 转换NULL值为特定比例数据 if obj==null flag == 1 return 100% flag == 2 return 0%
	 * other flag.toString();
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToFixedRate(Object obj, String flag) {
		if (obj == null) {
			if ("1".equals(flag)) {
				return transNullToFullRate(obj);
			} else if ("2".equals(flag)) {
				return transNullToZeroRate(obj);
			} else {
				return flag.toString();
			}

		}
		return obj.toString();

	}

	/**
	 * 转换NULL值为全额百分比 100%
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * if(obj==null) return 100%
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToFullRate(Object obj) {
		if (obj == null) {
			return "100%";
		}
		return obj.toString();

	}

	/**
	 * 转换NULL值为全额百分比 0%
	 * 
	 * （返回值）参数不为NULL ，返回正常值
	 * 
	 * if(obj==null) return 0%
	 * 
	 * @author jcm008
	 * @return obj.toString();
	 */
	public static String transNullToZeroRate(Object obj) {
		if (obj == null) {
			return "0%";
		}
		return obj.toString();

	}

	/**
	 * 转换NULL值 或 空值为 为 0
	 * 
	 * @param obj
	 * @return
	 */
	public static String transNullEmptyToZero(Object obj) {
		if (obj == null || "".equals(obj)) {
			return "0";
		}
		return obj.toString();
	}

}
