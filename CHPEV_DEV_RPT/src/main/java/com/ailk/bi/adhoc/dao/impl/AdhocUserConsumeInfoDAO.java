package com.ailk.bi.adhoc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ailk.bi.adhoc.dao.IAdhocUserConsumeInfoDAO;
import com.ailk.bi.adhoc.domain.UiAdhocUserCFDetail;
import com.ailk.bi.adhoc.domain.UiAdhocUserCallInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserConsumeInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserIX;
import com.ailk.bi.adhoc.domain.UiAdhocUserValueAdd;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class AdhocUserConsumeInfoDAO implements IAdhocUserConsumeInfoDAO {
	private Logger logger = Logger.getLogger(AdhocUserConsumeInfoDAO.class);

	public UiAdhocUserConsumeInfo getUserConsumeInfo(String userNo,
			String gatherMon) {
		UiAdhocUserConsumeInfo consumeInfo = null;
		if (null != userNo && !"".equals(userNo) && null != gatherMon
				&& !"".equals(gatherMon)) {
			// 获取基本消费行为
			consumeInfo = getBaseConsumeInfo(userNo, gatherMon);
			// 获取呼转明细
			// consumeInfo.userCFDetails = getCFInfo(userNo, gatherMon);
			// 获取增值业务
			// consumeInfo.valueAdds = getValueAddInfo(userNo, gatherMon);
			// 获取IX业务
			// consumeInfo.userIXs = getIXInfo(userNo, gatherMon);
		}
		return consumeInfo;
	}

	public List getUserCallInfo(String userNo, String gatherMon) {
		List callInfos = null;
		if (null != userNo && !"".equals(userNo) && null != gatherMon
				&& !"".equals(gatherMon)) {
			StringBuffer select = new StringBuffer();
			select.append("SELECT GATHER_MON,");
			select.append("V_CALL_TIMES,V_ORG_DURA/60,V_TRM_DURA/60,V_LOC_DURA/60,");
			select.append("V_TOLL_DURA/60,V_I_TOLL_DURA/60,V_N_TOLL_DURA/60 ");
			select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(
					userNo);
			select.append(" AND GATHER_MON>=").append(
					DateUtil.getDiffMonth(-5, gatherMon));
			select.append(" AND GATHER_MON<=").append(gatherMon);
			select.append(" ORDER BY GATHER_MON DESC");
			try {
				System.out.println(select);
				String[][] svces = WebDBUtil
						.execQryArray(select.toString(), "");
				if (null != svces) {
					callInfos = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						int index = 0;
						UiAdhocUserCallInfo callInfo = new UiAdhocUserCallInfo();
						callInfo.gather_mon = svces[i][index];
						index++;
						callInfo.call_times = svces[i][index];
						index++;
						callInfo.v_org_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						index++;
						callInfo.v_trm_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						index++;
						callInfo.v_loc_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						index++;
						callInfo.v_toll_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						index++;
						callInfo.v_i_toll_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						index++;
						callInfo.v_n_toll_dura = FormatUtil.formatStr(
								svces[i][index], 2, false);
						callInfos.add(callInfo);
					}
				}
			} catch (AppException e) {
			}
		}
		return callInfos;
	}

	private UiAdhocUserConsumeInfo getBaseConsumeInfo(String userNo,
			String gatherMon) {
		UiAdhocUserConsumeInfo consumeInfo = new UiAdhocUserConsumeInfo();
		try {
			String select = SQLGenator.genSQL("QT046", userNo, gatherMon);
			System.out.println("QT046:" + select);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces) {
				int index = 0;
				consumeInfo = new UiAdhocUserConsumeInfo();
				// index++;consumeInfo.v_call_num= svces[0][index];
				// index++;consumeInfo.v_org_call_num= svces[0][index];
				consumeInfo.v_call_times = svces[0][index];
				index++;
				// consumeInfo.v_d_avg_call_times = svces[0][index];
				// index++;
				consumeInfo.v_call_dura = Arith.divs(svces[0][index], "60", 2);
				index++;
				// consumeInfo.v_d_avg_dura = svces[0][index];
				// index++;
				if (!"0".equals(consumeInfo.v_call_times)) {
					if (svces[0][index].indexOf("-") >= 0)
						consumeInfo.local_roam = "漫游";
					else
						consumeInfo.local_roam = "本地";
					index++;
					if (svces[0][index].indexOf("-") >= 0)
						consumeInfo.busy_spare = "忙时";
					else
						consumeInfo.busy_spare = "闲时";
					index++;
				} else {
					index++;
					index++;
				}
				System.out.println(index);
				consumeInfo.v_lac_num = svces[0][index];
				index++;
				consumeInfo.v_fst_lac_desc = svces[0][index];
				// index++;
				// consumeInfo.v_fst_lac_times = svces[0][index];
				// index++;
				// consumeInfo.v_fst_lac_dura = svces[0][index];
				index++;
				consumeInfo.v_fst_busy_seg = svces[0][index];
				index++;
				consumeInfo.v_fst_busy_times = svces[0][index];
				index++;
				consumeInfo.v_fst_busy_dura = Arith.divs(svces[0][index], "60",
						2);
				index++;
				consumeInfo.v_fstcarrier_desc = svces[0][index];
				index++;
				consumeInfo.v_fstcarrier_times = svces[0][index];
				index++;
				consumeInfo.v_fstcarrier_dura = Arith.divs(svces[0][index],
						"60", 2);
				index++;
				consumeInfo.v_cf_times = svces[0][index];
				index++;
				consumeInfo.cf_times_ratio = svces[0][index];
				index++;
				consumeInfo.v_cf_dura = Arith.divs(svces[0][index], "60", 2);
				index++;
				consumeInfo.v_max_cf_svc_id = svces[0][index];
				index++;
				consumeInfo.v_max_cf_times = svces[0][index];
				index++;
				consumeInfo.v_max_cf_dura = Arith
						.divs(svces[0][index], "60", 2);
				index++;
				consumeInfo.v_i_cf_times = svces[0][index];
				index++;
				consumeInfo.v_i_cf_dura = Arith.divs(svces[0][index], "60", 2);
				;
				index++;
				consumeInfo.v_o_cf_times = svces[0][index];
				index++;
				consumeInfo.v_o_cf_dura = Arith.divs(svces[0][index], "60", 2);
				index++;
				consumeInfo.s_sms_num = svces[0][index];
				index++;
				consumeInfo.ptp_sms_ratio = svces[0][index];
				index++;
				consumeInfo.s_ix_bytes = svces[0][index];
			}
		} catch (AppException e) {
		}
		return consumeInfo;
	}

	private List getCFInfo(String userNo, String gatherMon) {
		List infos = null;
		// 需要设置六个月的时间
		StringBuffer select = new StringBuffer();
		select.append("SELECT OPP_NUMBER,");
		for (int i = 0; i < 6; i++) {
			select.append("CASE WHEN GATHER_MON=");
			select.append(DateUtil.getDiffMonth(-i, gatherMon));
			select.append(" THEN CALL_TIMES ELSE 0 END,");
		}
		// 去掉最后一个逗号
		select.deleteCharAt(select.length() - 1);
		select.append(" FROM FM_USER_CF_INFO_M WHERE USER_NO=").append(userNo);
		select.append(" AND GATHER_MON>=").append(
				DateUtil.getDiffMonth(-5, gatherMon));
		select.append(" AND GATHER_MON<=").append(gatherMon);
		try {
			System.out.println(select);
			String[][] svces = WebDBUtil.execQryArray(select.toString(), "");
			if (null != svces) {
				infos = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					int index = 0;
					UiAdhocUserCFDetail detail = new UiAdhocUserCFDetail();
					detail.opp_no = svces[i][index];
					index++;
					detail.call_times_1 = svces[i][index];
					index++;
					detail.call_times_2 = svces[i][index];
					index++;
					detail.call_times_3 = svces[i][index];
					index++;
					detail.call_times_4 = svces[i][index];
					index++;
					detail.call_times_5 = svces[i][index];
					index++;
					detail.call_times_6 = svces[i][index];
					infos.add(detail);
				}
			}
		} catch (AppException e) {
		}
		return infos;
	}

	private List getValueAddInfo(String userNo, String gatherMon) {
		List infos = null;
		StringBuffer select = new StringBuffer();
		select.append("SELECT GATHER_MON,");
		select.append("S_I_PTP_MO_NUM,S_O_CMC_PTP_MO_NUM+S_O_CMC_PTP_MT_NUM,");
		select.append("S_O_CNC_PTP_MO_NUM+S_O_CNC_PTP_MT_NUM,");
		select.append("S_O_PHS_PTP_MO_NUM+S_O_PHS_PTP_MT_NUM,");
		select.append("S_O_PTP_NUM-S_O_CMC_PTP_MO_NUM-S_O_PHS_PTP_MO_NUM-S_O_CMC_PTP_MT_NUM-S_O_PHS_PTP_MT_NUM-S_O_CNC_PTP_MO_NUM-S_O_CNC_PTP_MT_NUM,");
		select.append("S_LLTZX_MO_NUM+S_LLTZX_MT_NUM,");
		select.append("S_NLTZX_MO_NUM+S_NLTZX_MT_NUM,");
		select.append("S_VOICE_10155_NUM,");
		select.append("S_VOICE_101567_NUM,");
		select.append("S_VOICE_10158_NUM,");
		select.append("S_VOICE_10159_NUM,");
		select.append("S_VOICE_OTH_NUM ");
		select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(userNo);
		select.append(" AND GATHER_MON>=").append(
				DateUtil.getDiffMonth(-5, gatherMon));
		select.append(" AND GATHER_MON<=").append(gatherMon);
		select.append(" ORDER BY GATHER_MON DESC");
		try {
			System.out.println(select);
			String[][] svces = WebDBUtil.execQryArray(select.toString(), "");
			if (null != svces) {
				infos = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					int index = 0;
					UiAdhocUserValueAdd value = new UiAdhocUserValueAdd();
					value.gather_mon = svces[i][index];
					index++;
					value.s_i_ptp_mo_num = svces[i][index];
					index++;
					value.s_o_cmc_ptp_mo_num = svces[i][index];
					index++;
					value.s_o_cnet_ptp_mo_num = svces[i][index];
					index++;
					value.s_o_phs_ptp_mo_num = svces[i][index];
					index++;
					value.s_o_oth_ptp_mo_num = svces[i][index];
					index++;
					value.local_ltzx = svces[i][index];
					index++;
					value.toll_ltzx = svces[i][index];
					index++;
					value.sms_10155 = svces[i][index];
					index++;
					value.sms_10156 = svces[i][index];
					index++;
					value.sms_10158 = svces[i][index];
					index++;
					value.sms_10159 = svces[i][index];
					index++;
					value.sms_other = svces[i][index];
					infos.add(value);
				}
			}
		} catch (AppException ape) {
		}
		return infos;
	}

	private List getIXInfo(String userNo, String gatherMon) {
		List infos = null;
		StringBuffer select = new StringBuffer();
		select.append("SELECT GATHER_MON,");
		select.append("S_IX_BYTES,S_HAND_IX_BYTES,S_HDSJ_IX_BYTES,");
		select.append("S_E_IX_BYTES,S_STAR_IX_BYTES,S_SXXGX_IX_BYTES,");
		select.append("S_BREW_IX_BYTES+S_JAVA_IX_BYTES,S_EC_IX_BYTES,");
		select.append("S_PUB_IX_BYTES,S_OTH_IX_BYTES");
		select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(userNo);
		select.append(" AND GATHER_MON>=").append(
				DateUtil.getDiffMonth(-5, gatherMon));
		select.append(" AND GATHER_MON<=").append(gatherMon);
		select.append(" ORDER BY GATHER_MON DESC");
		try {
			System.out.println(select);
			String[][] svces = WebDBUtil.execQryArray(select.toString(), "");
			if (null != svces) {
				infos = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					int index = 0;
					UiAdhocUserIX ixInfo = new UiAdhocUserIX();
					ixInfo.gather_mon = svces[i][index];
					index++;
					ixInfo.ix_total = svces[i][index];
					index++;
					ixInfo.zzkd_ix = svces[i][index];
					index++;
					ixInfo.hdsj_ix = svces[i][index];
					index++;
					ixInfo.ce_ix = svces[i][index];
					index++;
					ixInfo.dwzx_ix = svces[i][index];
					index++;
					ixInfo.sxxgx_ix = svces[i][index];
					index++;
					ixInfo.sqbd_ix = svces[i][index];
					index++;
					ixInfo.dzsw_ix = svces[i][index];
					index++;
					ixInfo.gyll_ix = svces[i][index];
					index++;
					ixInfo.ix_other = svces[i][index];
					infos.add(ixInfo);
				}
			}
		} catch (AppException ape) {
		}
		return infos;
	}
}
