package com.ailk.bi.sigma.dwr;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ItemsBean {
	private String[] class1 = { "同学1", "同学2", "同学3", "同学4", "同学5", "同学6" };
	private String[] class2 = { "同学7", "同学8", "同学9", "同学10" };
	private String[] class3 = { "同学11", "同学12", "同学13", "同学14", "同学15", "同学16",
			"同学17" };
	private String[] class4 = { "同学18", "同学19", "同学20" };
	private String[] class5 = { "同学21", "同学22", "同学23", "同学24", "同学25", "同学26" };

	/** */
	/** Creates a new instance of ItemsBean */
	public ItemsBean() {
	}

	public Map ClassList() {
		System.out.println("tedt");
		Map reply = new LinkedHashMap();
		reply.put("0", "所有");
		reply.put("1", "班级1");
		reply.put("2", "班级2");
		reply.put("3", "班级3");
		reply.put("4", "班级4");
		reply.put("5", "班级5");
		return reply;
	}

	public Map UserList(String CLASSID) {
		Map reply = new LinkedHashMap();
		reply.put("0", "所有");

		// 这里用数组模拟数据库查询结果。
		// 真实环境中，你只要将数据库查询结果放入到reply里面就可以了。
		// reply的id就是返回后下拉框的option的value，reply的value就是返回后下拉框的option的text。
		// 如: sql = "select * from users where classid=?";
		if (CLASSID == null || CLASSID.equals("") || CLASSID.equals("0")) {
			//
		} else if (CLASSID.equals("1")) {
			int id = 1;
			for (int i = 0; i < class1.length; i++) {
				reply.put("" + id, class1[i]);
				id++;
			}
		} else if (CLASSID.equals("2")) {
			int id = 1;
			for (int i = 0; i < class2.length; i++) {
				reply.put("" + id, class2[i]);
				id++;
			}
		} else if (CLASSID.equals("3")) {
			int id = 1;
			for (int i = 0; i < class3.length; i++) {
				reply.put("" + id, class3[i]);
				id++;
			}
		} else if (CLASSID.equals("4")) {
			int id = 1;
			for (int i = 0; i < class4.length; i++) {
				reply.put("" + id, class4[i]);
				id++;
			}
		} else if (CLASSID.equals("5")) {
			int id = 1;
			for (int i = 0; i < class5.length; i++) {
				reply.put("" + id, class5[i]);
				id++;
			}
		}

		return reply;
	}

}
