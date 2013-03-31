package com.ailk.bi.adhoc.util;

import java.util.TimerTask;

public class AdhocExportXlsTask extends TimerTask {

	AdhocSaveTaskInJob jobSave;

	public AdhocExportXlsTask(AdhocSaveTaskInJob jobSave) {
		this.jobSave = jobSave;
	}

	public void run() {

		jobSave.doSaveTask();
	}

}
