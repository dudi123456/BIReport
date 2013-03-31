package com.ailk.bi.adhoc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.dao.IAdhocUserContributeInfoDAO;
import com.ailk.bi.adhoc.domain.UiAdhocUserContribute;
import com.ailk.bi.adhoc.domain.UiAdhocUserFavFeeInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserIX;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

import org.apache.log4j.Logger;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class AdhocUserContributeInfoDAO implements IAdhocUserContributeInfoDAO {
	private Logger logger = Logger.getLogger(AdhocUserContributeInfoDAO.class);

	public UiAdhocUserContribute getUserContributeInfo(String userNo,
			String gatherMon) {
		UiAdhocUserContribute userInfo = null;
		if (null != userNo && !"".equals(userNo) && null != gatherMon
				&& !"".equals(gatherMon)) {
			// 获取基本信息
			userInfo = getBaseInfo(userNo, gatherMon);
			List result = getSixMonInfo(userNo, gatherMon);
			if (null != result && result.size() >= 2) {
				userInfo.sixMonFavFee = (List) result.get(0);
				userInfo.sixMonIX = (List) result.get(1);
			}
			// 获取近6个月信息
		}
		return userInfo;
	}

	private UiAdhocUserContribute getBaseInfo(String userNo, String gatherMon) {
		UiAdhocUserContribute userInfo = new UiAdhocUserContribute();
		StringBuffer select = new StringBuffer();
		select.append("SELECT SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN F_FAV_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN F_PREF_FAV_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=")
				.append(gatherMon)
				.append(" THEN F_PREF_FAV_FEE ELSE 0 END)/SUM(CASE WHEN GATHER_MON=")
				.append(gatherMon)
				.append(" THEN F_PREF_FAV_FEE+F_FAV_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN V_BASE_FEE_DURA ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=")
				.append(gatherMon)
				.append(" THEN F_FAV_FEE ELSE 0 END)/SUM(CASE WHEN GATHER_MON=")
				.append(gatherMon).append(" THEN V_BASE_FEE_DURA ELSE 0 END),");
		select.append("SUM(F_FAV_FEE)/3,");
		select.append("SUM(V_BASE_FEE_DURA)/3,");
		select.append("SUM(F_FAV_FEE)/(3*SUM(V_BASE_FEE_DURA)),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_AGG_ARRFEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_ARR_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_YARR_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_FIRST_ARR_MON ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_LAST_ARR_MON ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN A_ARR_ACCT_MON_NUM ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN R_REAL_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN R_CORPUS_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN R_LAST_MON_FEE ELSE 0 END),");
		select.append("SUM(CASE WHEN GATHER_MON=").append(gatherMon)
				.append(" THEN R_THIS_YEAR_FEE ELSE 0 END) ");
		select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(userNo);
		select.append(" AND GATHER_MON>=").append(
				DateUtil.getDiffMonth(-2, gatherMon));
		select.append(" AND GATHER_MON<=").append(gatherMon);
		try {
			System.out.println(select);
			String[][] svces = WebDBUtil.execQryArray(select.toString(), "");
			if (null != svces) {
				int index = 0;
				userInfo.f_fav_fee = svces[0][index];
				index++;
				userInfo.f_pref_fav_fee = svces[0][index];
				index++;
				userInfo.f_pref_fav_fee_ratio = svces[0][index];
				index++;
				userInfo.v_base_fee_dura = FormatUtil.formatStr(
						Arith.divs(svces[0][index], "60", 3), 2, false);
				index++;
				userInfo.arpm = FormatUtil.formatStr(
						Arith.mul(svces[0][index], "60", 4), 3, false);
				index++;
				userInfo.thr_avg_arpu = FormatUtil.formatStr(svces[0][index],
						2, false);
				index++;
				userInfo.thr_avg_mou = FormatUtil.formatStr(
						Arith.divs(svces[0][index], "60", 3), 2, false);
				index++;
				userInfo.thr_avg_arpm = FormatUtil.formatStr(
						Arith.mul(svces[0][index], "60", 4), 3, false);
				index++;
				userInfo.a_aggarr_fee = svces[0][index];
				index++;
				userInfo.a_arr_fee = svces[0][index];
				index++;
				userInfo.a_yarr_fee = svces[0][index];
				index++;
				userInfo.a_first_arr_mon = svces[0][index];
				index++;
				userInfo.a_last_arr_mon = svces[0][index];
				index++;
				userInfo.a_arr_acct_mon_num = svces[0][index];
				index++;
				userInfo.r_real_fee = svces[0][index];
				index++;
				userInfo.r_real_cost = svces[0][index];
				index++;
				userInfo.r_last_mon_fee = svces[0][index];
				index++;
				userInfo.r_this_year_fee = svces[0][index];
			}
		} catch (AppException ape) {
		}
		return userInfo;
	}

	private List getSixMonInfo(String userNo, String gatherMon) {
		List infos = null;
		StringBuffer select = new StringBuffer();
		select.append("SELECT GATHER_MON,SUM(F_FAV_FEE),");
		select.append("SUM(F_PREF_FAV_FEE),");
		select.append("SUM(F_RENT_FEE),SUM(F_LOC_FEE),SUM(F_ROAM_FEE),");
		select.append("SUM(F_TOLL_FEE)+SUM(F_IP_TOLL_FEE+F_I93_TOLL_FEE),SUM(F_SMS_FEE),SUM(F_IX_FEE),");
		select.append("SUM(F_ADDVAL_FEE)-SUM(F_SMS_FEE-F_IX_FEE),SUM(F_LATE_FEE),SUM(F_OTH_FAV_FEE),");
		select.append("SUM(F_IX_FEE),SUM(F_HAND_IX_FEE),SUM(F_HDSJ_GN_IX_FEE)+SUM(F_HDSJ_LOC_IX_FEE),");
		select.append("SUM(F_E_IX_FEE),SUM(F_EC_IX_FEE),SUM(F_PUB_STR_IX_FEE),SUM(F_STAR_PUB_IX_FEE)+SUM(F_STAR_VOCA_IX_FEEA),");
		select.append("SUM(F_SXXGX_MAC_IX_FEE)+SUM(F_SXXGX_PDA_IX_FEE),SUM(F_SQBD_BREW_IX_FEE)+SUM(F_SQBD_JAVA_IX_FEE)+SUM(F_SQBD_OTH_IX_FEE),SUM(F_OTH_IX_FEE) ");
		select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(userNo);
		select.append(" AND GATHER_MON>=").append(
				DateUtil.getDiffMonth(-5, gatherMon));
		select.append(" AND GATHER_MON<=").append(gatherMon);
		select.append(" GROUP BY GATHER_MON ");
		select.append(" ORDER BY GATHER_MON DESC");
		try {
			System.out.println(select);
			String[][] svces = WebDBUtil.execQryArray(select.toString(), "");
			if (null != svces) {
				infos = new ArrayList();
				List favInfos = new ArrayList();
				List ixInfos = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					int index = 0;
					UiAdhocUserFavFeeInfo favInfo = new UiAdhocUserFavFeeInfo();
					favInfo.gather_mon = svces[i][index];
					index++;
					favInfo.f_fav_fee = svces[i][index];
					index++;
					favInfo.f_pref_fav_fee = svces[i][index];
					index++;
					favInfo.f_rent_fee = svces[i][index];
					index++;
					favInfo.f_loc_fee = svces[i][index];
					index++;
					favInfo.f_roam_fee = svces[i][index];
					index++;
					favInfo.f_toll_fee = svces[i][index];
					index++;
					favInfo.f_sms_fee = svces[i][index];
					index++;
					favInfo.f_ix_fee = svces[i][index];
					index++;
					favInfo.f_other_fee = svces[i][index];
					index++;
					favInfo.f_late_fee = svces[i][index];
					index++;
					favInfo.f_oth_fav_fee = svces[i][index];
					favInfos.add(favInfo);
					UiAdhocUserIX ixInfo = new UiAdhocUserIX();
					ixInfo.gather_mon = svces[i][0];
					index++;
					ixInfo.ix_total = svces[i][index];
					index++;
					ixInfo.zzkd_ix = svces[i][index];
					index++;
					ixInfo.hdsj_ix = svces[i][index];
					index++;
					ixInfo.ce_ix = svces[i][index];
					index++;
					ixInfo.dzsw_ix = svces[i][index];
					index++;
					ixInfo.gyll_ix = svces[i][index];
					index++;
					ixInfo.dwzx_ix = svces[i][index];
					index++;
					ixInfo.sxxgx_ix = svces[i][index];
					index++;
					ixInfo.sqbd_ix = svces[i][index];
					index++;
					ixInfo.ix_other = svces[i][index];
					ixInfos.add(ixInfo);
				}
				infos.add(favInfos);
				infos.add(ixInfos);
			}
		} catch (AppException ape) {
		}
		return infos;
	}
}
