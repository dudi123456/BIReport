package com.ailk.bi.marketing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "UI_MAP_AREAINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AreaInfo implements Serializable  {

	private static final long serialVersionUID = 1L;
	private String area_id;
	private String area_name;
	private String parent_id;

	@Id
	@Column(name = "AREA_ID", length = 100, nullable = false)
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	@Column(name = "AREA_NAME", length = 100, nullable = false)
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	@Column(name = "PARENT_ID", length = 100, nullable = false)
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
}
