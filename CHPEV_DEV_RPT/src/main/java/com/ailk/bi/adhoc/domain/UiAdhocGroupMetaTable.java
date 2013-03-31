package com.ailk.bi.adhoc.domain;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class UiAdhocGroupMetaTable extends JBTableBase implements Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String group_id = "";

	private String group_name = "";

	private String group_desc = "";

	private String group_belong = "";

	private String group_tag = "";

	private int sequence = 0;

	private String isshow = "";

	private String parent_group_id = "";

	private List Ltree;

	public List getLtree() {
		return Ltree;
	}

	public void setLtree(List ltree) {
		Ltree = ltree;
	}

	public String getParent_group_id() {
		return parent_group_id;
	}

	public void setParent_group_id(String parent_group_id) {
		this.parent_group_id = parent_group_id;
	}

	public UiAdhocGroupMetaTable() {
		super();
	}

	public String getGroup_belong() {
		return group_belong;
	}

	public void setGroup_belong(String group_belong) {
		this.group_belong = group_belong;
	}

	public String getGroup_desc() {
		return group_desc;
	}

	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getGroup_tag() {
		return group_tag;
	}

	public void setGroup_tag(String group_tag) {
		this.group_tag = group_tag;
	}

	public int compareTo(Object anotherPerson) throws ClassCastException {
		if (!(anotherPerson instanceof UiAdhocGroupMetaTable))
			throw new ClassCastException("非属性组元数据定义对象！");
		int metaSeq = ((UiAdhocGroupMetaTable) anotherPerson).getSequence();
		return this.getSequence() - metaSeq;
	}

}
