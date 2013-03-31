package com.ailk.bi.common.tree;

//import java.sql.*;

import javax.servlet.http.HttpServletRequest;

import com.crystaldecisions.sdk.exception.SDKException;
//import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

public class TreeService {

	public String getNodesInfo(Integer pid, HttpServletRequest request) {
		IInfoObjects list = null;
		String query = "SELECT SI_ID, SI_NAME, SI_PARENTID,SI_KIND FROM CI_INFOOBJECTS WHERE  SI_PARENTID="
				+ pid + " order by SI_NAME";

		IInfoStore iStore = (IInfoStore) request.getSession().getAttribute(
				"InfoStore");
		try {
			list = iStore.query(query);
		} catch (Exception sdke) {
			list = null;
		}

		StringBuffer temp = new StringBuffer();

		for (int j = 0; list != null && j < list.size(); j++) {
			IInfoObject collectSubfolder = (IInfoObject) list.get(j);
			String type = "";
			try {
				type = collectSubfolder.getKind();
			} catch (SDKException e) {

				e.printStackTrace();
			}

			if ("Folder".equals(type)) {
				temp.append(collectSubfolder.getID() + ","
						+ collectSubfolder.getTitle() + "," + "1|");
			} else {
				temp.append(collectSubfolder.getID() + ","
						+ collectSubfolder.getTitle() + "," + "0|");
			}
		}
		// System.out.println("the string value is:" + temp);

		return temp.toString();
	}
}
