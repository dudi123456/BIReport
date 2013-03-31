package com.ailk.bi.adhoc.dao;

import com.ailk.bi.adhoc.domain.UiAdhocUserContribute;

public interface IAdhocUserContributeInfoDAO {
	public UiAdhocUserContribute getUserContributeInfo(String userNo,
			String gatherMon);
}
