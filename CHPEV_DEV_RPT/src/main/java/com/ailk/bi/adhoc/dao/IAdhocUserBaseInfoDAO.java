package com.ailk.bi.adhoc.dao;

import com.ailk.bi.adhoc.domain.UiAdhocUserBaseInfo;

public interface IAdhocUserBaseInfoDAO {
	public UiAdhocUserBaseInfo getUserBaseInfo(String userNo, String gatherMon);
}
