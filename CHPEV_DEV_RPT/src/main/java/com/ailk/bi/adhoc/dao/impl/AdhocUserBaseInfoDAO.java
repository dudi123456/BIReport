package com.ailk.bi.adhoc.dao.impl;

import org.apache.log4j.Logger;

import com.ailk.bi.adhoc.dao.IAdhocUserBaseInfoDAO;
import com.ailk.bi.adhoc.domain.UiAdhocUserBaseInfo;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class AdhocUserBaseInfoDAO implements IAdhocUserBaseInfoDAO {
	private Logger logger = Logger.getLogger(AdhocUserBaseInfoDAO.class);

	public UiAdhocUserBaseInfo getUserBaseInfo(String userNo, String gatherMon) {
		// 拼装一个大的查询语句，包括维表查询
		UiAdhocUserBaseInfo userBaseInfo = null;
		if (null != userNo && !"".equals(userNo) && null != gatherMon
				&& !"".equals(gatherMon)) {
			StringBuffer select = new StringBuffer();
			StringBuffer from = new StringBuffer(" FROM FM_UNI_USER_VIEW_M A ");
			StringBuffer where = new StringBuffer(" WHERE A.USER_NO=");
			where.append(userNo).append(" AND A.GATHER_MON=").append(gatherMon);
			select.append("SELECT A.USER_NO,A.DEVICE_NUMBER,A.CUSTOMER_NO,A.CUSTOMER_NAME,");
			select.append("A.CORP_NO,A.CORP_NAME,A.VIP_LEVEL,A.INNET_TAG,");
			select.append("A.BRAND_KND,A.INNET_CHANNEL,A.EVAL_AREA_ID,");
			select.append("A.STATUS_CODE,A.IN_NET_DATE,A.OFF_NET_DATE,");
			select.append("A.SEX_FLAG,A.AGE,A.CUSTOMER_PHONE,");
			select.append("A.ID_TYPE,A.CUSTOMER_ID,A.CUSTOMER_ADDRESS,A.CUSTOMER_POST,");
			select.append("A.PRODUCT,A.DINNER_PKG,A.PRODUCT_TYPE,");
			select.append("A.MAC_TYPE,A.CARD_KND,");
			select.append("A.BEG_DAY,A.END_DAY,A.REAL_END_DAY,A.AGREE_FEE,A.AGREE_MONS,");
			select.append("A.RENT_TIMES,COALESCE(A.CONSUMED_FEE/(CASE WHEN A.AGREE_FEE=0 THEN NULL ELSE A.AGREE_FEE END),0),");
			select.append("A.AGREE_MONS-A.CONSUMED_MONS,A.MIN_CONSUMED_FEE,");
			select.append("A.AVG_CONSUMED_FEE,A.LINK_NAME,A.LINK_PHONE,A.LINK_EMAIL ");
			select.append(from).append(where).append(") A ");
			from.delete(0, from.length());
			from.append(" FROM (").append(select);
			select.delete(0, select.length());
			select.append("SELECT A.*,B.VIPLEVEL_DESC,");
			from.append(" LEFT JOIN D_VIP_LEVEL B ON A.VIP_LEVEL=B.VIP_LEVEL ");
			select.append("C.INNETTAG_DESC,");
			from.append(" LEFT JOIN D_INNET_TAG C ON A.INNET_TAG=C.INNET_TAG ");
			select.append("D.BRANDKND_DESC,");
			from.append(" LEFT JOIN D_BRAND_KND D ON A.BRAND_KND=D.BRAND_KND ");
			select.append("E.CHANNEL_DESC,");
			from.append(" LEFT JOIN D_CHANNEL E ON A.INNET_CHANNEL=E.CHANNEL_ID ");
			select.append("F.AREA_DESC,");
			from.append(" LEFT JOIN D_AREA F ON A.EVAL_AREA_ID=F.AREA_ID ");
			select.append("G.STATUS_DESC,");
			from.append(" LEFT JOIN D_USER_STATUS G ON A.STATUS_CODE=G.STATUS_CODE ");
			select.append("H.SEX_DESC,");
			from.append(" LEFT JOIN D_SEX H ON A.SEX_FLAG=H.SEX_ID ");
			select.append("I.IDTYPE_DESC,");
			from.append(" LEFT JOIN D_ID_TYPE I ON A.ID_TYPE=I.ID_TYPE ");
			select.append("J.COMMENTS,");
			from.append(" LEFT JOIN D_CODE_PRODUCT J ON A.PRODUCT=J.PRODUCT ");
			select.append("K.COMMENTS,");
			from.append(" LEFT JOIN D_PKG_DINNER K ON A.DINNER_PKG=K.DINNER_PKG ");
			select.append("L.PRODUCTTYPE_DESC, ");
			from.append(" LEFT JOIN D_PRODUCT_TYPE L ON A.PRODUCT_TYPE=L.PRODUCT_TYPE ");
			select.append("M.MACTYPE_DESC,");
			from.append(" LEFT JOIN D_MAC_TYPE M ON A.MAC_TYPE=M.MAC_TYPE ");
			select.append("N.COMMENTS ");
			from.append(" LEFT JOIN D_XIN_TYPE N ON A.CARD_KND=N.XIN_TYPE ");
			select.append(from);
			try {
				logger.debug(select);
				String[][] svces = WebDBUtil
						.execQryArray(select.toString(), "");
				if (null != svces) {
					int index = 0;
					userBaseInfo = new UiAdhocUserBaseInfo();
					userBaseInfo.gather_mon = gatherMon;
					userBaseInfo.user_no = svces[0][index];
					index++;
					userBaseInfo.device_number = svces[0][index];
					index++;
					userBaseInfo.customer_no = svces[0][index];
					index++;
					userBaseInfo.customer_name = svces[0][index];
					index++;
					index++;

					index++;
					index++;

					index++;
					index++;
					index++;
					index++;

					index++;
					userBaseInfo.in_net_date = svces[0][index];
					index++;
					userBaseInfo.off_net_date = svces[0][index];
					index++;
					index++;
					userBaseInfo.customer_age = svces[0][index];
					index++;
					userBaseInfo.customer_phone = svces[0][index];
					index++;
					index++;
					userBaseInfo.id_no = svces[0][index];
					index++;
					userBaseInfo.customer_address = svces[0][index];
					index++;
					userBaseInfo.post_code = svces[0][index];
					index++;
					index++;
					index++;
					index++;

					index++;

					index++;
					userBaseInfo.beg_day = svces[0][index];
					index++;
					userBaseInfo.end_day = svces[0][index];
					index++;
					userBaseInfo.real_end_day = svces[0][index];
					index++;
					userBaseInfo.agree_fee = svces[0][index];
					index++;
					userBaseInfo.agree_mons = svces[0][index];
					index++;
					userBaseInfo.rent_times = svces[0][index];
					index++;
					userBaseInfo.consumed_ratio = svces[0][index];
					index++;
					userBaseInfo.rest_mons = svces[0][index];
					index++;
					userBaseInfo.min_consumed_fee = svces[0][index];
					index++;
					userBaseInfo.avg_consumed_fee = svces[0][index];
					index++;
					userBaseInfo.link_name = svces[0][index];
					index++;
					userBaseInfo.link_phone = svces[0][index];
					index++;
					userBaseInfo.link_email = svces[0][index];
					index++;
					userBaseInfo.vip_level = svces[0][index];
					index++;
					userBaseInfo.innet_tag = svces[0][index];
					index++;
					userBaseInfo.brand_knd = svces[0][index];
					index++;
					userBaseInfo.innet_channel = svces[0][index];
					index++;
					userBaseInfo.eval_area_id = svces[0][index];
					index++;
					userBaseInfo.user_status = svces[0][index];
					index++;
					userBaseInfo.customer_gender = svces[0][index];
					index++;
					userBaseInfo.id_type = svces[0][index];
					index++;
					userBaseInfo.product = svces[0][index];
					index++;
					userBaseInfo.dinner_pkg = svces[0][index];
					index++;
					userBaseInfo.product_type = svces[0][index];
					index++;
					userBaseInfo.mac_type = svces[0][index];
					index++;
					userBaseInfo.card_knd = svces[0][index];
				}
			} catch (AppException ape) {

			}
		}
		return userBaseInfo;
	}

}
