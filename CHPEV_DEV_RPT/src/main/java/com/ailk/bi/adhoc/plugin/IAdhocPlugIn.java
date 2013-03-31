package com.ailk.bi.adhoc.plugin;

import javax.servlet.http.HttpSession;

public interface IAdhocPlugIn {

	String AdhocBusiness(String baseCode, HttpSession session);

}
