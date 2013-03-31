package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class SchemaInfoStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;
	private String schemaDesc;
	private String author;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSchemaDesc() {
		return schemaDesc;
	}

	public void setSchemaDesc(String schemaDesc) {
		this.schemaDesc = schemaDesc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
