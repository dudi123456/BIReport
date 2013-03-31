package com.ailk.bi.adhoc.service.impl;

import java.util.List;

import com.ailk.bi.adhoc.dao.IAdhocUserBaseInfoDAO;
import com.ailk.bi.adhoc.dao.IAdhocUserConsumeInfoDAO;
import com.ailk.bi.adhoc.dao.IAdhocUserContributeInfoDAO;
import com.ailk.bi.adhoc.dao.IAdhocUserScoreDAO;
import com.ailk.bi.adhoc.dao.impl.AdhocUserBaseInfoDAO;
import com.ailk.bi.adhoc.dao.impl.AdhocUserConsumeInfoDAO;
import com.ailk.bi.adhoc.dao.impl.AdhocUserContributeInfoDAO;
import com.ailk.bi.adhoc.dao.impl.AdhocUserScoreDAO;
import com.ailk.bi.adhoc.domain.UiAdhocUserBaseInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserConsumeInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserContribute;
import com.ailk.bi.adhoc.domain.UiAdhocUserScore;
import com.ailk.bi.adhoc.service.IAdhocUserService;

@SuppressWarnings({ "rawtypes" })
public class AdhocUserService implements IAdhocUserService {

	private IAdhocUserBaseInfoDAO baseInfoDAO = new AdhocUserBaseInfoDAO();
	private IAdhocUserConsumeInfoDAO consumeInfoDAO = new AdhocUserConsumeInfoDAO();
	private IAdhocUserContributeInfoDAO contributeInfoDAO = new AdhocUserContributeInfoDAO();
	private IAdhocUserScoreDAO scoreDAO = new AdhocUserScoreDAO();

	public UiAdhocUserBaseInfo getUserBaseInfo(String userNo, String gatherMon) {
		return baseInfoDAO.getUserBaseInfo(userNo, gatherMon);
	}

	public List getUserCallInfo(String userNo, String gatherMon) {
		return consumeInfoDAO.getUserCallInfo(userNo, gatherMon);
	}

	public UiAdhocUserConsumeInfo getUserConsumeInfo(String userNo,
			String gatherMon) {
		return consumeInfoDAO.getUserConsumeInfo(userNo, gatherMon);
	}

	public UiAdhocUserContribute getUserContributeInfo(String userNo,
			String gatherMon) {
		return contributeInfoDAO.getUserContributeInfo(userNo, gatherMon);
	}

	public UiAdhocUserScore getUsreScore(String userNo, String gatherMon) {
		return scoreDAO.getUsreScore(userNo, gatherMon);
	}

}
