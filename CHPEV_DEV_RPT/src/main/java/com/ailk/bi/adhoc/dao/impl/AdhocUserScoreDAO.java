package com.ailk.bi.adhoc.dao.impl;

import com.ailk.bi.adhoc.dao.IAdhocUserScoreDAO;
import com.ailk.bi.adhoc.domain.UiAdhocUserScore;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import org.apache.log4j.Logger;

public class AdhocUserScoreDAO implements IAdhocUserScoreDAO {
	private Logger logger = Logger.getLogger(AdhocUserScoreDAO.class);

	public UiAdhocUserScore getUsreScore(String userNo, String gatherMon) {
		UiAdhocUserScore userScore = null;
		if (null != userNo && !"".equals(userNo) && null != gatherMon
				&& !"".equals(gatherMon)) {
			StringBuffer select = new StringBuffer();
			select.append("SELECT SCORE,AGG_SCORE,BASE_SCORE,AGG_BASE_SCORE,");
			select.append("COMSUME_SCORE,AGG_COMSUME_SCORE,ONL_SCORE,AGG_ONL_SCORE,");
			select.append("EXCH_SCORE,CANCEL_SCORE ");
			select.append(" FROM FM_UNI_USER_VIEW_M WHERE USER_NO=").append(
					userNo);
			select.append(" AND GATHER_MON=").append(gatherMon);
			try {
				logger.debug(select);
				String[][] svces = WebDBUtil
						.execQryArray(select.toString(), "");
				if (null != svces) {
					userScore = new UiAdhocUserScore();
					int index = 0;
					userScore.score = svces[0][index];
					index++;
					userScore.agg_score = svces[0][index];
					index++;
					userScore.base_score = svces[0][index];
					index++;
					userScore.agg_base_score = svces[0][index];
					index++;
					userScore.consume_score = svces[0][index];
					index++;
					userScore.agg_consume_score = svces[0][index];
					index++;
					userScore.onl_score = svces[0][index];
					index++;
					userScore.agg_onl_score = svces[0][index];
					index++;
					userScore.exch_score = svces[0][index];
					index++;
					userScore.cancel_score = svces[0][index];
				}
			} catch (AppException ape) {
			}
		}
		return userScore;
	}

}
