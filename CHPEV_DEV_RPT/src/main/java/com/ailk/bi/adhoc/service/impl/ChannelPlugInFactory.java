package com.ailk.bi.adhoc.service.impl;

import com.ailk.bi.adhoc.plugin.IAdhocPlugIn;
import com.ailk.bi.adhoc.plugin.impl.ChannelPlugIn;
import com.ailk.bi.adhoc.service.IAdhocPlugInFactory;

public class ChannelPlugInFactory implements IAdhocPlugInFactory {

	public IAdhocPlugIn newInstance() {
		return new ChannelPlugIn();
	}

}
