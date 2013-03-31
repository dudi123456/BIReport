package com.ailk.bi.adhoc.service;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocUserBaseInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserConsumeInfo;
import com.ailk.bi.adhoc.domain.UiAdhocUserContribute;
import com.ailk.bi.adhoc.domain.UiAdhocUserScore;

@SuppressWarnings({ "rawtypes" })
public interface IAdhocUserService {
	public UiAdhocUserBaseInfo getUserBaseInfo(String userNo, String gatherMon);

	public UiAdhocUserConsumeInfo getUserConsumeInfo(String userNo,
			String gatherMon);

	public List getUserCallInfo(String userNo, String gatherMon);

	public UiAdhocUserContribute getUserContributeInfo(String userNo,
			String gatherMon);

	public UiAdhocUserScore getUsreScore(String userNo, String gatherMon);
}
