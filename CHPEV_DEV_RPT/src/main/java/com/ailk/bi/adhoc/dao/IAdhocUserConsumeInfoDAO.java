package com.ailk.bi.adhoc.dao;

import java.util.List;

import com.ailk.bi.adhoc.domain.UiAdhocUserConsumeInfo;

@SuppressWarnings({ "rawtypes" })
public interface IAdhocUserConsumeInfoDAO {
	public UiAdhocUserConsumeInfo getUserConsumeInfo(String userNo,
			String gatherMon);

	public List getUserCallInfo(String userNo, String gatherMon);
}
