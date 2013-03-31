package com.ailk.bi.subject.util;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;

public class UserRightUtil {
	// private static final String USER_RIGHT_LVL_HEADQ = "0";
	private static final String USER_RIGHT_LVL_PROVICE = "1";
	private static final String USER_RIGHT_LVL_CITY = "2";
	private static final String USER_RIGHT_LVL_COUNTY = "3";

	private UserRightUtil() {

	}

	public static boolean canLinkDisplay(SubjectCommTabDef subTable,
			SubjectCommTabCol tabCol, String[] svces) {
		boolean canDisplay = false;
		if (!SubjectConst.YES.equalsIgnoreCase(tabCol.link_limit_right))
			return true;
		if (null != subTable.userRight) {
			UserCtlRegionStruct userRight = subTable.userRight;
			// 权限较小
			if (userRight.ctl_lvl.compareTo(subTable.right_lvl) > 0) {
				return false;

			}
			// 权限大
			if (userRight.ctl_lvl.compareTo(subTable.right_lvl) < 0) {
				return true;
			}
			// 权限相等，需要判断值了
			if (null == svces || subTable.rightValueIndex == -1
					|| subTable.rightValueIndex >= svces.length - 1) {
				return false;
			}
			String rights = null;
			String rightValue = svces[subTable.rightValueIndex];
			// 取出
			if (USER_RIGHT_LVL_PROVICE.equals(userRight.ctl_lvl)) {
				rights = userRight.ctl_metro_str;
			}
			if (USER_RIGHT_LVL_CITY.equals(userRight.ctl_lvl)) {
				rights = userRight.ctl_city_str;
			}
			if (USER_RIGHT_LVL_COUNTY.equals(userRight.ctl_lvl)) {
				rights = userRight.ctl_county_str;
			}
			if (StringUtils.isBlank(rights)) {
				return false;
			}
			rights = rights.trim();
			rightValue = rightValue.trim();
			if (rights.indexOf(rightValue) >= 0) {
				return true;
			}
		}
		return canDisplay;
	}
}
