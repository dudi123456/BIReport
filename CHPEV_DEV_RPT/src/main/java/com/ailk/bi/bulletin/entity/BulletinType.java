package com.ailk.bi.bulletin.entity;

import java.util.HashMap;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class BulletinType {
	private static int type_id = 1;

	private HashMap bulletinType = new HashMap();

	public HashMap getBulletinType() {
		return bulletinType;
	}

	public BulletinType() {
		bulletinType.clear();
		bulletinType.put("1", "公告");
		bulletinType.put("2", "新闻");
		bulletinType.put("3", "其它");
	}

	public static void main(String[] args) {
		BulletinType type = new BulletinType();
		System.out.println(type.getBulletinType().get("1"));

	}
}
