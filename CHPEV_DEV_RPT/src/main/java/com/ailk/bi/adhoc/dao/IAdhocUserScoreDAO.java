package com.ailk.bi.adhoc.dao;

import com.ailk.bi.adhoc.domain.UiAdhocUserScore;

public interface IAdhocUserScoreDAO {
	public UiAdhocUserScore getUsreScore(String userNo, String gatherMon);
}
