package com.ailk.bi.adhoc.service;

import com.ailk.bi.adhoc.plugin.IAdhocPlugIn;

public interface IAdhocPlugInFactory {
	public IAdhocPlugIn newInstance();

}
