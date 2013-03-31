package com.ailk.bi.domain.olap;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "UI_RPT_INFO_USER_OLAP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UiRptInfoUserOlap implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4722317196551153773L;

	private Integer customRptId;

	private String customRptName;

	private String userId;

	private String reportId;

	private String displayMode;

	private String isValid;

	@Id
	@Column(name = "CUSTOM_RPTID", nullable = false)
	@SequenceGenerator(name = "SEQ_USER_OLAP", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_OLAP")
	public Integer getCustomRptId() {
		return customRptId;
	}

	public void setCustomRptId(Integer customRptId) {
		this.customRptId = customRptId;
	}

	@Column(name = "CUSTOM_RPTNAME", length = 100, nullable = false)
	public String getCustomRptName() {
		return customRptName;
	}

	public void setCustomRptName(String customRptName) {
		this.customRptName = customRptName;
	}

	@Column(name = "USER_ID", nullable = false, length = 20)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "REPORT_ID", nullable = false, length = 50)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Column(name = "DISPLAY_MODE", nullable = false, length = 1)
	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	@Column(name = "IS_VALID", nullable = false, length = 1)
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
}
